package edu.co.udes.spafeu.web.app.models.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USER_EXERCISE_SESSION")
@Getter
@Setter
public class UserExerciseSession {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_SESSION")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ID_USERS", nullable = false)
	private Usuario user;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ID_EXERCISE", nullable = false)
	private Exercise exercise;
	
	@Column(name = "START_DATE", nullable = false)
	private LocalDateTime fechaInicio;
	
	@Column(name = "END_DATE")
	private LocalDateTime fechaFin;
	
	@Column(name = "MINUTES_COMPLETED", nullable = false)
	private Integer minutosRealizados;
	
	@Column(name = "COMPLETED", nullable = false)
	private Boolean completado;
}