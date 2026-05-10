package edu.co.udes.spafeu.web.app.service.task;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;

public interface UsuarioServiceTask {

	ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> registarUsuario(UsuarioDTO userDTO);

	ResponseEntity<RespuestaServicioDTO<String>> forgotPassword(String email);

	ResponseEntity<RespuestaServicioDTO<Boolean>> existeCorreo(String email);

	ResponseEntity<RespuestaServicioDTO<Boolean>> existeUsername(String username);

	ResponseEntity<RespuestaServicioDTO<String>> resetPassword(String token, String newPassword);

	Resource obtenerImagenPerfil(String nombreRecurso, Long currentUserId);
	
	ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> findUser(Long userId);

	ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> cargarImg(MultipartFile archivo, Long currentUserId);

	
}
