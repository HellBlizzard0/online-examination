package com.code.enums;

public enum WFPositionDiscriminatorEnum {
	EMPLOYEE(1),
	DEPARTMENT(2),
	EMPS_GROUP(3)
	;
	private int code;

	private WFPositionDiscriminatorEnum(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
