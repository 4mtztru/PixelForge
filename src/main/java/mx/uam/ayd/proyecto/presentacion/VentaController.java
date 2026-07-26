package mx.uam.ayd.proyecto.presentacion;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.negocio.ServicioProductos;
import mx.uam.ayd.proyecto.negocio.VentaService;
import mx.uam.ayd.proyecto.negocio.modelo.Producto;
import mx.uam.ayd.proyecto.negocio.modelo.Venta;

/**
 * Controlador y Vista JavaFX sencilla para el Punto de Venta (HU-03).
 */
@Component
public class VentaController {

    private final VentaService ventaService;
    private final ServicioProductos servicioProductos;

    private Stage stage;
    private boolean initialized;

    private final ObservableList<Producto> catalogo = FXCollections.observableArrayList();
    private final ObservableList<String> carritoItems = FXCollections.observableArrayList();
    private final List<Producto> productosEnCarrito = new ArrayList<>();
    private final NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-MX"));
    private double totalVenta = 0.0;

    private TableView<Producto> tablaCatalogo;
    private TableColumn<Producto, String> colNombre;
    private TableColumn<Producto, Double> colPrecio;
    private TableColumn<Producto, Integer> colStock;

    private ListView<String> listaCarrito;
    private Label etiquetaTotal;
    private Label etiquetaMensaje;

    private ToggleGroup grupoMetodoPago;
    private RadioButton radioEfectivo;
    private RadioButton radioTarjeta;
    private RadioButton radioTransferencia;

    private VBox panelEfectivo;
    private VBox panelTarjeta;
    private VBox panelTransferencia;

    private TextField txtMontoRecibido;
    private Label etiquetaCambio;

    private TextField txtAutorizacionTarjeta;
    private TextField txtBanco;
    private TextField txtReferencia;

    @Autowired
    public VentaController(VentaService ventaService, ServicioProductos servicioProductos) {
        this.ventaService = ventaService;
        this.servicioProductos = servicioProductos;
    }

