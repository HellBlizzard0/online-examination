package com.code.dal.orm.assignment;

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
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.code.dal.audit.DeleteableAuditEntity;
import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;
import com.code.services.util.HijriDateService;

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_ASSIGNMENTS_DETAILS")
public class AssignmentDetail extends AuditEntity implements Serializable, DeleteableAuditEntity, InsertableAuditEntity, UpdateableAuditEntity {
	private Long id;
	private Long assignmentId;
	private Date startDate;
	private String startDateString;
	private Integer period;
	private String agentCode;
	private String workScope;
	private String agentClass;
	private Double monthlyReward;
	private Integer paymentMethod;
	private Integer location;
	private String rank;
	private String identity;
	private String fullName;
	private String workPlace;
	private String workNature;
	private Date birthDate;
	private String birthDateString;
	private String primaryPhone;
	private String secondaryPhone;
	private String ternaryPhone;
	private Long bankBranchId;
	private String iban;
	private Integer agentType;
	private Long countryId;
	private Long employeeId;
	private String employeeSpecialNumber;
	private String employeeNumber;
	private String reasons;
	private Boolean eliminated;
	private Date endDate;
	private String endDateString;
	private Date approvedEndDate;
	private String approvedEndDateString;
	private Integer status;
	private Integer type;
	private Integer typeTemp;
	private String agentClassTemp;
	private Double monthlyRewardTemp;
	private Integer paymentMethodTemp;
	private Long bankBranchIdTemp;
	private String ibanTemp;
	private String fullNameTemp;
	private Boolean active;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_ASSIGNMENTS_DETAILS_SEQ", sequenceName = "FIS_ASSIGNMENTS_DETAILS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_ASSIGNMENTS_DETAILS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "ASSIGNMENTS_ID")
	public Long getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(Long assignmentId) {
		this.assignmentId = assignmentId;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDateString = HijriDateService.getHijriDateString(startDate);
		this.startDate = startDate;
	}

	@Transient
	public String getStartDateString() {
		return startDateString;
	}

	@Basic
	@Column(name = "PERIOD")
	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
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
	@Column(name = "WORK_SCOPE")
	public String getWorkScope() {
		return workScope;
	}

	public void setWorkScope(String workScope) {
		this.workScope = workScope;
	}

	@Basic
	@Column(name = "AGENT_CLASS")
	public String getAgentClass() {
		return agentClass;
	}

	public void setAgentClass(String agentClass) {
		this.agentClass = agentClass;
	}

	@Basic
	@Column(name = "MONTHLY_REWARD")
	public Double getMonthlyReward() {
		return monthlyReward;
	}

	public void setMonthlyReward(Double monthlyReward) {
		this.monthlyReward = monthlyReward;
	}

	@Basic
	@Column(name = "PAYMENT_METHOD")
	public Integer getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(Integer paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	@Basic
	@Column(name = "LOCATION")
	public Integer getLocation() {
		return location;
	}

	public void setLocation(Integer location) {
		this.location = location;
	}

	@Basic
	@Column(name = "RANK")
	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	@Basic
	@Column(name = "IDENTITY_NUM")
	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	@Basic
	@Column(name = "FULL_NAME")
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Basic
	@Column(name = "WORK_PLACE")
	public String getWorkPlace() {
		return workPlace;
	}

	public void setWorkPlace(String workPlace) {
		this.workPlace = workPlace;
	}

	@Basic
	@Column(name = "WORK_NATURE")
	public String getWorkNature() {
		return workNature;
	}

	public void setWorkNature(String workNature) {
		this.workNature = workNature;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BIRTH_DATE")
	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDateString = HijriDateService.getHijriDateString(birthDate);
		this.birthDate = birthDate;
	}

	@Transient
	public String getBirthDateString() {
		return birthDateString;
	}

	@Basic
	@Column(name = "PRIMARY_PHONE")
	public String getPrimaryPhone() {
		return primaryPhone;
	}

	public void setPrimaryPhone(String primaryPhone) {
		this.primaryPhone = primaryPhone;
	}

	@Basic
	@Column(name = "SECONDARY_PHONE")
	public String getSecondaryPhone() {
		return secondaryPhone;
	}

	public void setSecondaryPhone(String secondaryPhone) {
		this.secondaryPhone = secondaryPhone;
	}

	@Basic
	@Column(name = "TERNARY_PHONE")
	public String getTernaryPhone() {
		return ternaryPhone;
	}

	public void setTernaryPhone(String ternaryPhone) {
		this.ternaryPhone = ternaryPhone;
	}

	@Basic
	@Column(name = "STP_VW_BANKS_BRANCHES_ID")
	public Long getBankBranchId() {
		return bankBranchId;
	}

	public void setBankBranchId(Long bankBranchId) {
		this.bankBranchId = bankBranchId;
	}

	@Basic
	@Column(name = "IBAN")
	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	@Basic
	@Column(name = "AGENT_TYPE")
	public Integer getAgentType() {
		return agentType;
	}

	public void setAgentType(Integer agentType) {
		this.agentType = agentType;
	}

	@Basic
	@Column(name = "STP_VW_COUNTRIES_ID")
	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	@Basic
	@Column(name = "STP_VW_EMPLOYEES_ID")
	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	@Basic
	@Column(name = "EMP_SPECIAL_NUMBER")
	public String getEmployeeSpecialNumber() {
		return employeeSpecialNumber;
	}

	public void setEmployeeSpecialNumber(String employeeSpecialNumber) {
		this.employeeSpecialNumber = employeeSpecialNumber;
	}

	@Basic
	@Column(name = "EMPLOYEE_NUMBER")
	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	@Override
	public Long calculateContentId() {
		return this.id;
	}

	@Basic
	@Column(name = "REASONS")
	public String getReasons() {
		return reasons;
	}

	public void setReasons(String reasons) {
		this.reasons = reasons;
	}

	@Basic
	@Column(name = "END_DATE")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDateString = HijriDateService.getHijriDateString(endDate);
		this.endDate = endDate;
	}

	@Transient
	public String getEndDateString() {
		return endDateString;
	}

	@Basic
	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Column(name = "ELIMINATED")
	public Boolean getEliminated() {
		return eliminated;
	}

	public void setEliminated(Boolean eliminated) {
		this.eliminated = eliminated;
	}

	@Basic
	@Column(name = "APPROVED_END_DATE")
	public Date getApprovedEndDate() {
		return approvedEndDate;
	}

	public void setApprovedEndDate(Date approvedEndDate) {
		this.approvedEndDateString = HijriDateService.getHijriDateString(approvedEndDate);
		this.approvedEndDate = approvedEndDate;
	}

	@Transient
	public String getApprovedEndDateString() {
		return approvedEndDateString;
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
	@Column(name = "TYPE")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	@Basic
	@Column(name = "TYPE_TEMP")
	public Integer getTypeTemp() {
		return typeTemp;
	}

	public void setTypeTemp(Integer typeTemp) {
		this.typeTemp = typeTemp;
	}

	@Basic
	@Column(name = "AGENT_CLASS_TEMP")
	public String getAgentClassTemp() {
		return agentClassTemp;
	}

	public void setAgentClassTemp(String agentClassTemp) {
		this.agentClassTemp = agentClassTemp;
	}

	@Basic
	@Column(name = "MONTHLY_REWARD_TEMP")
	public Double getMonthlyRewardTemp() {
		return monthlyRewardTemp;
	}

	public void setMonthlyRewardTemp(Double monthlyRewardTemp) {
		this.monthlyRewardTemp = monthlyRewardTemp;
	}

	@Basic
	@Column(name = "PAYMENT_METHOD_TEMP")
	public Integer getPaymentMethodTemp() {
		return paymentMethodTemp;
	}

	public void setPaymentMethodTemp(Integer paymentMethodTemp) {
		this.paymentMethodTemp = paymentMethodTemp;
	}

	@Basic
	@Column(name = "STP_VW_BANKS_BRANCHES_ID_TEMP")
	public Long getBankBranchIdTemp() {
		return bankBranchIdTemp;
	}

	public void setBankBranchIdTemp(Long bankBranchIdTemp) {
		this.bankBranchIdTemp = bankBranchIdTemp;
	}

	@Basic
	@Column(name = "IBAN_TEMP")
	public String getIbanTemp() {
		return ibanTemp;
	}

	public void setIbanTemp(String ibanTemp) {
		this.ibanTemp = ibanTemp;
	}
	
	@Basic                     
	@Column(name = "FULL_NAME_TEMP")
	public String getFullNameTemp() {
		return fullNameTemp;
	}

	public void setFullNameTemp(String fullNameTemp) {
		this.fullNameTemp = fullNameTemp;
	}


	@Transient
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}