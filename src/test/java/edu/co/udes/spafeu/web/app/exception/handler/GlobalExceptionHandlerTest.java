package edu.co.udes.spafeu.web.app.exception.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

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

import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil;
import edu.co.udes.spafeu.web.app.util.Messages;

/**
 * <p>Título: Proyecto SPAFEU</p>
 * <p>Descripción: Tests unitarios para GlobalExceptionHandler —
 * manejo de SpafeuException (error de negocio → 400 BAD_REQUEST)
 * y Exception genérica (error inesperado → 500 INTERNAL_SERVER_ERROR).</p>
 *
 * @author AGENT-QA-EXPERT-SPAFEU
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de GlobalExceptionHandler")
@MockitoSettings(strictness = Strictness.LENIENT)
class GlobalExceptionHandlerTest {

    @Mock
    private Messages messages;

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        lenient().when(messages.getKey(anyString())).thenReturn("mensaje-mock");
    }

    @Nested
    @DisplayName("handleSoft() — SpafeuException")
    class HandleSoft {

        @Test
        @DisplayName("Debe retornar 400 BAD_REQUEST con estructura de error")
        void debeRetornar400BadRequest() {
            SpafeuException ex = new SpafeuException("error.usuario.no.encontrado");
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_ERROR_LABEL)).thenReturn("Error");
            when(messages.getKey("error.usuario.no.encontrado")).thenReturn("Usuario no encontrado");

            ResponseEntity<RespuestaServicioDTO<Object>> response =
                    exceptionHandler.handleSoft(ex);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

            RespuestaServicioDTO<Object> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isFalse();

            MensajeDTO mensaje = body.getMensajeDTO();
            assertThat(mensaje).isNotNull();
            assertThat(mensaje.getSeverity()).isEqualTo(SeverityEnum.ERROR);
            assertThat(mensaje.getSummary()).isEqualTo("Error");
            assertThat(mensaje.getDetail()).isEqualTo("Usuario no encontrado");
        }

        @Test
        @DisplayName("Debe buscar la clave del mensaje usando Messages.getKey()")
        void debeBuscarMensajeEnProperties() {
            SpafeuException ex = new SpafeuException("error.salud.ya.existe");
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_ERROR_LABEL)).thenReturn("Error");
            when(messages.getKey("error.salud.ya.existe")).thenReturn("El perfil de salud ya existe");

            ResponseEntity<RespuestaServicioDTO<Object>> response =
                    exceptionHandler.handleSoft(ex);

            MensajeDTO mensaje = response.getBody().getMensajeDTO();
            assertThat(mensaje.getDetail()).isEqualTo("El perfil de salud ya existe");
        }

        @Test
        @DisplayName("Debe incluir el mensaje de la excepción usando ex.getMessage() como key")
        void debeUsarExGetMessageComoKey() {
            SpafeuException ex = new SpafeuException("error.custom.key");
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_ERROR_LABEL)).thenReturn("Error");
            when(messages.getKey("error.custom.key")).thenReturn("Mensaje personalizado");

            exceptionHandler.handleSoft(ex);

            verify(messages).getKey("error.custom.key");
        }
    }

    @Nested
    @DisplayName("handleGeneric() — Exception genérica")
    class HandleGeneric {

        @Test
        @DisplayName("Debe retornar 500 INTERNAL_SERVER_ERROR con estructura de error")
        void debeRetornar500InternalServerError() {
            Exception ex = new RuntimeException("Error inesperado en el sistema");
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_ERROR_LABEL)).thenReturn("Error");
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_ERROR_GENERICO))
                    .thenReturn("Ocurrió un error, código {1}");

            ResponseEntity<RespuestaServicioDTO<Object>> response =
                    exceptionHandler.handleGeneric(ex);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

            RespuestaServicioDTO<Object> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isFalse();

            MensajeDTO mensaje = body.getMensajeDTO();
            assertThat(mensaje).isNotNull();
            assertThat(mensaje.getSeverity()).isEqualTo(SeverityEnum.ERROR);
            assertThat(mensaje.getSummary()).isEqualTo("Error");
            assertThat(mensaje.getDetail()).contains("Ocurrió un error, código ");
        }

        @Test
        @DisplayName("Debe incluir un UUID en el detalle del mensaje")
        void debeIncluirUUID() {
            Exception ex = new RuntimeException("Error de prueba");
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_ERROR_LABEL)).thenReturn("Error");
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_ERROR_GENERICO))
                    .thenReturn("Código {1}");

            ResponseEntity<RespuestaServicioDTO<Object>> response =
                    exceptionHandler.handleGeneric(ex);

            String detalle = response.getBody().getMensajeDTO().getDetail();
            assertThat(detalle).startsWith("Código ");

            String uuidStr = detalle.replace("Código ", "");
            assertThat(uuidStr).isNotEmpty();
            assertDoesNotThrow(() -> UUID.fromString(uuidStr));
        }

        @Test
        @DisplayName("Debe reemplazar {1} por un UUID en el mensaje")
        void debeReemplazarPlaceholderPorUUID() {
            Exception ex = new RuntimeException("Error crítico");
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_ERROR_LABEL)).thenReturn("Error");
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_ERROR_GENERICO))
                    .thenReturn("Use el código {1} para soporte");

            ResponseEntity<RespuestaServicioDTO<Object>> response =
                    exceptionHandler.handleGeneric(ex);

            String detalle = response.getBody().getMensajeDTO().getDetail();
            assertThat(detalle).matches("Use el código [a-f0-9\\-]+ para soporte");
            assertThat(detalle).doesNotContain("{1}");
        }

        @Test
        @DisplayName("Debe responder incluso cuando el mensaje de error es null")
        void debeResponderCuandoMensajeNull() {
            Exception ex = new NullPointerException();
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_ERROR_LABEL)).thenReturn("Error");
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_ERROR_GENERICO))
                    .thenReturn("Código {1}");

            ResponseEntity<RespuestaServicioDTO<Object>> response =
                    exceptionHandler.handleGeneric(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(response.getBody()).isNotNull();
        }
    }

}
