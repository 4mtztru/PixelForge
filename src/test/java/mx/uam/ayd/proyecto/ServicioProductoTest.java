import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba unitaria para validar la lógica de negocio
 * asociada al registro de productos de la HU-04.
 */
public class ServicioProductoTest {

    private ServicioProducto servicioProducto;

    @BeforeEach
    public void setUp() {
        servicioProducto = new ServicioProductoImpl();
    }

    @Test
    public void testRegistrarProductoExitoso() {
        Producto productoNuevo = new Producto();
        productoNuevo.setNombre("Martillo de Garra Curva");
        productoNuevo.setCategoria("HERRAMIENTAS");
        productoNuevo.setPrecioCompra(150.00);
        productoNuevo.setPrecioVenta(245.00);
        productoNuevo.setExistencias(12);
        
        boolean resultado = servicioProducto.registrarProducto(productoNuevo);

        assertTrue(resultado, "El producto debería registrarse exitosamente.");
        assertNotNull(productoNuevo.getCodigo(), "El sistema debe generar un código único automáticamente.");
    }

    @Test
    public void testRegistrarProductoPrecioCompraNegativo() {
        Producto productoInvalido = new Producto();
        productoInvalido.setNombre("Tubo PVC");
        productoInvalido.setPrecioCompra(-10.00);
        productoInvalido.setPrecioVenta(89.50);

        assertThrows(IllegalArgumentException.class, () -> {
            servicioProducto.registrarProducto(productoInvalido);
        }, "Debería lanzar una excepción al ingresar un precio de compra negativo.");
    }
}
