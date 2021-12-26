package com.code.ui.backings.worklist;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.fishing.FishingBoatOwnerData;
import com.code.dal.orm.fishing.FishingNavigationData;
import com.code.dal.orm.fishing.FishingNavigationDelegationsData;
import com.code.dal.orm.fishing.FishingNavigationFollowerData;
import com.code.dal.orm.fishing.FishingNavigationSailorData;
import com.code.dal.orm.setup.DepartmentData;
import com.code.dal.orm.surveillance.SurveillanceEmpNonEmpData;
import com.code.dal.orm.workflow.WFTask;
import com.code.enums.FishingDelegatorsTypeEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.enums.WFTaskUrlEnum;
import com.code.exceptions.BusinessException;
import com.code.services.fishing.FishingService;
import com.code.services.infosys.surveillance.SurveillanceOrdersService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.BaseWorkFlow;
import com.code.services.workflow.surveillance.SurveillanceWorkFlow;
import com.code.ui.backings.base.WFBaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "fishingNotification")
@ViewScoped
public class FishingNotification extends WFBaseBacking implements Serializable {
	String navigationNumber, movementType;
	private FishingNavigationData fishingNavigationData;
	private List<FishingBoatOwnerData> fishingBoatOwnerDataList;
	private List<FishingNavigationSailorData> fishingNavigationSailorDataList;
	private List<FishingNavigationSailorData> fishingNavigationCaptainDataList;
	private List<FishingNavigationDelegationsData> fishingNavigationCaptainDelegateDataList;
	private List<FishingNavigationDelegationsData> fishingNavigationSailorDelegateDataList;
	private List<FishingNavigationFollowerData> fishingNavigationFollowerDataList;
	private boolean sendNotification = false;

	/**
	 * Constructor
	 * 
	 * @throws BusinessException
	 */
	public FishingNotification() throws Exception {
		super();
		init();
		fillNotificationData(currentTask);
	}

