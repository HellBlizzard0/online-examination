package com.code.ui.backings.minisearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.code.dal.dto.empcase.EmployeeCaseData;
import com.code.dal.orm.info.InfoData;
import com.code.dal.orm.labcheck.LabCheckEmployeeData;
import com.code.dal.orm.securitycheck.SecurityCheckData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.setup.NonEmployeeData;
import com.code.dal.orm.surveillance.SurveillanceEmpNonEmpData;
import com.code.enums.FlagsEnum;
import com.code.enums.LabCheckEmployeeCheckStatusEnum;
import com.code.enums.LabCheckReasonsEnum;
import com.code.enums.SecurityCheckStatusEnum;
import com.code.enums.WFInstanceStatusEnum;
import com.code.exceptions.BusinessException;
import com.code.integration.employeecasesdata.EmployeeCasesWSRespones;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.info.InfoService;
import com.code.services.infosys.labcheck.LabCheckService;
import com.code.services.infosys.securitycheck.SecurityCheckService;
import com.code.services.infosys.surveillance.SurveillanceOrdersService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.EmployeeService;
import com.code.services.setup.NonEmployeeService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.info.InfoWorkFlow;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "employeeInfoSysDetails")
@ViewScoped
public class EmployeeInfoSysDetails extends BaseBacking implements Serializable {
	private List<InfoData> employeeRelatedInfoList;
	private List<SurveillanceEmpNonEmpData> employeeSurveillanceOrderList;
	private List<SecurityCheckData> employeeSecurityCheckList;
	private List<EmployeeCaseData> employeeRelatedCasesList;
	private List<LabCheckEmployeeData> employeeLabCheckList;
	private Integer pageMode = 1;
	private String empName;

