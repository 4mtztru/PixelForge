package mx.uam.ayd.proyecto.presentacion.conciliar;

import jakarta.annotation.PostConstruct;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.negocio.ServicioConciliar;
import mx.uam.ayd.proyecto.negocio.modelo.Conciliacion;
import mx.uam.ayd.proyecto.negocio.modelo.EstadoOrden;
import mx.uam.ayd.proyecto.negocio.modelo.OrdenCompra;
import mx.uam.ayd.proyecto.negocio.modelo.ProductoConciliado;

/**
 * Controlador para la historia de usuario de Conciliación de Órdenes de Compra.
 * Conecta la interfaz gráfica (VentanaConciliar) con la capa de negocio (ServicioConciliar).
 */
@Component
public class ControlConciliar {

    private final ServicioConciliar servicioConciliar;
    private final VentanaConciliar ventana;

    @Autowired
    public ControlConciliar(ServicioConciliar servicioConciliar, VentanaConciliar ventana) {
        this.servicioConciliar = servicioConciliar;
        this.ventana = ventana;
    }

    /**
     * Establece la relación entre el controlador y la ventana al arrancar la aplicación.
     */
    @PostConstruct
    public void init() {
        ventana.setControl(this);
    }

    /**
     * Inicia la historia de usuario recuperando las órdenes entregadas
     * y desplegándolas en la ventana.
     */
    public void iniciaConciliarOrden() {
        // Obtenemos del negocio las órdenes de compra que ya fueron entregadas
        List<OrdenCompra> ordenesCompra = servicioConciliar.obtenerOrdenes(EstadoOrden.entregada);
        // Las mostramos en la vista
        ventana.muestra(ordenesCompra);
    }

    /**
     * Recibe los datos de la recepción de mercancía y los envía a la capa de negocio para procesar la conciliación.
     * 
     * @param orden Orden de compra seleccionada
     * @param productosConciliados Lista con el detalle de cantidades físicas recibidas
     * @return Registro de conciliación guardado
     */
    public Conciliacion procesarConciliacion(OrdenCompra orden, List<ProductoConciliado> productosConciliados) {
        // Solicitamos al servicio conciliar la orden y actualizar existencias
        Conciliacion conciliacion = servicioConciliar.conciliarOrden(orden, productosConciliados);
        // Volvemos a cargar las órdenes pendientes de conciliar
        iniciaConciliarOrden();
        return conciliacion;
    }
}
