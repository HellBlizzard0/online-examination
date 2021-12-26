package com.code.dal.orm.securityanalysis;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.code.dal.orm.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_SC_FOLLOW_UPS")
public class FollowUp extends BaseEntity implements Serializable, Cloneable {
	private Long id;
	private String contactNumber;
	private String code;
	private Integer ghostType;
	private Integer ghostCategory;
	private Long employeeId;
	private String nonEmployeeName;
	private String nonEmployeeSocialId;
	private String ownerName;
	private String ownerSocialId;
	private String aliasName;
	private Long domainIdDataSource;
	private String informationSource;
	private Double coordinateLatitude;
	private Double coordinateLongitude;
	private Date followUpStartDate;
	private Date followUpEndDate;
	private Integer followUpPeriod;
	private Long regionId;
	private String location;
	private Long sectorId;
	private Long domainIdNetworkRole;
	private Boolean directFlag;
	private Long domainIdMontrReason;
	private Long domainIdCasesType;
	private Long domainIdEquipmentType;
	private Integer status;
	private Boolean activeFlag;
	private Long followUpNetworkId;
	private Long instanceId;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_SC_FOLLOW_UPS_SEQ", sequenceName = "FIS_SC_FOLLOW_UPS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_SC_FOLLOW_UPS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "CONTACT_NUMBER")
	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
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
	@Column(name = "GHOST_TYPE")
	public Integer getGhostType() {
		return ghostType;
	}

	public void setGhostType(Integer ghostType) {
		this.ghostType = ghostType;
	}

	@Basic
	@Column(name = "STP_VW_EMPLOYEE_ID")
	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	@Basic
	@Column(name = "NON_EMPLOYEE_NAME")
	public String getNonEmployeeName() {
		return nonEmployeeName;
	}

	public void setNonEmployeeName(String nonEmployeeName) {
		this.nonEmployeeName = nonEmployeeName;
	}

	@Basic
	@Column(name = "NON_EMPLOYEE_SOCIAL_ID")
	public String getNonEmployeeSocialId() {
		return nonEmployeeSocialId;
	}

	public void setNonEmployeeSocialId(String nonEmployeeSocialId) {
		this.nonEmployeeSocialId = nonEmployeeSocialId;
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
	@Column(name = "OWNER_SOCIAL_ID")
	public String getOwnerSocialId() {
		return ownerSocialId;
	}

	public void setOwnerSocialId(String ownerSocialId) {
		this.ownerSocialId = ownerSocialId;
	}

	@Basic
	@Column(name = "ALIAS_NAME")
	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	@Basic
	@Column(name = "DOMAIN_ID_DATA_SOURCE_ID")
	public Long getDomainIdDataSource() {
		return domainIdDataSource;
	}

	public void setDomainIdDataSource(Long domainIdDataSource) {
		this.domainIdDataSource = domainIdDataSource;
	}

	@Basic
	@Column(name = "COORDINATE_LAT")
	public Double getCoordinateLatitude() {
		return coordinateLatitude;
	}

	public void setCoordinateLatitude(Double coordinateLatitude) {
		this.coordinateLatitude = coordinateLatitude;
	}

	@Basic
	@Column(name = "COORDINATE_LONG")
	public Double getCoordinateLongitude() {
		return coordinateLongitude;
	}

	public void setCoordinateLongitude(Double coordinateLongitude) {
		this.coordinateLongitude = coordinateLongitude;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FOLLOW_UP_START_DATE")
	public Date getFollowUpStartDate() {
		return followUpStartDate;
	}

	public void setFollowUpStartDate(Date followUpStartDate) {
		this.followUpStartDate = followUpStartDate;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FOLLOW_UP_END_DATE")
	public Date getFollowUpEndDate() {
		return followUpEndDate;
	}

	public void setFollowUpEndDate(Date followUpEndDate) {
		this.followUpEndDate = followUpEndDate;
	}

	@Basic
	@Column(name = "FOLLOW_UP_PERIOD")
	public Integer getFollowUpPeriod() {
		return followUpPeriod;
	}

	public void setFollowUpPeriod(Integer followUpPeriod) {
		this.followUpPeriod = followUpPeriod;
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
	@Column(name = "LOCATION")
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Basic
	@Column(name = "SECTOR_ID")
	public Long getSectorId() {
		return sectorId;
	}

	public void setSectorId(Long sectorId) {
		this.sectorId = sectorId;
	}

	@Basic
	@Column(name = "DOMAIN_ID_NETWORK_ROLE_ID")
	public Long getDomainIdNetworkRole() {
		return domainIdNetworkRole;
	}

	public void setDomainIdNetworkRole(Long domainIdNetworkRole) {
		this.domainIdNetworkRole = domainIdNetworkRole;
	}

	@Basic
	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Column(name = "DIRECT_FLAG")
	public Boolean getDirectFlag() {
		return directFlag;
	}

	public void setDirectFlag(Boolean directFlag) {
		this.directFlag = directFlag;
	}

	@Basic
	@Column(name = "DOMAIN_ID_MONTR_REASON_ID")
	public Long getDomainIdMontrReason() {
		return domainIdMontrReason;
	}

	public void setDomainIdMontrReason(Long domainIdMontrReason) {
		this.domainIdMontrReason = domainIdMontrReason;
	}

	@Basic
	@Column(name = "DOMAIN_ID_CASES_TYPE_ID")
	public Long getDomainIdCasesType() {
		return domainIdCasesType;
	}

	public void setDomainIdCasesType(Long domainIdCasesType) {
		this.domainIdCasesType = domainIdCasesType;
	}

	@Basic
	@Column(name = "DOMAIN_ID_EQUIPMENT_TYPE_ID")
	public Long getDomainIdEquipmentType() {
		return domainIdEquipmentType;
	}

	public void setDomainIdEquipmentType(Long domainIdEquipmentType) {
		this.domainIdEquipmentType = domainIdEquipmentType;
	}

	@Basic
	@Column(name = "STATUS")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Basic
	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Column(name = "ACTIVE_FLAG")
	public Boolean getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(Boolean activeFlag) {
		this.activeFlag = activeFlag;
	}

	@Basic
	@Column(name = "FOLLOW_UP_NETWORK_ID")
	public Long getFollowUpNetworkId() {
		return followUpNetworkId;
	}

	public void setFollowUpNetworkId(Long followUpNetworkId) {
		this.followUpNetworkId = followUpNetworkId;
	}

	@Basic
	@Column(name = "WF_INSTANCE_ID")
	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	@Basic
	@Column(name = "GHOST_CATEGORY")
	public Integer getGhostCategory() {
		return ghostCategory;
	}

	public void setGhostCategory(Integer ghostCategory) {
		this.ghostCategory = ghostCategory;
	}

	@Basic
	@Column(name = "INFO_SOURCE_DESCRIPTION")
	public String getInformationSource() {
		return informationSource;
	}

	public void setInformationSource(String informationSource) {
		this.informationSource = informationSource;
	}
}
