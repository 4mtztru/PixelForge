package mx.uam.ayd.proyecto.presentacion.inventario;

import java.io.IOException;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Ventana del módulo de Inventario.
 * Permite al usuario acceder a las opciones de búsqueda de productos y conciliación de órdenes.
 */
@Component
public class VentanaInventario {
    private Stage stage;
    private ControlInventario control;
    private boolean initialized;

    /**
     * Inicializa los componentes FXML de la interfaz de usuario en el hilo de JavaFX.
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
            stage.setTitle("Inventario - PixelForge");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-inventario.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load(), 500, 350);
            stage.setScene(scene);

            initialized = true;
        } catch (IOException exception) {
            throw new IllegalStateException("No fue posible abrir la ventana de inventario.", exception);
        }
    }

    public void setControl(ControlInventario control) {
        this.control = control;
    }

    public void mostrarInventario() {
        muestra();
    }

    /**
     * Muestra la ventana en pantalla completa o maximizada.
     */
    public void muestra() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::muestra);
            return;
        }

        initializeUI();
        stage.setMaximized(true);
        stage.show();
        stage.toFront();
    }

    public void solicitarProductos() {
        if (control != null) {
            control.solicitarProductos();
        }
    }

    public void solicitarConciliacion() {
        if (control != null) {
            control.solicitarConciliacion();
        }
    }

    public void solicitarRegistroProducto() {
        if (control != null) {
            control.solicitarRegistroProducto();
        }
    }

    public void solicitarPuntoVenta() {
        if (control != null) {
            control.solicitarPuntoVenta();
        }
    }

    @FXML
    private void handleBuscarProducto() {
        solicitarProductos();
    }

    @FXML
    private void handleConciliar() {
        solicitarConciliacion();
    }

    @FXML
    private void handleRegistrarProducto() {
        solicitarRegistroProducto();
    }

    @FXML
    private void handlePuntoVenta() {
        solicitarPuntoVenta();
    }

    @FXML
    private void handleCerrar() {
        if (stage != null) {
            stage.close();
        }
    }
}
