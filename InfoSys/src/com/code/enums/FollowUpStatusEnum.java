package com.code.enums;

public enum FollowUpStatusEnum {
	ACTIVE(0),
	SUSPENDED(1),
	FINISHED(3),
	RE_FOLLOW_UP(4)
	;

	private Integer code;

	private FollowUpStatusEnum(Integer code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}
}
