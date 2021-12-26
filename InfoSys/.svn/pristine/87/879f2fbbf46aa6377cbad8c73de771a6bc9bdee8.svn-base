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

import com.code.dal.orm.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_SC_FOLLOW_UP_RESULTS")
public class FollowUpResult extends BaseEntity implements Serializable {
	private Long id;
	private Long domainIdFollowResults;
	private Date resultDate;
	private Long domainIdResultRegion;
	private String resultTime;
	private Long domainIdTransType;
	private String resultDetails;
	private Long followUpId;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_SC_RESULTS_SEQ", sequenceName = "FIS_SC_RESULTS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_SC_RESULTS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "DOMAIN_ID_FOLLOW_RESULTS_ID")
	public Long getDomainIdFollowResults() {
		return domainIdFollowResults;
	}

	public void setDomainIdFollowResults(Long domainIdFollowResults) {
		this.domainIdFollowResults = domainIdFollowResults;
	}

	@Basic
	@Column(name = "DOMAIN_ID_RESULT_REGION_ID")
	public Long getDomainIdResultRegion() {
		return domainIdResultRegion;
	}

	public void setDomainIdResultRegion(Long domainIdResultRegion) {
		this.domainIdResultRegion = domainIdResultRegion;
	}

	@Basic
	@Column(name = "RESULT_TIME")
	public String getResultTime() {
		return resultTime;
	}

	public void setResultTime(String resultTime) {
		this.resultTime = resultTime;
	}

	@Basic
	@Column(name = "DOMAIN_ID_TRANS_TYPES_ID")
	public Long getDomainIdTransType() {
		return domainIdTransType;
	}

	public void setDomainIdTransType(Long domainIdTransType) {
		this.domainIdTransType = domainIdTransType;
	}

	@Basic
	@Column(name = "RESULT_DETAILS")
	public String getResultDetails() {
		return resultDetails;
	}

	public void setResultDetails(String resultDetails) {
		this.resultDetails = resultDetails;
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
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RESULT_DATE")
	public Date getResultDate() {
		return resultDate;
	}

	public void setResultDate(Date resultDate) {
		this.resultDate = resultDate;
	}
}
