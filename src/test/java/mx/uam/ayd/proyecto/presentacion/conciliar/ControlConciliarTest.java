package mx.uam.ayd.proyecto.presentacion.conciliar;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.negocio.ServicioConciliar;
import mx.uam.ayd.proyecto.negocio.modelo.EstadoOrden;
import mx.uam.ayd.proyecto.negocio.modelo.OrdenCompra;

/**
 * Pruebas unitarias para el controlador de conciliación (ControlConciliar).
 * Utiliza Mockito para simular la interacción con el servicio y la ventana.
 */
@ExtendWith(MockitoExtension.class)
class ControlConciliarTest {

    // Doble de prueba para el servicio de conciliación
    @Mock
    private ServicioConciliar servicioConciliar;

    // Doble de prueba para la vista de conciliación
    @Mock
    private VentanaConciliar ventana;

    // Objeto bajo prueba con los mocks inyectados
    @InjectMocks
    private ControlConciliar controlConciliar;

    /**
     * Prueba que al iniciar el proceso de conciliación se soliciten únicamente
     * las órdenes de compra con estado 'entregada' y se envíen a la ventana.
     */
    @Test
    void debeIniciarConciliarOrdenSolicitandoEstadoEntregada() {
        // Dado un conjunto de órdenes entregadas
        List<OrdenCompra> ordenes = new ArrayList<>();
        when(servicioConciliar.obtenerOrdenes(EstadoOrden.entregada)).thenReturn(ordenes);

        // Cuando se inicia el control de conciliación
        controlConciliar.iniciaConciliarOrden();

        // Entonces se verifica que se consulte al servicio con el estado correcto y se
        // muestren en la ventana
        verify(servicioConciliar).obtenerOrdenes(EstadoOrden.entregada);
        verify(ventana).muestra(ordenes);
    }
}
