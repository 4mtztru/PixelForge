package mx.uam.ayd.proyecto.presentacion.productos;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.datos.CategoriaRepository;
import mx.uam.ayd.proyecto.negocio.ServicioProductos;
import mx.uam.ayd.proyecto.negocio.modelo.Categoria;
import mx.uam.ayd.proyecto.negocio.modelo.Producto;
import mx.uam.ayd.proyecto.negocio.modelo.Proveedor;

/**
 * Controlador de la vista de registro de producto (HU-04).
 */
@Component
public class ControlRegistroProducto {

	private final ServicioProductos servicioProductos;
	private final CategoriaRepository categoriaRepository;
	private final VistaRegistroProducto vistaRegistroProducto;

	@Autowired
	public ControlRegistroProducto(ServicioProductos servicioProductos,
			CategoriaRepository categoriaRepository,
			VistaRegistroProducto vistaRegistroProducto) {
		this.servicioProductos = servicioProductos;
		this.categoriaRepository = categoriaRepository;
		this.vistaRegistroProducto = vistaRegistroProducto;
	}

	@PostConstruct
	public void init() {
		vistaRegistroProducto.setControlRegistroProducto(this);
	}

	public void inicia() {
		List<Proveedor> proveedores = servicioProductos.obtenerProveedoresActivos();
		List<Categoria> categorias = new ArrayList<>();
		categoriaRepository.findAll().forEach(categorias::add);

		vistaRegistroProducto.mostrarFormularioRegistro(proveedores, categorias);
	}

	public String generarCodigo(int categoriaId) {
		return servicioProductos.generarCodigoUnico(categoriaId);
	}

	public void validarYGuardarProducto(Producto producto, double precioCompra, boolean confirmacionPrecioMenor) {
		try {
			if (producto.getNombre() == null || producto.getNombre().isBlank()) {
				vistaRegistroProducto.mostrarError("Error: El nombre del producto es obligatorio.");
				return;
			}
			if (producto.getCategoria() == null) {
				vistaRegistroProducto.mostrarError("Error: Debe seleccionar una categoría.");
				return;
			}
			if (precioCompra < 0 || producto.getPrecio() < 0 || producto.getStockActual() < 0) {
				vistaRegistroProducto.mostrarError("Error: Precios o stock no pueden ser valores negativos.");
				return;
			}

			// Validar Venta < Precio Compra
			if (producto.getPrecio() < precioCompra && !confirmacionPrecioMenor) {
				vistaRegistroProducto.solicitarConfirmacionPrecioMenor(
					"Advertencia: El precio de venta ($" + producto.getPrecio() + 
					") es menor al precio de compra ($" + precioCompra + "). ¿Desea continuar?");
				return;
			}

			servicioProductos.registrarProducto(producto);
			vistaRegistroProducto.mostrarExito("Producto registrado exitosamente con código " + producto.getSku());

		} catch (Exception ex) {
			vistaRegistroProducto.mostrarError("Error al registrar producto: " + ex.getMessage());
		}
	}

	public void termina() {
		vistaRegistroProducto.ocultar();
	}
}
