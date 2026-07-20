package mx.uam.ayd.proyecto.negocio.modelo;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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
}
