package com.code.dal.orm.labcheck;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
	@NamedQuery(name  = "labCheckReportDepartmentData_searchlabCheckReportDepartmentData", 
        	 	query = " select ld " +
            	 		 " from LabCheckReportDepartmentData ld" +
            			 " where (:P_LAB_CHECK_REPORT_ID = -1 or ld.labCheckReportId = :P_LAB_CHECK_REPORT_ID )" +
            	 		 " and (:P_DEPARTMENT_ID = -1 or :P_DEPARTMENT_ID = ld.departmentId) " +
            			 " order by ld.id"
			 	)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_LAB_CHECK_REPORT_DEPT")
public class LabCheckReportDepartmentData extends BaseEntity implements Serializable {
	private Long id;
	private Long labCheckReportId;
	private Long departmentId;
	private String departmentName;
	private Integer forcesNumber;
	private Integer forcesActual;
	
	private List<LabCheckReportDepartmentEmployeeData> labCheckReportDepartmentEmployees;
	private List<LabCheckReportDepartmentAction> labCheckReportDepartmentActions;
	private LabCheckReportDepartment labCheckReportDepartment;

	public LabCheckReportDepartmentData() {
		this.labCheckReportDepartment = new LabCheckReportDepartment();
		this.labCheckReportDepartmentActions = new ArrayList<LabCheckReportDepartmentAction>();
		this.labCheckReportDepartmentEmployees= new ArrayList<LabCheckReportDepartmentEmployeeData>();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return labCheckReportDepartment.getId();
	}

	public void setId(Long id) {
		this.id = id;
		this.labCheckReportDepartment.setId(id);
	}

	@Basic
	@Column(name = "LAB_CHECK_REPORTS_ID")
	public Long getLabCheckReportId() {
		return labCheckReportId;
	}

	public void setLabCheckReportId(Long labCheckReportId) {
		this.labCheckReportId = labCheckReportId;
		this.labCheckReportDepartment.setLabCheckReportId(labCheckReportId);
	}

	@Basic
	@Column(name = "STP_VW_DEPARTMENTS_ID")
	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
		this.labCheckReportDepartment.setDepartmentId(departmentId);
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
	@Column(name = "FORCES_NUMBER")
	public Integer getForcesNumber() {
		return forcesNumber;
	}

	public void setForcesNumber(Integer forcesNumber) {
		this.forcesNumber = forcesNumber;
		this.labCheckReportDepartment.setForcesNumber(forcesNumber);
	}

	@Basic
	@Column(name = "FORCES_ACTUAL")
	public Integer getForcesActual() {
		return forcesActual;
	}

	public void setForcesActual(Integer forcesActual) {
		this.forcesActual = forcesActual;
		this.labCheckReportDepartment.setForcesActual(forcesActual);
	}

	@Transient
	public List<LabCheckReportDepartmentEmployeeData> getLabCheckReportDepartmentEmployees() {
		return labCheckReportDepartmentEmployees;
	}

	public void setLabCheckReportDepartmentEmployees(List<LabCheckReportDepartmentEmployeeData> labCheckReportDepartmentEmployees) {
		this.labCheckReportDepartmentEmployees = labCheckReportDepartmentEmployees;
	}

	@Transient
	public List<LabCheckReportDepartmentAction> getLabCheckReportDepartmentActions() {
		return labCheckReportDepartmentActions;
	}

	public void setLabCheckReportDepartmentActions(List<LabCheckReportDepartmentAction> labCheckReportDepartmentActions) {
		this.labCheckReportDepartmentActions = labCheckReportDepartmentActions;
	}

	@Transient
	public LabCheckReportDepartment getLabCheckReportDepartment() {
		return labCheckReportDepartment;
	}

	public void setLabCheckReportDepartment(LabCheckReportDepartment labCheckReportDepartment) {
		this.labCheckReportDepartment = labCheckReportDepartment;
	}
}
