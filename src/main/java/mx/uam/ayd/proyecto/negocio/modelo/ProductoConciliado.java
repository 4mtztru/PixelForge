package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ProductoConciliado {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idProductoConciliado;

	private int cantidadEsperada;

	private int cantidadRecibida;

	private int diferencia;

	@Enumerated(EnumType.STRING)
	private EstadoPartida estado;

	private double importeAjuste;

	public int getIdProductoConciliado() {
		return idProductoConciliado;
	}

	public int getCantidadEsperada() {
		return cantidadEsperada;
	}

	public void setCantidadEsperada(int cantidadEsperada) {
		this.cantidadEsperada = cantidadEsperada;
	}

	public int getCantidadRecibida() {
		return cantidadRecibida;
	}

	public void setCantidadRecibida(int cantidadRecibida) {
		this.cantidadRecibida = cantidadRecibida;
	}

	public int getDiferencia() {
		return diferencia;
	}

	public void setDiferencia(int diferencia) {
		this.diferencia = diferencia;
	}

	public EstadoPartida getEstado() {
		return estado;
	}

	public void setEstado(EstadoPartida estado) {
		this.estado = estado;
	}

	public double getImporteAjuste() {
		return importeAjuste;
	}

	public void setImporteAjuste(double importeAjuste) {
		this.importeAjuste = importeAjuste;
	}

	@ManyToOne
	@JoinColumn(name = "idProducto")
	private Producto producto;

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	@ManyToOne
	@JoinColumn(name = "idConciliacion")
	private Conciliacion conciliacion;

	public Conciliacion getConciliacion() {
		return conciliacion;
	}

	public void setConciliacion(Conciliacion conciliacion) {
		this.conciliacion = conciliacion;
	}
}
