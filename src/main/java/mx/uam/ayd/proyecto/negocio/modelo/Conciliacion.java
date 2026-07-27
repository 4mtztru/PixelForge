package mx.uam.ayd.proyecto.negocio.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

/**
 * Entidad del Dominio: Conciliación.
 * Representa el registro de recepción física de productos para una Orden de Compra.
 * Almacena el resultado del cotejo entre cantidades esperadas y recibidas, así como los ajustes.
 */
@Entity
public class Conciliacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idConciliacion;

	private LocalDateTime fecha;

	@Enumerated(EnumType.STRING)
	private EstadoConciliacion estado;

	private int totalEsperado;

	private int totalRecibido;

	private double montoAjuste;

	private double montoFinalPagar;

	@OneToOne
	@JoinColumn(name = "idOrdenCompra")
	private OrdenCompra ordenCompra;

	@OneToMany(targetEntity = ProductoConciliado.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "conciliacion")
	private List<ProductoConciliado> productosConciliados = new ArrayList<>();

	public long getIdConciliacion() {
		return idConciliacion;
	}

	public void setIdConciliacion(long idConciliacion) {
		this.idConciliacion = idConciliacion;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public EstadoConciliacion getEstado() {
		return estado;
	}

	public void setEstado(EstadoConciliacion estado) {
		this.estado = estado;
	}

	public int getTotalEsperado() {
		return totalEsperado;
	}

	public void setTotalEsperado(int totalEsperado) {
		this.totalEsperado = totalEsperado;
	}

	public int getTotalRecibido() {
		return totalRecibido;
	}

	public void setTotalRecibido(int totalRecibido) {
		this.totalRecibido = totalRecibido;
	}

	public double getMontoAjuste() {
		return montoAjuste;
	}

	public void setMontoAjuste(double montoAjuste) {
		this.montoAjuste = montoAjuste;
	}

	public double getMontoFinalPagar() {
		return montoFinalPagar;
	}

	public void setMontoFinalPagar(double montoFinalPagar) {
		this.montoFinalPagar = montoFinalPagar;
	}

	public OrdenCompra getOrdenCompra() {
		return ordenCompra;
	}

	public void setOrdenCompra(OrdenCompra ordenCompra) {
		this.ordenCompra = ordenCompra;
	}

	public List<ProductoConciliado> getProductosConciliados() {
		return productosConciliados;
	}

	public void setProductosConciliados(List<ProductoConciliado> productosConciliados) {
		this.productosConciliados = productosConciliados;
	}
}
