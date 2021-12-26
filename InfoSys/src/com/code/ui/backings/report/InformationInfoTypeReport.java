package com.code.ui.backings.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.info.InfoSubject;
import com.code.dal.orm.info.InfoType;
import com.code.dal.orm.setup.DepartmentData;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.WFPositionsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.info.InfoService;
import com.code.services.infosys.info.InfoTypeService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.WFPositionService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "informationInfoTypeReport")
@ViewScoped
public class InformationInfoTypeReport extends BaseBacking implements Serializable {
	private Long regionId;
	private String regionName;
	private String employeeRegionName;
	private Long sectorId;
	private String sectorName;
	private List<DepartmentData> regionDepartmentsList = new ArrayList<DepartmentData>();
	private List<DepartmentData> sectorDepartmentsList;
	private List<InfoType> infoTypeList;
	private Long infoTypeId;
	private String infoTypeDesc;
	private List<InfoSubject> infoSubjectList;
	private Long infoSubjectId;
	private String infoSubjectDesc;
	private Date fromDate;
	private Date toDate;
	private boolean isRegion;

	/**
	 * Constructor
	 */
	public InformationInfoTypeReport() {
		super();
		try {
			infoTypeList = InfoTypeService.getInfoTypes(null);
			Long empRegionId = DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());
			if (empRegionId == null) {
				employeeRegionName = null;
				regionDepartmentsList.add(DepartmentService.getDepartment(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId()));
				regionDepartmentsList.addAll(DepartmentService.getDepartmentsBydepartmentType(DepartmentTypeEnum.REGION.getCode()));
				regionId = (long) FlagsEnum.ALL.getCode();
			} else {
				employeeRegionName = DepartmentService.getDepartment(empRegionId).getArabicName();
				regionName = employeeRegionName;
				regionId = empRegionId;
				sectorDepartmentsList = DepartmentService.getDepartmentsByRegionIdAndType(regionId, null, null, DepartmentTypeEnum.SECTOR.getCode());
				isRegion = true;
			}
			fromDate = HijriDateService.getHijriSysDate();
			toDate = fromDate;
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InformationInfoTypeReport.class, e, "InformationInfoTypeReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Get info subject list from info type id
	 */
	public void getInfoSubjectByInfoTypeId() {
		try {
			if (infoTypeId == null) {
				infoSubjectList.clear();
			} else {
				infoSubjectList = InfoTypeService.getInfoSubjects(FlagsEnum.ALL.getCode(), infoTypeId, null);
			}
			infoSubjectId = null;
			infoSubjectDesc = null;
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Update Sector List
	 */
	public void updateSectorList() {
		try {
			sectorId = null;
			sectorName = null;
			sectorDepartmentsList = regionId == null ? new ArrayList<DepartmentData>() : DepartmentService.getDepartmentsByRegionIdAndType(regionId, null, null, DepartmentTypeEnum.SECTOR.getCode());
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
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
			infoTypeDesc = infoTypeId == null ? getParameterizedMessage("label_all") : InfoTypeService.getInfoTypeById(infoTypeId).getDescription();
			infoSubjectDesc = infoSubjectId == null ? getParameterizedMessage("label_all") : InfoTypeService.getsubjectById(infoSubjectId).getDescription();
			byte[] bytes = InfoService.getInformationInfoTypeBytes(regionId == null ? FlagsEnum.ALL.getCode() : regionId, sectorId == null ? FlagsEnum.ALL.getCode() : sectorId, fromDate, toDate, regionName, infoTypeId == null ? FlagsEnum.ALL.getCode() : infoTypeId, infoTypeDesc, infoSubjectId == null ? FlagsEnum.ALL.getCode() : infoSubjectId, infoSubjectDesc, loginEmpData.getFullName());
			String reportName = "Information Info Type Report";
			super.print(bytes, reportName);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
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
			infoTypeDesc = infoTypeId == null ? getParameterizedMessage("label_all") : InfoTypeService.getInfoTypeById(infoTypeId).getDescription();
			infoSubjectDesc = infoSubjectId == null ? getParameterizedMessage("label_all") : InfoTypeService.getsubjectById(infoSubjectId).getDescription();
			byte[] bytes = InfoService.getInformationInfoTypeStatisticsBytes(regionId == null ? FlagsEnum.ALL.getCode() : regionId, sectorId == null ? FlagsEnum.ALL.getCode() : sectorId, fromDate, toDate, regionName, infoTypeId == null ? FlagsEnum.ALL.getCode() : infoTypeId, infoTypeDesc, infoSubjectId == null ? FlagsEnum.ALL.getCode() : infoSubjectId, infoSubjectDesc, loginEmpData.getFullName());
			String reportName = "Information Info Type Report";
			super.print(bytes, reportName);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Reset Search Fields
	 */
	public void reset() {
		if (!isRegion) {
			regionId = null;
			regionName = null;
		}
		sectorId = null;
		sectorName = null;
		try {
			fromDate = HijriDateService.getHijriSysDate();
			toDate = HijriDateService.getHijriSysDate();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}


	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public Long getSectorId() {
		return sectorId;
	}

	public void setSectorId(Long sectorId) {
		this.sectorId = sectorId;
	}

	public String getSectorName() {
		return sectorName;
	}

	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}

	public List<DepartmentData> getRegionDepartmentsList() {
		return regionDepartmentsList;
	}

	public void setRegionDepartmentsList(List<DepartmentData> regionDepartmentsList) {
		this.regionDepartmentsList = regionDepartmentsList;
	}

	public List<DepartmentData> getSectorDepartmentsList() {
		return sectorDepartmentsList;
	}

	public void setSectorDepartmentsList(List<DepartmentData> sectorDepartmentsList) {
		this.sectorDepartmentsList = sectorDepartmentsList;
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

	public List<InfoType> getInfoTypeList() {
		return infoTypeList;
	}

	public void setInfoTypeList(List<InfoType> infoTypeList) {
		this.infoTypeList = infoTypeList;
	}

	public Long getInfoTypeId() {
		return infoTypeId;
	}

	public void setInfoTypeId(Long infoTypeId) {
		this.infoTypeId = infoTypeId;
	}

	public List<InfoSubject> getInfoSubjectList() {
		return infoSubjectList;
	}

	public void setInfoSubjectList(List<InfoSubject> infoSubjectList) {
		this.infoSubjectList = infoSubjectList;
	}

	public Long getInfoSubjectId() {
		return infoSubjectId;
	}

	public void setInfoSubjectId(Long infoSubjectId) {
		this.infoSubjectId = infoSubjectId;
	}

	public String getEmployeeRegionName() {
		return employeeRegionName;
	}

	public void setEmployeeRegionName(String employeeRegionName) {
		this.employeeRegionName = employeeRegionName;
	}
}
