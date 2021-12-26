package com.code.enums;

public enum CarRegisterationTypEnum {
	PRIVATE_CAR((short)1),
	PUBLIC_TRANSPORT((short)2),
	PRIVATE_TRANSPORT((short)3),
	PUBLIC_BUS((short)4),
	PRIVATE_BUS((short)5),
	TAXI((short)6),
	HEAVY_EQUIPMENT((short)7),
	EXPORT((short)8),
	DIPLOMATIC((short)9),
	MOTORCYCLE((short)10),
	TEMPORARY((short)11);
	
	private short code;

	private CarRegisterationTypEnum(short code) {
		this.code = code;
	}

	public short getCode() {
		return code;
	}
}