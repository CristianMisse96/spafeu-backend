package edu.co.udes.spafeu.web.app.service.entity;

import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.UserHealthProfileDTO;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;

public interface UserHealthProfileService {
	
	 /**
     * Registra el perfil de salud del usuario y sus respuestas al cuestionario.
     * <p>
     * Este método:
     * <ul>
     *   <li>Valida que el usuario exista y no tenga ya un perfil de salud</li>
     *   <li>Filtra las respuestas para la clasificación de actividad física</li>
     *   <li>Clasifica el nivel de actividad física usando el utilitario correspondiente</li>
     *   <li>Guarda el perfil de salud con el nivel de actividad física asignado</li>
     *   <li>Guarda todas las respuestas del cuestionario</li>
     * </ul>
     *
     * @param dto     datos del perfil de salud y respuestas del cuestionario
     * @param userId  identificador del usuario
     * @return UsuarioDTO con el estado KYC y nivel de actividad física asignado
     * @throws SpafeuException si el usuario no existe, ya tiene perfil o el campus/program no se encuentra
     */
    UsuarioDTO registerHealthProfile(UserHealthProfileDTO dto, Long userId) throws SpafeuException;
}
