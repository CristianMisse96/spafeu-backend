package edu.co.udes.spafeu.web.app.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import edu.co.udes.spafeu.web.app.models.dto.UserExerciseSessionDTO;
import edu.co.udes.spafeu.web.app.models.entities.UserExerciseSession;

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
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<UserExerciseSession, UserExerciseSessionDTO>() {
			@Override
			protected void configure() {
				map().setExerciseId(source.getExercise().getId());
				map().setExerciseName(source.getExercise().getName());
				map().setExercisePhoto(source.getExercise().getFoto());
				map().setActivityType(source.getExercise().getTipoActividadFisica().getName());
			}
		});
		return modelMapper;
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
