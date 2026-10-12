package edu.co.udes.spafeu.web.app.util;

import lombok.experimental.UtilityClass;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Constantes de seguridad utilizadas por los filtros JWT,
 * configuración CORS y reglas de acceso. La clave secreta JWT se obtiene de
 * la variable de entorno {@code JWT_SECRET} o de la propiedad del sistema
 * {@code jwt.secret}; en desarrollo puede omitirse y se generará una clave
 * temporal (esto último NO debe ocurrir en producción).</p>
 *
 * @author SPAFEU Backend Team
 */
@UtilityClass
public class SpringSecurityConstantes {

	public static final String HEADER_AUTHORIZATION = "Authorization";
	public static final String PREFIX_TOKEN = "Bearer ";
	public static final long ACCESS_TOKEN_EXPIRATION_DATE = 900000L;
	public static final int REFRESH_TOKEN_EXPIRATION_DAYS = 7;
	public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
	public static final String CONTENT_TYPE = "application/json";
	public static final String MESSAGE = "message";
	public static final String ROLE_ADMIN = "ADMIN";
	public static final String ROLE_USER = "USER";
	public static final String ROLE_VALIDADOR = "VALIDADOR";
	public static final String SAME_SITE_LAX = "Lax";
	
}
