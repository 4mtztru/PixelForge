package mx.uam.ayd.proyecto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import mx.uam.ayd.proyecto.datos.CategoriaRepository;
import mx.uam.ayd.proyecto.datos.RepositorioProductos;
import mx.uam.ayd.proyecto.negocio.ServicioProductos;
import mx.uam.ayd.proyecto.negocio.modelo.Categoria;
import mx.uam.ayd.proyecto.negocio.modelo.Producto;
import mx.uam.ayd.proyecto.presentacion.inventario.ControlInventario;

/**
 * 
 * Clase principal que arranca la aplicación 
 * construida usando el principio de 
 * inversión de control
 * Adaptada para usar JavaFX
 * 
 * @author Humberto Cervantes (c) 21 Nov 2022
 */
@SpringBootApplication
public class ProyectoApplication {

	private final ControlInventario controlInventario;
	private final CategoriaRepository categoriaRepository;
	private final RepositorioProductos repositorioProductos;
	private final ServicioProductos servicioProductos;
	
	@Autowired
	public ProyectoApplication(ControlInventario controlInventario, CategoriaRepository categoriaRepository,
			RepositorioProductos repositorioProductos,
			ServicioProductos servicioProductos) {
		this.controlInventario = controlInventario;
		this.categoriaRepository = categoriaRepository;
		this.repositorioProductos = repositorioProductos;
		this.servicioProductos = servicioProductos;
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
			controlInventario.inicia();
		});
	}
	
	/**
	 * Inicializa la BD con datos
	 */
	public void inicializaBD() {
		inicializaProductos();
	}

	private void inicializaProductos() {
		if (repositorioProductos.count() > 0) {
			return;
		}

		Categoria herramientas = creaCategoria("Herramientas", "Herramientas manuales y eléctricas");
		Categoria plomeria = creaCategoria("Plomería", "Suministros para instalaciones hidráulicas");

		repositorioProductos.save(creaProducto("HM-MT-001", "000000000001", "Martillo de Uña Curva 16 oz",
				185.00, 25, 10, herramientas));
		repositorioProductos.save(creaProducto("HM-MT-002", "000000000002", "Martillo de Bola 24 oz",
				210.00, 12, 10, herramientas));
		repositorioProductos.save(creaProducto("HM-MT-003", "000000000003", "Martillo de Goma 12 oz",
				145.00, 8, 10, herramientas));
		repositorioProductos.save(creaProducto("HM-RT-405", "000000000004", "Rotomartillo Industrial 18V",
				2450.00, 3, 5, herramientas));
		repositorioProductos.save(creaProducto("PL-LL-010", "000000000010", "Llave ajustable 10 pulgadas",
				320.00, 18, 6, plomeria));
	}

	private Categoria creaCategoria(String nombre, String descripcion) {
		Categoria categoria = new Categoria();
		categoria.setNombre(nombre);
		categoria.setDescripcion(descripcion);
		return categoriaRepository.save(categoria);
	}

	private Producto creaProducto(String sku, String codigoBarras, String nombre, double precio,
			int stockActual, int stockMinimo, Categoria categoria) {
		Producto producto = new Producto();
		producto.setSku(sku);
		producto.setCodigoBarras(codigoBarras);
		producto.setNombre(nombre);
		producto.setPrecio(precio);
		producto.setStockActual(stockActual);
		producto.setStockMinimo(stockMinimo);
		producto.setEstadoStock(servicioProductos.calcularEstadoStock(stockActual, stockMinimo));
		producto.setCategoria(categoria);
		return producto;
	}
}
