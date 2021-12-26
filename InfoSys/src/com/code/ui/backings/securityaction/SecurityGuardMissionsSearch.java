package com.code.ui.backings.securityaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.security.UserMenuActionData;
import com.code.dal.orm.securitymission.MissionCarData;
import com.code.dal.orm.securitymission.MissionEquipmentData;
import com.code.dal.orm.securitymission.MissionGuardData;
import com.code.dal.orm.securitymission.MissionWeaponData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.UserMenuActionsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.securityaction.SecurityGuardMissionService;
import com.code.services.log4j.Log4j;
import com.code.services.security.SecurityService;
import com.code.services.setup.SetupService;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "securityGuardMissionSearch")
@ViewScoped
public class SecurityGuardMissionsSearch extends BaseBacking implements Serializable {
	private String missionOrderNumber;
	private Date missionOrderDate;
	private String missionNumber;
	private Date missionStartDate;
	private Long missionType;
	private Date missionEndDate;
	private long employeeId;
	private String employeeName;
	private List<SetupDomain> missionTypes;

	private List<MissionWeaponData> missionWeaponDataList;
	private List<MissionCarData> missionCarDataList;
	private List<MissionEquipmentData> missionEquipmentDataList;
	private String activeIndexString;
	private int privilege;
	
	private MissionGuardData missionGuard;

	public SecurityGuardMissionsSearch() {
		initialize();
	}

