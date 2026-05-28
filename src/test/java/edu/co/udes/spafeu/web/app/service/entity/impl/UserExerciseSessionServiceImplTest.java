package edu.co.udes.spafeu.web.app.service.entity.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

import edu.co.udes.spafeu.web.app.models.dto.UserExerciseSessionDTO;
import edu.co.udes.spafeu.web.app.models.dto.UserProgressDashboardDTO;
import edu.co.udes.spafeu.web.app.models.entities.ActivityType;
import edu.co.udes.spafeu.web.app.models.entities.Exercise;
import edu.co.udes.spafeu.web.app.models.entities.UserExerciseSession;
import edu.co.udes.spafeu.web.app.repositories.UserExerciseSessionRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de UserExerciseSessionServiceImpl")
@Log4j2
class UserExerciseSessionServiceImplTest {

    @Mock
    private UserExerciseSessionRepository userExerciseSessionRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserExerciseSessionServiceImpl userExerciseSessionService;

    private ActivityType activityType;
    private Exercise exercise;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.of(2025, 6, 1, 10, 0);

        activityType = new ActivityType();
        activityType.setId(1L);
        activityType.setName("Fuerza");

        exercise = new Exercise();
        exercise.setId(1L);
        exercise.setName("Sentadillas");
        exercise.setFoto("sentadillas.jpg");
        exercise.setTipoActividadFisica(activityType);
    }

    private void configurarModelMapperStub() {
        when(modelMapper.map(any(UserExerciseSession.class), eq(UserExerciseSessionDTO.class)))
                .thenAnswer(invocation -> {
                    UserExerciseSession session = invocation.getArgument(0);
                    UserExerciseSessionDTO dto = new UserExerciseSessionDTO();
                    dto.setId(session.getId());
                    dto.setFechaInicio(session.getFechaInicio());
                    dto.setFechaFin(session.getFechaFin());
                    dto.setMinutosRealizados(session.getMinutosRealizados());
                    dto.setCompletado(session.getCompletado());
                    return dto;
                });
    }

    private UserExerciseSession createSession(Long id, LocalDateTime fechaInicio, Integer minutos) {
        UserExerciseSession session = new UserExerciseSession();
        session.setId(id);
        session.setFechaInicio(fechaInicio);
        session.setMinutosRealizados(minutos);
        session.setCompletado(true);
        session.setExercise(exercise);
        return session;
    }

    @Nested
    @DisplayName("getUserProgressDashboard(Long)")
    class GetUserProgressDashboard {

        @Test
        @DisplayName("Debe retornar dashboard completo cuando el usuario tiene sesiones")
        void debeRetornarDashboardCompleto() throws Exception {
            configurarModelMapperStub();
            // Arrange
            Long userId = 1L;
            Long totalSesiones = 10L;
            Integer totalMinutos = 300;
            LocalDateTime lastActivity = now.minusDays(1);

            List<UserExerciseSession> recentSessions = List.of(
                    createSession(1L, now.minusDays(1), 30),
                    createSession(2L, now.minusDays(2), 45),
                    createSession(3L, now.minusDays(3), 20),
                    createSession(4L, now.minusDays(4), 60),
                    createSession(5L, now.minusDays(5), 15)
            );

            when(userExerciseSessionRepository.countCompletedSessionsByUserId(userId)).thenReturn(totalSesiones);
            when(userExerciseSessionRepository.sumMinutesCompletedByUserId(userId)).thenReturn(totalMinutos);
            when(userExerciseSessionRepository.findLastActivityDateByUserId(userId)).thenReturn(Optional.of(lastActivity));
            when(userExerciseSessionRepository.findRecentCompletedSessionsByUserId(userId)).thenReturn(recentSessions);

            // Act
            UserProgressDashboardDTO resultado = userExerciseSessionService.getUserProgressDashboard(userId);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado.getTotalSesiones()).isEqualTo(totalSesiones);
            assertThat(resultado.getTotalMinutos()).isEqualTo(totalMinutos);
            assertThat(resultado.getUltimaActividad()).isEqualTo(lastActivity);
            assertThat(resultado.getSesionesRecientes()).hasSize(5);
            assertThat(resultado.getSesionesRecientes().get(0).getExerciseId()).isEqualTo(exercise.getId());
            assertThat(resultado.getSesionesRecientes().get(0).getExerciseName()).isEqualTo(exercise.getName());
            assertThat(resultado.getSesionesRecientes().get(0).getExercisePhoto()).isEqualTo(exercise.getFoto());
            assertThat(resultado.getSesionesRecientes().get(0).getActivityType()).isEqualTo(activityType.getName());

            verify(userExerciseSessionRepository, times(1)).countCompletedSessionsByUserId(userId);
            verify(userExerciseSessionRepository, times(1)).sumMinutesCompletedByUserId(userId);
            verify(userExerciseSessionRepository, times(1)).findLastActivityDateByUserId(userId);
            verify(userExerciseSessionRepository, times(1)).findRecentCompletedSessionsByUserId(userId);
            verify(modelMapper, times(5)).map(any(UserExerciseSession.class), eq(UserExerciseSessionDTO.class));
            log.info("Test dashboard completo: {} sesiones, {} minutos, {} recientes", totalSesiones, totalMinutos, resultado.getSesionesRecientes().size());
        }

        @Test
        @DisplayName("Debe retornar dashboard vacío cuando el usuario no tiene sesiones")
        void debeRetornarDashboardVacio() throws Exception {
            // Arrange
            Long userId = 1L;

            when(userExerciseSessionRepository.countCompletedSessionsByUserId(userId)).thenReturn(0L);
            when(userExerciseSessionRepository.sumMinutesCompletedByUserId(userId)).thenReturn(0);
            when(userExerciseSessionRepository.findLastActivityDateByUserId(userId)).thenReturn(Optional.empty());
            when(userExerciseSessionRepository.findRecentCompletedSessionsByUserId(userId)).thenReturn(Collections.emptyList());

            // Act
            UserProgressDashboardDTO resultado = userExerciseSessionService.getUserProgressDashboard(userId);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado.getTotalSesiones()).isEqualTo(0L);
            assertThat(resultado.getTotalMinutos()).isEqualTo(0);
            assertThat(resultado.getUltimaActividad()).isNull();
            assertThat(resultado.getSesionesRecientes()).isEmpty();

            verify(userExerciseSessionRepository, times(1)).countCompletedSessionsByUserId(userId);
            verify(userExerciseSessionRepository, times(1)).sumMinutesCompletedByUserId(userId);
            verify(userExerciseSessionRepository, times(1)).findLastActivityDateByUserId(userId);
            verify(userExerciseSessionRepository, times(1)).findRecentCompletedSessionsByUserId(userId);
            verify(modelMapper, never()).map(any(UserExerciseSession.class), eq(UserExerciseSessionDTO.class));
            log.info("Test dashboard vacío: retorna 0 sesiones, 0 minutos, sin actividad");
        }

        @Test
        @DisplayName("Debe retornar las sesiones recientes mapeadas cuando hay menos de 5 sesiones")
        void debeRetornarMenosDe5Sesiones() throws Exception {
            configurarModelMapperStub();
            // Arrange
            Long userId = 1L;
            List<UserExerciseSession> recentSessions = List.of(
                    createSession(1L, now.minusDays(1), 30),
                    createSession(2L, now.minusDays(2), 45),
                    createSession(3L, now.minusDays(3), 20)
            );

            when(userExerciseSessionRepository.countCompletedSessionsByUserId(userId)).thenReturn(3L);
            when(userExerciseSessionRepository.sumMinutesCompletedByUserId(userId)).thenReturn(95);
            when(userExerciseSessionRepository.findLastActivityDateByUserId(userId)).thenReturn(Optional.of(now.minusDays(1)));
            when(userExerciseSessionRepository.findRecentCompletedSessionsByUserId(userId)).thenReturn(recentSessions);

            // Act
            UserProgressDashboardDTO resultado = userExerciseSessionService.getUserProgressDashboard(userId);

            // Assert
            assertThat(resultado.getSesionesRecientes()).hasSize(3);
            assertThat(resultado.getSesionesRecientes().get(0).getId()).isEqualTo(1L);
            assertThat(resultado.getSesionesRecientes().get(1).getId()).isEqualTo(2L);
            assertThat(resultado.getSesionesRecientes().get(2).getId()).isEqualTo(3L);

            verify(modelMapper, times(3)).map(any(UserExerciseSession.class), eq(UserExerciseSessionDTO.class));
            log.info("Test dashboard con 3 sesiones: retorna {} recientes mapeadas", resultado.getSesionesRecientes().size());
        }

        @Test
        @DisplayName("Debe retornar solo las primeras 5 sesiones cuando hay más de 5 sesiones")
        void debeRetornarSolo5Sesiones() throws Exception {
            configurarModelMapperStub();
            // Arrange
            Long userId = 1L;
            List<UserExerciseSession> recentSessions = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                recentSessions.add(createSession((long) i, now.minusDays(i), i * 10));
            }

            when(userExerciseSessionRepository.countCompletedSessionsByUserId(userId)).thenReturn(10L);
            when(userExerciseSessionRepository.sumMinutesCompletedByUserId(userId)).thenReturn(550);
            when(userExerciseSessionRepository.findLastActivityDateByUserId(userId)).thenReturn(Optional.of(now.minusDays(1)));
            when(userExerciseSessionRepository.findRecentCompletedSessionsByUserId(userId)).thenReturn(recentSessions);

            // Act
            UserProgressDashboardDTO resultado = userExerciseSessionService.getUserProgressDashboard(userId);

            // Assert
            assertThat(resultado.getSesionesRecientes()).hasSize(5);
            assertThat(resultado.getSesionesRecientes().get(0).getId()).isEqualTo(1L);
            assertThat(resultado.getSesionesRecientes().get(4).getId()).isEqualTo(5L);

            verify(modelMapper, times(5)).map(any(UserExerciseSession.class), eq(UserExerciseSessionDTO.class));
            log.info("Test dashboard con 10 sesiones: retorna solo 5 recientes mapeadas");
        }
    }
}
