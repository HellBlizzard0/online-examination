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
	 @NamedQuery(name  = "missionCarData_searchMissionCarData", 
			 	query = " select mc " +
               	 		" from MissionCarData mc" +
               	 		" where (:P_MISSION_ID = -1 or mc.securityGuardMissionId = :P_MISSION_ID )" +
               			" and (:P_EMPLOYEE_ID = -1 or mc.employeeId = :P_EMPLOYEE_ID )" +
               			" and (:P_MISSION_NUMBER = '-1' or mc.missionNumber = :P_MISSION_NUMBER ) " +
               			" and (:P_ORDER_NUMBER = '-1' or mc.orderNumber = :P_ORDER_NUMBER ) " +
               			" and (:P_ORDER_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_ORDER_DATE, 'MI/MM/YYYY') = mc.orderDate)" + 
               			" and (:P_MISSION_START_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_MISSION_START_DATE, 'MI/MM/YYYY') <= mc.missionEndDate)" + 
               			" and (:P_MISSION_END_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_MISSION_END_DATE, 'MI/MM/YYYY') >= mc.missionStartDate)" + 
               			" and (:P_MISSION_TYPE = -1 or mc.missionType = :P_MISSION_TYPE )" +  
               			" and (:P_STATUS = -1 or mc.status = :P_STATUS) " +
               			" order by mc.id "
			 	),
	 @NamedQuery(name  = "missionCarData_countMissionCarData", 
	 			query = " select count(mc.id) " +
	 					" from MissionCarData mc " +
	 					" where (:P_MISSION_GUARD_ID = -1 or mc.securityGuardMissionId = :P_MISSION_GUARD_ID)"
	 	)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_MISSION_CARS")
public class MissionCarData extends BaseEntity implements Serializable {
	private Long id;
	private Long securityGuardMissionId;
	private String missionNumber;
	private Date missionStartDate;
	private String missionStartDateString;
	private Date missionEndDate;
	private String missionEndDateString;
	private Long missionType;
	private Integer missionPeriod;
	private String orderNumber;
	private Date orderDate;
	private String orderDateString;

	private Long employeeId;
	private String employeeRank;
	private String employeeSocialId;
	private String employeeName;
	private String employeeMilitaryNumber;

	private Long domainCarModelId;
	private String domainCarModelDescription;
	private String carModel;
	private Integer manufactureYear;
	private String plateNumber;
	private String borderGuardNumber;
	private Date receiveDate;
	private String receiveDateString;
	private String receiveTime;
	private Date returnDate;
	private String returnDateString;
	private String returnTime;
	private Integer status;

	private MissionCar missionCar;

	public MissionCarData() {
		missionCar = new MissionCar();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return missionCar.getId();
	}

	public void setId(Long id) {
		this.id = id;
		this.missionCar.setId(id);
	}

	@Basic
	@Column(name = "SECURITY_GUARD_MISSIONS_ID")
	public Long getSecurityGuardMissionId() {
		return securityGuardMissionId;
	}

	public void setSecurityGuardMissionId(Long securityGuardMissionId) {
		this.securityGuardMissionId = securityGuardMissionId;
		this.missionCar.setSecurityGuardMissionId(securityGuardMissionId);
	}

