package com.code.dal.orm;

import java.io.Serializable;

import javax.persistence.Transient;

public abstract class AuditEntity extends BaseEntity implements Serializable {
	protected final static String AUDIT_SEPARATOR = "*|#";
	private String systemUser;

	@Transient
	public String getSystemUser() {
		return systemUser;
	}

	public void setSystemUser(String systemUser) {
		this.systemUser = systemUser;
	}

	public abstract Long calculateContentId();
}