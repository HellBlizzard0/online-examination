package com.code.dal.orm.securitymission;

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
@Table(name = "FIS_SECURITY_GUARD_MISSIONS")
public class MissionGuard extends AuditEntity implements Serializable , InsertableAuditEntity,UpdateableAuditEntity {
	private Long id;
	private String missionNumber;
	private String orderNumber;
	private Date orderDate;
	private String orderDateString;
	private Long orderSourceDomainId;
	private Long missionType;
	private Integer missionPeriod;
	private Date missionStartDate;
	private String missionStartDateString;
	private Date missionEndDate;
	private String missionEndDateString;
	private Long departmentMissionLocationId;
	private String missionPath;
	private String remarks;
	private Long wFInstanceId;
	private Integer status;
	private Date movementDate;
	private String movementDateString;
	private String movementTime;
	private String missionEndTime;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_SEC_GUARD_MISSIONS_SEQ", sequenceName = "FIS_SEC_GUARD_MISSIONS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_SEC_GUARD_MISSIONS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "MISSION_NUMBER")
	public String getMissionNumber() {
		return missionNumber;
	}

	public void setMissionNumber(String missionNumber) {
		this.missionNumber = missionNumber;
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
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ORDER_DATE")
	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		if (orderDate != null) {
			this.orderDateString = HijriDateService.getHijriDateString(orderDate);
		}
		this.orderDate = orderDate;
	}

	@Transient
	public String getOrderDateString() {
		return orderDateString;
	}

	@Basic
	@Column(name = "ORDER_SOURCE_DOMAIN_ID")
	public Long getOrderSourceDomainId() {
		return orderSourceDomainId;
	}

	public void setOrderSourceDomainId(Long orderSourceDomainId) {
		this.orderSourceDomainId = orderSourceDomainId;
	}
	
	@Basic
	@Column(name = "MISSION_PERIOD")
	public Integer getMissionPeriod() {
		return missionPeriod;
	}

	public void setMissionPeriod(Integer missionPeriod) {
		this.missionPeriod = missionPeriod;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MISSION_START_DATE")
	public Date getMissionStartDate() {
		return missionStartDate;
	}

	public void setMissionStartDate(Date missionStartDate) {
		this.missionStartDateString = HijriDateService.getHijriDateString(missionStartDate);
		this.missionStartDate = missionStartDate;
	}

	@Transient
	public String getMissionStartDateString() {
		return missionStartDateString;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MISSION_END_DATE")
	public Date getMissionEndDate() {
		return missionEndDate;
	}

	public void setMissionEndDate(Date missionEndDate) {
		this.missionEndDateString = HijriDateService.getHijriDateString(missionEndDate);
		this.missionEndDate = missionEndDate;
	}

	@Transient
	public String getMissionEndDateString() {
		return missionEndDateString;
	}

	@Basic
	@Column(name = "STP_VW_DEPARTMTS_ID_MISION_LOC")
	public Long getDepartmentMissionLocationId() {
		return departmentMissionLocationId;
	}

	public void setDepartmentMissionLocationId(Long departmentMissionLocationId) {
		this.departmentMissionLocationId = departmentMissionLocationId;
	}

	@Basic
	@Column(name = "MISSION_PATH")
	public String getMissionPath() {
		return missionPath;
	}

	public void setMissionPath(String missionPath) {
		this.missionPath = missionPath;
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
	@Column(name = "WF_INSTANCES_ID")
	public Long getwFInstanceId() {
		return wFInstanceId;
	}

	public void setwFInstanceId(Long wFInstanceId) {
		this.wFInstanceId = wFInstanceId;
	}

	@Basic
	@Column(name = "STATUS")
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public Long calculateContentId() {
		return id;
	}

	@Basic
	@Column(name = "MISSION_TYPE")
	public Long getMissionType() {
		return missionType;
	}

	public void setMissionType(Long missionType) {
		this.missionType = missionType;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MOVEMENT_DATE")
	public Date getMovementDate() {
		return movementDate;
	}

	public void setMovementDate(Date movementDate) {
		this.movementDateString = HijriDateService.getHijriDateString(movementDate);
		this.movementDate = movementDate;
	}

	@Transient
	public String getMovementDateString() {
		return movementDateString;
	}

	@Basic
	@Column(name = "MOVEMENT_TIME")
	public String getMovementTime() {
		return movementTime;
	}

	public void setMovementTime(String movementTime) {
		this.movementTime = movementTime;
	}

	@Basic
	@Column(name = "MISSION_END_TIME")
	public String getMissionEndTime() {
		return missionEndTime;
	}

	public void setMissionEndTime(String missionEndTime) {
		this.missionEndTime = missionEndTime;
	}

}
