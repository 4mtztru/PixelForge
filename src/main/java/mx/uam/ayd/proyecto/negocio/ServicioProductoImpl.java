import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Clase que implementa la interfaz del servicio de productos.
 * Contiene la lógica de negocio para el registro, validación y control
 * de inventario inicial correspondientes a la HU-04.
 */
public class ServicioProductoImpl implements ServicioProducto {

    // Simulación de una base de datos local o repositorio en memoria para el ejemplo
    private List<Producto> baseDatosProductos = new ArrayList<>();

    /**
     * Registra un nuevo producto en el sistema validando las reglas de negocio
     * de la HU-04 (campos obligatorios, precios válidos y código único).
     * 
     * @param producto Objeto que contiene la información del producto a registrar.
     * @return true si el registro fue exitoso, false en caso contrario.
     * @throws IllegalArgumentException si algún precio de compra o venta es negativo.
     */
    @Override
    public boolean registrarProducto(Producto producto) {
        // 1. Validar campos obligatorios (Escenario 05 y 16)
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            System.err.println("Error: Debe ingresar el nombre del producto.");
            return false;
        }

        // 2. Validar precios negativos (Escenario 07 y 08)
        if (producto.getPrecioCompra() < 0) {
            throw new IllegalArgumentException("El precio de compra no puede ser menor a cero.");
        }
        if (producto.getPrecioVenta() < 0) {
            throw new IllegalArgumentException("El precio de venta no puede ser negativo.");
        }

        // 3. Validar existencias negativas (Escenario 09)
        if (producto.getExistencias() < 0) {
            System.err.println("Error: La cantidad inicial no puede ser menor a cero.");
            return false;
        }

        // 4. Generar código único automáticamente (Escenario 02)
        String codigoGenerado = "PROD-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        producto.setCodigo(codigoGenerado);

        // 5. Validar productos duplicados (Escenario 13)
        for (Producto p : baseDatosProductos) {
            if (p.getNombre().equalsIgnoreCase(producto.getNombre())) {
                System.err.println("Error: El producto ya se encuentra registrado.");
                return false;
            }
        }

        // 6. Guardar en la "base de datos"
        baseDatosProductos.add(producto);
        System.out.println("Producto registrado correctamente con código: " + codigoGenerado);
        return true;
    }
}
