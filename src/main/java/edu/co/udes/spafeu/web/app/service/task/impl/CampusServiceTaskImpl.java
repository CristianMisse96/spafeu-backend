package edu.co.udes.spafeu.web.app.service.task.impl;

import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.KEY_SUCCESS;
import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.SUCCESS_DETALLE;

import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import edu.co.udes.spafeu.web.app.models.dto.CampusDTO;
import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.entity.CampusService;
import edu.co.udes.spafeu.web.app.service.task.CampusServiceTask;
import edu.co.udes.spafeu.web.app.util.Messages;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CampusServiceTaskImpl implements CampusServiceTask {

    private final Messages messages;
    private final CampusService campusService;

    @Override
    public ResponseEntity<RespuestaServicioDTO<List<CampusDTO>>> findAllCampusesWithPrograms() {
        List<CampusDTO> resultado = campusService.findAllCampusesWithPrograms();

        RespuestaServicioDTO<List<CampusDTO>> respuesta = new RespuestaServicioDTO<>();
        respuesta.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS,
            messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
        respuesta.setOk(true);
        respuesta.setNegocio(resultado);

        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
}
