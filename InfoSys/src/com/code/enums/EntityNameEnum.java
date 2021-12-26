package com.code.enums;

public enum EntityNameEnum {
	CAR_RENTAL("CarRental"),
	SECURITY_CHECK("SecurityCheck"),
	SURVEILLANCE_ORDER("SurveillanceOrder"),
	MISSION_GUARD("MissionGuard"),
	SURVEILLANCE_REPORT("SurveillanceReport"),
	ASSIGNMENT("Assignment"),
	LAB_CHECK("LabCheck"),
	LAB_CHECK_EMPLOYEE("LabCheckEmployee"),
	LAB_CHECK_REPORT("LabCheckReport"),
	INFO("Info"),
	LETTER("LETTER"),
	FOLLOW_UP_RESULT("FollowUpResult"),
	CONVERSATION("Conversation"),
	FOLLOW_UP("FollowUp")
	;
	private String code;

	private EntityNameEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
