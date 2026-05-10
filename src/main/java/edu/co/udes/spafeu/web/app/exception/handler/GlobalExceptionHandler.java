package edu.co.udes.spafeu.web.app.exception.handler;

import static edu.co.udes.spafeu.web.app.util.ConstantesUtil.KEY_ERROR_LABEL;
import static edu.co.udes.spafeu.web.app.util.ConstantesUtil.KEY_ERROR_GENERICO;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.util.Messages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
	
	private final Messages messages;
	
	@ExceptionHandler(SpafeuException.class)
	public ResponseEntity<RespuestaServicioDTO<Object>> handleSoft(SpafeuException ex) {

		RespuestaServicioDTO<Object> dto = new RespuestaServicioDTO<>();
		dto.setMensajeDTO(
				new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(ex.getMessage())));

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<RespuestaServicioDTO<Object>> handleGeneric(Exception ex) {
		
		String uuid = UUID.randomUUID().toString();
		log.error("ERROR INESPERADO" + ex.getMessage());
		
		RespuestaServicioDTO<Object> dto = new RespuestaServicioDTO<>();
		dto.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL),
				messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
	}
}
