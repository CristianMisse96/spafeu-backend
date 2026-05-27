package edu.co.udes.spafeu.web.app.models.dto;

import java.io.Serializable;
import java.util.List;
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
public class ExerciseGroupedByActivityTypeDTO implements Serializable {

    private String activityType;
    private List<ExerciseDTO> exercises;

    private static final long serialVersionUID = 1L;
}
