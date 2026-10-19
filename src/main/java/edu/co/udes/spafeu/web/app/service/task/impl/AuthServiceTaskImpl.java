package edu.co.udes.spafeu.web.app.service.task.impl;

import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.KEY_SUCCESS;
import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.SUCCESS_DETALLE;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.entity.AuthService;
import edu.co.udes.spafeu.web.app.service.task.AuthServiceTask;
import edu.co.udes.spafeu.web.app.util.Messages;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Implementación ServiceTask para operaciones de autenticación, registro,
 * activación de cuenta, reenvío de correo de activación y gestión de credenciales.</p>
 * 
 * @author AGENT-DATA-EXPERT-SPAFEU
 */
@Service
@RequiredArgsConstructor
public class AuthServiceTaskImpl implements AuthServiceTask {

	// Keys de mensajes específicos de autenticación
	static final String KEY_SUCCESS_REGISTRO_EXITOSO = "auth.registro.exitoso";
	static final String KEY_SUCCESS_ACTIVACION_CUENTA = "auth.activacion.cuenta.exitoso";
	static final String KEY_SUCCESS_PASSWORD_RESET = "auth.password.reset.exitoso";
	static final String KEY_SUCCESS_REENVIO_ACTIVACION = "auth.activacion.correo.reenviado";

	private final Messages messages;
	private final AuthService authService;

	/**
	 * <p>Registra un nuevo usuario en el sistema. La cuenta se crea inactiva y se envía
	 * un correo electrónico con un token de activación.</p>
	 *
	 * @param userDTO datos del usuario a registrar
	 * @return ResponseEntity con RespuestaServicioDTO conteniendo el UsuarioDTO registrado
	 */
	@Override
	public ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> registrarUsuario(UsuarioDTO userDTO) {
		RespuestaServicioDTO<UsuarioDTO> respuestaServicioDTO = new RespuestaServicioDTO<>();

		UsuarioDTO respuesta = authService.registrarUsuario(userDTO);

		respuestaServicioDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS,
				messages.getKey(KEY_SUCCESS),
				messages.getKey(KEY_SUCCESS_REGISTRO_EXITOSO).replace("{1}", userDTO.getUsername())));
		respuestaServicioDTO.setOk(true);
		respuestaServicioDTO.setNegocio(respuesta);

		return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.CREATED);
	}

	/**
	 * <p>Inicia el proceso de recuperación de contraseña enviando un token al correo electrónico.</p>
	 *
	 * @param email correo electrónico del usuario
	 * @return ResponseEntity con RespuestaServicioDTO conteniendo la confirmación del proceso
	 */
	@Override
	public ResponseEntity<RespuestaServicioDTO<String>> forgotPassword(String email) {
		RespuestaServicioDTO<String> respuestaServicioDTO = new RespuestaServicioDTO<>();

		authService.forgotPassword(email);

		respuestaServicioDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS,
				messages.getKey(KEY_SUCCESS),
				messages.getKey(SUCCESS_DETALLE)));
		respuestaServicioDTO.setOk(true);
		respuestaServicioDTO.setNegocio("OK");

		return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.OK);
	}

	/**
	 * <p>Restablece la contraseña del usuario usando un token válido.</p>
	 *
	 * @param token token de recuperación de contraseña
	 * @param newPassword nueva contraseña a establecer
	 * @return ResponseEntity con RespuestaServicioDTO conteniendo un mensaje de confirmación
	 */
	@Override
	public ResponseEntity<RespuestaServicioDTO<String>> resetPassword(String token, String newPassword) {
		RespuestaServicioDTO<String> respuestaServicioDTO = new RespuestaServicioDTO<>();

		authService.resetPassword(token, newPassword);

		respuestaServicioDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS,
				messages.getKey(KEY_SUCCESS),
				messages.getKey(KEY_SUCCESS_PASSWORD_RESET)));
		respuestaServicioDTO.setOk(true);
		respuestaServicioDTO.setNegocio(KEY_SUCCESS_PASSWORD_RESET);

		return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.OK);
	}

	/**
	 * <p>Activa la cuenta de usuario usando el token de activación enviado al correo electrónico.</p>
	 *
	 * @param token token de activación de cuenta enviado por correo electrónico
	 * @return ResponseEntity con RespuestaServicioDTO conteniendo un mensaje de confirmación
	 */
	@Override
	public ResponseEntity<RespuestaServicioDTO<String>> activateAccount(String token) {
		RespuestaServicioDTO<String> respuestaServicioDTO = new RespuestaServicioDTO<>();

		authService.activateAccount(token);

		respuestaServicioDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS,
				messages.getKey(KEY_SUCCESS),
				messages.getKey(KEY_SUCCESS_ACTIVACION_CUENTA)));
		respuestaServicioDTO.setOk(true);
		respuestaServicioDTO.setNegocio(KEY_SUCCESS_ACTIVACION_CUENTA);

		return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.OK);
	}

	/**
	 * <p>
	 * Titulo: Proyecto SPAFEU
	 * </p>
	 * <p>
	 * Descripción: Reenvía el correo de activación de cuenta a un usuario registrado cuya cuenta
	 * aún no ha sido activada. Construye la respuesta de servicio con el mensaje de confirmación
	 * una vez que el servicio de autenticación genera el nuevo token y envía el correo.
	 * </p>
	 *
	 * @param email Correo electrónico del usuario registrado
	 * @return ResponseEntity con RespuestaServicioDTO conteniendo un mensaje de confirmación
	 * @throws SpafeuException Si el usuario no está registrado o la cuenta ya fue activada
	 */
	@Override
	public ResponseEntity<RespuestaServicioDTO<String>> resendActivationEmail(String email) {
		RespuestaServicioDTO<String> respuestaServicioDTO = new RespuestaServicioDTO<>();

		authService.resendActivationEmail(email);

		respuestaServicioDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS,
				messages.getKey(KEY_SUCCESS),
				messages.getKey(KEY_SUCCESS_REENVIO_ACTIVACION)));
		respuestaServicioDTO.setOk(true);
		respuestaServicioDTO.setNegocio(KEY_SUCCESS_REENVIO_ACTIVACION);

		return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.OK);
	}

}
