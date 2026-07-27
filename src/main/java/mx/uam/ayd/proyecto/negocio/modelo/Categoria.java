package mx.uam.ayd.proyecto.negocio.modelo;

/**
 * Enumeración que representa las diferentes categorías de productos en el catálogo.
 */
public enum Categoria {
	HERRAMIENTAS_MANUALES("Herramientas Manuales"),
	MEDICION_Y_PRUEBA("Medición y Prueba"),
	SIN_CATEGORIA("Sin categoría");

	private final String nombre;

	Categoria(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Devuelve el nombre legible de la categoría.
	 *
	 * @return Cadena con el nombre de la categoría
	 */
	public String getNombre() {
		return nombre;
	}

	@Override
	public String toString() {
		return nombre;
	}
}
