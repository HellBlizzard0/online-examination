package com.code.dal.orm.setup;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.code.dal.orm.BaseEntity;

@NamedQueries({
	@NamedQuery(name  = "departmentTitleData_searchDepartmentManager", 
				query = " select e.empId " +
					    " from DepartmentTitleData dt, EmployeeData e " +
					    " where dt.id = e.actualTitleId " +
					    " and dt.isDeptHead = 1 " + 
					    " and dt.deptId = :P_DEPT_ID " +
				    	" order by e.empId "
	),	
})


@SuppressWarnings("serial")
@Entity
@Table(name = "STP_VW_DEPARTMENT_TITLE")
public class DepartmentTitleData extends BaseEntity implements Serializable {
	private Long id;
	private Long deptId;
	private Boolean isDeptHead;
	
	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Basic
	@Column(name = "STP_DEPARTMENTS_ID")
	public Long getDeptId() {
		return deptId;
	}
	
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	
	@Basic
	@Column(name = "IS_DEPT_HEAD")
	public Boolean getIsDeptHead() {
		return isDeptHead;
	}
	
	public void setIsDeptHead(Boolean isDeptHead) {
		this.isDeptHead = isDeptHead;
	}
}