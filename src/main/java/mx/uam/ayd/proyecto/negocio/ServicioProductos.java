package mx.uam.ayd.proyecto.negocio;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.ProveedorRepository;
import mx.uam.ayd.proyecto.datos.RepositorioProductos;
import mx.uam.ayd.proyecto.negocio.modelo.EstadoStock;
import mx.uam.ayd.proyecto.negocio.modelo.Producto;
import mx.uam.ayd.proyecto.negocio.modelo.Proveedor;

/**
 * Servicio relacionado con la consulta y registro de productos e inventario.
 */
@Service
public class ServicioProductos {

	private final RepositorioProductos repositorioProductos;
	private final ProveedorRepository proveedorRepository;

	@Autowired
	public ServicioProductos(RepositorioProductos repositorioProductos, ProveedorRepository proveedorRepository) {
		this.repositorioProductos = repositorioProductos;
		this.proveedorRepository = proveedorRepository;
	}

	/**
	 * Obtiene la lista de proveedores activos registrados en el sistema.
	 *
	 * @return lista de proveedores
	 */
	public List<Proveedor> obtenerProveedoresActivos() {
		List<Proveedor> lista = new ArrayList<>();
		proveedorRepository.findAll().forEach(lista::add);
		return lista;
	}

	/**
	 * Genera un código de producto único verificando que no exista en el repositorio.
	 *
	 * @param categoriaId identificador de la categoría
	 * @return código único generado
	 */
	public String generarCodigoUnico(int categoriaId) {
		String codigoGenerado;
		boolean existe;
		do {
			codigoGenerado = "PROD-C" + categoriaId + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
			existe = !repositorioProductos.findBySkuIgnoreCase(codigoGenerado).isEmpty() ||
			         !repositorioProductos.findByCodigoBarras(codigoGenerado).isEmpty();
		} while (existe);
		return codigoGenerado;
	}

	/**
	 * Registra y guarda un nuevo producto en la base de datos validando reglas de negocio.
	 *
	 * @param producto objeto producto a guardar
	 * @return producto guardado
	 * @throws IllegalArgumentException si los datos requeridos no son válidos
	 */
	public Producto registrarProducto(Producto producto) {
		if (producto.getNombre() == null || producto.getNombre().isBlank()) {
			throw new IllegalArgumentException("El nombre del producto es obligatorio.");
		}
		if (producto.getPrecio() < 0) {
			throw new IllegalArgumentException("El precio del producto no puede ser negativo.");
		}
		if (producto.getStockActual() < 0) {
			throw new IllegalArgumentException("El stock inicial no puede ser negativo.");
		}

		if (producto.getSku() == null || producto.getSku().isBlank()) {
			int catId = producto.getCategoria() != null ? producto.getCategoria().getIdCategoria() : 1;
			producto.setSku(generarCodigoUnico(catId));
		}
		if (producto.getCodigoBarras() == null || producto.getCodigoBarras().isBlank()) {
			producto.setCodigoBarras(producto.getSku());
		}

		producto.setEstadoStock(calcularEstadoStock(producto.getStockActual(), producto.getStockMinimo()));
		return repositorioProductos.save(producto);
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
	 * Obtiene todos los productos registrados.
	 * 
	 * @return lista completa de productos
	 */
	public List<Producto> obtenerTodosLosProductos() {
		List<Producto> lista = new ArrayList<>();
		repositorioProductos.findAll().forEach(p -> {
			p.setEstadoStock(calcularEstadoStock(p.getStockActual(), p.getStockMinimo()));
			lista.add(p);
		});
		return lista;
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
