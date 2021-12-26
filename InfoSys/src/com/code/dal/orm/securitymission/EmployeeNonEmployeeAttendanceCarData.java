package com.code.dal.orm.securitymission;

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
		@NamedQuery(
				name = "employeeNonEmployeeAttendanceCarData_searchEmployeeNonEmployeeAttendanceData",
				query = " select d " +
						" from EmployeeNonEmployeeAttendanceCarData d " +
						" where (:P_ID = -1 or d.id = :P_ID )" +
						" and ( (:P_SEARCH_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_SEARCH_DATE, 'MI/MM/YYYY') = d.entryDate)" +
						" or (:P_SEARCH_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_SEARCH_DATE, 'MI/MM/YYYY') = d.exitDate))" +
						" and (:P_REGION_ID = -1 or d.regionId = :P_REGION_ID)" +
						" order by case when d.entryTime is null then 1 else 0 end , d.entryTime desc , d.exitTime desc"),
		@NamedQuery(
				name = "employeeNonEmployeeAttendanceCarData_searchEmployeeNonEmployeeAttendanceCarData",
				query = " select a " +
						" from EmployeeNonEmployeeAttendanceCarData a " +
						" where ( ( (:P_TYPE      = 0 or :P_TYPE            = -1) " +
						" and a.entryDate        >= PKG_CHAR_TO_DATE (:P_START_DATE,'MI/MM/YYYY') " +
						" and a.entryDate        <= PKG_CHAR_TO_DATE (:P_END_DATE,'MI/MM/YYYY') " +
						" and (:P_START_TIME    = '-1' or a.entryTime         >= :P_START_TIME) " +
						" and (:P_END_TIME      = '-1' or a.entryTime         <= :P_END_TIME))  " +
						" or ((:P_TYPE           = 1 or :P_TYPE            = -1) " +
						" and a.exitDate         >= PKG_CHAR_TO_DATE (:P_START_DATE,'MI/MM/YYYY') " +
						" and a.exitDate         <= PKG_CHAR_TO_DATE (:P_END_DATE,'MI/MM/YYYY') " +
						" and (:P_START_TIME    = '-1' or a.exitTime          >= :P_START_TIME) " +
						" and (:P_END_TIME      = '-1' or a.exitTime          <= :P_END_TIME)) ) " +
						" and (:P_DEPARTMENT_ID = -1 or a.departmentId       = :P_DEPARTMENT_ID) " +
						" and (:P_SOCIAL_ID     = -1 or a.socialId           = :P_SOCIAL_ID) " +
						" and (:P_REGION_ID = -1 or a.regionId       = :P_REGION_ID) " +
						" order by a.entryDate, a.entryTime"),
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_EMP_NON_EMP_ATTENDANCE")
public class EmployeeNonEmployeeAttendanceCarData extends BaseEntity {
	private Long id;
	private Long employeeId;
	private Long nonEmployeeId;
	private String socialId;
	private String rank;
	private String fullName;
	private Long departmentId;
	private String departmentName;
	private String nationality;
	private Date entryDate;
	private String entryDateString;
	private String entryTime;
	private Date exitDate;
	private String exitDateString;
	private String exitTime;
	private Long empNonEmpCarsId;
	private String mobileNo;
	private String telephoneExt;
	private Long domainCarModelId;
	private String carModel;
	private String plateNumber;
	private String permitNo;
	private String visitorCardNumber;
	private Long regionId;

	EmployeeNonEmployeeAttendance employeeNonEmployeeAttendance;
	EmployeeNonEmployeeCars employeeNonEmployeeCar;

	public EmployeeNonEmployeeAttendanceCarData() {
		employeeNonEmployeeAttendance = new EmployeeNonEmployeeAttendance();
		employeeNonEmployeeCar = new EmployeeNonEmployeeCars();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
		employeeNonEmployeeAttendance.setId(id);
	}

