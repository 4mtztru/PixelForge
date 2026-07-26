package mx.uam.ayd.proyecto.presentacion.inventario;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.presentacion.VentaController;
import mx.uam.ayd.proyecto.presentacion.productos.ControlProductos;
import mx.uam.ayd.proyecto.presentacion.productos.ControlRegistroProducto;

/**
 * Controla el menú principal y la navegación del sistema.
 */
@Component
public class ControlInventario {

	private final ControlProductos controlProductos;
	private final ControlRegistroProducto controlRegistroProducto;
	private final VentaController ventaController;
	private final VistaInventario vistaInventario;

	@Autowired
	public ControlInventario(ControlProductos controlProductos,
			ControlRegistroProducto controlRegistroProducto,
			VentaController ventaController,
			VistaInventario vistaInventario) {
		this.controlProductos = controlProductos;
		this.controlRegistroProducto = controlRegistroProducto;
		this.ventaController = ventaController;
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

	public void solicitarRegistrarProducto() {
		controlRegistroProducto.inicia();
	}

	public void solicitarPuntoVenta() {
		ventaController.inicia();
	}
}
