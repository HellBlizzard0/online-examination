package com.code.ui.backings.minisearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.securityanalysis.FollowUpData;
import com.code.dal.orm.securityanalysis.FollowUpResultData;
import com.code.dal.orm.securityanalysis.RegionData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.SecurityAnalysisClassesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.securityanalysis.FollowUpService;
import com.code.services.setup.SetupService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "followUpResultMiniSearch")
@ViewScoped
public class FollowUpResultMiniSearch extends BaseBacking implements Serializable {
	private int rowsCount = 10;
	private int page = 1;
	private Long followUpId;
	private FollowUpData selectedFollowUpData;
	private List<FollowUpResultData> followUpResultData, filteredFollowUpResultData;
	private FollowUpResultData selectedFollowUpResultData;
	private List<SetupDomain> followUpResultTypesList;
	private List<RegionData> followUpResultRegionList;
	private List<SetupDomain> followUpTransTypesList;

	/**
	 * Constructor
	 */
	public FollowUpResultMiniSearch() {
		super();
		try {
			if (!getRequest().getParameter("followUpId").equals("null") && !getRequest().getParameter("followUpId").isEmpty() && !getRequest().getParameter("followUpId").equals("undefined")) {
				followUpId = Long.parseLong(getRequest().getParameter("followUpId"));
				followUpResultTypesList = SetupService.getDomains(SecurityAnalysisClassesEnum.FOLLOW_RESULTS.getCode());
				followUpResultRegionList = FollowUpService.getAllRegionData();
				followUpTransTypesList = SetupService.getDomains(SecurityAnalysisClassesEnum.TRANS_TYPES.getCode());
				selectedFollowUpData = FollowUpService.getFollowUpDataById(followUpId);
				followUpResultData = FollowUpService.getFollowUpResultsByFollowUpId(followUpId);
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
			followUpResultData = FollowUpService.getFollowUpResultsByFollowUpId(followUpId);

		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
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

	public Long getFollowUpId() {
		return followUpId;
	}

	public void setFollowUpId(Long followUpId) {
		this.followUpId = followUpId;
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

}