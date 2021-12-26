package com.code.ui.backings.assignment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.assignment.AssignmentData;
import com.code.dal.orm.assignment.AssignmentDetailData;
import com.code.dal.orm.security.UserMenuActionData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.setup.SetupClass;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.AssignmentStatusEnum;
import com.code.enums.ClassesEnum;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.InfoSourceTypeEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.ReportNamesEnum;
import com.code.enums.UserMenuActionsEnum;
import com.code.enums.WFPositionsEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.assignment.AssignmentEmployeeService;
import com.code.services.infosys.assignment.AssignmentService;
import com.code.services.infosys.securitycheck.SecurityCheckService;
import com.code.services.log4j.Log4j;
import com.code.services.security.SecurityService;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.EmployeeService;
import com.code.services.setup.SetupService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.WFPositionService;
import com.code.services.workflow.assignment.EmployeesAssignementWorkFlow;
import com.code.services.workflow.surveillance.SurveillanceWorkFlow;
import com.code.ui.backings.base.WFBaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "employeeAssginment")
@ViewScoped
public class EmployeesAssignment extends WFBaseBacking implements Serializable {
	private AssignmentData assignmentData;
	private List<AssignmentDetailData> assignmentDetailsDataList;
	private List<AssignmentDetailData> assignmentDetailsDataDeletedList;
	private List<String> assignmentReasonsList;
	private List<String> assignmentSelectedReasonsList;
	private Long selectedEmpId;
	private String remarks;
	private Integer pageMode = 1;
	private String screenTitle;
	private Date rowAssignStartDate;
	private Integer rowAssignPeriodMonth;
	private String rowAssignPhoneNumberPrimary;
	private String rowAssignPhoneNumberSecondary;
	private String rowAssignPhoneNumberTrinary;
	private String rowAssignWorkNature;
	private String rowAssignWorkScope;
	private AssignmentDetailData selectedAssignDetailData;
	private boolean isReAssigned;
	private boolean regionSelected;
	private boolean sectorSelected;
	private boolean reChangeCode;
	private String depsList;
	private Integer reportMode = 1; // 1- Assignment 2-ReAssignment 3-m3lga
	private Boolean editprivilege = false;

