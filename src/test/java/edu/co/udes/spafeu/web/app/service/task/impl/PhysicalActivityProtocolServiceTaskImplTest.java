package edu.co.udes.spafeu.web.app.service.task.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import lombok.extern.log4j.Log4j2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.co.udes.spafeu.web.app.models.dto.DurationTypeDTO;
import edu.co.udes.spafeu.web.app.models.dto.PhysicalActivityProtocolDTO;
import edu.co.udes.spafeu.web.app.models.dto.ProtocolObjectiveDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.entity.PhysicalActivityProtocolService;
import edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil;
import edu.co.udes.spafeu.web.app.util.Messages;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de PhysicalActivityProtocolServiceTaskImpl")
@Log4j2
class PhysicalActivityProtocolServiceTaskImplTest {

    @Mock
    private Messages messages;

    @Mock
    private PhysicalActivityProtocolService physicalActivityProtocolService;

    @InjectMocks
    private PhysicalActivityProtocolServiceTaskImpl physicalActivityProtocolServiceTask;

    private PhysicalActivityProtocolDTO protocolDTO;
    private List<PhysicalActivityProtocolDTO> protocolDTOs;

    @BeforeEach
    void setUp() {
        DurationTypeDTO durationDTO = new DurationTypeDTO();
        durationDTO.setId(1L);
        durationDTO.setDescription("30 minutos");

        ProtocolObjectiveDTO objectiveDTO = new ProtocolObjectiveDTO();
        objectiveDTO.setId(1L);
        objectiveDTO.setDescription("Mejorar resistencia");

        protocolDTO = new PhysicalActivityProtocolDTO();
        protocolDTO.setId(1L);
        protocolDTO.setName("Protocolo Resistencia");
        protocolDTO.setDescription("Protocolo para mejorar la resistencia cardiovascular");
        protocolDTO.setNivelActividadFisica(NivelActividadFisicaEnum.NIVEL_3);
        protocolDTO.setSuggestedDuration(durationDTO);
        protocolDTO.setObjectives(List.of(objectiveDTO));

        protocolDTOs = List.of(protocolDTO);
    }

    @Nested
    @DisplayName("findAllWithoutExercises()")
    class FindAllWithoutExercises {

        @Test
        @DisplayName("Debe retornar 200 OK con lista de protocolos en RespuestaServicioDTO")
        void debeRetornar200ConListaDeProtocolos() throws Exception {
            when(physicalActivityProtocolService.findAllWithoutExercises()).thenReturn(protocolDTOs);
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_SUCCESS)).thenReturn("Éxito");
            when(messages.getKey(MensajesKeyConstantesUtil.SUCCESS_DETALLE)).thenReturn("Operación exitosa");

            ResponseEntity<RespuestaServicioDTO<List<PhysicalActivityProtocolDTO>>> response =
                    physicalActivityProtocolServiceTask.findAllWithoutExercises();

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            RespuestaServicioDTO<List<PhysicalActivityProtocolDTO>> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isTrue();
            assertThat(body.getNegocio()).isNotNull();
            assertThat(body.getNegocio()).hasSize(1);
            assertThat(body.getNegocio().get(0).getId()).isEqualTo(1L);
            assertThat(body.getMensajeDTO()).isNotNull();
            assertThat(body.getMensajeDTO().getSeverity()).isEqualTo(SeverityEnum.SUCCESS);
            assertThat(body.getMensajeDTO().getSummary()).isEqualTo("Éxito");
            assertThat(body.getMensajeDTO().getDetail()).isEqualTo("Operación exitosa");

