package com.code.ui.backings.surveillance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.attach.Attachment;
import com.code.dal.orm.security.UserMenuActionData;
import com.code.dal.orm.setup.SetupClass;
import com.code.dal.orm.setup.SetupDomain;
import com.code.dal.orm.surveillance.SurveillanceActionData;
import com.code.dal.orm.surveillance.SurveillanceEmpNonEmpData;
import com.code.dal.orm.surveillance.SurveillanceReport;
import com.code.dal.orm.surveillance.SurveillanceReportDetailData;
import com.code.enums.ClassesEnum;
import com.code.enums.EntityNameEnum;
import com.code.enums.FinalApprovalEntitiesEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.ServiceCodesEnum;
import com.code.enums.SurveillanceReportRecommendationEnum;
import com.code.enums.UserMenuActionsEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.attach.AttachmentService;
import com.code.services.attach.AttachmentServiceCallBack;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.surveillance.SurveillanceOrdersService;
import com.code.services.security.SecurityService;
import com.code.services.setup.SetupService;
import com.code.services.workflow.surveillance.SurveillanceReportWorkFlow;
import com.code.ui.backings.base.WFBaseBacking;
import com.code.ui.util.EncryptionUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "employeeSurveillanceReport")
@ViewScoped
public class EmployeeSurveillanceReport extends WFBaseBacking implements Serializable {
	private int rowsCount = 10;

	private int pageMode; // 1: view, 2: edit report, 3: edit recommendation
	private boolean canExtend;
	private boolean showEvaluation;

	private SurveillanceReport surveillanceReport;
	private SurveillanceEmpNonEmpData surveillanceEmployeeData;
	private List<SurveillanceActionData> surveillanceActionDataList;
	private List<SurveillanceReportDetailData> surveillanceReportDetailDataList;
	private List<SurveillanceReport> previousSurveillanceReportList;
	private List<String> surveillanceOrderReasonsList;
	private List<String> surveillanceOrderSelectedReasonsList;
	private String regionDirector;
	private String generalDirector;

	private String fileArchivingParam;
	private String downloadFileParamId;
	private List<Attachment> attachmentList;
	private boolean openDownloadPopupFlag;
	private boolean openDownloadDialogueFlag;
	private String boolServerUploadPath;
	private String boolServerDownloadPath;
	private boolean surveillanceEndDateExpired;
	private boolean deletePrivilage = false;

