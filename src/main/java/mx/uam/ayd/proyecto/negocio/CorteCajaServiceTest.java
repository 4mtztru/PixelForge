package mx.uam.ayd.proyecto.negocio;

import mx.uam.ayd.proyecto.modelo.CorteCaja;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CorteCajaServiceTest {

    private final CorteCajaService corteService = new CorteCajaService();

    @Test
    public void testDiferenciaEnCajaSinJustificacion() {
        CorteCaja corte = new CorteCaja();
        corte.setEfectivoEsperado(10250.00);
        corte.setEfectivoFisico(10000.00);
        corte.setJustificacion("");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            corteService.finalizarCorte(corte);
        });

        assertEquals("Se requiere una justificación obligatoria por la diferencia de efectivo.", exception.getMessage());
    }
}
