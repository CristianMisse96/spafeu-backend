package edu.co.udes.spafeu.web.app.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import edu.co.udes.spafeu.web.app.models.dto.UserAnswerDTO;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import lombok.extern.log4j.Log4j2;

/**
 * <p>Título: Proyecto SPAFEU</p>
 * <p>Descripción: Tests unitarios para ClasificacionActividadFisicaUtil —
 * clasificación del nivel de actividad física según las respuestas
 * al cuestionario de Historial de actividad física.</p>
 *
 * <p>Reglas de negocio validadas:
 * <ul>
 *   <li>NIVEL_1 – Sedentario: (0 días || 1-2 días) && (&lt;20 min || &gt;8 h sedentario)</li>
 *   <li>NIVEL_2 – Leve: 1-2 días && 20-30 min && (ligera || moderada)</li>
 *   <li>NIVEL_3 – Moderada: 3-4 días && 30-60 min && moderada</li>
 *   <li>NIVEL_4 – Vigorosa: 5+ días || vigorosa || &gt;60 min</li>
 *   <li>Default: NIVEL_1</li>
 * </ul>
 * </p>
 *
 * @author AGENT-QA-EXPERT-SPAFEU
 */
@Log4j2
@DisplayName("Tests de ClasificacionActividadFisicaUtil")
class ClasificacionActividadFisicaUtilTest {

    private List<UserAnswerDTO> buildAnswers(Long opcionDias, Long opcionDuracion,
                                              Long opcionTipo, Long opcionSedentario) {
        List<UserAnswerDTO> respuestas = new ArrayList<>();

        respuestas.add(crearRespuesta(ClasificacionConstantesUtil.ID_QUESTION_DIAS_ACTIVIDAD_FISICA, opcionDias));
        respuestas.add(crearRespuesta(ClasificacionConstantesUtil.ID_QUESTION_DURACION_ACTIVIDAD_FISICA, opcionDuracion));
        respuestas.add(crearRespuesta(ClasificacionConstantesUtil.ID_QUESTION_TIPO_ACTIVIDAD, opcionTipo));
        respuestas.add(crearRespuesta(ClasificacionConstantesUtil.ID_QUESTION_COMPORTAMIENTO_SEDENTARIO, opcionSedentario));

        return respuestas;
    }

    private UserAnswerDTO crearRespuesta(Long questionId, Long optionId) {
        UserAnswerDTO dto = new UserAnswerDTO();
        dto.setQuestionId(questionId);
        dto.setOptionId(optionId);
        return dto;
    }

    private void assertNivel(List<UserAnswerDTO> respuestas, NivelActividadFisicaEnum esperado) {
        NivelActividadFisicaEnum resultado = ClasificacionActividadFisicaUtil.clasificar(respuestas);
        assertThat(resultado)
                .as("Esperado %s para respuestas %s", esperado, respuestas)
                .isEqualTo(esperado);
        log.info("Clasificación correcta: {} -> {}", respuestas, esperado);
    }

    // ========================================================================
    // Casos borde: entrada nula o vacía
    // ========================================================================

    @Test
    @DisplayName("Debe retornar NIVEL_1 cuando la lista es null")
    void debeRetornarNivel1CuandoListaNull() {
        assertNivel(null, NivelActividadFisicaEnum.NIVEL_1);
    }

    @Test
    @DisplayName("Debe retornar NIVEL_1 cuando la lista está vacía")
    void debeRetornarNivel1CuandoListaVacia() {
        assertNivel(List.of(), NivelActividadFisicaEnum.NIVEL_1);
    }

    // ========================================================================
    // NIVEL 1 – Sedentario
    // ========================================================================

    @Nested
    @DisplayName("NIVEL_1 – Sedentario")
    class Nivel1 {

        @Test
        @DisplayName("0 días + duración < 20 min debe ser NIVEL_1")
        void ceroDiasYDuracionMenosVeinte() {
            List<UserAnswerDTO> respuestas = buildAnswers(
                    ClasificacionConstantesUtil.ID_OPTION_DIAS_CERO,
                    ClasificacionConstantesUtil.ID_OPTION_DURACION_MENOS_VEINTE_MINUTOS,
                    ClasificacionConstantesUtil.ID_OPTION_TIPO_CAMINATA_ACTIVIDAD_LIGERA,
                    ClasificacionConstantesUtil.ID_OPTION_SEDENTARIO_MENOS_CUATRO_HORAS
            );
            assertNivel(respuestas, NivelActividadFisicaEnum.NIVEL_1);
        }

        @Test
        @DisplayName("1-2 días + duración < 20 min debe ser NIVEL_1")
        void unoADosDiasYDuracionMenosVeinte() {
            List<UserAnswerDTO> respuestas = buildAnswers(
                    ClasificacionConstantesUtil.ID_OPTION_DIAS_UNO_A_DOS,
                    ClasificacionConstantesUtil.ID_OPTION_DURACION_MENOS_VEINTE_MINUTOS,
                    ClasificacionConstantesUtil.ID_OPTION_TIPO_ACTIVIDAD_MODERADA,
                    ClasificacionConstantesUtil.ID_OPTION_SEDENTARIO_CUATRO_A_SEIS_HORAS
            );
            assertNivel(respuestas, NivelActividadFisicaEnum.NIVEL_1);
        }

