package mx.uam.ayd.proyecto.datos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import mx.uam.ayd.proyecto.negocio.CriterioBusquedaProducto;
import mx.uam.ayd.proyecto.negocio.modelo.Producto;

/**
 * Repositorio de productos utilizado por HU-01.
 */
public interface RepositorioProductos extends CrudRepository<Producto, Integer> {

	/**
	 * Obtiene los productos que corresponden al criterio seleccionado.
	 *
	 * @param criterio criterio de búsqueda
	 * @param valor valor normalizado que se desea localizar
	 * @return productos que cumplen con el criterio
	 */
	default List<Producto> obtenerPorCriterio(CriterioBusquedaProducto criterio, String valor) {
		return switch (criterio) {
			case NOMBRE -> findByNombreContainingIgnoreCase(valor);
			case SKU -> findBySkuIgnoreCase(valor);
			case CODIGO_BARRAS -> findByCodigoBarras(valor);
		};
	}

	List<Producto> findByNombreContainingIgnoreCase(String nombre);

	List<Producto> findBySkuIgnoreCase(String sku);

	List<Producto> findByCodigoBarras(String codigoBarras);
}
