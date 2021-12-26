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
	 @NamedQuery(name = "fishingBoatOwnerData_searchFishingBoatOwners", 
	           	 query= " select f " +
	           	 		" from FishingBoatOwnerData f" +
	           			" where ( :P_BOAT_ID = -1 or f.boatId = :P_BOAT_ID ) " +
	           			" order by f.id "
	)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FSH_VW_BOAT_OWNERS")
public class FishingBoatOwnerData extends BaseEntity implements Serializable {
	private Long id;
	private Long boatId;
	private String ownerName;
	private String ownerIdentity;
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
	@Column(name = "BOAT_ID")
	public Long getBoatId() {
		return boatId;
	}

	public void setBoatId(Long boatId) {
		this.boatId = boatId;
	}

	@Basic
	@Column(name = "OWNER_NAME")
	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	@Basic
	@Column(name = "OWNER_IDNO")
	public String getOwnerIdentity() {
		return ownerIdentity;
	}

	public void setOwnerIdentity(String ownerIdentity) {
		this.ownerIdentity = ownerIdentity;
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
