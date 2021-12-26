package com.code.ui.backings.assignment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.assignment.AssignmentDetailData;
import com.code.dal.orm.finance.FinInfoReward;
import com.code.dal.orm.finance.FinMonthlyRewardResourceData;
import com.code.dal.orm.info.InfoData;
import com.code.dal.orm.security.UserMenuActionData;
import com.code.enums.AssignmentDetailStatusEnum;
import com.code.enums.AssignmentStatusEnum;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.InfoSourceTypeEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.PaymentMethodEnum;
import com.code.enums.UserMenuActionsEnum;
import com.code.enums.WFPositionsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.assignment.AssignmentService;
import com.code.services.infosys.finance.InfoRewardPaymentService;
import com.code.services.infosys.finance.MonthlyRewardPaymentService;
import com.code.services.infosys.info.InfoService;
import com.code.services.log4j.Log4j;
import com.code.services.security.SecurityService;
import com.code.services.setup.DepartmentService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.WFPositionService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "nonEmployeeAssignmentSearch")
@ViewScoped
public class NonEmployeeAssignmentsSearch extends BaseBacking implements Serializable {
	private int rowsCount = 10;
	private AssignmentDetailData assignmentDetailDataSearch;
	private List<AssignmentDetailData> assignmentDetailDataList;
	private List<InfoData> employeeInfosList;
	private List<FinMonthlyRewardResourceData> finMonthlyRewardResourceDataList;
	private List<FinInfoReward> finInfoRewardList;
	private Integer paymentMethod;
	private Integer active = -1;
	private Date fromDateFinance;
	private Date toDateFinance;
	private Long officerId;
	private String identity;
	private Boolean regionFlag = false;
	private Boolean editprivilege = false;
	private Integer assignmentType;
	private Boolean showMonthlyRewardDetails;
	private String depsList;

