package com.code.enums;

public enum PenaltyArrestLocationEnum {
	JAIL("\u0627\u0644\u0633\u062C\u0646"), MILITARY_POLICE("\u0645\u0642\u0631 \u0627\u0644\u0634\u0631\u0637\u0629 \u0627\u0644\u0639\u0633\u0643\u0631\u064A\u0629");
	private String code;

	private PenaltyArrestLocationEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
