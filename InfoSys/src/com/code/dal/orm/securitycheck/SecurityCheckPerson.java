package com.code.dal.orm.securitycheck;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.code.dal.audit.DeleteableAuditEntity;
import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;

@NamedQueries({
	 @NamedQuery(name = "SecurityCheckPerson_deleteSecurityCheckPerson", 
     	 		query = " delete SecurityCheckPerson p " +
	         			" where p.securityCheckId = :P_SECURITY_CHECK_ID"
				)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_SECURITY_CHECKS_PERSONS")
public class SecurityCheckPerson extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private Long securityCheckId;
	private Long employeeId;
	private Long nonEmployeeId;
	private Integer isEmp;
	private Long domainNoteSrcId;
	private String noteSubject;
	private String noteDetail;
	private Boolean notesExist;
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_SECURITY_CHECKS_PER_SEQ", sequenceName = "FIS_SECURITY_CHECKS_PER_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_SECURITY_CHECKS_PER_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "SECURITY_CHECKS_ID")
	public Long getSecurityCheckId() {
		return securityCheckId;
	}

	public void setSecurityCheckId(Long securityCheckId) {
		this.securityCheckId = securityCheckId;
	}

	@Basic
	@Column(name = "STP_VW_EMPLOYEES_ID")
	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	@Basic
	@Column(name = "NON_EMPLOYEES_ID")
	public Long getNonEmployeeId() {
		return nonEmployeeId;
	}

	public void setNonEmployeeId(Long nonEmployeeId) {
		this.nonEmployeeId = nonEmployeeId;
	}

	@Basic
	@Column(name = "IS_EMP")
	public Integer getIsEmp() {
		return isEmp;
	}

	public void setIsEmp(Integer isEmp) {
		this.isEmp = isEmp;
	}

	@Basic
	@Column(name = "DOMAINS_ID_NOTE_SRC")
	public Long getDomainNoteSrcId() {
		return domainNoteSrcId;
	}

	public void setDomainNoteSrcId(Long domainNoteSrcId) {
		this.domainNoteSrcId = domainNoteSrcId;
	}

	@Basic
	@Column(name = "NOTE_SUBJECT")
	public String getNoteSubject() {
		return noteSubject;
	}

	public void setNoteSubject(String noteSubject) {
		this.noteSubject = noteSubject;
	}

	@Basic
	@Column(name = "NOTE_DETAIL")
	public String getNoteDetail() {
		return noteDetail;
	}

	public void setNoteDetail(String noteDetail) {
		this.noteDetail = noteDetail;
	}

	@Basic
	@Column(name = "NOTES_EXISTS")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getNotesExist() {
		return notesExist;
	}

	public void setNotesExist(Boolean notesExist) {
		this.notesExist = notesExist;
	}

	@Override
	public Long calculateContentId() {
		return this.id;
	}
}