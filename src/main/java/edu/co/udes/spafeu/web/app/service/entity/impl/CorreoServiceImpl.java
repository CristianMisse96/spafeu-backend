package edu.co.udes.spafeu.web.app.service.entity.impl;

import edu.co.udes.spafeu.web.app.models.dto.MailDTO;
import edu.co.udes.spafeu.web.app.models.enums.TemplateMailEnum;
import edu.co.udes.spafeu.web.app.service.entity.CorreoService;
import edu.co.udes.spafeu.web.app.service.entity.EmailService;
import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Implementación Service con lógica de negocio para la generación y envío de correos.</p>
 *
 * @author Sistema SPAFEU
 */
@Service
@RequiredArgsConstructor
public class CorreoServiceImpl implements CorreoService {

    private final EmailService emailService;

    /**
     * <p>
     * Titulo: Proyecto SPAFEU
     * </p>
     * <p>
     * Descripción: Método auxiliar para generar y enviar el correo electrónico de recuperación
     * de contraseña utilizando una plantilla HTML y los parámetros proporcionados.
     * </p>
     *
     * @param parametrosCorreoModelo Mapa de variables para la plantilla del correo
     * @param usuarioCorreo Dirección de correo del destinatario
     * @param template Tipo de plantilla a utilizar para el correo
     */
    @Override
    public void generarCorreo(Map<String, Object> parametrosCorreoModelo, String usuarioCorreo,
            TemplateMailEnum template, String asunto) {

        String[] copia = null;

        MailDTO mail = new MailDTO(usuarioCorreo.trim().split(","),
                copia,
                asunto,
                null,
                null,
                parametrosCorreoModelo,
                null,
                null);

        emailService.sendMail(mail, template);
    }
}
