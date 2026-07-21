package mx.uam.ayd.proyecto.negocio.modelo;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class ReporteLiquidacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idReporteLiquidacion;

	private String folioReporte;

	private LocalDateTime fechaEmision;

	private double montoOriginal;

	private double ajusteFaltantes;

	private double anticipoPagado;

	private double montoFinalPagar;

	public int getIdReporteLiquidacion() {
		return idReporteLiquidacion;
	}

	public String getFolioReporte() {
		return folioReporte;
	}

	public void setFolioReporte(String folioReporte) {
		this.folioReporte = folioReporte;
	}

	public LocalDateTime getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(LocalDateTime fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public double getMontoOriginal() {
		return montoOriginal;
	}

	public void setMontoOriginal(double montoOriginal) {
		this.montoOriginal = montoOriginal;
	}

	public double getAjusteFaltantes() {
		return ajusteFaltantes;
	}

	public void setAjusteFaltantes(double ajusteFaltantes) {
		this.ajusteFaltantes = ajusteFaltantes;
	}

	public double getAnticipoPagado() {
		return anticipoPagado;
	}

	public void setAnticipoPagado(double anticipoPagado) {
		this.anticipoPagado = anticipoPagado;
	}

	public double getMontoFinalPagar() {
		return montoFinalPagar;
	}

	public void setMontoFinalPagar(double montoFinalPagar) {
		this.montoFinalPagar = montoFinalPagar;
	}

	@OneToOne
	@JoinColumn(name = "idConciliacion")
	private Conciliacion conciliacion;

	public Conciliacion getConciliacion() {
		return conciliacion;
	}

	public void setConciliacion(Conciliacion conciliacion) {
		this.conciliacion = conciliacion;
	}
}
