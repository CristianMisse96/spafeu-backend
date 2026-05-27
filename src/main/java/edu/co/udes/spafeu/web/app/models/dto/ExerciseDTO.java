package edu.co.udes.spafeu.web.app.models.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExerciseDTO implements Serializable {

    private Long id;
    private String name;
    private String descripcionEjercicio;
    private ActivityTypeDTO tipoActividadFisica;
    private IntensityTypeDTO tipoIntensidad;
    private DurationTypeDTO duracion;
    private String foto;

    private static final long serialVersionUID = 1L;
}
