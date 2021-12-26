package com.code.enums;

public enum GhostTypeEnum {
	EMPLOYEE(1),
	NON_EMPLOYEE(0)
	;

	private int code;

	private GhostTypeEnum(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