	/**
	 * Constructor
	 */
	public EmployeeSurveillanceReport() {
		super.init();
		this.init();
		regionDirector = WFTaskRolesEnum.REGION_DIRECTOR.getCode();
		generalDirector = WFTaskRolesEnum.GENERAL_DIRECTOR.getCode();

		try {
			UserMenuActionData deleteAttachmentAction = SecurityService.getAction(loginEmpData.getEmpId(), UserMenuActionsEnum.DELETE_ATTACHMENT_PERMISSION.getCode(), FlagsEnum.ALL.getCode());
			if (deleteAttachmentAction != null) {
				setDeletePrivilage(true);
			}
			if (getRequest().getAttribute("mode") != null) { // get report by id
				surveillanceReport = SurveillanceOrdersService.getSurveillanceReport(Long.valueOf(getRequest().getAttribute("mode").toString()), FlagsEnum.ALL.getCode());
			} else { // get report by wfInstanceId
				surveillanceReport = SurveillanceOrdersService.getSurveillanceReport(FlagsEnum.ALL.getCode(), instance.getInstanceId());
			}
			List<SetupClass> setupClass = SetupService.getClasses(ClassesEnum.SURVEILLANCE_REASONS.getCode(), null, FlagsEnum.ALL.getCode());
			if (!setupClass.isEmpty()) {
				List<SetupDomain> reasonsDomainList = SetupService.getDomains(setupClass.get(0).getId(), null, FlagsEnum.ALL.getCode());
				for (SetupDomain domain : reasonsDomainList) {
					surveillanceOrderReasonsList.add(domain.getDescription());
				}
			}
			// load employees, actions, report details and previous reports
			surveillanceEmployeeData = SurveillanceOrdersService.getSurveillanceEmployeeData(surveillanceReport.getSurveillanceEmpId());
			surveillanceActionDataList = SurveillanceOrdersService.getEmployeeSurveillanceActionData(surveillanceReport.getSurveillanceEmpId(), surveillanceReport.getStartDate(), surveillanceReport.getEndDate());
			surveillanceReportDetailDataList = SurveillanceOrdersService.getSurveillanceReportDetailData(surveillanceReport.getId());
			previousSurveillanceReportList = SurveillanceOrdersService.getSurveillanceReports(surveillanceEmployeeData.getId(), surveillanceReport.getStartDate());
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}

		// check if the previous reports were approved
		boolean prevApproved = true;
		for (SurveillanceReport report : previousSurveillanceReportList) {
			if (report.getApproved() == FlagsEnum.OFF.getCode()) {
				prevApproved = false;
				break;
			}
		}

		// check if the report has been evaluated before
		showEvaluation = true;
		if (surveillanceReport.getTotal() == 0) {
			showEvaluation = false;
		}

		// calculate the total evaluation
		calculateTotal();

		if (currentTask == null) {
			if (surveillanceReport.getwFInstanceId() == null && prevApproved) { // open
																				// for
																				// editing
				pageMode = 2;
				showEvaluation = true;
			} else { // open for view only
				pageMode = 1;
			}
		} else if (role.equals(WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER.getCode()) || role.equals(WFTaskRolesEnum.GENERAL_INTELLIGENCE_MANAGER.getCode()) || role.equals(WFTaskRolesEnum.REGION_SECURITY_INTELLIGENCE_MANAGER.getCode()) || role.equals(regionDirector) || role.equals(generalDirector)) { // open for adding recommendation
			pageMode = 3;
			if (surveillanceReport.getRecommendationDecisionType() == null) {
				surveillanceReport.setRecommendationDecisionType(SurveillanceReportRecommendationEnum.CONTINUE.getCode());
			}
		} else { // view only
			pageMode = 1;
		}

		boolServerUploadPath = InfoSysConfigurationService.getBoolServerUploadPath();
		boolServerDownloadPath = InfoSysConfigurationService.getBoolServerDownloadPath();

		if (surveillanceReport.getSurveillanceReasons() != null && !surveillanceReport.getSurveillanceReasons().isEmpty()) {
			String[] reasons = surveillanceReport.getSurveillanceReasons().split(",");
			surveillanceOrderSelectedReasonsList = Arrays.asList(reasons);
		}
	}

	/**
	 * compute the overall evaluation percentage
	 */
	public void calculateTotal() {
		int total = 0;
		int n = 0;
		for (SurveillanceReportDetailData detail : surveillanceReportDetailDataList) {
			total += detail.getGrade();
			n += 10;
		}
		total *= 100.0 / n;
		surveillanceReport.setTotal(total);
	}

	/**
	 * Initialize bean variables
	 */
	public void init() {
		surveillanceOrderReasonsList = new ArrayList<String>();
		surveillanceOrderSelectedReasonsList = new ArrayList<String>();
	}

