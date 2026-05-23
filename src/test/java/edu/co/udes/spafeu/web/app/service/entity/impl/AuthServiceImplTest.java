package edu.co.udes.spafeu.web.app.service.entity.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.security.crypto.password.PasswordEncoder;

import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;
import edu.co.udes.spafeu.web.app.models.entities.PasswordResetToken;
import edu.co.udes.spafeu.web.app.models.entities.Role;
import edu.co.udes.spafeu.web.app.models.entities.Usuario;
import edu.co.udes.spafeu.web.app.models.enums.RolEnum;
import edu.co.udes.spafeu.web.app.models.enums.TemplateMailEnum;
import edu.co.udes.spafeu.web.app.models.enums.TokenStatusEnum;
import edu.co.udes.spafeu.web.app.repositories.PasswordResetTokenRepository;
import edu.co.udes.spafeu.web.app.repositories.RoleRepository;
import edu.co.udes.spafeu.web.app.repositories.UsuarioRepository;
import edu.co.udes.spafeu.web.app.service.entity.CorreoService;
import lombok.extern.log4j.Log4j2;

/**
 * <p>Título: Proyecto SPAFEU</p>
 * <p>Descripción: Tests unitarios para AuthServiceImpl - lógica de negocio de autenticación y recuperación de contraseñas.</p>
 * @author Sistema SPAFEU
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de AuthServiceImpl")
@Log4j2
class AuthServiceImplTest {

    @Mock
    private UsuarioRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PasswordResetTokenRepository tokenRepository;

    @Mock
    private CorreoService correoService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    private Usuario usuario;
    private PasswordResetToken token;
    private static final String EMAIL_USUARIO = "usuario@test.com";
    private static final String TOKEN_VALIDO = "token-valido-123";
    private static final String NUEVA_PASSWORD = "NuevaPassword123!";

    @BeforeEach
    void setUp() {
        // Arrange - Datos de prueba
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setEmail(EMAIL_USUARIO);
        usuario.setPassword("passwordEncriptado");

        token = new PasswordResetToken();
        token.setId(1L);
        token.setToken(TOKEN_VALIDO);
        token.setUser(usuario);
        token.setExpiration(LocalDateTime.now().plusMinutes(15));
        token.setStatus(TokenStatusEnum.ACTIVE);
    }

    @Nested
    @DisplayName("Tests de forgotPassword()")
    class ForgotPasswordTests {

        @Test
        @DisplayName("Debe generar token y enviar correo cuando el usuario existe")
        void debeGenerarTokenYEnviarCorreoCuandoUsuarioExiste() throws Exception {
            // Arrange
            when(userRepository.findByUsernameOrCorreo(EMAIL_USUARIO)).thenReturn(Optional.of(usuario));
            doNothing().when(tokenRepository).invalidateTokensByUser(anyLong(), any());
            when(tokenRepository.save(any(PasswordResetToken.class))).thenReturn(token);
            doNothing().when(correoService).generarCorreo(anyMap(), anyString(), any(TemplateMailEnum.class), nullable(String.class));

            // Act
            authService.forgotPassword(EMAIL_USUARIO);

            // Assert
            verify(userRepository).findByUsernameOrCorreo(EMAIL_USUARIO);
            verify(tokenRepository).invalidateTokensByUser(usuario.getId(), TokenStatusEnum.EXPIRED);
            verify(tokenRepository).save(any(PasswordResetToken.class));

            // Capturar argumentos del correo
            ArgumentCaptor<Map<String, Object>> mailCaptor = ArgumentCaptor.forClass(Map.class);
            ArgumentCaptor<TemplateMailEnum> templateCaptor = ArgumentCaptor.forClass(TemplateMailEnum.class);
            verify(correoService).generarCorreo(mailCaptor.capture(), anyString(), templateCaptor.capture(), nullable(String.class));

            Map<String, Object> modeloEnviado = mailCaptor.getValue();
            assertThat(modeloEnviado).containsKeys("token", "fecha");
            assertThat(templateCaptor.getValue()).isEqualTo(TemplateMailEnum.PLANTILLA_FORGOT_PASSWORD_USUARIO);

            log.info("Test forgotPassword exitoso: token generado y correo enviado");
        }

        @Test
        @DisplayName("Debe lanzar SpafeuException cuando el usuario no existe")
        void debeLanzarExcepcionCuandoUsuarioNoExiste() {
            // Arrange
            when(userRepository.findByUsernameOrCorreo(EMAIL_USUARIO)).thenReturn(Optional.empty());

            // Act & Assert
            SpafeuException ex = assertThrows(SpafeuException.class,
                    () -> authService.forgotPassword(EMAIL_USUARIO));

            assertThat(ex.getMensaje()).isEqualTo("error.usuario.forgot.password.no.existe");
            verify(tokenRepository, never()).save(any());
            verify(correoService, never()).generarCorreo(any(), anyString(), any(), anyString());
            log.info("Test forgotPassword usuario no existe: excepción lanzada correctamente");
        }

        @Test
        @DisplayName("Debe invalidar tokens previos antes de generar uno nuevo")
        void debeInvalidarTokensPrevios() throws Exception {
            // Arrange
            when(userRepository.findByUsernameOrCorreo(EMAIL_USUARIO)).thenReturn(Optional.of(usuario));
            doNothing().when(tokenRepository).invalidateTokensByUser(anyLong(), any());
            when(tokenRepository.save(any(PasswordResetToken.class))).thenReturn(token);
            doNothing().when(correoService).generarCorreo(anyMap(), anyString(), any(TemplateMailEnum.class), nullable(String.class));

            // Act
            authService.forgotPassword(EMAIL_USUARIO);

            // Assert
            verify(tokenRepository).invalidateTokensByUser(usuario.getId(), TokenStatusEnum.EXPIRED);
            verify(tokenRepository, times(1)).invalidateTokensByUser(anyLong(), any());
            log.info("Test invalidación de tokens previos: correcto");
        }

        @Test
        @DisplayName("Debe generar token con expiración de 15 minutos")
        void debeGenerarTokenConExpiracionDe15Minutos() throws Exception {
            // Arrange
            when(userRepository.findByUsernameOrCorreo(EMAIL_USUARIO)).thenReturn(Optional.of(usuario));
            doNothing().when(tokenRepository).invalidateTokensByUser(anyLong(), any());
            when(tokenRepository.save(any(PasswordResetToken.class))).thenReturn(token);
            doNothing().when(correoService).generarCorreo(anyMap(), anyString(), any(TemplateMailEnum.class), nullable(String.class));

            // Act
            authService.forgotPassword(EMAIL_USUARIO);

            // Assert - Verificar que el token guardado tiene expiración de 15 minutos
            ArgumentCaptor<PasswordResetToken> tokenCaptor = ArgumentCaptor.forClass(PasswordResetToken.class);
            verify(tokenRepository).save(tokenCaptor.capture());
            PasswordResetToken savedToken = tokenCaptor.getValue();

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiration = savedToken.getExpiration();
            long minutesBetween = java.time.Duration.between(now, expiration).toMinutes();
            assertTrue(minutesBetween >= 14 && minutesBetween <= 16,
                    "El token debería expirar en 15 minutos, pero expira en " + minutesBetween + " minutos");
            log.info("Test expiración de token: 15 minutos configurados correctamente");
        }
    }

    @Nested
    @DisplayName("Tests de resetPassword()")
    class ResetPasswordTests {

        @Test
        @DisplayName("Debe restablecer contraseña cuando el token es válido y no expirado")
        void debeRestablecerContrasenaCuandoTokenValido() {
            // Arrange
            when(tokenRepository.findByTokenAndStatus(TOKEN_VALIDO, TokenStatusEnum.ACTIVE))
                    .thenReturn(Optional.of(token));
            when(passwordEncoder.encode(NUEVA_PASSWORD)).thenReturn("passwordEncriptadoNuevo");
            when(userRepository.save(any(Usuario.class))).thenReturn(usuario);
            when(tokenRepository.save(any(PasswordResetToken.class))).thenReturn(token);

            // Act
            authService.resetPassword(TOKEN_VALIDO, NUEVA_PASSWORD);

            // Assert
            verify(tokenRepository).findByTokenAndStatus(TOKEN_VALIDO, TokenStatusEnum.ACTIVE);
            verify(passwordEncoder).encode(NUEVA_PASSWORD);
            verify(userRepository).save(usuario);
            assertSame(TokenStatusEnum.USED, token.getStatus());
            verify(tokenRepository).save(token);
            log.info("Test resetPassword exitoso: contraseña actualizada y token marcado como USED");
        }

        @Test
        @DisplayName("Debe lanzar SpafeuException cuando el token no es válido")
        void debeLanzarExcepcionCuandoTokenNoValido() {
            // Arrange
            when(tokenRepository.findByTokenAndStatus(TOKEN_VALIDO, TokenStatusEnum.ACTIVE))
                    .thenReturn(Optional.empty());

            // Act & Assert
            SpafeuException ex = assertThrows(SpafeuException.class,
                    () -> authService.resetPassword(TOKEN_VALIDO, NUEVA_PASSWORD));

            assertThat(ex.getMensaje()).isEqualTo("error.usuario.reset.password.token.no.valido");
            verify(passwordEncoder, never()).encode(anyString());
            verify(userRepository, never()).save(any());
            verify(tokenRepository, never()).save(any());
            log.info("Test resetPassword token no válido: excepción lanzada correctamente");
        }

        @Test
        @DisplayName("Debe lanzar SpafeuException cuando el token está expirado")
        void debeLanzarExcepcionCuandoTokenExpirado() {
            // Arrange
            token.setExpiration(LocalDateTime.now().minusMinutes(1)); // Token expirado
            when(tokenRepository.findByTokenAndStatus(TOKEN_VALIDO, TokenStatusEnum.ACTIVE))
                    .thenReturn(Optional.of(token));

            // Act & Assert
            SpafeuException ex = assertThrows(SpafeuException.class,
                    () -> authService.resetPassword(TOKEN_VALIDO, NUEVA_PASSWORD));

            assertThat(ex.getMensaje()).isEqualTo("error.usuario.reset.password.token.expirado");
            assertSame(token.getStatus(), TokenStatusEnum.EXPIRED, "El token debe marcarse como EXPIRED en memoria");
            verify(tokenRepository, never()).save(any(PasswordResetToken.class));
            verify(passwordEncoder, never()).encode(anyString());
            verify(userRepository, never()).save(any());
            log.info("Test resetPassword token expirado: excepción lanzada y token marcado como EXPIRED");
        }

        @Test
        @DisplayName("Debe marcar token como USED después de restablecer contraseña")
        void debeMarcaTokenComoUsedDespuesDeRestablecer() {
            // Arrange
            when(tokenRepository.findByTokenAndStatus(TOKEN_VALIDO, TokenStatusEnum.ACTIVE))
                    .thenReturn(Optional.of(token));
            when(passwordEncoder.encode(NUEVA_PASSWORD)).thenReturn("passwordEncriptadoNuevo");
            when(userRepository.save(any(Usuario.class))).thenReturn(usuario);
            when(tokenRepository.save(any(PasswordResetToken.class))).thenReturn(token);

            // Act
            authService.resetPassword(TOKEN_VALIDO, NUEVA_PASSWORD);

            // Assert
            assertSame(token.getStatus(), TokenStatusEnum.USED);
            verify(tokenRepository).save(token);
            log.info("Test token marcado como USED: correcto");
        }
    }

    @Nested
    @DisplayName("Tests de registrarUsuario()")
    class RegistrarUsuarioTests {

        private UsuarioDTO usuarioDTO;
        private Usuario usuarioEntity;
        private Role rolUser;

        @BeforeEach
        void setUpRegistro() {
            // Arrange - DTO de usuario
            usuarioDTO = new UsuarioDTO();
            usuarioDTO.setEmail("nuevo@test.com");
            usuarioDTO.setUsername("nuevouser");
            usuarioDTO.setPassword("Password123!");

            // Entity usuario
            usuarioEntity = new Usuario();
            usuarioEntity.setId(1L);
            usuarioEntity.setEmail(usuarioDTO.getEmail());
            usuarioEntity.setUsername(usuarioDTO.getUsername());
            usuarioEntity.setPassword("passwordEncriptado");

            rolUser = new Role();
            rolUser.setId(1L);
            rolUser.setRol(RolEnum.ROLE_USER);
        }

        @Test
        @DisplayName("Debe registrar usuario correctamente con rol ROLE_USER")
        void debeRegistrarUsuarioCorrectamente() {
            // Arrange
            when(userRepository.existsByEmailOrUsername(usuarioDTO.getEmail(), usuarioDTO.getUsername()))
                    .thenReturn(false);
            when(modelMapper.map(usuarioDTO, Usuario.class)).thenReturn(usuarioEntity);
            when(passwordEncoder.encode(usuarioDTO.getPassword())).thenReturn("passwordEncriptado");
            when(roleRepository.findByRol(RolEnum.ROLE_USER)).thenReturn(Optional.of(rolUser));
            when(userRepository.save(any(Usuario.class))).thenReturn(usuarioEntity);
            when(modelMapper.map(usuarioEntity, UsuarioDTO.class)).thenReturn(usuarioDTO);

            // Act
            UsuarioDTO resultado = authService.registrarUsuario(usuarioDTO);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado.getEmail()).isEqualTo(usuarioDTO.getEmail());
            assertThat(resultado.getUsername()).isEqualTo(usuarioDTO.getUsername());
            verify(userRepository).existsByEmailOrUsername(usuarioDTO.getEmail(), usuarioDTO.getUsername());
            verify(modelMapper).map(usuarioDTO, Usuario.class);
            verify(passwordEncoder).encode(usuarioDTO.getPassword());
            verify(roleRepository).findByRol(RolEnum.ROLE_USER);
            verify(userRepository).save(any(Usuario.class));
            verify(modelMapper).map(usuarioEntity, UsuarioDTO.class);
            log.info("Test registrarUsuario exitoso: usuario creado con rol ROLE_USER");
        }

        @Test
        @DisplayName("Debe lanzar SpafeuException cuando el email o username ya existen")
        void debeLanzarExcepcionCuandoUsuarioYaExiste() {
            // Arrange
            when(userRepository.existsByEmailOrUsername(usuarioDTO.getEmail(), usuarioDTO.getUsername()))
                    .thenReturn(true);

            // Act & Assert
            SpafeuException ex = assertThrows(SpafeuException.class,
                    () -> authService.registrarUsuario(usuarioDTO));

            assertThat(ex.getMensaje()).isEqualTo("error.usuario.registrar.existe");
            verify(userRepository, never()).save(any());
            verify(roleRepository, never()).findByRol(any());
            log.info("Test registrarUsuario usuario ya existe: excepción lanzada correctamente");
        }

        @Test
        @DisplayName("Debe crear rol ROLE_USER si no existe en base de datos")
        void debeCrearRolSiNoExiste() {
            // Arrange
            when(userRepository.existsByEmailOrUsername(usuarioDTO.getEmail(), usuarioDTO.getUsername()))
                    .thenReturn(false);
            when(modelMapper.map(usuarioDTO, Usuario.class)).thenReturn(usuarioEntity);
            when(passwordEncoder.encode(usuarioDTO.getPassword())).thenReturn("passwordEncriptado");
            when(roleRepository.findByRol(RolEnum.ROLE_USER)).thenReturn(Optional.empty());
            when(roleRepository.save(any(Role.class))).thenAnswer(invocation -> {
                Role r = invocation.getArgument(0);
                r.setId(2L); // simula asignación de ID
                return r;
            });
            when(userRepository.save(any(Usuario.class))).thenReturn(usuarioEntity);
            when(modelMapper.map(usuarioEntity, UsuarioDTO.class)).thenReturn(usuarioDTO);

            // Act
            authService.registrarUsuario(usuarioDTO);

            // Assert - Verificar que se crea el rol cuando no existe
            ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
            verify(userRepository).save(usuarioCaptor.capture());
            Usuario usuarioGuardado = usuarioCaptor.getValue();

            assertThat(usuarioGuardado.getRoles()).hasSize(1);
            Role rolAsignado = usuarioGuardado.getRoles().iterator().next();
            assertThat(rolAsignado).isNotNull();
            assertThat(rolAsignado.getRol()).isEqualTo(RolEnum.ROLE_USER);
            verify(roleRepository).save(any(Role.class));
            log.info("Test registrarUsuario crea rol si no existe: correcto");
        }
    }
}
