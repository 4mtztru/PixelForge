package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private double total;
    private double montoRecibido;
    private String metodoPago;
    private String referenciaTransferencia;
    private boolean productosEnCarrito;

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public double getMontoRecibido() { return montoRecibido; }
    public void setMontoRecibido(double montoRecibido) { this.montoRecibido = montoRecibido; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getReferenciaTransferencia() { return referenciaTransferencia; }
    public void setReferenciaTransferencia(String referenciaTransferencia) { this.referenciaTransferencia = referenciaTransferencia; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public boolean isProductosEnCarrito() { return productosEnCarrito; }
    public void setProductosEnCarrito(boolean productosEnCarrito) { this.productosEnCarrito = productosEnCarrito; }
}
