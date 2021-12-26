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
	 @NamedQuery(name  = "missionWeaponData_searchMissionWeaponData", 
        	 	 query = " select mw " +
            	 		 " from MissionWeaponData mw" +
            			 " where (:P_MISSION_ID = -1 or mw.securityGuardMissionId = :P_MISSION_ID )" +
            			 " and (:P_EMPLOYEE_ID = -1 or mw.employeeId = :P_EMPLOYEE_ID )" +
            			 " and (:P_MISSION_NUMBER = '-1' or mw.missionNumber = :P_MISSION_NUMBER ) " +
            			 " and (:P_ORDER_NUMBER = '-1' or mw.orderNumber = :P_ORDER_NUMBER ) " +
    			 		 " and (:P_ORDER_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_ORDER_DATE, 'MI/MM/YYYY') = mw.orderDate)" + 
    			 		 " and (:P_MISSION_START_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_MISSION_START_DATE, 'MI/MM/YYYY') <= mw.missionEndDate)" + 
               			 " and (:P_MISSION_END_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_MISSION_END_DATE, 'MI/MM/YYYY') >= mw.missionStartDate)" + 
               			 " and (:P_MISSION_TYPE = -1 or mw.missionType = :P_MISSION_TYPE )" +
                		 " and (:P_STATUS = -1 or mw.status = :P_STATUS )" +
            			 " order by mw.id "
			 	)
 })
@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_MISSION_WPNS")
public class MissionWeaponData extends BaseEntity implements Serializable {
	private Long id;
	private Long employeeId;
	private String employeeRank;
	private String employeeSocialId;
	private String employeeName;
	private String employeeMilitaryNumber;
	private Long securityGuardMissionId;
	private String missionNumber;
	private Date missionStartDate;
	private Date missionEndDate;
	private String missionEndDateString;
	private String missionStartDateString;
	private Long missionType;
	private Integer missionPeriod;
	private String orderNumber;
	private Date orderDate;
	private String orderDateString;
	private Long domainWeaponTypeId;
	private String domainWeaponTypeDescription;
	private String weaponNumber;
	private Integer ammoAmount;
	private Date receiveDate;
	private String receiveDateString;
	private String receiveTime;
	private Date returnDate;
	private String returnDateString;
	private String returnTime;
	private Integer status;
	private MissionWeapon missionWeapon;

	public MissionWeaponData(){
		missionWeapon = new MissionWeapon();
	}
	@Id
	@Column(name = "ID")
	public Long getId() {
		return missionWeapon.getId();
	}

	public void setId(Long id) {
		this.id = id;
		missionWeapon.setId(id);
	}

	@Basic
	@Column(name = "STP_VW_EMPLOYEES_ID")
	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
		missionWeapon.setEmployeeId(employeeId);
	}

	@Basic
	@Column(name = "SECURITY_GUARD_MISSIONS_ID")
	public Long getSecurityGuardMissionId() {
		return securityGuardMissionId;
	}

	public void setSecurityGuardMissionId(Long securityGuardMissionId) {
		this.securityGuardMissionId = securityGuardMissionId;
		missionWeapon.setSecurityGuardMissionId(securityGuardMissionId);
	}

	@Basic
	@Column(name = "DOMAINS_ID_WEAPON_TYPE")
	public Long getDomainWeaponTypeId() {
		return domainWeaponTypeId;
	}

	public void setDomainWeaponTypeId(Long domainWeaponTypeId) {
		this.domainWeaponTypeId = domainWeaponTypeId;
		missionWeapon.setDomainWeaponTypeId(domainWeaponTypeId);
	}

	@Basic
	@Column(name = "WEAPON_NUMBER")
	public String getWeaponNumber() {
		return weaponNumber;
	}

	public void setWeaponNumber(String weaponNumber) {
		this.weaponNumber = weaponNumber;
		missionWeapon.setWeaponNumber(weaponNumber);
	}

	@Basic
	@Column(name = "AMMO_AMOUNT")
	public Integer getAmmoAmount() {
		return ammoAmount;
	}

	public void setAmmoAmount(Integer ammoAmount) {
		this.ammoAmount = ammoAmount;
		missionWeapon.setAmmoAmount(ammoAmount);
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
		missionWeapon.setReceiveDate(receiveDate);
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
		this.missionWeapon.setReceiveTime(receiveTime);
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
		missionWeapon.setReturnDate(returnDate);
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
		this.missionWeapon.setReturnTime(returnTime);
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
	public MissionWeapon getMissionWeapon() {
		return missionWeapon;
	}

	public void setMissionWeapon(MissionWeapon missionWeapon) {
		this.missionWeapon = missionWeapon;
	}
	
	@Basic
	@Column(name = "MISSION_END_DATE")
	public Date getMissionEndDate() {
		return missionEndDate;
	}

	public void setMissionEndDate(Date missionEndDate) {
		this.missionEndDate = missionEndDate;
		this.missionEndDateString= HijriDateService.getHijriDateString(missionEndDate);
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
	@Column(name = "WEAPON_TYPE_DESC")
	public String getDomainWeaponTypeDescription() {
		return domainWeaponTypeDescription;
	}
	public void setDomainWeaponTypeDescription(String domainWeaponTypeDescription) {
		this.domainWeaponTypeDescription = domainWeaponTypeDescription;
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
