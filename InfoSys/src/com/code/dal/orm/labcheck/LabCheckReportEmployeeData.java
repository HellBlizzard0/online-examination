package com.code.dal.orm.labcheck;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.code.dal.audit.DeleteableAuditEntity;
import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;

@NamedQueries({
	@NamedQuery(name  = "labCheckReportEmployeeData_searchlabCheckReportEmployee", 
        	 	query = " select e " +
            	 		 " from LabCheckReportEmployeeData e " +
            			 " where (:P_LAB_CHECK_REPORT_ID = -1 or e.labCheckReportId = :P_LAB_CHECK_REPORT_ID )" + 
            	 		 " and (:P_EMPLOYEE_ID = -1 or :P_EMPLOYEE_ID = e.employeeId)" +
            			 " order by e.fullName"
			 	)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_LAB_CHECK_REPORT_EMPS")
public class LabCheckReportEmployeeData extends AuditEntity implements Serializable, UpdateableAuditEntity, InsertableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private Long labCheckReportId;
	private Long employeeId;
	private String fullName;
	private String socialID;
	private String militaryNo;
	private String rank;

	private LabCheckReportEmployee labCheckReportEmployee;

	public LabCheckReportEmployeeData() {
		this.labCheckReportEmployee = new LabCheckReportEmployee();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return labCheckReportEmployee.getId();
	}

	public void setId(Long id) {
		this.id = id;
		this.labCheckReportEmployee.setId(id);
	}

	@Basic
	@Column(name = "LAB_CHECK_REPORTS_ID")
	public Long getLabCheckReportId() {
		return labCheckReportId;
	}

	public void setLabCheckReportId(Long labCheckReportId) {
		this.labCheckReportId = labCheckReportId;
		this.labCheckReportEmployee.setLabCheckReportId(labCheckReportId);
	}

	@Basic
	@Column(name = "STP_VW_EMPLOYEES_ID")
	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
		this.labCheckReportEmployee.setEmployeeId(employeeId);
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
	@Column(name = "SOCIAL_ID")
	public String getSocialID() {
		return socialID;
	}

	public void setSocialID(String socialID) {
		this.socialID = socialID;
	}

	@Basic
	@Column(name = "MILITARY_NO")
	public String getMilitaryNo() {
		return militaryNo;
	}

	public void setMilitaryNo(String militaryNo) {
		this.militaryNo = militaryNo;
	}

	@Basic
	@Column(name = "RANK")
	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	@Transient
	public LabCheckReportEmployee getLabCheckReportEmployee() {
		return labCheckReportEmployee;
	}

	public void setLabCheckReportEmployee(LabCheckReportEmployee labCheckReportEmployee) {
		this.labCheckReportEmployee = labCheckReportEmployee;
	}

	@Override
	public Long calculateContentId() {
		return id;
	}
}