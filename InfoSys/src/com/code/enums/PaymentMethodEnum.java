package com.code.enums;

public enum PaymentMethodEnum {
	BANK_ACCOUNT(0), CHEQUE(1), CASH(2);
	private Integer code;

	private PaymentMethodEnum(Integer code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}
}
