package edu.co.udes.spafeu.web.app.service.entity;

import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.UserHealthProfileDTO;

public interface UserHealthProfileService {

    UserHealthProfileDTO registerHealthProfile(UserHealthProfileDTO dto, Long userId) throws SpafeuException;
}
