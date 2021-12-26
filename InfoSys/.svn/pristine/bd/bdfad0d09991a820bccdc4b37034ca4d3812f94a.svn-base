package com.code.ui.backings.assignment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import com.code.dal.orm.assignment.AssignmentAgentClass;
import com.code.dal.orm.assignment.AssignmentData;
import com.code.dal.orm.assignment.AssignmentDetailData;
import com.code.dal.orm.attach.Attachment;
import com.code.dal.orm.security.UserMenuActionData;
import com.code.dal.orm.setup.BankBranchData;
import com.code.dal.orm.setup.NonEmployeeData;
import com.code.dal.orm.setup.SetupClass;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.AssignmentStatusEnum;
import com.code.enums.ClassesEnum;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.EntityNameEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.InfoSourceTypeEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.PaymentMethodEnum;
import com.code.enums.ServiceCodesEnum;
import com.code.enums.SessionAttributesEnum;
import com.code.enums.UserMenuActionsEnum;
import com.code.enums.WFPositionsEnum;
import com.code.enums.WFTaskActionsEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.attach.AttachmentService;
import com.code.services.attach.AttachmentServiceCallBack;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.assignment.AssignmentNonEmployeeService;
import com.code.services.infosys.assignment.AssignmentService;
import com.code.services.log4j.Log4j;
import com.code.services.security.SecurityService;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.NonEmployeeService;
import com.code.services.setup.SetupService;
import com.code.services.util.CommonService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.WFPositionService;
import com.code.services.workflow.assignment.NonEmployeesAssignementWorkFlow;
import com.code.services.workflow.surveillance.SurveillanceWorkFlow;
import com.code.ui.backings.base.WFBaseBacking;
import com.code.ui.util.EncryptionUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "nonEmployeeAssginment")
@ViewScoped
public class NonEmployeesAssignment extends WFBaseBacking implements Serializable {
	private AssignmentData assignmentData;
	private AssignmentDetailData assignmentDetailData;
	private List<String> assignmentReasonsList;
	private List<AssignmentAgentClass> assignmentAgentClassesList;
	private List<BankBranchData> bankBranchesDataList;
	private String remarks;
	private Integer pageMode = 1;
	private String screenTitle;
	private boolean isReAssigned;
	private boolean reAssignCoop;
	private boolean regionSelected;
	private boolean sectorSelected;
	private boolean isSaved;
	private String depsList;

	private String fileArchivingParam;
	private String downloadFileParamId;
	private List<Attachment> attachmentList;
	private String selectedDownloadFileId;
	private boolean openDownloadPopupFlag;
	private boolean openDownloadDialogueFlag;
	private String uploadFlag = getParameterizedMessage("label_no");
	private Integer reportMode = 1; // 1- Assignment 2-ReAssignment
	private Long nonEmployeeId = (long) FlagsEnum.OFF.getCode();
	private Long nonEmployeeIdentity = (long) FlagsEnum.OFF.getCode();
	private Boolean editprivilege = false;
	private Boolean wfInitFlag = false;
	private Integer assignmentTypeEditable;
	private String agentClassEditable;
	private boolean deletePrivilage = false;

