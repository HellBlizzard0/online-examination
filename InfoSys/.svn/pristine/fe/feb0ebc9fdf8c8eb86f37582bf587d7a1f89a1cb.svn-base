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
	 @NamedQuery(name  = "departmentHierData_searchDepartmentHier", 
            	 query = " select dH " +
            	 		 " from DepartmentHierData dH" +
            			 " where (:P_ID = -1 or dH.id = :P_ID )" +
            			 " and (:P_PARENT_ID = -1 or dH.parentId = :P_PARENT_ID )" +
            			 " order by dH.parentId "
   )
})

@SuppressWarnings("serial")
@Entity
@Table(name = "STP_VW_DEPARTMENT_HIER")
public class DepartmentHierData extends BaseEntity implements Serializable {
	private Long id;
	private Long parentId;

	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "PARENT_ID")
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
}
