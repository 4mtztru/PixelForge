package mx.uam.ayd.proyecto.presentacion.buscarProducto;

import jakarta.annotation.PostConstruct;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.negocio.ServicioBuscarProducto;
import mx.uam.ayd.proyecto.negocio.modelo.Producto;

/**
 * Controlador para la historia de usuario Buscar Producto.
 * Valida los criterios de búsqueda (un solo campo con datos), invoca al servicio de negocio y pasa los resultados a la vista.
 */
@Component
public class ControlBuscarProducto {
    public static final String MENSAJE_SIN_DATO = "No se ingresó dato alguno.";
    public static final String MENSAJE_UN_SOLO_CAMPO = "Solo es posible realizar la búsqueda con uno de los tres campos.";

    private final ServicioBuscarProducto servicioBuscarProducto;
    private final VentanaBuscarProducto ventana;

    @Autowired
    public ControlBuscarProducto(
            ServicioBuscarProducto servicioBuscarProducto,
            VentanaBuscarProducto ventana) {
        this.servicioBuscarProducto = servicioBuscarProducto;
        this.ventana = ventana;
    }

    /**
     * Vincula el controlador con la ventana al iniciar el componente de Spring.
     */
    @PostConstruct
    public void init() {
        ventana.setControl(this);
    }

    /**
     * Muestra la vista inicial de búsqueda de productos.
     */
    public void iniciarProductos() {
        ventana.mostrarVistaProductos();
    }

    /**
     * Ejecuta la búsqueda invocando al servicio de negocio y envía el resultado a la ventana.
     * 
     * @param criterio Campo seleccionado (nombre, sku o codigoBarras)
     * @param valor Texto buscado por el usuario
     */
    public void solicitarBusqueda(String criterio, String valor) {
        try {
            List<Producto> productos = servicioBuscarProducto.buscarProducto(criterio, valor);
            ventana.mostrarResultados(productos, criterio, valor.trim());
        } catch (IllegalArgumentException exception) {
            ventana.mostrarError(exception.getMessage());
        }
    }

    /**
     * Valida los campos ingresados por el usuario y determina el criterio de búsqueda a ejecutar.
     * Regla de negocio: Debe ingresarse exactamente 1 campo (Nombre, SKU o Código de Barras).
     * 
     * @param nombre Nombre del producto ingresado
     * @param sku SKU del producto ingresado
     * @param codigoBarras Código de barras ingresado
     */
    public void solicitarBusqueda(String nombre, String sku, String codigoBarras) {
        String nombreLimpio = limpiar(nombre);
        String skuLimpio = limpiar(sku);
        String codigoLimpio = limpiar(codigoBarras);

        int camposConDatos = contarCamposConDatos(nombreLimpio, skuLimpio, codigoLimpio);
        if (camposConDatos == 0) {
            ventana.mostrarError(MENSAJE_SIN_DATO);
            return;
        }
        if (camposConDatos > 1) {
            ventana.mostrarError(MENSAJE_UN_SOLO_CAMPO);
            return;
        }

        if (!nombreLimpio.isEmpty()) {
            solicitarBusqueda(ServicioBuscarProducto.CRITERIO_NOMBRE, nombreLimpio);
        } else if (!skuLimpio.isEmpty()) {
            solicitarBusqueda(ServicioBuscarProducto.CRITERIO_SKU, skuLimpio);
        } else {
            solicitarBusqueda(ServicioBuscarProducto.CRITERIO_CODIGO_BARRAS, codigoLimpio);
        }
    }

    private String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private int contarCamposConDatos(String... valores) {
        int total = 0;
        for (String valor : valores) {
            if (!valor.isEmpty()) {
                total++;
            }
        }
        return total;
    }
}
