package edu.co.udes.spafeu.web.app.controllers;

import java.util.Set;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.co.udes.spafeu.web.app.models.dto.ExerciseDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import edu.co.udes.spafeu.web.app.service.task.ExerciseServiceTask;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripcion: Controlador REST para gestion de ejercicios.</p>
 *
 * @author Cristian Misse - cgmisse@gmail.com
 */
@Tag(name = "Ejercicios", description = "Endpoints para la gestion de ejercicios")
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseRestController {

    private final ExerciseServiceTask exerciseServiceTask;

    /**
     * Obtiene un ejercicio por su identificador unico validando
     * que pertenezca al nivel de actividad fisica indicado.
     *
     * @param id el identificador del ejercicio
     * @param nivelActividadFisica el nivel de actividad fisica esperado
     * @return ResponseEntity con RespuestaServicioDTO conteniendo el ejercicio encontrado
     */
    @Operation(summary = "Obtener ejercicio por ID y nivel de actividad fisica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ejercicio obtenido exitosamente"),
            @ApiResponse(responseCode = "400", description = "Parametro de identificador o nivel no valido o faltante"),
            @ApiResponse(responseCode = "401", description = "Token JWT no valido o expirado"),
            @ApiResponse(responseCode = "403", description = "Ejercicio no pertenece al nivel de actividad fisica indicado"),
            @ApiResponse(responseCode = "404", description = "Ejercicio no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RespuestaServicioDTO<ExerciseDTO>> findById(
            @Parameter(description = "Identificador unico del ejercicio", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nivel de actividad fisica para validar el ejercicio", required = true)
            @RequestParam NivelActividadFisicaEnum nivelActividadFisica) {
    	
    	Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getDetails();
    	
        return exerciseServiceTask.findById(id, nivelActividadFisica, userId);
    }

    /**
     * Obtiene los identificadores de los ejercicios completados por el usuario autenticado.
     *
     * @return ResponseEntity con RespuestaServicioDTO conteniendo el set de identificadores de ejercicios completados
     */
    @Operation(
            summary = "Obtener ejercicios completados por el usuario autenticado",
            description = "Retorna el conjunto de identificadores de ejercicios que el usuario autenticado ha completado. Requiere autenticacion JWT."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ejercicios completados obtenidos exitosamente"),
            @ApiResponse(responseCode = "401", description = "Token JWT no valido o expirado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/completed")
    public ResponseEntity<RespuestaServicioDTO<Set<Long>>> findCompletedExerciseIdsByUserId() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return exerciseServiceTask.findCompletedExerciseIdsByUserId(userId);
    }

    /**
     * Completa la sesión de ejercicio activa del usuario autenticado
     * calculando los minutos realizados entre la fecha de inicio y la fecha de fin.
     *
     * @param exerciseId el identificador del ejercicio a completar
     * @return ResponseEntity con RespuestaServicioDTO indicando la finalización del ejercicio
     */
    @Operation(
            summary = "Completar sesión de ejercicio",
            description = "Finaliza la sesión de ejercicio activa del usuario autenticado, calcula los minutos realizados entre la fecha de inicio y la fecha de fin, y marca el ejercicio como completado. Requiere autenticación JWT."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ejercicio completado exitosamente"),
            @ApiResponse(responseCode = "400", description = "No existe una sesión activa para este ejercicio"),
            @ApiResponse(responseCode = "401", description = "Token JWT no valido o expirado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{exerciseId}/complete")
    public ResponseEntity<RespuestaServicioDTO<Void>> completeExerciseSession(
            @Parameter(description = "Identificador unico del ejercicio", required = true)
            @PathVariable Long exerciseId) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return exerciseServiceTask.completeExerciseSession(exerciseId, userId);
    }
}
