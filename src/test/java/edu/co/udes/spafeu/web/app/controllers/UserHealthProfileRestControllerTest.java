package edu.co.udes.spafeu.web.app.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;

import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.UserHealthProfileDTO;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.task.UserHealthProfileServiceTask;
import edu.co.udes.spafeu.web.app.util.Messages;
import lombok.extern.log4j.Log4j2;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de UserHealthProfileRestController")
@MockitoSettings(strictness = Strictness.LENIENT)
@Log4j2
class UserHealthProfileRestControllerTest {

    @Mock
    private Messages messages;

    @Mock
    private UserHealthProfileServiceTask userHealthProfileServiceTask;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private UserHealthProfileRestController userHealthProfileRestController;

    private UserHealthProfileDTO profileDTO;

    @BeforeEach
    void setUp() {
        profileDTO = new UserHealthProfileDTO();
        profileDTO.setFullName("Juan Perez");
        profileDTO.setCampusId(1L);
        profileDTO.setProgramId(1L);
        profileDTO.setSemester(3);
        profileDTO.setBirthDate(LocalDate.of(2000, 1, 1));
        profileDTO.setSex(edu.co.udes.spafeu.web.app.models.enums.SexEnum.MALE);
        profileDTO.setWeightKg(new BigDecimal("70.50"));
        profileDTO.setHeightM(new BigDecimal("1.75"));

        lenient().when(messages.getKey(any())).thenReturn("mock");
        when(bindingResult.hasErrors()).thenReturn(false);

        // Mockear SecurityContextHolder para simular usuario autenticado
        SecurityContext securityContext = org.mockito.Mockito.mock(SecurityContext.class);
        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);
        when(authentication.getDetails()).thenReturn(1L);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Nested
    @DisplayName("POST /api/user/health-profile")
    class RegisterHealthProfile {

        @Test
        @DisplayName("Debe registrar el perfil de salud exitosamente con datos válidos")
        void registrarPerfilExitoso() {
            UserHealthProfileDTO savedProfile = new UserHealthProfileDTO();
            savedProfile.setFullName("Juan Perez");
            savedProfile.setCampusId(1L);
            savedProfile.setProgramId(1L);
            savedProfile.setSemester(3);

            RespuestaServicioDTO<UserHealthProfileDTO> respuesta = new RespuestaServicioDTO<>();
            respuesta.setOk(true);
            respuesta.setNegocio(savedProfile);
            respuesta.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS,
                    messages.getKey("success"), "Perfil registrado"));

            when(userHealthProfileServiceTask.registerHealthProfile(profileDTO, 1L))
                    .thenReturn(new ResponseEntity<>(respuesta, HttpStatus.CREATED));

            ResponseEntity<RespuestaServicioDTO<UserHealthProfileDTO>> result =
                    userHealthProfileRestController.registerHealthProfile(profileDTO, bindingResult);

            assertNotNull(result);
            assertEquals(HttpStatus.CREATED, result.getStatusCode());
            assertTrue(result.getBody().isOk());
            assertNotNull(result.getBody().getNegocio());
            assertEquals("Juan Perez", result.getBody().getNegocio().getFullName());
            verify(userHealthProfileServiceTask, times(1)).registerHealthProfile(profileDTO, 1L);
            log.info("Test registro exitoso: perfil de salud registrado correctamente");
        }

        @Test
        @DisplayName("Debe retornar BAD_REQUEST cuando hay errores de validación en el DTO")
        void registrarPerfilValidacionFallida() {
            when(bindingResult.hasErrors()).thenReturn(true);

            ResponseEntity<RespuestaServicioDTO<UserHealthProfileDTO>> result =
                    userHealthProfileRestController.registerHealthProfile(profileDTO, bindingResult);

            assertNotNull(result);
            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertFalse(result.getBody().isOk());
            assertNotNull(result.getBody().getMensajeDTO());
            assertEquals(SeverityEnum.ERROR, result.getBody().getMensajeDTO().getSeverity());
            verify(userHealthProfileServiceTask, never()).registerHealthProfile(any(), any());
            log.info("Test validación fallida: retorna BAD_REQUEST sin llamar al ServiceTask");
        }

        @Test
        @DisplayName("Debe retornar BAD_REQUEST cuando el DTO es nulo y bindingResult tiene errores")
        void registrarPerfilDtoNuloConErrores() {
            when(bindingResult.hasErrors()).thenReturn(true);

            ResponseEntity<RespuestaServicioDTO<UserHealthProfileDTO>> result =
                    userHealthProfileRestController.registerHealthProfile(null, bindingResult);

            assertNotNull(result);
            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertFalse(result.getBody().isOk());
            verify(userHealthProfileServiceTask, never()).registerHealthProfile(any(), any());
            log.info("Test DTO nulo con errores: retorna BAD_REQUEST");
        }
    }
}
