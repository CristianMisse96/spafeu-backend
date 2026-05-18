package edu.co.udes.spafeu.web.app.config;

import static edu.co.udes.spafeu.web.app.util.SpringSecurityConstantes.HEADER_AUTHORIZATION;
import static edu.co.udes.spafeu.web.app.util.SpringSecurityConstantes.ROLE_USER;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import edu.co.udes.spafeu.web.app.auth.filter.JWTAuthenticationFilter;
import edu.co.udes.spafeu.web.app.auth.filter.JWTValidationFilter;
import edu.co.udes.spafeu.web.app.auth.service.JWTService;

@Configuration
public class SpringSecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTService jwtService;

    public SpringSecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTService jwtService) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtService = jwtService;
    }

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        return http.authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/recurso/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/forgot-password").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/reset-password").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/isemail/{email}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/isusername/{username}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user/kyc/register").hasRole(ROLE_USER)
                        .requestMatchers(HttpMethod.GET, "/api/user/kyc/recurso/**").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v2/api-docs", "/v3/api-docs",
                                "/v3/api-docs/swagger-config", "/swagger-resources/**", "/webjars/**").permitAll()
                        // Módulo Questionnaire - requiere rol USER para listar cuestionarios
                        .requestMatchers(HttpMethod.GET, "/api/questionnaires/all").permitAll()//.hasRole(ROLE_USER)
                        .anyRequest().authenticated())
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtService))
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
        config.setAllowedOriginPatterns(Arrays.asList("*"));
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

    @Bean
   FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return corsBean;
    }
}