    public void inicia() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::inicia);
            return;
        }

        try {
            inicializarInterfaz();
            cargarCatalogo();
            limpiarVenta();
            if (stage != null) {
                stage.show();
                stage.toFront();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR, "Error al abrir Punto de Venta: " + t.getMessage());
            alert.showAndWait();
        }
    }

    private void inicializarInterfaz() {
        if (initialized) {
            return;
        }

        stage = new Stage();
        stage.setTitle("Punto de Venta");

        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        Label titulo = new Label("Punto de Venta");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        etiquetaMensaje = new Label();
        etiquetaMensaje.setWrapText(true);
        etiquetaMensaje.setVisible(false);
        etiquetaMensaje.setManaged(false);

        // Panel Izquierdo: Catálogo y Carrito
        VBox panelIzquierdo = new VBox(8);
        HBox.setHgrow(panelIzquierdo, Priority.ALWAYS);

        Label lblCat = new Label("Productos Disponibles");
        lblCat.setStyle("-fx-font-weight: bold;");

        tablaCatalogo = new TableView<>();
        tablaCatalogo.setPrefHeight(150);

        colNombre = new TableColumn<>("Producto");
        colNombre.setPrefWidth(160);
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        colPrecio = new TableColumn<>("Precio");
        colPrecio.setPrefWidth(80);
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

        colStock = new TableColumn<>("Stock");
        colStock.setPrefWidth(70);
        colStock.setCellValueFactory(new PropertyValueFactory<>("stockActual"));

        tablaCatalogo.getColumns().addAll(colNombre, colPrecio, colStock);
        tablaCatalogo.setItems(catalogo);

        Button btnAgregar = new Button("Agregar al Carrito");
        btnAgregar.setOnAction(e -> handleAgregarCarrito());

        Label lblCar = new Label("Carrito de Compras");
        lblCar.setStyle("-fx-font-weight: bold;");

        listaCarrito = new ListView<>(carritoItems);
        listaCarrito.setPrefHeight(120);

        Button btnLimpiar = new Button("Limpiar Carrito");
        btnLimpiar.setOnAction(e -> handleLimpiarCarrito());

        Label lblTotalTxt = new Label("Total:");
        lblTotalTxt.setStyle("-fx-font-weight: bold;");

        etiquetaTotal = new Label("$0.00");
        etiquetaTotal.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        HBox boxTotal = new HBox(10, btnLimpiar, new Region(), lblTotalTxt, etiquetaTotal);
        HBox.setHgrow(boxTotal.getChildren().get(1), Priority.ALWAYS);
        boxTotal.setAlignment(Pos.CENTER_RIGHT);

        panelIzquierdo.getChildren().addAll(lblCat, tablaCatalogo, btnAgregar, lblCar, listaCarrito, boxTotal);

        // Panel Derecho: Métodos de Pago
        VBox panelDerecho = new VBox(8);
        panelDerecho.setPrefWidth(240);

        Label lblMetodo = new Label("Método de Pago");
        lblMetodo.setStyle("-fx-font-weight: bold;");

        grupoMetodoPago = new ToggleGroup();

        radioEfectivo = new RadioButton("Efectivo");
        radioEfectivo.setToggleGroup(grupoMetodoPago);
        radioEfectivo.setSelected(true);
        radioEfectivo.setOnAction(e -> handleCambioMetodo());

        radioTarjeta = new RadioButton("Tarjeta");
        radioTarjeta.setToggleGroup(grupoMetodoPago);
        radioTarjeta.setOnAction(e -> handleCambioMetodo());

        radioTransferencia = new RadioButton("Transferencia");
        radioTransferencia.setToggleGroup(grupoMetodoPago);
        radioTransferencia.setOnAction(e -> handleCambioMetodo());

        // Subpanel Efectivo
        panelEfectivo = new VBox(6);
        Label lblRecibido = new Label("Monto Recibido:");
        txtMontoRecibido = new TextField();
        txtMontoRecibido.setPromptText("0.00");

        Button btnCalcular = new Button("Calcular Cambio");
        btnCalcular.setOnAction(e -> handleCalcularCambio());

        etiquetaCambio = new Label("Cambio: $0.00");
        etiquetaCambio.setStyle("-fx-font-weight: bold;");
        panelEfectivo.getChildren().addAll(lblRecibido, txtMontoRecibido, btnCalcular, etiquetaCambio);

        // Subpanel Tarjeta
        panelTarjeta = new VBox(6);
        panelTarjeta.setVisible(false);
        panelTarjeta.setManaged(false);
        Label lblAuth = new Label("Código Autorización:");
        txtAutorizacionTarjeta = new TextField();
        panelTarjeta.getChildren().addAll(lblAuth, txtAutorizacionTarjeta);

        // Subpanel Transferencia
        panelTransferencia = new VBox(6);
        panelTransferencia.setVisible(false);
        panelTransferencia.setManaged(false);
        Label lblBanco = new Label("Banco:");
        txtBanco = new TextField();
        Label lblRef = new Label("Referencia:");
        txtReferencia = new TextField();
        panelTransferencia.getChildren().addAll(lblBanco, txtBanco, lblRef, txtReferencia);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button btnCobrar = new Button("Procesar Pago");
        btnCobrar.setPrefWidth(240);
        btnCobrar.setStyle("-fx-font-weight: bold;");
        btnCobrar.setOnAction(e -> handleProcesarCobro());

        panelDerecho.getChildren().addAll(lblMetodo, radioEfectivo, radioTarjeta, radioTransferencia,
                new Separator(), panelEfectivo, panelTarjeta, panelTransferencia, spacer, btnCobrar);

        HBox contenido = new HBox(12, panelIzquierdo, new Separator(Orientation.VERTICAL), panelDerecho);
        VBox.setVgrow(contenido, Priority.ALWAYS);

        root.getChildren().addAll(titulo, etiquetaMensaje, contenido);

        Scene scene = new Scene(root, 680, 520);
        stage.setScene(scene);
        initialized = true;
    }

    private void cargarCatalogo() {
        if (servicioProductos != null) {
            List<Producto> productos = servicioProductos.obtenerTodosLosProductos();
            if (productos != null) {
                catalogo.setAll(productos);
            }
        }
    }

    private void calcularTotal() {
        totalVenta = 0.0;
        for (Producto p : productosEnCarrito) {
            totalVenta += p.getPrecio();
        }
        if (etiquetaTotal != null) {
            etiquetaTotal.setText(formatoMoneda.format(totalVenta));
        }
    }

    @FXML
    public void handleAgregarCarrito() {
        Producto seleccionado = tablaCatalogo.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarMensaje("Seleccione un producto de la tabla.", "-fx-text-fill: red;");
            return;
        }

        if (seleccionado.getStockActual() <= 0) {
            mostrarMensaje("Producto sin stock disponible.", "-fx-text-fill: red;");
            return;
        }

        productosEnCarrito.add(seleccionado);
        carritoItems.add(seleccionado.getNombre() + " - " + formatoMoneda.format(seleccionado.getPrecio()));
        calcularTotal();
        ocultarMensaje();
    }

    @FXML
    public void handleLimpiarCarrito() {
        limpiarVenta();
    }

    @FXML
    public void handleCambioMetodo() {
        boolean esEfectivo = radioEfectivo != null && radioEfectivo.isSelected();
        boolean esTarjeta = radioTarjeta != null && radioTarjeta.isSelected();
        boolean esTransf = radioTransferencia != null && radioTransferencia.isSelected();

        if (panelEfectivo != null) {
            panelEfectivo.setVisible(esEfectivo);
            panelEfectivo.setManaged(esEfectivo);
        }
        if (panelTarjeta != null) {
            panelTarjeta.setVisible(esTarjeta);
            panelTarjeta.setManaged(esTarjeta);
        }
        if (panelTransferencia != null) {
            panelTransferencia.setVisible(esTransf);
            panelTransferencia.setManaged(esTransf);
        }

        ocultarMensaje();
    }

    @FXML
    public void handleCalcularCambio() {
        try {
            double recibido = Double.parseDouble(txtMontoRecibido.getText().trim());
            double cambio = recibido - totalVenta;
            if (cambio >= 0) {
                etiquetaCambio.setText("Cambio: " + formatoMoneda.format(cambio));
                ocultarMensaje();
            } else {
                etiquetaCambio.setText("Cambio: $0.00");
                mostrarMensaje("El monto recibido es insuficiente.", "-fx-text-fill: red;");
            }
        } catch (Exception e) {
            mostrarMensaje("Monto numérico no válido.", "-fx-text-fill: red;");
        }
    }

    @FXML
    public void handleProcesarCobro() {
        try {
            if (ventaService != null) {
                ventaService.validarCarrito(productosEnCarrito);
            } else if (productosEnCarrito.isEmpty()) {
                throw new IllegalArgumentException("El carrito está vacío");
            }

            Venta venta = new Venta();
            venta.setProductosEnCarrito(true);
            venta.setTotal(totalVenta);

            boolean esEfectivo = radioEfectivo != null && radioEfectivo.isSelected();
            boolean esTarjeta = radioTarjeta != null && radioTarjeta.isSelected();
            boolean esTransf = radioTransferencia != null && radioTransferencia.isSelected();

            if (esEfectivo) {
                venta.setMetodoPago("EFECTIVO");
                double recibido = 0.0;
                try {
                    recibido = Double.parseDouble(txtMontoRecibido.getText().trim());
                } catch (Exception ex) {
                    throw new IllegalArgumentException("Monto recibido no válido.");
                }
                venta.setMontoRecibido(recibido);

                if (ventaService != null) {
                    ventaService.procesarPago(venta);
                } else if (recibido < totalVenta) {
                    throw new IllegalArgumentException("El monto recibido es insuficiente.");
                }

                double cambio = recibido - totalVenta;
                if (etiquetaCambio != null) {
                    etiquetaCambio.setText("Cambio: " + formatoMoneda.format(cambio));
                }

            } else if (esTarjeta) {
                venta.setMetodoPago("TARJETA");
                String auth = txtAutorizacionTarjeta != null ? txtAutorizacionTarjeta.getText().trim() : "";
                venta.setReferenciaTransferencia(auth);

                if (ventaService != null) {
                    ventaService.procesarPago(venta);
                } else if (auth.isEmpty()) {
                    throw new IllegalArgumentException("Tarjeta rechazada / Código de autorización obligatorio.");
                }

            } else if (esTransf) {
                venta.setMetodoPago("TRANSFERENCIA");
                String ref = txtReferencia != null ? txtReferencia.getText().trim() : "";
                venta.setReferenciaTransferencia(ref);

                if (ventaService != null) {
                    ventaService.procesarPago(venta);
                } else if (ref.isEmpty()) {
                    throw new IllegalArgumentException("La referencia bancaria es obligatoria.");
                }
            }

            Venta ventaRealizada = (ventaService != null) ? ventaService.registrarVenta(venta, productosEnCarrito) : venta;
            mostrarTicketExitosa(ventaRealizada);
            cargarCatalogo();
            limpiarVenta();

        } catch (IllegalArgumentException ex) {
            mostrarMensaje("Error: " + ex.getMessage(), "-fx-text-fill: red;");
        }
    }

    private void mostrarTicketExitosa(Venta venta) {
        StringBuilder ticket = new StringBuilder();
        ticket.append("Venta registrada exitosamente.\n\n");
        ticket.append("Folio: ").append(venta.getId() != null ? venta.getId() : "TX-" + System.currentTimeMillis()).append("\n");
        ticket.append("Método: ").append(venta.getMetodoPago()).append("\n");
        ticket.append("Total: ").append(formatoMoneda.format(venta.getTotal())).append("\n");

        Alert alert = new Alert(AlertType.INFORMATION, ticket.toString());
        alert.setTitle("Venta Exitosa");
        alert.setHeaderText("Operación Completada");
        alert.showAndWait();
    }

    private void mostrarMensaje(String msg, String estilo) {
        if (etiquetaMensaje != null) {
            etiquetaMensaje.setText(msg);
            etiquetaMensaje.setStyle(estilo + " -fx-font-weight: bold;");
            etiquetaMensaje.setVisible(true);
            etiquetaMensaje.setManaged(true);
        }
    }

    private void ocultarMensaje() {
        if (etiquetaMensaje != null) {
            etiquetaMensaje.setVisible(false);
            etiquetaMensaje.setManaged(false);
        }
    }

    private void limpiarVenta() {
        productosEnCarrito.clear();
        carritoItems.clear();
        totalVenta = 0.0;
        if (etiquetaTotal != null) etiquetaTotal.setText("$0.00");
        if (txtMontoRecibido != null) txtMontoRecibido.clear();
        if (txtAutorizacionTarjeta != null) txtAutorizacionTarjeta.clear();
        if (txtBanco != null) txtBanco.clear();
        if (txtReferencia != null) txtReferencia.clear();
        if (etiquetaCambio != null) etiquetaCambio.setText("Cambio: $0.00");
        ocultarMensaje();
    }
}