	/**
	 * Default Constructor and Initializer
	 */
	public NonEmployeeAssignmentsSearch() {
		super();
		this.init();
		try {
			UserMenuActionData assignmentEditAction = SecurityService.getAction(loginEmpData.getEmpId(), UserMenuActionsEnum.NON_EMPLOYEE_ASSIGNMENT_EDIT.getCode(), FlagsEnum.ALL.getCode());
			if (assignmentEditAction != null) {
				editprivilege = true;
			}
			Long regionId = DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());
			if (regionId != null) {
				assignmentDetailDataSearch.setOfficerId(loginEmpData.getEmpId());
				assignmentDetailDataSearch.setOfficerName(loginEmpData.getFullName());
				regionFlag = true;
				Long sectorId = AssignmentService.isSectorDepartment(loginEmpData.getActualDepartmentId());
				if (sectorId != null) {
					assignmentDetailDataSearch.setSectorId(sectorId);
					assignmentDetailDataSearch.setSectorName(DepartmentService.getDepartment(sectorId).getArabicName());
				}
			}
			long regionDepId = DepartmentService.getDepartment(loginEmpData.getActualDepartmentId()).getRegionId();
			updateDepsList(regionDepId, null, null);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			 Log4j.traceErrorException(NonEmployeeAssignmentsSearch.class, e, "NonEmployeeAssignmentsSearch");
			 this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	/**
	 * Initialize Variables
	 */
	public void init() {
		assignmentType = FlagsEnum.ALL.getCode();
		assignmentDetailDataList = new ArrayList<AssignmentDetailData>();
		assignmentDetailDataSearch = new AssignmentDetailData();
		employeeInfosList = new ArrayList<InfoData>();
		resetSearchFields();
	}

	/**
	 * Reset Search Fields
	 */
	public void resetSearchFields() {
		try {
			active = FlagsEnum.ALL.getCode();
			assignmentType = FlagsEnum.ALL.getCode();
			assignmentDetailDataSearch = new AssignmentDetailData();
			Long regionId = DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());
			if (regionId != null) {
				assignmentDetailDataSearch.setRegionId(regionId);
				assignmentDetailDataSearch.setRegionName(DepartmentService.getDepartment(regionId).getArabicName());
			}
			if (regionId != null && editprivilege != true) {
				assignmentDetailDataSearch.setOfficerId(loginEmpData.getEmpId());
				assignmentDetailDataSearch.setOfficerName(loginEmpData.getFullName());
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			 Log4j.traceErrorException(NonEmployeeAssignmentsSearch.class, e, "NonEmployeeAssignmentsSearch");
			 this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	/**
	 * Search Non Employee Assignment Details
	 */
	public void searchEmployeeAssignmentDetails() {
		try {
			assignmentDetailDataList = AssignmentService.getLastAssignmentDetailsData(assignmentDetailDataSearch.getOfficerId(), assignmentDetailDataSearch.getAgentCode(), assignmentDetailDataSearch.getFullName(), assignmentDetailDataSearch.getIdentity(), assignmentDetailDataSearch.getStartDate(), assignmentDetailDataSearch.getApprovedEndDate(), assignmentDetailDataSearch.getRegionId(), null, AssignmentStatusEnum.APPROVED.getCode(), active, FlagsEnum.OFF.getCode(), assignmentType,
					assignmentDetailDataSearch.getSectorId(), assignmentDetailDataSearch.getUnitId());
			Date currDate = HijriDateService.getHijriSysDate();
			for (AssignmentDetailData assignmentDetailData : assignmentDetailDataList) {
				if (assignmentDetailData.getStartDate().compareTo(currDate) > 0 && assignmentDetailData.getApprovedEndDate().compareTo(currDate) > 0) {
					assignmentDetailData.setHeldFlag(AssignmentDetailStatusEnum.HELD_NOT_STARTED.getCode());
				} else if (assignmentDetailData.getApprovedEndDate().compareTo(currDate) <= 0) {
					assignmentDetailData.setHeldFlag(AssignmentDetailStatusEnum.NOT_HELD.getCode());
				} else {
					assignmentDetailData.setHeldFlag(AssignmentDetailStatusEnum.HELD.getCode());
				}
			}
			if (assignmentDetailDataList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
				return;
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			 Log4j.traceErrorException(NonEmployeeAssignmentsSearch.class, e, "NonEmployeeAssignmentsSearch");
			 this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	/**
	 * Evaluate Agent
	 * 
	 * @param assignmentDetailData
	 * @return
	 */
	public String evaluateAgent(AssignmentDetailData assignmentDetailData) {
		getRequest().setAttribute("mode", assignmentDetailData);
		return NavigationEnum.NON_EMPLOYEE_ASSIGNEMNT_EVALUATION.toString();
	}

	/**
	 * Get Agent Information
	 * 
	 * @param assignmentDetailData
	 */
	public void getEmployeeInfos(AssignmentDetailData assignmentDetailData) {
		try {
			employeeInfosList = InfoService.getInfoDataBySourceIdentityAndOfficerId(assignmentDetailData.getOfficerId(), assignmentDetailData.getIdentity(), loginEmpData);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Get Agent Information
	 * 
	 * @param assignmentDetailData
	 */
	public void getAgentFinDetails(AssignmentDetailData assignmentDetailData) {
		try {
			fromDateFinance = null;
			toDateFinance = null;
			identity = assignmentDetailData.getIdentity();
			officerId = assignmentDetailData.getOfficerId();
			showMonthlyRewardDetails = assignmentDetailData.getType().equals(InfoSourceTypeEnum.ASSIGNMENT.getCode());
			if (showMonthlyRewardDetails) {
				finMonthlyRewardResourceDataList = MonthlyRewardPaymentService.getAllFinMonthlyRewardResourceDataByOfficerIdAndIdentity(officerId, identity, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode());
			}
			finInfoRewardList = InfoRewardPaymentService.getAllInfoRewardByOfficerIdAndIdentity(officerId, identity, null, null);
			paymentMethod = assignmentDetailData.getPaymentMethod();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	public void searchAgentFinDetails() {
		try {
			if (fromDateFinance != null && toDateFinance != null) {
				int fromYear = HijriDateService.getHijriDateYear(fromDateFinance);
				int toYear = HijriDateService.getHijriDateYear(toDateFinance);
				int fromMonth = HijriDateService.getHijriDateMonth(fromDateFinance);
				int monthDiff = ((toYear - fromYear) * 12) + HijriDateService.getHijriDateMonth(toDateFinance) - fromMonth;
				if (showMonthlyRewardDetails) {
					finMonthlyRewardResourceDataList = new ArrayList<FinMonthlyRewardResourceData>();
					for (int i = 0; i <= monthDiff; i++) {
						if (fromMonth > 12) {
							fromMonth = 1;
							fromYear++;
						}
						finMonthlyRewardResourceDataList.addAll(MonthlyRewardPaymentService.getAllFinMonthlyRewardResourceDataByOfficerIdAndIdentity(officerId, identity, fromMonth, fromYear));
						fromMonth++;
					}
				}
				finInfoRewardList = InfoRewardPaymentService.getAllInfoRewardByOfficerIdAndIdentity(officerId, identity, fromDateFinance, toDateFinance);
			} else if (fromDateFinance != null || toDateFinance != null) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_fromAndToDatMandatory"));
			} else {
				if (showMonthlyRewardDetails) {
					finMonthlyRewardResourceDataList = MonthlyRewardPaymentService.getAllFinMonthlyRewardResourceDataByOfficerIdAndIdentity(officerId, identity, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode());
				}
				finInfoRewardList = InfoRewardPaymentService.getAllInfoRewardByOfficerIdAndIdentity(officerId, identity, null, null);
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			 Log4j.traceErrorException(NonEmployeeAssignmentsSearch.class, e, "NonEmployeeAssignmentsSearch");
			 this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	public void resetFinSearch() {
		fromDateFinance = null;
		toDateFinance = null;
	}

	/**
	 * Re Assign Agent
	 * 
	 * @param assignmentDetailData
	 * @return
	 */
	public String reAssign(AssignmentDetailData assignmentDetailData) {
		try {
			if (AssignmentService.getLastAssignmentDetailsData(loginEmpData.getEmpId(), null, null, assignmentDetailData.getIdentity(), null, null, null, null, AssignmentStatusEnum.APPROVED.getCode(), null, FlagsEnum.OFF.getCode(), assignmentDetailData.getType()).isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("label_canNotReAssignNonEmp"));
				return null;
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
		getRequest().setAttribute("mode", 1);
		getRequest().setAttribute("assignmentId", assignmentDetailData.getAssginmentId());
		getRequest().setAttribute("object", assignmentDetailData);
		return NavigationEnum.NON_EMPLOYEE_ASSIGNEMNT.toString();
	}

	/**
	 * View or edit Agent Assignment Details
	 * 
	 * @param assignmentDetailData
	 * @return
	 */
	public String viewAssignmentDetail(AssignmentDetailData assignmentDetailData) {
		getRequest().setAttribute("assignmentId", assignmentDetailData.getAssginmentId());
		getRequest().setAttribute("object", assignmentDetailData);
		getRequest().setAttribute("mode", 3);
		return NavigationEnum.NON_EMPLOYEE_ASSIGNEMNT.toString();
	}

	/**
	 * update department list when changing region and sector and unit
	 * 
	 * @param regionId
	 * @param sectorId
	 * @param unitId
	 */
	public void updateDepsList(Long regionId, Long sectorId, Long unitId) {
		try {
			depsList = "";
			List<Long> departmentList = new ArrayList<Long>();
			if (unitId != null && unitId != 0) {
				depsList = unitId.toString();
			} else if (sectorId != null && sectorId != 0) {
				departmentList = AssignmentService.getDepsIdsOfIntelligenceRegionDepartments(regionId, sectorId);
			} else if (regionId.equals(getHeadQuarterId())) {
				departmentList = AssignmentService.getDepsIdsOfIntelligenceRegionDepartments(null, null);
			}
			if (!departmentList.isEmpty()) {
				for (Long depId : departmentList) {
					depsList += depId + "" + ",";
				}
			} else if (depsList.isEmpty()) {
				depsList = FlagsEnum.ALL.getCode() + "";
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			 Log4j.traceErrorException(NonEmployeeAssignmentsSearch.class, e, "NonEmployeeAssignmentsSearch");
			 this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}

	}

	public int getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}

	public AssignmentDetailData getAssignmentDetailDataSearch() {
		return assignmentDetailDataSearch;
	}

	public void setAssignmentDetailDataSearch(AssignmentDetailData assignmentDetailDataSearch) {
		this.assignmentDetailDataSearch = assignmentDetailDataSearch;
	}

	public List<AssignmentDetailData> getAssignmentDetailDataList() {
		return assignmentDetailDataList;
	}

	public void setAssignmentDetailDataList(List<AssignmentDetailData> assignmentDetailDataList) {
		this.assignmentDetailDataList = assignmentDetailDataList;
	}

	public List<InfoData> getEmployeeInfosList() {
		return employeeInfosList;
	}

	public void setEmployeeInfosList(List<InfoData> employeeInfosList) {
		this.employeeInfosList = employeeInfosList;
	}

	public String getDepartmentType() {
		return DepartmentTypeEnum.REGION.getCode() + "," + DepartmentTypeEnum.DIRECTORATE.getCode();
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public List<FinMonthlyRewardResourceData> getFinMonthlyRewardResourceDataList() {
		return finMonthlyRewardResourceDataList;
	}

	public void setFinMonthlyRewardResourceDataList(List<FinMonthlyRewardResourceData> finMonthlyRewardResourceDataList) {
		this.finMonthlyRewardResourceDataList = finMonthlyRewardResourceDataList;
	}

	public List<FinInfoReward> getFinInfoRewardList() {
		return finInfoRewardList;
	}

	public void setFinInfoRewardList(List<FinInfoReward> finInfoRewardList) {
		this.finInfoRewardList = finInfoRewardList;
	}

	public Integer getBankAccountEnum() {
		return PaymentMethodEnum.BANK_ACCOUNT.getCode();
	}

	public Integer getChequeAcountEnum() {
		return PaymentMethodEnum.CHEQUE.getCode();
	}

	public Integer getCashAccountEnum() {
		return PaymentMethodEnum.CASH.getCode();
	}

	public Integer getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(Integer paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Integer getAssignmentDetailsRegisteredStatus() {
		return AssignmentStatusEnum.REGISTERED.getCode();
	}

	public Integer getAssignmentDetailsApprovedStatus() {
		return AssignmentStatusEnum.APPROVED.getCode();
	}

	public Integer getAssignmentDetailsUnderApprovalStatus() {
		return AssignmentStatusEnum.UNDER_APPROVAL.getCode();
	}

	public int getEnumHeldStarted() {
		return AssignmentDetailStatusEnum.HELD.getCode();
	}

	public int getEnumNotHeld() {
		return AssignmentDetailStatusEnum.NOT_HELD.getCode();
	}

	public int getEnumHeldNotStarted() {
		return AssignmentDetailStatusEnum.HELD_NOT_STARTED.getCode();
	}

	public Date getFromDateFinance() {
		return fromDateFinance;
	}

	public void setFromDateFinance(Date fromDateFinance) {
		this.fromDateFinance = fromDateFinance;
	}

	public Date getToDateFinance() {
		return toDateFinance;
	}

	public void setToDateFinance(Date toDateFinance) {
		this.toDateFinance = toDateFinance;
	}

	public boolean isRegionFlag() {
		return regionFlag;
	}

	public void setRegionFlag(boolean regionFlag) {
		this.regionFlag = regionFlag;
	}

	public Boolean getEditprivilege() {
		return editprivilege;
	}

	public void setEditprivilege(Boolean editprivilege) {
		this.editprivilege = editprivilege;
	}

	public Integer getAssignmentType() {
		return assignmentType;
	}

	public void setAssignmentType(Integer assignmentType) {
		this.assignmentType = assignmentType;
	}

	public Boolean getShowMonthlyRewardDetails() {
		return showMonthlyRewardDetails;
	}

	public void setShowMonthlyRewardDetails(Boolean showMonthlyRewardDetails) {
		this.showMonthlyRewardDetails = showMonthlyRewardDetails;
	}

	public String getDepsList() {
		return depsList;
	}

	public void setDepsList(String depsList) {
		this.depsList = depsList;
	}

	public Integer getAssignment() {
		return InfoSourceTypeEnum.ASSIGNMENT.getCode();
	}

	public Integer getCooperetor() {
		return InfoSourceTypeEnum.COOPERATOR.getCode();
	}

	public Long getHeadQuarterId() throws BusinessException {
		return WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId();
	}
}
