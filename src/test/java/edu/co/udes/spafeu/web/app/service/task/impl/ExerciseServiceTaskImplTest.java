package edu.co.udes.spafeu.web.app.service.task.impl;

import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.KEY_SUCCESS;
import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.SUCCESS_DETALLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import edu.co.udes.spafeu.web.app.models.dto.ExerciseDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.entity.ExerciseService;
import edu.co.udes.spafeu.web.app.util.Messages;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de ExerciseServiceTaskImpl")
@Log4j2
class ExerciseServiceTaskImplTest {

    @Mock
    private Messages messages;

    @Mock
    private ExerciseService exerciseService;

    @InjectMocks
    private ExerciseServiceTaskImpl exerciseServiceTask;

    private ExerciseDTO exerciseDTO;

    @BeforeEach
    void setUp() {
        exerciseDTO = new ExerciseDTO();
        exerciseDTO.setId(1L);
        exerciseDTO.setName("Sentadillas");
        exerciseDTO.setDescripcionEjercicio("Ejercicio de fuerza para piernas");
    }

    @Nested
    @DisplayName("findById(Long, NivelActividadFisicaEnum)")
    class FindById {

        @Test
        @DisplayName("Debe retornar 200 OK con ExerciseDTO en RespuestaServicioDTO")
        void debeRetornar200ConExerciseDto() throws Exception {
            when(exerciseService.findById(1L, NivelActividadFisicaEnum.NIVEL_3, 1L)).thenReturn(exerciseDTO);
            when(messages.getKey(KEY_SUCCESS)).thenReturn("Éxito");
            when(messages.getKey(SUCCESS_DETALLE)).thenReturn("Operación exitosa");

            ResponseEntity<RespuestaServicioDTO<ExerciseDTO>> response = exerciseServiceTask.findById(1L, NivelActividadFisicaEnum.NIVEL_3, 1L);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            RespuestaServicioDTO<ExerciseDTO> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isTrue();
            assertThat(body.getNegocio()).isNotNull();
            assertThat(body.getNegocio().getId()).isEqualTo(1L);
            assertThat(body.getNegocio().getName()).isEqualTo("Sentadillas");
            assertThat(body.getMensajeDTO()).isNotNull();
            assertThat(body.getMensajeDTO().getSeverity()).isEqualTo(SeverityEnum.SUCCESS);
            assertThat(body.getMensajeDTO().getSummary()).isEqualTo("Éxito");
            assertThat(body.getMensajeDTO().getDetail()).isEqualTo("Operación exitosa");

            verify(exerciseService, times(1)).findById(1L, NivelActividadFisicaEnum.NIVEL_3, 1L);
            verify(messages, times(1)).getKey(KEY_SUCCESS);
            verify(messages, times(1)).getKey(SUCCESS_DETALLE);
            log.info("Test findById con éxito: retorna 200 OK con ExerciseDTO id={}", body.getNegocio().getId());
        }

        @Test
        @DisplayName("Debe retornar 200 OK con ExerciseDTO vacío cuando no hay datos")
        void debeRetornar200ConExerciseDtoVacio() throws Exception {
            ExerciseDTO emptyDTO = new ExerciseDTO();
            when(exerciseService.findById(1L, NivelActividadFisicaEnum.NIVEL_3, 1L)).thenReturn(emptyDTO);
            when(messages.getKey(KEY_SUCCESS)).thenReturn("Éxito");
            when(messages.getKey(SUCCESS_DETALLE)).thenReturn("Operación exitosa");

            ResponseEntity<RespuestaServicioDTO<ExerciseDTO>> response = exerciseServiceTask.findById(1L, NivelActividadFisicaEnum.NIVEL_3, 1L);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            RespuestaServicioDTO<ExerciseDTO> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isTrue();
            assertThat(body.getNegocio()).isNotNull();
            assertThat(body.getNegocio().getId()).isNull();
            assertThat(body.getMensajeDTO()).isNotNull();
            assertThat(body.getMensajeDTO().getSeverity()).isEqualTo(SeverityEnum.SUCCESS);

            verify(exerciseService, times(1)).findById(1L, NivelActividadFisicaEnum.NIVEL_3, 1L);
            log.info("Test findById con datos vacíos: retorna 200 OK con ExerciseDTO vacío");
        }
    }
}
