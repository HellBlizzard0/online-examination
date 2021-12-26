package com.code.ui.backings.surveillance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.attach.Attachment;
import com.code.dal.orm.security.UserMenuActionData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.dal.orm.surveillance.SurveillanceActionData;
import com.code.dal.orm.surveillance.SurveillanceEmpNonEmpData;
import com.code.dal.orm.surveillance.SurveillanceReport;
import com.code.enums.ClassesEnum;
import com.code.enums.EntityNameEnum;
import com.code.enums.FinalApprovalEntitiesEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.SurveillanceReportRecommendationEnum;
import com.code.enums.UserMenuActionsEnum;
import com.code.enums.WFInstanceStatusEnum;
import com.code.exceptions.BusinessException;
import com.code.services.attach.AttachmentService;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.surveillance.SurveillanceOrdersService;
import com.code.services.log4j.Log4j;
import com.code.services.security.SecurityService;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.SetupService;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;
import com.code.ui.util.EncryptionUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "surveillanceOrdersSearch")
@ViewScoped
public class SurveillanceOrdersSearch extends BaseBacking implements Serializable {
	private int rowsCount = 10;
	private boolean actionCollapsFlag;
	private boolean reportCollapsFlag;
	private boolean isLateReports;
	private int surveillanceStatus;
	private Date startDate;
	private Date endDate;
	private Boolean employeeFlag;

	private SurveillanceEmpNonEmpData searchSurvEmployee;
	private SurveillanceEmpNonEmpData selectedEmployee;
	private SurveillanceActionData surveillanceAction;

	private List<SurveillanceEmpNonEmpData> surveillanceEmployeeList;
	private List<SurveillanceActionData> surveillanceActionList;
	private List<SurveillanceReport> surveillanceReportList;
	private List<SetupDomain> evalPointList, functionalEvalPointList, behavioralEvalPointList;
	private List<FinalApprovalEntitiesEnum> finalApprovalList;

	private String downloadFileParamId;
	private List<Attachment> attachmentList;
	private boolean openDownloadPopupFlag;
	private boolean openDownloadDialogueFlag;
	private String boolServerDownloadPath;
	private boolean deletePrivilage = false;

