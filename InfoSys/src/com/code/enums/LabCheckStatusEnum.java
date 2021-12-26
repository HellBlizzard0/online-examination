package com.code.enums;

public enum LabCheckStatusEnum {
	REGISTERED(0),
	UNDER_APPROVAL(1),
	APPROVED(2),
	REJECTED(3),
	;
	private int code;

	private LabCheckStatusEnum(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
}
