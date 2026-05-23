package edu.co.udes.spafeu.web.app.service.task.impl;

import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.KEY_SUCCESS;
import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.PERFIL_SALUD_REGISTRO_EXITOSO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.UserHealthProfileDTO;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.entity.UserHealthProfileService;
import edu.co.udes.spafeu.web.app.service.task.UserHealthProfileServiceTask;
import edu.co.udes.spafeu.web.app.util.Messages;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserHealthProfileServiceTaskImpl implements UserHealthProfileServiceTask {

    private final Messages messages;
    private final UserHealthProfileService userHealthProfileService;

    @Override
    public ResponseEntity<RespuestaServicioDTO<UserHealthProfileDTO>> registerHealthProfile(UserHealthProfileDTO dto, Long userId) {
        UserHealthProfileDTO resultado = userHealthProfileService.registerHealthProfile(dto, userId);

        RespuestaServicioDTO<UserHealthProfileDTO> respuesta = new RespuestaServicioDTO<>();
        respuesta.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS,
            messages.getKey(KEY_SUCCESS), messages.getKey(PERFIL_SALUD_REGISTRO_EXITOSO)));
        respuesta.setOk(true);
        respuesta.setNegocio(resultado);

        return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
    }
}
