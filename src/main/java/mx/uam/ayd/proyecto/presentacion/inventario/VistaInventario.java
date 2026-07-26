package mx.uam.ayd.proyecto.presentacion.inventario;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Vista principal de navegación estilo estándar JavaFX.
 */
@Component
public class VistaInventario {

	private Stage stage;
	private ControlInventario controlInventario;
	private boolean initialized;

	public void setControlInventario(ControlInventario controlInventario) {
		this.controlInventario = controlInventario;
	}

	public void mostrarInventario() {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(this::mostrarInventario);
			return;
		}

		inicializarInterfaz();
		if (stage != null) {
			stage.show();
			stage.toFront();
		}
	}

	private void inicializarInterfaz() {
		if (initialized) {
			return;
		}

		stage = new Stage();
		stage.setTitle("PixelForge");

		VBox root = new VBox(10);
		root.setPadding(new Insets(15));
		root.setAlignment(Pos.TOP_LEFT);

		Label titulo = new Label("PixelForge");
		titulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

		Label subtitulo = new Label("Control de inventario");
		subtitulo.setStyle("-fx-text-fill: #52606d;");

		Button btnInventario = new Button("Inventario");
		btnInventario.setPrefWidth(220);
		btnInventario.setPrefHeight(38);
		btnInventario.setStyle("-fx-font-weight: bold;");
		btnInventario.setOnAction(e -> handleInventario());

		Button btnRegistrar = new Button("Registrar Producto");
		btnRegistrar.setPrefWidth(220);
		btnRegistrar.setPrefHeight(38);
		btnRegistrar.setStyle("-fx-font-weight: bold;");
		btnRegistrar.setOnAction(e -> handleRegistrarProducto());

		Button btnPuntoVenta = new Button("Punto de Venta");
		btnPuntoVenta.setPrefWidth(220);
		btnPuntoVenta.setPrefHeight(38);
		btnPuntoVenta.setStyle("-fx-font-weight: bold;");
		btnPuntoVenta.setOnAction(e -> handlePuntoVenta());

		root.getChildren().addAll(
			titulo, 
			subtitulo, 
			new Separator(),
			btnInventario, 
			btnRegistrar, 
			btnPuntoVenta
		);

		Scene scene = new Scene(root, 360, 260);
		stage.setScene(scene);
		initialized = true;
	}

	private void handleInventario() {
		if (controlInventario != null) {
			controlInventario.solicitarProductos();
		}
	}

	private void handleRegistrarProducto() {
		if (controlInventario != null) {
			controlInventario.solicitarRegistrarProducto();
		}
	}

	private void handlePuntoVenta() {
		if (controlInventario != null) {
			controlInventario.solicitarPuntoVenta();
		}
	}
}
