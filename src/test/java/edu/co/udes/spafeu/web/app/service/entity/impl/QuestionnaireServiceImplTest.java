package edu.co.udes.spafeu.web.app.service.entity.impl;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.co.udes.spafeu.web.app.models.dto.questionnaire.OptionDTO;
import edu.co.udes.spafeu.web.app.models.dto.questionnaire.QuestionDTO;
import edu.co.udes.spafeu.web.app.models.dto.questionnaire.QuestionnaireDTO;
import edu.co.udes.spafeu.web.app.models.entities.Option;
import edu.co.udes.spafeu.web.app.models.entities.Question;
import edu.co.udes.spafeu.web.app.models.entities.QuestionOption;
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
            // Arrange - Entidad 1 con pregunta y opciones
            Option option1 = new Option();
            option1.setId(10L);
            option1.setLabel("Sí");

            Option option2 = new Option();
            option2.setId(11L);
            option2.setLabel("No");

            QuestionOption qo1 = new QuestionOption();
            qo1.setId(100L);
            qo1.setOption(option1);

            QuestionOption qo2 = new QuestionOption();
            qo2.setId(101L);
            qo2.setOption(option2);

            Question question1 = new Question();
            question1.setId(20L);
            question1.setText("¿Tiene dolor?");
            question1.setOptions(Set.of(qo1, qo2));

            qo1.setQuestion(question1);
            qo2.setQuestion(question1);

            questionnaireEntity.setQuestions(Set.of(question1));

            // Entidad 2 sin preguntas
            Questionnaire questionnaireEntity2 = new Questionnaire();
            questionnaireEntity2.setId(2L);
            questionnaireEntity2.setName("Cuestionario de Actividad Física");
            questionnaireEntity2.setQuestions(Set.of());

            // DTOs esperados (mapeo manual del servicio)
            OptionDTO optionDTO1 = new OptionDTO();
            optionDTO1.setId(10L);
            optionDTO1.setLabel("Sí");

            OptionDTO optionDTO2 = new OptionDTO();
            optionDTO2.setId(11L);
            optionDTO2.setLabel("No");

            QuestionDTO questionDTO1 = new QuestionDTO();
            questionDTO1.setId(20L);
            questionDTO1.setText("¿Tiene dolor?");
            questionDTO1.setOptions(Set.of(optionDTO1, optionDTO2));

            questionnaireDTO.setQuestions(Set.of(questionDTO1));

            QuestionnaireDTO questionnaireDTO2 = new QuestionnaireDTO();
            questionnaireDTO2.setId(2L);
            questionnaireDTO2.setName("Cuestionario de Actividad Física");
            questionnaireDTO2.setQuestions(Set.of());

            Set<Questionnaire> entities = new LinkedHashSet<>();
            entities.add(questionnaireEntity);
            entities.add(questionnaireEntity2);

            when(questionnaireRepository.findAllWithDetails()).thenReturn(entities);

            // Act
            Set<QuestionnaireDTO> resultado = questionnaireService.findAllQuestionnaires();

            // Assert
            assertThat(resultado)
                    .isNotNull()
                    .hasSize(2);
            assertThat(resultado)
                    .extracting(QuestionnaireDTO::getId)
                    .containsExactlyInAnyOrder(1L, 2L);
            assertThat(resultado)
                    .extracting(QuestionnaireDTO::getName)
                    .containsExactlyInAnyOrder("Cuestionario de Salud", "Cuestionario de Actividad Física");

            // Verificar mapeo completo del primer cuestionario
            QuestionnaireDTO primer = resultado.stream()
                    .filter(q -> q.getId().equals(1L))
                    .findFirst().orElseThrow();
            assertThat(primer.getQuestions())
                    .hasSize(1)
                    .extracting(QuestionDTO::getText)
                    .containsExactly("¿Tiene dolor?");
            assertThat(primer.getQuestions().iterator().next().getOptions())
                    .extracting(OptionDTO::getLabel)
                    .containsExactlyInAnyOrder("Sí", "No");

            // Verificar mapeo del segundo cuestionario (sin preguntas)
            QuestionnaireDTO segundo = resultado.stream()
                    .filter(q -> q.getId().equals(2L))
                    .findFirst().orElseThrow();
            assertThat(segundo.getQuestions()).isEmpty();

            verify(questionnaireRepository, times(1)).findAllWithDetails();
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
            log.info("Test findAllQuestionnaires sin datos: retorna lista vacía");
        }
    }
}
