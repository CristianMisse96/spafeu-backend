package edu.co.udes.spafeu.web.app.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import lombok.extern.log4j.Log4j2;

import org.junit.jupiter.api.AfterEach;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

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

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getDetails()).thenReturn(1L);
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Nested
    @DisplayName("GET /api/exercises/{id}")
    class FindById {

        @Test
        @DisplayName("Debe retornar 200 OK y delegar al ServiceTask cuando el ejercicio existe")
        void debeRetornar200CuandoExiste() {
            when(exerciseServiceTask.findById(1L, NivelActividadFisicaEnum.NIVEL_3, 1L)).thenReturn(serviceTaskResponse);

            ResponseEntity<RespuestaServicioDTO<ExerciseDTO>> response = exerciseRestController.findById(1L, NivelActividadFisicaEnum.NIVEL_3);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().isOk()).isTrue();
            assertThat(response.getBody().getNegocio().getId()).isEqualTo(1L);
            assertThat(response.getBody().getNegocio().getName()).isEqualTo("Sentadillas");

            verify(exerciseServiceTask, times(1)).findById(1L, NivelActividadFisicaEnum.NIVEL_3, 1L);
            log.info("Test GET findById con éxito: retorna 200 OK con ExerciseDTO id={}", response.getBody().getNegocio().getId());
        }

        @Test
        @DisplayName("Debe retornar 200 OK cuando el ServiceTask retorna datos vacíos")
        void debeRetornar200ConDatosVacios() {
            RespuestaServicioDTO<ExerciseDTO> respuestaVacia = new RespuestaServicioDTO<>();
            respuestaVacia.setOk(true);
            respuestaVacia.setNegocio(new ExerciseDTO());
            respuestaVacia.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, "Éxito", "Sin datos"));

            when(exerciseServiceTask.findById(1L, NivelActividadFisicaEnum.NIVEL_3, 1L)).thenReturn(new ResponseEntity<>(respuestaVacia, HttpStatus.OK));

            ResponseEntity<RespuestaServicioDTO<ExerciseDTO>> response = exerciseRestController.findById(1L, NivelActividadFisicaEnum.NIVEL_3);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().isOk()).isTrue();
            assertThat(response.getBody().getNegocio()).isNotNull();
            assertThat(response.getBody().getNegocio().getId()).isNull();

            verify(exerciseServiceTask, times(1)).findById(1L, NivelActividadFisicaEnum.NIVEL_3, 1L);
            log.info("Test GET findById sin datos: retorna 200 OK con ExerciseDTO vacío");
        }

        @Test
        @DisplayName("Debe pasar el identificador y nivel correctamente al ServiceTask")
        void debePasarIdYNivelCorrectamente() {
            when(exerciseServiceTask.findById(1L, NivelActividadFisicaEnum.NIVEL_3, 1L)).thenReturn(serviceTaskResponse);

            ResponseEntity<RespuestaServicioDTO<ExerciseDTO>> response = exerciseRestController.findById(1L, NivelActividadFisicaEnum.NIVEL_3);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(exerciseServiceTask, times(1)).findById(1L, NivelActividadFisicaEnum.NIVEL_3, 1L);
            log.info("Test GET findById: parámetro id=1L y nivel=NIVEL_3 pasados correctamente al ServiceTask");
        }
    }

    @Nested
    @DisplayName("GET /api/exercises/completed")
    class FindCompletedExerciseIdsByUserId {

        private Set<Long> completedIds;
        private RespuestaServicioDTO<Set<Long>> respuestaSet;
        private ResponseEntity<RespuestaServicioDTO<Set<Long>>> serviceTaskResponseSet;

        @BeforeEach
        void setUpCompleted() {
            completedIds = new HashSet<>(Set.of(1L, 2L, 3L));
            respuestaSet = new RespuestaServicioDTO<>();
            respuestaSet.setOk(true);
            respuestaSet.setNegocio(completedIds);
            respuestaSet.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, "Éxito", "Operación exitosa"));
            serviceTaskResponseSet = new ResponseEntity<>(respuestaSet, HttpStatus.OK);
        }

        @Test
        @DisplayName("Debe retornar 200 OK con set de IDs completados")
        void debeRetornar200ConSetDeIdsCompletados() {
            when(exerciseServiceTask.findCompletedExerciseIdsByUserId(1L)).thenReturn(serviceTaskResponseSet);

            ResponseEntity<RespuestaServicioDTO<Set<Long>>> response = exerciseRestController.findCompletedExerciseIdsByUserId();

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().isOk()).isTrue();
            assertThat(response.getBody().getNegocio()).containsExactlyInAnyOrder(1L, 2L, 3L);

            verify(exerciseServiceTask, times(1)).findCompletedExerciseIdsByUserId(1L);
            log.info("Test GET /completed con éxito: retorna 200 OK con {} IDs", response.getBody().getNegocio().size());
        }

        @Test
        @DisplayName("Debe retornar 200 OK con set vacío cuando no hay ejercicios completados")
        void debeRetornar200ConSetVacio() {
            RespuestaServicioDTO<Set<Long>> respuestaVacia = new RespuestaServicioDTO<>();
            respuestaVacia.setOk(true);
            respuestaVacia.setNegocio(Collections.emptySet());
            respuestaVacia.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, "Éxito", "Sin datos"));

            when(exerciseServiceTask.findCompletedExerciseIdsByUserId(1L)).thenReturn(new ResponseEntity<>(respuestaVacia, HttpStatus.OK));

            ResponseEntity<RespuestaServicioDTO<Set<Long>>> response = exerciseRestController.findCompletedExerciseIdsByUserId();

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().isOk()).isTrue();
            assertThat(response.getBody().getNegocio()).isEmpty();

            verify(exerciseServiceTask, times(1)).findCompletedExerciseIdsByUserId(1L);
            log.info("Test GET /completed sin datos: retorna 200 OK con set vacío");
        }

        @Test
        @DisplayName("Debe obtener userId del SecurityContextHolder y pasarlo al ServiceTask")
        void debeObtenerUserIdDelSecurityContext() {
            when(exerciseServiceTask.findCompletedExerciseIdsByUserId(1L)).thenReturn(serviceTaskResponseSet);

            exerciseRestController.findCompletedExerciseIdsByUserId();

            verify(exerciseServiceTask, times(1)).findCompletedExerciseIdsByUserId(1L);
            log.info("Test GET /completed: userId=1L obtenido del SecurityContextHolder y pasado al ServiceTask");
        }
    }

    @Nested
    @DisplayName("PATCH /api/exercises/{exerciseId}/complete")
    class CompleteExerciseSession {

        private RespuestaServicioDTO<Void> respuestaVoid;
        private ResponseEntity<RespuestaServicioDTO<Void>> serviceTaskResponseVoid;

        @BeforeEach
        void setUpComplete() {
            respuestaVoid = new RespuestaServicioDTO<>();
            respuestaVoid.setOk(true);
            respuestaVoid.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, "Éxito", "Ejercicio completado"));
            serviceTaskResponseVoid = new ResponseEntity<>(respuestaVoid, HttpStatus.OK);
        }

        @Test
        @DisplayName("Debe retornar 200 OK al completar sesión de ejercicio")
        void debeRetornar200AlCompletarSesion() {
            when(exerciseServiceTask.completeExerciseSession(1L, 1L)).thenReturn(serviceTaskResponseVoid);

            ResponseEntity<RespuestaServicioDTO<Void>> response = exerciseRestController.completeExerciseSession(1L);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().isOk()).isTrue();

            verify(exerciseServiceTask, times(1)).completeExerciseSession(1L, 1L);
            log.info("Test PATCH /complete con éxito: retorna 200 OK");
        }

        @Test
        @DisplayName("Debe obtener userId del SecurityContextHolder y pasarlo al ServiceTask")
        void debeObtenerUserIdDelSecurityContextParaComplete() {
            when(exerciseServiceTask.completeExerciseSession(1L, 1L)).thenReturn(serviceTaskResponseVoid);

            exerciseRestController.completeExerciseSession(1L);

            verify(exerciseServiceTask, times(1)).completeExerciseSession(1L, 1L);
            log.info("Test PATCH /complete: userId=1L obtenido del SecurityContextHolder y pasado al ServiceTask");
        }
    }
}
