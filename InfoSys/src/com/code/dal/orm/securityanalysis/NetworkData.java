package com.code.dal.orm.securityanalysis;

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
	 @NamedQuery(name = "networkData_searchFollowUpNetworks", 
			     query= " select network " +
			            " from NetworkData network " +
			            " where (:P_REGION_ID = -1 or :P_REGION_ID = network.regionId) " + 
			            " and (:P_GHOST_NAME = '-1' or network.employeeName like :P_GHOST_NAME  or network.nonEmployeeName like :P_GHOST_NAME) " +
			            " and (:P_GHOST_SOCIAL_ID = '-1' or network.employeeSocialId like :P_GHOST_SOCIAL_ID or network.nonEmployeeSocialId like :P_GHOST_SOCIAL_ID) " +
			            " and (:P_NETWORK_ID = -1 or :P_NETWORK_ID = network.id) " +
			            " and (:P_FOLLOW_UP_ID = -1 or :P_FOLLOW_UP_ID = network.followUpId) " +
			            " and (:P_CONTACT_NUMBER = '-1' or network.contactNumber = :P_CONTACT_NUMBER) " +
			            " order by network.id "
 	 ),
	 @NamedQuery(name = "networkData_searchNetworks", 
			     query= " select network " +
			            " from NetworkData network " +
			            " where (:P_NETWORK_NUMBER = '-1' or network.networkNumber like :P_NETWORK_NUMBER) " +
			            " and (:P_DESCRIPTION = '-1' or network.description like :P_DESCRIPTION) " +
			            " order by network.id "
 	 ),
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_SC_VW_NETWORKS")
public class NetworkData extends BaseEntity implements Serializable {
	private Long id;
	private String networkNumber;
	private String description;
	private Long followUpId;
	private String contactNumber;
	private String employeeName;
	private String employeeSocialId;
	private String nonEmployeeName;
	private String nonEmployeeSocialId;
	private Long regionId;
	private Network network;

	public NetworkData() {
		network = new Network();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.network.setId(id);
		this.id = id;
	}

	@Basic
	@Column(name = "NETWORK_NUMBER")
	public String getNetworkNumber() {
		return networkNumber;
	}

	public void setNetworkNumber(String networkNumber) {
		this.network.setNetworkNumber(networkNumber);
		this.networkNumber = networkNumber;
	}

	@Basic
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.network.setDescription(description);
		this.description = description;
	}

	@Basic
	@Column(name = "FOLLOW_UP_ID")
	public Long getFollowUpId() {
		return followUpId;
	}

	public void setFollowUpId(Long followUpId) {
		this.followUpId = followUpId;
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
	@Column(name = "REGION_ID")
	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	@Transient
	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}
}
