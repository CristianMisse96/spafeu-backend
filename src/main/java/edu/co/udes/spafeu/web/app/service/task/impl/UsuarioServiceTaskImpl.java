package edu.co.udes.spafeu.web.app.service.task.impl;

import static edu.co.udes.spafeu.web.app.util.ConstantesUtil.KEY_CORREO_EXITOSO;
import static edu.co.udes.spafeu.web.app.util.ConstantesUtil.KEY_ERROR_LABEL;
import static edu.co.udes.spafeu.web.app.util.ConstantesUtil.KEY_ERROR_PETICION_GENERICO;
import static edu.co.udes.spafeu.web.app.util.ConstantesUtil.KEY_SUCCESS;
import static edu.co.udes.spafeu.web.app.util.ConstantesUtil.KEY_UPDATE_PASSWORD_EXITOSO;
import static edu.co.udes.spafeu.web.app.util.ConstantesUtil.SUCCESS_DETALLE;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.entity.UserService;
import edu.co.udes.spafeu.web.app.service.task.UsuarioServiceTask;
import edu.co.udes.spafeu.web.app.util.Messages;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class UsuarioServiceTaskImpl implements UsuarioServiceTask {
	
	private static final String ERROR_INESPERADO_USUARIO = "error inesperado usuario -> ";
	static final String KEY_SUCCESS_REG_EXITOSO= "usuario.registro.exitoso";
	
	private final Messages messages;
	private final UserService userService;
	
	@Override
	public ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> registarUsuario(UsuarioDTO userDTO) {
		
		RespuestaServicioDTO<UsuarioDTO> respuestaServicioUserDTO = new RespuestaServicioDTO<>();

		userDTO.setEnabled(Boolean.FALSE);
		UsuarioDTO respuesta = userService.registrarUsuario(userDTO);

		respuestaServicioUserDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS),
				messages.getKey(KEY_SUCCESS_REG_EXITOSO).replace("{1}", userDTO.getUsername())));
		respuestaServicioUserDTO.setOk(true);
		respuestaServicioUserDTO.setNegocio(respuesta);

		return new ResponseEntity<>(respuestaServicioUserDTO, HttpStatus.CREATED);
	}


	@Override
	public ResponseEntity<RespuestaServicioDTO<String>> forgotPassword(String email) {
		
		RespuestaServicioDTO<String> respuestaServicioUserDTO = new RespuestaServicioDTO<>();

		userService.forgotPassword(email);

		respuestaServicioUserDTO.setMensajeDTO(
				new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
		respuestaServicioUserDTO.setOk(true);
		respuestaServicioUserDTO.setNegocio(KEY_CORREO_EXITOSO);

		return new ResponseEntity<>(respuestaServicioUserDTO, HttpStatus.OK);
	}


	@Override
	public ResponseEntity<RespuestaServicioDTO<Boolean>> existeCorreo(String email) {
		
		RespuestaServicioDTO<Boolean> respuestaServicioDTO = new RespuestaServicioDTO<>();
		
			Boolean validacionCorreo = userService.validarCorreo(email);
			respuestaServicioDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
			respuestaServicioDTO.setOk(validacionCorreo);
			respuestaServicioDTO.setNegocio(validacionCorreo);
		
		return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<RespuestaServicioDTO<Boolean>> existeUsername(String username) {
		RespuestaServicioDTO<Boolean> respuestaServicioDTO = new RespuestaServicioDTO<>();
		
			Boolean validacionUsername = userService.validarUsername(username);
			respuestaServicioDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
			respuestaServicioDTO.setOk(validacionUsername);
			respuestaServicioDTO.setNegocio(validacionUsername);
		
		return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.OK);
	}


	@Override
	public ResponseEntity<RespuestaServicioDTO<String>> resetPassword(String token, String newPassword) {
		RespuestaServicioDTO<String> respuestaServicioUserDTO = new RespuestaServicioDTO<>();
			
			userService.resetPassword(token, newPassword);
			
			respuestaServicioUserDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
			respuestaServicioUserDTO.setOk(true);
			respuestaServicioUserDTO.setNegocio(KEY_UPDATE_PASSWORD_EXITOSO); 
		
		return new ResponseEntity<>(respuestaServicioUserDTO, HttpStatus.OK);
	}

	@Override
	public Resource obtenerImagenPerfil(String nombreRecurso, Long currentUserId) {
		
		Resource recurso=null;
		
		try {
			recurso= userService.obtenerImagenPerfil(nombreRecurso, currentUserId);
			
			 if (!recurso.exists() && !recurso.isReadable()) {
				 Path rutaArchivo = Paths.get("uploads/usuarios","no-usuario.png").toAbsolutePath();

		         recurso = new UrlResource(rutaArchivo.toUri()); 
		    }
		} catch (Exception e) {
			log.error(ERROR_INESPERADO_USUARIO + e.getMessage());
			log.error(e);
		}
		
		return recurso;
	}

	@Override
	public ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> findUser(Long userId) {
		
		RespuestaServicioDTO<UsuarioDTO> respuestaServicioUserDTO = new RespuestaServicioDTO<>();

		UsuarioDTO respuesta= userService.buscarUsuario(userId);
		respuestaServicioUserDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
		respuestaServicioUserDTO.setOk(true);
		respuestaServicioUserDTO.setNegocio(respuesta);


		return new ResponseEntity<>(respuestaServicioUserDTO, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> cargarImg(MultipartFile archivo, Long currentUserId) {
		
		RespuestaServicioDTO<UsuarioDTO> respuestaServicioDTO = new RespuestaServicioDTO<>();
		
		UsuarioDTO updatedUser = null;
		
		try {
			updatedUser = userService.updateImageProfile(archivo, currentUserId);
		}catch (Exception e) {
			log.error(ERROR_INESPERADO_USUARIO + e.getMessage());
			log.error(e);
			respuestaServicioDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_PETICION_GENERICO)));
			return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		respuestaServicioDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), "Perfil actualizado correctamente"));
		respuestaServicioDTO.setOk(true);
		respuestaServicioDTO.setNegocio(updatedUser);
		
		return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.OK);
	}
}
