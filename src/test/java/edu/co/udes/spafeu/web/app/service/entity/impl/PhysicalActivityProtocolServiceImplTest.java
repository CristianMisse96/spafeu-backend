package edu.co.udes.spafeu.web.app.service.entity.impl;

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
import org.modelmapper.ModelMapper;

import edu.co.udes.spafeu.web.app.models.dto.ActivityTypeDTO;
import edu.co.udes.spafeu.web.app.models.dto.DurationTypeDTO;
import edu.co.udes.spafeu.web.app.models.dto.ExerciseDTO;
import edu.co.udes.spafeu.web.app.models.dto.ExerciseGroupedByActivityTypeDTO;
import edu.co.udes.spafeu.web.app.models.dto.PhysicalActivityProtocolDTO;
import edu.co.udes.spafeu.web.app.models.dto.ProtocolObjectiveDTO;
import edu.co.udes.spafeu.web.app.models.entities.ActivityType;
import edu.co.udes.spafeu.web.app.models.entities.Exercise;
import edu.co.udes.spafeu.web.app.models.entities.PhysicalActivityProtocol;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import edu.co.udes.spafeu.web.app.repositories.ExerciseRepository;
import edu.co.udes.spafeu.web.app.repositories.PhysicalActivityProtocolRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de PhysicalActivityProtocolServiceImpl")
@Log4j2
class PhysicalActivityProtocolServiceImplTest {

    @Mock
    private PhysicalActivityProtocolRepository physicalActivityProtocolRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private PhysicalActivityProtocolServiceImpl physicalActivityProtocolService;

    private PhysicalActivityProtocol protocolEntity;
    private PhysicalActivityProtocol protocolEntityNullNivel;
    private PhysicalActivityProtocolDTO protocolDTO;
    private PhysicalActivityProtocolDTO protocolDTONullNivel;

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

        protocolDTONullNivel = new PhysicalActivityProtocolDTO();
        protocolDTONullNivel.setId(2L);
        protocolDTONullNivel.setName("Protocolo Fuerza");
        protocolDTONullNivel.setDescription("Protocolo para mejorar la fuerza muscular");
        protocolDTONullNivel.setSuggestedDuration(durationDTO);
        protocolDTONullNivel.setObjectives(List.of(objectiveDTO));

        protocolEntity = new PhysicalActivityProtocol();
        protocolEntity.setId(1L);
        protocolEntity.setName("Protocolo Resistencia");
        protocolEntity.setDescription("Protocolo para mejorar la resistencia cardiovascular");
        protocolEntity.setNivelActividadFisica(NivelActividadFisicaEnum.NIVEL_3);

