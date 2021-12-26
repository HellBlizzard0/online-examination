package com.code.ui.backings.securityanalysisreports;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.securityanalysis.RegionData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.FlagsEnum;
import com.code.enums.SecurityAnalysisClassesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.NoDataException;
import com.code.services.infosys.securityanalysis.FollowUpService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.SetupService;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "referralGhostsInDuration")
@ViewScoped
public class ReferralGhostsInDuration extends BaseBacking implements Serializable {
	private List<RegionData> regionList;
	private List<SetupDomain> allcases, allReferrals;

	private Long regionId, caseTypeId, networkId, referralId;
	private String referralPlaceDesc;
	private Date fromDate, toDate;

	/**
	 * Default Constructor
	 */
	public ReferralGhostsInDuration() {
		try {
			regionList = FollowUpService.getAllRegionData();
			allcases = SetupService.getSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.CASES_TYPES.getCode());
			allReferrals = SetupService.getSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.EXTERNAL_SIDES.getCode());
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
		this.regionId = (long) FlagsEnum.ALL.getCode();
	}

	public void resetFormParameters() throws BusinessException {
		this.regionId = (long) FlagsEnum.ALL.getCode();
		this.caseTypeId = null;
		this.networkId = null;
		this.referralId = null;
		this.fromDate = null;
		this.toDate = null;
	}

	/**
	 * print Car Rentals
	 */
	public void print() {
		if (fromDate == null || toDate == null) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_mandatory"));
			return;
		}
		if (toDate.before(fromDate)) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_fromDateBeforeToDate"));
			return;
		}

		try {
			String regionName = "";
			if (!regionId.equals((long) FlagsEnum.ALL.getCode())) {
				regionName = getRegionName();
			}

			String networkName = "";
			try {
				if (networkId != null) {
					networkName = FollowUpService.getNetworkDataById(networkId).getDescription();
				}
			} catch (NoDataException e) {
				e.printStackTrace();
			}
			String caseTypeDesc = "";
			if (caseTypeId != null) {
				for (int i = 0; i < allcases.size(); i++) {
					if (caseTypeId.equals(allcases.get(i).getId())) {
						caseTypeDesc = allcases.get(i).getDescription();
						break;
					}
				}
			}
			byte[] bytes = FollowUpService.getReferralGhostInDurationReportBytes(regionId, regionName, caseTypeId, caseTypeDesc, networkId, networkName, referralId, referralPlaceDesc, HijriDateService.getHijriDateString(fromDate), HijriDateService.getHijriDateString(toDate), loginEmpData.getFullName());
			super.print(bytes, "Referral Ghost In Duration Report");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(ReferralGhostsInDuration.class, e, "ReferralGhostsInDuration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * printStatistics
	 */
	public void printStatistics() {
		if (fromDate == null || toDate == null) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_mandatory"));
			return;
		}
		if (toDate.before(fromDate)) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_fromDateBeforeToDate"));
			return;
		}
		try {
			String regionName = "";
			if (!regionId.equals((long) FlagsEnum.ALL.getCode())) {
				regionName = getRegionName();
			}

			String networkName = "";
			try {
				if (networkId != null) {
					networkName = FollowUpService.getNetworkDataById(networkId).getDescription();
				}
			} catch (NoDataException e) {
				e.printStackTrace();
			}
			String caseTypeDesc = "";
			if (caseTypeId != null) {
				for (int i = 0; i < allcases.size(); i++) {
					if (caseTypeId.equals(allcases.get(i).getId())) {
						caseTypeDesc = allcases.get(i).getDescription();
						break;
					}
				}
			}
			System.out.println(referralId + "  " + referralPlaceDesc);
			byte[] bytes = FollowUpService.getReferralGhostInDurationStatisticsReportBytes(regionId, regionName, caseTypeId, caseTypeDesc, networkId, networkName, referralId, referralPlaceDesc, HijriDateService.getHijriDateString(fromDate), HijriDateService.getHijriDateString(toDate), loginEmpData.getFullName());
			super.print(bytes, "Referral Ghost In Duration Statisitics Report");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(ReferralGhostsInDuration.class, e, "ReferralGhostsInDuration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Get region name
	 * 
	 * @return regionName
	 */
	public String getRegionName() {
		for (RegionData dep : regionList) {
			if (dep.getId().equals(regionId)) {
				return dep.getRegionName();
			}
		}
		return null;
	}

	public List<RegionData> getRegionList() {
		return regionList;
	}

	public void setRegionList(List<RegionData> regionList) {
		this.regionList = regionList;
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public List<SetupDomain> getAllcases() {
		return allcases;
	}

	public void setAllcases(List<SetupDomain> allcases) {
		this.allcases = allcases;
	}

	public Long getCaseTypeId() {
		return caseTypeId;
	}

	public void setCaseTypeId(Long caseTypeId) {
		this.caseTypeId = caseTypeId;
	}

	public Long getNetworkId() {
		return networkId;
	}

	public void setNetworkId(Long networkId) {
		this.networkId = networkId;
	}

	public Long getReferralId() {
		return referralId;
	}

	public void setReferralId(Long referralId) {
		this.referralId = referralId;
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

	public List<SetupDomain> getAllReferrals() {
		return allReferrals;
	}

	public void setAllReferrals(List<SetupDomain> allReferrals) {
		this.allReferrals = allReferrals;
	}

	public String getReferralPlaceDesc() {
		return referralPlaceDesc;
	}

	public void setReferralPlaceDesc(String referralPlaceDesc) {
		this.referralPlaceDesc = referralPlaceDesc;
	}

}