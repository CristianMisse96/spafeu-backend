package edu.co.udes.spafeu.web.app.controllers;

import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.KEY_ERROR_PETICION_GENERICO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.UserHealthProfileDTO;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.task.UserHealthProfileServiceTask;
import edu.co.udes.spafeu.web.app.util.Messages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Gestión de Perfil de Salud", description = "Endpoints para el registro del perfil de salud y respuestas del cuestionario del usuario")
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/user/health-profile")
@RequiredArgsConstructor
public class UserHealthProfileRestController {

    private final Messages messages;
    private final UserHealthProfileServiceTask userHealthProfileServiceTask;

    @Operation(summary = "Registrar el perfil de salud del usuario autenticado junto con sus respuestas al cuestionario",
        description = "Recibe los datos del perfil de salud (nombre, campus, programa, semestre, fecha de nacimiento, sexo, peso, altura) "
            + "y la lista de respuestas a las preguntas del cuestionario. El usuario se obtiene automáticamente del token JWT.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Perfil de salud registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o el usuario ya tiene un perfil registrado"),
        @ApiResponse(responseCode = "401", description = "Token JWT no válido o expirado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<RespuestaServicioDTO<UserHealthProfileDTO>> registerHealthProfile(
            @Valid @RequestBody UserHealthProfileDTO dto,
            BindingResult result) {

        if (result.hasErrors()) {
            RespuestaServicioDTO<UserHealthProfileDTO> respuesta = new RespuestaServicioDTO<>();
            respuesta.setMensajeDTO(new MensajeDTO(
                SeverityEnum.ERROR,
                messages.getKey(SeverityEnum.ERROR.getKey()),
                messages.getKey(KEY_ERROR_PETICION_GENERICO)
            ));
            respuesta.setOk(false);
            return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
        }

        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getDetails();

        return userHealthProfileServiceTask.registerHealthProfile(dto, userId);
    }
}
