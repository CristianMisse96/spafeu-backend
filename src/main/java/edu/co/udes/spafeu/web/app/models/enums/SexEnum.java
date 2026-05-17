package edu.co.udes.spafeu.web.app.models.enums;

import lombok.Getter;

@Getter
public enum SexEnum {

	MALE("M"), 
	FEMALE("F");

	private final String code;

	SexEnum(String code) {
		this.code = code;
	}

}