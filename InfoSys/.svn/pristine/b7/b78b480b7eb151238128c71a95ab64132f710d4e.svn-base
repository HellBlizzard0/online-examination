package com.code.dal.orm.assignment;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
	@NamedQuery(name = "assignmentDetailData_searchInfoAssignmentDetailsData", 
            	 query= " select a " +
            	 		" from AssignmentDetailData a, InfoSource infoS " +
            	 		" where (infoS.infoId = :P_INFO_ID ) " +
            	 		" and (infoS.assignmentDetailId = a.id ) " +
            			" order by a.id "
 	),
 	
 	@NamedQuery(name = "assignmentDetailData_searchLastAssignmentDetailsData", 
				 query= " select a " +
				 		" from AssignmentDetailData a" +
				 		" where (a.approvedEndDate in (select max(ad.approvedEndDate) from AssignmentDetail ad where ad.identity = a.identity and ad.status = a.status group by ad.identity) ) " +
					    " and (:P_OFFICER_ID = -1 or a.officerId = :P_OFFICER_ID) " +
					    " and (:P_IDENTITY = '-1' or a.identity = :P_IDENTITY) " +
					    " and ( ( ( :P_START_DATE_NULL = 1 or a.approvedEndDate >= PKG_CHAR_TO_DATE (:P_START_DATE, 'MI/MM/YYYY') )" +
					    " and ( :P_APPROVED_END_DATE_NULL = 1 or a.approvedEndDate < PKG_CHAR_TO_DATE (:P_APPROVED_END_DATE, 'MI/MM/YYYY') ) )" +
					    " or ( ( :P_START_DATE_NULL = 1 or a.startDate >= PKG_CHAR_TO_DATE (:P_START_DATE, 'MI/MM/YYYY') )" +
					    " and ( :P_APPROVED_END_DATE_NULL = 1 or a.startDate < PKG_CHAR_TO_DATE (:P_APPROVED_END_DATE, 'MI/MM/YYYY') ) ) )" +
					    " and (:P_REGION_ID = -1 or a.regionId = :P_REGION_ID) " +
					    " and (:P_SECTOR_ID = -1 or a.sectorId = :P_SECTOR_ID) " +
					    " and (:P_UNIT_ID = -1 or a.unitId = :P_UNIT_ID) " +
					    " and (:P_ELIMINATED = -1 or a.eliminated = :P_ELIMINATED) " +
					    " and (:P_AGENT_CODE = '-1' or a.agentCode like :P_AGENT_CODE ) " +
					    " and (:P_FULL_NAME = '-1' or a.fullName like :P_FULL_NAME) " +
					    " and (:P_STATUS = -1 or a.status = :P_STATUS) " +
					    " and (:P_HELD_FLAG = -1 or (:P_HELD_FLAG = 0 or :P_HELD_FLAG = 2 or (a.approvedEndDate > PKG_CHAR_TO_DATE (:P_CURRENT_DATE, 'MI/MM/YYYY') and a.startDate <= PKG_CHAR_TO_DATE (:P_CURRENT_DATE, 'MI/MM/YYYY') ) )) " +
					    " and (:P_HELD_FLAG = -1 or (:P_HELD_FLAG = 1 or :P_HELD_FLAG = 2 or a.approvedEndDate < PKG_CHAR_TO_DATE (:P_CURRENT_DATE, 'MI/MM/YYYY'))) " +
					    " and (:P_HELD_FLAG = -1 or (:P_HELD_FLAG = 0 or :P_HELD_FLAG = 1 or a.approvedEndDate > PKG_CHAR_TO_DATE (:P_CURRENT_DATE, 'MI/MM/YYYY') and a.startDate > PKG_CHAR_TO_DATE (:P_CURRENT_DATE, 'MI/MM/YYYY'))) " +
					    " and (:P_IS_EMPLOYEE = -1 or ( (:P_IS_EMPLOYEE = 1 or a.employeeId is null) and (:P_IS_EMPLOYEE = 0 or a.employeeId is not null) ) ) " +
					    " and (:P_TYPE = -1 or a.type = :P_TYPE) " +
					    " order by a.startDate desc"
	),
			 	
	@NamedQuery(name = "assignmentDetailData_searchAssignmentDetailsData", 
                query= " select a " +
	               	   " from AssignmentDetailData a" +
	               	   " where (:P_ASSIGNMENT_ID = -1 or a.assginmentId = :P_ASSIGNMENT_ID) " +
	               	   " and (:P_ID = -1 or a.id = :P_ID) " +
	               	   " and (:P_OFFICER_ID = -1 or a.officerId = :P_OFFICER_ID) " +
	               	   " and (:P_NOT_OFFICER_ID = -1 or a.officerId <> :P_NOT_OFFICER_ID) " +
	               	   " and (:P_EMPLOYEE_ID = -1 or a.employeeId = :P_EMPLOYEE_ID) " +
	               	   " and (:P_IDENTITY = '-1' or a.identity = :P_IDENTITY) " +
	               	   " and ( ( ( :P_START_DATE_NULL = 1 or a.approvedEndDate >= PKG_CHAR_TO_DATE (:P_START_DATE, 'MI/MM/YYYY') )" +
				 	   " and ( :P_APPROVED_END_DATE_NULL = 1 or a.approvedEndDate < PKG_CHAR_TO_DATE (:P_APPROVED_END_DATE, 'MI/MM/YYYY') ) )" +
				 	   " or ( ( :P_START_DATE_NULL = 1 or a.startDate >= PKG_CHAR_TO_DATE (:P_START_DATE, 'MI/MM/YYYY') )" +
				 	   " and ( :P_APPROVED_END_DATE_NULL = 1 or a.startDate < PKG_CHAR_TO_DATE (:P_APPROVED_END_DATE, 'MI/MM/YYYY') ) ) )" +
	               	   " and (:P_REQUEST_NUMBER = '-1' or a.requestNumber = :P_REQUEST_NUMBER) " +
	               	   " and (:P_REGION_ID = -1 or a.regionId = :P_REGION_ID) " +
	               	   " and (:P_SECTOR_ID = -1 or a.sectorId = :P_SECTOR_ID) " +
	               	   " and (:P_SECTOR_NAME = '-1' or a.sectorName like :P_SECTOR_NAME) " +
	               	   " and (:P_ELIMINATED = -1 or a.eliminated = :P_ELIMINATED) " +
	               	   " and (:P_AGENT_CODE = '-1' or a.agentCode like :P_AGENT_CODE ) " +
	               	   " and (:P_FULL_NAME = '-1' or a.fullName like :P_FULL_NAME) " +
	               	   " and (:P_STATUS = -1 or a.status = :P_STATUS) " +
	               	   " and (:P_NOT_EQ_STATUS = -1 or a.status <> :P_NOT_EQ_STATUS) " +
	               	   " and (:P_HELD_FLAG = -1 or (:P_HELD_FLAG = 0 or :P_HELD_FLAG = 2 or (a.approvedEndDate > PKG_CHAR_TO_DATE (:P_CURRENT_DATE, 'MI/MM/YYYY') and a.startDate <= PKG_CHAR_TO_DATE (:P_CURRENT_DATE, 'MI/MM/YYYY') ) )) " +
	               	   " and (:P_HELD_FLAG = -1 or (:P_HELD_FLAG = 1 or :P_HELD_FLAG = 2 or a.approvedEndDate <= PKG_CHAR_TO_DATE (:P_CURRENT_DATE, 'MI/MM/YYYY'))) " +
	               	   " and (:P_HELD_FLAG = -1 or (:P_HELD_FLAG = 0 or :P_HELD_FLAG = 1 or a.approvedEndDate > PKG_CHAR_TO_DATE (:P_CURRENT_DATE, 'MI/MM/YYYY') and a.startDate > PKG_CHAR_TO_DATE (:P_CURRENT_DATE, 'MI/MM/YYYY'))) " +
	               	   " and (:P_IS_EMPLOYEE = -1 or ( (:P_IS_EMPLOYEE = 1 or a.employeeId is null) and (:P_IS_EMPLOYEE = 0 or a.employeeId is not null) ) ) " +
	               	   " and (:P_TYPE = -1 or a.type = :P_TYPE) " +
	               	   " order by a.identity , a.approvedEndDate desc"
   ),
   
   @NamedQuery(name = "assignmentDetailData_sumMonthlyReward", 
			   query= " select sum(a.monthlyReward) " +
				 	  " from AssignmentDetailData a " +
				 	  " where (:P_REGION_ID = -1 or a.regionId = :P_REGION_ID ) " +
				 	  " and ( ( ( :P_START_DATE_NULL = 1 or a.approvedEndDate >= PKG_CHAR_TO_DATE (:P_START_DATE, 'MI/MM/YYYY') )" +
				 	  " and ( :P_END_DATE_NULL = 1 or a.approvedEndDate < PKG_CHAR_TO_DATE (:P_END_DATE, 'MI/MM/YYYY') ) )" +
				 	  " or ( ( :P_START_DATE_NULL = 1 or a.approvedEndDate >= PKG_CHAR_TO_DATE (:P_START_DATE, 'MI/MM/YYYY') )" +
				 	  " and ( :P_END_DATE_NULL = 1 or a.startDate < PKG_CHAR_TO_DATE (:P_END_DATE, 'MI/MM/YYYY') ) ) )" +
				 	  " and ( :P_STATUS = -1 or a.status = :P_STATUS ) " +
				 	  " and a.employeeId is null " +
				 	  " and ( a.type = :P_TYPE_ASSIGNMENT ) " +
				 	  " order by a.id"
   ),
   
   @NamedQuery(name = "assignmentDetailData_searchActiveAssignmentDetailData", 
			   query= " select a " +
				 	  " from AssignmentDetailData a" +
				 	  " where (:P_REGION_ID = -1 or a.regionId = :P_REGION_ID ) " +
				 	  " and ( ( ( :P_START_DATE_NULL = 1 or a.approvedEndDate >= PKG_CHAR_TO_DATE (:P_START_DATE, 'MI/MM/YYYY') )" +
				 	  " and ( :P_END_DATE_NULL = 1 or a.approvedEndDate < PKG_CHAR_TO_DATE (:P_END_DATE, 'MI/MM/YYYY') ) )" +
				 	  " or ( ( :P_START_DATE_NULL = 1 or a.approvedEndDate >= PKG_CHAR_TO_DATE (:P_START_DATE, 'MI/MM/YYYY') )" +
				 	  " and ( :P_END_DATE_NULL = 1 or a.startDate < PKG_CHAR_TO_DATE (:P_END_DATE, 'MI/MM/YYYY') ) ) )" +
				 	  " and ( :P_STATUS = -1 or a.status = :P_STATUS ) " +
				 	  " and a.employeeId is null " +
				 	  " and ( a.type = :P_TYPE_ASSIGNMENT ) " +
				 	  " order by a.id"
  ),
   
   @NamedQuery(name = "assignmentDetailData_searchAssignmentDetailsDataToSendNotif", 
			   query= " select a" +
				 	  " from AssignmentDetailData a, WFInstance wF " +
				 	  " where (:P_START_DATE_NULL = 1 or a.approvedEndDate >= PKG_CHAR_TO_DATE (:P_START_DATE, 'MI/MM/YYYY') ) " +
				 	  " and (:P_END_DATE_NULL = 1 or a.approvedEndDate < PKG_CHAR_TO_DATE (:P_END_DATE, 'MI/MM/YYYY') ) " +
				 	  " and (:P_INSTANCE_STATUS = -1 or wF.status = :P_INSTANCE_STATUS) " +
				 	  " and a.wFInstanceId = wF.id " +
				 	  " order by a.officerId"
   ),
   
   @NamedQuery(name = "assignmentDetailData_searchAssignmentPrevDetailsData", 
   			   query= " select a " +
			      	  " from AssignmentDetailData a , WFInstance wF" +
			      	  " where (:P_EMPLOYEE_ID = -1 or a.employeeId = :P_EMPLOYEE_ID) " +
			      	  " and (:P_IDENTITY = '-1' or a.identity = :P_IDENTITY) " +
			      	  " and (:P_START_DATE_NULL = 1 or a.startDate < PKG_CHAR_TO_DATE (:P_START_DATE, 'MI/MM/YYYY') ) " +
			          " and (:P_INSTANCE_STATUS = -1 or wF.status <> :P_INSTANCE_STATUS) " +
			      	  " and wF.instanceId = a.wFInstanceId " +
			      	  " order by a.id "
),
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_ASSIGNMENTS_DETAILS")
public class AssignmentDetailData extends BaseEntity implements Serializable {
	private Long id;
	private Long assginmentId;
	private Long regionId;
	private String regionName;
	private Long sectorId;
	private String sectorName;
	private Long unitId;
	private String unitName;
	private Date startDate;
	private String startDateString;
	private Integer period;
	private String requestNumber;
	private Date requestDate;
	private String requestDateString;
	private Long officerId;
	private String officerName;
	private String agentCode;
	private String workScope;
	private String agentClass;
	private Double monthlyReward;
	private Integer paymentMethod;
	private Integer location;
	private Long wFInstanceId;
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
	private String countryName;
	private Long employeeId;
	private String employeeSpecialNumber;
	private String employeeNumber;
	private String reasons;
	private List<String> reasonsList;
	private AssignmentDetail assignmentDetail;
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
	private Boolean changeAgentCode;
	private Integer heldFlag;

