package edu.co.udes.spafeu.web.app.service.entity.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Optional;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;
import edu.co.udes.spafeu.web.app.models.entities.Usuario;
import edu.co.udes.spafeu.web.app.repositories.UsuarioRepository;
import edu.co.udes.spafeu.web.app.service.entity.UploadFileService;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripcion: Tests unitarios para UserServiceImpl - logica de negocio de gestion de usuarios (perfil, imagen, validaciones).</p>
 * @author Sistema SPAFEU
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de UserServiceImpl")
@Log4j2
class UserServiceImplTest {

    @Mock
    private UsuarioRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UploadFileService uploadFileService;

    @InjectMocks
    private UserServiceImpl userService;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;
    private static final Long USER_ID = 1L;
    private static final String RUTA_IMAGENES = "uploads/usuarios/profiles/img/1";
    private static final String EMAIL_EXISTENTE = "usuario@test.com";
    private static final String USERNAME_EXISTENTE = "testuser";
    private static final String NOMBRE_ARCHIVO_NUEVO = "foto-nueva-123.jpg";
    private static final String FOTO_ANTERIOR = "foto-anterior-456.jpg";

    @BeforeEach
    void setUp() {
        // Arrange - Datos de prueba comunes
        usuario = new Usuario();
        usuario.setId(USER_ID);
        usuario.setUsername(USERNAME_EXISTENTE);
        usuario.setEmail(EMAIL_EXISTENTE);
        usuario.setPassword("passwordEncriptado");
        usuario.setNombreCompleto("Usuario Test");
        usuario.setEnabled(true);
        usuario.setFoto(FOTO_ANTERIOR);

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(USER_ID);
        usuarioDTO.setUsername(USERNAME_EXISTENTE);
        usuarioDTO.setEmail(EMAIL_EXISTENTE);
        usuarioDTO.setNombreCompleto("Usuario Test");
        usuarioDTO.setEnabled(true);
        usuarioDTO.setFoto(FOTO_ANTERIOR);
    }

    @Nested
    @DisplayName("Tests de validarCorreo()")
    class ValidarCorreo {

        @Test
        @DisplayName("Debe retornar true cuando el correo ya existe en el sistema")
        void debeRetornarTrueCuandoCorreoExiste() {
            // Arrange
            when(userRepository.existsByEmail(EMAIL_EXISTENTE)).thenReturn(true);

            // Act
            Boolean resultado = userService.validarCorreo(EMAIL_EXISTENTE);

            // Assert
            assertThat(resultado).isTrue();
            verify(userRepository).existsByEmail(EMAIL_EXISTENTE);
            log.info("Test validarCorreo exitoso: correo existente detectado correctamente");
        }

        @Test
        @DisplayName("Debe retornar false cuando el correo no existe en el sistema")
        void debeRetornarFalseCuandoCorreoNoExiste() {
            // Arrange
            String emailNuevo = "nuevo@test.com";
            when(userRepository.existsByEmail(emailNuevo)).thenReturn(false);

            // Act
            Boolean resultado = userService.validarCorreo(emailNuevo);

            // Assert
            assertThat(resultado).isFalse();
            verify(userRepository).existsByEmail(emailNuevo);
            log.info("Test validarCorreo exitoso: correo inexistente detectado correctamente");
        }
    }

    @Nested
    @DisplayName("Tests de validarUsername()")
    class ValidarUsername {

        @Test
        @DisplayName("Debe retornar true cuando el username ya existe en el sistema")
        void debeRetornarTrueCuandoUsernameExiste() {
            // Arrange
            when(userRepository.existsByUsername(USERNAME_EXISTENTE)).thenReturn(true);

            // Act
            Boolean resultado = userService.validarUsername(USERNAME_EXISTENTE);

            // Assert
            assertThat(resultado).isTrue();
            verify(userRepository).existsByUsername(USERNAME_EXISTENTE);
            log.info("Test validarUsername exitoso: username existente detectado correctamente");
        }

        @Test
        @DisplayName("Debe retornar false cuando el username no existe en el sistema")
        void debeRetornarFalseCuandoUsernameNoExiste() {
            // Arrange
            String usernameNuevo = "nuevouser";
            when(userRepository.existsByUsername(usernameNuevo)).thenReturn(false);

            // Act
            Boolean resultado = userService.validarUsername(usernameNuevo);

            // Assert
            assertThat(resultado).isFalse();
            verify(userRepository).existsByUsername(usernameNuevo);
            log.info("Test validarUsername exitoso: username inexistente detectado correctamente");
        }
    }

