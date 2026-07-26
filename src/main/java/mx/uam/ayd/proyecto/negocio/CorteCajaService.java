package mx.uam.ayd.proyecto.negocio;

import mx.uam.ayd.proyecto.modelo.CorteCaja;
import org.springframework.stereotype.Service;

@Service
public class CorteCajaService {

    public CorteCaja finalizarCorte(CorteCaja corte) {
        double diferencia = corte.getEfectivoEsperado() - corte.getEfectivoFisico();
        
        boolean hayDiscrepancia = (diferencia != 0);
        boolean sinJustificacion = (corte.getJustificacion() == null || corte.getJustificacion().trim().isEmpty());

        if (hayDiscrepancia && sinJustificacion) {
            throw new IllegalArgumentException("Se requiere una justificación obligatoria por la diferencia de efectivo.");
        }
        
        return corte;
    }
}
