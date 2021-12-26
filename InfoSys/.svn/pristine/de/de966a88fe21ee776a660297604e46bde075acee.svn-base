package com.code.dal.orm.finance;

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
	 @NamedQuery(name  = "finMonthlyRewardResourceData_searchFinMonthlyRewardResourceData", 
			 	 query = " select  dtl " + 
	           	 		 " from FinMonthlyRewardResourceData dtl" +
	           			 " where  (:P_AGENT_ID = -1 or dtl.assignmentDetailId = :P_AGENT_ID )" +
	           	 		 " and (:P_OFFICER_ID = -1 or dtl.agentOfficerId = :P_OFFICER_ID )" +
	           	 		 " and (:P_IDENTITY = -1 or dtl.agentIdentity = :P_IDENTITY )" +
	           	 		 " and (:P_REGION_ID = -1 or dtl.agentRegionId = :P_REGION_ID )" +
	           	 		 " and (:P_SECTOR_ID = -1 or dtl.agentSectorId = :P_SECTOR_ID )" +
	           	 		 " and (:P_MONTH = -1 or dtl.month = :P_MONTH )" +
	           	 		 " and (:P_YEAR = -1 or dtl.year = :P_YEAR )" +
	           	 		 " and (:P_HIJRI_YEAR = -1 or dtl.hijriYear = :P_HIJRI_YEAR )" +
	           	 		 " and (:P_MONTHLY_REWARD_STATUS = -1 or dtl.monthlyRewardStatus = :P_MONTHLY_REWARD_STATUS )" +
	           	 		 " order by dtl.id "
			 	),
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_MONTHLY_REWRD_DEPT_DTLS")
public class FinMonthlyRewardResourceData extends BaseEntity implements Serializable {
	private Long id;
	private Long monthlyRewardDeptDetailId;
	private Long assignmentDetailId;
	private Boolean received;
	private String agentCode;
	private String agentName;
	private Double agentMonthlyReward;
	private Integer agentPaymentMethod;
	private String agentIban;
	private Long agentSectorId;
	private String agentSectorName;
	private Long agentRegionId;
	private String agentRegionName;
	private Long agentOfficerId;
	private String agentIdentity;
	private String agentOfficerName;
	private Integer month;
	private Integer hijriYear;
	private Integer year;
	private Integer monthlyRewardStatus;
	private Date rewardRequestDate;
	private String rewardRequestDateString;

	private FinMonthlyRewardResource finMonthlyRewardResource;

	public FinMonthlyRewardResourceData() {
		this.finMonthlyRewardResource = new FinMonthlyRewardResource();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return finMonthlyRewardResource.getId();
	}

	public void setId(Long id) {
		this.finMonthlyRewardResource.setId(id);
		this.id = id;
	}

	@Basic
	@Column(name = "MONTHLY_REWARD_DEPT_DETAILS_ID")
	public Long getMonthlyRewardDeptDetailId() {
		return monthlyRewardDeptDetailId;
	}

	public void setMonthlyRewardDeptDetailId(Long monthlyRewardDeptDetailId) {
		this.finMonthlyRewardResource.setMonthlyRewardDeptDetailId(monthlyRewardDeptDetailId);
		this.monthlyRewardDeptDetailId = monthlyRewardDeptDetailId;
	}

	@Basic
	@Column(name = "ASSIGNMENT_DETAILS_ID")
	public Long getAssignmentDetailId() {
		return assignmentDetailId;
	}

	public void setAssignmentDetailId(Long assignmentDetailId) {
		this.finMonthlyRewardResource.setAssignmentDetailId(assignmentDetailId);
		this.assignmentDetailId = assignmentDetailId;
	}

	@Basic
	@Column(name = "RECEIVED")
	public Boolean getReceived() {
		return received;
	}

	public void setReceived(Boolean received) {
		this.finMonthlyRewardResource.setReceived(received);
		this.received = received;
	}

