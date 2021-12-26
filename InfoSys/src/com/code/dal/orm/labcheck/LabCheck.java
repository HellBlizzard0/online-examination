package com.code.dal.orm.labcheck;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.code.dal.audit.DeleteableAuditEntity;
import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;
import com.code.services.util.HijriDateService;

@NamedQueries({
	 @NamedQuery(name  = "labCheck_searchLabCheck", 
			 	 query = " select  lab " + 
	           	 		 " from LabCheck lab" +
	           			 " where (:P_INSTANCE_ID = -1 or lab.wFInstanceId = :P_INSTANCE_ID )" +
	           			 " and (:P_ID = -1 or lab.id = :P_ID )" +
	           			 " and (:P_ORDER_NUMBER = '-1' or lab.orderNumber = :P_ORDER_NUMBER )" +
	           			 " and (:P_CHECK_REASON = '-1' or lab.checkReason = :P_CHECK_REASON )" +
	           			 " and (:P_REGION_ID = -1 or lab.regionId = :P_REGION_ID )" +
	           			 " order by lab.orderDate desc "
			 	),
	 @NamedQuery(name  = "labCheck_searchLabCheckDetails", 
			 	 query = " select  lab " + 
			   	 		 " from LabCheck lab" +
			   			 " where (:P_ORDER_NUMBER = '-1' or lab.orderNumber = :P_ORDER_NUMBER )" +
			   			 " and (:P_FROM_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_FROM_DATE, 'MI/MM/YYYY') <= lab.orderDate)" + 
			  			 " and (:P_TO_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_TO_DATE, 'MI/MM/YYYY') >= lab.orderDate) " +
			  			 " and (:P_CHECK_REASON = -1 or lab.checkReason = :P_CHECK_REASON )" +
			   			 " and (:P_ORDER_SOURCE_DOMAIN_ID = -1 or lab.orderSourceDomainId = :P_ORDER_SOURCE_DOMAIN_ID )" +
			   			 " and (:P_LAB_CHECK_STATUS = -1 or lab.status = :P_LAB_CHECK_STATUS )" +
			   			 " order by lab.orderDate desc "
			 	),
	 @NamedQuery(name  = "labCheck_searchLabCheckByWFTaskIds", 
			 	 query = " select distinct lab " + 
			   	 		 " from LabCheck lab, WFTaskData task" +
			   			 " where task.taskId IN ( :P_TASKS_ID_LIST ) " +
			   	 		 " and task.instanceId = lab.wFInstanceId " +
			   			 " order by lab.orderDate desc "
			 	),
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_LAB_CHECKS")
public class LabCheck extends AuditEntity implements Serializable, Cloneable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private String orderNumber;
	private Date orderDate;
	private String orderDateString;
	private Long orderSourceDomainId;
	private String orderSourceDomainDescription;
	private Integer checkReason;
	private String remarks;
	private Long wFInstanceId;
	private Integer status;
	private String caseNumber;
	private Date caseDate;
	private String caseDateString;
	private Long regionId;
	private boolean integration = false;
	private String orderReferenceNumber;
	
	private String orderNumberBeforeEdit;
	private Date orderDateBeforeEdit;
	private String orderDateStringBeforeEdit;
	private Long orderSourceDomainIdBeforeEdit;
	private String orderSourceDomainDescriptionBeforeEdit;
	private Integer checkReasonBeforeEdit;
	private String caseNumberBeforeEdit;
	private Date caseDateBeforeEdit;
	private String caseDateStringBeforeEdit;
	private String remarksBeforeEdit;
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_LAB_CHECKS_SEQ", sequenceName = "FIS_LAB_CHECKS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_LAB_CHECKS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "ORDER_NUMBER")
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ORDER_DATE")
	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDateString = HijriDateService.getHijriDateString(orderDate);
		this.orderDate = orderDate;
	}

	@Transient
	public String getOrderDateString() {
		return orderDateString;
	}

	@Basic
	@Column(name = "CHECK_REASON")
	public Integer getCheckReason() {
		return checkReason;
	}

	public void setCheckReason(Integer checkReason) {
		this.checkReason = checkReason;
	}

	@Basic
	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Basic
	@Column(name = "WF_INSTANCES_ID")
	public Long getwFInstanceId() {
		return wFInstanceId;
	}

	public void setwFInstanceId(Long wFInstanceId) {
		this.wFInstanceId = wFInstanceId;
	}

	@Override
	public Long calculateContentId() {
		return this.id;
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
	@Column(name = "CASE_NUMBER")
	public String getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CASE_DATE")
	public Date getCaseDate() {
		return caseDate;
	}

	public void setCaseDate(Date caseDate) {
		this.caseDate = caseDate;
		this.caseDateString = HijriDateService.getHijriDateString(caseDate);
	}

	@Transient
	public String getCaseDateString() {
		return caseDateString;
	}

	@Basic
	@Column(name = "ORDER_SOURCE_DOMAIN_ID")
	public Long getOrderSourceDomainId() {
		return orderSourceDomainId;
	}

	public void setOrderSourceDomainId(Long orderSourceDomainId) {
		this.orderSourceDomainId = orderSourceDomainId;
	}

	@Basic
	@Column(name = "ORDER_SOURCE_DOMAIN_DESC")
	public String getOrderSourceDomainDescription() {
		return orderSourceDomainDescription;
	}

	public void setOrderSourceDomainDescription(String orderSourceDomainDescription) {
		this.orderSourceDomainDescription = orderSourceDomainDescription;
	}
	
	@Basic
	@Column(name = "STP_VW_DEPARTMENT_REGION_ID")
	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}
	
	@Transient
	public boolean isIntegration() {
		return integration;
	}

	public void setIntegration(boolean integration) {
		this.integration = integration;
	}

	@Basic
	@Column(name = "ORDER_REFERENCE_NUMBER")
	public String getOrderReferenceNumber() {
		return orderReferenceNumber;
	}

	public void setOrderReferenceNumber(String orderReferenceNumber) {
		this.orderReferenceNumber = orderReferenceNumber;
	}

	@Basic
	@Column(name = "ORDER_NUMBER_BEFORE_EDIT")
	public String getOrderNumberBeforeEdit() {
		return orderNumberBeforeEdit;
	}

	public void setOrderNumberBeforeEdit(String orderNumberBeforeEdit) {
		this.orderNumberBeforeEdit = orderNumberBeforeEdit;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ORDER_DATE_BEFORE_EDIT")
	public Date getOrderDateBeforeEdit() {
		return orderDateBeforeEdit;
	}

	public void setOrderDateBeforeEdit(Date orderDateBeforeEdit) {
		this.orderDateStringBeforeEdit = HijriDateService.getHijriDateString(orderDateBeforeEdit);
		this.orderDateBeforeEdit = orderDateBeforeEdit;
	}

	@Basic
	@Column(name = "ORDR_SRC_DMN_ID_BEFORE_EDIT")
	public Long getOrderSourceDomainIdBeforeEdit() {
		return orderSourceDomainIdBeforeEdit;
	}

	public void setOrderSourceDomainIdBeforeEdit(Long orderSourceDomainIdBeforeEdit) {
		this.orderSourceDomainIdBeforeEdit = orderSourceDomainIdBeforeEdit;
	}

	@Basic
	@Column(name = "ORDR_SRC_DMN_DESC_BEFORE_EDIT")
	public String getOrderSourceDomainDescriptionBeforeEdit() {
		return orderSourceDomainDescriptionBeforeEdit;
	}

	public void setOrderSourceDomainDescriptionBeforeEdit(String orderSourceDomainDescriptionBeforeEdit) {
		this.orderSourceDomainDescriptionBeforeEdit = orderSourceDomainDescriptionBeforeEdit;
	}

	@Basic
	@Column(name = "CHECK_REASON_BEFORE_EDIT")
	public Integer getCheckReasonBeforeEdit() {
		return checkReasonBeforeEdit;
	}

	public void setCheckReasonBeforeEdit(Integer checkReasonBeforeEdit) {
		this.checkReasonBeforeEdit = checkReasonBeforeEdit;
	}

	@Basic
	@Column(name = "CASE_NUMBER_BEFORE_EDIT")
	public String getCaseNumberBeforeEdit() {
		return caseNumberBeforeEdit;
	}

	public void setCaseNumberBeforeEdit(String caseNumberBeforeEdit) {
		this.caseNumberBeforeEdit = caseNumberBeforeEdit;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CASE_DATE_BEFORE_EDIT")
	public Date getCaseDateBeforeEdit() {
		return caseDateBeforeEdit;
	}

	public void setCaseDateBeforeEdit(Date caseDateBeforeEdit) {
		this.caseDateStringBeforeEdit = HijriDateService.getHijriDateString(caseDateBeforeEdit);
		this.caseDateBeforeEdit = caseDateBeforeEdit;
	}

	@Basic
	@Column(name = "REMARKS_BEFORE_EDIT")
	public String getRemarksBeforeEdit() {
		return remarksBeforeEdit;
	}

	public void setRemarksBeforeEdit(String remarksBeforeEdit) {
		this.remarksBeforeEdit = remarksBeforeEdit;
	}

	@Transient
	public String getOrderDateStringBeforeEdit() {
		return orderDateStringBeforeEdit;
	}

	@Transient
	public String getCaseDateStringBeforeEdit() {
		return caseDateStringBeforeEdit;
	}
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}