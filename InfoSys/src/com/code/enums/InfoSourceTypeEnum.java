package com.code.enums;

public enum InfoSourceTypeEnum {
	ASSIGNMENT(1),
	OPEN_SOURCE(2),
	COOPERATOR(3);

	private int code;

	private InfoSourceTypeEnum(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
