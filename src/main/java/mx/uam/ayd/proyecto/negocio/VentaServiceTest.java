package mx.uam.ayd.proyecto.negocio;

import mx.uam.ayd.proyecto.modelo.Venta;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VentaServiceTest {

    private final VentaService ventaService = new VentaService();

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
}
