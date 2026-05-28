package edu.co.udes.spafeu.web.app.service.entity;

import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.UserProgressDashboardDTO;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Interfaz Service con lógica de negocio para la gestión de sesiones de ejercicio de usuario.</p>
 * @author SPAFEU
 */
public interface UserExerciseSessionService {

    /**
     * Obtiene el dashboard de progreso del usuario autenticado.
     *
     * @param userId el identificador del usuario
     * @return DTO con el resumen de progreso y sesiones recientes
     * @throws SpafeuException si ocurre un error de negocio
     */
    UserProgressDashboardDTO getUserProgressDashboard(Long userId) throws SpafeuException;
}
