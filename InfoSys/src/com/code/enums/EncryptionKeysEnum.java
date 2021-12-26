package com.code.enums;

public enum EncryptionKeysEnum {
	BOOL_SERVER_PUBLIC_KEY_PATH("/keys/publicBoolServer.key");
	
	private String code;
	private EncryptionKeysEnum(String code) {
		this.code = code;
	}
	public String getCode() {
		return code;
	}
}
