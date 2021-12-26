package com.code.enums;

public enum FollowUpDecisionTypeEnum {
	
	FOLLOW_UP_EXTEND(0),
	FOLLOW_UP_END(1),
	;

	private Integer code;

	private FollowUpDecisionTypeEnum(Integer code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}
}
