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
	@NamedQuery(name  = "departmentTypeData_searchDepartmentType", 
			    query = " select t " +
				 		" from DepartmentTypeData t" +
						" order by t.id "
	)
})
@SuppressWarnings("serial")
@Entity
@Table(name = "STP_VW_DEPARTMENT_TYPE")
public class DepartmentTypeData extends BaseEntity implements Serializable{
	private Long id;
	private String description;
	
	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}