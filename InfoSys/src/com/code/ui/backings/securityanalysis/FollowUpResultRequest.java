package com.code.ui.backings.securityanalysis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.attach.Attachment;
import com.code.dal.orm.securityanalysis.FollowUpData;
import com.code.dal.orm.securityanalysis.FollowUpResultData;
import com.code.dal.orm.securityanalysis.RegionData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.EntityNameEnum;
import com.code.enums.GhostTypeEnum;
import com.code.enums.SecurityAnalysisClassesEnum;
import com.code.enums.ServiceCodesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.attach.AttachmentService;
import com.code.services.attach.AttachmentServiceCallBack;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.securityanalysis.FollowUpService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.SetupService;
import com.code.ui.backings.base.BaseBacking;
import com.code.ui.util.EncryptionUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "followUpResultRequest")
@ViewScoped
public class FollowUpResultRequest extends BaseBacking implements Serializable {
	private FollowUpData followUpData;
	private List<Attachment> attachmentList;
	private String fileArchivingParam;
	private String downloadFileParamId;
	private String selectedDownloadFileId;
	private boolean openDownloadPopupFlag;
	private boolean openDownloadDialogueFlag;
	private String boolServerDownloadPath;
	private String boolServerUploadPath;
	private boolean renderInformation;
	private FollowUpResultData followUpResultData;
	private List<SetupDomain> followUpResultTypesList;
	private List<RegionData> followUpResultRegionList;
	private List<SetupDomain> followUpTransTypesList;
	private Integer pageMode = 1; // 1 for EDIT, 2 for VIEW
	private boolean canEditFollow = true;

	public FollowUpResultRequest() {
		super();
		this.init();
		setBoolServerDownloadPath(InfoSysConfigurationService.getBoolServerDownloadPath());
		setBoolServerUploadPath(InfoSysConfigurationService.getBoolServerUploadPath());
	}

