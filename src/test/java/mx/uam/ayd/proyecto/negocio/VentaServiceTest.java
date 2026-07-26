package mx.uam.ayd.proyecto.negocio;

import mx.uam.ayd.proyecto.negocio.modelo.Venta;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
}
