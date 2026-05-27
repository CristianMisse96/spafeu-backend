package edu.co.udes.spafeu.web.app.models.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: DTO para el tipo de duración.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DurationTypeDTO implements Serializable {

    private Long id;
    private String description;

    private static final long serialVersionUID = 1L;
}
