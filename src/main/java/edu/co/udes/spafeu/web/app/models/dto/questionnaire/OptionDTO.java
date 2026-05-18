package edu.co.udes.spafeu.web.app.models.dto.questionnaire;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * Titulo: Proyecto SPAFEU
 * </p>
 * <p>
 * Descripción: DTO de salida para la opción (Option) de una pregunta del cuestionario.
 * Se utiliza dentro de QuestionDTO para representar cada alternativa de respuesta.
 * </p>
 *
 * @author AGENT-DATA-EXPERT-SPAFEU
 */
@Getter
@Setter
@NoArgsConstructor
public class OptionDTO {

    private Long id;

    private String label;
}
