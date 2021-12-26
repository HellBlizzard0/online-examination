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
@Table(name = "FIS_LAB_CHECK_REPORT_EMPS")
public class LabCheckReportEmployee extends AuditEntity implements Serializable, UpdateableAuditEntity, InsertableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private Long labCheckReportId;
	private Long employeeId;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_LAB_CHCK_REPORT_EMPS_SEQ", sequenceName = "FIS_LAB_CHCK_REPORT_EMPS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_LAB_CHCK_REPORT_EMPS_SEQ")
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
	@Column(name = "STP_VW_EMPLOYEES_ID")
	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	@Override
	public Long calculateContentId() {
		return id;
	}
}
