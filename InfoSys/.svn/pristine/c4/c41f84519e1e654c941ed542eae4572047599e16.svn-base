package com.code.ui.backings.finance;

import java.io.Serializable;
import java.util.Calendar;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.finance.FinYearApproval;
import com.code.enums.NavigationEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.finance.FinanceAccountsService;
import com.code.services.log4j.Log4j;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "approvedFinanceYear")
@ViewScoped
public class ApprovedFinanceYear extends BaseBacking implements Serializable {
	private int pageMode;
	private FinYearApproval yearApproval;
	private Double initialBalance;
	private Double lastYearBalance;
	private Double monthlyRewardTotalSpent;
	private Double regionsSupportAmount;
	private Double monthlyRewardTotal;
	private Double regionsSupportTotal;

	/**
	 * Constructor
	 */
	public ApprovedFinanceYear() {
		try {
			yearApproval = FinanceAccountsService.getFinYearApproval(Calendar.getInstance().get(Calendar.YEAR));
			if (yearApproval == null) {
				yearApproval = new FinYearApproval();
				yearApproval.setFinYear(Calendar.getInstance().get(Calendar.YEAR));
				monthlyRewardTotalSpent = 0.0;
				regionsSupportAmount = 0.0;
				yearApproval.setRegionsInitialBalance(0.0);
				yearApproval.setRewardInitialBalance(0.0);
				yearApproval.setCurrentYearBalance(0.0);
				pageMode = 2;
			} else {
				monthlyRewardTotalSpent = FinanceAccountsService.sumAllMonthlyRewardsTotalSpent(yearApproval.getId());
				regionsSupportAmount = FinanceAccountsService.sumAllDepSupportAmount(yearApproval.getId());
				pageMode = 1;
			}
			lastYearBalance = FinanceAccountsService.calculateYearBalance(yearApproval.getFinYear() - 1);
			initialBalance = lastYearBalance + yearApproval.getCurrentYearBalance();
			monthlyRewardTotal = yearApproval.getRewardInitialBalance() - monthlyRewardTotalSpent;
			regionsSupportTotal = yearApproval.getRegionsInitialBalance() - regionsSupportAmount;
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(ApprovedFinanceYear.class, e, "ApprovedFinanceYear");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Save
	 */
	public String save() {
		try {
			FinanceAccountsService.addFinYearApproval(loginEmpData, yearApproval);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
			pageMode = 1;
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(ApprovedFinanceYear.class, e, "ApprovedFinanceYear");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		return NavigationEnum.SUCCESS.toString();
	}

	// setters and getters
	public int getPageMode() {
		return pageMode;
	}

	public void setPageMode(int pageMode) {
		this.pageMode = pageMode;
	}

	public FinYearApproval getYearApproval() {
		return yearApproval;
	}

	public void setYearApproval(FinYearApproval yearApproval) {
		this.yearApproval = yearApproval;
	}

	public Double getLastYearBalance() {
		return lastYearBalance;
	}

	public void setLastYearBalance(Double lastYearBalance) {
		this.lastYearBalance = lastYearBalance;
	}

	public Double getInitialBalance() {
		return initialBalance;
	}

	public void setInitialBalance(Double initialBalance) {
		this.initialBalance = initialBalance;
	}

	public Double getMonthlyRewardTotalSpent() {
		return monthlyRewardTotalSpent;
	}

	public void setMonthlyRewardTotalSpent(Double monthlyRewardTotalSpent) {
		this.monthlyRewardTotalSpent = monthlyRewardTotalSpent;
	}

	public Double getRegionsSupportAmount() {
		return regionsSupportAmount;
	}

	public void setRegionsSupportAmount(Double regionsSupportAmount) {
		this.regionsSupportAmount = regionsSupportAmount;
	}

	public Double getMonthlyRewardTotal() {
		return monthlyRewardTotal;
	}

	public void setMonthlyRewardTotal(Double monthlyRewardTotal) {
		this.monthlyRewardTotal = monthlyRewardTotal;
	}

	public Double getRegionsSupportTotal() {
		return regionsSupportTotal;
	}

	public void setRegionsSupportTotal(Double regionsSupportTotal) {
		this.regionsSupportTotal = regionsSupportTotal;
	}
}