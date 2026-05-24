package edu.co.udes.spafeu.web.app.models.enums;

import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonValue; 
import com.fasterxml.jackson.annotation.JsonCreator;

@Getter
public enum SexEnum {

	MALE("M"), 
	FEMALE("F");
	
	@JsonValue
	private final String code;

	SexEnum(String code) {
		this.code = code;
	}
	
	@JsonCreator
    public static SexEnum fromValue(String value) {
        for (SexEnum sex : values()) {
            if (sex.code.equalsIgnoreCase(value)
                    || sex.name().equalsIgnoreCase(value)) {
                return sex;
            }
        }

        throw new IllegalArgumentException("Sexo inválido: " + value);
    }
}