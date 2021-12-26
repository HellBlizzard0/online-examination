package com.code.dal.orm.activity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
	 @NamedQuery(name = "integrationEmployeeActivityData_searchIntegrationEmployeeActivityData", 
	           	 query= " select iae " +
	           	 		" from IntegrationEmployeeActivityData iae" +
	           			" where ( :P_SOCIAL_ID = '-1' or iae.socialId = :P_SOCIAL_ID ) " +
	           			" and ( :P_FULL_NAME = '-1' or iae.fullName like :P_FULL_NAME ) " +
	           			" and ( :P_NOTES = '-1' or iae.notes like :P_NOTES ) "+
	           			" and ( :P_ACTIVITY_CLASS = '-1' or iae.activityClass = :P_ACTIVITY_CLASS ) " +
	           			" and ( :P_ACTIVITY_DOMAIN = '-1' or iae.activityDomain = :P_ACTIVITY_DOMAIN ) " +
	           			" and ( :P_FROM_DATE_STRING = '-1' or iae.activityDateString >= :P_FROM_DATE_STRING ) " +
	           			" and ( :P_TO_DATE_STRING = '-1' or iae.activityDateString <= :P_TO_DATE_STRING ) " +
	           			" order by iae.id "
	)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "INT_VW_ACTIVITY_EMPLOYEES")
public class IntegrationEmployeeActivityData implements Serializable{
	private Long id;
	private String socialId;
	private String fullName;
	private String activityClass;
	private String activityDomain;
	private String activityDateString;
	private String activityTime;
	private String notes;

	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "SOCIAL_ID")
	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

	@Basic
	@Column(name = "FULL_NAME")
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Basic
	@Column(name = "ACTIVITY_CLASS")
	public String getActivityClass() {
		return activityClass;
	}

	public void setActivityClass(String activityClass) {
		this.activityClass = activityClass;
	}

	@Basic
	@Column(name = "ACTIVITY_DOMAIN")
	public String getActivityDomain() {
		return activityDomain;
	}

	public void setActivityDomain(String activityDomain) {
		this.activityDomain = activityDomain;
	}

	@Basic
	@Column(name = "ACTIVITY_DATE")
	public String getActivityDateString() {
		return activityDateString;
	}

	public void setActivityDateString(String activityDateString) {
		this.activityDateString = activityDateString;
	}

	@Basic
	@Column(name = "ACTIVITY_TIME")
	public String getActivityTime() {
		return activityTime;
	}

	public void setActivityTime(String activityTime) {
		this.activityTime = activityTime;
	}

	@Basic
	@Column(name = "NOTES")
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
