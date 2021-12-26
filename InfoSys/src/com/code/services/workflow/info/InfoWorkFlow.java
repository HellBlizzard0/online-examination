package com.code.services.workflow.info;

import java.util.Date;
import java.util.List;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.assignment.AssignmentDetailData;
import com.code.dal.orm.info.InfoAnalysis;
import com.code.dal.orm.info.InfoAnalysisDetailData;
import com.code.dal.orm.info.InfoAnalysisReferenceData;
import com.code.dal.orm.info.InfoCoordinate;
import com.code.dal.orm.info.InfoData;
import com.code.dal.orm.info.InfoPhone;
import com.code.dal.orm.info.InfoRecommendationData;
import com.code.dal.orm.info.OpenSource;
import com.code.dal.orm.setup.CountryData;
import com.code.dal.orm.setup.DepartmentData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.setup.NonEmployeeData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.dal.orm.workflow.WFInstance;
import com.code.dal.orm.workflow.WFInstanceData;
import com.code.dal.orm.workflow.WFTask;
import com.code.enums.InfoStatusEnum;
import com.code.enums.WFPositionsEnum;
import com.code.enums.WFTaskActionsEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.enums.WFTaskUrlEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.NoDataException;
import com.code.services.infosys.info.InfoService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.EmployeeService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.BaseWorkFlow;
import com.code.services.workflow.WFPositionService;

/**
 * Head for switching workflow for HQ or Region
 * 
 * @author aismail
 * 
 */
public class InfoWorkFlow extends BaseWorkFlow {

	/**
	 * Constructor
	 */
	public InfoWorkFlow() {
	}

	/**
	 * start info WorkFlow
	 * 
	 * @param infoRequest
	 * @param attachments
	 * @param emp
	 * @param assignmentList
	 * @param openSourceList
	 * @param infoPhoneList
	 * @param infoCoordinateList
	 * @param domainsList
	 * @param departmentList
	 * @param employeeList
	 * @param countryList
	 * @param nonEmployeeList
	 * @throws BusinessException
	 */
	public static void initInfo(InfoData infoRequest, String attachments, EmployeeData emp, List<AssignmentDetailData> assignmentList, List<OpenSource> openSourceList, List<InfoPhone> infoPhoneList, List<InfoCoordinate> infoCoordinateList, List<SetupDomain> domainsList, List<DepartmentData> departmentList, List<EmployeeData> employeeList, List<CountryData> countryList, List<NonEmployeeData> nonEmployeeList) throws BusinessException {
		// Can't start workflow unless at least on related entity is saved
		CustomSession session = DataAccess.getSession();

		boolean isSave = false;
		try {
			session.beginTransaction();
			if (infoRequest.getId() == null) {
				isSave = true;
			}
			if (domainsList.isEmpty() && departmentList.isEmpty() && employeeList.isEmpty() && countryList.isEmpty() && nonEmployeeList.isEmpty())
			{
				throw new BusinessException("error_relatedEntitieIsMandatory");
			}
			if(assignmentList.isEmpty() && openSourceList.isEmpty() && infoPhoneList.isEmpty() && infoCoordinateList.isEmpty()){
				throw new BusinessException("error_extraInfoIsMandatory");
			}
			InfoService.saveInfoWithDetails(infoRequest, assignmentList, openSourceList, infoPhoneList, infoCoordinateList, domainsList, departmentList, employeeList, countryList, nonEmployeeList, emp, session);
			Long regionId = DepartmentService.isRegionDepartment(emp.getActualDepartmentId());
			if (regionId != null)
				InfoRegionWorkFlow.initInfo(infoRequest, regionId, attachments, emp, session);
			else
				InfoHQWorkFlow.initInfo(infoRequest, attachments, emp, session);

			session.commitTransaction();
		} catch (Exception e) {
			session.rollbackTransaction();

			infoRequest.setwFInstanceId(null);
			if (isSave) {
				infoRequest.setId(null);
			}
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoWorkFlow.class, e, "InfoWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			session.close();
		}
	}

