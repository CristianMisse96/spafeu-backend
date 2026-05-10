package edu.co.udes.spafeu.web.app.service.entity;


import edu.co.udes.spafeu.web.app.models.dto.MailDTO;
import edu.co.udes.spafeu.web.app.models.enums.TemplateMailEnum;

/**
 * <p>
 * Titulo: Proyecto JDS
 * </p>
 * <p>
 * Descripción: interface para la definición de Métodos que controlan el emvio de correos
 * </p>
 *
 * @author Cristian Misse
 *
 **/
public interface EmailService {
	
	/**
	 * Definción del Método de envio de correos electronicos.
	 * @param requestDTO httprequest con la data del ususrio
	 * @param mail objeto que contiene la información del correo a enviar,.
	 * @param template plantilla usara para enviar el correo
	 * @throws Exception 
	 */
	void sendMail(MailDTO mail, TemplateMailEnum template);

}
