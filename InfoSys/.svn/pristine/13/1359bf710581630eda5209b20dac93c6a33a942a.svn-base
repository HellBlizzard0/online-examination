package com.code.ui.backings.securityanalysisreports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.securityanalysis.RegionData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.SecurityAnalysisClassesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.securityanalysis.ConversationService;
import com.code.services.infosys.securityanalysis.FollowUpService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.SetupService;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "referralConversationReport")
@ViewScoped
public class ReferrallConversationReport extends BaseBacking implements Serializable {
	private Long followUpId;
	private String followUpCode;
	private List<Long> selectedRegionList;
	private Long employeeId;
	private String employeeName;
	private Long referralType;
	private Date fromDate;
	private Date toDate;
	private List<SetupDomain> referralDomainList;
	private List<RegionData> regionsList;

	/**
	 * Default Constructor
	 */
	public ReferrallConversationReport() {
		try {
			referralDomainList = SetupService.getSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.REFERRAL_TYPE.getCode());
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
		followUpId = null;
		followUpCode = null;
		selectedRegionList = new ArrayList<Long>();
		employeeId = null;
		employeeName = null;
		referralType = null;
		fromDate = null;
		toDate = null;
	}

	/**
	 * Print report
	 */
	public void printReport() {
		try {
			if (selectedRegionList.size() == 0) {
				throw new BusinessException("error_mandatoryFields", new Object[] { getParameterizedMessage("label_region", "ar") });
			}
			if (fromDate == null)
				throw new BusinessException("error_mandatoryFields", new Object[] { getParameterizedMessage("label_fromDate", "ar") });
			if (toDate == null)
				throw new BusinessException("error_mandatoryFields", new Object[] { getParameterizedMessage("label_toDate", "ar") });
			byte[] bytes = ConversationService.getReferralConversationReportBytes(followUpId, selectedRegionList, employeeId, referralType, HijriDateService.getHijriDateString(fromDate), HijriDateService.getHijriDateString(toDate), loginEmpData.getFullName());
			super.print(bytes, "Referral Conversation Report");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(ReferrallConversationReport.class, e, "ReferrallConversationReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
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

	public List<Long> getSelectedRegionList() {
		return selectedRegionList;
	}

	public void setSelectedRegionList(List<Long> selectedRegionList) {
		this.selectedRegionList = selectedRegionList;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Long getReferralType() {
		return referralType;
	}

	public void setReferralType(Long referralType) {
		this.referralType = referralType;
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

	public List<SetupDomain> getReferralDomainList() {
		return referralDomainList;
	}

	public void setReferralDomainList(List<SetupDomain> referralDomainList) {
		this.referralDomainList = referralDomainList;
	}

	public List<RegionData> getRegionsList() {
		return regionsList;
	}

	public void setRegionsList(List<RegionData> regionsList) {
		this.regionsList = regionsList;
	}
}