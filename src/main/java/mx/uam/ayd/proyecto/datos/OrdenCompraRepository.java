package mx.uam.ayd.proyecto.datos;

import org.springframework.data.repository.CrudRepository;

import mx.uam.ayd.proyecto.negocio.modelo.OrdenCompra;

/**
 * Repositorio para Ordenes de Compra
 */
public interface OrdenCompraRepository extends CrudRepository <OrdenCompra, Long> {

	public OrdenCompra findByFolio(String folio);

}
