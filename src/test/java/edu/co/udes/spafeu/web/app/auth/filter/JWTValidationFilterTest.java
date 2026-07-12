package edu.co.udes.spafeu.web.app.auth.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import edu.co.udes.spafeu.web.app.auth.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>Título: Proyecto SPAFEU</p>
 * <p>Descripción: Tests unitarios para JWTValidationFilter —
 * validación de tokens JWT en cada petición, establecimiento del
 * contexto de seguridad y manejo de tokens inválidos.</p>
 *
 * @author AGENT-QA-EXPERT-SPAFEU
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de JWTValidationFilter")
class JWTValidationFilterTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTService jWTService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    private JWTValidationFilter filter;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() throws Exception {
        filter = new JWTValidationFilter(authenticationManager, jWTService);
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
    }

    @Nested
    @DisplayName("requiresAuthentication()")
    class RequiresAuthentication {

        @Test
        @DisplayName("Debe retornar true cuando el header comienza con 'Bearer '")
        void debeRetornarTrueConBearerPrefix() {
            boolean resultado = filter.requiresAuthentication("Bearer eyJhbGciOiJIUzI1NiJ9.token");

            assertThat(resultado).isTrue();
        }

        @Test
        @DisplayName("Debe retornar false cuando el header es null")
        void debeRetornarFalseCuandoHeaderNull() {
            boolean resultado = filter.requiresAuthentication(null);

            assertThat(resultado).isFalse();
        }

        @Test
        @DisplayName("Debe retornar false cuando el header no comienza con 'Bearer '")
        void debeRetornarFalseCuandoNoTieneBearerPrefix() {
            boolean resultado = filter.requiresAuthentication("Basic dXNlcjpwYXNz");

            assertThat(resultado).isFalse();
        }

        @Test
        @DisplayName("Debe retornar false cuando el header está vacío")
        void debeRetornarFalseCuandoHeaderVacio() {
            boolean resultado = filter.requiresAuthentication("");

            assertThat(resultado).isFalse();
        }

        @Test
        @DisplayName("Debe retornar false cuando el header es solo 'Bearer' sin espacio")
        void debeRetornarFalseCuandoBearerSinEspacio() {
            boolean resultado = filter.requiresAuthentication("BearerExtra");

            assertThat(resultado).isFalse();
        }
    }

    @Nested
    @DisplayName("doFilterInternal()")
    class DoFilterInternal {

        private static final String VALID_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.tokenValido";
        private static final String INVALID_TOKEN = "Bearer tokenInvalido";

        @Test
        @DisplayName("Debe continuar la cadena cuando no hay header Authorization")
        void debeContinuarCadenaCuandoNoHeader() throws Exception {
            when(request.getHeader("Authorization")).thenReturn(null);

            filter.doFilterInternal(request, response, chain);

            verify(chain, times(1)).doFilter(request, response);
            verify(jWTService, never()).validate(anyString(), any());
        }

        @Test
        @DisplayName("Debe continuar la cadena cuando el header no tiene prefix Bearer")
        void debeContinuarCadenaCuandoHeaderSinBearer() throws Exception {
            when(request.getHeader("Authorization")).thenReturn("Basic dXNlcjpwYXNz");

            filter.doFilterInternal(request, response, chain);

            verify(chain, times(1)).doFilter(request, response);
            verify(jWTService, never()).validate(anyString(), any());
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        private void stubRolesValidos() throws Exception {
            when(jWTService.getRoles(anyString())).thenReturn((Collection) List.of(new SimpleGrantedAuthority("ROLE_USER")));
        }

        @Test
        @DisplayName("Debe establecer autenticación en SecurityContext cuando token es válido")
        void debeEstablecerAuthCuandoTokenValido() throws Exception {
            when(request.getHeader("Authorization")).thenReturn(VALID_TOKEN);
            when(jWTService.validate(VALID_TOKEN, response)).thenReturn(true);
            when(jWTService.getUsername(VALID_TOKEN)).thenReturn("testuser");
            stubRolesValidos();
            when(jWTService.getUserId(VALID_TOKEN)).thenReturn(1L);

            filter.doFilterInternal(request, response, chain);

            verify(jWTService, times(1)).validate(VALID_TOKEN, response);
            verify(jWTService, times(1)).getUsername(VALID_TOKEN);
            verify(jWTService, times(1)).getRoles(VALID_TOKEN);
            verify(jWTService, times(1)).getUserId(VALID_TOKEN);
            verify(chain, times(1)).doFilter(request, response);
        }

        @Test
        @DisplayName("Debe responder 401 UNAUTHORIZED cuando token es inválido")
        void debeResponder401CuandoTokenInvalido() throws Exception {
            when(response.getWriter()).thenReturn(printWriter);
            when(request.getHeader("Authorization")).thenReturn(INVALID_TOKEN);
            when(jWTService.validate(INVALID_TOKEN, response)).thenReturn(false);

            filter.doFilterInternal(request, response, chain);

            printWriter.flush();
            String cuerpo = stringWriter.toString();

            assertThat(cuerpo).contains("el token JWT es inválido");
            verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
            verify(response).setContentType("application/json");
            verify(chain, never()).doFilter(request, response);
        }

        @Test
        @DisplayName("Debe establecer el userId en los detalles de la autenticación cuando token es válido")
        void debeEstablecerUserIdEnDetalles() throws Exception {
            when(request.getHeader("Authorization")).thenReturn(VALID_TOKEN);
            when(jWTService.validate(VALID_TOKEN, response)).thenReturn(true);
            when(jWTService.getUsername(VALID_TOKEN)).thenReturn("testuser");
            stubRolesValidos();
            when(jWTService.getUserId(VALID_TOKEN)).thenReturn(99L);

            filter.doFilterInternal(request, response, chain);

            verify(jWTService, times(1)).getUserId(VALID_TOKEN);
            verify(chain, times(1)).doFilter(request, response);
        }
    }
}
