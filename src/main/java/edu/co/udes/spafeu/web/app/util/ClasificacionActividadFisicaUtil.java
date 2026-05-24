package edu.co.udes.spafeu.web.app.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.co.udes.spafeu.web.app.models.dto.UserAnswerDTO;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import lombok.experimental.UtilityClass;

/**
 * Utilidad encargada de clasificar el nivel de actividad física de un usuario
 * a partir de sus respuestas al cuestionario de Historial de actividad física.
 *
 * <p>La clasificación se rige por las siguientes reglas de negocio:
 * <ul>
 *   <li><b>Nivel 1 – Sedentario:</b> 0 días de actividad, o 1–2 días con duración menor a 20 min, o más de 8 h sedentario.</li>
 *   <li><b>Nivel 2 – Actividad leve:</b> 1–2 días, duración 20–30 min, intensidad ligera o moderada.</li>
 *   <li><b>Nivel 3 – Actividad moderada:</b> 3–4 días, duración 30–60 min, intensidad moderada.</li>
 *   <li><b>Nivel 4 – Actividad vigorosa:</b> 5+ días, o actividad vigorosa frecuente, o duración mayor a 60 min.</li>
 * </ul>
 *
 * <p>Si ninguna regla aplica, se devuelve {@link NivelActividadFisicaEnum#NIVEL_1} por defecto.
 */
@UtilityClass
public class ClasificacionActividadFisicaUtil {

    /**
     * Clasifica el nivel de actividad física del usuario según sus respuestas
     * al cuestionario de Historial de actividad física.
     *
     * @param respuestas lista de respuestas del usuario ({@link UserAnswerDTO})
     * @return el {@link NivelActividadFisicaEnum} correspondiente; {@link NivelActividadFisicaEnum#NIVEL_1} por defecto
     */
    public NivelActividadFisicaEnum clasificar(List<UserAnswerDTO> respuestas) {
        if (respuestas == null || respuestas.isEmpty()) {
            return NivelActividadFisicaEnum.NIVEL_1;
        }

        Map<Long, Long> respuestasPorPregunta = respuestas.stream()
                .collect(Collectors.toMap(
                        UserAnswerDTO::getQuestionId,
                        UserAnswerDTO::getOptionId
                ));

        // --- Pregunta 10: días de actividad física ---
        Long idOpcionDias = respuestasPorPregunta.get(ClasificacionConstantesUtil.ID_QUESTION_DIAS_ACTIVIDAD_FISICA);
        boolean ceroDias = idOpcionDias.equals(ClasificacionConstantesUtil.ID_OPTION_DIAS_CERO);
        boolean unoADosDias =  idOpcionDias.equals(ClasificacionConstantesUtil.ID_OPTION_DIAS_UNO_A_DOS);
        boolean tresACuatroDias = idOpcionDias.equals(ClasificacionConstantesUtil.ID_OPTION_DIAS_TRES_A_CUATRO);
        boolean cincoMasDias = idOpcionDias.equals(ClasificacionConstantesUtil.ID_OPTION_DIAS_CINCO_MAS);

        // --- Pregunta 11: duración de la sesión ---
        Long idOpcionDuracion = respuestasPorPregunta.get(ClasificacionConstantesUtil.ID_QUESTION_DURACION_ACTIVIDAD_FISICA);
        boolean duracionMenosVeinte = idOpcionDuracion.equals(ClasificacionConstantesUtil.ID_OPTION_DURACION_MENOS_VEINTE_MINUTOS);
        boolean duracionVeinteATreinta = idOpcionDuracion.equals(ClasificacionConstantesUtil.ID_OPTION_DURACION_VEINTE_A_TREINTA_MINUTOS);
        boolean duracionTreintaASesenta = idOpcionDuracion.equals(ClasificacionConstantesUtil.ID_OPTION_DURACION_TREINTA_A_SESENTA_MINUTOS);
        boolean duracionMasSesenta = idOpcionDuracion.equals(ClasificacionConstantesUtil.ID_OPTION_DURACION_MAS_SESENTA_MINUTOS);

        // --- Pregunta 12: tipo de actividad ---
        Long idOpcionTipo = respuestasPorPregunta.get(ClasificacionConstantesUtil.ID_QUESTION_TIPO_ACTIVIDAD);
        boolean tipoLigera = idOpcionTipo.equals(ClasificacionConstantesUtil.ID_OPTION_TIPO_CAMINATA_ACTIVIDAD_LIGERA);
        boolean tipoModerada = idOpcionTipo.equals(ClasificacionConstantesUtil.ID_OPTION_TIPO_ACTIVIDAD_MODERADA);
        boolean tipoVigorosa = idOpcionTipo.equals(ClasificacionConstantesUtil.ID_OPTION_TIPO_ACTIVIDAD_VIGOROSA);

        // --- Pregunta 13: comportamiento sedentario ---
        Long idOpcionSedentario = respuestasPorPregunta.get(ClasificacionConstantesUtil.ID_QUESTION_COMPORTAMIENTO_SEDENTARIO);
        boolean sedentarioMasOchoHoras = idOpcionSedentario.equals(ClasificacionConstantesUtil.ID_OPTION_SEDENTARIO_MAS_OCHO_HORAS);

        // ================================================================
        // Clasificación por orden de prioridad (mayor a menor)
        // ================================================================

        // NIVEL 1 – Sedentario
        // Condiciones (OR): 0 días | 1–2 días con duración < 20 min | > 8 h sedentario
        if ( (ceroDias || unoADosDias) && (duracionMenosVeinte || sedentarioMasOchoHoras)) {
            return NivelActividadFisicaEnum.NIVEL_1;
            
        }// NIVEL 2 – Actividad leve
        // Condiciones (AND): 1–2 días | duración 20–30 min | intensidad ligera o moderada
        else if (unoADosDias && duracionVeinteATreinta && (tipoLigera || tipoModerada)) {
            return NivelActividadFisicaEnum.NIVEL_2;
        }// NIVEL 3 – Actividad moderada
        // Condiciones (AND): 3–4 días | duración 30–60 min | intensidad moderada
        else if (tresACuatroDias && duracionTreintaASesenta && tipoModerada) {
            return NivelActividadFisicaEnum.NIVEL_3;
        }// NIVEL 4 – Actividad vigorosa
        // Condiciones (OR): 5+ días | actividad vigorosa | duración > 60 min
        else if (cincoMasDias || tipoVigorosa || duracionMasSesenta) {
            return NivelActividadFisicaEnum.NIVEL_4;
        }// Default: Nivel 1 – Sedentario
        else {
            return NivelActividadFisicaEnum.NIVEL_1;
        }
    }
}
