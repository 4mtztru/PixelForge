package mx.uam.ayd.proyecto;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Clase base abstracta para pruebas de integración de Spring Boot sin dependencia directa del entorno JavaFX.
 * Configura el perfil activo "test".
 */
@SpringBootTest
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {
}