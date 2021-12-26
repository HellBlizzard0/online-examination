package com.code.dal.orm.securityanalysis;

import java.io.Serializable;
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

import org.hibernate.annotations.Type;

import com.code.dal.orm.BaseEntity;
import com.code.services.util.HijriDateService;

@NamedQueries({
	@NamedQuery(name = "reminderData_searchReminders",
				query = " select reminder " +
						" from ReminderData reminder " +
						" where (:P_CONVERSATION_ID = -1 or :P_CONVERSATION_ID = reminder.conversationId)" +
						" and (:P_DONE_FLAG = -1 or :P_DONE_FLAG = reminder.doneFlag) " +
						" order by reminder.id ")
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_SC_VW_REMINDERS")
public class ReminderData extends BaseEntity implements Serializable {
	private Long id;
	private Date reminderDate;
	private String reminderDateString;
	private String reminderTime;
	private String reminderDetails;
	private Long conversationId;
	private Reminder reminder;
	private Long empId;
	private Boolean doneFlag;

	public ReminderData() {
		reminder = new Reminder();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.reminder.setId(id);
		this.id = id;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REMINDER_DATE")
	public Date getReminderDate() {
		return reminderDate;
	}

	public void setReminderDate(Date reminderDate) {
		this.reminderDate = reminderDate;
		this.reminderDateString = HijriDateService.getHijriDateString(reminderDate);
		this.reminder.setReminderDate(reminderDate);
	}

	@Transient
	public String getReminderDateString() {
		return reminderDateString;
	}

	public void setReminderDateString(String reminderDateString) {
		this.reminderDateString = reminderDateString;
		this.setReminderDate(HijriDateService.getHijriDate(reminderDateString));
	}

	@Basic
	@Column(name = "REMINDER_TIME")
	public String getReminderTime() {
		return reminderTime;
	}

	public void setReminderTime(String reminderTime) {
		this.reminderTime = reminderTime;
		this.reminder.setReminderTime(reminderTime);
	}

	@Basic
	@Column(name = "REMINDER_DETAILS")
	public String getReminderDetails() {
		return reminderDetails;
	}

	public void setReminderDetails(String reminderDetails) {
		this.reminderDetails = reminderDetails;
		this.reminder.setReminderDetails(reminderDetails);
	}

	@Basic
	@Column(name = "CONVERSATION_ID")
	public Long getConversationId() {
		return conversationId;
	}

	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
		this.reminder.setConversationId(conversationId);
	}

	@Transient
	public Reminder getReminder() {
		return reminder;
	}

	public void setReminder(Reminder reminder) {
		this.reminder = reminder;
	}

	@Basic
	@Column(name = "EMP_ID")
	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
		this.reminder.setEmpId(empId);
	}

	@Basic
	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Column(name = "DONE_FLAG")
	public Boolean getDoneFlag() {
		return doneFlag;
	}

	public void setDoneFlag(Boolean doneFlag) {
		this.doneFlag = doneFlag;
		this.reminder.setDoneFlag(doneFlag);
	}
}
