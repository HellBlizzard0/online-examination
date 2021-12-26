package com.code.ui.backings.surveillance;

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletResponse;

import com.code.dal.orm.attach.Attachment;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.setup.NonEmployeeData;
import com.code.dal.orm.setup.SetupClass;
import com.code.dal.orm.setup.SetupDomain;
import com.code.dal.orm.surveillance.SurveillanceEmpNonEmpData;
import com.code.dal.orm.surveillance.SurveillanceOrderData;
import com.code.enums.ClassesEnum;
import com.code.enums.EntityNameEnum;
import com.code.enums.FinalApprovalEntitiesEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.ServiceCodesEnum;
import com.code.enums.WFPositionsEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.attach.AttachmentService;
import com.code.services.attach.AttachmentServiceCallBack;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.surveillance.SurveillanceOrdersService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.EmployeeService;
import com.code.services.setup.NonEmployeeService;
import com.code.services.setup.SetupService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.WFPositionService;
import com.code.services.workflow.surveillance.SurveillanceWorkFlow;
import com.code.ui.backings.base.WFBaseBacking;
import com.code.ui.util.EncryptionUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "surveillanceOrderRegisteration")
@ViewScoped
public class SurveillanceOrderRegisteration extends WFBaseBacking implements Serializable {
	private SurveillanceOrderData surveillanceOrderData;
	private List<String> surveillanceOrderReasonsList;
	private List<String> surveillanceOrderSelectedReasonsList;
	private List<SurveillanceEmpNonEmpData> surveillanceEmpNonEmpList;
	private List<SurveillanceEmpNonEmpData> surveillanceEmployeesDeletedList;
	private List<FinalApprovalEntitiesEnum> finalApprovalList;

	private String regionIntelligenceManagerRole;
	private String generalIntelligenceManagerRole;
	private String regionDirectorRole;
	private String generalDirectorRole;

	private Integer pageMode = 1; // 1 for normal mode, 2 for approval cycle or view mode

	private String fileArchivingParam;
	private String downloadFileParamId;
	private List<Attachment> attachmentList;
	private String selectedDownloadFileId;
	private boolean openDownloadPopupFlag;
	private boolean openDownloadDialogueFlag;
	private boolean verbalOrderFlag;
	private Long socialId;
	private NonEmployeeData selectedNonEmp;
	private EmployeeData selectedEmp;
	private boolean showMsg;

	private StringBuilder warningAbsenceBirthDate = new StringBuilder();
	private int dialogueMode;

