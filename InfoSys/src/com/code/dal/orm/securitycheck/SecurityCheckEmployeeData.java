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
	 @NamedQuery(name = "securityCheckEmployeeData_searchSecurityCheckEmployee", 
      	 		query = " select p " +
	         	 		" from SecurityCheckEmployeeData p " +
	         			" where (:P_ID = -1 or p.id = :P_ID )" +
	         			" and (:P_SECURITY_CHECK_ID = -1 or p.securityCheckId = :P_SECURITY_CHECK_ID ) " +
	         			" order by p.id "
				)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_SECURITY_CHECK_EMPLOYEE")
public class SecurityCheckEmployeeData extends BaseEntity implements Serializable{
	private Long id;
	private Long securityCheckId;
	private Long employeeId;
	private Long domainNoteSrcId;
	private String noteSubject;
	private String noteDetail;
	private Boolean notesExist;
	private String fullName;
	private String socialID;
	private String militaryNo;
	private String rank;
	private String domainNoteSrcDescription;

	private SecurityCheckPerson person;
	
	public SecurityCheckEmployeeData(){
		person = new SecurityCheckPerson();
		person.setIsEmp(1);
	}
	
	@Id
	@Column(name = "ID")
	public Long getId() {
		return person.getId();
	}

	public void setId(Long id) {
		person.setId(id);
		this.id = id;
	}

	@Basic
	@Column(name = "SECURITY_CHECKS_ID")
	public Long getSecurityCheckId() {
		return securityCheckId;
	}

	public void setSecurityCheckId(Long securityCheckId) {
		person.setSecurityCheckId(securityCheckId);
		this.securityCheckId = securityCheckId;
	}

	@Basic
	@Column(name = "STP_VW_EMPLOYEES_ID")
	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		person.setEmployeeId(employeeId);
		this.employeeId = employeeId;
	}

	@Basic
	@Column(name = "DOMAINS_ID_NOTE_SRC")
	public Long getDomainNoteSrcId() {
		return domainNoteSrcId;
	}

	public void setDomainNoteSrcId(Long domainNoteSrcId) {
		person.setDomainNoteSrcId(domainNoteSrcId);
		this.domainNoteSrcId = domainNoteSrcId;
	}

	@Basic
	@Column(name = "NOTE_SUBJECT")
	public String getNoteSubject() {
		return noteSubject;
	}

	public void setNoteSubject(String noteSubject) {
		person.setNoteSubject(noteSubject);
		this.noteSubject = noteSubject;
	}

	@Basic
	@Column(name = "NOTE_DETAIL")
	public String getNoteDetail() {
		return noteDetail;
	}

	public void setNoteDetail(String noteDetail) {
		person.setNoteDetail(noteDetail);
		this.noteDetail = noteDetail;
	}

	@Basic
	@Column(name = "NOTES_EXISTS")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getNotesExist() {
		return notesExist;
	}

	public void setNotesExist(Boolean notesExist) {
		person.setNotesExist(notesExist);
		this.notesExist = notesExist;
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
	@Column(name = "SOCIAL_ID")
	public String getSocialID() {
		return socialID;
	}

	public void setSocialID(String socialID) {
		this.socialID = socialID;
	}

	@Basic
	@Column(name = "MILITARY_NO")
	public String getMilitaryNo() {
		return militaryNo;
	}

	public void setMilitaryNo(String militaryNo) {
		this.militaryNo = militaryNo;
	}

	@Basic
	@Column(name = "RANK")
	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}
	
	@Basic
	@Column(name = "DOMAIN_DESCRIPTION")
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