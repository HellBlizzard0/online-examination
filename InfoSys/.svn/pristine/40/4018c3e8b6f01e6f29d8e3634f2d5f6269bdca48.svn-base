package com.code.dal.dto.empcase;

import java.io.Serializable;

import javax.persistence.Transient;

import com.code.services.config.InfoSysConfigurationService;

public class EmployeeCaseData implements Serializable {
	private String caseNumber;
	private String caseDate;
	private String responsableDepName;
	private String caseType;
	private String subject;
	private String shortSubject = "";

	public String getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	public String getCaseDate() {
		return caseDate;
	}

	public void setCaseDate(String caseDate) {
		this.caseDate = caseDate;
	}

	public String getResponsableDepName() {
		return responsableDepName;
	}

	public void setResponsableDepName(String responsableDepName) {
		this.responsableDepName = responsableDepName;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Transient
	public String getShortSubject() {
		return subject.length() > InfoSysConfigurationService.getMinCaseDescriptionLength() ? subject.substring(0, InfoSysConfigurationService.getMinCaseDescriptionLength()) + "...." : subject;
	}

}