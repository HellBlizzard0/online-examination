package com.code.ui.backings.finance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.finance.FinMonthlyRewardResourceData;
import com.code.dal.orm.setup.DepartmentData;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.FinMonthlyRewardStatusEnum;
import com.code.enums.WFPositionsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.finance.FinanceAccountsService;
import com.code.services.infosys.finance.MonthlyRewardPaymentService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.WFPositionService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "monthlyRewardSearch")
@ViewScoped
public class MonthlyRewardPaymentsSearch extends BaseBacking implements Serializable {
	private List<FinMonthlyRewardResourceData> finMonthlyRewardResourceDataList;
	private List<FinMonthlyRewardResourceData> updatedFinMonthlyRewardResourceDataList;
	private FinMonthlyRewardResourceData finMonthlyRewardResourceDataSearch;
	private List<DepartmentData> sectorsDepartments;
	private List<DepartmentData> regionDepartments;
	private List<Long> finYears;
	private Integer pageMode = 1;
	private DepartmentData loginDep;

	public MonthlyRewardPaymentsSearch() {
		super();
		init();
		finMonthlyRewardResourceDataList = new ArrayList<FinMonthlyRewardResourceData>();
		finMonthlyRewardResourceDataSearch = new FinMonthlyRewardResourceData();
		sectorsDepartments = new ArrayList<DepartmentData>();
		finMonthlyRewardResourceDataSearch.setYear(Calendar.getInstance().get(Calendar.YEAR));
		try {
			finMonthlyRewardResourceDataSearch.setMonth(HijriDateService.getHijriDateMonth(HijriDateService.getHijriSysDate()));
			finYears = FinanceAccountsService.getAllFinYears();
			loginDep = DepartmentService.getDepartment(loginEmpData.getActualDepartmentId());
			regionDepartments = DepartmentService.getDepartmentsBydepartmentType(DepartmentTypeEnum.REGION.getCode());
			if (loginDep.getRegionId() != null && !loginDep.getRegionId().equals(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId())) {
				pageMode = 2;
				finMonthlyRewardResourceDataSearch.setAgentRegionId(loginDep.getRegionId());
				finMonthlyRewardResourceDataSearch.setAgentOfficerId(loginEmpData.getEmpId());
				finMonthlyRewardResourceDataSearch.setAgentOfficerName(loginEmpData.getFullName());
				sectorsDepartments = DepartmentService.getDepartmentsByRegionIdAndType(loginDep.getRegionId(), null, null, DepartmentTypeEnum.SECTOR.getCode());
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(MonthlyRewardPaymentsSearch.class, e, "MonthlyRewardPaymentsSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void search() {
		try {
			finMonthlyRewardResourceDataList = MonthlyRewardPaymentService.getFinMonthlyRewardResourceData(finMonthlyRewardResourceDataSearch.getAssignmentDetailId(), finMonthlyRewardResourceDataSearch.getAgentOfficerId(), finMonthlyRewardResourceDataSearch.getAgentRegionId(), finMonthlyRewardResourceDataSearch.getAgentSectorId(), finMonthlyRewardResourceDataSearch.getMonth(), finMonthlyRewardResourceDataSearch.getYear(), FinMonthlyRewardStatusEnum.APPROVED.getCode());
			for (FinMonthlyRewardResourceData finMonthlyRewardResourceData : finMonthlyRewardResourceDataList) {
				if (finMonthlyRewardResourceData.getReceived() != null) {
					finMonthlyRewardResourceData.setSelected(finMonthlyRewardResourceData.getReceived());
				}
			}
			if (finMonthlyRewardResourceDataList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(MonthlyRewardPaymentsSearch.class, e, "MonthlyRewardPaymentsSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void resetSearch() {
		finMonthlyRewardResourceDataSearch.setYear(Calendar.getInstance().get(Calendar.YEAR));
		finMonthlyRewardResourceDataSearch = new FinMonthlyRewardResourceData();
		try {
			if (loginDep.getRegionId() != null && !loginDep.getRegionId().equals(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId())) {
				finMonthlyRewardResourceDataSearch.setAgentRegionId(loginDep.getRegionId());
				finMonthlyRewardResourceDataSearch.setAgentOfficerId(loginEmpData.getEmpId());
				finMonthlyRewardResourceDataSearch.setAgentOfficerName(loginEmpData.getFullName());
			}
			finMonthlyRewardResourceDataSearch.setMonth(HijriDateService.getHijriDateMonth(HijriDateService.getHijriSysDate()));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(MonthlyRewardPaymentsSearch.class, e, "MonthlyRewardPaymentsSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void getSectorsByRegionId() {
		if (finMonthlyRewardResourceDataSearch.getAgentRegionId() == null) {
			sectorsDepartments = new ArrayList<DepartmentData>();
			return;
		}
		try {
			sectorsDepartments = DepartmentService.getAssignmentDepartments(finMonthlyRewardResourceDataSearch.getAgentRegionId());
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(MonthlyRewardPaymentsSearch.class, e, "MonthlyRewardPaymentsSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Search for car rental
	 */
	public void checkUncheckAllResources() {
		for (FinMonthlyRewardResourceData finMonthlyRewardResourceData : finMonthlyRewardResourceDataList) {
			if (finMonthlyRewardResourceData.getReceived() == null || !finMonthlyRewardResourceData.getReceived()) {
				finMonthlyRewardResourceData.setSelected(!finMonthlyRewardResourceData.getSelected());
				raiseChangedFlag(finMonthlyRewardResourceData);
			}
		}
	}

	public void updateFinMonthlyRewardResource() {
		try {
			if (finMonthlyRewardResourceDataList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_atLeastOneChoosed"));
			} else if (updatedFinMonthlyRewardResourceDataList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noEmployeeSelected"));
			} else {
				for (FinMonthlyRewardResourceData finMonthlyRewardResourceData : updatedFinMonthlyRewardResourceDataList) {
					finMonthlyRewardResourceData.setReceived(finMonthlyRewardResourceData.getSelected());
				}
				MonthlyRewardPaymentService.updateFinMonthlyRewardResourceData(updatedFinMonthlyRewardResourceDataList, loginEmpData);
				updatedFinMonthlyRewardResourceDataList = null;
			}
		} catch (BusinessException e) {
			for (FinMonthlyRewardResourceData finMonthlyRewardResourceData : updatedFinMonthlyRewardResourceDataList) {
				finMonthlyRewardResourceData.setReceived(null);
			}
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	public void raiseChangedFlag(FinMonthlyRewardResourceData record) {
		if (updatedFinMonthlyRewardResourceDataList == null) {
			updatedFinMonthlyRewardResourceDataList = new ArrayList<FinMonthlyRewardResourceData>();
		}
		if (record.getSelected() && !updatedFinMonthlyRewardResourceDataList.contains(record)) {
			updatedFinMonthlyRewardResourceDataList.add(record);
		} else {
			updatedFinMonthlyRewardResourceDataList.remove(record);
		}
	}

	/********************************************************** Setters And Getters **********************************************/

	public List<FinMonthlyRewardResourceData> getFinMonthlyRewardResourceDataList() {
		return finMonthlyRewardResourceDataList;
	}

	public void setFinMonthlyRewardResourceDataList(List<FinMonthlyRewardResourceData> finMonthlyRewardResourceDataList) {
		this.finMonthlyRewardResourceDataList = finMonthlyRewardResourceDataList;
	}

	public FinMonthlyRewardResourceData getFinMonthlyRewardResourceDataSearch() {
		return finMonthlyRewardResourceDataSearch;
	}

	public void setFinMonthlyRewardResourceDataSearch(FinMonthlyRewardResourceData finMonthlyRewardResourceDataSearch) {
		this.finMonthlyRewardResourceDataSearch = finMonthlyRewardResourceDataSearch;
	}

	public List<DepartmentData> getSectorsDepartments() {
		return sectorsDepartments;
	}

	public void setSectorsDepartments(List<DepartmentData> sectorsDepartments) {
		this.sectorsDepartments = sectorsDepartments;
	}

	public List<DepartmentData> getRegionDepartments() {
		return regionDepartments;
	}

	public void setRegionDepartments(List<DepartmentData> regionDepartments) {
		this.regionDepartments = regionDepartments;
	}

	public Integer getPageMode() {
		return pageMode;
	}

	public void setPageMode(Integer pageMode) {
		this.pageMode = pageMode;
	}

	public List<Long> getFinYears() {
		return finYears;
	}

	public void setFinYears(List<Long> finYears) {
		this.finYears = finYears;
	}
}
