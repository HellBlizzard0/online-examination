package com.code.dal.orm.surveillance;

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
	@NamedQuery(name = "surveillanceOrderData_searchSurveillanceOrder", 
				query = " select ord " +
						" from SurveillanceOrderData ord " +
						" where (:P_ORDER_NUMBER = '-1' or ord.orderNumber = :P_ORDER_NUMBER) " +
						" and (:P_ORDER_ID = -1 or ord.id = :P_ORDER_ID) " +
						" and (:P_INSTANCE_ID = -1 or ord.wFInstanceId = :P_INSTANCE_ID) " +
						" and (:P_YEAR = '-1' or PKG_DATE_TO_CHAR(ord.orderDate,'YYYY') = :P_YEAR) " +
						" and (:P_REGION_ID = -1 or ord.regionId = :P_REGION_ID) " +
						" order by ord.id "
				)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_SURVEILLANCE_ORDERS")
public class SurveillanceOrderData extends BaseEntity implements Serializable {
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
	private String infoNumber;
	private Long wFInstanceId;

	private SurveillanceOrder surveillanceOrder;

	public SurveillanceOrderData() {
		this.surveillanceOrder = new SurveillanceOrder();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return surveillanceOrder.getId();
	}

	public void setId(Long id) {
		this.surveillanceOrder.setId(id);
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
		this.surveillanceOrder.setOrderDate(orderDate);
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
		this.surveillanceOrder.setOrderNumber(orderNumber);
		this.orderNumber = orderNumber;
	}

	@Basic
	@Column(name = "PERIOD_MONTHS")
	public Integer getPeriodMonths() {
		return periodMonths;
	}

	public void setPeriodMonths(Integer periodMonths) {
		this.surveillanceOrder.setPeriodMonths(periodMonths);
		this.periodMonths = periodMonths;
	}

	@Basic
	@Column(name = "NOTES")
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.surveillanceOrder.setNotes(notes);
		this.notes = notes;
	}

	@Basic
	@Column(name = "FINAL_APPROVAL_ENTITY")
	public String getFinalApprovalEntity() {
		return finalApprovalEntity;
	}

	public void setFinalApprovalEntity(String finalApprovalEntity) {
		this.finalApprovalEntity = finalApprovalEntity;
		this.surveillanceOrder.setFinalApprovalEntity(finalApprovalEntity);
	}
	
	@Basic
	@Column(name = "PERIODIC_REPORTING")
	public Integer getPeriodicReporting() {
		return periodicReporting;
	}

	public void setPeriodicReporting(Integer periodicReporting) {
		this.surveillanceOrder.setPeriodicReporting(periodicReporting);
		this.periodicReporting = periodicReporting;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDateString = HijriDateService.getHijriDateString(startDate);
		this.surveillanceOrder.setStartDate(startDate);
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
		this.surveillanceOrder.setInfoId(infoId);
		this.infoId = infoId;
	}

	@Basic
	@Column(name = "WF_INSTANCES_ID")
	public Long getwFInstanceId() {
		return wFInstanceId;
	}

	public void setwFInstanceId(Long wFInstanceId) {
		this.surveillanceOrder.setwFInstanceId(wFInstanceId);
		this.wFInstanceId = wFInstanceId;
	}

	@Basic
	@Column(name = "INFO_NUMBER")
	public String getInfoNumber() {
		return infoNumber;
	}

	public void setInfoNumber(String infoNumber) {
		this.infoNumber = infoNumber;
	}

	@Transient
	public SurveillanceOrder getSurveillanceOrder() {
		return surveillanceOrder;
	}

	public void setSurveillanceOrder(SurveillanceOrder surveillanceOrder) {
		this.surveillanceOrder = surveillanceOrder;
	}

	@Basic
	@Column(name = "STP_VW_DEPARTMENTS_REGION_ID")
	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
		this.surveillanceOrder.setRegionId(regionId);
	}
}
