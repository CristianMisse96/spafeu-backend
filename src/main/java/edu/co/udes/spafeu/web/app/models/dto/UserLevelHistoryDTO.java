package edu.co.udes.spafeu.web.app.models.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

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
 * Descripción: DTO para el histórico de cambios de nivel de actividad física de un usuario.
 * </p>
 *
 * @author AGENT-DATA-EXPERT-SPAFEU
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLevelHistoryDTO implements Serializable {

    private Long id;
    private Long userId;
    private NivelActividadFisicaEnum nivelAnterior;
    private NivelActividadFisicaEnum nivelNuevo;
    private LocalDateTime fechaCambio;
    private String observacion;

    private static final long serialVersionUID = 1L;
}
