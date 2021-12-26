package com.code.enums;

public enum InfoRelatedEntityTypeEnum {
	EMPLOYEE(1),
	NON_EMPLOYEE(2),
	DEPARTMENT(3),
	COUNTRY(4),
	DOMAIN(5);

	private int code;

	private InfoRelatedEntityTypeEnum(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
