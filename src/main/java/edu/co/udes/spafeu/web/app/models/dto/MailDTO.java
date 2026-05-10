package edu.co.udes.spafeu.web.app.models.dto;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import org.springframework.util.ObjectUtils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * <p>
 * Titulo: Proyecto PNIS
 * </p>
 * <p>
 * Descripción: Clase para manejo de estructura de correo
 * </p>
 *
 * @author Cristian Misse
 *
 **/
@Getter @Setter
@Builder
public class MailDTO implements Serializable {

	private final String[] para;
	private final String[] copia;
	private final String asunto;
	private final String nombreArchivoAdjunto;
	private final File archivoAdjunto;
	private final transient Map<String, Object> model;
	private byte[] archivoAdjuntoBytes;
	private String mimeType;

	public MailDTO(String[] para, String[] copia, String asunto, String nombreArchivoAdjunto, File archivoAdjunto, Map<String, Object> model) {
		this.para = para;
		this.copia = copia;
		this.asunto = asunto;
		this.nombreArchivoAdjunto = nombreArchivoAdjunto;
		this.archivoAdjunto = archivoAdjunto;
		this.model = model;
	}
	
	public MailDTO(String[] para, String[] copia, String asunto, String nombreArchivoAdjunto, File archivoAdjunto, Map<String, Object> model, 
			byte[] archivoAdjuntoBytes, String mimeType) {
		this.para = para;
		this.copia = copia;
		this.asunto = asunto;
		this.nombreArchivoAdjunto = nombreArchivoAdjunto;
		this.archivoAdjunto = archivoAdjunto;
		this.model = model;
		this.archivoAdjuntoBytes = archivoAdjuntoBytes;
		this.mimeType = mimeType;
	}

	public boolean existeAdjunto() {
		return !ObjectUtils.isEmpty(this.nombreArchivoAdjunto) &&
			   !ObjectUtils.isEmpty(this.archivoAdjunto) &&
			   this.archivoAdjunto.exists();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
