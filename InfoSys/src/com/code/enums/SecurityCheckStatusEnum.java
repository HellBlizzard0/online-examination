package com.code.enums;

public enum SecurityCheckStatusEnum {
	SAVED(1),
	UNDER_APPROVAL(2),
	UNDER_PROCCESSING(3),
	PROCCESSING_UNDER_APPROVAL(4),
	APPROVED(5),
	REJECTED(6)
	;
	private int code;

	private SecurityCheckStatusEnum(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
