package mx.uam.ayd.proyecto.negocio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.RepositorioProductos;
import mx.uam.ayd.proyecto.negocio.modelo.EstadoStock;
import mx.uam.ayd.proyecto.negocio.modelo.Producto;

/**
 * Servicio relacionado con la consulta de productos del inventario.
 */
@Service
public class ServicioProductos {

	private final RepositorioProductos repositorioProductos;

	@Autowired
	public ServicioProductos(RepositorioProductos repositorioProductos) {
		this.repositorioProductos = repositorioProductos;
	}

	/**
	 * Busca productos usando exactamente uno de los criterios disponibles.
	 *
	 * @param criterio campo por el cual se realizará la búsqueda
	 * @param valor valor que se desea buscar
	 * @return todas las coincidencias; la lista estará vacía si no hay resultados
	 * @throws IllegalArgumentException si el criterio es nulo o el valor es nulo o vacío
	 */
	public List<Producto> buscarProducto(CriterioBusquedaProducto criterio, String valor) {
		if (criterio == null) {
			throw new IllegalArgumentException("El criterio de búsqueda no puede ser nulo");
		}

		if (valor == null || valor.isBlank()) {
			throw new IllegalArgumentException("El valor de búsqueda no puede ser nulo o vacío");
		}

		String valorNormalizado = valor.trim();
		List<Producto> productos = repositorioProductos.obtenerPorCriterio(criterio, valorNormalizado);

		for (Producto producto : productos) {
			producto.setEstadoStock(
					calcularEstadoStock(producto.getStockActual(), producto.getStockMinimo()));
		}

		return productos;
	}

	/**
	 * Determina si las existencias de un producto son críticas o solventes.
	 * El inventario se considera crítico cuando alcanza o baja del mínimo.
	 *
	 * @param stockActual existencias actuales
	 * @param stockMinimo límite mínimo de existencias
	 * @return estado calculado del inventario
	 */
	public EstadoStock calcularEstadoStock(int stockActual, int stockMinimo) {
		return stockActual <= stockMinimo ? EstadoStock.critico : EstadoStock.solvente;
	}
}
