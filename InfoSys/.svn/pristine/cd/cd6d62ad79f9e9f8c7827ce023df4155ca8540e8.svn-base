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
import com.code.enums.NavigationEnum;
import com.code.exceptions.BusinessException;
import com.code.services.attach.AttachmentService;
import com.code.services.infosys.securityanalysis.LetterService;
import com.code.services.log4j.Log4j;
import com.code.ui.backings.base.BaseBacking;
import com.code.ui.util.EncryptionUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "followUpLetters")
@ViewScoped
public class FollowUpLetters extends BaseBacking implements Serializable {
	private int rowsCount = 10;
	private int pageMode = 1;
	private FollowUpData followUpData;
	private List<LetterData> letterData, filteredLetterData;
	private LetterData selectedLetterData;
	private String downloadFileParamId;
	private List<Attachment> attachmentList;
	private boolean openDownloadPopupFlag;
	private boolean openDownloadDialogueFlag;
	private String boolServerDownloadPath;

	/**
	 * Constructor
	 */
	public FollowUpLetters() {
		super();
		try {
			if (getRequest().getAttribute("followUp") != null) {
				followUpData = (FollowUpData) getRequest().getAttribute("followUp");
				letterData = LetterService.getLettersByFollowUpId(followUpData.getId(), null);
			}
		} catch (Exception e) {
			letterData = new ArrayList<LetterData>();
		}
	}

	/**
	 * Search for information
	 */
	public void searchInfo() {
		try {
			letterData.clear();
			letterData = LetterService.getLettersByFollowUpId(followUpData.getId(), null);

		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	public void getDownloadParam(String attachmentId) {
		try {
			downloadFileParamId = EncryptionUtil.encryptSymmetrically("contentId=" + attachmentId);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			   Log4j.traceErrorException(FollowUpLetters.class, e, "FollowUpLetters");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Get attachment list related to the entity id passed to the method then check if there's no records then show message indicating no daya else if it's only one record , open the download popup directly , otherwise open a dialogue shows file names and make the user select which one to download then open the download popup with the choosen contentId (attachmentId)
	 * 
	 * @param
	 * @throws BusinessException
	 */
	public void getDownloadedAttachmentsIdList(long letterId) throws BusinessException {
		try {
			attachmentList = AttachmentService.getAttachments(EntityNameEnum.LETTER.getCode(), letterId);
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

	public String editFollowUpLetters(LetterData letterData) {
		getRequest().setAttribute("followUpId", letterData.getFollowUpId());
		getRequest().setAttribute("letterNumber", letterData.getLetterNumber());
		getRequest().setAttribute("mode", 1);
		return NavigationEnum.FOLLOW_UP_LETTER_DETAILS.toString();
	}

	public String viewFollowUpLetters(LetterData letterData) {
		getRequest().setAttribute("followUpId", letterData.getFollowUpId());
		getRequest().setAttribute("letterNumber", letterData.getLetterNumber());
		getRequest().setAttribute("mode", 2);
		return NavigationEnum.FOLLOW_UP_LETTER_DETAILS.toString();
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

	public FollowUpData getFollowUpData() {
		return followUpData;
	}

	public void setFollowUpData(FollowUpData followUpData) {
		this.followUpData = followUpData;
	}

	public List<LetterData> getLetterData() {
		return letterData;
	}

	public void setLetterData(List<LetterData> letterData) {
		this.letterData = letterData;
	}

	public List<LetterData> getFilteredLetterData() {
		return filteredLetterData;
	}

	public void setFilteredLetterData(List<LetterData> filteredLetterData) {
		this.filteredLetterData = filteredLetterData;
	}

	public LetterData getSelectedLetterData() {
		return selectedLetterData;
	}

	public void setSelectedLetterData(LetterData selectedLetterData) {
		this.selectedLetterData = selectedLetterData;
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

}