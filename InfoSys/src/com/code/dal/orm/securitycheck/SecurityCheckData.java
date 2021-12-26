package com.code.dal.orm.securitycheck;

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
		@NamedQuery(
				name = "securityCheckData_searchSecurityCheckById",
				query = " select distinct s " +
						" from SecurityCheckData s" +
						" where (:P_ID = -1 or s.id = :P_ID ) " +
						" and (:P_WF_INSTANCE_ID = -1 or s.wFInstanceId = :P_WF_INSTANCE_ID )" +
						" order by s.requestDate desc , s.id desc "),
		@NamedQuery(
				name = "securityCheckData_searchSecurityCheckEmployee",
				query = " select distinct s " +
						" from SecurityCheckData s , SecurityCheckPerson p" +
						" where s.id = p.securityCheckId" +
						" and (:P_EMPLOYEE_ID = -1 or p.employeeId = :P_EMPLOYEE_ID ) " +
						" and p.nonEmployeeId is null " +
						" and (:P_ID = -1 or s.id = :P_ID )" +
						" and (:P_NOT_REJECTED = -1 or s.status != 6)" +
						" and (:P_CHECK_REASON = -1 or s.checkReason = :P_CHECK_REASON)" +
						" and (:P_WF_INSTANCE_ID = -1 or s.wFInstanceId = :P_WF_INSTANCE_ID )" +
						" and (:P_DPET_ORDER_SRC_ID = -1 or s.departmentOrderSrcId = :P_DPET_ORDER_SRC_ID)" +
						" and (:P_DOMAIN_INCOMING_SRC_ID = -1 or s.domainIncomingSideId = :P_DOMAIN_INCOMING_SRC_ID)" +
						" and (:P_REGION_ID = -1 or s.regionId = :P_REGION_ID)" +
						" and (:P_START_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_START_DATE, 'MI/MM/YYYY') <= s.requestDate)" +
						" and (:P_END_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_END_DATE, 'MI/MM/YYYY') >= s.requestDate)" +
						" and (:P_REQUEST_NUMBER = '-1' or s.requestSource = :P_REQUEST_NUMBER) " +
						" order by s.requestDate desc , s.id desc "),
		@NamedQuery(
				name = "securityCheckData_searchSecurityCheckNonEmployee",
				query = " select distinct s " +
						" from SecurityCheckData s , SecurityCheckPerson p" +
						" where s.id = p.securityCheckId" +
						" and (:P_NON_EMPLOYEE_ID = -1 or p.nonEmployeeId = :P_NON_EMPLOYEE_ID )" +
						" and p.employeeId is null " +
						" and (:P_ID = -1 or s.id = :P_ID )" +
						" and (:P_NOT_REJECTED = -1 or s.status != 6)" +
						" and (:P_CHECK_REASON = -1 or s.checkReason = :P_CHECK_REASON)" +
						" and (:P_WF_INSTANCE_ID = -1 or s.wFInstanceId = :P_WF_INSTANCE_ID )" +
						" and (:P_DPET_ORDER_SRC_ID = -1 or s.departmentOrderSrcId = :P_DPET_ORDER_SRC_ID)" +
						" and (:P_DOMAIN_INCOMING_SRC_ID = -1 or s.domainIncomingSideId = :P_DOMAIN_INCOMING_SRC_ID)" +
						" and (:P_REGION_ID = -1 or s.regionId = :P_REGION_ID)" +
						" and (:P_START_DATE_NULL = 1 or to_date(:P_START_DATE, 'MI/MM/YYYY') <= s.requestDate)" +
						" and (:P_END_DATE_NULL = 1 or to_date(:P_END_DATE, 'MI/MM/YYYY') >= s.requestDate)" +
						" and (:P_REQUEST_NUMBER = '-1' or s.requestSource = :P_REQUEST_NUMBER) " +
						" order by s.requestDate desc , s.id desc "),
		@NamedQuery(
				name = "securityCheckData_searchSecurityCheckByPerson",
				query = " select s " +
						" from SecurityCheckData s , SecurityCheckPerson p" +
						" where s.id = p.securityCheckId" +
						" and (:P_NOT_REJECTED = -1 or s.status != 6)" +
						" and (:P_EMPLOYEE_ID = -1 or p.employeeId = :P_EMPLOYEE_ID )" +
						" and (:P_STATUS = -1 or s.status = :P_STATUS )" +
						" and (:P_NON_EMPLOYEE_ID = -1 or p.nonEmployeeId = :P_NON_EMPLOYEE_ID )" +
						" and (:P_SAVE_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_SAVE_DATE, 'MI/MM/YYYY') >= s.requestDate)" +
						" order by s.requestDate desc , s.id "),
		@NamedQuery(
				name = "securityCheckData_countSecurityCheck",
				query = " select count(s.id) " +
						" from SecurityCheckData s , SecurityCheckPerson p" +
						" where s.id = p.securityCheckId" +
						" and (:P_EMPLOYEE_ID = -1 or p.employeeId = :P_EMPLOYEE_ID )" +
						" and (:P_STATUS = -1 or s.status = :P_STATUS )")

})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_SECURITY_CHECK")
public class SecurityCheckData extends BaseEntity implements Serializable {
	private Long id;
	private Long infoId;
	private Date requestDate;
	private String requestDateString;
	private String requestNumber;
	private Long departmentOrderSrcId;
	private String reason;
	private Integer checkReason;
	private String incomingNumber;
	private Date incomingDate;
	private String incomingDateString;
	private Long wFInstanceId;
	private Long domainIncomingSideId;
	private String domainIncomingSideDescription;
	private Long domainReasonId;
	private String domainReasonDescription;
	private String departmentOrderSrcName;
	private Integer status;
	private Long regionId;
	private String regionName;
	private String requestSource;

