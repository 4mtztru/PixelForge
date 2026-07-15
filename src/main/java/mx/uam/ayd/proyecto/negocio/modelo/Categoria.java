package mx.uam.ayd.proyecto.negocio.modelo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Categoria {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idCategoria;

	private String nombre;

	private String descripcion;

	@OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private final List<Producto> productos = new ArrayList<>();

	public long getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(long idCategoria) {
		this.idCategoria = idCategoria;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos.clear();
		if (productos != null) {
			for (Producto producto : productos) {
				producto.setCategoria(this);
			}
			this.productos.addAll(productos);
		}
	}

	public boolean addProducto(Producto producto) {
		if (producto == null) {
			throw new IllegalArgumentException("El producto no puede ser null");
		}

		if (productos.contains(producto)) {
			return false;
		}

		producto.setCategoria(this);
		return productos.add(producto);
	}

	public boolean removeProducto(Producto producto) {
		if (producto == null) {
			throw new IllegalArgumentException("El producto no puede ser null");
		}

		if (productos.remove(producto)) {
			producto.setCategoria(null);
			return true;
		}

		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Categoria other = (Categoria) obj;
		return idCategoria == other.idCategoria;
	}

	@Override
	public int hashCode() {
		return (int) (31 * idCategoria);
	}

	@Override
	public String toString() {
		return "Categoria [idCategoria=" + idCategoria + ", nombre=" + nombre + ", descripcion="
			+ descripcion + ", productos=" + productos + "]";
	}
}
