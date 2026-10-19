package edu.co.udes.spafeu.web.app.service.task.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.log4j.Log4j2;
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
import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.UserLevelHistoryDTO;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.entity.UserLevelHistoryService;
import edu.co.udes.spafeu.web.app.util.Messages;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de UserLevelHistoryServiceTaskImpl")
@Log4j2
class UserLevelHistoryServiceTaskImplTest {

    @Mock
    private Messages messages;

    @Mock
    private UserLevelHistoryService userLevelHistoryService;

    @InjectMocks
    private UserLevelHistoryServiceTaskImpl userLevelHistoryServiceTask;

    @BeforeEach
    void setUp() {
        when(messages.getKey(any())).thenReturn("mock");
    }

    @Nested
    @DisplayName("findByUserId(Long)")
    class FindByUserId {

        @Test
        @DisplayName("Debe delegar al service y retornar 200 OK con RespuestaServicioDTO exitosa")
        void debeRetornar200ConRespuestaExitosa() throws Exception {
            UserLevelHistoryDTO dto1 = new UserLevelHistoryDTO();
            dto1.setId(1L);
            dto1.setUserId(1L);
            dto1.setNivelNuevo(NivelActividadFisicaEnum.NIVEL_2);
            dto1.setFechaCambio(LocalDateTime.now().minusDays(1));

            UserLevelHistoryDTO dto2 = new UserLevelHistoryDTO();
            dto2.setId(2L);
            dto2.setUserId(1L);
            dto2.setNivelNuevo(NivelActividadFisicaEnum.NIVEL_3);
            dto2.setFechaCambio(LocalDateTime.now());

            List<UserLevelHistoryDTO> historico = List.of(dto2, dto1);

            when(userLevelHistoryService.findByUserId(1L)).thenReturn(historico);

            ResponseEntity<RespuestaServicioDTO<List<UserLevelHistoryDTO>>> response =
                    userLevelHistoryServiceTask.findByUserId(1L);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            RespuestaServicioDTO<List<UserLevelHistoryDTO>> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isTrue();
            assertThat(body.getNegocio()).isNotNull();
            assertThat(body.getNegocio()).hasSize(2);
            assertThat(body.getNegocio().get(0).getId()).isEqualTo(2L);
            assertThat(body.getNegocio().get(1).getId()).isEqualTo(1L);

            MensajeDTO mensaje = body.getMensajeDTO();
            assertThat(mensaje).isNotNull();
            assertThat(mensaje.getSeverity()).isEqualTo(SeverityEnum.SUCCESS);
            assertThat(mensaje.getSummary()).isEqualTo("mock");
            assertThat(mensaje.getDetail()).isEqualTo("mock");

            verify(userLevelHistoryService, times(1)).findByUserId(1L);
            log.info("Test findByUserId: retorna 200 OK con {} registros históricos", body.getNegocio().size());
        }

        @Test
        @DisplayName("Debe retornar 200 OK con lista vacía cuando no hay registros")
        void debeRetornar200ConListaVacia() throws Exception {
            when(userLevelHistoryService.findByUserId(1L)).thenReturn(List.of());

            ResponseEntity<RespuestaServicioDTO<List<UserLevelHistoryDTO>>> response =
                    userLevelHistoryServiceTask.findByUserId(1L);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            RespuestaServicioDTO<List<UserLevelHistoryDTO>> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isTrue();
            assertThat(body.getNegocio()).isNotNull();
            assertThat(body.getNegocio()).isEmpty();

            verify(userLevelHistoryService, times(1)).findByUserId(1L);
            log.info("Test findByUserId sin datos: retorna 200 OK con lista vacía");
        }
    }
}
