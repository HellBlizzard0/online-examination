package com.code.ui.backings.labcheck;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.attach.Attachment;
import com.code.dal.orm.labcheck.LabCheck;
import com.code.dal.orm.labcheck.LabCheckEmployeeData;
import com.code.dal.orm.security.UserMenuActionData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.enums.EntityNameEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.LabCheckEmployeeCheckStatusEnum;
import com.code.enums.LabCheckReasonsEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.ServiceCodesEnum;
import com.code.enums.UserMenuActionsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.attach.AttachmentService;
import com.code.services.attach.AttachmentServiceCallBack;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.labcheck.LabCheckService;
import com.code.services.log4j.Log4j;
import com.code.services.security.SecurityService;
import com.code.services.setup.SetupService;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;
import com.code.ui.util.EncryptionUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "nationalForceSample")
@ViewScoped
public class NationalForcesSample extends BaseBacking implements Serializable {

	private LabCheckEmployeeData labCheckEmployeeData;
	private LabCheck labCheck;
	private List<LabCheckEmployeeData> perviousLabCheckEmployeeDataList;
	private List<SetupDomain> domainMetrialTypeList;
	private List<SetupDomain> domainCurmentHospitalsList;
	private Integer mode;
	private Integer retestMonthNumber;
	private String fileArchivingParam;
	private String downloadFileParamId;
	private List<Attachment> attachmentList;
	private boolean openDownloadPopupFlag;
	private boolean openDownloadDialogueFlag;
	private String boolServerUploadPath;
	private String boolServerDownloadPath;
	private boolean deletePrivilage;

