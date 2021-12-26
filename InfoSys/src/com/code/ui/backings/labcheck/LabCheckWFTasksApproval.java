package com.code.ui.backings.labcheck;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.labcheck.LabCheck;
import com.code.enums.LabCheckReasonsEnum;
import com.code.enums.NavigationEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.labcheck.LabCheckService;
import com.code.services.log4j.Log4j;
import com.code.services.workflow.labcheck.LabCheckWorkFlow;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "labCheckWFTasksApproval")
@ViewScoped
public class LabCheckWFTasksApproval extends BaseBacking implements Serializable {
	private List<LabCheck> labCheckList;
	private List<LabCheck> selectedLabCheckList;
	private Long[] labCheckWFTaskIds;
	private boolean isGeneralDirectorFlag;

	/**
	 * Default Constructor Initializer
	 */
	public LabCheckWFTasksApproval() {
		super();
		init();
		try {
			labCheckList = new ArrayList<LabCheck>();
			selectedLabCheckList = new ArrayList<LabCheck>();

			if (getRequest().getAttribute("labCheckWFTaskIds") != null && !getRequest().getAttribute("labCheckWFTaskIds").equals("null") && !getRequest().getAttribute("labCheckWFTaskIds").toString().isEmpty() && !getRequest().getAttribute("labCheckWFTaskIds").equals("undefined")) {
				String[] taskIds = getRequest().getAttribute("labCheckWFTaskIds").toString().split(",");
				labCheckWFTaskIds = new Long[taskIds.length];
				for (int i = 0; i < taskIds.length; i++) {
					labCheckWFTaskIds[i] = Long.parseLong(taskIds[i]);
				}
			}
			if (getRequest().getAttribute("isGeneralDirectorFlag") != null && !getRequest().getAttribute("isGeneralDirectorFlag").equals("null") && !getRequest().getAttribute("isGeneralDirectorFlag").toString().isEmpty() && !getRequest().getAttribute("isGeneralDirectorFlag").equals("undefined")) {
				isGeneralDirectorFlag = (boolean) getRequest().getAttribute("isGeneralDirectorFlag");
			}
			labCheckList = LabCheckService.getLabCheckByWFTaskIds(labCheckWFTaskIds);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckWFTasksApproval.class, e, "LabCheckWFTasksApproval");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * view Lab check Details
	 * 
	 * @param labCheck
	 * @return
	 */
	public String viewLabCheckDetails(LabCheck labCheck) {
		getRequest().setAttribute("mode", labCheck.getId());
		return NavigationEnum.LAB_CHECK_DETAILS.toString();
	}

	/**
	 * Print the report of all lab checks which need manager approval
	 */
	public void printAll() {
		try {
			ArrayList<Long> labCheckIds = new ArrayList<Long>();
			for (LabCheck labCheck : labCheckList) {
				labCheckIds.add(labCheck.getId());
			}
			byte[] bytes = LabCheckService.getLabCheckWFTasksApprovalReportBytes(labCheckIds, isGeneralDirectorFlag, loginEmpData.getFullName());
			super.print(bytes, "Lab_Check_Tasks_Approval");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckWFTasksApproval.class, e, "LabCheckWFTasksApproval");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Print the report of specific lab check
	 * 
	 * @param labCheck
	 */
	public void print(LabCheck labCheck) {
		try {
			byte[] bytes = LabCheckService.getLabCheckDetailsReportBytes(labCheck.getId(), loginEmpData.getFullName());
			super.print(bytes, "Lab_Check_Details");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckWFTasksApproval.class, e, "LabCheckWFTasksApproval");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Approve the selected lab check tasks
	 */
	public String doApprove() {
		try {
			if (selectedLabCheckList.size() > 0)
				LabCheckWorkFlow.doCollectiveApproveSM(selectedLabCheckList, labCheckWFTaskIds, loginEmpData);
			else
				throw new BusinessException("error_tasksNotSelected");
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
			return NavigationEnum.INBOX.toString();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckWFTasksApproval.class, e, "LabCheckWFTasksApproval");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
		return "";
	}

	/********************************************************** Setters And Getters **********************************************/
	public List<LabCheck> getLabCheckList() {
		return labCheckList;
	}

	public void setLabCheckList(List<LabCheck> labCheckList) {
		this.labCheckList = labCheckList;
	}

	public List<LabCheck> getSelectedLabCheckList() {
		return selectedLabCheckList;
	}

	public void setSelectedLabCheckList(List<LabCheck> selectedLabCheckList) {
		this.selectedLabCheckList = selectedLabCheckList;
	}

	public List<LabCheckReasonsEnum> getLabCheckReasonsEnumList() {
		return LabCheckReasonsEnum.getAllCheckReasonsEnumValues();
	}
}