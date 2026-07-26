package mx.uam.ayd.proyecto.negocio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.RepositorioProductos;
import mx.uam.ayd.proyecto.datos.VentaRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Producto;
import mx.uam.ayd.proyecto.negocio.modelo.Venta;

/**
 * Servicio de negocio para el procesamiento y registro de ventas (HU-03).
 */
@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final RepositorioProductos repositorioProductos;
    private final ServicioProductos servicioProductos;

    @Autowired
    public VentaService(VentaRepository ventaRepository,
                        RepositorioProductos repositorioProductos,
                        ServicioProductos servicioProductos) {
        this.ventaRepository = ventaRepository;
        this.repositorioProductos = repositorioProductos;
        this.servicioProductos = servicioProductos;
    }

    // Constructor sin argumentos para compatibilidad con pruebas unitarias simples
    public VentaService() {
        this.ventaRepository = null;
        this.repositorioProductos = null;
        this.servicioProductos = null;
    }

    /**
     * Valida que el carrito de compras no esté vacío.
     *
     * @param carrito lista de productos en el carrito
     * @throws IllegalArgumentException si el carrito es nulo o está vacío
     */
    public void validarCarrito(List<Producto> carrito) {
        if (carrito == null || carrito.isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío");
        }
    }

    /**
     * Valida y procesa las reglas del pago según el método seleccionado (EFECTIVO, TARJETA, TRANSFERENCIA).
     *
     * @param venta objeto con los datos del cobro
     * @return true si la validación del pago fue correcta
     * @throws IllegalArgumentException si los datos del pago no cumplen las reglas de negocio
     */
    public boolean procesarPago(Venta venta) {
        if (!venta.isProductosEnCarrito()) {
            throw new IllegalArgumentException("El carrito está vacío");
        }

        String metodo = venta.getMetodoPago() != null ? venta.getMetodoPago().toUpperCase() : "";

        if ("EFECTIVO".equals(metodo)) {
            if (venta.getMontoRecibido() < venta.getTotal()) {
                throw new IllegalArgumentException("El monto recibido es insuficiente.");
            }
        } else if ("TARJETA".equals(metodo)) {
            if (venta.getReferenciaTransferencia() == null || venta.getReferenciaTransferencia().trim().isEmpty()) {
                throw new IllegalArgumentException("Tarjeta rechazada / Código de autorización obligatorio.");
            }
        } else if ("TRANSFERENCIA".equals(metodo)) {
            if (venta.getReferenciaTransferencia() == null || venta.getReferenciaTransferencia().trim().isEmpty()) {
                throw new IllegalArgumentException("La referencia bancaria es obligatoria.");
            }
        }

        return true;
    }

    /**
     * Finaliza la venta guardándola en la base de datos y actualizando el inventario de productos.
     *
     * @param venta datos de la venta a registrar
     * @param productos productos vendidos a descontar del inventario
     * @return Venta guardada
     */
    public Venta registrarVenta(Venta venta, List<Producto> productos) {
        Venta ventaGuardada = (ventaRepository != null) ? ventaRepository.save(venta) : venta;

        if (productos != null && repositorioProductos != null) {
            for (Producto prod : productos) {
                int nuevoStock = Math.max(0, prod.getStockActual() - 1);
                prod.setStockActual(nuevoStock);
                if (servicioProductos != null) {
                    prod.setEstadoStock(servicioProductos.calcularEstadoStock(nuevoStock, prod.getStockMinimo()));
                }
                repositorioProductos.save(prod);
            }
        }

        return ventaGuardada;
    }
}
