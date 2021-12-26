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
import com.code.services.util.HijriDateService;

@NamedQueries({
	 @NamedQuery(name  = "infoAnalysisDetailData_searchInfoAnalysisDetails", 
            	 query = " select infoA " +
            	 		 " from InfoAnalysisDetailData infoA" +
            			 " where (:P_INFO_ANALYSIS_ID = '-1' or infoA.infoAnalysisId = :P_INFO_ANALYSIS_ID )" +
            			 " order by infoA.id "
			 )
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_INFO_ANALYSIS_DETAILS")
public class InfoAnalysisDetailData extends BaseEntity implements Serializable {
	private Long id;
	private Long infoAnalysisId;
	private String classificationTypeDescription;
	private Long classificationId;
	private String classificationDescription;
	private Date analysisDate;
	private String analysisDateString;
	private String conclusion;
	private Integer redirectionType;
	private Long departmentId;
	private String departmentArabicName;

	private InfoAnalysisDetail infoAnalysisDetail;

	private Long classificationTypeId;

	public InfoAnalysisDetailData() {
		this.infoAnalysisDetail = new InfoAnalysisDetail();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return infoAnalysisDetail.getId();
	}

	public void setId(Long id) {
		this.infoAnalysisDetail.setId(id);
		this.id = id;
	}

	@Basic
	@Column(name = "INFO_ANALYSIS_ID")
	public Long getInfoAnalysisId() {
		return infoAnalysisId;
	}

	public void setInfoAnalysisId(Long infoAnalysisId) {
		this.infoAnalysisDetail.setInfoAnalysisId(infoAnalysisId);
		this.infoAnalysisId = infoAnalysisId;
	}

	@Basic
	@Column(name = "CLASSFICATIONS_ID")
	public Long getClassificationId() {
		return classificationId;
	}

	public void setClassificationId(Long classificationId) {
		this.infoAnalysisDetail.setClassificationId(classificationId);
		this.classificationId = classificationId;
	}

	@Basic
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

	@Basic
	@Column(name = "CLASS_TYPE_DESCRIPTION")
	public String getClassificationTypeDescription() {
		return classificationTypeDescription;
	}

	public void setClassificationTypeDescription(String classificationTypeDescription) {
		this.classificationTypeDescription = classificationTypeDescription;
	}

	@Basic
	@Column(name = "CLASSIFICATION_DESCRIPTION")
	public String getClassificationDescription() {
		return classificationDescription;
	}

	public void setClassificationDescription(String classificationDescription) {
		this.classificationDescription = classificationDescription;
	}

	@Transient
	public InfoAnalysisDetail getInfoAnalysisDetail() {
		return infoAnalysisDetail;
	}

	public void setInfoAnalysisDetail(InfoAnalysisDetail infoAnalysisDetail) {
		this.infoAnalysisDetail = infoAnalysisDetail;
	}

	@Transient
	public Long getClassificationTypeId() {
		return classificationTypeId;
	}

	public void setClassificationTypeId(Long classificationTypeId) {
		this.classificationTypeId = classificationTypeId;
	}
}