	public NationalForcesSample() {
		super();
		init();
		boolServerUploadPath = InfoSysConfigurationService.getBoolServerUploadPath();
		boolServerDownloadPath = InfoSysConfigurationService.getBoolServerDownloadPath();
		try {
			UserMenuActionData deleteAttachmentAction = SecurityService.getAction(loginEmpData.getEmpId(), UserMenuActionsEnum.DELETE_ATTACHMENT_PERMISSION.getCode(), FlagsEnum.ALL.getCode());
			if (deleteAttachmentAction != null) {
				setDeletePrivilage(true);
			}
			if (getRequest().getAttribute("labCheckEmployeeDataId") != null && getRequest().getAttribute("mode") != null) {
				labCheckEmployeeData = LabCheckService.getLabCheckEmployeeDataById((Long) getRequest().getAttribute("labCheckEmployeeDataId"));
				labCheck = LabCheckService.getLabCheck(labCheckEmployeeData.getLabCheckId());
				mode = (Integer) getRequest().getAttribute("mode");
				perviousLabCheckEmployeeDataList = LabCheckService.getPervEmployeesLabChecks(labCheckEmployeeData.getEmployeeId(), labCheck.getOrderDate());
				domainMetrialTypeList = SetupService.getDomains(ClassesEnum.MATERIAL_TYPES.getCode());
				domainCurmentHospitalsList = SetupService.getDomains(ClassesEnum.HEALTH_CARE_CANTERS.getCode());
				if (labCheckEmployeeData.getPeriodicRetestDate() != null) {
					retestMonthNumber = HijriDateService.hijriDateDiff(labCheckEmployeeData.getSampleDate(), labCheckEmployeeData.getPeriodicRetestDate()) / 29;
				}
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(NationalForcesSample.class, e, "NationalForcesSample");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public String save() {
		try {
			if (mode == 1) {
				if (labCheckEmployeeData.getNationalForceSampleNumber() == null || labCheckEmployeeData.getNationalForceSampleNumber().trim().isEmpty() || labCheckEmployeeData.getNationalForceSampleSentDate() == null) {
					throw new BusinessException("error_mandatory");
				}
				if (labCheckEmployeeData.getNationalForceSampleSentDate() != null && labCheckEmployeeData.getNationalForceSampleSentDate().after(HijriDateService.getHijriSysDate())) {
					throw new BusinessException("error_comingDate");
				}
				if (labCheckEmployeeData.getNationalForceSampleSentDate() != null && labCheckEmployeeData.getNationalForceSampleSentDate().before(labCheckEmployeeData.getSampleDate())) {
					throw new BusinessException("error_nationalForcesSampleDateMustExceedSampleInitialDate");
				}
				labCheckEmployeeData.setCheckStatus(LabCheckEmployeeCheckStatusEnum.SENDING_TO_FORCE.getCode());
			} else {
				if (labCheckEmployeeData.getCheckStatus() == null)
					throw new BusinessException("error_mandatory");
				if (labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.POSITIVE.getCode()) && labCheckEmployeeData.getDomainNationalForceMaterialTypeId() == null)
					throw new BusinessException("error_mandatory");
				if (labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.POSITIVE.getCode()) && labCheckEmployeeData.getCurement() != null && labCheckEmployeeData.getCurement() && labCheckEmployeeData.getDomainCureHospitalId() == null)
					throw new BusinessException("error_mandatory");
				
				labCheckEmployeeData.setNationalForceResultEnteredDate(HijriDateService.getHijriSysDate());
				labCheckEmployeeData.setNationalForceResultEnteredEmpId(loginEmpData.getEmpId());
			}
			labCheckEmployeeData.setPeriodicRetestDate(retestMonthNumber == null ? null : HijriDateService.addSubHijriMonthsDays(labCheckEmployeeData.getNationalForceSampleSentDate(), retestMonthNumber, 0));
			LabCheckService.updateLabCheckEmployee(labCheckEmployeeData, loginEmpData);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
			return NavigationEnum.NATIONAL_LAB_SEARCH.toString();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(NationalForcesSample.class, e, "NationalForcesSample");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
		return null;
	}

	/**
	 * Constitute upload parameter which contains bool server registered serviceCode and the token (EntityName_EntityId)
	 * 
	 * @param labCheckEmployeeDataId
	 */
	public void getUploadParam(long labCheckEmployeeDataId) {
		try {
			String requestURL = getRequest().getRequestURL() + "";
			String url = requestURL.replace(getRequest().getServletPath(), "");
			String serviceURL = url + AttachmentServiceCallBack.SERVICE_URL;
			fileArchivingParam = EncryptionUtil.encryptSymmetrically("code=" + ServiceCodesEnum.INFO_SYS.getCode() + "&token=" + EntityNameEnum.LAB_CHECK_EMPLOYEE.getCode() + "_" + labCheckEmployeeDataId + "&serviceurl=" + serviceURL);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			   Log4j.traceErrorException(NationalForcesSample.class, e, "NationalForcesSample");
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
			   Log4j.traceErrorException(NationalForcesSample.class, e, "NationalForcesSample");
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
			   Log4j.traceErrorException(NationalForcesSample.class, e, "NationalForcesSample");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Get attachment list related to the entity id passed to the method then check if there's no records then show message indicating no data else if it's only one record , open the download popup directly , otherwise open a dialogue shows file names and make the user select which one to download then open the download popup with the chosen contentId (attachmentId)
	 * 
	 * @param labCheckEmployeeDataId
	 * @throws BusinessException
	 */
	public void getDownloadedAttachmentsIdList(long labCheckEmployeeDataId) throws BusinessException {
		try {
			attachmentList = AttachmentService.getAttachments(EntityNameEnum.LAB_CHECK_EMPLOYEE.getCode(), labCheckEmployeeDataId);
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
			byte[] bytes = LabCheckService.getEmployeeLabCheckDetailsReportBytes(labCheckEmployeeData.getId(), loginEmpData.getFullName());
			super.print(bytes, "Employee lab check details");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(NationalForcesSample.class, e, "NationalForcesSample");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/********************************************************** Setters And Getters **********************************************/
	public LabCheckEmployeeData getLabCheckEmployeeData() {
		return labCheckEmployeeData;
	}

	public void setLabCheckEmployeeData(LabCheckEmployeeData labCheckEmployeeData) {
		this.labCheckEmployeeData = labCheckEmployeeData;
	}

	public LabCheck getLabCheck() {
		return labCheck;
	}

	public void setLabCheck(LabCheck labCheck) {
		this.labCheck = labCheck;
	}

	public List<LabCheckEmployeeData> getPerviousLabCheckEmployeeDataList() {
		return perviousLabCheckEmployeeDataList;
	}

	public void setPerviousLabCheckEmployeeDataList(List<LabCheckEmployeeData> perviousLabCheckEmployeeDataList) {
		this.perviousLabCheckEmployeeDataList = perviousLabCheckEmployeeDataList;
	}

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public Integer getCasesReason() {
		return LabCheckReasonsEnum.CASES.getCode();
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

	public Integer getCheatingInCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.CHEATING.getCode();
	}

	public List<SetupDomain> getDomainMetrialTypeList() {
		return domainMetrialTypeList;
	}

	public void setDomainMetrialTypeList(List<SetupDomain> domainMetrialTypeList) {
		this.domainMetrialTypeList = domainMetrialTypeList;
	}

	public List<SetupDomain> getDomainCurmentHospitalsList() {
		return domainCurmentHospitalsList;
	}

	public void setDomainCurmentHospitalsList(List<SetupDomain> domainCurmentHospitalsList) {
		this.domainCurmentHospitalsList = domainCurmentHospitalsList;
	}

	public Integer getRetestMonthNumber() {
		return retestMonthNumber;
	}

	public void setRetestMonthNumber(Integer retestMonthNumber) {
		this.retestMonthNumber = retestMonthNumber;
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

	public boolean isDeletePrivilage() {
		return deletePrivilage;
	}

	public void setDeletePrivilage(boolean deletePrivilage) {
		this.deletePrivilage = deletePrivilage;
	}

}
