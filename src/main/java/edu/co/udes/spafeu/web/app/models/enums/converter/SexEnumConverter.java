package edu.co.udes.spafeu.web.app.models.enums.converter;

import edu.co.udes.spafeu.web.app.models.enums.SexEnum;
import jakarta.persistence.AttributeConverter;

public class SexEnumConverter implements AttributeConverter<SexEnum, String> {

	@Override
	public String convertToDatabaseColumn(SexEnum attribute) {
		
		return attribute != null ? attribute.getCode() : null;
	}

	@Override
	public SexEnum convertToEntityAttribute(String dbData) {
		
		if (dbData == null) return null;

        return switch (dbData) {
            case "M" -> SexEnum.MALE;
            case "F" -> SexEnum.FEMALE;
            default -> throw new IllegalArgumentException("Invalid sex code: " + dbData);
        };
	}

}