	@Basic
	@Column(name = "STP_VW_EMPLOYEES_ID")
	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
		this.missionCar.setEmployeeId(employeeId);
	}

	@Basic
	@Column(name = "DOMAINS_ID_CAR_MODEL")
	public Long getDomainCarModelId() {
		return domainCarModelId;
	}

	public void setDomainCarModelId(Long domainCarModelId) {
		this.domainCarModelId = domainCarModelId;
		this.missionCar.setDomainCarModelId(domainCarModelId);
	}

	@Basic
	@Column(name = "MANUFACTURE_YEAR")
	public Integer getManufactureYear() {
		return manufactureYear;
	}

	public void setManufactureYear(Integer manufactureYear) {
		this.manufactureYear = manufactureYear;
		this.missionCar.setManufactureYear(manufactureYear);
	}

	@Basic
	@Column(name = "PLATE_NUMBER")
	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
		this.missionCar.setPlateNumber(plateNumber);
	}

	@Basic
	@Column(name = "BORDER_GUARD_NUMBER")
	public String getBorderGuardNumber() {
		return borderGuardNumber;
	}

	public void setBorderGuardNumber(String borderGuardNumber) {
		this.borderGuardNumber = borderGuardNumber;
		this.missionCar.setBorderGuardNumber(borderGuardNumber);
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RECEIVE_DATE")
	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDateString = HijriDateService.getHijriDateString(receiveDate);
		this.receiveDate = receiveDate;
		this.missionCar.setReceiveDate(receiveDate);
	}

	@Transient
	public String getReceiveDateString() {
		return receiveDateString;
	}

	@Basic
	@Column(name = "RECEIVE_TIME")
	public String getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
		this.missionCar.setReceiveTime(receiveTime);
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RETURN_DATE")
	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDateString = HijriDateService.getHijriDateString(returnDate);
		this.returnDate = returnDate;
		this.missionCar.setReturnDate(returnDate);
	}

	@Transient
	public String getReturnDateString() {
		return returnDateString;
	}

	@Basic
	@Column(name = "RETURN_TIME")
	public String getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(String returnTime) {
		this.returnTime = returnTime;
		this.missionCar.setReturnTime(returnTime);
	}

	@Basic
	@Column(name = "RANK")
	public String getEmployeeRank() {
		return employeeRank;
	}

	public void setEmployeeRank(String employeeRank) {
		this.employeeRank = employeeRank;
	}

	@Basic
	@Column(name = "SOCIAL_ID")
	public String getEmployeeSocialId() {
		return employeeSocialId;
	}

	public void setEmployeeSocialId(String employeeSocialId) {
		this.employeeSocialId = employeeSocialId;
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
	}

	@Transient
	public String getOrderDateString() {
		return orderDateString;
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
	@Column(name = "MISSION_NUMBER")
	public String getMissionNumber() {
		return missionNumber;
	}

	public void setMissionNumber(String missionNumber) {
		this.missionNumber = missionNumber;
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
	@Column(name = "MISSION_TYPE")
	public Long getMissionType() {
		return missionType;
	}

	public void setMissionType(Long missionType) {
		this.missionType = missionType;
	}

	@Basic
	@Column(name = "MISSION_PERIOD")
	public Integer getMissionPeriod() {
		return missionPeriod;
	}

	public void setMissionPeriod(Integer missionPeriod) {
		this.missionPeriod = missionPeriod;
	}

	@Transient
	public MissionCar getMissionCar() {
		return missionCar;
	}

	public void setMissionCar(MissionCar missionCar) {
		this.missionCar = missionCar;
	}

	@Basic
	@Column(name = "MISSION_END_DATE")
	public Date getMissionEndDate() {
		return missionEndDate;
	}

	public void setMissionEndDate(Date missionEndDate) {
		this.missionEndDate = missionEndDate;
		this.missionEndDateString = HijriDateService.getHijriDateString(missionEndDate);
	}

	@Transient
	public String getMissionEndDateString() {
		return missionEndDateString;
	}

	@Basic
	@Column(name = "EMPLOYEE_NAME")
	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	@Basic
	@Column(name = "MILITARY_NO")
	public String getEmployeeMilitaryNumber() {
		return employeeMilitaryNumber;
	}

	public void setEmployeeMilitaryNumber(String employeeMilitaryNumber) {
		this.employeeMilitaryNumber = employeeMilitaryNumber;
	}

	@Basic
	@Column(name = "CAR_MODEL")
	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
		missionCar.setCarModel(carModel);
	}

	@Basic
	@Column(name = "CAR_MODEL_DESCRIPTION")
	public String getDomainCarModelDescription() {
		return domainCarModelDescription;
	}

	public void setDomainCarModelDescription(String domainCarModelDescription) {
		this.domainCarModelDescription = domainCarModelDescription;
	}
	
	@Basic
	@Column(name = "STATUS")
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
}
