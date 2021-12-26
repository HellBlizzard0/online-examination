package com.code.dal.orm.securityanalysis;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.code.dal.orm.BaseEntity;
import com.code.services.util.HijriDateService;


@NamedQueries({
	 @NamedQuery(name = "followUpData_searchLastFollowUpData", 
		         query= " select followUp " +
		                " from FollowUpData followUp " +
		                " where (:P_CONTACT_NUMBER = '-1' or :P_CONTACT_NUMBER = followUp.contactNumber)" + 
		                " and (:P_CODE = '-1' or :P_CODE = followUp.code) " +
		                " order by followUp.id desc "
			 	),
	 @NamedQuery(name = "followUpData_searchFollowUpData", 
			     query= " select followUp " +
			            " from FollowUpData followUp " +
			            " where (:P_ID = -1 or followUp.id = :P_ID)" + 
			            " and (:P_CONTACT_NUMBER = '-1' or followUp.contactNumber = :P_CONTACT_NUMBER) " +
			            " and (:P_NETWORK_ID = -1 or followUp.followUpNetworkId = :P_NETWORK_ID) " +
			            " and (:P_CODE = '-1' or followUp.code = :P_CODE) " +
			            " and (:P_GHOST_NAME = '-1' or followUp.employeeName like :P_GHOST_NAME or followUp.nonEmployeeName like :P_GHOST_NAME) " +
			            " and (:P_GHOST_SOCIAL_ID = '-1' or followUp.employeeSocialId like :P_GHOST_SOCIAL_ID or followUp.nonEmployeeSocialId like :P_GHOST_SOCIAL_ID) " +
			            " and (:P_EQUIPMENT_TYPE_ID = -1 or :P_EQUIPMENT_TYPE_ID = followUp.domainIdEquipmentType) " +
			            " and (:P_DIRECT_FLAG = -1 or :P_DIRECT_FLAG = followUp.directFlag) " +
			            " and (:P_STATUS = -1 or :P_STATUS = followUp.status) " +
			            " and (:P_ACTIVE_FLAG = -1 or :P_ACTIVE_FLAG = followUp.activeFlag) " +
			            " and (:P_END_DATE_FROM_NULL = 1 or PKG_CHAR_TO_DATE(:P_END_DATE_FROM, 'MI/MM/YYYY') <= followUp.followUpEndDate) " + 
			            " and (:P_END_DATE_TO_NULL = 1 or PKG_CHAR_TO_DATE(:P_END_DATE_TO, 'MI/MM/YYYY') >= followUp.followUpEndDate) " +
			            " order by followUp.followUpStartDate desc"
			 	),
	 @NamedQuery(name = "followUpData_currentFollowUpData", 
			     query= " select followUp " +
			            " from FollowUpData followUp " +
			            " where (:P_ID = -1 or followUp.id = :P_ID)" + 
			            " and (:P_CONTACT_NUMBER = '-1' or followUp.contactNumber = :P_CONTACT_NUMBER) " +
			            " and (:P_NETWORK_ID = -1 or followUp.followUpNetworkId = :P_NETWORK_ID) " +
			            " and (:P_CODE = '-1' or followUp.code = :P_CODE) " +
			            " and (:P_GHOST_NAME = '-1' or followUp.employeeName like :P_GHOST_NAME or followUp.nonEmployeeName like :P_GHOST_NAME) " +
			            " and (:P_GHOST_SOCIAL_ID = '-1' or followUp.employeeSocialId like :P_GHOST_SOCIAL_ID or followUp.nonEmployeeSocialId like :P_GHOST_SOCIAL_ID) " +
			            " and (:P_EQUIPMENT_TYPE_ID = -1 or :P_EQUIPMENT_TYPE_ID = followUp.domainIdEquipmentType) " +
			            " and (:P_DIRECT_FLAG = -1 or :P_DIRECT_FLAG = followUp.directFlag) " +
			            " and (:P_STATUS = -1 or :P_STATUS = followUp.status) " +
			            " and (:P_ACTIVE_FLAG = -1 or :P_ACTIVE_FLAG = followUp.activeFlag) " +
			            " and (PKG_CHAR_TO_DATE(:P_TODAY, 'MI/MM/YYYY') <= followUp.followUpEndDate) " + 
			            " and (PKG_CHAR_TO_DATE(:P_TODAY, 'MI/MM/YYYY') >= followUp.followUpStartDate)) " +
			            " order by followUp.followUpStartDate desc"
 	)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_SC_VW_FOLLOW_UPS")
public class FollowUpData extends BaseEntity implements Serializable, Cloneable {
	private Long id;
	private String contactNumber;
	private String code;
	private Integer ghostType;
	private Integer ghostCategory;
	private String ownerName;
	private String ownerSocialId;
	private Long employeeId;
	private String employeeName;
	private String employeeSocialId;
	private String nonEmployeeName;
	private String nonEmployeeSocialId;
	private String aliasName;
	private Long domainIdDataSource;
	private String informationSource;
	private String dataSourceDesc;
	private Double coordinateLatitude;
	private Double coordinateLongitude;
	private Date followUpStartDate;
	private String followUpStartDateString;
	private Date followUpEndDate;
	private String followUpEndDateString;
	private Integer followUpPeriod;
	private Long regionId;
	private String regionName;
	private Integer regionType;
	private String followUplocation;
	private Long sectorId;
	private String sectorName;
	private Long domainIdNetworkRole;
	private String networkRoleDesc;
	private Boolean directFlag;
	private Long domainIdMontrReason;
	private String montrReasonDesc;
	private Long domainIdCasesType;
	private String casesTypeDesc;
	private Long domainIdEquipmentType;
	private String equipmentTypeDesc;
	private Integer status;
	private Boolean activeFlag;
	private Long followUpNetworkId;
	private String followUpNetworkNum;
	private String followUpNetworkDesc;
	private Long instanceId;

	private FollowUp followUp;

	public FollowUpData() {
		this.followUp = new FollowUp();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.followUp.setId(id);
		this.id = id;
	}
	
	@Basic
	@Column(name = "CONTACT_NUMBER")
	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.followUp.setContactNumber(contactNumber);
		this.contactNumber = contactNumber;
	}

	@Basic
	@Column(name = "CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.followUp.setCode(code);
		this.code = code;
	}

	@Basic
	@Column(name = "GHOST_TYPE")
	public Integer getGhostType() {
		return ghostType;
	}

	public void setGhostType(Integer ghostType) {
		this.followUp.setGhostType(ghostType);
		this.ghostType = ghostType;
	}

	@Basic
	@Column(name = "OWNER_NAME")
	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.followUp.setOwnerName(ownerName);
		this.ownerName = ownerName;
	}

	@Basic
	@Column(name = "OWNER_SOCIAL_ID")
	public String getOwnerSocialId() {
		return ownerSocialId;
	}

	public void setOwnerSocialId(String ownerSocialId) {
		this.followUp.setOwnerSocialId(ownerSocialId);
		this.ownerSocialId = ownerSocialId;
	}

	@Basic
	@Column(name = "STP_VW_EMPLOYEE_ID")
	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.followUp.setEmployeeId(employeeId);
		this.employeeId = employeeId;
	}

	@Basic
	@Column(name = "EMPLOYEE_NAME")
	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	@Basic
	@Column(name = "EMPLOYEE_SOCIAL_ID")
	public String getEmployeeSocialId() {
		return employeeSocialId;
	}

	public void setEmployeeSocialId(String employeeSocialId) {
		this.employeeSocialId = employeeSocialId;
	}

	@Basic
	@Column(name = "NON_EMPLOYEE_NAME")
	public String getNonEmployeeName() {
		return nonEmployeeName;
	}

	public void setNonEmployeeName(String nonEmployeeName) {
		this.followUp.setNonEmployeeName(nonEmployeeName);
		this.nonEmployeeName = nonEmployeeName;
	}

	@Basic
	@Column(name = "NON_EMPLOYEE_SOCIAL_ID")
	public String getNonEmployeeSocialId() {
		return nonEmployeeSocialId;
	}

	public void setNonEmployeeSocialId(String nonEmployeeSocialId) {
		this.followUp.setNonEmployeeSocialId(nonEmployeeSocialId);
		this.nonEmployeeSocialId = nonEmployeeSocialId;
	}

	@Basic
	@Column(name = "ALIAS_NAME")
	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.followUp.setAliasName(aliasName);
		this.aliasName = aliasName;
	}

	@Basic
	@Column(name = "DOMAIN_ID_DATA_SOURCE_ID")
	public Long getDomainIdDataSource() {
		return domainIdDataSource;
	}

	public void setDomainIdDataSource(Long domainIdDataSource) {
		this.followUp.setDomainIdDataSource(domainIdDataSource);
		this.domainIdDataSource = domainIdDataSource;
	}

	@Basic
	@Column(name = "DATA_SOURCES_DESCRIPTION")
	public String getDataSourceDesc() {
		return dataSourceDesc;
	}

	public void setDataSourceDesc(String dataSourceDesc) {
		this.dataSourceDesc = dataSourceDesc;
	}

	@Basic
	@Column(name = "COORDINATE_LAT")
	public Double getCoordinateLatitude() {
		return coordinateLatitude;
	}

	public void setCoordinateLatitude(Double coordinateLatitude) {
		this.followUp.setCoordinateLatitude(coordinateLatitude);
		this.coordinateLatitude = coordinateLatitude;
	}

	@Basic
	@Column(name = "COORDINATE_LONG")
	public Double getCoordinateLongitude() {
		return coordinateLongitude;
	}

	public void setCoordinateLongitude(Double coordinateLongitude) {
		this.followUp.setCoordinateLongitude(coordinateLongitude);
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
		this.followUpStartDateString = HijriDateService.getHijriDateString(followUpStartDate);
		this.followUp.setFollowUpStartDate(followUpStartDate);
	}

	@Transient
	public String getFollowUpStartDateString() {
		return followUpStartDateString;
	}

	public void setFollowUpStartDateString(String followUpStartDateString) {
		this.followUpStartDateString = followUpStartDateString;
		this.setFollowUpStartDate(HijriDateService.getHijriDate(followUpStartDateString));
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FOLLOW_UP_END_DATE")
	public Date getFollowUpEndDate() {
		return followUpEndDate;
	}

	public void setFollowUpEndDate(Date followUpEndDate) {
		this.followUpEndDate = followUpEndDate;
		this.followUpEndDateString = HijriDateService.getHijriDateString(followUpEndDate);
		this.followUp.setFollowUpEndDate(followUpEndDate);
	}

	@Transient
	public String getFollowUpEndDateString() {
		return followUpEndDateString;
	}

	public void setFollowUpEndDateString(String followUpEndDateString) {
		this.followUpEndDateString = followUpEndDateString;
		this.setFollowUpEndDate(HijriDateService.getHijriDate(followUpEndDateString));
	}

	@Basic
	@Column(name = "FOLLOW_UP_PERIOD")
	public Integer getFollowUpPeriod() {
		return followUpPeriod;
	}

	public void setFollowUpPeriod(Integer followUpPeriod) {
		this.followUp.setFollowUpPeriod(followUpPeriod);
		this.followUpPeriod = followUpPeriod;
	}

	@Basic
	@Column(name = "REGION_ID")
	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.followUp.setRegionId(regionId);
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

	@Basic
	@Column(name = "FOLLOW_UPS_LOCATION")
	public String getFollowUplocation() {
		return followUplocation;
	}

	public void setFollowUplocation(String followUplocation) {
		this.followUp.setLocation(followUplocation);
		this.followUplocation = followUplocation;
	}

	@Basic
	@Column(name = "SECTOR_ID")
	public Long getSectorId() {
		return sectorId;
	}

	public void setSectorId(Long sectorId) {
		this.followUp.setSectorId(sectorId);
		this.sectorId = sectorId;
	}

	@Basic
	@Column(name = "SECTOR_NAME")
	public String getSectorName() {
		return sectorName;
	}

	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}

	@Basic
	@Column(name = "DOMAIN_ID_NETWORK_ROLE_ID")
	public Long getDomainIdNetworkRole() {
		return domainIdNetworkRole;
	}

	public void setDomainIdNetworkRole(Long domainIdNetworkRole) {
		this.followUp.setDomainIdNetworkRole(domainIdNetworkRole);
		this.domainIdNetworkRole = domainIdNetworkRole;
	}

	@Basic
	@Column(name = "NETWORK_ROLES_DESCRIPTION")
	public String getNetworkRoleDesc() {
		return networkRoleDesc;
	}

	public void setNetworkRoleDesc(String networkRoleDesc) {
		this.networkRoleDesc = networkRoleDesc;
	}

	@Basic
	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Column(name = "DIRECT_FLAG")
	public Boolean getDirectFlag() {
		return directFlag;
	}

	public void setDirectFlag(Boolean directFlag) {
		this.followUp.setDirectFlag(directFlag);
		this.directFlag = directFlag;
	}

	@Basic
	@Column(name = "DOMAIN_ID_MONTR_REASON_ID")
	public Long getDomainIdMontrReason() {
		return domainIdMontrReason;
	}

	public void setDomainIdMontrReason(Long domainIdMontrReason) {
		this.followUp.setDomainIdMontrReason(domainIdMontrReason);
		this.domainIdMontrReason = domainIdMontrReason;
	}

	@Basic
	@Column(name = "MONTR_REAS_DESCRIPTION")
	public String getMontrReasonDesc() {
		return montrReasonDesc;
	}

	public void setMontrReasonDesc(String montrReasonDesc) {
		this.montrReasonDesc = montrReasonDesc;
	}

	@Basic
	@Column(name = "DOMAIN_ID_CASES_TYPE_ID")
	public Long getDomainIdCasesType() {
		return domainIdCasesType;
	}

	public void setDomainIdCasesType(Long domainIdCasesType) {
		this.followUp.setDomainIdCasesType(domainIdCasesType);
		this.domainIdCasesType = domainIdCasesType;
	}

	@Basic
	@Column(name = "CASES_TYPE_DESCRIPTION")
	public String getCasesTypeDesc() {
		return casesTypeDesc;
	}

	public void setCasesTypeDesc(String casesTypeDesc) {
		this.casesTypeDesc = casesTypeDesc;
	}

	@Basic
	@Column(name = "DOMAIN_ID_EQUIPMENT_TYPE_ID")
	public Long getDomainIdEquipmentType() {
		return domainIdEquipmentType;
	}

	public void setDomainIdEquipmentType(Long domainIdEquipmentType) {
		this.followUp.setDomainIdEquipmentType(domainIdEquipmentType);
		this.domainIdEquipmentType = domainIdEquipmentType;
	}

	@Basic
	@Column(name = "EQUIPMENT_TYPE_DESCRIPTION")
	public String getEquipmentTypeDesc() {
		return equipmentTypeDesc;
	}

	public void setEquipmentTypeDesc(String equipmentTypeDesc) {
		this.equipmentTypeDesc = equipmentTypeDesc;
	}

	@Basic
	@Column(name = "STATUS")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.followUp.setStatus(status);
		this.status = status;
	}

	@Basic
	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Column(name = "ACTIVE_FLAG")
	public Boolean getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(Boolean activeFlag) {
		this.followUp.setActiveFlag(activeFlag);
		this.activeFlag = activeFlag;
	}

	@Basic
	@Column(name = "FOLLOW_UP_NETWORK_ID")
	public Long getFollowUpNetworkId() {
		return followUpNetworkId;
	}

	public void setFollowUpNetworkId(Long followUpNetworkId) {
		this.followUp.setFollowUpNetworkId(followUpNetworkId);
		this.followUpNetworkId = followUpNetworkId;
	}

	@Basic
	@Column(name = "FOLLOW_UP_NETWORK_NUMBER")
	public String getFollowUpNetworkNum() {
		return followUpNetworkNum;
	}

	public void setFollowUpNetworkNum(String followUpNetworkNum) {
		this.followUpNetworkNum = followUpNetworkNum;
	}

	@Basic
	@Column(name = "FOLLOW_UP_NETWORK_DESCRIPTION")
	public String getFollowUpNetworkDesc() {
		return followUpNetworkDesc;
	}

	public void setFollowUpNetworkDesc(String followUpNetworkDesc) {
		this.followUpNetworkDesc = followUpNetworkDesc;
	}

	@Basic
	@Column(name = "WF_INSTANCE_ID")
	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
		this.followUp.setInstanceId(instanceId);
	}

	@Transient
	public FollowUp getFollowUp() {
		return followUp;
	}

	public void setFollowUp(FollowUp followUp) {
		this.followUp = followUp;
	}

	public Object clone() throws CloneNotSupportedException {
		FollowUpData clonObj = (FollowUpData) super.clone();
		clonObj.followUp = (FollowUp) this.followUp.clone();
		return clonObj;
	}

	@Basic
	@Column(name = "GHOST_CATEGORY")
	public Integer getGhostCategory() {
		return ghostCategory;
	}

	public void setGhostCategory(Integer ghostCategory) {
		this.followUp.setGhostCategory(ghostCategory);
		this.ghostCategory = ghostCategory;
	}

	
	@Basic
	@Column(name = "INFO_SOURCE_DESCRIPTION")
	public String getInformationSource() {
		return informationSource;
	}

	public void setInformationSource(String informationSource) {
		this.followUp.setInformationSource(informationSource);
		this.informationSource = informationSource;
	}

}
