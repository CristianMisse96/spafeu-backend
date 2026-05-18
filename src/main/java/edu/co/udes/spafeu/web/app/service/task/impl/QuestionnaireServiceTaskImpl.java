package edu.co.udes.spafeu.web.app.service.task.impl;

import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.KEY_SUCCESS;
import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.SUCCESS_DETALLE;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.questionnaire.QuestionnaireDTO;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.entity.QuestionnaireService;
import edu.co.udes.spafeu.web.app.service.task.QuestionnaireServiceTask;
import edu.co.udes.spafeu.web.app.util.Messages;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Implementación de QuestionnaireServiceTask para la consulta de cuestionarios.
 * Delega completamente al Service y construye la RespuestaServicioDTO con el HttpStatus apropiado.</p>
 * @author AGENT-TASK-EXPERT-SPAFEU
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class QuestionnaireServiceTaskImpl implements QuestionnaireServiceTask {

    private final Messages messages;
    private final QuestionnaireService questionnaireService;

    /**
     * {@inheritDoc}
     * <p>
     * Delega la consulta al Service, construye la RespuestaServicioDTO con el mensaje de éxito
     * y retorna la respuesta envuelta en un ResponseEntity con HttpStatus.OK.
     * </p>
     */
    @Override
    public ResponseEntity<RespuestaServicioDTO<Set<QuestionnaireDTO>>> findAllQuestionnaires() {
        RespuestaServicioDTO<Set<QuestionnaireDTO>> respuesta = new RespuestaServicioDTO<>();

        Set<QuestionnaireDTO> resultado = questionnaireService.findAllQuestionnaires();

        respuesta.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS,
            messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
        respuesta.setOk(true);
        respuesta.setNegocio(resultado);

        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
}
