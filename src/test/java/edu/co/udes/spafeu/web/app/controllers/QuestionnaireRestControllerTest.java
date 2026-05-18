package edu.co.udes.spafeu.web.app.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.questionnaire.QuestionnaireDTO;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.task.QuestionnaireServiceTask;
import edu.co.udes.spafeu.web.app.util.Messages;
import lombok.extern.log4j.Log4j2;

/**
 * <p>Título: Proyecto SPAFEU</p>
 * <p>Descripción: Tests unitarios del controlador REST para gestión de cuestionarios (questionnaires).</p>
 *
 * @author AGENT-QA-EXPERT-SPAFEU
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de QuestionnaireRestController")
@Log4j2
@MockitoSettings(strictness = Strictness.LENIENT)
class QuestionnaireRestControllerTest {

    @Mock
    private Messages messages;

    @Mock
    private QuestionnaireServiceTask questionnaireServiceTask;

    @InjectMocks
    private QuestionnaireRestController questionnaireRestController;

    private QuestionnaireDTO questionnaireDTO;

    @BeforeEach
    void setUp() {
        questionnaireDTO = new QuestionnaireDTO();
        questionnaireDTO.setId(1L);
        questionnaireDTO.setName("Cuestionario de Salud");

        lenient().when(messages.getKey(anyString())).thenReturn("mock");
    }

    @Nested
    @DisplayName("GET /api/questionnaires")
    class FindAll {

        @Test
        @DisplayName("Debe retornar 200 OK con la lista de cuestionarios delegando al ServiceTask")
        void findAllDelegaAServiceTask() {
            // Arrange
            RespuestaServicioDTO<Set<QuestionnaireDTO>> respuesta = new RespuestaServicioDTO<>();
            respuesta.setOk(true);
            respuesta.setNegocio(Set.of(questionnaireDTO));
            respuesta.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS,
                    messages.getKey("success"), messages.getKey("success.detalle")));

            when(questionnaireServiceTask.findAllQuestionnaires())
                    .thenReturn(new ResponseEntity<>(respuesta, HttpStatus.OK));

            // Act
            ResponseEntity<RespuestaServicioDTO<Set<QuestionnaireDTO>>> response =
                    questionnaireRestController.findAll();

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().isOk());
            assertNotNull(response.getBody().getNegocio());

            verify(questionnaireServiceTask, times(1)).findAllQuestionnaires();
            log.info("Test findAllQuestionnaires delegación al ServiceTask: exitoso");
        }

        @Test
        @DisplayName("Debe retornar exactamente lo que el ServiceTask devuelve sin modificar la respuesta")
        void findAllRetornaExactamenteLoQueDevuelveServiceTask() {
            // Arrange
            QuestionnaireDTO otroCuestionario = new QuestionnaireDTO();
            otroCuestionario.setId(2L);
            otroCuestionario.setName("Cuestionario de Actividad Física");

            RespuestaServicioDTO<Set<QuestionnaireDTO>> respuesta = new RespuestaServicioDTO<>();
            respuesta.setOk(true);
            respuesta.setNegocio(Set.of(questionnaireDTO, otroCuestionario));
            respuesta.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS,
                    messages.getKey("success"), messages.getKey("success.detalle")));

            when(questionnaireServiceTask.findAllQuestionnaires())
                    .thenReturn(new ResponseEntity<>(respuesta, HttpStatus.OK));

            // Act
            ResponseEntity<RespuestaServicioDTO<Set<QuestionnaireDTO>>> response =
                    questionnaireRestController.findAll();

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertThat(response.getBody().getNegocio()).hasSize(2);
            assertThat(response.getBody().getNegocio()).extracting(QuestionnaireDTO::getId).containsExactlyInAnyOrder(1L, 2L);

            verify(questionnaireServiceTask, times(1)).findAllQuestionnaires();
            log.info("Test findAllQuestionnaires retorna respuesta exacta del ServiceTask: exitoso");
        }
    }
}
