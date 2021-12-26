package com.code.ui.backings.securityanalysisreports;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.securityanalysis.RegionData;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.securityanalysis.FollowUpService;
import com.code.services.log4j.Log4j;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "extendFollowupNumber")
@ViewScoped
public class ExtendedFollowUpReport extends BaseBacking implements Serializable {
	private Long regionId;
	private String regionName;
	private Long followUpId;
	private String followUpCode;
	private Long networkId;
	private String networkNumber;
	private List<RegionData> regionsList;

	/**
	 * Default Constructor
	 */
	public ExtendedFollowUpReport() {
		try {
			regionsList = FollowUpService.getAllRegionData();
			reset();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Reset report parameter
	 */
	public void reset() {
		regionId = null;
		regionName = null;
		followUpId = null;
		followUpCode = null;
		networkId = null;
		networkNumber = null;
	}

	/**
	 * Print report
	 */
	public void printReport() {
		try {
			if (regionId == null && followUpId == null && networkId == null) {
				throw new BusinessException("error_oneFieldMandatory");
			}
			byte[] bytes = FollowUpService.getExtendedFollowUpReportBytes(regionId, followUpId, networkId, loginEmpData.getFullName());
			super.print(bytes, "Extended FollowUp Report");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(ExtendedFollowUpReport.class, e, "ExtendedFollowUpReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
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

	public Long getNetworkId() {
		return networkId;
	}

	public void setNetworkId(Long networkId) {
		this.networkId = networkId;
	}

	public String getNetworkNumber() {
		return networkNumber;
	}

	public void setNetworkNumber(String networkNumber) {
		this.networkNumber = networkNumber;
	}

	public List<RegionData> getRegionsList() {
		return regionsList;
	}

	public void setRegionsList(List<RegionData> regionsList) {
		this.regionsList = regionsList;
	}

}