package mx.uam.ayd.proyecto.presentacion.conciliar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import mx.uam.ayd.proyecto.negocio.modelo.DetalleOrdenCompra;
import mx.uam.ayd.proyecto.negocio.modelo.EstadoPartida;
import mx.uam.ayd.proyecto.negocio.modelo.OrdenCompra;
import mx.uam.ayd.proyecto.negocio.modelo.ProductoConciliado;

/**
 * Ventana de conciliación de mercancía.
 * Código compacto, minimalista y muy fácil de explicar.
 */
@Component
public class VentanaConciliar {

    private Stage stage;
    private ControlConciliar control;
    private boolean initialized;

    @FXML
    private VBox vistaListaOrdenes;
    @FXML
    private ScrollPane scrollDetalle;
    @FXML
    private VBox vistaDetalleConciliacion;
    @FXML
    private VBox contenedorOrdenes;
    @FXML
    private Label lblMensajeExito;

    public void setControl(ControlConciliar control) {
        this.control = control;
    }

    private void initializeUI() {
        if (initialized)
            return;
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::initializeUI);
            return;
        }
        try {
            stage = new Stage();
            stage.setTitle("Conciliación de Inventario");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-conciliar.fxml"));
            loader.setController(this);
            stage.setScene(new Scene(loader.load(), 850, 500));
            initialized = true;
        } catch (IOException e) {
            throw new IllegalStateException("Error al abrir ventana conciliar.", e);
        }
    }

    public void muestra(List<OrdenCompra> ordenes) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> muestra(ordenes));
            return;
        }
        initializeUI();
        mostrarVistaLista();
        mostrarOrdenes(ordenes);
        stage.setMaximized(true);
        stage.show();
        stage.toFront();
    }

    public void muestra() {
        muestra(List.of());
    }

    private void mostrarVistaLista() {
        if (scrollDetalle != null)
            scrollDetalle.setVisible(false);
        if (vistaListaOrdenes != null)
            vistaListaOrdenes.setVisible(true);
    }

    private void mostrarOrdenes(List<OrdenCompra> ordenes) {
        contenedorOrdenes.getChildren().clear();
        if (lblMensajeExito != null)
            lblMensajeExito.setVisible(false);

        if (ordenes == null || ordenes.isEmpty()) {
            contenedorOrdenes.getChildren().add(new Label("No hay órdenes de compra entregadas pendientes."));
            return;
        }
        for (OrdenCompra orden : ordenes) {
            contenedorOrdenes.getChildren().add(crearTarjetaOrden(orden));
        }
    }

    private HBox crearTarjetaOrden(OrdenCompra orden) {
        HBox tarjeta = new HBox(15);
        tarjeta.setAlignment(Pos.CENTER_LEFT);
        tarjeta.setPadding(new Insets(10));
        tarjeta.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 4px;");

        Label lblId = new Label("Orden #" + orden.getIdOrdenCompra());
        lblId.setStyle("-fx-font-weight: bold;");
        lblId.setPrefWidth(100);

        String prov = (orden.getProveedor() != null && orden.getProveedor().getRazonSocial() != null)
                ? orden.getProveedor().getRazonSocial()
                : "N/A";
        Label lblProv = new Label("Proveedor: " + prov);
        HBox.setHgrow(lblProv, Priority.ALWAYS);

        Label lblMonto = new Label(String.format("Total: $%,.2f", orden.getMontoTotal()));
        lblMonto.setStyle("-fx-font-weight: bold;");

        Button btnConciliar = new Button("Conciliar");
        btnConciliar.setOnAction(e -> mostrarDetalleConciliacion(orden));

        tarjeta.getChildren().addAll(lblId, lblProv, lblMonto, btnConciliar);
        return tarjeta;
    }

    private void mostrarDetalleConciliacion(OrdenCompra orden) {
        vistaListaOrdenes.setVisible(false);
        scrollDetalle.setVisible(true);
        vistaDetalleConciliacion.getChildren().clear();

        // Header
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        Label lblTitulo = new Label("Conciliación de la Orden #" + orden.getIdOrdenCompra());
        lblTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnVolver = new Button("Volver a Lista");
        Button btnConfirmar = new Button("Confirmar Recepción");
        header.getChildren().addAll(lblTitulo, spacer, btnVolver, btnConfirmar);

        // Info & Resumen
        String prov = (orden.getProveedor() != null && orden.getProveedor().getRazonSocial() != null)
                ? orden.getProveedor().getRazonSocial()
                : "N/A";
        Label lblInfo = new Label("Proveedor: " + prov + "   |   Fecha Entrega: " + orden.getFechaEntrega());

        int totalEsp = orden.getDetalles() != null
                ? orden.getDetalles().stream().mapToInt(DetalleOrdenCompra::getCantidadEsperada).sum()
                : 0;

        Label lblEsperado = new Label("Total Esperado: " + totalEsp);
        Label lblRecibido = new Label("Total Recibido: " + totalEsp);
        Label lblDiscrepancia = new Label("Discrepancia: 0");
        HBox boxResumen = new HBox(20, lblEsperado, lblRecibido, lblDiscrepancia);
        boxResumen.setPadding(new Insets(10));
        boxResumen.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 4px;");

        // Tabla
        VBox tabla = new VBox(5);
        tabla.setStyle("-fx-border-color: #cccccc; -fx-padding: 10; -fx-border-radius: 4px;");

        HBox headerTabla = new HBox(10);
        headerTabla.setAlignment(Pos.CENTER_LEFT);
        headerTabla.setStyle(
                "-fx-font-weight: bold; -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0; -fx-padding: 5;");
        headerTabla.getChildren().addAll(
                crearCelda("SKU", 100, Pos.CENTER_LEFT),
                crearCeldaFlex("Producto"),
                crearCelda("Cant. Esperada", 120, Pos.CENTER),
                crearCelda("Cant. Recibida", 120, Pos.CENTER),
                crearCelda("Diferencia", 100, Pos.CENTER),
                crearCelda("Estado", 100, Pos.CENTER));
        tabla.getChildren().add(headerTabla);

        List<FilaPartida> filas = new ArrayList<>();
        if (orden.getDetalles() != null) {
            for (DetalleOrdenCompra detalle : orden.getDetalles()) {
                HBox fila = new HBox(10);
                fila.setAlignment(Pos.CENTER_LEFT);
                fila.setPadding(new Insets(5));

                Label lblSku = crearCelda(detalle.getProducto() != null ? detalle.getProducto().getSku() : "N/A", 100,
                        Pos.CENTER_LEFT);
                Label lblNom = crearCeldaFlex(detalle.getProducto() != null ? detalle.getProducto().getNombre() : "");
                Label lblEsp = crearCelda(String.valueOf(detalle.getCantidadEsperada()), 120, Pos.CENTER);

                TextField txtRec = new TextField(String.valueOf(detalle.getCantidadEsperada()));
                txtRec.setPrefWidth(60);
                txtRec.setAlignment(Pos.CENTER);

                HBox boxInput = new HBox(txtRec);
                boxInput.setPrefWidth(120);
                boxInput.setMinWidth(120);
                boxInput.setMaxWidth(120);
                boxInput.setAlignment(Pos.CENTER);

                Label lblDif = crearCelda("0", 100, Pos.CENTER);
                Label lblEst = crearCelda("COINCIDE", 100, Pos.CENTER);
                lblEst.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");

                FilaPartida fp = new FilaPartida(detalle, txtRec, lblDif, lblEst);
                filas.add(fp);

                txtRec.textProperty()
                        .addListener((obs, o, n) -> actualizarCalculos(filas, totalEsp, lblRecibido, lblDiscrepancia));

                fila.getChildren().addAll(lblSku, lblNom, lblEsp, boxInput, lblDif, lblEst);
                tabla.getChildren().add(fila);
            }
        }

        actualizarCalculos(filas, totalEsp, lblRecibido, lblDiscrepancia);

        btnVolver.setOnAction(e -> mostrarVistaLista());
        btnConfirmar.setOnAction(e -> {
            try {
                List<ProductoConciliado> conciliados = new ArrayList<>();
                for (FilaPartida fp : filas) {
                    ProductoConciliado pc = new ProductoConciliado();
                    pc.setProducto(fp.detalle.getProducto());
                    pc.setCantidadEsperada(fp.detalle.getCantidadEsperada());
                    pc.setCantidadRecibida(fp.getCantidadRecibida());
                    pc.setDiferencia(fp.getDiferencia());
                    pc.setEstado(fp.getDiferencia() == 0 ? EstadoPartida.entregada : EstadoPartida.pendiente);
                    conciliados.add(pc);
                }
                if (control != null) {
                    control.procesarConciliacion(orden, conciliados);
                    mostrarVistaLista();
                    if (lblMensajeExito != null)
                        lblMensajeExito.setVisible(true);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert err = new Alert(AlertType.ERROR,
                        "Ocurrió un error al procesar la conciliación: " + ex.getMessage());
                if (stage != null)
                    err.initOwner(stage);
                err.showAndWait();
            }
        });

        vistaDetalleConciliacion.getChildren().addAll(header, lblInfo, boxResumen, tabla);
    }

    private Label crearCelda(String texto, double ancho, Pos alineacion) {
        Label l = new Label(texto);
        l.setPrefWidth(ancho);
        l.setMinWidth(ancho);
        l.setMaxWidth(ancho);
        l.setAlignment(alineacion);
        return l;
    }

    private Label crearCeldaFlex(String texto) {
        Label l = new Label(texto);
        HBox.setHgrow(l, Priority.ALWAYS);
        l.setMaxWidth(Double.MAX_VALUE);
        return l;
    }

    private void actualizarCalculos(List<FilaPartida> filas, int totalEsp, Label lblRec, Label lblDisc) {
        int totalRec = 0;
        for (FilaPartida fp : filas) {
            int rec = fp.getCantidadRecibida();
            int esp = fp.detalle.getCantidadEsperada();
            int dif = rec - esp;
            totalRec += rec;
            fp.lblDif.setText(String.valueOf(dif));
            if (dif == 0) {
                fp.lblEst.setText("COINCIDE");
                fp.lblEst.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
            } else if (dif < 0) {
                fp.lblEst.setText("FALTANTE");
                fp.lblEst.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
            } else {
                fp.lblEst.setText("SOBRANTE");
                fp.lblEst.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
            }
        }
        int difTotal = totalRec - totalEsp;
        lblRec.setText("Total Recibido: " + totalRec);
        lblDisc.setText("Discrepancia: " + difTotal);
        if (difTotal == 0) {
            lblDisc.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        } else {
            lblDisc.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        }
    }

    @FXML
    private void handleCerrar() {
        if (stage != null)
            stage.close();
    }

    private static class FilaPartida {
        final DetalleOrdenCompra detalle;
        final TextField txtRecibida;
        final Label lblDif;
        final Label lblEst;

        FilaPartida(DetalleOrdenCompra detalle, TextField txtRecibida, Label lblDif, Label lblEst) {
            this.detalle = detalle;
            this.txtRecibida = txtRecibida;
            this.lblDif = lblDif;
            this.lblEst = lblEst;
        }

        int getCantidadRecibida() {
            try {
                return Integer.parseInt(txtRecibida.getText().trim());
            } catch (Exception e) {
                return 0;
            }
        }

        int getDiferencia() {
            return getCantidadRecibida() - detalle.getCantidadEsperada();
        }
    }
}
