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

import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;
import com.code.services.util.HijriDateService;

@NamedQueries({
		@NamedQuery(
				name = "employeeNonEmployeeAttendance_searchEmployeeNonEmployeeAttendance",
				query = " select d " +
						" from EmployeeNonEmployeeAttendance d " +
						" where (:P_EMPLOYEE_ID = -1 or d.employeeId = :P_EMPLOYEE_ID )" +
						" and (:P_NON_EMPLOYEE_ID = -1 or d.nonEmployeeId = :P_NON_EMPLOYEE_ID )" +
						" order by d.entryDate desc")
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_EMP_NON_EMP_ATNDNCE")
public class EmployeeNonEmployeeAttendance extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity {
	private Long id;
	private Long employeeId;
	private Long nonEmployeeId;
	private Date entryDate;
	private String entryDateString;
	private String entryTime;
	private Date exitDate;
	private String exitDateString;
	private String exitTime;
	private Long empNonEmpCarsId;
	private Long regionId;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_EMP_NON_EMP_ATND_DTLS_SEQ", sequenceName = "FIS_EMP_NON_EMP_ATND_DTLS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_EMP_NON_EMP_ATND_DTLS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "STP_VW_EMPLOYEE_ID")
	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	@Basic
	@Column(name = "NON_EMPLOYEE_ID")
	public Long getNonEmployeeId() {
		return nonEmployeeId;
	}

	public void setNonEmployeeId(Long nonEmployeeId) {
		this.nonEmployeeId = nonEmployeeId;
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
	}

	@Basic
	@Column(name = "EMP_NON_EMP_CARS_ID")
	public Long getEmpNonEmpCarsId() {
		return empNonEmpCarsId;
	}

	public void setEmpNonEmpCarsId(Long empNonEmpCarsId) {
		this.empNonEmpCarsId = empNonEmpCarsId;
	}

	@Override
	public Long calculateContentId() {
		return id;
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
