package com.code.ui.backings.info;

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletResponse;

import com.code.dal.orm.assignment.AssignmentDetailData;
import com.code.dal.orm.attach.Attachment;
import com.code.dal.orm.info.Classification;
import com.code.dal.orm.info.ClassificationType;
import com.code.dal.orm.info.InfoAnalysis;
import com.code.dal.orm.info.InfoAnalysisDetailData;
import com.code.dal.orm.info.InfoAnalysisReferenceData;
import com.code.dal.orm.info.InfoCoordinate;
import com.code.dal.orm.info.InfoData;
import com.code.dal.orm.info.InfoPhone;
import com.code.dal.orm.info.InfoRecommendationData;
import com.code.dal.orm.info.InfoSubject;
import com.code.dal.orm.info.InfoType;
import com.code.dal.orm.info.OpenSource;
import com.code.dal.orm.setup.CountryData;
import com.code.dal.orm.setup.DepartmentData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.setup.NonEmployeeData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.EntityNameEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.InfoAnalysisRedirectionTypeEnum;
import com.code.enums.InfoConfidentialityEnum;
import com.code.enums.InfoImportanceEnum;
import com.code.enums.InfoRelatedEntityTypeEnum;
import com.code.enums.InfoSourceTypeEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.ServiceCodesEnum;
import com.code.enums.WFPositionsEnum;
import com.code.enums.WFTaskActionsEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.attach.AttachmentService;
import com.code.services.attach.AttachmentServiceCallBack;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.assignment.AssignmentService;
import com.code.services.infosys.info.InfoService;
import com.code.services.infosys.info.InfoTypeService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.ClassificationService;
import com.code.services.setup.CountryService;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.EmployeeService;
import com.code.services.setup.NonEmployeeService;
import com.code.services.setup.OpenSourceService;
import com.code.services.setup.SetupService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.WFPositionService;
import com.code.services.workflow.info.InfoWorkFlow;
import com.code.ui.backings.base.WFBaseBacking;
import com.code.ui.util.EncryptionUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "infoRegistration")
@ViewScoped
public class InfoRegisteration extends WFBaseBacking implements Serializable {
	private InfoData infoRequest;
	private List<DepartmentData> regions;
	private List<DepartmentData> sectors;
	private Integer createInfoType;
	private Long infoTypeId;
	private List<InfoType> infoTypeList;
	private List<InfoSubject> infoSubjectList;
	private List<SetupDomain> infoImportanceList;
	private List<SetupDomain> infoConfidentialityList;
	private List<SetupDomain> incomingSideDomainList;
	private List<SetupDomain> dealingTypeDomainList;
	private List<SetupDomain> dealingLevelDomainList;
	private List<InfoCoordinate> infoCoordinateList;
	private List<InfoPhone> infoPhoneList;
	private List<OpenSource> openSourceList;
	private OpenSource openSourceInsert;
	private List<AssignmentDetailData> assignmentList;
	private AssignmentDetailData assignmentInsert;
	private List<DepartmentData> departmentDataList;
	private DepartmentData departmentDataInsert;
	private List<SetupDomain> setupDomainList;
	private SetupDomain setupDomainInsert;
	private List<CountryData> countryDataList;
	private CountryData countryDataInsert;
	private List<EmployeeData> employeeDataList;
	private EmployeeData employeeDataInsert;
	private List<NonEmployeeData> nonEmployeesDataList;
	private NonEmployeeData nonEmployeeDataInsert;
	private Map<Integer, Set<Long>> relatedEntitsSet;
	private Boolean isSaving;
	private List<DepartmentData> analysisDepartments;
	private Map<DepartmentData, Boolean> processingMap;
	private Map<DepartmentData, Boolean> notifiedMap;
	private Boolean isAnalysis;
	private List<InfoRecommendationData> infoRecommendationList;
	private Boolean internalMinistry;
	private Boolean secuirtySection;
	private Boolean internalDepartment;
	private Integer selectedOption;
	private String[] selectedSecurityDomains;
	private List<SetupDomain> securitySectionsDomainsList;
	private List<DepartmentData> regionDepartments;
	private Long[] selectedRegions;
	private String selectedDirDepartment;
	private Long directorateDepId;
	private Map<Integer, List<Classification>> classificationsMap;
	private List<ClassificationType> classificationTypesList;
	private List<InfoAnalysisDetailData> infoAnalysisDetailDataList;
	private InfoAnalysis infoAnalysis;
	private Long[] selectedAnalysisDomains;
	private List<InfoAnalysis> infoAnalysisList;
	private List<InfoAnalysisReferenceData> analysisRefenceList;
	private List<String> transferedProcessingList;
	private List<String> transferedNotifiedList;
	private String sectorIds = "";

	private String fileArchivingParam;
	private String downloadFileParamId;
	private List<Attachment> attachmentList;
	private String selectedDownloadFileId;
	private boolean openDownloadPopupFlag;
	private boolean openDownloadDialogueFlag;
	private boolean isSaveSM;
	private String errorMessage;
	private boolean isFinalSend;
	private List<SetupDomain> infoEvaluations;

	/**
	 * Constructor
	 */
	public InfoRegisteration() {
		super();
		init();
		infoInitilize();
	}

