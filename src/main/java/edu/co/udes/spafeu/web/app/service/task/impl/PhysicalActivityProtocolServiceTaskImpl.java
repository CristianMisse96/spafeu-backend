package edu.co.udes.spafeu.web.app.service.task.impl;

import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.KEY_SUCCESS;
import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.SUCCESS_DETALLE;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.PhysicalActivityProtocolDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.entity.PhysicalActivityProtocolService;
import edu.co.udes.spafeu.web.app.service.task.PhysicalActivityProtocolServiceTask;
import edu.co.udes.spafeu.web.app.util.Messages;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripcion: Implementacion de ServiceTask para operaciones REST de PhysicalActivityProtocol.</p>
 *
 * @author Cristian Misse - cgmisse@gmail.com
 */
@Service
@RequiredArgsConstructor
public class PhysicalActivityProtocolServiceTaskImpl implements PhysicalActivityProtocolServiceTask {

    private final Messages messages;
    private final PhysicalActivityProtocolService physicalActivityProtocolService;

    /**
     * Obtiene todos los protocolos de actividad física sin incluir los ejercicios
     * y construye la respuesta de servicio con mensaje de éxito.
     *
     * @return ResponseEntity con {@link RespuestaServicioDTO} conteniendo la lista de protocolos
     */
    @Override
    public ResponseEntity<RespuestaServicioDTO<List<PhysicalActivityProtocolDTO>>> findAllWithoutExercises() {
        List<PhysicalActivityProtocolDTO> resultado = physicalActivityProtocolService.findAllWithoutExercises();
        RespuestaServicioDTO<List<PhysicalActivityProtocolDTO>> respuesta = new RespuestaServicioDTO<>();
        respuesta.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS,
            messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
        respuesta.setOk(true);
        respuesta.setNegocio(resultado);
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    /**
     * Obtiene los protocolos de actividad fisica filtrados por nivel de actividad fisica
     * sin incluir los ejercicios y construye la respuesta de servicio con mensaje de exito.
     *
     * @param nivelActividadFisica el nivel de actividad fisica por el cual filtrar
     * @return ResponseEntity con {@link RespuestaServicioDTO} conteniendo la lista de protocolos filtrada
     */
    @Override
    public ResponseEntity<RespuestaServicioDTO<PhysicalActivityProtocolDTO>> findAllByNivelActividadFisicaWithoutExercises(NivelActividadFisicaEnum nivelActividadFisica) {
        PhysicalActivityProtocolDTO resultado = physicalActivityProtocolService.findAllByNivelActividadFisicaWithoutExercises(nivelActividadFisica);
        RespuestaServicioDTO<PhysicalActivityProtocolDTO> respuesta = new RespuestaServicioDTO<>();
        respuesta.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS,
            messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
        respuesta.setOk(true);
        respuesta.setNegocio(resultado);
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
}
