package edu.co.udes.spafeu.web.app.service.entity.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

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

import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.ExerciseDTO;
import edu.co.udes.spafeu.web.app.models.entities.Exercise;
import edu.co.udes.spafeu.web.app.models.entities.PhysicalActivityProtocol;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import edu.co.udes.spafeu.web.app.repositories.ExerciseRepository;
import edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de ExerciseServiceImpl")
@Log4j2
class ExerciseServiceImplTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ExerciseServiceImpl exerciseService;

    private Exercise exerciseEntity;
    private ExerciseDTO exerciseDTO;

    @BeforeEach
    void setUp() {
        PhysicalActivityProtocol protocol = new PhysicalActivityProtocol();
        protocol.setId(1L);
        protocol.setNivelActividadFisica(NivelActividadFisicaEnum.NIVEL_3);

        exerciseEntity = new Exercise();
        exerciseEntity.setId(1L);
        exerciseEntity.setName("Sentadillas");
        exerciseEntity.setDescripcionEjercicio("Ejercicio de fuerza para piernas");
        exerciseEntity.setProtocol(protocol);

        exerciseDTO = new ExerciseDTO();
        exerciseDTO.setId(1L);
        exerciseDTO.setName("Sentadillas");
        exerciseDTO.setDescripcionEjercicio("Ejercicio de fuerza para piernas");
    }

    @Nested
    @DisplayName("findById(Long, NivelActividadFisicaEnum)")
    class FindById {

        @Test
        @DisplayName("Debe retornar ExerciseDTO cuando el ejercicio existe y coincide el nivel")
        void debeRetornarExerciseDtoCuandoExisteYNivelCoincide() throws Exception {
            when(exerciseRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(exerciseEntity));
            when(modelMapper.map(exerciseEntity, ExerciseDTO.class)).thenReturn(exerciseDTO);

            ExerciseDTO resultado = exerciseService.findById(1L, NivelActividadFisicaEnum.NIVEL_3);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(1L);
            assertThat(resultado.getName()).isEqualTo("Sentadillas");
            assertThat(resultado.getDescripcionEjercicio()).isEqualTo("Ejercicio de fuerza para piernas");

            verify(exerciseRepository, times(1)).findByIdWithDetails(1L);
            verify(modelMapper, times(1)).map(exerciseEntity, ExerciseDTO.class);
            log.info("Test findById con éxito: retorna ExerciseDTO con id={}", resultado.getId());
        }

        @Test
        @DisplayName("Debe lanzar SpafeuException cuando el ejercicio no existe")
        void debeLanzarSpafeuExceptionCuandoNoExiste() {
            when(exerciseRepository.findByIdWithDetails(999L)).thenReturn(Optional.empty());

            SpafeuException ex = assertThrows(SpafeuException.class,
                    () -> exerciseService.findById(999L, NivelActividadFisicaEnum.NIVEL_3));

            assertThat(ex.getMensaje()).isEqualTo(MensajesKeyConstantesUtil.ERROR_EXERCISE_NO_ENCONTRADO);
            verify(exerciseRepository, times(1)).findByIdWithDetails(999L);
            log.info("Test findById sin datos: lanza SpafeuException con mensaje='{}'", ex.getMensaje());
        }

        @Test
        @DisplayName("Debe lanzar SpafeuException cuando el ejercicio no pertenece al nivel indicado")
        void debeLanzarSpafeuExceptionCuandoNivelNoCoincide() {
            when(exerciseRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(exerciseEntity));

            SpafeuException ex = assertThrows(SpafeuException.class,
                    () -> exerciseService.findById(1L, NivelActividadFisicaEnum.NIVEL_1));

            assertThat(ex.getMensaje()).isEqualTo(MensajesKeyConstantesUtil.ERROR_EXERCISE_NIVEL_NO_PERMITIDO);
            verify(exerciseRepository, times(1)).findByIdWithDetails(1L);
            log.info("Test findById nivel no coincide: lanza SpafeuException con mensaje='{}'", ex.getMensaje());
        }

        @Test
        @DisplayName("Debe lanzar SpafeuException cuando el ejercicio no tiene protocolo asociado")
        void debeLanzarSpafeuExceptionCuandoProtocoloEsNulo() {
            Exercise exerciseSinProtocolo = new Exercise();
            exerciseSinProtocolo.setId(2L);
            exerciseSinProtocolo.setName("Flexiones");
            exerciseSinProtocolo.setProtocol(null);

            when(exerciseRepository.findByIdWithDetails(2L)).thenReturn(Optional.of(exerciseSinProtocolo));

            SpafeuException ex = assertThrows(SpafeuException.class,
                    () -> exerciseService.findById(2L, NivelActividadFisicaEnum.NIVEL_3));

            assertThat(ex.getMensaje()).isEqualTo(MensajesKeyConstantesUtil.ERROR_EXERCISE_NIVEL_NO_PERMITIDO);
            verify(exerciseRepository, times(1)).findByIdWithDetails(2L);
            log.info("Test findById protocolo nulo: lanza SpafeuException con mensaje='{}'", ex.getMensaje());
        }
    }
}
