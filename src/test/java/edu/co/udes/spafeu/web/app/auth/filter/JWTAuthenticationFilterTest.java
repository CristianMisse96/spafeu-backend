package edu.co.udes.spafeu.web.app.auth.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import edu.co.udes.spafeu.web.app.auth.service.JWTService;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;
import edu.co.udes.spafeu.web.app.service.entity.RefreshTokenService;

/**
 * <p>Título: Proyecto SPAFEU</p>
 * <p>Descripción: Tests unitarios para JWTAuthenticationFilter —
 * flujo de autenticación (login), generación de token JWT en éxito
 * y manejo de errores de autenticación.</p>
 *
 * @author AGENT-QA-EXPERT-SPAFEU
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de JWTAuthenticationFilter")
class JWTAuthenticationFilterTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTService jWTService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @Mock
    private Authentication authResult;

    private JWTAuthenticationFilter filter;
    private UsuarioDTO usuarioDTO;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() throws Exception {
        filter = new JWTAuthenticationFilter(authenticationManager, jWTService, refreshTokenService, false);
        ReflectionTestUtils.setField(filter, "refreshTokenPath", "/api/session");
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setUsername("testuser");
        usuarioDTO.setEmail("test@udes.edu.co");
        usuarioDTO.setNombreCompleto("Test User");
    }

    @Nested
    @DisplayName("attemptAuthentication()")
    class AttemptAuthentication {

        private String jsonBody;

        @BeforeEach
        void setUp() throws Exception {
            jsonBody = "{\"username\":\"testuser\",\"password\":\"testpass123\"}";
            when(request.getInputStream()).thenReturn(
                new ServletInputStreamStub(new ByteArrayInputStream(jsonBody.getBytes(StandardCharsets.UTF_8))));
        }

        @Test
        @DisplayName("Debe autenticar exitosamente cuando credenciales son válidas")
        void debeAutenticarCuandoCredencialesValidas() {
            UsernamePasswordAuthenticationToken expectedToken =
                new UsernamePasswordAuthenticationToken("testuser", "testpass123");
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authResult);

            Authentication resultado = filter.attemptAuthentication(request, response);

            assertThat(resultado).isEqualTo(authResult);
            verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        }

        @Test
        @DisplayName("Debe retornar null y responder 400 BAD_REQUEST cuando username está vacío")
        void debeRetornarNullCuandoUsernameVacio() throws Exception {
            String jsonVacio = "{\"username\":\"\",\"password\":\"testpass123\"}";
            when(request.getInputStream()).thenReturn(
                new ServletInputStreamStub(new ByteArrayInputStream(jsonVacio.getBytes(StandardCharsets.UTF_8))));
            when(response.getWriter()).thenReturn(printWriter);

            Authentication resultado = filter.attemptAuthentication(request, response);

            assertThat(resultado).isNull();
            verify(response).setStatus(HttpStatus.BAD_REQUEST.value());
            verify(response).setContentType("application/json");
            verify(authenticationManager, never()).authenticate(any());
        }

        @Test
        @DisplayName("Debe retornar null y responder 400 BAD_REQUEST cuando username es null")
        void debeRetornarNullCuandoUsernameNull() throws Exception {
            String jsonNull = "{\"password\":\"testpass123\"}";
            when(request.getInputStream()).thenReturn(
                new ServletInputStreamStub(new ByteArrayInputStream(jsonNull.getBytes(StandardCharsets.UTF_8))));
            when(response.getWriter()).thenReturn(printWriter);

            Authentication resultado = filter.attemptAuthentication(request, response);

            assertThat(resultado).isNull();
            verify(response).setStatus(HttpStatus.BAD_REQUEST.value());
            verify(authenticationManager, never()).authenticate(any());
        }

        @Test
        @DisplayName("Debe retornar null y responder 400 BAD_REQUEST cuando password está vacío")
        void debeRetornarNullCuandoPasswordVacio() throws Exception {
            String jsonVacio = "{\"username\":\"testuser\",\"password\":\"\"}";
            when(request.getInputStream()).thenReturn(
                new ServletInputStreamStub(new ByteArrayInputStream(jsonVacio.getBytes(StandardCharsets.UTF_8))));
            when(response.getWriter()).thenReturn(printWriter);

            Authentication resultado = filter.attemptAuthentication(request, response);

            assertThat(resultado).isNull();
            verify(response).setStatus(HttpStatus.BAD_REQUEST.value());
            verify(authenticationManager, never()).authenticate(any());
        }

        @Test
        @DisplayName("Debe retornar null y responder 400 BAD_REQUEST cuando password es null")
        void debeRetornarNullCuandoPasswordNull() throws Exception {
            String jsonNull = "{\"username\":\"testuser\"}";
            when(request.getInputStream()).thenReturn(
                new ServletInputStreamStub(new ByteArrayInputStream(jsonNull.getBytes(StandardCharsets.UTF_8))));
            when(response.getWriter()).thenReturn(printWriter);

            Authentication resultado = filter.attemptAuthentication(request, response);

            assertThat(resultado).isNull();
            verify(response).setStatus(HttpStatus.BAD_REQUEST.value());
            verify(authenticationManager, never()).authenticate(any());
        }
    }

    @Nested
    @DisplayName("successfulAuthentication()")
    class SuccessfulAuthentication {

        @BeforeEach
        void setUp() throws Exception {
            when(authResult.getName()).thenReturn("testuser");
            when(jWTService.findUser("testuser")).thenReturn(usuarioDTO);
            when(jWTService.create(authResult, usuarioDTO)).thenReturn("jwt-token-test");
            when(refreshTokenService.createRefreshTokenValue(1L)).thenReturn("refresh-token-test");
            when(response.getWriter()).thenReturn(printWriter);
        }

        @Test
        @DisplayName("Debe agregar header Authorization con el token JWT")
        void debeAgregarHeaderAuthorization() throws Exception {
            filter.successfulAuthentication(request, response, chain, authResult);

            verify(response).addHeader("Authorization", "Bearer jwt-token-test");
        }

        @Test
        @DisplayName("Debe escribir cuerpo JSON con token, usuario y mensaje de bienvenida")
        void debeEscribirCuerpoJson() throws Exception {
            filter.successfulAuthentication(request, response, chain, authResult);

            printWriter.flush();
            String cuerpo = stringWriter.toString();

            assertThat(cuerpo).contains("jwt-token-test");
            assertThat(cuerpo).contains("testuser");
            assertThat(cuerpo).contains("Bienvenido");
            assertThat(cuerpo).contains("test@udes.edu.co");
        }

        @Test
        @DisplayName("Debe establecer Content-Type como application/json")
        void debeEstablecerContentType() throws Exception {
            filter.successfulAuthentication(request, response, chain, authResult);

            verify(response).setContentType("application/json");
        }

        @Test
        @DisplayName("Debe establecer HttpStatus OK (200)")
        void debeEstablecerStatusOk() throws Exception {
            filter.successfulAuthentication(request, response, chain, authResult);

            verify(response).setStatus(HttpStatus.OK.value());
        }

        @Test
        @DisplayName("Debe buscar el usuario y crear el token con JWTService")
        void debeBuscarUsuarioYCrearToken() throws Exception {
            filter.successfulAuthentication(request, response, chain, authResult);

            verify(jWTService, times(1)).findUser("testuser");
            verify(jWTService, times(1)).create(authResult, usuarioDTO);
        }

        @Test
        @DisplayName("Debe agregar cookie HttpOnly con el refresh token")
        void debeAgregarCookieRefreshToken() throws Exception {
            filter.successfulAuthentication(request, response, chain, authResult);

            ArgumentCaptor<String> cookieCaptor = ArgumentCaptor.forClass(String.class);
            verify(response).addHeader(eq(HttpHeaders.SET_COOKIE), cookieCaptor.capture());
            String cookieValue = cookieCaptor.getValue();
            assertThat(cookieValue).startsWith("refreshToken=refresh-token-test");
            assertThat(cookieValue).contains("Path=/api/session");
            assertThat(cookieValue).contains("Max-Age=604800");
            assertThat(cookieValue).contains("HttpOnly");
            assertThat(cookieValue).contains("SameSite=Lax");
        }
    }

    @Nested
    @DisplayName("unsuccessfulAuthentication()")
    class UnsuccessfulAuthentication {

        @BeforeEach
        void setUp() throws Exception {
            when(response.getWriter()).thenReturn(printWriter);
        }

        @Test
        @DisplayName("Debe responder con mensaje de cuenta deshabilitada cuando DisabledException")
        void debeResponderCuentaDeshabilitada() throws Exception {
            DisabledException exception = new DisabledException("User is disabled");

            filter.unsuccessfulAuthentication(request, response, exception);

            printWriter.flush();
            String cuerpo = stringWriter.toString();

            assertThat(cuerpo).contains("cuenta está deshabilitada");
            assertThat(cuerpo).contains("User is disabled");
            verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
            verify(response).setContentType("application/json");
        }

        @Test
        @DisplayName("Debe responder con mensaje genérico cuando AuthenticationException")
        void debeResponderMensajeGenerico() throws Exception {
            BadCredentialsException exception = new BadCredentialsException("Bad credentials");

            filter.unsuccessfulAuthentication(request, response, exception);

            printWriter.flush();
            String cuerpo = stringWriter.toString();

            assertThat(cuerpo).contains("username/correo o password incorrectos");
            assertThat(cuerpo).contains("Bad credentials");
            verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
            verify(response).setContentType("application/json");
        }
    }

    /**
     * Stub de ServletInputStream para poder proporcionar contenido JSON
     * simulado en las pruebas de attemptAuthentication.
     */
    private static class ServletInputStreamStub extends jakarta.servlet.ServletInputStream {

        private final ByteArrayInputStream delegate;

        ServletInputStreamStub(ByteArrayInputStream delegate) {
            this.delegate = delegate;
        }

        @Override
        public int read() throws IOException {
            return delegate.read();
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return delegate.read(b, off, len);
        }

        @Override
        public boolean isFinished() {
            return delegate.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(jakarta.servlet.ReadListener readListener) {
            // No-op
        }
    }
}
