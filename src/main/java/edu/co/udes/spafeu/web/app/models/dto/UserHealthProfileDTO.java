package edu.co.udes.spafeu.web.app.models.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import edu.co.udes.spafeu.web.app.models.enums.SexEnum;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    @Size(max = 100)
    private String fullName;

    @NotNull
    private Long campusId;

    @NotNull
    private Long programId;

    @NotNull
    @Min(value = 1)
    @Max(value = 12)
    private Integer semester;

    @NotNull
    @Past
    private LocalDate birthDate;

    @NotNull
    private SexEnum sex;

    @NotNull
    @Digits(integer = 5, fraction = 2)
    private BigDecimal weightKg;

    @NotNull
    @Digits(integer = 4, fraction = 2)
    private BigDecimal heightM;
    
    @NotNull
    private List<UserAnswerDTO> answers;

    private static final long serialVersionUID = 1L;
}
