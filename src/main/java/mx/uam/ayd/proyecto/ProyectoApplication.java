package mx.uam.ayd.proyecto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.time.LocalDate;

import mx.uam.ayd.proyecto.datos.RepositorioOrdenCompra;
import mx.uam.ayd.proyecto.datos.ProveedorRepository;
import mx.uam.ayd.proyecto.datos.RepositorioProductos;
import mx.uam.ayd.proyecto.negocio.modelo.Categoria;
import mx.uam.ayd.proyecto.negocio.modelo.DetalleOrdenCompra;
import mx.uam.ayd.proyecto.negocio.modelo.EstadoOrden;
import mx.uam.ayd.proyecto.negocio.modelo.OrdenCompra;
import mx.uam.ayd.proyecto.negocio.modelo.Producto;
import mx.uam.ayd.proyecto.negocio.modelo.Proveedor;
import mx.uam.ayd.proyecto.presentacion.principal.ControlPrincipal;

/**
 * Clase principal que arranca la aplicación Spring Boot integrada con JavaFX.
 * Aplica el principio de inversión de control (IoC) e inicializa la base de datos
 * con información de prueba para la ejecución de las historias de usuario.
 */
@SpringBootApplication
public class ProyectoApplication {

	private final RepositorioProductos repositorioProductos;
	private final ControlPrincipal controlPrincipal;
	private final RepositorioOrdenCompra ordenCompraRepository;
	private final ProveedorRepository proveedorRepository;

	@Autowired
	public ProyectoApplication(ControlPrincipal controlPrincipal,
			RepositorioProductos repositorioProductos,
			RepositorioOrdenCompra ordenCompraRepository, ProveedorRepository proveedorRepository) {
		this.controlPrincipal = controlPrincipal;
		this.repositorioProductos = repositorioProductos;
		this.ordenCompraRepository = ordenCompraRepository;
		this.proveedorRepository = proveedorRepository;
	}

	/**
	 * Método principal
	 *
	 * @param args argumentos de la línea de comando
	 */
	public static void main(String[] args) {
		// Launch JavaFX application
		Application.launch(JavaFXApplication.class, args);
	}

	/**
	 * Clase interna para manejar la inicialización de JavaFX
	 */
	public static class JavaFXApplication extends Application {

		private static ConfigurableApplicationContext applicationContext;

		@Override
		public void init() throws Exception {
			// Create Spring application context
			SpringApplicationBuilder builder = new SpringApplicationBuilder(ProyectoApplication.class);
			builder.headless(false);
			applicationContext = builder.run(getParameters().getRaw().toArray(new String[0]));
		}

		@Override
		public void start(Stage primaryStage) {
			// Initialize the application on the JavaFX thread
			Platform.runLater(() -> {
				applicationContext.getBean(ProyectoApplication.class).inicia();
			});
		}

		@Override
		public void stop() throws Exception {
			applicationContext.close();
			Platform.exit();
		}
	}

	/**
	 * Metodo que arranca la aplicacion
	 * inicializa la bd y arranca el controlador
	 */
	public void inicia() {
		inicializaBD();

		// Make sure controllers are created on JavaFX thread
		Platform.runLater(() -> {
			controlPrincipal.inicia();
		});
	}

