package com.code.enums;

public enum LabCheckEmployeeCheckStatusEnum {
	NO_SAMPLE(1),
	NEGATIVE(2),
	POSITIVE_UNDER_APPROVE(3),
	SENDING_TO_FORCE(4),
	POSITIVE(5),
	CHECK_PRIVATION(6),
	SAMPLE_TAKED(7),
	RETEST(8),
	CHEATING(9)
	;
	private int code;

	private LabCheckEmployeeCheckStatusEnum(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
