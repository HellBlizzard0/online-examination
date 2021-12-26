package com.code.ui.backings.securityanalysis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.securityanalysis.FollowUpData;
import com.code.dal.orm.securityanalysis.NetworkData;
import com.code.dal.orm.securityanalysis.RegionData;
import com.code.dal.orm.setup.DepartmentData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.EntityNameEnum;
import com.code.enums.FollowUpStatusEnum;
import com.code.enums.GhostCategoryEnum;
import com.code.enums.GhostTypeEnum;
import com.code.enums.RegionTypeEnum;
import com.code.enums.SecurityAnalysisClassesEnum;
import com.code.enums.ServiceCodesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.attach.AttachmentServiceCallBack;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.securityanalysis.FollowUpService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.SetupService;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;
import com.code.ui.util.EncryptionUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "followUpReg")
@ViewScoped
public class FollowUpRegistration extends BaseBacking implements Serializable {
	private FollowUpData followUp;
	private FollowUpData lastFollowUp;
	private NetworkData newNetwork;
	private String searchCode = "";
	private String searchContactNumber = "";
	private List<RegionData> regionsList;
	private List<DepartmentData> sectorsList;
	private List<SetupDomain> equipmentDomainList;
	private List<SetupDomain> dataSourceDomainList;
	private List<SetupDomain> networkRoleDomainList;
	private List<SetupDomain> montrReasonDomainList;
	private List<SetupDomain> casesTypeDomainList;
	private boolean isSave;
	private boolean isCodeEmpty;
	private String fileArchivingParam;
	private String boolServerUploadPath;

	/**
	 * Constructor
	 */
	public FollowUpRegistration() {
		super();
		init();
		followUpInitilize();
	}

