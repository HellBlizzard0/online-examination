package com.code.ui.backings.securityanalysis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.securityanalysis.FollowUpData;
import com.code.dal.orm.securityanalysis.RegionData;
import com.code.dal.orm.setup.DepartmentData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.RegionTypeEnum;
import com.code.enums.SecurityAnalysisClassesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.NoDataException;
import com.code.services.infosys.securityanalysis.FollowUpService;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.SetupService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "followUpManagement")
@ViewScoped
public class FollowUpManagement extends BaseBacking implements Serializable {
	private List<FollowUpData> followUpDataList;
	private String selectedContactNum;
	private String selectedSocialId;
	private List<SetupDomain> equipmentTypes, casesTypeDomainList, followUpResultsList;
	private List<RegionData> regionsList;
	private Double coordinateLatitude, coordinateLongitude;
	private Long selectedRegionId, selectedSectorId, selectedNetworkId, domainIdCasesType, followUpResultChoiceId, contactNumber;
	private Integer directFlag, conversationType, conversationResult;
	private String selectedGhostName, selectedCode, selectedNetworkNumber, conversationDetails;
	private Date followUpStartDate, followUpEndDate, fromDateConversation, toDateConversation, fromDateResult, toDateResult;
	private int rowsCount = 10;
	private List<DepartmentData> sectorsList;
	private boolean showData;

	/**
	 * Constructor
	 */
	public FollowUpManagement() {
		super();
		init();
	}

