package edu.co.udes.spafeu.web.app.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.task.AuthServiceTask;
import edu.co.udes.spafeu.web.app.util.Messages;

/**
 * <p>Título: Proyecto SPAFEU</p>
 * <p>Descripción: Tests unitarios del controlador REST para operaciones de autenticación.</p>
 *
 * @author AGENT-QA-EXPERT-SPAFEU
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de AuthRestController")
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthRestControllerTest {

    @Mock
    private Messages messages;

    @Mock
    private AuthServiceTask authServiceTask;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private AuthRestController authRestController;

    private UsuarioDTO usuarioDTOValido;

    @BeforeEach
    void setUp() {
        usuarioDTOValido = new UsuarioDTO();
        usuarioDTOValido.setUsername("testuser");
        usuarioDTOValido.setEmail("test@example.com");
        usuarioDTOValido.setPassword("password123");
        usuarioDTOValido.setNombreCompleto("Test User");

        lenient().when(messages.getKey(anyString())).thenReturn("mock");
        when(bindingResult.hasErrors()).thenReturn(false);
    }

    @Nested
    @DisplayName("POST /api/auth/register")
    class RegistrarUsuario {

        @Test
        @DisplayName("Debe registrar usuario exitosamente con datos válidos")
        void registrarUsuarioExitoso() {
            // Arrange
            UsuarioDTO usuarioRegistrado = new UsuarioDTO();
            usuarioRegistrado.setId(1L);
            usuarioRegistrado.setUsername("testuser");
            usuarioRegistrado.setEmail("test@example.com");

            RespuestaServicioDTO<UsuarioDTO> respuesta = new RespuestaServicioDTO<>();
            respuesta.setOk(true);
            respuesta.setNegocio(usuarioRegistrado);
            respuesta.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS,
                    messages.getKey("SUCCESS"), "Registro exitoso"));

            when(authServiceTask.registrarUsuario(usuarioDTOValido))
                    .thenReturn(new ResponseEntity<>(respuesta, HttpStatus.CREATED));

            // Act
            ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> result = authRestController.registrarUsuario(usuarioDTOValido, bindingResult);

            // Assert
            assertNotNull(result);
            assertEquals(HttpStatus.CREATED, result.getStatusCode());
            assertTrue(result.getBody().isOk());
            assertEquals(usuarioRegistrado, result.getBody().getNegocio());
            verify(authServiceTask).registrarUsuario(usuarioDTOValido);
        }

        @Test
        @DisplayName("Debe retornar BAD_REQUEST cuando hay errores de validación")
        void registrarUsuarioValidacionFallida() {
            // Arrange
            when(bindingResult.hasErrors()).thenReturn(true);

            // Act
            ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> result = authRestController.registrarUsuario(usuarioDTOValido, bindingResult);

            // Assert
            assertNotNull(result);
            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertFalse(result.getBody().isOk());
            assertNotNull(result.getBody().getMensajeDTO());
            verify(authServiceTask, never()).registrarUsuario(any());
        }
    }

    @Nested
    @DisplayName("POST /api/auth/forgot-password")
    class ForgotPassword {

        @Test
        @DisplayName("Debe iniciar recuperación de contraseña con email válido")
        void forgotPasswordExitoso() {
            // Arrange
            RespuestaServicioDTO<String> respuesta = new RespuestaServicioDTO<>();
            respuesta.setOk(true);
            respuesta.setNegocio("OK");
            respuesta.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS,
                    messages.getKey("SUCCESS"), "Token enviado"));

            when(authServiceTask.forgotPassword("test@example.com"))
                    .thenReturn(new ResponseEntity<>(respuesta, HttpStatus.OK));

            // Act
            ResponseEntity<RespuestaServicioDTO<String>> result = authRestController.forgotPassword("test@example.com");

            // Assert
            assertNotNull(result);
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertTrue(result.getBody().isOk());
            assertEquals("OK", result.getBody().getNegocio());
            verify(authServiceTask).forgotPassword("test@example.com");
        }

        @Test
        @DisplayName("Debe retornar BAD_REQUEST cuando email está vacío")
        void forgotPasswordEmailVacio() {
            // Act
            ResponseEntity<RespuestaServicioDTO<String>> result = authRestController.forgotPassword("");

            // Assert
            assertNotNull(result);
            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertFalse(result.getBody().isOk());
            verify(authServiceTask, never()).forgotPassword(anyString());
        }

        @Test
        @DisplayName("Debe retornar BAD_REQUEST cuando email es nulo")
        void forgotPasswordEmailNulo() {
            // Act
            ResponseEntity<RespuestaServicioDTO<String>> result = authRestController.forgotPassword(null);

            // Assert
            assertNotNull(result);
            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertFalse(result.getBody().isOk());
            verify(authServiceTask, never()).forgotPassword(anyString());
        }
    }

    @Nested
    @DisplayName("POST /api/auth/reset-password")
    class ResetPassword {

        @Test
        @DisplayName("Debe restablecer contraseña con token y newPassword válidos")
        void resetPasswordExitoso() {
            // Arrange
            RespuestaServicioDTO<String> respuesta = new RespuestaServicioDTO<>();
            respuesta.setOk(true);
            respuesta.setNegocio("auth.password.reset.exitoso");
            respuesta.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS,
                    messages.getKey("SUCCESS"), "Contraseña restablecida"));

            when(authServiceTask.resetPassword("token123", "newPass123"))
                    .thenReturn(new ResponseEntity<>(respuesta, HttpStatus.OK));

            // Act
            ResponseEntity<RespuestaServicioDTO<String>> result = authRestController.resetPassword("token123", "newPass123");

            // Assert
            assertNotNull(result);
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertTrue(result.getBody().isOk());
            assertEquals("auth.password.reset.exitoso", result.getBody().getNegocio());
            verify(authServiceTask).resetPassword("token123", "newPass123");
        }

        @Test
        @DisplayName("Debe retornar BAD_REQUEST cuando token está vacío")
        void resetPasswordTokenVacio() {
            // Act
            ResponseEntity<RespuestaServicioDTO<String>> result = authRestController.resetPassword("", "newPass123");

            // Assert
            assertNotNull(result);
            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertFalse(result.getBody().isOk());
            verify(authServiceTask, never()).resetPassword(anyString(), anyString());
        }

        @Test
        @DisplayName("Debe retornar BAD_REQUEST cuando newPassword está vacío")
        void resetPasswordNewPasswordVacio() {
            // Act
            ResponseEntity<RespuestaServicioDTO<String>> result = authRestController.resetPassword("token123", "");

            // Assert
            assertNotNull(result);
            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertFalse(result.getBody().isOk());
            verify(authServiceTask, never()).resetPassword(anyString(), anyString());
        }

        @Test
        @DisplayName("Debe retornar BAD_REQUEST cuando token y newPassword están vacíos")
        void resetPasswordAmbosVacios() {
            // Act
            ResponseEntity<RespuestaServicioDTO<String>> result = authRestController.resetPassword("", "");

            // Assert
            assertNotNull(result);
            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertFalse(result.getBody().isOk());
            verify(authServiceTask, never()).resetPassword(anyString(), anyString());
        }
    }
}
