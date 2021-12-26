package com.code.enums;

public enum InfoConfidentialityEnum {
	FORBIDDEN(1),
	VERY_SECRET(2),
	SECRET(3),
	WITHOUT(4)
	;
	private int code;

	private InfoConfidentialityEnum(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
