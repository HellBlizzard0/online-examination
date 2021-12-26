package com.code.ui.backings.finance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.assignment.AssignmentDetailData;
import com.code.dal.orm.finance.FinMonthlyRewDepDetail;
import com.code.dal.orm.finance.FinMonthlyReward;
import com.code.dal.orm.finance.FinMonthlyRewardResourceData;
import com.code.dal.orm.finance.FinYearApproval;
import com.code.dal.orm.setup.DepartmentData;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.FinMonthlyRewardStatusEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.PaymentMethodEnum;
import com.code.enums.WFPositionsEnum;
import com.code.enums.WFTaskActionsEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.assignment.AssignmentNonEmployeeService;
import com.code.services.infosys.finance.FinanceAccountsService;
import com.code.services.infosys.finance.MonthlyRewardPaymentService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.WFPositionService;
import com.code.services.workflow.finance.MonthlyRewardWorkFlow;
import com.code.ui.backings.base.WFBaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "monthlyReward")
@ViewScoped
public class MonthlyRewardPayment extends WFBaseBacking implements Serializable {

	private FinYearApproval finYearApproval;
	private FinMonthlyReward finMonthlyReward;
	private List<FinMonthlyRewDepDetail> finMonthlyRewDepDetailList;
	private Integer[] hijriYears;
	private boolean errorMonthExistFlag;

