package mx.uam.ayd.proyecto.presentacion.principal;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.presentacion.inventario.ControlInventario;

/**
 * Controlador principal que maneja la ventana inicial del sistema y la navegación
 * hacia los módulos principales (como Inventario).
 */
@Component
public class ControlPrincipal {

	private final ControlInventario controlInventario;
	private final VentanaPrincipal ventana;

	@Autowired
	public ControlPrincipal(
			ControlInventario controlInventario,
			VentanaPrincipal ventana) {
		this.controlInventario = controlInventario;
		this.ventana = ventana;
	}

	/**
	 * Método que se ejecuta después de la construcción del bean
	 * y realiza la conexión bidireccional entre el control principal y la ventana
	 * principal
	 */
	@PostConstruct
	public void init() {
		ventana.setControlPrincipal(this);
	}

	/**
	 * Inicia el flujo de control de la ventana principal
	 * 
	 */
	public void inicia() {
		ventana.muestra();
	}

	/**
	 * Método que arranca la historia de usuario "inventario"
	 * 
	 */
	public void inventario() {
		controlInventario.inicia();
	}
}
