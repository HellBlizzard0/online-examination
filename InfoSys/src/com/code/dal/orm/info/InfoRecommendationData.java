package com.code.dal.orm.info;

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
	 @NamedQuery(name = "infoRecommendationData_searchInfoRecommendations", 
       	 	 query= " select infoRec " +
       	 			" from InfoRecommendationData infoRec " +
       	 			" where (:P_ID = -1 or infoRec.id = :P_ID) " +
       	 			" and (:P_INFO_ID = '-1' or infoRec.infoId = :P_INFO_ID ) " +
       	 			" and (:P_LAB_CHECK = '-1' or infoRec.labCheck = :P_LAB_CHECK ) " +
       	 			" and (:P_SECURITY_CHECK = -1 or infoRec.securityCheck = :P_SECURITY_CHECK ) " +
       	 			" and (:P_EMPLOYEE_ID = '-1' or infoRec.employeeId = :P_EMPLOYEE_ID ) " +
       	 			" and (:P_EMPLOYEE_FULL_NAME = '-1' or infoRec.employeeFullName like :P_EMPLOYEE_FULL_NAME ) " +
       	 			" and (:P_SURVEILLANCE = '-1' or infoRec.surveillance = :P_SURVEILLANCE ) " +
       	 			" order by infoRec.id "
			 )
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_INFO_RECOMENDATIONS")
public class InfoRecommendationData extends BaseEntity implements Serializable {
	private Long id;
	private Long infoId;
	private Boolean labCheck;
	private Boolean securityCheck;
	private Long employeeId;
	private String employeeFullName;
	private String employeeSocialNumber;
	private String employeeRank;
	private Boolean surveillance;

	private InfoRecommendation infoRecommendation;

	public InfoRecommendationData() {
		this.infoRecommendation = new InfoRecommendation();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return infoRecommendation.getId();
	}

	public void setId(Long id) {
		this.infoRecommendation.setId(id);
		this.id = id;
	}

	@Basic
	@Column(name = "INFORMATION_ID")
	public Long getInfoId() {
		return infoId;
	}

	public void setInfoId(Long infoId) {
		this.infoRecommendation.setInfoId(infoId);
		this.infoId = infoId;
	}

	@Basic
	@Column(name = "LAB_CHECK")
	public Boolean getLabCheck() {
		return labCheck;
	}

	public void setLabCheck(Boolean labCheck) {
		this.infoRecommendation.setLabCheck(labCheck);
		this.labCheck = labCheck;
	}

	@Basic
	@Column(name = "SECURITY_CHECK")
	public Boolean getSecurityCheck() {
		return securityCheck;
	}

	public void setSecurityCheck(Boolean securityCheck) {
		this.infoRecommendation.setSecurityCheck(securityCheck);
		this.securityCheck = securityCheck;
	}

	@Basic
	@Column(name = "STP_VW_EMPLOYEES_ID")
	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.infoRecommendation.setEmployeeId(employeeId);
		this.employeeId = employeeId;
	}

	@Basic
	@Column(name = "FULL_NAME")
	public String getEmployeeFullName() {
		return employeeFullName;
	}

	public void setEmployeeFullName(String employeeFullName) {
		this.employeeFullName = employeeFullName;
	}

	@Basic
	@Column(name = "SURVEILLANCE")
	public Boolean getSurveillance() {
		return surveillance;
	}

	public void setSurveillance(Boolean surveillance) {
		this.infoRecommendation.setSurveillance(surveillance);
		this.surveillance = surveillance;
	}

	@Basic
	@Column(name = "EMPLOYEE_SOCIAL_NUMBER")
	public String getEmployeeSocialNumber() {
		return employeeSocialNumber;
	}

	public void setEmployeeSocialNumber(String employeeSocialNumber) {
		this.employeeSocialNumber = employeeSocialNumber;
	}

	@Basic
	@Column(name = "EMPLOYEE_RANK")
	public String getEmployeeRank() {
		return employeeRank;
	}

	public void setEmployeeRank(String employeeRank) {
		this.employeeRank = employeeRank;
	}

	@Transient
	public InfoRecommendation getInfoRecommendation() {
		return infoRecommendation;
	}

	public void setInfoRecommendation(InfoRecommendation infoRecommendation) {
		this.infoRecommendation = infoRecommendation;
	}
}
