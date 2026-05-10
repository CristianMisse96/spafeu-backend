package edu.co.udes.spafeu.web.app.util;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * <p>
 * Titulo: Proyecto SPAFEU
 * </p>
 * <p>
 * Descripción:clase para lectura de archivo messages.properties
 * </p>
 *
 * @author Cristian Misse 
 *
 **/
@Component
public class Messages {
	
	private MessageSource messageSource;
	
	public Messages(MessageSource messageSource) {
		this.messageSource=messageSource;
	}
	
	public String getKey(String key) {
		String value = null;
		if(ObjectUtils.isEmpty(key))
			throw new NullPointerException("No se encontro registrado en el messages.properties el key=" + key);
		try {
			value = messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
		}catch (NoSuchMessageException e) {
			value = "por favor registrar el mensaje en messages.properties "+key;
		}
		return value;
	}
}
