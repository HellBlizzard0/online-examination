package com.code.dal.orm.setup;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.code.dal.orm.BaseEntity;
import com.code.services.util.HijriDateService;

@NamedQueries({
	@NamedQuery(name = "nonEmployeeData_searchNonEmployee", 
				query = " select ne " +
						" from NonEmployeeData ne " +
						" where (:P_IDENTITY = -1 or ne.identity = :P_IDENTITY )" +
						" and  (:P_ID = -1 or ne.id = :P_ID )" +
						" and (:P_FULL_NAME = '-1' or ne.fullName like :P_FULL_NAME )" +
						" order by ne.fullName"
				),
				@NamedQuery(name  = "nonEmployeeData_searchInfoRelatedNonEmployees", 
		      	 query = " select ne " +
		      			 " from NonEmployeeData ne, InfoRelatedEntity infoRel" +
		      			 " where ( ne.id = infoRel.nonEmployeeId)" +
		      			 " and (:P_INFO_ID = '-1' or infoRel.infoId = :P_INFO_ID )" +
		             	 " order by ne.id "
		         )
})
@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_NON_EMPLOYEES")
public class NonEmployeeData extends BaseEntity implements Serializable, Cloneable{
	private Long id;
	private Long countryId;
	private Long identity;
	private String fullName;
	private String description;
	private String sponsorDescription;
	private String countryArabicName;
	private String phoneNo;
	private Date birthDate;
	private String birthDateString;
	private Date birthDateGreg;
	private String birthDateGregString;

	private NonEmployee nonEmployee;

	public NonEmployeeData() {
		this.nonEmployee = new NonEmployee();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return nonEmployee.getId();
	}

	public void setId(Long id) {
		this.nonEmployee.setId(id);
		this.id = id;
	}

	@Basic
	@Column(name = "STP_VW_COUNTIRES_ID")
	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.nonEmployee.setCountryId(countryId);
		this.countryId = countryId;
	}

	@Basic
	@Column(name = "IDENTITY_DESC")
	public Long getIdentity() {
		return identity;
	}

	public void setIdentity(Long identity) {
		this.nonEmployee.setIdentity(identity);
		this.identity = identity;
	}

	@Basic
	@Column(name = "FULL_NAME")
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.nonEmployee.setFullName(fullName);
		this.fullName = fullName;
	}

	@Basic
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.nonEmployee.setDescription(description);
		this.description = description;
	}

	@Basic
	@Column(name = "SPONSOR_DESCRIPTION")
	public String getSponsorDescription() {
		return sponsorDescription;
	}

	public void setSponsorDescription(String sponsorDescription) {
		this.nonEmployee.setSponsorDescription(sponsorDescription);
		this.sponsorDescription = sponsorDescription;
	}

	@Basic
	@Column(name = "COUNTRY_ARABIC_NAME")
	public String getCountryArabicName() {
		return countryArabicName;
	}

	public void setCountryArabicName(String countryArabicName) {
		this.countryArabicName = countryArabicName;
	}

	@Transient
	public NonEmployee getNonEmployee() {
		return nonEmployee;
	}

	public void setNonEmployee(NonEmployee nonEmployee) {
		this.nonEmployee = nonEmployee;
	}

	@Basic
	@Column(name = "PHONE_NO")
	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
		this.nonEmployee.setPhoneNo(phoneNo);
	}
	
	@Basic
	@Column(name = "BIRTH_DATE")
	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
		this.birthDateString = HijriDateService.getHijriDateString(birthDate);
		this.nonEmployee.setBirthDate(birthDate);
	}

	@Transient
	public String getBirthDateString() {
		return birthDateString;
	}
	
	public void setBirthDateString(String birthDateString) {
		this.birthDateString = birthDateString;
	}
	
	@Basic
	@Column(name = "BIRTH_DATE_GREG")
	public Date getBirthDateGreg() {
		return birthDateGreg;
	}

	public void setBirthDateGreg(Date birthDateGreg) {
		this.birthDateGreg = birthDateGreg;
		this.birthDateGregString = ((birthDateGreg == null) ? null : new SimpleDateFormat("dd/MM/yyyy").format(birthDateGreg));
		this.nonEmployee.setBirthDateGreg(birthDateGreg);
	}
	
	@Transient
	public String getBirthDateGregString() {
		return birthDateGregString;
	}
	
	public void setBirthDateGregString(String birthDateGregString) {
		this.birthDateGregString = birthDateGregString;
	}
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
