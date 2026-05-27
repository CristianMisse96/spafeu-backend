package edu.co.udes.spafeu.web.app.controllers;

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
import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.task.ExerciseServiceTask;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de ExerciseRestController")
@Log4j2
class ExerciseRestControllerTest {

    @Mock
    private ExerciseServiceTask exerciseServiceTask;

    @InjectMocks
    private ExerciseRestController exerciseRestController;

    private ExerciseDTO exerciseDTO;
    private RespuestaServicioDTO<ExerciseDTO> respuestaServicioDTO;
    private ResponseEntity<RespuestaServicioDTO<ExerciseDTO>> serviceTaskResponse;

    @BeforeEach
    void setUp() {
        exerciseDTO = new ExerciseDTO();
        exerciseDTO.setId(1L);
        exerciseDTO.setName("Sentadillas");
        exerciseDTO.setDescripcionEjercicio("Ejercicio de fuerza para piernas");

        respuestaServicioDTO = new RespuestaServicioDTO<>();
        respuestaServicioDTO.setOk(true);
        respuestaServicioDTO.setNegocio(exerciseDTO);
        respuestaServicioDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, "Éxito", "Operación exitosa"));

        serviceTaskResponse = new ResponseEntity<>(respuestaServicioDTO, HttpStatus.OK);
    }

    @Nested
    @DisplayName("GET /api/exercises/{id}")
    class FindById {

        @Test
        @DisplayName("Debe retornar 200 OK y delegar al ServiceTask cuando el ejercicio existe")
        void debeRetornar200CuandoExiste() {
            when(exerciseServiceTask.findById(1L, NivelActividadFisicaEnum.NIVEL_3)).thenReturn(serviceTaskResponse);

            ResponseEntity<RespuestaServicioDTO<ExerciseDTO>> response = exerciseRestController.findById(1L, NivelActividadFisicaEnum.NIVEL_3);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().isOk()).isTrue();
            assertThat(response.getBody().getNegocio().getId()).isEqualTo(1L);
            assertThat(response.getBody().getNegocio().getName()).isEqualTo("Sentadillas");

            verify(exerciseServiceTask, times(1)).findById(1L, NivelActividadFisicaEnum.NIVEL_3);
            log.info("Test GET findById con éxito: retorna 200 OK con ExerciseDTO id={}", response.getBody().getNegocio().getId());
        }

        @Test
        @DisplayName("Debe retornar 200 OK cuando el ServiceTask retorna datos vacíos")
        void debeRetornar200ConDatosVacios() {
            RespuestaServicioDTO<ExerciseDTO> respuestaVacia = new RespuestaServicioDTO<>();
            respuestaVacia.setOk(true);
            respuestaVacia.setNegocio(new ExerciseDTO());
            respuestaVacia.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, "Éxito", "Sin datos"));

            when(exerciseServiceTask.findById(1L, NivelActividadFisicaEnum.NIVEL_3)).thenReturn(new ResponseEntity<>(respuestaVacia, HttpStatus.OK));

            ResponseEntity<RespuestaServicioDTO<ExerciseDTO>> response = exerciseRestController.findById(1L, NivelActividadFisicaEnum.NIVEL_3);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().isOk()).isTrue();
            assertThat(response.getBody().getNegocio()).isNotNull();
            assertThat(response.getBody().getNegocio().getId()).isNull();

            verify(exerciseServiceTask, times(1)).findById(1L, NivelActividadFisicaEnum.NIVEL_3);
            log.info("Test GET findById sin datos: retorna 200 OK con ExerciseDTO vacío");
        }

        @Test
        @DisplayName("Debe pasar el identificador y nivel correctamente al ServiceTask")
        void debePasarIdYNivelCorrectamente() {
            when(exerciseServiceTask.findById(1L, NivelActividadFisicaEnum.NIVEL_3)).thenReturn(serviceTaskResponse);

            ResponseEntity<RespuestaServicioDTO<ExerciseDTO>> response = exerciseRestController.findById(1L, NivelActividadFisicaEnum.NIVEL_3);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(exerciseServiceTask, times(1)).findById(1L, NivelActividadFisicaEnum.NIVEL_3);
            log.info("Test GET findById: parámetro id=1L y nivel=NIVEL_3 pasados correctamente al ServiceTask");
        }
    }
}
