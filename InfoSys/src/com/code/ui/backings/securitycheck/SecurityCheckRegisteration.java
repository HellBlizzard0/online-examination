package com.code.ui.backings.securitycheck;

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

import com.code.dal.orm.attach.Attachment;
import com.code.dal.orm.securitycheck.SecurityCheckData;
import com.code.dal.orm.securitycheck.SecurityCheckEmployeeData;
import com.code.dal.orm.securitycheck.SecurityCheckNonEmployeeData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.enums.EntityNameEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.RequestSourceEnum;
import com.code.enums.SecurityCheckReasonEnum;
import com.code.enums.SecurityCheckStatusEnum;
import com.code.enums.ServiceCodesEnum;
import com.code.enums.WFTaskActionsEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.attach.AttachmentService;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.securitycheck.SecurityCheckService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.SetupService;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.WFBaseBacking;
import com.code.ui.util.EncryptionUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "securityCheckRegisteration")
@ViewScoped
public class SecurityCheckRegisteration extends WFBaseBacking implements Serializable {
	private int rowsCount = 10;
	private int pageMode;
	private boolean processed;
	private int viewMode = 1; // 1 for employees, 2 for non employees

	private Set<Long> employeeIdsSet;
	private Set<Long> nonEmployeeIdsSet;

	private List<SetupDomain> incomingSideList;
	private List<SetupDomain> noteSideList;
	private List<SetupDomain> securityCheckReasonList;

	private List<SecurityCheckEmployeeData> employeeList;
	private List<SecurityCheckEmployeeData> employeeDeletedList;
	private List<SecurityCheckNonEmployeeData> nonEmployeeList;
	private List<SecurityCheckNonEmployeeData> nonEmployeeDeletedList;

	private SecurityCheckData securityCheck;
	private SecurityCheckEmployeeData insertedEmployee;
	private SecurityCheckNonEmployeeData insertedNonEmployee;

	private String activeIndex = "-1";

	private String fileArchivingParam;
	private String downloadFileParamId;
	private List<Attachment> attachmentList;
	private boolean openDownloadPopupFlag;
	private boolean openDownloadDialogueFlag;
	private String boolServerUploadPath;
	private String boolServerDownloadPath;
	private Long regionId;

	private List<SelectItem> requestSourceEnumList = new ArrayList<SelectItem>();

	/**
	 * Constructor
	 */
	public SecurityCheckRegisteration() {
		super.init();
		try {
			regionId = DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());

		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
		employeeDeletedList = new ArrayList<SecurityCheckEmployeeData>();
		nonEmployeeDeletedList = new ArrayList<SecurityCheckNonEmployeeData>();

		if (getRequest().getParameter("viewMode") != null) {
			viewMode = (Integer.valueOf(getRequest().getParameter("viewMode")));
			activeIndex = viewMode == 1 ? "0" : "1";
		}
		if (getRequest().getAttribute("mode") != null) { // get security check by id
			try {
				securityCheck = SecurityCheckService.getSecurityCheckById(Long.valueOf(getRequest().getAttribute("mode").toString()), FlagsEnum.ALL.getCode());
				employeeList = SecurityCheckService.getSecurityCheckEmployees(securityCheck.getId());
				nonEmployeeList = SecurityCheckService.getSecurityCheckNonEmployees(securityCheck.getId());

				if (!employeeList.isEmpty() && !nonEmployeeList.isEmpty()) {
					viewMode = 0;
				} else if (employeeList.size() > 0) {
					viewMode = 1;
				} else {
					viewMode = 2;
				}
			} catch (NumberFormatException e) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			} catch (BusinessException e) {
				this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			} catch (Exception e) {
				Log4j.traceErrorException(SecurityCheckRegisteration.class, e, "SecurityCheckRegisteration");
				this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			}
		} else if (currentTask != null) { // get security check by wfInstanceId
			try {
				securityCheck = SecurityCheckService.getSecurityCheckById(FlagsEnum.ALL.getCode(), instance.getInstanceId());
				employeeList = SecurityCheckService.getSecurityCheckEmployees(securityCheck.getId());
				nonEmployeeList = SecurityCheckService.getSecurityCheckNonEmployees(securityCheck.getId());
				if (!employeeList.isEmpty() && !nonEmployeeList.isEmpty()) {
					viewMode = 0;
				} else if (employeeList.size() > 0) {
					viewMode = 1;
				} else {
					viewMode = 2;
				}
			} catch (BusinessException e) {
				this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			} catch (Exception e) {
				Log4j.traceErrorException(SecurityCheckRegisteration.class, e, "SecurityCheckRegisteration");
				this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			}
		} else { // add new security check
			securityCheck = new SecurityCheckData();
			employeeList = new ArrayList<SecurityCheckEmployeeData>();
			nonEmployeeList = new ArrayList<SecurityCheckNonEmployeeData>();
		}

