package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.RepositorioOrdenCompra;
import mx.uam.ayd.proyecto.datos.RepositorioProductos;
import mx.uam.ayd.proyecto.datos.RepositorioConciliacion;
import mx.uam.ayd.proyecto.negocio.modelo.Conciliacion;
import mx.uam.ayd.proyecto.negocio.modelo.DetalleOrdenCompra;
import mx.uam.ayd.proyecto.negocio.modelo.EstadoOrden;
import mx.uam.ayd.proyecto.negocio.modelo.OrdenCompra;
import mx.uam.ayd.proyecto.negocio.modelo.Producto;
import mx.uam.ayd.proyecto.negocio.modelo.ProductoConciliado;

/**
 * Pruebas unitarias para el servicio de conciliación (ServicioConciliar).
 * Evalúa las reglas de negocio para la recuperación de órdenes por estado y la validación de parámetros.
 */
@ExtendWith(MockitoExtension.class)
class ServicioConciliarTest {

    @Mock
    private RepositorioOrdenCompra repositorioOrdenCompra;

    @InjectMocks
    private ServicioConciliar servicioConciliar;

    /**
     * Prueba la recuperación de órdenes en estado 'entregada'.
     */
    @Test
    void debeRecuperarOrdenesEntregadas() {
        OrdenCompra orden = new OrdenCompra();
        orden.setEstado(EstadoOrden.entregada);
        List<OrdenCompra> esperadas = Arrays.asList(orden);

        when(repositorioOrdenCompra.findByEstado(EstadoOrden.entregada)).thenReturn(esperadas);

        List<OrdenCompra> resultado = servicioConciliar.obtenerOrdenes(EstadoOrden.entregada);

        assertEquals(esperadas, resultado);
        verify(repositorioOrdenCompra).findByEstado(EstadoOrden.entregada);
    }

    /**
     * Prueba la recuperación de órdenes en estado 'pendiente'.
     */
    @Test
    void debeRecuperarOrdenesPendientes() {
        OrdenCompra orden = new OrdenCompra();
        orden.setEstado(EstadoOrden.pendiente);
        List<OrdenCompra> esperadas = Arrays.asList(orden);

        when(repositorioOrdenCompra.findByEstado(EstadoOrden.pendiente)).thenReturn(esperadas);

        List<OrdenCompra> resultado = servicioConciliar.obtenerOrdenes(EstadoOrden.pendiente);

        assertEquals(esperadas, resultado);
        verify(repositorioOrdenCompra).findByEstado(EstadoOrden.pendiente);
    }

    /**
     * Prueba que se lance IllegalArgumentException si el parámetro de estado es nulo.
     */
    @Test
    void debeLanzarExcepcionSiEstadoEsNulo() {
        assertThrows(IllegalArgumentException.class, () -> servicioConciliar.obtenerOrdenes((EstadoOrden) null));
    }

    @Test
    void debeConciliarOrdenYActualizarStock() {
        RepositorioOrdenCompra ordenes = mock(RepositorioOrdenCompra.class);
        RepositorioProductos productos = mock(RepositorioProductos.class);
        RepositorioConciliacion conciliaciones = mock(RepositorioConciliacion.class);
        ServicioConciliar servicio = new ServicioConciliar(ordenes, productos, conciliaciones);
        OrdenCompra orden = new OrdenCompra();
        orden.setEstado(EstadoOrden.entregada);
        orden.setMontoTotal(500);
        orden.setAnticipoPagado(100);
        Producto producto = new Producto();
        producto.setPrecio(50);
        producto.setStockActual(4);
        DetalleOrdenCompra detalle = new DetalleOrdenCompra();
        detalle.setProducto(producto);
        detalle.setCantidadEsperada(5);
        detalle.setPrecioUnitario(50);
        orden.getDetalles().add(detalle);
        ProductoConciliado partida = new ProductoConciliado();
        partida.setProducto(producto);
        partida.setCantidadEsperada(5);
        partida.setCantidadRecibida(4);
        when(conciliaciones.save(org.mockito.ArgumentMatchers.any(Conciliacion.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        Conciliacion resultado = servicio.conciliarOrden(orden, List.of(partida));

        assertEquals(8, producto.getStockActual());
        assertEquals(-1, partida.getDiferencia());
        assertEquals(-50, resultado.getMontoAjuste());
        assertEquals(350, resultado.getMontoFinalPagar());
        assertEquals(EstadoOrden.conciliada, orden.getEstado());
        verify(productos).save(producto);
        verify(ordenes).save(orden);
    }

    @Test
    void debeRechazarOrdenQueNoEsteEntregada() {
        ServicioConciliar servicio = new ServicioConciliar(
                mock(RepositorioOrdenCompra.class),
                mock(RepositorioProductos.class),
                mock(RepositorioConciliacion.class));
        OrdenCompra orden = new OrdenCompra();
        orden.setEstado(EstadoOrden.pendiente);

        assertThrows(IllegalArgumentException.class,
                () -> servicio.conciliarOrden(
                        orden, List.of(new ProductoConciliado())));
    }

    @Test
    void debeUsarCantidadYPrecioDeLaOrdenComoFuenteAutoritativa() {
        RepositorioConciliacion conciliaciones = mock(RepositorioConciliacion.class);
        ServicioConciliar servicio = new ServicioConciliar(
                mock(RepositorioOrdenCompra.class),
                mock(RepositorioProductos.class),
                conciliaciones);
        Producto producto = new Producto();
        producto.setStockActual(2);
        producto.setPrecio(999);
        DetalleOrdenCompra detalle = new DetalleOrdenCompra();
        detalle.setProducto(producto);
        detalle.setCantidadEsperada(10);
        detalle.setPrecioUnitario(25);
        OrdenCompra orden = new OrdenCompra();
        orden.setEstado(EstadoOrden.entregada);
        orden.setMontoTotal(250);
        orden.getDetalles().add(detalle);
        ProductoConciliado partida = new ProductoConciliado();
        partida.setProducto(producto);
        partida.setCantidadEsperada(1);
        partida.setCantidadRecibida(8);
        when(conciliaciones.save(org.mockito.ArgumentMatchers.any(Conciliacion.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        Conciliacion resultado = servicio.conciliarOrden(orden, List.of(partida));

        assertEquals(10, partida.getCantidadEsperada());
        assertEquals(-2, partida.getDiferencia());
        assertEquals(-50, resultado.getMontoAjuste());
    }

    @Test
    void debeRechazarProductoAjenoALaOrden() {
        ServicioConciliar servicio = new ServicioConciliar(
                mock(RepositorioOrdenCompra.class),
                mock(RepositorioProductos.class),
                mock(RepositorioConciliacion.class));
        OrdenCompra orden = new OrdenCompra();
        orden.setEstado(EstadoOrden.entregada);
        DetalleOrdenCompra detalle = new DetalleOrdenCompra();
        detalle.setProducto(new Producto());
        detalle.setCantidadEsperada(1);
        orden.getDetalles().add(detalle);
        ProductoConciliado partidaAjena = new ProductoConciliado();
        partidaAjena.setProducto(new Producto());
        partidaAjena.setCantidadRecibida(1);

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.conciliarOrden(orden, List.of(partidaAjena)));

        assertEquals("La partida contiene un producto que no pertenece a la orden",
                error.getMessage());
    }
}
