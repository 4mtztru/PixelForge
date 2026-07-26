package mx.uam.ayd.proyecto.datos;

import mx.uam.ayd.proyecto.modelo.Venta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends CrudRepository<Venta, Long> {
}
