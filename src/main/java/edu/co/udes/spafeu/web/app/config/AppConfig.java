package edu.co.udes.spafeu.web.app.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * <p>
 * Titulo: Proyecto SPAFEU
 * </p>
 * <p>
 * Descripción: Clase de configuración de la aplicación encargada de proveer beans.
 * </p>
 *
 * @author Cristian Misse
 *
 **/

@Configuration
public class AppConfig {
	
	@Bean
    ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
