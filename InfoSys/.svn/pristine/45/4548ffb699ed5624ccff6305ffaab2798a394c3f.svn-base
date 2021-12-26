package com.code.ui.backings.labcheck;

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletResponse;

import com.code.dal.orm.attach.Attachment;
import com.code.dal.orm.labcheck.LabCheckReport;
import com.code.dal.orm.labcheck.LabCheckReportDepartmentAction;
import com.code.dal.orm.labcheck.LabCheckReportDepartmentData;
import com.code.dal.orm.labcheck.LabCheckReportDepartmentEmployeeData;
import com.code.dal.orm.labcheck.LabCheckReportEmployeeData;
import com.code.dal.orm.setup.DepartmentData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.EntityNameEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.ServiceCodesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.attach.AttachmentService;
import com.code.services.attach.AttachmentServiceCallBack;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.labcheck.MissionReportService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.SetupService;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;
import com.code.ui.util.EncryptionUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "missionReportRegisteration")
@ViewScoped
public class MissionReportRegisteration extends BaseBacking implements Serializable {
	private List<DepartmentData> regionList;
	private List<SetupDomain> missionTypes;
	private List<SetupDomain> healthCareCenters;
	private LabCheckReport labCheckReport;
	private LabCheckReportEmployeeData selectedLabCheckReportEmployeeData;;
	private List<LabCheckReportEmployeeData> labCheckReportEmployeeDataList;
	private LabCheckReportDepartmentData newModifylabCheckReportDepartmentData;
	private List<LabCheckReportDepartmentData> labCheckReportDepartmentDataList;
	private LabCheckReportDepartmentData selectedLabCheckReportDepartmentData;
	private LabCheckReportDepartmentAction newModifyLabCheckReportDepartmentAction;
	private List<LabCheckReportDepartmentAction> labCheckReportDepartmentActionList;
	private LabCheckReportDepartmentEmployeeData newModifyLabCheckReportDepartmentEmployeeData;
	private List<LabCheckReportDepartmentEmployeeData> labCheckReportDepartmentEmployeeDataList;

	private boolean addNewSectorCollapseFlag;
	private boolean addNewActionCollapseFlag;
	private boolean addNewBeneficiaryCollapseFlag;
	private boolean addNewEmployeeCollapseFlag;
	private boolean editSectorFlag;

	private String fileArchivingParam;
	private String downloadFileParamId;
	private List<Attachment> attachmentList;
	private boolean openDownloadPopupFlag;
	private boolean openDownloadDialogueFlag;
	private String boolServerUploadPath;
	private String boolServerDownloadPath;

	public MissionReportRegisteration() {
		initialize();
	}

