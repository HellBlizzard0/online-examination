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
import com.code.dal.orm.BaseEntity;
import com.code.services.util.HijriDateService;

@NamedQueries({
	 @NamedQuery(name = "followUpResultData_searchFollowUpResultData", 
		         query= " select followUpRes " +
		                " from FollowUpResultData followUpRes" +
		                " where (:P_FOLLOW_UP_ID = -1 or :P_FOLLOW_UP_ID = followUpRes.followUpId)" + 
		                " and (:P_ID = -1 or :P_ID = followUpRes.id) " +
		                " and (:P_DOMAIN_ID_FOLLOW_RESULTS = -1 or :P_DOMAIN_ID_FOLLOW_RESULTS = followUpRes.domainIdFollowResults) " +
		                " and (:P_RESULT_DETAILS = '-1' or :P_RESULT_DETAILS = followUpRes.resultDetails) " +
		                " order by followUpRes.id desc "
		       )
})


@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_SC_VW_FOLLOW_UP_RESULTS")
public class FollowUpResultData extends BaseEntity implements Serializable {
	private Long id;
	private Long domainIdFollowResults;
	private String domainIdFollowResultsDesc;
	private Date resultDate;
	private String resultDateString;
	private Long domainIdResultRegion;
	private String domainIdResultRegionDesc;
	private String resultTime;
	private Long domainIdTransType;
	private String domainIdTransTypeDesc;
	private String resultDetails;
	private Long followUpId;
	private FollowUpResult result;

	public FollowUpResultData() {
		this.result = new FollowUpResult();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.result.setId(id);
		this.id = id;
	}

	@Basic
	@Column(name = "DOMAIN_FOLLOW_RESULT_ID")
	public Long getDomainIdFollowResults() {
		return domainIdFollowResults;
	}

	public void setDomainIdFollowResults(Long domainIdFollowResults) {
		this.result.setDomainIdFollowResults(domainIdFollowResults);
		this.domainIdFollowResults = domainIdFollowResults;
	}

	@Basic
	@Column(name = "DOMAIN_FOLLOW_RESULT_DESC")
	public String getDomainIdFollowResultsDesc() {
		return domainIdFollowResultsDesc;
	}

	public void setDomainIdFollowResultsDesc(String domainIdFollowResultsDesc) {
		this.domainIdFollowResultsDesc = domainIdFollowResultsDesc;
	}

	@Basic
	@Column(name = "DOMAIN_RESULT_REGION_ID")
	public Long getDomainIdResultRegion() {
		return domainIdResultRegion;
	}

	public void setDomainIdResultRegion(Long domainIdResultRegion) {
		this.result.setDomainIdResultRegion(domainIdResultRegion);
		this.domainIdResultRegion = domainIdResultRegion;
	}

	@Basic
	@Column(name = "DOMAIN_RESULT_REGION_DESC")
	public String getDomainIdResultRegionDesc() {
		return domainIdResultRegionDesc;
	}

	public void setDomainIdResultRegionDesc(String domainIdResultRegionDesc) {
		this.domainIdResultRegionDesc = domainIdResultRegionDesc;
	}

	@Basic
	@Column(name = "RESULT_TIME")
	public String getResultTime() {
		return resultTime;
	}

	public void setResultTime(String resultTime) {
		this.result.setResultTime(resultTime);
		this.resultTime = resultTime;
	}

	@Basic
	@Column(name = "DOMAIN_TRANS_TYPE_ID")
	public Long getDomainIdTransType() {
		return domainIdTransType;
	}

	public void setDomainIdTransType(Long domainIdTransType) {
		this.result.setDomainIdTransType(domainIdTransType);
		this.domainIdTransType = domainIdTransType;
	}

	@Basic
	@Column(name = "DOMAIN_TRANS_TYPE_DESC")
	public String getDomainIdTransTypeDesc() {
		return domainIdTransTypeDesc;
	}

	public void setDomainIdTransTypeDesc(String domainIdTransTypeDesc) {
		this.domainIdTransTypeDesc = domainIdTransTypeDesc;
	}

	@Basic
	@Column(name = "RESULT_DETAILS")
	public String getResultDetails() {
		return resultDetails;
	}

	public void setResultDetails(String resultDetails) {
		this.result.setResultDetails(resultDetails);
		this.resultDetails = resultDetails;
	}

	@Basic
	@Column(name = "FOLLOW_UP_ID")
	public Long getFollowUpId() {
		return followUpId;
	}

	public void setFollowUpId(Long followUpId) {
		this.result.setFollowUpId(followUpId);
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
		this.resultDateString = HijriDateService.getHijriDateString(resultDate);
		this.result.setResultDate(resultDate);
	}

	@Transient
	public String getResultDateString() {
		return resultDateString;
	}

	public void setResultDateString(String resultDateString) {
		this.resultDateString = resultDateString;
		this.setResultDate(HijriDateService.getHijriDate(resultDateString));
	}

	@Transient
	public FollowUpResult getResult() {
		return result;
	}

	public void setResult(FollowUpResult result) {
		this.result = result;
	}
}
