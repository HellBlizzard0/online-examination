package com.code.ui.backings.labcheck;

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletResponse;

import com.code.dal.orm.attach.Attachment;
import com.code.dal.orm.labcheck.LabCheck;
import com.code.dal.orm.labcheck.LabCheckEmployeeData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.enums.EntityNameEnum;
import com.code.enums.LabCheckEmployeeCheckStatusEnum;
import com.code.enums.LabCheckReasonsEnum;
import com.code.enums.LabCheckStatusEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.ServiceCodesEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.attach.AttachmentService;
import com.code.services.attach.AttachmentServiceCallBack;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.labcheck.LabCheckService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.EmployeeService;
import com.code.services.setup.SetupService;
import com.code.services.workflow.labcheck.LabCheckWorkFlow;
import com.code.ui.backings.base.WFBaseBacking;
import com.code.ui.util.EncryptionUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "labCheckReg")
@ViewScoped
public class LabCheckRegisteration extends WFBaseBacking implements Serializable {
	private LabCheck labCheck;
	private List<LabCheckEmployeeData> labCheckEmployeeDataList;
	private Long selectedEmpId;
	private List<SetupDomain> orderSourcesList;
	private List<LabCheckEmployeeData> perviousLabCheckEmployeeDataList;
	private int pageMode;
	private String fileArchivingParam;
	private String downloadFileParamId;
	private List<Attachment> attachmentList;
	private boolean openDownloadPopupFlag;
	private boolean openDownloadDialogueFlag;
	private String boolServerUploadPath;
	private String boolServerDownloadPath;
	private String uploadFlag;

