package com.code.ui.backings.info;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.attach.Attachment;
import com.code.dal.orm.info.InfoData;
import com.code.dal.orm.info.InfoSubject;
import com.code.dal.orm.info.InfoType;
import com.code.dal.orm.security.UserMenuActionData;
import com.code.dal.orm.setup.DepartmentData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.EntityNameEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.InfoSourceTypeEnum;
import com.code.enums.InfoStatusEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.UserMenuActionsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.attach.AttachmentService;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.info.InfoService;
import com.code.services.infosys.info.InfoTypeService;
import com.code.services.log4j.Log4j;
import com.code.services.security.SecurityService;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.SetupService;
import com.code.services.workflow.info.InfoWorkFlow;
import com.code.ui.backings.base.BaseBacking;
import com.code.ui.util.EncryptionUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "informationSearch")
@ViewScoped
public class InformationSearch extends BaseBacking implements Serializable {
	private int rowsCount = 10;

	private Date fromDate;
	private Date toDate;
	private Date incomingFromDate;
	private Date incomingToDate;

	private InfoData searchInfo;

	private List<InfoData> infoDataList;

	private List<InfoType> infoTypeList;
	private List<InfoSubject> infoSubjectList;

	private Long selectedInfoTypeId;
	private Long selectedInfoSubjectId;

	private Integer infoRelatedEntityType;
	private Integer infoSourceType;

	private Long countryId;
	private Long departmentId;
	private Long domainId;
	private Long employeeId;
	private Long nonEmployeeId;

	private Long assignmentDetailId;
	private Long openSourceId;

	private String sourceCode;
	private String entityName;
	private List<DepartmentData> regionsList;
	private List<DepartmentData> sectorsList;

	private List<SetupDomain> incomingSideDomainList;
	private boolean isAuthorizedDepartment = false;
	private String searchPhoneNumber;
	private String sectorIds = "";

	private String downloadFileParamId;
	private List<Attachment> attachmentList;
	private boolean openDownloadPopupFlag;
	private boolean openDownloadDialogueFlag;
	private String boolServerDownloadPath;
	boolean deletePrivilage = false;

