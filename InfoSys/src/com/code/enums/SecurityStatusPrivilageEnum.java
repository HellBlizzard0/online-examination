package com.code.enums;

public enum SecurityStatusPrivilageEnum {
	SUPER_ADMIN(0),
	ADMIN(1),
	USER(2);	
	private int value;

	private SecurityStatusPrivilageEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
