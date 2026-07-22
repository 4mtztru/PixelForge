package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.UsuarioRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Usuario;

@ExtendWith(MockitoExtension.class)
class ServicioUsuarioTest {

	@Mock
	private UsuarioRepository usuarioRepository;

	@InjectMocks
	private ServicioUsuario servicioUsuario;

	@Test
	void recuperaUsuariosVaciosOExistentes() {
		assertEquals(List.of(), servicioUsuario.recuperaUsuarios());

		Usuario usuario1 = new Usuario();
		usuario1.setNombre("Juan");
		Usuario usuario2 = new Usuario();
		usuario2.setNombre("María");
		when(usuarioRepository.findAll()).thenReturn(List.of(usuario1, usuario2));

		assertEquals(List.of(usuario1, usuario2), servicioUsuario.recuperaUsuarios());
	}
}
