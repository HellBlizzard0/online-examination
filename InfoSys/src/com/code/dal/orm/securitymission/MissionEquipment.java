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
	 @NamedQuery(name  = "missionEquipment_receiveAllMissionEquipment", 
	       	 	 query = " update MissionEquipment me" +
	       	 			 " set me.receiveDate = PKG_CHAR_TO_DATE (:P_RECEIVE_DATE, 'MI/MM/YYYY')," +
	       	 			 "	   me.receiveTime = :P_RECEIVE_TIME" +
	           	 		 " where me.receiveDate is null" +
	       	 			 " and me.id IN( " + 
	       	 			 "		select med.id " + 
	           	 		 " 		from MissionEquipmentData med" +
	           			 " 		where (:P_EMPLOYEE_ID = -1 or med.employeeId = :P_EMPLOYEE_ID )" +
	           			 " 		and (:P_MISSION_NUMBER = '-1' or med.missionNumber = :P_MISSION_NUMBER ) " +
	           			 " 		and (:P_ORDER_NUMBER = '-1' or med.orderNumber = :P_ORDER_NUMBER ) " +
	   			 		 " 		and (:P_ORDER_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_ORDER_DATE, 'MI/MM/YYYY') = med.orderDate)" + 
	   			 		 " 		and (:P_MISSION_START_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_MISSION_START_DATE, 'MI/MM/YYYY') <= med.missionStartDate)" + 
	   			 		 " 		and (:P_MISSION_END_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_MISSION_END_DATE, 'MI/MM/YYYY') >= med.missionEndDate)" + 
	           			 " 		and (:P_MISSION_TYPE = -1 or med.missionType = :P_MISSION_TYPE )" +  
	               		 " 		and (:P_STATUS = -1 or med.status = :P_STATUS ))"
			 	),
	 @NamedQuery(name  = "missionEquipment_returnAllMissionEquipment", 
	       	 	 query = " update MissionEquipment me" +
	       	 			 " set me.returnDate = PKG_CHAR_TO_DATE (:P_RETURN_DATE, 'MI/MM/YYYY')," +
	       	 			 "	   me.returnTime = :P_RETURN_TIME" +
	           	 		 " where me.returnDate is null" +
	           	 		 " and me.receiveDate is not null" + 
	       	 			 " and me.id IN( " + 
	       	 			 "		select med.id " + 
	           	 		 " 		from MissionEquipmentData med" +
	           			 " 		where (:P_EMPLOYEE_ID = -1 or med.employeeId = :P_EMPLOYEE_ID )" +
	           			 " 		and (:P_MISSION_NUMBER = '-1' or med.missionNumber = :P_MISSION_NUMBER ) " +
	           			 " 		and (:P_ORDER_NUMBER = '-1' or med.orderNumber = :P_ORDER_NUMBER ) " +
	   			 		 " 		and (:P_ORDER_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_ORDER_DATE, 'MI/MM/YYYY') = med.orderDate)" + 
	   			 		 " 		and (:P_MISSION_START_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_MISSION_START_DATE, 'MI/MM/YYYY') <= med.missionStartDate)" + 
	   			 		 " 		and (:P_MISSION_END_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_MISSION_END_DATE, 'MI/MM/YYYY') >= med.missionEndDate)" + 
	           			 " 		and (:P_MISSION_TYPE = -1 or med.missionType = :P_MISSION_TYPE )" + 
	               		 " 		and (:P_STATUS = -1 or med.status = :P_STATUS ))"
			 	)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_MISSION_EQUIPMENTS")
public class MissionEquipment extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private Long securityGuardMissionId;
	private Long employeeId;
	private Long domainEquipmentTypeId;
	private String equipmentModel;
	private Integer manufactureYear;
	private String borderGuardNumber;
	private Date receiveDate;
	private String receiveDateString;
	private String receiveTime;
	private Date returnDate;
	private String returnDateString;
	private String returnTime;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_MISSION_EQUIPMENTS_SEQ", sequenceName = "FIS_MISSION_EQUIPMENTS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_MISSION_EQUIPMENTS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	@Column(name = "STP_VW_EMPLOYEES_ID")
	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	@Basic
	@Column(name = "DOMAINS_ID_EQUIPMENT_TYPE")
	public Long getDomainEquipmentTypeId() {
		return domainEquipmentTypeId;
	}

	public void setDomainEquipmentTypeId(Long domainEquipmentTypeId) {
		this.domainEquipmentTypeId = domainEquipmentTypeId;
	}

	@Basic
	@Column(name = "MANUFACTURE_YEAR")
	public Integer getManufactureYear() {
		return manufactureYear;
	}

	public void setManufactureYear(Integer manufactureYear) {
		this.manufactureYear = manufactureYear;
	}

	@Basic
	@Column(name = "BORDER_GUARD_NUMBER")
	public String getBorderGuardNumber() {
		return borderGuardNumber;
	}

	public void setBorderGuardNumber(String borderGuardNumber) {
		this.borderGuardNumber = borderGuardNumber;
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

	@Basic
	@Column(name = "MODEL")
	public String getEquipmentModel() {
		return equipmentModel;
	}

	public void setEquipmentModel(String equipmentModel) {
		this.equipmentModel = equipmentModel;
	}
}
