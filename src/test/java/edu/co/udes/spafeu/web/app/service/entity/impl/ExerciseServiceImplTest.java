package edu.co.udes.spafeu.web.app.service.entity.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.ExerciseCompletionResultDTO;
import edu.co.udes.spafeu.web.app.models.dto.ExerciseDTO;
import edu.co.udes.spafeu.web.app.models.entities.Exercise;
import edu.co.udes.spafeu.web.app.models.entities.PhysicalActivityProtocol;
import edu.co.udes.spafeu.web.app.models.entities.UserExerciseSession;
import edu.co.udes.spafeu.web.app.models.entities.UserHealthProfile;
import edu.co.udes.spafeu.web.app.models.entities.Usuario;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import edu.co.udes.spafeu.web.app.repositories.ExerciseRepository;
import edu.co.udes.spafeu.web.app.repositories.UserExerciseSessionRepository;
import edu.co.udes.spafeu.web.app.repositories.UserHealthProfileRepository;
import edu.co.udes.spafeu.web.app.repositories.UsuarioRepository;
import edu.co.udes.spafeu.web.app.service.entity.UserLevelHistoryService;
import edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de ExerciseServiceImpl")
@Log4j2
class ExerciseServiceImplTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private UserExerciseSessionRepository userExerciseSessionRepository;

    @Mock
    private UserHealthProfileRepository userHealthProfileRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UserLevelHistoryService userLevelHistoryService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ExerciseServiceImpl exerciseService;

    private Exercise exerciseEntity;
    private ExerciseDTO exerciseDTO;
    private Usuario usuario;

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

        usuario = new Usuario();
        usuario.setId(1L);
    }

    @Nested
    @DisplayName("findById(Long, NivelActividadFisicaEnum)")
    class FindById {

        @Test
        @DisplayName("Debe retornar ExerciseDTO cuando el ejercicio existe y coincide el nivel")
        void debeRetornarExerciseDtoCuandoExisteYNivelCoincide() throws Exception {
            when(exerciseRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(exerciseEntity));
            when(userExerciseSessionRepository.existsByUserIdAndExerciseIdAndCompletadoTrue(1L, 1L)).thenReturn(false);
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            when(userExerciseSessionRepository.save(any(UserExerciseSession.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(modelMapper.map(exerciseEntity, ExerciseDTO.class)).thenReturn(exerciseDTO);

            ExerciseDTO resultado = exerciseService.findById(1L, NivelActividadFisicaEnum.NIVEL_3, 1L);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(1L);
            assertThat(resultado.getName()).isEqualTo("Sentadillas");
            assertThat(resultado.getDescripcionEjercicio()).isEqualTo("Ejercicio de fuerza para piernas");

            verify(exerciseRepository, times(1)).findByIdWithDetails(1L);
            verify(userExerciseSessionRepository, times(1)).existsByUserIdAndExerciseIdAndCompletadoTrue(1L, 1L);
            verify(usuarioRepository, times(1)).findById(1L);
            verify(userExerciseSessionRepository, times(1)).save(any(UserExerciseSession.class));
            verify(modelMapper, times(1)).map(exerciseEntity, ExerciseDTO.class);
            log.info("Test findById con éxito: retorna ExerciseDTO con id={}", resultado.getId());
        }

        @Test
        @DisplayName("Debe guardar UserExerciseSession cuando el ejercicio es válido")
        void debeGuardarUserExerciseSessionCuandoEjercicioEsValido() throws Exception {
            when(exerciseRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(exerciseEntity));
            when(userExerciseSessionRepository.existsByUserIdAndExerciseIdAndCompletadoTrue(1L, 1L)).thenReturn(false);
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            when(userExerciseSessionRepository.save(any(UserExerciseSession.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(modelMapper.map(exerciseEntity, ExerciseDTO.class)).thenReturn(exerciseDTO);

            exerciseService.findById(1L, NivelActividadFisicaEnum.NIVEL_3, 1L);

            verify(userExerciseSessionRepository, times(1)).save(any(UserExerciseSession.class));
            log.info("Test findById guarda sesión: userExerciseSessionRepository.save llamado exactamente una vez");
        }

        @Test
        @DisplayName("Debe lanzar SpafeuException cuando el ejercicio no existe")
        void debeLanzarSpafeuExceptionCuandoNoExiste() {
            when(exerciseRepository.findByIdWithDetails(999L)).thenReturn(Optional.empty());

            SpafeuException ex = assertThrows(SpafeuException.class,
                    () -> exerciseService.findById(999L, NivelActividadFisicaEnum.NIVEL_3, 1L));

            assertThat(ex.getMensaje()).isEqualTo(MensajesKeyConstantesUtil.ERROR_EXERCISE_NO_ENCONTRADO);
            verify(exerciseRepository, times(1)).findByIdWithDetails(999L);
            log.info("Test findById sin datos: lanza SpafeuException con mensaje='{}'", ex.getMensaje());
        }

        @Test
        @DisplayName("Debe lanzar SpafeuException cuando el ejercicio no pertenece al nivel indicado")
        void debeLanzarSpafeuExceptionCuandoNivelNoCoincide() {
            when(exerciseRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(exerciseEntity));

            SpafeuException ex = assertThrows(SpafeuException.class,
                    () -> exerciseService.findById(1L, NivelActividadFisicaEnum.NIVEL_1, 1L));

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
                    () -> exerciseService.findById(2L, NivelActividadFisicaEnum.NIVEL_3, 1L));

            assertThat(ex.getMensaje()).isEqualTo(MensajesKeyConstantesUtil.ERROR_EXERCISE_NIVEL_NO_PERMITIDO);
            verify(exerciseRepository, times(1)).findByIdWithDetails(2L);
            log.info("Test findById protocolo nulo: lanza SpafeuException con mensaje='{}'", ex.getMensaje());
        }

        @Test
        @DisplayName("Debe lanzar SpafeuException cuando el ejercicio ya fue completado por el usuario")
        void debeLanzarSpafeuExceptionCuandoEjercicioYaCompletado() {
            when(exerciseRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(exerciseEntity));
            when(userExerciseSessionRepository.existsByUserIdAndExerciseIdAndCompletadoTrue(1L, 1L)).thenReturn(true);

            SpafeuException ex = assertThrows(SpafeuException.class,
                    () -> exerciseService.findById(1L, NivelActividadFisicaEnum.NIVEL_3, 1L));

            assertThat(ex.getMensaje()).isEqualTo(MensajesKeyConstantesUtil.ERROR_EXERCISE_COMPLETO);
            verify(exerciseRepository, times(1)).findByIdWithDetails(1L);
            verify(userExerciseSessionRepository, times(1)).existsByUserIdAndExerciseIdAndCompletadoTrue(1L, 1L);
            log.info("Test findById ejercicio completado: lanza SpafeuException con mensaje='{}'", ex.getMensaje());
        }

        @Test
        @DisplayName("Debe lanzar SpafeuException cuando el usuario no existe")
        void debeLanzarSpafeuExceptionCuandoUsuarioNoExiste() {
            when(exerciseRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(exerciseEntity));
            when(userExerciseSessionRepository.existsByUserIdAndExerciseIdAndCompletadoTrue(1L, 1L)).thenReturn(false);
            when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

            SpafeuException ex = assertThrows(SpafeuException.class,
                    () -> exerciseService.findById(1L, NivelActividadFisicaEnum.NIVEL_3, 1L));

            assertThat(ex.getMensaje()).isEqualTo(MensajesKeyConstantesUtil.ERROR_USUARIO_NO_EXISTE);
            verify(exerciseRepository, times(1)).findByIdWithDetails(1L);
            verify(userExerciseSessionRepository, times(1)).existsByUserIdAndExerciseIdAndCompletadoTrue(1L, 1L);
            verify(usuarioRepository, times(1)).findById(1L);
            log.info("Test findById usuario no existe: lanza SpafeuException con mensaje='{}'", ex.getMensaje());
        }
    }

    @Nested
    @DisplayName("findCompletedExerciseIdsByUserId(Long)")
    class FindCompletedExerciseIdsByUserId {

        @Test
        @DisplayName("Debe retornar Set con IDs de ejercicios completados")
        void debeRetornarSetConIds() throws SpafeuException {
            Set<Long> expected = new HashSet<>(Set.of(1L, 2L, 3L));
            when(userExerciseSessionRepository.findCompletedExerciseIdsByUserId(1L, true)).thenReturn(expected);

            Set<Long> resultado = exerciseService.findCompletedExerciseIdsByUserId(1L);

            assertThat(resultado).isNotNull();
            assertThat(resultado).containsExactlyInAnyOrder(1L, 2L, 3L);
            verify(userExerciseSessionRepository, times(1)).findCompletedExerciseIdsByUserId(1L, true);
            log.info("Test findCompletedExerciseIdsByUserId con éxito: retorna {} IDs", resultado.size());
        }

        @Test
        @DisplayName("Debe retornar Set vacío cuando no hay ejercicios completados")
        void debeRetornarSetVacio() throws SpafeuException {
            Set<Long> expected = new HashSet<>();
            when(userExerciseSessionRepository.findCompletedExerciseIdsByUserId(1L, true)).thenReturn(expected);

            Set<Long> resultado = exerciseService.findCompletedExerciseIdsByUserId(1L);

            assertThat(resultado).isNotNull();
            assertThat(resultado).isEmpty();
            verify(userExerciseSessionRepository, times(1)).findCompletedExerciseIdsByUserId(1L, true);
            log.info("Test findCompletedExerciseIdsByUserId sin datos: retorna set vacío");
        }
    }

    @Nested
    @DisplayName("completeExerciseSession(Long, Long)")
    class CompleteExerciseSession {

        @Test
        @DisplayName("Debe completar la sesión activa sin subir de nivel cuando no completa el protocolo")
        void debeCompletarSesionActivaSinSubirNivel() throws SpafeuException {
            UserExerciseSession session = new UserExerciseSession();
            session.setId(1L);
            session.setFechaInicio(LocalDateTime.now().minusMinutes(10));
            session.setCompletado(false);
            session.setMinutosRealizados(0);

            UserHealthProfile profile = new UserHealthProfile();
            profile.setUserId(1L);
            profile.setNivelActividadFisica(NivelActividadFisicaEnum.NIVEL_3);

            Exercise protocolExercise = new Exercise();
            protocolExercise.setId(2L);

            when(userExerciseSessionRepository.findByUserIdAndExerciseIdAndCompletadoFalse(1L, 1L))
                    .thenReturn(Optional.of(session));
            when(userExerciseSessionRepository.save(any(UserExerciseSession.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));
            when(userHealthProfileRepository.findById(1L)).thenReturn(Optional.of(profile));
            when(exerciseRepository.findByProtocolNivelActividadFisica(NivelActividadFisicaEnum.NIVEL_3))
                    .thenReturn(List.of(protocolExercise));
            when(userExerciseSessionRepository.findCompletedExerciseIdsByUserId(1L, true))
                    .thenReturn(Set.of(1L));

            ExerciseCompletionResultDTO resultado = exerciseService.completeExerciseSession(1L, 1L);

            ArgumentCaptor<UserExerciseSession> captor = ArgumentCaptor.forClass(UserExerciseSession.class);
            verify(userExerciseSessionRepository, times(1)).save(captor.capture());
            UserExerciseSession saved = captor.getValue();

            assertThat(saved.getCompletado()).isTrue();
            assertThat(saved.getFechaFin()).isNotNull();
            assertThat(saved.getMinutosRealizados()).isGreaterThanOrEqualTo(10);
            assertThat(resultado).isNotNull();
            assertThat(resultado.getSubioNivel()).isFalse();
            assertThat(resultado.getNivelAnterior()).isEqualTo(NivelActividadFisicaEnum.NIVEL_3);
            assertThat(resultado.getNivelNuevo()).isNull();
            assertThat(resultado.getMensaje()).isEqualTo(MensajesKeyConstantesUtil.SUCCESS_EXERCISE_COMPLETADO);
            log.info("Test completeExerciseSession sin subir nivel: sesión completada con {} minutos", saved.getMinutosRealizados());
        }

        @Test
        @DisplayName("Debe subir de nivel cuando se completan todos los ejercicios del protocolo")
        void debeSubirNivelCuandoCompletaProtocolo() throws SpafeuException {
            UserExerciseSession session = new UserExerciseSession();
            session.setId(1L);
            session.setFechaInicio(LocalDateTime.now().minusMinutes(10));
            session.setCompletado(false);
            session.setMinutosRealizados(0);

            UserHealthProfile profile = new UserHealthProfile();
            profile.setUserId(1L);
            profile.setNivelActividadFisica(NivelActividadFisicaEnum.NIVEL_2);

            Exercise protocolExercise = new Exercise();
            protocolExercise.setId(1L);

            when(userExerciseSessionRepository.findByUserIdAndExerciseIdAndCompletadoFalse(1L, 1L))
                    .thenReturn(Optional.of(session));
            when(userExerciseSessionRepository.save(any(UserExerciseSession.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));
            when(userHealthProfileRepository.findById(1L)).thenReturn(Optional.of(profile));
            when(exerciseRepository.findByProtocolNivelActividadFisica(NivelActividadFisicaEnum.NIVEL_2))
                    .thenReturn(List.of(protocolExercise));
            when(userExerciseSessionRepository.findCompletedExerciseIdsByUserId(1L, true))
                    .thenReturn(Set.of(1L));
            when(userHealthProfileRepository.save(any(UserHealthProfile.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            ExerciseCompletionResultDTO resultado = exerciseService.completeExerciseSession(1L, 1L);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getSubioNivel()).isTrue();
            assertThat(resultado.getUltimoNivel()).isFalse();
            assertThat(resultado.getNivelAnterior()).isEqualTo(NivelActividadFisicaEnum.NIVEL_2);
            assertThat(resultado.getNivelNuevo()).isEqualTo(NivelActividadFisicaEnum.NIVEL_3);
            assertThat(resultado.getMensaje()).isEqualTo(MensajesKeyConstantesUtil.SUCCESS_NIVEL_SUBIDO);
            assertThat(profile.getNivelActividadFisica()).isEqualTo(NivelActividadFisicaEnum.NIVEL_3);
            verify(userLevelHistoryService, times(1)).registrarCambioNivel(1L, NivelActividadFisicaEnum.NIVEL_2, NivelActividadFisicaEnum.NIVEL_3, "Completó el protocolo del Actividad Leve");
            log.info("Test completeExerciseSession sube nivel: NIVEL_2 -> NIVEL_3");
        }

        @Test
        @DisplayName("Debe subir de NIVEL_3 a NIVEL_4 sin marcar ultimoNivel")
        void debeSubirDeNivel3A4SinMarcarUltimoNivel() throws SpafeuException {
            UserExerciseSession session = new UserExerciseSession();
            session.setId(1L);
            session.setFechaInicio(LocalDateTime.now().minusMinutes(10));
            session.setCompletado(false);
            session.setMinutosRealizados(0);

            UserHealthProfile profile = new UserHealthProfile();
            profile.setUserId(1L);
            profile.setNivelActividadFisica(NivelActividadFisicaEnum.NIVEL_3);

            Exercise protocolExercise = new Exercise();
            protocolExercise.setId(1L);

            when(userExerciseSessionRepository.findByUserIdAndExerciseIdAndCompletadoFalse(1L, 1L))
                    .thenReturn(Optional.of(session));
            when(userExerciseSessionRepository.save(any(UserExerciseSession.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));
            when(userHealthProfileRepository.findById(1L)).thenReturn(Optional.of(profile));
            when(exerciseRepository.findByProtocolNivelActividadFisica(NivelActividadFisicaEnum.NIVEL_3))
                    .thenReturn(List.of(protocolExercise));
            when(userExerciseSessionRepository.findCompletedExerciseIdsByUserId(1L, true))
                    .thenReturn(Set.of(1L));
            when(userHealthProfileRepository.save(any(UserHealthProfile.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            ExerciseCompletionResultDTO resultado = exerciseService.completeExerciseSession(1L, 1L);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getSubioNivel()).isTrue();
            assertThat(resultado.getUltimoNivel()).isFalse();
            assertThat(resultado.getNivelAnterior()).isEqualTo(NivelActividadFisicaEnum.NIVEL_3);
            assertThat(resultado.getNivelNuevo()).isEqualTo(NivelActividadFisicaEnum.NIVEL_4);
            assertThat(resultado.getMensaje()).isEqualTo(MensajesKeyConstantesUtil.SUCCESS_NIVEL_SUBIDO);
            assertThat(profile.getNivelActividadFisica()).isEqualTo(NivelActividadFisicaEnum.NIVEL_4);
            verify(userLevelHistoryService, times(1)).registrarCambioNivel(1L, NivelActividadFisicaEnum.NIVEL_3, NivelActividadFisicaEnum.NIVEL_4, "Completó el protocolo del Actividad moderada");
            log.info("Test completeExerciseSession sube nivel: NIVEL_3 -> NIVEL_4 sin ultimoNivel");
        }

        @Test
        @DisplayName("Debe retornar último nivel completado cuando el usuario ya está en NIVEL_4")
        void debeRetornarUltimoNivelCompletado() throws SpafeuException {
            UserExerciseSession session = new UserExerciseSession();
            session.setId(1L);
            session.setFechaInicio(LocalDateTime.now().minusMinutes(10));
            session.setCompletado(false);
            session.setMinutosRealizados(0);

            UserHealthProfile profile = new UserHealthProfile();
            profile.setUserId(1L);
            profile.setNivelActividadFisica(NivelActividadFisicaEnum.NIVEL_4);

            Exercise protocolExercise = new Exercise();
            protocolExercise.setId(1L);

            when(userExerciseSessionRepository.findByUserIdAndExerciseIdAndCompletadoFalse(1L, 1L))
                    .thenReturn(Optional.of(session));
            when(userExerciseSessionRepository.save(any(UserExerciseSession.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));
            when(userHealthProfileRepository.findById(1L)).thenReturn(Optional.of(profile));
            when(exerciseRepository.findByProtocolNivelActividadFisica(NivelActividadFisicaEnum.NIVEL_4))
                    .thenReturn(List.of(protocolExercise));
            when(userExerciseSessionRepository.findCompletedExerciseIdsByUserId(1L, true))
                    .thenReturn(Set.of(1L));

            ExerciseCompletionResultDTO resultado = exerciseService.completeExerciseSession(1L, 1L);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getSubioNivel()).isFalse();
            assertThat(resultado.getUltimoNivel()).isTrue();
            assertThat(resultado.getNivelAnterior()).isEqualTo(NivelActividadFisicaEnum.NIVEL_4);
            assertThat(resultado.getNivelNuevo()).isEqualTo(NivelActividadFisicaEnum.NIVEL_4);
            assertThat(resultado.getMensaje()).isEqualTo(MensajesKeyConstantesUtil.SUCCESS_ULTIMO_NIVEL_COMPLETADO);
            log.info("Test completeExerciseSession último nivel: NIVEL_4 completado");
        }

        @Test
        @DisplayName("Debe lanzar SpafeuException cuando no existe sesión activa")
        void debeLanzarExcepcionCuandoNoExisteSesionActiva() {
            when(userExerciseSessionRepository.findByUserIdAndExerciseIdAndCompletadoFalse(1L, 1L))
                    .thenReturn(Optional.empty());

            SpafeuException ex = assertThrows(SpafeuException.class,
                    () -> exerciseService.completeExerciseSession(1L, 1L));

            assertThat(ex.getMensaje()).isEqualTo(MensajesKeyConstantesUtil.ERROR_EXERCISE_SESSION_NO_ENCONTRADA);
            verify(userExerciseSessionRepository, times(1)).findByUserIdAndExerciseIdAndCompletadoFalse(1L, 1L);
            log.info("Test completeExerciseSession sin sesión: lanza SpafeuException con mensaje='{}'", ex.getMensaje());
        }
    }
}
