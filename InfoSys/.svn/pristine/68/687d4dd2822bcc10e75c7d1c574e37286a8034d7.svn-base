package com.code.dal.orm.workflow;


import com.code.dal.orm.BaseEntity;
import com.code.services.util.HijriDateService;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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


@SuppressWarnings("serial")
@NamedQueries({
          @NamedQuery(name = "wf_taskData_searchWFTasksData", 
                      query= " select t from WFTaskData t " +
                             " where t.assigneeId = :P_ASSIGNEE_ID" +
                             " and t.taskOwnerName like :P_TASK_OWNER_NAME " +
                             " and ((t.arabicDetailsSummary like :P_TASK_DETAILS or t.englishDetailsSummary like :P_TASK_DETAILS) or (:P_TASK_DETAILS_FLAG = 0 and t.arabicDetailsSummary  is null))" +
                             " and (:P_PROCESS_GROUP_ID = 0 OR t.processGroupId = :P_PROCESS_GROUP_ID) " +
                             " and (:P_PROCESS_ID = 0 OR t.processId = :P_PROCESS_ID) " +
                             " and ((:P_RUNNING = 1 and t.action is null) OR (:P_RUNNING = 0 and t.action is not null)) " +
                             " and (:P_TASK_ROLE = -1 or (:P_TASK_ROLE = 1 and t.assigneeWfRole not in (:P_NOTIFICATION_ROLE) ) or (:P_TASK_ROLE = 2 and t.assigneeWfRole in (:P_NOTIFICATION_ROLE) ) ) " +
                             " and (:P_FROM_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_FROM_DATE, 'MI/MM/YYYY') <= t.hijriAssignDate)" + 
    	           			 " and (:P_TO_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_TO_DATE, 'MI/MM/YYYY') >= t.hijriAssignDate) " +
                             " order by t.assignDate desc "                             
          ),
          @NamedQuery(name = "wf_taskData_getWFInstanceTasksData", 
	                  query= " select t from WFTaskData t " +
                             " where t.instanceId = :P_INSTANCE_ID " +
                             " order by t.assignDate "
          ),
          @NamedQuery(name = "wf_taskData_getWFTaskDataByInstanceIdAndAssigneeRole", 
          query= " select t from WFTaskData t " +
                 " where t.instanceId = :P_INSTANCE_ID " +
        		 " and (:P_ASSIGNEE_WF_ROLE = '-1' or t.assigneeWfRole = :P_ASSIGNEE_WF_ROLE)" +
                 " order by t.assignDate "
          ),
          
          @NamedQuery(name="wf_taskData_getWFInstanceCompletedTasksData", 
                      query= " select t from WFTaskData t " +
                             " where t.instanceId = :P_INSTANCE_ID " +
                             " and t.action is not null " +
                             " and t.taskId < :P_TASK_ID " +
                             " and t.level in :P_LEVELS " +                             
                             " order by t.assignDate "                             
          ),
          @NamedQuery(name = "wf_taskData_getWFTaskDataById", 
	                  query= " select t from WFTaskData t " +
                             " where t.taskId = :P_TASK_ID "                             
          ),
          @NamedQuery(name = "wf_taskData_searchWFFishingTasksData", 
          query= " select t from WFTaskData t " +
                 " where (:P_ASSIGNEE_ID = -1 or t.assigneeId = :P_ASSIGNEE_ID)" +
                 " and (:P_TASK_OWNER_NAME = '-1' or t.taskOwnerName like :P_TASK_OWNER_NAME)" +
                 " and (:P_TASK_DETAILS = '-1' or t.arabicDetailsSummary like :P_TASK_DETAILS)" +
                 " and (:P_SOCIAL_ID = '-1' or t.arabicDetailsSummary like :P_SOCIAL_ID)" +
                 " and (:P_FULL_NAME = '-1' or t.arabicDetailsSummary like :P_FULL_NAME)" +
                 " and (:P_ACTIVITY_DOMAIN = '-1' or t.arabicDetailsSummary like :P_ACTIVITY_DOMAIN)" +
                 " and (:P_NAVIGATION_DOCK = '-1' or t.arabicDetailsSummary like :P_NAVIGATION_DOCK)" +
                 " and (:P_PROCESS_ID = -1 or t.processId = :P_PROCESS_ID) " +
                 " and (:P_FROM_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_FROM_DATE, 'MI/MM/YYYY') <= t.hijriAssignDate)" + 
       			 " and (:P_TO_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_TO_DATE, 'MI/MM/YYYY') >= t.hijriAssignDate) " + 
                 " order by t.assignDate desc "                             
          ),
          @NamedQuery(name="wf_taskData_getWFInstanceCompletedTasksDataSorted", 
 		   			  query= " select t from WFTaskData t " +
 		   					 " where t.instanceId = :P_INSTANCE_ID " +
 		   					 " order by  t.level , t.assignDate , t.taskId"
          ),
          @NamedQuery(name = "wf_taskData_getWFTaskIds", 
			          query= " select t.taskId from WFTaskData t " +
			                 " where :P_PROCESS_ID = t.processId " +
			                 " and :P_ASSIGNEE_ROLE = t.assigneeWfRole " +
			                 " and :P_ASSIGNEE_ID = t.assigneeId " +
			                 " and t.action is NULL "
          )
})