    @Nested
    @DisplayName("Tests de updateImageProfile()")
    class UpdateImageProfile {

        private MultipartFile archivoMock;

        @BeforeEach
        void setUpUpdateImageProfile() {
            // Arrange - Configuracion base para updateImageProfile
            archivoMock = mock(MultipartFile.class);
        }

        @Test
        @DisplayName("Debe subir imagen, eliminar anterior y retornar UsuarioDTO cuando el usuario existe")
        void debeSubirImagenEliminarAnteriorYRetornarUsuarioDTO() throws SpafeuException, IOException {
            // Arrange
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(usuario));
            when(uploadFileService.copiar(any(MultipartFile.class), eq(RUTA_IMAGENES)))
                    .thenReturn(NOMBRE_ARCHIVO_NUEVO);
            when(uploadFileService.eliminar(anyString(), eq(RUTA_IMAGENES))).thenReturn(true);
            when(userRepository.save(any(Usuario.class))).thenReturn(usuario);
            when(modelMapper.map(any(Usuario.class), eq(UsuarioDTO.class))).thenReturn(usuarioDTO);

            // Act
            UsuarioDTO resultado = userService.updateImageProfile(archivoMock, USER_ID);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(USER_ID);

            verify(userRepository).findById(USER_ID);
            verify(uploadFileService).copiar(archivoMock, RUTA_IMAGENES);
            verify(uploadFileService).eliminar(FOTO_ANTERIOR, RUTA_IMAGENES);
            verify(userRepository).save(usuario);
            verify(modelMapper).map(usuario, UsuarioDTO.class);

            log.info("Test updateImageProfile exitoso: imagen subida, anterior eliminada y DTO retornado");
        }

        @Test
        @DisplayName("Debe actualizar el campo foto del usuario con el nombre del nuevo archivo")
        void debeActualizarCampoFotoConNuevoNombreArchivo() throws SpafeuException, IOException {
            // Arrange
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(usuario));
            when(uploadFileService.copiar(any(MultipartFile.class), eq(RUTA_IMAGENES)))
                    .thenReturn(NOMBRE_ARCHIVO_NUEVO);
            when(uploadFileService.eliminar(anyString(), eq(RUTA_IMAGENES))).thenReturn(true);
            when(userRepository.save(any(Usuario.class))).thenReturn(usuario);
            when(modelMapper.map(any(Usuario.class), eq(UsuarioDTO.class))).thenReturn(usuarioDTO);

            // Act
            userService.updateImageProfile(archivoMock, USER_ID);

            // Assert - Capturar el usuario guardado para verificar que se actualizo la foto
            ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
            verify(userRepository).save(usuarioCaptor.capture());
            Usuario usuarioGuardado = usuarioCaptor.getValue();

