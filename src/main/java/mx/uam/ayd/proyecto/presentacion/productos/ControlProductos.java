package mx.uam.ayd.proyecto.presentacion.productos;

import java.util.List;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.negocio.CriterioBusquedaProducto;
import mx.uam.ayd.proyecto.negocio.ServicioProductos;
import mx.uam.ayd.proyecto.negocio.modelo.Producto;

/**
 * Coordina el flujo de HU-01 sin contener lógica de negocio.
 */
@Component
public class ControlProductos {

	private final ServicioProductos servicioProductos;
	private final VistaProductos vistaProductos;

	@Autowired
	public ControlProductos(ServicioProductos servicioProductos, VistaProductos vistaProductos) {
		this.servicioProductos = servicioProductos;
		this.vistaProductos = vistaProductos;
	}

	@PostConstruct
	public void init() {
		vistaProductos.setControlProductos(this);
	}

	public void iniciarProductos() {
		vistaProductos.mostrarVistaProductos();
	}

	public void solicitarBusqueda(CriterioBusquedaProducto criterio, String valor) {
		try {
			List<Producto> productos = servicioProductos.buscarProducto(criterio, valor);
			if (productos.isEmpty()) {
				vistaProductos.mostrarSinResultados(criterio, valor);
				return;
			}
			vistaProductos.mostrarResultados(productos);
		} catch (IllegalArgumentException ex) {
			vistaProductos.mostrarMensaje(ex.getMessage());
		}
	}

	public void termina() {
		vistaProductos.setVisible(false);
	}
}