		// Check if the processing happened yet
		processed = false;
		if (!employeeList.isEmpty() && employeeList.get(0).getNotesExist() != null) {
			processed = true;
		} else if (!nonEmployeeList.isEmpty() && nonEmployeeList.get(0).getNotesExist() != null) {
			processed = true;
		}

		if (currentTask == null) {
			if (securityCheck.getwFInstanceId() == null) { // edit mode
				pageMode = 2;
			} else { // view mode
				pageMode = 1;
			}
		} else if (role.equals(WFTaskRolesEnum.PROCESSING.getCode())) { // processing mode
			try {
				noteSideList = SetupService.getDomains(ClassesEnum.NOTE_SIDES.getCode());
				SecurityCheckService.countEmployeesNotes(employeeList);
			} catch (BusinessException e) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			}
			pageMode = 3;
		} else if (role.equals(WFTaskRolesEnum.REQUESTER.getCode())) {
			pageMode = 2;
		} else { // view mode
			pageMode = 1;
		}

		if (pageMode == 2) {
			try {
				employeeIdsSet = new HashSet<Long>();
				nonEmployeeIdsSet = new HashSet<Long>();
				insertedEmployee = new SecurityCheckEmployeeData();
				insertedNonEmployee = new SecurityCheckNonEmployeeData();
				boolean edit = (securityCheck.getId() != null);
				if (edit) {
					for (SecurityCheckEmployeeData emp : employeeList) {
						employeeIdsSet.add(emp.getEmployeeId());
					}
					for (SecurityCheckNonEmployeeData nonEmp : nonEmployeeList) {
						nonEmployeeIdsSet.add(nonEmp.getNonEmployeeId());
					}
				} else {
					securityCheck.setRequestDate(HijriDateService.getHijriSysDate());
				}
				incomingSideList = SetupService.getDomains(ClassesEnum.INCOMING_SIDES.getCode());
			} catch (BusinessException e) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			} catch (Exception e) {
				Log4j.traceErrorException(SecurityCheckRegisteration.class, e, "SecurityCheckRegisteration");
				this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			}
		}

		try {
			if (regionId != null) {
				requestSourceEnumList.add(new SelectItem(RequestSourceEnum.REGION_DIRECTOR.getCode(), RequestSourceEnum.REGION_DIRECTOR.getCode()));
			}
			if (viewMode == 1) {
				securityCheckReasonList = SetupService.getDomains(ClassesEnum.EMP_SEC_SURV_RESON.getCode());
				requestSourceEnumList.add(new SelectItem(RequestSourceEnum.GENERAL_PROTECTION_DIRECTORATE.getCode(), RequestSourceEnum.GENERAL_PROTECTION_DIRECTORATE.getCode()));

			} else {
				securityCheckReasonList = SetupService.getDomains(ClassesEnum.NONEMP_SEC_SURV_RESON.getCode());
				requestSourceEnumList.add(new SelectItem(RequestSourceEnum.GENERAL_INTELLIGENCE_DIRECTORATE.getCode(), RequestSourceEnum.GENERAL_INTELLIGENCE_DIRECTORATE.getCode()));

			}
			requestSourceEnumList.add(new SelectItem(RequestSourceEnum.OTHERS.getCode(), RequestSourceEnum.OTHERS.getCode()));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}

		if (!employeeList.isEmpty() && !nonEmployeeList.isEmpty()) {
			activeIndex = "0,1";
		} else if (!employeeList.isEmpty()) {
			activeIndex = "0";
		} else {
			activeIndex = "1";
		}

		boolServerUploadPath = InfoSysConfigurationService.getBoolServerUploadPath();
		boolServerDownloadPath = InfoSysConfigurationService.getBoolServerDownloadPath();
	}

	/**
	 * Initialize employee parameters
	 * 
	 * @param emp
	 */
	public void init(SecurityCheckEmployeeData emp) {
		emp.setNoteDetail(null);
		emp.setNoteSubject(null);
		emp.setDomainNoteSrcId(null);
	}

	/**
	 * Initialize nonemployee parameters
	 * 
	 * @param nonEmp
	 */
	public void init(SecurityCheckNonEmployeeData nonEmp) {
		nonEmp.setNoteDetail(null);
		nonEmp.setNoteSubject(null);
		nonEmp.setDomainNoteSrcId(null);
	}

	/**
	 * Insert employee into list
	 */
	public void insertEmployee() {
		if (employeeIdsSet.contains(insertedEmployee.getEmployeeId())) { // if the employee was already added
			this.setServerSideErrorMessages(getParameterizedMessage("error_employeeIsExist"));
		} else {
			employeeList.add(insertedEmployee);
			employeeIdsSet.add(insertedEmployee.getEmployeeId());
			insertedEmployee = new SecurityCheckEmployeeData();
		}
	}

	/**
	 * Insert nonemployee into list
	 */
	public void insertNonemployee() {
		if (nonEmployeeIdsSet.contains(insertedNonEmployee.getNonEmployeeId())) { // if the nonemployee was already inserted
			this.setServerSideErrorMessages(getParameterizedMessage("error_nonEmployeeIsExist"));
		} else {
			// TODOvinsertedNonEmployee.setSecurityCheckId(securityCheck.getId());
			nonEmployeeList.add(insertedNonEmployee);
			nonEmployeeIdsSet.add(insertedNonEmployee.getNonEmployeeId());
			insertedNonEmployee = new SecurityCheckNonEmployeeData();
		}
	}

	/**
	 * Delete employee from the table
	 * 
	 * @param employee
	 */
	public void deleteEmployee(SecurityCheckEmployeeData employee) {
		employeeList.remove(employee);
		employeeIdsSet.remove(employee.getEmployeeId());
		if (employee.getId() != null) {
			employeeDeletedList.add(employee);
		}
	}

	/**
	 * Delete nonemployee from the table
	 * 
	 * @param nonEmployee
	 */
	public void deleteNonEmployee(SecurityCheckNonEmployeeData nonEmployee) {
		nonEmployeeList.remove(nonEmployee);
		nonEmployeeIdsSet.remove(nonEmployee.getNonEmployeeId());
		if (nonEmployee.getId() != null) {
			nonEmployeeDeletedList.add(nonEmployee);
		}
	}

	/**
	 * Save and send the request
	 * 
	 * @return page to be directed to
	 */
	public String saveAndSendSecurityCheck() {
		try {
			securityCheck.setStatus(SecurityCheckStatusEnum.UNDER_APPROVAL.getCode());
			securityCheck.setRegionId(DepartmentService.getDepartment(loginEmpData.getActualDepartmentId()).getRegionId());
			if (regionId != null) {
				SecurityCheckService.initSecurityCheckRegion(securityCheck, employeeList, employeeDeletedList, nonEmployeeList, nonEmployeeDeletedList, loginEmpData, attachments, currentTask);
			} else {
				SecurityCheckService.initSecurityCheckHQ(securityCheck, employeeList, employeeDeletedList, nonEmployeeList, nonEmployeeDeletedList, loginEmpData, attachments, currentTask);
			}
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_securityCheckRegAndSendSuccess", securityCheck.getRequestNumber()));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityCheckRegisteration.class, e, "SecurityCheckRegisteration");
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			return null;
		}
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Save security check
	 * 
	 */
	public void saveSecurityCheck() {
		try {
			securityCheck.setStatus(SecurityCheckStatusEnum.SAVED.getCode());
			securityCheck.setRegionId(DepartmentService.getDepartment(loginEmpData.getActualDepartmentId()).getRegionId());
			SecurityCheckService.saveUpdateSecurityCheck(loginEmpData, securityCheck, employeeList, employeeDeletedList, nonEmployeeList, nonEmployeeDeletedList);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_securityCheckRegSuccess", securityCheck.getRequestNumber()));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Approve security check
	 * 
	 * @return page to be directed to
	 */
	public String approveSecurityCheck() {
		try {
			boolean isEmployee = viewMode == 1;
			if (regionId != null || role.equals(WFTaskRolesEnum.SECURITY_CHECK_GENERAL_INTELLIGENCE_MANAGER.getCode()) || role.equals(WFTaskRolesEnum.SECURITY_CHECK_GENERAL_DIRECTOR.getCode()) || role.equals(WFTaskRolesEnum.REGION_SECURITY_AFFAIRS_APPROVER_MANAGER.getCode()) || role.equals(WFTaskRolesEnum.REGION_GENERAL_PROTECTION_APPROVER_MANAGER.getCode())) {
				SecurityCheckService.doApproveRegion(currentTask, WFTaskActionsEnum.APPROVE, securityCheck.getSecurityCheck(), loginEmpData, attachments, isEmployee);
			} else {
				if (role.equals(WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER.getCode()) || role.equals(WFTaskRolesEnum.GENERAL_INTELLIGENCE_MANAGER.getCode()) || role.equals(WFTaskRolesEnum.SECURITY_AFFAIRS_MANAGER.getCode())) {
					SecurityCheckService.doApproveRejectDM(currentTask, WFTaskActionsEnum.APPROVE, securityCheck.getSecurityCheck(), loginEmpData, attachments, isEmployee);
				} else if (role.equals(WFTaskRolesEnum.REGION_DIRECTOR.getCode()) || role.equals(WFTaskRolesEnum.GENERAL_DIRECTOR.getCode())) {
					securityCheck.setStatus(SecurityCheckStatusEnum.UNDER_PROCCESSING.getCode());
					SecurityCheckService.doApproveRejectSM(currentTask, WFTaskActionsEnum.APPROVE, securityCheck.getSecurityCheck(), loginEmpData, attachments, isEmployee);
				} else if (role.equals(WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER_PROCESSING.getCode()) || role.equals(WFTaskRolesEnum.GENERAL_INTELLIGENCE_MANAGER_PROCESSING.getCode()) || role.equals(WFTaskRolesEnum.SECURITY_AFFAIRS_MANAGER_PROCESSING.getCode())) {
					SecurityCheckService.doApproveRejectProcessingDM(currentTask, WFTaskActionsEnum.APPROVE, securityCheck.getSecurityCheck(), loginEmpData, attachments, isEmployee);
				} else if (role.equals(WFTaskRolesEnum.REGION_DIRECTOR_PROCESSING.getCode()) || role.equals(WFTaskRolesEnum.GENERAL_DIRECTOR_PROCESSING.getCode())) {
					securityCheck.setStatus(SecurityCheckStatusEnum.APPROVED.getCode());
					SecurityCheckService.doApproveRejectProcessingSM(currentTask, WFTaskActionsEnum.APPROVE, securityCheck.getSecurityCheck(), loginEmpData, attachments, isEmployee);
				} else if (role.equals(WFTaskRolesEnum.REGION_SECURITY_INTELLIGENCE_MANAGER.getCode())) {
					SecurityCheckService.doApproveRejectRegionSecurity(currentTask, WFTaskActionsEnum.APPROVE, securityCheck.getSecurityCheck(), loginEmpData, attachments, isEmployee);
				} else if (role.equals(WFTaskRolesEnum.REGION_SECURITY_INTELLIGENCE_MANAGER_PROCESSING.getCode())) {
					SecurityCheckService.doApproveRejectProcessingRegionSecurityManager(currentTask, WFTaskActionsEnum.APPROVE, securityCheck.getSecurityCheck(), loginEmpData, attachments, isEmployee);
				}
			}
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityCheckRegisteration.class, e, "SecurityCheckRegisteration");
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			return null;
		}
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Reject security check
	 * 
	 * @return page to be directed to
	 */
	public String rejectSecurityCheck() {
		try {
			boolean isEmployee = viewMode == 1;
			if (currentTask.getNotes() != null && currentTask.getNotes().trim().isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_rejectReasonMandatory"));
				return null;
			}
			securityCheck.setStatus(SecurityCheckStatusEnum.REJECTED.getCode());
			if (regionId != null || role.equals(WFTaskRolesEnum.SECURITY_CHECK_GENERAL_INTELLIGENCE_MANAGER.getCode()) || role.equals(WFTaskRolesEnum.SECURITY_CHECK_GENERAL_DIRECTOR.getCode()) || role.equals(WFTaskRolesEnum.REGION_SECURITY_AFFAIRS_APPROVER_MANAGER.getCode()) || role.equals(WFTaskRolesEnum.REGION_GENERAL_PROTECTION_APPROVER_MANAGER.getCode())) {
				SecurityCheckService.doRejectRegion(currentTask, WFTaskActionsEnum.REJECT, securityCheck.getSecurityCheck(), loginEmpData, attachments, isEmployee);
			} else {
				if (role.equals(WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER.getCode()) || role.equals(WFTaskRolesEnum.GENERAL_INTELLIGENCE_MANAGER.getCode()) || role.equals(WFTaskRolesEnum.SECURITY_AFFAIRS_MANAGER.getCode())) {
					SecurityCheckService.doApproveRejectDM(currentTask, WFTaskActionsEnum.REJECT, securityCheck.getSecurityCheck(), loginEmpData, attachments, isEmployee);
				} else if (role.equals(WFTaskRolesEnum.REGION_DIRECTOR.getCode()) || role.equals(WFTaskRolesEnum.GENERAL_DIRECTOR.getCode())) {
					SecurityCheckService.doApproveRejectSM(currentTask, WFTaskActionsEnum.REJECT, securityCheck.getSecurityCheck(), loginEmpData, attachments, isEmployee);
				} else if (role.equals(WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER_PROCESSING.getCode()) || role.equals(WFTaskRolesEnum.GENERAL_INTELLIGENCE_MANAGER_PROCESSING.getCode()) || role.equals(WFTaskRolesEnum.SECURITY_AFFAIRS_MANAGER_PROCESSING.getCode())) {
					SecurityCheckService.doApproveRejectProcessingDM(currentTask, WFTaskActionsEnum.REJECT, securityCheck.getSecurityCheck(), loginEmpData, attachments, isEmployee);
				} else if (role.equals(WFTaskRolesEnum.REGION_DIRECTOR_PROCESSING.getCode()) || role.equals(WFTaskRolesEnum.GENERAL_DIRECTOR_PROCESSING.getCode())) {
					SecurityCheckService.doApproveRejectProcessingSM(currentTask, WFTaskActionsEnum.REJECT, securityCheck.getSecurityCheck(), loginEmpData, attachments, isEmployee);
				} else if (role.equals(WFTaskRolesEnum.REGION_SECURITY_INTELLIGENCE_MANAGER.getCode())) {
					SecurityCheckService.doApproveRejectRegionSecurity(currentTask, WFTaskActionsEnum.REJECT, securityCheck.getSecurityCheck(), loginEmpData, attachments, isEmployee);
				} else if (role.equals(WFTaskRolesEnum.REGION_SECURITY_INTELLIGENCE_MANAGER_PROCESSING.getCode())) {
					SecurityCheckService.doApproveRejectProcessingRegionSecurityManager(currentTask, WFTaskActionsEnum.REJECT, securityCheck.getSecurityCheck(), loginEmpData, attachments, isEmployee);
				}
			}

			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityCheckRegisteration.class, e, "SecurityCheckRegisteration");
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			return null;
		}
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Save processing result
	 * 
	 * @return page to be directed to
	 */
	public String saveSecurityCheckProcessing() {
		try {
			securityCheck.setStatus(SecurityCheckStatusEnum.PROCCESSING_UNDER_APPROVAL.getCode());
			boolean isEmployee = viewMode == 1;
			if (regionId != null) {
				SecurityCheckService.doProcessingRegion(currentTask, securityCheck.getSecurityCheck(), employeeList, nonEmployeeList, loginEmpData, attachments, isEmployee);

			} else {
				SecurityCheckService.doProcessingHQ(currentTask, securityCheck.getSecurityCheck(), employeeList, nonEmployeeList, loginEmpData, attachments, isEmployee);
			}
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityCheckRegisteration.class, e, "SecurityCheckRegisteration");
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			return null;
		}
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Do notify
	 * 
	 * @return page to be directed to
	 */
	public String doNotify() {
		try {
			boolean isEmployee = viewMode == 1;
			SecurityCheckService.doNotify(currentTask, loginEmpData, isEmployee);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityCheckRegisteration.class, e, "SecurityCheckRegisteration");
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			return null;
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Print reply letter
	 */
	public void printReplyLetter() {
		try {
			byte[] bytes = SecurityCheckService.getSecurityCheckReplyLetterBytes(securityCheck.getId(), securityCheck.getDepartmentOrderSrcName(), loginEmpData.getFullName());
			super.print(bytes, "Security_Check_Reply_Letter");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityCheckRegisteration.class, e, "SecurityCheckRegisteration");
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	/**
	 * Print
	 */
	public void print() {
		try {
			byte[] bytes = SecurityCheckService.getSecurityCheckReportBytes(securityCheck.getId(), loginEmpData.getFullName());
			super.print(bytes, "Security_Check_Details");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityCheckRegisteration.class, e, "SecurityCheckRegisteration");
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
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
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityCheckRegisteration.class, e, "SecurityCheckRegisteration");
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	/**
	 * printNonEmployeeDetails
	 * 
	 * @param nonEmployeeId
	 */
	public void printNonEmployeeDetails(long nonEmployeeId) {
		try {
			byte[] bytes = SecurityCheckService.getNonEmployeeDetailsBytes(nonEmployeeId, loginEmpData);
			super.print(bytes, "NonEmployee_Details");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityCheckRegisteration.class, e, "SecurityCheckRegisteration");
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	public void getUploadParam(long securityCheckId) {
		try {
			fileArchivingParam = EncryptionUtil.encryptSymmetrically("code=" + ServiceCodesEnum.INFO_SYS.getCode() + "&token=" + EntityNameEnum.SECURITY_CHECK.getCode() + "_" + securityCheckId);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityCheckRegisteration.class, e, "SecurityCheckRegisteration");
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	public void getDownloadParam(String attachmentId) {
		try {
			downloadFileParamId = EncryptionUtil.encryptSymmetrically("contentId=" + attachmentId);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityCheckRegisteration.class, e, "SecurityCheckRegisteration");
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	/**
	 * Delete Attachment
	 * 
	 * @param attachment
	 */
	public void getDeleteParam(Attachment attachment) {
		try {
			downloadFileParamId = EncryptionUtil.encryptSymmetrically("contentId=" + attachment.getAttachmentId());
			URL url = new URL(InfoSysConfigurationService.getBoolServerDeletePath() + downloadFileParamId);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			if (responseCode == HttpServletResponse.SC_OK) {
				AttachmentService.deleteAttachment(attachment);
				attachmentList.remove(attachment);
			} else {
				throw new BusinessException("error_general");
			}

		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityCheckRegisteration.class, e, "SecurityCheckRegisteration");
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	public void getDownloadedAttachmentsIdList(long securityCheckId) throws BusinessException {
		try {
			attachmentList = AttachmentService.getAttachments(EntityNameEnum.SECURITY_CHECK.getCode(), securityCheckId);
			openDownloadPopupFlag = false;
			openDownloadDialogueFlag = false;
			if (attachmentList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noAttachments"));
			} else {
				openDownloadDialogueFlag = true;
			}
		} catch (Exception e) {
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

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	public List<SetupDomain> getIncomingSideList() {
		return incomingSideList;
	}

	public void setIncomingSideList(List<SetupDomain> incomingSideList) {
		this.incomingSideList = incomingSideList;
	}

	public List<SetupDomain> getNoteSideList() {
		return noteSideList;
	}

	public void setNoteSideList(List<SetupDomain> noteSideList) {
		this.noteSideList = noteSideList;
	}

	public SecurityCheckData getSecurityCheck() {
		return securityCheck;
	}

	public void setSecurityCheck(SecurityCheckData securityCheck) {
		this.securityCheck = securityCheck;
	}

	public List<SecurityCheckEmployeeData> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<SecurityCheckEmployeeData> employeeList) {
		this.employeeList = employeeList;
	}

	public List<SecurityCheckNonEmployeeData> getNonEmployeeList() {
		return nonEmployeeList;
	}

	public void setNonEmployeeList(List<SecurityCheckNonEmployeeData> nonEmployeeList) {
		this.nonEmployeeList = nonEmployeeList;
	}

	public SecurityCheckEmployeeData getInsertedEmployee() {
		return insertedEmployee;
	}

	public void setInsertedEmployee(SecurityCheckEmployeeData insertedEmployee) {
		this.insertedEmployee = insertedEmployee;
	}

	public SecurityCheckNonEmployeeData getInsertedNonEmployee() {
		return insertedNonEmployee;
	}

	public void setInsertedNonEmployee(SecurityCheckNonEmployeeData insertedNonEmployee) {
		this.insertedNonEmployee = insertedNonEmployee;
	}

	public String getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(String activeIndex) {
		this.activeIndex = activeIndex;
	}

	public String getFileArchivingParam() {
		return fileArchivingParam;
	}

	public void setFileArchivingParam(String fileArchivingParam) {
		this.fileArchivingParam = fileArchivingParam;
	}

	public String getDownloadFileParamId() {
		return downloadFileParamId;
	}

	public void setDownloadFileParamId(String downloadFileParamId) {
		this.downloadFileParamId = downloadFileParamId;
	}

	public List<Attachment> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<Attachment> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public boolean isOpenDownloadPopupFlag() {
		return openDownloadPopupFlag;
	}

	public void setOpenDownloadPopupFlag(boolean openDownloadPopupFlag) {
		this.openDownloadPopupFlag = openDownloadPopupFlag;
	}

	public boolean isOpenDownloadDialogueFlag() {
		return openDownloadDialogueFlag;
	}

	public void setOpenDownloadDialogueFlag(boolean openDownloadDialogueFlag) {
		this.openDownloadDialogueFlag = openDownloadDialogueFlag;
	}

	public String getBoolServerUploadPath() {
		return boolServerUploadPath;
	}

	public void setBoolServerUploadPath(String boolServerUploadPath) {
		this.boolServerUploadPath = boolServerUploadPath;
	}

	public String getBoolServerDownloadPath() {
		return boolServerDownloadPath;
	}

	public void setBoolServerDownloadPath(String boolServerDownloadPath) {
		this.boolServerDownloadPath = boolServerDownloadPath;
	}

	public List<SecurityCheckReasonEnum> getSecurityCheckReasonsEnum() {
		return SecurityCheckReasonEnum.getAllSecurityCheckReason();
	}

	public int getViewMode() {
		return viewMode;
	}

	public void setViewMode(int viewMode) {
		this.viewMode = viewMode;
	}

	public List<SetupDomain> getSecurityCheckReasonList() {
		return securityCheckReasonList;
	}

	public void setSecurityCheckReasonList(List<SetupDomain> securityCheckReasonList) {
		this.securityCheckReasonList = securityCheckReasonList;
	}

	public String getOthersRequestSource() {
		return RequestSourceEnum.OTHERS.getCode();
	}

	public List<SelectItem> getRequestSourceEnumList() {
		return requestSourceEnumList;
	}

	public void setRequestSourceEnumList(List<SelectItem> requestSourceEnumList) {
		this.requestSourceEnumList = requestSourceEnumList;
	}

}