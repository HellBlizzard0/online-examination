package com.code.ui.backings.minisearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.securityanalysis.Network;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.securityanalysis.FollowUpService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "networkMiniSearch")
@ViewScoped
public class NetworkMiniSearch extends BaseBacking implements Serializable {
	private int rowsCount = 10;
	private int page = 1;
	private String selectedNetworkNumber;
	private String selectedNetworkDescription;
	private Long selectedNetworkId;
	private List<Network> networkDataList;

	/**
	 * Constructor
	 */
	public NetworkMiniSearch() {
		super();
		try {
			networkDataList = new ArrayList<Network>();
		} catch (Exception e) {
		}
	}

	/**
	 * Search for information
	 */
	public void searchNetworks() {
		try {
			if ((selectedNetworkNumber == null || selectedNetworkNumber.trim().isEmpty()) && (selectedNetworkDescription == null || selectedNetworkDescription.trim().isEmpty())) {
				throw new BusinessException("error_oneFieldMandatory");
			}
			networkDataList.clear();
			networkDataList = FollowUpService.getNetworks(selectedNetworkNumber, selectedNetworkDescription);
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
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

	public String getSelectedNetworkNumber() {
		return selectedNetworkNumber;
	}

	public void setSelectedNetworkNumber(String selectedNetworkNumber) {
		this.selectedNetworkNumber = selectedNetworkNumber;
	}

	public String getSelectedNetworkDescription() {
		return selectedNetworkDescription;
	}

	public void setSelectedNetworkDescription(String selectedNetworkDescription) {
		this.selectedNetworkDescription = selectedNetworkDescription;
	}

	public Long getSelectedNetworkId() {
		return selectedNetworkId;
	}

	public void setSelectedNetworkId(Long selectedNetworkId) {
		this.selectedNetworkId = selectedNetworkId;
	}

	public List<Network> getNetworkDataList() {
		return networkDataList;
	}

	public void setNetworkDataList(List<Network> networkDataList) {
		this.networkDataList = networkDataList;
	}
}