	/**
	 * Fill fishing notification Data given task
	 * 
	 * @param wfTask
	 * @throws BusinessException
	 */
	public void fillNotificationData(WFTask wfTask) throws BusinessException {
		try {
			fishingNavigationCaptainDataList = new ArrayList<FishingNavigationSailorData>();
			BaseWorkFlow.doNotified(wfTask);
			String[] msgTokens = currentTask.getNotes().split(";");
			this.navigationNumber = msgTokens[0];
			this.movementType = msgTokens[1];
			String hijriSailDateString = msgTokens[2];
			fishingNavigationData = FishingService.getNavigationByNumber(this.navigationNumber);
			if (fishingNavigationData != null) {
				fishingBoatOwnerDataList = FishingService.getFishingBoatOwnersByBoatId(fishingNavigationData.getBoatId());
				fishingNavigationSailorDataList = FishingService.getNavigationSailorsByNavigationId(fishingNavigationData.getId());
				fishingNavigationCaptainDelegateDataList = FishingService.getNavigationDelegatesByBoatIdAndDelegateType(fishingNavigationData.getBoatId(), FishingDelegatorsTypeEnum.CAPTAIN.getCode());
				fishingNavigationSailorDelegateDataList = FishingService.getNavigationDelegatesByBoatIdAndDelegateType(fishingNavigationData.getBoatId(), FishingDelegatorsTypeEnum.SAILOR.getCode());
				FishingNavigationSailorData captain = new FishingNavigationSailorData();
				captain.setSailorIdentity(fishingNavigationData.getCaptainIdentity());
				captain.setSailorName(fishingNavigationData.getCaptainName());
				captain.setNationality(fishingNavigationData.getCaptainNationality());
				fishingNavigationCaptainDataList.add(0, captain);
				fishingNavigationFollowerDataList = FishingService.getNavigationFollowersByNavigationId(fishingNavigationData.getId());
			} else {
				throw new BusinessException(getParameterizedMessage("error_general"));
			}

			for (FishingBoatOwnerData boatOwners : fishingBoatOwnerDataList) {
				SurveillanceEmpNonEmpData surveillanceEmpNonEmpData = SurveillanceOrdersService.getSurveillanceEmployeeData(boatOwners.getOwnerIdentity(), HijriDateService.getHijriDate(hijriSailDateString));
				if (surveillanceEmpNonEmpData != null) {
					boatOwners.setObserved(true);
				}
			}

			for (FishingNavigationSailorData sailors : fishingNavigationSailorDataList) {
				SurveillanceEmpNonEmpData surveillanceEmpNonEmpData = SurveillanceOrdersService.getSurveillanceEmployeeData(sailors.getSailorIdentity(), HijriDateService.getHijriDate(hijriSailDateString));
				if (surveillanceEmpNonEmpData != null) {
					sailors.setObserved(true);
				}
			}

			for (FishingNavigationFollowerData follower : fishingNavigationFollowerDataList) {
				SurveillanceEmpNonEmpData surveillanceEmpNonEmpData = SurveillanceOrdersService.getSurveillanceEmployeeData(follower.getFollowerIdentity(), HijriDateService.getHijriDate(hijriSailDateString));
				if (surveillanceEmpNonEmpData != null) {
					follower.setObserved(true);
				}
			}
			List<WFTask> wfTasks = BaseWorkFlow.getWFInstanceTasksByRole(currentTask.getInstanceId(), WFTaskRolesEnum.NOTIFICATION_WITHOUT_RESEND.getCode());
			DepartmentData employeeDep = DepartmentService.getDepartment(currentEmployee.getActualDepartmentId());
			if (!wfTasks.isEmpty() || wfTasks.size() != 0 || this.fishingNavigationData.getRegionId() == null || this.fishingNavigationData.getRegionId().equals(employeeDep.getRegionId())) {
				sendNotification = false;
			} else {
				sendNotification = true;
			}
		} catch (BusinessException e) {
			throw new BusinessException(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(FishingNotification.class, e, "FishingNotification");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Send Notification To Movement Region
	 */
	public void sendNotification() {
		try {
			SurveillanceWorkFlow.sendMovementNotifications(currentTask, this.fishingNavigationData.getRegionId(), loginEmpData);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return;
		}
		sendNotification = false;
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
	}

	/**
	 * Get Next Fishing Notification
	 */
	public void getNextNotification() {
		try {
			WFTask nextTask = BaseWorkFlow.getWfNextNotification(currentTask.getAssigneeId(), true, WFTaskUrlEnum.FIHSING_AND_EXCURSION_NOTIFICATION.getCode());
			if (nextTask == null) {
				super.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
			} else {
				fillNotificationData(nextTask);
			}
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(FishingNotification.class, e, "FishingNotification");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Print Report Using Given Fields
	 */
	public void printReport() {
		try {
			Date dateNow = new Date();

			DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String todayDateStr = df.format(new Date());
			String timeNow = todayDateStr.substring(11);

			String DATE_FORMAT_LTR = "yyyy/MM/mm";
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_LTR);
			String hijriDate = sdf.format(HijriDateService.gregToHijriDate(new Date()));

			Long exceededTime;
			Long exceededMinutes;
			Long exceededHours;
			String exceededTimestampStr = "";
			if (fishingNavigationData.getActualReturnDate() == null) {
				exceededTime = dateNow.getTime() - fishingNavigationData.getReturnDate().getTime();
			} else {
				exceededTime = fishingNavigationData.getActualReturnDate().getTime() - fishingNavigationData.getReturnDate().getTime();
			}
			exceededMinutes = exceededTime / (60 * 1000) % 60;
			exceededHours = exceededTime / (60 * 60 * 1000) % 24;

			if (exceededTime > 0)
				exceededTimestampStr = exceededHours.toString() + ":" + exceededMinutes.toString();

			byte[] bytes = FishingService.getFishingNavigationDetails(this.navigationNumber, dateNow, requester.getFullName(), hijriDate, timeNow, exceededTimestampStr);
			String reportName = "FishingDetails";
			super.print(bytes, reportName);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(FishingNotification.class, e, "FishingNotification");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Get Day String given date
	 * 
	 * @param date
	 * @return
	 */
	public String getDayString(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String day;
		int dayNumber = cal.get(Calendar.DAY_OF_WEEK);
		if (dayNumber == Calendar.SATURDAY) {
			day = getParameterizedMessage("label_saturday") + " ";
		} else if (dayNumber == Calendar.SUNDAY) {
			day = getParameterizedMessage("label_sunday") + " ";
		} else if (dayNumber == Calendar.MONDAY) {
			day = getParameterizedMessage("label_monday") + " ";
		} else if (dayNumber == Calendar.TUESDAY) {
			day = getParameterizedMessage("label_tuesday") + " ";
		} else if (dayNumber == Calendar.WEDNESDAY) {
			day = getParameterizedMessage("label_wednesday") + " ";
		} else if (dayNumber == Calendar.THURSDAY) {
			day = getParameterizedMessage("label_thursday") + " ";
		} else {
			day = getParameterizedMessage("label_friday") + " ";
		}
		return day;
	}

	public String getTimeString(Date date) {
		return new SimpleDateFormat("HH:mm:ss").format(date);
	}

	public String getMovementType() {
		return movementType;
	}

	public void setMovementType(String movementType) {
		this.movementType = movementType;
	}

	public FishingNavigationData getFishingNavigationData() {
		return fishingNavigationData;
	}

	public void setFishingNavigationData(FishingNavigationData fishingNavigationData) {
		this.fishingNavigationData = fishingNavigationData;
	}

	public List<FishingBoatOwnerData> getFishingBoatOwnerDataList() {
		return fishingBoatOwnerDataList;
	}

	public void setFishingBoatOwnerDataList(List<FishingBoatOwnerData> fishingBoatOwnerDataList) {
		this.fishingBoatOwnerDataList = fishingBoatOwnerDataList;
	}

	public List<FishingNavigationSailorData> getFishingNavigationSailorDataList() {
		return fishingNavigationSailorDataList;
	}

	public void setFishingNavigationSailorDataList(List<FishingNavigationSailorData> fishingNavigationSailorDataList) {
		this.fishingNavigationSailorDataList = fishingNavigationSailorDataList;
	}

	public List<FishingNavigationSailorData> getFishingNavigationCaptainDataList() {
		return fishingNavigationCaptainDataList;
	}

	public void setFishingNavigationCaptainDataList(List<FishingNavigationSailorData> fishingNavigationCaptainDataList) {
		this.fishingNavigationCaptainDataList = fishingNavigationCaptainDataList;
	}

	public List<FishingNavigationFollowerData> getFishingNavigationFollowerDataList() {
		return fishingNavigationFollowerDataList;
	}

	public void setFishingNavigationFollowerDataList(List<FishingNavigationFollowerData> fishingNavigationFollowerDataList) {
		this.fishingNavigationFollowerDataList = fishingNavigationFollowerDataList;
	}

	public List<FishingNavigationDelegationsData> getFishingNavigationCaptainDelegateDataList() {
		return fishingNavigationCaptainDelegateDataList;
	}

	public void setFishingNavigationCaptainDelegateDataList(List<FishingNavigationDelegationsData> fishingNavigationCaptainDelegateDataList) {
		this.fishingNavigationCaptainDelegateDataList = fishingNavigationCaptainDelegateDataList;
	}

	public List<FishingNavigationDelegationsData> getFishingNavigationSailorDelegateDataList() {
		return fishingNavigationSailorDelegateDataList;
	}

	public void setFishingNavigationSailorDelegateDataList(List<FishingNavigationDelegationsData> fishingNavigationSailorDelegateDataList) {
		this.fishingNavigationSailorDelegateDataList = fishingNavigationSailorDelegateDataList;
	}

	public boolean getSendNotificationFlag() {
		return !this.role.equals(WFTaskRolesEnum.NOTIFICATION_WITHOUT_RESEND.getCode()) && sendNotification;
	}

	public String confirmationMessage() {
		return this.getParameterizedMessage("q_sendFishingNotification", new Object[] { fishingNavigationData.getRegionName() });
	}
}