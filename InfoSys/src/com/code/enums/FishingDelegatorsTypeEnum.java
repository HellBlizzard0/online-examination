package com.code.enums;

public enum FishingDelegatorsTypeEnum {
	CAPTAIN("Y"),
	SAILOR("N");
	private String code;
	private FishingDelegatorsTypeEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
