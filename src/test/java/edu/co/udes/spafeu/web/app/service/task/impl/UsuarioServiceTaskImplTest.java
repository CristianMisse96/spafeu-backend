package edu.co.udes.spafeu.web.app.service.task.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.entity.UserService;
import edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil;
import edu.co.udes.spafeu.web.app.util.Messages;
import lombok.extern.log4j.Log4j2;

/**
 * <p>Título: Proyecto SPAFEU</p>
 * <p>Descripción: Tests unitarios para UsuarioServiceTaskImpl -
 * validación de existencia (correo/username), consulta de usuario,
 * obtención de imagen de perfil y carga de imagen.</p>
 *
 * @author AGENT-QA-EXPERT-SPAFEU
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de UsuarioServiceTaskImpl")
@Log4j2
class UsuarioServiceTaskImplTest {

    @Mock
    private Messages messages;

    @Mock
    private UserService userService;

    @InjectMocks
    private UsuarioServiceTaskImpl usuarioServiceTask;

    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setEmail("test@udes.edu.co");
        usuarioDTO.setUsername("testuser");
        usuarioDTO.setNombreCompleto("Test User");
    }

    @Nested
    @DisplayName("existeCorreo()")
    class ExisteCorreo {

        @Test
        @DisplayName("Debe retornar 200 OK con ok=true y negocio=true cuando el correo existe")
        void debeRetornarOkTrueCuandoCorreoExiste() {
            when(userService.validarCorreo("test@udes.edu.co")).thenReturn(true);
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_SUCCESS)).thenReturn("Éxito");
            when(messages.getKey(MensajesKeyConstantesUtil.SUCCESS_DETALLE)).thenReturn("Operación exitosa");

            ResponseEntity<RespuestaServicioDTO<Boolean>> response =
                    usuarioServiceTask.existeCorreo("test@udes.edu.co");

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            RespuestaServicioDTO<Boolean> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isTrue();
            assertThat(body.getNegocio()).isTrue();

            MensajeDTO mensaje = body.getMensajeDTO();
            assertThat(mensaje).isNotNull();
            assertThat(mensaje.getSeverity()).isEqualTo(SeverityEnum.SUCCESS);
            assertThat(mensaje.getSummary()).isEqualTo("Éxito");
            assertThat(mensaje.getDetail()).isEqualTo("Operación exitosa");

            verify(userService, times(1)).validarCorreo("test@udes.edu.co");
            log.info("Test existeCorreo con existencia: retorna 200 OK con negocio=true");
        }

        @Test
        @DisplayName("Debe retornar 200 OK con ok=true y negocio=false cuando el correo NO existe")
        void debeRetornarOkFalseCuandoCorreoNoExiste() {
            when(userService.validarCorreo("noexiste@test.com")).thenReturn(false);
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_SUCCESS)).thenReturn("Éxito");
            when(messages.getKey(MensajesKeyConstantesUtil.SUCCESS_DETALLE)).thenReturn("Operación exitosa");

            ResponseEntity<RespuestaServicioDTO<Boolean>> response =
                    usuarioServiceTask.existeCorreo("noexiste@test.com");

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            RespuestaServicioDTO<Boolean> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isFalse();
            assertThat(body.getNegocio()).isFalse();

            verify(userService, times(1)).validarCorreo("noexiste@test.com");
            log.info("Test existeCorreo sin existencia: retorna 200 OK con negocio=false");
        }

        @Test
        @DisplayName("Debe construir MensajeDTO con SeverityEnum.SUCCESS")
        void debeConstruirMensajeDtoConSuccess() {
            when(userService.validarCorreo(anyString())).thenReturn(true);
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_SUCCESS)).thenReturn("success");
            when(messages.getKey(MensajesKeyConstantesUtil.SUCCESS_DETALLE)).thenReturn("success.detalle");

            ResponseEntity<RespuestaServicioDTO<Boolean>> response =
                    usuarioServiceTask.existeCorreo("test@udes.edu.co");

            MensajeDTO mensaje = response.getBody().getMensajeDTO();
            assertThat(mensaje.getSeverity()).isEqualTo(SeverityEnum.SUCCESS);
            assertThat(mensaje.getSummary()).isEqualTo("success");
            assertThat(mensaje.getDetail()).isEqualTo("success.detalle");
            log.info("Test existeCorreo mensaje: severity=SUCCESS");
        }
    }

    @Nested
    @DisplayName("existeUsername()")
    class ExisteUsername {

        @Test
        @DisplayName("Debe retornar 200 OK con ok=true y negocio=true cuando el username existe")
        void debeRetornarOkTrueCuandoUsernameExiste() {
            when(userService.validarUsername("testuser")).thenReturn(true);
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_SUCCESS)).thenReturn("Éxito");
            when(messages.getKey(MensajesKeyConstantesUtil.SUCCESS_DETALLE)).thenReturn("Operación exitosa");

            ResponseEntity<RespuestaServicioDTO<Boolean>> response =
                    usuarioServiceTask.existeUsername("testuser");

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            RespuestaServicioDTO<Boolean> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isTrue();
            assertThat(body.getNegocio()).isTrue();

            MensajeDTO mensaje = body.getMensajeDTO();
            assertThat(mensaje).isNotNull();
            assertThat(mensaje.getSeverity()).isEqualTo(SeverityEnum.SUCCESS);

            verify(userService, times(1)).validarUsername("testuser");
            log.info("Test existeUsername con existencia: retorna 200 OK con negocio=true");
        }

        @Test
        @DisplayName("Debe retornar 200 OK con ok=false y negocio=false cuando el username NO existe")
        void debeRetornarOkFalseCuandoUsernameNoExiste() {
            when(userService.validarUsername("unknown")).thenReturn(false);
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_SUCCESS)).thenReturn("Éxito");
            when(messages.getKey(MensajesKeyConstantesUtil.SUCCESS_DETALLE)).thenReturn("Operación exitosa");

            ResponseEntity<RespuestaServicioDTO<Boolean>> response =
                    usuarioServiceTask.existeUsername("unknown");

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            RespuestaServicioDTO<Boolean> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isFalse();
            assertThat(body.getNegocio()).isFalse();

            verify(userService, times(1)).validarUsername("unknown");
            log.info("Test existeUsername sin existencia: retorna 200 OK con negocio=false");
        }

        @Test
        @DisplayName("Debe construir MensajeDTO con SeverityEnum.SUCCESS")
        void debeConstruirMensajeDtoConSuccess() {
            when(userService.validarUsername(anyString())).thenReturn(true);
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_SUCCESS)).thenReturn("success");
            when(messages.getKey(MensajesKeyConstantesUtil.SUCCESS_DETALLE)).thenReturn("success.detalle");

            ResponseEntity<RespuestaServicioDTO<Boolean>> response =
                    usuarioServiceTask.existeUsername("testuser");

            MensajeDTO mensaje = response.getBody().getMensajeDTO();
            assertThat(mensaje.getSeverity()).isEqualTo(SeverityEnum.SUCCESS);
            log.info("Test existeUsername mensaje: severity=SUCCESS");
        }
    }

    @Nested
    @DisplayName("findUser()")
    class FindUser {

        @Test
        @DisplayName("Debe retornar 200 OK con UsuarioDTO cuando el usuario existe")
        void debeRetornarUsuarioCuandoExiste() {
            when(userService.buscarUsuario(1L)).thenReturn(usuarioDTO);
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_SUCCESS)).thenReturn("Éxito");
            when(messages.getKey(MensajesKeyConstantesUtil.SUCCESS_DETALLE)).thenReturn("Operación exitosa");

            ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> response =
                    usuarioServiceTask.findUser(1L);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            RespuestaServicioDTO<UsuarioDTO> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isTrue();
            assertThat(body.getNegocio()).isEqualTo(usuarioDTO);
            assertThat(body.getNegocio().getId()).isEqualTo(1L);
            assertThat(body.getNegocio().getEmail()).isEqualTo("test@udes.edu.co");

            MensajeDTO mensaje = body.getMensajeDTO();
            assertThat(mensaje).isNotNull();
            assertThat(mensaje.getSeverity()).isEqualTo(SeverityEnum.SUCCESS);

            verify(userService, times(1)).buscarUsuario(1L);
            log.info("Test findUser: retorna 200 OK con datos del usuario");
        }

        @Test
        @DisplayName("Debe llamar a userService.buscarUsuario con el userId correcto")
        void debeLlamarServiceConUserIdCorrecto() {
            when(userService.buscarUsuario(99L)).thenReturn(usuarioDTO);
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_SUCCESS)).thenReturn("Éxito");
            when(messages.getKey(MensajesKeyConstantesUtil.SUCCESS_DETALLE)).thenReturn("Operación exitosa");

            usuarioServiceTask.findUser(99L);

            verify(userService, times(1)).buscarUsuario(99L);
            log.info("Test findUser: verifica llamado a buscarUsuario con userId=99");
        }

        @Test
        @DisplayName("Debe construir MensajeDTO con SeverityEnum.SUCCESS")
        void debeConstruirMensajeDtoConSuccess() {
            when(userService.buscarUsuario(anyLong())).thenReturn(usuarioDTO);
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_SUCCESS)).thenReturn("success");
            when(messages.getKey(MensajesKeyConstantesUtil.SUCCESS_DETALLE)).thenReturn("success.detalle");

            ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> response =
                    usuarioServiceTask.findUser(1L);

            MensajeDTO mensaje = response.getBody().getMensajeDTO();
            assertThat(mensaje.getSeverity()).isEqualTo(SeverityEnum.SUCCESS);
            assertThat(mensaje.getSummary()).isEqualTo("success");
            assertThat(mensaje.getDetail()).isEqualTo("success.detalle");
            log.info("Test findUser mensaje: severity=SUCCESS");
        }
    }

    @Nested
    @DisplayName("obtenerImagenPerfil()")
    class ObtenerImagenPerfil {

        @Mock
        private Resource resourceMock;

        @Test
        @DisplayName("Debe retornar Resource cuando el archivo existe y es legible")
        void debeRetornarResourceCuandoArchivoExiste() throws Exception {
            when(userService.obtenerImagenPerfil("foto.jpg", 1L)).thenReturn(resourceMock);
            when(resourceMock.exists()).thenReturn(true);
            when(resourceMock.isReadable()).thenReturn(true);

            Resource resultado = usuarioServiceTask.obtenerImagenPerfil("foto.jpg", 1L);

            assertThat(resultado).isNotNull();
            assertThat(resultado.exists()).isTrue();
            assertThat(resultado.isReadable()).isTrue();

            verify(userService, times(1)).obtenerImagenPerfil("foto.jpg", 1L);
            log.info("Test obtenerImagenPerfil: retorna Resource cuando el archivo existe");
        }

        @Test
        @DisplayName("Debe manejar Resource cuando el archivo no existe (fallback a no-usuario.png)")
        void debeManejarCuandoArchivoNoExiste() throws Exception {
            when(userService.obtenerImagenPerfil("no-existe.jpg", 1L)).thenReturn(resourceMock);
            when(resourceMock.exists()).thenReturn(false);
            when(resourceMock.isReadable()).thenReturn(false);

            Resource resultado = usuarioServiceTask.obtenerImagenPerfil("no-existe.jpg", 1L);

            assertThat(resultado).isNotNull();
            verify(userService, times(1)).obtenerImagenPerfil("no-existe.jpg", 1L);
            log.info("Test obtenerImagenPerfil sin existencia: retorna Resource fallback (UrlResource)");
        }

        @Test
        @DisplayName("Debe retornar null cuando el service lanza excepción")
        void debeRetornarNullCuandoServiceLanzaExcepcion() throws Exception {
            when(userService.obtenerImagenPerfil("error.jpg", 1L))
                    .thenThrow(new RuntimeException("Error simulado"));

            Resource resultado = usuarioServiceTask.obtenerImagenPerfil("error.jpg", 1L);

            assertThat(resultado).isNull();
            verify(userService, times(1)).obtenerImagenPerfil("error.jpg", 1L);
            log.info("Test obtenerImagenPerfil con excepción: retorna null");
        }
    }

    @Nested
    @DisplayName("cargarImg()")
    class CargarImg {

        @Mock
        private MultipartFile multipartFile;

        private UsuarioDTO usuarioActualizado;

        @BeforeEach
        void setUpCargarImg() {
            usuarioActualizado = new UsuarioDTO();
            usuarioActualizado.setId(1L);
            usuarioActualizado.setEmail("test@udes.edu.co");
            usuarioActualizado.setUsername("testuser");
            usuarioActualizado.setNombreCompleto("Test User");
            usuarioActualizado.setFoto("/uploads/usuarios/nueva-foto.jpg");
        }

        @Test
        @DisplayName("Debe retornar 200 OK con UsuarioDTO actualizado cuando la carga es exitosa")
        void debeRetornar200OkCuandoCargaExitosa() throws Exception {
            when(userService.updateImageProfile(multipartFile, 1L)).thenReturn(usuarioActualizado);
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_SUCCESS)).thenReturn("Éxito");

            ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> response =
                    usuarioServiceTask.cargarImg(multipartFile, 1L);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            RespuestaServicioDTO<UsuarioDTO> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isTrue();
            assertThat(body.getNegocio()).isEqualTo(usuarioActualizado);
            assertThat(body.getNegocio().getFoto()).isEqualTo("/uploads/usuarios/nueva-foto.jpg");

            MensajeDTO mensaje = body.getMensajeDTO();
            assertThat(mensaje).isNotNull();
            assertThat(mensaje.getSeverity()).isEqualTo(SeverityEnum.SUCCESS);
            assertThat(mensaje.getSummary()).isEqualTo("Éxito");
            assertThat(mensaje.getDetail()).isEqualTo("Perfil actualizado correctamente");

            verify(userService, times(1)).updateImageProfile(multipartFile, 1L);
            log.info("Test cargarImg exitoso: retorna 200 OK con datos actualizados");
        }

        @Test
        @DisplayName("Debe retornar 500 INTERNAL_SERVER_ERROR cuando el service lanza excepción")
        void debeRetornar500CuandoServiceLanzaExcepcion() throws Exception {
            when(userService.updateImageProfile(multipartFile, 1L))
                    .thenThrow(new RuntimeException("Error al subir imagen"));
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_ERROR_LABEL)).thenReturn("Error");
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_ERROR_PETICION_GENERICO))
                    .thenReturn("Error en la petición");

            ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> response =
                    usuarioServiceTask.cargarImg(multipartFile, 1L);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

            RespuestaServicioDTO<UsuarioDTO> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isFalse();

            MensajeDTO mensaje = body.getMensajeDTO();
            assertThat(mensaje).isNotNull();
            assertThat(mensaje.getSeverity()).isEqualTo(SeverityEnum.ERROR);
            assertThat(mensaje.getSummary()).isEqualTo("Error");
            assertThat(mensaje.getDetail()).isEqualTo("Error en la petición");

            verify(userService, times(1)).updateImageProfile(multipartFile, 1L);
            log.info("Test cargarImg con excepción: retorna 500 INTERNAL_SERVER_ERROR");
        }
    }
}