        @Test
        @DisplayName("0 días + > 8 h sedentario debe ser NIVEL_1")
        void ceroDiasYSedentarioMasOchoHoras() {
            List<UserAnswerDTO> respuestas = buildAnswers(
                    ClasificacionConstantesUtil.ID_OPTION_DIAS_CERO,
                    ClasificacionConstantesUtil.ID_OPTION_DURACION_MENOS_VEINTE_MINUTOS,
                    ClasificacionConstantesUtil.ID_OPTION_TIPO_CAMINATA_ACTIVIDAD_LIGERA,
                    ClasificacionConstantesUtil.ID_OPTION_SEDENTARIO_MAS_OCHO_HORAS
            );
            assertNivel(respuestas, NivelActividadFisicaEnum.NIVEL_1);
        }

        @Test
        @DisplayName("1-2 días + > 8 h sedentario debe ser NIVEL_1")
        void unoADosDiasYSedentarioMasOchoHoras() {
            List<UserAnswerDTO> respuestas = buildAnswers(
                    ClasificacionConstantesUtil.ID_OPTION_DIAS_UNO_A_DOS,
                    ClasificacionConstantesUtil.ID_OPTION_DURACION_VEINTE_A_TREINTA_MINUTOS,
                    ClasificacionConstantesUtil.ID_OPTION_TIPO_ACTIVIDAD_MODERADA,
                    ClasificacionConstantesUtil.ID_OPTION_SEDENTARIO_MAS_OCHO_HORAS
            );
            assertNivel(respuestas, NivelActividadFisicaEnum.NIVEL_1);
        }
    }

    // ========================================================================
    // NIVEL 2 – Actividad leve
    // ========================================================================

    @Nested
    @DisplayName("NIVEL_2 – Actividad leve")
    class Nivel2 {

        @Test
        @DisplayName("1-2 días + 20-30 min + ligera debe ser NIVEL_2")
        void unoADosDiasVeinteATreintaYLigera() {
            List<UserAnswerDTO> respuestas = buildAnswers(
                    ClasificacionConstantesUtil.ID_OPTION_DIAS_UNO_A_DOS,
                    ClasificacionConstantesUtil.ID_OPTION_DURACION_VEINTE_A_TREINTA_MINUTOS,
                    ClasificacionConstantesUtil.ID_OPTION_TIPO_CAMINATA_ACTIVIDAD_LIGERA,
                    ClasificacionConstantesUtil.ID_OPTION_SEDENTARIO_CUATRO_A_SEIS_HORAS
            );
            assertNivel(respuestas, NivelActividadFisicaEnum.NIVEL_2);
        }

        @Test
        @DisplayName("1-2 días + 20-30 min + moderada debe ser NIVEL_2")
        void unoADosDiasVeinteATreintaYModerada() {
            List<UserAnswerDTO> respuestas = buildAnswers(
                    ClasificacionConstantesUtil.ID_OPTION_DIAS_UNO_A_DOS,
                    ClasificacionConstantesUtil.ID_OPTION_DURACION_VEINTE_A_TREINTA_MINUTOS,
                    ClasificacionConstantesUtil.ID_OPTION_TIPO_ACTIVIDAD_MODERADA,
                    ClasificacionConstantesUtil.ID_OPTION_SEDENTARIO_CUATRO_A_SEIS_HORAS
            );
            assertNivel(respuestas, NivelActividadFisicaEnum.NIVEL_2);
        }
    }

    // ========================================================================
    // NIVEL 3 – Actividad moderada
    // ========================================================================

    @Nested
    @DisplayName("NIVEL_3 – Actividad moderada")
    class Nivel3 {

        @Test
        @DisplayName("3-4 días + 30-60 min + moderada debe ser NIVEL_3")
        void tresACuatroDiasTreintaASesentaYModerada() {
            List<UserAnswerDTO> respuestas = buildAnswers(
                    ClasificacionConstantesUtil.ID_OPTION_DIAS_TRES_A_CUATRO,
                    ClasificacionConstantesUtil.ID_OPTION_DURACION_TREINTA_A_SESENTA_MINUTOS,
                    ClasificacionConstantesUtil.ID_OPTION_TIPO_ACTIVIDAD_MODERADA,
                    ClasificacionConstantesUtil.ID_OPTION_SEDENTARIO_CUATRO_A_SEIS_HORAS
            );
            assertNivel(respuestas, NivelActividadFisicaEnum.NIVEL_3);
        }
    }

    // ========================================================================
    // NIVEL 4 – Actividad vigorosa
    // ========================================================================

    @Nested
    @DisplayName("NIVEL_4 – Actividad vigorosa")
    class Nivel4 {

