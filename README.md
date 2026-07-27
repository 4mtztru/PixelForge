# PixelForge

Aplicación de escritorio JavaFX con Spring Boot, JPA y H2 enfocada exclusivamente en cuatro historias de usuario:

- **HU-01 — Buscar producto:** consulta por nombre, SKU o código de barras y cálculo del estado de stock.
- **HU-04 — Registrar producto:** captura de categoría, proveedor, precios y existencias; genera un código único y valida los datos antes de guardar.
- **HU-09 — Procesar venta:** valida carrito y método de pago, registra la venta y descuenta el inventario.
- **HU-10 — Conciliar orden:** consulta órdenes entregadas, registra cantidades recibidas, calcula diferencias y actualiza inventario y estado de la orden.

## Requisitos

- JDK 17 o superior
- Maven 3.6 o el wrapper incluido

## Ejecutar

```bash
./mvnw spring-boot:run
```

## Probar

```bash
./mvnw test
```

## Estructura funcional

```text
src/main/java/mx/uam/ayd/proyecto/
├── datos/                 Repositorios de productos, proveedores, ventas,
│                          órdenes y conciliaciones
├── negocio/               Servicios de HU-01, HU-04, HU-09 y HU-10
│   └── modelo/            Entidades y enumeraciones requeridas
└── presentacion/
    ├── buscarProducto/    HU-01
    ├── productos/         HU-04
    ├── VentaController    HU-09
    ├── conciliar/         HU-10
    ├── inventario/        Navegación a las cuatro historias
    └── principal/         Entrada de la aplicación
```

Los diagramas de secuencia vigentes están en `docs/HU-01`, `docs/HU-04`, `docs/HU-09` y `docs/HU-10`.
