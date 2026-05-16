package edu.co.udes.spafeu.web.app.service.entity;

import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.enums.TemplateMailEnum;
import java.util.Map;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Interfaz Service para la generación y envío de correos electrónicos.</p>
 *
 * @author Sistema SPAFEU
 */
public interface CorreoService {

    void generarCorreo(Map<String, Object> parametrosCorreoModelo, String usuarioCorreo, TemplateMailEnum template, String asunto) throws SpafeuException;
}
