package com.code.dal.orm.surveillance;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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

import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;
import com.code.services.util.HijriDateService;

@NamedQueries({
	@NamedQuery(name = "surveillanceReport_searchEmployeeSurveillanceReport", 
				query = " select sr " +
						" from SurveillanceReport sr " +
						" where (:P_REPORT_ID = -1 or sr.id = :P_REPORT_ID )" + 
						" and (:P_WF_INSTANCE_ID = -1 or sr.wFInstanceId = :P_WF_INSTANCE_ID )" + 
						" and (:P_SUR_EMPLOYEE_ID = -1 or sr.surveillanceEmpId = :P_SUR_EMPLOYEE_ID )" +
						" and (:P_WF_INSTANCE_NOT_NULL = -1 or sr.wFInstanceId is not null) " +
						" and (:P_BEFORE_START_DATE_NULL = 1 or sr.startDate < PKG_CHAR_TO_DATE (:P_BEFORE_START_DATE, 'MI/MM/YYYY'))" +
						" and (:P_INCLUDE_DATE_NULL = 1 or (sr.startDate <= PKG_CHAR_TO_DATE (:P_INCLUDE_DATE, 'MI/MM/YYYY') and sr.endDate >= PKG_CHAR_TO_DATE (:P_INCLUDE_DATE, 'MI/MM/YYYY')))" +
						" order by sr.startDate"
				),
	@NamedQuery(name = "surveillanceReport_searchEmployeeSurveillanceReportForApprovedOrders", 
				query = " select sr " +
						" from SurveillanceReport sr, SurveillanceEmpNonEmpData sed, WFInstance ins " + 
						" where sr.surveillanceEmpId = sed.id " +
						" and sed.wFInstanceId = ins.instanceId" +
						" and (:P_START_WF = -1 or sr.wFInstanceId is null) " +
						" and (:P_APPROVED = -1 or sr.approved = :P_APPROVED ) " +
						" and (:P_BEFORE_START_DATE_NULL = 1 or sr.endDate <= PKG_CHAR_TO_DATE (:P_BEFORE_START_DATE, 'MI/MM/YYYY'))" +
						" and (:P_WF_INSTANCE_STATUS = -1 or ins.status = :P_WF_INSTANCE_STATUS) " +
						" order by sr.startDate"
				),
	@NamedQuery(name = "surveillanceReport_deleteEmployeeSurveillanceReport", 
				query = " delete SurveillanceReport sr " +
						" where (sr.surveillanceEmpId = :P_SUR_EMPLOYEE_ID )" + 
						" and (PKG_CHAR_TO_DATE(:P_END_DATE, 'MI/MM/YYYY') <= sr.startDate)"
				)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_SURVEILLANCE_REPORTS")
public class SurveillanceReport extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity {
	private Long id;
	private Long surveillanceEmpId;
	private Date startDate;
	private String startDateString;
	private Date endDate;
	private String endDateString;
	private String remarks;
	private Integer total;
	private Integer approved;
	private Long wFInstanceId;
	private Integer recommendationDecisionType;
	private Integer recommendationPeriod;
	private Date recommendationDecisionDate;
	private String recommendationDecisionDateString;
	private String recommendationRemarks;
	private String surveillanceReasons;
	private List<String> surveillanceOrderReasonsList;
	

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_SURVEILLANCE_REPORTS_SEQ", sequenceName = "FIS_SURVEILLANCE_REPORTS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_SURVEILLANCE_REPORTS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "SURVEILLANCE_EMPLOYEES_ID")
	public Long getSurveillanceEmpId() {
		return surveillanceEmpId;
	}

	public void setSurveillanceEmpId(Long surveillanceEmpId) {
		this.surveillanceEmpId = surveillanceEmpId;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "STRAT_DATE")
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
	@Temporal(TemporalType.TIMESTAMP)
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
	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Basic
	@Column(name = "TOTAL")
	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	@Basic
	@Column(name = "APPROVED")
	public Integer getApproved() {
		return approved;
	}

	public void setApproved(Integer approved) {
		this.approved = approved;
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
	@Column(name = "REC_DECISION_TYPE")
	public Integer getRecommendationDecisionType() {
		return recommendationDecisionType;
	}

	public void setRecommendationDecisionType(Integer recommendationDecisionType) {
		this.recommendationDecisionType = recommendationDecisionType;
	}

	@Basic
	@Column(name = "REC_PERIOD")
	public Integer getRecommendationPeriod() {
		return recommendationPeriod;
	}

	public void setRecommendationPeriod(Integer recommendationPeriod) {
		this.recommendationPeriod = recommendationPeriod;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REC_DECISION_DATE")
	public Date getRecommendationDecisionDate() {
		return recommendationDecisionDate;
	}

	public void setRecommendationDecisionDate(Date recommendationDecisionDate) {
		this.recommendationDecisionDateString = HijriDateService.getHijriDateString(recommendationDecisionDate);
		this.recommendationDecisionDate = recommendationDecisionDate;
	}

	@Transient
	public String getRecommendationDecisionDateString() {
		return recommendationDecisionDateString;
	}

	@Basic
	@Column(name = "REC_REMARKS")
	public String getRecommendationRemarks() {
		return recommendationRemarks;
	}

	public void setRecommendationRemarks(String recommendationRemarks) {
		this.recommendationRemarks = recommendationRemarks;
	}

	@Override
	public Long calculateContentId() {
		return id;
	}
	
	@Basic
	@Column(name = "SURVEILLANCE_REASONS")
	public String getSurveillanceReasons() {
		return surveillanceReasons;
	}

	public void setSurveillanceReasons(String surveillanceReasons) {
		this.surveillanceReasons = surveillanceReasons;
	}
	
	@Transient
	public List<String> getSurveillanceOrderReasonsList() {
		return surveillanceOrderReasonsList;
	}
	
	public void setSurveillanceOrderReasonsList(List<String> surveillanceOrderReasonsList) {
		this.surveillanceReasons = "";

		for (int i = 0; i < surveillanceOrderReasonsList.size(); i++) {
			if (i != 0) {
				this.surveillanceReasons = surveillanceReasons.concat(",");
			}
			this.surveillanceReasons += surveillanceOrderReasonsList.get(i);
			setSurveillanceReasons(this.surveillanceReasons);
		}
		this.surveillanceOrderReasonsList = surveillanceOrderReasonsList;
	}
}