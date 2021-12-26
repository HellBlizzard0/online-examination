package com.code.enums;

public enum CategoryEnum {
	OFFICER(1L),
	SOLIDER(2L);
	
	private Long code;
	private CategoryEnum(Long code) {
		this.code = code;
	}
	public Long getCode() {
		return code;
	}
}
