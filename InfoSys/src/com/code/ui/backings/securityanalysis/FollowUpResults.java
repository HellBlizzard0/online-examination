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
import com.code.enums.NavigationEnum;
import com.code.enums.SecurityAnalysisClassesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.attach.AttachmentService;
import com.code.services.infosys.securityanalysis.FollowUpService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.SetupService;
import com.code.ui.backings.base.BaseBacking;
import com.code.ui.util.EncryptionUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "followUpResults")
@ViewScoped
public class FollowUpResults extends BaseBacking implements Serializable {
	private int rowsCount = 10;
	private int page = 1;
	private FollowUpData followUpData;
	private FollowUpData selectedFollowUpData;
	private List<FollowUpResultData> followUpResultData, filteredFollowUpResultData;
	private FollowUpResultData selectedFollowUpResultData;
	private List<SetupDomain> followUpResultTypesList;
	private List<RegionData> followUpResultRegionList;
	private List<SetupDomain> followUpTransTypesList;
	private String downloadFileParamId;
	private List<Attachment> attachmentList;
	private boolean openDownloadPopupFlag;
	private boolean openDownloadDialogueFlag;
	private String boolServerDownloadPath;

	/**
	 * Constructor
	 */
	public FollowUpResults() {
		super();
		try {
			if (getRequest().getAttribute("followUp") != null) {
				followUpData = (FollowUpData) getRequest().getAttribute("followUp");
				followUpResultTypesList = SetupService.getDomains(SecurityAnalysisClassesEnum.FOLLOW_RESULTS.getCode());
				followUpResultRegionList = FollowUpService.getAllRegionData();
				followUpTransTypesList = SetupService.getDomains(SecurityAnalysisClassesEnum.TRANS_TYPES.getCode());
				selectedFollowUpData = FollowUpService.getFollowUpDataById(followUpData.getId());
				followUpResultData = FollowUpService.getFollowUpResultsByFollowUpId(followUpData.getId());
			}
		} catch (Exception e) {
			selectedFollowUpData = new FollowUpData();
			followUpResultData = new ArrayList<FollowUpResultData>();
		}
	}

	/**
	 * Search for information
	 */
	public void searchInfo() {
		try {
			followUpResultData.clear();
			followUpResultData = FollowUpService.getFollowUpResultsByFollowUpId(followUpData.getId());

		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	public void getDownloadParam(String attachmentId) {
		try {
			downloadFileParamId = EncryptionUtil.encryptSymmetrically("contentId=" + attachmentId);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(FollowUpResults.class, e, "FollowUpResults");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Get attachment list related to the entity id passed to the method then check if there's no records then show message indicating no daya else if it's only one record , open the download popup directly , otherwise open a dialogue shows file names and make the user select which one to download then open the download popup with the choosen contentId (attachmentId)
	 * 
	 * @param
	 * @throws BusinessException
	 */
	public void getDownloadedAttachmentsIdList(long resultId) throws BusinessException {
		try {
			attachmentList = AttachmentService.getAttachments(EntityNameEnum.FOLLOW_UP_RESULT.getCode(), resultId);
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

	public String editFollowUpDetails(FollowUpResultData followUpResultData) {
		getRequest().setAttribute("followUpResId", followUpResultData.getId());
		getRequest().setAttribute("mode", 1);
		return NavigationEnum.FOLLOW_UP_RESULT_DETAILS.toString();
	}

	public String viewFollowUpDetails(FollowUpResultData followUpResultData) {
		getRequest().setAttribute("followUpResId", followUpResultData.getId());
		getRequest().setAttribute("mode", 2);
		return NavigationEnum.FOLLOW_UP_RESULT_DETAILS.toString();
	}

	// setters and getters
	public int getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public List<FollowUpResultData> getFollowUpResultData() {
		return followUpResultData;
	}

	public void setFollowUpResultData(List<FollowUpResultData> followUpResultData) {
		this.followUpResultData = followUpResultData;
	}

	public FollowUpData getFollowUpData() {
		return followUpData;
	}

	public void setFollowUpData(FollowUpData followUpData) {
		this.followUpData = followUpData;
	}

	public FollowUpData getSelectedFollowUpData() {
		return selectedFollowUpData;
	}

	public void setSelectedFollowUpData(FollowUpData selectedFollowUpData) {
		this.selectedFollowUpData = selectedFollowUpData;
	}

	public FollowUpResultData getSelectedFollowUpResultData() {
		return selectedFollowUpResultData;
	}

	public void setSelectedFollowUpResultData(FollowUpResultData selectedFollowUpResultData) {
		this.selectedFollowUpResultData = selectedFollowUpResultData;
	}

	public List<FollowUpResultData> getFilteredFollowUpResultData() {
		return filteredFollowUpResultData;
	}

	public void setFilteredFollowUpResultData(List<FollowUpResultData> filteredFollowUpResultData) {
		this.filteredFollowUpResultData = filteredFollowUpResultData;
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