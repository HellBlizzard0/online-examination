package com.code.enums;

public enum SecurityStatusEnum {
	ARRESTED((short)1),
	SERVICE_SUSPENSION((short)2),
	REVIEWER((short)3),
	TRAVEL_BAN((short)4),
	UNSYSTEMATIC((short)5),
	WANTED((short)10);
	
	private short code;

	private SecurityStatusEnum(short code) {
		this.code = code;
	}

	public short getCode() {
		return code;
	}
}
