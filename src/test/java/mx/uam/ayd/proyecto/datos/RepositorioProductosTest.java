package mx.uam.ayd.proyecto.datos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import mx.uam.ayd.proyecto.BaseIntegrationTest;
import mx.uam.ayd.proyecto.negocio.modelo.Producto;

/**
 * Pruebas de integración para la capa de persistencia (RepositorioProductos).
 * Utiliza una base de datos en memoria o de prueba para verificar las consultas JPA por criterio.
 */
@Transactional
class RepositorioProductosTest extends BaseIntegrationTest {

    @Autowired
    private RepositorioProductos repositorio;

    /**
     * Precarga productos de prueba en la base de datos antes de cada test.
     */
    @BeforeEach
    void prepararProductos() {
        Producto martillo = new Producto();
        martillo.setNombre("Martillo de uña");
        martillo.setSku("MAR-001");
        martillo.setCodigoBarras("123456789");
        repositorio.save(martillo);

        Producto martilloGoma = new Producto();
        martilloGoma.setNombre("Martillo de goma");
        martilloGoma.setSku("MAR-002");
        martilloGoma.setCodigoBarras("987654321");
        repositorio.save(martilloGoma);
    }

    /**
     * Prueba la búsqueda por coincidencia parcial e insensible a mayúsculas/minúsculas en el nombre.
     */
    @Test
    void debeObtenerTodasLasCoincidenciasDeNombre() {
        List<Producto> productos = repositorio.obtenerPorCriterio("nombre", "MARTILLO");

        assertEquals(2, productos.size());
    }

    /**
     * Prueba la búsqueda exacta por clave SKU.
     */
    @Test
    void debeObtenerPorSkuExacto() {
        List<Producto> productos = repositorio.obtenerPorCriterio("sku", "MAR-002");

        assertEquals(1, productos.size());
        assertEquals("Martillo de goma", productos.get(0).getNombre());
    }

    /**
     * Prueba la búsqueda exacta por código de barras.
     */
    @Test
    void debeObtenerPorCodigoDeBarrasExacto() {
        List<Producto> productos =
                repositorio.obtenerPorCriterio("codigoBarras", "123456789");

        assertEquals(1, productos.size());
        assertEquals("MAR-001", productos.get(0).getSku());
    }
}
