package com.code.ui.backings.security;

import java.text.SimpleDateFormat;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.code.dal.orm.setup.EmployeeData;
import com.code.enums.FlagsEnum;
import com.code.enums.SessionAttributesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.log4j.Log4j;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.BaseWorkFlow;
import com.code.ui.backings.base.BaseBacking;

@ManagedBean(name = "headerBB")
@RequestScoped
public class Header extends BaseBacking {
	private String on;
	private String off;
	private String hijriSystemDate;
	private String GeorgSystemDate;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/mm");

	private String inboxCount;
	private String notificationsCount;

	public Header() {
		super();
		on = FlagsEnum.ON.getCode() + "";
		off = FlagsEnum.OFF.getCode() + "";
		try {
			hijriSystemDate = HijriDateService.getHijriSysDateString();
			GeorgSystemDate = HijriDateService.hijriToGregDateString(hijriSystemDate);
			hijriSystemDate = sdf.format(HijriDateService.getHijriSysDate());
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}

		this.loginEmpData = (EmployeeData) getSession().getAttribute(SessionAttributesEnum.EMP_DATA.getCode());

		calculateInboxCount();
		calculateNotificationsCount();
	}

	private void calculateInboxCount() {
		try {
			inboxCount = BaseWorkFlow.countWFTasks(this.loginEmpData.getEmpId(), FlagsEnum.OFF.getCode()) + "";
		} catch (BusinessException e) {
			Log4j.traceErrorException(Header.class, e, "Header");
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			inboxCount = "";
		}
	}

	private void calculateNotificationsCount() {
		try {
			notificationsCount = BaseWorkFlow.countWFTasks(this.loginEmpData.getEmpId(), FlagsEnum.ON.getCode()) + "";
		} catch (BusinessException e) {
			Log4j.traceErrorException(Header.class, e, "Header");
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			notificationsCount = "";
		}
	}

	public String getInboxLink() {
		return getRequest().getContextPath() + "/WorkList/WFInbox.jsf";
	}

	public String getOn() {
		return on;
	}

	public void setOn(String on) {
		this.on = on;
	}

	public String getOff() {
		return off;
	}

	public void setOff(String off) {
		this.off = off;
	}

	public String getHijriSystemDate() {
		return hijriSystemDate;
	}

	public void setHijriSystemDate(String hijriSystemDate) {
		this.hijriSystemDate = hijriSystemDate;
	}

	public String getGeorgSystemDate() {
		return GeorgSystemDate;
	}

	public void setGeorgSystemDate(String georgSystemDate) {
		GeorgSystemDate = georgSystemDate;
	}

	public String getInboxCount() {
		return inboxCount;
	}

	public String getNotificationsCount() {
		return notificationsCount;
	}
}
