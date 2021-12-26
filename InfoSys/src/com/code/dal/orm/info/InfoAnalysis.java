package com.code.dal.orm.info;

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
	@NamedQuery(name  = "infoAnalysis_searchInfoAnalysis", 
			    query = " select infoA " +
					    " from InfoAnalysis infoA " +
			    		" where (:P_INFO_ID = -1 or infoA.infoId = :P_INFO_ID )" +
					    " and (:P_DEPARTMENT_ID = -1 or infoA.departmentId = :P_DEPARTMENT_ID )" +
					    " and (:P_REDIRECTION_TYPE = -1 or infoA.redirectionType = :P_REDIRECTION_TYPE )" +
					    " order by infoA.id"
			    )
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_INFO_ANALYSIS")
public class InfoAnalysis extends AuditEntity implements Serializable, DeleteableAuditEntity, InsertableAuditEntity, UpdateableAuditEntity {
	private Long id;
	private Long infoId;
	private Long departmentId;
	private String departmentName;
	private Long redirectionType;
	private String conclusion;
	private Date analysisDate;
	private String analysisDateString;

	@Id
	@SequenceGenerator(name = "FIS_INFO_ANALYSIS_SEQ", sequenceName = "FIS_INFO_ANALYSIS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_INFO_ANALYSIS_SEQ")
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	@Column(name = "STP_VW_DEPARTMENTS_ID")
	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	@Basic
	@Column(name = "REDIRECTION_TYPE")
	public Long getRedirectionType() {
		return redirectionType;
	}

	public void setRedirectionType(Long redirectionType) {
		this.redirectionType = redirectionType;
	}

	@Basic
	@Column(name = "CONCLUSION")
	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ANALYSIS_DATE")
	public Date getAnalysisDate() {
		return analysisDate;
	}

	public void setAnalysisDate(Date analysisDate) {
			this.analysisDateString = HijriDateService.getHijriDateString(analysisDate);
		this.analysisDate = analysisDate;
	}

	@Transient
	public String getAnalysisDateString() {
		return analysisDateString;
	}

	@Override
	public Long calculateContentId() {
		return this.id;
	}

	@Basic
	@Column(name = "STP_DEPARTMENT_NAME")
	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
}
