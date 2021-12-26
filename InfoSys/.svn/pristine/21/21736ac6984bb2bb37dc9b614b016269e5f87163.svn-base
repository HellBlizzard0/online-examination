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
	 @NamedQuery(name = "assignmentAgentClass_searchAgentClasses", 
	           	 query= " select a " +
	           	 		" from AssignmentAgentClass a" +
	           			" order by a.id "
	)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_ASSIGNMENT_AGENT_CLASSES")
public class AssignmentAgentClass extends BaseEntity implements Serializable {
	private Long id;
	private String code;
	private Double value;

	
	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Basic
	@Column(name = "VALUE")
	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

}