	/**
	 * Constructor
	 */
	public SurveillanceOrderRegisteration() {
		super.init();
		this.init();
		try {
			Long surveillanceOrderId = null;
			// Set Reasons List
			List<SetupClass> setupClass = SetupService.getClasses(ClassesEnum.SURVEILLANCE_REASONS.getCode(), null, FlagsEnum.ALL.getCode());
			if (!setupClass.isEmpty()) {
				List<SetupDomain> reasonsDomainList = SetupService.getDomains(setupClass.get(0).getId(), null, FlagsEnum.ALL.getCode());
				for (SetupDomain domain : reasonsDomainList) {
					surveillanceOrderReasonsList.add(domain.getDescription());
				}
			}

			Long regionId = SurveillanceWorkFlow.getDirectorateOrRegionId(loginEmpData);
			if (!regionId.equals(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId())) {
				finalApprovalList = Arrays.asList(FinalApprovalEntitiesEnum.values());
			} else {
				finalApprovalList.add(FinalApprovalEntitiesEnum.GENERAL_INTELLIGENCE_DIRECTORATE);
			}

			if (getRequest().getAttribute("mode") != null) {
				surveillanceOrderId = (Long) getRequest().getAttribute("mode");
			}

			if (currentTask == null) {
				if (surveillanceOrderId == null) {
					pageMode = 1;
					surveillanceOrderData.setPeriodicReporting(6);
					surveillanceOrderData.setOrderDate(HijriDateService.getHijriSysDate());
					surveillanceOrderData.setStartDate(HijriDateService.getHijriSysDate());
					surveillanceOrderData.setRegionId(regionId);
				} else {
					surveillanceOrderData = SurveillanceOrdersService.getSurveillanceOrderData(surveillanceOrderId, null, FlagsEnum.ALL.getCode()).get(0);
					surveillanceEmpNonEmpList = SurveillanceOrdersService.getSurveillanceEmployeeDataByOrderId(surveillanceOrderId);
					verbalOrderFlag = surveillanceOrderData.getOrderNumber() != null ? false : true;
				}
			} else {
				pageMode = 2;
				surveillanceOrderData = SurveillanceOrdersService.getSurveillanceOrderData(FlagsEnum.ALL.getCode(), null, instance.getInstanceId()).get(0);
				surveillanceEmpNonEmpList = SurveillanceOrdersService.getSurveillanceEmployeeDataByOrderId(surveillanceOrderData.getId());
				if (role.equals(WFTaskRolesEnum.REQUESTER.getCode())) {
					pageMode = 1;
				}
				verbalOrderFlag = surveillanceOrderData.getOrderNumber() != null ? false : true;
			}
			// Set employees reasons list
			for (SurveillanceEmpNonEmpData emp : surveillanceEmpNonEmpList) {
				if (emp.getSurveillanceReasons() != null && !emp.getSurveillanceReasons().isEmpty()) {
					String[] reasons = emp.getSurveillanceReasons().split(",");
					emp.setSurveillanceOrderReasonsList(Arrays.asList(reasons));
				}
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SurveillanceOrderRegisteration.class, e, "SurveillanceOrderRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Initialize bean variables
	 */
	public void init() {
		surveillanceOrderData = new SurveillanceOrderData();
		finalApprovalList = new ArrayList<FinalApprovalEntitiesEnum>();
		surveillanceOrderReasonsList = new ArrayList<String>();
		surveillanceOrderSelectedReasonsList = new ArrayList<String>();
		surveillanceEmpNonEmpList = new ArrayList<SurveillanceEmpNonEmpData>();
		surveillanceEmployeesDeletedList = new ArrayList<SurveillanceEmpNonEmpData>();
		regionIntelligenceManagerRole = WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER.getCode();
		generalIntelligenceManagerRole = WFTaskRolesEnum.GENERAL_INTELLIGENCE_MANAGER.getCode();
		regionDirectorRole = WFTaskRolesEnum.REGION_DIRECTOR.getCode();
		generalDirectorRole = WFTaskRolesEnum.GENERAL_DIRECTOR.getCode();
		selectedNonEmp = new NonEmployeeData();
	}

	/**
	 * Add Employee using filled surveillance Data
	 */
	public void addEmpNonEmp() {
		try {
			SurveillanceEmpNonEmpData surveillanceEmpNonEmpData = new SurveillanceEmpNonEmpData();
			surveillanceEmpNonEmpData.setSelected(true);

			for (SurveillanceEmpNonEmpData empNonEmp : surveillanceEmpNonEmpList) {
				if (empNonEmp.getSocialId().equals(socialId.toString())) {
					throw new BusinessException("error_personAlreadyChoosen");
				}
			}

			// Copy Employee or NonEmployee Data To Surveillance Employee
			if (selectedEmp == null) {
				surveillanceEmpNonEmpData.setNonEmployeeId(selectedNonEmp.getId());
				surveillanceEmpNonEmpData.setEmployeeFullName(selectedNonEmp.getFullName());
				surveillanceEmpNonEmpData.setSocialId(selectedNonEmp.getIdentity().toString());
				if (selectedNonEmp.getIdentity().toString().startsWith("1") && selectedNonEmp.getBirthDateString() != null && !selectedNonEmp.getBirthDateString().trim().isEmpty()) {
					surveillanceEmpNonEmpData.setBirthDate(HijriDateService.getHijriDate(selectedNonEmp.getBirthDateString()));
				} else if (selectedNonEmp.getIdentity().toString().startsWith("2") && selectedNonEmp.getBirthDateGregString() != null && !selectedNonEmp.getBirthDateGregString().trim().isEmpty()) {
					surveillanceEmpNonEmpData.setBirthDateGreg(new SimpleDateFormat("dd/MM/yyyy").parse(selectedNonEmp.getBirthDateGregString()));
				}

			} else {
				surveillanceEmpNonEmpData.setEmployeeId(selectedEmp.getEmpId());
				surveillanceEmpNonEmpData.setEmployeeFullName(selectedEmp.getFullName());
				surveillanceEmpNonEmpData.setMilitaryNumber(selectedEmp.getMilitaryNo());
				surveillanceEmpNonEmpData.setRank(selectedEmp.getRank());
				surveillanceEmpNonEmpData.setSocialId(selectedEmp.getSocialID());
				surveillanceEmpNonEmpData.setBirthDate(selectedEmp.getBirthDate());
				selectedEmp = null;
			}

			// Copy Order Data To Surveillance Employee
			surveillanceEmpNonEmpData.setPeriodMonths(surveillanceOrderData.getPeriodMonths());
			surveillanceEmpNonEmpData.setPeriodicReporting(surveillanceOrderData.getPeriodicReporting());
			surveillanceEmpNonEmpList.add(surveillanceEmpNonEmpData);
			surveillanceEmpNonEmpData.setSurveillanceOrderReasonsList(surveillanceOrderSelectedReasonsList);
			socialId = null;
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (ParseException e) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}catch (Exception e) {
			   Log4j.traceErrorException(SurveillanceOrderRegisteration.class, e, "SurveillanceOrderRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * 
	 */
	public void validateSocialId() {
		try {
			System.out.println(socialId);
			showMsg = false;
			if (socialId == null) {
				throw new BusinessException("error_mandatory");
			}
			EmployeeData emp = EmployeeService.getEmployee(socialId.toString());
			if (emp == null) {
				List<NonEmployeeData> nonEmp = NonEmployeeService.getNonEmployee(socialId, null);
				if (!nonEmp.isEmpty()) {
					selectedNonEmp = nonEmp.get(0);
					addEmpNonEmp();
				} else {
					showMsg = true;
					selectedNonEmp = new NonEmployeeData();
				}
			} else {
				selectedEmp = emp;
				addEmpNonEmp();
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SurveillanceOrderRegisteration.class, e, "SurveillanceOrderRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Delete Employee
	 */
	public void deleteEmployee(SurveillanceEmpNonEmpData surveillanceEmployeeData) {
		surveillanceEmpNonEmpList.remove(surveillanceEmployeeData);
		if (surveillanceEmployeeData.getId() != null) {
			surveillanceEmployeesDeletedList.add(surveillanceEmployeeData);
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
	}

	/**
	 * Save and Start Surveillance order workflow
	 */
	public String save() {
		try {
			if (role.equals(WFTaskRolesEnum.REQUESTER.getCode()) && currentTask != null) {
				SurveillanceWorkFlow.closeSurveillanceOrder(instance, currentTask);
				return NavigationEnum.INBOX.toString();
			}
			if (surveillanceOrderData.getOrderDate().compareTo(HijriDateService.getHijriSysDate()) > 1) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_invalidDate"));
				return null;
			}
			if (surveillanceOrderData.getOrderDate().compareTo(surveillanceOrderData.getStartDate()) > 1) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_invalidStartDate"));
				return null;
			}
			SurveillanceOrdersService.addSurveillanceOrder(loginEmpData, surveillanceOrderData, surveillanceEmpNonEmpList, surveillanceEmployeesDeletedList, true, verbalOrderFlag);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(SurveillanceOrderRegisteration.class, e, "SurveillanceOrderRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		getRequest().setAttribute("mode", surveillanceOrderData.getId());
		return NavigationEnum.SURVEILLANCE_ORDER_REGISTERATION.toString();
	}

	/**
	 * Save and Start Surveillance order workflow
	 */
	public String saveAndSend() {
		try {
			if (surveillanceOrderData.getOrderDate().compareTo(HijriDateService.getHijriSysDate()) > 1) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_invalidDate"));
				return null;
			}
			if (surveillanceOrderData.getOrderDate().compareTo(surveillanceOrderData.getStartDate()) > 1) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_invalidStartDate"));
				return null;
			}
			if (currentTask == null) {
				SurveillanceOrdersService.addSurveillanceOrder(loginEmpData, surveillanceOrderData, surveillanceEmpNonEmpList, surveillanceEmployeesDeletedList, false, verbalOrderFlag);
			} else {
				SurveillanceWorkFlow.resendSurveillanceOrder(currentTask, surveillanceOrderData, surveillanceEmpNonEmpList, surveillanceEmployeesDeletedList, loginEmpData, generalDirectorRole, verbalOrderFlag);
			}
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(SurveillanceOrderRegisteration.class, e, "SurveillanceOrderRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Approve Surveillance Order
	 */
	public String doApprove() {
		try {
			if (role.equals(regionIntelligenceManagerRole)) {
				SurveillanceWorkFlow.doSurveillanceOrderDM(currentTask, surveillanceOrderData.getSurveillanceOrder(), loginEmpData, attachments);
			} else if (role.equals(generalIntelligenceManagerRole)) {
				SurveillanceWorkFlow.doSurveillanceOrderDM(currentTask, surveillanceOrderData.getSurveillanceOrder(), loginEmpData, attachments);
			} else if (role.equals(regionDirectorRole) || role.equals(generalDirectorRole)) {
				SurveillanceWorkFlow.doSurveillanceOrderSM(currentTask, surveillanceOrderData.getSurveillanceOrder(), surveillanceEmpNonEmpList, loginEmpData, attachments);
			} else if (role.equals(WFTaskRolesEnum.REGION_SECURITY_INTELLIGENCE_MANAGER.getCode())) {
				SurveillanceWorkFlow.doSurveillanceOrderRegionSecurityManager(currentTask, surveillanceOrderData.getSurveillanceOrder(), loginEmpData, attachments);
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(SurveillanceOrderRegisteration.class, e, "SurveillanceOrderRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Reject Surveillance Order
	 */
	public String doReject() {
		try {
			SurveillanceWorkFlow.rejectSurveillanceOrder(currentTask, surveillanceOrderData.getSurveillanceOrder(), loginEmpData, attachments);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
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
			SurveillanceWorkFlow.doNotify(currentTask);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Reset order number when user chooses verbal order check box
	 */
	public void resetOrderNumber() {
		surveillanceOrderData.setOrderNumber(null);
	}

	/**
	 * Prepare Upload Parameters
	 * 
	 * @param surveillanceOrderId
	 */
	public void getUploadParam(long surveillanceOrderId) {
		try {
			String requestURL = getRequest().getRequestURL() + "";
			String url = requestURL.replace(getRequest().getServletPath(), "");
			String serviceURL = url + AttachmentServiceCallBack.SERVICE_URL;
			fileArchivingParam = EncryptionUtil.encryptSymmetrically("code=" + ServiceCodesEnum.INFO_SYS.getCode() + "&token=" + EntityNameEnum.SURVEILLANCE_ORDER.getCode() + "_" + surveillanceOrderId + "&serviceurl=" + serviceURL);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			   Log4j.traceErrorException(SurveillanceOrderRegisteration.class, e, "SurveillanceOrderRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Prepare Download parameters
	 * 
	 * @param attachmentId
	 */
	public void getDownloadParam(String attachmentId) {
		try {
			downloadFileParamId = EncryptionUtil.encryptSymmetrically("contentId=" + attachmentId);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}  catch (Exception e) {
			   Log4j.traceErrorException(SurveillanceOrderRegisteration.class, e, "SurveillanceOrderRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Delete Attachment
	 * 
	 * @param attachment
	 */
	public void getDeleteParam(Attachment attachment) {
		try {
			downloadFileParamId = EncryptionUtil.encryptSymmetrically("contentId=" + attachment.getAttachmentId());
			URL url = new URL(InfoSysConfigurationService.getBoolServerDeletePath() + downloadFileParamId);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			if (responseCode == HttpServletResponse.SC_OK) {
				AttachmentService.deleteAttachment(attachment);
				attachmentList.remove(attachment);
			} else {
				throw new BusinessException("error_general");
			}

		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}  catch (Exception e) {
			   Log4j.traceErrorException(SurveillanceOrderRegisteration.class, e, "SurveillanceOrderRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Get Attachments Ids and prepare for downloading
	 * 
	 * @throws BusinessException
	 */
	public void getDownloadedAttachmentsIdList() throws BusinessException {
		try {
			attachmentList = AttachmentService.getAttachments(EntityNameEnum.SURVEILLANCE_ORDER.getCode(), surveillanceOrderData.getId());
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

	/**
	 * Print order report
	 */
	public void print() {
		try {
			byte[] bytes = SurveillanceOrdersService.getSurveillanceOrderReportBytes(surveillanceOrderData.getId(), loginEmpData.getFullName());
			super.print(bytes, "Surveillance_Order_Details");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			   Log4j.traceErrorException(SurveillanceOrderRegisteration.class, e, "SurveillanceOrderRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Open warning dialog before save or send
	 * 
	 * @param mode
	 */
	public void openDialogue(int mode) {
		dialogueMode = mode;
		// Check the absence of the birth date
		String missingBirthDateEmps = "";
		for (int i = 0; i < surveillanceEmpNonEmpList.size(); i++) {
			if (surveillanceEmpNonEmpList.get(i).getBirthDate() == null && surveillanceEmpNonEmpList.get(i).getBirthDateGreg() == null) {
				missingBirthDateEmps += " - " + surveillanceEmpNonEmpList.get(i).getSocialId() + ":" + surveillanceEmpNonEmpList.get(i).getEmployeeFullName();
			}
		}
		if (!(missingBirthDateEmps.trim().isEmpty())) {
			warningAbsenceBirthDate = new StringBuilder();
			warningAbsenceBirthDate.append(getParameterizedMessage("error_missingBirthdate", new Object[] { missingBirthDateEmps }));
		}
	}

	// setters and getters
	public SurveillanceOrderData getSurveillanceOrderData() {
		return surveillanceOrderData;
	}

	public void setSurveillanceOrderData(SurveillanceOrderData surveillanceOrderData) {
		this.surveillanceOrderData = surveillanceOrderData;
	}

	public List<String> getSurveillanceOrderReasonsList() {
		return surveillanceOrderReasonsList;
	}

	public void setSurveillanceOrderReasonsList(List<String> surveillanceOrderReasonsList) {
		this.surveillanceOrderReasonsList = surveillanceOrderReasonsList;
	}

	public List<SurveillanceEmpNonEmpData> getSurveillanceEmpNonEmpList() {
		return surveillanceEmpNonEmpList;
	}

	public void setSurveillanceEmpNonEmpList(List<SurveillanceEmpNonEmpData> surveillanceEmpNonEmpList) {
		this.surveillanceEmpNonEmpList = surveillanceEmpNonEmpList;
	}

	public Integer getPageMode() {
		return pageMode;
	}

	public void setPageMode(Integer pageMode) {
		this.pageMode = pageMode;
	}

	public List<String> getSurveillanceOrderSelectedReasonsList() {
		return surveillanceOrderSelectedReasonsList;
	}

	public void setSurveillanceOrderSelectedReasonsList(List<String> surveillanceOrderSelectedReasonsList) {
		this.surveillanceOrderSelectedReasonsList = surveillanceOrderSelectedReasonsList;
	}

	public String getFileArchivingParam() {
		return fileArchivingParam;
	}

	public void setFileArchivingParam(String fileArchivingParam) {
		this.fileArchivingParam = fileArchivingParam;
	}

	public List<Attachment> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<Attachment> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public String getSelectedDownloadFileId() {
		return selectedDownloadFileId;
	}

	public void setSelectedDownloadFileId(String selectedDownloadFileId) {
		this.selectedDownloadFileId = selectedDownloadFileId;
	}

	public boolean isOpenDownloadPopupFlag() {
		return openDownloadPopupFlag;
	}

	public void setOpenDownloadPopupFlag(boolean openDownloadPopupFlag) {
		this.openDownloadPopupFlag = openDownloadPopupFlag;
	}

	public String getDownloadFileParamId() {
		return downloadFileParamId;
	}

	public void setDownloadFileParamId(String downloadFileParamId) {
		this.downloadFileParamId = downloadFileParamId;
	}

	public boolean isOpenDownloadDialogueFlag() {
		return openDownloadDialogueFlag;
	}

	public void setOpenDownloadDialogueFlag(boolean openDownloadDialogueFlag) {
		this.openDownloadDialogueFlag = openDownloadDialogueFlag;
	}

	public String getBoolServerUploadPath() {
		return InfoSysConfigurationService.getBoolServerUploadPath();
	}

	public String getBoolServerDownloadPath() {
		return InfoSysConfigurationService.getBoolServerDownloadPath();
	}

	public boolean isVerbalOrderFlag() {
		return verbalOrderFlag;
	}

	public void setVerbalOrderFlag(boolean verbalOrderFlag) {
		this.verbalOrderFlag = verbalOrderFlag;
	}

	public List<FinalApprovalEntitiesEnum> getFinalApprovalList() {
		return finalApprovalList;
	}

	public void setFinalApprovalList(List<FinalApprovalEntitiesEnum> finalApprovalList) {
		this.finalApprovalList = finalApprovalList;
	}

	public Long getSocialId() {
		return socialId;
	}

	public void setSocialId(Long socialId) {
		this.socialId = socialId;
	}

	public StringBuilder getWarningAbsenceBirthDate() {
		return warningAbsenceBirthDate;
	}

	public void setWarningAbsenceBirthDate(StringBuilder warningAbsenceBirthDate) {
		this.warningAbsenceBirthDate = warningAbsenceBirthDate;
	}

	public int getDialogueMode() {
		return dialogueMode;
	}

	public void setDialogueMode(int dialogueMode) {
		this.dialogueMode = dialogueMode;
	}

	public boolean isShowMsg() {
		return showMsg;
	}

	public void setShowMsg(boolean showMsg) {
		this.showMsg = showMsg;
	}

	public NonEmployeeData getSelectedNonEmp() {
		return selectedNonEmp;
	}

	public void setSelectedNonEmp(NonEmployeeData selectedNonEmp) {
		this.selectedNonEmp = selectedNonEmp;
	}
}