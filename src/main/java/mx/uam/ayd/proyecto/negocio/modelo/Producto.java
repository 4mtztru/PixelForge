package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Producto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idProducto;

	private String codigoBarras;

	private String nombre;

	private double precio;

	private int stockActual;

	private int stockMinimo;

	private String estadoStock;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idCategoria")
	private Categoria categoria;

	public long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(long idProducto) {
		this.idProducto = idProducto;
	}

	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public int getStockActual() {
		return stockActual;
	}

	public void setStockActual(int stockActual) {
		this.stockActual = stockActual;
	}

	public int getStockMinimo() {
		return stockMinimo;
	}

	public void setStockMinimo(int stockMinimo) {
		this.stockMinimo = stockMinimo;
	}

	public String getEstadoStock() {
		return estadoStock;
	}

	public void setEstadoStock(String estadoStock) {
		this.estadoStock = estadoStock;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Producto other = (Producto) obj;
		return idProducto == other.idProducto;
	}

	@Override
	public int hashCode() {
		return (int) (31 * idProducto);
	}

	@Override
	public String toString() {
		return "Producto [idProducto=" + idProducto + ", codigoBarras=" + codigoBarras
			+ ", nombre=" + nombre + ", precio=" + precio + ", stockActual=" + stockActual
			+ ", stockMinimo=" + stockMinimo + ", estadoStock=" + estadoStock + "]";
	}
}
