# HU-01 — Buscar producto

## Cumplimiento de la base del profesor

La historia conserva el patrón de capas de `otrebmuh/AnalysisYDiseno`:

1. `VentanaInventario` delega la navegación a `ControlInventario`.
2. `ControlBuscarProducto` se enlaza con `VentanaBuscarProducto` mediante `@PostConstruct`.
3. `ControlBuscarProducto` valida que exista exactamente un criterio.
4. `ServicioBuscarProducto` aplica validaciones y calcula el estado del stock.
5. `RepositorioProductos` encapsula las consultas JPA.
6. `VentanaBuscarProducto` carga su interfaz desde FXML.

## Reglas verificadas

- Se acepta nombre, SKU o código de barras, nunca más de uno simultáneamente.
- Nombre admite coincidencia parcial sin distinguir mayúsculas.
- SKU y código de barras se consultan de forma exacta.
- Un valor vacío no consulta el repositorio.
- Stock actual menor o igual al mínimo se clasifica como `critico`.
- Los resultados y errores vuelven a la ventana a través del controlador.

## Pruebas

- `ServicioBuscarProductoTest`: validaciones, criterios y estado de stock.
- `ControlBuscarProductoTest`: exclusividad de campos y delegación.
- `RepositorioProductosTest`: consultas reales con JPA y H2.

Diagrama vigente: `DiagramaSecuenciaHU01.png`.
