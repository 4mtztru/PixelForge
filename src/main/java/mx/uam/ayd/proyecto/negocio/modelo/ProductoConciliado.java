package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class ProductoConciliado {

	private int cantidadEsperada;

	private int cantidadRecibida;

	private int diferencia;

	@Enumerated(EnumType.STRING)
	private EstadoPartida estado;

	private double importeAjuste;

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
}
