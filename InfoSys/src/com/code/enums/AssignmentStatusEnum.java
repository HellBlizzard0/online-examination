package com.code.enums;

public enum AssignmentStatusEnum {
	UNDER_APPROVAL(0),
	APPROVED(1),
	REJECTED(2),
	REGISTERED(3);
	private Integer code;

	private AssignmentStatusEnum(Integer code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}
}