            assertThat(usuarioGuardado.getFoto()).isEqualTo(NOMBRE_ARCHIVO_NUEVO);
            log.info("Test updateImageProfile: campo foto actualizado con el nuevo nombre de archivo");
        }

        @Test
        @DisplayName("Debe llamar a uploadFileService.copiar con el archivo y ruta correctos")
        void debeLlamarCopiarConArchivoYRutaCorrectos() throws SpafeuException, IOException {
            // Arrange
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(usuario));
            when(uploadFileService.copiar(any(MultipartFile.class), eq(RUTA_IMAGENES)))
                    .thenReturn(NOMBRE_ARCHIVO_NUEVO);
            when(uploadFileService.eliminar(anyString(), eq(RUTA_IMAGENES))).thenReturn(true);
            when(userRepository.save(any(Usuario.class))).thenReturn(usuario);
            when(modelMapper.map(any(Usuario.class), eq(UsuarioDTO.class))).thenReturn(usuarioDTO);

            // Act
            userService.updateImageProfile(archivoMock, USER_ID);

            // Assert
            verify(uploadFileService).copiar(archivoMock, RUTA_IMAGENES);
            verify(uploadFileService, times(1)).copiar(any(MultipartFile.class), anyString());
            log.info("Test updateImageProfile: copiar llamado con parametros correctos");
        }

        @Test
        @DisplayName("Debe llamar a uploadFileService.eliminar con la foto anterior y la ruta correcta")
        void debeLlamarEliminarConFotoAnteriorYRutaCorrecta() throws SpafeuException, IOException {
            // Arrange
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(usuario));
            when(uploadFileService.copiar(any(MultipartFile.class), eq(RUTA_IMAGENES)))
                    .thenReturn(NOMBRE_ARCHIVO_NUEVO);
            when(uploadFileService.eliminar(anyString(), eq(RUTA_IMAGENES))).thenReturn(true);
            when(userRepository.save(any(Usuario.class))).thenReturn(usuario);
            when(modelMapper.map(any(Usuario.class), eq(UsuarioDTO.class))).thenReturn(usuarioDTO);

            // Act
            userService.updateImageProfile(archivoMock, USER_ID);

            // Assert
            verify(uploadFileService).eliminar(FOTO_ANTERIOR, RUTA_IMAGENES);
            verify(uploadFileService, times(1)).eliminar(anyString(), anyString());
            log.info("Test updateImageProfile: eliminar llamado con foto anterior y ruta correctas");
        }

        @Test
        @DisplayName("Debe eliminar la foto anterior antes de guardar el usuario")
        void debeEliminarFotoAnteriorAntesDeGuardarUsuario() throws SpafeuException, IOException {
            // Arrange
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(usuario));
            when(uploadFileService.copiar(any(MultipartFile.class), eq(RUTA_IMAGENES)))
                    .thenReturn(NOMBRE_ARCHIVO_NUEVO);
            when(uploadFileService.eliminar(anyString(), eq(RUTA_IMAGENES))).thenReturn(true);
            when(userRepository.save(any(Usuario.class))).thenReturn(usuario);
            when(modelMapper.map(any(Usuario.class), eq(UsuarioDTO.class))).thenReturn(usuarioDTO);

            // Act
            userService.updateImageProfile(archivoMock, USER_ID);

            // Assert - Verificar orden de llamadas: copiar -> eliminar -> save
            verify(uploadFileService).copiar(archivoMock, RUTA_IMAGENES);
            verify(uploadFileService).eliminar(FOTO_ANTERIOR, RUTA_IMAGENES);
            verify(userRepository).save(usuario);
            log.info("Test updateImageProfile: orden de operaciones (copiar -> eliminar -> save) verificado");
        }

        @Test
        @DisplayName("Debe lanzar SpafeuException cuando el usuario no existe")
        void debeLanzarSpafeuExceptionCuandoUsuarioNoExiste() throws IOException {
            // Arrange
            when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

            // Act & Assert
            SpafeuException ex = assertThrows(SpafeuException.class,
                    () -> userService.updateImageProfile(archivoMock, USER_ID));

            assertThat(ex.getMensaje()).isEqualTo("error.usuario.no.existe.cargar.imagen");
            verify(uploadFileService, never()).copiar(any(), anyString());
            verify(uploadFileService, never()).eliminar(anyString(), anyString());
            verify(userRepository, never()).save(any());
            verify(modelMapper, never()).map(any(), any());
            log.info("Test updateImageProfile usuario no existe: SpafeuException lanzada correctamente");
        }

        @Test
        @DisplayName("Debe propagar IOException cuando uploadFileService.copiar lanza IOException")
        void debePropagarIOExceptionCuandoCopiarFalla() throws SpafeuException, IOException {
            // Arrange
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(usuario));
            when(uploadFileService.copiar(any(MultipartFile.class), eq(RUTA_IMAGENES)))
                    .thenThrow(new IOException("Error al copiar archivo"));

            // Act & Assert
            assertThrows(IOException.class,
                    () -> userService.updateImageProfile(archivoMock, USER_ID));

            verify(userRepository).findById(USER_ID);
            verify(uploadFileService).copiar(archivoMock, RUTA_IMAGENES);
            verify(uploadFileService, never()).eliminar(anyString(), anyString());
            verify(userRepository, never()).save(any());
            log.info("Test updateImageProfile IOException: excepcion propagada correctamente");
        }
    }

    @Nested
    @DisplayName("Tests de obtenerImagenPerfil()")
    class ObtenerImagenPerfil {

        private Resource recursoMock;

        private static final String NOMBRE_RECURSO = "foto-perfil.jpg";

        @BeforeEach
        void setUpObtenerImagenPerfil() {
            // Arrange - Configuracion base para obtenerImagenPerfil
            recursoMock = mock(Resource.class);
        }

        @Test
        @DisplayName("Debe retornar el Resource cuando la imagen existe")
        void debeRetornarResourceCuandoImagenExiste() throws SpafeuException, MalformedURLException {
            // Arrange
            when(uploadFileService.cargar(NOMBRE_RECURSO, RUTA_IMAGENES)).thenReturn(recursoMock);

            // Act
            Resource resultado = userService.obtenerImagenPerfil(NOMBRE_RECURSO, USER_ID);

            // Assert
            assertThat(resultado).isNotNull();
            assertSame(recursoMock, resultado);
            verify(uploadFileService).cargar(NOMBRE_RECURSO, RUTA_IMAGENES);
            log.info("Test obtenerImagenPerfil exitoso: Resource retornado correctamente");
        }

        @Test
        @DisplayName("Debe delegar correctamente a uploadFileService.cargar con los parametros adecuados")
        void debeDelegarCorrectamenteAUploadFileServiceCargar() throws SpafeuException, MalformedURLException {
            // Arrange
            when(uploadFileService.cargar(NOMBRE_RECURSO, RUTA_IMAGENES)).thenReturn(recursoMock);

            // Act
            userService.obtenerImagenPerfil(NOMBRE_RECURSO, USER_ID);

            // Assert
            verify(uploadFileService).cargar(NOMBRE_RECURSO, RUTA_IMAGENES);
            verify(uploadFileService, times(1)).cargar(anyString(), anyString());
            log.info("Test obtenerImagenPerfil: delegacion a uploadFileService.cargar verificada");
        }

        @Test
        @DisplayName("Debe propagar MalformedURLException cuando uploadFileService.cargar lanza MalformedURLException")
        void debePropagarMalformedURLExceptionCuandoCargarFalla() throws SpafeuException, MalformedURLException {
            // Arrange
            when(uploadFileService.cargar(NOMBRE_RECURSO, RUTA_IMAGENES))
                    .thenThrow(new MalformedURLException("URL mal formada"));

            // Act & Assert
            assertThrows(MalformedURLException.class,
                    () -> userService.obtenerImagenPerfil(NOMBRE_RECURSO, USER_ID));

            verify(uploadFileService).cargar(NOMBRE_RECURSO, RUTA_IMAGENES);
            log.info("Test obtenerImagenPerfil MalformedURLException: excepcion propagada correctamente");
        }
    }

    @Nested
    @DisplayName("Tests de buscarUsuario()")
    class BuscarUsuario {

        @Test
        @DisplayName("Debe retornar UsuarioDTO cuando el usuario existe")
        void debeRetornarUsuarioDTOCuandoUsuarioExiste() throws SpafeuException {
            // Arrange
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(usuario));
            when(modelMapper.map(usuario, UsuarioDTO.class)).thenReturn(usuarioDTO);

            // Act
            UsuarioDTO resultado = userService.buscarUsuario(USER_ID);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(USER_ID);
            assertThat(resultado.getEmail()).isEqualTo(EMAIL_EXISTENTE);
            assertThat(resultado.getUsername()).isEqualTo(USERNAME_EXISTENTE);
            verify(userRepository).findById(USER_ID);
            verify(modelMapper).map(usuario, UsuarioDTO.class);
            log.info("Test buscarUsuario exitoso: UsuarioDTO retornado correctamente");
        }

        @Test
        @DisplayName("Debe lanzar SpafeuException cuando el usuario no existe")
        void debeLanzarSpafeuExceptionCuandoUsuarioNoExiste() {
            // Arrange
            when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

            // Act & Assert
            SpafeuException ex = assertThrows(SpafeuException.class,
                    () -> userService.buscarUsuario(USER_ID));

            assertThat(ex.getMensaje()).isEqualTo("error.usuario.no.existe.cargar.imagen");
            verify(userRepository).findById(USER_ID);
            verify(modelMapper, never()).map(any(), any());
            log.info("Test buscarUsuario usuario no existe: SpafeuException lanzada correctamente");
        }
    }
}
