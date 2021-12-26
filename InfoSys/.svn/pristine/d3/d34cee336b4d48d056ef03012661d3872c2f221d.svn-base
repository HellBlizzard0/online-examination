package com.code.ui.backings.finance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.finance.FinDepSupportData;
import com.code.dal.orm.finance.FinDepSupportDetailData;
import com.code.dal.orm.finance.FinYearApproval;
import com.code.enums.FlagsEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.finance.FinanceAccountsService;
import com.code.services.infosys.finance.InfoRewardPaymentService;
import com.code.services.log4j.Log4j;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.finance.RegionFinancialSupportWorkFlow;
import com.code.ui.backings.base.WFBaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "departmentAccountSupport")
@ViewScoped
public class DepartmentAccountSupport extends WFBaseBacking implements Serializable {
	private int rowsCount = 10;
	private int pageMode;
	private boolean registered;
	private boolean edit;
	private String errorMessage;
	private FinYearApproval yearApproval;
	private FinDepSupportData depSupport;
	private Long selectedDepartmentId;
	private FinDepSupportDetailData depSupportDetail;
	private List<FinDepSupportDetailData> depsSupportDetailList;
	private List<FinDepSupportDetailData> depSupportDetailList;
	private Set<Long> departmentsIdsSet;
	private Double initialBalance;
	private Double oldSupportAmount;
	private Boolean actionCollapseFlag;
	private Double totalSupportAmount;
	private String finHijriYearsString;

