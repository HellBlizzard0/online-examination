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
	@NamedQuery(name  = "departmentSectorData_searchDepartmentSectorData", 
			    query = " select ds " +
				 		" from DepartmentSectorData ds" +
				 		" where (ds.id = :P_ID )" +
						" order by ds.id "
	)
})
@SuppressWarnings("serial")
@Entity
@Table(name = "STP_VW_DEPARTMENTS_SECOTRS")
public class DepartmentSectorData extends BaseEntity implements Serializable{
	private Long id;
	private Long sectorId;
	
	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Basic
	@Column(name = "SECTORID")
	public Long getSectorId() {
		return sectorId;
	}
	
	public void setSectorId(Long sectorId) {
		this.sectorId = sectorId;
	}
}
