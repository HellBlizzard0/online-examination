package com.code.dal.orm.assignment;

import java.io.Serializable;

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

import com.code.dal.orm.BaseEntity;

@NamedQueries({
	 @NamedQuery(name = "regionDepartments_searchdRegionDepartmentsIds", 
	           	 query= " select rd.departmentId " +
	           	 		" from IntelligenceRegionsDepartments rd" +
	           	 		" where (:P_REGION_ID = -1 or (rd.regionId = :P_REGION_ID and rd.sectorId = null))" +
	           	 	    " or    (:P_SECTOR_ID = -1 or rd.sectorId = :P_SECTOR_ID )" +														
	           			" order by rd.id "
	)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_INTEL_REGIONS_DEPARTMENTS")
public class IntelligenceRegionsDepartments extends BaseEntity implements Serializable {
	private Long id;
	private Long departmentId;
	private Long regionId;
	private Long sectorId;
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_INTL_REGIONS_DEPS_SEQ", sequenceName = "FIS_INTL_REGIONS_DEPS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_INTL_REGIONS_DEPS_SEQ")
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
	@Column(name = "REGION_ID")
	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	@Basic
	@Column(name = "SECTOR_ID")
	public Long getSectorId() {
		return sectorId;
	}

	public void setSectorId(Long sectorId) {
		this.sectorId = sectorId;
	}

}
