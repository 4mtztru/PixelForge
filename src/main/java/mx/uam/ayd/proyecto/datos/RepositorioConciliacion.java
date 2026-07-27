package mx.uam.ayd.proyecto.datos;

import org.springframework.data.repository.CrudRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Conciliacion;

/**
 * Repositorio de datos para la entidad Conciliación.
 * Permite realizar operaciones de persistencia y consulta en la base de datos.
 */
public interface RepositorioConciliacion extends CrudRepository<Conciliacion, Long> {
}
