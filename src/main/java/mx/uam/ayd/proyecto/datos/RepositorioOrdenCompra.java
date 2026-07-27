package mx.uam.ayd.proyecto.datos;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import mx.uam.ayd.proyecto.negocio.modelo.EstadoOrden;
import mx.uam.ayd.proyecto.negocio.modelo.OrdenCompra;

/**
 * Repositorio para Ordenes de Compra
 */
public interface RepositorioOrdenCompra extends CrudRepository<OrdenCompra, Long> {

	@Query("SELECT DISTINCT o FROM OrdenCompra o LEFT JOIN FETCH o.detalles LEFT JOIN FETCH o.proveedor WHERE o.estado = :estado")
	public List<OrdenCompra> findByEstado(@Param("estado") EstadoOrden estado);

}
