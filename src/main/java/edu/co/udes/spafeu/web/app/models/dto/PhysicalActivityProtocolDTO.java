package edu.co.udes.spafeu.web.app.models.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: DTO para protocolos de actividad física.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhysicalActivityProtocolDTO implements Serializable {

    private Long id;
    private NivelActividadFisicaEnum nivelActividadFisica;
    private String name;
    private String description;
    private DurationTypeDTO suggestedDuration;
    private List<ProtocolObjectiveDTO> objectives;

    private static final long serialVersionUID = 1L;
}
