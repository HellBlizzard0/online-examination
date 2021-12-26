package com.code.ui.backings.securityanalysis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import com.code.dal.orm.securityanalysis.FollowUpData;
import com.code.dal.orm.securityanalysis.NetworkData;
import com.code.dal.orm.securityanalysis.RegionData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.GhostTypeEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.SecurityAnalysisClassesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.securityanalysis.FollowUpService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.SetupService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "directFollowUps")
@ViewScoped
public class DirectFollowUps extends BaseBacking implements Serializable {
	private int rowsCount = 10;
	private List<FollowUpData> directFollowUpList;
	private List<FollowUpData> filteredDirectFollowUpList;
	private List<SelectItem> caseTypesList;
	private List<SelectItem> regionsList;
	private List<SelectItem> networkList;

	/**
	 * Constructor
	 * 
	 */
	public DirectFollowUps() {
		init();
		try {
			List<RegionData> allRegions = FollowUpService.getAllRegionData();
			List<SetupDomain> allcases = SetupService.getSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.CASES_TYPES.getCode());
			List<NetworkData> allnetworks = FollowUpService.getAllNetworks();

			regionsList = new ArrayList<SelectItem>();
			for (RegionData regionData : allRegions) {
				regionsList.add(new SelectItem(regionData.getRegionName(), regionData.getRegionName()));
			}

			caseTypesList = new ArrayList<SelectItem>();
			for (SetupDomain setupDomain : allcases) {
				caseTypesList.add(new SelectItem(setupDomain.getDescription(), setupDomain.getDescription()));
			}

			networkList = new ArrayList<SelectItem>();
			for (NetworkData networkData : allnetworks) {
				networkList.add(new SelectItem(networkData.getNetworkNumber(), networkData.getNetworkNumber()));
			}

		} catch (BusinessException e) {
			e.printStackTrace();
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(DirectFollowUps.class, e, "DirectFollowUps");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }

	}

	/**
	 * Initialize Search Parameters
	 */
	public void init() {
		searchFollowUpList();
		filteredDirectFollowUpList = directFollowUpList;
	}

	/**
	 * Search for FollowUp List
	 */
	public void searchFollowUpList() {
		try {
			setDirectFollowUpList(FollowUpService.getDirectFollowUps());
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(DirectFollowUps.class, e, "DirectFollowUps");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
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
	
	public void print(){
		byte[] bytes;
		try {
			List<Long> filteredFollowUpsIds = new ArrayList<Long>();
			for(FollowUpData filteredFollowUp : filteredDirectFollowUpList){
				filteredFollowUpsIds.add(filteredFollowUp.getId());
			}
			bytes = FollowUpService.getDirectFollowsUpsReportBytes(filteredFollowUpsIds , loginEmpData.getFullName());
			super.print(bytes, "DirectFollowUps Ghosts Report");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(DirectFollowUps.class, e, "DirectFollowUps");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}
	/**
	 * View FollowUpData
	 * 
	 * @return page to be directed to
	 */
	public String editDirectFollowUp(Long followUpId) {
		getRequest().setAttribute("followUpId", followUpId);
		return NavigationEnum.EDIT_FOLLOW_UP.toString();
	}

	public List<String> getRegoins() {
		ArrayList<String> ret = new ArrayList<String>();
		ret.add("test");
		return ret;
	}

	public List<String> allCaseTypes() {
		ArrayList<String> ret = new ArrayList<String>();
		ret.add("test");
		return ret;
	}

	// setters and getters
	public int getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}

	public List<FollowUpData> getDirectFollowUpList() {
		return directFollowUpList;
	}

	public void setDirectFollowUpList(List<FollowUpData> directFollowUpList) {
		this.directFollowUpList = directFollowUpList;
	}

	public List<FollowUpData> getFilteredDirectFollowUpList() {
		return filteredDirectFollowUpList;
	}

	public void setFilteredDirectFollowUpList(List<FollowUpData> filteredDirectFollowUpList) {
		this.filteredDirectFollowUpList = filteredDirectFollowUpList;
	}

	public int getEnumEmployee() {
		return GhostTypeEnum.EMPLOYEE.getCode();
	}

	public int getEnumNonEmployee() {
		return GhostTypeEnum.NON_EMPLOYEE.getCode();
	}

	public List<SelectItem> getCaseTypesList() {
		return caseTypesList;
	}

	public void setCaseTypesList(List<SelectItem> caseTypesList) {
		this.caseTypesList = caseTypesList;
	}

	public List<SelectItem> getRegionsList() {
		return regionsList;
	}

	public void setRegionsList(List<SelectItem> regionsList) {
		this.regionsList = regionsList;
	}

	public List<SelectItem> getNetworkList() {
		return networkList;
	}

	public void setNetworkList(List<SelectItem> networkList) {
		this.networkList = networkList;
	}

}