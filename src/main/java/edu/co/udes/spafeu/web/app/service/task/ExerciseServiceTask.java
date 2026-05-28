package edu.co.udes.spafeu.web.app.service.task;

import org.springframework.http.ResponseEntity;

import edu.co.udes.spafeu.web.app.models.dto.ExerciseDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripcion: Interfaz ServiceTask para operaciones REST de Exercise.</p>
 *
 * @author Cristian Misse - cgmisse@gmail.com
 */
public interface ExerciseServiceTask {

    /**
     * Obtiene un ejercicio por su identificador unico validando
     * que pertenezca al nivel de actividad fisica indicado.
     *
     * @param id el identificador del ejercicio
     * @param nivelActividadFisica el nivel de actividad fisica esperado
     * @param userId 
     * @return ResponseEntity con {@link RespuestaServicioDTO} conteniendo el ejercicio encontrado
     */
    ResponseEntity<RespuestaServicioDTO<ExerciseDTO>> findById(Long id, NivelActividadFisicaEnum nivelActividadFisica, Long userId);
}
