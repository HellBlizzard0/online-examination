package com.code.enums;

public enum HCMLabCheckStatusEnum {
	NO_LAB_CHECK(5),
	RUNNING(15),
	NEGATIVE(20),
	POSITIVE(25),
	SENT_TO_SECURITY_FORCES(30),
	CHEATING(35),
	;
	private int code;

	private HCMLabCheckStatusEnum(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
