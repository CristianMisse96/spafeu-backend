package edu.co.udes.spafeu.web.app.service.entity.impl;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import edu.co.udes.spafeu.web.app.models.dto.MailDTO;
import edu.co.udes.spafeu.web.app.models.enums.TemplateMailEnum;
import edu.co.udes.spafeu.web.app.service.entity.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;


/**
 * <p>
 * Titulo: Proyecto JDS
 * </p>
 * <p>
 * Descripción: Implementación de la interfaz del servicio EmailService para gestión de correos electrónicos
 * </p>
 *
 * @author Cristian Misse
 **/
@Service
@Log4j2
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    /**
     * objeto de spring usado para la gestión y manejo de correos electronicos.
     */
    
    private final JavaMailSender emailSender;

	/**
	 * plantilla
	 */
	
	private final TemplateEngine templateEngine;
	
	/**
	 * origen de correo a enviar
	 */
	@Value("${spafeu.mail.from}")
	private String from;
	
	/**
	 * servidor a usar para el envio de correos
	 */
	/*@Value("${sec.server.name}")
	private String serverName;*/
	
	/**
	 * Metodo de envio de correos electronicos.
	 * @param requestDTO httprequest con la data del ususrio
	 * @param mail objeto que contiene la información del correo a enviar,.
	 * @param template plantilla usara para enviar el correo
	 */
	@Async
	@Override
	public void sendMail(MailDTO mail, TemplateMailEnum template){
		//mail.getModel().put("servidor", serverName);
		String contentMail = buildGenericMessage(mail, template); 
		try {
			MimeMessage mimeMessage = emailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, 
					MimeMessageHelper.MULTIPART_MODE_MIXED, 
					StandardCharsets.UTF_8.name());
			
			messageHelper.setFrom(from);
			messageHelper.setTo(mail.getPara());
			messageHelper.setSubject(mail.getAsunto());
			
			if(!ObjectUtils.isEmpty(mail.getCopia())) {
				messageHelper.setCc(mail.getCopia());
			}
			
			if(!ObjectUtils.isEmpty(mail.getArchivoAdjuntoBytes())) {
				ByteArrayDataSource adjunto = new ByteArrayDataSource(mail.getArchivoAdjuntoBytes(), mail.getMimeType()); 
				messageHelper.addAttachment(mail.getNombreArchivoAdjunto(), adjunto);
			}
			
			if(mail.existeAdjunto()) {
				
				messageHelper.addAttachment(mail.getNombreArchivoAdjunto(), mail.getArchivoAdjunto());
			}
		
			messageHelper.setText(contentMail, true);
			emailSender.send(mimeMessage);
			log.info("Mensaje Enviado Correctamente");
		} catch (MessagingException e) {
			log.error( "Error al enviar el correo ",e.getCause().getMessage());
		}catch (Exception e) {
			log.error("Error genérico al enviar el correo ", e.getCause().getMessage());
		}
	}

    /**
     * Método para generar un mensaje generico
     *
     * @param mail     correo a enviar.
     * @param template plantilla usada para ernvia de correo
     * @return cadena con el correo a enviar
     */
    @Synchronized
    private String buildGenericMessage(MailDTO mail, TemplateMailEnum template) {
        Context context = new Context();
        context.setVariables(mail.getModel());
        return templateEngine.process("email/" + template.getName(), context);
    }

}
