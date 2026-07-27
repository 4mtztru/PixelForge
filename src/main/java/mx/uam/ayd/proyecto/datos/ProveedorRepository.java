package mx.uam.ayd.proyecto.datos;

import org.springframework.data.repository.CrudRepository;

import mx.uam.ayd.proyecto.negocio.modelo.Proveedor;

/**
 * Repositorio de datos para la entidad Proveedor.
 * Permite realizar operaciones CRUD y buscar proveedores por su RFC.
 */
public interface ProveedorRepository extends CrudRepository<Proveedor, Long> {

	/**
	 * Busca un proveedor en la base de datos a partir de su RFC.
	 *
	 * @param rfc Registro Federal de Contribuyentes del proveedor
	 * @return Objeto Proveedor encontrado o null si no existe
	 */
	public Proveedor findByRfc(String rfc);

}
