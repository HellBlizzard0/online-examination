package com.code.dal.orm.securityanalysis;

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
   @NamedQuery(name  = "regionData_searchRegionData", 
			    query = " select r " +
					    " from RegionData r " +
					    " where (:P_REGION_ID = -1 or r.regionId = :P_REGION_ID) " +
					    " and   (:P_REGION_TYPE = -1 or r.regionType = :P_REGION_TYPE)" +
			        	" order by r.id "
			
		      ),	
   
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_SC_VW_REGIONS")
public class RegionData extends BaseEntity implements Serializable {
	private Long id;
	private Long regionId;
	private String regionName;
	private Integer regionType;

	public RegionData() {
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	@Column(name = "REGION_NAME")
	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	@Basic
	@Column(name = "REGION_TYPE")
	public Integer getRegionType() {
		return regionType;
	}

	public void setRegionType(Integer regionType) {
		this.regionType = regionType;
	}
}
