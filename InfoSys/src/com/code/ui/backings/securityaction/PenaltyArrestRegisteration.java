package com.code.ui.backings.securityaction;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.securitymission.PenaltyArrestData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.enums.NavigationEnum;
import com.code.enums.PenaltyArrestLocationEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.securityaction.PenaltyArrestService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.EmployeeService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.securityaction.PenaltyArrestWorkFlow;
import com.code.ui.backings.base.WFBaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "penaltyArrestRegisteration")
@ViewScoped
public class PenaltyArrestRegisteration extends WFBaseBacking implements Serializable {
	private String screenTitle;
	private Integer pageMode;
	private PenaltyArrestData penaltyArrestData;
	private EmployeeData arrester;
	private EmployeeData arrested;
	private List<PenaltyArrestLocationEnum> penaltyArrestLocationEnums;

	/**
	 * Default Constructor
	 */
	public PenaltyArrestRegisteration() {
		super();
		super.init();
		this.init();
		try {
			penaltyArrestLocationEnums = Arrays.asList(PenaltyArrestLocationEnum.class.getEnumConstants());
			// Check Page Roles
			if (getRequest().getAttribute("mode") != null) {
				penaltyArrestData = (PenaltyArrestData) getRequest().getAttribute("mode");
			}

			if (currentTask == null && penaltyArrestData.getId() == null) {
				pageMode = 1;
				penaltyArrestData.setRequestDate(HijriDateService.getHijriSysDate());
			} else {
				pageMode = 2;
				if (penaltyArrestData.getId() == null) {
					penaltyArrestData = PenaltyArrestService.getPenaltyArrestByWFInsatnceId(currentTask.getInstanceId());
				}
				arrester = EmployeeService.getEmployee(penaltyArrestData.getRequesterEmployeeId());
				arrested = EmployeeService.getEmployee(penaltyArrestData.getArrestedEmployeeId());
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage()));
		}catch (Exception e) {
			   Log4j.traceErrorException(PenaltyArrestRegisteration.class, e, "PenaltyArrestRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Intialize Variables
	 */
	public void init() {
		penaltyArrestData = new PenaltyArrestData();
		arrester = new EmployeeData();
		arrested = new EmployeeData();
		screenTitle = getParameterizedMessage("title_penaltyArrestRegisteration");
	}

	/**
	 * Update Arrester Details
	 */
	public void updateArresterDetails() {
		try {
			arrester = EmployeeService.getEmployee(penaltyArrestData.getRequesterEmployeeId());
			penaltyArrestData.setRequesterEmployeeName(arrester.getFullName());
			penaltyArrestData.setRequesterEmployeeDepartmentName(arrester.getActualDepartmentName());
			penaltyArrestData.setRequesterEmployeeRank(arrester.getRank());
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage()));
		}
	}

	/**
	 * Update Arrested Details
	 */
	public void updateArrestedDetails() {
		try {
			arrested = EmployeeService.getEmployee(penaltyArrestData.getArrestedEmployeeId());
			penaltyArrestData.setArrestedEmployeeName(arrested.getFullName());
			penaltyArrestData.setArrestedEmployeeDepartmentName(arrested.getActualDepartmentName());
			penaltyArrestData.setArrestedEmployeeRank(arrested.getRank());
			penaltyArrestData.setArrestedEmployeeNumber(arrested.getMilitaryNo());
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage()));
		}
	}

	/**
	 * Save and Send Request
	 * 
	 * @return
	 */
	public String saveAndSend() {
		try {
			PenaltyArrestWorkFlow.initPenaltyArrest(penaltyArrestData, loginEmpData, null);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage()));
			return null;
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Do Approve
	 * 
	 * @return
	 */
	public String doApprove() {
		try {
			PenaltyArrestWorkFlow.doApprove(currentTask, penaltyArrestData);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage()));
			return null;
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Do Notified
	 * 
	 * @return
	 */
	public String doNotified() {
		try {
			PenaltyArrestWorkFlow.doNotify(currentTask);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage()));
			return null;
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	public String getScreenTitle() {
		return screenTitle;
	}

	public void setScreenTitle(String screenTitle) {
		this.screenTitle = screenTitle;
	}

	public Integer getPageMode() {
		return pageMode;
	}

	public void setPageMode(Integer pageMode) {
		this.pageMode = pageMode;
	}

	public PenaltyArrestData getPenaltyArrestData() {
		return penaltyArrestData;
	}

	public void setPenaltyArrestData(PenaltyArrestData penaltyArrestData) {
		this.penaltyArrestData = penaltyArrestData;
	}

	public EmployeeData getArrester() {
		return arrester;
	}

	public void setArrester(EmployeeData arrester) {
		this.arrester = arrester;
	}

	public EmployeeData getArrested() {
		return arrested;
	}

	public void setArrested(EmployeeData arrested) {
		this.arrested = arrested;
	}

	public List<PenaltyArrestLocationEnum> getPenaltyArrestLocationEnums() {
		return penaltyArrestLocationEnums;
	}

	public void setPenaltyArrestLocationEnums(List<PenaltyArrestLocationEnum> penaltyArrestLocationEnums) {
		this.penaltyArrestLocationEnums = penaltyArrestLocationEnums;
	}
}