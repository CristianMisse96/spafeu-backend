package edu.co.udes.spafeu.web.app.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.co.udes.spafeu.web.app.models.dto.CampusDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.service.task.CampusServiceTask;
import lombok.extern.log4j.Log4j2;

/**
 * <p>Título: Proyecto SPAFEU</p>
 * <p>Descripción: Tests unitarios del controlador REST para
 * gestión de campus y programas académicos.</p>
 *
 * @author AGENT-QA-EXPERT-SPAFEU
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de CampusRestController")
@Log4j2
@MockitoSettings(strictness = Strictness.LENIENT)
class CampusRestControllerTest {

    @Mock
    private CampusServiceTask campusServiceTask;

    @InjectMocks
    private CampusRestController campusRestController;

    @Nested
    @DisplayName("GET /api/campuses")
    class FindAll {

        @Test
        @DisplayName("Debe retornar 200 OK con lista de campus cuando existen datos")
        void debeRetornar200OkConLista() {
            CampusDTO campus1 = new CampusDTO();
            campus1.setId(1L);
            campus1.setName("Campus Principal");

            CampusDTO campus2 = new CampusDTO();
            campus2.setId(2L);
            campus2.setName("Campus Secundario");

            RespuestaServicioDTO<List<CampusDTO>> respuesta = new RespuestaServicioDTO<>();
            respuesta.setOk(true);
            respuesta.setNegocio(List.of(campus1, campus2));

            ResponseEntity<RespuestaServicioDTO<List<CampusDTO>>> respuestaEsperada =
                    new ResponseEntity<>(respuesta, HttpStatus.OK);
            when(campusServiceTask.findAllCampusesWithPrograms()).thenReturn(respuestaEsperada);

            ResponseEntity<RespuestaServicioDTO<List<CampusDTO>>> response =
                    campusRestController.findAll();

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            RespuestaServicioDTO<List<CampusDTO>> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isTrue();
            assertThat(body.getNegocio()).hasSize(2);
            assertThat(body.getNegocio().get(0).getName()).isEqualTo("Campus Principal");
            assertThat(body.getNegocio().get(1).getName()).isEqualTo("Campus Secundario");

            verify(campusServiceTask, times(1)).findAllCampusesWithPrograms();
            log.info("Test findAll: retorna 200 OK con {} campus", body.getNegocio().size());
        }

        @Test
        @DisplayName("Debe retornar 200 OK con lista vacía cuando no hay campus")
        void debeRetornar200OkConListaVacia() {
            RespuestaServicioDTO<List<CampusDTO>> respuesta = new RespuestaServicioDTO<>();
            respuesta.setOk(true);
            respuesta.setNegocio(List.of());

            ResponseEntity<RespuestaServicioDTO<List<CampusDTO>>> respuestaEsperada =
                    new ResponseEntity<>(respuesta, HttpStatus.OK);
            when(campusServiceTask.findAllCampusesWithPrograms()).thenReturn(respuestaEsperada);

            ResponseEntity<RespuestaServicioDTO<List<CampusDTO>>> response =
                    campusRestController.findAll();

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            RespuestaServicioDTO<List<CampusDTO>> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isTrue();
            assertThat(body.getNegocio()).isEmpty();

            verify(campusServiceTask, times(1)).findAllCampusesWithPrograms();
            log.info("Test findAll: retorna 200 OK con lista vacía");
        }

        @Test
        @DisplayName("Debe delegar la llamada a CampusServiceTask")
        void debeDelegarACampusServiceTask() {
            ResponseEntity<RespuestaServicioDTO<List<CampusDTO>>> respuestaEsperada =
                    new ResponseEntity<>(new RespuestaServicioDTO<>(), HttpStatus.OK);
            when(campusServiceTask.findAllCampusesWithPrograms()).thenReturn(respuestaEsperada);

            campusRestController.findAll();

            verify(campusServiceTask, times(1)).findAllCampusesWithPrograms();
            log.info("Test findAll: verifica delegación a CampusServiceTask");
        }
    }
}
