package com.code.ui.backings.securityaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.attach.Attachment;
import com.code.dal.orm.security.UserMenuActionData;
import com.code.dal.orm.securitymission.MissionCarData;
import com.code.dal.orm.securitymission.MissionEquipmentData;
import com.code.dal.orm.securitymission.MissionGuardData;
import com.code.dal.orm.securitymission.MissionWeaponData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.enums.EntityNameEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.ServiceCodesEnum;
import com.code.enums.SessionAttributesEnum;
import com.code.enums.UserMenuActionsEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.attach.AttachmentService;
import com.code.services.attach.AttachmentServiceCallBack;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.securityaction.SecurityGuardMissionService;
import com.code.services.log4j.Log4j;
import com.code.services.security.SecurityService;
import com.code.services.setup.SetupService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.securityaction.SecurityGuardMissionWorkFlow;
import com.code.ui.backings.base.WFBaseBacking;
import com.code.ui.util.EncryptionUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "securityGuardMissionRegisteration")
@ViewScoped
public class SecurityGuardMissionRegisteration extends WFBaseBacking implements Serializable {
	private MissionGuardData missionGuard;
	private List<MissionWeaponData> missionWeaponList;
	private List<MissionEquipmentData> missionEquipmentList;
	private List<MissionCarData> missionCarList;
	private String selectedDepartmentSourceName;
	private List<SetupDomain> orderSourcesList;

	private List<SetupDomain> missionTypes;
	private List<SetupDomain> weaponTypes;
	private List<SetupDomain> carModels;
	private List<SetupDomain> equipmentTypes;

	private String selectedDepartmentName;
	private Long selectedEmployeeForWeapons;
	private String selectedEmployeeForWeaponsName;
	private String selectedEmployeeForWeaponsRank;
	private String selectedEmployeeForWeaponsMilitaryNumber;

	private Long selectedEmployeeForCars;
	private String selectedEmployeeForCarsName;
	private String selectedEmployeeForCarsRank;
	private String selectedEmployeeForCarsMilitaryNumber;

	private Long selectedEmployeeForEquipment;
	private String selectedEmployeeForEquipmentName;
	private String selectedEmployeeForEquipmentRank;
	private String selectedEmployeeForEquipmentMilitaryNumber;

	private String employeeIdsList;
	private String warningEmployeeHasCarOrEquipment;

	private MissionWeaponData missionWeaponDataToDelete;
	private MissionCarData missionCarDataToDelete;
	private MissionEquipmentData missionEquipmentDataToDelete;

	int pageMode;

	private String fileArchivingParam;
	private String downloadFileParamId;
	private List<Attachment> attachmentList;
	private boolean openDownloadPopupFlag;
	private boolean openDownloadDialogueFlag;
	private String boolServerUploadPath;
	private String boolServerDownloadPath;
	private String uploadFlag;
	private boolean verbalOrderFlag;
	private boolean deletePrivilage = false;

	public SecurityGuardMissionRegisteration() {
		super();
		init();
		initialize();
	}

