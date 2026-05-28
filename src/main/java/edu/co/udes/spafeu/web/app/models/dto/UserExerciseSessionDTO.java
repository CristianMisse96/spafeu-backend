package edu.co.udes.spafeu.web.app.models.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserExerciseSessionDTO {
	
	 private Long id;
     private Long exerciseId;
     private String exerciseName;
     private String exercisePhoto;
     private String activityType;
     private LocalDateTime fechaInicio;
     private LocalDateTime fechaFin;
     private Integer minutosRealizados;
     private Boolean completado;
}
