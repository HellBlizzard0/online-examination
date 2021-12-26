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
	 @NamedQuery(name  = "missionCar_receiveAllMissionCar", 
	       	 	 query = " update MissionCar mc" +
	       	 			 " set mc.receiveDate = PKG_CHAR_TO_DATE (:P_RECEIVE_DATE, 'MI/MM/YYYY')," +
	       	 			 "	   mc.receiveTime = :P_RECEIVE_TIME" +
	           	 		 " where mc.receiveDate is null" +
	       	 			 " and mc.id IN( " + 
	       	 			 "		select mcd.id " + 
	           	 		 " 		from MissionCarData mcd" +
	           			 " 		where (:P_EMPLOYEE_ID = -1 or mcd.employeeId = :P_EMPLOYEE_ID )" +
	           			 " 		and (:P_MISSION_NUMBER = '-1' or mcd.missionNumber = :P_MISSION_NUMBER ) " +
	           			 " 		and (:P_ORDER_NUMBER = '-1' or mcd.orderNumber = :P_ORDER_NUMBER ) " +
	   			 		 " 		and (:P_ORDER_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_ORDER_DATE, 'MI/MM/YYYY') = mcd.orderDate)" + 
	   			 		 " 		and (:P_MISSION_START_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_MISSION_START_DATE, 'MI/MM/YYYY') <= mcd.missionStartDate)" + 
	   			 		 " 		and (:P_MISSION_END_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_MISSION_END_DATE, 'MI/MM/YYYY') >= mcd.missionEndDate)" + 
	           			 " 		and (:P_MISSION_TYPE = -1 or mcd.missionType = :P_MISSION_TYPE )" +
	               		 " 		and (:P_STATUS = -1 or mcd.status = :P_STATUS ))"
			 	),
	 @NamedQuery(name  = "missionCar_returnAllMissionCar", 
	       	 	 query = " update MissionCar mc" +
	       	 			 " set mc.returnDate = PKG_CHAR_TO_DATE (:P_RETURN_DATE, 'MI/MM/YYYY')," +
	       	 			 "	   mc.returnTime = :P_RETURN_TIME" +
	           	 		 " where mc.returnDate is null" +
	           	 		 " and mc.receiveDate is not null" + 
	       	 			 " and mc.id IN( " + 
	       	 			 "		select mcd.id " + 
	           	 		 " 		from MissionCarData mcd" +
	           			 " 		where (:P_EMPLOYEE_ID = -1 or mcd.employeeId = :P_EMPLOYEE_ID )" +
	           			 " 		and (:P_MISSION_NUMBER = '-1' or mcd.missionNumber = :P_MISSION_NUMBER ) " +
	           			 " 		and (:P_ORDER_NUMBER = '-1' or mcd.orderNumber = :P_ORDER_NUMBER ) " +
	   			 		 " 		and (:P_ORDER_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_ORDER_DATE, 'MI/MM/YYYY') = mcd.orderDate)" + 
	   			 		 " 		and (:P_MISSION_START_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_MISSION_START_DATE, 'MI/MM/YYYY') <= mcd.missionStartDate)" + 
	   			 		 " 		and (:P_MISSION_END_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_MISSION_END_DATE, 'MI/MM/YYYY') >= mcd.missionEndDate)" + 
	           			 " 		and (:P_MISSION_TYPE = -1 or mcd.missionType = :P_MISSION_TYPE )" + 
	               		 " 		and (:P_STATUS = -1 or mcd.status = :P_STATUS ))"
			 	)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_MISSION_CARS")
public class MissionCar extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private Long securityGuardMissionId;
	private Long employeeId;
	private Long domainCarModelId;
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

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_MISSION_CARS_SEQ", sequenceName = "FIS_MISSION_CARS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_MISSION_CARS_SEQ")
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
	@Column(name = "DOMAINS_ID_CAR_MODEL")
	public Long getDomainCarModelId() {
		return domainCarModelId;
	}

	public void setDomainCarModelId(Long domainCarModelId) {
		this.domainCarModelId = domainCarModelId;
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
	@Column(name = "PLATE_NUMBER")
	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
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

	@Basic
	@Column(name = "CAR_MODEL")
	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	@Override
	public Long calculateContentId() {
		return id;
	}
}
