package mx.uam.ayd.proyecto.presentacion;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import mx.uam.ayd.proyecto.modelo.Venta;
import mx.uam.ayd.proyecto.negocio.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @FXML
    private TextField txtMontoRecibido;

    @FXML
    public void procesarCobro() {
        try {
            Venta venta = new Venta();
            venta.setProductosEnCarrito(true);
            venta.setMetodoPago("Efectivo");
            venta.setTotal(1560.20);
            venta.setMontoRecibido(Double.parseDouble(txtMontoRecibido.getText()));

            ventaService.procesarPago(venta);
            System.out.println("Transacción exitosa: Venta registrada.");
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
