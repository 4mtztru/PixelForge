package mx.uam.ayd.proyecto.negocio.modelo;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class OrdenCompra {

	@Id
	private String folio;

	private LocalDate fechaEmision;

	private LocalDate fechaEntrega;

	@Enumerated(EnumType.STRING)
	private EstadoOrden estado;

	private double montoTotal;

	private double anticipoPagado;

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
}
