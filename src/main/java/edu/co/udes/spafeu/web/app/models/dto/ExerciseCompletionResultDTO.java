package edu.co.udes.spafeu.web.app.models.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * Titulo: Proyecto SPAFEU
 * </p>
 * <p>
 * Descripción: DTO que representa el resultado de completar un ejercicio y la posible transición de nivel.
 * </p>
 *
 * @author AGENT-DATA-EXPERT-SPAFEU
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExerciseCompletionResultDTO implements Serializable {

    private Boolean subioNivel;
    private Boolean ultimoNivel;
    private NivelActividadFisicaEnum nivelAnterior;
    private NivelActividadFisicaEnum nivelNuevo;
    private String mensaje;

    private static final long serialVersionUID = 1L;
}
