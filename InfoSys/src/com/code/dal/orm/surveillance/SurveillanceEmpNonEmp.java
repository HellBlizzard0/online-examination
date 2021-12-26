package com.code.dal.orm.surveillance;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;
import com.code.services.util.HijriDateService;

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_SURVEILLANCE_EMP_NON_EMP")
public class SurveillanceEmpNonEmp extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity {
	private Long id;
	private Long employeeId;
	private Long nonEmployeeId;
	private Long surveillanceOrderId;
	private Integer periodMonths;
	private Date endDate;
	private String endDateString;
	private String surveillanceReasons;
	private Integer yaqeenStatus;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_SURVEILLANCE_EMPLOYEES_SEQ", sequenceName = "FIS_SURVEILLANCE_EMPLOYEES_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_SURVEILLANCE_EMPLOYEES_SEQ")
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
	@Column(name = "FIS_SURVEILLANCE_ORDERS_ID")
	public Long getSurveillanceOrderId() {
		return surveillanceOrderId;
	}

	public void setSurveillanceOrderId(Long surveillanceOrderId) {
		this.surveillanceOrderId = surveillanceOrderId;
	}

	@Basic
	@Column(name = "PERIOD_MONTHS")
	public Integer getPeriodMonths() {
		return periodMonths;
	}

	public void setPeriodMonths(Integer periodMonths) {
		this.periodMonths = periodMonths;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_DATE")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDateString = HijriDateService.getHijriDateString(endDate);
		this.endDate = endDate;
	}

	@Transient
	public String getEndDateString() {
		return endDateString;
	}

	@Basic
	@Column(name = "SURVEILLANCE_REASONS")
	public String getSurveillanceReasons() {
		return surveillanceReasons;
	}

	public void setSurveillanceReasons(String surveillanceReasons) {
		this.surveillanceReasons = surveillanceReasons;
	}

	@Basic
	@Column(name = "YAQEEN_STATUS")
	public Integer getYaqeenStatus() {
		return yaqeenStatus;
	}

	public void setYaqeenStatus(Integer yaqeenStatus) {
		this.yaqeenStatus = yaqeenStatus;
	}

	@Basic
	@Column(name = "NON_EMPLOYEES_ID")
	public Long getNonEmployeeId() {
		return nonEmployeeId;
	}

	public void setNonEmployeeId(Long nonEmployeeId) {
		this.nonEmployeeId = nonEmployeeId;
	}
	
	@Override
	public Long calculateContentId() {
		return id;
	}
}