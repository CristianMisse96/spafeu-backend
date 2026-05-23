package edu.co.udes.spafeu.web.app.models.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import edu.co.udes.spafeu.web.app.models.enums.SexEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserHealthProfileDTO implements Serializable {

    @NotNull
    @Size(max = 100)
    private String fullName;

    @NotNull
    private Long campusId;

    @NotNull
    private Long programId;

    @NotNull
    private Integer semester;

    @NotNull
    @Past
    private LocalDate birthDate;

    @NotNull
    private SexEnum sex;

    @NotNull
    @DecimalMin(value = "0.1", inclusive = false)
    private BigDecimal weightKg;

    @NotNull
    @DecimalMin(value = "0.1", inclusive = false)
    private BigDecimal heightM;

    private List<UserAnswerDTO> answers;

    private static final long serialVersionUID = 1L;
}