        @Test
        @DisplayName("5+ días debe ser NIVEL_4 (independientemente de duración/tipo)")
        void cincoMasDias() {
            List<UserAnswerDTO> respuestas = buildAnswers(
                    ClasificacionConstantesUtil.ID_OPTION_DIAS_CINCO_MAS,
                    ClasificacionConstantesUtil.ID_OPTION_DURACION_VEINTE_A_TREINTA_MINUTOS,
                    ClasificacionConstantesUtil.ID_OPTION_TIPO_CAMINATA_ACTIVIDAD_LIGERA,
                    ClasificacionConstantesUtil.ID_OPTION_SEDENTARIO_MENOS_CUATRO_HORAS
            );
            assertNivel(respuestas, NivelActividadFisicaEnum.NIVEL_4);
        }

        @Test
        @DisplayName("Actividad vigorosa debe ser NIVEL_4 (independientemente de días/duración)")
        void tipoVigorosa() {
            List<UserAnswerDTO> respuestas = buildAnswers(
                    ClasificacionConstantesUtil.ID_OPTION_DIAS_UNO_A_DOS,
                    ClasificacionConstantesUtil.ID_OPTION_DURACION_VEINTE_A_TREINTA_MINUTOS,
                    ClasificacionConstantesUtil.ID_OPTION_TIPO_ACTIVIDAD_VIGOROSA,
                    ClasificacionConstantesUtil.ID_OPTION_SEDENTARIO_MENOS_CUATRO_HORAS
            );
            assertNivel(respuestas, NivelActividadFisicaEnum.NIVEL_4);
        }

        @Test
        @DisplayName("Duración > 60 min debe ser NIVEL_4 (independientemente de días/tipo)")
        void duracionMasSesenta() {
            List<UserAnswerDTO> respuestas = buildAnswers(
                    ClasificacionConstantesUtil.ID_OPTION_DIAS_UNO_A_DOS,
                    ClasificacionConstantesUtil.ID_OPTION_DURACION_MAS_SESENTA_MINUTOS,
                    ClasificacionConstantesUtil.ID_OPTION_TIPO_CAMINATA_ACTIVIDAD_LIGERA,
                    ClasificacionConstantesUtil.ID_OPTION_SEDENTARIO_MENOS_CUATRO_HORAS
            );
            assertNivel(respuestas, NivelActividadFisicaEnum.NIVEL_4);
        }
    }

    // ========================================================================
    // Caso por defecto: NIVEL_1 cuando ninguna regla aplica
    // ========================================================================

    @Nested
    @DisplayName("Default – NIVEL_1 por defecto")
    class Default {

        @Test
        @DisplayName("0 días + 20-30 min + ligera debe ser NIVEL_1 (default)")
        void ceroDiasVeinteATreintaYLigera() {
            List<UserAnswerDTO> respuestas = buildAnswers(
                    ClasificacionConstantesUtil.ID_OPTION_DIAS_CERO,
                    ClasificacionConstantesUtil.ID_OPTION_DURACION_VEINTE_A_TREINTA_MINUTOS,
                    ClasificacionConstantesUtil.ID_OPTION_TIPO_CAMINATA_ACTIVIDAD_LIGERA,
                    ClasificacionConstantesUtil.ID_OPTION_SEDENTARIO_MENOS_CUATRO_HORAS
            );
            assertNivel(respuestas, NivelActividadFisicaEnum.NIVEL_1);
        }

        @Test
        @DisplayName("3-4 días + 30-60 min + ligera debe ser NIVEL_1 (no cumple N2, N3 ni N4)")
        void tresACuatroDiasTreintaASesentaYLigera() {
            List<UserAnswerDTO> respuestas = buildAnswers(
                    ClasificacionConstantesUtil.ID_OPTION_DIAS_TRES_A_CUATRO,
                    ClasificacionConstantesUtil.ID_OPTION_DURACION_TREINTA_A_SESENTA_MINUTOS,
                    ClasificacionConstantesUtil.ID_OPTION_TIPO_CAMINATA_ACTIVIDAD_LIGERA,
                    ClasificacionConstantesUtil.ID_OPTION_SEDENTARIO_CUATRO_A_SEIS_HORAS
            );
            assertNivel(respuestas, NivelActividadFisicaEnum.NIVEL_1);
        }

        @Test
        @DisplayName("1-2 días + 30-60 min + moderada debe ser NIVEL_1 (no cumple condiciones exactas)")
        void unoADosDiasTreintaASesentaYModerada() {
            List<UserAnswerDTO> respuestas = buildAnswers(
                    ClasificacionConstantesUtil.ID_OPTION_DIAS_UNO_A_DOS,
                    ClasificacionConstantesUtil.ID_OPTION_DURACION_TREINTA_A_SESENTA_MINUTOS,
                    ClasificacionConstantesUtil.ID_OPTION_TIPO_ACTIVIDAD_MODERADA,
                    ClasificacionConstantesUtil.ID_OPTION_SEDENTARIO_CUATRO_A_SEIS_HORAS
            );
            assertNivel(respuestas, NivelActividadFisicaEnum.NIVEL_1);
        }
    }
}
