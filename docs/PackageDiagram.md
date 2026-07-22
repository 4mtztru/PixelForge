# Diagrama de paquetes: estructura del proyecto

Este diagrama resume las capas y dependencias de la consulta de usuarios.

```mermaid
flowchart TB
    subgraph main["mx.uam.ayd.proyecto"]
        ProyectoApplication["ProyectoApplication"]
    end

    subgraph presentacion["mx.uam.ayd.proyecto.presentacion"]
        subgraph inventario["inventario"]
            VistaInventario["VistaInventario"]
            ControlInventario["ControlInventario"]
        end

        subgraph listarUsuarios["listarUsuarios"]
            VentanaListarUsuarios["VentanaListarUsuarios"]
            ControlListarUsuarios["ControlListarUsuarios"]
        end
    end

    subgraph negocio["mx.uam.ayd.proyecto.negocio"]
        ServicioUsuario["ServicioUsuario"]

        subgraph modelo["modelo"]
            Usuario["Usuario"]
        end
    end

    subgraph datos["mx.uam.ayd.proyecto.datos"]
        UsuarioRepository["UsuarioRepository"]
    end

    ProyectoApplication --> ControlInventario
    ControlInventario --> VistaInventario
    ControlInventario --> ControlListarUsuarios

    ControlListarUsuarios --> VentanaListarUsuarios
    ControlListarUsuarios --> ServicioUsuario
    ServicioUsuario --> UsuarioRepository
    ServicioUsuario --> Usuario
```

## Responsabilidades principales

### Presentación

- `ControlInventario` coordina la vista inicial y el acceso a la consulta de usuarios.
- `ControlListarUsuarios` y `VentanaListarUsuarios` presentan el identificador, nombre y rol de cada usuario.

### Negocio

- `ServicioUsuario` recupera los usuarios registrados.
- `Usuario` conserva únicamente su identificador, nombre, rol y conciliaciones.

### Datos

- `UsuarioRepository` proporciona las operaciones CRUD de usuarios.

La aplicación mantiene la separación entre presentación, lógica de negocio, modelo y persistencia mediante inyección de dependencias de Spring.
