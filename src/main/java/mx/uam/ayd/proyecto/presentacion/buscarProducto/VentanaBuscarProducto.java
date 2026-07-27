package mx.uam.ayd.proyecto.presentacion.buscarProducto;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.negocio.ServicioBuscarProducto;
import mx.uam.ayd.proyecto.negocio.modelo.EstadoStock;
import mx.uam.ayd.proyecto.negocio.modelo.Producto;

/**
 * Ventana JavaFX para la búsqueda de productos en el catálogo de inventario.
 * Despliega el formulario de búsqueda por Nombre, SKU o Código de Barras, y muestra los resultados en tabla.
 */
@Component
public class VentanaBuscarProducto {
    private Stage stage;
    private ControlBuscarProducto control;
    private boolean initialized;

    @FXML
    private TextField textNombreProducto;

    @FXML
    private TextField textSku;

    @FXML
    private TextField textCodigoBarras;

    @FXML
    private TableView<Producto> tablaProductos;

    @FXML
    private TableColumn<Producto, String> colCodigo;

    @FXML
    private TableColumn<Producto, String> colNombre;

    @FXML
    private TableColumn<Producto, String> colCategoria;

    @FXML
    private TableColumn<Producto, Integer> colStockActual;

    @FXML
    private TableColumn<Producto, Double> colPrecio;

    @FXML
    private Label labelMensaje;

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
            stage.setTitle("Buscar Producto");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-buscar-producto.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load(), 850, 500);
            stage.setScene(scene);

            configurarTabla();
            initialized = true;
        } catch (IOException exception) {
            throw new IllegalStateException("No fue posible abrir el inventario de productos.", exception);
        }
    }

    private void configurarTabla() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("sku"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("nombreCategoria"));
        colStockActual.setCellValueFactory(new PropertyValueFactory<>("stockActual"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

        colStockActual.setCellFactory(column -> crearCeldaStock());
        colPrecio.setCellFactory(column -> crearCeldaPrecio());
        tablaProductos.setPlaceholder(new Label("Ingrese un criterio y presione Enter para buscar."));
    }

    private TableCell<Producto, Integer> crearCeldaStock() {
        return new TableCell<>() {
            @Override
            protected void updateItem(Integer stock, boolean empty) {
                super.updateItem(stock, empty);
                setText(null);
                setGraphic(null);

                if (empty || stock == null || getIndex() >= getTableView().getItems().size()) {
                    return;
                }

                Producto producto = getTableView().getItems().get(getIndex());
                Label cantidad = new Label(String.valueOf(stock));
                cantidad.setStyle("-fx-font-weight: bold;");

                HBox contenido = new HBox(7);
                contenido.setAlignment(Pos.CENTER_LEFT);
                contenido.getChildren().add(cantidad);

                if (producto.getEstadoStock() == EstadoStock.critico) {
                    cantidad.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
                    Label alerta = new Label("STOCK BAJO");
                    alerta.setStyle(
                            "-fx-font-size: 9px; -fx-text-fill: red; -fx-border-color: red;"
                                    + " -fx-padding: 2px 5px;");
                    contenido.getChildren().add(alerta);
                } else {
                    Label disponible = new Label("●");
                    disponible.setStyle("-fx-text-fill: green; -fx-font-size: 15px;");
                    contenido.getChildren().add(disponible);
                }

                setGraphic(contenido);
            }
        };
    }

    private TableCell<Producto, Double> crearCeldaPrecio() {
        return new TableCell<>() {
            @Override
            protected void updateItem(Double precio, boolean empty) {
                super.updateItem(precio, empty);
                setGraphic(null);
                setText(empty || precio == null
                        ? null
                        : String.format(Locale.forLanguageTag("es-MX"), "$%,.2f", precio));
            }
        };
    }

    public void setControl(ControlBuscarProducto control) {
        this.control = control;
    }

    public void muestra() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::muestra);
            return;
        }

        initializeUI();
        stage.setMaximized(true);
        limpiarBusqueda();
        stage.show();
        stage.toFront();
        textNombreProducto.requestFocus();
    }

    public void mostrarVistaProductos() {
        muestra();
    }

    public void mostrarResultados(List<Producto> productos, String criterio, String valor) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> mostrarResultados(productos, criterio, valor));
            return;
        }

        List<Producto> resultados = productos == null ? List.of() : productos;
        tablaProductos.setItems(FXCollections.observableArrayList(resultados));

        if (resultados.isEmpty()) {
            mostrarError(mensajeSinResultados(criterio, valor));
            return;
        }

        labelMensaje.setText("Se encontraron " + resultados.size() + " coincidencia(s).");
        labelMensaje.setStyle("-fx-text-fill: green;");
    }

    public void mostrarError(String mensaje) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> mostrarError(mensaje));
            return;
        }

        if (tablaProductos != null) {
            tablaProductos.getItems().clear();
        }
        if (labelMensaje != null) {
            labelMensaje.setText(mensaje);
            labelMensaje.setStyle("-fx-text-fill: red;");
        }
    }

    private String mensajeSinResultados(String criterio, String valor) {
        if (criterio == null || criterio.isBlank()) {
            return "No se encontró ningún producto.";
        }
        if (ServicioBuscarProducto.CRITERIO_NOMBRE.equalsIgnoreCase(criterio)) {
            return "No se encontró ningún producto con el nombre \"" + valor + "\".";
        }
        if (ServicioBuscarProducto.CRITERIO_SKU.equalsIgnoreCase(criterio)) {
            return "No se encontró ningún producto con el SKU \"" + valor + "\".";
        }
        return "No se encontró ningún producto con el código de barras \"" + valor + "\".";
    }

    @FXML
    private void handleBuscar() {
        if (control != null) {
            control.solicitarBusqueda(
                    textNombreProducto.getText(),
                    textSku.getText(),
                    textCodigoBarras.getText());
        }
    }

    @FXML
    public void limpiarBusqueda() {
        if (textNombreProducto != null) {
            textNombreProducto.clear();
        }
        if (textSku != null) {
            textSku.clear();
        }
        if (textCodigoBarras != null) {
            textCodigoBarras.clear();
        }
        if (tablaProductos != null) {
            tablaProductos.getItems().clear();
        }
        if (labelMensaje != null) {
            labelMensaje.setText("");
            labelMensaje.setStyle("");
        }
        if (textNombreProducto != null) {
            textNombreProducto.requestFocus();
        }
    }

    @FXML
    private void handleCerrar() {
        if (stage != null) {
            stage.close();
        }
    }
}
