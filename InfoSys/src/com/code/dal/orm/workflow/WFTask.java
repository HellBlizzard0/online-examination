package com.code.dal.orm.workflow;

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
import javax.persistence.Version;

import org.hibernate.annotations.QueryHints;

import com.code.dal.audit.DeleteableAuditEntity;
import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;

@SuppressWarnings("serial")
@NamedQueries({
		@NamedQuery(
				name = "wf_task_getWFTaskById",
				query = " select t from WFTask t " +
						" where t.taskId = :P_TASK_ID "),
		@NamedQuery(
				name = "wf_task_getWFInstanceTasks",
				query = " select t from WFTask t " +
						" where t.instanceId = :P_INSTANCE_ID " +
						" order by assignDate "),
		@NamedQuery(
				name = "wf_task_getWFNextNotification",
				query = " select t from WFTask t " +
						" where t.assigneeId = :P_ASSIGNEE_ID " +
						" and t.taskUrl = :P_TASK_URL " +
						" and (:P_UN_READED = 1 and t.action is NULL) " +
						// " and ROWNUM = 1 " +
						" order by assignDate ",
				hints = @javax.persistence.QueryHint(name = QueryHints.FETCH_SIZE, value = "1")),
		@NamedQuery(
				name = "wf_task_getWFNextNotificationRelativeTo",
				query = " select t from WFTask t " +
						" where t.assigneeId = :P_ASSIGNEE_ID " +
						" and ID < :P_ID " +
						" and t.action is NULL " +
						// " and ROWNUM = 1 " +
						" order by id desc ",
				hints = @javax.persistence.QueryHint(name = QueryHints.FETCH_SIZE, value = "1")),
		@NamedQuery(
				name = "wf_taskData_getWFInstanceTasksDataSortedByTaskId",
				query = " select t from WFTaskData t " +
						" where t.instanceId = :P_INSTANCE_ID " +
						" order by taskId"),
		@NamedQuery(
				name = "wf_task_getWFInstanceTasksByRole",
				query = " select t from WFTask t " +
						" where t.instanceId = :P_INSTANCE_ID " +
						"   and t.assigneeWfRole = :P_ROLE " +
						" order by assignDate "),
		@NamedQuery(
				name = "wf_task_getWFInstanceTasksByRoleAndLevel",
				query = " select t from WFTask t " +
						" where t.instanceId = :P_INSTANCE_ID " +
						"   and t.assigneeWfRole = :P_ROLE " +
						"   and t.level = :P_LEVEL " +
						" order by assignDate "),
		@NamedQuery(
				name = "wf_task_canChangeWFInstanceStatusToDone",
				query = " select count(t.id) from WFTask t " +
						" where t.instanceId = :P_INSTANCE_ID " +
						"   and t.action is NULL " +
						"   and t.assigneeWfRole <> 'Notification' "),
		@NamedQuery(
				name = "wf_task_canChangeWFInstanceStatusToComplete",
				query = " select count(t.id) from WFTask t " +
						" where t.instanceId = :P_INSTANCE_ID " +
						"   and t.action is NULL "),
		@NamedQuery(
				name = "wf_task_countTasks",
				query = " select count(t.taskId) from WFTask t " +
						" where t.assigneeId = :P_ASSIGNEE_ID" +
						" and((:P_NOTIFICATION_FLAG = -1 ) or (:P_NOTIFICATION_FLAG = 1 and t.assigneeWfRole in (:P_NOTIFICATION_ROLE) ) or (:P_NOTIFICATION_FLAG = 0 and t.assigneeWfRole not in (:P_NOTIFICATION_ROLE) ) )" +
						" and t.action is null"),
		@NamedQuery(
				name = "wf_task_taskSecurity",
				query = " select t.taskUrl from WFTask t " +
						" where t.assigneeId = :P_ASSIGNEE_ID " +
						" and (:P_TASK_ID <> -1 or t.action is null) " +
						" and (:P_TASK_ID = -1 or t.taskId = :P_TASK_ID) " +
						" and (:P_TASK_URL = '-1'  or t.taskUrl like :P_TASK_URL) "),
		@NamedQuery(
				name = "wf_task_getWFTaskByTaskIdsList",
				query = " select t from WFTask t " +
						" where t.taskId in (:P_TASK_IDS_LIST) ")
})

