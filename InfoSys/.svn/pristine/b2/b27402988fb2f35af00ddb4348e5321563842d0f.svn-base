package com.code.dal.orm.fishing;

import java.io.Serializable;
import java.util.Date;

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
	 @NamedQuery(name = "fishingNavigationData_searchFishingNavigations", 
	           	 query= " select f " +
	           	 		" from FishingNavigationData f" +
	           			" where ( :P_NUMBER = '-1' or f.number = :P_NUMBER ) " +
	           			" order by f.id "
	)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FSH_VW_NAVIGATIONS")
public class FishingNavigationData extends BaseEntity implements Serializable {
	private Long id;
	private String number;
	private Integer period;
	private Date actualNavDate;
	private String actualNavHijriDateString;
	private Date actualReturnDate;
	private String actualReturnHijriDateString;
	private String purpose;
	private String dock;
	private Long boatId;
	private String boatName;
	private String boatNumber;
	private String boatClass;
	private Double boatLength;
	private Double boatHeight;
	private String boatColor;
	private String boatRegNumber;
	private String boatSaftyStatus;
	private String captainName;
	private String captainIdentity;
	private String captainNationality;
	private boolean captainObserved;
	private Long regionId;
	private String regionName;
	private Date returnDate;
	private String returnHijriDateString;

	@Id
	@Column(name = "NAVPRMT_ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "NAVIGATION_NO")
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Basic
	@Column(name = "NAVIGATION_PERIOD")
	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	@Basic
	@Column(name = "NAVIGATION_ACTUAL_NAV_DT")
	public Date getActualNavDate() {
		return actualNavDate;
	}

	public void setActualNavDate(Date actualNavDate) {
		this.actualNavDate = actualNavDate;
	}

	@Basic
	@Column(name = "NAVIGATION_ACTUAL_RET_DT")
	public Date getActualReturnDate() {
		return actualReturnDate;
	}

	public void setActualReturnDate(Date actualReturnDate) {
		this.actualReturnDate = actualReturnDate;
	}

	@Basic
	@Column(name = "NAVIGATION_ACTUAL_NAV_HJ_DT")
	public String getActualNavHijriDateString() {
		return actualNavHijriDateString;
	}

	public void setActualNavHijriDateString(String actualNavHijriDateString) {
		this.actualNavHijriDateString = actualNavHijriDateString;
	}

	@Basic
	@Column(name = "NAVIGATION_ACTUAL_RET_HJ_DT")
	public String getActualReturnHijriDateString() {
		return actualReturnHijriDateString;
	}

	public void setActualReturnHijriDateString(String actualReturnHijriDateString) {
		this.actualReturnHijriDateString = actualReturnHijriDateString;
	}

	@Basic
	@Column(name = "NAVIGATION_PURPOSE")
	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	@Basic
	@Column(name = "NAVIGATION_DOCK")
	public String getDock() {
		return dock;
	}

	public void setDock(String dock) {
		this.dock = dock;
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
	@Column(name = "BOAT_NAME")
	public String getBoatName() {
		return boatName;
	}

	public void setBoatName(String boatName) {
		this.boatName = boatName;
	}

	@Basic
	@Column(name = "BOAT_NO")
	public String getBoatNumber() {
		return boatNumber;
	}

	public void setBoatNumber(String boatNumber) {
		this.boatNumber = boatNumber;
	}

	@Basic
	@Column(name = "BOAT_CLASS")
	public String getBoatClass() {
		return boatClass;
	}

	public void setBoatClass(String boatClass) {
		this.boatClass = boatClass;
	}

	@Basic
	@Column(name = "BOAT_LENGTH")
	public Double getBoatLength() {
		return boatLength;
	}

	public void setBoatLength(Double boatLength) {
		this.boatLength = boatLength;
	}

	@Basic
	@Column(name = "BOAT_HEIGHT")
	public Double getBoatHeight() {
		return boatHeight;
	}

	public void setBoatHeight(Double boatHeight) {
		this.boatHeight = boatHeight;
	}

	@Basic
	@Column(name = "BOAT_COLOR")
	public String getBoatColor() {
		return boatColor;
	}

	public void setBoatColor(String boatColor) {
		this.boatColor = boatColor;
	}

	@Basic
	@Column(name = "BOAT_REGISTERATION_NO")
	public String getBoatRegNumber() {
		return boatRegNumber;
	}

	public void setBoatRegNumber(String boatRegNumber) {
		this.boatRegNumber = boatRegNumber;
	}

	@Basic
	@Column(name = "BOAT_SAFTY_STATUS")
	public String getBoatSaftyStatus() {
		return boatSaftyStatus;
	}

	public void setBoatSaftyStatus(String boatSaftyStatus) {
		this.boatSaftyStatus = boatSaftyStatus;
	}

	@Basic
	@Column(name = "CAPTAIN_NAME")
	public String getCaptainName() {
		return captainName;
	}

	public void setCaptainName(String captainName) {
		this.captainName = captainName;
	}

	@Basic
	@Column(name = "CAPTAIN_IDNO")
	public String getCaptainIdentity() {
		return captainIdentity;
	}

	public void setCaptainIdentity(String captainIdentity) {
		this.captainIdentity = captainIdentity;
	}

	@Basic
	@Column(name = "CAPTAIN_NATIONALITY")
	public String getCaptainNationality() {
		return captainNationality;
	}

	public void setCaptainNationality(String captainNationality) {
		this.captainNationality = captainNationality;
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

	@Transient
	public boolean isCaptainObserved() {
		return captainObserved;
	}

	public void setCaptainObserved(boolean captainObserved) {
		this.captainObserved = captainObserved;
	}

	@Basic
	@Column(name = "EXPECTED_RET_DT")
	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	@Basic
	@Column(name = "EXPECTED_RET_HJ_DT")
	public String getReturnHijriDateString() {
		return returnHijriDateString;
	}

	public void setReturnHijriDateString(String returnHijriDateString) {
		this.returnHijriDateString = returnHijriDateString;
	}
}