	public EmployeeInfoSysDetails() {
		super();
		this.init();
		try {
			Boolean isEmployee = true;
			Long employeeNonEmployeeId = null;
			String employeeNonEmployeeSocialId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("social");

			if (employeeNonEmployeeSocialId != null && !employeeNonEmployeeSocialId.equals("null") && !employeeNonEmployeeSocialId.equals("undefined") && !employeeNonEmployeeSocialId.isEmpty()) {
				EmployeeData empData = EmployeeService.getEmployee(employeeNonEmployeeSocialId);
				if (empData == null) {
					NonEmployeeData nonEmpData = NonEmployeeService.getNonEmployeeByIdentity(Long.parseLong(employeeNonEmployeeSocialId));
					if (nonEmpData != null) {
						employeeNonEmployeeId = nonEmpData.getId();
						isEmployee = false;
					} else {
						throw new BusinessException("error_general");
					}
				} else {
					employeeNonEmployeeId = empData.getEmpId();
				}
			} else {
				throw new BusinessException("error_general");
			}

			Date saveDate = null;
			boolean isAuthDep = InfoWorkFlow.isIntelleigenceOrAnalysisDepartment(loginEmpData.getActualDepartmentId());
			if (getRequest().getParameter("saveDate") != null && !getRequest().getParameter("saveDate").equals("null") && !getRequest().getParameter("saveDate").isEmpty() && !getRequest().getParameter("saveDate").equals("undefined")) {
				saveDate = HijriDateService.getHijriDate(getRequest().getParameter("saveDate"));
			}
			if (!isEmployee) { // nonemp
				if (employeeNonEmployeeId.equals(FlagsEnum.OFF.getCode())) {
					employeeRelatedInfoList = new ArrayList<InfoData>();
					employeeSecurityCheckList = new ArrayList<SecurityCheckData>();
					employeeRelatedCasesList = new ArrayList<EmployeeCaseData>();
					employeeLabCheckList = new ArrayList<LabCheckEmployeeData>();
				} else {
					if (isAuthDep) {
						employeeRelatedInfoList = InfoService.getInfoDataRelatedEntity(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), employeeNonEmployeeId, null, loginEmpData);
					} else {
						employeeRelatedInfoList = InfoService.getInfoDataRelatedEntityByAnalysis(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), employeeNonEmployeeId, null, loginEmpData);
					}
					employeeSecurityCheckList = SecurityCheckService.getAllSecurityCheck(employeeNonEmployeeId, false, SecurityCheckStatusEnum.APPROVED.getCode(), null);
					try {
						employeeRelatedCasesList = EmployeeCasesWSRespones.getEmployeeRelatedCaseDataBySoicalId(employeeNonEmployeeSocialId);
					} catch (BusinessException e) {
						super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
					}
					employeeLabCheckList = new ArrayList<LabCheckEmployeeData>();
					employeeSurveillanceOrderList = SurveillanceOrdersService.getSurveillanceEmployeeData(null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), false, FlagsEnum.ALL.getCode(), WFInstanceStatusEnum.COMPLETED.getCode(), employeeNonEmployeeSocialId);
				}
				pageMode = 2;
			} else {
				Long empDepartmentId = EmployeeService.getEmployee(employeeNonEmployeeId).getActualDepartmentId();
				if (empDepartmentId == null) {
					throw new BusinessException("error_general");
				}
				if (isAuthDep) {
					employeeRelatedInfoList = InfoService.getInfoDataRelatedEntity(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), employeeNonEmployeeId, FlagsEnum.ALL.getCode(), saveDate, loginEmpData);
				} else {
					employeeRelatedInfoList = InfoService.getInfoDataRelatedEntityByAnalysis(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), employeeNonEmployeeId, FlagsEnum.ALL.getCode(), saveDate, loginEmpData);
				}
				employeeSurveillanceOrderList = SurveillanceOrdersService.getSurveillanceEmployeeData(null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), false, empDepartmentId, WFInstanceStatusEnum.COMPLETED.getCode(), employeeNonEmployeeSocialId);
				employeeSecurityCheckList = SecurityCheckService.getAllSecurityCheck(employeeNonEmployeeId, true, SecurityCheckStatusEnum.APPROVED.getCode(), saveDate);
				try {
					employeeRelatedCasesList = EmployeeCasesWSRespones.getEmployeeRelatedCaseDataBySoicalId(employeeNonEmployeeSocialId);
				} catch (BusinessException e) {
					super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
				}
				employeeLabCheckList = LabCheckService.getPervEmployeesLabChecks(employeeNonEmployeeId, saveDate);
				empName = EmployeeService.getEmployee(employeeNonEmployeeId).getFullName();
			}

		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(EmployeeInfoSysDetails.class, e, "EmployeeInfoSysDetails");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void init() {
		employeeRelatedInfoList = new ArrayList<InfoData>();
		employeeSurveillanceOrderList = new ArrayList<SurveillanceEmpNonEmpData>();
		employeeSecurityCheckList = new ArrayList<SecurityCheckData>();
		employeeRelatedCasesList = new ArrayList<EmployeeCaseData>();
		employeeLabCheckList = new ArrayList<LabCheckEmployeeData>();
	}

	public List<InfoData> getEmployeeRelatedInfoList() {
		return employeeRelatedInfoList;
	}

	public void setEmployeeRelatedInfoList(List<InfoData> employeeRelatedInfoList) {
		this.employeeRelatedInfoList = employeeRelatedInfoList;
	}

	public List<SurveillanceEmpNonEmpData> getEmployeeSurveillanceOrderList() {
		return employeeSurveillanceOrderList;
	}

	public void setEmployeeSurveillanceOrderList(List<SurveillanceEmpNonEmpData> employeeSurveillanceOrderList) {
		this.employeeSurveillanceOrderList = employeeSurveillanceOrderList;
	}

	public List<SecurityCheckData> getEmployeeSecurityCheckList() {
		return employeeSecurityCheckList;
	}

	public void setEmployeeSecurityCheckList(List<SecurityCheckData> employeeSecurityCheckList) {
		this.employeeSecurityCheckList = employeeSecurityCheckList;
	}

	public List<EmployeeCaseData> getEmployeeRelatedCasesList() {
		return employeeRelatedCasesList;
	}

	public void setEmployeeRelatedCasesList(List<EmployeeCaseData> employeeRelatedCasesList) {
		this.employeeRelatedCasesList = employeeRelatedCasesList;
	}

	public List<LabCheckEmployeeData> getEmployeeLabCheckList() {
		return employeeLabCheckList;
	}

	public void setEmployeeLabCheckList(List<LabCheckEmployeeData> employeeLabCheckList) {
		this.employeeLabCheckList = employeeLabCheckList;
	}

	public Integer getNoSampleCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.NO_SAMPLE.getCode();
	}

	public Integer getRetestCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.RETEST.getCode();
	}

	public Integer getCheckPrivationCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.CHECK_PRIVATION.getCode();
	}

	public Integer getNegativeCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.NEGATIVE.getCode();
	}

	public Integer getPositiveCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.POSITIVE.getCode();
	}

	public Integer getPositiveUnderApproveCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.POSITIVE_UNDER_APPROVE.getCode();
	}

	public Integer getSendingToForceCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.SENDING_TO_FORCE.getCode();
	}

	public Integer getCheatingInCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.CHEATING.getCode();
	}

	public Integer getPageMode() {
		return pageMode;
	}

	public void setPageMode(Integer pageMode) {
		this.pageMode = pageMode;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public List<LabCheckReasonsEnum> getLabCheckReasonsEnumList() {
		return LabCheckReasonsEnum.getAllCheckReasonsEnumValues();
	}

	public int getMaxDescLn() {
		return InfoSysConfigurationService.getMinCaseDescriptionLength();
	}

}
