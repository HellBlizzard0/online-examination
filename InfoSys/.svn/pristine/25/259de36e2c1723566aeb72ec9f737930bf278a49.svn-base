package com.code.ui.backings.securityanalysis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.attach.Attachment;
import com.code.dal.orm.securityanalysis.FollowUpData;
import com.code.dal.orm.securityanalysis.LetterData;
import com.code.enums.EntityNameEnum;
import com.code.enums.GhostTypeEnum;
import com.code.enums.ServiceCodesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.attach.AttachmentService;
import com.code.services.attach.AttachmentServiceCallBack;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.securityanalysis.FollowUpService;
import com.code.services.infosys.securityanalysis.LetterService;
import com.code.services.log4j.Log4j;
import com.code.ui.backings.base.BaseBacking;
import com.code.ui.util.EncryptionUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "letterRegisteration")
@ViewScoped
public class LetterRegisteration extends BaseBacking implements Serializable {
	private LetterData letterData;
	private List<LetterData> oldLetters;
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
	private Integer pageMode = 1; // 1 for EDIT, 2 for VIEW
	private boolean canEditFollow = true;

	public LetterRegisteration() {
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

			if (getRequest().getAttribute("followUpId") != null && getRequest().getAttribute("letterNumber") != null) {
				letterData = LetterService.getLetters((Long) getRequest().getAttribute("followUpId"), (String) getRequest().getAttribute("letterNumber")).get(0);
				followUpData = FollowUpService.getFollowUpDataById(letterData.getFollowUpId());
				renderInformation = true;
			} else {
				letterData = new LetterData();
				followUpData = new FollowUpData();
				followUpData.setId(-1L);
				renderInformation = false;
			}
			oldLetters = new ArrayList<LetterData>();
			attachmentList = new ArrayList<Attachment>();
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(LetterRegisteration.class, e, "LetterRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void saveLetter() {
		try {
			LetterService.saveLetter(letterData);
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return;
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		getRequest().setAttribute("followUpId", letterData.getFollowUpId());
		getRequest().setAttribute("letterNumber", letterData.getLetterNumber());
		init();
	}

	public void selectFollowUp() {
		try {
			followUpData = FollowUpService.getFollowUpDataById(followUpData.getId());
			letterData.setFollowUpId(followUpData.getId());
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
		} catch (Exception e) {
			   Log4j.traceErrorException(LetterRegisteration.class, e, "LetterRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void getDownloadedAttachmentsIdList(long infoId) throws BusinessException {
		attachmentList = AttachmentService.getAttachments(EntityNameEnum.INFO.getCode(), infoId);
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

			fileArchivingParam = EncryptionUtil.encryptSymmetrically("code=" + ServiceCodesEnum.INFO_SYS.getCode() + "&token=" + EntityNameEnum.INFO.getCode() + "_" + infoId + "&serviceurl=" + serviceURL);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			   Log4j.traceErrorException(LetterRegisteration.class, e, "LetterRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/******** GETTERS & SETTERS *********/

	public LetterData getLetterData() {
		return letterData;
	}

	public void setLetterData(LetterData letterData) {
		this.letterData = letterData;
	}

	public List<LetterData> getOldLetters() {
		return oldLetters;
	}

	public void setOldLetters(List<LetterData> oldLetters) {
		this.oldLetters = oldLetters;
	}

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