@Entity
@Table(name = "FIS_VW_TASKS")
public class WFTaskData extends BaseEntity implements Serializable {
	private Long taskId;
	private Long instanceId;
	private Long processId;
	private String processName;
	private Long processGroupId;
	private String taskOwnerName;
	private Long assigneeId;
	private Long delegatingId;
	private String delegatingName;
	private Long originalId;
	private String originalNumber;
	private String originalName;
	private String originalRankDesc;
	private Date assignDate;
	private Date hijriAssignDate;
	private String hijriAssignDateString;
	private String taskUrl;
	private String assigneeWfRole;
	private String action;
	private Date actionDate;
	private Date hijriActionDate;
	private String hijriActionDateString;
	private String notes;
	private String refuseReasons;
	private String attachments;
	private String flexField1;
	private String flexField2;
	private String flexField3;
    private String arabicDetailsSummary;
    private String englishDetailsSummary;
	private String level;
	private WFTask wFtask;

	public WFTaskData() {
		wFtask = new WFTask();
	}

	public void setTaskId(Long taskId) {
		wFtask.setTaskId(taskId);
		this.taskId = taskId;
	}

	@Id
	@Column(name = "ID")
	public Long getTaskId() {
		return wFtask.getTaskId();
	}

	public void setInstanceId(Long instanceId) {
		wFtask.setInstanceId(instanceId);
		this.instanceId = instanceId;
	}

