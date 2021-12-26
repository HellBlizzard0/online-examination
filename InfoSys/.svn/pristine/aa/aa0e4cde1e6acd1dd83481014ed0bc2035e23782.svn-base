package com.code.dal.orm.labcheck;

import java.io.Serializable;
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
@Table(name = "FIS_LAB_CHECK_RPRT_DPTS_EMPS")
public class LabCheckReportDepartmentEmployee extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private Long employeeId;
	private Long domainHospitalId;
	private Date forwardDate;
	private String forwardDateString;
	private Long labCheckReportDepartmentId;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_LAB_CHK_RPRT_DPT_EMPS_SEQ", sequenceName = "FIS_LAB_CHK_RPRT_DPT_EMPS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_LAB_CHK_RPRT_DPT_EMPS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	@Column(name = "DOMAINS_ID_HOSPITAL")
	public Long getDomainHospitalId() {
		return domainHospitalId;
	}

	public void setDomainHospitalId(Long domainHospitalId) {
		this.domainHospitalId = domainHospitalId;
	}

	@Basic
	@Column(name = "FORWARD_DATE")
	public Date getForwardDate() {
		return forwardDate;
	}

	public void setForwardDate(Date forwardDate) {
		this.forwardDate = forwardDate;
		this.forwardDateString = HijriDateService.getHijriDateString(forwardDate);
	}

	@Transient
	public String getForwardDateString() {
		return forwardDateString;
	}

	@Basic
	@Column(name = "LAB_CHECK_REPORT_DEPTS_ID")
	public Long getLabCheckReportDepartmentId() {
		return labCheckReportDepartmentId;
	}

	public void setLabCheckReportDepartmentId(Long labCheckReportDepartmentId) {
		this.labCheckReportDepartmentId = labCheckReportDepartmentId;
	}

	@Override
	public Long calculateContentId() {
		return id;
	}
}
