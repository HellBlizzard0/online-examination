package com.code.dal.orm.labcheck;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.code.dal.orm.BaseEntity;
import com.code.services.util.HijriDateService;

@NamedQueries({
	@NamedQuery(name  = "labCheckReportDepartmentEmployeeData_searchlbChkRprtDptEmpData", 
        	 	query = " select le " +
            	 		 " from LabCheckReportDepartmentEmployeeData le" +
            			 " where (:P_DEPARTMENT_ID = -1 or le.labCheckReportDepartmentId = :P_DEPARTMENT_ID )" + 
            			 " order by le.id"
			 	)
})
@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_LAB_CHK_RPRT_DPT_EMPS")
public class LabCheckReportDepartmentEmployeeData extends BaseEntity implements Serializable {
	private Long id;
	private Long employeeId;
	private String employeeName;
	private Long domainHospitalId;
	private String domainHospitalDescription;
	private Date forwardDate;
	private String forwardDateString;
	private Long labCheckReportDepartmentId;
	private LabCheckReportDepartmentEmployee labCheckReportDepartmentEmployee;

	public LabCheckReportDepartmentEmployeeData() {
		this.labCheckReportDepartmentEmployee = new LabCheckReportDepartmentEmployee();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return labCheckReportDepartmentEmployee.getId();
	}

	public void setId(Long id) {
		this.id = id;
		labCheckReportDepartmentEmployee.setId(id);
	}

	@Basic
	@Column(name = "STP_VW_EMPLOYEES_ID")
	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
		labCheckReportDepartmentEmployee.setEmployeeId(employeeId);
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
	@Column(name = "DOMAINS_ID_HOSPITAL")
	public Long getDomainHospitalId() {
		return domainHospitalId;
	}

	public void setDomainHospitalId(Long domainHospitalId) {
		this.domainHospitalId = domainHospitalId;
		labCheckReportDepartmentEmployee.setDomainHospitalId(domainHospitalId);
	}

	@Basic
	@Column(name = "DOMAINS_NAME_HOSPITAL")
	public String getDomainHospitalDescription() {
		return domainHospitalDescription;
	}

	public void setDomainHospitalDescription(String domainHospitalDescription) {
		this.domainHospitalDescription = domainHospitalDescription;
	}

	@Basic
	@Column(name = "FORWARD_DATE")
	public Date getForwardDate() {
		return forwardDate;
	}

	public void setForwardDate(Date forwardDate) {
		this.forwardDate = forwardDate;
		this.forwardDateString = HijriDateService.getHijriDateString(forwardDate);
		labCheckReportDepartmentEmployee.setForwardDate(forwardDate);
	}

	@Transient
	public String getForwardDateString() {
		return forwardDateString;
	}

	@Basic
	@Column(name = "LAB_CHECK_REPORT_DEPTS_ID")
	public Long getLabCheckReportDepartmentId() {
		return labCheckReportDepartmentId;
	}

	public void setLabCheckReportDepartmentId(Long labCheckReportDepartmentId) {
		this.labCheckReportDepartmentId = labCheckReportDepartmentId;
		labCheckReportDepartmentEmployee.setLabCheckReportDepartmentId(labCheckReportDepartmentId);
	}

	@Transient
	public LabCheckReportDepartmentEmployee getLabCheckReportDepartmentEmployee() {
		return labCheckReportDepartmentEmployee;
	}

	public void setLabCheckReportDepartmentEmployee(LabCheckReportDepartmentEmployee labCheckReportDepartmentEmployee) {
		this.labCheckReportDepartmentEmployee = labCheckReportDepartmentEmployee;
	}
}
