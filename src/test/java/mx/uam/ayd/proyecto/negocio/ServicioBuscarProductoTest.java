package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.RepositorioProductos;
import mx.uam.ayd.proyecto.negocio.modelo.EstadoStock;
import mx.uam.ayd.proyecto.negocio.modelo.Producto;

/**
 * Pruebas unitarias para el servicio de búsqueda de productos (ServicioBuscarProducto).
 * Verifica el cálculo del estado de stock (crítico vs solvente) y las búsquedas por criterio.
 */
@ExtendWith(MockitoExtension.class)
class ServicioBuscarProductoTest {

    @Mock
    private RepositorioProductos repositorioProductos;

    @InjectMocks
    private ServicioBuscarProducto servicioBuscarProducto;

    /**
     * Prueba que evalúa el estado del stock según el nivel actual y el mínimo configurado.
     */
    @Test
    void testEvaluarEstadoStock() {
        Producto productoCritico = new Producto();
        productoCritico.setStockActual(2);
        productoCritico.setStockMinimo(5);

        EstadoStock estado1 = servicioBuscarProducto.evaluarEstadoStock(productoCritico);
        assertEquals(EstadoStock.critico, estado1);
        assertEquals(EstadoStock.critico, productoCritico.getEstadoStock());

        Producto productoSolvente = new Producto();
        productoSolvente.setStockActual(10);
        productoSolvente.setStockMinimo(5);

        EstadoStock estado2 = servicioBuscarProducto.evaluarEstadoStock(productoSolvente);
        assertEquals(EstadoStock.solvente, estado2);
        assertEquals(EstadoStock.solvente, productoSolvente.getEstadoStock());
    }

    /**
     * Prueba la búsqueda de productos según los criterios válidos (nombre, sku y codigoBarras).
     */
    @Test
    void testBuscarProductos() {
        Producto producto1 = new Producto();
        producto1.setNombre("Teclado Mecanico");
        producto1.setSku("TEC-001");
        producto1.setCodigoBarras("1234567890");
        producto1.setStockActual(10);
        producto1.setStockMinimo(2);

        when(repositorioProductos.obtenerPorCriterio("nombre", "Teclado"))
                .thenReturn(List.of(producto1));
        when(repositorioProductos.obtenerPorCriterio("sku", "TEC-001"))
                .thenReturn(List.of(producto1));
        when(repositorioProductos.obtenerPorCriterio("codigobarras", "1234567890"))
                .thenReturn(List.of(producto1));

        List<Producto> resNombre = servicioBuscarProducto.buscarProducto("nombre", "Teclado");
        assertEquals(1, resNombre.size());
        assertEquals("Teclado Mecanico", resNombre.get(0).getNombre());
        assertEquals(EstadoStock.solvente, resNombre.get(0).getEstadoStock());

        List<Producto> resSku = servicioBuscarProducto.buscarProducto("sku", "TEC-001");
        assertEquals(1, resSku.size());
        assertEquals("TEC-001", resSku.get(0).getSku());

        List<Producto> resCodigo =
                servicioBuscarProducto.buscarProducto("codigoBarras", "1234567890");
        assertEquals(1, resCodigo.size());
        assertEquals("1234567890", resCodigo.get(0).getCodigoBarras());
    }

    /**
     * Prueba que no se consulte al repositorio si el texto de búsqueda está en blanco.
     */
    @Test
    void noDebeConsultarTodoElInventarioCuandoElValorEstaVacio() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> servicioBuscarProducto.buscarProducto("nombre", "   "));

        assertEquals("No se ingresó dato alguno.", exception.getMessage());
        verifyNoInteractions(repositorioProductos);
    }

    /**
     * Prueba que un criterio de búsqueda no soportado sea rechazado lanzando una excepción.
     */
    @Test
    void debeRechazarUnCriterioDesconocido() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> servicioBuscarProducto.buscarProducto("precio", "100"));

        assertEquals("El criterio de búsqueda no es válido.", exception.getMessage());
        verifyNoInteractions(repositorioProductos);
    }

    /**
     * Prueba que si el stock actual es exactamente igual al stock mínimo, se considere estado crítico.
     */
    @Test
    void elStockIgualAlMinimoDebeSerCritico() {
        assertEquals(EstadoStock.critico, servicioBuscarProducto.calcularEstadoStock(5, 5));
    }
}
