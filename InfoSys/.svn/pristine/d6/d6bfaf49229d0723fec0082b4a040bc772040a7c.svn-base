package com.code.dal.orm.setup;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.code.dal.audit.DeleteableAuditEntity;
import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;
import com.code.services.util.HijriDateService;

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_NON_EMPLOYEES")
public class NonEmployee extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private Long countryId;
	private Long identity;
	private String fullName;
	private String description;
	private String sponsorDescription;
	private String phoneNo;
	private Date birthDate;
	private String birthDateString;
	private Date birthDateGreg;
	private String birthDateGregString;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_NON_EMPLOYEES_SEQ", sequenceName = "FIS_NON_EMPLOYEES_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_NON_EMPLOYEES_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "STP_VW_COUNTIRES_ID")
	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	@Basic
	@Column(name = "IDENTITY_DESC")
	public Long getIdentity() {
		return identity;
	}

	public void setIdentity(Long identity) {
		this.identity = identity;
	}

	@Basic
	@Column(name = "FULL_NAME")
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Basic
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Basic
	@Column(name = "SPONSOR_DESCRIPTION")
	public String getSponsorDescription() {
		return sponsorDescription;
	}

	public void setSponsorDescription(String sponsorDescription) {
		this.sponsorDescription = sponsorDescription;
	}
	
	@Override
	public Long calculateContentId() {
		return this.id;
	}
	
	@Basic
	@Column(name = "PHONE_NO")
	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	@Basic
	@Column(name = "BIRTH_DATE")
	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
		this.birthDateString = HijriDateService.getHijriDateString(birthDate);
	}
	
	@Transient
	public String getBirthDateString() {
		return birthDateString;
	}

	@Basic
	@Column(name = "BIRTH_DATE_GREG")
	public Date getBirthDateGreg() {
		return birthDateGreg;
	}

	public void setBirthDateGreg(Date birthDateGreg) {
		this.birthDateGreg = birthDateGreg;
		this.birthDateGregString = ((birthDateGreg == null) ? null : new SimpleDateFormat("dd/MM/yyyy").format(birthDateGreg));
	}
	
	@Transient
	public String getBirthDateGregString() {
		return birthDateGregString;
	}
	
}
