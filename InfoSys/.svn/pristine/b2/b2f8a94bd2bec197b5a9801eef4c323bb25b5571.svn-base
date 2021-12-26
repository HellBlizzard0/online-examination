package com.code.dal.orm.assignment;

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
	 @NamedQuery(name = "assignmentAgentCode_searchAssignmentAgentCode", 
	           	 query= " select a " +
	           	 		" from AssignmentAgentCode a" +
	           	 		" where a.departmentId = :P_DEPARTMENT_ID " +
	           			" order by a.id "
	)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_ASSIGNMENT_AGENT_CODES")
public class AssignmentAgentCode extends BaseEntity implements Serializable {
	private Long id;
	private Long departmentId;
	private String agentPrefix ;
	
	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	@Column(name = "AGENT_PREFIX")
	public String getAgentPrefix() {
		return agentPrefix;
	}

	public void setAgentPrefix(String agentPrefix) {
		this.agentPrefix = agentPrefix;
	}
}