	/**
	 * Constructor
	 */
	public SurveillanceOrdersSearch() {
		super();
		init();

		surveillanceEmployeeList = new ArrayList<SurveillanceEmpNonEmpData>();
		boolServerDownloadPath = InfoSysConfigurationService.getBoolServerDownloadPath();

		try {
			UserMenuActionData deleteAttachmentAction = SecurityService.getAction(loginEmpData.getEmpId(), UserMenuActionsEnum.DELETE_ATTACHMENT_PERMISSION.getCode(), FlagsEnum.ALL.getCode());
			if (deleteAttachmentAction != null) {
				setDeletePrivilage(true);
			}
			// load order issue site list and evaluation points list
			finalApprovalList = Arrays.asList(FinalApprovalEntitiesEnum.values());
			behavioralEvalPointList = SetupService.getDomains(ClassesEnum.SURVEILLANCE_BEHAVIOURAL_EVALUATION_POINT.getCode());
			functionalEvalPointList = SetupService.getDomains(ClassesEnum.SURVEILLANCE_FUNCTIONAL_EVALUATION_POINT.getCode());
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			   Log4j.traceErrorException(SurveillanceOrdersSearch.class, e, "SurveillanceOrdersSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }

		actionCollapsFlag = false;
		reportCollapsFlag = false;
		selectedEmployee = null;
		surveillanceAction = new SurveillanceActionData();
		try {
			surveillanceAction.setEventDate(HijriDateService.getHijriSysDate());
			surveillanceAction.setClassCode(ClassesEnum.SURVEILLANCE_BEHAVIOURAL_EVALUATION_POINT.getCode());
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SurveillanceOrdersSearch.class, e, "SurveillanceOrdersSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Initialize/Reset search variables
	 */
	public void init() {
		surveillanceStatus = FlagsEnum.ALL.getCode();
		searchSurvEmployee = new SurveillanceEmpNonEmpData();
		try {
			Long regionId = DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());
			if (regionId != null) {
				searchSurvEmployee.setRegionId(regionId);
				searchSurvEmployee.setRegionName(DepartmentService.getDepartment(regionId).getArabicName());
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SurveillanceOrdersSearch.class, e, "SurveillanceOrdersSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * On Selection Change to all reset fields
	 */
	public void onSelectionChange() {
		searchSurvEmployee.setSocialId(null);
		searchSurvEmployee.setEmployeeFullName(null);
	}

	/**
	 * Search surveillance employee
	 */
	public void searchSurveillanceEmployee() {
		try {
			surveillanceEmployeeList = SurveillanceOrdersService.getSurveillanceEmployeeDataByRegionId(searchSurvEmployee.getOrderNumber(), searchSurvEmployee.getOrderDate(), searchSurvEmployee.getFinalApprovalEntity(), FlagsEnum.ALL.getCode(), surveillanceStatus, isLateReports, searchSurvEmployee.getRegionId(), searchSurvEmployee.getwFInstanceStatus() == null ? FlagsEnum.ALL.getCode() : searchSurvEmployee.getwFInstanceStatus(), searchSurvEmployee.getSocialId());
			selectedEmployee = null;
			actionCollapsFlag = false;
			reportCollapsFlag = false;
			if (surveillanceEmployeeList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SurveillanceOrdersSearch.class, e, "SurveillanceOrdersSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Open EmployeeSurveillanceReport page
	 * 
	 * @param surveillanceReportId
	 * @return page to be directed to
	 */
	public String viewReportDetail(long surveillanceReportId) {
		getRequest().setAttribute("mode", surveillanceReportId);
		return NavigationEnum.SURVEILLANCE_REPORT.toString();
	}

	/**
	 * Edit Surveillance Detail
	 * 
	 * @param surveillanceId
	 * @return
	 */
	public String editSurveillanceDetail(long surveillanceId) {
		getRequest().setAttribute("mode", surveillanceId);
		return NavigationEnum.SURVEILLANCE_ORDER_REGISTERATION.toString();
	}

	/**
	 * Save action
	 */
	public void saveAction() {
		try {
			if (surveillanceAction.getId() == null) {
				SurveillanceOrdersService.addSurveillanceAction(loginEmpData, surveillanceAction);
			} else {
				SurveillanceOrdersService.updateSurveillanceAction(loginEmpData, surveillanceAction);
			}
			surveillanceAction = new SurveillanceActionData();
			surveillanceAction.setSurveillanceEmpId(selectedEmployee.getId());
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SurveillanceOrdersSearch.class, e, "SurveillanceOrdersSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
		viewSurveillanceActionsAndReports(selectedEmployee);
	}

	/**
	 * Cancel Action
	 */
	public void cancelAction() {
		surveillanceAction = new SurveillanceActionData();
		surveillanceAction.setSurveillanceEmpId(selectedEmployee.getId());
		actionCollapsFlag = true;
	}

	/**
	 * View surveillance actions and reports for employee
	 * 
	 * @param employee
	 */
	public void viewSurveillanceActionsAndReports(SurveillanceEmpNonEmpData employee) {
		try {
			evalPointList = behavioralEvalPointList;
			selectedEmployee = employee;
			surveillanceAction.setSurveillanceEmpId(employee.getId());
			surveillanceActionList = SurveillanceOrdersService.getEmployeeSurveillanceActionData(employee.getId(), null, null);
			surveillanceReportList = SurveillanceOrdersService.getEmployeeSurveillanceReports(employee.getId(), HijriDateService.addSubHijriDays(HijriDateService.getHijriSysDate(), Integer.valueOf(InfoSysConfigurationService.getSurveillanceNotifyLateReportAhead())));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SurveillanceOrdersSearch.class, e, "SurveillanceOrdersSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Change the evaluation point(domain) list depending on the class
	 */
	public void changeSurveillanceActionEvalPoint() {
		if (surveillanceAction.getClassCode().equals(ClassesEnum.SURVEILLANCE_BEHAVIOURAL_EVALUATION_POINT.toString())) {
			evalPointList = behavioralEvalPointList;
		} else if (surveillanceAction.getClassCode().equals(ClassesEnum.SURVEILLANCE_FUNCTIONAL_EVALUATION_POINT.toString())) {
			evalPointList = functionalEvalPointList;
		}
	}

	/**
	 * Edit surveillance action
	 * 
	 * @param action
	 */
	public void editSurveillanceAction(SurveillanceActionData action) {
		actionCollapsFlag = true;
		surveillanceAction = action;
		changeSurveillanceActionEvalPoint();
	}

	/**
	 * Delete surveillance action
	 * 
	 * @param action
	 */
	public void deleteSurveillanceAction(SurveillanceActionData action) {
		try {
			SurveillanceOrdersService.deleteSurveillanceAction(loginEmpData, action);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
			surveillanceActionList.remove(action);
			surveillanceAction = new SurveillanceActionData();
			surveillanceAction.setSurveillanceEmpId(selectedEmployee.getId());
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SurveillanceOrdersSearch.class, e, "SurveillanceOrdersSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Add Surveillance Report
	 */
	public void addSurveillanceReport() {
		if (startDate == null || endDate == null) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_mandatory"));
			return;
		}
		if (endDate.before(startDate)) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_reportStartDateAfterEndDate"));
			return;
		}

		if (startDate.before(selectedEmployee.getStartDate()) || endDate.after(selectedEmployee.getEndDate())) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_invalidReportPeriod"));
			return;
		}
		for (SurveillanceReport surveillance : surveillanceReportList) {
			if (surveillance.getRecommendationDecisionType() != null && surveillance.getRecommendationDecisionType() == SurveillanceReportRecommendationEnum.END.getCode()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_surveillanceReportDecisionIsEnd"));
				return;
			}
		}
		SurveillanceReport surveillanceReport = new SurveillanceReport();
		surveillanceReport.setApproved(FlagsEnum.OFF.getCode());
		surveillanceReport.setTotal(0);
		surveillanceReport.setSurveillanceEmpId(selectedEmployee.getId());
		surveillanceReport.setStartDate(startDate);
		surveillanceReport.setEndDate(endDate);
		try {
			SurveillanceOrdersService.addSurveillanceReport(loginEmpData, surveillanceReport);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
			surveillanceReportList.add(surveillanceReport);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SurveillanceOrdersSearch.class, e, "SurveillanceOrdersSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void cancelReport() {
		startDate = null;
		endDate = null;
		reportCollapsFlag = true;
	}

	/**
	 * Constitute Download parameter which contains contentId (attachmentId)
	 * 
	 * @param attachmentId
	 */
	public void getDownloadParam(String attachmentId) {
		try {
			downloadFileParamId = EncryptionUtil.encryptSymmetrically("contentId=" + attachmentId);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SurveillanceOrdersSearch.class, e, "SurveillanceOrdersSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Delete Attachment
	 * 
	 * @param attachment
	 */
	public void deleteAttachment(Attachment attachment) {
		try {
			AttachmentService.deleteFileNetAttachment(attachment);
			attachmentList.remove(attachment);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SurveillanceOrdersSearch.class, e, "SurveillanceOrdersSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Get attachment list related to the entity id passed to the method then check if there's no records then show message indicating no daya else if it's only one record , open the download popup directly , otherwise open a dialogue shows file names and make the user select which one to download then open the download popup with the choosen contentId (attachmentId)
	 * 
	 * @param surveillanceOrderId
	 * @throws BusinessException
	 */
	public void getDownloadedAttachmentsIdList(long surveillanceOrderId) throws BusinessException {
		try {
			attachmentList = AttachmentService.getAttachments(EntityNameEnum.SURVEILLANCE_ORDER.getCode(), surveillanceOrderId);
			openDownloadPopupFlag = false;
			openDownloadDialogueFlag = false;
			if (attachmentList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noAttachments"));
			} else {
				openDownloadDialogueFlag = true;
			}
		} catch (Exception e) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	// getters and setters
	public int getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}

	public boolean isActionCollapsFlag() {
		return actionCollapsFlag;
	}

	public void setActionCollapsFlag(boolean actionCollapsFlag) {
		this.actionCollapsFlag = actionCollapsFlag;
	}

	public SurveillanceEmpNonEmpData getSearchSurvEmployee() {
		return searchSurvEmployee;
	}

	public void setSearchSurvEmployee(SurveillanceEmpNonEmpData searchSurvEmployee) {
		this.searchSurvEmployee = searchSurvEmployee;
	}

	public SurveillanceEmpNonEmpData getSelectedEmployee() {
		return selectedEmployee;
	}

	public void setSelectedEmployee(SurveillanceEmpNonEmpData selectedEmployee) {
		this.selectedEmployee = selectedEmployee;
	}

	public SurveillanceActionData getSurveillanceAction() {
		return surveillanceAction;
	}

	public void setSurveillanceAction(SurveillanceActionData surveillanceAction) {
		this.surveillanceAction = surveillanceAction;
	}

	public int getSurveillanceStatus() {
		return surveillanceStatus;
	}

	public void setSurveillanceStatus(int surveillanceStatus) {
		this.surveillanceStatus = surveillanceStatus;
	}

	public boolean isLateReports() {
		return isLateReports;
	}

	public void setLateReports(boolean isLateReports) {
		this.isLateReports = isLateReports;
	}

	public List<SurveillanceEmpNonEmpData> getSurveillanceEmployeeList() {
		return surveillanceEmployeeList;
	}

	public void setSurveillanceEmployeeList(List<SurveillanceEmpNonEmpData> surveillanceEmployeeList) {
		this.surveillanceEmployeeList = surveillanceEmployeeList;
	}

	public List<SurveillanceActionData> getSurveillanceActionList() {
		return surveillanceActionList;
	}

	public void setSurveillanceActionList(List<SurveillanceActionData> surveillanceActionList) {
		this.surveillanceActionList = surveillanceActionList;
	}

	public List<SurveillanceReport> getSurveillanceReportList() {
		return surveillanceReportList;
	}

	public void setSurveillanceReportList(List<SurveillanceReport> surveillanceReportList) {
		this.surveillanceReportList = surveillanceReportList;
	}

	public List<SetupDomain> getEvalPointList() {
		return evalPointList;
	}

	public void setEvalPointList(List<SetupDomain> evalPointList) {
		this.evalPointList = evalPointList;
	}

	public Integer getWfInstanceRunningEnum() {
		return WFInstanceStatusEnum.RUNING.getCode();
	}

	public Integer getWfInstanceCompleteEnum() {
		return WFInstanceStatusEnum.COMPLETED.getCode();
	}

	public Integer getWfInstanceRejectedEnum() {
		return WFInstanceStatusEnum.REJECTED.getCode();
	}

	public boolean isReportCollapsFlag() {
		return reportCollapsFlag;
	}

	public void setReportCollapsFlag(boolean reportCollapsFlag) {
		this.reportCollapsFlag = reportCollapsFlag;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<FinalApprovalEntitiesEnum> getFinalApprovalList() {
		return finalApprovalList;
	}

	public void setFinalApprovalList(List<FinalApprovalEntitiesEnum> finalApprovalList) {
		this.finalApprovalList = finalApprovalList;
	}

	public String getDownloadFileParamId() {
		return downloadFileParamId;
	}

	public void setDownloadFileParamId(String downloadFileParamId) {
		this.downloadFileParamId = downloadFileParamId;
	}

	public List<Attachment> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<Attachment> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public boolean isOpenDownloadPopupFlag() {
		return openDownloadPopupFlag;
	}

	public void setOpenDownloadPopupFlag(boolean openDownloadPopupFlag) {
		this.openDownloadPopupFlag = openDownloadPopupFlag;
	}

	public boolean isOpenDownloadDialogueFlag() {
		return openDownloadDialogueFlag;
	}

	public void setOpenDownloadDialogueFlag(boolean openDownloadDialogueFlag) {
		this.openDownloadDialogueFlag = openDownloadDialogueFlag;
	}

	public String getBoolServerDownloadPath() {
		return boolServerDownloadPath;
	}

	public void setBoolServerDownloadPath(String boolServerDownloadPath) {
		this.boolServerDownloadPath = boolServerDownloadPath;
	}

	public Boolean getEmployeeFlag() {
		return employeeFlag;
	}

	public void setEmployeeFlag(Boolean employeeFlag) {
		this.employeeFlag = employeeFlag;
	}

	public Long getHeadQuarter() {
		return InfoSysConfigurationService.getHeadQuarter();
	}

	public boolean isDeletePrivilage() {
		return deletePrivilage;
	}

	public void setDeletePrivilage(boolean deletePrivilage) {
		this.deletePrivilage = deletePrivilage;
	}

}