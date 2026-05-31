package edu.co.udes.spafeu.web.app.util;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SpringSecurityConstantes {
	
	public static final String HEADER_AUTHORIZATION = "Authorization";
	public static final String PREFIX_TOKEN = "Bearer ";
	public static final long EXPIRATION_DATE=14400000L;//1800000L
	public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
	public static final String CONTENT_TYPE = "application/json";
	public static final String MESSAGE = "message";
	public static final String ROLE_ADMIN = "ADMIN";
	public static final String ROLE_USER = "USER";
	public static final String ROLE_VALIDADOR = "VALIDADOR";
}
