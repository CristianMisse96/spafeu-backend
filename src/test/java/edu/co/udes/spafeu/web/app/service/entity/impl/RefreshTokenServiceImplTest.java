package edu.co.udes.spafeu.web.app.service.entity.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
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
import org.springframework.test.util.ReflectionTestUtils;

import edu.co.udes.spafeu.web.app.auth.service.JWTService;
import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.RefreshTokenResponseDTO;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;
import edu.co.udes.spafeu.web.app.models.entities.RefreshToken;
import edu.co.udes.spafeu.web.app.models.entities.Usuario;
import edu.co.udes.spafeu.web.app.models.enums.TokenStatusEnum;
import edu.co.udes.spafeu.web.app.repositories.RefreshTokenRepository;
import edu.co.udes.spafeu.web.app.repositories.UsuarioRepository;
import edu.co.udes.spafeu.web.app.service.entity.RefreshTokenRotationResult;

/**
 * <p>Título: Proyecto SPAFEU</p>
 * <p>Descripción: Tests unitarios para RefreshTokenServiceImpl — generación,
 * rotación, revocación y limpieza de refresh tokens.</p>
 *
 * @author AGENT-QA-EXPERT-SPAFEU
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de RefreshTokenServiceImpl")
class RefreshTokenServiceImplTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    private static final Long USER_ID = 1L;
    private static final String RAW_TOKEN = "raw-refresh-token-value";
    private static final String ACCESS_TOKEN = "jwt-access-token";

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(USER_ID);
        usuario.setUsername("testuser");
        usuario.setEmail("test@udes.edu.co");

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(USER_ID);
        usuarioDTO.setUsername("testuser");
        usuarioDTO.setEmail("test@udes.edu.co");

        ReflectionTestUtils.setField(refreshTokenService, "refreshTokenExpirationDays", 7);
        ReflectionTestUtils.setField(refreshTokenService, "accessTokenExpirationMinutes", 15);
    }

    private String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible", e);
        }
    }

    private RefreshToken buildActiveToken(String rawToken, LocalDateTime expiration) {
        RefreshToken token = new RefreshToken();
        token.setId(1L);
        token.setTokenHash(hashToken(rawToken));
        token.setUser(usuario);
        token.setStatus(TokenStatusEnum.ACTIVE);
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiration(expiration);
        return token;
    }

    @Nested
    @DisplayName("createRefreshTokenValue()")
    class CreateRefreshTokenValue {

        @Test
        @DisplayName("Debe generar y persistir refresh token cuando el usuario existe")
        void debeCrearTokenCuandoUsuarioExiste() throws SpafeuException {
            when(usuarioRepository.findById(USER_ID)).thenReturn(Optional.of(usuario));
            when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> {
                RefreshToken saved = invocation.getArgument(0);
                saved.setId(1L);
                return saved;
            });

            String rawToken = refreshTokenService.createRefreshTokenValue(USER_ID);

            assertThat(rawToken).isNotBlank();
            ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
            verify(refreshTokenRepository).save(captor.capture());

            RefreshToken saved = captor.getValue();
            assertThat(saved.getTokenHash()).isEqualTo(hashToken(rawToken));
            assertThat(saved.getUser()).isEqualTo(usuario);
            assertThat(saved.getStatus()).isEqualTo(TokenStatusEnum.ACTIVE);
            assertThat(saved.getExpiration()).isAfter(LocalDateTime.now().plusDays(6));
        }

        @Test
        @DisplayName("Debe lanzar SpafeuException cuando el usuario no existe")
        void debeLanzarExcepcionCuandoUsuarioNoExiste() {
            when(usuarioRepository.findById(USER_ID)).thenReturn(Optional.empty());

            SpafeuException ex = assertThrows(SpafeuException.class,
                    () -> refreshTokenService.createRefreshTokenValue(USER_ID));

            assertThat(ex.getMensaje()).isEqualTo("error.refresh.token.usuario.no.existe");
            verify(refreshTokenRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("rotateRefreshToken()")
    class RotateRefreshToken {

        @Test
        @DisplayName("Debe rotar token ACTIVE válido y retornar nuevo access token")
        void debeRotarTokenActivoValido() throws SpafeuException {
            RefreshToken activeToken = buildActiveToken(RAW_TOKEN, LocalDateTime.now().plusDays(7));
            when(refreshTokenRepository.findByTokenHashAndStatus(hashToken(RAW_TOKEN), TokenStatusEnum.ACTIVE))
                    .thenReturn(Optional.of(activeToken));
            when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> {
                RefreshToken saved = invocation.getArgument(0);
                if (saved.getId() == null) {
                    saved.setId(2L);
                }
                return saved;
            });
            when(jwtService.createAccessToken(any(UsuarioDTO.class))).thenReturn(ACCESS_TOKEN);

            RefreshTokenRotationResult result = refreshTokenService.rotateRefreshToken(RAW_TOKEN);

            assertThat(result).isNotNull();
            RefreshTokenResponseDTO response = result.refreshTokenResponse();
            assertThat(response.getAccessToken()).isEqualTo(ACCESS_TOKEN);
            assertThat(response.getTokenType()).isEqualTo("Bearer");
            assertThat(response.getExpiresIn()).isEqualTo(15L * 60L);
            assertThat(response.getUsuario()).isNotNull();
            assertThat(response.getUsuario().getId()).isEqualTo(USER_ID);
            assertThat(response.getUsuario().getUsername()).isEqualTo("testuser");
            assertThat(response.getUsuario().getEmail()).isEqualTo("test@udes.edu.co");
            assertThat(result.rawRefreshToken()).isNotBlank();

            assertThat(activeToken.getStatus()).isEqualTo(TokenStatusEnum.USED);
            assertThat(activeToken.getUsedAt()).isNotNull();
            verify(refreshTokenRepository, times(2)).save(any(RefreshToken.class));
            verify(jwtService).createAccessToken(any(UsuarioDTO.class));
        }

        @Test
        @DisplayName("Debe lanzar SpafeuException cuando el token es null")
        void debeLanzarExcepcionCuandoTokenNull() {
            SpafeuException ex = assertThrows(SpafeuException.class,
                    () -> refreshTokenService.rotateRefreshToken(null));

            assertThat(ex.getMensaje()).isEqualTo("error.refresh.token.no.valido");
            verify(refreshTokenRepository, never()).findByTokenHashAndStatus(anyString(), any());
        }

        @Test
        @DisplayName("Debe lanzar SpafeuException cuando el token está en blanco")
        void debeLanzarExcepcionCuandoTokenBlank() {
            SpafeuException ex = assertThrows(SpafeuException.class,
                    () -> refreshTokenService.rotateRefreshToken("   "));

            assertThat(ex.getMensaje()).isEqualTo("error.refresh.token.no.valido");
            verify(refreshTokenRepository, never()).findByTokenHashAndStatus(anyString(), any());
        }

        @Test
        @DisplayName("Debe lanzar SpafeuException cuando el token no existe")
        void debeLanzarExcepcionCuandoTokenNoExiste() {
            when(refreshTokenRepository.findByTokenHashAndStatus(hashToken(RAW_TOKEN), TokenStatusEnum.ACTIVE))
                    .thenReturn(Optional.empty());
            when(refreshTokenRepository.findByTokenHashAndStatus(hashToken(RAW_TOKEN), TokenStatusEnum.USED))
                    .thenReturn(Optional.empty());

            SpafeuException ex = assertThrows(SpafeuException.class,
                    () -> refreshTokenService.rotateRefreshToken(RAW_TOKEN));

            assertThat(ex.getMensaje()).isEqualTo("error.refresh.token.no.valido");
        }

        @Test
        @DisplayName("Debe marcar EXPIRED, persistir y lanzar SpafeuException cuando el token expiró")
        void debeMarcarExpiradoYPersistirCuandoTokenExpirado() {
            RefreshToken expiredToken = buildActiveToken(RAW_TOKEN, LocalDateTime.now().minusMinutes(1));
            when(refreshTokenRepository.findByTokenHashAndStatus(hashToken(RAW_TOKEN), TokenStatusEnum.ACTIVE))
                    .thenReturn(Optional.of(expiredToken));
            when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(expiredToken);

            SpafeuException ex = assertThrows(SpafeuException.class,
                    () -> refreshTokenService.rotateRefreshToken(RAW_TOKEN));

            assertThat(ex.getMensaje()).isEqualTo("error.refresh.token.expirado");
            assertThat(expiredToken.getStatus()).isEqualTo(TokenStatusEnum.EXPIRED);
            verify(refreshTokenRepository).save(expiredToken);
        }

        @Test
        @DisplayName("Debe revocar tokens activos y lanzar SpafeuException cuando el token fue reutilizado")
        void debeRevocarTokensCuandoTokenReutilizado() {
            RefreshToken usedToken = buildActiveToken(RAW_TOKEN, LocalDateTime.now().plusDays(7));
            usedToken.setStatus(TokenStatusEnum.USED);
            when(refreshTokenRepository.findByTokenHashAndStatus(hashToken(RAW_TOKEN), TokenStatusEnum.ACTIVE))
                    .thenReturn(Optional.empty());
            when(refreshTokenRepository.findByTokenHashAndStatus(hashToken(RAW_TOKEN), TokenStatusEnum.USED))
                    .thenReturn(Optional.of(usedToken));

            SpafeuException ex = assertThrows(SpafeuException.class,
                    () -> refreshTokenService.rotateRefreshToken(RAW_TOKEN));

            assertThat(ex.getMensaje()).isEqualTo("error.refresh.token.reutilizado");
            verify(refreshTokenRepository).invalidateTokensByUser(USER_ID, TokenStatusEnum.REVOKED);
        }
    }

    @Nested
    @DisplayName("revokeRefreshToken()")
    class RevokeRefreshToken {

        @Test
        @DisplayName("Debe marcar REVOKED un token ACTIVE válido")
        void debeRevocarTokenActivo() throws SpafeuException {
            RefreshToken activeToken = buildActiveToken(RAW_TOKEN, LocalDateTime.now().plusDays(7));
            when(refreshTokenRepository.findByTokenHashAndStatus(hashToken(RAW_TOKEN), TokenStatusEnum.ACTIVE))
                    .thenReturn(Optional.of(activeToken));
            when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(activeToken);

            refreshTokenService.revokeRefreshToken(RAW_TOKEN);

            assertThat(activeToken.getStatus()).isEqualTo(TokenStatusEnum.REVOKED);
            verify(refreshTokenRepository).save(activeToken);
        }

        @Test
        @DisplayName("Debe lanzar SpafeuException cuando el token es null")
        void debeLanzarExcepcionCuandoTokenNull() {
            SpafeuException ex = assertThrows(SpafeuException.class,
                    () -> refreshTokenService.revokeRefreshToken(null));

            assertThat(ex.getMensaje()).isEqualTo("error.refresh.token.no.valido");
        }

        @Test
        @DisplayName("Debe lanzar SpafeuException cuando el token no existe o no está activo")
        void debeLanzarExcepcionCuandoTokenNoExiste() {
            when(refreshTokenRepository.findByTokenHashAndStatus(hashToken(RAW_TOKEN), TokenStatusEnum.ACTIVE))
                    .thenReturn(Optional.empty());

            SpafeuException ex = assertThrows(SpafeuException.class,
                    () -> refreshTokenService.revokeRefreshToken(RAW_TOKEN));

            assertThat(ex.getMensaje()).isEqualTo("error.refresh.token.no.valido");
        }
    }

    @Nested
    @DisplayName("cleanupExpiredTokens()")
    class CleanupExpiredTokens {

        @Test
        @DisplayName("Debe invocar deleteByExpirationBefore con fecha actual")
        void debeInvocarDeleteByExpirationBefore() {
            refreshTokenService.cleanupExpiredTokens();

            verify(refreshTokenRepository).deleteByExpirationBefore(any(LocalDateTime.class));
        }
    }
}
