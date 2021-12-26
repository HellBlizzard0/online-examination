package com.code.ui.backings.securityanalysis;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import com.code.dal.orm.securityanalysis.DecisionData;
import com.code.dal.orm.securityanalysis.FollowUpData;
import com.code.dal.orm.securityanalysis.LetterData;
import com.code.dal.orm.securitymission.PenaltyArrestData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.FollowUpDecisionTypeEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.SecurityAnalysisClassesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.securityanalysis.FollowUpService;
import com.code.services.setup.SetupService;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "followUpTakeAction")
@ViewScoped
public class FollowUpTakeAction extends BaseBacking implements Serializable {
	private LetterData letterData;
	private FollowUpData followUpData;
	private List<SetupDomain> stopReasonList;
	private DecisionData decisionData;
	private Date followUpEndDate;
	private String followUpEndDateStr;

	public FollowUpTakeAction() {
		super();
		followUpData = new FollowUpData();
		if (getRequest().getAttribute("mode") != null) {
			followUpData = (FollowUpData) getRequest().getAttribute("mode");
		}
		this.init();

	}

	public void init() {
		try {
			decisionData = new DecisionData();
			decisionData.setDecisionType(FollowUpDecisionTypeEnum.FOLLOW_UP_END.getCode());
			decisionData.setFollowUpId(followUpData.getId());
			decisionData.setReferralDate(HijriDateService.getHijriSysDate());
			followUpEndDate = followUpData.getFollowUpEndDate();
			followUpEndDateStr = followUpData.getFollowUpEndDateString();
			stopReasonList = SetupService.getSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.STOP_REASONS.getCode());
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * reset data on change event
	 */
	public void clearData() {
		if (decisionData.getDecisionType() != null && decisionData.getDecisionType().equals(FollowUpDecisionTypeEnum.FOLLOW_UP_EXTEND.getCode())) {
			decisionData.setDomainIdStopReasons(null);
			decisionData.setResumeDate(null);
		} else if (decisionData.getDecisionType() != null && decisionData.getDecisionType().equals(FollowUpDecisionTypeEnum.FOLLOW_UP_END.getCode())) {
			decisionData.setExtensionDuration(null);
		}
		followUpEndDate = followUpData.getFollowUpEndDate();
		decisionData.setDomainIdExternalSidesDesc(null);
		decisionData.setDomainIdExternalSides(null);
		decisionData.setEmployeeName(null);
		followUpEndDateStr = followUpData.getFollowUpEndDateString();
	}

	/**
	 * Calculate FollowUpEndDate Period
	 * 
	 * @param event
	 */
	public void dateChangedListener(AjaxBehaviorEvent event) {
		calculateEndDate();
	}

	/**
	 * Calculate EndDate by Resume Date
	 */
	public void calculateEndDate() {
		try {
			if (decisionData.getResumeDate() != null && followUpData.getFollowUpPeriod() != null && followUpData.getFollowUpPeriod() > 0) {
				if (decisionData.getResumeDate().before(HijriDateService.getHijriSysDate()) && decisionData.getResumeDate().before(followUpData.getFollowUpStartDate())) {
					throw new BusinessException("error_resumeDateBeforeCurrDate");
				}
				followUpEndDate = (HijriDateService.addSubHijriDays(decisionData.getResumeDate(), followUpData.getFollowUpPeriod()));
				followUpEndDateStr = HijriDateService.getHijriDateString(followUpEndDate);
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * calculateFollowUpEndDate by Extended Duration
	 */
	public void calculateFollowUpEndDate() {
		try {
			if (decisionData.getExtensionDuration() == null) {
				throw new BusinessException("error_extendDurationMandatory");
			}
			if (decisionData.getExtensionDuration() != null && followUpData.getFollowUpEndDate() != null) {
				followUpEndDate = (HijriDateService.addSubHijriDays(followUpData.getFollowUpEndDate(), decisionData.getExtensionDuration()));
				followUpEndDateStr = HijriDateService.getHijriDateString(followUpEndDate);
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	public String saveDecision() {
		try {
			FollowUpService.saveDecision(loginEmpData, decisionData, followUpData, followUpEndDate);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
			return NavigationEnum.INBOX.toString();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage()));

		}
		return null;
	}

	public void selectFollowUp() {
		try {
			followUpData = FollowUpService.getFollowUpDataById(followUpData.getId());
			letterData.setFollowUpId(followUpData.getId());
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage()));

		}
	}

	public int getFollowUpExtended() {
		return FollowUpDecisionTypeEnum.FOLLOW_UP_EXTEND.getCode();
	}

	public int getFollowUpEnd() {
		return FollowUpDecisionTypeEnum.FOLLOW_UP_END.getCode();
	}

	/**
	 * View Penalty Arrest
	 * 
	 * @return page to be directed to
	 */
	public String viewPenaltyArrest(PenaltyArrestData penaltyArrest) {
		getRequest().setAttribute("mode", penaltyArrest);
		return NavigationEnum.PENALTY_ARREST.toString();
	}

	/******** GETTERS & SETTERS *********/

	public LetterData getLetterData() {
		return letterData;
	}

	public void setLetterData(LetterData letterData) {
		this.letterData = letterData;
	}

	public FollowUpData getFollowUpData() {
		return followUpData;
	}

	public void setFollowUpData(FollowUpData followUpData) {
		this.followUpData = followUpData;
	}

	public DecisionData getDecisionData() {
		return decisionData;
	}

	public void setDecisionData(DecisionData decisionData) {
		this.decisionData = decisionData;
	}

	public List<SetupDomain> getStopReasonList() {
		return stopReasonList;
	}

	public void setStopReasonList(List<SetupDomain> stopReasonList) {
		this.stopReasonList = stopReasonList;
	}

	public Date getFollowUpEndDate() {
		return followUpEndDate;
	}

	public void setFollowUpEndDate(Date followUpEndDate) {
		this.followUpEndDate = followUpEndDate;
	}

	public String getFollowUpEndDateStr() {
		return followUpEndDateStr;
	}

	public void setFollowUpEndDateStr(String followUpEndDateStr) {
		this.followUpEndDateStr = followUpEndDateStr;
	}

}