	/**
	 * Constructor
	 */
	public DepartmentAccountSupport() {
		super.init();
		pageMode = 1;
		selectedDepartmentId = null;
		oldSupportAmount = 0.0;
		try {
			if (currentTask != null) {
				depSupport = FinanceAccountsService.getFinDepSupport(instance.getInstanceId());
				depsSupportDetailList = FinanceAccountsService.getFinDepSupportDetail(depSupport.getId());
				
				calculateTotalSupportAmount();
				finHijriYearsString = HijriDateService.getHijriYearsString(depSupport.getFinYear());
				if (role.equals(WFTaskRolesEnum.PROCESSING.getCode())) {
					departmentsIdsSet = new HashSet<Long>();
					for(FinDepSupportDetailData detail: depsSupportDetailList){
						departmentsIdsSet.add(detail.getDepartmentId());
					}
					depSupport.setBalance(FinanceAccountsService.calculateDepartmentsSupportNetBalance(depSupport.getFinYear()));
					depSupportDetail = new FinDepSupportDetailData();
					depSupportDetail.setDepartmentSupportId(depSupport.getId());
					pageMode = 2;
					registered = true;
				}
			} else {
				registered = false;
				yearApproval = FinanceAccountsService.getFinYearApproval(Calendar.getInstance().get(Calendar.YEAR));
				if (yearApproval == null) {
					errorMessage = getParameterizedMessage("error_finYearApprovalNotRegistered");
				} else {
					pageMode = 2;
					init();
				}
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(DepartmentAccountSupport.class, e, "DepartmentAccountSupport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Initialize page
	 */
	protected void init() {
		try {
			depSupport = new FinDepSupportData();
			depSupport.setFinYearApprovalId(yearApproval.getId());
			depSupport.setRequestDate(HijriDateService.getHijriSysDate());
			depSupport.setBalance(FinanceAccountsService.calculateDepartmentsSupportNetBalance(yearApproval.getFinYear()));
			depSupport.setFinYear(yearApproval.getFinYear());
			depSupport.setApproved(FlagsEnum.OFF.getCode());
			finHijriYearsString = HijriDateService.getHijriYearsString(depSupport.getFinYear());
			depsSupportDetailList = new ArrayList<FinDepSupportDetailData>();
			depSupportDetail = new FinDepSupportDetailData();
			departmentsIdsSet = new HashSet<Long>();
			registered = true;
			totalSupportAmount = 0.0;
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(DepartmentAccountSupport.class, e, "DepartmentAccountSupport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Reset department value
	 */
	public void cancelDepSupportDetail() {
		depSupportDetail = new FinDepSupportDetailData();
		oldSupportAmount = 0.0;
	}

	/**
	 * Add new department support Detail
	 */
	public void addDepSupportDetail() {
		if(!edit && departmentsIdsSet.contains(depSupportDetail.getDepartmentId())){
			this.setServerSideErrorMessages(getParameterizedMessage("error_departmentIsExist"));
			return;
		}
		try {
			depSupportDetail.setBalance(FinanceAccountsService.calculateDepartmentSupportNetBalance(depSupportDetail.getDepartmentId(), depSupport.getFinYearApprovalId()));
			depSupportDetail.setTotalSpent(InfoRewardPaymentService.getSumInfoRewardAmount(depSupportDetail.getDepartmentId(), depSupport.getFinYearApprovalId()));
			if(!depsSupportDetailList.contains(depSupportDetail)) {
				if(currentTask != null){
					FinanceAccountsService.addDepartmentAccountSupportDetail(loginEmpData, depSupportDetail, depSupport, depsSupportDetailList);
					this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));			
				}
				depsSupportDetailList.add(depSupportDetail);
				departmentsIdsSet.add(depSupportDetail.getDepartmentId());
				depSupportDetail = new FinDepSupportDetailData();
				depSupportDetail.setDepartmentSupportId(depSupport.getId());
				oldSupportAmount = 0.0;
			}else{
				try {
					if(currentTask != null){
						FinanceAccountsService.updateDepartmentAccountSupportDetail(loginEmpData, depSupportDetail, depSupport, depsSupportDetailList);
						this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
					}
					depSupportDetail = new FinDepSupportDetailData();
					depSupportDetail.setDepartmentSupportId(depSupport.getId());
					oldSupportAmount = 0.0;
					edit = false;
				} catch (BusinessException e) {
					depSupportDetail.setSupportAmount(oldSupportAmount);
					this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
					return;
				}
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return;
		}catch (Exception e) {
			   Log4j.traceErrorException(DepartmentAccountSupport.class, e, "DepartmentAccountSupport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return;
		  }
		calculateTotalSupportAmount();
	}

	/**
	 * Edit Department support detail
	 * 
	 * @param detail
	 */
	public void editDepSupportDetail(FinDepSupportDetailData detail) {
		edit = true;
		oldSupportAmount = detail.getSupportAmount();
		actionCollapseFlag = true;
		depSupportDetail = detail;
	}

	/**
	 * Delete department support detail
	 * 
	 * @param detail
	 */
	public void deleteDepSupportDetail(FinDepSupportDetailData detail) {
		if(currentTask != null){
			try {
				if(depsSupportDetailList.size() == 1){
					this.setServerSideErrorMessages(getParameterizedMessage("error_atLeastOneDepartmentOnRequest"));
					return;
				}
				FinanceAccountsService.deleteDepartmentAccountSupportDetail(loginEmpData, detail);
				this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
			} catch (BusinessException e) {
				this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
				return;
			}catch (Exception e) {
				   Log4j.traceErrorException(DepartmentAccountSupport.class, e, "DepartmentAccountSupport");
				   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
				   return;
			  }
		}
		depsSupportDetailList.remove(detail);
		departmentsIdsSet.remove(depSupportDetail.getDepartmentId());
		calculateTotalSupportAmount();
	}

	/**
	 * Calculate total support amount
	 */
	public void calculateTotalSupportAmount() {
		totalSupportAmount = 0.0;
		for (FinDepSupportDetailData detail : depsSupportDetailList) {
			totalSupportAmount += detail.getSupportAmount();
		}
	}

	/**
	 * Save department support
	 * 
	 * @return page to be redirected to
	 */
	public String saveDepSupport() {
		try {
			if (currentTask == null) {
				FinanceAccountsService.addDepartmentAccountSupport(loginEmpData, depSupport, depsSupportDetailList);
			} else {
				RegionFinancialSupportWorkFlow.doModify(currentTask, depSupport, depsSupportDetailList, loginEmpData, attachments);
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(DepartmentAccountSupport.class, e, "DepartmentAccountSupport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Approve department support
	 * 
	 * @return page to be redirected to
	 */
	public String approveDepSupport() {
		try {
			if (role.equals(WFTaskRolesEnum.GENERAL_INTELLIGENCE_MANAGER.getCode())) {
				RegionFinancialSupportWorkFlow.doApproveDM(currentTask, depSupport, loginEmpData, attachments);
			} else if (role.equals(WFTaskRolesEnum.GENERAL_DIRECTOR.getCode())) {
				RegionFinancialSupportWorkFlow.doApproveSM(currentTask, depSupport, loginEmpData, attachments);
			} else if (role.equals(WFTaskRolesEnum.GENERAL_MANAGER.getCode())) {
				depSupport.setApproved(FlagsEnum.ON.getCode());
				RegionFinancialSupportWorkFlow.doApproveGM(currentTask, depSupport, depsSupportDetailList, loginEmpData, attachments);
			}
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(DepartmentAccountSupport.class, e, "DepartmentAccountSupport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Reject department support
	 * 
	 * @return page to be redirected to
	 */
	public String rejectDepSupport() {
		try {
			if (currentTask.getNotes() != null && currentTask.getNotes().trim().isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_rejectReasonMandatory"));
				return null;
			}
			RegionFinancialSupportWorkFlow.doReject(currentTask, depSupport, loginEmpData, attachments);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(DepartmentAccountSupport.class, e, "DepartmentAccountSupport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Do notify
	 * 
	 * @return page to be redirected to
	 */
	public String doNotify() {
		try {
			RegionFinancialSupportWorkFlow.doNotify(currentTask);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(DepartmentAccountSupport.class, e, "DepartmentAccountSupport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		return NavigationEnum.INBOX.toString();
	}
	
	/**
	 * Show All Supports on the chosen department
	 * 
	 * @param departmentId
	 */
	public void showDepSupport(long departmentId) {
		try {
			selectedDepartmentId = departmentId;
			depSupportDetailList = FinanceAccountsService.getFinDepSupportDetailByDepartment(selectedDepartmentId, depSupport.getFinYearApprovalId());
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(DepartmentAccountSupport.class, e, "DepartmentAccountSupport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}
	
	/**
	 * Print report
	 */
	public void print() {
		try {
			byte[] bytes = FinanceAccountsService.getDepartmentSupportDetailsReportBytes(depSupport.getId(), depSupport.getBalance(), finHijriYearsString, loginEmpData.getFullName());
			super.print(bytes, "Department_Account_Support_Details");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(DepartmentAccountSupport.class, e, "DepartmentAccountSupport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	// setters and getters
	public int getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}

	public int getPageMode() {
		return pageMode;
	}

	public void setPageMode(int pageMode) {
		this.pageMode = pageMode;
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

	public FinYearApproval getYearApproval() {
		return yearApproval;
	}

	public void setYearApproval(FinYearApproval yearApproval) {
		this.yearApproval = yearApproval;
	}

	public Double getInitialBalance() {
		return initialBalance;
	}

	public void setInitialBalance(Double initialBalance) {
		this.initialBalance = initialBalance;
	}

	public Double getOldSupportAmount() {
		return oldSupportAmount;
	}

	public void setOldSupportAmount(Double oldSupportAmount) {
		this.oldSupportAmount = oldSupportAmount;
	}

	public FinDepSupportData getDepSupport() {
		return depSupport;
	}

	public void setDepSupport(FinDepSupportData depSupport) {
		this.depSupport = depSupport;
	}

	public Long getSelectedDepartmentId() {
		return selectedDepartmentId;
	}

	public void setSelectedDepartmentId(Long selectedDepartmentId) {
		this.selectedDepartmentId = selectedDepartmentId;
	}

	public List<FinDepSupportDetailData> getDepsSupportDetailList() {
		return depsSupportDetailList;
	}

	public void setDepsSupportDetailList(List<FinDepSupportDetailData> depsSupportDetailList) {
		this.depsSupportDetailList = depsSupportDetailList;
	}

	public List<FinDepSupportDetailData> getDepSupportDetailList() {
		return depSupportDetailList;
	}

	public void setDepSupportDetailList(List<FinDepSupportDetailData> depSupportDetailList) {
		this.depSupportDetailList = depSupportDetailList;
	}

	public Boolean getActionCollapseFlag() {
		return actionCollapseFlag;
	}

	public void setActionCollapseFlag(Boolean actionCollapseFlag) {
		this.actionCollapseFlag = actionCollapseFlag;
	}

	public FinDepSupportDetailData getDepSupportDetail() {
		return depSupportDetail;
	}

	public void setDepSupportDetail(FinDepSupportDetailData depSupportDetail) {
		this.depSupportDetail = depSupportDetail;
	}

	public Double getTotalSupportAmount() {
		return totalSupportAmount;
	}

	public void setTotalSupportAmount(Double totalSupportAmount) {
		this.totalSupportAmount = totalSupportAmount;
	}

	public String getFinHijriYearsString() {
		return finHijriYearsString;
	}

	public void setFinHijriYearsString(String finHijriYearsString) {
		this.finHijriYearsString = finHijriYearsString;
	}

	public Set<Long> getDepartmentsIdsSet() {
		return departmentsIdsSet;
	}

	public void setDepartmentsIdsSet(Set<Long> departmentsIdsSet) {
		this.departmentsIdsSet = departmentsIdsSet;
	}
}