	@Basic
	@Column(name = "AGENT_CODE")
	public String getAgentCode() {
		return agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	@Basic
	@Column(name = "AGENT_NAME")
	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	@Basic
	@Column(name = "AGENT_MONTHLY_REWARD")
	public Double getAgentMonthlyReward() {
		return agentMonthlyReward;
	}

	public void setAgentMonthlyReward(Double agentMonthlyReward) {
		this.agentMonthlyReward = agentMonthlyReward;
	}

	@Basic
	@Column(name = "AGENT_PAYMENT_METHOD")
	public Integer getAgentPaymentMethod() {
		return agentPaymentMethod;
	}

	public void setAgentPaymentMethod(Integer agentPaymentMethod) {
		this.agentPaymentMethod = agentPaymentMethod;
	}

	@Basic
	@Column(name = "AGENT_IBAN")
	public String getAgentIban() {
		return agentIban;
	}

	public void setAgentIban(String agentIban) {
		this.agentIban = agentIban;
	}

	@Basic
	@Column(name = "AGENT_SECTOR_ID")
	public Long getAgentSectorId() {
		return agentSectorId;
	}

	public void setAgentSectorId(Long agentSectorId) {
		this.agentSectorId = agentSectorId;
	}

	@Basic
	@Column(name = "AGENT_SECTOR_NAME")
	public String getAgentSectorName() {
		return agentSectorName;
	}

	public void setAgentSectorName(String agentSectorName) {
		this.agentSectorName = agentSectorName;
	}

	@Basic
	@Column(name = "AGENT_REGION_ID")
	public Long getAgentRegionId() {
		return agentRegionId;
	}

	public void setAgentRegionId(Long agentRegionId) {
		this.agentRegionId = agentRegionId;
	}

	@Basic
	@Column(name = "AGENT_REGION_NAME")
	public String getAgentRegionName() {
		return agentRegionName;
	}

	public void setAgentRegionName(String agentRegionName) {
		this.agentRegionName = agentRegionName;
	}

	@Basic
	@Column(name = "AGENT_OFFICER_ID")
	public Long getAgentOfficerId() {
		return agentOfficerId;
	}

	public void setAgentOfficerId(Long agentOfficerId) {
		this.agentOfficerId = agentOfficerId;
	}
	
	@Basic
	@Column(name = "MONTH_NUMBER")
	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	@Basic
	@Column(name = "FIN_YEAR")
	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}
	
	@Basic
	@Column(name = "MONTHLY_REWARD_STATUS")
	public Integer getMonthlyRewardStatus() {
		return monthlyRewardStatus;
	}

	public void setMonthlyRewardStatus(Integer monthlyRewardStatus) {
		this.monthlyRewardStatus = monthlyRewardStatus;
	}

	@Transient
	public FinMonthlyRewardResource getFinMonthlyRewardResource() {
		return finMonthlyRewardResource;
	}

	public void setFinMonthlyRewardResource(FinMonthlyRewardResource finMonthlyRewardResource) {
		this.finMonthlyRewardResource = finMonthlyRewardResource;
	}

	@Transient
	public String getAgentOfficerName() {
		return agentOfficerName;
	}

	public void setAgentOfficerName(String agentOfficerName) {
		this.agentOfficerName = agentOfficerName;
	}
	@Basic
	@Column(name = "HIJRI_YEAR")
	public Integer getHijriYear() {
		return hijriYear;
	}

	public void setHijriYear(Integer hijriYear) {
		this.hijriYear = hijriYear;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REQUEST_DATE")
	public Date getRewardRequestDate() {
		return rewardRequestDate;
	}

	public void setRewardRequestDate(Date rewardRequestDate) {
		this.rewardRequestDateString = HijriDateService.getHijriDateString(rewardRequestDate);
		this.rewardRequestDate = rewardRequestDate;
	}

	@Transient
	public String getRewardRequestDateString() {
		return rewardRequestDateString;
	}

	@Basic
	@Column(name = "AGENT_IDENTITY")
	public String getAgentIdentity() {
		return agentIdentity;
	}

	public void setAgentIdentity(String agentIdentity) {
		this.agentIdentity = agentIdentity;
	}
}
