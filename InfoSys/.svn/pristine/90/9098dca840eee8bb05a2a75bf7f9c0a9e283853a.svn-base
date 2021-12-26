package com.code.dal.orm.fishing;

import java.io.Serializable;

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
	 @NamedQuery(name = "fishingNavigationSailorData_searchFishingNavigationSailors", 
	           	 query= " select f " +
	           	 		" from FishingNavigationSailorData f" +
	           			" where ( :P_NAVIGATION_ID = -1 or f.navId = :P_NAVIGATION_ID ) " +
	           			" order by f.id "
	)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FSH_VW_NAVIGATION_SAILORS")
public class FishingNavigationSailorData extends BaseEntity implements Serializable {
	private Long id;
	private Long navId;
	private String sailorName;
	private String sailorIdentity;
	private String nationality;
	private boolean observed;

	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Basic
	@Column(name = "NAVPRMT_ID")
	public Long getNavId() {
		return navId;
	}

	public void setNavId(Long navId) {
		this.navId = navId;
	}

	@Basic
	@Column(name = "SAILOR_NAME")
	public String getSailorName() {
		return sailorName;
	}

	public void setSailorName(String sailorName) {
		this.sailorName = sailorName;
	}

	@Basic
	@Column(name = "SAILOR_IDNO")
	public String getSailorIdentity() {
		return sailorIdentity;
	}

	public void setSailorIdentity(String sailorIdentity) {
		this.sailorIdentity = sailorIdentity;
	}
	
	@Basic
	@Column(name = "NATIONALITY")
	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	@Transient
	public boolean isObserved() {
		return observed;
	}

	public void setObserved(boolean observed) {
		this.observed = observed;
	}
}
