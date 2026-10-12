package edu.co.udes.spafeu.web.app.auth.filter;

import static edu.co.udes.spafeu.web.app.util.SpringSecurityConstantes.CONTENT_TYPE;
import static edu.co.udes.spafeu.web.app.util.SpringSecurityConstantes.HEADER_AUTHORIZATION;
import static edu.co.udes.spafeu.web.app.util.SpringSecurityConstantes.MESSAGE;
import static edu.co.udes.spafeu.web.app.util.SpringSecurityConstantes.PREFIX_TOKEN;
import static edu.co.udes.spafeu.web.app.util.SpringSecurityConstantes.REFRESH_TOKEN_COOKIE_NAME;
import static edu.co.udes.spafeu.web.app.util.SpringSecurityConstantes.REFRESH_TOKEN_EXPIRATION_DAYS;
import static edu.co.udes.spafeu.web.app.util.SpringSecurityConstantes.SAME_SITE_LAX;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.co.udes.spafeu.web.app.auth.service.JWTService;
import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;
import edu.co.udes.spafeu.web.app.models.entities.Usuario;
import edu.co.udes.spafeu.web.app.service.entity.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Filtro de autenticación JWT. Valida credenciales de usuario y,
 * en caso de éxito, genera un access token (header y body) y un refresh token
 * almacenado en una cookie HttpOnly.</p>
 *
 * @author SPAFEU Backend Team
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	@Value("${cookie.refresh-token-path:/api/session}")
	private String refreshTokenPath;

	private AuthenticationManager authenticationManager;
	private JWTService jWTService;
	private RefreshTokenService refreshTokenService;
	private boolean cookieSecure;

	/**
	 * Construye el filtro de autenticación JWT.
	 *
	 * @param authenticationManager gestor de autenticación de Spring Security
	 * @param jWTService            servicio de generación y validación de tokens JWT
	 * @param refreshTokenService   servicio de gestión de refresh tokens
	 * @param cookieSecure          indica si la cookie de refresh token debe marcarse como Secure
	 */
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTService jWTService,
			RefreshTokenService refreshTokenService, boolean cookieSecure) {
		this.authenticationManager = authenticationManager;
		this.jWTService = jWTService;
		this.refreshTokenService = refreshTokenService;
		this.cookieSecure = cookieSecure;
		setRequiresAuthenticationRequestMatcher(
				PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/api/login"));
	}


	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		Usuario user= new Usuario();
		
		try {
			 user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class) ;
			 
			 if (user.getUsername() == null || user.getUsername().isEmpty() ||
					 user.getPassword() == null ||user.getPassword().isEmpty()) {
		          
				 Map<String, String> body= new HashMap<>();
				 
					body.put(MESSAGE, "Empty");
					body.put("error", "El nombre de usuario y la contraseña son obligatorios");
					response.getWriter().write(new ObjectMapper().writeValueAsString(body));
					response.setContentType(CONTENT_TYPE);
					response.setStatus(HttpStatus.BAD_REQUEST.value());
					return null;
		      }

		} catch (IOException e) {
			logger.error("Error leyendo credenciales del request", e);
		}

		
		UsernamePasswordAuthenticationToken authToken= new UsernamePasswordAuthenticationToken(
				user.getUsername(), user.getPassword());
		
		
		return authenticationManager.authenticate(authToken);
		
	}


    /**
     * Invocado cuando la autenticación es exitosa. Genera un access token que
     * se devuelve en el header y cuerpo de la respuesta, y crea un refresh token
     * opaco que se almacena en una cookie HttpOnly.
     *
     * @param request    petición HTTP entrante
     * @param response   respuesta HTTP saliente
     * @param chain      cadena de filtros
     * @param authResult resultado de la autenticación exitosa
     * @throws IOException      si ocurre un error de escritura en la respuesta
     * @throws ServletException si ocurre un error en el procesamiento del filtro
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        UsuarioDTO usuarioDTO = jWTService.findUser(authResult.getName());

        String token = jWTService.create(authResult, usuarioDTO);

        try {
            String refreshToken = refreshTokenService.createRefreshTokenValue(usuarioDTO.getId());

            ResponseCookie refreshCookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
                    .path(refreshTokenPath)
                    .httpOnly(true)
                    .secure(cookieSecure)
                    .sameSite(SAME_SITE_LAX)
                    .maxAge(REFRESH_TOKEN_EXPIRATION_DAYS * 24L * 60L * 60L)
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        } catch (SpafeuException e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType(CONTENT_TYPE);
            Map<String, String> errorBody = new HashMap<>();
            errorBody.put(MESSAGE, "Error generando sesión de refresco");
            errorBody.put("error", e.getMessage());
            response.getWriter().write(new ObjectMapper().writeValueAsString(errorBody));
            return;
        }

        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);
        Map<String, Object> body = new HashMap<>();
        body.put("token", token);
        body.put("usuario", usuarioDTO);
        body.put(MESSAGE, String.format("Bienvenido %s, ha iniciado sesión con éxito!", authResult.getName()));

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpStatus.OK.value());
    }


	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		
		Map<String, String> body= new HashMap<>();
		if (failed instanceof DisabledException) {
	        body.put(MESSAGE, "Tu cuenta está deshabilitada. Por favor, contacta al administrador.");
	    } else {
	        body.put(MESSAGE, "username/correo o password incorrectos");
	        
	    }
		body.put("error", failed.getMessage());
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setContentType(CONTENT_TYPE);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
	}
	
}
