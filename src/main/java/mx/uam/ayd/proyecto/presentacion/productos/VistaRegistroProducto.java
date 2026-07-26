package mx.uam.ayd.proyecto.presentacion.productos;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.negocio.modelo.Categoria;
import mx.uam.ayd.proyecto.negocio.modelo.Producto;
import mx.uam.ayd.proyecto.negocio.modelo.Proveedor;

/**
 * Vista JavaFX estándar para registrar productos (HU-04).
 */
@Component
public class VistaRegistroProducto {

	private Stage stage;
	private ControlRegistroProducto control;
	private boolean initialized;
	private boolean confirmacionPrecioMenorOtorgada;

	private ComboBox<Categoria> comboCategoria;
	private ComboBox<Proveedor> comboProveedor;
	private TextField campoNombre;
	private TextField campoPrecioCompra;
	private TextField campoPrecioVenta;
	private TextField campoStockInicial;
	private TextField campoStockMinimo;
	private TextField campoCodigo;
	private Label etiquetaMensaje;

	public void setControlRegistroProducto(ControlRegistroProducto control) {
		this.control = control;
	}

	public void mostrarFormularioRegistro(List<Proveedor> proveedores, List<Categoria> categorias) {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> mostrarFormularioRegistro(proveedores, categorias));
			return;
		}

		try {
			inicializarInterfaz();
			confirmacionPrecioMenorOtorgada = false;

			comboCategoria.getItems().setAll(categorias);
			comboProveedor.getItems().setAll(proveedores);

			if (!categorias.isEmpty()) {
				comboCategoria.getSelectionModel().select(0);
			}
			if (!proveedores.isEmpty()) {
				comboProveedor.getSelectionModel().select(0);
			}

			limpiarCampos();
			if (stage != null) {
				stage.show();
				stage.toFront();
			}
		} catch (Throwable t) {
			t.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR, "Error al abrir registro de producto: " + t.getMessage());
			alert.showAndWait();
		}
	}

	private void inicializarInterfaz() {
		if (initialized) {
			return;
		}

		stage = new Stage();
		stage.setTitle("Registrar Producto");

		VBox root = new VBox(10);
		root.setPadding(new Insets(15));

		Label titulo = new Label("Registrar Producto");
		titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

		etiquetaMensaje = new Label();
		etiquetaMensaje.setWrapText(true);
		etiquetaMensaje.setVisible(false);
		etiquetaMensaje.setManaged(false);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(8);

		ColumnConstraints col1 = new ColumnConstraints(140);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setHgrow(Priority.ALWAYS);
		grid.getColumnConstraints().addAll(col1, col2);

		comboCategoria = new ComboBox<>();
		comboCategoria.setPromptText("Seleccione categoría");
		comboCategoria.setPrefWidth(260);

		comboProveedor = new ComboBox<>();
		comboProveedor.setPromptText("Seleccione proveedor");
		comboProveedor.setPrefWidth(260);

		campoNombre = new TextField();
		campoPrecioCompra = new TextField("0.00");
		campoPrecioVenta = new TextField("0.00");
		campoStockInicial = new TextField("10");
		campoStockMinimo = new TextField("5");

		campoCodigo = new TextField();
		campoCodigo.setEditable(false);

		Button btnGenerar = new Button("Generar Código");
		btnGenerar.setOnAction(e -> handleGenerarCodigo());

		HBox boxCodigo = new HBox(8, campoCodigo, btnGenerar);
		HBox.setHgrow(campoCodigo, Priority.ALWAYS);

		grid.add(new Label("Categoría:"), 0, 0);
		grid.add(comboCategoria, 1, 0);
		grid.add(new Label("Proveedor:"), 0, 1);
		grid.add(comboProveedor, 1, 1);
		grid.add(new Label("Nombre:"), 0, 2);
		grid.add(campoNombre, 1, 2);
		grid.add(new Label("Precio Compra ($):"), 0, 3);
		grid.add(campoPrecioCompra, 1, 3);
		grid.add(new Label("Precio Venta ($):"), 0, 4);
		grid.add(campoPrecioVenta, 1, 4);
		grid.add(new Label("Stock Inicial:"), 0, 5);
		grid.add(campoStockInicial, 1, 5);
		grid.add(new Label("Stock Mínimo:"), 0, 6);
		grid.add(campoStockMinimo, 1, 6);
		grid.add(new Label("Código:"), 0, 7);
		grid.add(boxCodigo, 1, 7);

		Button btnCancelar = new Button("Cancelar");
		btnCancelar.setOnAction(e -> handleCancelar());

		Button btnGuardar = new Button("Guardar Producto");
		btnGuardar.setStyle("-fx-font-weight: bold;");
		btnGuardar.setOnAction(e -> handleGuardar());

		HBox boxBotones = new HBox(10, btnCancelar, btnGuardar);
		boxBotones.setAlignment(Pos.CENTER_RIGHT);

		root.getChildren().addAll(titulo, etiquetaMensaje, grid, new Separator(), boxBotones);

		comboCategoria.setCellFactory(param -> new ListCell<>() {
			@Override
			protected void updateItem(Categoria cat, boolean empty) {
				super.updateItem(cat, empty);
				setText(empty || cat == null ? "" : cat.getNombre());
			}
		});
		comboCategoria.setButtonCell(comboCategoria.getCellFactory().call(null));

		comboProveedor.setCellFactory(param -> new ListCell<>() {
			@Override
			protected void updateItem(Proveedor prov, boolean empty) {
				super.updateItem(prov, empty);
				setText(empty || prov == null ? "" : prov.getRazonSocial());
			}
		});
		comboProveedor.setButtonCell(comboProveedor.getCellFactory().call(null));

		Scene scene = new Scene(root, 460, 420);
		stage.setScene(scene);
		initialized = true;
	}

	public void mostrarError(String mensaje) {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> mostrarError(mensaje));
			return;
		}
		if (etiquetaMensaje != null) {
			etiquetaMensaje.setText(mensaje);
			etiquetaMensaje.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
			etiquetaMensaje.setVisible(true);
			etiquetaMensaje.setManaged(true);
		}
	}

	public void mostrarExito(String mensaje) {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> mostrarExito(mensaje));
			return;
		}
		if (etiquetaMensaje != null) {
			etiquetaMensaje.setText(mensaje);
			etiquetaMensaje.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
			etiquetaMensaje.setVisible(true);
			etiquetaMensaje.setManaged(true);
		}

		Alert alert = new Alert(AlertType.INFORMATION, mensaje, ButtonType.OK);
		alert.setHeaderText("Registro Exitoso");
		alert.showAndWait();
		ocultar();
	}

	public void solicitarConfirmacionPrecioMenor(String advertencia) {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> solicitarConfirmacionPrecioMenor(advertencia));
			return;
		}

		Alert alert = new Alert(AlertType.CONFIRMATION, advertencia, ButtonType.YES, ButtonType.NO);
		alert.setTitle("Confirmación Requerida");
		alert.setHeaderText("Precio de Venta Menor al Costo");
		Optional<ButtonType> respuesta = alert.showAndWait();

		if (respuesta.isPresent() && respuesta.get() == ButtonType.YES) {
			confirmacionPrecioMenorOtorgada = true;
			handleGuardar();
		}
	}

	public void ocultar() {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(this::ocultar);
			return;
		}
		if (stage != null) {
			stage.hide();
		}
	}

	private void limpiarCampos() {
		if (campoNombre != null) campoNombre.clear();
		if (campoPrecioCompra != null) campoPrecioCompra.setText("0.00");
		if (campoPrecioVenta != null) campoPrecioVenta.setText("0.00");
		if (campoStockInicial != null) campoStockInicial.setText("10");
		if (campoStockMinimo != null) campoStockMinimo.setText("5");
		if (campoCodigo != null) campoCodigo.clear();
		if (etiquetaMensaje != null) {
			etiquetaMensaje.setVisible(false);
			etiquetaMensaje.setManaged(false);
		}
	}

	private void handleGenerarCodigo() {
		Categoria cat = comboCategoria != null ? comboCategoria.getValue() : null;
		int catId = cat != null ? cat.getIdCategoria() : 1;
		String codigo = control.generarCodigo(catId);
		if (campoCodigo != null) campoCodigo.setText(codigo);
	}

	private void handleGuardar() {
		try {
			double precioCompra = Double.parseDouble(campoPrecioCompra.getText().trim());
			double precioVenta = Double.parseDouble(campoPrecioVenta.getText().trim());
			int stockInicial = Integer.parseInt(campoStockInicial.getText().trim());
			int stockMinimo = Integer.parseInt(campoStockMinimo.getText().trim());

			if (campoCodigo.getText() == null || campoCodigo.getText().isBlank()) {
				handleGenerarCodigo();
			}

			Producto p = new Producto();
			p.setNombre(campoNombre.getText().trim());
			p.setPrecio(precioVenta);
			p.setStockActual(stockInicial);
			p.setStockMinimo(stockMinimo);
			p.setSku(campoCodigo.getText().trim());
			p.setCodigoBarras(campoCodigo.getText().trim());
			p.setCategoria(comboCategoria.getValue());

			control.validarYGuardarProducto(p, precioCompra, confirmacionPrecioMenorOtorgada);

		} catch (NumberFormatException nfe) {
			mostrarError("Error: Ingrese valores numéricos válidos en los campos de precios y stock.");
		}
	}

	private void handleCancelar() {
		ocultar();
	}
}
