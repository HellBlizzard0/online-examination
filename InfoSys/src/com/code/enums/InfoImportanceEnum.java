package com.code.enums;

public enum InfoImportanceEnum {
	IMMEDIATE(1),
	URGENT(2),
	MEDIUM(3),
	NORMAL(4),
	WITHOUT(5)
	;
	private int code;

	private InfoImportanceEnum(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
