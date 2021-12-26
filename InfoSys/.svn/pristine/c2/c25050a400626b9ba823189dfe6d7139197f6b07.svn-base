package com.code.enums;

import java.util.Arrays;
import java.util.List;

public enum LabCheckReasonsEnum {
	PROMOTION(1, "\u0627\u0644\u062A\u0631\u0642\u064A\u0629"),
	UNEXPECTED(2,"\u0645\u0641\u0627\u062C\u0626"),
	CASES(3,"\u0642\u0636\u0627\u064A\u0627"),
	PERIODICAL_CHECK(7,"\u062F\u0648\u0631\u0629"),
	TREATMENT_PURPOSE(6,"\u0622\u0644\u064A\u0629 \u0639\u0644\u0627\u062C"),
	MEDICAL_COMMITTEE_CHECK(5,"\u0639\u0631\u0636 \u0639\u0644\u0649 \u0627\u0644\u0644\u062C\u0646\u0629 \u0627\u0644\u0637\u0628\u064A\u0629"),
	RANDOM_MISSION_FROM_DIRECTORATE(4,"\u0645\u0647\u0627\u0645 \u0627\u0644\u0645\u062F\u064A\u0631\u064A\u0629")
	;
	private int code;
	private String arabicDescription;

	private LabCheckReasonsEnum(int code, String arabicDescription) {
		this.code = code;
		this.arabicDescription = arabicDescription;
	}

	public int getCode() {
		return code;
	}
	
	public String getArabicDescription() {
		return arabicDescription;
	}
	
	public static List<LabCheckReasonsEnum> getAllCheckReasonsEnumValues(){
		return Arrays.asList(LabCheckReasonsEnum.values());
	}
}
