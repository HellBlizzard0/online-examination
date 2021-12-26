package com.code.dal.orm.fishing;

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
	 @NamedQuery(name = "fishingNavigationDelegationData_searchFishingNavigationDelegates", 
	           	 query= " select f " +
	           	 		" from FishingNavigationDelegationsData f" +
	           			" where ( :P_BOAT_ID = -1 or f.boatId = :P_BOAT_ID )" + 
	           			" and ( f.captinFlag =:P_DELEGATE_TYPE ) " +
	           			" order by f.id "
	)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FSH_VW_NAVIGATION_DELEGATIONS")
public class FishingNavigationDelegationsData extends BaseEntity implements Serializable {
	private Long id;
	private Long boatId;
	private String delegateName;
	private Character captinFlag;
	private String idNo;
	private Long personNationalityId;	
	private String personNationalityName;

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
	@Column(name = "A_NM")
	public String getDelegateName() {
		return delegateName;
	}

	public void setDelegateName(String delegateName) {
		this.delegateName = delegateName;
	}

	@Basic
	@Column(name = "CAPTIN_FLAG")
	public Character getCaptinFlag() {
		return captinFlag;
	}

	public void setCaptinFlag(Character captinFlag) {
		this.captinFlag = captinFlag;
	}

	@Basic
	@Column(name = "ID_NO")
	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	@Basic
	@Column(name = "PRSN_NATIONALITY_ID")
	public Long getPersonNationalityId() {
		return personNationalityId;
	}

	public void setPersonNationalityId(Long personNationalityId) {
		this.personNationalityId = personNationalityId;
	}

	@Basic
	@Column(name = "PRSN_NATIONALITY_NAME")
	public String getPersonNationalityName() {
		return personNationalityName;
	}

	public void setPersonNationalityName(String personNationalityName) {
		this.personNationalityName = personNationalityName;
	}
	

}