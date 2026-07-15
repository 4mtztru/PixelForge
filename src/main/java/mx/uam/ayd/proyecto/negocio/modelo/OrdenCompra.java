package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
public class OrdenCompra {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idOrdenCompra;

	private String folio;

	private LocalDate fechaEmision;

	private LocalDate fechaEntrega;

	private String estado;

	private double montoTotal;

	private double anticipoPagado;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idProveedor")
	private Proveedor proveedor;

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

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
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

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrdenCompra other = (OrdenCompra) obj;
		return idOrdenCompra == other.idOrdenCompra;
	}

	@Override
	public int hashCode() {
		return (int) (31 * idOrdenCompra);
	}

	@Override
	public String toString() {
		return "OrdenCompra [idOrdenCompra=" + idOrdenCompra + ", folio=" + folio
			+ ", fechaEmision=" + fechaEmision + ", fechaEntrega=" + fechaEntrega + ", estado="
			+ estado + ", montoTotal=" + montoTotal + ", anticipoPagado=" + anticipoPagado
			+ "]";
	}
}
