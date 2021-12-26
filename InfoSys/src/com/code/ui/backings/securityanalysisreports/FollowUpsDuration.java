package com.code.ui.backings.securityanalysisreports;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.securityanalysis.RegionData;
import com.code.enums.FlagsEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.NoDataException;
import com.code.services.infosys.securityanalysis.FollowUpService;
import com.code.services.log4j.Log4j;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "followUpsDuration")
@ViewScoped
public class FollowUpsDuration extends BaseBacking implements Serializable {
	private List<RegionData> regionList;
	private Long regionId, followUpId, networkId;
	private String ghostName;
	private Date fromDate, toDate;

	/**
	 * Default Constructor
	 */
	public FollowUpsDuration() {
		try {
			regionList = FollowUpService.getAllRegionData();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
		this.regionId = (long) FlagsEnum.ALL.getCode();
	}

	public void resetFormParameters() throws BusinessException {
		this.regionId = (long) FlagsEnum.ALL.getCode();
		this.followUpId = null;
		this.networkId = null;
		this.ghostName = "";
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
			String followUpCode = "";
			if (followUpId != null) {
				followUpCode = FollowUpService.getFollowUpDataById(followUpId).getCode();
			}
			byte[] bytes = FollowUpService.getFollowUpInDurationReportBytes(regionId, regionName, followUpId, followUpCode, networkId, networkName, ghostName, HijriDateService.getHijriDateString(fromDate), HijriDateService.getHijriDateString(toDate), loginEmpData.getFullName());
			super.print(bytes, "Follow Up In Duration Report");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(FollowUpsDuration.class, e, "FollowUpsDuration");
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
			String followUpCode = "";
			if (followUpId != null) {
				followUpCode = FollowUpService.getFollowUpDataById(followUpId).getCode();
			}
			byte[] bytes = FollowUpService.getFollowUpInDurationStatisiticsReportBytes(regionId, regionName, followUpId, followUpCode, networkId, networkName, ghostName, HijriDateService.getHijriDateString(fromDate), HijriDateService.getHijriDateString(toDate), loginEmpData.getFullName());
			super.print(bytes, "Follow Up In Duration Statisitics Report");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(FollowUpsDuration.class, e, "FollowUpsDuration");
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

	public Long getFollowUpId() {
		return followUpId;
	}

	public void setFollowUpId(Long followUpId) {
		this.followUpId = followUpId;
	}

	public Long getNetworkId() {
		return networkId;
	}

	public void setNetworkId(Long networkId) {
		this.networkId = networkId;
	}

	public String getGhostName() {
		return ghostName;
	}

	public void setGhostName(String ghostName) {
		this.ghostName = ghostName;
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
}