	/**
	 * Constructor
	 */
	public InformationSearch() {
		super();
		init();
		boolServerDownloadPath = InfoSysConfigurationService.getBoolServerDownloadPath();
		try {
			UserMenuActionData deleteAttachmentAction = SecurityService.getAction(loginEmpData.getEmpId(), UserMenuActionsEnum.DELETE_ATTACHMENT_PERMISSION.getCode(), FlagsEnum.ALL.getCode());
			if (deleteAttachmentAction != null) {
				setDeletePrivilage(true);
			}
			incomingSideDomainList = SetupService.getDomains(ClassesEnum.INCOMING_SIDES.getCode());
			infoTypeList = InfoTypeService.getInfoTypes(null);
			regionsList = DepartmentService.getDepartmentsBydepartmentTypes(null, null, new Long[] { DepartmentTypeEnum.REGION.getCode(), DepartmentTypeEnum.DIRECTORATE.getCode() });;
			sectorsList = new ArrayList<DepartmentData>();
			isAuthorizedDepartment = InfoWorkFlow.isIntelleigenceOrAnalysisDepartment(loginEmpData.getActualDepartmentId());
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InformationSearch.class, e, "InformationSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Get info subject list from info type id
	 */
	public void getInfoSubjectByInfoTypeId() {
		try {
			if (selectedInfoTypeId == null) {
				infoSubjectList.clear();
			} else {
				infoSubjectList = InfoTypeService.getInfoSubjects(FlagsEnum.ALL.getCode(), selectedInfoTypeId, null);
			}
			selectedInfoSubjectId = null;
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InformationSearch.class, e, "InformationSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Initialize search parameters
	 */
	public void init() {
		searchInfo = new InfoData();

		initSources();
		initRelatedEntities();

		infoRelatedEntityType = null;
		infoSourceType = 1;

		selectedInfoTypeId = null;
		selectedInfoSubjectId = null;
		infoSubjectList = null;
		fromDate = null;
		toDate = null;
		incomingFromDate = null;
		incomingToDate = null;
		sectorsList = new ArrayList<DepartmentData>();
	}

	/**
	 * Search info
	 */
	public void searchInfoData() {
		try {
			if (isAuthorizedDepartment) {
				infoDataList = InfoService.getInfoData(searchInfo.getInfoNumber(), searchInfo.getDomainIncomingSideId(), selectedInfoSubjectId, selectedInfoTypeId, loginEmpData, fromDate, toDate, countryId, departmentId, domainId, employeeId, nonEmployeeId, assignmentDetailId, openSourceId, searchInfo.getRegionId(), searchInfo.getSectorId(), searchInfo.getUnitId(), searchInfo.getInfoDetails(), searchPhoneNumber, searchInfo.getIncomingFileNumber(), searchInfo.getIncomingFileYear(), searchInfo.getIncomingNumber(), incomingFromDate, incomingToDate);
			} else {
				infoDataList = InfoService.getInfoDataByAnalysis(searchInfo.getInfoNumber(), searchInfo.getDomainIncomingSideId(), selectedInfoSubjectId, selectedInfoTypeId, loginEmpData, fromDate, toDate, countryId, departmentId, domainId, employeeId, nonEmployeeId, assignmentDetailId, openSourceId, searchInfo.getRegionId(), searchInfo.getSectorId(), searchInfo.getUnitId(), searchInfo.getInfoDetails(), searchPhoneNumber, searchInfo.getIncomingFileNumber(), searchInfo.getIncomingFileYear(), searchInfo.getIncomingNumber(), incomingFromDate, incomingToDate);
			}

			if (infoDataList.isEmpty()) {
				if (searchInfo.getInfoNumber() != null && !searchInfo.getInfoNumber().isEmpty() && !searchInfo.getInfoNumber().trim().equals("")) {
					if (InfoService.isExistingInfo(searchInfo.getInfoNumber())) {
						this.setServerSideSuccessMessages(getParameterizedMessage("error_nonAuthInfo"));
					} else {
						this.setServerSideSuccessMessages(getParameterizedMessage("error_noData"));
					}
				} else {
					this.setServerSideSuccessMessages(getParameterizedMessage("error_noData"));
				}
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InformationSearch.class, e, "InformationSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Initialize sources
	 */
	public void initSources() {
		assignmentDetailId = null;
		openSourceId = null;
	}

	/**
	 * Initialize related entities
	 */
	public void initRelatedEntities() {
		countryId = null;
		departmentId = null;
		domainId = null;
		employeeId = null;
		nonEmployeeId = null;
	}

	/**
	 * View info
	 * 
	 * @param infoDataId
	 * @return navigation string
	 */
	public String viewInfoData(InfoData info) {
		getRequest().setAttribute("mode", info.getId());
		if (info.getStatus().equals(InfoStatusEnum.REGISTERED.getCode())) {
			return NavigationEnum.INFO_DATA_VIEW.toString();
		}
		return NavigationEnum.INFO_VIEW_MODE.toString();
	}

	/**
	 * Delete info
	 * 
	 * @param infoData
	 */
	public void deleteInfoData(InfoData infoData) {
		try {
			InfoService.deleteInfo(infoData, loginEmpData);
			infoDataList.remove(infoData);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InformationSearch.class, e, "InformationSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Print
	 */
	public void print(InfoData infoData) {
		try {
			byte[] bytes = InfoService.getInformationDetailsBytes(infoData.getLetterCopies(), infoData.getLetterNotes());
			String reportName = "Information Search Detail";
			super.print(bytes, reportName);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InformationSearch.class, e, "InformationSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Update
	 */
	public void update(InfoData infoData) {
		try {
			InfoService.updateInfo(infoData, loginEmpData);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InformationSearch.class, e, "InformationSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * getRegionSectors
	 */
	public void getRegionSectors() {
		try {
			searchInfo.setSectorId(null);
			if (searchInfo.getRegionId() != null) {
				sectorsList = DepartmentService.getDepartmentsByRegionIdAndType(searchInfo.getRegionId(), null, null, DepartmentTypeEnum.SECTOR.getCode());
				sectorIds = "";
				for (DepartmentData sector : sectorsList) {
					sectorIds += sector.getId() + ",";
				}
			} else {
				sectorsList = new ArrayList<DepartmentData>();
				sectorIds = "";
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InformationSearch.class, e, "InformationSearch");
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
			   Log4j.traceErrorException(InformationSearch.class, e, "InformationSearch");
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
			   Log4j.traceErrorException(InformationSearch.class, e, "InformationSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Get attachment list related to the entity id passed to the method then check if there's no records then show message indicating no daya else if it's only one record , open the download popup directly , otherwise open a dialogue shows file names and make the user select which one to download then open the download popup with the choosen contentId (attachmentId)
	 * 
	 * @param
	 * @throws BusinessException
	 */
	public void getDownloadedAttachmentsIdList(long infoId) throws BusinessException {
		try {
			attachmentList = AttachmentService.getAttachments(EntityNameEnum.INFO.getCode(), infoId);
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

	// setters and getters
	public int getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
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

	public Date getIncomingFromDate() {
		return incomingFromDate;
	}

	public void setIncomingFromDate(Date incomingFromDate) {
		this.incomingFromDate = incomingFromDate;
	}

	public Date getIncomingToDate() {
		return incomingToDate;
	}

	public void setIncomingToDate(Date incomingToDate) {
		this.incomingToDate = incomingToDate;
	}

	public InfoData getSearchInfo() {
		return searchInfo;
	}

	public void setSearchInfo(InfoData searchInfo) {
		this.searchInfo = searchInfo;
	}

	public List<InfoData> getInfoDataList() {
		return infoDataList;
	}

	public void setInfoDataList(List<InfoData> infoDataList) {
		this.infoDataList = infoDataList;
	}

	public List<InfoType> getInfoTypeList() {
		return infoTypeList;
	}

	public void setInfoTypeList(List<InfoType> infoTypeList) {
		this.infoTypeList = infoTypeList;
	}

	public Long getSelectedInfoTypeId() {
		return selectedInfoTypeId;
	}

	public void setSelectedInfoTypeId(Long selectedInfoTypeId) {
		this.selectedInfoTypeId = selectedInfoTypeId;
	}

	public List<InfoSubject> getInfoSubjectList() {
		return infoSubjectList;
	}

	public void setInfoSubjectList(List<InfoSubject> infoSubjectList) {
		this.infoSubjectList = infoSubjectList;
	}

	public Long getSelectedInfoSubjectId() {
		return selectedInfoSubjectId;
	}

	public void setSelectedInfoSubjectId(Long selectedInfoSubjectId) {
		this.selectedInfoSubjectId = selectedInfoSubjectId;
	}

	public Integer getInfoRelatedEntityType() {
		return infoRelatedEntityType;
	}

	public void setInfoRelatedEntityType(Integer infoRelatedEntityType) {
		this.infoRelatedEntityType = infoRelatedEntityType;
	}

	public Integer getInfoSourceType() {
		return infoSourceType;
	}

	public void setInfoSourceType(Integer infoSourceType) {
		this.infoSourceType = infoSourceType;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Long getDomainId() {
		return domainId;
	}

	public void setDomainId(Long domainId) {
		this.domainId = domainId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Long getNonEmployeeId() {
		return nonEmployeeId;
	}

	public void setNonEmployeeId(Long nonEmployeeId) {
		this.nonEmployeeId = nonEmployeeId;
	}

	public Long getAssignmentDetailId() {
		return assignmentDetailId;
	}

	public void setAssignmentDetailId(Long assignmentDetailId) {
		this.assignmentDetailId = assignmentDetailId;
	}

	public Long getOpenSourceId() {
		return openSourceId;
	}

	public void setOpenSourceId(Long openSourceId) {
		this.openSourceId = openSourceId;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public List<SetupDomain> getIncomingSideDomainList() {
		return incomingSideDomainList;
	}

	public void setIncomingSideDomainList(List<SetupDomain> incomingSideDomainList) {
		this.incomingSideDomainList = incomingSideDomainList;
	}

	public List<DepartmentData> getRegionsList() {
		return regionsList;
	}

	public void setRegionsList(List<DepartmentData> regionsList) {
		this.regionsList = regionsList;
	}

	public List<DepartmentData> getSectorsList() {
		return sectorsList;
	}

	public void setSectorsList(List<DepartmentData> sectorsList) {
		this.sectorsList = sectorsList;
	}

	public String getInfoDomainPopupSearch() {
		return "" + ClassesEnum.ORGNAIZATIONS_AND_ISSUES.getCode();
	}

	public String getSearchPhoneNumber() {
		return searchPhoneNumber;
	}

	public void setSearchPhoneNumber(String searchPhoneNumber) {
		this.searchPhoneNumber = searchPhoneNumber;
	}

	public String getSectorIds() {
		return sectorIds;
	}

	public void setSectorIds(String sectorIds) {
		this.sectorIds = sectorIds;
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

	public String getBoolServerDownloadPath() {
		return boolServerDownloadPath;
	}

	public void setBoolServerDownloadPath(String boolServerDownloadPath) {
		this.boolServerDownloadPath = boolServerDownloadPath;
	}

	public Integer getAssignmentType() {
		return InfoSourceTypeEnum.ASSIGNMENT.getCode();
	}

	public Integer getCooperatorType() {
		return InfoSourceTypeEnum.COOPERATOR.getCode();
	}

	public Integer getOpenSourceType() {
		return InfoSourceTypeEnum.OPEN_SOURCE.getCode();
	}
	
	public boolean isDeletePrivilage() {
		return deletePrivilage;
	}

	public void setDeletePrivilage(boolean deletePrivilage) {
		this.deletePrivilage = deletePrivilage;
	}
}