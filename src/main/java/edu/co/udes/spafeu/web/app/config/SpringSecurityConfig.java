package edu.co.udes.spafeu.web.app.config;

import static edu.co.udes.spafeu.web.app.util.SpringSecurityConstantes.HEADER_AUTHORIZATION;
import static edu.co.udes.spafeu.web.app.util.SpringSecurityConstantes.ROLE_USER;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import edu.co.udes.spafeu.web.app.auth.filter.JWTAuthenticationFilter;
import edu.co.udes.spafeu.web.app.auth.filter.JWTValidationFilter;
import edu.co.udes.spafeu.web.app.auth.service.JWTService;
import edu.co.udes.spafeu.web.app.service.entity.RefreshTokenService;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Configuración central de seguridad de la aplicación. Define
 * la cadena de filtros JWT, las reglas de autorización de endpoints, la
 * política de sesión sin estado y la configuración CORS.</p>
 *
 * @author SPAFEU Backend Team
 */
@Configuration
public class SpringSecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Value("${cors.allowed-origins:http://localhost:4200}")
    private String corsAllowedOrigins;

    @Value("${cookie.secure:false}")
    private boolean cookieSecure;

    public SpringSecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTService jwtService,
            RefreshTokenService refreshTokenService) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        return http.authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/recurso/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/forgot-password").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/reset-password").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/activate-account").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/resend-activation-email").permitAll()
                        // Módulo Refresh Token - endpoints públicos para rotar y revocar sesiones
                        .requestMatchers(HttpMethod.POST, "/api/session/refresh").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/session/logout").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/isemail/{email}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/isusername/{username}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user/kyc/register").hasRole(ROLE_USER)
                        .requestMatchers(HttpMethod.GET, "/api/user/kyc/recurso/**").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v2/api-docs", "/v3/api-docs",
                                "/v3/api-docs/swagger-config", "/swagger-resources/**", "/webjars/**").permitAll()
                        // Módulo Questionnaire - requiere rol USER para listar cuestionarios
                        .requestMatchers(HttpMethod.GET, "/api/questionnaires/all").permitAll()//.hasRole(ROLE_USER)
                        .requestMatchers(HttpMethod.GET, "/api/campuses").permitAll()
                        // Módulo Physical Activity Protocols - endpoint público para listar protocolos
                        .requestMatchers(HttpMethod.GET, "/api/physical-activity-protocols").permitAll()
                        // Módulo Physical Activity Protocols - endpoint público para listar ejercicios
                        .requestMatchers(HttpMethod.GET, "/api/physical-activity-protocols/exercises").permitAll()
                        // Módulo Exercises - endpoint autenticado para obtener ejercicios completados del usuario
                        .requestMatchers(HttpMethod.GET, "/api/exercises/completed").authenticated()
                        // Módulo Exercises - endpoint público para consultar ejercicio por ID
                        .requestMatchers(HttpMethod.GET, "/api/exercises/{id}").permitAll()
                        // Módulo Exercises - endpoint autenticado para marcar ejercicio como completado
                        .requestMatchers(HttpMethod.PATCH, "/api/exercises/{exerciseId}/complete").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/user/health-profile/register").hasRole(ROLE_USER)
                        .anyRequest().authenticated())
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtService, refreshTokenService, cookieSecure))
                .addFilter(new JWTValidationFilter(authenticationManager(), jwtService))
                .csrf(config -> config.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // Orígenes explícitos para permitir el envío de credenciales (cookies HttpOnly)
        config.setAllowedOrigins(Arrays.asList(corsAllowedOrigins.split(",")));
        config.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name()
        ));
        config.setAllowedHeaders(Arrays.asList(HEADER_AUTHORIZATION, "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