	protected void initialize() {
		try {
			if (getRequest().getAttribute("mode") == null) {
				labCheckReport = new LabCheckReport();
				labCheckReport.setStartDate(HijriDateService.getHijriSysDate());
				labCheckReport.setEndDate(HijriDateService.getHijriSysDate());
				labCheckReport.setCheckStartDate(HijriDateService.getHijriSysDate());
				labCheckReport.setCheckEndDate(HijriDateService.getHijriSysDate());
				labCheckReport.setPeriod(0);
			} else {
				labCheckReport = MissionReportService.getLabCheck(Long.parseLong((String) getRequest().getAttribute("mode")));
				labCheckReportEmployeeDataList = MissionReportService.getLabCheckReportEmployeeData(labCheckReport.getId(), FlagsEnum.ALL.getCode());
				labCheckReportDepartmentDataList = MissionReportService.getLabCheckReportDepartmentData(labCheckReport.getId(), FlagsEnum.ALL.getCode());
				selectedLabCheckReportEmployeeData = new LabCheckReportEmployeeData();
				newModifylabCheckReportDepartmentData = new LabCheckReportDepartmentData();
			}

			regionList = DepartmentService.getDepartmentsBydepartmentType(DepartmentTypeEnum.REGION.getCode());
			missionTypes = SetupService.getDomains(ClassesEnum.LAB_CHECK_MISSIONS.getCode());
			healthCareCenters = SetupService.getDomains(ClassesEnum.HEALTH_CARE_CANTERS.getCode());
			boolServerUploadPath = InfoSysConfigurationService.getBoolServerUploadPath();
			boolServerDownloadPath = InfoSysConfigurationService.getBoolServerDownloadPath();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(MissionReportRegisteration.class, e, "MissionReportRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	// always inserting new only
	public void saveEmployee() {
		try {
			selectedLabCheckReportEmployeeData.setLabCheckReportId(labCheckReport.getId());
			MissionReportService.saveMissionReportEmployee(selectedLabCheckReportEmployeeData, loginEmpData);
			labCheckReportEmployeeDataList.add(selectedLabCheckReportEmployeeData);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
			selectedLabCheckReportEmployeeData = new LabCheckReportEmployeeData();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(MissionReportRegisteration.class, e, "MissionReportRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void deleteMissionEmployee(LabCheckReportEmployeeData labCheckReportEmployeeData) {
		try {
			MissionReportService.deleteMissionReportEmployee(labCheckReportEmployeeData, loginEmpData);
			labCheckReportEmployeeDataList.remove(labCheckReportEmployeeData);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(MissionReportRegisteration.class, e, "MissionReportRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void saveSector() {
		try {
			if (newModifylabCheckReportDepartmentData.getId() == null) {
				newModifylabCheckReportDepartmentData.setLabCheckReportId(labCheckReport.getId());
				MissionReportService.saveMissionReportDepartment(newModifylabCheckReportDepartmentData, loginEmpData);
				this.labCheckReportDepartmentDataList.add(newModifylabCheckReportDepartmentData);
			} else {
				MissionReportService.updateMissionReportDepartment(newModifylabCheckReportDepartmentData, loginEmpData);
			}

			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
			newModifylabCheckReportDepartmentData = new LabCheckReportDepartmentData();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(MissionReportRegisteration.class, e, "MissionReportRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void editSector(LabCheckReportDepartmentData labCheckReportDepartmentData) {
		newModifylabCheckReportDepartmentData = labCheckReportDepartmentData;
		addNewSectorCollapseFlag = true;
	}

	public void selectSector(LabCheckReportDepartmentData labCheckReportDepartmentData) {
		try {
			selectedLabCheckReportDepartmentData = labCheckReportDepartmentData;
			labCheckReportDepartmentActionList = MissionReportService.getLabCheckReportDepartmentAction(labCheckReportDepartmentData.getId());
			labCheckReportDepartmentEmployeeDataList = MissionReportService.getLabCheckReportDepartmentEmployee(labCheckReportDepartmentData.getId());
			newModifyLabCheckReportDepartmentAction = new LabCheckReportDepartmentAction();
			newModifyLabCheckReportDepartmentEmployeeData = new LabCheckReportDepartmentEmployeeData();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(MissionReportRegisteration.class, e, "MissionReportRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void deleteSector(LabCheckReportDepartmentData labCheckReportDepartmentData) {
		try {
			MissionReportService.deleteMissionReportDepartment(labCheckReportDepartmentData, loginEmpData);
			labCheckReportDepartmentDataList.remove(labCheckReportDepartmentData);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(MissionReportRegisteration.class, e, "MissionReportRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void saveDepartmentActivity() {
		try {
			if (newModifyLabCheckReportDepartmentAction.getId() == null) {
				newModifyLabCheckReportDepartmentAction.setLabCheckReportDepartmentId(selectedLabCheckReportDepartmentData.getId());
				MissionReportService.saveLabCheckReportDepartmentAction(newModifyLabCheckReportDepartmentAction, loginEmpData);
				labCheckReportDepartmentActionList.add(newModifyLabCheckReportDepartmentAction);
			} else {
				MissionReportService.updateLabCheckReportDepartmentAction(newModifyLabCheckReportDepartmentAction, loginEmpData);
			}
			newModifyLabCheckReportDepartmentAction = new LabCheckReportDepartmentAction();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(MissionReportRegisteration.class, e, "MissionReportRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void editDepartmentActivity(LabCheckReportDepartmentAction labCheckReportDepartmentAction) {
		newModifyLabCheckReportDepartmentAction = labCheckReportDepartmentAction;
		addNewActionCollapseFlag = true;
	}

	public void deleteDepartmentActivity(LabCheckReportDepartmentAction labCheckReportDepartmentAction) {
		try {
			MissionReportService.deleteLabCheckReportDepartmentAction(labCheckReportDepartmentAction, loginEmpData);
			labCheckReportDepartmentActionList.remove(labCheckReportDepartmentAction);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(MissionReportRegisteration.class, e, "MissionReportRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void saveDepartmentEmployee() {
		try {

			if (newModifyLabCheckReportDepartmentEmployeeData.getId() == null) {
				for (LabCheckReportDepartmentEmployeeData employeeData : labCheckReportDepartmentEmployeeDataList) {
					if (employeeData.getEmployeeId().equals(newModifyLabCheckReportDepartmentEmployeeData.getEmployeeId())) {
						this.setServerSideErrorMessages(getParameterizedMessage("error_alreadyChoosen"));
						return;
					}
				}

				for (SetupDomain healthCareCenter : healthCareCenters) {
					if (newModifyLabCheckReportDepartmentEmployeeData.getDomainHospitalId().equals(healthCareCenter.getId())) {
						newModifyLabCheckReportDepartmentEmployeeData.setDomainHospitalDescription(healthCareCenter.getDescription());
					}
				}

				newModifyLabCheckReportDepartmentEmployeeData.setLabCheckReportDepartmentId(selectedLabCheckReportDepartmentData.getId());
				MissionReportService.saveLabCheckReportDepartmentEmployee(newModifyLabCheckReportDepartmentEmployeeData, loginEmpData);
				labCheckReportDepartmentEmployeeDataList.add(newModifyLabCheckReportDepartmentEmployeeData);
			} else {
				MissionReportService.updateLabCheckReportDepartmentEmployee(newModifyLabCheckReportDepartmentEmployeeData, loginEmpData);
			}
			newModifyLabCheckReportDepartmentEmployeeData = new LabCheckReportDepartmentEmployeeData();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(MissionReportRegisteration.class, e, "MissionReportRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void editDepartmentEmployee(LabCheckReportDepartmentEmployeeData labCheckReportDepartmentEmployeeData) {
		newModifyLabCheckReportDepartmentEmployeeData = labCheckReportDepartmentEmployeeData;
		addNewBeneficiaryCollapseFlag = true;
	}

	public void deleteDepartmentEmployee(LabCheckReportDepartmentEmployeeData labCheckReportDepartmentEmployeeData) {
		try {
			MissionReportService.deleteLabCheckReportDepartmentEmployee(labCheckReportDepartmentEmployeeData, loginEmpData);
			labCheckReportDepartmentEmployeeDataList.remove(labCheckReportDepartmentEmployeeData);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(MissionReportRegisteration.class, e, "MissionReportRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void saveMissionReport() {
		try {
			labCheckReport.setPeriod(HijriDateService.hijriDateDiff(labCheckReport.getStartDate(), labCheckReport.getEndDate()));
			if (labCheckReport.getId() != null) {
				MissionReportService.updateMissionReportData(labCheckReport, loginEmpData);
			} else {
				MissionReportService.saveMissionReportData(labCheckReport, loginEmpData);
				labCheckReportEmployeeDataList = new ArrayList<LabCheckReportEmployeeData>();
				labCheckReportDepartmentDataList = new ArrayList<LabCheckReportDepartmentData>();
				selectedLabCheckReportEmployeeData = new LabCheckReportEmployeeData();
				newModifylabCheckReportDepartmentData = new LabCheckReportDepartmentData();
			}
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(MissionReportRegisteration.class, e, "MissionReportRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void cancelEditingDetails(String cancelledObject) {
		if (cancelledObject.equals("sector")) {
			newModifylabCheckReportDepartmentData = new LabCheckReportDepartmentData();
		} else if (cancelledObject.equals("action")) {
			newModifyLabCheckReportDepartmentAction = new LabCheckReportDepartmentAction();
		} else if (cancelledObject.equals("beneficiaryEmployee")) {
			newModifyLabCheckReportDepartmentEmployeeData = new LabCheckReportDepartmentEmployeeData();
		}
	}

	/**
	 * Constitute upload parameter which contains bool server registered serviceCode and the token (EntityName_EntityId)
	 * 
	 * @param labCheckReportId
	 */
	public void getUploadParam(long labCheckReportId) {
		try {
			String requestURL = getRequest().getRequestURL() + "";
			String url = requestURL.replace(getRequest().getServletPath(), "");
			String serviceURL = url + AttachmentServiceCallBack.SERVICE_URL;
			fileArchivingParam = EncryptionUtil.encryptSymmetrically("code=" + ServiceCodesEnum.INFO_SYS.getCode() + "&token=" + EntityNameEnum.LAB_CHECK_REPORT.getCode() + "_" + labCheckReportId + "&serviceurl=" + serviceURL);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(MissionReportRegisteration.class, e, "MissionReportRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Constitute Download parameter which contains contentId (attachmentId)
	 * 
	 * @param attachmentId
	 */
	public void getDownloadParam(String attachmentId) {
		try {
			downloadFileParamId = EncryptionUtil.encryptSymmetrically("contentId=" + attachmentId);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			   Log4j.traceErrorException(MissionReportRegisteration.class, e, "MissionReportRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Delete Attachment
	 * 
	 * @param attachment
	 */
	public void getDeleteParam(Attachment attachment) {
		try {
			downloadFileParamId = EncryptionUtil.encryptSymmetrically("contentId=" + attachment.getAttachmentId());
			URL url = new URL(InfoSysConfigurationService.getBoolServerDeletePath() + downloadFileParamId);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			if (responseCode == HttpServletResponse.SC_OK) {
				AttachmentService.deleteAttachment(attachment);
				attachmentList.remove(attachment);
			} else {
				throw new BusinessException("error_general");
			}

		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			   Log4j.traceErrorException(MissionReportRegisteration.class, e, "MissionReportRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Get attachment list related to the entity id passed to the method then check if there's no records then show message indicating no daya else if it's only one record , open the download popup directly , otherwise open a dialogue shows file names and make the user select which one to download then open the download popup with the choosen contentId (attachmentId)
	 * 
	 * @param labCheckReportId
	 * @throws BusinessException
	 */
	public void getDownloadedAttachmentsIdList(long labCheckReportId) throws BusinessException {
		try {
			attachmentList = AttachmentService.getAttachments(EntityNameEnum.LAB_CHECK_REPORT.getCode(), labCheckReportId);
			openDownloadPopupFlag = false;
			openDownloadDialogueFlag = false;
			if (attachmentList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noAttachments"));
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
			byte[] bytes = MissionReportService.getMissionReportDetailsBytes(labCheckReport.getId(), loginEmpData.getFullName());
			super.print(bytes, "Mission_Report");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(MissionReportRegisteration.class, e, "MissionReportRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public LabCheckReport getLabCheckReport() {
		return labCheckReport;
	}

	public void setLabCheckReport(LabCheckReport labCheckReport) {
		this.labCheckReport = labCheckReport;
	}

	public List<DepartmentData> getRegionList() {
		return regionList;
	}

	public void setRegionList(List<DepartmentData> regionList) {
		this.regionList = regionList;
	}

	public List<SetupDomain> getMissionTypes() {
		return missionTypes;
	}

	public void setMissionTypes(List<SetupDomain> missionTypes) {
		this.missionTypes = missionTypes;
	}

	public boolean isAddNewSectorCollapseFlag() {
		return addNewSectorCollapseFlag;
	}

	public void setAddNewSectorCollapseFlag(boolean addNewSectorCollapseFlag) {
		this.addNewSectorCollapseFlag = addNewSectorCollapseFlag;
	}

	public List<LabCheckReportDepartmentAction> getLabCheckReportDepartmentActionList() {
		return labCheckReportDepartmentActionList;
	}

	public void setLabCheckReportDepartmentActionList(List<LabCheckReportDepartmentAction> labCheckReportDepartmentActionList) {
		this.labCheckReportDepartmentActionList = labCheckReportDepartmentActionList;
	}

	public LabCheckReportDepartmentAction getNewModifyLabCheckReportDepartmentAction() {
		return newModifyLabCheckReportDepartmentAction;
	}

	public void setNewModifyLabCheckReportDepartmentAction(LabCheckReportDepartmentAction newModifyLabCheckReportDepartmentAction) {
		this.newModifyLabCheckReportDepartmentAction = newModifyLabCheckReportDepartmentAction;
	}

	public boolean isAddNewActionCollapseFlag() {
		return addNewActionCollapseFlag;
	}

	public void setAddNewActionCollapseFlag(boolean addNewActionCollapseFlag) {
		this.addNewActionCollapseFlag = addNewActionCollapseFlag;
	}

	public boolean isAddNewBeneficiaryCollapseFlag() {
		return addNewBeneficiaryCollapseFlag;
	}

	public void setAddNewBeneficiaryCollapseFlag(boolean addNewBeneficiaryCollapseFlag) {
		this.addNewBeneficiaryCollapseFlag = addNewBeneficiaryCollapseFlag;
	}

	public List<SetupDomain> getHealthCareCenters() {
		return healthCareCenters;
	}

	public void setHealthCareCenters(List<SetupDomain> healthCareCenters) {
		this.healthCareCenters = healthCareCenters;
	}

	public LabCheckReportDepartmentData getSelectedLabCheckReportDepartmentData() {
		return selectedLabCheckReportDepartmentData;
	}

	public void setSelectedLabCheckReportDepartmentData(LabCheckReportDepartmentData selectedLabCheckReportDepartmentData) {
		this.selectedLabCheckReportDepartmentData = selectedLabCheckReportDepartmentData;
	}

	public LabCheckReportDepartmentData getNewModifylabCheckReportDepartmentData() {
		return newModifylabCheckReportDepartmentData;
	}

	public void setNewModifylabCheckReportDepartmentData(LabCheckReportDepartmentData newModifylabCheckReportDepartmentData) {
		this.newModifylabCheckReportDepartmentData = newModifylabCheckReportDepartmentData;
	}

	public List<LabCheckReportDepartmentData> getLabCheckReportDepartmentDataList() {
		return labCheckReportDepartmentDataList;
	}

	public void setLabCheckReportDepartmentDataList(List<LabCheckReportDepartmentData> labCheckReportDepartmentDataList) {
		this.labCheckReportDepartmentDataList = labCheckReportDepartmentDataList;
	}

	public List<LabCheckReportDepartmentEmployeeData> getLabCheckReportDepartmentEmployeeDataList() {
		return labCheckReportDepartmentEmployeeDataList;
	}

	public void setLabCheckReportDepartmentEmployeeDataList(List<LabCheckReportDepartmentEmployeeData> labCheckReportDepartmentEmployeeDataList) {
		this.labCheckReportDepartmentEmployeeDataList = labCheckReportDepartmentEmployeeDataList;
	}

	public LabCheckReportDepartmentEmployeeData getNewModifyLabCheckReportDepartmentEmployeeData() {
		return newModifyLabCheckReportDepartmentEmployeeData;
	}

	public void setNewModifyLabCheckReportDepartmentEmployeeData(LabCheckReportDepartmentEmployeeData newModifyLabCheckReportDepartmentEmployeeData) {
		this.newModifyLabCheckReportDepartmentEmployeeData = newModifyLabCheckReportDepartmentEmployeeData;
	}

	public boolean isEditSectorFlag() {
		return editSectorFlag;
	}

	public void setEditSectorFlag(boolean editSectorFlag) {
		this.editSectorFlag = editSectorFlag;
	}

	public boolean isAddNewEmployeeCollapseFlag() {
		return addNewEmployeeCollapseFlag;
	}

	public void setAddNewEmployeeCollapseFlag(boolean addNewEmployeeCollapseFlag) {
		this.addNewEmployeeCollapseFlag = addNewEmployeeCollapseFlag;
	}

	public List<LabCheckReportEmployeeData> getLabCheckReportEmployeeDataList() {
		return labCheckReportEmployeeDataList;
	}

	public void setLabCheckReportEmployeeDataList(List<LabCheckReportEmployeeData> labCheckReportEmployeeDataList) {
		this.labCheckReportEmployeeDataList = labCheckReportEmployeeDataList;
	}

	public LabCheckReportEmployeeData getSelectedLabCheckReportEmployeeData() {
		return selectedLabCheckReportEmployeeData;
	}

	public void setSelectedLabCheckReportEmployeeData(LabCheckReportEmployeeData selectedLabCheckReportEmployeeData) {
		this.selectedLabCheckReportEmployeeData = selectedLabCheckReportEmployeeData;
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
}
