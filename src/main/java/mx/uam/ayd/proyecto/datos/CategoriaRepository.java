package mx.uam.ayd.proyecto.datos;

import org.springframework.data.repository.CrudRepository;

import mx.uam.ayd.proyecto.negocio.modelo.Categoria;

/**
 * Repositorio para Categorias
 */
public interface CategoriaRepository extends CrudRepository <Categoria, Long> {

	public Categoria findByNombre(String nombre);

}
