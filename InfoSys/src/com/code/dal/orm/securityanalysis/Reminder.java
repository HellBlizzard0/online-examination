package com.code.dal.orm.securityanalysis;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.code.dal.orm.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_SC_REMINDERS")
public class Reminder extends BaseEntity implements Serializable {
	private Long id;
	private Date reminderDate;
	private String reminderDateString;
	private String reminderTime;
	private String reminderDetails;
	private Long conversationId;
	private Long empId;
	private Boolean doneFlag;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_SC_REMINDER_SEQ", sequenceName = "FIS_SC_REMINDER_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_SC_REMINDER_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
	}

	@Basic
	@Column(name = "REMINDER_TIME")
	public String getReminderTime() {
		return reminderTime;
	}

	public void setReminderTime(String reminderTime) {
		this.reminderTime = reminderTime;
	}

	@Basic
	@Column(name = "REMINDER_DETAILS")
	public String getReminderDetails() {
		return reminderDetails;
	}

	public void setReminderDetails(String reminderDetails) {
		this.reminderDetails = reminderDetails;
	}

	@Basic
	@Column(name = "CONVERSATION_ID")
	public Long getConversationId() {
		return conversationId;
	}

	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
	}

	@Transient
	public String getReminderDateString() {
		return reminderDateString;
	}

	public void setReminderDateString(String reminderDateString) {
		this.reminderDateString = reminderDateString;
	}

	@Basic
	@Column(name = "EMP_ID")
	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Basic
	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Column(name = "DONE_FLAG")
	public Boolean getDoneFlag() {
		return doneFlag;
	}

	public void setDoneFlag(Boolean doneFlag) {
		this.doneFlag = doneFlag;
	}
}
