package com.code.ui.backings.worklist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;

import com.code.dal.orm.workflow.WFProcess;
import com.code.dal.orm.workflow.WFProcessGroup;
import com.code.dal.orm.workflow.WFTaskData;
import com.code.enums.FlagsEnum;
import com.code.enums.ServiceCodesEnum;
import com.code.enums.SessionAttributesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.attach.AttachmentServiceCallBack;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.log4j.Log4j;
import com.code.services.workflow.BaseWorkFlow;
import com.code.ui.backings.base.BaseBacking;
import com.code.ui.util.EncryptionUtil;

@ManagedBean(name = "wfInbox")
@SessionScoped
public class WFInbox extends BaseBacking {
	private List<WFTaskData> tasks;
	private String searchByTaskOwnerName;
	private long searchByAssigneeId;
	private String searchByAssigneeName;
	private List<WFProcessGroup> processesGroups;
	private long selectedProcessGroupId;
	private Integer selectedGridIndex;
	private List<WFProcess> processes;
	private long selectedProcessId;
	private int selectedTaskRole;
	private boolean runningFlag;
	private boolean isDESC;
	private boolean viewDelegation;
	private boolean admin = false;
	private String fileArchivingParam;
	private String searchByTaskDetails;
	private Date fromDate;
	private Date toDate;

	private Long searchEmpId;

	private int pageSize = 10;

	public WFInbox() {
		super();
		this.setScreenTitle(getParameterizedMessage("title_followingInboxProcesses"));

		try {
			processesGroups = BaseWorkFlow.getProcessesGroups();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	public void init() {
		super.init();
		fileArchivingParam = "";
		selectedTaskRole = FlagsEnum.ALL.getCode();
		if (getRequest().getParameter("role") != null) {
			selectedTaskRole = Integer.parseInt(getRequest().getParameter("role"));
		}

		try {
			if (getRequest().getAttribute(SessionAttributesEnum.FILE_ARCHIVING_PARAM.getCode()) != null) {
				String requestURL = getRequest().getRequestURL() + "";
				String url = requestURL.replace(getRequest().getServletPath(), "");
				String serviceURL = url + AttachmentServiceCallBack.SERVICE_URL;
				fileArchivingParam = (String) getRequest().getAttribute(SessionAttributesEnum.FILE_ARCHIVING_PARAM.getCode());
				fileArchivingParam = EncryptionUtil.encryptSymmetrically("code=" + ServiceCodesEnum.INFO_SYS.getCode() + "&token=" + fileArchivingParam + "&serviceurl=" + serviceURL);
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(WFInbox.class, e, "WFInbox");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
		searchByTaskOwnerName = "";
		searchByTaskDetails = "";
		searchByAssigneeId = this.loginEmpData.getEmpId();
		searchByAssigneeName = this.loginEmpData.getFullName();
		selectedProcessGroupId = 0;
		runningFlag = true;
		viewDelegation = true;

		isDESC = false;

		searchProcesses(null);
		admin = false;
		/*
		 * try { if (getRequest().getParameter("mode") != null && getRequest().getParameter("mode").equals("1")) { if (SecurityService.isEmployeeMenuActionGranted(this.loginEmpData.getEmpId(), MenuCodesEnum.WF_INBOX.getCode(), MenuActionsEnum.WF_INBOX_ADMIN.getCode())) admin = true; } else { admin = false; } } catch (Exception e) { admin = false; this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams())); }
		 */

		/*
		 * if (getRequest().getParameter("init") == null) this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		 */
	}

	public void searchProcesses(AjaxBehaviorEvent event) {
		try {
			processes = BaseWorkFlow.getGroupProcesses(selectedProcessGroupId);
			selectedProcessId = 0;
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}

		searchTasks();
	}

	private void searchTasks() {
		try {
			tasks = BaseWorkFlow.searchWFTasksData(searchByAssigneeId, searchByTaskOwnerName, searchByTaskDetails, selectedProcessGroupId, selectedProcessId, runningFlag, selectedTaskRole, isDESC, fromDate, toDate);
		} catch (BusinessException e) {
			tasks = new ArrayList<WFTaskData>();
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage()));
		}
	}

	public void searchByTaskOwner() {
		searchTasks();
	}

	public void searchByAssigneeId() {
		searchTasks();
	}

	public void searchByProcess(AjaxBehaviorEvent event) {
		searchTasks();
	}

	public void sortASC() {
		isDESC = false;
		searchTasks();
	}

	public void sortDESC() {
		isDESC = true;
		searchTasks();
	}

	public void getRunningTasks() {
		runningFlag = true;
		viewDelegation = true;
		searchTasks();
	}

	public void getDoneCompletedTasks() {
		runningFlag = false;
		viewDelegation = false;
		searchTasks();
	}

	public void delegateTaskEmployee() {
		if (searchEmpId != null && searchEmpId != 0 && selectedGridIndex != null && !tasks.isEmpty()) {
			try {
				BaseWorkFlow.delegateTask(tasks.get(selectedGridIndex).getTaskId(), searchEmpId);
				searchTasks();
			} catch (BusinessException e) {
				this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			}
			searchEmpId = null;
			selectedGridIndex = null;
		}
	}
	
	public void searchByTaskDetails() {
		searchTasks();
	}

	// -----------------------------------------------------------------------------------------------------------------

	public void setTasks(List<WFTaskData> tasks) {
		this.tasks = tasks;
	}

	public List<WFTaskData> getTasks() {
		return tasks;
	}

	public void setSearchByTaskOwnerName(String searchByTaskOwnerName) {
		this.searchByTaskOwnerName = searchByTaskOwnerName;
	}

	public String getSearchByTaskOwnerName() {
		return searchByTaskOwnerName;
	}

	public void setSearchByAssigneeId(long searchByAssigneeId) {
		this.searchByAssigneeId = searchByAssigneeId;
	}

	public long getSearchByAssigneeId() {
		return searchByAssigneeId;
	}

	public void setSearchByAssigneeName(String searchByAssigneeName) {
		this.searchByAssigneeName = searchByAssigneeName;
	}

	public String getSearchByAssigneeName() {
		return searchByAssigneeName;
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

	public void setSearchEmpId(Long searchEmpId) {
		this.searchEmpId = searchEmpId;
	}

	public Long getSearchEmpId() {
		return searchEmpId;
	}

	public void setViewDelegation(boolean viewDelegation) {
		this.viewDelegation = viewDelegation;
	}

	public boolean isViewDelegation() {
		return viewDelegation;
	}

	public void setSelectedGridIndex(Integer selectedGridIndex) {
		this.selectedGridIndex = selectedGridIndex;
	}

	public Integer getSelectedGridIndex() {
		return selectedGridIndex;
	}

	public int getSelectedTaskRole() {
		return selectedTaskRole;
	}

	public void setSelectedTaskRole(int selectedTaskRole) {
		this.selectedTaskRole = selectedTaskRole;
	}

	public boolean isRunningFlag() {
		return runningFlag;
	}

	public void setRunningFlag(boolean runningFlag) {
		this.runningFlag = runningFlag;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public String getFileArchivingParam() {
		return fileArchivingParam;
	}

	public String getBoolServerUploadPath() {
		return InfoSysConfigurationService.getBoolServerUploadPath();
	}

	public String getSearchByTaskDetails() {
		return searchByTaskDetails;
	}

	public void setSearchByTaskDetails(String searchByTaskDetails) {
		this.searchByTaskDetails = searchByTaskDetails;
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
