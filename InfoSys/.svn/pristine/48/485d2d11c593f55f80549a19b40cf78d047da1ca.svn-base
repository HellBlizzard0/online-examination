package com.code.ui.backings.minisearch;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.attach.Attachment;
import com.code.dal.orm.security.UserMenuActionData;
import com.code.dal.orm.securityanalysis.FollowUpData;
import com.code.dal.orm.securityanalysis.LetterData;
import com.code.enums.EntityNameEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.UserMenuActionsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.attach.AttachmentService;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.securityanalysis.FollowUpService;
import com.code.services.infosys.securityanalysis.LetterService;
import com.code.services.log4j.Log4j;
import com.code.services.security.SecurityService;
import com.code.ui.backings.base.BaseBacking;
import com.code.ui.util.EncryptionUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "letterMiniSearch")
@ViewScoped
public class LetterMiniSearch extends BaseBacking implements Serializable {
	private int rowsCount = 10;
	private String screenTitle;
	private Long followUpId;
	private FollowUpData followUpData;
	private List<LetterData> lettersList;
	private String fileArchivingParam;
	private String downloadFileParamId;
	private List<Attachment> attachmentList;
	private boolean openDownloadPopupFlag;
	private boolean openDownloadDialogueFlag;
	private String boolServerDownloadPath;
	private boolean deletePrivilage = false;

	public LetterMiniSearch() {
		if (!getRequest().getParameter("followUpId").equals("null") && !getRequest().getParameter("followUpId").isEmpty() && !getRequest().getParameter("followUpId").equals("undefined")) {
			followUpId = Long.parseLong(getRequest().getParameter("followUpId"));
		}
		try {
			UserMenuActionData deleteAttachmentAction = SecurityService.getAction(loginEmpData.getEmpId(), UserMenuActionsEnum.DELETE_ATTACHMENT_PERMISSION.getCode(), FlagsEnum.ALL.getCode());
			if (deleteAttachmentAction != null) {
				setDeletePrivilage(true);
			}
			followUpData = FollowUpService.getFollowUpDataById(followUpId);
			screenTitle = getParameterizedMessage("title_letterMiniSearch", new Object[] { followUpData.getCode(), followUpData.getFollowUpStartDateString(), followUpData.getFollowUpEndDateString() });
			lettersList = LetterService.getLettersByFollowUpId(followUpId, null);
			boolServerDownloadPath = InfoSysConfigurationService.getBoolServerDownloadPath();

		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));

		}

	}

	public void getDownloadParam(String attachmentId) {
		try {
			downloadFileParamId = EncryptionUtil.encryptSymmetrically("contentId=" + attachmentId);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(LetterMiniSearch.class, e, "LetterMiniSearch");
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
			   Log4j.traceErrorException(LetterMiniSearch.class, e, "LetterMiniSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void getDownloadedAttachmentsIdList(long missionGuardId) throws BusinessException {
		try {
			attachmentList = AttachmentService.getAttachments(EntityNameEnum.LETTER.getCode(), followUpId);
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
	 * Initialize/Reset Variables
	 */
	public void init() {

	}

	// setters and getters
	public int getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}

	public String getScreenTitle() {
		return screenTitle;
	}

	public void setScreenTitle(String screenTitle) {
		this.screenTitle = screenTitle;
	}

	public FollowUpData getFollowUpData() {
		return followUpData;
	}

	public void setFollowUpData(FollowUpData followUpData) {
		this.followUpData = followUpData;
	}

	public List<LetterData> getLettersList() {
		return lettersList;
	}

	public void setLettersList(List<LetterData> lettersList) {
		this.lettersList = lettersList;
	}

	public Long getFollowUpId() {
		return followUpId;
	}

	public void setFollowUpId(Long followUpId) {
		this.followUpId = followUpId;
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

	public String getBoolServerDownloadPath() {
		return boolServerDownloadPath;
	}

	public void setBoolServerDownloadPath(String boolServerDownloadPath) {
		this.boolServerDownloadPath = boolServerDownloadPath;
	}

	public boolean isDeletePrivilage() {
		return deletePrivilage;
	}

	public void setDeletePrivilage(boolean deletePrivilage) {
		this.deletePrivilage = deletePrivilage;
	}

}