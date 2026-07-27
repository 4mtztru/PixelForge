package mx.uam.ayd.proyecto.presentacion.inventario;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import mx.uam.ayd.proyecto.presentacion.VentaController;
import mx.uam.ayd.proyecto.presentacion.buscarProducto.ControlBuscarProducto;
import mx.uam.ayd.proyecto.presentacion.conciliar.ControlConciliar;
import mx.uam.ayd.proyecto.presentacion.productos.ControlRegistroProducto;

/**
 * Controlador del módulo de Inventario.
 * Coordina la vista principal de inventario y la navegación hacia los submódulos de búsqueda y conciliación.
 */
@Component
public class ControlInventario {
    private final ControlBuscarProducto controlBuscarProducto;
    private final ControlConciliar controlConciliar;
    private final ControlRegistroProducto controlRegistroProducto;
    private final VentaController ventaController;
    private final VentanaInventario ventana;

    @Autowired
    public ControlInventario(
            ControlBuscarProducto controlBuscarProducto,
            ControlConciliar controlConciliar,
            ControlRegistroProducto controlRegistroProducto,
            VentaController ventaController,
            VentanaInventario ventana) {
        this.controlBuscarProducto = controlBuscarProducto;
        this.controlConciliar = controlConciliar;
        this.controlRegistroProducto = controlRegistroProducto;
        this.ventaController = ventaController;
        this.ventana = ventana;
    }

    /**
     * Conecta este controlador con su correspondiente ventana tras instanciar el componente.
     */
    @PostConstruct
    public void init() {
        ventana.setControl(this);
    }

    /**
     * Muestra la ventana de inventario al usuario.
     */
    public void inicia() {
        ventana.mostrarInventario();
    }

    /**
     * Solicita la apertura de la funcionalidad de búsqueda de productos.
     */
    public void solicitarProductos() {
        controlBuscarProducto.iniciarProductos();
    }

    /**
     * Solicita la apertura de la funcionalidad de conciliación de órdenes.
     */
    public void solicitarConciliacion() {
        controlConciliar.iniciaConciliarOrden();
    }

    /**
     * Solicita la apertura del formulario de registro aportado por el repositorio remoto.
     */
    public void solicitarRegistroProducto() {
        controlRegistroProducto.inicia();
    }

    /**
     * Solicita la apertura del punto de venta aportado por el repositorio remoto.
     */
    public void solicitarPuntoVenta() {
        ventaController.inicia();
    }
}
