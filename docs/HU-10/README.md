# HU-10 — Conciliar orden de compra

## Cumplimiento de la base del profesor

La historia sigue el patrón repositorio–servicio–control–ventana:

1. `ControlInventario` delega a `ControlConciliar`.
2. `ControlConciliar`, enlazado con su ventana mediante `@PostConstruct`, solicita únicamente órdenes `entregada`.
3. `ServicioConciliar` ejecuta la operación dentro de una transacción.
4. `RepositorioOrdenCompra`, `RepositorioProductos` y `RepositorioConciliacion` concentran la persistencia.
5. `VentanaConciliar` carga FXML y sólo captura las cantidades recibidas.

## Reglas verificadas

- Sólo una orden entregada puede conciliarse.
- Deben procesarse todas las partidas y cada producto debe pertenecer a la orden.
- La cantidad esperada y el precio unitario siempre proceden de la orden; la interfaz no puede sustituirlos.
- Diferencia, estado de partida, ajuste y saldo final se calculan en negocio.
- El inventario aumenta con la cantidad recibida.
- La conciliación queda completada y la orden cambia a `conciliada`.
- Al terminar se vuelve a consultar la lista de órdenes entregadas.

## Pruebas

- `ServicioConciliarTest`: filtro, validaciones, pertenencia de productos, cálculos, inventario y persistencia.
- `ControlConciliarTest`: consulta de órdenes entregadas y presentación.

Diagrama vigente: `DiagramaSecuenciaHU10.png`.
