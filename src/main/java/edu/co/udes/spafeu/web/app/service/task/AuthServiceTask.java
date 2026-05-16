package edu.co.udes.spafeu.web.app.service.task;

import org.springframework.http.ResponseEntity;

import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Interfaz ServiceTask para operaciones de autenticación y gestión de credenciales.</p>
 * 
 * @author AGENT-DATA-EXPERT-SPAFEU
 */
public interface AuthServiceTask {

	/**
	 * <p>Registra un nuevo usuario en el sistema.</p>
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

}
