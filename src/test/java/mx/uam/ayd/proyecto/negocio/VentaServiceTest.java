package mx.uam.ayd.proyecto.negocio;

import mx.uam.ayd.proyecto.negocio.modelo.Venta;
import mx.uam.ayd.proyecto.negocio.modelo.Producto;
import mx.uam.ayd.proyecto.datos.RepositorioProductos;
import mx.uam.ayd.proyecto.datos.VentaRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;

public class VentaServiceTest {

    private final VentaService ventaService = new VentaService();

    @Test
    public void testValidarCarritoVacio() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ventaService.validarCarrito(Collections.emptyList());
        });
        assertEquals("El carrito está vacío", exception.getMessage());
    }

    @Test
    public void testPagoInsuficienteEfectivo() {
        Venta venta = new Venta();
        venta.setProductosEnCarrito(true);
        venta.setMetodoPago("Efectivo");
        venta.setTotal(1560.20);
        venta.setMontoRecibido(1000.00);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ventaService.procesarPago(venta);
        });

        assertEquals("El monto recibido es insuficiente.", exception.getMessage());
    }

    @Test
    public void testPagoTarjetaSinAutorizacion() {
        Venta venta = new Venta();
        venta.setProductosEnCarrito(true);
        venta.setMetodoPago("Tarjeta");
        venta.setTotal(500.00);
        venta.setReferenciaTransferencia("");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ventaService.procesarPago(venta);
        });

        assertEquals("Tarjeta rechazada / Código de autorización obligatorio.", exception.getMessage());
    }

    @Test
    public void testPagoTransferenciaSinReferencia() {
        Venta venta = new Venta();
        venta.setProductosEnCarrito(true);
        venta.setMetodoPago("Transferencia");
        venta.setTotal(800.00);
        venta.setReferenciaTransferencia("");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ventaService.procesarPago(venta);
        });

        assertEquals("La referencia bancaria es obligatoria.", exception.getMessage());
    }

    @Test
    public void testPagoEfectivoExitoso() {
        Venta venta = new Venta();
        venta.setProductosEnCarrito(true);
        venta.setMetodoPago("Efectivo");
        venta.setTotal(100.00);
        venta.setMontoRecibido(150.00);

        assertTrue(ventaService.procesarPago(venta));
    }

    @Test
    public void testRechazaMetodoDesconocido() {
        Venta venta = new Venta();
        venta.setProductosEnCarrito(true);
        venta.setMetodoPago("CHEQUE");

        assertThrows(IllegalArgumentException.class, () -> ventaService.procesarPago(venta));
    }

    @Test
    public void testRegistrarVentaActualizaInventario() {
        VentaRepository ventaRepository = mock(VentaRepository.class);
        RepositorioProductos productosRepository = mock(RepositorioProductos.class);
        ServicioProductos servicioProductos = mock(ServicioProductos.class);
        VentaService servicio = new VentaService(
                ventaRepository, productosRepository, servicioProductos);
        Venta venta = new Venta();
        Producto producto = new Producto();
        producto.setStockActual(3);
        producto.setStockMinimo(1);
        when(ventaRepository.save(venta)).thenReturn(venta);

        servicio.registrarVenta(venta, Arrays.asList(producto, producto));

        assertEquals(1, producto.getStockActual());
        verify(ventaRepository).save(venta);
        verify(productosRepository).save(producto);
    }

    @Test
    public void testRegistrarVentaRechazaCantidadMayorAlStock() {
        VentaService servicio = new VentaService(
                mock(VentaRepository.class),
                mock(RepositorioProductos.class),
                mock(ServicioProductos.class));
        Producto producto = new Producto();
        producto.setStockActual(1);

        assertThrows(IllegalArgumentException.class,
                () -> servicio.registrarVenta(
                        new Venta(), Arrays.asList(producto, producto)));
    }
}
