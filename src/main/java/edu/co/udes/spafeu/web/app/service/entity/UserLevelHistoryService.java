package edu.co.udes.spafeu.web.app.service.entity;

import java.util.List;
import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.UserLevelHistoryDTO;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;

/**
 * <p>
 * Titulo: Proyecto SPAFEU
 * </p>
 * <p>
 * Descripción: Interfaz Service con lógica de negocio para UserLevelHistory.
 * </p>
 *
 * @author Cristian Misse - cgmisse@gmail.com
 */
public interface UserLevelHistoryService {

    /**
     * Registra el nivel inicial de actividad fisica de un usuario durante el onboarding.
     *
     * @param userId    identificador del usuario
     * @param nivelNuevo nivel de actividad fisica asignado
     * @return {@link UserLevelHistoryDTO} con el registro creado
     * @throws SpafeuException si el usuario no existe
     */
    UserLevelHistoryDTO registrarNivelInicial(Long userId, NivelActividadFisicaEnum nivelNuevo) throws SpafeuException;

    /**
     * Registra un cambio de nivel de actividad fisica de un usuario.
     *
     * @param userId        identificador del usuario
     * @param nivelAnterior nivel de actividad fisica anterior
     * @param nivelNuevo    nuevo nivel de actividad fisica
     * @param observacion   observacion del cambio
     * @return {@link UserLevelHistoryDTO} con el registro creado
     * @throws SpafeuException si el usuario no existe
     */
    UserLevelHistoryDTO registrarCambioNivel(Long userId, NivelActividadFisicaEnum nivelAnterior,
            NivelActividadFisicaEnum nivelNuevo, String observacion) throws SpafeuException;

    /**
     * Obtiene el historico de niveles de un usuario ordenado descendientemente por fecha.
     *
     * @param userId identificador del usuario
     * @return lista de {@link UserLevelHistoryDTO}
     * @throws SpafeuException si ocurre un error de negocio
     */
    List<UserLevelHistoryDTO> findByUserId(Long userId) throws SpafeuException;
}