	protected void initialize() {
		try {
			UserMenuActionData deleteAttachmentAction = SecurityService.getAction(loginEmpData.getEmpId(), UserMenuActionsEnum.DELETE_ATTACHMENT_PERMISSION.getCode(), FlagsEnum.ALL.getCode());
			if (deleteAttachmentAction != null) {
				setDeletePrivilage(true);
			}
			orderSourcesList = SetupService.getDomains(ClassesEnum.ORDER_SOURCES.getCode());
			if (currentTask == null && getRequest().getAttribute("mode") == null) {
				pageMode = 1; // inserting mode

				missionWeaponDataToDelete = new MissionWeaponData();
				missionCarDataToDelete = new MissionCarData();
				missionEquipmentDataToDelete = new MissionEquipmentData();

				missionGuard = new MissionGuardData();
				missionGuard.setOrderDate(HijriDateService.getHijriSysDate());
				missionGuard.setMissionStartDate(HijriDateService.getHijriSysDate());
				missionWeaponList = new ArrayList<MissionWeaponData>();
				missionEquipmentList = new ArrayList<MissionEquipmentData>();
				missionCarList = new ArrayList<MissionCarData>();
				missionTypes = SetupService.getDomains(ClassesEnum.MISSION_TYPES.getCode());
				weaponTypes = SetupService.getDomains(ClassesEnum.WEAPON_TYPES.getCode());
				carModels = SetupService.getDomains(ClassesEnum.CAR_MODELS.getCode());
				equipmentTypes = SetupService.getDomains(ClassesEnum.EQUIPMENT_TYPES.getCode());
				selectedDepartmentSourceName = null;
				selectedDepartmentName = null;
				uploadFlag = getParameterizedMessage("label_yes");
			} else if (getRequest().getAttribute("mode") != null) {
				pageMode = 2; // view mode
				missionGuard = SecurityGuardMissionService.getMissionGuard(Long.parseLong((String) getRequest().getAttribute("mode")));
				missionWeaponList = SecurityGuardMissionService.getMissionWeaponData(missionGuard.getId(), FlagsEnum.ALL.getCode(), null, null, null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode());
				missionEquipmentList = SecurityGuardMissionService.getMissionEquipmentData(missionGuard.getId(), FlagsEnum.ALL.getCode(), null, null, null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode());
				missionCarList = SecurityGuardMissionService.getMissionCarData(missionGuard.getId(), FlagsEnum.ALL.getCode(), null, null, null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode());
				verbalOrderFlag = missionGuard.getOrderNumber() != null ? false : true;
			} else if (currentTask != null) {
				if (this.role == WFTaskRolesEnum.HISTORY.getCode() || this.role == WFTaskRolesEnum.NOTIFICATION.getCode()) {
					pageMode = 2; //
				} else {
					pageMode = 3;
				}
				missionGuard = SecurityGuardMissionService.getMissionGuardByInstanceId(currentTask.getInstanceId());
				missionWeaponList = SecurityGuardMissionService.getMissionWeaponData(missionGuard.getId(), FlagsEnum.ALL.getCode(), null, null, null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode());
				missionEquipmentList = SecurityGuardMissionService.getMissionEquipmentData(missionGuard.getId(), FlagsEnum.ALL.getCode(), null, null, null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode());
				missionCarList = SecurityGuardMissionService.getMissionCarData(missionGuard.getId(), FlagsEnum.ALL.getCode(), null, null, null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode());
				verbalOrderFlag = missionGuard.getOrderNumber() != null ? false : true;
			}
			boolServerUploadPath = InfoSysConfigurationService.getBoolServerUploadPath();
			boolServerDownloadPath = InfoSysConfigurationService.getBoolServerDownloadPath();
			employeeIdsList = "";
			warningEmployeeHasCarOrEquipment = "";
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SecurityGuardMissionRegisteration.class, e, "SecurityGuardMissionRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void addEmployee(int choice) {
		if (choice == 1) {
			/*
			 * for (MissionWeaponData weapon : missionWeaponList) { if (weapon.getEmployeeId().equals(selectedEmployeeForWeapons)) { this.setServerSideErrorMessages(getParameterizedMessage("error_alreadyChoosen")); return; } }
			 */

			MissionWeaponData missionWeaponData = new MissionWeaponData();
			missionWeaponData.setSecurityGuardMissionId(missionGuard.getId());
			missionWeaponData.setEmployeeId(selectedEmployeeForWeapons);
			missionWeaponData.setEmployeeRank(selectedEmployeeForWeaponsRank);
			missionWeaponData.setEmployeeName(selectedEmployeeForWeaponsName);
			missionWeaponData.setEmployeeMilitaryNumber(selectedEmployeeForWeaponsMilitaryNumber);

			employeeIdsList += selectedEmployeeForWeapons + ",";
			missionWeaponList.add(missionWeaponData);

		} else if (choice == 2) {
			for (MissionCarData car : missionCarList) {
				if (car.getEmployeeId().equals(selectedEmployeeForCars)) {
					this.setServerSideErrorMessages(getParameterizedMessage("error_alreadyChoosen"));
					return;
				}
			}

			MissionCarData missionCarData = new MissionCarData();
			missionCarData.setSecurityGuardMissionId(missionGuard.getId());
			missionCarData.setEmployeeId(selectedEmployeeForCars);
			missionCarData.setEmployeeRank(selectedEmployeeForCarsRank);
			missionCarData.setEmployeeName(selectedEmployeeForCarsName);
			missionCarData.setEmployeeMilitaryNumber(selectedEmployeeForCarsMilitaryNumber);

			missionCarList.add(missionCarData);
		} else if (choice == 3) {
			for (MissionEquipmentData equipment : missionEquipmentList) {
				if (equipment.getEmployeeId().equals(selectedEmployeeForEquipment)) {
					this.setServerSideErrorMessages(getParameterizedMessage("error_alreadyChoosen"));
					return;
				}
			}

			MissionEquipmentData missionEquipmentData = new MissionEquipmentData();
			missionEquipmentData.setSecurityGuardMissionId(missionGuard.getId());
			missionEquipmentData.setEmployeeId(selectedEmployeeForEquipment);
			missionEquipmentData.setEmployeeRank(selectedEmployeeForEquipmentRank);
			missionEquipmentData.setEmployeeName(selectedEmployeeForEquipmentName);
			missionEquipmentData.setEmployeeMilitaryNumber(selectedEmployeeForEquipmentMilitaryNumber);

			missionEquipmentList.add(missionEquipmentData);
		}
	}

	/**
	 * Select employee and his vehicles or equipments will be deleted
	 * 
	 * @param missionWeaponData
	 */
	public void confirmDeleteEmployee(MissionWeaponData missionWeaponData) {
		missionWeaponDataToDelete = missionWeaponData;
		for (MissionCarData missionCar : missionCarList) {
			if (missionCar.getEmployeeId().equals(missionWeaponDataToDelete.getEmployeeId())) {
				missionCarDataToDelete = missionCar;
			}
		}
		for (MissionEquipmentData missionEquipment : missionEquipmentList) {
			if (missionEquipment.getEmployeeId().equals(missionWeaponDataToDelete.getEmployeeId())) {
				missionEquipmentDataToDelete = missionEquipment;
			}
		}

	}

	public void cancelDeletion() {
		missionWeaponDataToDelete = new MissionWeaponData();
		missionCarDataToDelete = new MissionCarData();
		missionEquipmentDataToDelete = new MissionEquipmentData();
	}

	/**
	 * Delete employee or vehicle or equipment
	 * 
	 * @param missionCarData
	 * @param missionEquipmentData
	 */
	public void deleteFromList(MissionCarData missionCarData, MissionEquipmentData missionEquipmentData) {
		if (missionWeaponDataToDelete.getEmployeeId() != null) {
			missionWeaponList.remove(missionWeaponDataToDelete);
			missionWeaponDataToDelete = new MissionWeaponData();
			if (missionCarDataToDelete.getEmployeeId() != null) {
				missionCarList.remove(missionCarDataToDelete);
				missionCarDataToDelete = new MissionCarData();
			}
			if (missionEquipmentDataToDelete.getEmployeeId() != null) {
				missionEquipmentList.remove(missionEquipmentDataToDelete);
				missionEquipmentDataToDelete = new MissionEquipmentData();
			}
			employeeIdsList = "";
			for (MissionWeaponData missionWeapon : missionWeaponList) {
				employeeIdsList += missionWeapon.getEmployeeId() + ",";
			}
		} else if (missionCarData != null) {
			missionCarList.remove(missionCarData);
		} else if (missionEquipmentData != null) {
			missionEquipmentList.remove(missionEquipmentData);
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
	}

	public String saveNewMissionGuard() {
		try {
			for (SetupDomain source : orderSourcesList) {
				if (source.getId().equals(missionGuard.getOrderSourceDomainId())) {
					missionGuard.setOrderSourceDomainName(source.getDescription());
					break;
				}
			}
			for (SetupDomain type : missionTypes) {
				if (type.getId().equals(missionGuard.getMissionType())) {
					missionGuard.setMissionTypeDescription(type.getDescription());
					break;
				}
			}
			SecurityGuardMissionService.saveSecurityGuardMission(missionGuard, missionWeaponList, missionEquipmentList, missionCarList, loginEmpData, verbalOrderFlag);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}
		if (uploadFlag.equals(getParameterizedMessage("label_yes"))) {
			getRequest().setAttribute(SessionAttributesEnum.FILE_ARCHIVING_PARAM.getCode(), EntityNameEnum.MISSION_GUARD.getCode() + "_" + missionGuard.getId());
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_securityGuardMissionRegSuccess", missionGuard.getMissionNumber()));
		return NavigationEnum.INBOX.toString();
	}

	public String approve() {
		try {
			missionGuard.setStatus(FlagsEnum.ON.getCode());
			SecurityGuardMissionWorkFlow.doApproveMissionGuardDM(currentTask, missionGuard, loginEmpData, attachments);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	public String reject() {
		try {
			if (currentTask.getNotes() != null && currentTask.getNotes().trim().isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_rejectReasonMandatory"));
				return null;
			}
			missionGuard.setStatus(FlagsEnum.OFF.getCode());
			SecurityGuardMissionWorkFlow.doRejectMissionGuardDM(currentTask, missionGuard, loginEmpData, attachments);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	public String notified() {
		try {
			SecurityGuardMissionWorkFlow.doNotify(currentTask);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	public void getUploadParam(long missionGuardId) {
		try {
			String requestURL = getRequest().getRequestURL() + "";
			String url = requestURL.replace(getRequest().getServletPath(), "");
			String serviceURL = url + AttachmentServiceCallBack.SERVICE_URL;
			fileArchivingParam = EncryptionUtil.encryptSymmetrically("code=" + ServiceCodesEnum.INFO_SYS.getCode() + "&token=" + EntityNameEnum.MISSION_GUARD.getCode() + "_" + missionGuardId + "&serviceurl=" + serviceURL);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SecurityGuardMissionRegisteration.class, e, "SecurityGuardMissionRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void getDownloadParam(String attachmentId) {
		try {
			downloadFileParamId = EncryptionUtil.encryptSymmetrically("contentId=" + attachmentId);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			   Log4j.traceErrorException(SecurityGuardMissionRegisteration.class, e, "SecurityGuardMissionRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Delete Attachment
	 * 
	 * @param attachment
	 */
	public void deleteAttachment(Attachment attachment) {
		try {
			AttachmentService.deleteFileNetAttachment(attachment);
			attachmentList.remove(attachment);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SecurityGuardMissionRegisteration.class, e, "SecurityGuardMissionRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void getDownloadedAttachmentsIdList(long missionGuardId) throws BusinessException {
		try {
			attachmentList = AttachmentService.getAttachments(EntityNameEnum.MISSION_GUARD.getCode(), missionGuardId);
			openDownloadPopupFlag = false;
			openDownloadDialogueFlag = false;
			if (attachmentList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noAttachments"));
			} else if (attachmentList.size() == 1) {
				openDownloadPopupFlag = true;
				downloadFileParamId = EncryptionUtil.encryptSymmetrically("contentId=" + attachmentList.get(0).getAttachmentId());
			} else {
				openDownloadDialogueFlag = true;
			}
		} catch (Exception e) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	/**
	 * Print report
	 */
	public void print() {
		try {
			byte[] bytes = SecurityGuardMissionService.getSecurityGuardMissionDetailsBytes(missionGuard.getId(), loginEmpData.getFullName());
			super.print(bytes, "Security_Guard_Mission_Details");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SecurityGuardMissionRegisteration.class, e, "SecurityGuardMissionRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public List<SetupDomain> getMissionTypes() {
		return missionTypes;
	}

	public void setMissionTypes(List<SetupDomain> missionTypes) {
		this.missionTypes = missionTypes;
	}

	public String getSelectedDepartmentName() {
		return selectedDepartmentName;
	}

	public void setSelectedDepartmentName(String selectedDepartmentName) {
		this.selectedDepartmentName = selectedDepartmentName;
	}

	public Long getSelectedEmployeeForWeapons() {
		return selectedEmployeeForWeapons;
	}

	public void setSelectedEmployeeForWeapons(Long selectedEmployeeForWeapons) {
		this.selectedEmployeeForWeapons = selectedEmployeeForWeapons;
	}

	public Long getSelectedEmployeeForCars() {
		return selectedEmployeeForCars;
	}

	public void setSelectedEmployeeForCars(Long selectedEmployeeForCars) {
		this.selectedEmployeeForCars = selectedEmployeeForCars;
	}

	public Long getSelectedEmployeeForEquipment() {
		return selectedEmployeeForEquipment;
	}

	public void setSelectedEmployeeForEquipment(Long selectedEmployeeForEquipment) {
		this.selectedEmployeeForEquipment = selectedEmployeeForEquipment;
	}

	public List<MissionWeaponData> getMissionWeaponList() {
		return missionWeaponList;
	}

	public void setMissionWeaponList(List<MissionWeaponData> missionWeaponList) {
		this.missionWeaponList = missionWeaponList;
	}

	public List<MissionEquipmentData> getMissionEquipmentList() {
		return missionEquipmentList;
	}

	public void setMissionEquipmentList(List<MissionEquipmentData> missionEquipmentList) {
		this.missionEquipmentList = missionEquipmentList;
	}

	public List<MissionCarData> getMissionCarList() {
		return missionCarList;
	}

	public void setMissionCarList(List<MissionCarData> missionCarList) {
		this.missionCarList = missionCarList;
	}

	public String getSelectedEmployeeForWeaponsName() {
		return selectedEmployeeForWeaponsName;
	}

	public void setSelectedEmployeeForWeaponsName(String selectedEmployeeForWeaponsName) {
		this.selectedEmployeeForWeaponsName = selectedEmployeeForWeaponsName;
	}

	public String getSelectedEmployeeForWeaponsRank() {
		return selectedEmployeeForWeaponsRank;
	}

	public void setSelectedEmployeeForWeaponsRank(String selectedEmployeeForWeaponsRank) {
		this.selectedEmployeeForWeaponsRank = selectedEmployeeForWeaponsRank;
	}

	public String getSelectedEmployeeForWeaponsMilitaryNumber() {
		return selectedEmployeeForWeaponsMilitaryNumber;
	}

	public void setSelectedEmployeeForWeaponsMilitaryNumber(String selectedEmployeeForWeaponsMilitaryNumber) {
		this.selectedEmployeeForWeaponsMilitaryNumber = selectedEmployeeForWeaponsMilitaryNumber;
	}

	public List<SetupDomain> getWeaponTypes() {
		return weaponTypes;
	}

	public void setWeaponTypes(List<SetupDomain> weaponTypes) {
		this.weaponTypes = weaponTypes;
	}

	public String getSelectedEmployeeForCarsName() {
		return selectedEmployeeForCarsName;
	}

	public void setSelectedEmployeeForCarsName(String selectedEmployeeForCarsName) {
		this.selectedEmployeeForCarsName = selectedEmployeeForCarsName;
	}

	public String getSelectedEmployeeForCarsRank() {
		return selectedEmployeeForCarsRank;
	}

	public void setSelectedEmployeeForCarsRank(String selectedEmployeeForCarsRank) {
		this.selectedEmployeeForCarsRank = selectedEmployeeForCarsRank;
	}

	public String getSelectedEmployeeForCarsMilitaryNumber() {
		return selectedEmployeeForCarsMilitaryNumber;
	}

	public void setSelectedEmployeeForCarsMilitaryNumber(String selectedEmployeeForCarsMilitaryNumber) {
		this.selectedEmployeeForCarsMilitaryNumber = selectedEmployeeForCarsMilitaryNumber;
	}

	public List<SetupDomain> getCarModels() {
		return carModels;
	}

	public void setCarModels(List<SetupDomain> carModels) {
		this.carModels = carModels;
	}

	public String getSelectedEmployeeForEquipmentName() {
		return selectedEmployeeForEquipmentName;
	}

	public void setSelectedEmployeeForEquipmentName(String selectedEmployeeForEquipmentName) {
		this.selectedEmployeeForEquipmentName = selectedEmployeeForEquipmentName;
	}

	public String getSelectedEmployeeForEquipmentRank() {
		return selectedEmployeeForEquipmentRank;
	}

	public void setSelectedEmployeeForEquipmentRank(String selectedEmployeeForEquipmentRank) {
		this.selectedEmployeeForEquipmentRank = selectedEmployeeForEquipmentRank;
	}

	public String getSelectedEmployeeForEquipmentMilitaryNumber() {
		return selectedEmployeeForEquipmentMilitaryNumber;
	}

	public void setSelectedEmployeeForEquipmentMilitaryNumber(String selectedEmployeeForEquipmentMilitaryNumber) {
		this.selectedEmployeeForEquipmentMilitaryNumber = selectedEmployeeForEquipmentMilitaryNumber;
	}

	public String getEmployeeIdsList() {
		return employeeIdsList;
	}

	public void setEmployeeIdsList(String employeeIdsList) {
		this.employeeIdsList = employeeIdsList;
	}

	public String getWarningEmployeeHasCarOrEquipment() {
		return warningEmployeeHasCarOrEquipment;
	}

	public void setWarningEmployeeHasCarOrEquipment(String warningEmployeeHasCarOrEquipment) {
		this.warningEmployeeHasCarOrEquipment = warningEmployeeHasCarOrEquipment;
	}

	public MissionWeaponData getMissionWeaponDataToDelete() {
		return missionWeaponDataToDelete;
	}

	public void setMissionWeaponDataToDelete(MissionWeaponData missionWeaponDataToDelete) {
		this.missionWeaponDataToDelete = missionWeaponDataToDelete;
	}

	public MissionCarData getMissionCarDataToDelete() {
		return missionCarDataToDelete;
	}

	public void setMissionCarDataToDelete(MissionCarData missionCarDataToDelete) {
		this.missionCarDataToDelete = missionCarDataToDelete;
	}

	public MissionEquipmentData getMissionEquipmentDataToDelete() {
		return missionEquipmentDataToDelete;
	}

	public void setMissionEquipmentDataToDelete(MissionEquipmentData missionEquipmentDataToDelete) {
		this.missionEquipmentDataToDelete = missionEquipmentDataToDelete;
	}

	public List<SetupDomain> getEquipmentTypes() {
		return equipmentTypes;
	}

	public void setEquipmentTypes(List<SetupDomain> equipmentTypes) {
		this.equipmentTypes = equipmentTypes;
	}

	public String getSelectedDepartmentSourceName() {
		return selectedDepartmentSourceName;
	}

	public void setSelectedDepartmentSourceName(String selectedDepartmentSourceName) {
		this.selectedDepartmentSourceName = selectedDepartmentSourceName;
	}

	public int getPageMode() {
		return pageMode;
	}

	public void setPageMode(int pageMode) {
		this.pageMode = pageMode;
	}

	public MissionGuardData getMissionGuard() {
		return missionGuard;
	}

	public void setMissionGuard(MissionGuardData missionGuard) {
		this.missionGuard = missionGuard;
	}

	public String getNotificationRole() {
		return WFTaskRolesEnum.NOTIFICATION.getCode();
	}

	public String getHistoricalRole() {
		return WFTaskRolesEnum.HISTORY.getCode();
	}

	public String getDirectorateMilitaryPoliceManagerRole() {
		return WFTaskRolesEnum.GENERAL_MILITARY_POLICE_MANAGER.getCode();
	}

	public String getRegionMilitaryPoliceManagerRole() {
		return WFTaskRolesEnum.REGION_MILITARY_POLICE_MANAGER.getCode();
	}

	public String getRequesterRole() {
		return WFTaskRolesEnum.REQUESTER.getCode();
	}

	public String getFileArchivingParam() {
		return fileArchivingParam;
	}

	public void setFileArchivingParam(String fileArchivingParam) {
		this.fileArchivingParam = fileArchivingParam;
	}

	public String getDownloadFileParamId() {
		return downloadFileParamId;
	}

	public void setDownloadFileParamId(String downloadFileParamId) {
		this.downloadFileParamId = downloadFileParamId;
	}

	public List<Attachment> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<Attachment> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public boolean isOpenDownloadPopupFlag() {
		return openDownloadPopupFlag;
	}

	public void setOpenDownloadPopupFlag(boolean openDownloadPopupFlag) {
		this.openDownloadPopupFlag = openDownloadPopupFlag;
	}

	public boolean isOpenDownloadDialogueFlag() {
		return openDownloadDialogueFlag;
	}

	public void setOpenDownloadDialogueFlag(boolean openDownloadDialogueFlag) {
		this.openDownloadDialogueFlag = openDownloadDialogueFlag;
	}

	public String getBoolServerUploadPath() {
		return boolServerUploadPath;
	}

	public void setBoolServerUploadPath(String boolServerUploadPath) {
		this.boolServerUploadPath = boolServerUploadPath;
	}

	public String getBoolServerDownloadPath() {
		return boolServerDownloadPath;
	}

	public void setBoolServerDownloadPath(String boolServerDownloadPath) {
		this.boolServerDownloadPath = boolServerDownloadPath;
	}

	public String getUploadFlag() {
		return uploadFlag;
	}

	public void setUploadFlag(String uploadFlag) {
		this.uploadFlag = uploadFlag;
	}

	public List<SetupDomain> getOrderSourcesList() {
		return orderSourcesList;
	}

	public void setOrderSourcesList(List<SetupDomain> orderSourcesList) {
		this.orderSourcesList = orderSourcesList;
	}

	public boolean isVerbalOrderFlag() {
		return verbalOrderFlag;
	}

	public void setVerbalOrderFlag(boolean verbalOrderFlag) {
		this.verbalOrderFlag = verbalOrderFlag;
	}

	public boolean isDeletePrivilage() {
		return deletePrivilage;
	}

	public void setDeletePrivilage(boolean deletePrivilage) {
		this.deletePrivilage = deletePrivilage;
	}
}
