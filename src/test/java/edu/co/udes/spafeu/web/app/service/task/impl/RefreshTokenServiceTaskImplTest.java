package edu.co.udes.spafeu.web.app.service.task.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import edu.co.udes.spafeu.web.app.models.dto.RefreshTokenResponseDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.service.entity.RefreshTokenRotationResult;
import edu.co.udes.spafeu.web.app.service.entity.RefreshTokenService;
import edu.co.udes.spafeu.web.app.util.Messages;
import edu.co.udes.spafeu.web.app.util.SpringSecurityConstantes;

/**
 * <p>Título: Proyecto SPAFEU</p>
 * <p>Descripción: Tests unitarios para RefreshTokenServiceTaskImpl — refresh
 * de access token y logout gestionando la cookie HttpOnly del refresh token.</p>
 *
 * @author AGENT-QA-EXPERT-SPAFEU
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de RefreshTokenServiceTaskImpl")
class RefreshTokenServiceTaskImplTest {

    @Mock
    private Messages messages;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private RefreshTokenServiceTaskImpl refreshTokenServiceTask;

    private static final String RAW_REFRESH_TOKEN = "raw-refresh-token";
    private static final String NEW_REFRESH_TOKEN = "new-raw-refresh-token";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(refreshTokenServiceTask, "cookieSecure", false);
        when(messages.getKey(anyString())).thenReturn("mock");
    }

    private Cookie[] cookiesWithRefreshToken(String value) {
        return new Cookie[] { new Cookie(SpringSecurityConstantes.REFRESH_TOKEN_COOKIE_NAME, value) };
    }

    @Nested
    @DisplayName("refreshAccessToken()")
    class RefreshAccessToken {

        @Test
        @DisplayName("Debe rotar token, setear nueva cookie y retornar 200 OK")
        void debeRetornar200OkConNuevaCookie() {
            when(request.getCookies()).thenReturn(cookiesWithRefreshToken(RAW_REFRESH_TOKEN));

            RefreshTokenResponseDTO responseDTO = new RefreshTokenResponseDTO();
            responseDTO.setAccessToken("access-token");
            responseDTO.setTokenType("Bearer");
            responseDTO.setExpiresIn(900L);

            RefreshTokenRotationResult rotationResult =
                    new RefreshTokenRotationResult(responseDTO, NEW_REFRESH_TOKEN);
            when(refreshTokenService.rotateRefreshToken(RAW_REFRESH_TOKEN)).thenReturn(rotationResult);

            ResponseEntity<RespuestaServicioDTO<RefreshTokenResponseDTO>> result =
                    refreshTokenServiceTask.refreshAccessToken(request, response);

            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(result.getBody()).isNotNull();
            assertThat(result.getBody().isOk()).isTrue();
            assertThat(result.getBody().getNegocio()).isEqualTo(responseDTO);

            verify(refreshTokenService).rotateRefreshToken(RAW_REFRESH_TOKEN);

            ArgumentCaptor<String> headerCaptor = ArgumentCaptor.forClass(String.class);
            verify(response).addHeader(anyString(), headerCaptor.capture());
            assertThat(headerCaptor.getValue()).contains(SpringSecurityConstantes.REFRESH_TOKEN_COOKIE_NAME + "=" + NEW_REFRESH_TOKEN);
        }

        @Test
        @DisplayName("Debe retornar 400 BAD_REQUEST cuando no hay cookie de refresh token")
        void debeRetornar400CuandoCookieNoPresente() {
            when(request.getCookies()).thenReturn(null);

            ResponseEntity<RespuestaServicioDTO<RefreshTokenResponseDTO>> result =
                    refreshTokenServiceTask.refreshAccessToken(request, response);

            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(result.getBody()).isNotNull();
            assertThat(result.getBody().isOk()).isFalse();
            verify(refreshTokenService, never()).rotateRefreshToken(anyString());
            verify(response, never()).addHeader(anyString(), anyString());
        }

        @Test
        @DisplayName("Debe retornar 400 BAD_REQUEST cuando la cookie está vacía")
        void debeRetornar400CuandoCookieVacia() {
            when(request.getCookies()).thenReturn(cookiesWithRefreshToken("   "));

            ResponseEntity<RespuestaServicioDTO<RefreshTokenResponseDTO>> result =
                    refreshTokenServiceTask.refreshAccessToken(request, response);

            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(result.getBody().isOk()).isFalse();
            verify(refreshTokenService, never()).rotateRefreshToken(anyString());
        }
    }

    @Nested
    @DisplayName("logout()")
    class Logout {

        @Test
        @DisplayName("Debe revocar token, eliminar cookie y retornar 200 OK")
        void debeRetornar200OkConCookieEliminada() throws Exception {
            when(request.getCookies()).thenReturn(cookiesWithRefreshToken(RAW_REFRESH_TOKEN));

            ResponseEntity<RespuestaServicioDTO<String>> result =
                    refreshTokenServiceTask.logout(request, response);

            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(result.getBody()).isNotNull();
            assertThat(result.getBody().isOk()).isTrue();
            assertThat(result.getBody().getNegocio()).isEqualTo("OK");

            verify(refreshTokenService).revokeRefreshToken(RAW_REFRESH_TOKEN);

            ArgumentCaptor<String> headerNameCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<String> headerValueCaptor = ArgumentCaptor.forClass(String.class);
            verify(response).addHeader(headerNameCaptor.capture(), headerValueCaptor.capture());
            assertThat(headerNameCaptor.getValue()).isEqualTo(HttpHeaders.SET_COOKIE);
            assertThat(headerValueCaptor.getValue()).contains(SpringSecurityConstantes.REFRESH_TOKEN_COOKIE_NAME + "=");
            assertThat(headerValueCaptor.getValue()).contains("Max-Age=0");
        }

        @Test
        @DisplayName("Debe retornar 400 BAD_REQUEST cuando no hay cookie de refresh token")
        void debeRetornar400CuandoCookieNoPresente() {
            when(request.getCookies()).thenReturn(null);

            ResponseEntity<RespuestaServicioDTO<String>> result =
                    refreshTokenServiceTask.logout(request, response);

            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(result.getBody()).isNotNull();
            assertThat(result.getBody().isOk()).isFalse();
            verify(refreshTokenService, never()).revokeRefreshToken(anyString());
            verify(response, never()).addHeader(anyString(), anyString());
        }
    }
}
