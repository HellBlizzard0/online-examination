package com.code.dal.orm.securitymission;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.code.dal.orm.BaseEntity;

@NamedQueries({
	@NamedQuery(name = "empNonEmpCarPermitsData_searchEmpNonEmpCarPermitsData",
				query = " select cp from EmpNonEmpCarPermitsData cp" +
						" where (:P_EMP_ID = -1 or cp.empId = :P_EMP_ID ) " +
						" and (:P_NON_EMP_ID = -1 or cp.nonEmpId = :P_NON_EMP_ID ) " +
						" and (:P_PERMIT_NO = '-1' or cp.permitNo = :P_PERMIT_NO )" +
						" and (:P_PLATE_NUMBER = '-1' or cp.plateNumber = :P_PLATE_NUMBER ) " +
						" order by cp.id desc") 
	})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_EMP_NON_EMP_CAR_PRMTS")
public class EmpNonEmpCarPermitsData extends BaseEntity implements Serializable {
	private Long id;
	private String plateNumber;
	private Long carModelId;
	private String carModel;
	private String permitNo;
	private Long nonEmpId;
	private Long empId;
	private EmployeeNonEmployeeCars empNonEmpCars;
	private CarPermit carPermit;

	public EmpNonEmpCarPermitsData(){
		empNonEmpCars = new EmployeeNonEmployeeCars();
		carPermit = new CarPermit();
	}
	
	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
		empNonEmpCars.setId(id);
		carPermit.setEmployeeNonEmployeeCarsId(id);
	}

	@Id
	@Column(name = "PLATE_NUMBER")
	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
		String[] plateNumberArr = plateNumber.split(" ");
		empNonEmpCars.setPlateNumber(plateNumberArr[3]);
		empNonEmpCars.setPlateChar1(plateNumberArr[0]);
		empNonEmpCars.setPlateChar2(plateNumberArr[1]);
		empNonEmpCars.setPlateChar3(plateNumberArr[2]);
	}

	@Id
	@Column(name = "DOMAINS_ID_CAR_MODEL")
	public Long getCarModelId() {
		return carModelId;
	}

	public void setCarModelId(Long carModelId) {
		this.carModelId = carModelId;
		empNonEmpCars.setDomainCarModelId(carModelId);
	}

	@Id
	@Column(name = "DOMAINS_NAME_CAR_MODEL")
	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
		empNonEmpCars.setDomainCarModelDescription(carModel);
	}

	@Basic
	@Column(name = "PERMIT_NO")
	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
		carPermit.setPermitNo(permitNo);
	}

	@Basic
	@Column(name = "NON_EMPLOYEES_ID")
	public Long getNonEmpId() {
		return nonEmpId;
	}

	public void setNonEmpId(Long nonEmpId) {
		this.nonEmpId = nonEmpId;
		empNonEmpCars.setNonEmployeeId(nonEmpId);
	}

	@Basic
	@Column(name = "STP_VW_EMPLOYEES_ID")
	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
		empNonEmpCars.setEmployeeId(empId);
	}

	@Transient
	public EmployeeNonEmployeeCars getEmpNonEmpCars() {
		return empNonEmpCars;
	}

	public void setEmpNonEmpCars(EmployeeNonEmployeeCars empNonEmpCars) {
		this.empNonEmpCars = empNonEmpCars;
	}

	@Transient
	public CarPermit getCarPermit() {
		return carPermit;
	}

	public void setCarPermit(CarPermit carPermit) {
		this.carPermit = carPermit;
	}
}