	/**
	 * resendInfo
	 * 
	 * @param infoRequest
	 * @param attachments
	 * @param emp
	 * @param assignmentList
	 * @param openSourceList
	 * @param infoPhoneList
	 * @param infoCoordinateList
	 * @param domainsList
	 * @param departmentList
	 * @param employeeList
	 * @param countryList
	 * @param nonEmployeeList
	 * @throws BusinessException
	 */
	public static void resendInfo(WFTask dmTask, InfoData infoRequest, String attachments, EmployeeData emp, List<AssignmentDetailData> assignmentList, List<OpenSource> openSourceList, List<InfoPhone> infoPhoneList, List<InfoCoordinate> infoCoordinateList, List<SetupDomain> domainsList, List<DepartmentData> departmentList, List<EmployeeData> employeeList, List<CountryData> countryList, List<NonEmployeeData> nonEmployeeList) throws BusinessException {
		CustomSession session = DataAccess.getSession();

		try {
			session.beginTransaction();

			if (domainsList.isEmpty() && departmentList.isEmpty() && employeeList.isEmpty() && countryList.isEmpty() && nonEmployeeList.isEmpty())
				throw new BusinessException("error_relatedEntitieIsMandatory");
			InfoService.saveInfoWithDetails(infoRequest, assignmentList, openSourceList, infoPhoneList, infoCoordinateList, domainsList, departmentList, employeeList, countryList, nonEmployeeList, emp, session);
			Long regionId = DepartmentService.isRegionDepartment(emp.getActualDepartmentId());
			if (regionId != null)
				InfoRegionWorkFlow.resendInfo(dmTask, infoRequest, regionId, attachments, emp, session);
			else
				InfoHQWorkFlow.resendInfo(dmTask, infoRequest, attachments, emp, session);

			session.commitTransaction();
		} catch (Exception e) {
			session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoWorkFlow.class, e, "InfoWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			session.close();
		}
	}

	/**
	 * Intelligence Manager Department WorkFlow action "Moder Edaret el este5barat" in region or HQ
	 * 
	 * @param dmTask
	 * @param infoRequest
	 * @param attachments
	 * @param isSaving
	 * @param emp
	 * @throws BusinessException
	 */
	public static void doInfoDM(WFTask dmTask, InfoData infoRequest, String attachments, WFTaskActionsEnum isSaving, EmployeeData emp) throws BusinessException {

		CustomSession session = DataAccess.getSession();
		try {
			session.beginTransaction();

			Long regionId = DepartmentService.isRegionDepartment(emp.getActualDepartmentId());
			if (regionId != null)
				InfoRegionWorkFlow.doInfoDM(dmTask, infoRequest, attachments, isSaving, regionId, emp, session);
			else
				InfoHQWorkFlow.doInfoDM(dmTask, infoRequest, attachments, isSaving, emp, session);

			session.commitTransaction();
		} catch (Exception e) {
			session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoWorkFlow.class, e, "InfoWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			session.close();
		}
	}

	/**
	 * doInfoRegionIntelligenceSecuirtyManager
	 * 
	 * @param dmTask
	 * @param infoRequest
	 * @param attachments
	 * @param isSaving
	 * @param emp
	 * @throws BusinessException
	 */
	public static void doInfoRegionIntelligenceSecuirtyManager(WFTask dmTask, InfoData infoRequest, String attachments, WFTaskActionsEnum isSaving, EmployeeData emp) throws BusinessException {

		CustomSession session = DataAccess.getSession();
		try {
			session.beginTransaction();

			Long regionId = DepartmentService.isRegionDepartment(emp.getActualDepartmentId());
			InfoRegionWorkFlow.doInfoRegionIntelligenceSecuirtyManager(dmTask, infoRequest, attachments, isSaving, regionId, emp, session);

			session.commitTransaction();
		} catch (Exception e) {
			session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoWorkFlow.class, e, "InfoWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			session.close();
		}
	}

