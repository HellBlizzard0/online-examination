package com.code.dal.orm.info;

import java.io.Serializable;
import java.util.Date;

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
	 @NamedQuery(name  = "infoAnalysisReferenceData_searchInfoAnalysisReferences", 
           	 query = " select infoA " +
           	 		 " from InfoAnalysisReferenceData infoA" +
           			 " where (:P_INFO_ANALYSIS_ID = '-1' or infoA.infoAnalysisId = :P_INFO_ANALYSIS_ID )" +
           			 " order by infoA.id "
			 )
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_INFO_ANALYSIS_REFERENCE")
public class InfoAnalysisReferenceData extends BaseEntity implements Serializable {
	private Long id;
	private Long infoAnalysisId;
	private Long domainReferenceId;
	private String domainReferenceDescription;
	private Date analysisDate;
	private String analysisDateString;
	private String conclusion;
	private Integer redirectionType;
	private Long departmentId;
	private String departmentArabicName;

	private InfoAnalysisReference infoAnalysisReference;

	public InfoAnalysisReferenceData() {
		this.infoAnalysisReference = new InfoAnalysisReference();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return infoAnalysisReference.getId();
	}

	public void setId(Long id) {
		this.infoAnalysisReference.setId(id);
		this.id = id;
	}

	@Basic
	@Column(name = "INFO_ANALYSIS_ID")
	public Long getInfoAnalysisId() {
		return infoAnalysisId;
	}

	public void setInfoAnalysisId(Long infoAnalysisId) {
		this.infoAnalysisReference.setInfoAnalysisId(infoAnalysisId);
		this.infoAnalysisId = infoAnalysisId;
	}

	@Basic
	@Column(name = "DOMAINS_ID_REFERENCE")
	public Long getDomainReferenceId() {
		return domainReferenceId;
	}

	public void setDomainReferenceId(Long domainReferenceId) {
		this.infoAnalysisReference.setDomainReferenceId(domainReferenceId);
		this.domainReferenceId = domainReferenceId;
	}

	@Basic
	@Column(name = "ANALYSIS_DATE")
	public Date getAnalysisDate() {
		return analysisDate;
	}

	public void setAnalysisDate(Date analysisDate) {
		this.analysisDate = analysisDate;
	}

	@Transient
	public String getAnalysisDateString() {
		return analysisDateString;
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
	@Column(name = "REDIRECTION_TYPE")
	public Integer getRedirectionType() {
		return redirectionType;
	}

	public void setRedirectionType(Integer redirectionType) {
		this.redirectionType = redirectionType;
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
	@Column(name = "DEPARTMENT_ARABIC_NAME")
	public String getDepartmentArabicName() {
		return departmentArabicName;
	}

	public void setDepartmentArabicName(String departmentArabicName) {
		this.departmentArabicName = departmentArabicName;
	}

	@Transient
	public InfoAnalysisReference getInfoAnalysisReference() {
		return infoAnalysisReference;
	}

	public void setInfoAnalysisReference(InfoAnalysisReference infoAnalysisReference) {
		this.infoAnalysisReference = infoAnalysisReference;
	}

	@Basic
	@Column(name = "DOMAIN_REFERENCE_DESCRIPTION")
	public String getDomainReferenceDescription() {
		return domainReferenceDescription;
	}

	public void setDomainReferenceDescription(String domainReferenceDescription) {
		this.domainReferenceDescription = domainReferenceDescription;
	}
}