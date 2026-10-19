package edu.co.udes.spafeu.web.app.service.task;

import org.springframework.http.ResponseEntity;

import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Interfaz ServiceTask para operaciones de autenticación, registro,
 * activación de cuenta, reenvío de correo de activación y gestión de credenciales.</p>
 * 
 * @author AGENT-DATA-EXPERT-SPAFEU
 */
public interface AuthServiceTask {

	/**
	 * <p>Registra un nuevo usuario en el sistema. La cuenta se crea inactiva y se envía
	 * un correo electrónico con un token de activación.</p>
	 * 
	 * @param userDTO datos del usuario a registrar
	 * @return ResponseEntity con RespuestaServicioDTO conteniendo el UsuarioDTO registrado
	 */
	ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> registrarUsuario(UsuarioDTO userDTO);

	/**
	 * <p>Inicia el proceso de recuperación de contraseña enviando un token al correo electrónico.</p>
	 * 
	 * @param email correo electrónico del usuario
	 * @return ResponseEntity con RespuestaServicioDTO conteniendo el token de recuperación
	 */
	ResponseEntity<RespuestaServicioDTO<String>> forgotPassword(String email);

	/**
	 * <p>Restablece la contraseña del usuario usando un token válido.</p>
	 * 
	 * @param token token de recuperación de contraseña
	 * @param newPassword nueva contraseña a establecer
	 * @return ResponseEntity con RespuestaServicioDTO conteniendo un mensaje de confirmación
	 */
	ResponseEntity<RespuestaServicioDTO<String>> resetPassword(String token, String newPassword);

	/**
	 * <p>Activa la cuenta de usuario usando el token de activación enviado al correo electrónico.</p>
	 * 
	 * @param token token de activación de cuenta
	 * @return ResponseEntity con RespuestaServicioDTO conteniendo un mensaje de confirmación
	 */
	ResponseEntity<RespuestaServicioDTO<String>> activateAccount(String token);

	/**
	 * <p>
	 * Titulo: Proyecto SPAFEU
	 * </p>
	 * <p>
	 * Descripción: Reenvía el correo de activación de cuenta a un usuario registrado cuya cuenta
	 * aún no ha sido activada. Delega en el servicio de autenticación para generar un nuevo token
	 * de activación, invalidar los tokens previos y enviar el correo con el enlace de activación.
	 * </p>
	 *
	 * @param email Correo electrónico del usuario registrado
	 * @return ResponseEntity con RespuestaServicioDTO conteniendo un mensaje de confirmación
	 * @throws SpafeuException Si el usuario no está registrado o la cuenta ya fue activada
	 */
	ResponseEntity<RespuestaServicioDTO<String>> resendActivationEmail(String email);

}
