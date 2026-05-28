package edu.co.udes.spafeu.web.app.service.entity;

import java.util.Set;

import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.ExerciseDTO;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripcion: Interfaz Service con logica de negocio para Exercise.</p>
 * @author Cristian Misse - cgmisse@gmail.com
 */
public interface ExerciseService {

    /**
     * Obtiene un ejercicio por su identificador unico validando
     * que pertenezca al nivel de actividad fisica indicado.
     *
     * @param id el identificador del ejercicio
     * @param nivelActividadFisica el nivel de actividad fisica esperado
     * @param userId 
     * @return {@link ExerciseDTO} con la informacion del ejercicio encontrado
     * @throws SpafeuException si ocurre un error de negocio, el ejercicio no existe
     *                         o no pertenece al nivel de actividad fisica indicado
     */
    ExerciseDTO findById(Long id, NivelActividadFisicaEnum nivelActividadFisica, Long userId) throws SpafeuException;

    /**
     * Obtiene los identificadores de ejercicios completados por un usuario.
     *
     * @param userId el identificador del usuario
     * @return {@link Set} con los identificadores de ejercicios completados
     * @throws SpafeuException si ocurre un error de negocio
     */
    Set<Long> findCompletedExerciseIdsByUserId(Long userId) throws SpafeuException;

    /**
     * Completa la sesión de ejercicio activa de un usuario calculando
     * los minutos realizados entre la fecha de inicio y la fecha de fin.
     *
     * @param exerciseId el identificador del ejercicio
     * @param userId el identificador del usuario
     * @throws SpafeuException si no existe una sesión activa para completar
     */
    void completeExerciseSession(Long exerciseId, Long userId) throws SpafeuException;
}
