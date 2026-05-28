package edu.co.udes.spafeu.web.app.service.task;

import java.util.Set;

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

    /**
     * Obtiene los identificadores de ejercicios completados por un usuario
     * y construye la respuesta de servicio con mensaje de exito.
     *
     * @param userId el identificador del usuario
     * @return ResponseEntity con {@link RespuestaServicioDTO} conteniendo el set de identificadores
     */
    ResponseEntity<RespuestaServicioDTO<Set<Long>>> findCompletedExerciseIdsByUserId(Long userId);

    /**
     * Completa la sesion de ejercicio activa de un usuario calculando
     * los minutos realizados y construye la respuesta de servicio con mensaje de exito.
     *
     * @param exerciseId el identificador del ejercicio
     * @param userId el identificador del usuario
     * @return ResponseEntity con {@link RespuestaServicioDTO} indicando la finalizacion del ejercicio
     */
    ResponseEntity<RespuestaServicioDTO<Void>> completeExerciseSession(Long exerciseId, Long userId);
}
