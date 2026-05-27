package edu.co.udes.spafeu.web.app.controllers;

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

import edu.co.udes.spafeu.web.app.models.dto.ActivityTypeDTO;
import edu.co.udes.spafeu.web.app.models.dto.DurationTypeDTO;
import edu.co.udes.spafeu.web.app.models.dto.ExerciseDTO;
import edu.co.udes.spafeu.web.app.models.dto.ExerciseGroupedByActivityTypeDTO;
import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.PhysicalActivityProtocolDTO;
import edu.co.udes.spafeu.web.app.models.dto.ProtocolObjectiveDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.task.PhysicalActivityProtocolServiceTask;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de PhysicalActivityProtocolRestController")
@Log4j2
class PhysicalActivityProtocolRestControllerTest {

    @Mock
    private PhysicalActivityProtocolServiceTask physicalActivityProtocolServiceTask;

    @InjectMocks
    private PhysicalActivityProtocolRestController physicalActivityProtocolRestController;

    private PhysicalActivityProtocolDTO protocolDTO;
    private RespuestaServicioDTO<PhysicalActivityProtocolDTO> respuestaServicioDTO;
    private ResponseEntity<RespuestaServicioDTO<PhysicalActivityProtocolDTO>> serviceTaskResponse;

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

        respuestaServicioDTO = new RespuestaServicioDTO<>();
        respuestaServicioDTO.setOk(true);
        respuestaServicioDTO.setNegocio(protocolDTO);
        respuestaServicioDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, "Éxito", "Operación exitosa"));

        serviceTaskResponse = new ResponseEntity<>(respuestaServicioDTO, HttpStatus.OK);
    }

    @Nested
    @DisplayName("GET /api/physical-activity-protocols")
    class FindAllWithoutExercises {

        @Test
        @DisplayName("Debe retornar 200 OK y delegar al ServiceTask con NIVEL_3 cuando hay protocolos")
        void debeRetornar200CuandoHayProtocolos() {
            when(physicalActivityProtocolServiceTask.findAllByNivelActividadFisicaWithoutExercises(NivelActividadFisicaEnum.NIVEL_3)).thenReturn(serviceTaskResponse);

            ResponseEntity<RespuestaServicioDTO<PhysicalActivityProtocolDTO>> response =
                    physicalActivityProtocolRestController.findAllWithoutExercises(NivelActividadFisicaEnum.NIVEL_3);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().isOk()).isTrue();
            assertThat(response.getBody().getNegocio().getId()).isEqualTo(1L);

            verify(physicalActivityProtocolServiceTask, times(1)).findAllByNivelActividadFisicaWithoutExercises(NivelActividadFisicaEnum.NIVEL_3);
            log.info("Test GET findAllWithoutExercises: retorna 200 OK con {} protocolos", response.getBody().getNegocio());
        }

        @Test
        @DisplayName("Debe retornar 200 OK cuando el ServiceTask retorna lista vacía para el nivel")
        void debeRetornar200ConListaVacia() {
            RespuestaServicioDTO<PhysicalActivityProtocolDTO> respuestaVacia = new RespuestaServicioDTO<>();
            respuestaVacia.setOk(true);
            respuestaVacia.setNegocio(new PhysicalActivityProtocolDTO());
            respuestaVacia.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, "Éxito", "Sin datos"));

            when(physicalActivityProtocolServiceTask.findAllByNivelActividadFisicaWithoutExercises(NivelActividadFisicaEnum.NIVEL_3))
                    .thenReturn(new ResponseEntity<>(respuestaVacia, HttpStatus.OK));

            ResponseEntity<RespuestaServicioDTO<PhysicalActivityProtocolDTO>> response =
                    physicalActivityProtocolRestController.findAllWithoutExercises(NivelActividadFisicaEnum.NIVEL_3);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().isOk()).isTrue();
            assertThat(response.getBody().getNegocio()).isNotNull();

            verify(physicalActivityProtocolServiceTask, times(1)).findAllByNivelActividadFisicaWithoutExercises(NivelActividadFisicaEnum.NIVEL_3);
            log.info("Test GET findAllWithoutExercises sin datos: retorna 200 OK con lista vacía");
        }

        @Test
        @DisplayName("Debe pasar NivelActividadFisicaEnum.NIVEL_3 correctamente al ServiceTask")
        void debePasarNivel3Correctamente() {
            when(physicalActivityProtocolServiceTask.findAllByNivelActividadFisicaWithoutExercises(NivelActividadFisicaEnum.NIVEL_3)).thenReturn(serviceTaskResponse);

            ResponseEntity<RespuestaServicioDTO<PhysicalActivityProtocolDTO>> response =
                    physicalActivityProtocolRestController.findAllWithoutExercises(NivelActividadFisicaEnum.NIVEL_3);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(physicalActivityProtocolServiceTask, times(1)).findAllByNivelActividadFisicaWithoutExercises(NivelActividadFisicaEnum.NIVEL_3);
            log.info("Test GET findAllWithoutExercises: parámetro NIVEL_3 pasado correctamente al ServiceTask");
        }
    }

    @Nested
    @DisplayName("GET /api/physical-activity-protocols/exercises")
    class FindExercisesGroupedByActivityType {

        @Test
        @DisplayName("Debe retornar 200 OK y delegar al ServiceTask con NIVEL_3 cuando hay ejercicios")
        void debeRetornar200CuandoHayEjercicios() {
            ActivityTypeDTO cardioTypeDTO = new ActivityTypeDTO();
            cardioTypeDTO.setId(1L);
            cardioTypeDTO.setName("Cardio");

            ExerciseDTO exerciseDTO = new ExerciseDTO();
            exerciseDTO.setId(1L);
            exerciseDTO.setName("Correr");
            exerciseDTO.setTipoActividadFisica(cardioTypeDTO);

            ExerciseGroupedByActivityTypeDTO grupo = new ExerciseGroupedByActivityTypeDTO();
            grupo.setActivityType("Cardio");
            grupo.setExercises(List.of(exerciseDTO));

            RespuestaServicioDTO<List<ExerciseGroupedByActivityTypeDTO>> respuesta = new RespuestaServicioDTO<>();
            respuesta.setOk(true);
            respuesta.setNegocio(List.of(grupo));
            respuesta.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, "Éxito", "Operación exitosa"));

            when(physicalActivityProtocolServiceTask.findExercisesByNivelActividadFisicaGroupedByActivityType(NivelActividadFisicaEnum.NIVEL_3))
                    .thenReturn(new ResponseEntity<>(respuesta, HttpStatus.OK));

            ResponseEntity<RespuestaServicioDTO<List<ExerciseGroupedByActivityTypeDTO>>> response =
                    physicalActivityProtocolRestController.findExercisesGroupedByActivityType(NivelActividadFisicaEnum.NIVEL_3);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().isOk()).isTrue();
            assertThat(response.getBody().getNegocio()).isNotNull();
            assertThat(response.getBody().getNegocio()).hasSize(1);

            verify(physicalActivityProtocolServiceTask, times(1)).findExercisesByNivelActividadFisicaGroupedByActivityType(NivelActividadFisicaEnum.NIVEL_3);
            log.info("Test GET findExercisesGroupedByActivityType: retorna 200 OK con {} grupos", response.getBody().getNegocio().size());
        }

        @Test
        @DisplayName("Debe retornar 200 OK cuando el ServiceTask retorna lista vacía")
        void debeRetornar200ConListaVacia() {
            RespuestaServicioDTO<List<ExerciseGroupedByActivityTypeDTO>> respuestaVacia = new RespuestaServicioDTO<>();
            respuestaVacia.setOk(true);
            respuestaVacia.setNegocio(List.of());
            respuestaVacia.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, "Éxito", "Sin datos"));

            when(physicalActivityProtocolServiceTask.findExercisesByNivelActividadFisicaGroupedByActivityType(NivelActividadFisicaEnum.NIVEL_3))
                    .thenReturn(new ResponseEntity<>(respuestaVacia, HttpStatus.OK));

            ResponseEntity<RespuestaServicioDTO<List<ExerciseGroupedByActivityTypeDTO>>> response =
                    physicalActivityProtocolRestController.findExercisesGroupedByActivityType(NivelActividadFisicaEnum.NIVEL_3);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().isOk()).isTrue();
            assertThat(response.getBody().getNegocio()).isNotNull();
            assertThat(response.getBody().getNegocio()).isEmpty();

            verify(physicalActivityProtocolServiceTask, times(1)).findExercisesByNivelActividadFisicaGroupedByActivityType(NivelActividadFisicaEnum.NIVEL_3);
            log.info("Test GET findExercisesGroupedByActivityType sin datos: retorna 200 OK con lista vacía");
        }

        @Test
        @DisplayName("Debe pasar NivelActividadFisicaEnum.NIVEL_3 correctamente al ServiceTask")
        void debePasarNivel3Correctamente() {
            ActivityTypeDTO cardioTypeDTO = new ActivityTypeDTO();
            cardioTypeDTO.setId(1L);
            cardioTypeDTO.setName("Cardio");

            ExerciseDTO exerciseDTO = new ExerciseDTO();
            exerciseDTO.setId(1L);
            exerciseDTO.setName("Correr");
            exerciseDTO.setTipoActividadFisica(cardioTypeDTO);

            ExerciseGroupedByActivityTypeDTO grupo = new ExerciseGroupedByActivityTypeDTO();
            grupo.setActivityType("Cardio");
            grupo.setExercises(List.of(exerciseDTO));

            RespuestaServicioDTO<List<ExerciseGroupedByActivityTypeDTO>> respuesta = new RespuestaServicioDTO<>();
            respuesta.setOk(true);
            respuesta.setNegocio(List.of(grupo));
            respuesta.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, "Éxito", "Operación exitosa"));

            when(physicalActivityProtocolServiceTask.findExercisesByNivelActividadFisicaGroupedByActivityType(NivelActividadFisicaEnum.NIVEL_3))
                    .thenReturn(new ResponseEntity<>(respuesta, HttpStatus.OK));

            ResponseEntity<RespuestaServicioDTO<List<ExerciseGroupedByActivityTypeDTO>>> response =
                    physicalActivityProtocolRestController.findExercisesGroupedByActivityType(NivelActividadFisicaEnum.NIVEL_3);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(physicalActivityProtocolServiceTask, times(1)).findExercisesByNivelActividadFisicaGroupedByActivityType(NivelActividadFisicaEnum.NIVEL_3);
            log.info("Test GET findExercisesGroupedByActivityType: parámetro NIVEL_3 pasado correctamente al ServiceTask");
        }
    }
}
