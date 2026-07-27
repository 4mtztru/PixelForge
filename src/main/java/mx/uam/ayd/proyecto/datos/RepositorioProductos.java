package mx.uam.ayd.proyecto.datos;

import java.util.List;
import java.util.Locale;

import org.springframework.data.repository.CrudRepository;

import mx.uam.ayd.proyecto.negocio.modelo.Producto;

/**
 * Repositorio para Productos
 */
public interface RepositorioProductos extends CrudRepository<Producto, Long> {

	List<Producto> findByNombreContainingIgnoreCase(String nombre);

	List<Producto> findBySku(String sku);

	List<Producto> findBySkuIgnoreCase(String sku);

	List<Producto> findByCodigoBarras(String codigoBarras);

	default List<Producto> obtenerPorCriterio(String criterio, String valor) {
		return switch (criterio.toLowerCase(Locale.ROOT)) {
			case "nombre" -> findByNombreContainingIgnoreCase(valor);
			case "sku" -> findBySku(valor);
			case "codigobarras" -> findByCodigoBarras(valor);
			default -> throw new IllegalArgumentException("El criterio de búsqueda no es válido.");
		};
	}
}
