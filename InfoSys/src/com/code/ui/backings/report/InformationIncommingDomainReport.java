package com.code.ui.backings.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.setup.DepartmentData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.InfoImportanceEnum;
import com.code.enums.WFPositionsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.info.InfoService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.SetupService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.WFPositionService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "informationIncommingDomainReport")
@ViewScoped
public class InformationIncommingDomainReport extends BaseBacking implements Serializable {
	private List<DepartmentData> regions = new ArrayList<DepartmentData>();
	private Long regionId;
	private String employeeRegionName;
	private Date fromDate;
	private Date toDate;
	private List<SetupDomain> incomingSideDomainList;
	private Long incommingId;
	private String incommingName;
	private Integer importance;
	private Map<Long, String> regionsMap;
	private boolean isRegion;
	/**
	 * Constructor
	 */
	public InformationIncommingDomainReport() {
		super();
		try {
			Long empRegionId = DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());
			if (empRegionId == null) {
				employeeRegionName = null;
				regions.add(DepartmentService.getDepartment(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId()));
				regions.addAll(DepartmentService.getDepartmentsBydepartmentType(DepartmentTypeEnum.REGION.getCode()));
				regionId = (long) FlagsEnum.ALL.getCode();
			} else {
				employeeRegionName = DepartmentService.getDepartment(empRegionId).getArabicName();
				regionId = empRegionId;
				isRegion = true;
			}
			incomingSideDomainList = SetupService.getDomains(ClassesEnum.INCOMING_SIDES.getCode());
			fromDate = HijriDateService.getHijriSysDate();
			toDate = fromDate;
			regionsMap = new HashMap<Long, String>();
			for (DepartmentData region : regions) {
				regionsMap.put(region.getId(), region.getArabicName());
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InformationIncommingDomainReport.class, e, "InformationIncommingDomainReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Print Report Using Given Fields
	 */
	public void printReport() { 
		if (fromDate == null || toDate == null) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_mandatory"));
			return;
		}
		try {
			if (incommingId == null) {
				incommingName = getParameterizedMessage("label_all");
			} else {
				for (int i = 0; i < incomingSideDomainList.size(); i++) {
					if (incomingSideDomainList.get(i).getId() == incommingId) {
						incommingName = incomingSideDomainList.get(i).getDescription();
					}
				}
			}
			byte[] bytes = InfoService.getInformationIncommingDomainBytes(regionId == null ? FlagsEnum.ALL.getCode() : regionId, employeeRegionName == null ? regionsMap.get(regionId) : employeeRegionName,incommingId == null ? FlagsEnum.ALL.getCode() : incommingId, incommingName, importance == null ? FlagsEnum.ALL.getCode() : importance, fromDate, toDate, loginEmpData.getFullName());
			String reportName = "Information Incomming Domain Report";
			super.print(bytes, reportName);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InformationIncommingDomainReport.class, e, "InformationIncommingDomainReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Print Report Using Given Fields
	 */
	public void printReportStatistics() {
		if (fromDate == null || toDate == null) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_mandatory"));
			return;
		}
		try {
			if (incommingId == null) {
				incommingName = getParameterizedMessage("label_all");
			} else {
				for (int i = 0; i < incomingSideDomainList.size(); i++) {
					if (incomingSideDomainList.get(i).getId() == incommingId) {
						incommingName = incomingSideDomainList.get(i).getDescription();
					}
				}
			}
			byte[] bytes = InfoService.getInformationIncommingDomainStatisticsBytes(regionId == null ? FlagsEnum.ALL.getCode() : regionId, employeeRegionName == null ? regionsMap.get(regionId) : employeeRegionName ,incommingId == null ? FlagsEnum.ALL.getCode() : incommingId, incommingName, importance == null ? FlagsEnum.ALL.getCode() : importance, fromDate, toDate, loginEmpData.getFullName());
			String reportName = "Information Incomming Domain Report";
			super.print(bytes, reportName);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InformationIncommingDomainReport.class, e, "InformationIncommingDomainReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Reset Search Fields
	 */
	public void reset() {
		incommingId = null;
		incommingName = null;
		importance = null;
		if (!isRegion) {
			regionId = null;
		}
		try {
			fromDate = HijriDateService.getHijriSysDate();
			toDate = HijriDateService.getHijriSysDate();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}
	public List<DepartmentData> getRegions() {
		return regions;
	}

	public void setRegions(List<DepartmentData> regions) {
		this.regions = regions;
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}
	public String getEmployeeRegionName() {
		return employeeRegionName;
	}

	public void setEmployeeRegionName(String employeeRegionName) {
		this.employeeRegionName = employeeRegionName;
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

	public List<SetupDomain> getIncomingSideDomainList() {
		return incomingSideDomainList;
	}

	public void setIncomingSideDomainList(List<SetupDomain> incomingSideDomainList) {
		this.incomingSideDomainList = incomingSideDomainList;
	}

	public Long getIncommingId() {
		return incommingId;
	}

	public void setIncommingId(Long incommingId) {
		this.incommingId = incommingId;
	}

	public String getIncommingName() {
		return incommingName;
	}

	public void setIncommingName(String incommingName) {
		this.incommingName = incommingName;
	}

	public Integer getImportance() {
		return importance;
	}

	public void setImportance(Integer importance) {
		this.importance = importance;
	}

	public Integer getInfoImportanceEnumImmediate() {
		return InfoImportanceEnum.IMMEDIATE.getCode();
	}

	public Integer getInfoImportanceEnumUrgent() {
		return InfoImportanceEnum.URGENT.getCode();
	}

	public Integer getInfoImportanceEnumMedium() {
		return InfoImportanceEnum.MEDIUM.getCode();
	}

	public Integer getInfoImportanceEnumNormal() {
		return InfoImportanceEnum.NORMAL.getCode();
	}

	public Integer getInfoImportanceEnumWithout() {
		return InfoImportanceEnum.WITHOUT.getCode();
	}
}
