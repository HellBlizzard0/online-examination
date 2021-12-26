package com.code.dal.orm.securitymission;

import java.io.Serializable;

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

import com.code.dal.audit.DeleteableAuditEntity;
import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;

@NamedQueries({
		@NamedQuery(
				name = "employeeNonEmployeeCars_searchEmployeeNonEmployeeCars",
				query = " select m " +
						" from EmployeeNonEmployeeCars m " +
						" where (:P_EMPLOYEE_ID = -1 or m.employeeId = :P_EMPLOYEE_ID )" +
						" and (:P_NON_EMPLOYEE_ID = -1 or m.nonEmployeeId = :P_NON_EMPLOYEE_ID )" +
						" and (:P_ID = -1 or m.id = :P_ID )" +
						" and (:P_PLATE_NUMBER_FLAG = 1 or (m.plateNumber = :P_PLATE_NUMBER and m.plateChar1 = :P_PLATE_CHAR_1 and m.plateChar2 = :P_PLATE_CHAR_2 and m.plateChar3 = :P_PLATE_CHAR_3 )) " +
						" order by m.id desc")
})
@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_EMP_NON_EMP_CARS")
public class EmployeeNonEmployeeCars extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private Long employeeId;
	private Long domainCarModelId;
	private String domainCarModelDescription;
	private Integer manufactureYear;
	private String plateNumber;
	private String plateChar1;
	private String plateChar2;
	private String plateChar3;
	private Long nonEmployeeId;
	private Long regionId;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_ATTENDANCE_SEQ", sequenceName = "FIS_ATTENDANCE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_ATTENDANCE_SEQ")
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

	@Override
	public Long calculateContentId() {
		return id;
	}

	@Basic
	@Column(name = "DOMAINS_NAME_CAR_MODEL")
	public String getDomainCarModelDescription() {
		return domainCarModelDescription;
	}

	public void setDomainCarModelDescription(String domainCarModelDescription) {
		this.domainCarModelDescription = domainCarModelDescription;
	}

	@Basic
	@Column(name = "NON_EMPLOYEES_ID")
	public Long getNonEmployeeId() {
		return nonEmployeeId;
	}

	public void setNonEmployeeId(Long nonEmployeeId) {
		this.nonEmployeeId = nonEmployeeId;
	}

	@Basic
	@Column(name = "PLATE_CHAR1")
	public String getPlateChar1() {
		return plateChar1;
	}

	public void setPlateChar1(String plateChar1) {
		this.plateChar1 = plateChar1;
	}

	@Basic
	@Column(name = "PLATE_CHAR2")
	public String getPlateChar2() {
		return plateChar2;
	}

	public void setPlateChar2(String plateChar2) {
		this.plateChar2 = plateChar2;
	}

	@Basic
	@Column(name = "PLATE_CHAR3")
	public String getPlateChar3() {
		return plateChar3;
	}

	public void setPlateChar3(String plateChar3) {
		this.plateChar3 = plateChar3;
	}

	@Basic
	@Column(name = "REGION_ID")
	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}
}
