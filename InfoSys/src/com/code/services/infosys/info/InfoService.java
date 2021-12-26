package com.code.services.infosys.info;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.ReportService;
import com.code.dal.orm.assignment.AssignmentDetailData;
import com.code.dal.orm.info.Info;
import com.code.dal.orm.info.InfoAnalysis;
import com.code.dal.orm.info.InfoAnalysisDetailData;
import com.code.dal.orm.info.InfoAnalysisReferenceData;
import com.code.dal.orm.info.InfoCoordinate;
import com.code.dal.orm.info.InfoData;
import com.code.dal.orm.info.InfoPhone;
import com.code.dal.orm.info.InfoRecommendationData;
import com.code.dal.orm.info.InfoRelatedEntity;
import com.code.dal.orm.info.InfoSource;
import com.code.dal.orm.info.InfoVisibleDepartment;
import com.code.dal.orm.info.OpenSource;
import com.code.dal.orm.setup.CountryData;
import com.code.dal.orm.setup.DepartmentData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.setup.NonEmployeeData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.dal.orm.workflow.WFPosition;
import com.code.enums.EntitySequenceGeneratorEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.InfoAnalysisRedirectionTypeEnum;
import com.code.enums.InfoRelatedEntityTypeEnum;
import com.code.enums.InfoSourceTypeEnum;
import com.code.enums.InfoStatusEnum;
import com.code.enums.QueryNamesEnum;
import com.code.enums.ReportNamesEnum;
import com.code.enums.WFPositionsEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseConstraintException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.infosys.assignment.AssignmentService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.CountryService;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.EmployeeService;
import com.code.services.setup.NonEmployeeService;
import com.code.services.setup.OpenSourceService;
import com.code.services.setup.SetupService;
import com.code.services.util.CommonService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.WFPositionService;
import com.code.services.workflow.info.InfoRegionWorkFlow;

public class InfoService extends BaseService {

	/**
	 * Constructor
	 */
	private InfoService() {

	}