	public void initialize() {
		try {
			reset();
			missionTypes = SetupService.getDomains(ClassesEnum.MISSION_TYPES.getCode());
			UserMenuActionData carManagementActionData = SecurityService.getAction(loginEmpData.getEmpId(), UserMenuActionsEnum.CAR_MANAGEMENT_EMPLOYEE_DELIVER_CARS.getCode(),FlagsEnum.ALL.getCode());
			UserMenuActionData militaryPoliceDutyActionData = SecurityService.getAction(loginEmpData.getEmpId(), UserMenuActionsEnum.MILITARY_POLICE_DUTY_EMPLOYEE_DELIVER_EQUIPMENTS.getCode(),FlagsEnum.ALL.getCode());
			if (carManagementActionData != null && militaryPoliceDutyActionData == null) {
				privilege = 1;
			} else if (militaryPoliceDutyActionData != null && carManagementActionData == null) {
				privilege = 2;
			} else if (militaryPoliceDutyActionData != null && carManagementActionData != null) {
				privilege = 3;
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SecurityGuardMissionsSearch.class, e, "SecurityGuardMissionsSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void search() {
		try {
			missionType = missionType == null ? FlagsEnum.ALL.getCode() : missionType;
			missionOrderNumber = missionOrderNumber == null ? missionOrderNumber : missionOrderNumber.trim();
			missionNumber = missionNumber == null ? missionNumber : missionNumber.trim();
			// Search data according to privilege of the user
			if (privilege == 1) {
				missionCarDataList = SecurityGuardMissionService.getMissionCarData(FlagsEnum.ALL.getCode(), employeeId, missionNumber, missionOrderNumber, missionOrderDate, missionStartDate, missionEndDate, missionType, FlagsEnum.ON.getCode());
			} else if (privilege == 2) {
				missionWeaponDataList = SecurityGuardMissionService.getMissionWeaponData(FlagsEnum.ALL.getCode(), employeeId, missionNumber, missionOrderNumber, missionOrderDate, missionStartDate, missionEndDate, missionType, FlagsEnum.ON.getCode());
				missionEquipmentDataList = SecurityGuardMissionService.getMissionEquipmentData(FlagsEnum.ALL.getCode(), employeeId, missionNumber, missionOrderNumber, missionOrderDate, missionStartDate, missionEndDate, missionType, FlagsEnum.ON.getCode());
			} else {
				missionWeaponDataList = SecurityGuardMissionService.getMissionWeaponData(FlagsEnum.ALL.getCode(), employeeId, missionNumber, missionOrderNumber, missionOrderDate, missionStartDate, missionEndDate, missionType, FlagsEnum.ON.getCode());
				missionCarDataList = SecurityGuardMissionService.getMissionCarData(FlagsEnum.ALL.getCode(), employeeId, missionNumber, missionOrderNumber, missionOrderDate, missionStartDate, missionEndDate, missionType, FlagsEnum.ON.getCode());
				missionEquipmentDataList = SecurityGuardMissionService.getMissionEquipmentData(FlagsEnum.ALL.getCode(), employeeId, missionNumber, missionOrderNumber, missionOrderDate, missionStartDate, missionEndDate, missionType, FlagsEnum.ON.getCode());
			}

			// Show no data error message according to privilege of the user
			if (privilege == 1 && (missionCarDataList.isEmpty())) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
			} else if (privilege == 2 && (missionWeaponDataList.isEmpty() && missionEquipmentDataList.isEmpty())) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
			} else if ((privilege == 3) && (missionWeaponDataList.isEmpty() && missionCarDataList.isEmpty() && missionEquipmentDataList.isEmpty())) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SecurityGuardMissionsSearch.class, e, "SecurityGuardMissionsSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void reset() {
		missionGuard = new MissionGuardData();
		missionOrderNumber = null;
		missionOrderDate = null;
		missionNumber = null;
		missionStartDate = null;
		missionType = null;
		missionEndDate = null;
		employeeId = FlagsEnum.ALL.getCode();
		employeeName = null;
		activeIndexString = FlagsEnum.OFF.getCode() + "";
	}

	public void receiveMissionAttachments(MissionWeaponData missionWeaponData, MissionCarData missionCarData, MissionEquipmentData missionEquipmentData) {
		try {
			Date receiveDate = HijriDateService.getHijriSysDate();
			String hrs = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "";
			if (hrs.length() < 2) {
				hrs = "0" + hrs;
			}
			String mins = Calendar.getInstance().get(Calendar.MINUTE) + "";
			if (mins.length() < 2) {
				mins = "0" + mins;
			}
			String receiveTime = hrs + ":" + mins;

			if (missionWeaponData != null) {
				ArrayList<MissionWeaponData> missionWeaponDataReceiveList = new ArrayList<MissionWeaponData>();
				missionWeaponDataReceiveList.add(missionWeaponData);
				SecurityGuardMissionService.receiveWeaponList(missionWeaponDataReceiveList, receiveDate, receiveTime, loginEmpData);
				this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
				activeIndexString = "0";
			} else if (missionCarData != null) {
				ArrayList<MissionCarData> missionCarDataReceiveList = new ArrayList<MissionCarData>();
				missionCarDataReceiveList.add(missionCarData);
				SecurityGuardMissionService.receiveCarList(missionCarDataReceiveList, receiveDate, receiveTime, loginEmpData);
				this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
				activeIndexString = "1";
			} else if (missionEquipmentData != null) {
				ArrayList<MissionEquipmentData> missionEquipmentDataReceiveList = new ArrayList<MissionEquipmentData>();
				missionEquipmentDataReceiveList.add(missionEquipmentData);
				SecurityGuardMissionService.receiveEquipmentList(missionEquipmentDataReceiveList, receiveDate, receiveTime, loginEmpData);
				this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
				activeIndexString = "2";
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SecurityGuardMissionsSearch.class, e, "SecurityGuardMissionsSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void returnMissionAttachments(MissionWeaponData missionWeaponData, MissionCarData missionCarData, MissionEquipmentData missionEquipmentData) {
		try {
			Date returnDate = HijriDateService.getHijriSysDate();
			String hrs = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "";
			if (hrs.length() < 2) {
				hrs = "0" + hrs;
			}
			String mins = Calendar.getInstance().get(Calendar.MINUTE) + "";
			if (mins.length() < 2) {
				mins = "0" + mins;
			}
			String returnTime = hrs + ":" + mins;

			if (missionWeaponData != null) {
				ArrayList<MissionWeaponData> missionWeaponDataReturnList = new ArrayList<MissionWeaponData>();
				missionWeaponDataReturnList.add(missionWeaponData);
				SecurityGuardMissionService.returnWeaponList(missionWeaponDataReturnList, returnDate, returnTime, loginEmpData);
				this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
				activeIndexString = "0";
			} else if (missionCarData != null) {
				ArrayList<MissionCarData> missionCarDataReturnList = new ArrayList<MissionCarData>();
				missionCarDataReturnList.add(missionCarData);
				SecurityGuardMissionService.returnCarList(missionCarDataReturnList, returnDate, returnTime, loginEmpData);
				this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
				activeIndexString = "1";
			} else if (missionEquipmentData != null) {
				ArrayList<MissionEquipmentData> missionEquipmentDataReturnList = new ArrayList<MissionEquipmentData>();
				missionEquipmentDataReturnList.add(missionEquipmentData);
				SecurityGuardMissionService.returnEquipmentList(missionEquipmentDataReturnList, returnDate, returnTime, loginEmpData);
				this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
				activeIndexString = "2";
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SecurityGuardMissionsSearch.class, e, "SecurityGuardMissionsSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public String view(long missionGuardId) {
		getRequest().setAttribute("mode", String.valueOf(missionGuardId));
		return NavigationEnum.VIEW_GUARD_MISSION.toString();
	}

	public void recieveAll(int type) {
		try {
			String receiveDate = HijriDateService.getHijriSysDateString();
			String hrs = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "";
			if (hrs.length() < 2) {
				hrs = "0" + hrs;
			}
			String mins = Calendar.getInstance().get(Calendar.MINUTE) + "";
			if (mins.length() < 2) {
				mins = "0" + mins;
			}
			String receiveTime = hrs + ":" + mins;

			if (type == 0) {
				int count = SecurityGuardMissionService.receiveAllMissionWeapon(employeeId, missionNumber, missionOrderNumber, missionOrderDate, missionStartDate, missionEndDate, missionType, receiveDate, receiveTime);
				if (count > 0) {
					this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
				} else {
					this.setServerSideErrorMessages(getParameterizedMessage("notify_recivedAlready"));
				}
				missionWeaponDataList = SecurityGuardMissionService.getMissionWeaponData(FlagsEnum.ALL.getCode(), employeeId, missionNumber, missionOrderNumber, missionOrderDate, missionStartDate, missionEndDate, missionType, FlagsEnum.ON.getCode());
			} else if (type == 1) {
				int count = SecurityGuardMissionService.receiveAllMissionCar(employeeId, missionNumber, missionOrderNumber, missionOrderDate, missionStartDate, missionEndDate, missionType, receiveDate, receiveTime);
				if (count > 0) {
					this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
				} else {
					this.setServerSideErrorMessages(getParameterizedMessage("notify_recivedAlready"));
				}
				missionCarDataList = SecurityGuardMissionService.getMissionCarData(FlagsEnum.ALL.getCode(), employeeId, missionNumber, missionOrderNumber, missionOrderDate, missionStartDate, missionEndDate, missionType, FlagsEnum.ON.getCode());
			} else if (type == 2) {
				int count = SecurityGuardMissionService.receiveAllMissionEquipment(employeeId, missionNumber, missionOrderNumber, missionOrderDate, missionStartDate, missionEndDate, missionType, receiveDate, receiveTime);
				if (count > 0) {
					this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
				} else {
					this.setServerSideErrorMessages(getParameterizedMessage("notify_recivedAlready"));
				}
				missionEquipmentDataList = SecurityGuardMissionService.getMissionEquipmentData(FlagsEnum.ALL.getCode(), employeeId, missionNumber, missionOrderNumber, missionOrderDate, missionStartDate, missionEndDate, missionType, FlagsEnum.ON.getCode());
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SecurityGuardMissionsSearch.class, e, "SecurityGuardMissionsSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void returnAll(int type) {
		try {
			String returnDate = HijriDateService.getHijriSysDateString();
			String hrs = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "";
			if (hrs.length() < 2) {
				hrs = "0" + hrs;
			}
			String mins = Calendar.getInstance().get(Calendar.MINUTE) + "";
			if (mins.length() < 2) {
				mins = "0" + mins;
			}
			String returnTime = hrs + ":" + mins;
			if (type == 0) {
				int count = SecurityGuardMissionService.returnAllMissionWeapon(employeeId, missionNumber, missionOrderNumber, missionOrderDate, missionStartDate, missionEndDate, missionType, returnDate, returnTime);
				if (count > 0) {
					this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
				} else {
					this.setServerSideErrorMessages(getParameterizedMessage("notify_returnAlready"));
				}
				missionWeaponDataList = SecurityGuardMissionService.getMissionWeaponData(FlagsEnum.ALL.getCode(), employeeId, missionNumber, missionOrderNumber, missionOrderDate, missionStartDate, missionEndDate, missionType, FlagsEnum.ON.getCode());
			} else if (type == 1) {
				int count = SecurityGuardMissionService.returnAllMissionCar(employeeId, missionNumber, missionOrderNumber, missionOrderDate, missionStartDate, missionEndDate, missionType, returnDate, returnTime);
				if (count > 0) {
					this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
				} else {
					this.setServerSideErrorMessages(getParameterizedMessage("notify_returnAlready"));
				}
				missionCarDataList = SecurityGuardMissionService.getMissionCarData(FlagsEnum.ALL.getCode(), employeeId, missionNumber, missionOrderNumber, missionOrderDate, missionStartDate, missionEndDate, missionType, FlagsEnum.ON.getCode());
			} else if (type == 2) {
				int count = SecurityGuardMissionService.returnAllMissionEquipment(employeeId, missionNumber, missionOrderNumber, missionOrderDate, missionStartDate, missionEndDate, missionType, returnDate, returnTime);
				if (count > 0) {
					this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
				} else {
					this.setServerSideErrorMessages(getParameterizedMessage("notify_returnAlready"));
				}
				missionEquipmentDataList = SecurityGuardMissionService.getMissionEquipmentData(FlagsEnum.ALL.getCode(), employeeId, missionNumber, missionOrderNumber, missionOrderDate, missionStartDate, missionEndDate, missionType, FlagsEnum.ON.getCode());
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SecurityGuardMissionsSearch.class, e, "SecurityGuardMissionsSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}
	
	
	/**
	 * Return name of the day from day number
	 * 
	 * @param dayNo
	 * @return
	 */
	private String getDayOfWeek(int dayNo){
		String day = "";
	    switch(dayNo){
	    case 1:
	        day=getParameterizedMessage("label_sunday");
	        break;
	    case 2:
	        day=getParameterizedMessage("label_monday");
	        break;
	    case 3:
	        day=getParameterizedMessage("label_tuesday");
	        break;
	    case 4:
	        day=getParameterizedMessage("label_wednesday");
	        break;
	    case 5:
	        day=getParameterizedMessage("label_thursday");
	        break;
	    case 6:
	        day=getParameterizedMessage("label_friday");
	        break;
	    case 7:
	        day=getParameterizedMessage("label_saturday");
	        break;
	    }
	    return day;
	}
	
	/**
	 * Print report details
	 * 
	 * @param missionOrderNo
	 */
	public void print(String missionOrderNo) {
		try {
			missionGuard = SecurityGuardMissionService.getMissionGuardByOrderNumber(missionOrderNo);
			String missionEquipmentdetails = "";
			String timeFlag = "" ;
			
			for (MissionEquipmentData missionEquipmentData : missionEquipmentDataList) {
				missionEquipmentdetails += missionEquipmentData.getDomainEquipmentTypeDescription() + " - " + missionEquipmentData.getEquipmentModel() + " - " + missionEquipmentData.getBorderGuardNumber() + "  -  ";
			}
			
			Date missionStartDate = HijriDateService.hijriToGregDate(missionGuard.getMissionStartDate());
			Date missionEndDate = HijriDateService.hijriToGregDate(missionGuard.getMissionEndDate()) ;
			
			Calendar startDateCalendar = Calendar.getInstance();
			startDateCalendar.setTime(missionStartDate);
			int startDay = startDateCalendar.get(Calendar.DAY_OF_WEEK);
			
			Calendar endDateCalendar = Calendar.getInstance();
			endDateCalendar.setTime(missionEndDate); 
			int endDay = endDateCalendar.get(Calendar.DAY_OF_WEEK);
		
			String missionStartDay = getDayOfWeek(startDay) ;
			String missionEndDay = getDayOfWeek(endDay) ;
			String missionEndTime = missionGuard.getMissionEndTime() ;
			
			int endHour = Integer.parseInt(missionEndTime.substring(0,2));
			
			// condition to decide day or night time
			if(endHour < 12)
				timeFlag = "D"; // D for day
			else{
				timeFlag = "N"; // N for night
				if(endHour != 12)
					endHour -= 12;
				missionEndTime = String.valueOf(endHour) + ":" + missionEndTime.substring(3);
			}
			
			if(!missionEquipmentdetails.isEmpty()){
				missionEquipmentdetails = missionEquipmentdetails.trim();
				missionEquipmentdetails = missionEquipmentdetails.substring(0, missionEquipmentdetails.length()-1);
				}
			
			byte[] bytes = SecurityGuardMissionService.getSecurityGuardMissionBytes(missionGuard.getId(), loginEmpData.getFullName(),missionStartDay ,missionEndDay ,missionEndTime ,missionEquipmentdetails ,timeFlag );
			super.print(bytes, "Security_Guard_Mission");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SecurityGuardMissionsSearch.class, e, "SecurityGuardMissionsSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public String getMissionOrderNumber() {
		return missionOrderNumber;
	}

	public void setMissionOrderNumber(String missionOrderNumber) {
		this.missionOrderNumber = missionOrderNumber;
	}

	public Date getMissionOrderDate() {
		return missionOrderDate;
	}

	public void setMissionOrderDate(Date missionOrderDate) {
		this.missionOrderDate = missionOrderDate;
	}

	public String getMissionNumber() {
		return missionNumber;
	}

	public void setMissionNumber(String missionNumber) {
		this.missionNumber = missionNumber;
	}

	public Date getMissionStartDate() {
		return missionStartDate;
	}

	public void setMissionStartDate(Date missionStartDate) {
		this.missionStartDate = missionStartDate;
	}

	public Long getMissionType() {
		return missionType;
	}

	public void setMissionType(Long missionType) {
		this.missionType = missionType;
	}

	public Date getMissionEndDate() {
		return missionEndDate;
	}

	public void setMissionEndDate(Date missionEndDate) {
		this.missionEndDate = missionEndDate;
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

	public List<SetupDomain> getMissionTypes() {
		return missionTypes;
	}

	public void setMissionTypes(List<SetupDomain> missionTypes) {
		this.missionTypes = missionTypes;
	}

	public List<MissionWeaponData> getMissionWeaponDataList() {
		return missionWeaponDataList;
	}

	public void setMissionWeaponDataList(List<MissionWeaponData> missionWeaponDataList) {
		this.missionWeaponDataList = missionWeaponDataList;
	}

	public List<MissionCarData> getMissionCarDataList() {
		return missionCarDataList;
	}

	public void setMissionCarDataList(List<MissionCarData> missionCarDataList) {
		this.missionCarDataList = missionCarDataList;
	}

	public List<MissionEquipmentData> getMissionEquipmentDataList() {
		return missionEquipmentDataList;
	}

	public void setMissionEquipmentDataList(List<MissionEquipmentData> missionEquipmentDataList) {
		this.missionEquipmentDataList = missionEquipmentDataList;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public String getActiveIndexString() {
		return activeIndexString;
	}

	public void setActiveIndexString(String activeIndexString) {
		this.activeIndexString = activeIndexString;
	}

	public int getPrivilege() {
		return privilege;
	}
}