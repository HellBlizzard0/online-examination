package com.code.enums;

public enum FinMonthlyRewardStatusEnum {
	UNDER_PROCESSING(1),
	APPROVED(2),
	REJECTED(3)
	;

	private int code;

	private FinMonthlyRewardStatusEnum(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
