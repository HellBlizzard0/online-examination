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
import javax.persistence.Transient;

import com.code.dal.audit.DeleteableAuditEntity;
import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;
import com.code.services.util.HijriDateService;



@NamedQueries({
	@NamedQuery(name  = "labCheckReportDepartmentAction_searchlabCheckReportDepartmentAction", 
        	 	query = " select la " +
            	 		 " from LabCheckReportDepartmentAction la" +
            			 " where (:P_LAB_CHECK_REPORT_DEPARTMENT_ID = -1 or la.labCheckReportDepartmentId = :P_LAB_CHECK_REPORT_DEPARTMENT_ID )" + 
            			 " order by la.id"
			 	)
})
@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_LAB_CHECK_RPRT_DPTS_ACTNS")
public class LabCheckReportDepartmentAction extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private String actionName;
	private String actionLocation;
	private Date actionDate;
	private String actionDateString;
	private Long labCheckReportDepartmentId;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_LAB_CHK_RPRT_DPT_ACTN_SEQ", sequenceName = "FIS_LAB_CHK_RPRT_DPT_ACTN_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_LAB_CHK_RPRT_DPT_ACTN_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "ACTION_NAME")
	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	@Basic
	@Column(name = "ACTION_LOCATION")
	public String getActionLocation() {
		return actionLocation;
	}

	public void setActionLocation(String actionLocation) {
		this.actionLocation = actionLocation;
	}

	@Basic
	@Column(name = "ACTION_DATE")
	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
		this.actionDateString=HijriDateService.getHijriDateString(actionDate);
	}

	@Transient
	public String getActionDateString() {
		return actionDateString;
	}

	@Basic
	@Column(name = "LAB_CHECK_REPORT_DEPTS_ID")
	public Long getLabCheckReportDepartmentId() {
		return labCheckReportDepartmentId;
	}

	public void setLabCheckReportDepartmentId(Long labCheckReportDepartmentId) {
		this.labCheckReportDepartmentId = labCheckReportDepartmentId;
	}

	@Override
	public Long calculateContentId() {
		return id;
	}
}
