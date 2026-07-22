package mx.uam.ayd.proyecto.presentacion.productos;

import java.util.ArrayList;
import java.util.List;

import mx.uam.ayd.proyecto.negocio.CriterioBusquedaProducto;

/**
 * Solicitud validada que la vista entrega al control de productos.
 */
public record SolicitudBusquedaProducto(CriterioBusquedaProducto criterio, String valor) {

	public SolicitudBusquedaProducto {
		if (criterio == null) {
			throw new IllegalArgumentException("El criterio de búsqueda es obligatorio.");
		}
		if (valor == null || valor.isBlank()) {
			throw new IllegalArgumentException("No se ingresó dato alguno.");
		}
		valor = valor.trim();
	}

	public static SolicitudBusquedaProducto desdeCampos(String nombre, String sku, String codigoBarras) {
		List<SolicitudBusquedaProducto> solicitudes = new ArrayList<>(3);
		agregaSiTieneValor(solicitudes, CriterioBusquedaProducto.NOMBRE, nombre);
		agregaSiTieneValor(solicitudes, CriterioBusquedaProducto.SKU, sku);
		agregaSiTieneValor(solicitudes, CriterioBusquedaProducto.CODIGO_BARRAS, codigoBarras);

		if (solicitudes.isEmpty()) {
			throw new IllegalArgumentException("No se ingresó dato alguno.");
		}
		if (solicitudes.size() > 1) {
			throw new IllegalArgumentException("Solo es posible realizar búsquedas con un campo de los tres.");
		}

		return solicitudes.get(0);
	}

	private static void agregaSiTieneValor(List<SolicitudBusquedaProducto> solicitudes,
			CriterioBusquedaProducto criterio, String valor) {
		if (valor != null && !valor.isBlank()) {
			solicitudes.add(new SolicitudBusquedaProducto(criterio, valor));
		}
	}
}
