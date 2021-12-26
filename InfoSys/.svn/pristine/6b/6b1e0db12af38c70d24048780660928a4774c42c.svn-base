package com.code.dal.orm.labcheck;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.code.dal.audit.DeleteableAuditEntity;
import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_LAB_CHECK_REPORT_DEPTS")
public class LabCheckReportDepartment extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private Long labCheckReportId;
	private Long departmentId;
	private Integer forcesNumber;
	private Integer forcesActual;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_LAB_CHCK_REPORT_DEPT_SEQ", sequenceName = "FIS_LAB_CHCK_REPORT_DEPT_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_LAB_CHCK_REPORT_DEPT_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "LAB_CHECK_REPORTS_ID")
	public Long getLabCheckReportId() {
		return labCheckReportId;
	}

	public void setLabCheckReportId(Long labCheckReportId) {
		this.labCheckReportId = labCheckReportId;
	}

	@Basic
	@Column(name = "STP_VW_DEPARTMENTS_ID")
	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	@Basic
	@Column(name = "FORCES_NUMBER")
	public Integer getForcesNumber() {
		return forcesNumber;
	}

	public void setForcesNumber(Integer forcesNumber) {
		this.forcesNumber = forcesNumber;
	}

	@Basic
	@Column(name = "FORCES_ACTUAL")
	public Integer getForcesActual() {
		return forcesActual;
	}

	public void setForcesActual(Integer forcesActual) {
		this.forcesActual = forcesActual;
	}

	@Override
	public Long calculateContentId() {
		return id;
	}
}
