package com.code.dal.orm.securitycheck;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.code.dal.orm.BaseEntity;

@NamedQueries({
	 @NamedQuery(name = "securityCheckNonEmployeeData_searchSecurityCheckNonEmployee", 
      	 		query = " select p " +
	         	 		" from SecurityCheckNonEmployeeData p " +
	         			" where (:P_ID = -1 or p.id = :P_ID )" +
	         			" and (:P_SECURITY_CHECK_ID = -1 or p.securityCheckId = :P_SECURITY_CHECK_ID ) " +
	         			" order by p.id "
				)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_SECURITY_CHECK_NON_EMP")
public class SecurityCheckNonEmployeeData extends BaseEntity implements Serializable{
	private Long id;
	private Long securityCheckId;
	private Long nonEmployeeId;
	private Long domainNoteSrcId;
	private String noteSubject;
	private String noteDetail;
	private Boolean notesExist;
	private Long countryId;
	private Long identity;
	private String fullName;
	private String sponsorDescription;
	private String countryArabicName;
	private String domainNoteSrcDescription;
	
	private SecurityCheckPerson person;

	public SecurityCheckNonEmployeeData(){
		person = new SecurityCheckPerson();
		person.setIsEmp(0);
	}
	
	@Id
	@Column(name = "ID")
	public Long getId() {
		return person.getId();
	}

	public void setId(Long id) {
		this.person.setId(id);
		this.id = id;
	}

	@Basic
	@Column(name = "SECURITY_CHECKS_ID")
	public Long getSecurityCheckId() {
		return securityCheckId;
	}

	public void setSecurityCheckId(Long securityCheckId) {
		this.person.setSecurityCheckId(securityCheckId);
		this.securityCheckId = securityCheckId;
	}

	@Basic
	@Column(name = "NON_EMPLOYEES_ID")
	public Long getNonEmployeeId() {
		return nonEmployeeId;
	}

	public void setNonEmployeeId(Long nonEmployeeId) {
		this.person.setNonEmployeeId(nonEmployeeId);
		this.nonEmployeeId = nonEmployeeId;
	}

	@Basic
	@Column(name = "DOMAINS_ID_NOTE_SRC")
	public Long getDomainNoteSrcId() {
		return domainNoteSrcId;
	}

	public void setDomainNoteSrcId(Long domainNoteSrcId) {
		this.person.setDomainNoteSrcId(domainNoteSrcId);
		this.domainNoteSrcId = domainNoteSrcId;
	}

	@Basic
	@Column(name = "NOTE_SUBJECT")
	public String getNoteSubject() {
		return noteSubject;
	}

	public void setNoteSubject(String noteSubject) {
		this.person.setNoteSubject(noteSubject);
		this.noteSubject = noteSubject;
	}

	@Basic
	@Column(name = "NOTE_DETAIL")
	public String getNoteDetail() {
		return noteDetail;
	}

	public void setNoteDetail(String noteDetail) {
		this.person.setNoteDetail(noteDetail);
		this.noteDetail = noteDetail;
	}

	@Basic
	@Column(name = "NOTES_EXISTS")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getNotesExist() {
		return notesExist;
	}

	public void setNotesExist(Boolean notesExist) {
		this.person.setNotesExist(notesExist);
		this.notesExist = notesExist;
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
	@Column(name = "SPONSOR_DESCRIPTION")
	public String getSponsorDescription() {
		return sponsorDescription;
	}

	public void setSponsorDescription(String sponsorDescription) {
		this.sponsorDescription = sponsorDescription;
	}

	@Basic
	@Column(name = "COUNTRY_NAME")
	public String getCountryArabicName() {
		return countryArabicName;
	}

	public void setCountryArabicName(String countryArabicName) {
		this.countryArabicName = countryArabicName;
	}
	
	@Basic
	@Column(name = "DESCRIPTION")
	public String getDomainNoteSrcDescription() {
		return domainNoteSrcDescription;
	}

	public void setDomainNoteSrcDescription(String domainNoteSrcDescription) {
		this.domainNoteSrcDescription = domainNoteSrcDescription;
	}
	
	@Transient
	public SecurityCheckPerson getPerson() {
		return person;
	}

	public void setPerson(SecurityCheckPerson person) {
		this.person = person;
	}
}