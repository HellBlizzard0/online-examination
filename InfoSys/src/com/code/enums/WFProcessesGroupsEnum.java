package com.code.enums;

public enum WFProcessesGroupsEnum {
	FIS_GROUP(1);
	;
	private int code;

	private WFProcessesGroupsEnum(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
