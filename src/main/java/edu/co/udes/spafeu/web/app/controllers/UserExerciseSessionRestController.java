package edu.co.udes.spafeu.web.app.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.UserProgressDashboardDTO;
import edu.co.udes.spafeu.web.app.service.task.UserExerciseSessionServiceTask;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripcion: Controlador REST para gestion de sesiones de ejercicio de usuario.</p>
 *
 * @author SPAFEU
 */
@Tag(name = "Sesiones de Ejercicio", description = "Endpoints para la gestion de sesiones de ejercicio de usuario")
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/user-exercise-sessions")
@RequiredArgsConstructor
public class UserExerciseSessionRestController {

    private final UserExerciseSessionServiceTask userExerciseSessionServiceTask;

    /**
     * Obtiene el dashboard de progreso del usuario autenticado.
     *
     * @return ResponseEntity con RespuestaServicioDTO conteniendo el resumen de sesiones completadas,
     *         minutos totales, ultima actividad y las 5 sesiones mas recientes
     */
    @Operation(
            summary = "Obtener dashboard de progreso del usuario autenticado",
            description = "Retorna el resumen de sesiones completadas, minutos totales, ultima actividad y las 5 sesiones mas recientes. Requiere autenticacion JWT."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dashboard obtenido exitosamente"),
            @ApiResponse(responseCode = "401", description = "Token JWT no valido o expirado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/dashboard")
    public ResponseEntity<RespuestaServicioDTO<UserProgressDashboardDTO>> getUserProgressDashboard() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return userExerciseSessionServiceTask.getUserProgressDashboard(userId);
    }
}
