package mx.uam.ayd.proyecto.presentacion.buscarProducto;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.negocio.ServicioBuscarProducto;
import mx.uam.ayd.proyecto.negocio.modelo.Producto;

/**
 * Pruebas unitarias para el controlador de búsqueda de productos (ControlBuscarProducto).
 * Valida la lógica de negocio de la UI sobre la exclusividad de campos de búsqueda.
 */
@ExtendWith(MockitoExtension.class)
class ControlBuscarProductoTest {

    @Mock
    private ServicioBuscarProducto servicioBuscarProducto;

    @Mock
    private VentanaBuscarProducto ventana;

    @InjectMocks
    private ControlBuscarProducto control;

    /**
     * Prueba que se muestre un mensaje de error cuando todos los campos de búsqueda están vacíos.
     */
    @Test
    void debeIndicarCuandoLosTresCamposEstanVacios() {
        control.solicitarBusqueda(" ", null, "");

        verify(ventana).mostrarError(ControlBuscarProducto.MENSAJE_SIN_DATO);
        verify(servicioBuscarProducto, never()).buscarProducto(
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString());
    }

    /**
     * Prueba que no se permita realizar la búsqueda si el usuario ingresa datos en más de un campo.
     */
    @Test
    void debeImpedirLaBusquedaConMasDeUnCampo() {
        control.solicitarBusqueda("Martillo", "MAR-001", "");

        verify(ventana).mostrarError(ControlBuscarProducto.MENSAJE_UN_SOLO_CAMPO);
        verify(servicioBuscarProducto, never()).buscarProducto(
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString());
    }

    /**
     * Prueba el flujo exitoso de búsqueda cuando se ingresa únicamente un parámetro (SKU).
     */
    @Test
    void debeBuscarAlPresionarEnterEnUnSoloCampo() {
        Producto producto = new Producto();
        List<Producto> resultados = List.of(producto);
        when(servicioBuscarProducto.buscarProducto(
                ServicioBuscarProducto.CRITERIO_SKU, "MAR-001"))
                .thenReturn(resultados);

        control.solicitarBusqueda("", "  MAR-001  ", "");

        verify(servicioBuscarProducto).buscarProducto(
                ServicioBuscarProducto.CRITERIO_SKU, "MAR-001");
        verify(ventana).mostrarResultados(
                resultados, ServicioBuscarProducto.CRITERIO_SKU, "MAR-001");
    }

    /**
     * Prueba que las excepciones lanzadas por el servicio sean capturadas y enviadas a la ventana como error.
     */
    @Test
    void debeMostrarComoErrorUnaSolicitudInvalidaDelServicio() {
        when(servicioBuscarProducto.buscarProducto("precio", "100"))
                .thenThrow(new IllegalArgumentException("El criterio de búsqueda no es válido."));

        control.solicitarBusqueda("precio", "100");

        verify(ventana).mostrarError("El criterio de búsqueda no es válido.");
    }
}
