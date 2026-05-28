package edu.co.udes.spafeu.web.app.models.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: DTO para el dashboard de progreso del usuario.</p>
 *
 * @author SPAFEU
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "DTO para el dashboard de progreso del usuario")
public class UserProgressDashboardDTO implements Serializable {

    @Schema(description = "Total de sesiones de ejercicio completadas por el usuario", example = "42")
    private Long totalSesiones;

    @Schema(description = "Suma total de minutos realizados en todas las sesiones completadas", example = "1260")
    private Integer totalMinutos;

    @Schema(description = "Fecha de inicio de la sesión más reciente", example = "2026-05-28T10:15:30")
    private LocalDateTime ultimaActividad;

    @Schema(description = "Lista de las últimas 5 sesiones de ejercicio completadas")
    private List<UserExerciseSessionDTO> sesionesRecientes;

    private static final long serialVersionUID = 1L;
}
