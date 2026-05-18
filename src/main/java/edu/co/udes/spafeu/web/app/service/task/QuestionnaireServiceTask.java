package edu.co.udes.spafeu.web.app.service.task;

import org.springframework.http.ResponseEntity;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.questionnaire.QuestionnaireDTO;
import java.util.Set;

/**
 * <p>
 * Titulo: Proyecto SPAFEU
 * </p>
 * <p>
 * Descripción: Interfaz ServiceTask para operaciones REST de Questionnaire.
 * Define el contrato que debe cumplir la implementación encargada de construir
 * la RespuestaServicioDTO y delegar la lógica al Service.
 * </p>
 *
 * @author AGENT-TASK-EXPERT-SPAFEU
 */
public interface QuestionnaireServiceTask {

    /**
     * Obtiene la lista de todos los cuestionarios con sus preguntas y opciones cargadas.
     * Delega la consulta al Service y construye la RespuestaServicioDTO con el HttpStatus apropiado.
     *
     * @return ResponseEntity con la lista de {@link QuestionnaireDTO} envuelta en {@link RespuestaServicioDTO}
     */
    ResponseEntity<RespuestaServicioDTO<Set<QuestionnaireDTO>>> findAllQuestionnaires();
}
