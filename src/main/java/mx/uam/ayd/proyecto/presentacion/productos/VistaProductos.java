package mx.uam.ayd.proyecto.presentacion.productos;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.negocio.CriterioBusquedaProducto;
import mx.uam.ayd.proyecto.negocio.modelo.Categoria;
import mx.uam.ayd.proyecto.negocio.modelo.EstadoStock;
import mx.uam.ayd.proyecto.negocio.modelo.Producto;

/**
 * Vista JavaFX de HU-01 para buscar y consultar productos del inventario.
 */
@Component
public class VistaProductos {

	private static final String ESTILO_MENSAJE_INICIAL =
			"-fx-text-fill: #52606d; -fx-background-color: #eef6fb;";
	private static final String ESTILO_MENSAJE_ERROR =
			"-fx-text-fill: #b42318; -fx-background-color: #fef3f2;";
	private static final String ESTILO_MENSAJE_EXITO =
			"-fx-text-fill: #067647; -fx-background-color: #ecfdf3;";

	private final ObservableList<Producto> productos = FXCollections.observableArrayList();
	private final NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-MX"));

	private Stage stage;
	private ControlProductos control;
	private boolean initialized;

	@FXML
	private TextField campoNombre;

	@FXML
	private TextField campoSku;

	@FXML
	private TextField campoCodigoBarras;

	@FXML
	private Label etiquetaMensaje;

	@FXML
	private TableView<Producto> tablaProductos;

	@FXML
	private TableColumn<Producto, String> columnaCodigo;

	@FXML
	private TableColumn<Producto, String> columnaProducto;

	@FXML
	private TableColumn<Producto, String> columnaCategoria;

	@FXML
	private TableColumn<Producto, Integer> columnaStock;

	@FXML
	private TableColumn<Producto, Double> columnaPrecio;

	public void setControlProductos(ControlProductos control) {
		this.control = control;
	}

	public void mostrarVistaProductos() {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(this::mostrarVistaProductos);
			return;
		}

		inicializaInterfaz();
		limpiarBusqueda();
		stage.show();
		stage.toFront();
	}

	public void mostrarResultados(List<Producto> resultados) {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> mostrarResultados(resultados));
			return;
		}

		productos.setAll(resultados);
		int total = resultados.size();
		mostrarEstado(total == 1 ? "Se encontró 1 producto."
				: "Se encontraron " + total + " productos.", ESTILO_MENSAJE_EXITO);
	}

	public void mostrarSinResultados(CriterioBusquedaProducto criterio, String valor) {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> mostrarSinResultados(criterio, valor));
			return;
		}

		productos.clear();
		String mensaje = switch (criterio) {
			case NOMBRE -> "No se encontraron coincidencias para \"" + valor + "\". "
					+ "Intente con términos más generales o verifique la ortografía.";
			case SKU -> "No se encontró ningún producto con el SKU: " + valor
					+ ". Verifique el formato e intente de nuevo.";
			case CODIGO_BARRAS -> "Código de barras no reconocido. "
					+ "Verifique el código o ingréselo manualmente.";
		};
		mostrarEstado(mensaje, ESTILO_MENSAJE_ERROR);
	}

	public void mostrarMensaje(String mensaje) {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> mostrarMensaje(mensaje));
			return;
		}

		productos.clear();
		mostrarEstado(mensaje, ESTILO_MENSAJE_ERROR);
	}

	public void limpiarBusqueda() {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(this::limpiarBusqueda);
			return;
		}

		if (!initialized) {
			return;
		}

		campoNombre.clear();
		campoSku.clear();
		campoCodigoBarras.clear();
		productos.clear();
		mostrarEstado("Inicie una búsqueda para ver resultados. Ingrese el nombre del producto, "
				+ "SKU o código de barras y presione Enter.", ESTILO_MENSAJE_INICIAL);
		campoNombre.requestFocus();
	}

	public void setVisible(boolean visible) {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> setVisible(visible));
			return;
		}
		if (!initialized) {
			if (!visible) {
				return;
			}
			inicializaInterfaz();
		}
		if (visible) {
			stage.show();
		} else {
			stage.hide();
		}
	}

	private void inicializaInterfaz() {
		if (initialized) {
			return;
		}

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/vista-productos.fxml"));
			loader.setController(this);
			Scene scene = new Scene(loader.load(), 1180, 650);
			var css = getClass().getResource("/css/inventario.css");
			if (css != null) {
				scene.getStylesheets().add(css.toExternalForm());
			}

			stage = new Stage();
			stage.setTitle("Inventario de Productos");
			stage.setMinWidth(1000);
			stage.setMinHeight(520);
			stage.setScene(scene);

			configuraTabla();
			initialized = true;
		} catch (IOException ex) {
			throw new IllegalStateException("No fue posible cargar la vista de productos", ex);
		}
	}

	private void configuraTabla() {
		columnaCodigo.setCellValueFactory(new PropertyValueFactory<>("sku"));
		columnaProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		columnaCategoria.setCellValueFactory(datos -> {
			Categoria categoria = datos.getValue().getCategoria();
			return new SimpleStringProperty(categoria == null ? "Sin categoría" : categoria.getNombre());
		});
		columnaStock.setCellValueFactory(new PropertyValueFactory<>("stockActual"));
		columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
		columnaStock.setCellFactory(columna -> creaCeldaStock());
		columnaPrecio.setCellFactory(columna -> creaCeldaPrecio());
		tablaProductos.setItems(productos);
	}

	private TableCell<Producto, Integer> creaCeldaStock() {
		return new TableCell<>() {
			@Override
			protected void updateItem(Integer stock, boolean vacia) {
				super.updateItem(stock, vacia);
				setText(null);
				setStyle("");
				if (vacia || stock == null || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
					return;
				}

				Producto producto = getTableView().getItems().get(getIndex());
				if (producto.getEstadoStock() == EstadoStock.critico) {
					setText(stock + "  STOCK BAJO");
					setStyle("-fx-text-fill: #b42318; -fx-background-color: #fee4e2; -fx-font-weight: bold;");
				} else {
					setText(stock + "  ●");
					setStyle("-fx-text-fill: #067647; -fx-font-weight: bold;");
				}
			}
		};
	}

	private TableCell<Producto, Double> creaCeldaPrecio() {
		return new TableCell<>() {
			@Override
			protected void updateItem(Double precio, boolean vacia) {
				super.updateItem(precio, vacia);
				setText(vacia || precio == null ? null : formatoMoneda.format(precio));
			}
		};
	}

	private void mostrarEstado(String mensaje, String estilo) {
		etiquetaMensaje.setText(mensaje);
		etiquetaMensaje.setStyle(estilo);
	}

	@FXML
	private void handleBuscar() {
		try {
			SolicitudBusquedaProducto solicitud = SolicitudBusquedaProducto.desdeCampos(
					campoNombre.getText(), campoSku.getText(), campoCodigoBarras.getText());
			control.solicitarBusqueda(solicitud.criterio(), solicitud.valor());
		} catch (IllegalArgumentException ex) {
			mostrarMensaje(ex.getMessage());
		}
	}

	@FXML
	private void handleLimpiar() {
		limpiarBusqueda();
	}

	@FXML
	private void handleCerrar() {
		control.termina();
	}
}
