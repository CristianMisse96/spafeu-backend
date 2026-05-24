package edu.co.udes.spafeu.web.app.models.enums.converter;

import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import jakarta.persistence.AttributeConverter;

public class NivelActividadFisicaEnumConverter implements AttributeConverter<NivelActividadFisicaEnum, String> {

    @Override
    public String convertToDatabaseColumn(NivelActividadFisicaEnum attribute) {
        return attribute != null ? attribute.name() : null;
    }

    @Override
    public NivelActividadFisicaEnum convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;

        return switch (dbData) {
            case "NIVEL_1" -> NivelActividadFisicaEnum.NIVEL_1;
            case "NIVEL_2" -> NivelActividadFisicaEnum.NIVEL_2;
            case "NIVEL_3" -> NivelActividadFisicaEnum.NIVEL_3;
            case "NIVEL_4" -> NivelActividadFisicaEnum.NIVEL_4;
            default -> throw new IllegalArgumentException("Invalid activity level code: " + dbData);
        };
    }

}