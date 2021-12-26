package com.code.ui.backings.worklist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import com.code.dal.orm.workflow.WFProcess;
import com.code.dal.orm.workflow.WFProcessGroup;
import com.code.enums.NavigationEnum;
import com.code.enums.WFPositionsEnum;
import com.code.enums.WFProcessesEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.workflow.BaseWorkFlow;
import com.code.services.workflow.WFPositionService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "wfCollectiveInbox")
@ViewScoped
public class WFCollectiveInbox extends BaseBacking implements Serializable {
	private List<SelectItem> collectiveProcesses;
	private Long searchByAssigneeId;
	private List<Long> labCheckWFTaskIds;
	private List<WFProcessGroup> processesGroups;
	private Long selectedProcessGroupId;
	private Integer selectedGridIndex;
	private List<WFProcess> processes;
	private Long selectedProcessId;
	private boolean isGeneralDirectorFlag = false;
	private int pageSize = 10;

	/**
	 * Default Constructor Initializer
	 */
	public WFCollectiveInbox() {
		super();
		this.setScreenTitle(getParameterizedMessage("title_wfCollectiveInbox"));
		try {
			System.out.println("WFCollectiveInbox");
			collectiveProcesses = new ArrayList<SelectItem>();
			searchByAssigneeId = this.loginEmpData.getEmpId();
			processesGroups = BaseWorkFlow.getProcessesGroups();
			selectedProcessId = -1L;
			selectedProcessGroupId = 0L;
			searchProcesses();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	public void init() {
		super.init();
	}

	/**
	 * Search processes by process group
	 */
	public void searchProcesses() {
		try {
			processes = BaseWorkFlow.getGroupProcesses(selectedProcessGroupId);
			//TODO : Uncomment this line when generalize this page 
			// selectedProcessId = -1L;
			selectedProcessId = WFProcessesEnum.LAB_CHECK.getCode();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}

		searchCollectiveProcesses(null);
	}

	/**
	 * Search the group of running tasks for each process
	 * 
	 * @param event
	 */
	public void searchCollectiveProcesses(AjaxBehaviorEvent event) {
		try {
			if (selectedProcessId == -1 || selectedProcessId == WFProcessesEnum.LAB_CHECK.getCode()) {
				Long regionId;
				Long originalId;
				regionId = DepartmentService.isRegionDepartment(this.loginEmpData.getActualDepartmentId());

				if (regionId != null) {
					originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionProtectionDepartmentId(regionId));
					if (searchByAssigneeId.equals(originalId)) {
						labCheckWFTaskIds = BaseWorkFlow.getWFRunningTasks(WFProcessesEnum.LAB_CHECK.getCode(), searchByAssigneeId, WFTaskRolesEnum.REGION_DIRECTOR.getCode());
						System.out.println(labCheckWFTaskIds.size());
						if (labCheckWFTaskIds.size() > 0)
							collectiveProcesses.add(new SelectItem(labCheckWFTaskIds.size(), getParameterizedMessage("label_labCheckWFTasksCollectiveApproval"), WFProcessesEnum.LAB_CHECK.toString()));
					}
				} else {
					originalId = DepartmentService.getDepartmentManager(WFPositionService.getWFPosition(WFPositionsEnum.GENERAL_PROTECTION_DIRECTORATE.getCode()).getUnitId());
					if (searchByAssigneeId.equals(originalId)) {
						labCheckWFTaskIds = BaseWorkFlow.getWFRunningTasks(WFProcessesEnum.LAB_CHECK.getCode(), searchByAssigneeId, WFTaskRolesEnum.GENERAL_DIRECTOR.getCode());
						System.out.println(labCheckWFTaskIds.size());
						if (labCheckWFTaskIds.size() > 0)
							collectiveProcesses.add(new SelectItem(labCheckWFTaskIds.size(), getParameterizedMessage("label_labCheckWFTasksCollectiveApproval"), WFProcessesEnum.LAB_CHECK.toString()));
						isGeneralDirectorFlag = true;
					}
				}
			}
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(WFCollectiveInbox.class, e, "WFCollectiveInbox");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Redirect to approval page
	 * 
	 * @param index
	 * @return
	 */
	public String redirectToApprovalPage(int index) {
		if (collectiveProcesses.get(index).getDescription().equals(WFProcessesEnum.LAB_CHECK.toString())) {
			String taksIdsList = "";
			for (int i = 0; i < labCheckWFTaskIds.size(); i++) {
				if (i == labCheckWFTaskIds.size() - 1)
					taksIdsList += String.valueOf(labCheckWFTaskIds.get(i));
				else
					taksIdsList += String.valueOf(labCheckWFTaskIds.get(i)) + ",";
			}
			getRequest().setAttribute("isGeneralDirectorFlag", isGeneralDirectorFlag);
			getRequest().setAttribute("labCheckWFTaskIds", taksIdsList);
			return NavigationEnum.LAB_CHECK_COLLECTIVE_APPROVAL.toString();
		}
		return "";
	}

	public List<SelectItem> getCollectiveProcesses() {
		return collectiveProcesses;
	}

	public void setCollectiveProcesses(List<SelectItem> collectiveProcesses) {
		this.collectiveProcesses = collectiveProcesses;
	}

	public Long getSearchByAssigneeId() {
		return searchByAssigneeId;
	}

	public void setSearchByAssigneeId(Long searchByAssigneeId) {
		this.searchByAssigneeId = searchByAssigneeId;
	}

	public List<WFProcessGroup> getProcessesGroups() {
		return processesGroups;
	}

	public void setProcessesGroups(List<WFProcessGroup> processesGroups) {
		this.processesGroups = processesGroups;
	}

	public Long getSelectedProcessGroupId() {
		return selectedProcessGroupId;
	}

	public void setSelectedProcessGroupId(Long selectedProcessGroupId) {
		this.selectedProcessGroupId = selectedProcessGroupId;
	}

	public Integer getSelectedGridIndex() {
		return selectedGridIndex;
	}

	public void setSelectedGridIndex(Integer selectedGridIndex) {
		this.selectedGridIndex = selectedGridIndex;
	}

	public List<WFProcess> getProcesses() {
		return processes;
	}

	public void setProcesses(List<WFProcess> processes) {
		this.processes = processes;
	}

	public long getSelectedProcessId() {
		return selectedProcessId;
	}

	public void setSelectedProcessId(long selectedProcessId) {
		this.selectedProcessId = selectedProcessId;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
