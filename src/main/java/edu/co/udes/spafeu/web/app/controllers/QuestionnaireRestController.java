package edu.co.udes.spafeu.web.app.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.questionnaire.QuestionnaireDTO;
import edu.co.udes.spafeu.web.app.service.task.QuestionnaireServiceTask;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Set;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * Titulo: Proyecto SPAFEU
 * </p>
 * <p>
 * Descripción: Controlador REST para la gestión de cuestionarios de salud en el sistema SPAFEU.
 * Expone el endpoint GET /api/questionnaires para consultar todos los cuestionarios
 * con sus preguntas y opciones asociadas.
 * </p>
 *
 * @author SPAFEU
 */
@Tag(name = "Gestión de Cuestionarios", description = "Endpoints para la gestión de cuestionarios de salud en el sistema SPAFEU")
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/questionnaires")
@RequiredArgsConstructor
public class QuestionnaireRestController {

    private final QuestionnaireServiceTask questionnaireServiceTask;

    /**
     * Lista todos los cuestionarios disponibles en el sistema, incluyendo sus preguntas y opciones.
     *
     * @return ResponseEntity con RespuestaServicioDTO conteniendo la lista de QuestionnaireDTO
     */
    @Operation(
        summary = "Listar todos los cuestionarios",
        description = "Retorna la lista completa de cuestionarios de salud registrados en el sistema, " +
                "cada uno con sus preguntas y opciones de respuesta cargadas."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de cuestionarios obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "Token JWT no válido o expirado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor al consultar los cuestionarios")
    })
    @GetMapping
    public ResponseEntity<RespuestaServicioDTO<Set<QuestionnaireDTO>>> findAll() {
        return questionnaireServiceTask.findAllQuestionnaires();
    }
}
