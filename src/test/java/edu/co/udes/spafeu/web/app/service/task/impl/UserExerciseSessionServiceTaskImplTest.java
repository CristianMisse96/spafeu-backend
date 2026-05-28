package edu.co.udes.spafeu.web.app.service.task.impl;

import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.KEY_SUCCESS;
import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.SUCCESS_DETALLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
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

import edu.co.udes.spafeu.web.app.models.dto.UserExerciseSessionDTO;
import edu.co.udes.spafeu.web.app.models.dto.UserProgressDashboardDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.entity.UserExerciseSessionService;
import edu.co.udes.spafeu.web.app.util.Messages;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de UserExerciseSessionServiceTaskImpl")
@Log4j2
class UserExerciseSessionServiceTaskImplTest {

    @Mock
    private Messages messages;

    @Mock
    private UserExerciseSessionService userExerciseSessionService;

    @InjectMocks
    private UserExerciseSessionServiceTaskImpl userExerciseSessionServiceTask;

    private UserProgressDashboardDTO dashboardDTO;

    @BeforeEach
    void setUp() {
        dashboardDTO = new UserProgressDashboardDTO();
        dashboardDTO.setTotalSesiones(10L);
        dashboardDTO.setTotalMinutos(300);
        dashboardDTO.setUltimaActividad(LocalDateTime.of(2025, 6, 1, 10, 0));

        UserExerciseSessionDTO sessionDTO = new UserExerciseSessionDTO();
        sessionDTO.setId(1L);
        sessionDTO.setExerciseId(1L);
        sessionDTO.setExerciseName("Sentadillas");
        sessionDTO.setExercisePhoto("sentadillas.jpg");
        sessionDTO.setActivityType("Fuerza");

        dashboardDTO.setSesionesRecientes(List.of(sessionDTO));
    }

    @Nested
    @DisplayName("getUserProgressDashboard(Long)")
    class GetUserProgressDashboard {

        @Test
        @DisplayName("Debe retornar 200 OK con UserProgressDashboardDTO en RespuestaServicioDTO")
        void debeRetornar200ConDashboard() throws Exception {
            // Arrange
            Long userId = 1L;
            when(userExerciseSessionService.getUserProgressDashboard(userId)).thenReturn(dashboardDTO);
            when(messages.getKey(KEY_SUCCESS)).thenReturn("Éxito");
            when(messages.getKey(SUCCESS_DETALLE)).thenReturn("Operación exitosa");

            // Act
            ResponseEntity<RespuestaServicioDTO<UserProgressDashboardDTO>> response =
                    userExerciseSessionServiceTask.getUserProgressDashboard(userId);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            RespuestaServicioDTO<UserProgressDashboardDTO> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isTrue();
            assertThat(body.getNegocio()).isNotNull();
            assertThat(body.getNegocio().getTotalSesiones()).isEqualTo(10L);
            assertThat(body.getNegocio().getTotalMinutos()).isEqualTo(300);
            assertThat(body.getNegocio().getSesionesRecientes()).hasSize(1);
            assertThat(body.getNegocio().getSesionesRecientes().get(0).getExerciseName()).isEqualTo("Sentadillas");
            assertThat(body.getMensajeDTO()).isNotNull();
            assertThat(body.getMensajeDTO().getSeverity()).isEqualTo(SeverityEnum.SUCCESS);
            assertThat(body.getMensajeDTO().getSummary()).isEqualTo("Éxito");
            assertThat(body.getMensajeDTO().getDetail()).isEqualTo("Operación exitosa");

            verify(userExerciseSessionService, times(1)).getUserProgressDashboard(userId);
            verify(messages, times(1)).getKey(KEY_SUCCESS);
            verify(messages, times(1)).getKey(SUCCESS_DETALLE);
            log.info("Test getUserProgressDashboard con éxito: retorna 200 OK con dashboard");
        }

        @Test
        @DisplayName("Debe retornar 200 OK con dashboard vacío cuando no hay datos")
        void debeRetornar200ConDashboardVacio() throws Exception {
            // Arrange
            Long userId = 1L;
            UserProgressDashboardDTO emptyDashboard = new UserProgressDashboardDTO();
            emptyDashboard.setTotalSesiones(0L);
            emptyDashboard.setTotalMinutos(0);
            emptyDashboard.setUltimaActividad(null);
            emptyDashboard.setSesionesRecientes(Collections.emptyList());

            when(userExerciseSessionService.getUserProgressDashboard(userId)).thenReturn(emptyDashboard);
            when(messages.getKey(KEY_SUCCESS)).thenReturn("Éxito");
            when(messages.getKey(SUCCESS_DETALLE)).thenReturn("Operación exitosa");

            // Act
            ResponseEntity<RespuestaServicioDTO<UserProgressDashboardDTO>> response =
                    userExerciseSessionServiceTask.getUserProgressDashboard(userId);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            RespuestaServicioDTO<UserProgressDashboardDTO> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isTrue();
            assertThat(body.getNegocio()).isNotNull();
            assertThat(body.getNegocio().getTotalSesiones()).isEqualTo(0L);
            assertThat(body.getNegocio().getTotalMinutos()).isEqualTo(0);
            assertThat(body.getNegocio().getUltimaActividad()).isNull();
            assertThat(body.getNegocio().getSesionesRecientes()).isEmpty();
            assertThat(body.getMensajeDTO()).isNotNull();
            assertThat(body.getMensajeDTO().getSeverity()).isEqualTo(SeverityEnum.SUCCESS);

            verify(userExerciseSessionService, times(1)).getUserProgressDashboard(userId);
            log.info("Test getUserProgressDashboard vacío: retorna 200 OK con dashboard vacío");
        }
    }
}
