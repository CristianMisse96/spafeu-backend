package edu.co.udes.spafeu.web.app.controllers;

import static edu.co.udes.spafeu.web.app.util.ConstantesUtil.KEY_ERROR_PETICION_GENERICO;

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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

	private final Messages messages;
	private final AuthServiceTask authServiceTask;

	/**
	 * <p>Registra un nuevo usuario en el sistema.</p>
	 *
	 * @param userDTO datos del usuario a registrar
	 * @param result resultado de validación del DTO
	 * @return ResponseEntity con RespuestaServicioDTO conteniendo el UsuarioDTO registrado
	 */
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

	/**
	 * <p>Inicia el proceso de recuperación de contraseña enviando un token al correo electrónico.</p>
	 *
	 * @param email correo electrónico del usuario
	 * @return ResponseEntity con RespuestaServicioDTO conteniendo el token de recuperación
	 */
	@PostMapping(value = "/forgot-password")
	public ResponseEntity<RespuestaServicioDTO<String>> forgotPassword(@RequestParam String email) {

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

	/**
	 * <p>Restablece la contraseña del usuario usando un token válido.</p>
	 *
	 * @param token token de recuperación de contraseña
	 * @param newPassword nueva contraseña a establecer
	 * @return ResponseEntity con RespuestaServicioDTO conteniendo un mensaje de confirmación
	 */
	@PostMapping(value = "/reset-password")
	public ResponseEntity<RespuestaServicioDTO<String>> resetPassword(
			@RequestParam String token, @RequestParam String newPassword) {

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

}
