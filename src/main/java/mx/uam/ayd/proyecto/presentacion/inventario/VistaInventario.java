package mx.uam.ayd.proyecto.presentacion.inventario;

import java.io.IOException;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Vista desde la que el empleado inicia la consulta del inventario.
 */
@Component
public class VistaInventario {

	private Stage stage;
	private ControlInventario controlInventario;
	private boolean initialized;

	public void setControlInventario(ControlInventario controlInventario) {
		this.controlInventario = controlInventario;
	}

	/**
	 * Muestra la vista inicial del inventario.
	 */
	public void mostrarInventario() {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(this::mostrarInventario);
			return;
		}

		inicializarInterfaz();
		stage.show();
		stage.toFront();
	}

	private void inicializarInterfaz() {
		if (initialized) {
			return;
		}

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/vista-inventario.fxml"));
			loader.setController(this);
			Scene scene = new Scene(loader.load(), 450, 360);

			stage = new Stage();
			stage.setTitle("PixelForge");
			stage.setScene(scene);
			initialized = true;
		} catch (IOException ex) {
			throw new IllegalStateException("No fue posible cargar la vista del inventario", ex);
		}
	}

	@FXML
	private void handleInventario() {
		if (controlInventario != null) {
			controlInventario.solicitarProductos();
		}
	}

	@FXML
	private void handleListarUsuarios() {
		if (controlInventario != null) {
			controlInventario.listarUsuarios();
		}
	}
}
