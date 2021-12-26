package com.code.dal.orm.carrental;

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
	 @NamedQuery(name  = "carRentalData_searchCarRentals", 
          	 	query = " select c " +
          	 		 " from CarRentalData c " +
          			 " where (:P_ID = -1 or c.id = :P_ID )" +
          			 " and (:P_CONTRACT_NUMBER = '-1' or c.contractNumber = :P_CONTRACT_NUMBER ) " +
          			 " and (:P_CHEQUE_NUMBER = '-1' or c.chequeNumber = :P_CHEQUE_NUMBER ) " +
          			 " and (:P_PLATE_NUMBER = '-1' or c.carPlateNumber = :P_PLATE_NUMBER ) " +
          			 " and (:P_EMPLOYEE_ID = -1 or c.employeeId = :P_EMPLOYEE_ID ) " +
          			 " and (:P_DOMAIN_SUB_RENTAL_COMPANY = -1 or c.domainSubRentalCompanyId = :P_DOMAIN_SUB_RENTAL_COMPANY ) " +
			 		 " and (:P_CONTRACT_STATUS = -1 or c.paid = :P_CONTRACT_STATUS ) "+
			 		 " and (:P_DEPT_ID_LIST_SIZE = 0 or c.departmentId IN (:P_DEPT_ID_LIST)) "+
			 		 " and (:P_RENT_START_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_RENT_START_DATE, 'MI/MM/YYYY') <= c.rentStartDate)" + 
			 		 " and (:P_RENT_END_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_RENT_END_DATE, 'MI/MM/YYYY') >= c.rentEndDate)" +      
          			 " order by c.rentEndDate desc , c.id "
			 	)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_CARS_RENTALS")
public class CarRentalData extends BaseEntity implements Serializable {
	private Long id;
	private String orderNumber;
	private Date orderDate;
	private String orderDateString;

	private Long departmentId;
	private String registerDepartmentName;

	private Long employeeId;
	private String employeeName;
	private String employeeUnitName;
	private String employeeRank;

	private String infoNumber;
	private String missionDescription;
	private String missionConclusion;

	private Long domainSubRentalCompanyId;
	private String domainSubRentalComapnyName;

	private String contractNumber;
	private Date rentStartDate;
	private String rentStartDateString;
	private Date rentEndDate;
	private String rentEndDateString;
	private Integer rentPeriod;
	private Float dailyAmount;
	private Float totalAmount;
	private Float extraAmount;
	private String carPlateNumber;

	private Long domainCarModelId;
	private String domainCarModelName;

	private Integer paid;
	private String chequeNumber;
	private Date chequeDate;
	private String chequeDateString;
	private Float chequeAmount;
	
	private String plateText1;
	private String plateText2;
	private String plateText3;
	private String plateNumber;

	private CarRental carRental;

	public CarRentalData() {
		this.carRental = new CarRental();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return carRental.getId();
	}

	public void setId(Long id) {
		this.id = id;
		this.carRental.setId(id);
	}

