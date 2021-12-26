package com.code.enums;


public enum ConversationResultsEnum {
	NO_RESULTS_EXISTS(0),
	NEGATIVE(1),
	POSITIVE(2)
	;

	private Integer code;

	private ConversationResultsEnum(Integer code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}
}

