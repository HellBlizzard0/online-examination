package com.code.ui.backings.minisearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.securityanalysis.ConversationData;
import com.code.services.infosys.securityanalysis.ConversationService;
import com.code.services.infosys.securityanalysis.FollowUpService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "conversationMiniSearch")
@ViewScoped
public class ConversationMiniSearch extends BaseBacking implements Serializable {
	private int rowsCount = 10;
	private int page = 1;
	private Long followUpId, selectedConversationPartyContactNum;
	private String followUpCode;
	private Integer selectedConversationType, selectedConversationResult;
	private Double coordinateLatitude, coordinateLongitude;
	private Date fromDate, toDate;
	private List<ConversationData> conversationDataList;

	/**
	 * Constructor
	 */
	public ConversationMiniSearch() {
		super();
		try {
			if (!getRequest().getParameter("followUpId").equals("null") && !getRequest().getParameter("followUpId").isEmpty() && !getRequest().getParameter("followUpId").equals("undefined")) {
				followUpId = Long.parseLong(getRequest().getParameter("followUpId"));
				followUpCode = FollowUpService.getFollowUpDataById(followUpId).getCode();
			}
			conversationDataList = new ArrayList<ConversationData>();
		} catch (Exception e) {
		}
	}

	/**
	 * Search for information
	 */
	public void searchConversations() {
		try {
			conversationDataList.clear();
			if (selectedConversationPartyContactNum == null) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_mandatory"));
				return;
			}
			conversationDataList = ConversationService.getConversationDataList(followUpId, selectedConversationType, selectedConversationResult, coordinateLatitude, coordinateLongitude, fromDate, toDate, selectedConversationPartyContactNum);
		} catch (Exception e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage()));
		}
	}

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

	public Long getFollowUpId() {
		return followUpId;
	}

	public void setFollowUpId(Long followUpId) {
		this.followUpId = followUpId;
	}

	public String getFollowUpCode() {
		return followUpCode;
	}

	public void setFollowUpCode(String followUpCode) {
		this.followUpCode = followUpCode;
	}

	public Integer getSelectedConversationType() {
		return selectedConversationType;
	}

	public void setSelectedConversationType(Integer selectedConversationType) {
		this.selectedConversationType = selectedConversationType;
	}

	public Integer getSelectedConversationResult() {
		return selectedConversationResult;
	}

	public void setSelectedConversationResult(Integer selectedConversationResult) {
		this.selectedConversationResult = selectedConversationResult;
	}

	public Double getCoordinateLatitude() {
		return coordinateLatitude;
	}

	public void setCoordinateLatitude(Double coordinateLatitude) {
		this.coordinateLatitude = coordinateLatitude;
	}

	public Double getCoordinateLongitude() {
		return coordinateLongitude;
	}

	public void setCoordinateLongitude(Double coordinateLongitude) {
		this.coordinateLongitude = coordinateLongitude;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Long getSelectedConversationPartyContactNum() {
		return selectedConversationPartyContactNum;
	}

	public void setSelectedConversationPartyContactNum(Long selectedConversationPartyContactNum) {
		this.selectedConversationPartyContactNum = selectedConversationPartyContactNum;
	}

	public List<ConversationData> getConversationDataList() {
		return conversationDataList;
	}

	public void setConversationDataList(List<ConversationData> conversationDataList) {
		this.conversationDataList = conversationDataList;
	}

}