	/**
	 * Save report
	 * 
	 * @return page to be directed to
	 */
	public String saveReport() {
		try {
			SurveillanceOrdersService.updateSurveillanceReportWithDetails(loginEmpData, surveillanceReport, surveillanceReportDetailDataList, false);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.SURVEILLANCE_ORDER_SEARCH.toString();
	}

	/**
	 * Save and send report
	 * 
	 * @return page to be directed to
	 */
	public String saveAndSendReport() {
		try {
			for (SurveillanceReport surveillance : previousSurveillanceReportList) {
				if (surveillance.getRecommendationDecisionType() != null && surveillance.getRecommendationDecisionType() == SurveillanceReportRecommendationEnum.END.getCode()) {
					this.setServerSideErrorMessages(getParameterizedMessage("error_surveillanceReportDecisionIsEnd"));
					return null;
				}
			}
			SurveillanceOrdersService.updateSurveillanceReportWithDetails(loginEmpData, surveillanceReport, surveillanceReportDetailDataList, true);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Save and send report recommendation
	 * 
	 * @return page to be directed to
	 */
	public String saveAndSendReportRecommendation() {
		try {
			saveRecommendation();
			if (surveillanceOrderSelectedReasonsList.isEmpty() && (surveillanceReport.getRecommendationDecisionType() == 0 || surveillanceReport.getRecommendationDecisionType() == 1)) {
				this.setServerSideErrorMessages(getParameterizedMessage("label_continueSurvelliancePeriodIsMandatory"));
				return null;
			}
			surveillanceReport.setSurveillanceOrderReasonsList(surveillanceOrderSelectedReasonsList);
			SurveillanceReportWorkFlow.doSurveillanceOrderReportDM(currentTask, surveillanceReport, attachments, loginEmpData);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * approveReportSecurityManager
	 * 
	 * @return
	 */
	public String approveReportSecurityManager() {
		try {
			saveRecommendation();
			if (surveillanceOrderSelectedReasonsList.isEmpty() && (surveillanceReport.getRecommendationDecisionType() == 0 || surveillanceReport.getRecommendationDecisionType() == 1)) {
				this.setServerSideErrorMessages(getParameterizedMessage("label_continueSurvelliancePeriodIsMandatory"));
				return null;
			}
			surveillanceReport.setSurveillanceOrderReasonsList(surveillanceOrderSelectedReasonsList);
			SurveillanceReportWorkFlow.doSurveillanceOrderReportSecurityManager(currentTask, surveillanceReport, attachments, loginEmpData);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Approve to report recommendation
	 * 
	 * @return page to be directed to
	 */
	public String approveReportRecommendation() {
		try {
			saveRecommendation();
			if (surveillanceOrderSelectedReasonsList.isEmpty() && (surveillanceReport.getRecommendationDecisionType() == 0 || surveillanceReport.getRecommendationDecisionType() == 1)) {
				this.setServerSideErrorMessages(getParameterizedMessage("label_continueSurvelliancePeriodIsMandatory"));
				return null;
			}
			surveillanceReport.setSurveillanceOrderReasonsList(surveillanceOrderSelectedReasonsList);
			if (role.equals(regionDirector) && surveillanceEmployeeData.getFinalApprovalEntity().equals(FinalApprovalEntitiesEnum.GENERAL_INTELLIGENCE_DIRECTORATE.getCode())) {
				SurveillanceReportWorkFlow.doSurveillanceOrderReportRegionDM(currentTask, surveillanceReport, null, loginEmpData);
			} else {
				SurveillanceReportWorkFlow.doSurveillanceOrderReportSM(currentTask, surveillanceReport, loginEmpData, attachments);
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Do notify
	 * 
	 * @return page to be directed to
	 */
	public String doNotify() {
		try {
			SurveillanceReportWorkFlow.doNotify(currentTask);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	public void saveRecommendation() {
		String recommendation = "";
		switch (surveillanceReport.getRecommendationDecisionType()) {
		case 0:
			recommendation = getParameterizedMessage("label_continueSurveillance");
			break;
		case 1:
			recommendation = getParameterizedMessage("label_extendSurv") + " " + getParameterizedMessage("label_for") + " " + surveillanceReport.getRecommendationPeriod() + " " + getParameterizedMessage("label_month");
			break;
		case 2:
			recommendation = getParameterizedMessage("label_endSurveillance");
			break;
		}
		String notes = getParameterizedMessage("label_recommendation") + ": " + recommendation + "\n";
		notes += getParameterizedMessage("label_reasonsRemarks") + ": " + surveillanceReport.getRecommendationRemarks();
		currentTask.setNotes(notes);
	}

	public void getUploadParam(long surveillanceReportId) {
		try {
			String requestURL = getRequest().getRequestURL() + "";
			String url = requestURL.replace(getRequest().getServletPath(), "");
			String serviceURL = url + AttachmentServiceCallBack.SERVICE_URL;
			fileArchivingParam = EncryptionUtil.encryptSymmetrically("code=" + ServiceCodesEnum.INFO_SYS.getCode() + "&token=" + EntityNameEnum.SURVEILLANCE_REPORT.getCode() + "_" + surveillanceReportId + "&serviceurl=" + serviceURL);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	public void getDownloadParam(String attachmentId) {
		try {
			downloadFileParamId = EncryptionUtil.encryptSymmetrically("contentId=" + attachmentId);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
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
		}
	}

	public void getDownloadedAttachmentsIdList(long surveillanceReportId) throws BusinessException {
		try {
			attachmentList = AttachmentService.getAttachments(EntityNameEnum.SURVEILLANCE_REPORT.getCode(), surveillanceReportId);
			openDownloadPopupFlag = false;
			openDownloadDialogueFlag = false;
			if (attachmentList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noAttachments"));
			} else if (attachmentList.size() == 1) {
				openDownloadPopupFlag = true;
				downloadFileParamId = EncryptionUtil.encryptSymmetrically("contentId=" + attachmentList.get(0).getAttachmentId());
			} else {
				openDownloadDialogueFlag = true;
			}
		} catch (Exception e) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	/**
	 * Print report
	 */
	public void print() {
		try {
			byte[] bytes = SurveillanceOrdersService.getSurveillanceReportBytes(surveillanceReport.getId(), loginEmpData.getFullName());
			super.print(bytes, "Surveillance_Report_Details");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	// setters and getters
	public int getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}

	public int getPageMode() {
		return pageMode;
	}

	public void setPageMode(int pageMode) {
		this.pageMode = pageMode;
	}

	public boolean isCanExtend() {
		return canExtend;
	}

	public void setCanExtend(boolean canExtend) {
		this.canExtend = canExtend;
	}

	public boolean isShowEvaluation() {
		return showEvaluation;
	}

	public void setShowEvaluation(boolean showEvaluation) {
		this.showEvaluation = showEvaluation;
	}

	public SurveillanceReport getSurveillanceReport() {
		return surveillanceReport;
	}

	public void setSurveillanceReport(SurveillanceReport surveillanceReport) {
		this.surveillanceReport = surveillanceReport;
	}

	public SurveillanceEmpNonEmpData getSurveillanceEmployeeData() {
		return surveillanceEmployeeData;
	}

	public void setSurveillanceEmployeeData(SurveillanceEmpNonEmpData surveillanceEmployeeData) {
		this.surveillanceEmployeeData = surveillanceEmployeeData;
	}

	public List<SurveillanceActionData> getSurveillanceActionDataList() {
		return surveillanceActionDataList;
	}

	public void setSurveillanceActionDataList(List<SurveillanceActionData> surveillanceActionDataList) {
		this.surveillanceActionDataList = surveillanceActionDataList;
	}

	public List<SurveillanceReportDetailData> getSurveillanceReportDetailDataList() {
		return surveillanceReportDetailDataList;
	}

	public void setSurveillanceReportDetailDataList(List<SurveillanceReportDetailData> surveillanceReportDetailDataList) {
		this.surveillanceReportDetailDataList = surveillanceReportDetailDataList;
	}

	public List<SurveillanceReport> getPreviousSurveillanceReportList() {
		return previousSurveillanceReportList;
	}

	public void setPreviousSurveillanceReportList(List<SurveillanceReport> previousSurveillanceReportList) {
		this.previousSurveillanceReportList = previousSurveillanceReportList;
	}

	public String getRegionDirector() {
		return regionDirector;
	}

	public void setRegionDirector(String regionDirector) {
		this.regionDirector = regionDirector;
	}

	public String getGeneralDirector() {
		return generalDirector;
	}

	public void setGeneralDirector(String generalDirector) {
		this.generalDirector = generalDirector;
	}

	public String getRegionSecurityManager() {
		return WFTaskRolesEnum.REGION_SECURITY_INTELLIGENCE_MANAGER.getCode();
	}

	public String getFileArchivingParam() {
		return fileArchivingParam;
	}

	public void setFileArchivingParam(String fileArchivingParam) {
		this.fileArchivingParam = fileArchivingParam;
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

	public String getBoolServerUploadPath() {
		return boolServerUploadPath;
	}

	public void setBoolServerUploadPath(String boolServerUploadPath) {
		this.boolServerUploadPath = boolServerUploadPath;
	}

	public String getBoolServerDownloadPath() {
		return boolServerDownloadPath;
	}

	public void setBoolServerDownloadPath(String boolServerDownloadPath) {
		this.boolServerDownloadPath = boolServerDownloadPath;
	}

	public boolean isSurveillanceEndDateExpired() {
		return surveillanceEndDateExpired;
	}

	public void setSurveillanceEndDateExpired(boolean surveillanceEndDateExpired) {
		this.surveillanceEndDateExpired = surveillanceEndDateExpired;
	}

	public boolean isDeletePrivilage() {
		return deletePrivilage;
	}

	public void setDeletePrivilage(boolean deletePrivilage) {
		this.deletePrivilage = deletePrivilage;
	}

	public List<String> getSurveillanceOrderReasonsList() {
		return surveillanceOrderReasonsList;
	}

	public void setSurveillanceOrderReasonsList(List<String> surveillanceOrderReasonsList) {
		this.surveillanceOrderReasonsList = surveillanceOrderReasonsList;
	}

	public List<String> getSurveillanceOrderSelectedReasonsList() {
		return surveillanceOrderSelectedReasonsList;
	}

	public void setSurveillanceOrderSelectedReasonsList(List<String> surveillanceOrderSelectedReasonsList) {
		this.surveillanceOrderSelectedReasonsList = surveillanceOrderSelectedReasonsList;
	}

}