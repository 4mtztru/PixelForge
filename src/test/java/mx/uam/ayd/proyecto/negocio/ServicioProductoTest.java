package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.ProveedorRepository;
import mx.uam.ayd.proyecto.datos.RepositorioProductos;
import mx.uam.ayd.proyecto.negocio.modelo.EstadoStock;
import mx.uam.ayd.proyecto.negocio.modelo.Categoria;
import mx.uam.ayd.proyecto.negocio.modelo.Producto;
import mx.uam.ayd.proyecto.negocio.modelo.Proveedor;

/**
 * Clase de prueba unitaria para validar la lógica de negocio
 * asociada al registro de productos de la HU-04.
 */
@ExtendWith(MockitoExtension.class)
public class ServicioProductoTest {

    @Mock
    private RepositorioProductos repositorioProductos;

    @Mock
    private ProveedorRepository proveedorRepository;

    @InjectMocks
    private ServicioProductos servicioProductos;

    @Test
    public void testRegistrarProductoExitoso() {
        Producto productoNuevo = new Producto();
        productoNuevo.setNombre("Martillo de Garra Curva");
        productoNuevo.setCategoria(Categoria.HERRAMIENTAS_MANUALES);
        productoNuevo.setProveedor(new Proveedor());
        productoNuevo.setPrecioCompra(100.00);
        productoNuevo.setPrecio(150.00);
        productoNuevo.setStockActual(12);
        productoNuevo.setStockMinimo(5);

        when(repositorioProductos.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Producto resultado = servicioProductos.registrarProducto(productoNuevo);

        assertNotNull(resultado, "El producto debería registrarse exitosamente.");
        assertNotNull(resultado.getSku(), "El sistema debe generar un código único automáticamente.");
        assertEquals(EstadoStock.solvente, resultado.getEstadoStock());
    }

    @Test
    public void testRegistrarProductoPrecioVentaNegativo() {
        Producto productoInvalido = new Producto();
        productoInvalido.setNombre("Tubo PVC");
        productoInvalido.setCategoria(Categoria.SIN_CATEGORIA);
        productoInvalido.setProveedor(new Proveedor());
        productoInvalido.setPrecioCompra(5.00);
        productoInvalido.setPrecio(-10.00);

        assertThrows(IllegalArgumentException.class, () -> {
            servicioProductos.registrarProducto(productoInvalido);
        }, "Debería lanzar una excepción al ingresar un precio negativo.");
    }

    @Test
    public void testRegistrarProductoRequiereProveedor() {
        Producto producto = new Producto();
        producto.setNombre("Taladro");
        producto.setCategoria(Categoria.HERRAMIENTAS_MANUALES);
        producto.setPrecioCompra(800);
        producto.setPrecio(1200);

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> servicioProductos.registrarProducto(producto));

        assertEquals("El proveedor del producto es obligatorio.", error.getMessage());
    }
}