	@Basic
	@Column(name = "ORDER_NUMBER")
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
		this.carRental.setOrderNumber(orderNumber);
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
		this.carRental.setOrderDate(orderDate);
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
		this.carRental.setDepartmentId(departmentId);
	}

	@Basic
	@Column(name = "REGISTER_DEPARTMENT_NAME")
	public String getRegisterDepartmentName() {
		return registerDepartmentName;
	}

	public void setRegisterDepartmentName(String departmentName) {
		this.registerDepartmentName = departmentName;
	}

	@Basic
	@Column(name = "STP_VW_EMPLOYEES_ID")
	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
		this.carRental.setEmployeeId(employeeId);
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
	@Column(name = "INFO_NUMBER")
	public String getInfoNumber() {
		return infoNumber;
	}

	public void setInfoNumber(String infoNumber) {
		this.infoNumber = infoNumber;
		this.carRental.setInfoNumber(infoNumber);
	}

	@Basic
	@Column(name = "MISSION_DESCRIPTION")
	public String getMissionDescription() {
		return missionDescription;
	}

	public void setMissionDescription(String missionDescription) {
		this.missionDescription = missionDescription;
		this.carRental.setMissionDescription(missionDescription);
	}

	@Basic
	@Column(name = "MISSION_CONCLUSION")
	public String getMissionConclusion() {
		return missionConclusion;
	}

	public void setMissionConclusion(String missionConclusion) {
		this.missionConclusion = missionConclusion;
		this.carRental.setMissionConclusion(missionConclusion);
	}

	@Basic
	@Column(name = "DOMAINS_ID_SUB_RENTAL_COMPANY")
	public Long getDomainSubRentalCompanyId() {
		return domainSubRentalCompanyId;
	}

	public void setDomainSubRentalCompanyId(Long domainSubRentalCompanyId) {
		this.domainSubRentalCompanyId = domainSubRentalCompanyId;
		this.carRental.setDomainSubRentalCompanyId(domainSubRentalCompanyId);
	}

	@Basic
	@Column(name = "DOMAIN_NAME_SUB_RENTAL_COMPANY")
	public String getDomainSubRentalComapnyName() {
		return domainSubRentalComapnyName;
	}

	public void setDomainSubRentalComapnyName(String domainSubRentalComapnyName) {
		this.domainSubRentalComapnyName = domainSubRentalComapnyName;
	}

	@Basic
	@Column(name = "CONTRACT_NUMBER")
	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
		this.carRental.setContractNumber(contractNumber);
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
		this.carRental.setRentStartDate(rentStartDate);
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
		this.carRental.setRentEndDate(rentEndDate);
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
		this.carRental.setRentPeriod(rentPeriod);
	}

	@Basic
	@Column(name = "DAILY_AMOUNT")
	public Float getDailyAmount() {
		return dailyAmount;
	}

	public void setDailyAmount(Float dailyAmount) {
		this.dailyAmount = dailyAmount;
		this.carRental.setDailyAmount(dailyAmount);
	}

	@Basic
	@Column(name = "EXTRA_AMOUNT")
	public Float getExtraAmount() {
		return extraAmount;
	}

	public void setExtraAmount(Float extraAmount) {
		this.extraAmount = extraAmount;
		this.carRental.setExtraAmount(extraAmount);
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
		this.carRental.setCarPlateNumber(carPlateNumber);
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
		this.carRental.setDomainCarModelId(domainCarModelId);
	}

	@Basic
	@Column(name = "DOMAINS_NAME_CAR_MODEL")
	public String getDomainCarModelName() {
		return domainCarModelName;
	}

	public void setDomainCarModelName(String domainCarModelName) {
		this.domainCarModelName = domainCarModelName;
	}

	@Basic
	@Column(name = "PAID")
	public Integer getPaid() {
		return paid;
	}

	public void setPaid(Integer paid) {
		this.paid = paid;
		this.carRental.setPaid(paid);
	}

	@Basic
	@Column(name = "CHEQUE_NUMBER")
	public String getChequeNumber() {
		return chequeNumber;
	}

	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
		this.carRental.setChequeNumber(chequeNumber);
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
		this.carRental.setChequeDate(chequeDate);
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
		this.carRental.setChequeAmount(chequeAmount);
	}

	@Transient
	public CarRental getCarRental() {
		return carRental;
	}

	public void setCarRental(CarRental carRental) {
		this.carRental = carRental;
	}

	@Transient
	public Float getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Float totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Basic
	@Column(name = "EMPLOYEE_UNIT_NAME")
	public String getEmployeeUnitName() {
		return employeeUnitName;
	}

	public void setEmployeeUnitName(String employeeUnitName) {
		this.employeeUnitName = employeeUnitName;
	}

	@Basic
	@Column(name = "EMPLOYEE_RANK")
	public String getEmployeeRank() {
		return employeeRank;
	}

	public void setEmployeeRank(String employeeRank) {
		this.employeeRank = employeeRank;
	}

}