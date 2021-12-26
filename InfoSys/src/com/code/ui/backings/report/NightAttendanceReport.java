package com.code.ui.backings.report;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.data.SortEvent;

import com.code.dal.orm.securitymission.EmployeeNonEmployeeAttendanceCarData;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.FlagsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.securityaction.EmployeeNonEmployeeCarService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "nightAttendanceReport")
@ViewScoped
public class NightAttendanceReport extends BaseBacking implements Serializable {
	private Date startDate;
	private Date endDate;
	private String startTime;
	private String endTime;
	private String socialId;
	private String employeeName;
	private Long departmentId;
	private String departmentName;
	private int type;
	private List<EmployeeNonEmployeeAttendanceCarData> employeeNonEmployeeAttendanceDetailsList;
	private EmployeeNonEmployeeAttendanceCarData selectedAttendanceCar = new EmployeeNonEmployeeAttendanceCarData();
	private Integer orderParameter;
	private Integer sortingIndex = 3;
	private String sortingType = "ASC";
	private Long regionId;
	private String regionName;

	/**
	 * 
	 */
	public NightAttendanceReport() {
		type = FlagsEnum.ALL.getCode();
		try {
			Long regionDepId = DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());
			if (regionDepId != null) {
				regionId = regionDepId;
				regionName = DepartmentService.getDepartment(regionId).getArabicName();
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Reset search parameters
	 */
	public void reset() {
		startDate = null;
		endDate = null;
		startTime = null;
		endTime = null;
		type = FlagsEnum.ALL.getCode();
		regionId = null;
		regionName = "";
		try {
			Long regionDepId = DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());
			if (regionDepId != null) {
				regionId = regionDepId;
				regionName = DepartmentService.getDepartment(regionId).getArabicName();
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * 
	 */
	public void search() {
		try {
			if (startDate == null || endDate == null) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_mandatory"));
				return;
			}
			employeeNonEmployeeAttendanceDetailsList = EmployeeNonEmployeeCarService.getEmployeeNonEmployeeAttendanceCarDataDetails(type, startDate, endDate, startTime, endTime, departmentId, socialId, regionId == null ? FlagsEnum.ALL.getCode() : regionId);
			if (employeeNonEmployeeAttendanceDetailsList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * 
	 * @param event
	 */
	public void updateOrderParam(SortEvent event) {
		if (event.getSortColumnIndex() == 1) {
			System.out.println();
		}
		String columnName = event.getSortColumn().getValueExpression("sortBy").getExpressionString();
		if (columnName.equals("#{attendanceDtl.fullName}")) {
			sortingIndex = 1;
		} else if (columnName.equals("#{attendanceDtl.socialId}")) {
			sortingIndex = 2;
		} else if (columnName.equals("#{attendanceDtl.entryDate} #{attendanceDtl.entryTime}")) {
			sortingIndex = 3;
		} else if (columnName.equals("#{attendanceDtl.exitDate} #{attendanceDtl.exitTime}")) {
			sortingIndex = 4;
		} else if (columnName.equals("#{attendanceDtl.carModel}")) {
			sortingIndex = 5;
		} else if (columnName.equals("#{attendanceDtl.plateNumber}")) {
			sortingIndex = 6;
		} else if (columnName.equals("#{attendanceDtl.permitNo}")) {
			sortingIndex = 2;
		}
		sortingType = event.isAscending() ? "ASC" : "DESC";
	}

	/**
	 * Print report
	 * 
	 */
	public void printReport() {
		if (startDate == null || endDate == null) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_mandatory"));
			return;
		}
		try {
			String depName = "";
			if (regionId != null) {
				depName = DepartmentService.getDepartment(regionId).getArabicName();
			}
			byte[] bytes = EmployeeNonEmployeeCarService.getNightAttendanceReportBytes(HijriDateService.getHijriDateString(startDate), HijriDateService.getHijriDateString(endDate), startTime, endTime, type, loginEmpData.getFullName(), socialId, departmentId, sortingIndex, sortingType, regionId == null ? FlagsEnum.ALL.getCode() : regionId, depName);
			super.print(bytes, "Night_Attendance_Report");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			Log4j.traceErrorException(NightAttendanceReport.class, e, "NightAttendanceReport");
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	/**
	 * Select A Car Object
	 * 
	 * @param empNonEmpAttCarData
	 */
	public void selectCar(EmployeeNonEmployeeAttendanceCarData empNonEmpAttCarData) {
		selectedAttendanceCar = empNonEmpAttCarData;
	}

	// setters and getters
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Integer getOrderParameter() {
		return orderParameter;
	}

	public void setOrderParameter(Integer orderParameter) {
		this.orderParameter = orderParameter;
	}

	public EmployeeNonEmployeeAttendanceCarData getSelectedAttendanceCar() {
		return selectedAttendanceCar;
	}

	public void setSelectedAttendanceCar(EmployeeNonEmployeeAttendanceCarData selectedAttendanceCar) {
		this.selectedAttendanceCar = selectedAttendanceCar;
	}

	public List<EmployeeNonEmployeeAttendanceCarData> getEmployeeNonEmployeeAttendanceDetailsList() {
		return employeeNonEmployeeAttendanceDetailsList;
	}

	public void setEmployeeNonEmployeeAttendanceDetailsList(List<EmployeeNonEmployeeAttendanceCarData> employeeNonEmployeeAttendanceDetailsList) {
		this.employeeNonEmployeeAttendanceDetailsList = employeeNonEmployeeAttendanceDetailsList;
	}

	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
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

	public String getDepartmentType() {
		return DepartmentTypeEnum.REGION.getCode() + "," + DepartmentTypeEnum.DIRECTORATE.getCode();
	}

}