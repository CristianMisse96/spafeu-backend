package edu.co.udes.spafeu.web.app.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

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

import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.UserExerciseSessionDTO;
import edu.co.udes.spafeu.web.app.models.dto.UserProgressDashboardDTO;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.task.UserExerciseSessionServiceTask;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de UserExerciseSessionRestController")
@Log4j2
class UserExerciseSessionRestControllerTest {

    @Mock
    private UserExerciseSessionServiceTask userExerciseSessionServiceTask;

    @InjectMocks
    private UserExerciseSessionRestController userExerciseSessionRestController;

    private UserProgressDashboardDTO dashboardDTO;
    private RespuestaServicioDTO<UserProgressDashboardDTO> respuestaServicioDTO;
    private ResponseEntity<RespuestaServicioDTO<UserProgressDashboardDTO>> serviceTaskResponse;

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

        respuestaServicioDTO = new RespuestaServicioDTO<>();
        respuestaServicioDTO.setOk(true);
        respuestaServicioDTO.setNegocio(dashboardDTO);
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
    @DisplayName("GET /api/user-exercise-sessions/dashboard")
    class GetUserProgressDashboard {

        @Test
        @DisplayName("Debe retornar 200 OK y delegar al ServiceTask")
        void debeRetornar200YDelegarAlServiceTask() {
            when(userExerciseSessionServiceTask.getUserProgressDashboard(1L)).thenReturn(serviceTaskResponse);

            ResponseEntity<RespuestaServicioDTO<UserProgressDashboardDTO>> response =
                    userExerciseSessionRestController.getUserProgressDashboard();

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().isOk()).isTrue();
            assertThat(response.getBody().getNegocio()).isNotNull();
            assertThat(response.getBody().getNegocio().getTotalSesiones()).isEqualTo(10L);
            assertThat(response.getBody().getNegocio().getTotalMinutos()).isEqualTo(300);
            assertThat(response.getBody().getNegocio().getSesionesRecientes()).hasSize(1);
            assertThat(response.getBody().getNegocio().getSesionesRecientes().get(0).getExerciseName()).isEqualTo("Sentadillas");

            verify(userExerciseSessionServiceTask, times(1)).getUserProgressDashboard(1L);
            log.info("Test GET /dashboard con éxito: retorna 200 OK con dashboard");
        }

        @Test
        @DisplayName("Debe retornar 200 OK cuando el ServiceTask retorna datos vacíos")
        void debeRetornar200ConDatosVacios() {
            UserProgressDashboardDTO emptyDashboard = new UserProgressDashboardDTO();
            emptyDashboard.setTotalSesiones(0L);
            emptyDashboard.setTotalMinutos(0);
            emptyDashboard.setUltimaActividad(null);
            emptyDashboard.setSesionesRecientes(List.of());

            RespuestaServicioDTO<UserProgressDashboardDTO> respuestaVacia = new RespuestaServicioDTO<>();
            respuestaVacia.setOk(true);
            respuestaVacia.setNegocio(emptyDashboard);
            respuestaVacia.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, "Éxito", "Sin datos"));

            when(userExerciseSessionServiceTask.getUserProgressDashboard(1L))
                    .thenReturn(new ResponseEntity<>(respuestaVacia, HttpStatus.OK));

            ResponseEntity<RespuestaServicioDTO<UserProgressDashboardDTO>> response =
                    userExerciseSessionRestController.getUserProgressDashboard();

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().isOk()).isTrue();
            assertThat(response.getBody().getNegocio()).isNotNull();
            assertThat(response.getBody().getNegocio().getTotalSesiones()).isEqualTo(0L);
            assertThat(response.getBody().getNegocio().getSesionesRecientes()).isEmpty();

            verify(userExerciseSessionServiceTask, times(1)).getUserProgressDashboard(1L);
            log.info("Test GET /dashboard sin datos: retorna 200 OK con dashboard vacío");
        }

        @Test
        @DisplayName("Debe obtener userId del SecurityContextHolder y pasarlo al ServiceTask")
        void debeObtenerUserIdDelSecurityContext() {
            when(userExerciseSessionServiceTask.getUserProgressDashboard(1L)).thenReturn(serviceTaskResponse);

            userExerciseSessionRestController.getUserProgressDashboard();

            verify(userExerciseSessionServiceTask, times(1)).getUserProgressDashboard(1L);
            log.info("Test GET /dashboard: userId=1L obtenido del SecurityContextHolder y pasado al ServiceTask");
        }
    }
}
