package edu.co.udes.spafeu.web.app.models.enums;

/**
 * Enum que representa los niveles de clasificación de actividad física
 * de un usuario, según las respuestas al cuestionario de Historial de actividad física.
 *
 * <p>Los niveles se ordenan de menor a mayor intensidad de actividad:
 * <ul>
 *   <li>{@link #NIVEL_1} – Sedentario</li>
 *   <li>{@link #NIVEL_2} – Actividad leve</li>
 *   <li>{@link #NIVEL_3} – Actividad moderada</li>
 *   <li>{@link #NIVEL_4} – Actividad vigorosa</li>
 * </ul>
 */
public enum NivelActividadFisicaEnum {

    NIVEL_1("Sedentario"),
    NIVEL_2("Actividad Leve"),
    NIVEL_3("Actividad moderada"),
    NIVEL_4("Actividad vigorosa");

    private final String label;

    NivelActividadFisicaEnum(String label) {
        this.label = label;
    }

    /**
     * Devuelve la etiqueta legible del nivel de actividad física.
     *
     * @return la etiqueta del nivel
     */
    public String getLabel() {
        return label;
    }
}
