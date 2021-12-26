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
import com.code.dal.orm.labcheck.LabCheck;
import com.code.dal.orm.labcheck.LabCheckEmployeeData;
import com.code.enums.EntityNameEnum;
import com.code.enums.LabCheckEmployeeCheckStatusEnum;
import com.code.enums.LabCheckReasonsEnum;
import com.code.enums.ServiceCodesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.attach.AttachmentService;
import com.code.services.attach.AttachmentServiceCallBack;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.labcheck.LabCheckService;
import com.code.services.infosys.labcheck.MissionReportService;
import com.code.services.log4j.Log4j;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;
import com.code.ui.util.EncryptionUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "randomLabCheckSearch")
@ViewScoped
public class RandomLabChecksSearch extends BaseBacking implements Serializable {
	 private List<LabCheck> randomLabChecksList;
	 private LabCheck selectedLabCheck;
	 private List<LabCheckEmployeeData> labCheckEmployeeDataList;
	 private LabCheckEmployeeData selectedLabCheckEmployeeData;
	 private String orderNumber;
	 private boolean inCompleteCheck;
	 private String fileArchivingParam;
	 private String downloadFileParamId;
	 private List<Attachment> attachmentList;
	 private boolean openDownloadPopupFlag;
	 private boolean openDownloadDialogueFlag;
	 private String boolServerUploadPath;
	 private String boolServerDownloadPath;

	 /**
	  * Constructor
	  */
	 public RandomLabChecksSearch() {
		  boolServerUploadPath = InfoSysConfigurationService.getBoolServerUploadPath();
		  boolServerDownloadPath = InfoSysConfigurationService.getBoolServerDownloadPath();
	 }

