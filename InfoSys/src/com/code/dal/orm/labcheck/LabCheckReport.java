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
@Table(name = "FIS_LAB_CHECK_REPORTS")
public class LabCheckReport extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private String orderNumber;
	private Long labChecksId;
	private Long destinationDepartmentId;
	private Date startDate;
	private String startDateString;
	private Date endDate;
	private String endDateString;
	private Integer period;
	private String remarks;
	private Long missionTypeId;
	private Integer checkedNumber;
	private Date checkStartDate;
	private Date checkEndDate;
	private String checkStartDateString;
	private String checkEndDateString;
	private String pros;
	private String cons;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_LAB_CHECK_REPORTS_SEQ", sequenceName = "FIS_LAB_CHECK_REPORTS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_LAB_CHECK_REPORTS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "ORDER_NUMBER")
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	@Basic
	@Column(name = "LAB_CHECKS_ID")
	public Long getLabChecksId() {
		return labChecksId;
	}

	public void setLabChecksId(Long labChecksId) {
		this.labChecksId = labChecksId;
	}

	@Basic
	@Column(name = "STP_VW_DEPARTMENTS_ID_DST")
	public Long getDestinationDepartmentId() {
		return destinationDepartmentId;
	}

	public void setDestinationDepartmentId(Long destinationDepartmentId) {
		this.destinationDepartmentId = destinationDepartmentId;
	}

	@Basic
	@Column(name = "START_DATE")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
		this.startDateString = HijriDateService.getHijriDateString(startDate);
	}

	@Transient
	public String getStartDateString() {
		return startDateString;
	}

	@Basic
	@Column(name = "END_DATE")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
		this.endDateString = HijriDateService.getHijriDateString(endDate);
	}

	@Transient
	public String getEndDateString() {
		return endDateString;
	}

	@Basic
	@Column(name = "PERIOD")
	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	@Basic
	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Basic
	@Column(name = "MISSION_TYPE")
	public Long getMissionTypeId() {
		return missionTypeId;
	}

	public void setMissionTypeId(Long missionTypeId) {
		this.missionTypeId = missionTypeId;
	}

	@Basic
	@Column(name = "CHECKED_NUMBER")
	public Integer getCheckedNumber() {
		return checkedNumber;
	}

	public void setCheckedNumber(Integer checkedNumber) {
		this.checkedNumber = checkedNumber;
	}

	@Basic
	@Column(name = "CHECK_START_DATE")
	public Date getCheckStartDate() {
		return checkStartDate;
	}

	public void setCheckStartDate(Date checkStartDate) {
		this.checkStartDate = checkStartDate;
		this.startDateString = HijriDateService.getHijriDateString(checkStartDate);
	}

	@Transient
	public String getCheckStartDateString() {
		return checkStartDateString;
	}

	@Basic
	@Column(name = "CHECK_END_DATE")
	public Date getCheckEndDate() {
		return checkEndDate;
	}

	public void setCheckEndDate(Date checkEndDate) {
		this.checkEndDate = checkEndDate;
		this.checkEndDateString = HijriDateService.getHijriDateString(checkEndDate);
	}

	@Transient
	public String getCheckEndDateString() {
		return checkEndDateString;
	}

	@Basic
	@Column(name = "PROS")
	public String getPros() {
		return pros;
	}

	public void setPros(String pros) {
		this.pros = pros;
	}

	@Basic
	@Column(name = "CONS")
	public String getCons() {
		return cons;
	}

	public void setCons(String cons) {
		this.cons = cons;
	}

	@Override
	public Long calculateContentId() {
		return id;
	}
}
