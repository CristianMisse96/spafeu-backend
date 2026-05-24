package edu.co.udes.spafeu.web.app.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ClasificacionConstantesUtil {
	
	//PREGUNTA
	// ============================================================
	// CUESTIONARIO 2: HISTORIAL DE ACTIVIDAD FÍSICA (ID 2)
	// ============================================================

	// ------------------------------------------------------------
	// PREGUNTA 10 — Días de actividad física moderada o vigorosa
	// ------------------------------------------------------------
	public static final Long ID_QUESTION_DIAS_ACTIVIDAD_FISICA = 10L;

	// OPCIONES PREGUNTA 10
	public static final Long ID_OPTION_DIAS_CERO = 3L;
	public static final Long ID_OPTION_DIAS_UNO_A_DOS = 4L;
	public static final Long ID_OPTION_DIAS_TRES_A_CUATRO = 5L;
	public static final Long ID_OPTION_DIAS_CINCO_MAS = 6L;

	// ------------------------------------------------------------
	// PREGUNTA 11 — Duración de la sesión de actividad física
	// ------------------------------------------------------------
	public static final Long ID_QUESTION_DURACION_ACTIVIDAD_FISICA = 11L;

	// OPCIONES PREGUNTA 11
	public static final Long ID_OPTION_DURACION_MENOS_VEINTE_MINUTOS = 7L;
	public static final Long ID_OPTION_DURACION_VEINTE_A_TREINTA_MINUTOS = 8L;
	public static final Long ID_OPTION_DURACION_TREINTA_A_SESENTA_MINUTOS = 9L;
	public static final Long ID_OPTION_DURACION_MAS_SESENTA_MINUTOS = 10L;

	// ------------------------------------------------------------
	// PREGUNTA 12 — Tipo de actividad realizada con mayor frecuencia
	// ------------------------------------------------------------
	public static final Long ID_QUESTION_TIPO_ACTIVIDAD = 12L;

	// OPCIONES PREGUNTA 12
	public static final Long ID_OPTION_TIPO_CAMINATA_ACTIVIDAD_LIGERA = 11L;
	public static final Long ID_OPTION_TIPO_ACTIVIDAD_MODERADA = 12L;
	public static final Long ID_OPTION_TIPO_ACTIVIDAD_VIGOROSA = 13L;

	// ------------------------------------------------------------
	// PREGUNTA 13 — Tiempo de comportamiento sedentario
	// ------------------------------------------------------------
	public static final Long ID_QUESTION_COMPORTAMIENTO_SEDENTARIO = 13L;

	// OPCIONES PREGUNTA 13
	public static final Long ID_OPTION_SEDENTARIO_MENOS_CUATRO_HORAS = 14L;
	public static final Long ID_OPTION_SEDENTARIO_CUATRO_A_SEIS_HORAS = 15L;
	public static final Long ID_OPTION_SEDENTARIO_SEIS_A_OCHO_HORAS = 16L;
	public static final Long ID_OPTION_SEDENTARIO_MAS_OCHO_HORAS = 17L;

	// ------------------------------------------------------------
	// PREGUNTA 14 — Nivel de condición física autopercibido
	// ------------------------------------------------------------
	public static final Long ID_QUESTION_NIVEL_CONDICION_FISICA = 14L;

	// OPCIONES PREGUNTA 14
	public static final Long ID_OPTION_CONDICION_MUY_BAJA = 18L;
	public static final Long ID_OPTION_CONDICION_BAJA = 19L;
	public static final Long ID_OPTION_CONDICION_MODERADA = 20L;
	public static final Long ID_OPTION_CONDICION_BUENA = 21L;
	public static final Long ID_OPTION_CONDICION_MUY_BUENA = 22L;

}
