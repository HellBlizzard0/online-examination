package com.code.dal.orm.surveillance;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.code.dal.orm.BaseEntity;

@NamedQueries({
	@NamedQuery(name = "surveillanceReportDetailData_searchSurveillanceReportDetail", 
				query = " select srd " +
						" from SurveillanceReportDetailData srd " +
						" where (:P_REPORT_ID = -1 or srd.surveillanceReportId = :P_REPORT_ID )" +						
						" order by srd.classDescription, srd.domainDescription "
				),
	@NamedQuery(name = "surveillanceReportDetailData_deleteSurveillanceReportDetail", 
				query = " delete SurveillanceReportDetail srd" + 
						" where srd.surveillanceReportId in (select sr.id " +
						" 									 from SurveillanceReport sr" +
						"									 where sr.surveillanceEmpId = :P_SUR_EMPLOYEE_ID " + 
						" 									 and PKG_CHAR_TO_DATE (:P_END_DATE, 'MI/MM/YYYY') <= sr.startDate)"
				)
})
@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_SURV_REPORT_DETAILS")
public class SurveillanceReportDetailData extends BaseEntity implements Serializable {

	private Long id;
	private Long surveillanceReportId;
	private Long evalPointDomainId;
	private Integer grade;
	private String remarks;
	private String domainDescription;
	private String classDescription;

	private SurveillanceReportDetail surveillanceReportDetail;

	public SurveillanceReportDetailData() {
		this.surveillanceReportDetail = new SurveillanceReportDetail();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return surveillanceReportDetail.getId();
	}

	public void setId(Long id) {
		this.surveillanceReportDetail.setId(id);
		this.id = id;
	}

	@Basic
	@Column(name = "FIS_SURVEILLANCE_REPORTS_ID")
	public Long getSurveillanceReportId() {
		return surveillanceReportId;
	}

	public void setSurveillanceReportId(Long surveillanceReportId) {
		this.surveillanceReportDetail.setSurveillanceReportId(surveillanceReportId);
		this.surveillanceReportId = surveillanceReportId;
	}

	@Basic
	@Column(name = "DOMAINS_ID_EVALUATION_POINT")
	public Long getEvalPointDomainId() {
		return evalPointDomainId;
	}

	public void setEvalPointDomainId(Long evalPointDomainId) {
		this.surveillanceReportDetail.setEvalPointDomainId(evalPointDomainId);
		this.evalPointDomainId = evalPointDomainId;
	}

	@Basic
	@Column(name = "GRADE")
	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.surveillanceReportDetail.setGrade(grade);
		this.grade = grade;
	}

	@Basic
	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.surveillanceReportDetail.setRemarks(remarks);
		this.remarks = remarks;
	}

	@Basic
	@Column(name = "EVAL_POINT_DOMAIN_DESCRIPTION")
	public String getDomainDescription() {
		return domainDescription;
	}

	public void setDomainDescription(String domainDescription) {
		this.domainDescription = domainDescription;
	}

	@Basic
	@Column(name = "EVAL_POINT_CLASS_DESCRIPTION")
	public String getClassDescription() {
		return classDescription;
	}

	public void setClassDescription(String classDescription) {
		this.classDescription = classDescription;
	}

	@Transient
	public SurveillanceReportDetail getSurveillanceReportDetail() {
		return surveillanceReportDetail;
	}

	public void setSurveillanceReportDetail(SurveillanceReportDetail surveillanceReportDetail) {
		this.surveillanceReportDetail = surveillanceReportDetail;
	}
}
