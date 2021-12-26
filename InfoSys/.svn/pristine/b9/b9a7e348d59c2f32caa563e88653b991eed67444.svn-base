package com.code.enums;

import java.util.Arrays;
import java.util.List;

public enum SecurityCheckReasonEnum {
	RECRUTMENT(1 , "\u0627\u0644\u062A\u062D\u0627\u0642 \u0628\u0627\u0644\u062E\u062F\u0645\u0629 / \u062A\u0639\u064A\u064A\u0646 \u0623\u0648 \u062A\u0648\u0638\u064A\u0641"),
	RETIRMENTORSEPARATION(2 , "\u062A\u0642\u0627\u0639\u062F \u0623\u0648 \u0641\u0635\u0644"),
	CONTRACT(3 , "\u062A\u0639\u0627\u0642\u062F"),
	REQUESTFORRESIGNATION(4 , "\u0637\u0644\u0628 \u0627\u0633\u062A\u0642\u0627\u0644\u0647"),
	DEPOSIT(5 , "\u0627\u0633\u062A\u064A\u062F\u0627\u0639")
	;
	private int code;
	private String arabicDescription;
	
	private SecurityCheckReasonEnum(int code , String arabicDescription){
		this.code = code;
		this.arabicDescription = arabicDescription;
	}
	
	public int getCode(){
		return code;
	}
	
	public String getArabicDescription(){
		return arabicDescription;
	}

	public static List<SecurityCheckReasonEnum> getAllSecurityCheckReason(){
		return Arrays.asList(SecurityCheckReasonEnum.values());
	}
}
