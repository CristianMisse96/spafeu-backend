package edu.co.udes.spafeu.web.app.service.task;

import org.springframework.http.ResponseEntity;
import edu.co.udes.spafeu.web.app.models.dto.CampusDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;

import java.util.List;
import java.util.Set;

public interface CampusServiceTask {

    ResponseEntity<RespuestaServicioDTO<List<CampusDTO>>> findAllCampusesWithPrograms();
}
