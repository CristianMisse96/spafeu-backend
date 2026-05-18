package edu.co.udes.spafeu.web.app.service.task.impl;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

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

import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.questionnaire.QuestionnaireDTO;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.entity.QuestionnaireService;
import edu.co.udes.spafeu.web.app.util.Messages;
import lombok.extern.log4j.Log4j2;

/**
 * <p>Título: Proyecto SPAFEU</p>
 * <p>Descripción: Tests unitarios para QuestionnaireServiceTaskImpl - construcción de RespuestaServicioDTO para consulta de cuestionarios.</p>
 *
 * @author AGENT-QA-EXPERT-SPAFEU
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de QuestionnaireServiceTaskImpl")
@Log4j2
class QuestionnaireServiceTaskImplTest {

    @Mock
    private Messages messages;

    @Mock
    private QuestionnaireService questionnaireService;

    @InjectMocks
    private QuestionnaireServiceTaskImpl questionnaireServiceTask;

    private QuestionnaireDTO questionnaireDTO;
    private static final String ID_CUESTIONARIO = "1";

    @BeforeEach
    void setUp() {
        questionnaireDTO = new QuestionnaireDTO();
        questionnaireDTO.setId(1L);
        questionnaireDTO.setName("Cuestionario de Salud");

        when(messages.getKey(anyString())).thenReturn("mock");
    }

    @Nested
    @DisplayName("findAllQuestionnaires()")
    class FindAllQuestionnaires {

        @Test
        @DisplayName("Debe retornar 200 OK con RespuestaServicioDTO correcto cuando hay cuestionarios")
        void debeRetornar200OkConRespuestaCorrecta() {
            // Arrange
            QuestionnaireDTO questionnaireDTO2 = new QuestionnaireDTO();
            questionnaireDTO2.setId(2L);
            questionnaireDTO2.setName("Cuestionario de Actividad Física");

            Set<QuestionnaireDTO> listaDTOs = Set.of(questionnaireDTO, questionnaireDTO2);
            when(questionnaireService.findAllQuestionnaires()).thenReturn(listaDTOs);

            // Act
            ResponseEntity<RespuestaServicioDTO<Set<QuestionnaireDTO>>> response =
                    questionnaireServiceTask.findAllQuestionnaires();

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            RespuestaServicioDTO<Set<QuestionnaireDTO>> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isTrue();
            assertThat(body.getNegocio()).isEqualTo(listaDTOs);
            assertThat(body.getNegocio()).hasSize(2);

            MensajeDTO mensaje = body.getMensajeDTO();
            assertThat(mensaje).isNotNull();
            assertThat(mensaje.getSeverity()).isEqualTo(SeverityEnum.SUCCESS);
            assertThat(mensaje.getSummary()).isEqualTo("mock");
            assertThat(mensaje.getDetail()).isEqualTo("mock");

            verify(questionnaireService, times(1)).findAllQuestionnaires();
            log.info("Test findAllQuestionnaires exitoso: retorna 200 OK con {} cuestionarios", body.getNegocio().size());
        }

        @Test
        @DisplayName("Debe retornar 200 OK con lista vacía cuando no hay cuestionarios")
        void debeRetornar200OkConListaVacia() {
            // Arrange
            when(questionnaireService.findAllQuestionnaires()).thenReturn(Set.of());

            // Act
            ResponseEntity<RespuestaServicioDTO<Set<QuestionnaireDTO>>> response =
                    questionnaireServiceTask.findAllQuestionnaires();

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            RespuestaServicioDTO<Set<QuestionnaireDTO>> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isTrue();
            assertThat(body.getNegocio()).isEmpty();

            MensajeDTO mensaje = body.getMensajeDTO();
            assertThat(mensaje).isNotNull();
            assertThat(mensaje.getSeverity()).isEqualTo(SeverityEnum.SUCCESS);

            verify(questionnaireService, times(1)).findAllQuestionnaires();
            log.info("Test findAllQuestionnaires sin datos: retorna 200 OK con lista vacía");
        }
    }
}
