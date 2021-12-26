package com.code.ui.backings.securityanalysis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.securityanalysis.NetworkData;
import com.code.dal.orm.securityanalysis.RegionData;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.securityanalysis.FollowUpService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "networkSearch")
@ViewScoped
public class NetworkSearch extends BaseBacking implements Serializable {

	private List<NetworkData> networkList;
	private List<RegionData> regionsList;
	private Long selectedRegionId;
	private String selectedName;
	private String selectedContactNum;
	private Long selectedFollowUpId;
	private String selectedNetworkNumber;
	private String selectedFollowUpCode;
	private Long selectedNetworkId;
	private String selectedSocialId;

	/**
	 * Constructor
	 */
	public NetworkSearch() {
		super();
		init();
	}

	/**
	 * Initialize/Reset search variables
	 */
	public void init() {
		try {
			networkList = new ArrayList<NetworkData>();
			regionsList = FollowUpService.getAllRegionData();

		} catch (BusinessException e) {
		}
	}

	public void clearSearch() {
		networkList = new ArrayList<NetworkData>();
		selectedRegionId = null;
		selectedName = null;
		selectedContactNum = null;
		selectedFollowUpId = null;
		selectedNetworkId = null;
		selectedSocialId = null;
	}

	/**
	 * Search Class
	 */
	public void searchNetworksList() {
		networkList.clear();
		try {
			if ((selectedRegionId == null) && (selectedName == null || selectedName.trim().isEmpty()) && selectedNetworkId == null && selectedFollowUpId == null && (selectedContactNum == null || selectedContactNum.trim().isEmpty()) && (selectedSocialId == null || selectedSocialId.trim().isEmpty()) && (selectedNetworkId == null)) {
				throw new BusinessException("error_oneFieldMandatory");
			}

			networkList = FollowUpService.getFollowUpNetworks(selectedRegionId, selectedName, selectedSocialId, selectedNetworkId, selectedFollowUpId, selectedContactNum);
			if (networkList.isEmpty()) {
				if ((selectedContactNum != null && !selectedContactNum.isEmpty()))
					selectedContactNum = selectedContactNum.substring(3);
				this.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
			}
		} catch (BusinessException e) {
			if ((selectedContactNum != null && !selectedContactNum.isEmpty()))
				selectedContactNum = selectedContactNum.substring(3);
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	public List<NetworkData> getNetworkList() {
		return networkList;
	}

	public void setNetworkList(List<NetworkData> networkList) {
		this.networkList = networkList;
	}

	public List<RegionData> getRegionsList() {
		return regionsList;
	}

	public void setRegionsList(List<RegionData> regionsList) {
		this.regionsList = regionsList;
	}

	public Long getSelectedRegionId() {
		return selectedRegionId;
	}

	public void setSelectedRegionId(Long selectedRegionId) {
		this.selectedRegionId = selectedRegionId;
	}

	public String getSelectedName() {
		return selectedName;
	}

	public void setSelectedName(String selectedName) {
		this.selectedName = selectedName;
	}

	public String getSelectedContactNum() {
		return selectedContactNum;
	}

	public void setSelectedContactNum(String selectedContactNum) {
		this.selectedContactNum = selectedContactNum;
	}

	public Long getSelectedFollowUpId() {
		return selectedFollowUpId;
	}

	public void setSelectedFollowUpId(Long selectedFollowUpId) {
		this.selectedFollowUpId = selectedFollowUpId;
	}

	public String getSelectedNetworkNumber() {
		return selectedNetworkNumber;
	}

	public void setSelectedNetworkNumber(String selectedNetworkNumber) {
		this.selectedNetworkNumber = selectedNetworkNumber;
	}

	public String getSelectedFollowUpCode() {
		return selectedFollowUpCode;
	}

	public void setSelectedFollowUpCode(String selectedFollowUpCode) {
		this.selectedFollowUpCode = selectedFollowUpCode;
	}

	public Long getSelectedNetworkId() {
		return selectedNetworkId;
	}

	public void setSelectedNetworkId(Long selectedNetworkId) {
		this.selectedNetworkId = selectedNetworkId;
	}

	public String getSelectedSocialId() {
		return selectedSocialId;
	}

	public void setSelectedSocialId(String selectedSocialId) {
		this.selectedSocialId = selectedSocialId;
	}
}