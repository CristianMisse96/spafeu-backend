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
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.UserLevelHistoryDTO;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.task.UserLevelHistoryServiceTask;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de UserLevelHistoryRestController")
@Log4j2
class UserLevelHistoryRestControllerTest {

    @Mock
    private UserLevelHistoryServiceTask userLevelHistoryServiceTask;

    @InjectMocks
    private UserLevelHistoryRestController userLevelHistoryRestController;

    @BeforeEach
    void setUp() {
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
    @DisplayName("GET /api/user-level-history")
    class FindByUserId {

        @Test
        @DisplayName("Debe obtener userId del SecurityContextHolder, delegar al ServiceTask y retornar 200 OK")
        void debeRetornar200ConHistoricoDeNiveles() {
            UserLevelHistoryDTO dto = new UserLevelHistoryDTO();
            dto.setId(1L);
            dto.setUserId(1L);
            dto.setNivelNuevo(NivelActividadFisicaEnum.NIVEL_2);
            dto.setFechaCambio(LocalDateTime.now());

            RespuestaServicioDTO<List<UserLevelHistoryDTO>> respuesta = new RespuestaServicioDTO<>();
            respuesta.setOk(true);
            respuesta.setNegocio(List.of(dto));
            respuesta.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, "Éxito", "Operación exitosa"));

            when(userLevelHistoryServiceTask.findByUserId(1L))
                    .thenReturn(new ResponseEntity<>(respuesta, HttpStatus.OK));

            ResponseEntity<RespuestaServicioDTO<List<UserLevelHistoryDTO>>> response =
                    userLevelHistoryRestController.findByUserId();

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().isOk()).isTrue();
            assertThat(response.getBody().getNegocio()).hasSize(1);
            assertThat(response.getBody().getNegocio().get(0).getId()).isEqualTo(1L);

            verify(userLevelHistoryServiceTask, times(1)).findByUserId(1L);
            log.info("Test GET /api/user-level-history: retorna 200 OK con historial de niveles");
        }

        @Test
        @DisplayName("Debe retornar 200 OK con lista vacía cuando no hay historial")
        void debeRetornar200ConListaVacia() {
            RespuestaServicioDTO<List<UserLevelHistoryDTO>> respuesta = new RespuestaServicioDTO<>();
            respuesta.setOk(true);
            respuesta.setNegocio(List.of());
            respuesta.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, "Éxito", "Sin datos"));

            when(userLevelHistoryServiceTask.findByUserId(1L))
                    .thenReturn(new ResponseEntity<>(respuesta, HttpStatus.OK));

            ResponseEntity<RespuestaServicioDTO<List<UserLevelHistoryDTO>>> response =
                    userLevelHistoryRestController.findByUserId();

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().isOk()).isTrue();
            assertThat(response.getBody().getNegocio()).isEmpty();

            verify(userLevelHistoryServiceTask, times(1)).findByUserId(1L);
            log.info("Test GET /api/user-level-history sin datos: retorna 200 OK con lista vacía");
        }
    }
}
