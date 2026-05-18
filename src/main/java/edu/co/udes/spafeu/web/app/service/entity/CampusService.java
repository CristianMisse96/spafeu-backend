package edu.co.udes.spafeu.web.app.service.entity;

import java.util.List;
import java.util.Set;
import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.CampusDTO;

public interface CampusService {

    List<CampusDTO> findAllCampusesWithPrograms() throws SpafeuException;
}
