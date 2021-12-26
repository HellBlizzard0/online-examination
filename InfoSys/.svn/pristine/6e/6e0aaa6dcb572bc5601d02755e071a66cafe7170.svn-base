package com.code.dal.orm.surveillance;

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

import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;
import com.code.services.util.HijriDateService;

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_SURVEILLANCE_ORDERS")
public class SurveillanceOrder extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity {
	private Long id;
	private Date orderDate;
	private String orderDateString;
	private String orderNumber;
	private Integer periodMonths;
	private String notes;
	private String finalApprovalEntity;
	private Long regionId;
	private Integer periodicReporting;
	private Date startDate;
	private String startDateString;
	private Long infoId;
	private Long wFInstanceId;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_SURVEILLANCE_ORDERS_SEQ", sequenceName = "FIS_SURVEILLANCE_ORDERS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_SURVEILLANCE_ORDERS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	@Column(name = "ORDER_NUMBER")
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	@Basic
	@Column(name = "PERIOD_MONTHS")
	public Integer getPeriodMonths() {
		return periodMonths;
	}

	public void setPeriodMonths(Integer periodMonths) {
		this.periodMonths = periodMonths;
	}

	@Basic
	@Column(name = "NOTES")
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Basic
	@Column(name = "PERIODIC_REPORTING")
	public Integer getPeriodicReporting() {
		return periodicReporting;
	}

	public void setPeriodicReporting(Integer periodicReporting) {
		this.periodicReporting = periodicReporting;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		if (startDate != null) {
			this.startDateString = HijriDateService.getHijriDateString(startDate);
		}
		this.startDate = startDate;
	}

	@Transient
	public String getStartDateString() {
		return startDateString;
	}

	@Basic
	@Column(name = "INFORMATION_ID")
	public Long getInfoId() {
		return infoId;
	}

	public void setInfoId(Long infoId) {
		this.infoId = infoId;
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
		return id;
	}

	@Basic
	@Column(name = "FINAL_APPROVAL_ENTITY")
	public String getFinalApprovalEntity() {
		return finalApprovalEntity;
	}

	public void setFinalApprovalEntity(String finalApprovalEntity) {
		this.finalApprovalEntity = finalApprovalEntity;
	}

	@Basic
	@Column(name = "STP_VW_DEPARTMENTS_REGION_ID")
	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}
}
