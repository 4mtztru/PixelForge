package mx.uam.ayd.proyecto.presentacion.inventario;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import mx.uam.ayd.proyecto.presentacion.buscarProducto.ControlBuscarProducto;
import mx.uam.ayd.proyecto.presentacion.conciliar.ControlConciliar;
import mx.uam.ayd.proyecto.presentacion.productos.ControlRegistroProducto;
import mx.uam.ayd.proyecto.presentacion.VentaController;

/**
 * Pruebas unitarias para el controlador del módulo de inventario (ControlInventario).
 * Verifica la correcta delegación de navegación hacia subcontroladores y la ventana.
 */
class ControlInventarioTest {

    /**
     * Prueba que al iniciar el control de inventario se registre la referencia en la ventana y se ordene mostrarla.
     */
    @Test
    void debeMostrarLaVentanaIntermediaDeInventario() {
        ControlBuscarProducto controlBuscarProducto = Mockito.mock(ControlBuscarProducto.class);
        ControlConciliar controlConciliar = Mockito.mock(ControlConciliar.class);
        ControlRegistroProducto controlRegistro = Mockito.mock(ControlRegistroProducto.class);
        VentaController ventaController = Mockito.mock(VentaController.class);
        VentanaInventario ventanaInventario = Mockito.mock(VentanaInventario.class);
        ControlInventario controlInventario =
                new ControlInventario(controlBuscarProducto, controlConciliar,
                        controlRegistro, ventaController, ventanaInventario);
        controlInventario.init();

        controlInventario.inicia();

        verify(ventanaInventario).setControl(controlInventario);
        verify(ventanaInventario).mostrarInventario();
    }

    /**
     * Prueba que al solicitar productos se delegue la acción al controlador de búsqueda de productos.
     */
    @Test
    void debeAbrirBuscarProductoCuandoSeSolicitaDesdeElBoton() {
        ControlBuscarProducto controlBuscarProducto = Mockito.mock(ControlBuscarProducto.class);
        ControlConciliar controlConciliar = Mockito.mock(ControlConciliar.class);
        ControlRegistroProducto controlRegistro = Mockito.mock(ControlRegistroProducto.class);
        VentaController ventaController = Mockito.mock(VentaController.class);
        VentanaInventario ventanaInventario = Mockito.mock(VentanaInventario.class);
        ControlInventario controlInventario =
                new ControlInventario(controlBuscarProducto, controlConciliar,
                        controlRegistro, ventaController, ventanaInventario);

        controlInventario.solicitarProductos();

        verify(controlBuscarProducto).iniciarProductos();
    }

    /**
     * Prueba que al solicitar conciliación se delegue la acción al controlador de conciliación de órdenes.
     */
    @Test
    void debeAbrirConciliarCuandoSeSolicitaDesdeElBoton() {
        ControlBuscarProducto controlBuscarProducto = Mockito.mock(ControlBuscarProducto.class);
        ControlConciliar controlConciliar = Mockito.mock(ControlConciliar.class);
        ControlRegistroProducto controlRegistro = Mockito.mock(ControlRegistroProducto.class);
        VentaController ventaController = Mockito.mock(VentaController.class);
        VentanaInventario ventanaInventario = Mockito.mock(VentanaInventario.class);
        ControlInventario controlInventario =
                new ControlInventario(controlBuscarProducto, controlConciliar,
                        controlRegistro, ventaController, ventanaInventario);

        controlInventario.solicitarConciliacion();

        verify(controlConciliar).iniciaConciliarOrden();
    }

    @Test
    void debeAbrirRegistroDeProducto() {
        ControlBuscarProducto busqueda = Mockito.mock(ControlBuscarProducto.class);
        ControlConciliar conciliacion = Mockito.mock(ControlConciliar.class);
        ControlRegistroProducto registro = Mockito.mock(ControlRegistroProducto.class);
        VentaController venta = Mockito.mock(VentaController.class);
        ControlInventario control = new ControlInventario(
                busqueda, conciliacion, registro, venta, Mockito.mock(VentanaInventario.class));

        control.solicitarRegistroProducto();

        verify(registro).inicia();
    }

    @Test
    void debeAbrirPuntoDeVenta() {
        ControlBuscarProducto busqueda = Mockito.mock(ControlBuscarProducto.class);
        ControlConciliar conciliacion = Mockito.mock(ControlConciliar.class);
        ControlRegistroProducto registro = Mockito.mock(ControlRegistroProducto.class);
        VentaController venta = Mockito.mock(VentaController.class);
        ControlInventario control = new ControlInventario(
                busqueda, conciliacion, registro, venta, Mockito.mock(VentanaInventario.class));

        control.solicitarPuntoVenta();

        verify(venta).inicia();
    }
}
