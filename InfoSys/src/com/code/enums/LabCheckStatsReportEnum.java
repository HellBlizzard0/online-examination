package com.code.enums;

import java.util.Arrays;
import java.util.List;

public enum LabCheckStatsReportEnum {
	LAB_CHECK_STATS_FOR_REGIONS(1, "LAB_CHECK_STATS_FOR_REGIONS_REPORT"),
	LAB_CHECK_STATS_FOR_TRAINING_CENTERS(2,"LAB_CHECK_STATS_FOR_TRAINING_CENTERS_REPORT"),
	LAB_CHECK_STATS_BASED_ON_MATERIAL_TYPE(3,"LAB_CHECK_STATS_BASED_ON_MATERIAL_TYPE_REPORT"),
	LAB_CHECK_STATS_BASED_ON_AGE(4,"LAB_CHECK_STATS_BASED_ON_AGE_REPORT"),
	LAB_CHECK_STATS_BASED_ON_SOLDIER_RANKS(5,"LAB_CHECK_STATS_BASED_ON_OFFICER_RANKS_REPORT"),
	LAB_CHECK_STATS_BASED_ON_OFFICER_RANKS(6,"LAB_CHECK_STATS_BASED_ON_SOLDIER_RANKS_REPORT"),
	LAB_CHECK_STATS_BASED_ON_OPERATIONAL_DEP_TYPE(7,"LAB_CHECK_STATS_BASED_ON_OPERATIONAL_DEP_TYPE_REPORT")
	;
	private int code;
	private String reportEnumName;

	private LabCheckStatsReportEnum(int code, String reportEnumName) {
		this.code = code;
		this.reportEnumName = reportEnumName;
	}

	public int getCode() {
		return code;
	}
	
	public String getReportEnumName() {
		return reportEnumName;
	}

	public static List<LabCheckStatsReportEnum> getAllCheckStatsValues(){
		return Arrays.asList(LabCheckStatsReportEnum.values());
	}
}