@Entity
@Table(name = "FIS_WF_TASKS")
public class WFTask extends AuditEntity implements Serializable, InsertableAuditEntity, DeleteableAuditEntity, UpdateableAuditEntity {
	private Long taskId;
	private Long instanceId;
	private Long assigneeId;
	private Long originalId;
	private Date assignDate;
	private Date hijriAssignDate;
	private String taskUrl;
	private String assigneeWfRole;
	private String action;
	private Date actionDate;
	private Date hijriActionDate;
	private String notes;
	private String refuseReasons;
	private String attachments;
	private String flexField1;
	private String flexField2;
	private String flexField3;
	private String arabicDetailsSummary;
	private String englishDetailsSummary;
	private String level;
	private Long version;

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	@SequenceGenerator(name = "FIS_WF_TASKS_SEQ", sequenceName = "FIS_WF_TASKS_SEQ", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_WF_TASKS_SEQ")
	@Column(name = "ID")
	public Long getTaskId() {
		return taskId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	@Basic
	@Column(name = "INSTANCE_ID")
	public Long getInstanceId() {
		return instanceId;
	}

	public void setAssigneeId(Long assigneeId) {
		this.assigneeId = assigneeId;
	}

	@Basic
	@Column(name = "ASSIGNEE_ID")
	public Long getAssigneeId() {
		return assigneeId;
	}

	public void setOriginalId(Long originalId) {
		this.originalId = originalId;
	}

	@Basic
	@Column(name = "ORIGINAL_ID")
	public Long getOriginalId() {
		return originalId;
	}

	public void setAssignDate(Date assignDate) {
		this.assignDate = assignDate;
	}

	@Basic
	@Column(name = "ASSIGN_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getAssignDate() {
		return assignDate;
	}

	public void setHijriAssignDate(Date hijriAssignDate) {
		this.hijriAssignDate = hijriAssignDate;
	}

	@Basic
	@Column(name = "HIJRI_ASSIGN_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getHijriAssignDate() {
		return hijriAssignDate;
	}

	public void setTaskUrl(String taskUrl) {
		this.taskUrl = taskUrl;
	}

	@Basic
	@Column(name = "TASK_URL")
	public String getTaskUrl() {
		return taskUrl;
	}

	public void setAssigneeWfRole(String assigneeWfRole) {
		this.assigneeWfRole = assigneeWfRole;
	}

	@Basic
	@Column(name = "ASSIGNEE_WF_ROLE")
	public String getAssigneeWfRole() {
		return assigneeWfRole;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Basic
	@Column(name = "ACTION")
	public String getAction() {
		return action;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	@Basic
	@Column(name = "ACTION_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getActionDate() {
		return actionDate;
	}

	public void setHijriActionDate(Date hijriActionDate) {
		this.hijriActionDate = hijriActionDate;
	}

	@Basic
	@Column(name = "HIJRI_ACTION_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getHijriActionDate() {
		return hijriActionDate;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Basic
	@Column(name = "NOTES")
	public String getNotes() {
		return notes;
	}

	public void setRefuseReasons(String refuseReasons) {
		this.refuseReasons = refuseReasons;
	}

	@Basic
	@Column(name = "REFUSE_REASONS")
	public String getRefuseReasons() {
		return refuseReasons;
	}

	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}

	@Basic
	@Column(name = "ATTACHMENTS")
	public String getAttachments() {
		return attachments;
	}

	public void setFlexField1(String flexField1) {
		this.flexField1 = flexField1;
	}

	@Basic
	@Column(name = "FLEX_FIELD1")
	public String getFlexField1() {
		return flexField1;
	}

	public void setFlexField2(String flexField2) {
		this.flexField2 = flexField2;
	}

	@Basic
	@Column(name = "FLEX_FIELD2")
	public String getFlexField2() {
		return flexField2;
	}

	public void setFlexField3(String flexField3) {
		this.flexField3 = flexField3;
	}

	@Basic
	@Column(name = "FLEX_FIELD3")
	public String getFlexField3() {
		return flexField3;
	}

	@Basic
	@Column(name = "ARABIC_DETAILS_SUMMARY")
	public String getArabicDetailsSummary() {
		return arabicDetailsSummary;
	}

	public void setArabicDetailsSummary(String arabicDetailsSummary) {
		this.arabicDetailsSummary = arabicDetailsSummary;
	}

	@Basic
	@Column(name = "ENGLISH_DETAILS_SUMMARY")
	public String getEnglishDetailsSummary() {
		return englishDetailsSummary;
	}

	public void setEnglishDetailsSummary(String englishDetailsSummary) {
		this.englishDetailsSummary = englishDetailsSummary;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	@Basic
	@Column(name = "TASK_LEVEL")
	public String getLevel() {
		return level;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@Version
	@Column(name = "VERSION")
	/*
	 * This attribute handles the optimistic transaction management for the task entity.
	 */
	public Long getVersion() {
		return version;
	}

	@Override
	public void setId(Long id) {
		this.taskId = id;
	}

	@Override
	public Long calculateContentId() {
		return this.taskId;
	}
}