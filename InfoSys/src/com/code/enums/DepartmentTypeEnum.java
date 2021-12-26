package com.code.enums;

import com.code.dal.DataAccess;

public enum DepartmentTypeEnum {
	DIRECTORATE(isTypeIntegrated() ? 100L : 1L),
	REGION(isTypeIntegrated() ? 75L : 2L),
	SECTOR(isTypeIntegrated() ? 65L : 3L),
	GENERAL_UNIT(60L),
	INSTITUTE(50L),
	MARINE_UNITS(45L),
	UNIT(40L),
	TRAINING_CENTER(38L),
	HEAD_TRAINING_CENTER(36L),
	CENTERS_FORCE(30L),
	FORCE(27L),
	UNITY(25L),
	BATTALION(24L),
	CENTER(23L),
	PATROLS_AND_SARAYA(22L),
	GROUP_OF_BOATS(20L),
	COMPANY(17L),
	DIVISION(15L),
	SECTION(13L),
	OFFICE(11L),
	POINT(10L),
	TEMPORARY_UNIT(9L),
	WORKSHOP(7L),
	;
	private Long code;

	private DepartmentTypeEnum(Long code) {
		this.code = code;
	}

	public Long getCode() {
		return code;
	}
	public static boolean isTypeIntegrated() {
		try {
			String flag = DataAccess.configuration.getString("typesFlag");
			if (flag != null && !flag.isEmpty() && !flag.trim().equals("") && flag.equals("false")) {
				return false;
			}
		} catch (Exception e) {
			return true;
		}
		return true;
	}
}
