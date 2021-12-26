package com.code.ui.backings.worklist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;

import com.code.dal.orm.workflow.WFInstanceData;
import com.code.dal.orm.workflow.WFProcess;
import com.code.dal.orm.workflow.WFProcessGroup;
import com.code.exceptions.BusinessException;
import com.code.services.workflow.BaseWorkFlow;
import com.code.ui.backings.base.BaseBacking;

/*
 import com.code.enums.MenuActionsEnum;
 import com.code.enums.MenuCodesEnum;*/

@ManagedBean(name = "wfOutbox")
@SessionScoped
public class WFOutbox extends BaseBacking {
	private List<WFInstanceData> instances;
	private String searchByRequesterId;
	private String searchByRequesterName;
	private List<WFProcessGroup> processesGroups;
	private long selectedProcessGroupId;
	private List<WFProcess> processes;
	private long selectedProcessId;
	private boolean runningFlag;
	private boolean isASC;
	private boolean admin = false;
	private String searchDetails = "";
	private Date fromDate;
	private Date toDate;

	private int pageSize = 10;

	public WFOutbox() {
		super();
		this.setScreenTitle(getParameterizedMessage("title_followingOutboxProcesses"));

		try {
			processesGroups = BaseWorkFlow.getProcessesGroups();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	public void init() {
		super.init();

		searchByRequesterId = this.loginEmpData.getEmpId() + "";
		searchByRequesterName = this.loginEmpData.getFullName();
		selectedProcessGroupId = 0;
		runningFlag = true;
		isASC = false;

		searchProcesses(null);

		/*
		 * if (getRequest().getParameter("init") == null)
		 * this.setServerSideSuccessMessages
		 * (getParameterizedMessage("notify_requestSent"));
		 */

		/*
		 * try { if (getRequest().getParameter("A") != null) { if
		 * (SecurityService
		 * .isEmployeeMenuActionGranted(this.loginEmpData.getEmpId(),
		 * MenuCodesEnum.WF_OUTBOX.getCode(),
		 * MenuActionsEnum.WF_OUTBOX_ADMIN.getCode())) admin = true; } else
		 * admin = false; } catch (Exception e) { admin = false; }
		 */
		admin = false;
	}

	public void searchProcesses(AjaxBehaviorEvent event) {
		try {
			processes = BaseWorkFlow.getGroupProcesses(selectedProcessGroupId);
			selectedProcessId = 0;
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}

		searchInstances();
	}

	private void searchInstances() {
		try {
			if (searchByRequesterId == null || searchByRequesterId.equals("")) {
				instances = new ArrayList<WFInstanceData>();
				return;
			}

			long requesterId = Long.parseLong(searchByRequesterId);
			/*
			 * if (!this.loginEmpData.getEmpId().equals(requesterId)) { if
			 * (!admin &&
			 * !EmployeesService.getEmpData(requesterId).getManagerId(
			 * ).equals(this.loginEmpData.getEmpId())) { instances = new
			 * ArrayList<WFInstanceData>();
			 * this.setServerSideErrorMessages(getParameterizedMessage
			 * ("error_notAuthorized")); return; } }
			 */

			instances = BaseWorkFlow.searchWFInstancesData(requesterId, selectedProcessGroupId, selectedProcessId, runningFlag, isASC, searchDetails, fromDate, toDate);
		} catch (BusinessException e) {
			instances = new ArrayList<WFInstanceData>();
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			instances = new ArrayList<WFInstanceData>();
			return;
		}
	}

	public void searchByRequesterId() {
		searchInstances();
	}

	public void searchByProcess(AjaxBehaviorEvent event) {
		searchInstances();
	}

	public void sortASC() {
		isASC = true;
		searchInstances();
	}

	public void sortDESC() {
		isASC = false;
		searchInstances();
	}

	public void getRunningInstances() {
		runningFlag = true;
		searchInstances();
	}

	public void getDoneCompletedInstances() {
		runningFlag = false;
		searchInstances();
	}

	// -----------------------------------------------------------------------------------------------------------------

	public void setInstances(List<WFInstanceData> instances) {
		this.instances = instances;
	}

	public List<WFInstanceData> getInstances() {
		return instances;
	}

	public void setSearchByRequesterId(String searchByRequesterId) {
		this.searchByRequesterId = searchByRequesterId;
	}

	public String getSearchByRequesterId() {
		return searchByRequesterId;
	}

	public void setProcessesGroups(List<WFProcessGroup> processesGroups) {
		this.processesGroups = processesGroups;
	}

	public List<WFProcessGroup> getProcessesGroups() {
		return processesGroups;
	}

	public void setSelectedProcessGroupId(long selectedProcessGroupId) {
		this.selectedProcessGroupId = selectedProcessGroupId;
	}

	public long getSelectedProcessGroupId() {
		return selectedProcessGroupId;
	}

	public void setProcesses(List<WFProcess> processes) {
		this.processes = processes;
	}

	public List<WFProcess> getProcesses() {
		return processes;
	}

	public void setSelectedProcessId(long selectedProcessId) {
		this.selectedProcessId = selectedProcessId;
	}

	public long getSelectedProcessId() {
		return selectedProcessId;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	public String getSearchByRequesterName() {
		return searchByRequesterName;
	}

	public void setSearchByRequesterName(String searchByRequesterName) {
		this.searchByRequesterName = searchByRequesterName;
	}

	public boolean isRunningFlag() {
		return runningFlag;
	}

	public void setRunningFlag(boolean runningFlag) {
		this.runningFlag = runningFlag;
	}

	public String getSearchDetails() {
		return searchDetails;
	}

	public void setSearchDetails(String searchDetails) {
		this.searchDetails = searchDetails;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
}