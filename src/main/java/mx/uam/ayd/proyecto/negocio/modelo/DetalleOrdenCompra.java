package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Entidad que representa una línea o partida individual dentro de una Orden de Compra.
 * Almacena el producto solicitado, la cantidad esperada, precio unitario y subtotal.
 */
@Entity
public class DetalleOrdenCompra {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idDetalleOrdenCompra;

	private int cantidadEsperada;

	private double precioUnitario;

	private double subtotal;

	@ManyToOne
	@JoinColumn(name = "idProducto")
	private Producto producto;

	@ManyToOne
	@JoinColumn(name = "idOrdenCompra")
	private OrdenCompra ordenCompra;

	public long getIdDetalleOrdenCompra() {
		return idDetalleOrdenCompra;
	}

	public void setIdDetalleOrdenCompra(long idDetalleOrdenCompra) {
		this.idDetalleOrdenCompra = idDetalleOrdenCompra;
	}

	public int getCantidadEsperada() {
		return cantidadEsperada;
	}

	public void setCantidadEsperada(int cantidadEsperada) {
		this.cantidadEsperada = cantidadEsperada;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public OrdenCompra getOrdenCompra() {
		return ordenCompra;
	}

	public void setOrdenCompra(OrdenCompra ordenCompra) {
		this.ordenCompra = ordenCompra;
	}
}
