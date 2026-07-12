package edu.co.udes.spafeu.web.app.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.task.UsuarioServiceTask;
import edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil;
import edu.co.udes.spafeu.web.app.util.Messages;
import lombok.extern.log4j.Log4j2;

/**
 * <p>Título: Proyecto SPAFEU</p>
 * <p>Descripción: Tests unitarios del controlador REST para operaciones
 * de perfil y validación de usuarios — validación de entrada y delegación
 * a UsuarioServiceTask.</p>
 *
 * @author AGENT-QA-EXPERT-SPAFEU
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de UsuarioRestController")
@Log4j2
@MockitoSettings(strictness = Strictness.LENIENT)
class UsuarioRestControllerTest {

    @Mock
    private Messages messages;

    @Mock
    private UsuarioServiceTask userServiceTask;

    @InjectMocks
    private UsuarioRestController usuarioRestController;

    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setUsername("testuser");
        usuarioDTO.setEmail("test@udes.edu.co");
        usuarioDTO.setNombreCompleto("Test User");

        lenient().when(messages.getKey(anyString())).thenReturn("mensaje-mock");
    }

    @Nested
    @DisplayName("GET /api/users/isemail/{email}")
    class ExisteCorreo {

        @Test
        @DisplayName("Debe retornar 200 OK cuando el email es válido")
        void debeRetornar200OkCuandoEmailValido() {
            ResponseEntity<RespuestaServicioDTO<Boolean>> respuestaEsperada =
                    new ResponseEntity<>(new RespuestaServicioDTO<>(), HttpStatus.OK);
            when(userServiceTask.existeCorreo("test@udes.edu.co")).thenReturn(respuestaEsperada);

            ResponseEntity<RespuestaServicioDTO<Boolean>> response =
                    usuarioRestController.existeCorreo("test@udes.edu.co");

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(userServiceTask, times(1)).existeCorreo("test@udes.edu.co");
            log.info("Test existeCorreo: retorna 200 OK para email válido");
        }

        @Test
        @DisplayName("Debe retornar 400 BAD_REQUEST cuando el email está vacío")
        void debeRetornar400CuandoEmailVacio() {
            when(messages.getKey(SeverityEnum.ERROR.getKey())).thenReturn("error");
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_ERROR_PETICION_GENERICO))
                    .thenReturn("Error en la petición");

            ResponseEntity<RespuestaServicioDTO<Boolean>> response =
                    usuarioRestController.existeCorreo("");

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

            RespuestaServicioDTO<Boolean> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isFalse();
            assertThat(body.getNegocio()).isFalse();

            MensajeDTO mensaje = body.getMensajeDTO();
            assertThat(mensaje).isNotNull();
            assertThat(mensaje.getSeverity()).isEqualTo(SeverityEnum.ERROR);

            verify(userServiceTask, never()).existeCorreo(anyString());
            log.info("Test existeCorreo: retorna 400 BAD_REQUEST para email vacío");
        }

        @Test
        @DisplayName("Debe retornar 400 BAD_REQUEST cuando el email es solo espacios")
        void debeRetornar400CuandoEmailSoloEspacios() {
            ResponseEntity<RespuestaServicioDTO<Boolean>> response =
                    usuarioRestController.existeCorreo("   ");

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            verify(userServiceTask, never()).existeCorreo(anyString());
            log.info("Test existeCorreo: retorna 400 BAD_REQUEST para email con espacios");
        }
    }

    @Nested
    @DisplayName("GET /api/users/isusername/{username}")
    class ExisteUsername {

        @Test
        @DisplayName("Debe retornar 200 OK cuando el username es válido")
        void debeRetornar200OkCuandoUsernameValido() {
            ResponseEntity<RespuestaServicioDTO<Boolean>> respuestaEsperada =
                    new ResponseEntity<>(new RespuestaServicioDTO<>(), HttpStatus.OK);
            when(userServiceTask.existeUsername("testuser")).thenReturn(respuestaEsperada);

            ResponseEntity<RespuestaServicioDTO<Boolean>> response =
                    usuarioRestController.existeUsername("testuser");

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(userServiceTask, times(1)).existeUsername("testuser");
            log.info("Test existeUsername: retorna 200 OK para username válido");
        }

        @Test
        @DisplayName("Debe retornar 400 BAD_REQUEST cuando el username está vacío")
        void debeRetornar400CuandoUsernameVacio() {
            when(messages.getKey(SeverityEnum.ERROR.getKey())).thenReturn("error");
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_ERROR_PETICION_GENERICO))
                    .thenReturn("Error en la petición");

            ResponseEntity<RespuestaServicioDTO<Boolean>> response =
                    usuarioRestController.existeUsername("");

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

            RespuestaServicioDTO<Boolean> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isFalse();
            assertThat(body.getNegocio()).isFalse();

            MensajeDTO mensaje = body.getMensajeDTO();
            assertThat(mensaje).isNotNull();
            assertThat(mensaje.getSeverity()).isEqualTo(SeverityEnum.ERROR);

            verify(userServiceTask, never()).existeUsername(anyString());
            log.info("Test existeUsername: retorna 400 BAD_REQUEST para username vacío");
        }

        @Test
        @DisplayName("Debe retornar 400 BAD_REQUEST cuando el username es solo espacios")
        void debeRetornar400CuandoUsernameSoloEspacios() {
            ResponseEntity<RespuestaServicioDTO<Boolean>> response =
                    usuarioRestController.existeUsername("   ");

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            verify(userServiceTask, never()).existeUsername(anyString());
            log.info("Test existeUsername: retorna 400 BAD_REQUEST para username con espacios");
        }
    }

    @Nested
    @DisplayName("GET /api/users/recurso/{currentUserId}/{nombreRecurso}")
    class ObtenerImagenPerfil {

        @Mock
        private Resource resourceMock;

        @Test
        @DisplayName("Debe retornar 200 OK con el recurso y cabecera Content-Disposition")
        void debeRetornar200OkConRecurso() {
            when(resourceMock.getFilename()).thenReturn("foto.jpg");
            when(userServiceTask.obtenerImagenPerfil("foto.jpg", 1L)).thenReturn(resourceMock);

            ResponseEntity<Resource> response =
                    usuarioRestController.obtenerImagenPerfil("foto.jpg", 1L);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(resourceMock);

            HttpHeaders headers = response.getHeaders();
            assertThat(headers).isNotNull();
            assertThat(headers.get(HttpHeaders.CONTENT_DISPOSITION))
                    .contains("attachment; filename=\"foto.jpg\"");

            verify(userServiceTask, times(1)).obtenerImagenPerfil("foto.jpg", 1L);
            log.info("Test obtenerImagenPerfil: retorna 200 OK con recurso y Content-Disposition");
        }

        @Test
        @DisplayName("Debe incluir el nombre del recurso en la cabecera Content-Disposition")
        void debeIncluirNombreEnContentDisposition() {
            when(resourceMock.getFilename()).thenReturn("mi-foto-perfil.png");
            when(userServiceTask.obtenerImagenPerfil("mi-foto-perfil.png", 5L))
                    .thenReturn(resourceMock);

            ResponseEntity<Resource> response =
                    usuarioRestController.obtenerImagenPerfil("mi-foto-perfil.png", 5L);

            assertThat(response.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION))
                    .contains("attachment; filename=\"mi-foto-perfil.png\"");
            log.info("Test obtenerImagenPerfil: verifica nombre en Content-Disposition");
        }
    }

    @Nested
    @DisplayName("POST /api/users/upload")
    class UploadImg {

        @Mock
        private MultipartFile multipartFile;

        @Test
        @DisplayName("Debe retornar 200 OK cuando el archivo y el id son válidos")
        void debeRetornar200OkCuandoArchivoValido() {
            when(multipartFile.isEmpty()).thenReturn(false);

            ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> respuestaEsperada =
                    new ResponseEntity<>(new RespuestaServicioDTO<>(), HttpStatus.OK);
            when(userServiceTask.cargarImg(multipartFile, 1L)).thenReturn(respuestaEsperada);

            ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> response =
                    usuarioRestController.uploadImg(multipartFile, 1L);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(userServiceTask, times(1)).cargarImg(multipartFile, 1L);
            log.info("Test uploadImg: retorna 200 OK para archivo válido");
        }

        @Test
        @DisplayName("Debe retornar 400 BAD_REQUEST cuando el archivo está vacío")
        void debeRetornar400CuandoArchivoVacio() {
            when(multipartFile.isEmpty()).thenReturn(true);
            when(messages.getKey(SeverityEnum.ERROR.getKey())).thenReturn("error");
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_ERROR_PETICION_GENERICO))
                    .thenReturn("Error en la petición");

            ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> response =
                    usuarioRestController.uploadImg(multipartFile, 1L);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

            RespuestaServicioDTO<UsuarioDTO> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isFalse();

            verify(userServiceTask, never()).cargarImg(any(), anyLong());
            log.info("Test uploadImg: retorna 400 BAD_REQUEST para archivo vacío");
        }

        @Test
        @DisplayName("Debe retornar 400 BAD_REQUEST cuando el id es null")
        void debeRetornar400CuandoIdNull() {
            when(multipartFile.isEmpty()).thenReturn(false);
            when(messages.getKey(SeverityEnum.ERROR.getKey())).thenReturn("error");
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_ERROR_PETICION_GENERICO))
                    .thenReturn("Error en la petición");

            ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> response =
                    usuarioRestController.uploadImg(multipartFile, null);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

            RespuestaServicioDTO<UsuarioDTO> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isFalse();

            verify(userServiceTask, never()).cargarImg(any(), any());
            log.info("Test uploadImg: retorna 400 BAD_REQUEST para id null");
        }
    }
}
