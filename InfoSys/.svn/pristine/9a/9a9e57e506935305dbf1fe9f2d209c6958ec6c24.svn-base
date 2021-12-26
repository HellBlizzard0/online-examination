package com.code.ui.backings.finance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.finance.FinYearAccountSupport;
import com.code.dal.orm.finance.FinYearApproval;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.finance.FinanceAccountsService;
import com.code.services.log4j.Log4j;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "supportSearch")
@ViewScoped
public class SupportFinanceAccountSearch extends BaseBacking implements Serializable {
	private FinYearApproval yearApproval;
	private String errorMessage;
	private List<FinYearAccountSupport> yearAccountSupport;
	private Date fromDate;
	private Date toDate;
	private String finHijriYearsString;
	private FinYearAccountSupport selectedFinYearAccountSupport;
	private Integer suppIndex;
	private boolean suppExpendFlag;
	private Double supportAmount;
	private Double regionsSupportCurrentBalance;
	private Double monthlyRewardCurrentBalance;
	private Double savedRegionBalance;
	private Double savedRewardBalance;

	public SupportFinanceAccountSearch() {
		super();
		init();
		selectedFinYearAccountSupport = new FinYearAccountSupport();
		yearAccountSupport = new ArrayList<FinYearAccountSupport>();
		try {
			yearApproval = FinanceAccountsService.getFinYearApproval(Calendar.getInstance().get(Calendar.YEAR));
			if (yearApproval == null) {
				errorMessage = getParameterizedMessage("error_finYearApprovalNotRegistered");
			} else {
				finHijriYearsString = HijriDateService.getHijriYearsString(yearApproval.getFinYear());
				monthlyRewardCurrentBalance = FinanceAccountsService.calculateMonthlyRewardNetBalance(yearApproval.getFinYear());
				regionsSupportCurrentBalance = FinanceAccountsService.calculateDepartmentsSupportNetBalance(yearApproval.getFinYear());
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SupportFinanceAccountSearch.class, e, "SupportFinanceAccountSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * search
	 */
	public void search() {
		try {
			yearAccountSupport = FinanceAccountsService.getFinYearAccountSupport(yearApproval.getId(), fromDate, toDate);
			if (yearAccountSupport.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
			}
			selectedFinYearAccountSupport = new FinYearAccountSupport();
			supportAmount = 0.0;
			suppExpendFlag = false;
			suppIndex = null;
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SupportFinanceAccountSearch.class, e, "SupportFinanceAccountSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * reset search
	 */
	public void resetSearch() {
		fromDate = null;
		toDate = null;
		suppExpendFlag = false;
		suppIndex = null;
		selectedFinYearAccountSupport = new FinYearAccountSupport();
		supportAmount = 0.0;
	}

	/**
	 * save support
	 */
	public void saveSupport() {
		try {
			if (savedRegionBalance + selectedFinYearAccountSupport.getRegionsSupportBalance() < 0 || savedRewardBalance + selectedFinYearAccountSupport.getRewardSupportBalance() < 0) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_can'tDecresseAccountAmount"));
				return;
			}
			FinanceAccountsService.updateFinYearAccountSupport(loginEmpData, selectedFinYearAccountSupport);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
			yearAccountSupport.set(suppIndex, selectedFinYearAccountSupport);
			selectedFinYearAccountSupport = new FinYearAccountSupport();
			supportAmount = 0.0;
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SupportFinanceAccountSearch.class, e, "SupportFinanceAccountSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * editSupport
	 * 
	 * @param row
	 * @param index
	 */
	public void editSupport(FinYearAccountSupport row, Integer index) {
		try {
			selectedFinYearAccountSupport = (FinYearAccountSupport) row.clone();
			suppExpendFlag = true;
			suppIndex = index;
			supportAmount = selectedFinYearAccountSupport.getRegionsSupportBalance() + selectedFinYearAccountSupport.getRewardSupportBalance();
			savedRegionBalance = regionsSupportCurrentBalance - selectedFinYearAccountSupport.getRegionsSupportBalance();
			savedRewardBalance = monthlyRewardCurrentBalance - selectedFinYearAccountSupport.getRewardSupportBalance();
		} catch (CloneNotSupportedException e) {
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		}
	}

	/**
	 * deleteSupport
	 * 
	 * @param row
	 */
	public void deleteSupport(FinYearAccountSupport row) {
		try {
			if (regionsSupportCurrentBalance - row.getRegionsSupportBalance() < 0 || monthlyRewardCurrentBalance - row.getRewardSupportBalance() < 0) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_can'tDecresseAccountAmount"));
				return;
			}
			FinanceAccountsService.deleteFinYearAccountSupport(loginEmpData, row);
			yearAccountSupport.remove(row);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
			selectedFinYearAccountSupport = new FinYearAccountSupport();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SupportFinanceAccountSearch.class, e, "SupportFinanceAccountSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public FinYearApproval getYearApproval() {
		return yearApproval;
	}

	public void setYearApproval(FinYearApproval yearApproval) {
		this.yearApproval = yearApproval;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<FinYearAccountSupport> getYearAccountSupport() {
		return yearAccountSupport;
	}

	public void setYearAccountSupport(List<FinYearAccountSupport> yearAccountSupport) {
		this.yearAccountSupport = yearAccountSupport;
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

	public String getFinHijriYearsString() {
		return finHijriYearsString;
	}

	public void setFinHijriYearsString(String finHijriYearsString) {
		this.finHijriYearsString = finHijriYearsString;
	}

	public FinYearAccountSupport getSelectedFinYearAccountSupport() {
		return selectedFinYearAccountSupport;
	}

	public void setSelectedFinYearAccountSupport(FinYearAccountSupport selectedFinYearAccountSupport) {
		this.selectedFinYearAccountSupport = selectedFinYearAccountSupport;
	}

	public Integer getSuppIndex() {
		return suppIndex;
	}

	public void setSuppIndex(Integer suppIndex) {
		this.suppIndex = suppIndex;
	}

	public boolean isSuppExpendFlag() {
		return suppExpendFlag;
	}

	public void setSuppExpendFlag(boolean suppExpendFlag) {
		this.suppExpendFlag = suppExpendFlag;
	}

	public Double getSupportAmount() {
		return supportAmount;
	}

	public void setSupportAmount(Double supportAmount) {
		this.supportAmount = supportAmount;
	}
}
