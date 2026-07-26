package mx.uam.ayd.proyecto.negocio;

import mx.uam.ayd.proyecto.modelo.Venta;
import org.springframework.stereotype.Service;

@Service
public class VentaService {

    public boolean procesarPago(Venta venta) {
        if (!venta.isProductosEnCarrito()) {
            throw new IllegalArgumentException("El carrito de compras está vacío.");
        }

        if ("Efectivo".equalsIgnoreCase(venta.getMetodoPago()) && venta.getMontoRecibido() < venta.getTotal()) {
            throw new IllegalArgumentException("El monto recibido es insuficiente.");
        }

        if ("Transferencia".equalsIgnoreCase(venta.getMetodoPago()) && 
            (venta.getReferenciaTransferencia() == null || venta.getReferenciaTransferencia().trim().isEmpty())) {
            throw new IllegalArgumentException("La referencia bancaria es obligatoria.");
        }

        return true;
    }
}