	public void init() {
		try {
			if (getRequest().getAttribute("mode") != null && getRequest().getAttribute("mode") != null) {
				this.pageMode = (Integer) getRequest().getAttribute("mode");
				if ((Integer) getRequest().getAttribute("mode") == 1) {
					canEditFollow = false;
				}
			}
			if (getRequest().getAttribute("followUpResId") != null) {
				followUpResultData = FollowUpService.getFollowUpResultsById((Long) getRequest().getAttribute("followUpResId")).get(0);
				followUpData = FollowUpService.getFollowUpDataById(followUpResultData.getFollowUpId());
				renderInformation = true;
			} else {
				followUpData = new FollowUpData();
				followUpData.setId(-1L);
				followUpResultData = new FollowUpResultData();
				renderInformation = false;
			}
			attachmentList = new ArrayList<Attachment>();
			if (this.pageMode != null && this.pageMode == 1) {
				followUpResultTypesList = SetupService.getAllSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.FOLLOW_RESULTS.getCode());
				followUpTransTypesList = SetupService.getAllSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.TRANS_TYPES.getCode());
			} else {
				followUpResultTypesList = SetupService.getSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.FOLLOW_RESULTS.getCode());
				followUpTransTypesList = SetupService.getSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.TRANS_TYPES.getCode());
			}
			followUpResultRegionList = FollowUpService.getAllRegionData();
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(FollowUpResultRequest.class, e, "FollowUpResultRequest");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void saveResult() {
		try {
			if (followUpResultData.getResultDate() != null && followUpResultData.getResultDate().before(followUpData.getFollowUpStartDate())) {
				throw new BusinessException("error_resultDateBeforeFollowUpDate");
			}
			followUpResultData.setFollowUpId(followUpData.getId());
			FollowUpService.saveFollowUpResult(followUpResultData);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
			getRequest().setAttribute("followUpId", followUpResultData.getFollowUpId());
			getRequest().setAttribute("followUpResId", followUpResultData.getId());
			init();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage()));
		}catch (Exception e) {
			   Log4j.traceErrorException(FollowUpResultRequest.class, e, "FollowUpResultRequest");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void selectFollowUp() {
		try {
			followUpData = FollowUpService.getFollowUpDataById(followUpData.getId());
			followUpResultData.setFollowUpId(followUpData.getId());
			renderInformation = true;
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage()));
		}
	}

	public void getDownloadParam(String attachmentId) {
		try {
			downloadFileParamId = EncryptionUtil.encryptSymmetrically("contentId=" + attachmentId);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(FollowUpResultRequest.class, e, "FollowUpResultRequest");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void getDownloadedAttachmentsIdList(long infoId) throws BusinessException {
		attachmentList = AttachmentService.getAttachments(EntityNameEnum.FOLLOW_UP_RESULT.getCode(), infoId);
		openDownloadPopupFlag = false;
		openDownloadDialogueFlag = false;
		if (attachmentList.isEmpty()) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_noAttachments"));
		} else {
			openDownloadDialogueFlag = true;
		}
	}

	public void getUploadParam(long infoId) {
		try {
			String requestURL = getRequest().getRequestURL() + "";
			String url = requestURL.replace(getRequest().getServletPath(), "");
			String serviceURL = url + AttachmentServiceCallBack.SERVICE_URL;

			fileArchivingParam = EncryptionUtil.encryptSymmetrically("code=" + ServiceCodesEnum.INFO_SYS.getCode() + "&token=" + EntityNameEnum.FOLLOW_UP_RESULT.getCode() + "_" + infoId + "&serviceurl=" + serviceURL);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			   Log4j.traceErrorException(FollowUpResultRequest.class, e, "FollowUpResultRequest");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/******** GETTERS & SETTERS *********/

	public FollowUpData getFollowUpData() {
		return followUpData;
	}

	public void setFollowUpData(FollowUpData followUpData) {
		this.followUpData = followUpData;
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

	public String getDownloadFileParamId() {
		return downloadFileParamId;
	}

	public void setDownloadFileParamId(String downloadFileParamId) {
		this.downloadFileParamId = downloadFileParamId;
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

	public String getBoolServerUploadPath() {
		return boolServerUploadPath;
	}

	public void setBoolServerUploadPath(String boolServerUploadPath) {
		this.boolServerUploadPath = boolServerUploadPath;
	}

	public boolean isRenderInformation() {
		return renderInformation;
	}

	public void setRenderInformation(boolean renderInformation) {
		this.renderInformation = renderInformation;
	}

	public FollowUpResultData getFollowUpResultData() {
		return followUpResultData;
	}

	public void setFollowUpResultData(FollowUpResultData followUpResultData) {
		this.followUpResultData = followUpResultData;
	}

	public List<SetupDomain> getFollowUpResultTypesList() {
		return followUpResultTypesList;
	}

	public void setFollowUpResultTypesList(List<SetupDomain> followUpResultTypesList) {
		this.followUpResultTypesList = followUpResultTypesList;
	}

	public List<RegionData> getFollowUpResultRegionList() {
		return followUpResultRegionList;
	}

	public void setFollowUpResultRegionList(List<RegionData> followUpResultRegionList) {
		this.followUpResultRegionList = followUpResultRegionList;
	}

	public List<SetupDomain> getFollowUpTransTypesList() {
		return followUpTransTypesList;
	}

	public void setFollowUpTransTypesList(List<SetupDomain> followUpTransTypesList) {
		this.followUpTransTypesList = followUpTransTypesList;
	}

	public Integer getEmployeeType() {
		return GhostTypeEnum.EMPLOYEE.getCode();
	}

	public Integer getNonEmployeeType() {
		return GhostTypeEnum.NON_EMPLOYEE.getCode();
	}

	public Integer getPageMode() {
		return pageMode;
	}

	public void setPageMode(Integer pageMode) {
		this.pageMode = pageMode;
	}

	public boolean isCanEditFollow() {
		return canEditFollow;
	}

	public void setCanEditFollow(boolean canEditFollow) {
		this.canEditFollow = canEditFollow;
	}
}