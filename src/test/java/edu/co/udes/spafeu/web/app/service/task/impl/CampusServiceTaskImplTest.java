package edu.co.udes.spafeu.web.app.service.task.impl;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
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
import edu.co.udes.spafeu.web.app.models.dto.AcademicProgramDTO;
import edu.co.udes.spafeu.web.app.models.dto.CampusDTO;
import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.entity.CampusService;
import edu.co.udes.spafeu.web.app.util.Messages;
import edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil;
import lombok.extern.log4j.Log4j2;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de CampusServiceTaskImpl")
@Log4j2
class CampusServiceTaskImplTest {

    @Mock
    private Messages messages;

    @Mock
    private CampusService campusService;

    @InjectMocks
    private CampusServiceTaskImpl campusServiceTask;

    private CampusDTO campusDTO;
    private List<CampusDTO> campusDTOs;

    @BeforeEach
    void setUp() {
        campusDTO = new CampusDTO();
        campusDTO.setId(1L);
        campusDTO.setName("Campus Principal");

        AcademicProgramDTO programDTO = new AcademicProgramDTO();
        programDTO.setId(10L);
        programDTO.setName("Ingeniería de Sistemas");
        campusDTO.setAcademicPrograms(List.of(programDTO));

        campusDTOs = new ArrayList<>();
        campusDTOs.add(campusDTO);
    }

    @Nested
    @DisplayName("findAllCampusesWithPrograms()")
    class FindAllCampusesWithPrograms {

        @Test
        @DisplayName("Debe retornar ResponseEntity con RespuestaServicioDTO y HttpStatus.OK")
        void debeRetornarResponseEntityConRespuestaServicio() {
            when(campusService.findAllCampusesWithPrograms()).thenReturn(campusDTOs);
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_SUCCESS)).thenReturn("Éxito");
            when(messages.getKey(MensajesKeyConstantesUtil.SUCCESS_DETALLE)).thenReturn("Operación exitosa");

            ResponseEntity<RespuestaServicioDTO<List<CampusDTO>>> response = campusServiceTask.findAllCampusesWithPrograms();

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            RespuestaServicioDTO<List<CampusDTO>> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isTrue();
            assertThat(body.getNegocio()).hasSize(1);
            assertThat(body.getMensajeDTO()).isNotNull();
            assertThat(body.getMensajeDTO().getSeverity()).isEqualTo(SeverityEnum.SUCCESS);

            verify(campusService, times(1)).findAllCampusesWithPrograms();
            log.info("Test findAllCampusesWithPrograms: retorna ResponseEntity con {} campus", body.getNegocio().size());
        }

        @Test
        @DisplayName("Debe retornar lista vacía cuando el service no devuelve campus")
        void debeRetornarListaVaciaCuandoServiceNoDevuelveDatos() {
            when(campusService.findAllCampusesWithPrograms()).thenReturn(List.of());
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_SUCCESS)).thenReturn("Éxito");
            when(messages.getKey(MensajesKeyConstantesUtil.SUCCESS_DETALLE)).thenReturn("Operación exitosa");

            ResponseEntity<RespuestaServicioDTO<List<CampusDTO>>> response = campusServiceTask.findAllCampusesWithPrograms();

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().getNegocio()).isEmpty();
            verify(campusService, times(1)).findAllCampusesWithPrograms();
            log.info("Test findAllCampusesWithPrograms sin datos: retorna ResponseEntity con lista vacía");
        }
    }
}
