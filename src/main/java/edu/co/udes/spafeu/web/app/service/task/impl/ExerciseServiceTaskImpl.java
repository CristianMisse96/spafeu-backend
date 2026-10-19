package edu.co.udes.spafeu.web.app.service.task.impl;

import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.KEY_SUCCESS;
import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.SUCCESS_DETALLE;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import edu.co.udes.spafeu.web.app.models.dto.ExerciseCompletionResultDTO;
import edu.co.udes.spafeu.web.app.models.dto.ExerciseDTO;
import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.entity.ExerciseService;
import edu.co.udes.spafeu.web.app.service.task.ExerciseServiceTask;
import edu.co.udes.spafeu.web.app.util.Messages;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripcion: Implementacion de ServiceTask para operaciones REST de Exercise.</p>
 *
 * @author Cristian Misse - cgmisse@gmail.com
 */
@Service
@RequiredArgsConstructor
public class ExerciseServiceTaskImpl implements ExerciseServiceTask {

    private final Messages messages;
    private final ExerciseService exerciseService;

    /**
     * Obtiene un ejercicio por su identificador unico validando
     * que pertenezca al nivel de actividad fisica indicado
     * y construye la respuesta de servicio con mensaje de exito.
     *
     * @param id el identificador del ejercicio
     * @param nivelActividadFisica el nivel de actividad fisica esperado
     * @return ResponseEntity con {@link RespuestaServicioDTO} conteniendo el ejercicio encontrado
     */
    @Override
    public ResponseEntity<RespuestaServicioDTO<ExerciseDTO>> findById(Long id, NivelActividadFisicaEnum nivelActividadFisica, Long userId) {
        ExerciseDTO resultado = exerciseService.findById(id, nivelActividadFisica, userId);
        RespuestaServicioDTO<ExerciseDTO> respuesta = new RespuestaServicioDTO<>();
        respuesta.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS,
            messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
        respuesta.setOk(true);
        respuesta.setNegocio(resultado);
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    /**
     * Obtiene los identificadores de ejercicios completados por un usuario
     * y construye la respuesta de servicio con mensaje de exito.
     *
     * @param userId el identificador del usuario
     * @return ResponseEntity con {@link RespuestaServicioDTO} conteniendo el set de identificadores
     */
    @Override
    public ResponseEntity<RespuestaServicioDTO<Set<Long>>> findCompletedExerciseIdsByUserId(Long userId) {
        Set<Long> resultado = exerciseService.findCompletedExerciseIdsByUserId(userId);
        RespuestaServicioDTO<Set<Long>> respuesta = new RespuestaServicioDTO<>();
        respuesta.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS,
            messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
        respuesta.setOk(true);
        respuesta.setNegocio(resultado);
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    /**
     * Completa la sesion de ejercicio activa de un usuario calculando
     * los minutos realizados, evalua la transicion de nivel y construye
     * la respuesta de servicio con mensaje de exito.
     *
     * @param exerciseId el identificador del ejercicio
     * @param userId el identificador del usuario
     * @return ResponseEntity con {@link RespuestaServicioDTO} indicando el resultado de la operacion
     */
    @Override
    public ResponseEntity<RespuestaServicioDTO<ExerciseCompletionResultDTO>> completeExerciseSession(Long exerciseId, Long userId) {
        ExerciseCompletionResultDTO resultado = exerciseService.completeExerciseSession(exerciseId, userId);
        RespuestaServicioDTO<ExerciseCompletionResultDTO> respuesta = new RespuestaServicioDTO<>();
        respuesta.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS,
            messages.getKey(KEY_SUCCESS), messages.getKey(resultado.getMensaje())));
        respuesta.setOk(true);
        respuesta.setNegocio(resultado);
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
}
