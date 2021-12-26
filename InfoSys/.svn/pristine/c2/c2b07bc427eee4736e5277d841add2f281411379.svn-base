package com.code.ui.backings.securityaction;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.security.UserMenuActionData;
import com.code.dal.orm.securitymission.EmployeeNonEmployeeAttendance;
import com.code.dal.orm.securitymission.EmployeeNonEmployeeAttendanceCarData;
import com.code.dal.orm.securitymission.NonEmployeePermit;
import com.code.dal.orm.securitymission.VisitorEntranceData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.setup.NonEmployeeData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.UserMenuActionsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.securityaction.EmployeeNonEmployeeCarService;
import com.code.services.infosys.securityaction.VisitorsEntranceService;
import com.code.services.log4j.Log4j;
import com.code.services.security.SecurityService;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.EmployeeService;
import com.code.services.setup.NonEmployeeService;
import com.code.services.setup.SetupService;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "employeeNonEmployeeNightAttendance")
@ViewScoped
public class EmployeeNonEmployeeNightAttendance extends BaseBacking implements Serializable {
	private Long entrySocialId;
	private Long exitSocialId;
	private Long selectedNonEmployeeId;
	private Long selectedEmployeeId;
	private List<EmployeeNonEmployeeAttendanceCarData> employeeNonEmployeeAttendanceDetailsList;
	private int showMsg = 0;
	private String errorMsg = "";
	private String entryPermitNo;
	private String entryPlateChar1;
	private String entryPlateChar2;
	private String entryPlateChar3;
	private String entryPlateNumber;
	private String exitPermitNo;
	private String exitPlateChar1;
	private String exitPlateChar2;
	private String exitPlateChar3;
	private String exitPlateNumber;
	private EmployeeNonEmployeeAttendanceCarData selectedCar = new EmployeeNonEmployeeAttendanceCarData();
	private List<SetupDomain> carModels;
	private String name;
	private boolean canUpdateCar;
	VisitorEntranceData selectedVisitorEntranceData;
	private Long loginEmpRegionId;
	private String loginEmpRegionName;