	public static void saveInfoWithDetails(InfoData infoData, List<AssignmentDetailData> assignmentList, List<OpenSource> openSourceList, List<InfoPhone> infoPhoneList, List<InfoCoordinate> infoCoordinateList, List<SetupDomain> domainsList, List<DepartmentData> departmentList, List<EmployeeData> employeeList, List<CountryData> countryList, List<NonEmployeeData> nonEmployeeList, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		boolean isSave = false;
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			if (infoData.getId() == null) {
				saveInfo(infoData, loginEmpData, session);
				isSave = true;
			} else
				updateInfo(infoData, loginEmpData, session);

			if (assignmentList != null) {
				for (AssignmentDetailData assignment : assignmentList) {
					if (assignment.getSelected()) {
						saveInfoSource(assignment.getId(), infoData.getId(), assignment.getType(), loginEmpData, session);
					}
				}
			}

			if (openSourceList != null) {
				for (OpenSource openSource : openSourceList) {
					if (openSource.getSelected()) {
						saveInfoSource(openSource.getId(), infoData.getId(), InfoSourceTypeEnum.OPEN_SOURCE.getCode(), loginEmpData, session);
					}
				}
			}

			valdiatePhoneNumberDuplicates(infoPhoneList);
			for (InfoPhone infoPhone : infoPhoneList) {
				if (infoPhone.getSelected()) {
					infoPhone.setInfoId(infoData.getId());
					saveInfoPhones(infoPhone, loginEmpData, session);
				} else {
					updateInfoPhones(infoPhone, loginEmpData, session);
				}
			}

			for (InfoCoordinate infoCoordinate : infoCoordinateList) {
				if (infoCoordinate.getSelected()) {
					infoCoordinate.setInfoId(infoData.getId());
					saveInfoCoordinates(infoCoordinate, loginEmpData, session);
				} else {
					updateInfoCoordinates(infoCoordinate, loginEmpData, session);
				}
			}

			for (SetupDomain domain : domainsList) {
				if (domain.getSelected()) {
					saveInfoRelate(domain.getId(), infoData.getId(), InfoRelatedEntityTypeEnum.DOMAIN, loginEmpData, session);
				}
			}

			for (DepartmentData department : departmentList) {
				if (department.getSelected()) {
					saveInfoRelate(department.getId(), infoData.getId(), InfoRelatedEntityTypeEnum.DEPARTMENT, loginEmpData, session);
				}
			}
			for (EmployeeData emp : employeeList) {
				if (emp.getSelected()) {
					saveInfoRelate(emp.getEmpId(), infoData.getId(), InfoRelatedEntityTypeEnum.EMPLOYEE, loginEmpData, session);
				}
			}

			for (CountryData country : countryList) {
				if (country.getSelected()) {
					saveInfoRelate(country.getId(), infoData.getId(), InfoRelatedEntityTypeEnum.COUNTRY, loginEmpData, session);
				}
			}

			for (NonEmployeeData nonEmp : nonEmployeeList) {
				if (nonEmp.getSelected()) {
					saveInfoRelate(nonEmp.getId(), infoData.getId(), InfoRelatedEntityTypeEnum.NON_EMPLOYEE, loginEmpData, session);
				}
			}

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (isSave) {
				infoData.setId(null);
			}

			if (infoPhoneList != null) {
				for (InfoPhone infoPhone : infoPhoneList) {
					if (infoPhone.getSelected()) {
						infoPhone.setId(null);
					}
				}
			}

			if (infoCoordinateList != null) {
				for (InfoCoordinate infoCoordinate : infoCoordinateList) {
					if (infoCoordinate.getSelected()) {
						infoCoordinate.setId(null);
					}
				}
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoService.class, e, "InfoService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Copy information in case manager want to work on notified info
	 * 
	 * @param infoData
	 * @return InfoData
	 * @throws BusinessException
	 */
	public static InfoData copyInfo(InfoData infoData, EmployeeData loginEmpData) throws BusinessException {
		InfoData copiedInfoData = new InfoData();
		try {
			copiedInfoData = (InfoData) infoData.clone();
			copiedInfoData.setStatus(InfoStatusEnum.REGISTERED.getCode());
			copiedInfoData.setRelatedInfoId(infoData.getId());
			copiedInfoData.setId(null);
			saveInfo(copiedInfoData, loginEmpData);
			return copiedInfoData;
		} catch (CloneNotSupportedException e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Save info data
	 * 
	 * @param infoData
	 * @throws BusinessException
	 */
	public static void saveInfo(InfoData infoData, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateInfo(infoData.getInfo());

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			infoData.setStatus(InfoStatusEnum.REGISTERED.getCode());
			infoData.getInfo().setSystemUser(loginEmpData.getEmpId().toString());
			infoData.getInfo().setInfoNumber(generateInfoNumber(loginEmpData.getActualDepartmentId(), session));
			infoData.getInfo().setRegisterationDate(HijriDateService.getHijriSysDate());

			DataAccess.addEntity(infoData.getInfo(), session);
			infoData.setId(infoData.getInfo().getId());
			infoData.setInfoNumber(infoData.getInfo().getInfoNumber());
			infoData.setRegisterationDate(infoData.getInfo().getRegisterationDate());

			List<Long> depListIds = new ArrayList<Long>();
			depListIds.add(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId());

			DepartmentData dep = DepartmentService.getDepartment(loginEmpData.getActualDepartmentId());
			if (!dep.getRegionId().equals(depListIds.get(0))) {
				depListIds.add(dep.getRegionId());
			}
			saveInfoVisibleDepartments(infoData.getInfo().getId(), depListIds, loginEmpData, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();

			}
			infoData.setId(null);

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoService.class, e, "InfoService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	private static void validateInfo(Info info) throws BusinessException {
		if (info.getReceiveDate() == null || info.getImportance() == null || info.getConfidentiality() == null) {
			throw new BusinessException("error_mandatory");
		}
		if (info.getReceiveTime() == null || info.getReceiveTime().trim().isEmpty()) {
			throw new BusinessException("error_reveiveTimeIsMandatory");
		}
		if (info.getRegionId() == null) {
			throw new BusinessException("error_regionMandatory");
		}
		if (info.getInfoSubjectId() == null) {
			throw new BusinessException("error_infoSubjectIsMandatory");
		}
		if (info.getInfoDetails() == null || info.getInfoDetails().trim().isEmpty()) {
			throw new BusinessException("error_infoDetailsIsMandatory");
		}

		if (info.getDomainIncomingSideId() != null || (info.getIncomingNumber() != null && !info.getIncomingNumber().trim().isEmpty()) || info.getIncomingDate() != null) {
			if (info.getIncomingNumber() == null || info.getIncomingNumber().trim().isEmpty()) {
				throw new BusinessException("error_incomingNumberIsMandatory");
			}
			if (info.getIncomingDate() == null) {
				throw new BusinessException("error_incomingDateIsMandatory");
			}

			if (info.getDomainIncomingSideId() == null) {
				throw new BusinessException("error_incomingDomainIsMandatory");
			}
		}

		if (info.getReceiveDate().after(HijriDateService.getHijriSysDate())) {
			throw new BusinessException("error_receiveDateAfterToday");
		}

		if (info.getIncomingDate() != null && info.getIncomingDate().after(HijriDateService.getHijriSysDate())) {
			throw new BusinessException("error_incomingDateAfterToday");
		}

		/*
		 * if (info.getInfoDetails().length() < 2000) { //TODO throw new BusinessException("error_detailsLessThan2000"); }
		 */
		if (info.getInfoDetails().length() > 4000) {
			throw new BusinessException("error_detailsGreatThanLimit");
		}
	}

	/**
	 * Generates info number
	 * 
	 * @return info number
	 */
	private static String generateInfoNumber(long departmentId, CustomSession session) throws BusinessException, DatabaseException {
		// 2- infoNumber is calculated as follow :
		// 6 digits :
		// 3 digits info serial
		// 3 digits info region sequence
		Long regionId = DepartmentService.getRegionByDepId(departmentId);
		long infoNumber = CommonService.generateSequenceNumber(EntitySequenceGeneratorEnum.INFO.getEntityId(), 1000, session);
		long infoSeq = getInfoSeqNumber(regionId, session);
		return String.format("%03d", infoSeq) + String.format("%03d", infoNumber);
	}

	/**
	 * Get Info Sequence Number
	 * 
	 * @param regionId
	 * @return
	 * @throws BusinessException
	 */
	private static Long getInfoSeqNumber(long regionId, CustomSession session) throws BusinessException {
		try {
			int maxValue = 1000;
			WFPosition pos = WFPositionService.getWFPositionByUnitId(regionId);
			if (pos.getPositionId().equals((long) WFPositionsEnum.ALMADINA_REGION_DEPARTMENT.getCode())) {
				return CommonService.generateSequenceNumber(EntitySequenceGeneratorEnum.INFO_ALMADINA_REGION.getEntityId(), maxValue, session);
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.EASTERN_REGION_DEPARTMENT.getCode())) {
				return CommonService.generateSequenceNumber(EntitySequenceGeneratorEnum.INFO_EASTERN_REGION_.getEntityId(), maxValue, session);
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ASEER_REGION_DEPARTMENT.getCode())) {
				return CommonService.generateSequenceNumber(EntitySequenceGeneratorEnum.INFO_ASEER_REGION.getEntityId(), maxValue, session);
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.GAZAN_REGION_DEPARTMENT.getCode())) {
				return CommonService.generateSequenceNumber(EntitySequenceGeneratorEnum.INFO_GAZAN_REGION.getEntityId(), maxValue, session);
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.GOUF_REGION_DEPARTMENT.getCode())) {
				return CommonService.generateSequenceNumber(EntitySequenceGeneratorEnum.INFO_GOUF_REGION.getEntityId(), maxValue, session);
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.MAKA_ELMOKRAMA_REGION_DEPARTMENT.getCode())) {
				return CommonService.generateSequenceNumber(EntitySequenceGeneratorEnum.INFO_MAKA_ELMOKRAMA.getEntityId(), maxValue, session);
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.NAGRAN_REGION_DEPARTMENT.getCode())) {
				return CommonService.generateSequenceNumber(EntitySequenceGeneratorEnum.INFO_NAGRAN_REGION.getEntityId(), maxValue, session);
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.NORTH_BORDER_REGION_DEPARTMENT.getCode())) {
				return CommonService.generateSequenceNumber(EntitySequenceGeneratorEnum.INFO_NORTH_BORDER_REGION.getEntityId(), maxValue, session);
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.TABOK_REGION_DEPARTMENT.getCode())) {
				return CommonService.generateSequenceNumber(EntitySequenceGeneratorEnum.INFO_TABOK_REGION.getEntityId(), maxValue, session);
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ACADEMY_REGION_DEPARTMENT.getCode())) {
				return CommonService.generateSequenceNumber(EntitySequenceGeneratorEnum.INFO_ACADEMY_REGION.getEntityId(), maxValue, session);
			} else {
				return CommonService.generateSequenceNumber(EntitySequenceGeneratorEnum.INFO_DIRECTORATE.getEntityId(), maxValue, session);
			}

		} catch (Exception e) {
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoRegionWorkFlow.class, e, "InfoRegionWorkFlow");
				throw new BusinessException("error_general");
			}
		}
	}

	/**
	 * Update info data
	 * 
	 * @param infoData
	 * @throws BusinessException
	 */
	public static void updateInfo(InfoData infoData, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateInfo(infoData.getInfo());
		// can't update while in work flow mode, message
		if (infoData.getInfo().getwFInstanceId() != null && infoData.getStatus() == InfoStatusEnum.REGISTERED.getCode()) {
			throw new BusinessException("error_infoUnderApproval");
		}

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			infoData.getInfo().setSystemUser(loginEmpData.getEmpId().toString());

			DataAccess.updateEntity(infoData.getInfo(), session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoService.class, e, "InfoService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Delete info data
	 * 
	 * @param infoData
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteInfo(InfoData infoData, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		if (infoData.getwFInstanceId() != null) {
			throw new BusinessException("error_withinWorkFlow");
		}

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			infoData.getInfo().setSystemUser(loginEmpData.getEmpId().toString());

			deleteInfoPhones(infoData.getId(), session);
			deleteInfoSources(infoData.getId(), session);
			deleteInfoCoordinates(infoData.getId(), session);
			deleteInfoVisibleDepartments(infoData.getId(), session);
			deleteInfoRelatedEntities(infoData.getId(), session);

			DataAccess.deleteEntity(infoData.getInfo(), session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_relatedData");
			} else {
				Log4j.traceErrorException(InfoService.class, e, "InfoService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Save selected info source
	 * 
	 * @param infoSourceId
	 * @param infoId
	 * @param infoSourceType
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveInfoSource(long infoSourceId, long infoId, int infoSourceType, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			InfoSource infoSource = new InfoSource();

			infoSource.setInfoId(infoId);

			infoSource.setSourceType(infoSourceType);
			if (infoSourceType == InfoSourceTypeEnum.OPEN_SOURCE.getCode()) {
				infoSource.setOpenSourceId(infoSourceId);
			} else {
				infoSource.setAssignmentDetailId(infoSourceId);
			}

			infoSource.setSystemUser(loginEmpData.getEmpId().toString());

			DataAccess.addEntity(infoSource, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();

			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoService.class, e, "InfoService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Delete selected info source
	 * 
	 * @param infoSourceId
	 * @param infoId
	 * @param infoSourceTypeEnum
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteInfoSource(long infoSourceId, long infoId, InfoSourceTypeEnum infoSourceTypeEnum, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		InfoSource infoSource = null;
		try {
			if (infoSourceTypeEnum.equals(InfoSourceTypeEnum.OPEN_SOURCE)) {
				infoSource = searchInfoSources(infoId, FlagsEnum.ALL.getCode(), infoSourceId).get(0);
			} else {
				infoSource = searchInfoSources(infoId, infoSourceId, FlagsEnum.ALL.getCode()).get(0);
			}
		} catch (NoDataException e1) {
			throw new BusinessException("error_general");
		}

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			infoSource.setSystemUser(loginEmpData.getEmpId().toString());

			DataAccess.deleteEntity(infoSource, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();

			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_relatedData");
			} else {
				Log4j.traceErrorException(InfoService.class, e, "InfoService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * List all open sources related to info
	 * 
	 * @param infoId
	 * @return list of open sources
	 * @throws BusinessException
	 */
	public static List<OpenSource> getInfoOpenSources(long infoId) throws BusinessException {
		return OpenSourceService.getInfoOpenSources(infoId);
	}

	/**
	 * List all Assignment related to info
	 * 
	 * @param infoId
	 * @return list of assignments
	 * @throws BusinessException
	 */
	public static List<AssignmentDetailData> getInfoAssignmentDetailsData(long infoId) throws BusinessException {
		return AssignmentService.getInfoAssignments(infoId);
	}

	/**
	 * Save info coordinate
	 * 
	 * @param coordinate
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveInfoCoordinates(InfoCoordinate coordinate, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		valdiateInfoCoordinate(coordinate);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			coordinate.setSystemUser(loginEmpData.getEmpId().toString());

			DataAccess.addEntity(coordinate, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();

			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoService.class, e, "InfoService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * update info coordinate
	 * 
	 * @param coordinate
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateInfoCoordinates(InfoCoordinate coordinate, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		valdiateInfoCoordinate(coordinate);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			coordinate.setSystemUser(loginEmpData.getEmpId().toString());

			DataAccess.updateEntity(coordinate, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();

			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoService.class, e, "InfoService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Delete selected coordinate
	 * 
	 * @param coordinate
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteInfoCoordinates(InfoCoordinate coordinate, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			coordinate.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.deleteEntity(coordinate, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();

			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoService.class, e, "InfoService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * valdiateInfoCoordinate
	 * 
	 * @param infoCoordinate
	 * @throws BusinessException
	 */
	private static void valdiateInfoCoordinate(InfoCoordinate infoCoordinate) throws BusinessException {
		if (infoCoordinate.getNorthDegree() == null || infoCoordinate.getNorthMinutes() == null || infoCoordinate.getNorthSecondes() == null || infoCoordinate.getNorthSecondes().isEmpty() || infoCoordinate.getNorthSecondes().trim().equals("")) {
			throw new BusinessException("error_coordianteIsMandatory");
		}
		if (infoCoordinate.getEastDegree() == null || infoCoordinate.getEastMinutes() == null || infoCoordinate.getEastSecondes() == null || infoCoordinate.getEastSecondes().isEmpty() || infoCoordinate.getEastSecondes().trim().equals("")) {
			throw new BusinessException("error_coordianteIsMandatory");
		}
	}

	/**
	 * List all coordinates related to info
	 * 
	 * @param infoId
	 * @return list of info coordinates
	 * @throws BusinessException
	 */
	public static List<InfoCoordinate> getInfoCoordinates(long infoId) throws BusinessException {
		return searchInfoCoordinates(infoId);
	}

	/**
	 * Valdiate Phone Number Duplicates For The Same Information
	 * 
	 * @param infoPhoneList
	 * @throws BusinessException
	 */
	private static void valdiatePhoneNumberDuplicates(List<InfoPhone> infoPhoneList) throws BusinessException {
		int count = 0;
		for (InfoPhone infoPhone : infoPhoneList) {
			for (InfoPhone searchedInfoPhone : infoPhoneList) {
				if (searchedInfoPhone.getPhoneNumber() != null) {
					if (searchedInfoPhone.getPhoneNumber().equals(infoPhone.getPhoneNumber())) {
						count++;
					}
				}
			}
			if (count > 1) {
				throw new BusinessException("error_phoneNumberIsUniqueForInfo");
			} else {
				count = 0;
			}
		}
	}

	private static void valdiatePhoneNumber(InfoPhone infoPhone) throws BusinessException {
		if (infoPhone.getPhoneNumber() == null || infoPhone.getPhoneNumber().isEmpty() || infoPhone.getPhoneNumber().trim().equals("")) { // || infoPhone.getPhoneNumber().length() != 12 || !infoPhone.getPhoneNumber().startsWith("966")
			// throw new BusinessException("error_contactPhoneValidation");
			throw new BusinessException("error_phoneIsMandatory");
		}
	}

	/**
	 * Save info phones
	 * 
	 * @param infoPhone
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveInfoPhones(InfoPhone infoPhone, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		valdiatePhoneNumber(infoPhone);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			infoPhone.setSystemUser(loginEmpData.getEmpId().toString());

			DataAccess.addEntity(infoPhone, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoService.class, e, "InfoService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * update info Date
	 * 
	 * @param infoPhone
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateInfoPhones(InfoPhone infoPhone, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			infoPhone.setSystemUser(loginEmpData.getEmpId().toString());

			DataAccess.updateEntity(infoPhone, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoService.class, e, "InfoService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Delete selected info phone
	 * 
	 * @param infoPhone
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteInfoPhones(InfoPhone infoPhone, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			infoPhone.setSystemUser(loginEmpData.getEmpId().toString());

			DataAccess.deleteEntity(infoPhone, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoService.class, e, "InfoService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * List all phones related to info
	 * 
	 * @param infoId
	 * @return list of info phones
	 * @throws BusinessException
	 */
	public static List<InfoPhone> getInfoPhones(long infoId) throws BusinessException {
		return searchInfoPhones(infoId);
	}

	/**
	 * Save info related entity
	 * 
	 * @param relatedEntityId
	 * @param infoId
	 * @param infoRelatedEntityTypeEnum
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveInfoRelate(long relatedEntityId, long infoId, InfoRelatedEntityTypeEnum infoRelatedEntityTypeEnum, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			InfoRelatedEntity infoRelatedEntity = new InfoRelatedEntity();
			infoRelatedEntity.setInfoId(infoId);

			infoRelatedEntity.setEntityType(infoRelatedEntityTypeEnum.getCode());

			if (infoRelatedEntityTypeEnum.equals(InfoRelatedEntityTypeEnum.COUNTRY)) {
				infoRelatedEntity.setCountryId(relatedEntityId);
			} else if (infoRelatedEntityTypeEnum.equals(InfoRelatedEntityTypeEnum.DEPARTMENT)) {
				infoRelatedEntity.setDepartmentId(relatedEntityId);
			} else if (infoRelatedEntityTypeEnum.equals(InfoRelatedEntityTypeEnum.DOMAIN)) {
				infoRelatedEntity.setDomainId(relatedEntityId);
			} else if (infoRelatedEntityTypeEnum.equals(InfoRelatedEntityTypeEnum.EMPLOYEE)) {
				infoRelatedEntity.setEmployeeId(relatedEntityId);
			} else if (infoRelatedEntityTypeEnum.equals(InfoRelatedEntityTypeEnum.NON_EMPLOYEE)) {
				infoRelatedEntity.setNonEmployeeId(relatedEntityId);
			}

			infoRelatedEntity.setSystemUser(loginEmpData.getEmpId().toString());

			DataAccess.addEntity(infoRelatedEntity, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();

			}
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoService.class, e, "InfoService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Delete info related entity
	 * 
	 * @param relatedEntityId
	 * @param infoId
	 * @param infoRelatedEntityTypeEnum
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteInfoRelate(long relatedEntityId, long infoId, InfoRelatedEntityTypeEnum infoRelatedEntityTypeEnum, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		InfoRelatedEntity infoRelatedEntity = null;

		try {
			if (infoRelatedEntityTypeEnum.equals(InfoRelatedEntityTypeEnum.COUNTRY))
				infoRelatedEntity = searchInfoRelatedEntities(infoId, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), relatedEntityId, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode()).get(0);

			else if (infoRelatedEntityTypeEnum.equals(InfoRelatedEntityTypeEnum.DEPARTMENT))
				infoRelatedEntity = searchInfoRelatedEntities(infoId, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), relatedEntityId, FlagsEnum.ALL.getCode()).get(0);

			else if (infoRelatedEntityTypeEnum.equals(InfoRelatedEntityTypeEnum.DOMAIN))
				infoRelatedEntity = searchInfoRelatedEntities(infoId, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), relatedEntityId).get(0);

			else if (infoRelatedEntityTypeEnum.equals(InfoRelatedEntityTypeEnum.EMPLOYEE))
				infoRelatedEntity = searchInfoRelatedEntities(infoId, FlagsEnum.ALL.getCode(), relatedEntityId, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode()).get(0);

			else if (infoRelatedEntityTypeEnum.equals(InfoRelatedEntityTypeEnum.NON_EMPLOYEE))
				infoRelatedEntity = searchInfoRelatedEntities(infoId, relatedEntityId, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode()).get(0);
		} catch (NoDataException e) {
			throw new BusinessException("error_general");
		}

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			infoRelatedEntity.setSystemUser(loginEmpData.getEmpId().toString());

			DataAccess.deleteEntity(infoRelatedEntity, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_relatedData");
			} else {
				Log4j.traceErrorException(InfoService.class, e, "InfoService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * List all related employees
	 * 
	 * @param infoId
	 * @return list of employee data
	 * @throws BusinessException
	 */
	public static List<EmployeeData> getInfoRelatedEmployees(long infoId) throws BusinessException {
		return EmployeeService.getInfoRelatedEmployees(infoId);
	}

	/**
	 * List all related non employees
	 * 
	 * @param infoId
	 * @return list of non employee data
	 * @throws BusinessException
	 */
	public static List<NonEmployeeData> getInfoRelatedNonEmployees(long infoId) throws BusinessException {
		return NonEmployeeService.getInfoRelatedNonEmployees(infoId);
	}

	/**
	 * List all related CountryData
	 * 
	 * @param infoId
	 * @return list of country data
	 * @throws BusinessException
	 */
	public static List<CountryData> getInfoRelatedCountries(long infoId) throws BusinessException {
		return CountryService.getInfoRelatedCountries(infoId);
	}

	/**
	 * List all related DepartmentData
	 * 
	 * @param infoId
	 * @return department data
	 * @throws BusinessException
	 */
	public static List<DepartmentData> getInfoRelatedDepartments(long infoId) throws BusinessException {
		return DepartmentService.getInfoRelatedDepartments(infoId);
	}

	/**
	 * List all related other entities as Domain values
	 * 
	 * @param infoId
	 * @return setup domains
	 * @throws BusinessException
	 */
	public static List<SetupDomain> getInfoRelatedEntities(long infoId) throws BusinessException {
		return SetupService.getInfoRelatedEntities(infoId);
	}

	/**
	 * Save info analysis
	 * 
	 * @param processingList
	 * @param notifiedList
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveInfoAnalysis(List<DepartmentData> processingList, List<DepartmentData> notifiedList, long infoId, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();

			for (int i = 0; i < processingList.size(); i++) {
				InfoAnalysis infoAnalysis = new InfoAnalysis();
				infoAnalysis.setInfoId(infoId);
				infoAnalysis.setDepartmentId(processingList.get(i).getId());
				infoAnalysis.setDepartmentName(processingList.get(i).getArabicName());
				infoAnalysis.setRedirectionType(new Long(InfoAnalysisRedirectionTypeEnum.PROCESSING.getCode()));
				infoAnalysis.setSystemUser(loginEmpData.getEmpId().toString());

				DataAccess.addEntity(infoAnalysis, session);
			}

			for (int i = 0; i < notifiedList.size(); i++) {
				InfoAnalysis infoAnalysis = new InfoAnalysis();
				infoAnalysis.setInfoId(infoId);
				infoAnalysis.setDepartmentId(notifiedList.get(i).getId());
				infoAnalysis.setDepartmentName(notifiedList.get(i).getArabicName());
				infoAnalysis.setRedirectionType(new Long(InfoAnalysisRedirectionTypeEnum.NOTIFICATION.getCode()));
				infoAnalysis.setSystemUser(loginEmpData.getEmpId().toString());

				DataAccess.addEntity(infoAnalysis, session);
			}

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoService.class, e, "InfoService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Update info analysis
	 * 
	 * @param infoAnalysis
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateInfoAnalysis(InfoAnalysis infoAnalysis, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			infoAnalysis.setSystemUser(loginEmpData.getEmpId().toString());

			DataAccess.updateEntity(infoAnalysis, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoService.class, e, "InfoService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Get all info analysis by info id
	 * 
	 * @return list of info analysis
	 * @throws BusinessException
	 */
	public static List<InfoAnalysis> getInfoAnalysis(long infoId, Integer redirectionType) throws BusinessException {
		try {
			return searchInfoAnalysis(infoId, FlagsEnum.ALL.getCode(), redirectionType == null ? FlagsEnum.ALL.getCode() : redirectionType);
		} catch (NoDataException e) {
			return new ArrayList<InfoAnalysis>();
		}
	}

	/**
	 * 
	 * @param infoId
	 * @param departmentId
	 * @return
	 * @throws BusinessException
	 */
	public static InfoAnalysis getInfoAnalysisByInfoIdAndDepartmentId(long infoId, long departmentId, int redirectionType) throws BusinessException {
		try {
			return searchInfoAnalysis(infoId, departmentId, redirectionType).get(0);
		} catch (NoDataException e) {
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Save info analysis reference data
	 * 
	 * @param infoAnalysisReferenceDataList
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveInfoAnalysisReferenceData(List<InfoAnalysisReferenceData> infoAnalysisReferenceDataList, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		// redirection processingList 1, redirection notifiedList 2
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			for (int i = 0; i < infoAnalysisReferenceDataList.size(); i++) {
				infoAnalysisReferenceDataList.get(i).getInfoAnalysisReference().setSystemUser(loginEmpData.getEmpId().toString());
				DataAccess.addEntity(infoAnalysisReferenceDataList.get(i).getInfoAnalysisReference(), session);
				infoAnalysisReferenceDataList.get(i).setId(infoAnalysisReferenceDataList.get(i).getInfoAnalysisReference().getId());
			}

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoService.class, e, "InfoService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Save info analysis detail data
	 * 
	 * @param infoAnalysisDetailDataList
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveInfoAnalysisDetailData(List<InfoAnalysisDetailData> infoAnalysisDetailDataList, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession)

				session.beginTransaction();

			for (int i = 0; i < infoAnalysisDetailDataList.size(); i++) {
				infoAnalysisDetailDataList.get(i).getInfoAnalysisDetail().setSystemUser(loginEmpData.getEmpId().toString());
				DataAccess.addEntity(infoAnalysisDetailDataList.get(i).getInfoAnalysisDetail(), session);
				infoAnalysisDetailDataList.get(i).setId(infoAnalysisDetailDataList.get(i).getInfoAnalysisDetail().getId());
			}

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoService.class, e, "InfoService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	public static byte[] getRegionInformationBytes(long regionId, List<Long> visibleList, long sectorId, Date fromDate, Date toDate, String regionName, String loginEmployeeName, String agentCode, String identity, long officerId) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_REGION_ID", regionId);
			parameters.put("P_VISIBLE_LIST", visibleList);
			parameters.put("P_SECTOR_ID", sectorId);
			// Ahmad Alsaqqa Modification
			{
			if (fromDate != null)
				parameters.put("P_FROM_DATE", HijriDateService.getHijriDateString(fromDate));
			else
				parameters.put("P_FROM_DATE", HijriDateService.getHijriDateString(toDate));
			}
			// =====================
			parameters.put("P_TO_DATE", HijriDateService.getHijriDateString(toDate));
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_REGION_NAME", regionName);
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			parameters.put("P_IDENTITY", identity == null || identity.isEmpty() || identity.trim().equals("") ? FlagsEnum.ALL.getCode() + "" : identity);
			parameters.put("P_OFFICER_ID", officerId);
			parameters.put("P_AGENT_CODE", agentCode == null || agentCode.isEmpty() || agentCode.trim().equals("") ? FlagsEnum.ALL.getCode() + "" : "%" + agentCode + "%");
			parameters.put("P_AGENT_CODE_STRING", agentCode == null || agentCode.isEmpty() || agentCode.trim().equals("") ? FlagsEnum.ALL.getCode() + "" : agentCode);
			reportName = ReportNamesEnum.REGION_INFORMATION_REPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_general");
		}
	}

	public static byte[] getRegionInformationStatisticsBytes(long regionId, long sectorId, Date fromDate, Date toDate, String regionName, String loginEmployeeName, String agentCode) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_REGION_ID", regionId);
			parameters.put("P_SECTOR_ID", sectorId);
			parameters.put("P_FROM_DATE", HijriDateService.getHijriDateString(fromDate));
			parameters.put("P_TO_DATE", HijriDateService.getHijriDateString(toDate));
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_REGION_NAME", regionName);
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			parameters.put("P_AGENT_CODE", agentCode == null || agentCode.isEmpty() || agentCode.trim().equals("") ? FlagsEnum.ALL.getCode() + "" : "%" + agentCode + "%");
			parameters.put("P_AGENT_CODE_STRING", agentCode == null || agentCode.isEmpty() || agentCode.trim().equals("") ? FlagsEnum.ALL.getCode() + "" : agentCode);
			reportName = ReportNamesEnum.REGION_INFORMATION_STATISTICS_REPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_general");
		}
	}

	public static byte[] getInformationAnalysisBytes(long regionId, long sectorId, Date fromDate, Date toDate, String regionName, long classificationTypeId, String classificationTypeDesc, long classificationId, String classificationDesc, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_REGION_ID", regionId);
			parameters.put("P_SECTOR_ID", sectorId);
			parameters.put("P_FROM_DATE", HijriDateService.getHijriDateString(fromDate));
			parameters.put("P_TO_DATE", HijriDateService.getHijriDateString(toDate));
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_REGION_NAME", regionName);
			parameters.put("P_CLASSIFICATION_TYPE_ID", classificationTypeId);
			parameters.put("P_CLASSIFICATION_TYPE_DESC", classificationTypeDesc);
			parameters.put("P_CLASSIFICATION_ID", classificationId);
			parameters.put("P_CLASSIFICATION_DESC", classificationDesc);
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.INFORMATION_ANALYSIS_REPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_general");
		}
	}

	public static byte[] getInformationAnalysisStatisticsBytes(long regionId, long sectorId, Date fromDate, Date toDate, String regionName, long classificationTypeId, String classificationTypeDesc, long classificationId, String classificationDesc, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_REGION_ID", regionId);
			parameters.put("P_SECTOR_ID", sectorId);
			parameters.put("P_FROM_DATE", HijriDateService.getHijriDateString(fromDate));
			parameters.put("P_TO_DATE", HijriDateService.getHijriDateString(toDate));
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_REGION_NAME", regionName);
			parameters.put("P_CLASSIFICATION_TYPE_ID", classificationTypeId);
			parameters.put("P_CLASSIFICATION_TYPE_DESC", classificationTypeDesc);
			parameters.put("P_CLASSIFICATION_ID", classificationId);
			parameters.put("P_CLASSIFICATION_DESC", classificationDesc);
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.INFORMATION_ANALYSIS_STATISTICS_REPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_general");
		}
	}

	public static byte[] getInformationInfoTypeBytes(long regionId, long sectorId, Date fromDate, Date toDate, String regionName, long infoTypeId, String infoTypeDesc, long infoSubjectId, String infoSubjectDesc, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_REGION_ID", regionId);
			parameters.put("P_SECTOR_ID", sectorId);
			parameters.put("P_FROM_DATE", HijriDateService.getHijriDateString(fromDate));
			parameters.put("P_TO_DATE", HijriDateService.getHijriDateString(toDate));
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_REGION_NAME", regionName);
			parameters.put("P_TYPE_ID", infoTypeId);
			parameters.put("P_TYPE_NAME", infoTypeDesc);
			parameters.put("P_SUBJECT_ID", infoSubjectId);
			parameters.put("P_SUBJECT_NAME", infoSubjectDesc);
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.INFORMATION_INFO_TYPE_REPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_general");
		}
	}

	public static byte[] getInformationInfoTypeStatisticsBytes(long regionId, long sectorId, Date fromDate, Date toDate, String regionName, long infoTypeId, String infoTypeDesc, long infoSubjectId, String infoSubjectDesc, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_REGION_ID", regionId);
			parameters.put("P_SECTOR_ID", sectorId);
			parameters.put("P_FROM_DATE", HijriDateService.getHijriDateString(fromDate));
			parameters.put("P_TO_DATE", HijriDateService.getHijriDateString(toDate));
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_REGION_NAME", regionName);
			parameters.put("P_TYPE_ID", infoTypeId);
			parameters.put("P_TYPE_NAME", infoTypeDesc);
			parameters.put("P_SUBJECT_ID", infoSubjectId);
			parameters.put("P_SUBJECT_NAME", infoSubjectDesc);
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.INFORMATION_INFO_TYPE_STATISTICS_REPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_general");
		}
	}

	public static byte[] getInformationIncommingDomainBytes(Long regionId, String regionName, long incommingId, String incommingName, int importance, Date fromDate, Date toDate, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_REGION_ID", regionId);
			parameters.put("P_REGION_NAME", regionName == null || regionName.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : regionName);
			parameters.put("P_INCOMING_ID", incommingId);
			parameters.put("P_INCOMING_DESC", incommingName);
			parameters.put("P_IMPORTANCE", importance);
			parameters.put("P_FROM_DATE", HijriDateService.getHijriDateString(fromDate));
			parameters.put("P_TO_DATE", HijriDateService.getHijriDateString(toDate));
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.INFORMATION_INCOMMING_DOMAIN_REPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_general");
		}
	}

	public static byte[] getInformationIncommingDomainStatisticsBytes(long regionId, String regionName, long incommingId, String incommingName, int importance, Date fromDate, Date toDate, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_REGION_ID", regionId);
			parameters.put("P_REGION_NAME", regionName == null || regionName.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : regionName);
			parameters.put("P_INCOMING_ID", incommingId);
			parameters.put("P_INCOMING_DESC", incommingName);
			parameters.put("P_IMPORTANCE", importance);
			parameters.put("P_FROM_DATE", HijriDateService.getHijriDateString(fromDate));
			parameters.put("P_TO_DATE", HijriDateService.getHijriDateString(toDate));
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.INFORMATION_INCOMMING_DOMAIN_STATISTICS_REPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * getInformationDetailsBytes
	 * 
	 * @param infoId
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getInformationDetailsBytes(long infoId, String loginEmployeeName, boolean isWithAnalysis) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_INFO_LIST", new Long[] { infoId });
			parameters.put("P_IS_ANALYSIS", isWithAnalysis);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.INFORMATION_DETAILS_REPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * 
	 * @param copy
	 * @param notes
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getInformationDetailsBytes(String copy, String notes) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_COPY", copy);
			parameters.put("P_NOTES", notes);
			reportName = ReportNamesEnum.INFORMATION_SEARCH_DETAIL.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get all info analysis data
	 * 
	 * @param infoAnalysisId
	 * @return list of info analysis data
	 * @throws BusinessException
	 */
	public static List<InfoAnalysisDetailData> getInfoAnalysisDetails(long infoAnalysisId) throws BusinessException {
		return searchInfoAnalysisDetails(infoAnalysisId);
	}

	/**
	 * Get all info analysis reference
	 * 
	 * @param infoAnalysisId
	 * @return list of info reference data
	 * @throws BusinessException
	 */
	public static List<InfoAnalysisReferenceData> getInfoAnalysisReferences(long infoAnalysisId) throws BusinessException {
		return searchInfoAnalysisReferences(infoAnalysisId);
	}

	/**
	 * Save list of InfoRecommendationData
	 * 
	 * @param infoRecommendationsList
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveInfoRecommendations(List<InfoRecommendationData> infoRecommendationsList, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();

			for (int i = 0; i < infoRecommendationsList.size(); i++) {
				infoRecommendationsList.get(i).getInfoRecommendation().setSystemUser(loginEmpData.getEmpId().toString());
				DataAccess.addEntity(infoRecommendationsList.get(i).getInfoRecommendation(), session);
				infoRecommendationsList.get(i).setId(infoRecommendationsList.get(i).getInfoRecommendation().getId());
			}

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();

			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoService.class, e, "InfoService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Get all info recommendations
	 * 
	 * @return list of info recommendation data
	 * @throws BusinessException
	 */
	public static List<InfoRecommendationData> getInfoRecommendations(long infoId) throws BusinessException {
		return searchInfoRecommendations(FlagsEnum.ALL.getCode(), infoId, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode());
	}

	/**
	 * Save info visible departments
	 * 
	 * @param infoId
	 * @param departmentsList
	 *            : types of regions or modrya
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveInfoVisibleDepartments(long infoId, List<Long> departmentsList, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		InfoVisibleDepartment infoVisibleDepartment;

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();

			for (int i = 0; i < departmentsList.size(); i++) {
				infoVisibleDepartment = new InfoVisibleDepartment();
				infoVisibleDepartment.setDepartmentId(departmentsList.get(i));
				infoVisibleDepartment.setInfoId(infoId);
				infoVisibleDepartment.setSystemUser(loginEmpData.getEmpId().toString());

				DataAccess.addEntity(infoVisibleDepartment, session);
			}

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoService.class, e, "InfoService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Get all visible departments
	 * 
	 * @param infoId
	 * @return list of visible departments
	 * @throws BusinessException
	 */
	public static List<DepartmentData> getInfoVisibleDepartments(long infoId) throws BusinessException {
		return searchInfoVisibleDepartments(infoId);
	}

	/**
	 * Check if there is info with the following number or not
	 * 
	 * @param infoNumber
	 * @return boolean
	 * @throws BusinessException
	 */
	public static boolean isExistingInfo(String infoNumber) throws BusinessException {
		if (searchInfoData(infoNumber, FlagsEnum.ALL.getCode(), null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, null, null, null, null, null, null).size() > 0)
			return true;
		return false;
	}

	/**
	 * Get all information
	 * 
	 * @param infoNumber
	 * @param selectedIncomingSideDomainId
	 * @param selectedInfoSubjectId
	 * @param selectedInfoTypeId
	 * @param loginEmpData
	 * @param fromDate
	 * @param toDate
	 * @param countryId
	 * @param departmentId
	 * @param domainId
	 * @param employeeId
	 * @param nonEmployeeId
	 * @param assignmentDetailId
	 * @param openSourceId
	 * @param regionId
	 * @param sectorId
	 * @param infoDetails
	 * @param searchPhoneNumber
	 * @param incomingFileNumber
	 * @param incomingFileYear
	 * @return
	 * @throws BusinessException
	 */
	public static List<InfoData> getInfoData(String infoNumber, Long selectedIncomingSideDomainId, Long selectedInfoSubjectId, Long selectedInfoTypeId, EmployeeData loginEmpData, Date fromDate, Date toDate, Long countryId, Long departmentId, Long domainId, Long employeeId, Long nonEmployeeId, Long assignmentDetailId, Long openSourceId, Long regionId, Long sectorId, Long unitId, String infoDetails, String searchPhoneNumber, String incomingFileNumber, Integer incomingFileYear, String incomingNumber, Date incomingFromDate, Date incomingToDate)
			throws BusinessException {
		DepartmentData dep = DepartmentService.getDepartment(loginEmpData.getActualDepartmentId());

		if (assignmentDetailId == null && openSourceId == null) { // no sources
			if (countryId == null && departmentId == null && domainId == null && employeeId == null && nonEmployeeId == null) { // no sources and related entities
				return searchInfoData(infoNumber, selectedIncomingSideDomainId == null ? FlagsEnum.ALL.getCode() : selectedIncomingSideDomainId, new Long[] { dep.getRegionId(), dep.getId() }, fromDate, toDate, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), selectedInfoSubjectId == null ? FlagsEnum.ALL.getCode() : selectedInfoSubjectId, selectedInfoTypeId == null ? FlagsEnum.ALL.getCode() : selectedInfoTypeId, regionId == null ? FlagsEnum.ALL.getCode() : regionId,
						sectorId == null ? FlagsEnum.ALL.getCode() : sectorId, unitId == null ? FlagsEnum.ALL.getCode() : unitId, infoDetails, searchPhoneNumber, incomingFileNumber, incomingFileYear, incomingNumber, incomingFromDate, incomingToDate);
			} else { // related entities but no sources
				return searchInfoData(infoNumber, selectedIncomingSideDomainId == null ? FlagsEnum.ALL.getCode() : selectedIncomingSideDomainId, new Long[] { dep.getRegionId(), dep.getId() }, fromDate, toDate, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), selectedInfoSubjectId == null ? FlagsEnum.ALL.getCode() : selectedInfoSubjectId, selectedInfoTypeId == null ? FlagsEnum.ALL.getCode() : selectedInfoTypeId, countryId == null ? FlagsEnum.ALL.getCode() : countryId,
						departmentId == null ? FlagsEnum.ALL.getCode() : departmentId, domainId == null ? FlagsEnum.ALL.getCode() : domainId, employeeId == null ? FlagsEnum.ALL.getCode() : employeeId, nonEmployeeId == null ? FlagsEnum.ALL.getCode() : nonEmployeeId, regionId == null ? FlagsEnum.ALL.getCode() : regionId, sectorId == null ? FlagsEnum.ALL.getCode() : sectorId, unitId == null ? FlagsEnum.ALL.getCode() : unitId, infoDetails, searchPhoneNumber, incomingFileNumber, incomingFileYear, incomingNumber, incomingFromDate, incomingToDate);
			}
		} else { // sources
			Long officerId = null;
			String identity = null;
			if (assignmentDetailId != null) {
				AssignmentDetailData assignement = AssignmentService.getAssignmentDetailsDataById(assignmentDetailId);
				officerId = assignement.getOfficerId();
				identity = assignement.getIdentity();
			}
			if (countryId == null && departmentId == null && domainId == null && employeeId == null && nonEmployeeId == null) { // sources and related entities
				return searchInfoData(infoNumber, selectedIncomingSideDomainId == null ? FlagsEnum.ALL.getCode() : selectedIncomingSideDomainId, new Long[] { dep.getRegionId(), dep.getId() }, fromDate, toDate, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), selectedInfoSubjectId == null ? FlagsEnum.ALL.getCode() : selectedInfoSubjectId, selectedInfoTypeId == null ? FlagsEnum.ALL.getCode() : selectedInfoTypeId, assignmentDetailId == null ? FlagsEnum.ALL.getCode() : assignmentDetailId,
						openSourceId == null ? FlagsEnum.ALL.getCode() : openSourceId, regionId == null ? FlagsEnum.ALL.getCode() : regionId, sectorId == null ? FlagsEnum.ALL.getCode() : sectorId, unitId == null ? FlagsEnum.ALL.getCode() : unitId, infoDetails, searchPhoneNumber, incomingFileNumber, incomingFileYear, officerId == null ? FlagsEnum.ALL.getCode() : officerId, identity, incomingNumber, incomingFromDate, incomingToDate);
			} else { // related entities and sources
				return searchInfoData(infoNumber, selectedIncomingSideDomainId == null ? FlagsEnum.ALL.getCode() : selectedIncomingSideDomainId, new Long[] { dep.getRegionId(), dep.getId() }, fromDate, toDate, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), selectedInfoSubjectId == null ? FlagsEnum.ALL.getCode() : selectedInfoSubjectId, selectedInfoTypeId == null ? FlagsEnum.ALL.getCode() : selectedInfoTypeId, countryId == null ? FlagsEnum.ALL.getCode() : countryId,
						departmentId == null ? FlagsEnum.ALL.getCode() : departmentId, domainId == null ? FlagsEnum.ALL.getCode() : domainId, employeeId == null ? FlagsEnum.ALL.getCode() : employeeId, nonEmployeeId == null ? FlagsEnum.ALL.getCode() : nonEmployeeId, assignmentDetailId == null ? FlagsEnum.ALL.getCode() : assignmentDetailId, openSourceId == null ? FlagsEnum.ALL.getCode() : openSourceId, regionId == null ? FlagsEnum.ALL.getCode() : regionId,
						sectorId == null ? FlagsEnum.ALL.getCode() : sectorId, unitId == null ? FlagsEnum.ALL.getCode() : unitId, infoDetails, searchPhoneNumber, incomingFileNumber, incomingFileYear, officerId == null ? FlagsEnum.ALL.getCode() : officerId, identity, incomingNumber, incomingFromDate, incomingToDate);
			}
		}
	}

	/**
	 * getInfoDataByAnalysis
	 * 
	 * @param infoNumber
	 * @param selectedIncomingSideDomainId
	 * @param selectedInfoSubjectId
	 * @param selectedInfoTypeId
	 * @param loginEmpData
	 * @param fromDate
	 * @param toDate
	 * @param countryId
	 * @param departmentId
	 * @param domainId
	 * @param employeeId
	 * @param nonEmployeeId
	 * @param assignmentDetailId
	 * @param openSourceId
	 * @param regionId
	 * @param sectorId
	 * @param infoDetails
	 * @param searchPhoneNumber
	 * @param incomingFileNumber
	 * @param incomingFileYear
	 * @return
	 * @throws BusinessException
	 */
	public static List<InfoData> getInfoDataByAnalysis(String infoNumber, Long selectedIncomingSideDomainId, Long selectedInfoSubjectId, Long selectedInfoTypeId, EmployeeData loginEmpData, Date fromDate, Date toDate, Long countryId, Long departmentId, Long domainId, Long employeeId, Long nonEmployeeId, Long assignmentDetailId, Long openSourceId, Long regionId, Long sectorId, Long unitId, String infoDetails, String searchPhoneNumber, String incomingFileNumber, Integer incomingFileYear, String incomingNumber, Date incomingFromDate, Date incomingToDate)
			throws BusinessException {
		DepartmentData dep = DepartmentService.getDepartment(loginEmpData.getActualDepartmentId());

		if (assignmentDetailId == null && openSourceId == null) { // no sources
			if (countryId == null && departmentId == null && domainId == null && employeeId == null && nonEmployeeId == null) { // no sources and related entities
				return searchInfoData(infoNumber, selectedIncomingSideDomainId == null ? FlagsEnum.ALL.getCode() : selectedIncomingSideDomainId, new Long[] { dep.getId() }, fromDate, toDate, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), selectedInfoSubjectId == null ? FlagsEnum.ALL.getCode() : selectedInfoSubjectId, selectedInfoTypeId == null ? FlagsEnum.ALL.getCode() : selectedInfoTypeId, regionId == null ? FlagsEnum.ALL.getCode() : regionId,
						sectorId == null ? FlagsEnum.ALL.getCode() : sectorId, unitId == null ? FlagsEnum.ALL.getCode() : unitId, infoDetails, searchPhoneNumber, incomingFileNumber, incomingFileYear, incomingNumber, incomingFromDate, incomingToDate);
			} else { // related entities but no sources
				return searchInfoData(infoNumber, selectedIncomingSideDomainId == null ? FlagsEnum.ALL.getCode() : selectedIncomingSideDomainId, new Long[] { dep.getId() }, fromDate, toDate, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), selectedInfoSubjectId == null ? FlagsEnum.ALL.getCode() : selectedInfoSubjectId, selectedInfoTypeId == null ? FlagsEnum.ALL.getCode() : selectedInfoTypeId, countryId == null ? FlagsEnum.ALL.getCode() : countryId,
						departmentId == null ? FlagsEnum.ALL.getCode() : departmentId, domainId == null ? FlagsEnum.ALL.getCode() : domainId, employeeId == null ? FlagsEnum.ALL.getCode() : employeeId, nonEmployeeId == null ? FlagsEnum.ALL.getCode() : nonEmployeeId, regionId == null ? FlagsEnum.ALL.getCode() : regionId, sectorId == null ? FlagsEnum.ALL.getCode() : sectorId, unitId == null ? FlagsEnum.ALL.getCode() : unitId, infoDetails, searchPhoneNumber, incomingFileNumber, incomingFileYear, incomingNumber, incomingFromDate, incomingToDate);
			}
		} else { // sources
			Long officerId = null;
			String identity = null;
			if (assignmentDetailId != null) {
				AssignmentDetailData assignement = AssignmentService.getAssignmentDetailsDataById(assignmentDetailId);
				officerId = assignement.getOfficerId();
				identity = assignement.getIdentity();
			}
			if (countryId == null && departmentId == null && domainId == null && employeeId == null && nonEmployeeId == null) { // sources and related entities
				return searchInfoData(infoNumber, selectedIncomingSideDomainId == null ? FlagsEnum.ALL.getCode() : selectedIncomingSideDomainId, new Long[] { dep.getId() }, fromDate, toDate, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), selectedInfoSubjectId == null ? FlagsEnum.ALL.getCode() : selectedInfoSubjectId, selectedInfoTypeId == null ? FlagsEnum.ALL.getCode() : selectedInfoTypeId, assignmentDetailId == null ? FlagsEnum.ALL.getCode() : assignmentDetailId,
						openSourceId == null ? FlagsEnum.ALL.getCode() : openSourceId, regionId == null ? FlagsEnum.ALL.getCode() : regionId, sectorId == null ? FlagsEnum.ALL.getCode() : sectorId, unitId == null ? FlagsEnum.ALL.getCode() : unitId, infoDetails, searchPhoneNumber, incomingFileNumber, incomingFileYear, officerId == null ? FlagsEnum.ALL.getCode() : officerId, identity, incomingNumber, incomingFromDate, incomingToDate);
			} else { // related entities and sources
				return searchInfoData(infoNumber, selectedIncomingSideDomainId == null ? FlagsEnum.ALL.getCode() : selectedIncomingSideDomainId, new Long[] { dep.getId() }, fromDate, toDate, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), selectedInfoSubjectId == null ? FlagsEnum.ALL.getCode() : selectedInfoSubjectId, selectedInfoTypeId == null ? FlagsEnum.ALL.getCode() : selectedInfoTypeId, countryId == null ? FlagsEnum.ALL.getCode() : countryId,
						departmentId == null ? FlagsEnum.ALL.getCode() : departmentId, domainId == null ? FlagsEnum.ALL.getCode() : domainId, employeeId == null ? FlagsEnum.ALL.getCode() : employeeId, nonEmployeeId == null ? FlagsEnum.ALL.getCode() : nonEmployeeId, assignmentDetailId == null ? FlagsEnum.ALL.getCode() : assignmentDetailId, openSourceId == null ? FlagsEnum.ALL.getCode() : openSourceId, regionId == null ? FlagsEnum.ALL.getCode() : regionId,
						sectorId == null ? FlagsEnum.ALL.getCode() : sectorId, unitId == null ? FlagsEnum.ALL.getCode() : unitId, infoDetails, searchPhoneNumber, incomingFileNumber, incomingFileYear, officerId == null ? FlagsEnum.ALL.getCode() : officerId, identity, incomingNumber, incomingFromDate, incomingToDate);
			}
		}
	}

	/**
	 * Get info data by info number
	 * 
	 * @param infoNumber
	 * @return list of info data
	 * @throws BusinessException
	 */
	public static List<InfoData> getAllInfoData(String infoNumber) throws BusinessException {
		return searchInfoData(infoNumber, FlagsEnum.ALL.getCode(), new Long[] { WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId() }, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, null, null, null, null, null, null);

	}

	/**
	 * Get infoData By InstanceId
	 * 
	 * @param instanceId
	 * @return list of info data
	 * @throws BusinessException
	 */
	public static InfoData getInfoDataByInstanceId(long instanceId) throws BusinessException {
		return searchInfoData(null, FlagsEnum.ALL.getCode(), null, null, null, instanceId, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, null, null, null, null, null, null).get(0);
	}

	/**
	 * get infoData By id
	 * 
	 * @param id
	 * @return list of info data
	 * @throws BusinessException
	 */
	public static InfoData getInfoDataById(long id) throws BusinessException {
		return searchInfoData(null, FlagsEnum.ALL.getCode(), null, null, null, FlagsEnum.ALL.getCode(), id, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, null, null, null, null, null, null).get(0);
	}

	/**
	 * Get all information by subject id and type id
	 * 
	 * @param infoSubjectId
	 * @param infoTypeId
	 * @return list of info data
	 * @throws BusinessException
	 */
	public static List<InfoData> getInfoDataBySubjectIdAndTypeId(long infoSubjectId, long infoTypeId, Date saveDate, EmployeeData loginEmpData) throws BusinessException {
		DepartmentData dep = DepartmentService.getDepartment(loginEmpData.getActualDepartmentId());
		return searchInfoData(null, FlagsEnum.ALL.getCode(), new Long[] { dep.getRegionId(), dep.getId() }, null, saveDate, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), infoSubjectId, infoTypeId, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, null, null, null, null, null, null);
	}

	/**
	 * Get all information by subject id and type id
	 * 
	 * @param infoSubjectId
	 * @param infoTypeId
	 * @return list of info data
	 * @throws BusinessException
	 */
	public static List<InfoData> getInfoDataBySubjectIdAndTypeIdByAnalysis(long infoSubjectId, long infoTypeId, Date saveDate, EmployeeData loginEmpData) throws BusinessException {
		return searchInfoData(null, FlagsEnum.ALL.getCode(), new Long[] { loginEmpData.getActualDepartmentId() }, null, saveDate, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), infoSubjectId, infoTypeId, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, null, null, null, null, null, null);
	}

	/**
	 * Get info data by info related data
	 * 
	 * @param countryId
	 * @param departmentId
	 * @param domainId
	 * @param employeeId
	 * @param nonEmployeeId
	 * @return list of info data
	 * @throws BusinessException
	 */
	public static List<InfoData> getInfoDataRelatedEntity(long countryId, long departmentId, long domainId, long employeeId, long nonEmployeeId, Date saveDate, EmployeeData loginEmpData) throws BusinessException {
		try {
			DepartmentData dep = DepartmentService.getDepartment(loginEmpData.getActualDepartmentId());
			return searchInfoRelatedEntity(countryId, departmentId, domainId, employeeId, nonEmployeeId, saveDate, new Long[] { dep.getRegionId(), dep.getId() });
		} catch (NoDataException e) {
			return new ArrayList<InfoData>();
		}
	}

	/**
	 * getInfoDataRelatedEntityByAnalysis
	 * 
	 * @param countryId
	 * @param departmentId
	 * @param domainId
	 * @param employeeId
	 * @param nonEmployeeId
	 * @param saveDate
	 * @param loginEmpData
	 * @return
	 * @throws BusinessException
	 */
	public static List<InfoData> getInfoDataRelatedEntityByAnalysis(long countryId, long departmentId, long domainId, long employeeId, long nonEmployeeId, Date saveDate, EmployeeData loginEmpData) throws BusinessException {
		try {
			return searchInfoRelatedEntity(countryId, departmentId, domainId, employeeId, nonEmployeeId, saveDate, new Long[] { loginEmpData.getActualDepartmentId() });
		} catch (NoDataException e) {
			return new ArrayList<InfoData>();
		}
	}

	/**
	 * Get all information by agentCode and infoNumber
	 * 
	 * @param agentCode
	 * @param infoNumber
	 * @return list of info data
	 * @throws BusinessException
	 */
	public static List<InfoData> getInfoDataByAgentCodeAndInfoNumber(String agentCode, String infoNumber, EmployeeData loginEmpData) throws BusinessException {
		try {
			DepartmentData dep = DepartmentService.getDepartment(loginEmpData.getActualDepartmentId());
			return searchInfoAssignment(agentCode, infoNumber, FlagsEnum.ALL.getCode(), null, new Long[] { dep.getRegionId(), dep.getId() }, FlagsEnum.ALL.getCode(), null);
		} catch (NoDataException e) {
			return new ArrayList<InfoData>();
		}
	}

	/**
	 * getInfoDataByAgentCodeAndInfoNumberByAnalysis
	 * 
	 * @param agentCode
	 * @param infoNumber
	 * @param loginEmpData
	 * @return
	 * @throws BusinessException
	 */
	public static List<InfoData> getInfoDataByAgentCodeAndInfoNumberByAnalysis(String agentCode, String infoNumber, EmployeeData loginEmpData) throws BusinessException {
		try {
			return searchInfoAssignment(agentCode, infoNumber, FlagsEnum.ALL.getCode(), null, new Long[] { loginEmpData.getActualDepartmentId() }, FlagsEnum.ALL.getCode(), null);
		} catch (NoDataException e) {
			return new ArrayList<InfoData>();
		}
	}

	/**
	 * Get all information by assignmentId
	 * 
	 * @param assignmentDetailId
	 * @return list of info data
	 * @throws BusinessException
	 */
	public static List<InfoData> getInfoDataByAssignmentId(long assignmentDetailId, Date saveDate, EmployeeData loginEmpData) throws BusinessException {
		try {
			DepartmentData dep = DepartmentService.getDepartment(loginEmpData.getActualDepartmentId());
			return searchInfoAssignment(null, null, assignmentDetailId, saveDate, new Long[] { dep.getRegionId(), dep.getId() }, FlagsEnum.ALL.getCode(), null);
		} catch (NoDataException e) {
			return new ArrayList<InfoData>();
		}
	}

	/**
	 * 
	 * @param officerId
	 * @param identity
	 * @param loginEmpData
	 * @return
	 * @throws BusinessException
	 */
	public static List<InfoData> getInfoDataBySourceIdentityAndOfficerId(Long officerId, String identity, EmployeeData loginEmpData) throws BusinessException {
		try {
			DepartmentData dep = DepartmentService.getDepartment(loginEmpData.getActualDepartmentId());
			return searchInfoAssignment(null, null, FlagsEnum.ALL.getCode(), null, new Long[] { dep.getRegionId(), dep.getId() }, officerId, identity);
		} catch (NoDataException e) {
			return new ArrayList<InfoData>();
		}
	}

	/**
	 * Get all information by assignmentId
	 * 
	 * @param assignmentDetailId
	 * @return list of info data
	 * @throws BusinessException
	 */
	public static List<InfoData> getInfoDataByAssignmentIdByAnalysis(long assignmentDetailId, Date saveDate, EmployeeData loginEmpData) throws BusinessException {
		try {
			return searchInfoAssignment(null, null, assignmentDetailId, saveDate, new Long[] { loginEmpData.getActualDepartmentId() }, FlagsEnum.ALL.getCode(), null);
		} catch (NoDataException e) {
			return new ArrayList<InfoData>();
		}
	}

	/**
	 * Get all information by phoneNumber
	 * 
	 * @param phoneNumber
	 * @return list of info data
	 * @throws BusinessException
	 */
	public static List<InfoData> getInfoPhone(String phoneNumber, EmployeeData loginEmpData) throws BusinessException {
		try {
			DepartmentData dep = DepartmentService.getDepartment(loginEmpData.getActualDepartmentId());
			return searchInfoPhone(phoneNumber, new Long[] { dep.getRegionId(), dep.getId() });
		} catch (NoDataException e) {
			return new ArrayList<InfoData>();
		}
	}

	/**
	 * Get all information by phoneNumber
	 * 
	 * @param phoneNumber
	 * @return list of info data
	 * @throws BusinessException
	 */
	public static List<InfoData> getInfoPhoneByAnalysis(String phoneNumber, EmployeeData loginEmpData) throws BusinessException {
		try {
			return searchInfoPhone(phoneNumber, new Long[] { loginEmpData.getActualDepartmentId() });
		} catch (NoDataException e) {
			return new ArrayList<InfoData>();
		}
	}

	/**
	 * Get all information by openSourceId
	 * 
	 * @param openSourceId
	 * @return list of info data
	 * @throws BusinessException
	 */
	public static List<InfoData> getInfoDataByOpenSourceId(long openSourceId, Date saveDate, EmployeeData loginEmpData) throws BusinessException {
		try {
			DepartmentData dep = DepartmentService.getDepartment(loginEmpData.getActualDepartmentId());
			return searchInfoOpenSource(openSourceId, saveDate, new Long[] { dep.getRegionId(), dep.getId() });
		} catch (NoDataException e) {
			return new ArrayList<InfoData>();
		}
	}

	/**
	 * Get all information by openSourceId
	 * 
	 * @param openSourceId
	 * @return list of info data
	 * @throws BusinessException
	 */
	public static List<InfoData> getInfoDataByOpenSourceIdByAnalysis(long openSourceId, Date saveDate, EmployeeData loginEmpData) throws BusinessException {
		try {
			return searchInfoOpenSource(openSourceId, saveDate, new Long[] { loginEmpData.getActualDepartmentId() });
		} catch (NoDataException e) {
			return new ArrayList<InfoData>();
		}
	}

	/**********************************************/

	/******************* Queries *****************/

	/**
	 * 
	 * @param infoNumber
	 * @param domainIncomingSideId
	 * @param infoVisibleDepList
	 * @param fromDate
	 * @param toDate
	 * @param instanceId
	 * @param infoId
	 * @param infoSubjectId
	 * @param infoTypeId
	 * @param regionId
	 * @param sectorId
	 * @param unitId
	 * @param infoDetails
	 * @param searchPhoneNumber
	 * @param incomingFileNumber
	 * @param incomingFileYear
	 * @param incomingNumber
	 * @param incomingFromDate
	 * @param incomingToDate
	 * @return
	 * @throws BusinessException
	 */
	private static List<InfoData> searchInfoData(String infoNumber, long domainIncomingSideId, Long[] infoVisibleDepList, Date fromDate, Date toDate, long instanceId, long infoId, long infoSubjectId, long infoTypeId, long regionId, long sectorId, long unitId, String infoDetails, String searchPhoneNumber, String incomingFileNumber, Integer incomingFileYear, String incomingNumber, Date incomingFromDate, Date incomingToDate) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INFO_NUMBER", infoNumber == null || infoNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : infoNumber);
			qParams.put("P_INFO_DETAILS", infoDetails == null || infoDetails.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + infoDetails + "%");
			qParams.put("P_DOMAIN_INCOMING_SIDE_ID", domainIncomingSideId);
			qParams.put("P_FROM_DATE_NULL", fromDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_TO_DATE_NULL", toDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_FROM_DATE", fromDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(fromDate));
			qParams.put("P_TO_DATE", toDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(toDate));
			qParams.put("P_VISIBLE_DPT_LIST_SIZE", infoVisibleDepList == null ? 0 : infoVisibleDepList.length);
			qParams.put("P_VISIBLE_DPT_LIST", infoVisibleDepList == null || infoVisibleDepList.length == 0 ? new Long[] { -1L } : infoVisibleDepList);
			qParams.put("P_ID", infoId);
			qParams.put("P_INSTANCE_ID", instanceId);
			qParams.put("P_INFO_SUBJECT_ID", infoSubjectId);
			qParams.put("P_INFO_TYPE_ID", infoTypeId);
			qParams.put("P_REGION_ID", regionId);
			qParams.put("P_SECTOR_ID", sectorId);
			qParams.put("P_UNIT_ID", unitId);
			qParams.put("P_PHONE_NUMBER", searchPhoneNumber == null || searchPhoneNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : searchPhoneNumber);
			qParams.put("P_INCOMING_FILE_NUMBER", incomingFileNumber == null || incomingFileNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + incomingFileNumber + "%");
			qParams.put("P_INCOMING_FILE_YEAR", incomingFileYear == null ? FlagsEnum.ALL.getCode() : incomingFileYear);
			qParams.put("P_INCOMING_NUMBER", incomingNumber == null || incomingNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + incomingNumber + "%");
			qParams.put("P_INCOMING_FROM_DATE_NULL", incomingFromDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_INCOMING_FROM_DATE", incomingFromDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(incomingFromDate));
			qParams.put("P_INCOMING_TO_DATE_NULL", incomingToDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_INCOMING_TO_DATE", incomingToDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(incomingToDate));

			return DataAccess.executeNamedQuery(InfoData.class, QueryNamesEnum.INFO_DATA_SEARCH_INFO.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return new ArrayList<InfoData>();
		}
	}

	/**
	 * 
	 * @param infoNumber
	 * @param domainIncomingSideId
	 * @param infoVisibleDepList
	 * @param fromDate
	 * @param toDate
	 * @param instanceId
	 * @param infoId
	 * @param infoSubjectId
	 * @param infoTypeId
	 * @param countryId
	 * @param departmentId
	 * @param domainId
	 * @param employeeId
	 * @param nonEmployeeId
	 * @param assignmentDetailId
	 * @param openSourceId
	 * @param regionId
	 * @param sectorId
	 * @param unitId
	 * @param infoDetails
	 * @param searchPhoneNumber
	 * @param incomingFileNumber
	 * @param incomingFileYear
	 * @param officerId
	 * @param identity
	 * @param incomingNumber
	 * @param incomingFromDate
	 * @param incomingToDate
	 * @return
	 * @throws BusinessException
	 */
	private static List<InfoData> searchInfoData(String infoNumber, long domainIncomingSideId, Long[] infoVisibleDepList, Date fromDate, Date toDate, long instanceId, long infoId, long infoSubjectId, long infoTypeId, long countryId, long departmentId, long domainId, long employeeId, long nonEmployeeId, long assignmentDetailId, long openSourceId, long regionId, long sectorId, long unitId, String infoDetails, String searchPhoneNumber, String incomingFileNumber, Integer incomingFileYear,
			long officerId, String identity, String incomingNumber, Date incomingFromDate, Date incomingToDate) throws BusinessException {
		try {

			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INFO_NUMBER", infoNumber == null || infoNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : infoNumber);
			qParams.put("P_INFO_DETAILS", infoDetails == null || infoDetails.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + infoDetails + "%");
			qParams.put("P_DOMAIN_INCOMING_SIDE_ID", domainIncomingSideId);
			qParams.put("P_FROM_DATE_NULL", fromDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_TO_DATE_NULL", toDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_FROM_DATE", fromDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(fromDate));
			qParams.put("P_TO_DATE", toDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(toDate));
			qParams.put("P_VISIBLE_DPT_LIST_SIZE", infoVisibleDepList == null ? 0 : infoVisibleDepList.length);
			qParams.put("P_VISIBLE_DPT_LIST", infoVisibleDepList == null || infoVisibleDepList.length == 0 ? new Long[] { -1L } : infoVisibleDepList);
			qParams.put("P_ID", infoId);
			qParams.put("P_INSTANCE_ID", instanceId);
			qParams.put("P_INFO_SUBJECT_ID", infoSubjectId);
			qParams.put("P_INFO_TYPE_ID", infoTypeId);

			qParams.put("P_COUNTRY_ID", countryId);
			qParams.put("P_DEPARTMENT_ID", departmentId);
			qParams.put("P_DOMAIN_ID", domainId);
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_NON_EMPLOYEE_ID", nonEmployeeId);

			qParams.put("P_ASSIGNMENT_DETAIL_ID", assignmentDetailId);
			qParams.put("P_OPEN_SOURCE_ID", openSourceId);

			qParams.put("P_OOFFICER_ID", openSourceId);
			qParams.put("P_IDENTITY", identity == null || identity.isEmpty() ? FlagsEnum.ALL.getCode() : identity);

			qParams.put("P_REGION_ID", regionId);
			qParams.put("P_SECTOR_ID", sectorId);
			qParams.put("P_UNIT_ID", unitId);
			qParams.put("P_PHONE_NUMBER", searchPhoneNumber == null || searchPhoneNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : searchPhoneNumber);

			qParams.put("P_INCOMING_FILE_NUMBER", incomingFileNumber == null || incomingFileNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + incomingFileNumber + "%");
			qParams.put("P_INCOMING_FILE_YEAR", incomingFileYear == null ? FlagsEnum.ALL.getCode() : incomingFileYear);

			qParams.put("P_INCOMING_NUMBER", incomingNumber == null || incomingNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + incomingNumber + "%");
			qParams.put("P_INCOMING_FROM_DATE_NULL", incomingFromDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_INCOMING_FROM_DATE", incomingFromDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(incomingFromDate));
			qParams.put("P_INCOMING_TO_DATE_NULL", incomingToDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_INCOMING_TO_DATE", incomingToDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(incomingToDate));

			return DataAccess.executeNamedQuery(InfoData.class, QueryNamesEnum.INFO_DATA_SEARCH_INFO_WITH_RELATED_ENTITIES_AND_SOURCES.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return new ArrayList<InfoData>();
		}
	}

	/**
	 * 
	 * @param infoNumber
	 * @param domainIncomingSideId
	 * @param infoVisibleDepList
	 * @param fromDate
	 * @param toDate
	 * @param instanceId
	 * @param infoId
	 * @param infoSubjectId
	 * @param infoTypeId
	 * @param countryId
	 * @param departmentId
	 * @param domainId
	 * @param employeeId
	 * @param nonEmployeeId
	 * @param regionId
	 * @param sectorId
	 * @param unitId
	 * @param infoDetails
	 * @param searchPhoneNumber
	 * @param incomingFileNumber
	 * @param incomingFileYear
	 * @param incomingNumber
	 * @param incomingFromDate
	 * @param incomingToDate
	 * @return
	 * @throws BusinessException
	 */
	private static List<InfoData> searchInfoData(String infoNumber, long domainIncomingSideId, Long[] infoVisibleDepList, Date fromDate, Date toDate, long instanceId, long infoId, long infoSubjectId, long infoTypeId, long countryId, long departmentId, long domainId, long employeeId, long nonEmployeeId, long regionId, long sectorId, long unitId, String infoDetails, String searchPhoneNumber, String incomingFileNumber, Integer incomingFileYear, String incomingNumber, Date incomingFromDate, Date incomingToDate) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INFO_NUMBER", infoNumber == null || infoNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : infoNumber);
			qParams.put("P_INFO_DETAILS", infoDetails == null || infoDetails.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + infoDetails + "%");
			qParams.put("P_DOMAIN_INCOMING_SIDE_ID", domainIncomingSideId);
			qParams.put("P_FROM_DATE_NULL", fromDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_TO_DATE_NULL", toDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_FROM_DATE", fromDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(fromDate));
			qParams.put("P_TO_DATE", toDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(toDate));
			qParams.put("P_VISIBLE_DPT_LIST_SIZE", infoVisibleDepList == null ? 0 : infoVisibleDepList.length);
			qParams.put("P_VISIBLE_DPT_LIST", infoVisibleDepList == null || infoVisibleDepList.length == 0 ? new Long[] { -1L } : infoVisibleDepList);
			qParams.put("P_ID", infoId);
			qParams.put("P_INSTANCE_ID", instanceId);
			qParams.put("P_INFO_SUBJECT_ID", infoSubjectId);
			qParams.put("P_INFO_TYPE_ID", infoTypeId);

			qParams.put("P_COUNTRY_ID", countryId);
			qParams.put("P_DEPARTMENT_ID", departmentId);
			qParams.put("P_DOMAIN_ID", domainId);
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_NON_EMPLOYEE_ID", nonEmployeeId);

			qParams.put("P_REGION_ID", regionId);
			qParams.put("P_SECTOR_ID", sectorId);
			qParams.put("P_UNIT_ID", unitId);
			qParams.put("P_PHONE_NUMBER", searchPhoneNumber == null || searchPhoneNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : searchPhoneNumber);

			qParams.put("P_INCOMING_FILE_NUMBER", incomingFileNumber == null || incomingFileNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + incomingFileNumber + "%");
			qParams.put("P_INCOMING_FILE_YEAR", incomingFileYear == null ? FlagsEnum.ALL.getCode() : incomingFileYear);
			
			qParams.put("P_INCOMING_NUMBER", incomingNumber == null || incomingNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + incomingNumber + "%");
			qParams.put("P_INCOMING_FROM_DATE_NULL", incomingFromDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_INCOMING_FROM_DATE", incomingFromDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(incomingFromDate));
			qParams.put("P_INCOMING_TO_DATE_NULL", incomingToDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_INCOMING_TO_DATE", incomingToDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(incomingToDate));

			return DataAccess.executeNamedQuery(InfoData.class, QueryNamesEnum.INFO_DATA_SEARCH_INFO_WITH_RELATED_ENTITIES.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return new ArrayList<InfoData>();
		}
	}

	/**
	 * 
	 * @param infoNumber
	 * @param domainIncomingSideId
	 * @param infoVisibleDepList
	 * @param fromDate
	 * @param toDate
	 * @param instanceId
	 * @param infoId
	 * @param infoSubjectId
	 * @param infoTypeId
	 * @param assignmentDetailId
	 * @param openSourceId
	 * @param regionId
	 * @param sectorId
	 * @param unitId
	 * @param infoDetails
	 * @param searchPhoneNumber
	 * @param incomingFileNumber
	 * @param incomingFileYear
	 * @param officerId
	 * @param identity
	 * @param incomingNumber
	 * @param incomingFromDate
	 * @param incomingToDate
	 * @return
	 * @throws BusinessException
	 */
	private static List<InfoData> searchInfoData(String infoNumber, long domainIncomingSideId, Long[] infoVisibleDepList, Date fromDate, Date toDate, long instanceId, long infoId, long infoSubjectId, long infoTypeId, long assignmentDetailId, long openSourceId, long regionId, long sectorId, long unitId, String infoDetails, String searchPhoneNumber, String incomingFileNumber, Integer incomingFileYear, long officerId, String identity, String incomingNumber, Date incomingFromDate, Date incomingToDate) throws BusinessException {
		try {

			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INFO_NUMBER", infoNumber == null || infoNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : infoNumber);
			qParams.put("P_INFO_DETAILS", infoDetails == null || infoDetails.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + infoDetails + "%");
			qParams.put("P_DOMAIN_INCOMING_SIDE_ID", domainIncomingSideId);
			qParams.put("P_FROM_DATE_NULL", fromDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_TO_DATE_NULL", toDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_FROM_DATE", fromDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(fromDate));
			qParams.put("P_TO_DATE", toDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(toDate));
			qParams.put("P_VISIBLE_DPT_LIST_SIZE", infoVisibleDepList == null ? 0 : infoVisibleDepList.length);
			qParams.put("P_VISIBLE_DPT_LIST", infoVisibleDepList == null || infoVisibleDepList.length == 0 ? new Long[] { -1L } : infoVisibleDepList);
			qParams.put("P_ID", infoId);
			qParams.put("P_INSTANCE_ID", instanceId);
			qParams.put("P_INFO_SUBJECT_ID", infoSubjectId);
			qParams.put("P_INFO_TYPE_ID", infoTypeId);

			qParams.put("P_ASSIGNMENT_DETAIL_ID", assignmentDetailId);
			qParams.put("P_OPEN_SOURCE_ID", openSourceId);
			qParams.put("P_OFFICER_ID", officerId);
			qParams.put("P_IDENTITY", identity == null || identity.isEmpty() ? FlagsEnum.ALL.getCode() + "" : identity);

			qParams.put("P_REGION_ID", regionId);
			qParams.put("P_SECTOR_ID", sectorId);
			qParams.put("P_UNIT_ID", unitId);
			qParams.put("P_PHONE_NUMBER", searchPhoneNumber == null || searchPhoneNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : searchPhoneNumber);

			qParams.put("P_INCOMING_FILE_NUMBER", incomingFileNumber == null || incomingFileNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + incomingFileNumber + "%");
			qParams.put("P_INCOMING_FILE_YEAR", incomingFileYear == null ? FlagsEnum.ALL.getCode() : incomingFileYear);
			
			qParams.put("P_INCOMING_NUMBER", incomingNumber == null || incomingNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + incomingNumber + "%");
			qParams.put("P_INCOMING_FROM_DATE_NULL", incomingFromDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_INCOMING_FROM_DATE", incomingFromDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(incomingFromDate));
			qParams.put("P_INCOMING_TO_DATE_NULL", incomingToDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_INCOMING_TO_DATE", incomingToDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(incomingToDate));

			return DataAccess.executeNamedQuery(InfoData.class, QueryNamesEnum.INFO_DATA_SEARCH_INFO_WITH_SOURCES.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return new ArrayList<InfoData>();
		}
	}

	/**
	 * Count info data
	 * 
	 * @param employeeId
	 * @return count of info data
	 * @throws BusinessException
	 */
	public static Long getCountInfoRelatedEntity(long employeeId) throws BusinessException {
		return countInfoRelatedEntity(employeeId);
	}

	/**
	 * Count info data
	 * 
	 * @param employeeId
	 * @return count of info data
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static Long countInfoRelatedEntity(long employeeId) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_EMPLOYEE_ID", employeeId);
			return DataAccess.executeNamedQuery(Long.class, QueryNamesEnum.INFO_DATA_COUNT_INFO_RELATED_ENTITY.getCode(), qParams).get(0);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return 0L;
		}
	}

	/**
	 * Search info data related entities
	 * 
	 * @param countryId
	 * @param departmentId
	 * @param domainId
	 * @param employeeId
	 * @param nonEmployeeId
	 * @return list of info data
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<InfoData> searchInfoRelatedEntity(long countryId, long departmentId, long domainId, long employeeId, long nonEmployeeId, Date saveDate, Long[] infoVisibleDepList) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_COUNTRY_ID", countryId);
			qParams.put("P_DEPARTMENT_ID", departmentId);
			qParams.put("P_DOMAIN_ID", domainId);
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_NON_EMPLOYEE_ID", nonEmployeeId);
			qParams.put("P_SAVE_DATE_NULL", saveDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_SAVE_DATE", saveDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(saveDate));
			qParams.put("P_VISIBLE_DPT_LIST_SIZE", infoVisibleDepList == null ? 0 : infoVisibleDepList.length);
			qParams.put("P_VISIBLE_DPT_LIST", infoVisibleDepList == null || infoVisibleDepList.length == 0 ? new Long[] { -1L } : infoVisibleDepList);

			return DataAccess.executeNamedQuery(InfoData.class, QueryNamesEnum.INFO_DATA_SEARCH_INFO_RELATED_ENTITY.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Search info data by agent code and info number and assignment id
	 * 
	 * @param agentCode
	 * @param infoNumber
	 * @param assignmentId
	 * @return list of info data
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<InfoData> searchInfoAssignment(String agentCode, String infoNumber, long assignmentDetailId, Date saveDate, Long[] infoVisibleDepList, long officerId, String identity) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ASSIGNMENT_DETAIL_ID", assignmentDetailId);
			qParams.put("P_INFO_NUMBER", infoNumber == null || infoNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : infoNumber);
			qParams.put("P_AGENT_CODE", agentCode == null || agentCode.isEmpty() ? FlagsEnum.ALL.getCode() + "" : agentCode);
			qParams.put("P_SAVE_DATE_NULL", saveDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_SAVE_DATE", saveDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(saveDate));
			qParams.put("P_VISIBLE_DPT_LIST_SIZE", infoVisibleDepList == null ? 0 : infoVisibleDepList.length);
			qParams.put("P_VISIBLE_DPT_LIST", infoVisibleDepList == null || infoVisibleDepList.length == 0 ? new Long[] { -1L } : infoVisibleDepList);
			qParams.put("P_OFFICER_ID", officerId);
			qParams.put("P_IDENTITY", identity == null || identity.isEmpty() ? FlagsEnum.ALL.getCode() + "" : identity);

			return DataAccess.executeNamedQuery(InfoData.class, QueryNamesEnum.INFO_DATA_SEARCH_INFO_ASSIGNMENT.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Search info data by phone number
	 * 
	 * @param phoneNumber
	 * @param infoVisibleDepList
	 * @return list of info data
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<InfoData> searchInfoPhone(String phoneNumber, Long[] infoVisibleDepList) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_PHONE_NUMBER", phoneNumber);
			qParams.put("P_VISIBLE_DPT_LIST_SIZE", infoVisibleDepList == null ? 0 : infoVisibleDepList.length);
			qParams.put("P_VISIBLE_DPT_LIST", infoVisibleDepList == null || infoVisibleDepList.length == 0 ? new Long[] { -1L } : infoVisibleDepList);

			return DataAccess.executeNamedQuery(InfoData.class, QueryNamesEnum.INFO_DATA_SEARCH_INFO_PHONE.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Search info data by open source Id
	 * 
	 * @param openSourceId
	 * @return list of info data
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<InfoData> searchInfoOpenSource(long openSourceId, Date saveDate, Long[] infoVisibleDepList) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_OPEN_SOURCE_ID", openSourceId);
			qParams.put("P_SAVE_DATE_NULL", saveDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_SAVE_DATE", saveDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(saveDate));
			qParams.put("P_VISIBLE_DPT_LIST_SIZE", infoVisibleDepList == null ? 0 : infoVisibleDepList.length);
			qParams.put("P_VISIBLE_DPT_LIST", infoVisibleDepList == null || infoVisibleDepList.length == 0 ? new Long[] { -1L } : infoVisibleDepList);

			return DataAccess.executeNamedQuery(InfoData.class, QueryNamesEnum.INFO_DATA_SEARCH_INFO_OPEN_SOURCE.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Search info sources
	 * 
	 * @param infoId
	 * @param assignmentId
	 * @param openSourceId
	 * @return list of info sources
	 * @throws BusinessException
	 */
	private static List<InfoSource> searchInfoSources(long infoId, long assignmentDetailId, long openSourceId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INFO_ID", infoId);
			qParams.put("P_ASSIGNMENT_DETAIL_ID", assignmentDetailId);
			qParams.put("P_OPEN_SOURCE_ID", openSourceId);
			return DataAccess.executeNamedQuery(InfoSource.class, QueryNamesEnum.INFO_SOURCE_SEARCH_INFO_SOURCE.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Search info coordinates
	 * 
	 * @param infoId
	 * @return list of info coordinates
	 * @throws BusinessException
	 */
	private static List<InfoCoordinate> searchInfoCoordinates(long infoId) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INFO_ID", infoId);
			return DataAccess.executeNamedQuery(InfoCoordinate.class, QueryNamesEnum.INFO_COORDINATE_SEARCH_INFO_COORDINATE.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return new ArrayList<InfoCoordinate>();
		}
	}

	/**
	 * Search info phones
	 * 
	 * @param infoId
	 * @return list of info phones
	 * @throws BusinessException
	 */
	private static List<InfoPhone> searchInfoPhones(long infoId) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INFO_ID", infoId);

			return DataAccess.executeNamedQuery(InfoPhone.class, QueryNamesEnum.INFO_PHONE_SEARCH_INFO_PHONE.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return new ArrayList<InfoPhone>();
		}
	}

	/**
	 * Search info related entities
	 * 
	 * @param infoId
	 * @param nonEmployeeId
	 * @param employeeId
	 * @param countryId
	 * @param departmentId
	 * @param domainId
	 * @return list of info related entities
	 * @throws BusinessException
	 */
	private static List<InfoRelatedEntity> searchInfoRelatedEntities(long infoId, long nonEmployeeId, long employeeId, long countryId, long departmentId, long domainId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INFO_ID", infoId);
			qParams.put("P_NON_EMPLOYEE_ID", nonEmployeeId);
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_COUNTRY_ID", countryId);
			qParams.put("P_DEPARTMENT_ID", departmentId);
			qParams.put("P_DOMAIN_ID", domainId);

			return DataAccess.executeNamedQuery(InfoRelatedEntity.class, QueryNamesEnum.INFO_RELATED_ENTITY_SEARCH_INFO_RELATED_ENTITY.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_DBError");

		}
	}

	/**
	 * Search info recommendation data
	 * 
	 * @param id
	 * @param infoId
	 * @param labCheck
	 * @param securityCheck
	 * @param employeeId
	 * @param employeeFullName
	 * @param surveillance
	 * @return list of recommendation data
	 * @throws BusinessException
	 */
	private static List<InfoRecommendationData> searchInfoRecommendations(long id, long infoId, int labCheck, int securityCheck, long employeeId, String employeeFullName, int surveillance) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_INFO_ID", infoId);
			qParams.put("P_LAB_CHECK", labCheck);
			qParams.put("P_SECURITY_CHECK", securityCheck);
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_EMPLOYEE_FULL_NAME", employeeFullName == null || employeeFullName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + employeeFullName + "%");
			qParams.put("P_SURVEILLANCE", surveillance);

			return DataAccess.executeNamedQuery(InfoRecommendationData.class, QueryNamesEnum.INFO_RECOMMENDATION_DATA_SEARCH_INFO_RECOMMENDATIONS.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return new ArrayList<InfoRecommendationData>();
		}
	}

	/**
	 * Search department data
	 * 
	 * @param infoId
	 * @return list of department data
	 * @throws BusinessException
	 */
	private static List<DepartmentData> searchInfoVisibleDepartments(long infoId) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INFO_ID", infoId);

			return DataAccess.executeNamedQuery(DepartmentData.class, QueryNamesEnum.INFO_VISIBLE_DEPRATMENT_SEARCH_INFO_VISIBLE_DEPRATMENTS.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return new ArrayList<DepartmentData>();
		}
	}

	/**
	 * Search info analysis detail data
	 * 
	 * @param infoAnalysisId
	 * @return list of info analysis detail data
	 * @throws BusinessException
	 */
	private static List<InfoAnalysisDetailData> searchInfoAnalysisDetails(long infoAnalysisId) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INFO_ANALYSIS_ID", infoAnalysisId);

			return DataAccess.executeNamedQuery(InfoAnalysisDetailData.class, QueryNamesEnum.INFO_ANALYSIS_DETAIL_DATA_SEARCH_INFO_ANALYSIS_DETAILS.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return new ArrayList<InfoAnalysisDetailData>();
		}
	}

	/**
	 * Search info analysis reference data
	 * 
	 * @param infoAnalysisId
	 * @return list of info analysis reference data
	 * @throws BusinessException
	 */
	private static List<InfoAnalysisReferenceData> searchInfoAnalysisReferences(long infoAnalysisId) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INFO_ANALYSIS_ID", infoAnalysisId);

			return DataAccess.executeNamedQuery(InfoAnalysisReferenceData.class, QueryNamesEnum.INFO_ANALYSIS_REFERENCE_DATA_SEARCH_INFO_ANALYSIS_REFERENCES.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return new ArrayList<InfoAnalysisReferenceData>();
		}
	}

	/**
	 * Search info analysis
	 * 
	 * @param infoId
	 * @return list of info analysis
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<InfoAnalysis> searchInfoAnalysis(long infoId, long departmentId, int redirectionType) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INFO_ID", infoId);
			qParams.put("P_DEPARTMENT_ID", departmentId);
			qParams.put("P_REDIRECTION_TYPE", redirectionType);
			return DataAccess.executeNamedQuery(InfoAnalysis.class, QueryNamesEnum.INFO_ANALYSIS_SEARCH_INFO_ANALYSIS.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * 
	 * @param infoId
	 * @throws BusinessException
	 */
	private static void deleteInfoPhones(long infoId, CustomSession useSession) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INFO_ID", infoId);
			DataAccess.updateDeleteNamedQuery(InfoPhone.class, QueryNamesEnum.INFO_PHONE_DELETE_INFO_PHONES.getCode(), qParams, useSession);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {

		}
	}

	/**
	 * 
	 * @param infoId
	 * @throws BusinessException
	 */
	private static void deleteInfoSources(long infoId, CustomSession useSession) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INFO_ID", infoId);

			DataAccess.updateDeleteNamedQuery(InfoSource.class, QueryNamesEnum.INFO_SOURCE_DELETE_INFO_SOURCES.getCode(), qParams, useSession);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {

		}
	}

	/**
	 * 
	 * @param infoId
	 * @throws BusinessException
	 */
	private static void deleteInfoVisibleDepartments(long infoId, CustomSession useSession) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INFO_ID", infoId);

			DataAccess.updateDeleteNamedQuery(InfoVisibleDepartment.class, QueryNamesEnum.INFO_VISIBLE_DEPARTMENT_DELETE_INFO_VISIBLE_DEPARTMENTS.getCode(), qParams, useSession);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {

		}
	}

	/**
	 * 
	 * @param infoId
	 * @throws BusinessException
	 */
	private static void deleteInfoCoordinates(long infoId, CustomSession useSession) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INFO_ID", infoId);

			DataAccess.updateDeleteNamedQuery(InfoCoordinate.class, QueryNamesEnum.INFO_COORDINATE_DELETE_INFO_COORDINATES.getCode(), qParams, useSession);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {

		}
	}

	/**
	 * 
	 * @param infoId
	 * @throws BusinessException
	 */
	private static void deleteInfoRelatedEntities(long infoId, CustomSession useSession) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INFO_ID", infoId);

			DataAccess.updateDeleteNamedQuery(InfoRelatedEntity.class, QueryNamesEnum.INFO_RELATED_ENTITY_DELETE_INFO_RELATED_ENTITIES.getCode(), qParams, useSession);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {

		}
	}

	/**
	 * Get info ids visible for a department
	 * 
	 * @param departmentId
	 * @return List of Long (infoId)
	 * @throws BusinessException
	 */
	public static List<Long> getInfoVisibleDepartment(long departmentId) throws BusinessException {
		try {
			return searchInfoVisibleDepartment(departmentId);
		} catch (NoDataException e) {
			return new ArrayList<Long>();
		}
	}

	/**
	 * Search info ids visible for a department
	 * 
	 * @param departmentId
	 * @return List of Long (infoId)
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<Long> searchInfoVisibleDepartment(long departmentId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_DEPARTMENT_ID", departmentId);
			return DataAccess.executeNamedQuery(Long.class, QueryNamesEnum.INFO_DATA_SEARCH_INFO_VISIBLE_DEPARTMENTS.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(InfoService.class, e, "InfoService");
			throw new BusinessException("error_DBError");
		}
	}

}