        protocolEntityNullNivel = new PhysicalActivityProtocol();
        protocolEntityNullNivel.setId(2L);
        protocolEntityNullNivel.setName("Protocolo Fuerza");
        protocolEntityNullNivel.setDescription("Protocolo para mejorar la fuerza muscular");
        protocolEntityNullNivel.setNivelActividadFisica(null);
    }

    @Nested
    @DisplayName("findAllWithoutExercises()")
    class FindAllWithoutExercises {

        @Test
        @DisplayName("Debe retornar lista de protocolos mapeados correctamente con nivel de actividad física")
        void debeRetornarListaDeProtocolosMapeados() throws Exception {
            List<PhysicalActivityProtocol> entities = List.of(protocolEntity, protocolEntityNullNivel);
            when(physicalActivityProtocolRepository.findAllWithoutExercises()).thenReturn(entities);
            when(modelMapper.map(protocolEntity, PhysicalActivityProtocolDTO.class)).thenReturn(protocolDTO);
            when(modelMapper.map(protocolEntityNullNivel, PhysicalActivityProtocolDTO.class)).thenReturn(protocolDTONullNivel);

            List<PhysicalActivityProtocolDTO> resultado = physicalActivityProtocolService.findAllWithoutExercises();

            assertThat(resultado).isNotNull();
            assertThat(resultado).hasSize(2);
            assertThat(resultado.get(0).getId()).isEqualTo(1L);
            assertThat(resultado.get(0).getNivelActividadFisica()).isEqualTo(NivelActividadFisicaEnum.NIVEL_3);
            assertThat(resultado.get(1).getId()).isEqualTo(2L);
            assertThat(resultado.get(1).getNivelActividadFisica()).isNull();

            verify(physicalActivityProtocolRepository, times(1)).findAllWithoutExercises();
            verify(modelMapper, times(1)).map(protocolEntity, PhysicalActivityProtocolDTO.class);
            verify(modelMapper, times(1)).map(protocolEntityNullNivel, PhysicalActivityProtocolDTO.class);
            log.info("Test findAllWithoutExercises con datos: {} protocolos retornados", resultado.size());
        }

        @Test
        @DisplayName("Debe retornar lista vacía cuando no hay protocolos registrados")
        void debeRetornarListaVaciaCuandoNoHayProtocolos() throws Exception {
            when(physicalActivityProtocolRepository.findAllWithoutExercises()).thenReturn(List.of());

            List<PhysicalActivityProtocolDTO> resultado = physicalActivityProtocolService.findAllWithoutExercises();

            assertThat(resultado).isNotNull();
            assertThat(resultado).isEmpty();
            verify(physicalActivityProtocolRepository, times(1)).findAllWithoutExercises();
            log.info("Test findAllWithoutExercises sin datos: lista vacía retornada");
        }
    }

    @Nested
    @DisplayName("findAllByNivelActividadFisicaWithoutExercises(NivelActividadFisicaEnum)")
    class FindAllByNivelActividadFisicaWithoutExercises {

        @Test
        @DisplayName("Debe retornar protocolo filtrado por nivel de actividad física cuando hay coincidencias")
        void debeRetornarProtocoloFiltradoConResultados() throws Exception {
            when(physicalActivityProtocolRepository.findAllByNivelActividadFisicaWithoutExercises(NivelActividadFisicaEnum.NIVEL_3)).thenReturn(protocolEntity);
            when(modelMapper.map(protocolEntity, PhysicalActivityProtocolDTO.class)).thenReturn(protocolDTO);

            PhysicalActivityProtocolDTO resultado = physicalActivityProtocolService.findAllByNivelActividadFisicaWithoutExercises(NivelActividadFisicaEnum.NIVEL_3);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(1L);
            assertThat(resultado.getNivelActividadFisica()).isEqualTo(NivelActividadFisicaEnum.NIVEL_3);

            verify(physicalActivityProtocolRepository, times(1)).findAllByNivelActividadFisicaWithoutExercises(NivelActividadFisicaEnum.NIVEL_3);
            verify(modelMapper, times(1)).map(protocolEntity, PhysicalActivityProtocolDTO.class);
            log.info("Test findAllByNivelActividadFisicaWithoutExercises con datos: protocolo retornado");
        }

        @Test
        @DisplayName("Debe retornar null cuando no hay protocolos para el nivel indicado")
        void debeRetornarNullCuandoNoHayCoincidencias() throws Exception {
            when(physicalActivityProtocolRepository.findAllByNivelActividadFisicaWithoutExercises(NivelActividadFisicaEnum.NIVEL_1)).thenReturn(null);

            PhysicalActivityProtocolDTO resultado = physicalActivityProtocolService.findAllByNivelActividadFisicaWithoutExercises(NivelActividadFisicaEnum.NIVEL_1);

            assertThat(resultado).isNull();
            verify(physicalActivityProtocolRepository, times(1)).findAllByNivelActividadFisicaWithoutExercises(NivelActividadFisicaEnum.NIVEL_1);
            log.info("Test findAllByNivelActividadFisicaWithoutExercises sin datos: null retornado");
        }
    }

    @Nested
    @DisplayName("findExercisesByNivelActividadFisicaGroupedByActivityType(NivelActividadFisicaEnum)")
    class FindExercisesByNivelActividadFisicaGroupedByActivityType {

        @Test
        @DisplayName("Debe retornar ejercicios agrupados por tipo de actividad correctamente")
        void debeRetornarEjerciciosAgrupadosPorTipoDeActividad() throws Exception {
            ActivityType cardioType = new ActivityType();
            cardioType.setId(1L);
            cardioType.setName("Cardio");

            ActivityType fuerzaType = new ActivityType();
            fuerzaType.setId(2L);
            fuerzaType.setName("Fuerza");

            Exercise exerciseCardio = new Exercise();
            exerciseCardio.setId(1L);
            exerciseCardio.setName("Correr");
            exerciseCardio.setTipoActividadFisica(cardioType);

            Exercise exerciseFuerza = new Exercise();
            exerciseFuerza.setId(2L);
            exerciseFuerza.setName("Levantar pesas");
            exerciseFuerza.setTipoActividadFisica(fuerzaType);

            List<Exercise> exercises = List.of(exerciseCardio, exerciseFuerza);

            ActivityTypeDTO cardioTypeDTO = new ActivityTypeDTO();
            cardioTypeDTO.setId(1L);
            cardioTypeDTO.setName("Cardio");

            ActivityTypeDTO fuerzaTypeDTO = new ActivityTypeDTO();
            fuerzaTypeDTO.setId(2L);
            fuerzaTypeDTO.setName("Fuerza");

            ExerciseDTO exerciseCardioDTO = new ExerciseDTO();
            exerciseCardioDTO.setId(1L);
            exerciseCardioDTO.setName("Correr");
            exerciseCardioDTO.setTipoActividadFisica(cardioTypeDTO);

            ExerciseDTO exerciseFuerzaDTO = new ExerciseDTO();
            exerciseFuerzaDTO.setId(2L);
            exerciseFuerzaDTO.setName("Levantar pesas");
            exerciseFuerzaDTO.setTipoActividadFisica(fuerzaTypeDTO);

            when(exerciseRepository.findByProtocolNivelActividadFisica(NivelActividadFisicaEnum.NIVEL_3)).thenReturn(exercises);
            when(modelMapper.map(exerciseCardio, ExerciseDTO.class)).thenReturn(exerciseCardioDTO);
            when(modelMapper.map(exerciseFuerza, ExerciseDTO.class)).thenReturn(exerciseFuerzaDTO);

            List<ExerciseGroupedByActivityTypeDTO> resultado =
                    physicalActivityProtocolService.findExercisesByNivelActividadFisicaGroupedByActivityType(NivelActividadFisicaEnum.NIVEL_3);

            assertThat(resultado).isNotNull();
            assertThat(resultado).hasSize(2);

            assertThat(resultado.get(0).getActivityType()).isEqualTo("Cardio");
            assertThat(resultado.get(0).getExercises()).hasSize(1);
            assertThat(resultado.get(0).getExercises().get(0).getName()).isEqualTo("Correr");

            assertThat(resultado.get(1).getActivityType()).isEqualTo("Fuerza");
            assertThat(resultado.get(1).getExercises()).hasSize(1);
            assertThat(resultado.get(1).getExercises().get(0).getName()).isEqualTo("Levantar pesas");

            verify(exerciseRepository, times(1)).findByProtocolNivelActividadFisica(NivelActividadFisicaEnum.NIVEL_3);
            verify(modelMapper, times(1)).map(exerciseCardio, ExerciseDTO.class);
            verify(modelMapper, times(1)).map(exerciseFuerza, ExerciseDTO.class);
            log.info("Test findExercisesByNivelActividadFisicaGroupedByActivityType: retorna {} grupos ordenados", resultado.size());
        }

        @Test
        @DisplayName("Debe retornar lista vacía cuando no hay ejercicios para el nivel")
        void debeRetornarListaVaciaCuandoNoHayEjercicios() throws Exception {
            when(exerciseRepository.findByProtocolNivelActividadFisica(NivelActividadFisicaEnum.NIVEL_3)).thenReturn(List.of());

            List<ExerciseGroupedByActivityTypeDTO> resultado =
                    physicalActivityProtocolService.findExercisesByNivelActividadFisicaGroupedByActivityType(NivelActividadFisicaEnum.NIVEL_3);

            assertThat(resultado).isNotNull();
            assertThat(resultado).isEmpty();

            verify(exerciseRepository, times(1)).findByProtocolNivelActividadFisica(NivelActividadFisicaEnum.NIVEL_3);
            log.info("Test findExercisesByNivelActividadFisicaGroupedByActivityType sin datos: lista vacía retornada");
        }
    }
}
