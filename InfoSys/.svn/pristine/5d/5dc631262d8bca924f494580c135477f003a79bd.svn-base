package com.code.ui.backings.securityanalysis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import com.code.dal.orm.securityanalysis.FollowUpData;
import com.code.dal.orm.securityanalysis.RegionData;
import com.code.enums.FollowUpStatusEnum;
import com.code.enums.NavigationEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.securityanalysis.FollowUpService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "followUpActionRequest")
@ViewScoped
public class FollowUpActionRequest extends BaseBacking implements Serializable {
	private int rowsCount = 10;
	private int page = 1;
	private Date searchedDateFrom, searchedDateTo;
	private List<FollowUpData> followUpDataList;
	private List<FollowUpData> filteredFollowUpDataList;
	private List<SelectItem> followUpResultRegionList;
	private List<SelectItem> statusTypesList;

	/**
	 * Constructor
	 */
	public FollowUpActionRequest() {
		super();
		try {
			followUpDataList = new ArrayList<FollowUpData>();
			statusTypesList = new ArrayList<SelectItem>();
			statusTypesList.add(new SelectItem(FollowUpStatusEnum.ACTIVE.getCode(), getParameterizedMessage("status_active")));
			statusTypesList.add(new SelectItem(FollowUpStatusEnum.SUSPENDED.getCode(), getParameterizedMessage("status_suspended")));
			statusTypesList.add(new SelectItem(FollowUpStatusEnum.FINISHED.getCode(), getParameterizedMessage("label_finished")));
			followUpResultRegionList = new ArrayList<SelectItem>();
			List<RegionData> allRegions = FollowUpService.getAllRegionData();
			followUpResultRegionList = new ArrayList<SelectItem>();
			for (RegionData regionData : allRegions) {
				followUpResultRegionList.add(new SelectItem(regionData.getRegionName(), regionData.getRegionName()));
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Search for information
	 */
	public void searchByDate() {
		try {
			followUpDataList.clear();
			followUpDataList = FollowUpService.getNearEndFollowUpsByEndDate(searchedDateFrom, searchedDateTo);

		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	public String viewFollowUpTakeAction(FollowUpData followUpData) {
		getRequest().setAttribute("mode", followUpData);
		return NavigationEnum.FOLLOW_UP_TAKE_ACTION.toString();
	}

	/**
	 * View FollowUpData
	 * 
	 * @return page to be directed to
	 */
	public String viewDirectFollowUp(Long followUpId) {
		getRequest().setAttribute("followUpId", followUpId);
		return NavigationEnum.FOLLOW_UP_DETAILS.toString();
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

	public List<FollowUpData> getFollowUpDataList() {
		return followUpDataList;
	}

	public void setFollowUpDataList(List<FollowUpData> followUpDataList) {
		this.followUpDataList = followUpDataList;
	}

	public List<SelectItem> getFollowUpResultRegionList() {
		return followUpResultRegionList;
	}

	public void setFollowUpResultRegionList(List<SelectItem> followUpResultRegionList) {
		this.followUpResultRegionList = followUpResultRegionList;
	}

	public List<SelectItem> getStatusTypesList() {
		return statusTypesList;
	}

	public void setStatusTypesList(List<SelectItem> statusTypesList) {
		this.statusTypesList = statusTypesList;
	}

	public List<FollowUpData> getFilteredFollowUpDataList() {
		return filteredFollowUpDataList;
	}

	public void setFilteredFollowUpDataList(List<FollowUpData> filteredFollowUpDataList) {
		this.filteredFollowUpDataList = filteredFollowUpDataList;
	}

	public Date getSearchedDateFrom() {
		return searchedDateFrom;
	}

	public void setSearchedDateFrom(Date searchedDateFrom) {
		this.searchedDateFrom = searchedDateFrom;
	}

	public Date getSearchedDateTo() {
		return searchedDateTo;
	}

	public void setSearchedDateTo(Date searchedDateTo) {
		this.searchedDateTo = searchedDateTo;
	}
}