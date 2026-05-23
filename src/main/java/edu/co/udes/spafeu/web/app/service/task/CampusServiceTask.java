package edu.co.udes.spafeu.web.app.service.task;

import java.util.List;

import org.springframework.http.ResponseEntity;

import edu.co.udes.spafeu.web.app.models.dto.CampusDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;

public interface CampusServiceTask {

    ResponseEntity<RespuestaServicioDTO<List<CampusDTO>>> findAllCampusesWithPrograms();
}