	/**
	 * General Intelligence Manager / Region Director WorkFlow actions "Moder eledara el 3ama" in HQ or "qa2ed el manteka" in region
	 * 
	 * @param dmTask
	 * @param infoRequest
	 * @param processingList
	 * @param notifiedList
	 * @param infoRecommendationsList
	 * @param visibleDepartments
	 * @param attachments
	 * @param action
	 * @param emp
	 * @throws BusinessException
	 */
	public static void doInfoSM(WFTask dmTask, InfoData infoRequest, List<DepartmentData> processingList, List<DepartmentData> notifiedList, List<InfoRecommendationData> infoRecommendationsList, List<Long> visibleDepartments, String attachments, WFTaskActionsEnum action, EmployeeData emp) throws BusinessException {
		CustomSession session = DataAccess.getSession();
		try {
			session.beginTransaction();

			if (action.equals(WFTaskActionsEnum.SEND)) {
				if (infoRequest.getLetterCopies() == null || infoRequest.getLetterCopies().trim().isEmpty()) {
					throw new BusinessException("error_mandatory");
				}
			}

			Long regionId = DepartmentService.isRegionDepartment(emp.getActualDepartmentId());

			/*
			 * if (regionId == null) {
			 * 
			 * List<DepartmentData> modifiedProcessingList = new ArrayList<DepartmentData>(); List<DepartmentData> modifiedNotifiedList = new ArrayList<DepartmentData>(); for (DepartmentData departmentData : processingList) { if (departmentData.getDepartmentTypeId().equals(DepartmentTypeEnum.REGION.getCode())) { modifiedProcessingList.add(getRegionDepartment(departmentData.getId())); } else { modifiedProcessingList.add(departmentData); } } for (DepartmentData departmentData : notifiedList) { if
			 * (departmentData.getDepartmentTypeId().equals(DepartmentTypeEnum.REGION.getCode())) { modifiedNotifiedList.add(getRegionDepartment(departmentData.getId())); } else { modifiedNotifiedList.add(departmentData); } } processingList = new ArrayList<DepartmentData>(); processingList.addAll(modifiedProcessingList); notifiedList = new ArrayList<DepartmentData>(); notifiedList.addAll(modifiedNotifiedList); }
			 */

			InfoService.saveInfoAnalysis(processingList, notifiedList, infoRequest.getId(), emp, session);
			if (visibleDepartments != null)
				InfoService.saveInfoVisibleDepartments(infoRequest.getId(), visibleDepartments, emp, session);
			if (infoRecommendationsList != null)
				InfoService.saveInfoRecommendations(infoRecommendationsList, emp, session);
			if (action.equals(WFTaskActionsEnum.REQUEST_ANALYSIS))
				infoRequest.setStatus(InfoStatusEnum.PROCESSING.getCode());
			else
				infoRequest.setStatus(InfoStatusEnum.DONE.getCode());

			InfoService.updateInfo(infoRequest, emp, session);

			if (regionId != null)
				InfoRegionWorkFlow.doInfoSM(dmTask, infoRequest, regionId, processingList, notifiedList, infoRecommendationsList, attachments, action, session);
			else
				InfoHQWorkFlow.doInfoSM(dmTask, infoRequest, processingList, notifiedList, infoRecommendationsList, attachments, action, session);

			session.commitTransaction();
		} catch (Exception e) {
			session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoWorkFlow.class, e, "InfoWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			session.close();
		}
	}