	@Basic
	@Column(name = "STP_VW_EMPLOYEE_ID")
	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
		employeeNonEmployeeAttendance.setEmployeeId(employeeId);
		employeeNonEmployeeCar.setEmployeeId(employeeId);
	}

	@Basic
	@Column(name = "NON_EMPLOYEE_ID")
	public Long getNonEmployeeId() {
		return nonEmployeeId;
	}

	public void setNonEmployeeId(Long nonEmployeeId) {
		this.nonEmployeeId = nonEmployeeId;
		employeeNonEmployeeAttendance.setNonEmployeeId(nonEmployeeId);
		employeeNonEmployeeCar.setNonEmployeeId(nonEmployeeId);
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENTRY_DATE")
	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		if (entryDate != null) {
			this.entryDateString = HijriDateService.getHijriDateString(entryDate);
		}
		this.entryDate = entryDate;
		employeeNonEmployeeAttendance.setEntryDate(entryDate);
	}

	@Transient
	public String getEntryDateString() {
		return entryDateString;
	}

	@Basic
	@Column(name = "ENTRY_TIME")
	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
		employeeNonEmployeeAttendance.setEntryTime(entryTime);
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXIT_DATE")
	public Date getExitDate() {
		return exitDate;
	}

	public void setExitDate(Date exitDate) {
		if (exitDate != null) {
			this.exitDateString = HijriDateService.getHijriDateString(exitDate);
		}
		this.exitDate = exitDate;
		employeeNonEmployeeAttendance.setExitDate(exitDate);
	}

	@Transient
	public String getExitDateString() {
		return exitDateString;
	}

	@Basic
	@Column(name = "EXIT_TIME")
	public String getExitTime() {
		return exitTime;
	}

	public void setExitTime(String exitTime) {
		this.exitTime = exitTime;
		employeeNonEmployeeAttendance.setExitTime(exitTime);
	}

	@Basic
	@Column(name = "EMP_NON_EMP_CAR_ID")
	public Long getEmpNonEmpCarsId() {
		return empNonEmpCarsId;
	}

	public void setEmpNonEmpCarsId(Long empNonEmpCarsId) {
		this.empNonEmpCarsId = empNonEmpCarsId;
		employeeNonEmployeeAttendance.setEmpNonEmpCarsId(empNonEmpCarsId);
	}

	@Basic
	@Column(name = "SOCIAL_ID")
	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

	@Basic
	@Column(name = "RANK")
	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	@Basic
	@Column(name = "FULL_NAME")
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Basic
	@Column(name = "DEPARTMENT_ID")
	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	@Basic
	@Column(name = "DEPARTMENT_NAME")
	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	@Basic
	@Column(name = "COUNTRY_ARABIC_NAME")
	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	@Basic
	@Column(name = "MOBILE_NO")
	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	@Basic
	@Column(name = "TELEPHONE_EXT")
	public String getTelephoneExt() {
		return telephoneExt;
	}

	public void setTelephoneExt(String telephoneExt) {
		this.telephoneExt = telephoneExt;
	}

	@Basic
	@Column(name = "CAR_MODEL")
	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	@Basic
	@Column(name = "PLATE_NUMBER")
	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
		if (plateNumber != null && !plateNumber.trim().isEmpty()) {
			String[] plateNumberArray = plateNumber.split(" ");
			employeeNonEmployeeCar.setPlateNumber(plateNumberArray[3]);
			employeeNonEmployeeCar.setPlateChar1(plateNumberArray[0]);
			employeeNonEmployeeCar.setPlateChar2(plateNumberArray[1]);
			employeeNonEmployeeCar.setPlateChar3(plateNumberArray[2]);
		}
	}

	@Basic
	@Column(name = "PERMIT_NO")
	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}

	@Basic
	@Column(name = "DOMAINS_ID_CAR_MODEL")
	public Long getDomainCarModelId() {
		return domainCarModelId;
	}

	public void setDomainCarModelId(Long domainCarModelId) {
		this.domainCarModelId = domainCarModelId;
		employeeNonEmployeeCar.setDomainCarModelId(domainCarModelId);
	}

	@Basic
	@Column(name = "REGION_ID")
	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
		employeeNonEmployeeAttendance.setRegionId(regionId);
	}

	@Transient
	public EmployeeNonEmployeeAttendance getEmployeeNonEmployeeAttendance() {
		return employeeNonEmployeeAttendance;
	}

	public void setEmployeeNonEmployeeAttendance(EmployeeNonEmployeeAttendance employeeNonEmployeeAttendance) {
		this.employeeNonEmployeeAttendance = employeeNonEmployeeAttendance;
	}

	@Transient
	public EmployeeNonEmployeeCars getEmployeeNonEmployeeCar() {
		return employeeNonEmployeeCar;
	}

	public void setEmployeeNonEmployeeCar(EmployeeNonEmployeeCars employeeNonEmployeeCar) {
		this.employeeNonEmployeeCar = employeeNonEmployeeCar;
	}

	@Override
	public boolean equals(Object o) {
		return ((o instanceof EmployeeNonEmployeeAttendanceCarData) && (id.equals(((EmployeeNonEmployeeAttendanceCarData) o).getId())));
	}

	@Transient
	public String getVisitorCardNumber() {
		return visitorCardNumber;
	}

	public void setVisitorCardNumber(String visitorCardNumber) {
		this.visitorCardNumber = visitorCardNumber;
	}
}
