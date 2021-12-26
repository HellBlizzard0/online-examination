package com.code.ui.backings.labcheck;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.labcheck.LabCheckEmployeeData;
import com.code.dal.orm.security.UserMenuActionData;
import com.code.dal.orm.setup.DepartmentData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.LabCheckEmployeeCheckStatusEnum;
import com.code.enums.LabCheckReasonsEnum;
import com.code.enums.LabCheckStatusEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.UserMenuActionsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.labcheck.LabCheckService;
import com.code.services.log4j.Log4j;
import com.code.services.security.SecurityService;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.SetupService;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "labCheckSearch")
@ViewScoped
public class LabChecksSearch extends BaseBacking implements Serializable {
	private List<LabCheckEmployeeData> labCheckEmployeeDataList;
	private LabCheckEmployeeData labCheckEmployeeDataSearch;
	private LabCheckEmployeeData selectedLabCheckEmployeeData;
	private List<SetupDomain> domainMetrialTypeList;
	private boolean isSave;
	private Integer mode;
	private Integer selectedCheckStatus;
	private Long selectectedMetrial;
	private Boolean editprivilege = false;
	private boolean userAtRegion = false;

	/**
	 * Default Constructor Initializer
	 */
	public LabChecksSearch() {
		super();
		init();
		labCheckEmployeeDataList = new ArrayList<LabCheckEmployeeData>();
		labCheckEmployeeDataSearch = new LabCheckEmployeeData();
		selectedLabCheckEmployeeData = new LabCheckEmployeeData();
		domainMetrialTypeList = new ArrayList<SetupDomain>();
		isSave = false;

		if (getRequest().getParameter("mode") != null && !getRequest().getParameter("mode").equals("null") && !getRequest().getParameter("mode").isEmpty() && !getRequest().getParameter("mode").equals("undefined")) {
			mode = Integer.parseInt(getRequest().getParameter("mode"));
		} else {
			mode = 1;
		}
		try {
			UserMenuActionData assignmentEditAction = SecurityService.getAction(loginEmpData.getEmpId(), UserMenuActionsEnum.LAB_CHECK_EDIT.getCode(), FlagsEnum.ALL.getCode());
			if (assignmentEditAction != null) {
				editprivilege = true;
			}
			domainMetrialTypeList = SetupService.getDomains(ClassesEnum.MATERIAL_TYPES.getCode());
			Long userRegion = DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());
			if (userRegion != null) {
				DepartmentData regionDep = DepartmentService.getDepartment(userRegion);
				labCheckEmployeeDataSearch.setEmployeeRegionId(regionDep.getId());
				labCheckEmployeeDataSearch.setEmployeeRegionName(regionDep.getArabicName());
				userAtRegion = true;
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(LabChecksSearch.class, e, "LabChecksSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Search Lab Checks
	 */
	public void search() {
		try {
			isSave = false;
			labCheckEmployeeDataList = LabCheckService.getLabCheckEmployees(labCheckEmployeeDataSearch.getEmployeeId(), labCheckEmployeeDataSearch.getOrderDate(), labCheckEmployeeDataSearch.getOrderNumber(), labCheckEmployeeDataSearch.getCheckReason(), labCheckEmployeeDataSearch.getCheckStatus(), labCheckEmployeeDataSearch.getEmployeeDepartmentId(), labCheckEmployeeDataSearch.getLabCheckStatus(),
					labCheckEmployeeDataSearch.getEmployeeRegionId(), null);
			if (labCheckEmployeeDataList.isEmpty()) {
				this.setServerSideSuccessMessages(getParameterizedMessage("error_noData"));
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(LabChecksSearch.class, e, "LabChecksSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Reset Search Form
	 */
	public void resetSearch() {
		isSave = false;
		labCheckEmployeeDataList = new ArrayList<LabCheckEmployeeData>();
		labCheckEmployeeDataSearch = new LabCheckEmployeeData();
		try {
			if (userAtRegion) {
				DepartmentData regionDep = DepartmentService.getDepartment(DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId()));
				labCheckEmployeeDataSearch.setEmployeeRegionId(regionDep.getId());
				labCheckEmployeeDataSearch.setEmployeeRegionName(regionDep.getArabicName());
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(LabChecksSearch.class, e, "LabChecksSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * View Lab Check Order
	 * 
	 * @param labCheckEmp
	 * @return
	 */
	public String viewLabCheckOrder(LabCheckEmployeeData labCheckEmp) {
		getRequest().setAttribute("mode", labCheckEmp.getLabCheckId());
		getRequest().setAttribute("retest", labCheckEmp.getRetestNumber());
		return NavigationEnum.LAB_CHECK_SEARCH.toString();
	}

	/**
	 * sample taccking
	 * 
	 * @param labCheckEmp
	 */
	public void sampleTacking(LabCheckEmployeeData labCheckEmp) {
		isSave = false;
		selectedLabCheckEmployeeData = new LabCheckEmployeeData();
		selectedLabCheckEmployeeData = labCheckEmp;
		selectectedMetrial = null;
		selectedCheckStatus = null;
	}

	/**
	 * save sample tacking for selected employee
	 */
	public void saveSampleTacking() {
		try {
			selectedLabCheckEmployeeData.setCheckStatus(LabCheckEmployeeCheckStatusEnum.SAMPLE_TAKED.getCode());
			if (selectedLabCheckEmployeeData.getSampleDate() == null || selectedLabCheckEmployeeData.getSampleNumber() == null || selectedLabCheckEmployeeData.getSampleNumber().trim().isEmpty()) {
				throw new BusinessException("error_mandatory");
			}
			Long samplesNo = LabCheckService.getCountSampleNumber(HijriDateService.getHijriDateYear(selectedLabCheckEmployeeData.getSampleDate()) + "", selectedLabCheckEmployeeData.getSampleNumber());
			if (samplesNo > 0L) {
				throw new BusinessException("error_sampleDuplicated");
			}
			isSave = true;
			LabCheckService.updateLabCheckEmployee(selectedLabCheckEmployeeData, loginEmpData);
			selectedLabCheckEmployeeData = new LabCheckEmployeeData();
			selectectedMetrial = null;
			selectedCheckStatus = null;
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			selectedLabCheckEmployeeData.setCheckStatus(LabCheckEmployeeCheckStatusEnum.NO_SAMPLE.getCode());
			isSave = false;
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			 selectedLabCheckEmployeeData.setCheckStatus(LabCheckEmployeeCheckStatusEnum.NO_SAMPLE.getCode());
				isSave = false;
			   Log4j.traceErrorException(LabChecksSearch.class, e, "LabChecksSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * check pervintion
	 * 
	 * @param labCheckEmp
	 */
	public void checkPervintion(LabCheckEmployeeData labCheckEmp) {
		try {
			labCheckEmp.setCheckStatus(LabCheckEmployeeCheckStatusEnum.CHECK_PRIVATION.getCode());
			LabCheckService.updateLabCheckEmployee(labCheckEmp, loginEmpData);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(LabChecksSearch.class, e, "LabChecksSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * sending to force page
	 * 
	 * @param labCheckEmp
	 * @param mode
	 * @return
	 */
	public String sendingToForce(LabCheckEmployeeData labCheckEmp, Integer mode) {
		getRequest().setAttribute("labCheckEmployeeDataId", labCheckEmp.getId());
		getRequest().setAttribute("mode", mode);
		return NavigationEnum.NATIONAL_FORCE_CHECK.toString();
	}

	/**
	 * view Lab check Details
	 * 
	 * @param labCheckEmp
	 * @return
	 */
	public String viewLabCheckDetails(LabCheckEmployeeData labCheckEmp) {
		getRequest().setAttribute("labCheckEmployeeDataId", labCheckEmp.getId());
		getRequest().setAttribute("mode", 3);
		return NavigationEnum.NATIONAL_FORCE_CHECK.toString();
	}

	/**
	 * edit Lab check Details
	 * 
	 * @param labCheckEmp
	 * @return
	 */
	public String editLabCheckDetails(LabCheckEmployeeData labCheckEmp) {
		getRequest().setAttribute("labCheckEmployeeDataId", labCheckEmp.getId());
		return NavigationEnum.LAB_CHECK_EDIT.toString();
	}

	/**
	 * print lab check report
	 * 
	 * @param labCheckEmployeeData
	 */
	public void printLabCheckReport(LabCheckEmployeeData labCheckEmployeeData) {
		try {
			byte[] bytes;
			String employeeId = labCheckEmployeeData.getEmployeeId().toString();
			String sampleNumber = labCheckEmployeeData.getSampleNumber();
			String sampleDateString = HijriDateService.getHijriDateString(labCheckEmployeeData.getSampleDate());
			bytes = LabCheckService.getLabCheckReportBytes(employeeId, sampleNumber, sampleDateString);
			super.print(bytes, "Lab check report");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(LabChecksSearch.class, e, "LabChecksSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Print Report Using Given Fields
	 */
	public void printReport() {
		try {
			byte[] bytes = LabCheckService.getLabCheckSearchBytes(labCheckEmployeeDataSearch.getEmployeeId() == null ? FlagsEnum.ALL.getCode() : labCheckEmployeeDataSearch.getEmployeeId(), labCheckEmployeeDataSearch.getOrderDate(), labCheckEmployeeDataSearch.getOrderNumber(), labCheckEmployeeDataSearch.getCheckReason() == null ? FlagsEnum.ALL.getCode() : labCheckEmployeeDataSearch.getCheckReason(),
					labCheckEmployeeDataSearch.getCheckStatus() == null ? FlagsEnum.ALL.getCode() : labCheckEmployeeDataSearch.getCheckStatus(), labCheckEmployeeDataSearch.getEmployeeDepartmentId() == null ? FlagsEnum.ALL.getCode() : labCheckEmployeeDataSearch.getEmployeeDepartmentId(), labCheckEmployeeDataSearch.getEmployeeRegionId() == null || labCheckEmployeeDataSearch.getEmployeeRegionId().equals(getHeadQuarter()) ? FlagsEnum.ALL.getCode() : labCheckEmployeeDataSearch.getEmployeeRegionId(),
					loginEmpData.getFullName());
			super.print(bytes, "Lab Check Report");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(LabChecksSearch.class, e, "LabChecksSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/********************************************************** Setters And Getters **********************************************/
	public List<LabCheckEmployeeData> getLabCheckEmployeeDataList() {
		return labCheckEmployeeDataList;
	}

	public void setLabCheckEmployeeDataList(List<LabCheckEmployeeData> labCheckEmployeeDataList) {
		this.labCheckEmployeeDataList = labCheckEmployeeDataList;
	}

	public LabCheckEmployeeData getLabCheckEmployeeDataSearch() {
		return labCheckEmployeeDataSearch;
	}

	public void setLabCheckEmployeeDataSearch(LabCheckEmployeeData labCheckEmployeeDataSearch) {
		this.labCheckEmployeeDataSearch = labCheckEmployeeDataSearch;
	}

	public Integer getLabCheckUnderApproval() {
		return LabCheckStatusEnum.UNDER_APPROVAL.getCode();
	}

	public Integer getNoSampleCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.NO_SAMPLE.getCode();
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

	public Integer getSampleTakedResult() {
		return LabCheckEmployeeCheckStatusEnum.SAMPLE_TAKED.getCode();
	}

	public Integer getCheatingInCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.CHEATING.getCode();
	}

	public LabCheckEmployeeData getSelectedLabCheckEmployeeData() {
		return selectedLabCheckEmployeeData;
	}

	public void setSelectedLabCheckEmployeeData(LabCheckEmployeeData selectedLabCheckEmployeeData) {
		this.selectedLabCheckEmployeeData = selectedLabCheckEmployeeData;
	}

	public List<SetupDomain> getDomainMetrialTypeList() {
		return domainMetrialTypeList;
	}

	public void setDomainMetrialTypeList(List<SetupDomain> domainMetrialTypeList) {
		this.domainMetrialTypeList = domainMetrialTypeList;
	}

	public boolean isSave() {
		return isSave;
	}

	public void setSave(boolean isSave) {
		this.isSave = isSave;
	}

	public Integer getApprovedLabCheckStatus() {
		return LabCheckStatusEnum.APPROVED.getCode();
	}

	public Integer getUnderApprovedLabCheckStatus() {
		return LabCheckStatusEnum.UNDER_APPROVAL.getCode();
	}

	public Integer getRegisteredLabCheckStatus() {
		return LabCheckStatusEnum.REGISTERED.getCode();
	}

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public Integer getSelectedCheckStatus() {
		return selectedCheckStatus;
	}

	public void setSelectedCheckStatus(Integer selectedCheckStatus) {
		this.selectedCheckStatus = selectedCheckStatus;
	}

	public Long getSelectectedMetrial() {
		return selectectedMetrial;
	}

	public void setSelectectedMetrial(Long selectectedMetrial) {
		this.selectectedMetrial = selectectedMetrial;
	}

	public Boolean getEditprivilege() {
		return editprivilege;
	}

	public void setEditprivilege(Boolean editprivilege) {
		this.editprivilege = editprivilege;
	}

	public boolean isUserAtRegion() {
		return userAtRegion;
	}

	public void setUserAtRegion(boolean userAtRegion) {
		this.userAtRegion = userAtRegion;
	}

	public Integer getRetestCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.RETEST.getCode();
	}

	public Integer getMissionFromDirectorateReason() {
		return LabCheckReasonsEnum.RANDOM_MISSION_FROM_DIRECTORATE.getCode();
	}

	public Long getHeadQuarter() {
		return InfoSysConfigurationService.getHeadQuarter();
	}

	public List<LabCheckReasonsEnum> getLabCheckReasonsEnumList() {
		return LabCheckReasonsEnum.getAllCheckReasonsEnumValues();
	}

	public String getDepartmentType() {
		return DepartmentTypeEnum.REGION.getCode() + "," + DepartmentTypeEnum.DIRECTORATE.getCode();
	}
}