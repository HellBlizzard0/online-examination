package com.code.dal.orm.carrental;

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

import com.code.dal.audit.DeleteableAuditEntity;
import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;
import com.code.services.util.HijriDateService;

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_CARS_RENTALS")
public class CarRental extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private String orderNumber;
	private Date orderDate;
	private String orderDateString;
	private Long departmentId;
	private Long employeeId;
	private String infoNumber;
	private String missionDescription;
	private String missionConclusion;
	private Long domainSubRentalCompanyId;
	private String contractNumber;
	private Date rentStartDate;
	private String rentStartDateString;
	private Date rentEndDate;
	private String rentEndDateString;
	private Integer rentPeriod;
	private Float dailyAmount;
	private Float extraAmount;
	private String carPlateNumber;
	private String plateText1;
	private String plateText2;
	private String plateText3;
	private String plateNumber;
	private Long domainCarModelId;
	private Integer paid;
	private String chequeNumber;
	private Date chequeDate;
	private String chequeDateString;
	private Float chequeAmount;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_CARS_RENTALS_SEQ", sequenceName = "FIS_CARS_RENTALS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_CARS_RENTALS_SEQ")
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
	@Column(name = "STP_VW_DEPARTMENTS_ID")
	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
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
	@Column(name = "INFO_NUMBER")
	public String getInfoNumber() {
		return infoNumber;
	}

	public void setInfoNumber(String infoNumber) {
		this.infoNumber = infoNumber;
	}

	@Basic
	@Column(name = "MISSION_DESCRIPTION")
	public String getMissionDescription() {
		return missionDescription;
	}

	public void setMissionDescription(String missionDescription) {
		this.missionDescription = missionDescription;
	}

	@Basic
	@Column(name = "MISSION_CONCLUSION")
	public String getMissionConclusion() {
		return missionConclusion;
	}

	public void setMissionConclusion(String missionConclusion) {
		this.missionConclusion = missionConclusion;
	}

	@Basic
	@Column(name = "DOMAINS_ID_SUB_RENTAL_COMPANY")
	public Long getDomainSubRentalCompanyId() {
		return domainSubRentalCompanyId;
	}

	public void setDomainSubRentalCompanyId(Long domainSubRentalCompanyId) {
		this.domainSubRentalCompanyId = domainSubRentalCompanyId;
	}

	@Basic
	@Column(name = "CONTRACT_NUMBER")
	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RENT_START_DATE")
	public Date getRentStartDate() {
		return rentStartDate;
	}

	public void setRentStartDate(Date rentStartDate) {
		this.rentStartDateString = HijriDateService.getHijriDateString(rentStartDate);
		this.rentStartDate = rentStartDate;
	}

	@Transient
	public String getRentStartDateString() {
		return rentStartDateString;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RENT_END_DATE")
	public Date getRentEndDate() {
		return rentEndDate;
	}

	public void setRentEndDate(Date rentEndDate) {
		this.rentEndDateString = HijriDateService.getHijriDateString(rentEndDate);
		this.rentEndDate = rentEndDate;
	}

	@Transient
	public String getRentEndDateString() {
		return rentEndDateString;
	}

	@Basic
	@Column(name = "RENT_PERIOD")
	public Integer getRentPeriod() {
		return rentPeriod;
	}

	public void setRentPeriod(Integer rentPeriod) {
		this.rentPeriod = rentPeriod;
	}

	@Basic
	@Column(name = "DAILY_AMOUNT")
	public Float getDailyAmount() {
		return dailyAmount;
	}

	public void setDailyAmount(Float dailyAmount) {
		this.dailyAmount = dailyAmount;
	}

	@Basic
	@Column(name = "EXTRA_AMOUNT")
	public Float getExtraAmount() {
		return extraAmount;
	}

	public void setExtraAmount(Float extraAmount) {
		this.extraAmount = extraAmount;
	}

	@Basic
	@Column(name = "CAR_PLATE_NUMBER")
	public String getCarPlateNumber() {
		return carPlateNumber;
	}

	public void setCarPlateNumber(String carPlateNumber) {
		this.carPlateNumber = carPlateNumber;
		this.plateText1 = carPlateNumber.substring(0, 1);
		this.plateText2 = carPlateNumber.substring(2, 3);
		this.plateText3 = carPlateNumber.substring(4, 5);
		this.plateNumber = carPlateNumber.substring(6);
	}
	
	@Transient
	public String getPlateText1() {
		return plateText1;
	}

	@Transient
	public String getPlateText2() {
		return plateText2;
	}

	@Transient
	public String getPlateText3() {
		return plateText3;
	}

	@Transient
	public String getPlateNumber() {
		return plateNumber;
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
	@Column(name = "PAID")
	public Integer getPaid() {
		return paid;
	}

	public void setPaid(Integer paid) {
		this.paid = paid;
	}

	@Basic
	@Column(name = "CHEQUE_NUMBER")
	public String getChequeNumber() {
		return chequeNumber;
	}

	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHEQUE_DATE")
	public Date getChequeDate() {
		return chequeDate;
	}

	public void setChequeDate(Date chequeDate) {
		this.chequeDateString = HijriDateService.getHijriDateString(chequeDate);
		this.chequeDate = chequeDate;
	}

	@Transient
	public String getChequeDateString() {
		return chequeDateString;
	}

	@Basic
	@Column(name = "CHEQUE_AMOUNT")
	public Float getChequeAmount() {
		return chequeAmount;
	}

	public void setChequeAmount(Float chequeAmount) {
		this.chequeAmount = chequeAmount;
	}

	@Override
	public Long calculateContentId() {
		return id;
	}
}
