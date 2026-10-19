package edu.co.udes.spafeu.web.app.service.task.impl;

import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.KEY_SUCCESS;
import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.SUCCESS_DETALLE;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.UserLevelHistoryDTO;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.entity.UserLevelHistoryService;
import edu.co.udes.spafeu.web.app.service.task.UserLevelHistoryServiceTask;
import edu.co.udes.spafeu.web.app.util.Messages;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * Titulo: Proyecto SPAFEU
 * </p>
 * <p>
 * Descripción: Implementación de ServiceTask para operaciones REST de UserLevelHistory.
 * </p>
 *
 * @author Cristian Misse - cgmisse@gmail.com
 */
@Service
@RequiredArgsConstructor
public class UserLevelHistoryServiceTaskImpl implements UserLevelHistoryServiceTask {

    private final Messages messages;
    private final UserLevelHistoryService userLevelHistoryService;

    /**
     * Obtiene el historico de niveles de un usuario ordenado descendientemente por fecha
     * y construye la respuesta de servicio con mensaje de exito.
     *
     * @param userId el identificador del usuario
     * @return ResponseEntity con {@link RespuestaServicioDTO} conteniendo el historico de niveles
     */
    @Override
    public ResponseEntity<RespuestaServicioDTO<List<UserLevelHistoryDTO>>> findByUserId(Long userId) {
        List<UserLevelHistoryDTO> resultado = userLevelHistoryService.findByUserId(userId);
        RespuestaServicioDTO<List<UserLevelHistoryDTO>> respuesta = new RespuestaServicioDTO<>();
        respuesta.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS,
            messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
        respuesta.setOk(true);
        respuesta.setNegocio(resultado);
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
}
