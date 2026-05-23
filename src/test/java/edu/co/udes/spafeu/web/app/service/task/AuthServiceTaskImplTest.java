package edu.co.udes.spafeu.web.app.service.task;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.entity.AuthService;
import edu.co.udes.spafeu.web.app.service.task.impl.AuthServiceTaskImpl;
import edu.co.udes.spafeu.web.app.util.Messages;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Tests unitarios para AuthServiceTaskImpl.</p>
 *
 * @author AGENT-QA-EXPERT-SPAFEU
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de AuthServiceTaskImpl")
class AuthServiceTaskImplTest {

	@Mock
	private Messages messages;

	@Mock
	private AuthService authService;

	@InjectMocks
	private AuthServiceTaskImpl authServiceTask;

	private UsuarioDTO usuarioDTO;
	private static final String USERNAME = "testuser";
	private static final String EMAIL = "test@example.com";

	@BeforeEach
	void setUp() {
		usuarioDTO = new UsuarioDTO();
		usuarioDTO.setId(1L);
		usuarioDTO.setUsername(USERNAME);
		usuarioDTO.setEmail(EMAIL);

		when(messages.getKey(anyString())).thenReturn("mensaje.mocked");
	}

	@Nested
	@DisplayName("registrarUsuario()")
	class RegistrarUsuario {

		@Test
		@DisplayName("Debe registrar usuario exitosamente y retornar 201 CREATED")
		void debeRegistrarUsuarioExitosamente() {
			when(authService.registrarUsuario(any(UsuarioDTO.class))).thenReturn(usuarioDTO);

			ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> response = authServiceTask.registrarUsuario(usuarioDTO);

			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
			assertThat(response.getBody()).isNotNull();
			assertThat(response.getBody().isOk()).isTrue();
			assertThat(response.getBody().getNegocio()).isEqualTo(usuarioDTO);

			MensajeDTO mensaje = response.getBody().getMensajeDTO();
			assertThat(mensaje).isNotNull();
			assertThat(mensaje.getSeverity()).isEqualTo(SeverityEnum.SUCCESS);
			assertThat(mensaje.getSummary()).isEqualTo("mensaje.mocked");
			assertThat(mensaje.getDetail()).isEqualTo("mensaje.mocked".replace("{1}", USERNAME));

			verify(authService).registrarUsuario(usuarioDTO);
		}
	}

	@Nested
	@DisplayName("forgotPassword()")
	class ForgotPassword {

		@Test
		@DisplayName("Debe procesar recuperación de contraseña exitosamente y retornar 200 OK")
		void debeProcesarForgotPasswordExitosamente() {
			doNothing().when(authService).forgotPassword(EMAIL);

			ResponseEntity<RespuestaServicioDTO<String>> response = authServiceTask.forgotPassword(EMAIL);

			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
			assertThat(response.getBody()).isNotNull();
			assertThat(response.getBody().isOk()).isTrue();
			assertThat(response.getBody().getNegocio()).isEqualTo("OK");

			MensajeDTO mensaje = response.getBody().getMensajeDTO();
			assertThat(mensaje).isNotNull();
			assertThat(mensaje.getSeverity()).isEqualTo(SeverityEnum.SUCCESS);
			assertThat(mensaje.getSummary()).isEqualTo("mensaje.mocked");
			assertThat(mensaje.getDetail()).isEqualTo("mensaje.mocked");

			verify(authService).forgotPassword(EMAIL);
		}
	}

	@Nested
	@DisplayName("resetPassword()")
	class ResetPassword {

		@Test
		@DisplayName("Debe resetear contraseña exitosamente y retornar 200 OK")
		void debeResetearPasswordExitosamente() {
			String token = "token-reset";
			String newPassword = "newPassword123";
			doNothing().when(authService).resetPassword(token, newPassword);

			ResponseEntity<RespuestaServicioDTO<String>> response = authServiceTask.resetPassword(token, newPassword);

			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
			assertThat(response.getBody()).isNotNull();
			assertThat(response.getBody().isOk()).isTrue();
			assertThat(response.getBody().getNegocio()).isEqualTo("auth.password.reset.exitoso");

			MensajeDTO mensaje = response.getBody().getMensajeDTO();
			assertThat(mensaje).isNotNull();
			assertThat(mensaje.getSeverity()).isEqualTo(SeverityEnum.SUCCESS);
			assertThat(mensaje.getSummary()).isEqualTo("mensaje.mocked");
			assertThat(mensaje.getDetail()).isEqualTo("mensaje.mocked");

			verify(authService).resetPassword(token, newPassword);
		}
	}
}
