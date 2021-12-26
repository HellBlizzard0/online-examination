package com.code.ui.backings.minisearch;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.securityanalysis.ReminderData;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "reminderMiniSearch")
@ViewScoped
public class ReminderMiniSearch extends BaseBacking implements Serializable {
	private ReminderData reminderData;

	public ReminderMiniSearch() {
		super();
		setReminderData(new ReminderData());
	}

	public ReminderData getReminderData() {
		return reminderData;
	}

	public void setReminderData(ReminderData reminderData) {
		this.reminderData = reminderData;
	}

}
