package com.code.dal.orm.securitymission;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.code.dal.audit.DeleteableAuditEntity;
import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;
import com.code.services.util.HijriDateService;

@NamedQueries({
	 @NamedQuery(name  = "missionWeapon_receiveAllMissionWeapon", 
	       	 	 query = " update MissionWeapon mw" +
	       	 			 " set mw.receiveDate = PKG_CHAR_TO_DATE (:P_RECEIVE_DATE, 'MI/MM/YYYY')," +
	       	 			 "	   mw.receiveTime = :P_RECEIVE_TIME" +
	       	 			 " where mw.receiveDate is null" +
	           	 		 " and mw.id IN( " + 
	       	 			 "		select mwd.id " + 
	           	 		 " 		from MissionWeaponData mwd" +
	           			 " 		where (:P_EMPLOYEE_ID = -1 or mwd.employeeId = :P_EMPLOYEE_ID )" +
	           			 " 		and (:P_MISSION_NUMBER = '-1' or mwd.missionNumber = :P_MISSION_NUMBER ) " +
	           			 " 		and (:P_ORDER_NUMBER = '-1' or mwd.orderNumber = :P_ORDER_NUMBER ) " +
	   			 		 " 		and (:P_ORDER_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_ORDER_DATE, 'MI/MM/YYYY') = mwd.orderDate)" + 
	   			 		 " 		and (:P_MISSION_START_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_MISSION_START_DATE, 'MI/MM/YYYY') <= mwd.missionStartDate)" + 
	   			 		 " 		and (:P_MISSION_END_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_MISSION_END_DATE, 'MI/MM/YYYY') >= mwd.missionEndDate)" + 
	           			 " 		and (:P_MISSION_TYPE = -1 or mwd.missionType = :P_MISSION_TYPE )" + 
	               		 " 		and (:P_STATUS = -1 or mwd.status = :P_STATUS ))"
			 	),
	 @NamedQuery(name  = "missionWeapon_returnAllMissionWeapon", 
	       	 	 query = " update MissionWeapon mw" +
	       	 			 " set mw.returnDate = PKG_CHAR_TO_DATE (:P_RETURN_DATE, 'MI/MM/YYYY')," +
	       	 			 "	   mw.returnTime = :P_RETURN_TIME" +
	           	 		 " where mw.returnDate is null" +
	       	 			 " and mw.receiveDate is not null" + 
	           	 		 " and mw.id IN( " + 
	       	 			 "		select mwd.id " + 
	           	 		 " 		from MissionWeaponData mwd" +
	           			 " 		where (:P_EMPLOYEE_ID = -1 or mwd.employeeId = :P_EMPLOYEE_ID )" +
	           			 " 		and (:P_MISSION_NUMBER = '-1' or mwd.missionNumber = :P_MISSION_NUMBER ) " +
	           			 " 		and (:P_ORDER_NUMBER = '-1' or mwd.orderNumber = :P_ORDER_NUMBER ) " +
	   			 		 " 		and (:P_ORDER_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_ORDER_DATE, 'MI/MM/YYYY') = mwd.orderDate)" + 
	   			 		 " 		and (:P_MISSION_START_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_MISSION_START_DATE, 'MI/MM/YYYY') <= mwd.missionStartDate)" + 
	   			 		 " 		and (:P_MISSION_END_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_MISSION_END_DATE, 'MI/MM/YYYY') >= mwd.missionEndDate)" + 
	           			 " 		and (:P_MISSION_TYPE = -1 or mwd.missionType = :P_MISSION_TYPE )" +
	               		 " 		and (:P_STATUS = -1 or mwd.status = :P_STATUS ))"
			 	)
})
@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_MISSION_WEAPONS")
public class MissionWeapon extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private Long employeeId;
	private Long securityGuardMissionId;
	private Long domainWeaponTypeId;
	private String weaponNumber;
	private Integer ammoAmount;
	private Date receiveDate;
	private String receiveDateString;
	private String receiveTime;
	private Date returnDate;
	private String returnDateString;
	private String returnTime;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_MISSION_WEAPONS_SEQ", sequenceName = "FIS_MISSION_WEAPONS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_MISSION_WEAPONS_SEQ")
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
	@Column(name = "SECURITY_GUARD_MISSIONS_ID")
	public Long getSecurityGuardMissionId() {
		return securityGuardMissionId;
	}

	public void setSecurityGuardMissionId(Long securityGuardMissionId) {
		this.securityGuardMissionId = securityGuardMissionId;
	}

	@Basic
	@Column(name = "DOMAINS_ID_WEAPON_TYPE")
	public Long getDomainWeaponTypeId() {
		return domainWeaponTypeId;
	}

	public void setDomainWeaponTypeId(Long domainWeaponTypeId) {
		this.domainWeaponTypeId = domainWeaponTypeId;
	}

	@Basic
	@Column(name = "WEAPON_NUMBER")
	public String getWeaponNumber() {
		return weaponNumber;
	}

	public void setWeaponNumber(String weaponNumber) {
		this.weaponNumber = weaponNumber;
	}

	@Basic
	@Column(name = "AMMO_AMOUNT")
	public Integer getAmmoAmount() {
		return ammoAmount;
	}

	public void setAmmoAmount(Integer ammoAmount) {
		this.ammoAmount = ammoAmount;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RECEIVE_DATE")
	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		if (receiveDate != null) {
			this.receiveDateString = HijriDateService.getHijriDateString(receiveDate);
		}
		this.receiveDate = receiveDate;
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
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RETURN_DATE")
	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		if (returnDate != null) {
			this.returnDateString = HijriDateService.getHijriDateString(returnDate);
		}
		this.returnDate = returnDate;
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
	}

	@Override
	public Long calculateContentId() {
		return id;
	}
}
