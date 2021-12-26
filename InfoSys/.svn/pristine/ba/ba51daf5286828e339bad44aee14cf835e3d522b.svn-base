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
import com.code.dal.orm.labcheck.LabCheckEmployeeData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.enums.EntityNameEnum;
import com.code.enums.LabCheckEmployeeCheckStatusEnum;
import com.code.enums.LabCheckReasonsEnum;
import com.code.enums.ServiceCodesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.attach.AttachmentService;
import com.code.services.attach.AttachmentServiceCallBack;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.labcheck.LabCheckService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.SetupService;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;
import com.code.ui.util.EncryptionUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "labCheckResultReg")
@ViewScoped
public class LabCheckResultRegisteration extends BaseBacking implements Serializable {
	private List<LabCheckEmployeeData> labCheckEmployeeDataList;
	private LabCheckEmployeeData selectedLabCheckEmployeeData;
	private String sampleNumber;
	private Long employeeId;
	private String employeeName;
	private List<LabCheckEmployeeData> pervEmployeeLabCheckList;
	private List<SetupDomain> domainMetrialTypeList;
	private String fileArchivingParam;
	private String downloadFileParamId;
	private List<Attachment> attachmentList;
	private boolean openDownloadPopupFlag;
	private boolean openDownloadDialogueFlag;
	private String boolServerUploadPath;
	private String boolServerDownloadPath;

	public LabCheckResultRegisteration() {
		super();
		init();
		labCheckEmployeeDataList = new ArrayList<LabCheckEmployeeData>();
		pervEmployeeLabCheckList = new ArrayList<LabCheckEmployeeData>();
		selectedLabCheckEmployeeData = new LabCheckEmployeeData();
		boolServerUploadPath = InfoSysConfigurationService.getBoolServerUploadPath();
		boolServerDownloadPath = InfoSysConfigurationService.getBoolServerDownloadPath();

		try {
			domainMetrialTypeList = SetupService.getDomains(ClassesEnum.MATERIAL_TYPES.getCode());
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckResultRegisteration.class, e, "LabCheckResultRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * search
	 */
	public void search() {
		try {
			labCheckEmployeeDataList = LabCheckService.getLabCheckEmployees(employeeId, null, null, null, LabCheckEmployeeCheckStatusEnum.SAMPLE_TAKED.getCode(), null, null, null, sampleNumber);
			if (labCheckEmployeeDataList.isEmpty()) {
				this.setServerSideSuccessMessages(getParameterizedMessage("error_noData"));
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckResultRegisteration.class, e, "LabCheckResultRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * resetSearch
	 */
	public void resetSearch() {
		labCheckEmployeeDataList = new ArrayList<LabCheckEmployeeData>();
		pervEmployeeLabCheckList = new ArrayList<LabCheckEmployeeData>();
		selectedLabCheckEmployeeData = new LabCheckEmployeeData();
		sampleNumber = null;
		employeeId = null;
		employeeName = null;
	}

	/**
	 * getPrevEmployeeLabChecks
	 */
	public void getPrevEmployeeLabChecks(LabCheckEmployeeData row) {
		try {
			pervEmployeeLabCheckList = LabCheckService.getPervEmployeesLabChecks(row.getEmployeeId(), row.getOrderDate());
			if (pervEmployeeLabCheckList.isEmpty()) {
				this.setServerSideSuccessMessages(getParameterizedMessage("error_noData"));
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckResultRegisteration.class, e, "LabCheckResultRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * selectEmployee
	 * 
	 * @param row
	 */
	public void selectEmployee(LabCheckEmployeeData row) {
		selectedLabCheckEmployeeData = row;
		pervEmployeeLabCheckList = new ArrayList<LabCheckEmployeeData>();
	}

	/**
	 * saveResult
	 */
	public void saveResult() {
		try {
			if (selectedLabCheckEmployeeData.getSampleNumber().trim().isEmpty() || selectedLabCheckEmployeeData.getCheckStatus() == null || (selectedLabCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.POSITIVE_UNDER_APPROVE.getCode()) && selectedLabCheckEmployeeData.getDomainMaterialTypeId() == null)) {
				throw new BusinessException("error_mandatory");
			}
			selectedLabCheckEmployeeData.setResultEnteredDate(HijriDateService.getHijriSysDate());
			selectedLabCheckEmployeeData.setResultEnteredEmpId(loginEmpData.getEmpId());
			if (selectedLabCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.RETEST.getCode())) {
				LabCheckService.retestLabCheckEmployee(selectedLabCheckEmployeeData, loginEmpData);
			} else {
				LabCheckService.updateLabCheckEmployee(selectedLabCheckEmployeeData, loginEmpData);
			}
			resetSearch();
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckResultRegisteration.class, e, "LabCheckResultRegisteration");
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
		} catch (Exception e) {
			   Log4j.traceErrorException(LabCheckResultRegisteration.class, e, "LabCheckResultRegisteration");
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
			   Log4j.traceErrorException(LabCheckResultRegisteration.class, e, "LabCheckResultRegisteration");
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
		} catch (Exception e) {
			   Log4j.traceErrorException(LabCheckResultRegisteration.class, e, "LabCheckResultRegisteration");
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

	public List<LabCheckEmployeeData> getLabCheckEmployeeDataList() {
		return labCheckEmployeeDataList;
	}

	public void setLabCheckEmployeeDataList(List<LabCheckEmployeeData> labCheckEmployeeDataList) {
		this.labCheckEmployeeDataList = labCheckEmployeeDataList;
	}

	public LabCheckEmployeeData getSelectedLabCheckEmployeeData() {
		return selectedLabCheckEmployeeData;
	}

	public void setSelectedLabCheckEmployeeData(LabCheckEmployeeData selectedLabCheckEmployeeData) {
		this.selectedLabCheckEmployeeData = selectedLabCheckEmployeeData;
	}

	public String getSampleNumber() {
		return sampleNumber;
	}

	public void setSampleNumber(String sampleNumber) {
		this.sampleNumber = sampleNumber;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public List<LabCheckEmployeeData> getPervEmployeeLabCheckList() {
		return pervEmployeeLabCheckList;
	}

	public void setPervEmployeeLabCheckList(List<LabCheckEmployeeData> pervEmployeeLabCheckList) {
		this.pervEmployeeLabCheckList = pervEmployeeLabCheckList;
	}

	public List<SetupDomain> getDomainMetrialTypeList() {
		return domainMetrialTypeList;
	}

	public void setDomainMetrialTypeList(List<SetupDomain> domainMetrialTypeList) {
		this.domainMetrialTypeList = domainMetrialTypeList;
	}

	public Integer getNoSampleCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.NO_SAMPLE.getCode();
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

	public Integer getRetestCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.RETEST.getCode();
	}
	
	public Integer getCheatingInCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.CHEATING.getCode();
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

	public List<LabCheckReasonsEnum> getLabCheckReasonsEnumList() {
		return LabCheckReasonsEnum.getAllCheckReasonsEnumValues();
	}

}