	/**
	 * Initialize/Reset search variables
	 */
	public void init() {
		try {
			followUpDataList = new ArrayList<FollowUpData>();
			equipmentTypes = SetupService.getSecurityAnalysisDomainsByClassCode(ClassesEnum.EQUIPMENT_TYPES.getCode());
			sectorsList = new ArrayList<DepartmentData>();
			casesTypeDomainList = SetupService.getSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.CASES_TYPES.getCode());
			followUpResultsList = SetupService.getSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.CHAT_ACTIONS.getCode());
			regionsList = FollowUpService.getAllRegionData();
			conversationType = -1;
			coordinateLatitude = null;
			coordinateLongitude = null;
			contactNumber = null;
			selectedNetworkId = null;
			selectedRegionId = -1L;
			selectedSectorId = null;
			followUpResultChoiceId = -1L;
			selectedSocialId = null;
			selectedContactNum = null;
			selectedCode = null;
			selectedGhostName = null;
			followUpStartDate = null;
			followUpEndDate = null;
			fromDateConversation = null;
			toDateConversation = null;
			fromDateResult = null;
			toDateResult = null;
			selectedNetworkNumber = null;
			conversationDetails = null;
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * get Region sectors by RegionId
	 */
	public void getRegionSectors() {
		try {
			RegionData region = getRegionType(selectedRegionId);
			if (selectedRegionId != null && region != null) {
				sectorsList = DepartmentService.getDepartmentsByRegionIdAndType(region.getRegionId(), null, null, DepartmentTypeEnum.SECTOR.getCode());
				if (sectorsList.size() > 0) {
					selectedSectorId = sectorsList.get(0).getId();
				}
			} else {
				sectorsList = new ArrayList<DepartmentData>();
				selectedSectorId = null;
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	public void searchFollowUps() throws NoDataException {
		try {
			this.followUpDataList = FollowUpService.advancedSearchFollowUps(selectedContactNum, selectedCode, selectedGhostName, selectedSocialId, selectedRegionId, selectedSectorId, followUpStartDate, followUpEndDate, selectedNetworkId, domainIdCasesType, directFlag, conversationType, conversationResult, fromDateConversation, toDateConversation, coordinateLongitude, coordinateLatitude, contactNumber, conversationDetails, followUpResultChoiceId, fromDateResult, toDateResult,
					FlagsEnum.ALL.getCode(), null);
		} catch (BusinessException e) {
			if ((selectedContactNum != null && !selectedContactNum.isEmpty()))
				selectedContactNum = selectedContactNum.substring(3);
			if (contactNumber != null) {
				contactNumber = Long.parseLong(String.valueOf(contactNumber).substring(3));
			}
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage()));
			return;
		}

		if (followUpDataList.isEmpty()) {
			if ((selectedContactNum != null && !selectedContactNum.isEmpty()))
				selectedContactNum = selectedContactNum.substring(3);
			if (contactNumber != null) {
				String contactNum = String.valueOf(contactNumber).substring(3);
				contactNumber = Long.parseLong(contactNum);
			}
			this.setServerSideSuccessMessages(getParameterizedMessage("error_noData"));
		}
	}

	/**
	 * Check internal region or external region
	 * 
	 * @param regionId
	 * @return
	 */
	private RegionData getRegionType(Long regionId) {
		for (RegionData region : regionsList) {
			if (region.getId().equals(regionId)) {
				if (region.getRegionType() == RegionTypeEnum.INTERNAL.getCode())
					return region;
				else
					return null;
			}
		}
		return null;
	}

	public String editFollowUp(FollowUpData followUpData) {
		getRequest().setAttribute("followUpId", followUpData.getId());
		return NavigationEnum.FOLLOW_UP_EDIT.toString();
	}

	public String viewFollowUpConversations(FollowUpData followUpData) {
		getRequest().setAttribute("followUp", followUpData);
		return NavigationEnum.FOLLOW_UP_CONVERSATIONS.toString();
	}

	public String viewFollowUpResult(FollowUpData followUpData) {
		getRequest().setAttribute("followUp", followUpData);
		return NavigationEnum.FOLLOW_UP_RESULT.toString();
	}

	public String viewFollowUpLetters(FollowUpData followUpData) {
		getRequest().setAttribute("followUp", followUpData);
		return NavigationEnum.FOLLOW_UP_LETTERS.toString();
	}

	public List<FollowUpData> getFollowUpDataList() {
		return followUpDataList;
	}

	public void setFollowUpDataList(List<FollowUpData> followUpDataList) {
		this.followUpDataList = followUpDataList;
	}

	public String getSelectedContactNum() {
		return selectedContactNum;
	}

	public void setSelectedContactNum(String selectedContactNum) {
		this.selectedContactNum = selectedContactNum;
	}

	public String getSelectedSocialId() {
		return selectedSocialId;
	}

	public void setSelectedSocialId(String selectedSocialId) {
		this.selectedSocialId = selectedSocialId;
	}

	public List<SetupDomain> getEquipmentTypes() {
		return equipmentTypes;
	}

	public void setEquipmentTypes(List<SetupDomain> equipmentTypes) {
		this.equipmentTypes = equipmentTypes;
	}

	public List<SetupDomain> getCasesTypeDomainList() {
		return casesTypeDomainList;
	}

	public void setCasesTypeDomainList(List<SetupDomain> casesTypeDomainList) {
		this.casesTypeDomainList = casesTypeDomainList;
	}

	public List<SetupDomain> getFollowUpResultsList() {
		return followUpResultsList;
	}

	public void setFollowUpResultsList(List<SetupDomain> followUpResultsList) {
		this.followUpResultsList = followUpResultsList;
	}

	public List<RegionData> getRegionsList() {
		return regionsList;
	}

	public void setRegionsList(List<RegionData> regionsList) {
		this.regionsList = regionsList;
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

	public Long getSelectedRegionId() {
		return selectedRegionId;
	}

	public void setSelectedRegionId(Long selectedRegionId) {
		this.selectedRegionId = selectedRegionId;
	}

	public Long getSelectedSectorId() {
		return selectedSectorId;
	}

	public void setSelectedSectorId(Long selectedSectorId) {
		this.selectedSectorId = selectedSectorId;
	}

	public Long getSelectedNetworkId() {
		return selectedNetworkId;
	}

	public void setSelectedNetworkId(Long selectedNetworkId) {
		this.selectedNetworkId = selectedNetworkId;
	}

	public Long getDomainIdCasesType() {
		return domainIdCasesType;
	}

	public void setDomainIdCasesType(Long domainIdCasesType) {
		this.domainIdCasesType = domainIdCasesType;
	}

	public Long getFollowUpResultChoiceId() {
		return followUpResultChoiceId;
	}

	public void setFollowUpResultChoiceId(Long followUpResultChoiceId) {
		this.followUpResultChoiceId = followUpResultChoiceId;
	}

	public Long getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(Long contactNumber) {
		this.contactNumber = contactNumber;
	}

	public Integer getDirectFlag() {
		return directFlag;
	}

	public void setDirectFlag(Integer directFlag) {
		this.directFlag = directFlag;
	}

	public Integer getConversationType() {
		return conversationType;
	}

	public void setConversationType(Integer conversationType) {
		this.conversationType = conversationType;
	}

	public Integer getConversationResult() {
		return conversationResult;
	}

	public void setConversationResult(Integer conversationResult) {
		this.conversationResult = conversationResult;
	}

	public String getSelectedGhostName() {
		return selectedGhostName;
	}

	public void setSelectedGhostName(String selectedGhostName) {
		this.selectedGhostName = selectedGhostName;
	}

	public String getSelectedCode() {
		return selectedCode;
	}

	public void setSelectedCode(String selectedCode) {
		this.selectedCode = selectedCode;
	}

	public Date getFollowUpStartDate() {
		return followUpStartDate;
	}

	public void setFollowUpStartDate(Date followUpStartDate) {
		this.followUpStartDate = followUpStartDate;
	}

	public Date getFollowUpEndDate() {
		return followUpEndDate;
	}

	public void setFollowUpEndDate(Date followUpEndDate) {
		this.followUpEndDate = followUpEndDate;
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

	public int getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}

	public List<DepartmentData> getSectorsList() {
		return sectorsList;
	}

	public void setSectorsList(List<DepartmentData> sectorsList) {
		this.sectorsList = sectorsList;
	}

	public boolean isShowData() {
		return showData;
	}

	public void setShowData(boolean showData) {
		this.showData = showData;
	}

	public String getSelectedNetworkNumber() {
		return selectedNetworkNumber;
	}

	public void setSelectedNetworkNumber(String selectedNetworkNumber) {
		this.selectedNetworkNumber = selectedNetworkNumber;
	}

	public String getConversationDetails() {
		return conversationDetails;
	}

	public void setConversationDetails(String conversationDetails) {
		this.conversationDetails = conversationDetails;
	}
}