	/**
	 * Analysis Department Manager WorkFlow Actions
	 * 
	 * @param dmTask
	 * @param infoRequest
	 * @param infoAnalysis
	 * @param infoAnalysisDetailDataList
	 * @param infoAnalysisReferenceDataList
	 * @param infoPhoneList
	 * @param infoCoordinateList
	 * @param domainsList
	 * @param departmentList
	 * @param employeeList
	 * @param countryList
	 * @param nonEmployeeList
	 * @param attachments
	 * @param action
	 * @param emp
	 * @throws BusinessException
	 */
	public static void doInfoAnalysis(WFTask dmTask, InfoData infoRequest, InfoAnalysis infoAnalysis, List<InfoAnalysisDetailData> infoAnalysisDetailDataList, List<InfoAnalysisReferenceData> infoAnalysisReferenceDataList, List<InfoPhone> infoPhoneList, List<InfoCoordinate> infoCoordinateList, List<SetupDomain> domainsList, List<DepartmentData> departmentList, List<EmployeeData> employeeList, List<CountryData> countryList, List<NonEmployeeData> nonEmployeeList, String attachments,
			WFTaskActionsEnum action, EmployeeData emp) throws BusinessException {
		CustomSession session = DataAccess.getSession();
		try {
			session.beginTransaction();
			if (infoAnalysisDetailDataList.isEmpty()) {
				throw new BusinessException("error_analysisDetailsMandatory");
			} else {
				for (InfoAnalysisDetailData analysisData : infoAnalysisDetailDataList) {
					if (analysisData.getClassificationId() == null || analysisData.getClassificationTypeId() == null) {
						throw new BusinessException("error_analysisDetailsMandatory");
					}
				}
			}
			if (infoAnalysisReferenceDataList.isEmpty()) {
				throw new BusinessException("error_analysisReferenceMandatory");
			}
			if (infoAnalysis.getConclusion() == null || infoAnalysis.getConclusion().trim().isEmpty()) {
				throw new BusinessException("error_conclusionMandatory");
			}
			infoRequest.setStatus(InfoStatusEnum.DONE_PROCESSING.getCode());
			InfoService.saveInfoWithDetails(infoRequest, null, null, infoPhoneList, infoCoordinateList, domainsList, departmentList, employeeList, countryList, nonEmployeeList, emp, session);
			InfoService.saveInfoAnalysisDetailData(infoAnalysisDetailDataList, emp, session);
			InfoService.saveInfoAnalysisReferenceData(infoAnalysisReferenceDataList, emp, session);
			infoAnalysis.setAnalysisDate(HijriDateService.getHijriSysDate());
			InfoService.updateInfoAnalysis(infoAnalysis, emp, session);
			WFInstanceData instance = getWFInstanceDataById(dmTask.getInstanceId());
			Long department = DepartmentService.getDepartment(EmployeeService.getEmployee(instance.getRequesterId()).getActualDepartmentId()).getId();
			Long regionId = DepartmentService.isRegionDepartment(department);
			if (regionId != null) {
				InfoRegionWorkFlow.doInfoAnalysis(dmTask, infoRequest, attachments, action, regionId, infoAnalysis.getDepartmentName(), session);
			} else {
				InfoHQWorkFlow.doInfoAnalysis(dmTask, infoRequest, attachments, action, infoAnalysis.getDepartmentName(), session);
			}

			session.commitTransaction();
		} catch (Exception e) {
			session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoWorkFlow.class, e, "InfoWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			session.close();
		}
	}

	/**
	 * General Intelligence Manager / Region Director after analysis WorkFlow actions
	 * 
	 * @param dmTask
	 * @param infoRequest
	 * @param infoRecommendationsList
	 * @param visibleDepartments
	 * @param attachments
	 * @param action
	 * @param emp
	 * @throws BusinessException
	 */
	public static void doInfoSMAnalysisReview(WFTask dmTask, InfoData infoRequest, List<InfoRecommendationData> infoRecommendationsList, List<Long> visibleDepartments, String attachments, WFTaskActionsEnum action, EmployeeData emp) throws BusinessException {
		CustomSession session = DataAccess.getSession();
		try {
			session.beginTransaction();

			if (action.equals(WFTaskActionsEnum.SEND)) {
				if (infoRequest.getLetterCopies() == null || infoRequest.getLetterCopies().trim().isEmpty()) {
					throw new BusinessException("error_mandatory");
				}
			}

			InfoService.saveInfoRecommendations(infoRecommendationsList, emp, session);
			InfoService.saveInfoVisibleDepartments(infoRequest.getId(), visibleDepartments, emp, session);
			infoRequest.setStatus(InfoStatusEnum.DONE.getCode());
			InfoService.updateInfo(infoRequest, emp, session);
			Long regionId = DepartmentService.isRegionDepartment(emp.getActualDepartmentId());
			if (regionId != null)
				InfoRegionWorkFlow.doInfoSMAnalysisReview(dmTask, regionId, infoRequest, infoRecommendationsList, attachments, action, session);
			else
				InfoHQWorkFlow.doInfoSMAnalysisReview(dmTask, infoRequest, infoRecommendationsList, attachments, action, session);
			session.commitTransaction();
		} catch (Exception e) {
			session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoWorkFlow.class, e, "InfoWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			session.close();
		}
	}

	/**
	 * doReject
	 * 
	 * @param dmTask
	 * @param infoRequest
	 * @param loginUser
	 * @param attachments
	 * @throws BusinessException
	 */
	public static void doReject(WFTask dmTask, InfoData infoRequest, EmployeeData loginUser, String attachments) throws BusinessException {
		CustomSession session = DataAccess.getSession();

		try {
			session.beginTransaction();
			if (dmTask.getNotes() == null || dmTask.getNotes().isEmpty() || dmTask.getNotes().trim().equals("")) {
				throw new BusinessException("error_rejectReasonMandatory");
			}
			dmTask.setRefuseReasons(dmTask.getNotes());

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			WFInstance instance = getWFInstanceById(dmTask.getInstanceId());

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "ar", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "en", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });

