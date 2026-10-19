package edu.co.udes.spafeu.web.app.controllers;

import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.KEY_ERROR_PETICION_GENERICO;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.task.AuthServiceTask;
import edu.co.udes.spafeu.web.app.util.Messages;

/**
 * <p>
 * Titulo: Proyecto SPAFEU
 * </p>
 * <p>
 * Descripción: Controlador REST para operaciones de autenticación y gestión de credenciales.
 * </p>
 *
 * @author AGENT-API-EXPERT-SPAFEU
 */
@Tag(name = "Autenticación", description = "Endpoints para registro, activación de cuenta, reenvío de correo de activación, recuperación y restablecimiento de contraseña")
@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

	private final Messages messages;
	private final AuthServiceTask authServiceTask;

	@Operation(
			summary = "Registrar usuario",
			description = "Registra un nuevo usuario en el sistema. La cuenta se crea inactiva y se envía "
					+ "un correo electrónico con un token de activación.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
			@ApiResponse(responseCode = "400", description = "Datos inválidos o usuario ya existe"),
			@ApiResponse(responseCode = "401", description = "Token JWT no válido o expirado"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@PostMapping(value = "/register")
	public ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> registrarUsuario(
			@Valid @RequestBody UsuarioDTO userDTO, BindingResult result) {

		if (result.hasErrors()) {
			RespuestaServicioDTO<UsuarioDTO> respuesta = new RespuestaServicioDTO<>();
			respuesta.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR,
					messages.getKey(SeverityEnum.ERROR.getKey()),
					messages.getKey(KEY_ERROR_PETICION_GENERICO)));
			respuesta.setOk(false);
			return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
		}

		return authServiceTask.registrarUsuario(userDTO);
	}

	@Operation(
			summary = "Recuperar contraseña",
			description = "Genera un token de recuperación de contraseña y lo envía al correo electrónico del usuario.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Token de recuperación enviado exitosamente"),
			@ApiResponse(responseCode = "400", description = "Correo electrónico vacío o inválido"),
			@ApiResponse(responseCode = "401", description = "Token JWT no válido o expirado"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@PostMapping(value = "/forgot-password")
	public ResponseEntity<RespuestaServicioDTO<String>> forgotPassword(
			@Parameter(description = "Correo electrónico del usuario", required = true, example = "usuario@ejemplo.com")
			@RequestParam String email) {

		if (!StringUtils.hasText(email)) {
			RespuestaServicioDTO<String> respuesta = new RespuestaServicioDTO<>();
			respuesta.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR,
					messages.getKey(SeverityEnum.ERROR.getKey()),
					messages.getKey(KEY_ERROR_PETICION_GENERICO)));
			respuesta.setOk(false);
			return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
		}

		return authServiceTask.forgotPassword(email);
	}

	@Operation(
			summary = "Restablecer contraseña",
			description = "Restablece la contraseña del usuario validando el token de recuperación.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Contraseña restablecida exitosamente"),
			@ApiResponse(responseCode = "400", description = "Token o nueva contraseña vacíos"),
			@ApiResponse(responseCode = "401", description = "Token JWT no válido o expirado"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@PostMapping(value = "/reset-password")
	public ResponseEntity<RespuestaServicioDTO<String>> resetPassword(
			@Parameter(description = "Token de recuperación de contraseña", required = true, example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890")
			@RequestParam String token,
			@Parameter(description = "Nueva contraseña del usuario", required = true, example = "NuevaContraseña123")
			@RequestParam String newPassword) {

		if (!StringUtils.hasText(token) || !StringUtils.hasText(newPassword)) {
			RespuestaServicioDTO<String> respuesta = new RespuestaServicioDTO<>();
			respuesta.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR,
					messages.getKey(SeverityEnum.ERROR.getKey()),
					messages.getKey(KEY_ERROR_PETICION_GENERICO)));
			respuesta.setOk(false);
			return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
		}

		return authServiceTask.resetPassword(token, newPassword);
	}

	@Operation(
			summary = "Activa la cuenta de un usuario",
			description = "Recibe el token UUID enviado por correo electrónico y activa la cuenta del usuario.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Cuenta activada exitosamente"),
			@ApiResponse(responseCode = "400", description = "Token vacío o solicitud incorrecta"),
			@ApiResponse(responseCode = "422", description = "Token inválido, expirado o ya usado"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@PostMapping(value = "/activate-account")
	public ResponseEntity<RespuestaServicioDTO<String>> activateAccount(
			@Parameter(description = "Token UUID de activación enviado por correo electrónico", required = true, example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890")
			@RequestParam String token) {

		if (!StringUtils.hasText(token)) {
			RespuestaServicioDTO<String> respuesta = new RespuestaServicioDTO<>();
			respuesta.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR,
					messages.getKey(SeverityEnum.ERROR.getKey()),
					messages.getKey(KEY_ERROR_PETICION_GENERICO)));
			respuesta.setOk(false);
			return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
		}

		return authServiceTask.activateAccount(token);
	}

	
	@Operation(
			summary = "Reenviar correo de activación",
			description = "Reenvía el correo de activación a un usuario registrado cuya cuenta aún no ha sido activada.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Correo de activación reenviado exitosamente"),
			@ApiResponse(responseCode = "400", description = "Correo vacío o solicitud incorrecta"),
			@ApiResponse(responseCode = "422", description = "Usuario no registrado o cuenta ya activada"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@PostMapping(value = "/resend-activation-email")
	public ResponseEntity<RespuestaServicioDTO<String>> resendActivationEmail(
			@Parameter(description = "Correo electrónico del usuario registrado", required = true, example = "usuario@ejemplo.com")
			@RequestParam String email) {

		if (!StringUtils.hasText(email)) {
			RespuestaServicioDTO<String> respuesta = new RespuestaServicioDTO<>();
			respuesta.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR,
					messages.getKey(SeverityEnum.ERROR.getKey()),
					messages.getKey(KEY_ERROR_PETICION_GENERICO)));
			respuesta.setOk(false);
			return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
		}

		return authServiceTask.resendActivationEmail(email);
	}

}
