package mx.uam.ayd.proyecto.presentacion.principal;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Ventana principal de la aplicación construida con JavaFX y FXML.
 * Proporciona el menú inicial de acceso a las funcionalidades del sistema.
 */
@Component
public class VentanaPrincipal {

	private Stage stage;
	private ControlPrincipal control;
	private boolean initialized = false;

	public VentanaPrincipal() {
	}

	/**
	 * Inicializa los componentes de la interfaz de usuario en el hilo principal de JavaFX.
	 */
	private void initializeUI() {
		if (initialized) {
			return;
		}

		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(this::initializeUI);
			return;
		}

		try {
			stage = new Stage();
			stage.setTitle("Mi Aplicación");

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-principal.fxml"));
			loader.setController(this);
			Scene scene = new Scene(loader.load(), 400, 220);
			stage.setScene(scene);

			initialized = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setControlPrincipal(ControlPrincipal control) {
		this.control = control;
	}

	/**
	 * Muestra la ventana principal en pantalla.
	 */
	public void muestra() {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> this.muestra());
			return;
		}

		initializeUI();
		stage.show();
		stage.toFront();
	}

	/**
	 * Manejador del evento del botón de Inventario. Delega la acción al controlador principal.
	 */
	@FXML
	private void handleInventario() {
		if (control != null) {
			control.inventario();
		}
	}
}