	public EmployeeNonEmployeeNightAttendance() throws BusinessException {
		init();
		UserMenuActionData canUpdateCarAction = SecurityService.getAction(loginEmpData.getEmpId(), UserMenuActionsEnum.UPDATE_ATTENDANCE_CAR.getCode(), FlagsEnum.ALL.getCode());
		if (canUpdateCarAction != null) {
			canUpdateCar = true;
		}
		selectedCar = new EmployeeNonEmployeeAttendanceCarData();
		carModels = SetupService.getDomains(ClassesEnum.CAR_MODELS.getCode());
		try {
			Long regionId = DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());
			if (regionId != null) {
				loginEmpRegionId = regionId;
				loginEmpRegionName = DepartmentService.getDepartment(loginEmpRegionId).getArabicName();
			} else {
				loginEmpRegionId = getHeadQuarter();
				loginEmpRegionName = DepartmentService.getDepartment(loginEmpRegionId).getArabicName();
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
		employeeNonEmployeeAttendanceDetailsList = EmployeeNonEmployeeCarService.getEmployeeNonEmployeeAttendanceCarData(null, HijriDateService.getHijriSysDate(), loginEmpRegionId);
	}

	/**
	 * reset
	 */
	public void reset() {
		entrySocialId = null;
		exitSocialId = null;
		selectedNonEmployeeId = null;
		selectedEmployeeId = null;
		showMsg = 0;
		errorMsg = null;
		selectedCar = new EmployeeNonEmployeeAttendanceCarData();
		entryPermitNo = null;
		entryPlateChar1 = null;
		entryPlateChar2 = null;
		entryPlateChar3 = null;
		entryPlateNumber = null;
		exitPermitNo = null;
		exitPlateChar1 = null;
		exitPlateChar2 = null;
		exitPlateChar3 = null;
		exitPlateNumber = null;
		name = null;
	}

	/**
	 * validateSocialId
	 */
	public void validateSocialId(int mode) {
		try {
			Long socialId = null;
			if (mode == 1)
				socialId = entrySocialId;
			else
				socialId = exitSocialId;

			reset();
			if(socialId == null || socialId.toString().isEmpty())
				throw new BusinessException("error_identityNumberMandatory");
			EmployeeData emp = EmployeeService.getEmployee(socialId.toString());
			if (emp == null) {
				selectedVisitorEntranceData = VisitorsEntranceService.getCurrentVisitorExitByIdentity(socialId);
				if (mode != 1 && selectedVisitorEntranceData != null) {
					selectedNonEmployeeId = selectedVisitorEntranceData.getVisitorId();
					exitRegister();
				} else {
					List<NonEmployeePermit> permits = EmployeeNonEmployeeCarService.getNonEmployeePermit(socialId);
					if (!permits.isEmpty() && permits.get(0).getEndDate().after(HijriDateService.getHijriSysDate())) {
						selectedNonEmployeeId = permits.get(0).getNonEmployeeId();
						if (mode == 1)
							entryRegister();
						else
							exitRegister();
					} else if (!permits.isEmpty()) {
						showMsg = 1;
						errorMsg = getParameterizedMessage("error_permitDateEnded");
						selectedNonEmployeeId = permits.get(0).getNonEmployeeId();
					} else {
						List<NonEmployeeData> nonEmp = NonEmployeeService.getNonEmployee(socialId, null);
						if (!nonEmp.isEmpty()) {
							showMsg = 1;
							errorMsg = getParameterizedMessage("error_nonPermitFound");
							selectedNonEmployeeId = nonEmp.get(0).getId();
						} else {
							showMsg = 2;
						}
					}
				}
			} else {
				selectedEmployeeId = emp.getEmpId();
				if (mode == 1)
					entryRegister();
				else
					exitRegister();
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(EmployeeNonEmployeeNightAttendance.class, e, "EmployeeNonEmployeeNightAttendance");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Add entry register record
	 */
	public void entryRegister() {
		try {
			EmployeeNonEmployeeAttendance employeeNonEmployeeAttendanceDetails = new EmployeeNonEmployeeAttendance();
			employeeNonEmployeeAttendanceDetails.setEntryDate(HijriDateService.getHijriSysDate());
			if (selectedEmployeeId == null) {
				employeeNonEmployeeAttendanceDetails.setNonEmployeeId(selectedNonEmployeeId);
			} else {
				employeeNonEmployeeAttendanceDetails.setEmployeeId(selectedEmployeeId);
			}
			String hrs = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "";
			if (hrs.length() < 2) {
				hrs = "0" + hrs;
			}
			String mins = Calendar.getInstance().get(Calendar.MINUTE) + "";
			if (mins.length() < 2) {
				mins = "0" + mins;
			}
			employeeNonEmployeeAttendanceDetails.setEntryTime(hrs + ":" + mins);
			employeeNonEmployeeAttendanceDetails.setRegionId(loginEmpRegionId);
			employeeNonEmployeeAttendanceDetailsList.add(0, EmployeeNonEmployeeCarService.saveEmployeeNonEmployeeAttendanceDetailsEntry(employeeNonEmployeeAttendanceDetails, null, loginEmpData));
			reset();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(EmployeeNonEmployeeNightAttendance.class, e, "EmployeeNonEmployeeNightAttendance");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Add exit Register record
	 */
	public void exitRegister() {
		try {
			EmployeeNonEmployeeAttendanceCarData empNonEmpAttCarData = null;
			int index = -1;
			if (selectedVisitorEntranceData == null) {
				for (int i = 0; i < employeeNonEmployeeAttendanceDetailsList.size(); i++) {
					empNonEmpAttCarData = employeeNonEmployeeAttendanceDetailsList.get(i);
					if ((empNonEmpAttCarData.getEmployeeId() != null && empNonEmpAttCarData.getEmployeeId().equals(selectedEmployeeId)) || (empNonEmpAttCarData.getNonEmployeeId() != null && empNonEmpAttCarData.getNonEmployeeId().equals(selectedNonEmployeeId))) {
						if (empNonEmpAttCarData.getExitDate() == null) {
							index = i;
						}
						break;
					}
				}
			}
			if (index == -1) {
				empNonEmpAttCarData = new EmployeeNonEmployeeAttendanceCarData();
			}
			Date sysDate = HijriDateService.getHijriSysDate();
			empNonEmpAttCarData.setExitDate(sysDate);
			if (selectedEmployeeId == null) {
				empNonEmpAttCarData.setNonEmployeeId(selectedNonEmployeeId);
			} else {
				empNonEmpAttCarData.setEmployeeId(selectedEmployeeId);
			}
			String hrs = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "";
			if (hrs.length() < 2) {
				hrs = "0" + hrs;
			}
			String mins = Calendar.getInstance().get(Calendar.MINUTE) + "";
			if (mins.length() < 2) {
				mins = "0" + mins;
			}
			empNonEmpAttCarData.setExitTime(hrs + ":" + mins);
			if (selectedVisitorEntranceData != null) {
				selectedVisitorEntranceData.setExitDate(sysDate);
				selectedVisitorEntranceData.setExitTime(hrs + ":" + mins);
			}
			if (index == -1) {
				empNonEmpAttCarData.setRegionId(loginEmpRegionId);
				employeeNonEmployeeAttendanceDetailsList.add(0, EmployeeNonEmployeeCarService.saveEmployeeNonEmployeeAttendanceDetailsEntry(empNonEmpAttCarData.getEmployeeNonEmployeeAttendance(), selectedVisitorEntranceData, loginEmpData));
				employeeNonEmployeeAttendanceDetailsList.get(0).setVisitorCardNumber(selectedVisitorEntranceData != null ? selectedVisitorEntranceData.getVisitorCardNumber() : null);
			} else {
				employeeNonEmployeeAttendanceDetailsList.remove(index);
				employeeNonEmployeeAttendanceDetailsList.add(0, EmployeeNonEmployeeCarService.updateEmployeeNonEmployeeAttendanceDetailsEntry(empNonEmpAttCarData.getEmployeeNonEmployeeAttendance(), loginEmpData));
			}

			reset();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(EmployeeNonEmployeeNightAttendance.class, e, "EmployeeNonEmployeeNightAttendance");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Select A Car Object
	 * 
	 * @param empNonEmpAttCarData
	 */
	public void selectCar(EmployeeNonEmployeeAttendanceCarData empNonEmpAttCarData) {
		selectedCar = empNonEmpAttCarData;
	}

	/**
	 * Update Attendance Car Object
	 */
	public void updateAttendanceCar() {
		try {
			EmployeeNonEmployeeCarService.updateEmployeeNonEmployeeAttendanceDetailsEntry(selectedCar.getEmployeeNonEmployeeAttendance(), loginEmpData);
		} catch (BusinessException e) {
			selectedCar.setCarModel(null);
			selectedCar.setDomainCarModelId(null);
			selectedCar.setPlateNumber(null);
			selectedCar.setPermitNo(null);
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	public Long getEntrySocialId() {
		return entrySocialId;
	}

	public void setEntrySocialId(Long entrySocialId) {
		this.entrySocialId = entrySocialId;
	}

	public Long getExitSocialId() {
		return exitSocialId;
	}

	public void setExitSocialId(Long exitSocialId) {
		this.exitSocialId = exitSocialId;
	}

	public Long getSelectedNonEmployeeId() {
		return selectedNonEmployeeId;
	}

	public void setSelectedNonEmployeeId(Long selectedNonEmployeeId) {
		this.selectedNonEmployeeId = selectedNonEmployeeId;
	}

	public List<EmployeeNonEmployeeAttendanceCarData> getEmployeeNonEmployeeAttendanceDetailsList() {
		return employeeNonEmployeeAttendanceDetailsList;
	}

	public void setEmployeeNonEmployeeAttendanceDetailsList(List<EmployeeNonEmployeeAttendanceCarData> employeeNonEmployeeAttendanceDetailsList) {
		this.employeeNonEmployeeAttendanceDetailsList = employeeNonEmployeeAttendanceDetailsList;
	}

	public int getShowMsg() {
		return showMsg;
	}

	public void setShowMsg(int showMsg) {
		this.showMsg = showMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Long getSelectedEmployeeId() {
		return selectedEmployeeId;
	}

	public void setSelectedEmployeeId(Long selectedEmployeeId) {
		this.selectedEmployeeId = selectedEmployeeId;
	}

	public String getEntryPermitNo() {
		return entryPermitNo;
	}

	public void setEntryPermitNo(String entryPermitNo) {
		this.entryPermitNo = entryPermitNo;
	}

	public String getEntryPlateChar1() {
		return entryPlateChar1;
	}

	public void setEntryPlateChar1(String entryPlateChar1) {
		this.entryPlateChar1 = entryPlateChar1;
	}

	public String getEntryPlateChar2() {
		return entryPlateChar2;
	}

	public void setEntryPlateChar2(String entryPlateChar2) {
		this.entryPlateChar2 = entryPlateChar2;
	}

	public String getEntryPlateChar3() {
		return entryPlateChar3;
	}

	public void setEntryPlateChar3(String entryPlateChar3) {
		this.entryPlateChar3 = entryPlateChar3;
	}

	public String getExitPermitNo() {
		return exitPermitNo;
	}

	public void setExitPermitNo(String exitPermitNo) {
		this.exitPermitNo = exitPermitNo;
	}

	public String getExitPlateChar1() {
		return exitPlateChar1;
	}

	public void setExitPlateChar1(String exitPlateChar1) {
		this.exitPlateChar1 = exitPlateChar1;
	}

	public String getExitPlateChar2() {
		return exitPlateChar2;
	}

	public void setExitPlateChar2(String exitPlateChar2) {
		this.exitPlateChar2 = exitPlateChar2;
	}

	public String getExitPlateChar3() {
		return exitPlateChar3;
	}

	public void setExitPlateChar3(String exitPlateChar3) {
		this.exitPlateChar3 = exitPlateChar3;
	}

	public String getEntryPlateNumber() {
		return entryPlateNumber;
	}

	public void setEntryPlateNumber(String entryPlateNumber) {
		this.entryPlateNumber = entryPlateNumber;
	}

	public String getExitPlateNumber() {
		return exitPlateNumber;
	}

	public void setExitPlateNumber(String exitPlateNumber) {
		this.exitPlateNumber = exitPlateNumber;
	}

	public EmployeeNonEmployeeAttendanceCarData getSelectedCar() {
		return selectedCar;
	}

	public void setSelectedCar(EmployeeNonEmployeeAttendanceCarData selectedCar) {
		this.selectedCar = selectedCar;
	}

	public List<SetupDomain> getCarModels() {
		return carModels;
	}

	public void setCarModels(List<SetupDomain> carModels) {
		this.carModels = carModels;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isCanUpdateCar() {
		return canUpdateCar;
	}

	public void setCanUpdateCar(boolean canUpdateCar) {
		this.canUpdateCar = canUpdateCar;
	}

	public Long getHeadQuarter() {
		return InfoSysConfigurationService.getHeadQuarter();
	}

	public Long getLoginEmpRegionId() {
		return loginEmpRegionId;
	}

	public void setLoginEmpRegionId(Long loginEmpRegionId) {
		this.loginEmpRegionId = loginEmpRegionId;
	}

	public String getLoginEmpRegionName() {
		return loginEmpRegionName;
	}

	public void setLoginEmpRegionName(String loginEmpRegionName) {
		this.loginEmpRegionName = loginEmpRegionName;
	}

}
