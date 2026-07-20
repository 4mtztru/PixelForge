package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Empleado {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idEmpleado;

	private String nombre;

	@Enumerated(EnumType.STRING)
	private RolEmpleado rol;

	public int getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(int idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public RolEmpleado getRol() {
		return rol;
	}

	public void setRol(RolEmpleado rol) {
		this.rol = rol;
	}
}
