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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Conciliacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idConciliacion;

	private LocalDateTime fecha;

	@Enumerated(EnumType.STRING)
	private EstadoConciliacion estado;

	private int totalEsperado;

	private int totalRecibido;

	private double montoAjuste;

	private double montoFinalPagar;

	public int getIdConciliacion() {
		return idConciliacion;
	}

	public void setIdConciliacion(int idConciliacion) {
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

	@OneToOne
	@JoinColumn(name = "idOrdenCompra")
	private OrdenCompra ordenCompra;

	@OneToOne(mappedBy = "conciliacion", cascade = CascadeType.ALL)
	private ReporteLiquidacion reporteLiquidacion;

	@ManyToOne
	@JoinColumn(name = "idUsuario")
	private Usuario usuario;

	@OneToMany(targetEntity = ProductoConciliado.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "conciliacion")
	private List<ProductoConciliado> productosConciliados = new ArrayList<>();

	public OrdenCompra getOrdenCompra() {
		return ordenCompra;
	}

	public void setOrdenCompra(OrdenCompra ordenCompra) {
		this.ordenCompra = ordenCompra;
	}

	public ReporteLiquidacion getReporteLiquidacion() {
		return reporteLiquidacion;
	}

	public void setReporteLiquidacion(ReporteLiquidacion reporteLiquidacion) {
		this.reporteLiquidacion = reporteLiquidacion;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<ProductoConciliado> getProductosConciliados() {
		return productosConciliados;
	}

	public void setProductosConciliados(List<ProductoConciliado> productosConciliados) {
		this.productosConciliados = productosConciliados;
	}
}
