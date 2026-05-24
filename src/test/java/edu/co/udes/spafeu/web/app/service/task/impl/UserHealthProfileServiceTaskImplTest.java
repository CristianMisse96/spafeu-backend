package edu.co.udes.spafeu.web.app.service.task.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.UserHealthProfileDTO;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;
import edu.co.udes.spafeu.web.app.models.enums.EstadoKycEnum;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.entity.UserHealthProfileService;
import edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil;
import edu.co.udes.spafeu.web.app.util.Messages;
import lombok.extern.log4j.Log4j2;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de UserHealthProfileServiceTaskImpl")
@Log4j2
class UserHealthProfileServiceTaskImplTest {

    @Mock
    private Messages messages;

    @Mock
    private UserHealthProfileService userHealthProfileService;

    @InjectMocks
    private UserHealthProfileServiceTaskImpl userHealthProfileServiceTask;

    private UserHealthProfileDTO profileDTO;
    private UsuarioDTO usuarioResponse;

    @BeforeEach
    void setUp() {
        profileDTO = new UserHealthProfileDTO();
        profileDTO.setFullName("Juan Perez");
        profileDTO.setCampusId(1L);
        profileDTO.setProgramId(1L);
        profileDTO.setSemester(3);

        usuarioResponse = new UsuarioDTO();
        usuarioResponse.setId(1L);
        usuarioResponse.setEstadoKyc(EstadoKycEnum.COMPLETO);
    }

    @Nested
    @DisplayName("registerHealthProfile()")
    class RegisterHealthProfile {

        @Test
        @DisplayName("Debe retornar ResponseEntity con RespuestaServicioDTO y HttpStatus.CREATED")
        void debeRetornarResponseEntityConRespuestaServicio() {
            when(userHealthProfileService.registerHealthProfile(profileDTO, 1L)).thenReturn(usuarioResponse);
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_SUCCESS)).thenReturn("Éxito");
            when(messages.getKey(MensajesKeyConstantesUtil.PERFIL_SALUD_REGISTRO_EXITOSO)).thenReturn("Perfil registrado");

            ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> response =
                userHealthProfileServiceTask.registerHealthProfile(profileDTO, 1L);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

            RespuestaServicioDTO<UsuarioDTO> body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.isOk()).isTrue();
            assertThat(body.getNegocio()).isEqualTo(usuarioResponse);
            assertThat(body.getMensajeDTO()).isNotNull();
            assertThat(body.getMensajeDTO().getSeverity()).isEqualTo(SeverityEnum.SUCCESS);
            assertThat(body.getMensajeDTO().getSummary()).isEqualTo("Éxito");
            assertThat(body.getMensajeDTO().getDetail()).isEqualTo("Perfil registrado");

            verify(userHealthProfileService, times(1)).registerHealthProfile(profileDTO, 1L);
            log.info("Test registerHealthProfile: retorna ResponseEntity CREATED con datos correctos");
        }

        @Test
        @DisplayName("Debe construir RespuestaServicioDTO con ok=true y datos del usuario")
        void debeConstruirRespuestaServicioCorrectamente() {
            when(userHealthProfileService.registerHealthProfile(profileDTO, 1L)).thenReturn(usuarioResponse);
            when(messages.getKey(MensajesKeyConstantesUtil.KEY_SUCCESS)).thenReturn("Éxito");
            when(messages.getKey(MensajesKeyConstantesUtil.PERFIL_SALUD_REGISTRO_EXITOSO)).thenReturn("Perfil registrado");

            ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> response =
                userHealthProfileServiceTask.registerHealthProfile(profileDTO, 1L);

            RespuestaServicioDTO<UsuarioDTO> body = response.getBody();
            assertThat(body.getNegocio().getId()).isEqualTo(1L);
            assertThat(body.getNegocio().getEstadoKyc()).isEqualTo(EstadoKycEnum.COMPLETO);
            log.info("Test construcción de respuesta: campos del DTO mapeados correctamente");
        }
    }
}
