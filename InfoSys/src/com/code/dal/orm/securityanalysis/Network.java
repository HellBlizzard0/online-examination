package com.code.dal.orm.securityanalysis;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.code.dal.orm.BaseEntity;
@NamedQuery(name = "networkData_searchNetworksSummary", 
			query= " select network " +
			       " from Network network " +
			       " where (:P_NETWORK_NUMBER = '-1' or network.networkNumber like :P_NETWORK_NUMBER) " +
			       " and (:P_DESCRIPTION = '-1' or network.description like :P_DESCRIPTION) " +
			       " order by network.id "
)

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_SC_NETWORKS")
public class Network extends BaseEntity implements Serializable {
	private Long id;
	private String networkNumber;
	private String description;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_SC_NETWORK_SEQ", sequenceName = "FIS_SC_NETWORK_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_SC_NETWORK_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "NETWORK_NUMBER")
	public String getNetworkNumber() {
		return networkNumber;
	}

	public void setNetworkNumber(String networkNumber) {
		this.networkNumber = networkNumber;
	}

	@Basic
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