	@Basic
	@Column(name = "INSTANCE_ID")
	public Long getInstanceId() {
		return instanceId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	@Basic
	@Column(name = "PROCESS_ID")
	public Long getProcessId() {
		return processId;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	@Basic
	@Column(name = "PROCESS_NAME")
	public String getProcessName() {
		return processName;
	}

	public void setProcessGroupId(Long processGroupId) {
		this.processGroupId = processGroupId;
	}

	@Basic
	@Column(name = "PROCESS_GROUP_ID")
	public Long getProcessGroupId() {
		return processGroupId;
	}

	public void setTaskOwnerName(String taskOwnerName) {
		this.taskOwnerName = taskOwnerName;
	}

	@Basic
	@Column(name = "TASK_OWNER_NAME")
	public String getTaskOwnerName() {
		return taskOwnerName;
	}

	public void setAssigneeId(Long assigneeId) {
		wFtask.setAssigneeId(assigneeId);
		this.assigneeId = assigneeId;
	}

	@Basic
	@Column(name = "ASSIGNEE_ID")
	public Long getAssigneeId() {
		return assigneeId;
	}

	public void setDelegatingId(Long delegatingId) {
		this.delegatingId = delegatingId;
	}

	@Basic
	@Column(name = "DELEGATING_ID")
	public Long getDelegatingId() {
		return delegatingId;
	}

	public void setDelegatingName(String delegatingName) {
		this.delegatingName = delegatingName;
	}

	@Basic
	@Column(name = "DELEGATING_NAME")
	public String getDelegatingName() {
		return delegatingName;
	}

	public void setOriginalId(Long originalId) {
		wFtask.setOriginalId(originalId);
		this.originalId = originalId;
	}

	@Basic
	@Column(name = "ORIGINAL_ID")
	public Long getOriginalId() {
		return originalId;
	}

	public void setOriginalNumber(String originalNumber) {
		this.originalNumber = originalNumber;
	}

	@Basic
	@Column(name = "ORIGINAL_NUMBER")
	public String getOriginalNumber() {
		return originalNumber;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	@Basic
	@Column(name = "ORIGINAL_NAME")
	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalRankDesc(String originalRankDesc) {
		this.originalRankDesc = originalRankDesc;
	}

	@Basic
	@Column(name = "ORIGINAL_RANK_DESC")
	public String getOriginalRankDesc() {
		return originalRankDesc;
	}

	@Basic
	@Column(name = "ASSIGN_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getAssignDate() {
		return assignDate;
	}

	public void setAssignDate(Date assignDate) {
		wFtask.setAssignDate(assignDate);
		this.assignDate = assignDate;
	}

	public void setHijriAssignDate(Date hijriAssignDate) {
		wFtask.setHijriActionDate(hijriAssignDate);
		this.hijriAssignDate = hijriAssignDate;

		if (this.assignDate != null && this.hijriAssignDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			this.hijriAssignDateString = HijriDateService.getHijriDateString(hijriAssignDate) + " " + sdf.format(assignDate);
		}
	}

	@Basic
	@Column(name = "HIJRI_ASSIGN_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getHijriAssignDate() {
		return hijriAssignDate;
	}

	public void setTaskUrl(String taskUrl) {
		wFtask.setTaskUrl(taskUrl);
		this.taskUrl = taskUrl;
	}

	@Basic
	@Column(name = "TASK_URL")
	public String getTaskUrl() {
		return taskUrl;
	}

	public void setAssigneeWfRole(String assigneeWfRole) {
		wFtask.setAssigneeWfRole(assigneeWfRole);
		this.assigneeWfRole = assigneeWfRole;
	}

	@Basic
	@Column(name = "ASSIGNEE_WF_ROLE")
	public String getAssigneeWfRole() {
		return assigneeWfRole;
	}

	public void setAction(String action) {
		wFtask.setAction(action);
		this.action = action;
	}

	@Basic
	@Column(name = "ACTION")
	public String getAction() {
		return action;
	}

	public void setActionDate(Date actionDate) {
		wFtask.setActionDate(actionDate);
		this.actionDate = actionDate;

		if (this.actionDate != null && this.hijriActionDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			this.hijriActionDateString = HijriDateService.getHijriDateString(hijriActionDate) + " " + sdf.format(actionDate);
		}
	}

	@Basic
	@Column(name = "ACTION_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getActionDate() {
		return actionDate;
	}

	public void setHijriActionDate(Date hijriActionDate) {
		wFtask.setHijriActionDate(hijriActionDate);
		this.hijriActionDate = hijriActionDate;

		if (this.actionDate != null && this.hijriActionDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			this.hijriActionDateString = HijriDateService.getHijriDateString(hijriActionDate) + " " + sdf.format(actionDate);
		}
	}

	@Basic
	@Column(name = "HIJRI_ACTION_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getHijriActionDate() {
		return hijriActionDate;
	}

	public void setNotes(String notes) {
		wFtask.setNotes(notes);
		this.notes = notes;
	}

	@Basic
	@Column(name = "NOTES")
	public String getNotes() {
		return notes;
	}

	public void setRefuseReasons(String refuseReasons) {
		wFtask.setRefuseReasons(refuseReasons);
		this.refuseReasons = refuseReasons;
	}

	@Basic
	@Column(name = "REFUSE_REASONS")
	public String getRefuseReasons() {
		return refuseReasons;
	}

	public void setAttachments(String attachments) {
		wFtask.setAttachments(attachments);
		this.attachments = attachments;
	}

	@Basic
	@Column(name = "ATTACHMENTS")
	public String getAttachments() {
		return attachments;
	}

	public void setFlexField1(String flexField1) {
		wFtask.setFlexField1(flexField1);
		this.flexField1 = flexField1;
	}

	@Basic
	@Column(name = "FLEX_FIELD1")
	public String getFlexField1() {
		return flexField1;
	}

	public void setFlexField2(String flexField2) {
		wFtask.setFlexField2(flexField2);
		this.flexField2 = flexField2;
	}

	@Basic
	@Column(name = "FLEX_FIELD2")
	public String getFlexField2() {
		return flexField2;
	}

	public void setFlexField3(String flexField3) {
		wFtask.setFlexField3(flexField3);
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
		wFtask.setLevel(level);
		this.level = level;
	}

	@Basic
	@Column(name = "TASK_LEVEL")
	public String getLevel() {
		return level;
	}

	@Transient
	public String getHijriAssignDateString() {
		return hijriAssignDateString;
	}

	@Transient
	public String getHijriActionDateString() {
		return hijriActionDateString;
	}

	@Transient
	public WFTask getwFtask() {
		return wFtask;
	}

	public void setwFtask(WFTask wFtask) {
		this.wFtask = wFtask;
	}

	@Override
	public void setId(Long id) {
		wFtask.setTaskId(id);
		this.taskId = id;
	}
}