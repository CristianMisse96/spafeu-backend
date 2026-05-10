package edu.co.udes.spafeu.web.app.controllers;

import static edu.co.udes.spafeu.web.app.util.ConstantesUtil.KEY_ERROR_PETICION_GENERICO;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.task.UsuarioServiceTask;
import edu.co.udes.spafeu.web.app.util.Messages;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * Titulo: Proyecto SPAFEU
 * </p>
 * <p>
 * Descripción: controlador REST para realizar procesos de gestión de usuarios.
 * </p>
 *
 * @author Cristian Misse - cgmisse@gmail.com
 *
 **/

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsuarioRestController {
	
	private final Messages messages;
	private final UsuarioServiceTask userServiceTask;

	@PostMapping(value="/register")
	public ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> newUserAdmin(
			@Valid @RequestBody UsuarioDTO userDTO,
			BindingResult result) {

		if (result.hasErrors()) {
			RespuestaServicioDTO<UsuarioDTO> respuestaServicioDTO = new RespuestaServicioDTO<>();
			respuestaServicioDTO.setMensajeDTO(
					new MensajeDTO(SeverityEnum.ERROR,
							messages.getKey(SeverityEnum.ERROR.getKey()),messages.getKey(KEY_ERROR_PETICION_GENERICO)));
			respuestaServicioDTO.setOk(false);
			return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.BAD_REQUEST);
		}

		return userServiceTask.registarUsuario(userDTO);
	}
	
	@PostMapping(value = "/forgot-password")
	public ResponseEntity<RespuestaServicioDTO<String>> forgotPassword(@RequestParam String email){
		if (!StringUtils.hasText(email)) {
			RespuestaServicioDTO<String> respuestaServicioDTO = new RespuestaServicioDTO<>();
			
			respuestaServicioDTO.setMensajeDTO(
					new MensajeDTO(SeverityEnum.ERROR, 
							messages.getKey(SeverityEnum.ERROR.getKey()),messages.getKey(KEY_ERROR_PETICION_GENERICO)));
			
			respuestaServicioDTO.setOk(false);
			
			return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.BAD_REQUEST);
		}
		
		return userServiceTask.forgotPassword(email);
	}
	
	@GetMapping(value = "/isemail/{email}")
	public ResponseEntity<RespuestaServicioDTO<Boolean>> existeCorreo(@PathVariable String email){
		
		if (!StringUtils.hasText(email)) {
			RespuestaServicioDTO<Boolean> respuestaServicioDTO = new RespuestaServicioDTO<>();
			
			respuestaServicioDTO.setMensajeDTO(
					new MensajeDTO(SeverityEnum.ERROR, 
							messages.getKey(SeverityEnum.ERROR.getKey()),messages.getKey(KEY_ERROR_PETICION_GENERICO)));
			
			respuestaServicioDTO.setOk(false);
			respuestaServicioDTO.setNegocio(false);
			return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.BAD_REQUEST);
		}
		return userServiceTask.existeCorreo(email);
	}
	
	@GetMapping(value = "/isusername/{username}")
	public ResponseEntity<RespuestaServicioDTO<Boolean>> existeUsername(@PathVariable String username){
		
		if (!StringUtils.hasText(username)) {
			RespuestaServicioDTO<Boolean> respuestaServicioDTO = new RespuestaServicioDTO<>();
			
			respuestaServicioDTO.setMensajeDTO(
					new MensajeDTO(SeverityEnum.ERROR, 
							messages.getKey(SeverityEnum.ERROR.getKey()),messages.getKey(KEY_ERROR_PETICION_GENERICO)));
			
			respuestaServicioDTO.setOk(false);
			respuestaServicioDTO.setNegocio(false);
			return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.BAD_REQUEST);
		}
		return userServiceTask.existeUsername(username);
	}
	
	 @PostMapping("/reset-password")
	    public ResponseEntity<RespuestaServicioDTO<String>> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
		 
		 if (!StringUtils.hasText(token) || !StringUtils.hasText(newPassword) ) {
				RespuestaServicioDTO<String> respuestaServicioDTO = new RespuestaServicioDTO<>();
				
				respuestaServicioDTO.setMensajeDTO(
						new MensajeDTO(SeverityEnum.ERROR, 
								messages.getKey(SeverityEnum.ERROR.getKey()),messages.getKey(KEY_ERROR_PETICION_GENERICO)));
				
				return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.BAD_REQUEST);
		}
	       
		 return userServiceTask.resetPassword(token,newPassword);
	  }

	
	@GetMapping("/recurso/{currentUserId}/{nombreRecurso:.+}")
	public ResponseEntity<Resource> obtenerImagenPerfil(@PathVariable String nombreRecurso,@PathVariable Long currentUserId){
		
		
		Resource recurso = userServiceTask.obtenerImagenPerfil(nombreRecurso,currentUserId);
		
		HttpHeaders cabecera= new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + recurso.getFilename() + "\"");
		return new ResponseEntity<>(recurso,cabecera, HttpStatus.OK);
	}
	
	@PostMapping("/upload")
	public ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> uploadImg(@RequestParam("archivo") MultipartFile archivo, 
			@RequestParam("id") Long id){
		
		if (archivo.isEmpty() || ObjectUtils.isEmpty(id)) {
			RespuestaServicioDTO<UsuarioDTO> respuestaServicioDTO = new RespuestaServicioDTO<>();
			
			respuestaServicioDTO.setMensajeDTO(
					new MensajeDTO(SeverityEnum.ERROR, 
							messages.getKey(SeverityEnum.ERROR.getKey()),messages.getKey(KEY_ERROR_PETICION_GENERICO)));
			
			respuestaServicioDTO.setOk(false);
			return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.BAD_REQUEST);
		}
		
		return userServiceTask.cargarImg(archivo,id);
	}
	

}
