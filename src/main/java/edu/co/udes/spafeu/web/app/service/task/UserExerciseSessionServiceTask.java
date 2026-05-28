package edu.co.udes.spafeu.web.app.service.task;

import org.springframework.http.ResponseEntity;

import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.UserProgressDashboardDTO;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Interfaz ServiceTask para operaciones REST de sesiones de ejercicio de usuario.</p>
 * @author SPAFEU
 */
public interface UserExerciseSessionServiceTask {

    /**
     * Obtiene el dashboard de progreso del usuario autenticado.
     *
     * @param userId el identificador del usuario
     * @return ResponseEntity con el DTO de progreso encapsulado en RespuestaServicioDTO
     */
    ResponseEntity<RespuestaServicioDTO<UserProgressDashboardDTO>> getUserProgressDashboard(Long userId);
}
