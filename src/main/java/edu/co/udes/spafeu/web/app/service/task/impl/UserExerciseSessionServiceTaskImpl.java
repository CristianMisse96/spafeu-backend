package edu.co.udes.spafeu.web.app.service.task.impl;

import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.KEY_SUCCESS;
import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.SUCCESS_DETALLE;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.UserProgressDashboardDTO;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.entity.UserExerciseSessionService;
import edu.co.udes.spafeu.web.app.service.task.UserExerciseSessionServiceTask;
import edu.co.udes.spafeu.web.app.util.Messages;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Implementación de UserExerciseSessionServiceTask para operaciones REST de sesiones de ejercicio de usuario.</p>
 * @author SPAFEU
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class UserExerciseSessionServiceTaskImpl implements UserExerciseSessionServiceTask {

    private final Messages messages;
    private final UserExerciseSessionService userExerciseSessionService;

    /**
     * Obtiene el dashboard de progreso del usuario autenticado.
     *
     * @param userId el identificador del usuario
     * @return ResponseEntity con el DTO de progreso encapsulado en RespuestaServicioDTO
     */
    @Override
    public ResponseEntity<RespuestaServicioDTO<UserProgressDashboardDTO>> getUserProgressDashboard(Long userId) {
        RespuestaServicioDTO<UserProgressDashboardDTO> respuesta = new RespuestaServicioDTO<>();
        UserProgressDashboardDTO resultado = userExerciseSessionService.getUserProgressDashboard(userId);
        respuesta.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS,
            messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
        respuesta.setOk(true);
        respuesta.setNegocio(resultado);
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
}
