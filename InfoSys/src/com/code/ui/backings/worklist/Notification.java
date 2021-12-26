package com.code.ui.backings.worklist;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.workflow.WFTask;
import com.code.enums.WFTaskUrlEnum;
import com.code.exceptions.BusinessException;
import com.code.services.workflow.BaseWorkFlow;
import com.code.ui.backings.base.WFBaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "notification")
@ViewScoped
public class Notification extends WFBaseBacking implements Serializable {
	private String notes;

	public Notification() {
		super();
		try {
			init();
			BaseWorkFlow.doNotified(currentTask);
			notes = currentTask.getNotes();
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	public void getNextNotification() {
		try {
			WFTask nextTask = BaseWorkFlow.getWfNextNotification(currentTask.getAssigneeId(), currentTask.getTaskId(), true, WFTaskUrlEnum.NOTIFICATION.getCode());
			if (nextTask == null) {
				super.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
			} else {
				BaseWorkFlow.doNotified(nextTask);
				notes = nextTask.getNotes();
				currentTask = nextTask;
			}
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
