package com.code.enums;

public enum FinalApprovalEntitiesEnum {
	REGION_DIRECTOR("\u0642\u0627\u0626\u062F \u0627\u0644\u0645\u0646\u0637\u0642\u0629"),
	GENERAL_INTELLIGENCE_DIRECTORATE("\u0627\u0644\u0625\u062F\u0627\u0631\u0629 \u0627\u0644\u0639\u0627\u0645\u0629 \u0644\u0644\u0623\u0645\u0646 \u0648\u0627\u0644\u0627\u0633\u062A\u062E\u0628\u0627\u0631\u0627\u062A")
	;
	private String code;

	private FinalApprovalEntitiesEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
