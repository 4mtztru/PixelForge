package mx.uam.ayd.proyecto.negocio.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class OrdenCompra {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idOrdenCompra;

	private String folio;

	private LocalDate fechaEmision;

	private LocalDate fechaEntrega;

	@Enumerated(EnumType.STRING)
	private EstadoOrden estado;

	private double montoTotal;

	private double anticipoPagado;

	public long getIdOrdenCompra() {
		return idOrdenCompra;
	}

	public void setIdOrdenCompra(long idOrdenCompra) {
		this.idOrdenCompra = idOrdenCompra;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public LocalDate getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(LocalDate fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public LocalDate getFechaEntrega() {
		return fechaEntrega;
	}

	public void setFechaEntrega(LocalDate fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}

	public EstadoOrden getEstado() {
		return estado;
	}

	public void setEstado(EstadoOrden estado) {
		this.estado = estado;
	}

	public double getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(double montoTotal) {
		this.montoTotal = montoTotal;
	}

	public double getAnticipoPagado() {
		return anticipoPagado;
	}

	public void setAnticipoPagado(double anticipoPagado) {
		this.anticipoPagado = anticipoPagado;
	}

	@ManyToOne
	@JoinColumn(name = "idProveedor")
	private Proveedor proveedor;

	@OneToOne(mappedBy = "ordenCompra", cascade = CascadeType.ALL)
	private Conciliacion conciliacion;

	@OneToMany(targetEntity = DetalleOrdenCompra.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "ordenCompra")
	private List<DetalleOrdenCompra> detalles = new ArrayList<>();

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public Conciliacion getConciliacion() {
		return conciliacion;
	}

	public void setConciliacion(Conciliacion conciliacion) {
		this.conciliacion = conciliacion;
	}

	public List<DetalleOrdenCompra> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleOrdenCompra> detalles) {
		this.detalles = detalles;
	}
}
