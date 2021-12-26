package com.code.dal.orm.securitymission;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.code.dal.orm.BaseEntity;
import com.code.services.util.HijriDateService;

@NamedQueries({
	@NamedQuery(name = "missionGuard_searchMissionGuard",
				query = " select m " + 
						" from MissionGuardData m " +
						" where (:P_ID = -1 or m.id = :P_ID )" +
						" and (:P_INSTANCE_ID = -1 or m.wFInstanceId = :P_INSTANCE_ID )" +
						" and (:P_ORDER_NUMBER = '-1' or m.orderNumber = :P_ORDER_NUMBER) "
				) 
})
@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_SCRTY_GRD_MISSIONS")
public class MissionGuardData extends BaseEntity implements Serializable {
	private Long id;
	private String missionNumber;
	private String orderNumber;
	private Date orderDate;
	private String orderDateString;
	private Long orderSourceDomainId;
	private String orderSourceDomainName;
	private Long missionType;
	private String missionTypeDescription;
	private Integer missionPeriod;
	private Date missionStartDate;
	private String missionStartDateString;
	private Date missionEndDate;
	private String missionEndDateString;
	private Long departmentMissionLocationId;
	private String departmentMissionLocationName;
	private String missionPath;
	private String remarks;
	private Long wFInstanceId;
	private Integer status;
	private Date movementDate;
	private String movementDateString;
	private String movementTime;
	private String missionEndTime;

	private MissionGuard missionGuard;

	public MissionGuardData() {
		setMissionGuard(new MissionGuard());
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return missionGuard.getId();
	}

	public void setId(Long id) {
		this.id = id;
		missionGuard.setId(id);
		;
	}

	@Basic
	@Column(name = "MISSION_NUMBER")
	public String getMissionNumber() {
		return missionNumber;
	}

	public void setMissionNumber(String missionNumber) {
		this.missionNumber = missionNumber;
		missionGuard.setMissionNumber(missionNumber);
	}

	@Basic
	@Column(name = "ORDER_NUMBER")
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
		missionGuard.setOrderNumber(orderNumber);
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ORDER_DATE")
	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDateString = HijriDateService.getHijriDateString(orderDate);
		this.orderDate = orderDate;
		missionGuard.setOrderDate(orderDate);
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
		this.missionGuard.setOrderSourceDomainId(orderSourceDomainId);
		this.orderSourceDomainId = orderSourceDomainId;
	}

	@Basic
	@Column(name = "MISSION_PERIOD")
	public Integer getMissionPeriod() {
		return missionPeriod;
	}

	public void setMissionPeriod(Integer missionPeriod) {
		this.missionPeriod = missionPeriod;
		missionGuard.setMissionPeriod(missionPeriod);
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
		missionGuard.setMissionStartDate(missionStartDate);
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
		missionGuard.setMissionEndDate(missionEndDate);
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
		missionGuard.setDepartmentMissionLocationId(departmentMissionLocationId);
	}

	@Basic
	@Column(name = "MISSION_PATH")
	public String getMissionPath() {
		return missionPath;
	}

	public void setMissionPath(String missionPath) {
		this.missionPath = missionPath;
		missionGuard.setMissionPath(missionPath);
	}

	@Basic
	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
		missionGuard.setRemarks(remarks);
	}

	@Basic
	@Column(name = "WF_INSTANCES_ID")
	public Long getwFInstanceId() {
		return wFInstanceId;
	}

	public void setwFInstanceId(Long wFInstanceId) {
		this.wFInstanceId = wFInstanceId;
		missionGuard.setwFInstanceId(wFInstanceId);
	}

	@Basic
	@Column(name = "MISSION_TYPE")
	public Long getMissionType() {
		return missionType;
	}

	public void setMissionType(Long missionType) {
		this.missionType = missionType;
		missionGuard.setMissionType(missionType);
	}

	@Basic
	@Column(name = "STATUS")
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
		missionGuard.setStatus(status);
	}
	
	@Basic
	@Column(name = "MISSION_TYPE_DESC")
	public String getMissionTypeDescription() {
		return missionTypeDescription;
	}

	public void setMissionTypeDescription(String missionTypeDescription) {
		this.missionTypeDescription = missionTypeDescription;
	}

	@Basic
	@Column(name = "SRC_NAME")
	public String getOrderSourceDomainName() {
		return orderSourceDomainName;
	}

	public void setOrderSourceDomainName(String orderSourceDomainName) {
		this.orderSourceDomainName = orderSourceDomainName;
	}

	@Basic
	@Column(name = "LOC_DEPT")
	public String getDepartmentMissionLocationName() {
		return departmentMissionLocationName;
	}

	public void setDepartmentMissionLocationName(String departmentMissionLocationName) {
		this.departmentMissionLocationName = departmentMissionLocationName;
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
		missionGuard.setMovementDate(movementDate);
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
		missionGuard.setMovementTime(movementTime);
	}

	@Basic
	@Column(name = "MISSION_END_TIME")
	public String getMissionEndTime() {
		return missionEndTime;
	}

	public void setMissionEndTime(String missionEndTime) {
		this.missionEndTime = missionEndTime;
		missionGuard.setMissionEndTime(missionEndTime);
	}

	@Transient
	public MissionGuard getMissionGuard() {
		return missionGuard;
	}

	public void setMissionGuard(MissionGuard missionGuard) {
		this.missionGuard = missionGuard;
	}

}