	/**
	 * infoInitilize
	 */
	public void infoInitilize() {
		try {
			isSaveSM = false;
			isSaving = false;
			isFinalSend = false;
			regions = new ArrayList<DepartmentData>();
			sectors = new ArrayList<DepartmentData>();

			directorateDepId = InfoWorkFlow.getDirectorateOrRegionId(loginEmpData);

			if (getRequest().getAttribute("mode") != null) {
				infoRequest = InfoService.getInfoDataById((Long) getRequest().getAttribute("mode"));
			} else if (currentTask != null) {
				infoRequest = InfoService.getInfoDataByInstanceId(currentTask.getInstanceId());
			}

			assignmentInsert = new AssignmentDetailData();
			departmentDataInsert = new DepartmentData();
			countryDataInsert = new CountryData();
			setupDomainInsert = new SetupDomain();
			openSourceInsert = new OpenSource();
			employeeDataInsert = new EmployeeData();
			nonEmployeeDataInsert = new NonEmployeeData();
			transferedProcessingList = new ArrayList<String>();
			transferedNotifiedList = new ArrayList<String>();
			
			infoImportanceList = SetupService.getDomains(ClassesEnum.INFO_PRIORITY.getCode());
			infoConfidentialityList = SetupService.getDomains(ClassesEnum.INFO_CONFIDENTIALITY.getCode());

			if (infoRequest != null) {
				infoEvaluations = SetupService.getDomains(ClassesEnum.INFO_EVALUATION.getCode());
				infoCoordinateList = InfoService.getInfoCoordinates(infoRequest.getId());
				infoPhoneList = InfoService.getInfoPhones(infoRequest.getId());
				assignmentList = AssignmentService.getInfoAssignments(infoRequest.getId());
				openSourceList = OpenSourceService.getInfoOpenSources(infoRequest.getId());
				departmentDataList = DepartmentService.getInfoRelatedDepartments(infoRequest.getId());
				countryDataList = CountryService.getInfoRelatedCountries(infoRequest.getId());
				setupDomainList = SetupService.getInfoRelatedEntities(infoRequest.getId());
				employeeDataList = EmployeeService.getInfoRelatedEmployees(infoRequest.getId());
				nonEmployeesDataList = NonEmployeeService.getInfoRelatedNonEmployees(infoRequest.getId());
				if (infoRequest.getInfoEvaluationId() != null) {
					Iterator<SetupDomain> iter = infoEvaluations.iterator();
					while (iter.hasNext()) {
						SetupDomain obj = iter.next();
						if (obj.getDescription().equals(infoRequest.getInfoEvaluation())) {
							iter.remove();
						}
					}
				}
			}
			if (role.equals(WFTaskRolesEnum.REQUESTER.getCode())) {
				relatedEntitsSet = new HashMap<Integer, Set<Long>>();
				if (infoRequest == null) {
					infoRequest = new InfoData();
					createInfoType = 1;
					infoRequest.setReceiveDate(HijriDateService.getHijriSysDate());
					DateFormat dateFormat = new SimpleDateFormat("HH:mm");
					infoRequest.setReceiveTime(dateFormat.format(new Date()));
					infoRequest.setImportance(infoImportanceList.get(0).getId());
					infoRequest.setConfidentiality(infoConfidentialityList.get(0).getId());
					infoCoordinateList = new ArrayList<InfoCoordinate>();
					infoPhoneList = new ArrayList<InfoPhone>();
					openSourceList = new ArrayList<OpenSource>();
					assignmentList = new ArrayList<AssignmentDetailData>();
					departmentDataList = new ArrayList<DepartmentData>();
					setupDomainList = new ArrayList<SetupDomain>();
					countryDataList = new ArrayList<CountryData>();
					employeeDataList = new ArrayList<EmployeeData>();
					nonEmployeesDataList = new ArrayList<NonEmployeeData>();
					infoSubjectList = new ArrayList<InfoSubject>();

				} else {
					if (infoRequest.getRelatedInfoId() != null) {
						createInfoType = 2;
					} else {
						createInfoType = 1;
					}
					infoTypeId = infoRequest.getInfoTypeId();
					infoSubjectList = InfoTypeService.getInfoSubjects(FlagsEnum.ALL.getCode(), infoTypeId, null);
					if (!departmentDataList.isEmpty()) {
						Set<Long> newRelatedSet = new HashSet<Long>();
						for (DepartmentData relatedEntity : departmentDataList) {
							newRelatedSet.add(relatedEntity.getId());
						}
						relatedEntitsSet.put(InfoRelatedEntityTypeEnum.DEPARTMENT.getCode(), newRelatedSet);
					}
					if (!setupDomainList.isEmpty()) {
						Set<Long> newRelatedSet = new HashSet<Long>();
						for (SetupDomain relatedEntity : setupDomainList) {
							newRelatedSet.add(relatedEntity.getId());
						}
						relatedEntitsSet.put(InfoRelatedEntityTypeEnum.DOMAIN.getCode(), newRelatedSet);
					}
					if (!countryDataList.isEmpty()) {
						Set<Long> newRelatedSet = new HashSet<Long>();
						for (CountryData relatedEntity : countryDataList) {
							newRelatedSet.add(relatedEntity.getId());
						}
						relatedEntitsSet.put(InfoRelatedEntityTypeEnum.COUNTRY.getCode(), newRelatedSet);
					}
					if (!employeeDataList.isEmpty()) {
						Set<Long> newRelatedSet = new HashSet<Long>();
						for (EmployeeData relatedEntity : employeeDataList) {
							newRelatedSet.add(relatedEntity.getEmpId());
						}
						relatedEntitsSet.put(InfoRelatedEntityTypeEnum.EMPLOYEE.getCode(), newRelatedSet);
					}
					if (!nonEmployeesDataList.isEmpty()) {
						Set<Long> newRelatedSet = new HashSet<Long>();
						for (NonEmployeeData relatedEntity : nonEmployeesDataList) {
							newRelatedSet.add(relatedEntity.getId());
						}
						relatedEntitsSet.put(InfoRelatedEntityTypeEnum.NON_EMPLOYEE.getCode(), newRelatedSet);
					}
				}
				infoTypeList = InfoTypeService.getInfoTypes(null);

				Long regionId = DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());
				// If login employee under directorate department then we will get all regions to choose from them
				
				// Ahmad Alsaqqa modification
				regionId = null;
				// ===
				
				if (regionId != null) {
					infoRequest.setRegionId(regionId);
					infoRequest.setRegionName(DepartmentService.getDepartment(regionId).getArabicName());
					Long sectorId = infoRequest.getSectorId();
					getRegionSectors();
					infoRequest.setSectorId(sectorId);
				} else {
					regions = DepartmentService.getDepartmentsBydepartmentTypes(null, null, new Long[] { DepartmentTypeEnum.REGION.getCode(), DepartmentTypeEnum.DIRECTORATE.getCode() });
				}
				if (infoRequest.getRegionId() != null) {
					sectors = DepartmentService.getDepartmentsByRegionIdAndType(infoRequest.getRegionId(), null, null, DepartmentTypeEnum.SECTOR.getCode());
				} else {
					sectors = new ArrayList<DepartmentData>();
				}
				incomingSideDomainList = SetupService.getDomains(ClassesEnum.INCOMING_SIDES.getCode());
				dealingTypeDomainList = SetupService.getDomains(ClassesEnum.DEALING_TYPES.getCode());
				dealingLevelDomainList = SetupService.getDomains(ClassesEnum.DEALING_LEVELS.getCode());

			} else {
				if (infoRequest.getRelatedInfoId() == null) {
					createInfoType = 1;
				} else {
					createInfoType = 2;
				}

				if (role.equals(WFTaskRolesEnum.GENERAL_DIRECTOR.getCode()) || role.equals(WFTaskRolesEnum.GENERAL_DIRECTOR_ANALYSIS.getCode()) || role.equals(WFTaskRolesEnum.REGION_DIRECTOR.getCode()) || role.equals(WFTaskRolesEnum.REGION_DIRECTOR_ANALYSIS.getCode())) {
					if (role.equals(WFTaskRolesEnum.GENERAL_DIRECTOR.getCode()) || role.equals(WFTaskRolesEnum.REGION_DIRECTOR.getCode())) {
						analysisDepartments = InfoWorkFlow.getAnalysisDepartments(loginEmpData);
						processingMap = new HashMap<DepartmentData, Boolean>();
						notifiedMap = new HashMap<DepartmentData, Boolean>();
						for (DepartmentData department : analysisDepartments) {
							processingMap.put(department, false);
							notifiedMap.put(department, false);
						}
						isAnalysis = true;
					}

					infoRecommendationList = new ArrayList<InfoRecommendationData>();
					for (EmployeeData employee : employeeDataList) {
						InfoRecommendationData infoRecommendationData = new InfoRecommendationData();
						infoRecommendationData.setEmployeeFullName(employee.getFullName());
						infoRecommendationData.setInfoId(infoRequest.getId());
						infoRecommendationData.setEmployeeId(employee.getEmpId());
						infoRecommendationData.setEmployeeRank(employee.getRank());
						infoRecommendationData.setEmployeeSocialNumber(employee.getSocialID());
						infoRecommendationList.add(infoRecommendationData);
					}
					securitySectionsDomainsList = SetupService.getDomains(ClassesEnum.SECURITY_SECTIONS.getCode());
					regionDepartments = DepartmentService.getDepartmentsBydepartmentType(DepartmentTypeEnum.REGION.getCode());
					if (!directorateDepId.equals(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId())) {
						int index = 0;
						for (int i = 0; i < regionDepartments.size(); i++) {
							if (regionDepartments.get(i).getId().equals(directorateDepId)) {
								index = i;
								break;
							}
						}
						regionDepartments.remove(index);
					}

					if (role.equals(WFTaskRolesEnum.GENERAL_DIRECTOR_ANALYSIS.getCode()) || role.equals(WFTaskRolesEnum.REGION_DIRECTOR_ANALYSIS.getCode())) {
						infoAnalysisList = InfoService.getInfoAnalysis(infoRequest.getId(), InfoAnalysisRedirectionTypeEnum.PROCESSING.getCode());
						isAnalysis = false;
						analysisRefenceList = new ArrayList<InfoAnalysisReferenceData>();
					}
				} else if (role.equals(WFTaskRolesEnum.GENERAL_INFO_ANALYSIS_DEPARTMENT_MANAGER.getCode()) || role.equals(WFTaskRolesEnum.REGION_INFO_ANALYSIS_DEPARTMENT_MANAGER.getCode())) {
					classificationTypesList = ClassificationService.getClassificationTypes(null, null);
					infoAnalysis = InfoService.getInfoAnalysisByInfoIdAndDepartmentId(infoRequest.getId(), EmployeeService.getEmployee(currentTask.getOriginalId()).getActualDepartmentId(), InfoAnalysisRedirectionTypeEnum.PROCESSING.getCode());
					infoAnalysisDetailDataList = new ArrayList<InfoAnalysisDetailData>();
					classificationsMap = new HashMap<Integer, List<Classification>>();
					securitySectionsDomainsList = SetupService.getDomains(ClassesEnum.ANALYSIS_REFERENCE.getCode());
					relatedEntitsSet = new HashMap<Integer, Set<Long>>();
					if (!departmentDataList.isEmpty()) {
						Set<Long> newRelatedSet = new HashSet<Long>();
						for (DepartmentData relatedEntity : departmentDataList) {
							newRelatedSet.add(relatedEntity.getId());
						}
						relatedEntitsSet.put(InfoRelatedEntityTypeEnum.DEPARTMENT.getCode(), newRelatedSet);
					}
					if (!setupDomainList.isEmpty()) {
						Set<Long> newRelatedSet = new HashSet<Long>();
						for (SetupDomain relatedEntity : setupDomainList) {
							newRelatedSet.add(relatedEntity.getId());
						}
						relatedEntitsSet.put(InfoRelatedEntityTypeEnum.DOMAIN.getCode(), newRelatedSet);
					}
					if (!countryDataList.isEmpty()) {
						Set<Long> newRelatedSet = new HashSet<Long>();
						for (CountryData relatedEntity : countryDataList) {
							newRelatedSet.add(relatedEntity.getId());
						}
						relatedEntitsSet.put(InfoRelatedEntityTypeEnum.COUNTRY.getCode(), newRelatedSet);
					}
					if (!employeeDataList.isEmpty()) {
						Set<Long> newRelatedSet = new HashSet<Long>();
						for (EmployeeData relatedEntity : employeeDataList) {
							newRelatedSet.add(relatedEntity.getEmpId());
						}
						relatedEntitsSet.put(InfoRelatedEntityTypeEnum.EMPLOYEE.getCode(), newRelatedSet);
					}
					if (!nonEmployeesDataList.isEmpty()) {
						Set<Long> newRelatedSet = new HashSet<Long>();
						for (NonEmployeeData relatedEntity : nonEmployeesDataList) {
							newRelatedSet.add(relatedEntity.getId());
						}
						relatedEntitsSet.put(InfoRelatedEntityTypeEnum.NON_EMPLOYEE.getCode(), newRelatedSet);
					}
				}
			}
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void loadInfoRelatedData() {
		try {
			InfoData relatedInfoData = InfoService.getInfoDataById(infoRequest.getRelatedInfoId());
			infoRequest.setImportance(relatedInfoData.getImportance());
			infoRequest.setInfoDetails(relatedInfoData.getInfoDetails());
			infoRequest.setInfoSubjectId(relatedInfoData.getInfoSubjectId());
			infoRequest.setInfoTypeId(relatedInfoData.getInfoTypeId());
			infoRequest.setReceiveDate(relatedInfoData.getReceiveDate());
			infoRequest.setReceiveTime(relatedInfoData.getReceiveTime());
			infoRequest.setSectorId(relatedInfoData.getSectorId());
			infoRequest.setConfidentiality(relatedInfoData.getConfidentiality());
			infoTypeId = relatedInfoData.getInfoTypeId();
			infoSubjectList = InfoTypeService.getInfoSubjects(FlagsEnum.ALL.getCode(), infoTypeId, null);
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * 
	 */
	public void emptyInfoRelatedData() {
		try {
			if (createInfoType == 1) {
				infoRequest.setReceiveDate(HijriDateService.getHijriSysDate());
				infoRequest.setInfoDetails(null);
				infoRequest.setInfoSubjectId(null);
				infoRequest.setInfoTypeId(null);
				infoRequest.setSectorId(null);
				infoRequest.setRelatedInfoId(null);
				DateFormat dateFormat = new SimpleDateFormat("HH:mm");
				infoRequest.setReceiveTime(dateFormat.format(new Date()));
				infoRequest.setImportance(infoImportanceList.get(0).getId());
				infoRequest.setConfidentiality(infoConfidentialityList.get(0).getId());
				infoSubjectList = new ArrayList<InfoSubject>();
			}
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * get Info Subject By Info Type Id
	 */
	public void getInfoSubjectByInfoTypeId() {
		try {
			infoSubjectList.clear();
			infoRequest.setInfoSubjectId(null);
			if (infoTypeId != null)
				infoSubjectList = InfoTypeService.getInfoSubjects(FlagsEnum.ALL.getCode(), infoTypeId, null);
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * add New Coordinate
	 */
	public void addNewCoordinate() {
		InfoCoordinate newInfoCoordinate = new InfoCoordinate();
		newInfoCoordinate.setSelected(true);
		infoCoordinateList.add(newInfoCoordinate);
	}

	/**
	 * delete Coordinate
	 * 
	 * @param infoCoordinate
	 */
	public void deleteCoordinate(InfoCoordinate infoCoordinate) {
		try {
			if (infoCoordinate.getId() != null) {
				InfoService.deleteInfoCoordinates(infoCoordinate, loginEmpData);
			}
			infoCoordinateList.remove(infoCoordinate);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * add New Phone
	 */
	public void addNewPhone() {
		InfoPhone newInfoPhone = new InfoPhone();
		newInfoPhone.setSelected(true);
		infoPhoneList.add(newInfoPhone);

	}

	/**
	 * delete Phone
	 * 
	 * @param infoPhone
	 */
	public void deletePhone(InfoPhone infoPhone) {
		try {
			if (infoPhone.getId() != null) {
				InfoService.deleteInfoPhones(infoPhone, loginEmpData);
			}
			infoPhoneList.remove(infoPhone);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * add Source depend on info source Type
	 * 
	 * @param infoSourceType
	 */
	public void addSource(Integer infoSourceType) {
		try {
			if (infoSourceType.equals(InfoSourceTypeEnum.OPEN_SOURCE.getCode())) {
				for (OpenSource openSource : openSourceList) {
					if (openSourceInsert.getId().equals(openSource.getId())) {
						throw new BusinessException("error_alreadyChoosen");
					}
				}
				OpenSource newOpenSource = new OpenSource();
				newOpenSource.setName(openSourceInsert.getName());
				newOpenSource.setId(openSourceInsert.getId());
				newOpenSource.setSelected(true);
				openSourceList.add(newOpenSource);
				openSourceInsert = new OpenSource();
			} else {
				for (AssignmentDetailData assignmentDetailData : assignmentList) {
					if (assignmentInsert.getId().equals(assignmentDetailData.getId())) {
						throw new BusinessException("error_alreadyChoosen");
					}
				}
				AssignmentDetailData newAssignment = new AssignmentDetailData();
				newAssignment.setId(assignmentInsert.getId());
				newAssignment.setFullName(assignmentInsert.getFullName());
				newAssignment.setAgentCode(assignmentInsert.getAgentCode());
				newAssignment.setSelected(true);
				newAssignment.setType(assignmentInsert.getType());
				assignmentList.add(newAssignment);
				assignmentInsert = new AssignmentDetailData();
			}
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * delete Source
	 * 
	 * @param infoSourceType
	 * @param opensource
	 * @param assignment
	 */
	public void deleteSource(Integer infoSourceType, OpenSource opensource, AssignmentDetailData assignment) {
		try {
			if (infoSourceType.equals(InfoSourceTypeEnum.OPEN_SOURCE.getCode())) {
				if (!opensource.getSelected()) {
					InfoService.deleteInfoSource(opensource.getId(), infoRequest.getId(), InfoSourceTypeEnum.OPEN_SOURCE, loginEmpData);
				}
				openSourceList.remove(opensource);
			} else if (infoSourceType.equals(InfoSourceTypeEnum.ASSIGNMENT.getCode())) {
				if (!assignment.getSelected()) {
					InfoService.deleteInfoSource(assignment.getId(), infoRequest.getId(), InfoSourceTypeEnum.ASSIGNMENT, loginEmpData);
				}
				assignmentList.remove(assignment);
			} else if (infoSourceType.equals(InfoSourceTypeEnum.COOPERATOR.getCode())) {
				if (!assignment.getSelected()) {
					InfoService.deleteInfoSource(assignment.getId(), infoRequest.getId(), InfoSourceTypeEnum.COOPERATOR, loginEmpData);
				}
				assignmentList.remove(assignment);
			}

			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * add Related Entities depend on relatedEntityType
	 * 
	 * @param relatedEntityType
	 */
	public void addRelatedEntities(Integer relatedEntityType) {
		try {
			if (relatedEntityType.equals(InfoRelatedEntityTypeEnum.DEPARTMENT.getCode())) {
				if (!relatedEntitsSet.containsKey(relatedEntityType)) {
					Set<Long> newDepartmentSet = new HashSet<Long>();
					newDepartmentSet.add(departmentDataInsert.getId());
					relatedEntitsSet.put(relatedEntityType, newDepartmentSet);
				} else {
					Set<Long> existDepartmentSet = relatedEntitsSet.get(relatedEntityType);
					if (!existDepartmentSet.contains(departmentDataInsert.getId())) {
						existDepartmentSet.add(departmentDataInsert.getId());
						relatedEntitsSet.put(relatedEntityType, existDepartmentSet);
					} else {
						departmentDataInsert = new DepartmentData();
						throw new BusinessException("error_departmentIsExist");
					}
				}
				DepartmentData newDepartmentData = new DepartmentData();
				newDepartmentData.setArabicName(departmentDataInsert.getArabicName());
				newDepartmentData.setId(departmentDataInsert.getId());
				newDepartmentData.setCode(departmentDataInsert.getCode());
				newDepartmentData.setSelected(true);
				departmentDataList.add(newDepartmentData);
				departmentDataInsert = new DepartmentData();
			} else if (relatedEntityType.equals(InfoRelatedEntityTypeEnum.COUNTRY.getCode())) {
				if (!relatedEntitsSet.containsKey(relatedEntityType)) {
					Set<Long> newObjectSet = new HashSet<Long>();
					newObjectSet.add(countryDataInsert.getId());
					relatedEntitsSet.put(relatedEntityType, newObjectSet);
				} else {
					Set<Long> existObjectSet = relatedEntitsSet.get(relatedEntityType);
					if (!existObjectSet.contains(countryDataInsert.getId())) {
						existObjectSet.add(countryDataInsert.getId());
						relatedEntitsSet.put(relatedEntityType, existObjectSet);
					} else {
						countryDataInsert = new CountryData();
						throw new BusinessException("error_countryIsExist");
					}
				}
				CountryData newCountryData = new CountryData();
				newCountryData.setId(countryDataInsert.getId());
				newCountryData.setArabicName(countryDataInsert.getArabicName());
				newCountryData.setSelected(true);
				countryDataList.add(newCountryData);
				countryDataInsert = new CountryData();
			} else if (relatedEntityType.equals(InfoRelatedEntityTypeEnum.DOMAIN.getCode())) {
				if (!relatedEntitsSet.containsKey(relatedEntityType)) {
					Set<Long> newObjectSet = new HashSet<Long>();
					newObjectSet.add(setupDomainInsert.getId());
					relatedEntitsSet.put(relatedEntityType, newObjectSet);
				} else {
					Set<Long> existObjectSet = relatedEntitsSet.get(relatedEntityType);
					if (!existObjectSet.contains(setupDomainInsert.getId())) {
						existObjectSet.add(setupDomainInsert.getId());
						relatedEntitsSet.put(relatedEntityType, existObjectSet);
					} else {
						setupDomainInsert = new SetupDomain();
						throw new BusinessException("error_domainIsExist");
					}
				}
				SetupDomain newSetupDomain = new SetupDomain();
				newSetupDomain.setId(setupDomainInsert.getId());
				newSetupDomain.setDescription(setupDomainInsert.getDescription());
				newSetupDomain.setSelected(true);
				setupDomainList.add(newSetupDomain);
				setupDomainInsert = new SetupDomain();
			} else if (relatedEntityType.equals(InfoRelatedEntityTypeEnum.EMPLOYEE.getCode())) {
				if (!relatedEntitsSet.containsKey(relatedEntityType)) {
					Set<Long> newObjectSet = new HashSet<Long>();
					newObjectSet.add(employeeDataInsert.getEmpId());
					relatedEntitsSet.put(relatedEntityType, newObjectSet);
				} else {
					Set<Long> existObjectSet = relatedEntitsSet.get(relatedEntityType);
					if (!existObjectSet.contains(employeeDataInsert.getEmpId())) {
						existObjectSet.add(employeeDataInsert.getEmpId());
						relatedEntitsSet.put(relatedEntityType, existObjectSet);
					} else {
						employeeDataInsert = new EmployeeData();
						throw new BusinessException("error_employeeIsExist");
					}
				}
				EmployeeData newEmployeeData = new EmployeeData();
				newEmployeeData.setEmpId(employeeDataInsert.getEmpId());
				newEmployeeData.setFullName(employeeDataInsert.getFullName());
				newEmployeeData.setSocialID(employeeDataInsert.getSocialID());
				newEmployeeData.setSelected(true);
				employeeDataList.add(newEmployeeData);
				employeeDataInsert = new EmployeeData();
			} else if (relatedEntityType.equals(InfoRelatedEntityTypeEnum.NON_EMPLOYEE.getCode())) {
				if (!relatedEntitsSet.containsKey(relatedEntityType)) {
					Set<Long> newObjectSet = new HashSet<Long>();
					newObjectSet.add(nonEmployeeDataInsert.getId());
					relatedEntitsSet.put(relatedEntityType, newObjectSet);
				} else {
					Set<Long> existObjectSet = relatedEntitsSet.get(relatedEntityType);
					if (!existObjectSet.contains(nonEmployeeDataInsert.getId())) {
						existObjectSet.add(nonEmployeeDataInsert.getId());
						relatedEntitsSet.put(relatedEntityType, existObjectSet);
					} else {
						nonEmployeeDataInsert = new NonEmployeeData();
						throw new BusinessException("error_nonEmployeeIsExist");
					}
				}
				NonEmployeeData newNonEmployeeData = new NonEmployeeData();
				newNonEmployeeData.setId(nonEmployeeDataInsert.getId());
				newNonEmployeeData.setCountryArabicName(nonEmployeeDataInsert.getCountryArabicName());
				newNonEmployeeData.setIdentity(nonEmployeeDataInsert.getIdentity());
				newNonEmployeeData.setFullName(nonEmployeeDataInsert.getFullName());
				newNonEmployeeData.setDescription(nonEmployeeDataInsert.getDescription());
				newNonEmployeeData.setSelected(true);
				nonEmployeesDataList.add(newNonEmployeeData);
				nonEmployeeDataInsert = new NonEmployeeData();
			}
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * requester save function
	 */
	public void save() {
		try {
			InfoService.saveInfoWithDetails(infoRequest, assignmentList, openSourceList, infoPhoneList, infoCoordinateList, setupDomainList, departmentDataList, employeeDataList, countryDataList, nonEmployeesDataList, loginEmpData);
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return;
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return;
		  }
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_infoSuccessRegestration", new Object[] { infoRequest.getInfoNumber() }));
		getRequest().setAttribute("mode", infoRequest.getId());
		infoInitilize();
	}

	/**
	 * requester save and send to init workflow
	 * 
	 * @return
	 */
	public String saveAndSend() {
		try {
			if (infoRequest.getwFInstanceId() == null) {
				InfoWorkFlow.initInfo(infoRequest, attachments, loginEmpData, assignmentList, openSourceList, infoPhoneList, infoCoordinateList, setupDomainList, departmentDataList, employeeDataList, countryDataList, nonEmployeesDataList);
			} else {
				InfoWorkFlow.resendInfo(currentTask, infoRequest, attachments, loginEmpData, assignmentList, openSourceList, infoPhoneList, infoCoordinateList, setupDomainList, departmentDataList, employeeDataList, countryDataList, nonEmployeesDataList);
			}
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_infoSuccessRegAndSend", new Object[] { infoRequest.getInfoNumber() }));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * reject
	 * 
	 * @return
	 */
	public String doReject() {
		try {
			InfoWorkFlow.doReject(currentTask, infoRequest, loginEmpData, null);
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Intelligence Manager Department WorkFlow action
	 * 
	 * @return
	 */
	public String doInfoDm() {
		try {
			if (infoRequest.getInfoEvaluationId() == null) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_infoEvaluationIsMandatory"));
				return null;
			}
			InfoWorkFlow.doInfoDM(currentTask, infoRequest, null, isSaving ? WFTaskActionsEnum.SAVE : WFTaskActionsEnum.SEND, loginEmpData);
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * doInfoRegionIntelligenceSecuirtyManager
	 * 
	 * @return
	 */
	public String doInfoRegionIntelligenceSecuirtyManager() {
		try {
			InfoWorkFlow.doInfoRegionIntelligenceSecuirtyManager(currentTask, infoRequest, null, isSaving ? WFTaskActionsEnum.SAVE : WFTaskActionsEnum.SEND, loginEmpData);
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * General Intelligence Manager / Region Director WorkFlow actions
	 * 
	 * @return
	 */
	public void doInfoSm() {
		try {
			List<DepartmentData> notifiedList = new ArrayList<DepartmentData>();
			List<DepartmentData> processingList = new ArrayList<DepartmentData>();
			for (Map.Entry<DepartmentData, Boolean> entry : notifiedMap.entrySet()) {
				if (entry.getValue()) {
					notifiedList.add(entry.getKey());
				}
			}
			if (isAnalysis) {
				for (Map.Entry<DepartmentData, Boolean> entry : processingMap.entrySet()) {
					if (entry.getValue()) {
						processingList.add(entry.getKey());
					}
				}

				if (!processingList.isEmpty()) {
					InfoWorkFlow.doInfoSM(currentTask, infoRequest, processingList, notifiedList, null, null, null, WFTaskActionsEnum.REQUEST_ANALYSIS, loginEmpData);
				} else {
					isAnalysis = false;
					return;
				}
			} else {
				isFinalSend = true;
				String letterCopies = "";
				if (internalMinistry != null && internalMinistry) {
					if (directorateDepId.equals(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId())) {
						letterCopies += getParameterizedMessage("label_internalMinistry");
					} else {
						letterCopies += getParameterizedMessage("label_regionPrincedom");
					}
				}
				if (secuirtySection != null && secuirtySection) {
					for (int i = 0; i < selectedSecurityDomains.length; i++) {
						letterCopies += "," + selectedSecurityDomains[i];
					}
				}
				if (internalDepartment != null && internalDepartment) {
					if (selectedDirDepartment == null || selectedDirDepartment.trim().isEmpty() || selectedDirDepartment.equals("")) {
						throw new BusinessException("error_internalDepartmentMandatory");
					}
					letterCopies += "," + selectedDirDepartment;
				}
				infoRequest.setLetterCopies(letterCopies);
				List<Long> visibleDepartments = new ArrayList<Long>();
				for (int i = 0; i < selectedRegions.length; i++) {
					visibleDepartments.add(new Long(selectedRegions[i]));
				}
				if (selectedOption == null) {
					throw new BusinessException("error_mandatory");
				}

				InfoWorkFlow.doInfoSM(currentTask, infoRequest, processingList, notifiedList, infoRecommendationList, visibleDepartments, null, selectedOption == 0 ? WFTaskActionsEnum.SEND : WFTaskActionsEnum.SAVE, loginEmpData);
			}
		} catch (BusinessException e) {
			errorMessage = getParameterizedMessage(e.getMessage(), e.getParams());
			super.setServerSideErrorMessages(errorMessage);
			return;
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return;
		  }
		isSaveSM = true;
	}

	public String doInfoSMRedirect() {
		if (isSaveSM) {
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
			return NavigationEnum.INBOX.toString();
		} else {
			this.setServerSideSuccessMessages(errorMessage);
			return null;
		}
	}

	/**
	 * when select no in confirm dialog
	 */
	public void confirmDailog() {
		isAnalysis = true;
	}

	/**
	 * delete Department
	 * 
	 * @param departmentData
	 */
	public void deleteDepartment(DepartmentData departmentData) {
		try {
			if (!departmentData.getSelected()) {
				InfoService.deleteInfoRelate(departmentData.getId(), infoRequest.getId(), InfoRelatedEntityTypeEnum.DEPARTMENT, loginEmpData);
			}
			if (relatedEntitsSet.containsKey(InfoRelatedEntityTypeEnum.DEPARTMENT.getCode())) {
				relatedEntitsSet.get(InfoRelatedEntityTypeEnum.DEPARTMENT.getCode()).remove(departmentData.getId());
			}
			departmentDataList.remove(departmentData);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * delete Country
	 * 
	 * @param countryData
	 */
	public void deleteCountry(CountryData countryData) {
		try {
			if (!countryData.getSelected()) {
				InfoService.deleteInfoRelate(countryData.getId(), infoRequest.getId(), InfoRelatedEntityTypeEnum.COUNTRY, loginEmpData);
			}
			if (relatedEntitsSet.containsKey(InfoRelatedEntityTypeEnum.COUNTRY.getCode())) {
				relatedEntitsSet.get(InfoRelatedEntityTypeEnum.COUNTRY.getCode()).remove(countryData.getId());
			}
			countryDataList.remove(countryData);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * delete Employee
	 * 
	 * @param empData
	 */
	public void deleteEmployee(EmployeeData empData) {
		try {
			if (!empData.getSelected()) {
				InfoService.deleteInfoRelate(empData.getEmpId(), infoRequest.getId(), InfoRelatedEntityTypeEnum.EMPLOYEE, loginEmpData);
			}
			if (relatedEntitsSet.containsKey(InfoRelatedEntityTypeEnum.EMPLOYEE.getCode())) {
				relatedEntitsSet.get(InfoRelatedEntityTypeEnum.EMPLOYEE.getCode()).remove(empData.getEmpId());
			}
			employeeDataList.remove(empData);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * delete Domain
	 * 
	 * @param domain
	 */
	public void deleteDomain(SetupDomain domain) {
		try {
			if (!domain.getSelected()) {
				InfoService.deleteInfoRelate(domain.getId(), infoRequest.getId(), InfoRelatedEntityTypeEnum.DOMAIN, loginEmpData);
			}
			if (relatedEntitsSet.containsKey(InfoRelatedEntityTypeEnum.DOMAIN.getCode())) {
				relatedEntitsSet.get(InfoRelatedEntityTypeEnum.DOMAIN.getCode()).remove(domain.getId());
			}
			setupDomainList.remove(domain);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * delete Non Employee
	 * 
	 * @param nonEmpData
	 */
	public void deleteNonEmployee(NonEmployeeData nonEmpData) {
		try {
			if (!nonEmpData.getSelected()) {
				InfoService.deleteInfoRelate(nonEmpData.getId(), infoRequest.getId(), InfoRelatedEntityTypeEnum.NON_EMPLOYEE, loginEmpData);
			}
			if (relatedEntitsSet.containsKey(InfoRelatedEntityTypeEnum.NON_EMPLOYEE.getCode())) {
				relatedEntitsSet.get(InfoRelatedEntityTypeEnum.NON_EMPLOYEE.getCode()).remove(nonEmpData.getId());
			}
			nonEmployeesDataList.remove(nonEmpData);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * add Info Analysis
	 */
	public void addInfoAnalysis() {
		InfoAnalysisDetailData newInfoAnalysisDetailData = new InfoAnalysisDetailData();
		newInfoAnalysisDetailData.setInfoAnalysisId(infoAnalysis.getId());
		infoAnalysisDetailDataList.add(newInfoAnalysisDetailData);
	}

	/**
	 * add New Classification
	 * 
	 * @param index
	 * @param classificationTypeId
	 */
	public void addNewClassification(Integer index, Long classificationTypeId) {
		try {
			List<Classification> classification = ClassificationService.getClassifications(classificationTypeId, null);
			classificationsMap.put(index, classification);
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * delete Info Analysis
	 * 
	 * @param deletedInfoAnalysis
	 */
	public void deleteInfoAnalysis(InfoAnalysisDetailData deletedInfoAnalysis) {
		infoAnalysisDetailDataList.remove(deletedInfoAnalysis);
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
	}

	/**
	 * Analysis Department Manager WorkFlow Actions
	 * 
	 * @return
	 */
	public String doInfoAnalysis() {
		try {
			List<InfoAnalysisReferenceData> infoAnalysisRefernceDataList = new ArrayList<InfoAnalysisReferenceData>();
			for (int i = 0; i < selectedAnalysisDomains.length; i++) {
				InfoAnalysisReferenceData infoAnalysisReferenceData = new InfoAnalysisReferenceData();
				infoAnalysisReferenceData.setInfoAnalysisId(infoAnalysis.getId());
				infoAnalysisReferenceData.setDomainReferenceId(selectedAnalysisDomains[i]);
				infoAnalysisRefernceDataList.add(infoAnalysisReferenceData);
			}
			InfoWorkFlow.doInfoAnalysis(currentTask, infoRequest, infoAnalysis, infoAnalysisDetailDataList, infoAnalysisRefernceDataList, infoPhoneList, infoCoordinateList, setupDomainList, departmentDataList, employeeDataList, countryDataList, nonEmployeesDataList, null, WFTaskActionsEnum.SEND, loginEmpData);
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
				return null;

		  }
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * show Info Analysis Details
	 * 
	 * @param infoAnalysisId
	 */
	public void showInfoAnalysisDetails(Long infoAnalysisId) {
		try {
			infoAnalysisDetailDataList = InfoService.getInfoAnalysisDetails(infoAnalysisId);
			analysisRefenceList = InfoService.getInfoAnalysisReferences(infoAnalysisId);
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * General Intelligence Manager / Region Director after analysis WorkFlow
	 * 
	 * @return
	 */
	public String doInfoAnalysisReview() {
		try {
			String letterCopies = "";
			if (internalMinistry != null && internalMinistry)
				letterCopies += getParameterizedMessage("label_internalMinistry");
			if (secuirtySection != null && secuirtySection) {
				for (int i = 0; i < selectedSecurityDomains.length; i++) {
					letterCopies += "," + selectedSecurityDomains[i];
				}
			}
			if (internalDepartment != null && internalDepartment) {
				if (selectedDirDepartment == null || selectedDirDepartment.trim().isEmpty() || selectedDirDepartment.equals("")) {
					throw new BusinessException("error_internalDepartmentMandatory");
				}
				letterCopies += "," + selectedDirDepartment;
			}
			infoRequest.setLetterCopies(letterCopies);
			List<Long> visibleDepartments = new ArrayList<Long>();
			for (int i = 0; i < selectedRegions.length; i++) {
				visibleDepartments.add(new Long(selectedRegions[i]));
			}

			if (selectedOption == null) {
				throw new BusinessException("error_mandatory");
			}
			InfoWorkFlow.doInfoSMAnalysisReview(currentTask, infoRequest, infoRecommendationList, visibleDepartments, null, selectedOption == 0 ? WFTaskActionsEnum.SEND : WFTaskActionsEnum.SAVE, loginEmpData);
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * notified
	 * 
	 * @return
	 */
	public String doNotify() {
		try {
			InfoWorkFlow.doNotify(currentTask);
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * handleTransferedList
	 * 
	 * @param transferDep
	 * @param type
	 */
	public void handleTransferedList(DepartmentData transferDep, Integer type) {
		if (type == 1) {
			if (processingMap.get(transferDep)) {
				transferedProcessingList.add(transferDep.getArabicName());
			} else {
				transferedProcessingList.remove(transferDep.getArabicName());
			}
		} else {
			if (notifiedMap.get(transferDep)) {
				transferedNotifiedList.add(transferDep.getArabicName());
			} else {
				transferedNotifiedList.remove(transferDep.getArabicName());
			}
		}

	}

	/**
	 * print information with details
	 */
	public void print() {
		try {
			byte[] bytes = InfoService.getInformationDetailsBytes(infoRequest.getId(), loginEmpData.getFullName(), infoAnalysisList != null && !infoAnalysisList.isEmpty());
			super.print(bytes, "InformationReport");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Prepare Upload Parameters
	 * 
	 * @param infoId
	 */
	public void getUploadParam(long infoId) {
		try {
			String requestURL = getRequest().getRequestURL() + "";
			String url = requestURL.replace(getRequest().getServletPath(), "");
			String serviceURL = url + AttachmentServiceCallBack.SERVICE_URL;

			fileArchivingParam = EncryptionUtil.encryptSymmetrically("code=" + ServiceCodesEnum.INFO_SYS.getCode() + "&token=" + EntityNameEnum.INFO.getCode() + "_" + infoId + "&serviceurl=" + serviceURL);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Prepare Download parameters
	 * 
	 * @param attachmentId
	 */
	public void getDownloadParam(String attachmentId) {
		try {
			downloadFileParamId = EncryptionUtil.encryptSymmetrically("contentId=" + attachmentId);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
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
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Get Attachments Ids and prepare for downloading
	 * 
	 * @throws BusinessException
	 */
	public void getDownloadedAttachmentsIdList(long infoId) throws BusinessException {
		attachmentList = AttachmentService.getAttachments(EntityNameEnum.INFO.getCode(), infoId);
		openDownloadPopupFlag = false;
		openDownloadDialogueFlag = false;
		if (attachmentList.isEmpty()) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_noAttachments"));
		} else {
			openDownloadDialogueFlag = true;
		}
	}

	/**
	 * get Region sectors by RegionId
	 */
	public void getRegionSectors() {
		try {
			infoRequest.setSectorId(null);
			if (infoRequest.getRegionId() != null) {
				sectors = DepartmentService.getDepartmentsByRegionIdAndType(infoRequest.getRegionId(), null, null, DepartmentTypeEnum.SECTOR.getCode());
				sectorIds = "";
				for (DepartmentData sector : sectors) {
					sectorIds += sector.getId() + ",";
				}
			} else {
				sectors = new ArrayList<DepartmentData>();
				sectorIds = "";
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/********************************************************** Setters And Getters **********************************************/
	public InfoData getInfoRequest() {
		return infoRequest;
	}

	public void setInfoRequest(InfoData infoRequest) {
		this.infoRequest = infoRequest;
	}

	public List<DepartmentData> getRegions() {
		System.out.print("");
		return regions;
	}

	public void setRegions(List<DepartmentData> regions) {
		this.regions = regions;
	}

	public List<DepartmentData> getSectors() {
		return sectors;
	}

	public void setSectors(List<DepartmentData> sectors) {
		this.sectors = sectors;
	}

	public Integer getCreateInfoType() {
		return createInfoType;
	}

	public void setCreateInfoType(Integer createInfoType) {
		this.createInfoType = createInfoType;
	}

	public Long getInfoTypeId() {
		return infoTypeId;
	}

	public void setInfoTypeId(Long infoTypeId) {
		this.infoTypeId = infoTypeId;
	}

	public List<InfoType> getInfoTypeList() {
		return infoTypeList;
	}

	public void setInfoTypeList(List<InfoType> infoTypeList) {
		this.infoTypeList = infoTypeList;
	}

	public List<InfoSubject> getInfoSubjectList() {
		return infoSubjectList;
	}

	public void setInfoSubjectList(List<InfoSubject> infoSubjectList) {
		this.infoSubjectList = infoSubjectList;
	}

	public List<SetupDomain> getInfoImportanceList() {
		return infoImportanceList;
	}

	public void setInfoImportanceList(List<SetupDomain> infoImportanceList) {
		this.infoImportanceList = infoImportanceList;
	}

	public List<SetupDomain> getInfoConfidentialityList() {
		return infoConfidentialityList;
	}

	public void setInfoConfidentialityList(List<SetupDomain> infoConfidentialityList) {
		this.infoConfidentialityList = infoConfidentialityList;
	}

	public List<SetupDomain> getIncomingSideDomainList() {
		return incomingSideDomainList;
	}

	public void setIncomingSideDomainList(List<SetupDomain> incomingSideDomainList) {
		this.incomingSideDomainList = incomingSideDomainList;
	}

	public List<SetupDomain> getDealingTypeDomainList() {
		return dealingTypeDomainList;
	}

	public void setDealingTypeDomainList(List<SetupDomain> dealingTypeDomainList) {
		this.dealingTypeDomainList = dealingTypeDomainList;
	}

	public List<SetupDomain> getDealingLevelDomainList() {
		return dealingLevelDomainList;
	}

	public void setDealingLevelDomainList(List<SetupDomain> dealingLevelDomainList) {
		this.dealingLevelDomainList = dealingLevelDomainList;
	}

	public List<InfoCoordinate> getInfoCoordinateList() {
		return infoCoordinateList;
	}

	public void setInfoCoordinateList(List<InfoCoordinate> infoCoordinateList) {
		this.infoCoordinateList = infoCoordinateList;
	}

	public List<InfoPhone> getInfoPhoneList() {
		return infoPhoneList;
	}

	public void setInfoPhoneList(List<InfoPhone> infoPhoneList) {
		this.infoPhoneList = infoPhoneList;
	}

	public Integer getOpenSourceType() {
		return InfoSourceTypeEnum.OPEN_SOURCE.getCode();
	}

	public Integer getAssignmentSourceType() {
		return InfoSourceTypeEnum.ASSIGNMENT.getCode();
	}

	public List<OpenSource> getOpenSourceList() {
		return openSourceList;
	}

	public void setOpenSourceList(List<OpenSource> openSourceList) {
		this.openSourceList = openSourceList;
	}

	public OpenSource getOpenSourceInsert() {
		return openSourceInsert;
	}

	public void setOpenSourceInsert(OpenSource openSourceInsert) {
		this.openSourceInsert = openSourceInsert;
	}

	public List<DepartmentData> getDepartmentDataList() {
		return departmentDataList;
	}

	public void setDepartmentDataList(List<DepartmentData> departmentDataList) {
		this.departmentDataList = departmentDataList;
	}

	public DepartmentData getDepartmentDataInsert() {
		return departmentDataInsert;
	}

	public void setDepartmentDataInsert(DepartmentData departmentDataInsert) {
		this.departmentDataInsert = departmentDataInsert;
	}

	public List<SetupDomain> getSetupDomainList() {
		return setupDomainList;
	}

	public void setSetupDomainList(List<SetupDomain> setupDomainList) {
		this.setupDomainList = setupDomainList;
	}

	public SetupDomain getSetupDomainInsert() {
		return setupDomainInsert;
	}

	public void setSetupDomainInsert(SetupDomain setupDomainInsert) {
		this.setupDomainInsert = setupDomainInsert;
	}

	public List<CountryData> getCountryDataList() {
		return countryDataList;
	}

	public void setCountryDataList(List<CountryData> countryDataList) {
		this.countryDataList = countryDataList;
	}

	public CountryData getCountryDataInsert() {
		return countryDataInsert;
	}

	public void setCountryDataInsert(CountryData countryDataInsert) {
		this.countryDataInsert = countryDataInsert;
	}

	public List<EmployeeData> getEmployeeDataList() {
		return employeeDataList;
	}

	public void setEmployeeDataList(List<EmployeeData> employeeDataList) {
		this.employeeDataList = employeeDataList;
	}

	public EmployeeData getEmployeeDataInsert() {
		return employeeDataInsert;
	}

	public void setEmployeeDataInsert(EmployeeData employeeDataInsert) {
		this.employeeDataInsert = employeeDataInsert;
	}

	public List<NonEmployeeData> getNonEmployeesDataList() {
		return nonEmployeesDataList;
	}

	public void setNonEmployeesDataList(List<NonEmployeeData> nonEmployeesDataList) {
		this.nonEmployeesDataList = nonEmployeesDataList;
	}

	public NonEmployeeData getNonEmployeeDataInsert() {
		return nonEmployeeDataInsert;
	}

	public void setNonEmployeeDataInsert(NonEmployeeData nonEmployeeDataInsert) {
		this.nonEmployeeDataInsert = nonEmployeeDataInsert;
	}

	public Integer getRelatedDepartmentType() {
		return InfoRelatedEntityTypeEnum.DEPARTMENT.getCode();
	}

	public Integer getRelatedCountryType() {
		return InfoRelatedEntityTypeEnum.COUNTRY.getCode();
	}

	public Integer getRelatedEmployeeType() {
		return InfoRelatedEntityTypeEnum.EMPLOYEE.getCode();
	}

	public Integer getRelatedSetupDomainType() {
		return InfoRelatedEntityTypeEnum.DOMAIN.getCode();
	}

	public Integer getRelatedNonEmployeeType() {
		return InfoRelatedEntityTypeEnum.NON_EMPLOYEE.getCode();
	}

	public List<AssignmentDetailData> getAssignmentList() {
		return assignmentList;
	}

	public void setAssignmentList(List<AssignmentDetailData> assignmentList) {
		this.assignmentList = assignmentList;
	}

	public AssignmentDetailData getAssignmentInsert() {
		return assignmentInsert;
	}

	public void setAssignmentInsert(AssignmentDetailData assignmentInsert) {
		this.assignmentInsert = assignmentInsert;
	}

	public String getRequesterRole() {
		return WFTaskRolesEnum.REQUESTER.getCode();
	}

	public String getGeneralIntelligenceRole() {
		return WFTaskRolesEnum.GENERAL_INTELLIGENCE_MANAGER.getCode();
	}

	public String getRegionIntelligenceSecurityRole() {
		return WFTaskRolesEnum.REGION_SECURITY_INTELLIGENCE_MANAGER.getCode();
	}

	public Boolean getIsSaving() {
		return isSaving;
	}

	public void setIsSaving(Boolean isSaving) {
		this.isSaving = isSaving;
	}

	public List<DepartmentData> getAnalysisDepartments() {
		return analysisDepartments;
	}

	public void setAnalysisDepartments(List<DepartmentData> analysisDepartments) {
		this.analysisDepartments = analysisDepartments;
	}

	public String getGeneralDirctorRole() {
		return WFTaskRolesEnum.GENERAL_DIRECTOR.getCode();
	}

	public Map<DepartmentData, Boolean> getProcessingMap() {
		return processingMap;
	}

	public void setProcessingMap(Map<DepartmentData, Boolean> processingMap) {
		this.processingMap = processingMap;
	}

	public Map<DepartmentData, Boolean> getNotifiedMap() {
		return notifiedMap;
	}

	public void setNotifiedMap(Map<DepartmentData, Boolean> notifiedMap) {
		this.notifiedMap = notifiedMap;
	}

	public Boolean getIsAnalysis() {
		return isAnalysis;
	}

	public void setIsAnalysis(Boolean isAnalysis) {
		this.isAnalysis = isAnalysis;
	}

	public List<InfoRecommendationData> getInfoRecommendationList() {
		return infoRecommendationList;
	}

	public void setInfoRecommendationList(List<InfoRecommendationData> infoRecommendationList) {
		this.infoRecommendationList = infoRecommendationList;
	}

	public Boolean getInternalDepartment() {
		return internalDepartment;
	}

	public void setInternalDepartment(Boolean internalDepartment) {
		this.internalDepartment = internalDepartment;
	}

	public Boolean getSecuirtySection() {
		return secuirtySection;
	}

	public void setSecuirtySection(Boolean secuirtySection) {
		this.secuirtySection = secuirtySection;
	}

	public Boolean getInternalMinistry() {
		return internalMinistry;
	}

	public void setInternalMinistry(Boolean internalMinistry) {
		this.internalMinistry = internalMinistry;
	}

	public Integer getSelectedOption() {
		return selectedOption;
	}

	public void setSelectedOption(Integer selectedOption) {
		this.selectedOption = selectedOption;
	}

	public String[] getSelectedSecurityDomains() {
		return selectedSecurityDomains;
	}

	public void setSelectedSecurityDomains(String[] selectedSecurityDomains) {
		this.selectedSecurityDomains = selectedSecurityDomains;
	}

	public List<SetupDomain> getSecuritySectionsDomainsList() {
		return securitySectionsDomainsList;
	}

	public void setSecuritySectionsDomainsList(List<SetupDomain> securitySectionsDomainsList) {
		this.securitySectionsDomainsList = securitySectionsDomainsList;
	}

	public List<DepartmentData> getRegionDepartments() {
		return regionDepartments;
	}

	public void setRegionDepartments(List<DepartmentData> regionDepartments) {
		this.regionDepartments = regionDepartments;
	}

	public Long[] getSelectedRegions() {
		return selectedRegions;
	}

	public void setSelectedRegions(Long[] selectedRegions) {
		this.selectedRegions = selectedRegions;
	}

	public String getSelectedDirDepartment() {
		return selectedDirDepartment;
	}

	public void setSelectedDirDepartment(String selectedDirDepartment) {
		this.selectedDirDepartment = selectedDirDepartment;
	}

	public Long getDirectorateDepId() {
		return directorateDepId;
	}

	public void setDirectorateDepId(Long directorateDepId) {
		this.directorateDepId = directorateDepId;
	}

	public List<ClassificationType> getClassificationTypesList() {
		return classificationTypesList;
	}

	public void setClassificationTypesList(List<ClassificationType> classificationTypesList) {
		this.classificationTypesList = classificationTypesList;
	}

	public String getAnlaysisMangerRole() {
		return WFTaskRolesEnum.GENERAL_INFO_ANALYSIS_DEPARTMENT_MANAGER.getCode();
	}

	public List<InfoAnalysisDetailData> getInfoAnalysisDetailDataList() {
		return infoAnalysisDetailDataList;
	}

	public void setInfoAnalysisDetailDataList(List<InfoAnalysisDetailData> infoAnalysisDetailDataList) {
		this.infoAnalysisDetailDataList = infoAnalysisDetailDataList;
	}

	public InfoAnalysis getInfoAnalysis() {
		return infoAnalysis;
	}

	public void setInfoAnalysis(InfoAnalysis infoAnalysis) {
		this.infoAnalysis = infoAnalysis;
	}

	public Map<Integer, List<Classification>> getClassificationsMap() {
		return classificationsMap;
	}

	public void setClassificationsMap(Map<Integer, List<Classification>> classificationsMap) {
		this.classificationsMap = classificationsMap;
	}

	public Long[] getSelectedAnalysisDomains() {
		return selectedAnalysisDomains;
	}

	public void setSelectedAnalysisDomains(Long[] selectedAnalysisDomains) {
		this.selectedAnalysisDomains = selectedAnalysisDomains;
	}

	public String getGeneralMangerAnalysis() {
		return WFTaskRolesEnum.GENERAL_DIRECTOR_ANALYSIS.getCode();
	}

	public List<InfoAnalysis> getInfoAnalysisList() {
		return infoAnalysisList;
	}

	public void setInfoAnalysisList(List<InfoAnalysis> infoAnalysisList) {
		this.infoAnalysisList = infoAnalysisList;
	}

	public List<InfoAnalysisReferenceData> getAnalysisRefenceList() {
		return analysisRefenceList;
	}

	public void setAnalysisRefenceList(List<InfoAnalysisReferenceData> analysisRefenceList) {
		this.analysisRefenceList = analysisRefenceList;
	}

	public String getRegionIntelligenceRole() {
		return WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER.getCode();
	}

	public String getRegionDirctorRole() {
		return WFTaskRolesEnum.REGION_DIRECTOR.getCode();
	}

	public String getRegionAnlaysisMangerRole() {
		return WFTaskRolesEnum.REGION_INFO_ANALYSIS_DEPARTMENT_MANAGER.getCode();
	}

	public String getRegionMangerAnalysis() {
		return WFTaskRolesEnum.REGION_DIRECTOR_ANALYSIS.getCode();
	}

	public String getHistoryRole() {
		return WFTaskRolesEnum.HISTORY.getCode();
	}

	public String getNotificationRole() {
		return WFTaskRolesEnum.NOTIFICATION.getCode();
	}

	public String getInfoDomainPopupSearch() {
		return "" + ClassesEnum.ORGNAIZATIONS_AND_ISSUES.getCode();
	}

	public List<String> getTransferedProcessingList() {
		return transferedProcessingList;
	}

	public void setTransferedProcessingList(List<String> transferedProcessingList) {
		this.transferedProcessingList = transferedProcessingList;
	}

	public List<String> getTransferedNotifiedList() {
		return transferedNotifiedList;
	}

	public void setTransferedNotifiedList(List<String> transferedNotifiedList) {
		this.transferedNotifiedList = transferedNotifiedList;
	}

	public String getSectorIds() {
		return sectorIds;
	}

	public void setSectorIds(String sectorIds) {
		this.sectorIds = sectorIds;
	}

	public String getFileArchivingParam() {
		return fileArchivingParam;
	}

	public void setFileArchivingParam(String fileArchivingParam) {
		this.fileArchivingParam = fileArchivingParam;
	}

	public List<Attachment> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<Attachment> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public String getSelectedDownloadFileId() {
		return selectedDownloadFileId;
	}

	public void setSelectedDownloadFileId(String selectedDownloadFileId) {
		this.selectedDownloadFileId = selectedDownloadFileId;
	}

	public boolean isOpenDownloadPopupFlag() {
		return openDownloadPopupFlag;
	}

	public void setOpenDownloadPopupFlag(boolean openDownloadPopupFlag) {
		this.openDownloadPopupFlag = openDownloadPopupFlag;
	}

	public String getDownloadFileParamId() {
		return downloadFileParamId;
	}

	public void setDownloadFileParamId(String downloadFileParamId) {
		this.downloadFileParamId = downloadFileParamId;
	}

	public boolean isOpenDownloadDialogueFlag() {
		return openDownloadDialogueFlag;
	}

	public void setOpenDownloadDialogueFlag(boolean openDownloadDialogueFlag) {
		this.openDownloadDialogueFlag = openDownloadDialogueFlag;
	}

	public String getBoolServerUploadPath() {
		return InfoSysConfigurationService.getBoolServerUploadPath();
	}

	public String getBoolServerDownloadPath() {
		return InfoSysConfigurationService.getBoolServerDownloadPath();
	}

	public String getBoolServerDeletePath() {
		return InfoSysConfigurationService.getBoolServerDeletePath();
	}

	public boolean isFinalSend() {
		return isFinalSend;
	}

	public void setFinalSend(boolean isFinalSend) {
		this.isFinalSend = isFinalSend;
	}

	public Integer getAssignment() {
		return InfoSourceTypeEnum.ASSIGNMENT.getCode();
	}

	public Long isHeadQuarter() {
		try {
			return DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRegisteration.class, e, "InfoRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
	}

	public Integer getInfoImportanceEnumImmediate() {
		return InfoImportanceEnum.IMMEDIATE.getCode();
	}

	public Integer getInfoImportanceEnumUrgent() {
		return InfoImportanceEnum.URGENT.getCode();
	}

	public Integer getInfoImportanceEnumMedium() {
		return InfoImportanceEnum.MEDIUM.getCode();
	}

	public Integer getInfoImportanceEnumNormal() {
		return InfoImportanceEnum.NORMAL.getCode();
	}

	public Integer getInfoImportanceEnumWithout() {
		return InfoImportanceEnum.WITHOUT.getCode();
	}

	public Integer getInfoConfidentialityEnumForbidden() {
		return InfoConfidentialityEnum.FORBIDDEN.getCode();
	}

	public Integer getInfoConfidentialityEnumVerySecret() {
		return InfoConfidentialityEnum.VERY_SECRET.getCode();
	}

	public Integer getInfoConfidentialityEnumSecret() {
		return InfoConfidentialityEnum.SECRET.getCode();
	}

	public Integer getInfoConfidentialityEnumWithout() {
		return InfoConfidentialityEnum.WITHOUT.getCode();
	}

	public List<SetupDomain> getInfoEvaluations() {
		return infoEvaluations;
	}

	public void setInfoEvaluations(List<SetupDomain> infoEvaluations) {
		this.infoEvaluations = infoEvaluations;
	}
}
