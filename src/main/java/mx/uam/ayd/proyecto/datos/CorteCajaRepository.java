package mx.uam.ayd.proyecto.datos;

import mx.uam.ayd.proyecto.modelo.CorteCaja;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorteCajaRepository extends CrudRepository<CorteCaja, Long> {
}
