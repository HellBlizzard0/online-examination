package com.code.ui.backings.securitycheck;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import com.code.dal.orm.securitycheck.SecurityCheckData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.RequestSourceEnum;
import com.code.enums.SecurityCheckReasonEnum;
import com.code.exceptions.BusinessException;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.securitycheck.SecurityCheckService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.SetupService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "securityChecksSearch")
@ViewScoped
public class SecurityChecksSearch extends BaseBacking implements Serializable {
	private int rowsCount = 10;
	private Long domainIncomingSourceId;
	private Long departmentSourceId;
	private String departmentSourceName;
	private Date fromDate;
	private Date toDate;
	private Long employeeId;
	private String employeeName;
	private Long nonEmployeeId;
	private String nonEmployeeName;
	private Long orderRegionId;
	private String orderRegionName;
	private Integer securityCheckReason;
	private List<SetupDomain> incomingSideList;
	private List<SecurityCheckData> securityCheckList;
	private int viewMode = 1; // 1 for employees, 2 for non employees

	private String requestSource;
	private List<SelectItem> requestSourceEnumList = new ArrayList<SelectItem>();

	/**
	 * Constructor
	 * 
	 */
	public SecurityChecksSearch() {
		try {
			if (getRequest().getParameter("viewMode") != null) {
				viewMode = (Integer.valueOf(getRequest().getParameter("viewMode")));
			}

			incomingSideList = SetupService.getDomains(ClassesEnum.INCOMING_SIDES.getCode());
			Long regionId = DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());

			if (regionId != null) {
				orderRegionId = regionId;
				orderRegionName = DepartmentService.getDepartment(regionId).getArabicName();
				requestSourceEnumList.add(new SelectItem(RequestSourceEnum.REGION_DIRECTOR.getCode(), RequestSourceEnum.REGION_DIRECTOR.getCode()));

			} else {
				orderRegionName = DepartmentService.getDepartment(getHeadQuarter()).getArabicName();
			}

			if (viewMode == 1) {
				requestSourceEnumList.add(new SelectItem(RequestSourceEnum.GENERAL_PROTECTION_DIRECTORATE.getCode(), RequestSourceEnum.GENERAL_PROTECTION_DIRECTORATE.getCode()));

			} else {
				requestSourceEnumList.add(new SelectItem(RequestSourceEnum.GENERAL_INTELLIGENCE_DIRECTORATE.getCode(), RequestSourceEnum.GENERAL_INTELLIGENCE_DIRECTORATE.getCode()));
			}
			requestSourceEnumList.add(new SelectItem(RequestSourceEnum.OTHERS.getCode(), RequestSourceEnum.OTHERS.getCode()));

		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityChecksSearch.class, e, "SecurityChecksSearch");
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	/**
	 * Initialize Search Parameters
	 */
	public void init() {
		fromDate = null;
		toDate = null;
		domainIncomingSourceId = null;
		departmentSourceId = null;
		departmentSourceName = null;
		employeeId = null;
		employeeName = null;
		nonEmployeeId = null;
		nonEmployeeName = null;
		orderRegionId = null;
		orderRegionName = null;
		requestSource = null;

		try {
			Long regionId = DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());
			if (regionId != null) {
				orderRegionId = regionId;
				orderRegionName = DepartmentService.getDepartment(regionId).getArabicName();
			} else {
				orderRegionName = DepartmentService.getDepartment(getHeadQuarter()).getArabicName();
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Search for Security Checks
	 */
	public void searchSecurityChecks() {
		try {
			if (viewMode == 1) {
				securityCheckList = SecurityCheckService.getSecurityCheckEmployee(departmentSourceId == null ? FlagsEnum.ALL.getCode() : departmentSourceId, domainIncomingSourceId == null ? FlagsEnum.ALL.getCode() : domainIncomingSourceId, orderRegionId == null || orderRegionId.equals(getHeadQuarter()) ? FlagsEnum.ALL.getCode() : orderRegionId, fromDate, toDate, employeeId, securityCheckReason == null ? FlagsEnum.ALL.getCode() : securityCheckReason, requestSource);
			} else {
				securityCheckList = SecurityCheckService.getSecurityCheckNonEmployee(departmentSourceId == null ? FlagsEnum.ALL.getCode() : departmentSourceId, domainIncomingSourceId == null ? FlagsEnum.ALL.getCode() : domainIncomingSourceId, orderRegionId == null || orderRegionId.equals(getHeadQuarter()) ? FlagsEnum.ALL.getCode() : orderRegionId, fromDate, toDate, nonEmployeeId, securityCheckReason == null ? FlagsEnum.ALL.getCode() : securityCheckReason, requestSource);
			}
			if (securityCheckList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityChecksSearch.class, e, "SecurityChecksSearch");
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	/**
	 * Delete security check
	 * 
	 * @param securityCheckData
	 */
	public void deleteSecurityCheck(SecurityCheckData securityCheckData) {
		try {
			SecurityCheckService.deleteSecurityCheck(loginEmpData, securityCheckData);
			securityCheckList.remove(securityCheckData);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityChecksSearch.class, e, "SecurityChecksSearch");
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	/**
	 * View Security Check
	 * 
	 * @return page to be directed to
	 */
	public String viewSecurityCheck(long securityCheckId) {
		getRequest().setAttribute("mode", securityCheckId);
		return NavigationEnum.SECURITY_CHECK.toString();
	}

	/**
	 * Print
	 * 
	 * @param securityCheckData
	 */
	public void print(SecurityCheckData securityCheckData) {
		try {
			byte[] bytes = SecurityCheckService.getSecurityCheckReplyLetterBytes(securityCheckData.getId(), securityCheckData.getDepartmentOrderSrcName(), loginEmpData.getFullName());
			super.print(bytes, "Security_Check_Reply_Letter");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityChecksSearch.class, e, "SecurityChecksSearch");
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

	public Long getDomainIncomingSourceId() {
		return domainIncomingSourceId;
	}

	public void setDomainIncomingSourceId(Long domainIncomingSourceId) {
		this.domainIncomingSourceId = domainIncomingSourceId;
	}

	public Long getDepartmentSourceId() {
		return departmentSourceId;
	}

	public void setDepartmentSourceId(Long departmentSourceId) {
		this.departmentSourceId = departmentSourceId;
	}

	public String getDepartmentSourceName() {
		return departmentSourceName;
	}

	public void setDepartmentSourceName(String departmentSourceName) {
		this.departmentSourceName = departmentSourceName;
	}

	public List<SetupDomain> getIncomingSideList() {
		return incomingSideList;
	}

	public void setIncomingSideList(List<SetupDomain> incomingSideList) {
		this.incomingSideList = incomingSideList;
	}

	public List<SecurityCheckData> getSecurityCheckList() {
		return securityCheckList;
	}

	public void setSecurityCheckList(List<SecurityCheckData> securityCheckList) {
		this.securityCheckList = securityCheckList;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Long getNonEmployeeId() {
		return nonEmployeeId;
	}

	public void setNonEmployeeId(Long nonEmployeeId) {
		this.nonEmployeeId = nonEmployeeId;
	}

	public String getNonEmployeeName() {
		return nonEmployeeName;
	}

	public void setNonEmployeeName(String nonEmployeeName) {
		this.nonEmployeeName = nonEmployeeName;
	}

	public Long getOrderRegionId() {
		return orderRegionId;
	}

	public void setOrderRegionId(Long orderRegionId) {
		this.orderRegionId = orderRegionId;
	}

	public String getOrderRegionName() {
		return orderRegionName;
	}

	public void setOrderRegionName(String orderRegionName) {
		this.orderRegionName = orderRegionName;
	}

	public Long getHeadQuarter() {
		return InfoSysConfigurationService.getHeadQuarter();
	}

	public List<SecurityCheckReasonEnum> getSecurityCheckReasonsEnum() {
		return SecurityCheckReasonEnum.getAllSecurityCheckReason();
	}

	public Integer getSecurityCheckReason() {
		return securityCheckReason;
	}

	public void setSecurityCheckReason(Integer securityCheckReason) {
		this.securityCheckReason = securityCheckReason;
	}

	public int getViewMode() {
		return viewMode;
	}

	public void setViewMode(int viewMode) {
		this.viewMode = viewMode;
	}

	public String getRequestSource() {
		return requestSource;
	}

	public void setRequestSource(String requestSource) {
		this.requestSource = requestSource;
	}

	public List<SelectItem> getRequestSourceEnumList() {
		return requestSourceEnumList;
	}

	public void setRequestSourceEnumList(List<SelectItem> requestSourceEnumList) {
		this.requestSourceEnumList = requestSourceEnumList;
	}

	public String getOthersRequestSource() {
		return RequestSourceEnum.OTHERS.getCode();
	}

}