            verify(physicalActivityProtocolService, times(1)).findAllWithoutExercises();
            log.info("Test findAllWithoutExercises: retorna 200 OK con {} protocolos", body.getNegocio().size());
        }

        @Test
        @DisplayName("Debe retornar 200 OK con lista vacía cuando no hay protocolos")
        void debeRetornar200ConListaVacia() throws Exception {
            when(physicalActivityProtocolService.findAllWithoutExercises()).thenReturn(List.of());
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_SUCCESS)).thenReturn("Éxito");
            when(messages.getKey(MensajesKeyConstantesUtil.SUCCESS_DETALLE)).thenReturn("Operación exitosa");

            ResponseEntity<RespuestaServicioDTO<List<PhysicalActivityProtocolDTO>>> response =
                    physicalActivityProtocolServiceTask.findAllWithoutExercises();

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            RespuestaServicioDTO<List<PhysicalActivityProtocolDTO>> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isTrue();
            assertThat(body.getNegocio()).isNotNull();
            assertThat(body.getNegocio()).isEmpty();
            assertThat(body.getMensajeDTO()).isNotNull();
            assertThat(body.getMensajeDTO().getSeverity()).isEqualTo(SeverityEnum.SUCCESS);

            verify(physicalActivityProtocolService, times(1)).findAllWithoutExercises();
            log.info("Test findAllWithoutExercises sin datos: retorna 200 OK con lista vacía");
        }
    }

    @Nested
    @DisplayName("findAllByNivelActividadFisicaWithoutExercises(NivelActividadFisicaEnum)")
    class FindAllByNivelActividadFisicaWithoutExercises {

        @Test
        @DisplayName("Debe retornar 200 OK con lista filtrada por nivel en RespuestaServicioDTO")
        void debeRetornar200ConListaFiltrada() throws Exception {
            when(physicalActivityProtocolService.findAllByNivelActividadFisicaWithoutExercises(NivelActividadFisicaEnum.NIVEL_3)).thenReturn(protocolDTO);
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_SUCCESS)).thenReturn("Éxito");
            when(messages.getKey(MensajesKeyConstantesUtil.SUCCESS_DETALLE)).thenReturn("Operación exitosa");

            ResponseEntity<RespuestaServicioDTO<PhysicalActivityProtocolDTO>> response =
                    physicalActivityProtocolServiceTask.findAllByNivelActividadFisicaWithoutExercises(NivelActividadFisicaEnum.NIVEL_3);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            RespuestaServicioDTO<PhysicalActivityProtocolDTO> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isTrue();
            assertThat(body.getNegocio()).isNotNull();
            assertThat(body.getNegocio().getId()).isEqualTo(1L);
            assertThat(body.getMensajeDTO()).isNotNull();
            assertThat(body.getMensajeDTO().getSeverity()).isEqualTo(SeverityEnum.SUCCESS);
            assertThat(body.getMensajeDTO().getSummary()).isEqualTo("Éxito");
            assertThat(body.getMensajeDTO().getDetail()).isEqualTo("Operación exitosa");

            verify(physicalActivityProtocolService, times(1)).findAllByNivelActividadFisicaWithoutExercises(NivelActividadFisicaEnum.NIVEL_3);
            log.info("Test findAllByNivelActividadFisicaWithoutExercises: retorna 200 OK con {} protocolos", body.getNegocio());
        }

        @Test
        @DisplayName("Debe retornar 200 OK con lista vacía cuando no hay protocolos para el nivel")
        void debeRetornar200ConListaVaciaParaNivel() throws Exception {
            when(physicalActivityProtocolService.findAllByNivelActividadFisicaWithoutExercises(NivelActividadFisicaEnum.NIVEL_1)).thenReturn(new PhysicalActivityProtocolDTO());
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_SUCCESS)).thenReturn("Éxito");
            when(messages.getKey(MensajesKeyConstantesUtil.SUCCESS_DETALLE)).thenReturn("Operación exitosa");

            ResponseEntity<RespuestaServicioDTO<PhysicalActivityProtocolDTO>> response =
                    physicalActivityProtocolServiceTask.findAllByNivelActividadFisicaWithoutExercises(NivelActividadFisicaEnum.NIVEL_1);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            RespuestaServicioDTO<PhysicalActivityProtocolDTO> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isTrue();
            assertThat(body.getNegocio()).isNotNull();
            assertThat(body.getMensajeDTO()).isNotNull();
            assertThat(body.getMensajeDTO().getSeverity()).isEqualTo(SeverityEnum.SUCCESS);

            verify(physicalActivityProtocolService, times(1)).findAllByNivelActividadFisicaWithoutExercises(NivelActividadFisicaEnum.NIVEL_1);
            log.info("Test findAllByNivelActividadFisicaWithoutExercises sin datos: retorna 200 OK con lista vacía");
        }
    }
}