			completeWFTask(dmTask, WFTaskActionsEnum.REJECT.getCode(), new Date(), hijriCurDate, instance.getInstanceId(), instance.getRequesterId(), instance.getRequesterId(), WFTaskUrlEnum.INFORMATION.getCode(), WFTaskRolesEnum.REQUESTER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);

			session.commitTransaction();
		} catch (Exception e) {
			session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoWorkFlow.class, e, "InfoWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			session.close();
		}
	}

	/**
	 * get directorate or region id depend on employee actual department
	 * 
	 * @param emp
	 * @return
	 * @throws BusinessException
	 */
	public static Long getDirectorateOrRegionId(EmployeeData emp) throws BusinessException {
		Long regionId = DepartmentService.isRegionDepartment(emp.getActualDepartmentId());
		if (regionId != null)
			return regionId;
		else
			return WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId();
	}

	/**
	 * get analysis departments used in processing and analysis
	 * 
	 * @param emp
	 * @return
	 * @throws BusinessException
	 */
	public static List<DepartmentData> getAnalysisDepartments(EmployeeData emp) throws BusinessException {
		try {
			Long regionId = DepartmentService.isRegionDepartment(emp.getActualDepartmentId());
			if (regionId != null)
				return InfoRegionWorkFlow.getAnalysisDepartments(regionId);
			else
				return InfoHQWorkFlow.getAnalysisDepartments();
		} catch (Exception e) {

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoWorkFlow.class, e, "InfoWorkFlow");
				throw new BusinessException("error_general");
			}
		}
	}

	/**
	 * set notification action
	 * 
	 * @param currentTask
	 * @throws BusinessException
	 */
	public static void doNotify(WFTask currentTask) throws BusinessException {
		try {
			setWFTaskAction(currentTask, WFTaskActionsEnum.NOTIFIED.getCode(), new Date(), HijriDateService.getHijriSysDate());
		} catch (Exception e) {
			Log4j.traceErrorException(InfoWorkFlow.class, e, "InfoWorkFlow");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * getInfoClosedData
	 * 
	 * @param info
	 * @return
	 * @throws BusinessException
	 */
	public static String getInfoClosedData(InfoData info) throws BusinessException {
		try {
			for (WFTask task : BaseWorkFlow.getWFInstanceTasks(info.getwFInstanceId())) {
				if (task.getAction() != null && (task.getAction().equals(WFTaskActionsEnum.SAVE.getCode()) || task.getAction().equals(WFTaskActionsEnum.SEND.getCode()))) {
					return HijriDateService.getHijriDateString(task.getHijriActionDate());
				}
			}
			return null;
		} catch (Exception e) {
			if (e instanceof NoDataException) {
				return null;
			} else {
				Log4j.traceErrorException(InfoWorkFlow.class, e, "InfoWorkFlow");
				throw new BusinessException("error_general");
			}
		}
	}

	/**
	 * isIntelleigenceOrAnalysisDepartment
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public static boolean isIntelleigenceOrAnalysisDepartment(long departmentId) throws BusinessException {
		try {
			Long regionId = DepartmentService.isRegionDepartment(departmentId);
			if (regionId == null) {
				if (WFPositionService.getWFPosition(WFPositionsEnum.GENERAL_INTELLIGENCE_DIRECTORATE.getCode()).getUnitId().equals(departmentId) || WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId().equals(departmentId) || WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_SECURITY_ANALYSIS_DEPARTMENT.getCode()).getUnitId().equals(departmentId)) {
					return true;
				} else {
					return false;
				}
			} else {
				if (WFPositionService.getRegionIntelligenceDepartmentId(regionId).equals(departmentId)) {
					return true;
				} 
				else if(WFPositionService.getRegionSecurityIntelligenceDepartmentId(regionId).equals(departmentId)) {
					return true;
				} 
				else {
					return false;
				}
			}
		} catch (Exception e) {
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoWorkFlow.class, e, "InfoWorkFlow");
				throw new BusinessException("error_general");
			}
		}
	}
}