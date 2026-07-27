package mx.uam.ayd.proyecto.negocio;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.RepositorioProductos;
import mx.uam.ayd.proyecto.negocio.modelo.EstadoStock;
import mx.uam.ayd.proyecto.negocio.modelo.Producto;

@Service
public class ServicioBuscarProducto {
    public static final String CRITERIO_NOMBRE = "nombre";
    public static final String CRITERIO_SKU = "sku";
    public static final String CRITERIO_CODIGO_BARRAS = "codigoBarras";

    private final RepositorioProductos repositorioProductos;

    @Autowired
    public ServicioBuscarProducto(RepositorioProductos repositorioProductos) {
        this.repositorioProductos = repositorioProductos;
    }

    /**
     * Calcula el EstadoStock según los niveles de stock actual y mínimo.
     */
    public EstadoStock calcularEstadoStock(int stockActual, int stockMinimo) {
        if (stockActual <= stockMinimo) {
            return EstadoStock.critico;
        } else {
            return EstadoStock.solvente;
        }
    }

    /**
     * Evalúa y asigna el EstadoStock a un producto según sus niveles de stock
     * actual y mínimo.
     */
    public EstadoStock evaluarEstadoStock(Producto producto) {
        if (producto == null) {
            return null;
        }
        EstadoStock estado = calcularEstadoStock(producto.getStockActual(), producto.getStockMinimo());
        producto.setEstadoStock(estado);
        return estado;
    }

    /**
     * Obtiene los productos desde el repositorio según el criterio y valor (según
     * el Diagrama de Secuencia).
     */
    public List<Producto> obtenerPorCriterio(String criterio, String valor) {
        if (criterio == null) {
            throw new IllegalArgumentException("El criterio de búsqueda es obligatorio.");
        }
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("No se ingresó dato alguno.");
        }

        String valorTrim = valor.trim();
        String criterioNormalizado = criterio.toLowerCase(Locale.ROOT);
        switch (criterioNormalizado) {
            case CRITERIO_NOMBRE:
            case CRITERIO_SKU:
            case "codigobarras":
                return repositorioProductos.obtenerPorCriterio(criterioNormalizado, valorTrim);
            default:
                throw new IllegalArgumentException("El criterio de búsqueda no es válido.");
        }
    }

    /**
     * Busca productos filtrados por un criterio (nombre, sku, codigoBarras) y valor
     * (buscarProducto en el Diagrama de Secuencia).
     */
    public List<Producto> buscarProducto(String criterio, String valor) {
        List<Producto> productosEncontrados = obtenerPorCriterio(criterio, valor);
        for (Producto producto : productosEncontrados) {
            evaluarEstadoStock(producto);
        }
        return productosEncontrados;
    }
}
