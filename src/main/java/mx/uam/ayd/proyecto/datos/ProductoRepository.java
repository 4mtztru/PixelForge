package mx.uam.ayd.proyecto.datos;

import org.springframework.data.repository.CrudRepository;

import mx.uam.ayd.proyecto.negocio.modelo.Producto;

/**
 * Repositorio para Productos
 */
public interface ProductoRepository extends CrudRepository <Producto, Long> {

	public Producto findByCodigoBarras(String codigoBarras);

}
