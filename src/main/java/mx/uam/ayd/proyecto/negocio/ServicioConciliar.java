package mx.uam.ayd.proyecto.negocio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.uam.ayd.proyecto.datos.RepositorioConciliacion;
import mx.uam.ayd.proyecto.datos.RepositorioOrdenCompra;
import mx.uam.ayd.proyecto.datos.RepositorioProductos;
import mx.uam.ayd.proyecto.negocio.modelo.Conciliacion;
import mx.uam.ayd.proyecto.negocio.modelo.DetalleOrdenCompra;
import mx.uam.ayd.proyecto.negocio.modelo.EstadoConciliacion;
import mx.uam.ayd.proyecto.negocio.modelo.EstadoOrden;
import mx.uam.ayd.proyecto.negocio.modelo.EstadoPartida;
import mx.uam.ayd.proyecto.negocio.modelo.OrdenCompra;
import mx.uam.ayd.proyecto.negocio.modelo.Producto;
import mx.uam.ayd.proyecto.negocio.modelo.ProductoConciliado;

@Service
public class ServicioConciliar {

    private final RepositorioOrdenCompra repositorioOrdenCompra;
    private final RepositorioProductos repositorioProductos;
    private final RepositorioConciliacion repositorioConciliacion;

    @Autowired
    public ServicioConciliar(RepositorioOrdenCompra repositorioOrdenCompra,
            RepositorioProductos repositorioProductos,
            RepositorioConciliacion repositorioConciliacion) {
        this.repositorioOrdenCompra = repositorioOrdenCompra;
        this.repositorioProductos = repositorioProductos;
        this.repositorioConciliacion = repositorioConciliacion;
    }

    /**
     * Recupera las órdenes de compra filtradas por el estado especificado (por
     * ejemplo, entregada o pendiente).
     * 
     * @param estado El estado de la orden deseado (EstadoOrden.entregada,
     *               EstadoOrden.pendiente)
     * @return Lista de órdenes de compra en el estado especificado
     */
    public List<OrdenCompra> obtenerOrdenes(EstadoOrden estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado de la orden no puede ser nulo");
        }
        return repositorioOrdenCompra.findByEstado(estado);
    }

    /**
     * Procesa y guarda la conciliación de una orden de compra,
     * actualizando los stocks de productos en inventario.
     *
     * @param orden                La orden de compra a conciliar
     * @param productosConciliados Lista de partidas concilidadas
     * @return La conciliación creada y guardada
     */
    @Transactional
    public Conciliacion conciliarOrden(OrdenCompra orden, List<ProductoConciliado> productosConciliados) {
        if (orden == null) {
            throw new IllegalArgumentException("La orden de compra no puede ser nula");
        }
        if (orden.getEstado() != EstadoOrden.entregada) {
            throw new IllegalArgumentException("Sólo se pueden conciliar órdenes entregadas");
        }
        if (productosConciliados == null || productosConciliados.isEmpty()) {
            throw new IllegalArgumentException("Debe incluir al menos una partida a conciliar");
        }
        if (orden.getDetalles() == null || orden.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("La orden no contiene partidas para conciliar");
        }
        if (productosConciliados.size() != orden.getDetalles().size()) {
            throw new IllegalArgumentException("Deben conciliarse todas las partidas de la orden");
        }

        Conciliacion conciliacion = orden.getConciliacion();
        if (conciliacion == null) {
            conciliacion = new Conciliacion();
            conciliacion.setOrdenCompra(orden);
        }

        conciliacion.setFecha(LocalDateTime.now());
        conciliacion.setEstado(EstadoConciliacion.completada);

        int totalEsp = 0;
        int totalRec = 0;
        double montoAjuste = 0;
        List<DetalleOrdenCompra> detallesPendientes = new ArrayList<>(orden.getDetalles());

        if (conciliacion.getProductosConciliados() != null) {
            conciliacion.getProductosConciliados().clear();
        }

        for (ProductoConciliado pc : productosConciliados) {
            if (pc == null || pc.getProducto() == null) {
                throw new IllegalArgumentException("Cada partida debe incluir un producto");
            }
            if (pc.getCantidadEsperada() < 0 || pc.getCantidadRecibida() < 0) {
                throw new IllegalArgumentException("Las cantidades no pueden ser negativas");
            }
            DetalleOrdenCompra detalle = retirarDetalleCorrespondiente(
                    detallesPendientes, pc.getProducto());
            pc.setCantidadEsperada(detalle.getCantidadEsperada());
            int diferencia = pc.getCantidadRecibida() - detalle.getCantidadEsperada();
            pc.setDiferencia(diferencia);
            pc.setEstado(diferencia == 0 ? EstadoPartida.entregada : EstadoPartida.pendiente);
            pc.setImporteAjuste(diferencia * detalle.getPrecioUnitario());
            pc.setConciliacion(conciliacion);
            if (conciliacion.getProductosConciliados() != null) {
                conciliacion.getProductosConciliados().add(pc);
            }
            totalEsp += pc.getCantidadEsperada();
            totalRec += pc.getCantidadRecibida();
            montoAjuste += pc.getImporteAjuste();

            // Actualizar stock del producto
            Producto prod = pc.getProducto();
            if (prod != null) {
                prod.setStockActual(prod.getStockActual() + pc.getCantidadRecibida());
                repositorioProductos.save(prod);
            }
        }

        conciliacion.setTotalEsperado(totalEsp);
        conciliacion.setTotalRecibido(totalRec);
        conciliacion.setMontoAjuste(montoAjuste);
        conciliacion.setMontoFinalPagar(
                Math.max(0, orden.getMontoTotal() + montoAjuste - orden.getAnticipoPagado()));

        orden.setEstado(EstadoOrden.conciliada);
        orden.setConciliacion(conciliacion);
        Conciliacion conciliacionGuardada = repositorioConciliacion.save(conciliacion);
        repositorioOrdenCompra.save(orden);

        return conciliacionGuardada;
    }

    /**
     * Localiza la partida original de la orden y la retira para impedir productos
     * ajenos o duplicados en una misma conciliación.
     */
    private DetalleOrdenCompra retirarDetalleCorrespondiente(
            List<DetalleOrdenCompra> detallesPendientes, Producto producto) {
        for (int indice = 0; indice < detallesPendientes.size(); indice++) {
            DetalleOrdenCompra detalle = detallesPendientes.get(indice);
            Producto productoOrden = detalle.getProducto();
            boolean mismaInstancia = productoOrden == producto;
            boolean mismoIdPersistido = productoOrden != null
                    && productoOrden.getIdProducto() > 0
                    && productoOrden.getIdProducto() == producto.getIdProducto();
            if (mismaInstancia || mismoIdPersistido) {
                detallesPendientes.remove(indice);
                return detalle;
            }
        }
        throw new IllegalArgumentException(
                "La partida contiene un producto que no pertenece a la orden");
    }
}