	 /**
	  * Search lab checks by order number
	  */
	 public void search() {
		  try {
			   randomLabChecksList = LabCheckService.getLabCheckByOrderNumberAndCheckReason(LabCheckReasonsEnum.RANDOM_MISSION_FROM_DIRECTORATE.getCode(), orderNumber);
			   labCheckEmployeeDataList = new ArrayList<LabCheckEmployeeData>();
			   selectedLabCheck = null;
			   if (randomLabChecksList.isEmpty()) {
					this.setServerSideSuccessMessages(getParameterizedMessage("error_noData"));
			   }
		  } catch (BusinessException e) {
			   this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		  }catch (Exception e) {
			   Log4j.traceErrorException(RandomLabChecksSearch.class, e, "RandomLabChecksSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	 }

	 /**
	  * Show lab specific check details (ie : employees assigned to this lab check)
	  */
	 public void showRandomLabCheckDetails(LabCheck labCheck) {
		  try {
			   labCheckEmployeeDataList = LabCheckService.getLabCheckEmployesByLabCheckId(labCheck.getId(), null);
			   selectedLabCheck = labCheck;
			   selectedLabCheckEmployeeData = new LabCheckEmployeeData();
			   if (labCheckEmployeeDataList.isEmpty()) {
					this.setServerSideSuccessMessages(getParameterizedMessage("error_noData"));
					inCompleteCheck = true;
			   } else {
					if (MissionReportService.getCountLabChecksPerOrderNumber(selectedLabCheck.getOrderNumber()) > 0) {
						 inCompleteCheck = false;
					} else {
						 inCompleteCheck = true;
					}
			   }
		  } catch (BusinessException e) {
			   this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		  }catch (Exception e) {
			   Log4j.traceErrorException(RandomLabChecksSearch.class, e, "RandomLabChecksSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	 }

	 /**
	  * Add new labCheckEmployee to a specific lab check
	  */
	 public void addEmployee() {
		  for (LabCheckEmployeeData labCheckEmployeeData : labCheckEmployeeDataList) {
			   if (labCheckEmployeeData.getEmployeeId().equals(selectedLabCheckEmployeeData.getEmployeeId())) {
					this.setServerSideErrorMessages(getParameterizedMessage("error_alreadyChoosen"));
					return;
			   }
		  }
		  try {
			   selectedLabCheckEmployeeData.setLabCheckId(selectedLabCheck.getId());
			   selectedLabCheckEmployeeData.setOrderNumber(selectedLabCheck.getOrderNumber());
			   selectedLabCheckEmployeeData.setOrderDate(selectedLabCheck.getOrderDate());
			   selectedLabCheckEmployeeData.setCheckStatus(LabCheckEmployeeCheckStatusEnum.NO_SAMPLE.getCode());

			   List<LabCheckEmployeeData> labCheckEmployeeDataListTemp = new ArrayList<LabCheckEmployeeData>();
			   labCheckEmployeeDataListTemp.add(selectedLabCheckEmployeeData);
			   LabCheckService.checkEmployeesLabCheckUnderProcess(labCheckEmployeeDataListTemp);

			   LabCheckService.saveLabCheckEmployee(selectedLabCheckEmployeeData, loginEmpData);
			   labCheckEmployeeDataList.add(selectedLabCheckEmployeeData);
			   selectedLabCheckEmployeeData = new LabCheckEmployeeData();
			   this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		  } catch (BusinessException e) {
			   this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		  }catch (Exception e) {
			   Log4j.traceErrorException(RandomLabChecksSearch.class, e, "RandomLabChecksSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	 }

	 /**
	  * Delete lab check employee from a specific lab check
	  */
	 public void deleteEmployee(LabCheckEmployeeData labCheckEmployeeData) {
		  try {
			   LabCheckService.deleteLabCheckEmployee(labCheckEmployeeData, loginEmpData);
			   labCheckEmployeeDataList.remove(labCheckEmployeeData);
		  } catch (BusinessException e) {
			   this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		  }catch (Exception e) {
			   Log4j.traceErrorException(RandomLabChecksSearch.class, e, "RandomLabChecksSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	 }

	 /**
	  * print lab check report
	  * 
	  * @param labCheckEmployeeData
	  */
	 public void printLabCheckReport(LabCheckEmployeeData labCheckEmployeeData) {
		  try {
			   byte[] bytes;
			   String employeeId = labCheckEmployeeData.getEmployeeId().toString();
			   String sampleNumber = labCheckEmployeeData.getSampleNumber();
			   String sampleDateString = HijriDateService.getHijriDateString(labCheckEmployeeData.getSampleDate());
			   bytes = LabCheckService.getLabCheckReportBytes(employeeId, sampleNumber, sampleDateString);
			   super.print(bytes, "Lab check report");
		  } catch (BusinessException e) {
			   this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		  }catch (Exception e) {
			   Log4j.traceErrorException(RandomLabChecksSearch.class, e, "RandomLabChecksSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	 }

	 /**
	  * reset search parameters
	  */
	 public void resetSearch() {
		  orderNumber = null;
	 }

	 /**
	  * Constitute upload parameter which contains bool server registered serviceCode and the token (EntityName_EntityId)
	  * 
	  * @param labCheckId
	  */
	 public void getUploadParam(long labCheckId) {
		  try {
			   String requestURL = getRequest().getRequestURL() + "";
			   String url = requestURL.replace(getRequest().getServletPath(), "");
			   String serviceURL = url + AttachmentServiceCallBack.SERVICE_URL;
			   fileArchivingParam = EncryptionUtil.encryptSymmetrically("code=" + ServiceCodesEnum.INFO_SYS.getCode() + "&token=" + EntityNameEnum.LAB_CHECK.getCode() + "_" + labCheckId + "&serviceurl=" + serviceURL);
		  } catch (BusinessException e) {
			   this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		  } catch (Exception e) {
			   Log4j.traceErrorException(RandomLabChecksSearch.class, e, "RandomLabChecksSearch");
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
			   Log4j.traceErrorException(RandomLabChecksSearch.class, e, "RandomLabChecksSearch");
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
		  }catch (Exception e) {
			   Log4j.traceErrorException(RandomLabChecksSearch.class, e, "RandomLabChecksSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	 }

	 /**
	  * Get attachment list related to the entity id passed to the method then check if there's no records then show message indicating no daya else if it's only one record , open the download popup directly , otherwise open a dialogue shows file names and make the user select which one to download then open the download popup with the choosen contentId (attachmentId)
	  * 
	  * @param labCheckId
	  * @throws BusinessException
	  */
	 public void getDownloadedAttachmentsIdList(long labCheckId) throws BusinessException {
		  try {
			   attachmentList = AttachmentService.getAttachments(EntityNameEnum.LAB_CHECK.getCode(), labCheckId);
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
	 public void print(long labCheckId) {
		  try {
			   byte[] bytes = LabCheckService.getLabCheckDetailsReportBytes(labCheckId, loginEmpData.getFullName());
			   super.print(bytes, "Lab_Check_Details");
		  } catch (BusinessException e) {
			   this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		  }catch (Exception e) {
			   Log4j.traceErrorException(RandomLabChecksSearch.class, e, "RandomLabChecksSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	 }

	 /**
	  * Setters and getters
	  */
	 public String getOrderNumber() {
		  return orderNumber;
	 }

	 public void setOrderNumber(String orderNumber) {
		  this.orderNumber = orderNumber;
	 }

	 public List<LabCheck> getRandomLabChecksList() {
		  return randomLabChecksList;
	 }

	 public void setRandomLabChecksList(List<LabCheck> randomLabChecksList) {
		  this.randomLabChecksList = randomLabChecksList;
	 }

	 public List<LabCheckEmployeeData> getLabCheckEmployeeDataList() {
		  return labCheckEmployeeDataList;
	 }

	 public void setLabCheckEmployeeDataList(List<LabCheckEmployeeData> labCheckEmployeeDataList) {
		  this.labCheckEmployeeDataList = labCheckEmployeeDataList;
	 }

	 public LabCheckEmployeeData getSelectedLabCheckEmployeeData() {
		  return selectedLabCheckEmployeeData;
	 }

	 public void setSelectedLabCheckEmployeeData(LabCheckEmployeeData selectedLabCheckEmployeeData) {
		  this.selectedLabCheckEmployeeData = selectedLabCheckEmployeeData;
	 }

	 public LabCheck getSelectedLabCheck() {
		  return selectedLabCheck;
	 }

	 public boolean isInCompleteCheck() {
		  return inCompleteCheck;
	 }

	 public void setInCompleteCheck(boolean inCompleteCheck) {
		  this.inCompleteCheck = inCompleteCheck;
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
