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
	 @NamedQuery(name  = "employeeNonEmployeeDetail_searchEmployeeNonEmployeeDetail", 
             	 query = " select e" +
             			 " from EmployeeNonEmployeeDetail e" +
             			 " where (:P_EMP_ID = -1 or e.empId = :P_EMP_ID )" +
             			 " and   (:P_NON_EMP_ID = -1 or e.nonEmpId = :P_NON_EMP_ID )" +
                    	 " order by e.id "
    ),
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_EMP_NON_EMP_DETAILS")
public class EmployeeNonEmployeeDetail extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private Long empId;
	private Long qualificationId;
	private String qualificationDescription;
	private Integer maritalStatus;
	private String phoneNumber;
	private String mobileNumber;
	private String homeDescription;
	private Long imageId;
	private Long nonEmpId;

	@Id
	@SequenceGenerator(name="FIS_EMPLOYEES_DETAILS_SEQ", sequenceName="FIS_EMPLOYEES_DETAILS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="FIS_EMPLOYEES_DETAILS_SEQ")
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
	@Column(name = "STP_QUALIFICATION_ID")
	public Long getQualificationId() {
		return qualificationId;
	}

	public void setQualificationId(Long qualificationId) {
		this.qualificationId = qualificationId;
	}

	@Basic
	@Column(name = "STP_QUALIFICATION_DESCRIPTION")
	public String getQualificationDescription() {
		return qualificationDescription;
	}

	public void setQualificationDescription(String qualificationDescription) {
		this.qualificationDescription = qualificationDescription;
	}

	@Basic
	@Column(name = "MARITAL_STATUS")
	public Integer getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(Integer maritalStatus) {
		this.maritalStatus = maritalStatus;
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
	@Column(name = "MOBILE_NUMBER")
	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	@Basic
	@Column(name = "HOME_DESCRIPTION")
	public String getHomeDescription() {
		return homeDescription;
	}

	public void setHomeDescription(String homeDescription) {
		this.homeDescription = homeDescription;
	}

	@Basic
	@Column(name = "IMAGE_ID")
	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
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