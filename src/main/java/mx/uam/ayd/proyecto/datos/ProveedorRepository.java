package mx.uam.ayd.proyecto.datos;

import org.springframework.data.repository.CrudRepository;

import mx.uam.ayd.proyecto.negocio.modelo.Proveedor;

/**
 * Repositorio para Proveedores
 */
public interface ProveedorRepository extends CrudRepository <Proveedor, Long> {

	public Proveedor findByRfc(String rfc);

}
