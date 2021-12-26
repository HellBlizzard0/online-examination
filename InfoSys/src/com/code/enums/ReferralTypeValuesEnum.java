package com.code.enums;

public enum ReferralTypeValuesEnum {
	REDIRECT("\u062A\u0648\u062C\u064A\u0647")
	;
    private String code;
      
    private ReferralTypeValuesEnum(String code){
        this.code = code;
    }
    
    public String getCode(){return code;}
}