	/**
	 * followUpInitilize
	 */
	public void followUpInitilize() {
		try {
			boolServerUploadPath = InfoSysConfigurationService.getBoolServerUploadPath();
			newNetwork = new NetworkData();
			equipmentDomainList = SetupService.getSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.EQUIPMENT_TYPES.getCode());
			dataSourceDomainList = SetupService.getSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.DATA_SOURCES.getCode());
			networkRoleDomainList = SetupService.getSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.NETWORK_ROLES.getCode());
			montrReasonDomainList = SetupService.getSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.MONTR_REASONS.getCode());
			casesTypeDomainList = SetupService.getSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.CASES_TYPES.getCode());
			regionsList = FollowUpService.getAllRegionData();

			isSave = false;
			isCodeEmpty = true;
			if (getRequest().getAttribute("followUpId") != null) {
				followUp = FollowUpService.getFollowUpDataById((Long) getRequest().getAttribute("followUpId"));
				getSelectedSector();
				if (followUp.getCode() != null && !followUp.getCode().isEmpty()) {
					isCodeEmpty = false;
				}
			} else {
				followUp = new FollowUpData();
			}
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(FollowUpRegistration.class, e, "FollowUpRegistration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }

	}

	public void searchFollowUps() {
		try {
			isSave = false;
			followUp = new FollowUpData();
			if ((searchContactNumber == null || searchContactNumber.trim().isEmpty()) && (searchCode == null || searchCode.trim().isEmpty())) {
				throw new BusinessException("error_mandatoryFields", new Object[] { getParameterizedMessage("label_contactNumberOrCodeNumber", "ar") });
			}
			if (searchContactNumber != null && searchContactNumber.length() > 15) {
				throw new BusinessException("error_contactNumberValidation");
			}
			lastFollowUp = FollowUpService.getLastFollowUpDataByContactAndCode(searchContactNumber, searchCode);
			if (!(searchContactNumber.trim().isEmpty()) && !(searchCode.trim().isEmpty())) {
				if (lastFollowUp == null) {
					lastFollowUp = FollowUpService.getLastFollowUpDataByContactAndCode(searchContactNumber, null);
					if (lastFollowUp != null) {
						throw new BusinessException("error_noData");
					}
					lastFollowUp = FollowUpService.getLastFollowUpDataByContactAndCode(null, searchCode);
					if (lastFollowUp != null) {
						throw new BusinessException("error_noData");
					}
				}
			} else if (!(searchCode.trim().isEmpty()) && searchContactNumber.trim().isEmpty()) {
				lastFollowUp = FollowUpService.getLastFollowUpDataByContactAndCode(null, searchCode);
				if (lastFollowUp == null) {
					throw new BusinessException("error_noData");
				}
			}
			if (lastFollowUp != null) {
				if (lastFollowUp.getStatus() != FollowUpStatusEnum.ACTIVE.getCode()) {
					followUp.setContactNumber(lastFollowUp.getContactNumber());
					followUp.setCode(lastFollowUp.getCode());
					followUp.setDomainIdEquipmentType(lastFollowUp.getDomainIdEquipmentType());
					followUp.setEquipmentTypeDesc(lastFollowUp.getEquipmentTypeDesc());
					followUp.setOwnerName(lastFollowUp.getOwnerName());
					followUp.setOwnerSocialId(lastFollowUp.getOwnerSocialId());
					followUp.setGhostType(lastFollowUp.getGhostType());
					followUp.setAliasName(lastFollowUp.getAliasName());
					if (lastFollowUp.getGhostType() == GhostTypeEnum.EMPLOYEE.getCode()) {
						followUp.setEmployeeId(lastFollowUp.getEmployeeId());
						followUp.setEmployeeName(lastFollowUp.getEmployeeName());
						followUp.setEmployeeSocialId(lastFollowUp.getEmployeeSocialId());
					} else {
						followUp.setNonEmployeeName(lastFollowUp.getNonEmployeeName());
						followUp.setNonEmployeeSocialId(lastFollowUp.getNonEmployeeSocialId());
					}

					if (lastFollowUp.getCode() != null && !lastFollowUp.getCode().isEmpty())
						isCodeEmpty = false;
					this.setServerSideWarnMessages(getParameterizedMessage("notify_reFollowUp"));
				} else {
					searchCode = "";
					searchContactNumber = "";
					this.setServerSideErrorMessages(getParameterizedMessage("error_activeFollowUpExist"));
					return;
				}
			} else {
				followUp.setContactNumber(searchContactNumber);
				followUp.setCode(searchCode);
				followUp.setGhostType(GhostTypeEnum.EMPLOYEE.getCode());
				followUp.setGhostCategory(GhostCategoryEnum.CIVIL.getCode());
				isCodeEmpty = true;
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(FollowUpRegistration.class, e, "FollowUpRegistration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * get selected sector
	 */
	public void getSelectedSector() {
		try {
			RegionData region = getRegionType(followUp.getRegionId());
			if (followUp.getRegionId() != null && region != null) {
				sectorsList = DepartmentService.getDepartmentsByRegionIdAndType(region.getRegionId(), null, null, DepartmentTypeEnum.SECTOR.getCode());
			} else {
				sectorsList = new ArrayList<DepartmentData>();
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(FollowUpRegistration.class, e, "FollowUpRegistration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * get Region sectors by RegionId
	 */
	public void getRegionSectors() {
		try {
			followUp.setSectorId(null);
			RegionData region = getRegionType(followUp.getRegionId());
			if (followUp.getRegionId() != null && region != null) {
				sectorsList = DepartmentService.getDepartmentsByRegionIdAndType(region.getRegionId(), null, null, DepartmentTypeEnum.SECTOR.getCode());
			} else {
				sectorsList = new ArrayList<DepartmentData>();
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(FollowUpRegistration.class, e, "FollowUpRegistration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Calculate EndDate
	 */
	public void calculateEndDate() {
		try {
			if (followUp.getFollowUpStartDate() != null && followUp.getFollowUpPeriod() != null && followUp.getFollowUpPeriod() > 0)
				followUp.setFollowUpEndDate(HijriDateService.addSubHijriDays(followUp.getFollowUpStartDate(), followUp.getFollowUpPeriod()));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(FollowUpRegistration.class, e, "FollowUpRegistration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
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

	/**
	 * Open Network Dialog to add new Network
	 */
	public void openNetworkDialog() {
		newNetwork = new NetworkData();
	}

	/**
	 * Add New Network
	 */
	public void saveNetwork() {
		try {
			if (newNetwork.getDescription() == null || newNetwork.getDescription().trim().isEmpty())
				throw new BusinessException("error_mandatoryFields", new Object[] { getParameterizedMessage("label_followUpNetworkDesc", "ar") });
			isSave = true;
			FollowUpService.saveNetwork(newNetwork);
			followUp.setFollowUpNetworkId(newNetwork.getId());
			followUp.setFollowUpNetworkNum(newNetwork.getNetworkNumber());
			followUp.setFollowUpNetworkDesc(newNetwork.getDescription());
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			newNetwork = new NetworkData();
			isSave = false;
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Save or Update FollowUp
	 * 
	 * @return
	 */
	public void saveFollowUp() {
		try {
			if (followUp.getGhostType() == GhostTypeEnum.EMPLOYEE.getCode()) {
				followUp.setNonEmployeeName(null);
				followUp.setNonEmployeeSocialId(null);
			} else {
				followUp.setEmployeeId(null);
				followUp.setEmployeeName(null);
				followUp.setEmployeeSocialId(null);
			}
			if (followUp.getId() == null) {
				followUp.setActiveFlag(true);
				followUp.setStatus(FollowUpStatusEnum.ACTIVE.getCode());
				FollowUpService.saveFollowUp(followUp);
			} else {
				FollowUpService.updateFollowUp(followUp);
			}
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return;
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		getRequest().setAttribute("followUpId", followUp.getId());
		followUpInitilize();
	}

	/**
	 * Prepare Upload Parameters
	 * 
	 * @param followUpId
	 */
	public void getUploadParam(long followUpId) {
		try {
			String requestURL = getRequest().getRequestURL() + "";
			String url = requestURL.replace(getRequest().getServletPath(), "");
			String serviceURL = url + AttachmentServiceCallBack.SERVICE_URL;

			fileArchivingParam = EncryptionUtil.encryptSymmetrically("code=" + ServiceCodesEnum.INFO_SYS.getCode() + "&token=" + EntityNameEnum.FOLLOW_UP.getCode() + "_" + followUpId + "&serviceurl=" + serviceURL);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			   Log4j.traceErrorException(FollowUpRegistration.class, e, "FollowUpRegistration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public FollowUpData getFollowUp() {
		return followUp;
	}

	public void setFollowUp(FollowUpData followUp) {
		this.followUp = followUp;
	}

	public FollowUpData getLastFollowUp() {
		return lastFollowUp;
	}

	public void setLastFollowUp(FollowUpData lastFollowUp) {
		this.lastFollowUp = lastFollowUp;
	}

	public NetworkData getNewNetwork() {
		return newNetwork;
	}

	public void setNewNetwork(NetworkData newNetwork) {
		this.newNetwork = newNetwork;
	}

	public String getSearchCode() {
		return searchCode;
	}

	public void setSearchCode(String searchCode) {
		this.searchCode = searchCode;
	}

	public String getSearchContactNumber() {
		return searchContactNumber;
	}

	public void setSearchContactNumber(String searchContactNumber) {
		this.searchContactNumber = searchContactNumber;
	}

	public List<RegionData> getRegionsList() {
		return regionsList;
	}

	public void setRegionsList(List<RegionData> regionsList) {
		this.regionsList = regionsList;
	}

	public List<DepartmentData> getSectorsList() {
		return sectorsList;
	}

	public void setSectorsList(List<DepartmentData> sectorsList) {
		this.sectorsList = sectorsList;
	}

	public List<SetupDomain> getEquipmentDomainList() {
		return equipmentDomainList;
	}

	public void setEquipmentDomainList(List<SetupDomain> equipmentDomainList) {
		this.equipmentDomainList = equipmentDomainList;
	}

	public List<SetupDomain> getDataSourceDomainList() {
		return dataSourceDomainList;
	}

	public void setDataSourceDomainList(List<SetupDomain> dataSourceDomainList) {
		this.dataSourceDomainList = dataSourceDomainList;
	}

	public List<SetupDomain> getNetworkRoleDomainList() {
		return networkRoleDomainList;
	}

	public void setNetworkRoleDomainList(List<SetupDomain> networkRoleDomainList) {
		this.networkRoleDomainList = networkRoleDomainList;
	}

	public List<SetupDomain> getMontrReasonDomainList() {
		return montrReasonDomainList;
	}

	public void setMontrReasonDomainList(List<SetupDomain> montrReasonDomainList) {
		this.montrReasonDomainList = montrReasonDomainList;
	}

	public List<SetupDomain> getCasesTypeDomainList() {
		return casesTypeDomainList;
	}

	public void setCasesTypeDomainList(List<SetupDomain> casesTypeDomainList) {
		this.casesTypeDomainList = casesTypeDomainList;
	}

	public boolean isSave() {
		return isSave;
	}

	public void setSave(boolean isSave) {
		this.isSave = isSave;
	}

	public boolean isCodeEmpty() {
		return isCodeEmpty;
	}

	public void setCodeEmpty(boolean isCodeEmpty) {
		this.isCodeEmpty = isCodeEmpty;
	}

	public String getFileArchivingParam() {
		return fileArchivingParam;
	}

	public void setFileArchivingParam(String fileArchivingParam) {
		this.fileArchivingParam = fileArchivingParam;
	}

	public String getBoolServerUploadPath() {
		return boolServerUploadPath;
	}

	public void setBoolServerUploadPath(String boolServerUploadPath) {
		this.boolServerUploadPath = boolServerUploadPath;
	}

	public Integer getEmployeeType() {
		return GhostTypeEnum.EMPLOYEE.getCode();
	}

	public Integer getNonEmployeeType() {
		return GhostTypeEnum.NON_EMPLOYEE.getCode();
	}

	public Integer getGhostCivilCategory() {
		return GhostCategoryEnum.CIVIL.getCode();
	}

	public Integer getGhostMilitaryCategory() {
		return GhostCategoryEnum.MILITARY.getCode();
	}
}
