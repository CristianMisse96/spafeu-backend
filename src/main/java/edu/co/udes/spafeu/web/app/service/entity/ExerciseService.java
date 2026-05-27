package edu.co.udes.spafeu.web.app.service.entity;

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
     * @return {@link ExerciseDTO} con la informacion del ejercicio encontrado
     * @throws SpafeuException si ocurre un error de negocio, el ejercicio no existe
     *                         o no pertenece al nivel de actividad fisica indicado
     */
    ExerciseDTO findById(Long id, NivelActividadFisicaEnum nivelActividadFisica) throws SpafeuException;
}
