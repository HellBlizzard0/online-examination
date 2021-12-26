package com.code.ui.backings.securityaction;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.securitymission.VisitorEntranceData;
import com.code.dal.orm.setup.NonEmployeeData;
import com.code.enums.FlagsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.securityaction.VisitorsEntranceService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.NonEmployeeService;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;
import com.code.services.setup.DepartmentService;

@SuppressWarnings("serial")
@ManagedBean(name = "visitorsEntranceRegistration")
@ViewScoped
public class VisitorsEntranceRegistration extends BaseBacking implements Serializable {
	private VisitorEntranceData visitorEntranceData;
	private String employeeName;
	private int nonEmpExistsFlag;
	private List<VisitorEntranceData> visitorEntranceList;
	private Long loginEmpRegionId;
	private String loginEmpRegionName;

	/**
	 * Default Constructor
	 */
	public VisitorsEntranceRegistration() {
		visitorEntranceData = new VisitorEntranceData();
		try {
			Long regionId = DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());
			if (regionId != null) {
				loginEmpRegionId = regionId;
				loginEmpRegionName = DepartmentService.getDepartment(loginEmpRegionId).getArabicName();
			} else {
				loginEmpRegionId = getHeadQuarter();
				loginEmpRegionName = DepartmentService.getDepartment(loginEmpRegionId).getArabicName();
			}
			visitorEntranceList = VisitorsEntranceService.getCurrentVisitorEntranceData(loginEmpRegionId);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Validate Social Id
	 */
	public void validateSocialId() {
		try {
			if (visitorEntranceData.getVisitorIdentity() == null) {
				setNonEmpExistsFlag(FlagsEnum.ALL.getCode());
				throw new BusinessException("error_visitorIdentityMandatory");
			}
			List<NonEmployeeData> nonEmp = NonEmployeeService.getNonEmployee(visitorEntranceData.getVisitorIdentity(), null);
			setNonEmpExistsFlag(nonEmp.isEmpty() ? FlagsEnum.OFF.getCode() : FlagsEnum.ON.getCode());
			if (!nonEmp.isEmpty()) {
				visitorEntranceData.setVisitorCountryName(nonEmp.get(0).getCountryArabicName());
				visitorEntranceData.setVisitorId(nonEmp.get(0).getId());
				visitorEntranceData.setVisitorName(nonEmp.get(0).getFullName());
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(VisitorsEntranceRegistration.class, e, "VisitorsEntranceRegistration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void entryRegister() {
		try {
			visitorEntranceData.setEntryDate(HijriDateService.getHijriSysDate());
			String hrs = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "";
			if (hrs.length() < 2) {
				hrs = "0" + hrs;
			}
			String mins = Calendar.getInstance().get(Calendar.MINUTE) + "";
			if (mins.length() < 2) {
				mins = "0" + mins;
			}
			visitorEntranceData.setEntryTime(hrs + ":" + mins);
			visitorEntranceData.setRegionId(loginEmpRegionId);
			VisitorsEntranceService.addVisitorEntrance(loginEmpData, visitorEntranceData);
			visitorEntranceList.add(0, visitorEntranceData);
			visitorEntranceData = new VisitorEntranceData();
			employeeName = null;
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(VisitorsEntranceRegistration.class, e, "VisitorsEntranceRegistration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void exitRegister() {
		try {
			visitorEntranceData.setExitDate(HijriDateService.getHijriSysDate());
			String hrs = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "";
			if (hrs.length() < 2) {
				hrs = "0" + hrs;
			}
			String mins = Calendar.getInstance().get(Calendar.MINUTE) + "";
			if (mins.length() < 2) {
				mins = "0" + mins;
			}
			visitorEntranceData.setExitTime(hrs + ":" + mins);

			int index = FlagsEnum.ALL.getCode();
			for (int i = 0; i < visitorEntranceList.size(); i++) {
				VisitorEntranceData prevVisitorEntranceData = visitorEntranceList.get(i);
				if (prevVisitorEntranceData.getVisitorId().equals(visitorEntranceData.getVisitorId())) {
					if (prevVisitorEntranceData.getExitDate() != null) {
						break;
					}
					prevVisitorEntranceData.setExitDate(visitorEntranceData.getExitDate());
					prevVisitorEntranceData.setExitTime(visitorEntranceData.getExitTime());
					visitorEntranceData = prevVisitorEntranceData;
					index = i;
					break;
				}
			}
			if (index == FlagsEnum.ALL.getCode()) {
				visitorEntranceData.setRegionId(loginEmpRegionId);
				VisitorsEntranceService.addVisitorEntrance(loginEmpData, visitorEntranceData);
			} else {
				VisitorsEntranceService.updateVisitorEntrance(loginEmpData, visitorEntranceData);
				visitorEntranceList.remove(index);
			}
			visitorEntranceList.add(0, visitorEntranceData);
			visitorEntranceData = new VisitorEntranceData();
			employeeName = null;
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(VisitorsEntranceRegistration.class, e, "VisitorsEntranceRegistration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	// Setter and getters
	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public VisitorEntranceData getVisitorEntranceData() {
		return visitorEntranceData;
	}

	public void setVisitorEntranceData(VisitorEntranceData visitorEntranceData) {
		this.visitorEntranceData = visitorEntranceData;
	}

	public List<VisitorEntranceData> getVisitorEntranceList() {
		return visitorEntranceList;
	}

	public void setVisitorEntranceList(List<VisitorEntranceData> visitorEntranceList) {
		this.visitorEntranceList = visitorEntranceList;
	}

	public int getNonEmpExistsFlag() {
		return nonEmpExistsFlag;
	}

	public void setNonEmpExistsFlag(int nonEmpExistsFlag) {
		this.nonEmpExistsFlag = nonEmpExistsFlag;
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