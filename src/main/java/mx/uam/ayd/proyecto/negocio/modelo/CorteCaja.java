package mx.uam.ayd.proyecto.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CorteCaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private double efectivoEsperado;
    private double efectivoFisico;
    private String justificacion;

    public double getEfectivoEsperado() { return efectivoEsperado; }
    public void setEfectivoEsperado(double efectivoEsperado) { this.efectivoEsperado = efectivoEsperado; }

    public double getEfectivoFisico() { return efectivoFisico; }
    public void setEfectivoFisico(double efectivoFisico) { this.efectivoFisico = efectivoFisico; }

    public String getJustificacion() { return justificacion; }
    public void setJustificacion(String justificacion) { this.justificacion = justificacion; }
}