	/**
	 * Default Constructor and Initializer
	 */
	public NonEmployeesAssignment() {
		super.init();
		this.init();
		try {
			UserMenuActionData assignmentEditAction = SecurityService.getAction(loginEmpData.getEmpId(), UserMenuActionsEnum.NON_EMPLOYEE_ASSIGNMENT_EDIT.getCode(), FlagsEnum.ALL.getCode());
			if (assignmentEditAction != null) {
				editprivilege = true;
			}
			UserMenuActionData deleteAttachmentAction = SecurityService.getAction(loginEmpData.getEmpId(), UserMenuActionsEnum.DELETE_ATTACHMENT_PERMISSION.getCode(), FlagsEnum.ALL.getCode());
			if (deleteAttachmentAction != null) {
				setDeletePrivilage(true);
			}
			Long assignmentId = null;
			assignmentAgentClassesList = AssignmentService.getAgentClasses();
			bankBranchesDataList = CommonService.getBankBranches();
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

			Integer mode = 0;
			if (getRequest().getAttribute("mode") != null) {
				mode = (Integer) getRequest().getAttribute("mode");
			}
			if (getRequest().getAttribute("assignmentId") != null) {
				assignmentId = (Long) getRequest().getAttribute("assignmentId");
			}
			if (getRequest().getAttribute("object") != null) {
				assignmentDetailData = (AssignmentDetailData) getRequest().getAttribute("object");
			}
			Date currDate = HijriDateService.getHijriSysDate();
			// Check Page Roles
			if (currentTask == null) {
				if (assignmentId == null) {
					pageMode = 1;
					assignmentDetailData = new AssignmentDetailData();
					assignmentData.setRequestDate(currDate);
					assignmentData.setStartDate(currDate);
					// assignmentData.setOfficerId(loginEmpData.getEmpId());
					// assignmentData.setOfficerName(loginEmpData.getFullName());
					if(assignmentAgentClassesList != null && assignmentAgentClassesList.size() > 1) {
					assignmentDetailData.setAgentClass(assignmentAgentClassesList.get(0).getCode());
					assignmentDetailData.setMonthlyReward(assignmentAgentClassesList.get(0).getValue());
					}
					assignmentDetailData.setPaymentMethod(PaymentMethodEnum.BANK_ACCOUNT.getCode());
					assignmentDetailData.setLocation(0);
					assignmentDetailData.setAgentType(0);
					assignmentDetailData.setType(InfoSourceTypeEnum.ASSIGNMENT.getCode());
					assignmentDetailData.setOfficerId(loginEmpData.getEmpId());
					assignmentDetailData.setOfficerName(loginEmpData.getFullName());
					setRegionSector();
					uploadFlag = getParameterizedMessage("label_yes");
				} else {
					if (assignmentDetailData != null && mode == 1) {
						reportMode = 2;
						screenTitle = getParameterizedMessage("title_nonEmployeeAssignmentRe");
						pageMode = 1;
						assignmentData = AssignmentService.getAssignments(assignmentId, null, null, null, null, null).get(0);
						assignmentData.setId(null);
						assignmentData.setwFInstanceId(null);
						assignmentData.setRequestDate(currDate);
						assignmentData.setRequestNumber(null);
						assignmentData.setStartDate(currDate);
						// assignmentData.setOfficerId(loginEmpData.getEmpId());
						// assignmentData.setOfficerName(loginEmpData.getFullName());
						setRegionSector();
						// Load Last Assignment for this Officer
						assignmentDetailData = AssignmentService.getLastAssignmentDetailsData(loginEmpData.getEmpId(), null, null, assignmentDetailData.getIdentity(), null, null, null, null, AssignmentStatusEnum.APPROVED.getCode(), null, FlagsEnum.OFF.getCode(), assignmentDetailData.getType()).get(0);
						assignmentDetailData.setStartDate(currDate);
						assignmentDetailData.setId(null);
						assignmentDetailData.setAgentCode(AssignmentService.getLastAssignmentDetailsData(null, null, null, assignmentDetailData.getIdentity(), null, null, assignmentData.getRegionId(), null, AssignmentStatusEnum.APPROVED.getCode(), null, FlagsEnum.OFF.getCode(), assignmentDetailData.getType()).get(0).getAgentCode());
						isReAssigned = true;
						reAssignCoop = assignmentDetailData.getType().equals(InfoSourceTypeEnum.ASSIGNMENT.getCode());
						uploadFlag = getParameterizedMessage("label_yes");
					} else if (assignmentDetailData != null && mode == 2) {
						pageMode = 1;
						isSaved = true;
						assignmentData = AssignmentService.getAssignments(assignmentId, null, null, null, null, null).get(0);
					} else if (assignmentDetailData != null && mode == 3) {
						screenTitle = getParameterizedMessage("title_nonEmployeeAssignmentView");
						pageMode = 4;
						assignmentData = AssignmentService.getAssignments(assignmentId, null, null, null, null, null).get(0);
						assignmentTypeEditable = assignmentDetailData.getType();
						agentClassEditable = assignmentDetailData.getAgentClass();

						assignmentDetailData.setTypeTemp(assignmentDetailData.getType());
						assignmentDetailData.setAgentClassTemp(assignmentDetailData.getAgentClass());
						assignmentDetailData.setMonthlyRewardTemp(assignmentDetailData.getMonthlyReward());
						assignmentDetailData.setPaymentMethodTemp(assignmentDetailData.getPaymentMethod());
						assignmentDetailData.setBankBranchIdTemp(assignmentDetailData.getBankBranchId());
						assignmentDetailData.setIbanTemp(assignmentDetailData.getIban());
						assignmentDetailData.setFullNameTemp(assignmentDetailData.getFullName());
					}
					// Set employees reasons list
					if (assignmentDetailData.getReasons() != null && !assignmentDetailData.getReasons().isEmpty()) {
						String[] reasons = assignmentDetailData.getReasons().split(",");
						assignmentDetailData.setReasonsList(Arrays.asList(reasons));
					}
				}
			} else {
				if (role.equals(WFTaskRolesEnum.GENERAL_INTELLIGENCE_MANAGER.getCode()) || role.equals(WFTaskRolesEnum.REGION_INTELLIGENCE_DEP_HEAD.getCode()) || role.equals(WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER.getCode()) || role.equals(WFTaskRolesEnum.GENERAL_DIRECTOR.getCode())) {
					if (currentTask.getFlexField3() != null && currentTask.getFlexField3().equals("true")) {
						reportMode = 2;
						screenTitle = getParameterizedMessage("title_nonEmployeeAssignmentApproveRe");
					} else if (currentTask.getFlexField2() != null && currentTask.getFlexField2().equals("true")) {
						screenTitle = getParameterizedMessage("title_nonEmployeeAssignmentApproveEd");
					} else {
						screenTitle = getParameterizedMessage("title_nonEmployeeAssignmentApprove");
					}
					pageMode = 3;
				} else {
					if (currentTask.getFlexField3() != null && currentTask.getFlexField3().equals("true")) {
						reportMode = 2;
						screenTitle = getParameterizedMessage("title_nonEmployeeAssignmentRe");
					}
					pageMode = 2;
					if (prevTasks.get(prevTasks.size() - 1).getAction().equals(WFTaskActionsEnum.REJECT.getCode())) {
						pageMode = 1;
						if (currentTask.getFlexField3() != null && currentTask.getFlexField3().equals("true")) {
							reportMode = 2;
							screenTitle = getParameterizedMessage("title_nonEmployeeAssignmentRejectedRe");
						} else if (currentTask.getFlexField2() != null && currentTask.getFlexField2().equals("true")) {
							screenTitle = getParameterizedMessage("title_nonEmployeeAssignmentRejectedEd");
						} else {
							screenTitle = getParameterizedMessage("title_nonEmployeeAssignmentRejected");
						}
					}
				}
				assignmentData = AssignmentService.getAssignments(null, instance.getInstanceId(), null, null, null, null).get(0);
				assignmentDetailData = AssignmentService.getAssignmentDetailsDataByAssignmentId(assignmentData.getId()).get(0);
				// Set employees reasons list
				if (assignmentDetailData.getReasons() != null && !assignmentDetailData.getReasons().isEmpty()) {
					String[] reasons = assignmentDetailData.getReasons().split(",");
					assignmentDetailData.setReasonsList(Arrays.asList(reasons));
				}
			}

			if (pageMode == 1 || editprivilege) {
				long regionId = DepartmentService.getDepartment(loginEmpData.getActualDepartmentId()).getRegionId();
				updateDepsList(regionId, assignmentDetailData == null ? null : assignmentDetailData.getSectorId(), assignmentDetailData == null ? null : assignmentDetailData.getUnitId());
			}
			if (assignmentDetailData.getIdentity() != null && !assignmentDetailData.getIdentity().isEmpty()) {
				List<NonEmployeeData> nonEmps = NonEmployeeService.getNonEmployee(Long.parseLong(assignmentDetailData.getIdentity()), null);
				if (!nonEmps.isEmpty()) {
					nonEmployeeId = nonEmps.get(0).getId();
					nonEmployeeIdentity = nonEmps.get(0).getIdentity();
				}
			}
		  } catch (BusinessException e) {
			   this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		  } catch (Exception e) {
			   Log4j.traceErrorException(NonEmployeesAssignment.class, e, "NonEmployeesAssignment");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Initialize bean variables
	 */
	public void init() {
		assignmentData = new AssignmentData();
		assignmentReasonsList = new ArrayList<String>();
		assignmentAgentClassesList = new ArrayList<AssignmentAgentClass>();
		bankBranchesDataList = new ArrayList<BankBranchData>();
		screenTitle = getParameterizedMessage("title_nonEmployeeAssignment");
		depsList = null;
		wfInitFlag = false;
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
		}catch (Exception e) {
			 Log4j.traceErrorException(NonEmployeesAssignment.class, e, "NonEmployeesAssignment");
			 this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	/**
	 * Change Monthly Reward Based on agent class
	 * 
	 * @param event
	 */
	public void changeMonthlyReward(AjaxBehaviorEvent event) {
		if (assignmentTypeEditable != null && assignmentTypeEditable.equals(getAssignment())) { // Masdr
			if (!(agentClassEditable.equals(assignmentDetailData.getAgentClass()))) {
				wfInitFlag = true;
			} else {
				wfInitFlag = false;
			}
		}
		for (int i = 0; i < assignmentAgentClassesList.size(); i++) {
			if (assignmentAgentClassesList.get(i).getCode().equals(assignmentDetailData.getAgentClass())) {
				assignmentDetailData.setMonthlyReward(assignmentAgentClassesList.get(i).getValue());
				return;
			}
		}
	}

	/**
	 * Change Payment Method Based on Assignment Type
	 */
	public void changePaymentMethod() {
		if (assignmentDetailData.getType().equals(InfoSourceTypeEnum.ASSIGNMENT.getCode())) {
			assignmentDetailData.setAgentClass(assignmentAgentClassesList.get(0).getCode());
			assignmentDetailData.setMonthlyReward(assignmentAgentClassesList.get(0).getValue());
			assignmentDetailData.setPaymentMethod(PaymentMethodEnum.BANK_ACCOUNT.getCode());
		} else {
			assignmentDetailData.setPaymentMethod(null);
			assignmentDetailData.setAgentClass(null);
			assignmentDetailData.setBankBranchId(null);
			assignmentDetailData.setMonthlyReward((double) 0);
		}
		if (assignmentTypeEditable != null) {
			if (assignmentTypeEditable.equals(getAssignment())) { // Masdr
				if (assignmentDetailData.getType().equals(getCooperetor())) {
					wfInitFlag = true;
				} else if (assignmentDetailData.getType().equals(getAssignment())) {
					if (agentClassEditable.equals(assignmentDetailData.getAgentClass())) {
						wfInitFlag = false;
					} else {
						wfInitFlag = true;
					}
				}
			} else if (assignmentTypeEditable.equals(getCooperetor())) { // Mota3awan
				if (assignmentDetailData.getType().equals(getCooperetor())) {
					wfInitFlag = false;
				} else if (assignmentDetailData.getType().equals(getAssignment())) {
					wfInitFlag = true;
				}
			}
		}

	}

	/**
	 * Save and Start Assignment order work flow
	 */
	public String saveAndSend() {
		try {
			if (assignmentData.getId() != null && role.equals(WFTaskRolesEnum.REQUESTER.getCode())) {
				if (assignmentData.getStartDate() != null && assignmentData.getStartDate().compareTo(assignmentData.getRequestDate()) < 0) {
					this.setServerSideErrorMessages(getParameterizedMessage("error_assignmentNonEmpInvalideDate"));
					return null;
				}
			}
			else if (assignmentData.getStartDate() != null && assignmentData.getStartDate().compareTo(HijriDateService.getHijriSysDate()) < 0) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_assignmentNonEmpInvalideDate"));
				return null;
			}
			Date endDate = HijriDateService.addSubHijriMonthsDays(assignmentData.getStartDate(), assignmentData.getPeriod(), 0);
			assignmentDetailData.setStartDate(assignmentData.getStartDate());
			assignmentDetailData.setPeriod(assignmentData.getPeriod());
			assignmentDetailData.setEndDate(endDate);
			assignmentDetailData.setApprovedEndDate(endDate);
			assignmentDetailData.setStatus(AssignmentStatusEnum.UNDER_APPROVAL.getCode());
			List<AssignmentDetailData> tempDetailList = new ArrayList<AssignmentDetailData>();
			tempDetailList.add(assignmentDetailData);

			if (assignmentData.getId() != null && role.equals(WFTaskRolesEnum.REQUESTER.getCode())) {
				AssignmentNonEmployeeService.updateAssignments(currentTask, assignmentData, tempDetailList, loginEmpData, isReAssigned);
			} else {
				AssignmentNonEmployeeService.addAssignments(assignmentData, tempDetailList, loginEmpData, isReAssigned);
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			 Log4j.traceErrorException(NonEmployeesAssignment.class, e, "NonEmployeesAssignment");
			 this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			 return null;
		}

		if (uploadFlag.equals(getParameterizedMessage("label_yes"))) {
			getRequest().setAttribute(SessionAttributesEnum.FILE_ARCHIVING_PARAM.getCode(), EntityNameEnum.ASSIGNMENT.getCode() + "_" + assignmentData.getId());
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
				NonEmployeesAssignementWorkFlow.approveRegionDepHead(currentTask, assignmentData, assignmentDetailData, loginEmpData);
			} else if (role.equals(WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER.getCode())) {
				NonEmployeesAssignementWorkFlow.approveRegionManager(currentTask, assignmentData, assignmentDetailData, loginEmpData);
			} else if (role.equals(WFTaskRolesEnum.GENERAL_INTELLIGENCE_MANAGER.getCode())) {
				NonEmployeesAssignementWorkFlow.approveHQManager(currentTask, assignmentData, assignmentDetailData);
			} else if (role.equals(WFTaskRolesEnum.GENERAL_DIRECTOR.getCode())) {
				assignmentDetailData.setStatus(AssignmentStatusEnum.APPROVED.getCode());
				NonEmployeesAssignementWorkFlow.approveHQGeneralManager(currentTask, assignmentData, assignmentDetailData);
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
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
		if (remarks != null && remarks.trim().isEmpty()) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_rejectReasonMandatory"));
			return null;
		}
		try {
			currentTask.setNotes(remarks);
			assignmentDetailData.setStatus(AssignmentStatusEnum.REJECTED.getCode());
			if (assignmentDetailData.getTypeTemp() != null) {
				assignmentDetailData.setStatus(AssignmentStatusEnum.APPROVED.getCode());
				assignmentDetailData.setType(assignmentDetailData.getTypeTemp());
				assignmentDetailData.setAgentClass(assignmentDetailData.getAgentClassTemp());
				assignmentDetailData.setMonthlyReward(assignmentDetailData.getMonthlyRewardTemp());
				assignmentDetailData.setPaymentMethod(assignmentDetailData.getPaymentMethodTemp());
				assignmentDetailData.setBankBranchId(assignmentDetailData.getBankBranchIdTemp());
				assignmentDetailData.setIban(assignmentDetailData.getIbanTemp());
				assignmentDetailData.setFullName(assignmentDetailData.getFullNameTemp());
			}

			assignmentDetailData.setTypeTemp(null);
			assignmentDetailData.setAgentClassTemp(null);
			assignmentDetailData.setMonthlyRewardTemp(null);
			assignmentDetailData.setPaymentMethodTemp(null);
			assignmentDetailData.setBankBranchIdTemp(null);
			assignmentDetailData.setIbanTemp(null);
			assignmentDetailData.setFullNameTemp(null);

			NonEmployeesAssignementWorkFlow.rejectNonEmployeeAssignment(currentTask, assignmentData, assignmentDetailData);
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
	 * Save Changes
	 */
	public String saveChanges() {
		try {
			AssignmentNonEmployeeService.updateAssignmentDetails(assignmentDetailData, assignmentData, loginEmpData, wfInitFlag || !(assignmentDetailData.getFullName().equals(assignmentDetailData.getFullNameTemp())));

		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		if (wfInitFlag == true) {
			return NavigationEnum.INBOX.toString();
		}
		return NavigationEnum.NON_EMPLOYEE_ASSIGNEMNT_SEARCH.toString();
	}

	/**
	 * Prepare Upload Parameters
	 * 
	 * @param nonEmployeeAssignmentId
	 */
	public void getUploadParam(long nonEmployeeAssignmentId) {
		try {
			String requestURL = getRequest().getRequestURL() + "";
			String url = requestURL.replace(getRequest().getServletPath(), "");
			String serviceURL = url + AttachmentServiceCallBack.SERVICE_URL;

			fileArchivingParam = EncryptionUtil.encryptSymmetrically("code=" + ServiceCodesEnum.INFO_SYS.getCode() + "&token=" + EntityNameEnum.ASSIGNMENT.getCode() + "_" + nonEmployeeAssignmentId + "&serviceurl=" + serviceURL);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	/**
	 * Prepare Download parameters
	 * 
	 * @param attachmentId
	 */
	public void getDownloadParam(String attachmentId) {
		try {
			downloadFileParamId = EncryptionUtil.encryptSymmetrically("contentId=" + attachmentId);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	/**
	 * Delete Attachment
	 * 
	 * @param attachment
	 */
	public void deleteAttachment(Attachment attachment) {
		try {
			AttachmentService.deleteFileNetAttachment(attachment);
			attachmentList.remove(attachment);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Get Attachments Ids and prepare for downloading
	 * 
	 * @throws BusinessException
	 */
	public void getDownloadedAttachmentsIdList(long nonEmployeeAssignmentId) throws BusinessException {
		try {
			attachmentList = AttachmentService.getAttachments(EntityNameEnum.ASSIGNMENT.getCode(), nonEmployeeAssignmentId);
			openDownloadPopupFlag = false;
			openDownloadDialogueFlag = false;
			if (attachmentList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noAttachments"));
			} else if (attachmentList.size() == 1) {
				openDownloadPopupFlag = true;
				downloadFileParamId = EncryptionUtil.encryptSymmetrically("contentId=" + attachmentList.get(0).getAttachmentId());
			} else {
				openDownloadDialogueFlag = true;
			}
		} catch (Exception e) {
			 Log4j.traceErrorException(NonEmployeesAssignment.class, e, "NonEmployeesAssignment");
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	/**
	 * print details
	 */
	public void print() {
		try {
			if (reportMode == 1) {
				byte[] bytes = AssignmentService.getNonEmployeeAssignmentDetailBytes(assignmentData.getId(), loginEmpData.getFullName());
				super.print(bytes, "nonEmployeeAssignDetail");
			} else if (reportMode == 2) {
				byte[] bytes = AssignmentService.getNonEmployeeReAssignmentDetailBytes(assignmentData.getId(), loginEmpData.getFullName());
				super.print(bytes, "nonEmployeeReAssignDetail");
			}
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
		}catch (Exception e) {
			 Log4j.traceErrorException(NonEmployeesAssignment.class, e, "NonEmployeesAssignment");
			 this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}

	}

	public AssignmentData getAssignmentData() {
		return assignmentData;
	}

	public void setAssignmentData(AssignmentData assignmentData) {
		this.assignmentData = assignmentData;
	}

	public AssignmentDetailData getAssignmentDetailData() {
		return assignmentDetailData;
	}

	public void setAssignmentDetailData(AssignmentDetailData assignmentDetailData) {
		this.assignmentDetailData = assignmentDetailData;
	}

	public List<String> getAssignmentReasonsList() {
		return assignmentReasonsList;
	}

	public void setAssignmentReasonsList(List<String> assignmentReasonsList) {
		this.assignmentReasonsList = assignmentReasonsList;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getPageMode() {
		return pageMode;
	}

	public void setPageMode(Integer pageMode) {
		this.pageMode = pageMode;
	}

	public String getScreenTitle() {
		return screenTitle;
	}

	public void setScreenTitle(String screenTitle) {
		this.screenTitle = screenTitle;
	}

	public List<AssignmentAgentClass> getAssignmentAgentClassesList() {
		return assignmentAgentClassesList;
	}

	public void setAssignmentAgentClassesList(List<AssignmentAgentClass> assignmentAgentClassesList) {
		this.assignmentAgentClassesList = assignmentAgentClassesList;
	}

	public List<BankBranchData> getBankBranchesDataList() {
		return bankBranchesDataList;
	}

	public void setBankBranchesDataList(List<BankBranchData> bankBranchesDataList) {
		this.bankBranchesDataList = bankBranchesDataList;
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

	public boolean isSaved() {
		return isSaved;
	}

	public void setSaved(boolean isSaved) {
		this.isSaved = isSaved;
	}

	public boolean isReAssigned() {
		return isReAssigned;
	}

	public void setReAssigned(boolean isReAssigned) {
		this.isReAssigned = isReAssigned;
	}

	public boolean isReAssignCoop() {
		return reAssignCoop;
	}

	public void setReAssignCoop(boolean reAssignCoop) {
		this.reAssignCoop = reAssignCoop;
	}

	public String getDepsList() {
		return depsList;
	}

	public void setDepsList(String depsList) {
		this.depsList = depsList;
	}

	public String getFileArchivingParam() {
		return fileArchivingParam;
	}

	public void setFileArchivingParam(String fileArchivingParam) {
		this.fileArchivingParam = fileArchivingParam;
	}

	public List<Attachment> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<Attachment> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public String getSelectedDownloadFileId() {
		return selectedDownloadFileId;
	}

	public void setSelectedDownloadFileId(String selectedDownloadFileId) {
		this.selectedDownloadFileId = selectedDownloadFileId;
	}

	public boolean isOpenDownloadPopupFlag() {
		return openDownloadPopupFlag;
	}

	public void setOpenDownloadPopupFlag(boolean openDownloadPopupFlag) {
		this.openDownloadPopupFlag = openDownloadPopupFlag;
	}

	public String getDownloadFileParamId() {
		return downloadFileParamId;
	}

	public void setDownloadFileParamId(String downloadFileParamId) {
		this.downloadFileParamId = downloadFileParamId;
	}

	public boolean isOpenDownloadDialogueFlag() {
		return openDownloadDialogueFlag;
	}

	public void setOpenDownloadDialogueFlag(boolean openDownloadDialogueFlag) {
		this.openDownloadDialogueFlag = openDownloadDialogueFlag;
	}

	public String getBoolServerUploadPath() {
		return InfoSysConfigurationService.getBoolServerUploadPath();
	}

	public String getBoolServerDownloadPath() {
		return InfoSysConfigurationService.getBoolServerDownloadPath();
	}

	public String getUploadFlag() {
		return uploadFlag;
	}

	public void setUploadFlag(String uploadFlag) {
		this.uploadFlag = uploadFlag;
	}

	public Integer getReportMode() {
		return reportMode;
	}

	public void setReportMode(Integer reportMode) {
		this.reportMode = reportMode;
	}

	public Long getNonEmployeeId() {
		return nonEmployeeId;
	}

	public void setNonEmployeeId(Long nonEmployeeId) {
		this.nonEmployeeId = nonEmployeeId;
	}

	public Long getNonEmployeeIdentity() {
		return nonEmployeeIdentity;
	}

	public void setNonEmployeeIdentity(Long nonEmployeeIdentity) {
		this.nonEmployeeIdentity = nonEmployeeIdentity;
	}

	public Boolean getEditprivilege() {
		return editprivilege;
	}

	public void setEditprivilege(Boolean editprivilege) {
		this.editprivilege = editprivilege;
	}

	public Boolean getWfInitFlag() {
		return wfInitFlag;
	}

	public void setWfInitFlag(Boolean wfInitFlag) {
		this.wfInitFlag = wfInitFlag;
	}

	public Integer getAssignment() {
		return InfoSourceTypeEnum.ASSIGNMENT.getCode();
	}

	public Integer getCooperetor() {
		return InfoSourceTypeEnum.COOPERATOR.getCode();
	}

	public boolean isDeletePrivilage() {
		return deletePrivilage;
	}

	public void setDeletePrivilage(boolean deletePrivilage) {
		this.deletePrivilage = deletePrivilage;
	}
}