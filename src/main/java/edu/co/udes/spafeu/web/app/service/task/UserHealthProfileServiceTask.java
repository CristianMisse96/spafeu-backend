package edu.co.udes.spafeu.web.app.service.task;

import org.springframework.http.ResponseEntity;

import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.UserHealthProfileDTO;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;

public interface UserHealthProfileServiceTask {

    ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> registerHealthProfile(UserHealthProfileDTO dto, Long userId);
}
