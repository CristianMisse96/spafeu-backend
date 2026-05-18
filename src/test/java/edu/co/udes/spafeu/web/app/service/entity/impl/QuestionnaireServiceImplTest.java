package edu.co.udes.spafeu.web.app.service.entity.impl;

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
import org.modelmapper.ModelMapper;

import edu.co.udes.spafeu.web.app.models.dto.questionnaire.QuestionnaireDTO;
import edu.co.udes.spafeu.web.app.models.entities.Questionnaire;
import edu.co.udes.spafeu.web.app.repositories.QuestionnaireRepository;
import lombok.extern.log4j.Log4j2;

/**
 * <p>Título: Proyecto SPAFEU</p>
 * <p>Descripción: Tests unitarios para QuestionnaireServiceImpl - lógica de negocio de consulta de cuestionarios.</p>
 *
 * @author AGENT-QA-EXPERT-SPAFEU
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de QuestionnaireServiceImpl")
@Log4j2
class QuestionnaireServiceImplTest {

    @Mock
    private QuestionnaireRepository questionnaireRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private QuestionnaireServiceImpl questionnaireService;

    private Questionnaire questionnaireEntity;
    private QuestionnaireDTO questionnaireDTO;

    @BeforeEach
    void setUp() {
        // Arrange - Datos de prueba
        questionnaireEntity = new Questionnaire();
        questionnaireEntity.setId(1L);
        questionnaireEntity.setName("Cuestionario de Salud");

        questionnaireDTO = new QuestionnaireDTO();
        questionnaireDTO.setId(1L);
        questionnaireDTO.setName("Cuestionario de Salud");
    }

    @Nested
    @DisplayName("findAllQuestionnaires()")
    class FindAllQuestionnaires {

        @Test
        @DisplayName("Debe retornar lista de DTOs cuando el repository devuelve cuestionarios")
        void debeRetornarListaDTOsCuandoHayDatos() {
            // Arrange
            Questionnaire questionnaireEntity2 = new Questionnaire();
            questionnaireEntity2.setId(2L);
            questionnaireEntity2.setName("Cuestionario de Actividad Física");

            QuestionnaireDTO questionnaireDTO2 = new QuestionnaireDTO();
            questionnaireDTO2.setId(2L);
            questionnaireDTO2.setName("Cuestionario de Actividad Física");

            Set<Questionnaire> entities = Set.of(questionnaireEntity, questionnaireEntity2);
            when(questionnaireRepository.findAllWithDetails()).thenReturn(entities);
            when(modelMapper.map(questionnaireEntity, QuestionnaireDTO.class)).thenReturn(questionnaireDTO);
            when(modelMapper.map(questionnaireEntity2, QuestionnaireDTO.class)).thenReturn(questionnaireDTO2);

            // Act
            Set<QuestionnaireDTO> resultado = questionnaireService.findAllQuestionnaires();

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado).hasSize(2);
            assertThat(resultado).extracting(QuestionnaireDTO::getId).containsExactlyInAnyOrder(1L, 2L);
            assertThat(resultado).extracting(QuestionnaireDTO::getName).containsExactlyInAnyOrder("Cuestionario de Salud", "Cuestionario de Actividad Física");

            verify(questionnaireRepository, times(1)).findAllWithDetails();
            verify(modelMapper, times(1)).map(questionnaireEntity, QuestionnaireDTO.class);
            verify(modelMapper, times(1)).map(questionnaireEntity2, QuestionnaireDTO.class);
            log.info("Test findAllQuestionnaires con datos: retorna lista de {} DTOs", resultado.size());
        }

        @Test
        @DisplayName("Debe retornar lista vacía cuando el repository no devuelve cuestionarios")
        void debeRetornarListaVaciaCuandoNoHayDatos() {
            // Arrange
            when(questionnaireRepository.findAllWithDetails()).thenReturn(Set.of());

            // Act
            Set<QuestionnaireDTO> resultado = questionnaireService.findAllQuestionnaires();

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado).isEmpty();

            verify(questionnaireRepository, times(1)).findAllWithDetails();
            verify(modelMapper, never()).map(any(), any());
            log.info("Test findAllQuestionnaires sin datos: retorna lista vacía");
        }
    }
}