	public MonthlyRewardPayment() {
		super();
		init();
		hijriYears = new Integer[3];
		finYearApproval = new FinYearApproval();
		finMonthlyReward = new FinMonthlyReward();
		finMonthlyRewDepDetailList = new ArrayList<FinMonthlyRewDepDetail>();

		try {
			if (currentTask == null) {
				Integer curYear = HijriDateService.getHijriDateYear(HijriDateService.getHijriSysDate());
				hijriYears[0] = curYear - 1;
				hijriYears[1] = curYear;
				hijriYears[2] = curYear + 1;
				monthlyRewardPaymentInit(HijriDateService.getHijriDateMonth(HijriDateService.getHijriSysDate()), curYear);
				checkIfMonthExists();
				
			} else if (currentTask != null) {
				finMonthlyReward = MonthlyRewardPaymentService.getFinMonthlyRewardByInstanceId(currentTask.getInstanceId());
				finYearApproval = FinanceAccountsService.getFinYearApprovalById(finMonthlyReward.getFinYearApprovalId());
				finMonthlyRewDepDetailList = MonthlyRewardPaymentService.getFinMonthlyRewDepDetail(finMonthlyReward.getId());
				Double totalSpent = 0.0;
				Integer totalResourcesNumbers = 0;
				for (FinMonthlyRewDepDetail depDetails : finMonthlyRewDepDetailList) {
					totalSpent += depDetails.getTotalSpent();
					totalResourcesNumbers += depDetails.getResourceNumber();
				}
				finMonthlyReward.setTotalAgentsNumbers(totalResourcesNumbers);
				finMonthlyReward.setTotalSpent(totalSpent);
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(MonthlyRewardPayment.class, e, "MonthlyRewardPayment");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void monthlyRewardPaymentInit(int month, int year) {
		try {

			finYearApproval = new FinYearApproval();
			finMonthlyReward = new FinMonthlyReward();
			finMonthlyRewDepDetailList = new ArrayList<FinMonthlyRewDepDetail>();

			Calendar cal = Calendar.getInstance();
			cal.setTime(HijriDateService.hijriToGregDate(HijriDateService.getHijriDate("1/" + month + "/" + year)));
			int gregYear = cal.get(Calendar.YEAR);
			finYearApproval = FinanceAccountsService.getFinYearApproval(gregYear);
			if (finYearApproval != null) {

				finMonthlyReward.setFinYearApprovalId(finYearApproval.getId());
				finMonthlyReward.setMonthNumber(month);
				finMonthlyReward.setHijriYear(year);
				finMonthlyReward.setTotalAmount(FinanceAccountsService.calculateMonthlyRewardCurrentYearBalance(gregYear));
				finMonthlyReward.setAccountBalance(FinanceAccountsService.calculateMonthlyRewardNetBalance(gregYear));

				List<DepartmentData> regionsDepartmetns = new ArrayList<DepartmentData>();
				regionsDepartmetns.add(DepartmentService.getDepartment(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId()));
				regionsDepartmetns.addAll(DepartmentService.getDepartmentsBydepartmentType(DepartmentTypeEnum.REGION.getCode()));

				Double totalSpent = 0.0;
				Integer totalResourcesNumbers = 0;

				for (DepartmentData region : regionsDepartmetns) {
					FinMonthlyRewDepDetail newFinMonthlyRewDepDetail = new FinMonthlyRewDepDetail();
					newFinMonthlyRewDepDetail.setDepartmentId(region.getId());
					newFinMonthlyRewDepDetail.setDepartmentName(region.getArabicName());
					Double regionTotalSpent = AssignmentNonEmployeeService.getRegionAssignmentDetailsMonthlyReward(region.getId(), month, year);
					Integer regionTotalResourcesNumbers = AssignmentNonEmployeeService.getRegionAssignmentDetailsCount(region.getId(), month, year);
					newFinMonthlyRewDepDetail.setTotalSpent(regionTotalSpent == null ? 0.0 : regionTotalSpent);
					newFinMonthlyRewDepDetail.setResourceNumber(regionTotalResourcesNumbers);
					if (regionTotalSpent != null && regionTotalResourcesNumbers != null) {
						totalSpent += regionTotalSpent;
						totalResourcesNumbers += regionTotalResourcesNumbers;
					}

					List<FinMonthlyRewardResourceData> finMonthlyRewardResourceDataList = new ArrayList<FinMonthlyRewardResourceData>();
					List<AssignmentDetailData> agentsList = AssignmentNonEmployeeService.getRegionAssignmentDetails(region.getId(), month, year);
					for (AssignmentDetailData agent : agentsList) {
						FinMonthlyRewardResourceData newFinMonthlyRewardResourceData = new FinMonthlyRewardResourceData();
						newFinMonthlyRewardResourceData.setAssignmentDetailId(agent.getId());
						finMonthlyRewardResourceDataList.add(newFinMonthlyRewardResourceData);
					}

					newFinMonthlyRewDepDetail.setFinMonthlyRewardResourceDataList(finMonthlyRewardResourceDataList);
					finMonthlyRewDepDetailList.add(newFinMonthlyRewDepDetail);
				}
				finMonthlyReward.setTotalAgentsNumbers(totalResourcesNumbers);
				finMonthlyReward.setTotalSpent(totalSpent);
			} else {
				finYearApproval = new FinYearApproval();
				finYearApproval.setFinYear(gregYear);
				finMonthlyReward.setMonthNumber(month);
				finMonthlyReward.setHijriYear(year);
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(MonthlyRewardPayment.class, e, "MonthlyRewardPayment");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void getMonthDetails() {
		errorMonthExistFlag = false;
		monthlyRewardPaymentInit(finMonthlyReward.getMonthNumber(), finMonthlyReward.getHijriYear());
		checkIfMonthExists();
	}

	/**
	 * 
	 * @return
	 */
	public String send() {
		try {
			MonthlyRewardWorkFlow.initFinMonthlyReward(finMonthlyReward, finMonthlyRewDepDetailList, loginEmpData, null);
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(MonthlyRewardPayment.class, e, "MonthlyRewardPayment");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * 
	 * @param action
	 * @return
	 */
	public String doApproveRejectDM(int action) {
		try {
			if (action == 1) // Approve
				MonthlyRewardWorkFlow.doApproveRejectDM(currentTask, WFTaskActionsEnum.APPROVE, finMonthlyReward, loginEmpData, null);
			else { // reject
				if (currentTask.getNotes() != null && currentTask.getNotes().trim().isEmpty()) {
					this.setServerSideErrorMessages(getParameterizedMessage("error_rejectReasonMandatory"));
					return null;
				}
				currentTask.setRefuseReasons(currentTask.getNotes());
				MonthlyRewardWorkFlow.doApproveRejectDM(currentTask, WFTaskActionsEnum.REJECT, finMonthlyReward, loginEmpData, null);
			}
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(MonthlyRewardPayment.class, e, "MonthlyRewardPayment");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * 
	 * @param action
	 * @return
	 */
	public String doApproveRejectSM(int action) {
		try {
			if (action == 1) // Approve
				MonthlyRewardWorkFlow.doApproveRejectSM(currentTask, WFTaskActionsEnum.APPROVE, finMonthlyReward, loginEmpData, null);
			else { // reject
				if (currentTask.getNotes() != null && currentTask.getNotes().trim().isEmpty()) {
					this.setServerSideErrorMessages(getParameterizedMessage("error_rejectReasonMandatory"));
					return null;
				}
				currentTask.setRefuseReasons(currentTask.getNotes());
				MonthlyRewardWorkFlow.doApproveRejectSM(currentTask, WFTaskActionsEnum.REJECT, finMonthlyReward, loginEmpData, null);
			}
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(MonthlyRewardPayment.class, e, "MonthlyRewardPayment");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * 
	 * @param action
	 * @return
	 */
	public String doApproveRejectGM(int action) {
		try {
			if (action == 1) // Approve
				MonthlyRewardWorkFlow.doApproveRejectGM(currentTask, WFTaskActionsEnum.APPROVE, finMonthlyReward, loginEmpData, null);
			else { // reject
				if (currentTask.getNotes() != null && currentTask.getNotes().trim().isEmpty()) {
					this.setServerSideErrorMessages(getParameterizedMessage("error_rejectReasonMandatory"));
					return null;
				}
				currentTask.setRefuseReasons(currentTask.getNotes());
				MonthlyRewardWorkFlow.doApproveRejectGM(currentTask, WFTaskActionsEnum.REJECT, finMonthlyReward, loginEmpData, null);
			}
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(MonthlyRewardPayment.class, e, "MonthlyRewardPayment");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * 
	 * @return
	 */
	public String doNotified() {
		try {
			MonthlyRewardWorkFlow.doNotify(currentTask);
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(MonthlyRewardPayment.class, e, "MonthlyRewardPayment");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * check if month entered by the user already processed or under processing
	 * or not
	 */
	public void checkIfMonthExists() {
		if (finMonthlyReward.getFinYearApprovalId() != null) {
			try {
				if (!MonthlyRewardPaymentService.getFinMonthlyReward(finMonthlyReward.getMonthNumber(), finMonthlyReward.getFinYearApprovalId(), FinMonthlyRewardStatusEnum.APPROVED.getCode(), finMonthlyReward.getHijriYear()).isEmpty()) {
					errorMonthExistFlag = true;
				} else if (!MonthlyRewardPaymentService.getFinMonthlyReward(finMonthlyReward.getMonthNumber(), finMonthlyReward.getFinYearApprovalId(), FinMonthlyRewardStatusEnum.UNDER_PROCESSING.getCode(), finMonthlyReward.getHijriYear()).isEmpty()) {
					errorMonthExistFlag = true;
				}
			} catch (BusinessException e) {
				super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			}catch (Exception e) {
				   Log4j.traceErrorException(MonthlyRewardPayment.class, e, "MonthlyRewardPayment");
				   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			  }
		}
	}
	
	/**
	 * printReport
	 */
	public void printReport(int reportType) {
		try {
			byte[] bytes = MonthlyRewardPaymentService.getMonthlyRewardByIBANOrChequeReportBytes(finMonthlyReward.getId(), reportType, loginEmpData.getFullName());
			String reportName = reportType == getIBANPaymentMethod() ?   "MonthlyRewardReportByIBAN" : "MonthlyRewardReportByCheque" ;
			super.print(bytes, reportName);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(MonthlyRewardPayment.class, e, "MonthlyRewardPayment");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}
	
	/**
	 * Print report
	 */
	public void print() {
		try {
			byte[] bytes = MonthlyRewardPaymentService.getMonthlyRewardDetailsReportBytes(finMonthlyReward.getId(), finYearApproval.getFinYear() + "", loginEmpData.getFullName());
			super.print(bytes, "Monthly_Reward_Details");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(MonthlyRewardPayment.class, e, "MonthlyRewardPayment");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/********************************************************** Setters And Getters **********************************************/

	public FinYearApproval getFinYearApproval() {
		return finYearApproval;
	}

	public void setFinYearApproval(FinYearApproval finYearApproval) {
		this.finYearApproval = finYearApproval;
	}

	public FinMonthlyReward getFinMonthlyReward() {
		return finMonthlyReward;
	}

	public void setFinMonthlyReward(FinMonthlyReward finMonthlyReward) {
		this.finMonthlyReward = finMonthlyReward;
	}

	public List<FinMonthlyRewDepDetail> getFinMonthlyRewDepDetailList() {
		return finMonthlyRewDepDetailList;
	}

	public void setFinMonthlyRewDepDetailList(List<FinMonthlyRewDepDetail> finMonthlyRewDepDetailList) {
		this.finMonthlyRewDepDetailList = finMonthlyRewDepDetailList;
	}

	public String getGeneralIntelligenceManagerRole() {
		return WFTaskRolesEnum.GENERAL_INTELLIGENCE_MANAGER.getCode();
	}

	public String getGeneralDirectorRole() {
		return WFTaskRolesEnum.GENERAL_DIRECTOR.getCode();
	}

	public String getGeneralManagerRole() {
		return WFTaskRolesEnum.GENERAL_MANAGER.getCode();
	}

	public String getNotificationRole() {
		return WFTaskRolesEnum.NOTIFICATION.getCode();
	}

	public String getHistoryRole() {
		return WFTaskRolesEnum.HISTORY.getCode();
	}
	
	public int getIBANPaymentMethod() {
		return PaymentMethodEnum.BANK_ACCOUNT.getCode();
	}
	
	public int getChequePaymentMethod() {
		return PaymentMethodEnum.CHEQUE.getCode();
	}

	public Integer[] getHijriYears() {
		return hijriYears;
	}

	public void setHijriYears(Integer[] hijriYears) {
		this.hijriYears = hijriYears;
	}

	public boolean isErrorMonthExistFlag() {
		return errorMonthExistFlag;
	}

	public void setErrorMonthExistFlag(boolean errorMonthExistFlag) {
		this.errorMonthExistFlag = errorMonthExistFlag;
	}
}