	/**
	 * Inicializa la BD con datos
	 */
	public void inicializaBD() {
		if (repositorioProductos.count() > 0 || ordenCompraRepository.count() > 0) {
			return;
		}

		Proveedor proveedor = new Proveedor();
		proveedor.setRazonSocial("Ferretera Nacional S.A. de C.V.");
		proveedor.setRfc("FNA900101ABC");
		proveedor.setContacto("contacto@ferreteranacional.com");
		proveedorRepository.save(proveedor);

		// creamos productos
		Producto martillo = new Producto();
		martillo.setNombre("Martillo");
		martillo.setSku("MAR-001");
		martillo.setCodigoBarras("123456789");
		martillo.setPrecio(150.0);
		martillo.setPrecioCompra(110.0);
		martillo.setStockActual(20);
		martillo.setStockMinimo(5);
		martillo.setCategoria(Categoria.HERRAMIENTAS_MANUALES);
		martillo.setProveedor(proveedor);
		repositorioProductos.save(martillo);

		Producto cintaMetrica = new Producto();
		cintaMetrica.setNombre("Cinta métrica");
		cintaMetrica.setSku("CIN-001");
		cintaMetrica.setCodigoBarras("987654321");
		cintaMetrica.setPrecio(85.0);
		cintaMetrica.setPrecioCompra(60.0);
		cintaMetrica.setStockActual(3);
		cintaMetrica.setStockMinimo(5);
		cintaMetrica.setCategoria(Categoria.MEDICION_Y_PRUEBA);
		cintaMetrica.setProveedor(proveedor);
		repositorioProductos.save(cintaMetrica);

		// Producto nuevo sin stock previo en inventario (stockActual = 0)
		Producto destornillador = new Producto();
		destornillador.setNombre("Destornillador Cruz");
		destornillador.setSku("DES-001");
		destornillador.setCodigoBarras("112233445");
		destornillador.setPrecio(45.0);
		destornillador.setPrecioCompra(30.0);
		destornillador.setStockActual(0);
		destornillador.setStockMinimo(5);
		destornillador.setCategoria(Categoria.HERRAMIENTAS_MANUALES);
		destornillador.setProveedor(proveedor);
		repositorioProductos.save(destornillador);

		// Crear Orden de Compra entregada (lista para conciliación)
		OrdenCompra ordenCompra = new OrdenCompra();
		ordenCompra.setFolio("OC-2026-001");
		ordenCompra.setFechaEmision(LocalDate.now().minusDays(5));
		ordenCompra.setFechaEntrega(LocalDate.now().minusDays(2));
		ordenCompra.setEstado(EstadoOrden.entregada);
		ordenCompra.setProveedor(proveedor);
		ordenCompra.setAnticipoPagado(500.0);

		// Detalles de la Orden de Compra
		DetalleOrdenCompra detalle1 = new DetalleOrdenCompra();
		detalle1.setProducto(cintaMetrica);
		detalle1.setCantidadEsperada(10);
		detalle1.setPrecioUnitario(cintaMetrica.getPrecio());
		detalle1.setSubtotal(10 * cintaMetrica.getPrecio());
		detalle1.setOrdenCompra(ordenCompra);

		DetalleOrdenCompra detalle2 = new DetalleOrdenCompra();
		detalle2.setProducto(martillo);
		detalle2.setCantidadEsperada(5);
		detalle2.setPrecioUnitario(martillo.getPrecio());
		detalle2.setSubtotal(5 * martillo.getPrecio());
		detalle2.setOrdenCompra(ordenCompra);

		ordenCompra.getDetalles().add(detalle1);
		ordenCompra.getDetalles().add(detalle2);
		ordenCompra.setMontoTotal(detalle1.getSubtotal() + detalle2.getSubtotal());

		ordenCompraRepository.save(ordenCompra);

		// Crear segunda Orden de Compra entregada (lista para conciliación)
		OrdenCompra ordenCompra2 = new OrdenCompra();
		ordenCompra2.setFolio("OC-2026-002");
		ordenCompra2.setFechaEmision(LocalDate.now().minusDays(3));
		ordenCompra2.setFechaEntrega(LocalDate.now());
		ordenCompra2.setEstado(EstadoOrden.entregada);
		ordenCompra2.setProveedor(proveedor);
		ordenCompra2.setAnticipoPagado(400.0);

		// Detalles de la segunda Orden de Compra (surtirá stock del producto nuevo)
		DetalleOrdenCompra detalle3 = new DetalleOrdenCompra();
		detalle3.setProducto(destornillador);
		detalle3.setCantidadEsperada(15);
		detalle3.setPrecioUnitario(destornillador.getPrecio());
		detalle3.setSubtotal(15 * destornillador.getPrecio());
		detalle3.setOrdenCompra(ordenCompra2);

		ordenCompra2.getDetalles().add(detalle3);
		ordenCompra2.setMontoTotal(detalle3.getSubtotal());

		ordenCompraRepository.save(ordenCompra2);
	}
}
