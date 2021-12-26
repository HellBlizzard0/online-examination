package com.code.ui.backings.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.info.Classification;
import com.code.dal.orm.info.ClassificationType;
import com.code.dal.orm.setup.DepartmentData;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.WFPositionsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.info.InfoService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.ClassificationService;
import com.code.services.setup.DepartmentService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.WFPositionService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "informationAnalysisReport")
@ViewScoped
public class InformationAnalysisReport extends BaseBacking implements Serializable {
	private Long regionId;
	private String regionName;
	private String employeeRegionName;
	private Long sectorId;
	private String sectorName;
	private List<DepartmentData> regionDepartmentsList = new ArrayList<DepartmentData>();
	private List<DepartmentData> sectorDepartmentsList;
	private List<ClassificationType> classificationTypesList;
	private Long classificationTypeId;
	private String classificationTypeDesc;
	private List<Classification> classificationsList;
	private Long classificationId;
	private String classificationDesc;
	private Date fromDate;
	private Date toDate;
	private boolean isRegion;

	/**
	 * Constructor
	 */
	public InformationAnalysisReport() {
		super();
		try {
			classificationTypesList = ClassificationService.getClassificationTypes(null, null);
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
			   Log4j.traceErrorException(InformationAnalysisReport.class, e, "InformationAnalysisReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
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
	 * Update Sector List
	 */
	public void updateClassificationList() {
		try {
			classificationId = null;
			classificationDesc = null;
			if (classificationTypeId == null) {
				classificationsList.clear();
			} else {
				classificationsList = ClassificationService.getClassifications(classificationTypeId, null);
			}
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
			classificationTypeDesc = classificationTypeId == null ? getParameterizedMessage("label_all") : ClassificationService.getClassificationTypes(classificationTypeId, null).get(0).getDescription();
			classificationDesc = classificationId == null ? getParameterizedMessage("label_all") : ClassificationService.getClassificationById(classificationId).getDescription();
			byte[] bytes = InfoService.getInformationAnalysisBytes(regionId == null ? FlagsEnum.ALL.getCode() : regionId, sectorId == null ? FlagsEnum.ALL.getCode() : sectorId, fromDate, toDate, regionName, classificationTypeId == null ? FlagsEnum.ALL.getCode() : classificationTypeId, classificationTypeDesc, classificationId == null ? FlagsEnum.ALL.getCode() : classificationId, classificationDesc, loginEmpData.getFullName());
			String reportName = "Information Analysis Report";
			super.print(bytes, reportName);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InformationAnalysisReport.class, e, "InformationAnalysisReport");
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
			classificationTypeDesc = classificationTypeId == null ? getParameterizedMessage("label_all") : ClassificationService.getClassificationTypes(classificationTypeId, null).get(0).getDescription();
			classificationDesc = classificationId == null ? getParameterizedMessage("label_all") : ClassificationService.getClassificationById(classificationId).getDescription();
			byte[] bytes = InfoService.getInformationAnalysisStatisticsBytes(regionId == null ? FlagsEnum.ALL.getCode() : regionId, sectorId == null ? FlagsEnum.ALL.getCode() : sectorId, fromDate, toDate, regionName, classificationTypeId == null ? FlagsEnum.ALL.getCode() : classificationTypeId, classificationTypeDesc, classificationId == null ? FlagsEnum.ALL.getCode() : classificationId, classificationDesc, loginEmpData.getFullName());
			String reportName = "Information Analysis Report";
			super.print(bytes, reportName);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InformationAnalysisReport.class, e, "InformationAnalysisReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
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

	public List<ClassificationType> getClassificationTypesList() {
		return classificationTypesList;
	}

	public void setClassificationTypesList(List<ClassificationType> classificationTypesList) {
		this.classificationTypesList = classificationTypesList;
	}

	public Long getClassificationTypeId() {
		return classificationTypeId;
	}

	public void setClassificationTypeId(Long classificationTypeId) {
		this.classificationTypeId = classificationTypeId;
	}

	public String getClassificationTypeDesc() {
		return classificationTypeDesc;
	}

	public void setClassificationTypeDesc(String classificationTypeDesc) {
		this.classificationTypeDesc = classificationTypeDesc;
	}

	public List<Classification> getClassificationsList() {
		return classificationsList;
	}

	public void setClassificationsList(List<Classification> classificationsList) {
		this.classificationsList = classificationsList;
	}

	public Long getClassificationId() {
		return classificationId;
	}

	public void setClassificationId(Long classificationId) {
		this.classificationId = classificationId;
	}

	public String getClassificationDesc() {
		return classificationDesc;
	}

	public void setClassificationDesc(String classificationDesc) {
		this.classificationDesc = classificationDesc;
	}

	public String getEmployeeRegionName() {
		return employeeRegionName;
	}

	public void setEmployeeRegionName(String employeeRegionName) {
		this.employeeRegionName = employeeRegionName;
	}
}
