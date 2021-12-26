package com.code.ui.backings.minisearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import com.code.dal.orm.securityanalysis.ConversationData;
import com.code.dal.orm.securityanalysis.FollowUpData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ConversationResultsEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.SecurityAnalysisClassesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.NoDataException;
import com.code.services.infosys.securityanalysis.FollowUpService;
import com.code.services.setup.SetupService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "followUpMiniSearch")
@ViewScoped
public class FollowUpMiniSearch extends BaseBacking implements Serializable {
	private int rowsCount = 10;
	private Boolean isActive;
	private List<FollowUpData> followUpDataList;
	private FollowUpData followUpData;
	private ConversationData conversationData;
	private List<SetupDomain> casesTypes;
	private List<SetupDomain> followUpResultsList;
	private Long followUpResultChoiceId;
	private Date fromDateResult;
	private Date toDateResult;
	private Date fromDateConversation;
	private Date toDateConversation;
	private Integer directFlag;
	private Long contactNumber;

	public FollowUpMiniSearch() {
		super();
		init();
		casesTypes = new ArrayList<>();
		followUpResultsList = new ArrayList<>();
		if (!getRequest().getParameter("isActive").equals("null") && !getRequest().getParameter("isActive").isEmpty() && !getRequest().getParameter("isActive").equals("undefined")) {
			isActive = Boolean.parseBoolean(getRequest().getParameter("isActive"));
		} else {
			isActive = null;
		}

		try {
			setCasesTypes(SetupService.getSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.CASES_TYPES.getCode()));
			setFollowUpResultsList(SetupService.getSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.FOLLOW_RESULTS.getCode()));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Initialize/Reset Variables
	 */
	public void init() {
		super.init();
		followUpData = new FollowUpData();
		directFlag = -1;
		followUpDataList = new ArrayList<FollowUpData>();
		conversationData = new ConversationData();
		followUpResultChoiceId = null;
		followUpData.setGhostType(1);
		followUpData.setDomainIdCasesType(-1L);
		followUpData.setDomainIdEquipmentType(-1L);
		conversationData.setConversationType(-1);
		conversationData.setConversationResult(-1);
		contactNumber = null;
		toDateConversation = null;
		toDateResult = null;
		fromDateConversation = null;
		fromDateResult = null;

	}

	public void searchFollowUps() throws NoDataException {
		try {
			if ((followUpData.getContactNumber() != null && !followUpData.getContactNumber().isEmpty()) || (followUpData.getCode() != null && !followUpData.getCode().isEmpty()) || (followUpData.getNetworkRoleDesc() != null && !followUpData.getNetworkRoleDesc().isEmpty()) || (followUpData.getEmployeeName() != null && !followUpData.getEmployeeName().isEmpty()) || (followUpData.getEmployeeSocialId() != null && !followUpData.getEmployeeSocialId().isEmpty())
					|| (followUpData.getFollowUpStartDate() != null) || (followUpData.getFollowUpEndDate() != null) || (followUpData.getDomainIdCasesType() != null && !followUpData.getDomainIdCasesType().equals(-1L)) || (!directFlag.equals(-1)) || (fromDateConversation != null) || (toDateConversation != null)
					|| (conversationData.getConversationType() != null && !conversationData.getConversationType().equals(-1)) || (conversationData.getConversationResult() != null && !conversationData.getConversationResult().equals(-1)) || (conversationData.getCoordinateLongitude() != null) || (conversationData.getCoordinateLatitude() != null) || (followUpResultChoiceId != null) || (contactNumber != null && !contactNumber.equals(-1L)) || (fromDateResult != null) || (toDateResult != null))
				this.followUpDataList = FollowUpService.advancedSearchFollowUps(followUpData.getContactNumber(), followUpData.getCode(), followUpData.getGhostType() == 1 ? followUpData.getEmployeeName() : followUpData.getNonEmployeeName(), followUpData.getGhostType() == 1 ? followUpData.getEmployeeSocialId() : followUpData.getNonEmployeeSocialId() , null, null, followUpData.getFollowUpStartDate(), followUpData.getFollowUpEndDate(), null,
						followUpData.getDomainIdCasesType(), directFlag, conversationData.getConversationType(), conversationData.getConversationResult(), fromDateConversation, toDateConversation, conversationData.getCoordinateLongitude(), conversationData.getCoordinateLatitude(), contactNumber, conversationData.getConversationDetails() , followUpResultChoiceId, fromDateResult, toDateResult, FlagsEnum.ON.getCode(), followUpData.getNetworkRoleDesc());
			else
				throw new BusinessException("error_oneFieldMandatory");

			if (followUpDataList.isEmpty()) {
				this.setServerSideSuccessMessages(getParameterizedMessage("error_noData"));
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage()));

		}
	}

	public void selectFollowUp(FollowUpData followUpData) {
		RequestContext.getCurrentInstance().closeDialog(followUpData);
	}

	// setters and getters
	public int getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public List<FollowUpData> getFollowUpDataList() {
		return followUpDataList;
	}

	public void setFollowUpDataList(List<FollowUpData> followUpDataList) {
		this.followUpDataList = followUpDataList;
	}

	public FollowUpData getFollowUpData() {
		return followUpData;
	}

	public void setFollowUpData(FollowUpData followUpData) {
		this.followUpData = followUpData;
	}

	public ConversationData getConversationData() {
		return conversationData;
	}

	public void setConversationData(ConversationData conversationData) {
		this.conversationData = conversationData;
	}

	public List<SetupDomain> getCasesTypes() {
		return casesTypes;
	}

	public void setCasesTypes(List<SetupDomain> casesTypes) {
		this.casesTypes = casesTypes;
	}

	public List<SetupDomain> getFollowUpResultsList() {
		return followUpResultsList;
	}

	public void setFollowUpResultsList(List<SetupDomain> followUpResultsList) {
		this.followUpResultsList = followUpResultsList;
	}

	public Long getFollowUpResultChoiceId() {
		return followUpResultChoiceId;
	}

	public void setFollowUpResultChoiceId(Long followUpResultChoiceId) {
		this.followUpResultChoiceId = followUpResultChoiceId;
	}

	public Date getFromDateConversation() {
		return fromDateConversation;
	}

	public void setFromDateConversation(Date fromDateConversation) {
		this.fromDateConversation = fromDateConversation;
	}

	public Date getToDateConversation() {
		return toDateConversation;
	}

	public void setToDateConversation(Date toDateConversation) {
		this.toDateConversation = toDateConversation;
	}

	public Date getFromDateResult() {
		return fromDateResult;
	}

	public void setFromDateResult(Date fromDateResult) {
		this.fromDateResult = fromDateResult;
	}

	public Date getToDateResult() {
		return toDateResult;
	}

	public void setToDateResult(Date toDateResult) {
		this.toDateResult = toDateResult;
	}

	public Integer getDirectFlag() {
		return directFlag;
	}

	public void setDirectFlag(Integer directFlag) {
		if (directFlag != null && directFlag == 1)
			this.followUpData.setDirectFlag(true);
		this.directFlag = directFlag;
	}

	public Long getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(Long contactNumber) {
		this.contactNumber = contactNumber;
	}

	public int getConversationNoResultValue() {
		return ConversationResultsEnum.NO_RESULTS_EXISTS.getCode();
	}

	public int getConversationNegativeValue() {
		return ConversationResultsEnum.NEGATIVE.getCode();
	}

	public int getConversationPositiveValue() {
		return ConversationResultsEnum.POSITIVE.getCode();
	}
}