	public LabCheckRegisteration() {
		super();
		init();
		perviousLabCheckEmployeeDataList = new ArrayList<LabCheckEmployeeData>();
		labCheck = new LabCheck();
		labCheckEmployeeDataList = new ArrayList<LabCheckEmployeeData>();
		boolServerUploadPath = InfoSysConfigurationService.getBoolServerUploadPath();
		boolServerDownloadPath = InfoSysConfigurationService.getBoolServerDownloadPath();
		try {
			orderSourcesList = SetupService.getDomains(ClassesEnum.ORDER_SOURCES.getCode());
			if (getRequest().getParameter("pageMode") != null) {
				pageMode = (Integer.valueOf(getRequest().getParameter("pageMode")));
				if (pageMode == 2) {
					labCheck.setCheckReason(LabCheckReasonsEnum.RANDOM_MISSION_FROM_DIRECTORATE.getCode());
					uploadFlag = null;
				}
			} else {
				pageMode = 1;
			}
			if (getRequest().getAttribute("mode") != null) {
				labCheck = LabCheckService.getLabCheck((Long) getRequest().getAttribute("mode"));
				Integer retestNumber = null;
				if (getRequest().getAttribute("retest") != null) {
					retestNumber = (Integer) getRequest().getAttribute("retest");
				}
				labCheckEmployeeDataList = LabCheckService.getLabCheckEmployesByLabCheckId(labCheck.getId(), retestNumber);
			}

			if (currentTask != null) {
				labCheck = LabCheckService.getLabCheckByWfInstanceId(currentTask.getInstanceId());
				labCheckEmployeeDataList = LabCheckService.getLabCheckEmployesByLabCheckId(labCheck.getId(), null);
				if (role.equals(WFTaskRolesEnum.REQUESTER.getCode())) {
					pageMode = 3;
				}
			}
			if (pageMode == 1 && labCheck.getId() == null) {
				uploadFlag = getParameterizedMessage("label_yes");
			} else {
				uploadFlag = getParameterizedMessage("label_no");
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckRegisteration.class, e, "LabCheckRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Add Employee
	 */
	public void addEmployee() {
		try {
			EmployeeData emp = EmployeeService.getEmployee(selectedEmpId);
			for (LabCheckEmployeeData employee : labCheckEmployeeDataList) {
				if (employee.getEmployeeId().equals(selectedEmpId)) {
					this.setServerSideErrorMessages(getParameterizedMessage("error_alreadyChoosen"));
					return;
				}
			}

			LabCheckEmployeeData labCheckEmployeeData = new LabCheckEmployeeData();
			labCheckEmployeeData.setEmployeeId(emp.getEmpId());
			labCheckEmployeeData.setEmployeeFullName(emp.getFullName());
			labCheckEmployeeData.setEmployeeMilitaryNumber(emp.getMilitaryNo());
			labCheckEmployeeData.setEmployeeRank(emp.getRank());
			labCheckEmployeeData.setEmployeeSocialId(emp.getSocialID());
			labCheckEmployeeData.setEmployeeDepartmentId(emp.getActualDepartmentId());
			labCheckEmployeeData.setEmployeeDepartmentName(emp.getActualDepartmentName());

			if (labCheck.getId() != null) {
				labCheckEmployeeData.setLabCheckId(labCheck.getId());
				labCheckEmployeeData.setOrderDate(labCheck.getOrderDate());
				LabCheckService.addLabCheckEmployee(labCheck, labCheckEmployeeData, loginEmpData);
			}
			labCheckEmployeeDataList.add(labCheckEmployeeData);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckRegisteration.class, e, "LabCheckRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * delete Employee
	 * 
	 * @param labCheckEmployeeData
	 */
	public void deleteEmployee(LabCheckEmployeeData labCheckEmployeeData) {
		try {
			labCheckEmployeeDataList.remove(labCheckEmployeeData);
			if (labCheck.getId() != null) {
				LabCheckService.deleteLabCheckEmployee(labCheckEmployeeData, loginEmpData);
			}
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckRegisteration.class, e, "LabCheckRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * save And init workflow
	 */
	public String saveAndSend() {
		try {
			for (SetupDomain domain : orderSourcesList) {
				if (domain.getId().equals(labCheck.getOrderSourceDomainId())) {
					labCheck.setOrderSourceDomainDescription(domain.getDescription());
					break;
				}
			}
			if (labCheck.getRegionId() == null) {
				labCheck.setRegionId(DepartmentService.getDepartment(loginEmpData.getActualDepartmentId()).getRegionId());
			}
			LabCheckWorkFlow.initLabCheck(labCheck, labCheckEmployeeDataList, loginEmpData, null, currentTask);
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckRegisteration.class, e, "LabCheckRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Save lab check
	 * 
	 * @return
	 */
	public String save() {
		try {
			for (SetupDomain domain : orderSourcesList) {
				if (domain.getId().equals(labCheck.getOrderSourceDomainId())) {
					labCheck.setOrderSourceDomainDescription(domain.getDescription());
					break;
				}
			}
			if (labCheck.getRegionId() == null) {
				labCheck.setRegionId(DepartmentService.getDepartment(loginEmpData.getActualDepartmentId()).getRegionId());
			}
			labCheck.setStatus(LabCheckStatusEnum.REGISTERED.getCode());
			LabCheckService.saveLabCheck(labCheck, labCheckEmployeeDataList, loginEmpData);
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckRegisteration.class, e, "LabCheckRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		getRequest().setAttribute("mode", labCheck.getId());
		return NavigationEnum.LAB_CHECK_SEARCH.toString();
	}

	/**
	 * Anti Drug Department Manager Approve
	 * 
	 * @return
	 */
	public String doApproveDM() {
		try {
			LabCheckWorkFlow.doApproveDM(currentTask, labCheck, loginEmpData, null);
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckRegisteration.class, e, "LabCheckRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * General / Region Director Approve
	 * 
	 * @return
	 */
	public String doApproveSM() {
		try {
			LabCheckWorkFlow.doApproveSM(currentTask, labCheck, labCheckEmployeeDataList, loginEmpData, null);
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckRegisteration.class, e, "LabCheckRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * doApproveRegionSecurityManager
	 * 
	 * @return
	 */
	public String doApproveRegionSecurityManager() {
		try {
			LabCheckWorkFlow.doApproveRegionProtectionManager(currentTask, labCheck, loginEmpData, null);
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckRegisteration.class, e, "LabCheckRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * doReject
	 * 
	 * @return
	 */
	public String doReject() {
		try {
			LabCheckWorkFlow.doReject(currentTask, labCheck, loginEmpData, null);
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckRegisteration.class, e, "LabCheckRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Closed Notification
	 * 
	 * @return
	 */
	public String doNotify() {
		try {
			LabCheckWorkFlow.doNotify(currentTask);
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckRegisteration.class, e, "LabCheckRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * 
	 * @param employeeId
	 */
	public void getPerviousEmployeeChecks(Long employeeId) {
		try {
			selectedEmpId = employeeId;
			perviousLabCheckEmployeeDataList = LabCheckService.getPervEmployeesLabChecks(employeeId, labCheck.getOrderDate());
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckRegisteration.class, e, "LabCheckRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Constitute upload parameter which contains bool server registered serviceCode and the token (EntityName_EntityId)
	 * 
	 * @param labCheckId
	 */
	public void getUploadParam(long labCheckId) {
		try {
			String requestURL = getRequest().getRequestURL() + "";
			String url = requestURL.replace(getRequest().getServletPath(), "");
			String serviceURL = url + AttachmentServiceCallBack.SERVICE_URL;
			fileArchivingParam = EncryptionUtil.encryptSymmetrically("code=" + ServiceCodesEnum.INFO_SYS.getCode() + "&token=" + EntityNameEnum.LAB_CHECK.getCode() + "_" + labCheckId + "&serviceurl=" + serviceURL);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckRegisteration.class, e, "LabCheckRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
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
		} catch (Exception e) {
			   Log4j.traceErrorException(LabCheckRegisteration.class, e, "LabCheckRegisteration");
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
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckRegisteration.class, e, "LabCheckRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Get attachment list related to the entity id passed to the method then check if there's no records then show message indicating no daya else if it's only one record , open the download popup directly , otherwise open a dialogue shows file names and make the user select which one to download then open the download popup with the choosen contentId (attachmentId)
	 * 
	 * @param labCheckId
	 * @throws BusinessException
	 */
	public void getDownloadedAttachmentsIdList(long labCheckId) throws BusinessException {
		try {
			attachmentList = AttachmentService.getAttachments(EntityNameEnum.LAB_CHECK.getCode(), labCheckId);
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
	 * Print report
	 */
	public void print() {
		try {
			byte[] bytes = LabCheckService.getLabCheckDetailsReportBytes(labCheck.getId(), loginEmpData.getFullName());
			super.print(bytes, "Lab_Check_Details");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckRegisteration.class, e, "LabCheckRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/********************************************************** Setters And Getters **********************************************/
	public LabCheck getLabCheck() {
		return labCheck;
	}

	public void setLabCheck(LabCheck labCheck) {
		this.labCheck = labCheck;
	}

	public List<LabCheckEmployeeData> getLabCheckEmployeeDataList() {
		return labCheckEmployeeDataList;
	}

	public void setLabCheckEmployeeDataList(List<LabCheckEmployeeData> labCheckEmployeeDataList) {
		this.labCheckEmployeeDataList = labCheckEmployeeDataList;
	}

	public Long getSelectedEmpId() {
		return selectedEmpId;
	}

	public void setSelectedEmpId(Long selectedEmpId) {
		this.selectedEmpId = selectedEmpId;
	}

	public String getRegionDirector() {
		return WFTaskRolesEnum.REGION_DIRECTOR.getCode();
	}

	public String getGeneralDirector() {
		return WFTaskRolesEnum.GENERAL_DIRECTOR.getCode();
	}

	public String getRegionAntiDrugManager() {
		return WFTaskRolesEnum.REGION_ANTI_DRUG_MANAGER.getCode();
	}

	public String getRegionSecurityManager() {
		return WFTaskRolesEnum.REGION_SECURITY_INTELLIGENCE_MANAGER.getCode();
	}

	public String getGeneralAntiDrugManager() {
		return WFTaskRolesEnum.GENERAL_ANTI_DRUG_MANAGER.getCode();
	}

	public String getNotification() {
		return WFTaskRolesEnum.NOTIFICATION.getCode();
	}

	public String getRequesterRole() {
		return WFTaskRolesEnum.REQUESTER.getCode();
	}

	public List<LabCheckEmployeeData> getPerviousLabCheckEmployeeDataList() {
		return perviousLabCheckEmployeeDataList;
	}

	public void setPerviousLabCheckEmployeeDataList(List<LabCheckEmployeeData> perviousLabCheckEmployeeDataList) {
		this.perviousLabCheckEmployeeDataList = perviousLabCheckEmployeeDataList;
	}

	public Integer getNoSampleCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.NO_SAMPLE.getCode();
	}

	public Integer getRetestCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.RETEST.getCode();
	}

	public Integer getCheckPrivationCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.CHECK_PRIVATION.getCode();
	}

	public Integer getNegativeCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.NEGATIVE.getCode();
	}

	public Integer getPositiveCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.POSITIVE.getCode();
	}

	public Integer getPositiveUnderApproveCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.POSITIVE_UNDER_APPROVE.getCode();
	}

	public Integer getSendingToForceCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.SENDING_TO_FORCE.getCode();
	}

	public Integer getCheatingCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.CHEATING.getCode();
	}

	public List<SetupDomain> getOrderSourcesList() {
		return orderSourcesList;
	}

	public void setOrderSourcesList(List<SetupDomain> orderSourcesList) {
		this.orderSourcesList = orderSourcesList;
	}

	public int getPageMode() {
		return pageMode;
	}

	public void setPageMode(int pageMode) {
		this.pageMode = pageMode;
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

	public String getUploadFlag() {
		return uploadFlag;
	}

	public void setUploadFlag(String uploadFlag) {
		this.uploadFlag = uploadFlag;
	}

	public Integer getCasesReason() {
		return LabCheckReasonsEnum.CASES.getCode();
	}

	public List<LabCheckReasonsEnum> getLabCheckReasonsEnumList() {
		return LabCheckReasonsEnum.getAllCheckReasonsEnumValues();
	}

}