	public AssignmentDetailData() {
		assignmentDetail = new AssignmentDetail();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return assignmentDetail.getId();
	}

	public void setId(Long id) {
		assignmentDetail.setId(id);
		this.id = id;
	}

	@Basic
	@Column(name = "ASSIGNMENTS_ID")
	public Long getAssginmentId() {
		return assginmentId;
	}

	public void setAssginmentId(Long assginmentId) {
		assignmentDetail.setAssignmentId(assginmentId);
		this.assginmentId = assginmentId;
	}

	@Basic
	@Column(name = "STP_REGION_ID")
	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	@Basic
	@Column(name = "STP_REGION_NAME")
	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	@Basic
	@Column(name = "STP_SECTOR_ID")
	public Long getSectorId() {
		return sectorId;
	}

	public void setSectorId(Long sectorId) {
		this.sectorId = sectorId;
	}

	@Basic
	@Column(name = "STP_SECTOR_NAME")
	public String getSectorName() {
		return sectorName;
	}

	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}
	
	@Basic
	@Column(name = "STP_UNIT_ID")
	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	@Basic
	@Column(name = "STP_UNIT_NAME")
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		assignmentDetail.setStartDate(startDate);
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
		assignmentDetail.setPeriod(period);
		this.period = period;
	}

	@Basic
	@Column(name = "REQUEST_NUMBER")
	public String getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(String requestNumber) {
		this.requestNumber = requestNumber;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REQUEST_DATE")
	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDateString = HijriDateService.getHijriDateString(requestDate);
		this.requestDate = requestDate;
	}

	@Transient
	public String getRequestDateString() {
		return requestDateString;
	}

	@Basic
	@Column(name = "STP_VW_EMPLOYEES_ID_OFFICER")
	public Long getOfficerId() {
		return officerId;
	}

	public void setOfficerId(Long officerId) {
		this.officerId = officerId;
	}

	@Basic
	@Column(name = "STP_VW_EMPLOYEES_NAME_OFFICER")
	public String getOfficerName() {
		return officerName;
	}

	public void setOfficerName(String officerName) {
		this.officerName = officerName;
	}

	@Basic
	@Column(name = "AGENT_CODE")
	public String getAgentCode() {
		return agentCode;
	}

	public void setAgentCode(String agentCode) {
		assignmentDetail.setAgentCode(agentCode);
		this.agentCode = agentCode;
	}

	@Basic
	@Column(name = "WORK_SCOPE")
	public String getWorkScope() {
		return workScope;
	}

	public void setWorkScope(String workScope) {
		assignmentDetail.setWorkScope(workScope);
		this.workScope = workScope;
	}

	@Basic
	@Column(name = "WORK_NATURE")
	public String getWorkNature() {
		return workNature;
	}

	public void setWorkNature(String workNature) {
		assignmentDetail.setWorkNature(workNature);
		this.workNature = workNature;
	}

	@Basic
	@Column(name = "AGENT_CLASS")
	public String getAgentClass() {
		return agentClass;
	}

	public void setAgentClass(String agentClass) {
		assignmentDetail.setAgentClass(agentClass);
		this.agentClass = agentClass;
	}

	@Basic
	@Column(name = "MONTHLY_REWARD")
	public Double getMonthlyReward() {
		return monthlyReward;
	}

	public void setMonthlyReward(Double monthlyReward) {
		assignmentDetail.setMonthlyReward(monthlyReward);
		this.monthlyReward = monthlyReward;
	}

	@Basic
	@Column(name = "PAYMENT_METHOD")
	public Integer getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(Integer paymentMethod) {
		assignmentDetail.setPaymentMethod(paymentMethod);
		this.paymentMethod = paymentMethod;
	}

	@Basic
	@Column(name = "LOCATION")
	public Integer getLocation() {
		return location;
	}

	public void setLocation(Integer location) {
		assignmentDetail.setLocation(location);
		this.location = location;
	}

	@Basic
	@Column(name = "WF_INSTANCES_ID")
	public Long getwFInstanceId() {
		return wFInstanceId;
	}

	public void setwFInstanceId(Long wFInstanceId) {
		this.wFInstanceId = wFInstanceId;
	}

	@Basic
	@Column(name = "RANK")
	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		assignmentDetail.setRank(rank);
		this.rank = rank;
	}

	@Basic
	@Column(name = "IDENTITY_NUM")
	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		assignmentDetail.setIdentity(identity);
		this.identity = identity;
	}

	@Basic
	@Column(name = "FULL_NAME")
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		assignmentDetail.setFullName(fullName);
		this.fullName = fullName;
	}

	@Basic
	@Column(name = "WORK_PLACE")
	public String getWorkPlace() {
		return workPlace;
	}

	public void setWorkPlace(String workPlace) {
		assignmentDetail.setWorkPlace(workPlace);
		this.workPlace = workPlace;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BIRTH_DATE")
	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		assignmentDetail.setBirthDate(birthDate);
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
		assignmentDetail.setPrimaryPhone(primaryPhone);
		this.primaryPhone = primaryPhone;
	}

	@Basic
	@Column(name = "SECONDARY_PHONE")
	public String getSecondaryPhone() {
		return secondaryPhone;
	}

	public void setSecondaryPhone(String secondaryPhone) {
		assignmentDetail.setSecondaryPhone(secondaryPhone);
		this.secondaryPhone = secondaryPhone;
	}

	@Basic
	@Column(name = "TERNARY_PHONE")
	public String getTernaryPhone() {
		return ternaryPhone;
	}

	public void setTernaryPhone(String ternaryPhone) {
		assignmentDetail.setTernaryPhone(ternaryPhone);
		this.ternaryPhone = ternaryPhone;
	}

	@Basic
	@Column(name = "STP_VW_BANKS_BRANCHES_ID")
	public Long getBankBranchId() {
		return bankBranchId;
	}

	public void setBankBranchId(Long bankBranchId) {
		assignmentDetail.setBankBranchId(bankBranchId);
		this.bankBranchId = bankBranchId;
	}

	@Basic
	@Column(name = "IBAN")
	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		assignmentDetail.setIban(iban);
		this.iban = iban;
	}

	@Basic
	@Column(name = "AGENT_TYPE")
	public Integer getAgentType() {
		return agentType;
	}

	public void setAgentType(Integer agentType) {
		assignmentDetail.setAgentType(agentType);
		this.agentType = agentType;
	}

	@Basic
	@Column(name = "STP_VW_COUNTRIES_ID")
	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		assignmentDetail.setCountryId(countryId);
		this.countryId = countryId;
	}

	@Basic
	@Column(name = "STP_VW_COUNTRIES_NAME")
	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	@Basic
	@Column(name = "STP_VW_EMPLOYEES_ID")
	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		assignmentDetail.setEmployeeId(employeeId);
		this.employeeId = employeeId;
	}

	@Basic
	@Column(name = "EMP_SPECIAL_NUMBER")
	public String getEmployeeSpecialNumber() {
		return employeeSpecialNumber;
	}

	public void setEmployeeSpecialNumber(String employeeSpecialNumber) {
		assignmentDetail.setEmployeeSpecialNumber(employeeSpecialNumber);
		this.employeeSpecialNumber = employeeSpecialNumber;
	}

	@Basic
	@Column(name = "EMPLOYEE_NUMBER")
	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		assignmentDetail.setEmployeeNumber(employeeNumber);
		this.employeeNumber = employeeNumber;
	}

	@Transient
	public AssignmentDetail getAssignmentDetail() {
		return assignmentDetail;
	}

	public void setAssignmentDetail(AssignmentDetail assignmentDetail) {
		this.assignmentDetail = assignmentDetail;
	}

	@Basic
	@Column(name = "REASONS")
	public String getReasons() {
		return reasons;
	}

	public void setReasons(String reasons) {
		assignmentDetail.setReasons(reasons);
		this.reasons = reasons;
	}

	@Transient
	public List<String> getReasonsList() {
		return reasonsList;
	}

	public void setReasonsList(List<String> reasonsList) {
		this.reasons = "";
		for (int i = 0; i < reasonsList.size(); i++) {
			if (i != 0) {
				this.reasons = reasons.concat(",");
			}
			this.reasons += reasonsList.get(i);
			setReasons(this.reasons);
		}
		this.reasonsList = reasonsList;
	}

	@Basic
	@Column(name = "END_DATE")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		assignmentDetail.setEndDate(endDate);
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
		assignmentDetail.setEliminated(eliminated);
		this.eliminated = eliminated;
	}

	@Basic
	@Column(name = "APPROVED_END_DATE")
	public Date getApprovedEndDate() {
		return approvedEndDate;
	}

	public void setApprovedEndDate(Date approvedEndDate) {
		assignmentDetail.setApprovedEndDate(approvedEndDate);
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
		assignmentDetail.setStatus(status);
		this.status = status;
	}

	@Basic
	@Column(name = "TYPE")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		assignmentDetail.setType(type);
		this.type = type;
	}
	
	@Basic
	@Column(name = "TYPE_TEMP")
	public Integer getTypeTemp() {
		return typeTemp;
	}

	public void setTypeTemp(Integer typeTemp) {
		assignmentDetail.setTypeTemp(typeTemp);
		this.typeTemp = typeTemp;
	}

	@Basic
	@Column(name = "AGENT_CLASS_TEMP")
	public String getAgentClassTemp() {
		return agentClassTemp;
	}

	public void setAgentClassTemp(String agentClassTemp) {
		assignmentDetail.setAgentClassTemp(agentClassTemp);
		this.agentClassTemp = agentClassTemp;
	}

	@Basic
	@Column(name = "MONTHLY_REWARD_TEMP")
	public Double getMonthlyRewardTemp() {
		return monthlyRewardTemp;
	}

	public void setMonthlyRewardTemp(Double monthlyRewardTemp) {
		assignmentDetail.setMonthlyRewardTemp(monthlyRewardTemp);
		this.monthlyRewardTemp = monthlyRewardTemp;
	}

	@Basic
	@Column(name = "PAYMENT_METHOD_TEMP")
	public Integer getPaymentMethodTemp() {
		return paymentMethodTemp;
	}

	public void setPaymentMethodTemp(Integer paymentMethodTemp) {
		assignmentDetail.setPaymentMethodTemp(paymentMethodTemp);
		this.paymentMethodTemp = paymentMethodTemp;
	}

	@Basic
	@Column(name = "STP_VW_BANKS_BRANCHES_ID_TEMP")
	public Long getBankBranchIdTemp() {
		return bankBranchIdTemp;
	}

	public void setBankBranchIdTemp(Long bankBranchIdTemp) {
		assignmentDetail.setBankBranchIdTemp(bankBranchIdTemp);
		this.bankBranchIdTemp = bankBranchIdTemp;
	}

	@Basic
	@Column(name = "IBAN_TEMP")
	public String getIbanTemp() {
		return ibanTemp;
	}

	public void setIbanTemp(String ibanTemp) {
		assignmentDetail.setIbanTemp(ibanTemp);
		this.ibanTemp = ibanTemp;
	}

	@Basic                     
	@Column(name = "FULL_NAME_TEMP")
	public String getFullNameTemp() {
		return fullNameTemp;
	}

	public void setFullNameTemp(String fullNameTemp) {
		assignmentDetail.setFullNameTemp(fullNameTemp);
		this.fullNameTemp = fullNameTemp;
	}

	@Transient
	public Boolean getChangeAgentCode() {
		return changeAgentCode;
	}

	public void setChangeAgentCode(Boolean changeAgentCode) {
		this.changeAgentCode = changeAgentCode;
	}

	@Transient
	public Integer getHeldFlag() {
		return heldFlag;
	}

	public void setHeldFlag(Integer heldFlag) {
		this.heldFlag = heldFlag;
	}
}