package edu.co.udes.spafeu.web.app.service.task.impl;

import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.KEY_SUCCESS;
import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.SUCCESS_DETALLE;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.entity.AuthService;
import edu.co.udes.spafeu.web.app.service.task.AuthServiceTask;
import edu.co.udes.spafeu.web.app.util.Messages;
import lombok.RequiredArgsConstructor;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Implementación ServiceTask para operaciones de autenticación y gestión de credenciales.</p>
 * 
 * @author AGENT-DATA-EXPERT-SPAFEU
 */
@Service
@RequiredArgsConstructor
public class AuthServiceTaskImpl implements AuthServiceTask {

	// Keys de mensajes específicos de autenticación
	static final String KEY_SUCCESS_REGISTRO_EXITOSO = "auth.registro.exitoso";
	static final String KEY_SUCCESS_PASSWORD_RESET = "auth.password.reset.exitoso";

	private final Messages messages;
	private final AuthService authService;

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

}