	/**
	 * Default Constructor and Initializer
	 */
	public EmployeesAssignment() {
		super.init();
		this.init();
		try {
			Long assignmentId = null;
			AssignmentDetailData assignmentDetailData = null;
			// Set Reasons List
			List<SetupClass> setupClass = SetupService.getClasses(ClassesEnum.ASSIGNMENT_REASONS.getCode(), null, FlagsEnum.ALL.getCode());
			if (!setupClass.isEmpty()) {
				List<SetupDomain> reasonsDomainList = SetupService.getDomains(setupClass.get(0).getId(), null, FlagsEnum.ALL.getCode());
				if (reasonsDomainList.isEmpty()) {
					this.setServerSideErrorMessages(getParameterizedMessage("error_DBError"));
				}
				for (SetupDomain domain : reasonsDomainList) {
					assignmentReasonsList.add(domain.getDescription());
				}
			}

			if (getRequest().getAttribute("mode") != null) {
				assignmentId = (Long) getRequest().getAttribute("mode");
			}
			if (getRequest().getAttribute("object") != null) {
				assignmentDetailData = (AssignmentDetailData) getRequest().getAttribute("object");
			}
			Date currDate = HijriDateService.getHijriSysDate();
			// Check Page Roles
			if (currentTask == null) {
				if (assignmentId == null) {
					pageMode = 1;
					assignmentData.setRequestDate(currDate);
					assignmentData.setStartDate(currDate);
					// assignmentData.setOfficerId(loginEmpData.getEmpId());
					// assignmentData.setOfficerName(loginEmpData.getFullName());
					// If login dep from a region / sector then set region /
					// sector automatically
					setRegionSector();
				} else {
					reChangeCode = (Integer) getRequest().getAttribute("pageMode") != null ? true : false;
					if (!reChangeCode) {
						reportMode = 2;
						screenTitle = getParameterizedMessage("title_employeeAssignmentRe");
						pageMode = 1;
						assignmentData = AssignmentService.getAssignments(assignmentId, null, null, null, null, null).get(0);
						assignmentData.setId(null);
						assignmentData.setwFInstanceId(null);
						assignmentData.setRequestDate(currDate);
						assignmentData.setRequestNumber(null);
						assignmentData.setStartDate(currDate);
						// assignmentData.setOfficerId(loginEmpData.getEmpId());
						// assignmentData.setOfficerName(loginEmpData.getFullName());
						setRegionSector();// If login dep from a region / sector then set region / sector automatically
						// Load Last Assignment for this Officer
						assignmentDetailData = AssignmentService.getLastAssignmentDetailsData(loginEmpData.getEmpId(), null, null, assignmentDetailData.getIdentity(), null, null, null, FlagsEnum.OFF.getCode(), AssignmentStatusEnum.APPROVED.getCode(), null, FlagsEnum.ON.getCode(), FlagsEnum.ALL.getCode()).get(0);
						assignmentDetailData.setAgentCode(AssignmentService.getLastAssignmentDetailsData(null, null, null, assignmentDetailData.getIdentity(), null, null, assignmentData.getRegionId(), FlagsEnum.OFF.getCode(), AssignmentStatusEnum.APPROVED.getCode(), null, FlagsEnum.ON.getCode(), FlagsEnum.ALL.getCode()).get(0).getAgentCode());
						assignmentDetailData.setSelected(true);
						assignmentDetailData.setStartDate(currDate);
						assignmentDetailData.setId(null);
						assignmentDetailsDataList.add(assignmentDetailData);

						isReAssigned = true;
					} else {
						screenTitle = getParameterizedMessage("title_employeeAssignmentView");
						pageMode = 2;
						UserMenuActionData assignmentEditAction = SecurityService.getAction(loginEmpData.getEmpId(), UserMenuActionsEnum.EMPLOYEE_ASSIGNMENT_EDIT.getCode(), FlagsEnum.ALL.getCode());
						if (assignmentEditAction != null) {
							editprivilege = true;
							pageMode = 10;
						}
						assignmentDetailsDataList.add(assignmentDetailData);
						assignmentData = AssignmentService.getAssignments(assignmentId, null, null, null, null, null).get(0);
					}
					// Set employees reasons list
					for (AssignmentDetailData emp : assignmentDetailsDataList) {
						if (emp.getReasons() != null && !emp.getReasons().isEmpty()) {
							String[] reasons = emp.getReasons().split(",");
							emp.setReasonsList(Arrays.asList(reasons));
						}
					}
				}
			} else {
				if (role.equals(WFTaskRolesEnum.HQ_SECURITY_AFFAIRS_APPROVER_MANAGER.getCode()) || role.equals(WFTaskRolesEnum.REGION_INTELLIGENCE_DEP_HEAD.getCode()) || role.equals(WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER.getCode()) || role.equals(WFTaskRolesEnum.HQ_GENERAL_PROTECTION_APPROVER_MANAGER.getCode())) {
					if (currentTask.getFlexField3().equals("true")) {
						reportMode = 2;
						screenTitle = getParameterizedMessage("title_employeeAssignmentApproveRe");
					} else {
						screenTitle = getParameterizedMessage("title_employeeAssignmentApprove");
					}
					pageMode = 3;
				} else if (role.equals(WFTaskRolesEnum.HQ_SECURITY_AFFAIRS_PROCESSOR_MANAGER.getCode())) {
					if (currentTask.getFlexField3().equals("true")) {
						reportMode = 2;
						screenTitle = getParameterizedMessage("title_employeeAssignmentAnalysisRe");
					} else {
						screenTitle = getParameterizedMessage("title_employeeAssignmentAnalysis");
					}
					pageMode = 4;

				} else if (role.equals(WFTaskRolesEnum.HQ_GENERAL_PROTECTION_PROCESSOR_MANAGER.getCode())) {
					if (currentTask.getFlexField3().equals("true")) {
						reportMode = 2;
						screenTitle = getParameterizedMessage("title_employeeAssignmentFinalApprovalRe");
					} else {
						screenTitle = getParameterizedMessage("title_employeeAssignmentFinalApproval");
					}
					pageMode = 5;
				} else if (role.equals(WFTaskRolesEnum.SECURITY_AFFAIRS_PRINT_ASSIGNMENT_LETTER_MANAGER.getCode())) {
					screenTitle = getParameterizedMessage("title_employeePrintAssignmentLetter");
					pageMode = 6;
				} else {
					if (currentTask.getFlexField3() != null && currentTask.getFlexField3().equals("true")) {
						reportMode = 2;
						screenTitle = getParameterizedMessage("title_employeeAssignmentRe");
					}
					pageMode = 2;
					if (role.equals(WFTaskRolesEnum.REQUESTER.getCode())) {
						pageMode = 1;
						if (currentTask.getFlexField3().equals("true")) {
							reportMode = 2;
							screenTitle = getParameterizedMessage("title_employeeAssignmentRejectedRe");
						} else {
							screenTitle = getParameterizedMessage("title_employeeAssignmentRejected");
						}
					}
				}
				assignmentData = AssignmentService.getAssignments(null, instance.getInstanceId(), null, null, null, null).get(0);
				assignmentDetailsDataList = AssignmentService.getAssignmentDetailsDataByAssignmentId(assignmentData.getId());
				// Set employees reasons list
				for (AssignmentDetailData emp : assignmentDetailsDataList) {
					if (emp.getReasons() != null && !emp.getReasons().isEmpty()) {
						String[] reasons = emp.getReasons().split(",");
						emp.setReasonsList(Arrays.asList(reasons));
					}
				}
			}

			if (pageMode == 1 || editprivilege) {
				long regionId = DepartmentService.getDepartment(loginEmpData.getActualDepartmentId()).getRegionId();
				updateDepsList(regionId, assignmentDetailData == null ? null : assignmentDetailData.getSectorId(), assignmentDetailData == null ? null : assignmentDetailData.getUnitId());
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			Log4j.traceErrorException(EmployeesAssignment.class, e, "EmployeesAssignment");
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	/**
	 * Initialize bean variables
	 */
	public void init() {
		assignmentData = new AssignmentData();
		assignmentDetailsDataList = new ArrayList<AssignmentDetailData>();
		assignmentDetailsDataDeletedList = new ArrayList<AssignmentDetailData>();
		assignmentReasonsList = new ArrayList<String>();
		assignmentSelectedReasonsList = new ArrayList<String>();
		screenTitle = getParameterizedMessage("title_employeeAssignment");
		depsList = null;
	}

	/**
	 * Set Both Region and Sector if Login user from Region or from Sector
	 */
	public void setRegionSector() {
		Long regionId;
		try {
			regionId = DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());
			if (regionId != null) {
				assignmentData.setRegionId(regionId);
				assignmentData.setRegionName(DepartmentService.getDepartment(regionId).getArabicName());
				regionSelected = true;
				Long sectorId = AssignmentService.isSectorDepartment(loginEmpData.getActualDepartmentId());
				if (sectorId != null) {
					assignmentData.setSectorId(sectorId);
					assignmentData.setSectorName(DepartmentService.getDepartment(sectorId).getArabicName());
					sectorSelected = true;
				}
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			Log4j.traceErrorException(EmployeesAssignment.class, e, "EmployeesAssignment");
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	/**
	 * Add assignment detail data using filled Assignment Data
	 */
	public void addAssignmentDetailData() {
		try {
			EmployeeData emp = EmployeeService.getEmployee(selectedEmpId);
			for (AssignmentDetailData employee : assignmentDetailsDataList) {
				if (employee.getEmployeeId().equals(selectedEmpId)) {
					this.setServerSideErrorMessages(getParameterizedMessage("error_alreadyChoosen"));
					return;
				}
			}

			// Copy Employee Data To Assignment Data
			AssignmentDetailData assignmentDetailData = new AssignmentDetailData();
			assignmentDetailData.setEmployeeId(selectedEmpId);
			assignmentDetailData.setFullName(emp.getFullName());
			assignmentDetailData.setIdentity(emp.getSocialID());
			assignmentDetailData.setRank(emp.getRank());
			assignmentDetailData.setWorkPlace(emp.getActualDepartmentName());
			// Copy Order Data To Assignment Detail Data
			assignmentDetailData.setEliminated(false);
			assignmentDetailData.setRequestDate(assignmentData.getRequestDate());
			assignmentDetailData.setOfficerId(assignmentData.getOfficerId());
			assignmentDetailData.setRegionId(assignmentData.getRegionId());
			assignmentDetailData.setRegionName(assignmentData.getRegionName());
			assignmentDetailData.setSectorId(assignmentData.getSectorId());
			assignmentDetailData.setSectorName(assignmentData.getSectorName());
			assignmentDetailData.setStartDate(assignmentData.getStartDate());
			assignmentDetailData.setPeriod(assignmentData.getPeriod());
			assignmentDetailData.setType(InfoSourceTypeEnum.ASSIGNMENT.getCode());
			Date endDate = HijriDateService.addSubHijriMonthsDays(assignmentData.getStartDate(), assignmentData.getPeriod(), 0);
			assignmentDetailData.setEndDate(endDate);
			assignmentDetailData.setApprovedEndDate(endDate);
			assignmentDetailData.setReasonsList(assignmentSelectedReasonsList);
			assignmentDetailsDataList.add(assignmentDetailData);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			Log4j.traceErrorException(EmployeesAssignment.class, e, "EmployeesAssignment");
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	/**
	 * Edit selected Row details
	 * 
	 * @param assignDetailData
	 */
	public void editSelectedRow(AssignmentDetailData assignDetailData) {
		selectedAssignDetailData = assignDetailData;
		rowAssignStartDate = assignDetailData.getStartDate();
		rowAssignPeriodMonth = assignDetailData.getPeriod();
		rowAssignPhoneNumberPrimary = assignDetailData.getPrimaryPhone();
		rowAssignPhoneNumberSecondary = assignDetailData.getSecondaryPhone();
		rowAssignPhoneNumberTrinary = assignDetailData.getTernaryPhone();
		rowAssignWorkNature = assignDetailData.getWorkNature();
		rowAssignWorkScope = assignDetailData.getWorkScope();
	}

	/**
	 * Save Selected Row Changes
	 */
	public void saveSelectedRowChanges() {
		selectedAssignDetailData.setStartDate(rowAssignStartDate);
		selectedAssignDetailData.setPeriod(rowAssignPeriodMonth);
		selectedAssignDetailData.setPrimaryPhone(rowAssignPhoneNumberPrimary);
		selectedAssignDetailData.setSecondaryPhone(rowAssignPhoneNumberSecondary);
		selectedAssignDetailData.setTernaryPhone(rowAssignPhoneNumberTrinary);
		selectedAssignDetailData.setWorkNature(rowAssignWorkNature);
		selectedAssignDetailData.setWorkScope(rowAssignWorkScope);
	}

	/**
	 * Delete assignment Detail
	 */
	public void deleteAssignmentDetail(AssignmentDetailData assignmentDetailData) {
		assignmentDetailsDataList.remove(assignmentDetailData);
		assignmentDetailsDataDeletedList.add(assignmentDetailData);
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
	}

	/**
	 * Save and Start Assignment order work flow
	 */
	public String saveAndSend() {
		try {
			if (assignmentData.getStartDate() != null && assignmentData.getStartDate().compareTo(HijriDateService.getHijriSysDate()) < 0) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_assignmentInvalideDate"));
				return null;
			}
			if (assignmentDetailsDataList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_atLeastOneChoosed"));
				return null;
			}
			for (AssignmentDetailData assignmentDetailData : assignmentDetailsDataList) {
				Date endDate = HijriDateService.addSubHijriMonthsDays(assignmentDetailData.getStartDate(), assignmentDetailData.getPeriod(), 0);
				assignmentDetailData.setEndDate(endDate);
				assignmentDetailData.setApprovedEndDate(endDate);
				assignmentDetailData.setStatus(AssignmentStatusEnum.UNDER_APPROVAL.getCode());
			}

			if (assignmentData.getId() != null && role.equals(WFTaskRolesEnum.REQUESTER.getCode())) {
				AssignmentEmployeeService.updateAssignments(currentTask, assignmentData, assignmentDetailsDataList, assignmentDetailsDataDeletedList, loginEmpData, isReAssigned);
			} else {
				AssignmentEmployeeService.addAssignments(assignmentData, assignmentDetailsDataList, loginEmpData, isReAssigned);
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		} catch (Exception e) {
			Log4j.traceErrorException(EmployeesAssignment.class, e, "EmployeesAssignment");
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			return null;
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Save and send analysis
	 * 
	 * @return
	 */
	public String saveAndSendAnalysis() {
		try {
			currentTask.setNotes(remarks);
			boolean eliminated = false;
			for (AssignmentDetailData assignmentDetailData : assignmentDetailsDataList) {
				if (assignmentDetailData.getEliminated()) {
					eliminated = true;
					break;
				}
			}
			if (eliminated && (remarks == null || remarks.trim().isEmpty())) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_notesMandatoryAtElimination"));
				return null;
			}
			EmployeesAssignementWorkFlow.processHQManager(currentTask, assignmentData, assignmentDetailsDataList, null);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Approve Assignment Request
	 * 
	 * @return
	 */
	public String doApprove() {
		try {
			currentTask.setNotes(remarks);
			if (role.equals(WFTaskRolesEnum.REGION_INTELLIGENCE_DEP_HEAD.getCode())) {
				EmployeesAssignementWorkFlow.approveRegionDepHead(currentTask, assignmentData, loginEmpData);
			} else if (role.equals(WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER.getCode())) {
				EmployeesAssignementWorkFlow.approveHQGeneralManager(currentTask, assignmentData, null);
			} else if (role.equals(WFTaskRolesEnum.HQ_SECURITY_AFFAIRS_APPROVER_MANAGER.getCode())) {
				EmployeesAssignementWorkFlow.approveHQManager(currentTask, assignmentData);
			} else if (role.equals(WFTaskRolesEnum.HQ_GENERAL_PROTECTION_PROCESSOR_MANAGER.getCode())) {
				boolean eliminated = false;
				for (AssignmentDetailData assignmentDetailData : assignmentDetailsDataList) {
					if (assignmentDetailData.getEliminated()) {
						assignmentDetailData.setStatus(AssignmentStatusEnum.REJECTED.getCode());
						eliminated = true;
					} else {
						assignmentDetailData.setStatus(AssignmentStatusEnum.APPROVED.getCode());
					}
				}
				if (eliminated && (remarks == null || remarks.trim().isEmpty())) {
					this.setServerSideErrorMessages(getParameterizedMessage("error_notesMandatoryAtElimination"));
					return null;
				}
				EmployeesAssignementWorkFlow.processHQGeneralManager(currentTask, assignmentData, assignmentDetailsDataList, null);
			} else if (role.equals(WFTaskRolesEnum.HQ_GENERAL_PROTECTION_APPROVER_MANAGER.getCode())) {
				EmployeesAssignementWorkFlow.approveHQGeneralManager(currentTask, assignmentData, null);
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		} catch (Exception e) {
			Log4j.traceErrorException(EmployeesAssignment.class, e, "EmployeesAssignment");
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			return null;
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Reject Assignment
	 * 
	 * @return
	 */
	public String doReject() {
		try {
			if (remarks != null && remarks.trim().isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_rejectReasonMandatory"));
				return null;
			}
			currentTask.setNotes(remarks);
			if (role.equals(WFTaskRolesEnum.HQ_GENERAL_PROTECTION_PROCESSOR_MANAGER.getCode())) {
				EmployeesAssignementWorkFlow.rejectEmployeeAssignmentAnalaysis(currentTask, assignmentData, null);
			} else {
				EmployeesAssignementWorkFlow.rejectEmployeeAssignment(currentTask, assignmentData, null);
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Do notify
	 * 
	 * @return
	 */
	public String doNotified() {
		try {
			SurveillanceWorkFlow.doNotify(currentTask);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Print Assignment Letter Using Assignment Id
	 */
	public void printLetter() {
		try {
			byte[] bytes = AssignmentEmployeeService.getAssignmentLetterBytes(assignmentData.getId(), loginEmpData.getFullName());
			String reportName = ReportNamesEnum.EMPLOYEE_ASSIGNMENT_REWARD_LETTER.toString().replace("_", " ");
			super.print(bytes, reportName);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Save Changes
	 */
	public void saveChanges() {
		try {
			AssignmentEmployeeService.updateAssignmentDetails(assignmentDetailsDataList, assignmentData, loginEmpData);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * saveAssignmentChange
	 */
	public String saveAssignmentChange() {
		try {
			AssignmentService.updateAssignment(assignmentData, loginEmpData);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
			return NavigationEnum.EMPLOYEE_ASSIGNEMNT_SEARCH.toString();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}
	}

	/**
	 * print details
	 */
	public void print() {
		try {
			if (reportMode == 1) {
				byte[] bytes = AssignmentService.getEmployeeAssignmentDetailBytes(assignmentData.getId(), loginEmpData.getFullName());
				super.print(bytes, "employeeAssignDetail");
			} else if (reportMode == 2) {
				byte[] bytes = AssignmentService.getEmployeeReAssignmentDetailBytes(assignmentDetailsDataList.get(0).getId(), assignmentData.getId(), loginEmpData.getFullName());
				super.print(bytes, "employeeReAssignDetail");
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * printEmployeeDetails
	 * 
	 * @param employeeId
	 */
	public void printEmployeeDetails(long employeeId) {
		try {
			byte[] bytes = SecurityCheckService.getEmployeeDetailsBytes(employeeId, loginEmpData);
			super.print(bytes, "Employee_Details");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	// update department list when changing region
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
		} catch (Exception e) {
			Log4j.traceErrorException(EmployeesAssignment.class, e, "EmployeesAssignment");
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}

	}

	public AssignmentData getAssignmentData() {
		return assignmentData;
	}

	public void setAssignmentData(AssignmentData assignmentData) {
		this.assignmentData = assignmentData;
	}

	public List<AssignmentDetailData> getAssignmentDetailsDataList() {
		return assignmentDetailsDataList;
	}

	public void setAssignmentDetailsDataList(List<AssignmentDetailData> assignmentDetailsDataList) {
		this.assignmentDetailsDataList = assignmentDetailsDataList;
	}

	public List<String> getAssignmentReasonsList() {
		return assignmentReasonsList;
	}

	public void setAssignmentReasonsList(List<String> assignmentReasonsList) {
		this.assignmentReasonsList = assignmentReasonsList;
	}

	public List<String> getAssignmentSelectedReasonsList() {
		return assignmentSelectedReasonsList;
	}

	public void setAssignmentSelectedReasonsList(List<String> assignmentSelectedReasonsList) {
		this.assignmentSelectedReasonsList = assignmentSelectedReasonsList;
	}

	public Integer getPageMode() {
		return pageMode;
	}

	public void setPageMode(Integer pageMode) {
		this.pageMode = pageMode;
	}

	public Long getSelectedEmpId() {
		return selectedEmpId;
	}

	public void setSelectedEmpId(Long selectedEmpId) {
		this.selectedEmpId = selectedEmpId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getRowAssignStartDate() {
		return rowAssignStartDate;
	}

	public void setRowAssignStartDate(Date rowAssignStartDate) {
		this.rowAssignStartDate = rowAssignStartDate;
	}

	public Integer getRowAssignPeriodMonth() {
		return rowAssignPeriodMonth;
	}

	public void setRowAssignPeriodMonth(Integer rowAssignPeriodMonth) {
		this.rowAssignPeriodMonth = rowAssignPeriodMonth;
	}

	public String getRowAssignPhoneNumberPrimary() {
		return rowAssignPhoneNumberPrimary;
	}

	public void setRowAssignPhoneNumberPrimary(String rowAssignPhoneNumberPrimary) {
		this.rowAssignPhoneNumberPrimary = rowAssignPhoneNumberPrimary;
	}

	public String getRowAssignPhoneNumberSecondary() {
		return rowAssignPhoneNumberSecondary;
	}

	public void setRowAssignPhoneNumberSecondary(String rowAssignPhoneNumberSecondary) {
		this.rowAssignPhoneNumberSecondary = rowAssignPhoneNumberSecondary;
	}

	public String getRowAssignPhoneNumberTrinary() {
		return rowAssignPhoneNumberTrinary;
	}

	public void setRowAssignPhoneNumberTrinary(String rowAssignPhoneNumberTrinary) {
		this.rowAssignPhoneNumberTrinary = rowAssignPhoneNumberTrinary;
	}

	public String getRowAssignWorkNature() {
		return rowAssignWorkNature;
	}

	public void setRowAssignWorkNature(String rowAssignWorkNature) {
		this.rowAssignWorkNature = rowAssignWorkNature;
	}

	public String getRowAssignWorkScope() {
		return rowAssignWorkScope;
	}

	public void setRowAssignWorkScope(String rowAssignWorkScope) {
		this.rowAssignWorkScope = rowAssignWorkScope;
	}

	public String getScreenTitle() {
		return screenTitle;
	}

	public void setScreenTitle(String screenTitle) {
		this.screenTitle = screenTitle;
	}

	public String getDepartmentType() {
		return DepartmentTypeEnum.REGION.getCode() + "," + DepartmentTypeEnum.DIRECTORATE.getCode();
	}

	public Long getHeadQuarterId() throws BusinessException {
		return WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId();
	}

	public boolean isRegionSelected() {
		return regionSelected;
	}

	public void setRegionSelected(boolean regionSelected) {
		this.regionSelected = regionSelected;
	}

	public boolean isSectorSelected() {
		return sectorSelected;
	}

	public void setSectorSelected(boolean sectorSelected) {
		this.sectorSelected = sectorSelected;
	}

	public boolean isReChangeCode() {
		return reChangeCode;
	}

	public void setReChangeCode(boolean reChangeCode) {
		this.reChangeCode = reChangeCode;
	}

	public boolean isReAssigned() {
		return isReAssigned;
	}

	public void setReAssigned(boolean isReAssigned) {
		this.isReAssigned = isReAssigned;
	}

	public String getDepsList() {
		return depsList;
	}

	public void setDepsList(String depsList) {
		this.depsList = depsList;
	}

	public Boolean getEditprivilege() {
		return editprivilege;
	}

	public void setEditprivilege(Boolean editprivilege) {
		this.editprivilege = editprivilege;
	}
}
