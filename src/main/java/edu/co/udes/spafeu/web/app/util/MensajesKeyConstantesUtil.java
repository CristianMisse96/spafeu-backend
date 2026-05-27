package edu.co.udes.spafeu.web.app.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MensajesKeyConstantesUtil {


	public static final String KEY_ERROR_GENERICO = "error.generico";
	public static final String KEY_ERROR_PETICION_GENERICO = "error.peticion.generico";
	public static final String KEY_SUCCESS = "success";
	public static final String KEY_ERROR_LABEL = "error";
	public static final String KEY_CORREO_EXITOSO = "correo enviado exitosamente";
	public static final String SUCCESS_DETALLE = "success.detalle";
	public static final String KEY_UPDATE_PASSWORD_EXITOSO = "Contraseña actualizada correctamente";

	// ============================================================
	// Keys del proceso de Autenticación (Auth)
	// Recuperación de contraseña (forgot/reset password) y registro de usuario
	// ============================================================
	public static final String ERROR_USUARIO_FORGOT_PASSWORD_NO_EXISTE = "error.usuario.forgot.password.no.existe";
	public static final String ERROR_USUARIO_RESET_PASSWORD_TOKEN_EXPIRADO = "error.usuario.reset.password.token.expirado";
	public static final String ERROR_USUARIO_RESET_PASSWORD_TOKEN_NO_VALIDO = "error.usuario.reset.password.token.no.valido";
	public static final String ERROR_USUARIO_REGISTRAR_EXISTE = "error.usuario.registrar.existe";

	// ============================================================
	// Keys del proceso de Perfil de Salud
	// Registro de perfil de salud y respuestas al cuestionario
	// ============================================================
	public static final String PERFIL_SALUD_REGISTRO_EXITOSO = "perfil.salud.registro.exitoso";
	public static final String ERROR_PERFIL_SALUD_YA_EXISTE = "error.perfil.salud.ya.existe";
	public static final String ERROR_CAMPUS_PROGRAM_NO_ENCONTRADO = "error.campus.program.no.encontrado";

	// ============================================================
	// Keys del módulo de Ejercicios (Exercise)
	// ============================================================
	public static final String ERROR_EXERCISE_NO_ENCONTRADO = "error.exercise.no.encontrado";
	public static final String ERROR_EXERCISE_NIVEL_NO_PERMITIDO = "error.exercise.nivel.no.permitido";
}