	private SecurityCheck securityCheck;

	public SecurityCheckData() {
		setSecurityCheck(new SecurityCheck());
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return securityCheck.getId();
	}

	public void setId(Long id) {
		securityCheck.setId(id);
		this.id = id;
	}

	@Basic
	@Column(name = "INFORMATION_ID")
	public Long getInfoId() {
		return infoId;
	}

	public void setInfoId(Long infoId) {
		securityCheck.setInfoId(infoId);
		this.infoId = infoId;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REQUEST_DATE")
	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		securityCheck.setRequestDate(requestDate);
		this.requestDateString = HijriDateService.getHijriDateString(requestDate);
		this.requestDate = requestDate;
	}

	@Transient
	public String getRequestDateString() {
		return requestDateString;
	}

	@Basic
	@Column(name = "REQUEST_NUMBER")
	public String getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(String requestNumber) {
		securityCheck.setRequestNumber(requestNumber);
		this.requestNumber = requestNumber;
	}

	@Basic
	@Column(name = "STP_VW_DEPARTMENTS_ID_ORDR_SRC")
	public Long getDepartmentOrderSrcId() {
		return departmentOrderSrcId;
	}

	public void setDepartmentOrderSrcId(Long departmentOrderSrcId) {
		securityCheck.setDepartmentOrderSrcId(departmentOrderSrcId);
		this.departmentOrderSrcId = departmentOrderSrcId;
	}

	@Basic
	@Column(name = "REASON")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		securityCheck.setReason(reason);
		this.reason = reason;
	}

	@Basic
	@Column(name = "INCOMMING_NUMBER")
	public String getIncomingNumber() {
		return incomingNumber;
	}

	public void setIncomingNumber(String incomingNumber) {
		securityCheck.setIncomingNumber(incomingNumber);
		this.incomingNumber = incomingNumber;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INCOMING_DATE")
	public Date getIncomingDate() {
		return incomingDate;
	}

	public void setIncomingDate(Date incomingDate) {
		securityCheck.setIncomingDate(incomingDate);
		this.incomingDateString = HijriDateService.getHijriDateString(incomingDate);
		this.incomingDate = incomingDate;
	}

	@Transient
	public String getIncomingDateString() {
		return incomingDateString;
	}

	@Basic
	@Column(name = "WF_INSTANCES_ID")
	public Long getwFInstanceId() {
		return wFInstanceId;
	}

	public void setwFInstanceId(Long wFInstanceId) {
		securityCheck.setwFInstanceId(wFInstanceId);
		this.wFInstanceId = wFInstanceId;
	}

	@Basic
	@Column(name = "DOMAINS_ID_INCOMING_SIDES")
	public Long getDomainIncomingSideId() {
		return domainIncomingSideId;
	}

	public void setDomainIncomingSideId(Long domainIncomingSideId) {
		securityCheck.setDomainIncomingSideId(domainIncomingSideId);
		this.domainIncomingSideId = domainIncomingSideId;
	}

	@Basic
	@Column(name = "INCOMING_SIDE_NAME")
	public String getDomainIncomingSideDescription() {
		return domainIncomingSideDescription;
	}

	public void setDomainIncomingSideDescription(String domainIncomingSideDescription) {
		this.domainIncomingSideDescription = domainIncomingSideDescription;
	}

	@Basic
	@Column(name = "DEPARTMENT_NAME")
	public String getDepartmentOrderSrcName() {
		return departmentOrderSrcName;
	}

	public void setDepartmentOrderSrcName(String departmentOrderSrcName) {
		this.departmentOrderSrcName = departmentOrderSrcName;
	}

	@Basic
	@Column(name = "STATUS")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		securityCheck.setStatus(status);
		this.status = status;
	}

	@Basic
	@Column(name = "REGION_ID")
	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		securityCheck.setRegionId(regionId);
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
	public SecurityCheck getSecurityCheck() {
		return securityCheck;
	}

	public void setSecurityCheck(SecurityCheck securityCheck) {
		this.securityCheck = securityCheck;
	}

	@Basic
	@Column(name = "CHECK_REASON")
	public Integer getCheckReason() {
		return checkReason;
	}

	public void setCheckReason(Integer checkReason) {
		securityCheck.setCheckReason(checkReason);
		this.checkReason = checkReason;
	}

	@Basic
	@Column(name = "DOMAINS_ID_REASON")
	public Long getDomainReasonId() {
		return domainReasonId;
	}

	public void setDomainReasonId(Long domainReasonId) {
		this.domainReasonId = domainReasonId;
	}

	@Basic
	@Column(name = "DOMAINS_DESCRIPTION_REASON")
	public String getDomainReasonDescription() {
		return domainReasonDescription;
	}

	public void setDomainReasonDescription(String domainReasonDescription) {
		this.domainReasonDescription = domainReasonDescription;
	}

	@Basic
	@Column(name = "REQUEST_SOURCE")
	public String getRequestSource() {
		return requestSource;
	}

	public void setRequestSource(String requestSource) {
		securityCheck.setRequestSource(requestSource);
		this.requestSource = requestSource;
	}

}