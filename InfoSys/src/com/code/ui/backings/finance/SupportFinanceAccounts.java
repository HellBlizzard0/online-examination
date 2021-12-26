package com.code.ui.backings.finance;

import java.io.Serializable;
import java.util.Calendar;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.finance.FinYearAccountSupport;
import com.code.dal.orm.finance.FinYearApproval;
import com.code.enums.NavigationEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.finance.FinanceAccountsService;
import com.code.services.log4j.Log4j;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "supportFinanceAccounts")
@ViewScoped
public class SupportFinanceAccounts extends BaseBacking implements Serializable {
	private FinYearApproval yearApproval;
	private FinYearAccountSupport yearAccountSupport;
	private Double supportAmount;
	private Double currentBalance;
	private Double totalBalance;
	private Double initialBalance;
	private Double lastYearBalance;
	private Double monthlyRewardCurrentBalance;
	private Double regionsSupportCurrentBalance;
	private Double monthlyRewardTotal;
	private Double regionsSupportTotal;
	private boolean registered;
	private String errorMessage;
	private Double finYearAccountSum;

	/**
	 * Constructor
	 */
	public SupportFinanceAccounts() {
		try {
			registered = false;
			yearApproval = FinanceAccountsService.getFinYearApproval(Calendar.getInstance().get(Calendar.YEAR));
			if (yearApproval == null) {
				errorMessage = getParameterizedMessage("error_finYearApprovalNotRegistered");
			} else {
				init();
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SupportFinanceAccounts.class, e, "SupportFinanceAccounts");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Initialize page
	 */
	public void init() {
		try {
			yearAccountSupport = new FinYearAccountSupport();
			yearAccountSupport.setFinYearApprovalId(yearApproval.getId());
			yearAccountSupport.setSupportDate(HijriDateService.getHijriSysDate());
			yearAccountSupport.setRegionsSupportBalance(0.0);
			yearAccountSupport.setRewardSupportBalance(0.0);
			currentBalance = FinanceAccountsService.calculateYearBalance(yearApproval.getFinYear());
			supportAmount = 0.0;
			totalBalance = currentBalance + supportAmount;
			lastYearBalance = FinanceAccountsService.calculateYearBalance(yearApproval.getFinYear() - 1);
			finYearAccountSum = FinanceAccountsService.calculateFinYearAcoountSum(yearApproval.getId());
			initialBalance = lastYearBalance + yearApproval.getCurrentYearBalance() + finYearAccountSum;
			monthlyRewardCurrentBalance = FinanceAccountsService.calculateMonthlyRewardNetBalance(yearApproval.getFinYear());
			regionsSupportCurrentBalance = FinanceAccountsService.calculateDepartmentsSupportNetBalance(yearApproval.getFinYear());
			monthlyRewardTotal = monthlyRewardCurrentBalance;
			regionsSupportTotal = regionsSupportCurrentBalance;
			registered = true;
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SupportFinanceAccounts.class, e, "SupportFinanceAccounts");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Save
	 */
	public String save() {
		try {
			FinanceAccountsService.addFinYearAccountSupport(loginEmpData, yearAccountSupport);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
			init();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(SupportFinanceAccounts.class, e, "SupportFinanceAccounts");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		return NavigationEnum.SUCCESS.toString();
	}

	// setters and getters
	public FinYearApproval getYearApproval() {
		return yearApproval;
	}

	public void setYearApproval(FinYearApproval yearApproval) {
		this.yearApproval = yearApproval;
	}

	public FinYearAccountSupport getYearAccountSupport() {
		return yearAccountSupport;
	}

	public void setYearAccountSupport(FinYearAccountSupport yearAccountSupport) {
		this.yearAccountSupport = yearAccountSupport;
	}

	public Double getSupportAmount() {
		return supportAmount;
	}

	public void setSupportAmount(Double supportAmount) {
		this.supportAmount = supportAmount;
	}

	public Double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(Double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public Double getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(Double totalBalance) {
		this.totalBalance = totalBalance;
	}

	public Double getInitialBalance() {
		return initialBalance;
	}

	public void setInitialBalance(Double initialBalance) {
		this.initialBalance = initialBalance;
	}

	public Double getLastYearBalance() {
		return lastYearBalance;
	}

	public void setLastYearBalance(Double lastYearBalance) {
		this.lastYearBalance = lastYearBalance;
	}

	public Double getMonthlyRewardCurrentBalance() {
		return monthlyRewardCurrentBalance;
	}

	public void setMonthlyRewardCurrentBalance(Double monthlyRewardCurrentBalance) {
		this.monthlyRewardCurrentBalance = monthlyRewardCurrentBalance;
	}

	public Double getRegionsSupportCurrentBalance() {
		return regionsSupportCurrentBalance;
	}

	public void setRegionsSupportCurrentBalance(Double regionsSupportCurrentBalance) {
		this.regionsSupportCurrentBalance = regionsSupportCurrentBalance;
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

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Double getFinYearAccountSum() {
		return finYearAccountSum;
	}

	public void setFinYearAccountSum(Double finYearAccountSum) {
		this.finYearAccountSum = finYearAccountSum;
	}
}