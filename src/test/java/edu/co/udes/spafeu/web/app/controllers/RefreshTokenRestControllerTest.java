package edu.co.udes.spafeu.web.app.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.co.udes.spafeu.web.app.models.dto.RefreshTokenResponseDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.service.task.RefreshTokenServiceTask;

/**
 * <p>Título: Proyecto SPAFEU</p>
 * <p>Descripción: Tests unitarios para RefreshTokenRestController — delegación
 * de refresh de access token y logout al ServiceTask.</p>
 *
 * @author AGENT-QA-EXPERT-SPAFEU
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de RefreshTokenRestController")
class RefreshTokenRestControllerTest {

    @Mock
    private RefreshTokenServiceTask refreshTokenServiceTask;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private RefreshTokenRestController refreshTokenRestController;

    @Nested
    @DisplayName("POST /api/auth/refresh")
    class Refresh {

        @Test
        @DisplayName("Debe delegar a RefreshTokenServiceTask.refreshAccessToken")
        void debeDelegarRefreshAccessToken() {
            RespuestaServicioDTO<RefreshTokenResponseDTO> respuesta = new RespuestaServicioDTO<>();
            respuesta.setOk(true);
            ResponseEntity<RespuestaServicioDTO<RefreshTokenResponseDTO>> expectedResponse =
                    new ResponseEntity<>(respuesta, HttpStatus.OK);

            when(refreshTokenServiceTask.refreshAccessToken(request, response)).thenReturn(expectedResponse);

            ResponseEntity<RespuestaServicioDTO<RefreshTokenResponseDTO>> result =
                    refreshTokenRestController.refresh(request, response);

            assertThat(result).isEqualTo(expectedResponse);
            verify(refreshTokenServiceTask).refreshAccessToken(request, response);
        }
    }

    @Nested
    @DisplayName("POST /api/auth/logout")
    class Logout {

        @Test
        @DisplayName("Debe delegar a RefreshTokenServiceTask.logout")
        void debeDelegarLogout() {
            RespuestaServicioDTO<String> respuesta = new RespuestaServicioDTO<>();
            respuesta.setOk(true);
            ResponseEntity<RespuestaServicioDTO<String>> expectedResponse =
                    new ResponseEntity<>(respuesta, HttpStatus.OK);

            when(refreshTokenServiceTask.logout(request, response)).thenReturn(expectedResponse);

            ResponseEntity<RespuestaServicioDTO<String>> result =
                    refreshTokenRestController.logout(request, response);

            assertThat(result).isEqualTo(expectedResponse);
            verify(refreshTokenServiceTask).logout(request, response);
        }
    }
}
