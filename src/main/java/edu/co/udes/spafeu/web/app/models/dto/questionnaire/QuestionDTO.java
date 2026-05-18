package edu.co.udes.spafeu.web.app.models.dto.questionnaire;

import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * Titulo: Proyecto SPAFEU
 * </p>
 * <p>
 * Descripción: DTO de salida para la pregunta (Question) con sus opciones de respuesta.
 * Se utiliza dentro de QuestionnaireDTO para representar cada pregunta del cuestionario.
 * </p>
 *
 * @author AGENT-DATA-EXPERT-SPAFEU
 */
@Getter
@Setter
@NoArgsConstructor
public class QuestionDTO {

    private Long id;

    private String text;

    private Set<OptionDTO> options;
}
