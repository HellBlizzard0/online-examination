package com.code.ui.backings.minisearch;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ReferralTypeValuesEnum;
import com.code.enums.SecurityAnalysisClassesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.setup.SetupService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "insertReferralMiniSearch")
@ViewScoped
public class InsertReferralMiniSearch extends BaseBacking implements Serializable {
	private List<SetupDomain> processTypeList;
	private Long employeeId;
	private String employeeName;
	private String referralDetails;
	private Long referralProcessId;
	private String referralProcessDesc;
	private Boolean viewReferralSpecialist;

	public InsertReferralMiniSearch() {
		super();
		try {
			processTypeList = SetupService.getSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.REFERRAL_TYPE.getCode());
			referralProcessId = processTypeList.get(0).getId();
			referralProcessDesc = processTypeList.get(0).getDescription();
			viewReferralSpecialist = true;
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}

	}

	/**
	 * change conversation result view Conversation Summary in Positive Case
	 */
	public void viewSpecialistEmployee() {
		if (referralProcessId != null) {

			for (SetupDomain domain : processTypeList) {
				if (domain.getId().equals(referralProcessId)) {
					referralProcessDesc = domain.getDescription();
					break;
				}
			}

			if (referralProcessDesc.equals(ReferralTypeValuesEnum.REDIRECT.getCode())) {
				viewReferralSpecialist = false;
				employeeId = null;
				employeeName = null;
			} else {
				viewReferralSpecialist = true;
			}
		} else {
			referralProcessDesc = null;
			viewReferralSpecialist = false;
		}

	}

	public List<SetupDomain> getProcessTypeList() {
		return processTypeList;
	}

	public void setProcessTypeList(List<SetupDomain> processTypeList) {
		this.processTypeList = processTypeList;
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

	public String getReferralDetails() {
		return referralDetails;
	}

	public void setReferralDetails(String referralDetails) {
		this.referralDetails = referralDetails;
	}

	public Long getReferralProcessId() {
		return referralProcessId;
	}

	public void setReferralProcessId(Long referralProcessId) {
		this.referralProcessId = referralProcessId;
	}

	public Boolean getViewReferralSpecialist() {
		return viewReferralSpecialist;
	}

	public void setViewReferralSpecialist(Boolean viewReferralSpecialist) {
		this.viewReferralSpecialist = viewReferralSpecialist;
	}

	public String getReferralProcessDesc() {
		return referralProcessDesc;
	}

	public void setReferralProcessDesc(String referralProcessDesc) {
		this.referralProcessDesc = referralProcessDesc;
	}
}
