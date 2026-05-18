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
 * Descripción: DTO de salida para el cuestionario (Questionnaire) con sus preguntas y opciones.
 * Se utiliza para exponer la información de los cuestionarios de salud al frontend.
 * </p>
 *
 * @author AGENT-DATA-EXPERT-SPAFEU
 */
@Getter
@Setter
@NoArgsConstructor
public class QuestionnaireDTO {

    private Long id;

    private String name;

    private Set<QuestionDTO> questions;
}
