package mx.uam.ayd.proyecto.presentacion.inventario;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.presentacion.listarUsuarios.ControlListarUsuarios;
import mx.uam.ayd.proyecto.presentacion.productos.ControlProductos;

/**
 * Controla la entrada a la sección de inventario.
 */
@Component
public class ControlInventario {

	private final ControlProductos controlProductos;
	private final ControlListarUsuarios controlListarUsuarios;
	private final VistaInventario vistaInventario;

	@Autowired
	public ControlInventario(ControlProductos controlProductos,
			ControlListarUsuarios controlListarUsuarios,
			VistaInventario vistaInventario) {
		this.controlProductos = controlProductos;
		this.controlListarUsuarios = controlListarUsuarios;
		this.vistaInventario = vistaInventario;
	}

	@PostConstruct
	public void init() {
		vistaInventario.setControlInventario(this);
	}

	public void inicia() {
		vistaInventario.mostrarInventario();
	}

	public void solicitarProductos() {
		controlProductos.iniciarProductos();
	}

	public void listarUsuarios() {
		controlListarUsuarios.inicia();
	}
}
