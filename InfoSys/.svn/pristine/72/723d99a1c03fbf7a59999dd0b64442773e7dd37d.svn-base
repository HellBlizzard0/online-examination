package com.code.dal.orm.setup;

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

import com.code.dal.audit.DeleteableAuditEntity;
import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;

@NamedQueries({
	 @NamedQuery(name  = "empNonEmpRelative_searchEmpNonEmpRelatives", 
            	 query = " select e" +
            			 " from EmpNonEmpRelative e" +
            			 " where (:P_EMP_ID = -1 or e.empId = :P_EMP_ID )" +
            			 " and   (:P_NON_EMP_ID = -1 or e.nonEmpId = :P_NON_EMP_ID )" +
                   	     " order by e.empId "
   ),
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_EMP_NON_EMP_RELATIVES")
public class EmpNonEmpRelative extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private Long empId;
	private Long nonEmpId;
	private String fullName;
	private String jobDescription;
	private String phoneNumber;

	@Id
	@SequenceGenerator(name="FIS_EMPLOYEES_RELATIVES_SEQ", sequenceName="FIS_EMPLOYEES_RELATIVES_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="FIS_EMPLOYEES_RELATIVES_SEQ")
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "EMP_ID")
	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
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
	@Column(name = "JOB_DESCRIPTION")
	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	@Basic
	@Column(name = "PHONE_NUMBER")
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Basic
	@Column(name = "NON_EMP_ID")
	public Long getNonEmpId() {
		return nonEmpId;
	}

	public void setNonEmpId(Long nonEmpId) {
		this.nonEmpId = nonEmpId;
	}
	
	@Override
	public Long calculateContentId() {
		return this.id;
	}
}