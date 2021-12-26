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
	 @NamedQuery(name  = "missionEquipmentData_searchMissionEquipmentData", 
			 	 query = " select me " +
            	 		 " from MissionEquipmentData me" +
            			 " where (:P_MISSION_ID = -1 or me.securityGuardMissionId = :P_MISSION_ID )" +
            			 " and (:P_EMPLOYEE_ID = -1 or me.employeeId = :P_EMPLOYEE_ID )" +
            			 " and (:P_MISSION_NUMBER = '-1' or me.missionNumber = :P_MISSION_NUMBER ) " +
            			 " and (:P_ORDER_NUMBER = '-1' or me.orderNumber = :P_ORDER_NUMBER ) " +
    			 		 " and (:P_ORDER_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_ORDER_DATE, 'MI/MM/YYYY') = me.orderDate)" + 
    			 		 " and (:P_MISSION_START_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_MISSION_START_DATE, 'MI/MM/YYYY') <= me.missionEndDate)" + 
               			 " and (:P_MISSION_END_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_MISSION_END_DATE, 'MI/MM/YYYY') >= me.missionStartDate)" + 
               			 " and (:P_MISSION_TYPE = -1 or me.missionType = :P_MISSION_TYPE )" +  
             			 " and (:P_STATUS = -1 or me.status = :P_STATUS )" + 
            			 " order by me.id "
			 	)
})
@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_MISSION_EQPMNTS")
public class MissionEquipmentData extends BaseEntity implements Serializable {
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

	private Long domainEquipmentTypeId;
	private String domainEquipmentTypeDescription;
	private Integer manufactureYear;
	private String borderGuardNumber;
	private Date receiveDate;
	private String receiveDateString;
	private String receiveTime;
	private Date returnDate;
	private String returnDateString;
	private String returnTime;
	private String equipmentModel;
	private Integer status;

	private MissionEquipment missionEquipment;

	public MissionEquipmentData() {
		missionEquipment = new MissionEquipment();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return missionEquipment.getId();
	}

	public void setId(Long id) {
		this.id = id;
		missionEquipment.setId(id);
	}

	@Basic
	@Column(name = "SECURITY_GUARD_MISSIONS_ID")
	public Long getSecurityGuardMissionId() {
		return securityGuardMissionId;
	}

	public void setSecurityGuardMissionId(Long securityGuardMissionId) {
		this.securityGuardMissionId = securityGuardMissionId;
		missionEquipment.setSecurityGuardMissionId(securityGuardMissionId);
	}

	@Basic
	@Column(name = "STP_VW_EMPLOYEES_ID")
	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
		missionEquipment.setEmployeeId(employeeId);
	}

	@Basic
	@Column(name = "DOMAINS_ID_EQUIPMENT_TYPE")
	public Long getDomainEquipmentTypeId() {
		return domainEquipmentTypeId;
	}

	public void setDomainEquipmentTypeId(Long domainEquipmentTypeId) {
		this.domainEquipmentTypeId = domainEquipmentTypeId;
		missionEquipment.setDomainEquipmentTypeId(domainEquipmentTypeId);
	}

	@Basic
	@Column(name = "MANUFACTURE_YEAR")
	public Integer getManufactureYear() {
		return manufactureYear;
	}

	public void setManufactureYear(Integer manufactureYear) {
		this.manufactureYear = manufactureYear;
		missionEquipment.setManufactureYear(manufactureYear);
	}

	@Basic
	@Column(name = "BORDER_GUARD_NUMBER")
	public String getBorderGuardNumber() {
		return borderGuardNumber;
	}

	public void setBorderGuardNumber(String borderGuardNumber) {
		this.borderGuardNumber = borderGuardNumber;
		missionEquipment.setBorderGuardNumber(borderGuardNumber);
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
		missionEquipment.setReceiveDate(receiveDate);
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
		this.missionEquipment.setReceiveTime(receiveTime);
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
		missionEquipment.setReturnDate(returnDate);
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
		this.missionEquipment.setReturnTime(returnTime);
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
		this.missionStartDate = missionStartDate;
		this.missionStartDateString=HijriDateService.getHijriDateString(missionStartDate);
	}

	@Transient
	public String getMissionStartDateString() {
		return missionStartDateString;
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
	public MissionEquipment getMissionEquipment() {
		return missionEquipment;
	}

	public void setMissionEquipment(MissionEquipment missionEquipment) {
		this.missionEquipment = missionEquipment;
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
	@Column(name = "MODEL")
	public String getEquipmentModel() {
		return equipmentModel;
	}

	public void setEquipmentModel(String equipmentModel) {
		this.equipmentModel = equipmentModel;
		missionEquipment.setEquipmentModel(equipmentModel);
	}

	@Basic
	@Column(name = "EQUIPMENT_TYPE_DESCRIPTION")
	public String getDomainEquipmentTypeDescription() {
		return domainEquipmentTypeDescription;
	}

	public void setDomainEquipmentTypeDescription(String domainEquipmentTypeDescription) {
		this.domainEquipmentTypeDescription = domainEquipmentTypeDescription;
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
