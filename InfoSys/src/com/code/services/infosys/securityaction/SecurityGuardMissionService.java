package com.code.services.infosys.securityaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.ReportService;
import com.code.dal.orm.securitymission.MissionCarData;
import com.code.dal.orm.securitymission.MissionEquipmentData;
import com.code.dal.orm.securitymission.MissionGuard;
import com.code.dal.orm.securitymission.MissionGuardData;
import com.code.dal.orm.securitymission.MissionWeaponData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.enums.EntitySequenceGeneratorEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryNamesEnum;
import com.code.enums.ReportNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.log4j.Log4j;
import com.code.services.util.CommonService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.securityaction.SecurityGuardMissionWorkFlow;

public class SecurityGuardMissionService extends BaseService {
	/**
	 * Save new security guard mission with at least one attached employee to the mission in the weapon-employees list and save it's details (cars , weapons , equipment)
	 * 
	 * @param missionGuardData
	 * @param missionWeaponList
	 * @param missionEquipmentList
	 * @param missionCarList
	 * @param loginEmpData
	 * @param verbalOrderFlag
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveSecurityGuardMission(MissionGuardData missionGuardData, List<MissionWeaponData> missionWeaponList, List<MissionEquipmentData> missionEquipmentList, List<MissionCarData> missionCarList, EmployeeData loginEmpData, boolean verbalOrderFlag, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		validateMissionGuardData(missionGuardData.getMissionGuard(), missionWeaponList.size(), verbalOrderFlag);
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			missionGuardData.getMissionGuard().setSystemUser(loginEmpData.getEmpId().toString());
			missionGuardData.setMissionNumber(generateMissionNumber(session));
			missionGuardData.setMissionEndDate(HijriDateService.addSubHijriDays(missionGuardData.getMissionStartDate(), missionGuardData.getMissionPeriod()));
			DataAccess.addEntity(missionGuardData.getMissionGuard(), session);
			missionGuardData.setId(missionGuardData.getMissionGuard().getId());

			validateMissionWeaponDataList(missionWeaponList);
			for (MissionWeaponData missionWeaponData : missionWeaponList) {
				missionWeaponData.setSecurityGuardMissionId(missionGuardData.getMissionGuard().getId());
				missionWeaponData.getMissionWeapon().setSystemUser(loginEmpData.getEmpId().toString());
				DataAccess.addEntity(missionWeaponData.getMissionWeapon(), session);
				missionWeaponData.setId(missionWeaponData.getMissionWeapon().getId());
			}

			validateMissionEquipmentDataList(missionEquipmentList);
			for (MissionEquipmentData missionEquipmentData : missionEquipmentList) {
				missionEquipmentData.setSecurityGuardMissionId(missionGuardData.getMissionGuard().getId());
				missionEquipmentData.getMissionEquipment().setSystemUser(loginEmpData.getEmpId().toString());
				DataAccess.addEntity(missionEquipmentData.getMissionEquipment(), session);
				missionEquipmentData.setId(missionEquipmentData.getMissionEquipment().getId());
			}

			validateMissionCarDataList(missionCarList);
			for (MissionCarData missionCarData : missionCarList) {
				missionCarData.setSecurityGuardMissionId(missionGuardData.getMissionGuard().getId());
				missionCarData.getMissionCar().setSystemUser(loginEmpData.getEmpId().toString());
				DataAccess.addEntity(missionCarData.getMissionCar(), session);
				missionCarData.setId(missionCarData.getMissionCar().getId());
			}

			SecurityGuardMissionWorkFlow.initMissionGuard(missionGuardData, null, loginEmpData, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Update security guard mission
	 * 
	 * @param missionGuardData
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateSecurityGuardMission(MissionGuardData missionGuardData, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			missionGuardData.getMissionGuard().setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.updateEntity(missionGuardData.getMissionGuard(), session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Validate mandatory fields and business rules
	 * 
	 * @param missionGuard
	 * @param employeeListSize
	 * @param verbalOrderFlag
	 * @throws BusinessException
	 */
	public static void validateMissionGuardData(MissionGuard missionGuard, long employeeListSize, boolean verbalOrderFlag) throws BusinessException {
		if (employeeListSize == 0) {
			throw new BusinessException("error_noAssignedEmployeedToMission");
		}

		if (!verbalOrderFlag && (missionGuard.getOrderNumber() == null || (missionGuard.getOrderNumber().trim().isEmpty()))) {
			throw new BusinessException("error_orderNumberMandatory");
		}

		if (!verbalOrderFlag && (missionGuard.getOrderNumber().replace("/", "").equals(generateZerosString(missionGuard.getOrderNumber().replace("/", "").length())))) {
			throw new BusinessException("error_orderNumberCan'tBeZeros");
		}

		if (missionGuard.getOrderDate() == null) {
			throw new BusinessException("error_orderDateMandatory");
		}

		if (missionGuard.getOrderDate().after(HijriDateService.getHijriSysDate())) {
			throw new BusinessException("error_missionOrderDateCantExceedCurrentDate");
		}

		if (missionGuard.getOrderSourceDomainId() == null) {
			throw new BusinessException("error_orderSourceMandatory");
		}

		if (missionGuard.getMissionPeriod() == null) {
			throw new BusinessException("error_missionPeriodMandatory");
		}

		if (missionGuard.getMissionPeriod() <= 0 || missionGuard.getMissionPeriod() > 99999) {
			throw new BusinessException("error_invalidPeriod");
		}

		if (missionGuard.getMissionStartDate() == null) {
			throw new BusinessException("error_missionStartDateMandatory");
		}

		if (missionGuard.getMissionStartDate().after(HijriDateService.getHijriSysDate())) {
			throw new BusinessException("error_missionStartDateCantExceedCurrentDate");
		}

		if (missionGuard.getMissionType() == null) {
			throw new BusinessException("error_missionTypeMandatory");
		}

		if (missionGuard.getDepartmentMissionLocationId() == null) {
			throw new BusinessException("error_missionLocationMandatory");
		}

		if (missionGuard.getMissionPath() == null || (missionGuard.getMissionPath().trim().isEmpty())) {
			throw new BusinessException("error_missionPathMandatory");
		}

		if (missionGuard.getMovementDate() == null) {
			throw new BusinessException("error_movementDateMandatory");
		}

		if (missionGuard.getMissionStartDate().after(missionGuard.getMovementDate())) {
			throw new BusinessException("error_startDateCantExceedMovementDate");
		}

		if (missionGuard.getMovementTime() == null || missionGuard.getMovementTime().trim().isEmpty()) {
			throw new BusinessException("error_movementTimeMandatory");
		}

		if (missionGuard.getMissionEndTime() == null || missionGuard.getMissionEndTime().trim().isEmpty()) {
			throw new BusinessException("error_missionEndTimeMandatory");
		}

		try {
			if (!verbalOrderFlag) {
				searchMissionGuard(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), missionGuard.getOrderNumber());
				throw new BusinessException("error_orderNumberDuplicate");
			}
		} catch (NoDataException e) {

		}
	}

	/**
	 * Validate mission weapons rules
	 * 
	 * @param MissionWeaponDataList
	 * @throws BusinessException
	 */
	private static void validateMissionWeaponDataList(List<MissionWeaponData> MissionWeaponDataList) throws BusinessException {
		for (MissionWeaponData missionWeaponData : MissionWeaponDataList) {
			if (missionWeaponData.getDomainWeaponTypeId() == null) {
				throw new BusinessException("error_weaponTypeMandatory");
			}

			if (missionWeaponData.getWeaponNumber() == null || missionWeaponData.getWeaponNumber().trim().isEmpty()) {
				throw new BusinessException("error_weaponNumberMandatory");
			}

			if (missionWeaponData.getWeaponNumber().replaceAll("[ ,/,\\\\]", "").equals(generateZerosString(missionWeaponData.getWeaponNumber().replaceAll("[ ,/,\\\\]", "").length()))) {
				throw new BusinessException("error_weaponNumberCan'tBeZeros");
			}

			if (missionWeaponData.getAmmoAmount() == null) {
				throw new BusinessException("error_ammoAmountMandatory");
			}

			if (missionWeaponData.getAmmoAmount() <= 0 || missionWeaponData.getAmmoAmount() > 99999) {
				throw new BusinessException("error_invalidAmmoAmount");
			}
		}
	}

	private static void validateMissionCarDataList(List<MissionCarData> missionCarDataList) throws BusinessException {
		for (MissionCarData missionCarData : missionCarDataList) {
			if (missionCarData.getDomainCarModelId() == null) {
				throw new BusinessException("error_carTypeMandatory");
			}

			if (missionCarData.getCarModel() == null || missionCarData.getCarModel().trim().isEmpty()) {
				throw new BusinessException("error_carModelMandatory");
			}

			if (missionCarData.getCarModel().replaceAll("[ ,/,\\\\]", "").equals(generateZerosString(missionCarData.getCarModel().replaceAll("[ ,/,\\\\]", "").length()))) {
				throw new BusinessException("error_carModelCan'tBeZeros");
			}

			if (missionCarData.getPlateNumber() == null || missionCarData.getPlateNumber().trim().isEmpty()) {
				throw new BusinessException("error_plateNumberMandatory");
			}

			if (missionCarData.getPlateNumber().length() > 20 || missionCarData.getPlateNumber().replaceAll("[ ,/,\\\\]", "").equals(generateZerosString(missionCarData.getPlateNumber().replaceAll("[ ,/,\\\\]", "").length()))) {
				throw new BusinessException("error_invalidPlateNumber");
			}

			if (missionCarData.getBorderGuardNumber() == null || missionCarData.getBorderGuardNumber().trim().isEmpty()) {
				throw new BusinessException("error_borderGuardNumberMandatory");
			}

			if (missionCarData.getBorderGuardNumber().length() > 50 || missionCarData.getBorderGuardNumber().replaceAll("[ ,/,\\\\]", "").equals(generateZerosString(missionCarData.getBorderGuardNumber().replaceAll("[ ,/,\\\\]", "").length()))) {
				throw new BusinessException("error_invalidBorderGuardNumber");
			}
		}
	}

	private static void validateMissionEquipmentDataList(List<MissionEquipmentData> missionEquipmentDataList) throws BusinessException {
		for (MissionEquipmentData missionEquipmentData : missionEquipmentDataList) {
			if (missionEquipmentData.getDomainEquipmentTypeId() == null) {
				throw new BusinessException("error_equipmentTypeMandatory");
			}

			if (missionEquipmentData.getEquipmentModel() == null || (missionEquipmentData.getEquipmentModel() != null && missionEquipmentData.getEquipmentModel().trim().isEmpty())) {
				throw new BusinessException("error_equipmentModelMandatory");
			}

			if (missionEquipmentData.getEquipmentModel().replaceAll("[ ,/,\\\\]", "").equals(generateZerosString(missionEquipmentData.getEquipmentModel().replaceAll("[ ,/,\\\\]", "").length()))) {
				throw new BusinessException("error_equipmentModelCan'tBeZeros");
			}

			if (missionEquipmentData.getBorderGuardNumber() == null || (missionEquipmentData.getBorderGuardNumber() != null && missionEquipmentData.getBorderGuardNumber().trim().isEmpty())) {
				throw new BusinessException("error_borderGuardNumberMandatory");
			}

			if (missionEquipmentData.getBorderGuardNumber().length() > 50 || missionEquipmentData.getBorderGuardNumber().replaceAll("[ ,/,\\\\]", "").equals(generateZerosString(missionEquipmentData.getBorderGuardNumber().replaceAll("[ ,/,\\\\]", "").length()))) {
				throw new BusinessException("error_invalidBorderGuardNumber");
			}
		}
	}

	private static String generateZerosString(int size) {
		if (size != 0) {
			char[] zrosarray = new char[size];
			for (int i = 0; i < zrosarray.length; i++) {
				zrosarray[i] = '0';
			}
			return new String(zrosarray);
		}
		return null;
	}

	/**
	 * Set receive date for every mission weapon in the sent list to be the current date
	 * 
	 * @param missionWeaponDataList
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void receiveWeaponList(List<MissionWeaponData> missionWeaponDataList, Date receiveDate, String receiveTime, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {

			if (!isOpenedSession) {
				session.beginTransaction();
			}

			for (MissionWeaponData missionWeaponData : missionWeaponDataList) {
				missionWeaponData.setReceiveDate(receiveDate);
				missionWeaponData.setReceiveTime(receiveTime);
				missionWeaponData.getMissionWeapon().setSystemUser(loginEmpData.getEmpId().toString());
				DataAccess.updateEntity(missionWeaponData.getMissionWeapon(), session);
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
				Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Set return date for every mission weapon in the sent list to be the current date
	 * 
	 * @param missionWeaponDataList
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void returnWeaponList(List<MissionWeaponData> missionWeaponDataList, Date returnDate, String returnTime, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {

			if (!isOpenedSession) {
				session.beginTransaction();
			}

			for (MissionWeaponData missionWeaponData : missionWeaponDataList) {
				missionWeaponData.setReturnDate(returnDate);
				missionWeaponData.setReturnTime(returnTime);
				missionWeaponData.getMissionWeapon().setSystemUser(loginEmpData.getEmpId().toString());
				DataAccess.updateEntity(missionWeaponData.getMissionWeapon(), session);
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
				Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Set receive date for every mission car in the sent list to be the current date
	 * 
	 * @param missionCarDataList
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void receiveCarList(List<MissionCarData> missionCarDataList, Date receiveDate, String receiveTime, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {

			if (!isOpenedSession) {
				session.beginTransaction();
			}

			for (MissionCarData missionCarData : missionCarDataList) {
				missionCarData.setReceiveDate(receiveDate);
				missionCarData.setReceiveTime(receiveTime);
				missionCarData.getMissionCar().setSystemUser(loginEmpData.getEmpId().toString());
				DataAccess.updateEntity(missionCarData.getMissionCar(), session);
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
				Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Set return date for every mission car in the sent list to be the current date
	 * 
	 * @param missionCarDataList
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void returnCarList(List<MissionCarData> missionCarDataList, Date returnDate, String returnTime, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			for (MissionCarData missionCarData : missionCarDataList) {
				missionCarData.setReturnDate(returnDate);
				missionCarData.setReturnTime(returnTime);
				missionCarData.getMissionCar().setSystemUser(loginEmpData.getEmpId().toString());
				DataAccess.updateEntity(missionCarData.getMissionCar(), session);
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
				Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Set receive date for every mission equipment in the sent list to be the current date
	 * 
	 * @param missionEquipmentDataList
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void receiveEquipmentList(List<MissionEquipmentData> missionEquipmentDataList, Date receiveDate, String receiveTime, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {

			if (!isOpenedSession) {
				session.beginTransaction();
			}

			for (MissionEquipmentData missionEquipmentData : missionEquipmentDataList) {
				missionEquipmentData.setReceiveDate(receiveDate);
				missionEquipmentData.setReceiveTime(receiveTime);
				missionEquipmentData.getMissionEquipment().setSystemUser(loginEmpData.getEmpId().toString());
				DataAccess.updateEntity(missionEquipmentData.getMissionEquipment(), session);
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
				Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Set return date for every mission equipment in the sent list to be the current date
	 * 
	 * @param missionEquipmentDataList
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void returnEquipmentList(List<MissionEquipmentData> missionEquipmentDataList, Date returnDate, String returnTime, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {

			if (!isOpenedSession) {
				session.beginTransaction();
			}

			for (MissionEquipmentData missionEquipmentData : missionEquipmentDataList) {
				missionEquipmentData.setReturnDate(returnDate);
				missionEquipmentData.setReturnTime(returnTime);
				missionEquipmentData.getMissionEquipment().setSystemUser(loginEmpData.getEmpId().toString());
				DataAccess.updateEntity(missionEquipmentData.getMissionEquipment(), session);
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
				Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/*************************************************/
	/*************** Queries ************************/

	/**
	 * Get weapon mission data
	 * 
	 * @param missionId
	 * @param employeeId
	 * @param missionNumber
	 * @param orderNumber
	 * @param orderDate
	 * @param missionStartDate
	 * @param missionEndDate
	 * @param missionType
	 * @return
	 * @throws BusinessException
	 */
	public static List<MissionWeaponData> getMissionWeaponData(long missionId, long employeeId, String missionNumber, String orderNumber, Date orderDate, Date missionStartDate, Date missionEndDate, long missionType, int status) throws BusinessException {
		try {
			return searchMissionWeaponData(missionId, employeeId, missionNumber, orderNumber, orderDate, missionStartDate, missionEndDate, missionType, status);
		} catch (NoDataException e) {
			return new ArrayList<MissionWeaponData>();
		}
	}

	/**
	 * search weapon mission data
	 * 
	 * @param employeeId
	 * @param missionNumber
	 * @param orderNumber
	 * @param orderDate
	 * @param missionStartDate
	 * @param missionEndDate
	 * @param missionType
	 * @param status
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	public static List<MissionWeaponData> searchMissionWeaponData(long missionId, long employeeId, String missionNumber, String orderNumber, Date orderDate, Date missionStartDate, Date missionEndDate, long missionType, int status) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_MISSION_ID", missionId);
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_MISSION_NUMBER", missionNumber == null || missionNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : missionNumber);
			qParams.put("P_ORDER_NUMBER", orderNumber == null || orderNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : orderNumber);
			qParams.put("P_ORDER_DATE_NULL", orderDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_ORDER_DATE", orderDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(orderDate));
			qParams.put("P_MISSION_START_DATE_NULL", missionStartDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_MISSION_START_DATE", missionStartDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(missionStartDate));
			qParams.put("P_MISSION_END_DATE_NULL", missionEndDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_MISSION_END_DATE", missionEndDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(missionEndDate));
			qParams.put("P_MISSION_TYPE", missionType);
			qParams.put("P_STATUS", status);

			return DataAccess.executeNamedQuery(MissionWeaponData.class, QueryNamesEnum.MISSION_WEAPON_DATA_SEARCH_MISSION_WEAPON_DATA.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get mission car data
	 * 
	 * @param employeeId
	 * @param missionNumber
	 * @param orderNumber
	 * @param orderDate
	 * @param missionStartDate
	 * @param missionEndDate
	 * @param missionType
	 * @return
	 * @throws BusinessException
	 */
	public static List<MissionCarData> getMissionCarData(long missionId, long employeeId, String missionNumber, String orderNumber, Date orderDate, Date missionStartDate, Date missionEndDate, long missionType, int status) throws BusinessException {
		try {
			return searchMissionCarData(missionId, employeeId, missionNumber, orderNumber, orderDate, missionStartDate, missionEndDate, missionType, status);
		} catch (NoDataException e) {
			return new ArrayList<MissionCarData>();
		}
	}

	/**
	 * Search mission car data
	 * 
	 * @param employeeId
	 * @param missionNumber
	 * @param orderNumber
	 * @param orderDate
	 * @param missionStartDate
	 * @param missionEndDate
	 * @param missionType
	 * @param status
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	public static List<MissionCarData> searchMissionCarData(long missionId, long employeeId, String missionNumber, String orderNumber, Date orderDate, Date missionStartDate, Date missionEndDate, long missionType, int status) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_MISSION_ID", missionId);
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_MISSION_NUMBER", missionNumber == null || missionNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : missionNumber);
			qParams.put("P_ORDER_NUMBER", orderNumber == null || orderNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : orderNumber);
			qParams.put("P_ORDER_DATE_NULL", orderDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_ORDER_DATE", orderDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(orderDate));
			qParams.put("P_MISSION_START_DATE_NULL", missionStartDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_MISSION_START_DATE", missionStartDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(missionStartDate));
			qParams.put("P_MISSION_END_DATE_NULL", missionEndDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_MISSION_END_DATE", missionEndDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(missionEndDate));
			qParams.put("P_MISSION_TYPE", missionType);
			qParams.put("P_STATUS", status);

			return DataAccess.executeNamedQuery(MissionCarData.class, QueryNamesEnum.MISSION_CARS_SEARCH_MISSION_CAR_DATA.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get mission equipment data
	 * 
	 * @param employeeId
	 * @param missionNumber
	 * @param orderNumber
	 * @param orderDate
	 * @param missionStartDate
	 * @param missionEndDate
	 * @param missionType
	 * @return
	 * @throws BusinessException
	 */
	public static List<MissionEquipmentData> getMissionEquipmentData(long missionId, long employeeId, String missionNumber, String orderNumber, Date orderDate, Date missionStartDate, Date missionEndDate, long missionType, int status) throws BusinessException {
		try {
			return searchMissionEquipmentData(missionId, employeeId, missionNumber, orderNumber, orderDate, missionStartDate, missionEndDate, missionType, status);
		} catch (NoDataException e) {
			return new ArrayList<MissionEquipmentData>();
		}
	}

	/**
	 * Search mission equipment data
	 * 
	 * @param employeeId
	 * @param missionNumber
	 * @param orderNumber
	 * @param orderDate
	 * @param missionStartDate
	 * @param missionEndDate
	 * @param missionType
	 * @param status
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<MissionEquipmentData> searchMissionEquipmentData(long missionId, long employeeId, String missionNumber, String orderNumber, Date orderDate, Date missionStartDate, Date missionEndDate, long missionType, int status) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_MISSION_ID", missionId);
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_MISSION_NUMBER", missionNumber == null || missionNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : missionNumber);
			qParams.put("P_ORDER_NUMBER", orderNumber == null || orderNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : orderNumber);
			qParams.put("P_ORDER_DATE_NULL", orderDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_ORDER_DATE", orderDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(orderDate));
			qParams.put("P_MISSION_START_DATE_NULL", missionStartDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_MISSION_START_DATE", missionStartDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(missionStartDate));
			qParams.put("P_MISSION_END_DATE_NULL", missionEndDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_MISSION_END_DATE", missionEndDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(missionEndDate));
			qParams.put("P_MISSION_TYPE", missionType);
			qParams.put("P_STATUS", status);

			return DataAccess.executeNamedQuery(MissionEquipmentData.class, QueryNamesEnum.MISSION_EQUEPMENT_SEARCH_MISSION_EQUEPMENT_DATA.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get mission guard by id
	 * 
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	public static MissionGuardData getMissionGuard(long id) throws BusinessException {
		try {
			return searchMissionGuard(id, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode() + "").get(0);
		} catch (NoDataException e) {
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get mission guard by instance id
	 * 
	 * @param instanceId
	 * @return
	 * @throws BusinessException
	 */

	public static MissionGuardData getMissionGuardByInstanceId(long instanceId) throws BusinessException {
		try {
			return searchMissionGuard(FlagsEnum.ALL.getCode(), instanceId, FlagsEnum.ALL.getCode() + "").get(0);
		} catch (NoDataException e) {
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get mission guard by order number
	 * 
	 * @param orderNo
	 * @return
	 * @throws BusinessException
	 */

	public static MissionGuardData getMissionGuardByOrderNumber(String orderNo) throws BusinessException {
		try {
			return searchMissionGuard(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), orderNo).get(0);
		} catch (NoDataException e) {
			throw new BusinessException("error_general");
		}
	}

	/**
	 * search mission guard
	 * 
	 * @param id
	 * @param instanceId
	 * @param orderNumber
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<MissionGuardData> searchMissionGuard(long id, long instanceId, String orderNumber) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_INSTANCE_ID", instanceId);
			qParams.put("P_ORDER_NUMBER", orderNumber);
			return DataAccess.executeNamedQuery(MissionGuardData.class, QueryNamesEnum.MISSION_GUARD_SEARCH_MISSION_GUARD.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Count all mission cars related to specific mission
	 * 
	 * @param missionGuardId
	 * @return
	 * @throws BusinessException
	 */
	public static long countAllMissionCars(long missionGuardId) throws BusinessException {
		return countMissionCars(missionGuardId);
	}

	/**
	 * Count all mission cars related to specific mission
	 * 
	 * @param missionGuardId
	 * @return
	 * @throws BusinessException
	 */
	private static long countMissionCars(long missionGuardId) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_MISSION_GUARD_ID", missionGuardId);
			return DataAccess.executeNamedQuery(Long.class, QueryNamesEnum.MISSION_CARS_COUNT_MISSION_CAR_DATA.getCode(), qParams).get(0);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return 0L;
		}
	}

	/**
	 * Generate mission number
	 * 
	 * @param session
	 * @return
	 * @throws BusinessException
	 */
	private static String generateMissionNumber(CustomSession session) throws BusinessException {
		try {
			return CommonService.generateSequenceNumber(EntitySequenceGeneratorEnum.SECURITY_GUARD_MISSION.getEntityId(), Integer.MAX_VALUE, session).toString();
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Receive All Mission Weapon
	 * 
	 * @param employeeId
	 * @param missionNumber
	 * @param orderNumber
	 * @param orderDate
	 * @param missionStartDate
	 * @param missionEndDate
	 * @param missionType
	 * @param receiveDate
	 * @param receiveTime
	 * @throws BusinessException
	 */
	public static Integer receiveAllMissionWeapon(long employeeId, String missionNumber, String orderNumber, Date orderDate, Date missionStartDate, Date missionEndDate, long missionType, String receiveDate, String receiveTime) throws BusinessException {
		CustomSession session = DataAccess.getSession();
		try {
			session.beginTransaction();

			int updatedRecordes = receiveMissionWeapon(employeeId, missionNumber, orderNumber, orderDate, missionStartDate, missionEndDate, missionType, FlagsEnum.ON.getCode(), receiveDate, receiveTime, session).get(0);

			session.commitTransaction();
			return updatedRecordes;
		} catch (Exception e) {
			session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
				throw new BusinessException("error_general");
			}
		} finally {
			session.close();
		}
	}

	/**
	 * Receive All Mission Car
	 * 
	 * @param employeeId
	 * @param missionNumber
	 * @param orderNumber
	 * @param orderDate
	 * @param missionStartDate
	 * @param missionEndDate
	 * @param missionType
	 * @param receiveDate
	 * @param receiveTime
	 * @throws BusinessException
	 */
	public static int receiveAllMissionCar(long employeeId, String missionNumber, String orderNumber, Date orderDate, Date missionStartDate, Date missionEndDate, long missionType, String receiveDate, String receiveTime) throws BusinessException {
		CustomSession session = DataAccess.getSession();
		try {
			session.beginTransaction();

			int updatedRecords = receiveMissionCar(employeeId, missionNumber, orderNumber, orderDate, missionStartDate, missionEndDate, missionType, FlagsEnum.ON.getCode(), receiveDate, receiveTime, session).get(0);

			session.commitTransaction();
			return updatedRecords;
		} catch (Exception e) {
			session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
				throw new BusinessException("error_general");
			}
		} finally {
			session.close();
		}
	}

	/**
	 * Receive All Mission Equipment
	 * 
	 * @param employeeId
	 * @param missionNumber
	 * @param orderNumber
	 * @param orderDate
	 * @param missionStartDate
	 * @param missionEndDate
	 * @param missionType
	 * @param receiveDate
	 * @param receiveTime
	 * @throws BusinessException
	 */
	public static int receiveAllMissionEquipment(long employeeId, String missionNumber, String orderNumber, Date orderDate, Date missionStartDate, Date missionEndDate, long missionType, String receiveDate, String receiveTime) throws BusinessException {
		CustomSession session = DataAccess.getSession();
		try {
			session.beginTransaction();

			int updatedRecords = receiveMissionEquipment(employeeId, missionNumber, orderNumber, orderDate, missionStartDate, missionEndDate, missionType, FlagsEnum.ON.getCode(), receiveDate, receiveTime, session).get(0);

			session.commitTransaction();
			return updatedRecords;
		} catch (Exception e) {
			session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
				throw new BusinessException("error_general");
			}
		} finally {
			session.close();
		}
	}

	/**
	 * Return All Mission Weapon
	 * 
	 * @param employeeId
	 * @param missionNumber
	 * @param orderNumber
	 * @param orderDate
	 * @param missionStartDate
	 * @param missionEndDate
	 * @param missionType
	 * @param returnDate
	 * @param returnTime
	 * @throws BusinessException
	 */
	public static int returnAllMissionWeapon(long employeeId, String missionNumber, String orderNumber, Date orderDate, Date missionStartDate, Date missionEndDate, long missionType, String returnDate, String returnTime) throws BusinessException {
		CustomSession session = DataAccess.getSession();
		try {
			session.beginTransaction();

			int updatedRecords = returnMissionWeapon(employeeId, missionNumber, orderNumber, orderDate, missionStartDate, missionEndDate, missionType, FlagsEnum.ON.getCode(), returnDate, returnTime, session).get(0);

			session.commitTransaction();
			return updatedRecords;
		} catch (Exception e) {
			session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
				throw new BusinessException("error_general");
			}
		} finally {
			session.close();
		}
	}

	/**
	 * Return All Mission Car
	 * 
	 * @param employeeId
	 * @param missionNumber
	 * @param orderNumber
	 * @param orderDate
	 * @param missionStartDate
	 * @param missionEndDate
	 * @param missionType
	 * @param returnDate
	 * @param returnTime
	 * @throws BusinessException
	 */
	public static int returnAllMissionCar(long employeeId, String missionNumber, String orderNumber, Date orderDate, Date missionStartDate, Date missionEndDate, long missionType, String returnDate, String returnTime) throws BusinessException {
		CustomSession session = DataAccess.getSession();
		try {
			session.beginTransaction();

			int updatedRecords = returnMissionCar(employeeId, missionNumber, orderNumber, orderDate, missionStartDate, missionEndDate, missionType, FlagsEnum.ON.getCode(), returnDate, returnTime, session).get(0);

			session.commitTransaction();
			return updatedRecords;
		} catch (Exception e) {
			session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
				throw new BusinessException("error_general");
			}
		} finally {
			session.close();
		}
	}

	/**
	 * Return All Mission Equipment
	 * 
	 * @param employeeId
	 * @param missionNumber
	 * @param orderNumber
	 * @param orderDate
	 * @param missionStartDate
	 * @param missionEndDate
	 * @param missionType
	 * @param returnDate
	 * @param returnTime
	 * @throws BusinessException
	 */
	public static int returnAllMissionEquipment(long employeeId, String missionNumber, String orderNumber, Date orderDate, Date missionStartDate, Date missionEndDate, long missionType, String returnDate, String returnTime) throws BusinessException {
		CustomSession session = DataAccess.getSession();
		try {
			session.beginTransaction();

			int updatedRecords = returnMissionEquipment(employeeId, missionNumber, orderNumber, orderDate, missionStartDate, missionEndDate, missionType, FlagsEnum.ON.getCode(), returnDate, returnTime, session).get(0);

			session.commitTransaction();
			return updatedRecords;
		} catch (Exception e) {
			session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
				throw new BusinessException("error_general");
			}
		} finally {
			session.close();
		}
	}

	/**
	 * Get Security Guard Mission Bytes
	 * 
	 * @param missionType
	 * @param employeeId
	 * @param missionSrcId
	 * @param fromDate
	 * @param toDate
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getSecurityGuardMissionBytes(String missionType, long employeeId, long missionSrcId, Date fromDate, Date toDate, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_MISSION_TYPE", missionType == null || missionType.isEmpty() ? FlagsEnum.ALL.getCode() + "" : missionType);
			parameters.put("P_EMP_ID", employeeId);
			parameters.put("P_MISSION_SRC", missionSrcId);
			parameters.put("P_FROM_DATE", HijriDateService.getHijriDateString(fromDate));
			parameters.put("P_TO_DATE", HijriDateService.getHijriDateString(toDate));
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.SECURITY_GUARD_MISSION.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Security Guard Mission Bytes
	 * 
	 * @param securityMissionId
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getSecurityGuardMissionDetailsBytes(long securityMissionId, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_SECURITY_MISSON_ID", securityMissionId);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			reportName = ReportNamesEnum.SECURITY_GUARD_MISSION_DETAILS.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Security Guard Mission Search Bytes
	 * 
	 * @param securityMissionId
	 * @param loginEmployeeName
	 * @param missionStartDay
	 * @param missionEndDay
	 * @param missionEndTime
	 * @param missionEquipmentdetails
	 * @param timeFlag
	 * @return
	 * @throws BusinessException
	 */

	public static byte[] getSecurityGuardMissionBytes(long securityMissionId, String loginEmployeeName, String missionStartDay, String missionEndDay, String missionEndTime, String missionEquipmentdetails, String timeFlag) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_SECURITY_MISSON_ID", securityMissionId);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			parameters.put("P_START_DAY", missionStartDay);
			parameters.put("P_END_DAY", missionEndDay);
			parameters.put("P_MISSION_END_TIME", missionEndTime);
			parameters.put("P_MISSION_EQUIPMENT", missionEquipmentdetails);
			parameters.put("P_TIME_FLAG", timeFlag);
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			reportName = ReportNamesEnum.SECURITY_GUARD_MISSION_SEARCH_DETAILS.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Receive All Mission Weapon
	 * 
	 * @param employeeId
	 * @param missionNumber
	 * @param orderNumber
	 * @param orderDate
	 * @param missionStartDate
	 * @param missionEndDate
	 * @param missionType
	 * @param status
	 * @param receiveDate
	 * @param receiveTime
	 * @param session
	 * @return
	 * @throws BusinessException
	 */
	private static List<Integer> receiveMissionWeapon(long employeeId, String missionNumber, String orderNumber, Date orderDate, Date missionStartDate, Date missionEndDate, long missionType, int status, String receiveDate, String receiveTime, CustomSession session) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_MISSION_NUMBER", missionNumber == null || missionNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : missionNumber);
			qParams.put("P_ORDER_NUMBER", orderNumber == null || orderNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : orderNumber);
			qParams.put("P_ORDER_DATE_NULL", orderDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_ORDER_DATE", orderDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(orderDate));
			qParams.put("P_MISSION_START_DATE_NULL", missionStartDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_MISSION_START_DATE", missionStartDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(missionStartDate));
			qParams.put("P_MISSION_END_DATE_NULL", missionEndDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_MISSION_END_DATE", missionEndDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(missionEndDate));
			qParams.put("P_MISSION_TYPE", missionType);
			qParams.put("P_STATUS", status);
			qParams.put("P_RECEIVE_DATE", receiveDate);
			qParams.put("P_RECEIVE_TIME", receiveTime);

			return DataAccess.updateDeleteNamedQuery(Integer.class, QueryNamesEnum.MISSION_WEAPON_RECEIVE_ALL_MISSION_WEAPON.getCode(), qParams, session);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
		}
		return null;
	}

	/**
	 * Receive All Mission Car.
	 * 
	 * @param employeeId
	 * @param missionNumber
	 * @param orderNumber
	 * @param orderDate
	 * @param missionStartDate
	 * @param missionEndDate
	 * @param missionType
	 * @param status
	 * @param receiveDate
	 * @param receiveTime
	 * @param session
	 * @return
	 * @throws BusinessException
	 */
	private static List<Integer> receiveMissionCar(long employeeId, String missionNumber, String orderNumber, Date orderDate, Date missionStartDate, Date missionEndDate, long missionType, int status, String receiveDate, String receiveTime, CustomSession session) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_MISSION_NUMBER", missionNumber == null || missionNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : missionNumber);
			qParams.put("P_ORDER_NUMBER", orderNumber == null || orderNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : orderNumber);
			qParams.put("P_ORDER_DATE_NULL", orderDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_ORDER_DATE", orderDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(orderDate));
			qParams.put("P_MISSION_START_DATE_NULL", missionStartDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_MISSION_START_DATE", missionStartDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(missionStartDate));
			qParams.put("P_MISSION_END_DATE_NULL", missionEndDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_MISSION_END_DATE", missionEndDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(missionEndDate));
			qParams.put("P_MISSION_TYPE", missionType);
			qParams.put("P_STATUS", status);
			qParams.put("P_RECEIVE_DATE", receiveDate);
			qParams.put("P_RECEIVE_TIME", receiveTime);

			return DataAccess.updateDeleteNamedQuery(Integer.class, QueryNamesEnum.MISSION_CAR_RECEIVE_ALL_MISSION_CAR.getCode(), qParams, session);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
		}
		return null;
	}

	/**
	 * Receive All Mission Equipment
	 * 
	 * @param employeeId
	 * @param missionNumber
	 * @param orderNumber
	 * @param orderDate
	 * @param missionStartDate
	 * @param missionEndDate
	 * @param missionType
	 * @param status
	 * @param receiveDate
	 * @param receiveTime
	 * @param session
	 * @return
	 * @throws BusinessException
	 */
	private static List<Integer> receiveMissionEquipment(long employeeId, String missionNumber, String orderNumber, Date orderDate, Date missionStartDate, Date missionEndDate, long missionType, int status, String receiveDate, String receiveTime, CustomSession session) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_MISSION_NUMBER", missionNumber == null || missionNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : missionNumber);
			qParams.put("P_ORDER_NUMBER", orderNumber == null || orderNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : orderNumber);
			qParams.put("P_ORDER_DATE_NULL", orderDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_ORDER_DATE", orderDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(orderDate));
			qParams.put("P_MISSION_START_DATE_NULL", missionStartDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_MISSION_START_DATE", missionStartDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(missionStartDate));
			qParams.put("P_MISSION_END_DATE_NULL", missionEndDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_MISSION_END_DATE", missionEndDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(missionEndDate));
			qParams.put("P_MISSION_TYPE", missionType);
			qParams.put("P_STATUS", status);
			qParams.put("P_RECEIVE_DATE", receiveDate);
			qParams.put("P_RECEIVE_TIME", receiveTime);

			return DataAccess.updateDeleteNamedQuery(Integer.class, QueryNamesEnum.MISSION_EQUIPMENT_RECEIVE_ALL_MISSION_EQUIPMENT.getCode(), qParams, session);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
		}
		return null;
	}

	/**
	 * Return All Mission Weapon
	 * 
	 * @param employeeId
	 * @param missionNumber
	 * @param orderNumber
	 * @param orderDate
	 * @param missionStartDate
	 * @param missionEndDate
	 * @param missionType
	 * @param status
	 * @param returnDate
	 * @param returnTime
	 * @param session
	 * @return
	 * @throws BusinessException
	 */
	private static List<Integer> returnMissionWeapon(long employeeId, String missionNumber, String orderNumber, Date orderDate, Date missionStartDate, Date missionEndDate, long missionType, int status, String returnDate, String returnTime, CustomSession session) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_MISSION_NUMBER", missionNumber == null || missionNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : missionNumber);
			qParams.put("P_ORDER_NUMBER", orderNumber == null || orderNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : orderNumber);
			qParams.put("P_ORDER_DATE_NULL", orderDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_ORDER_DATE", orderDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(orderDate));
			qParams.put("P_MISSION_START_DATE_NULL", missionStartDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_MISSION_START_DATE", missionStartDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(missionStartDate));
			qParams.put("P_MISSION_END_DATE_NULL", missionEndDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_MISSION_END_DATE", missionEndDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(missionEndDate));
			qParams.put("P_MISSION_TYPE", missionType);
			qParams.put("P_STATUS", status);
			qParams.put("P_RETURN_DATE", returnDate);
			qParams.put("P_RETURN_TIME", returnTime);

			return DataAccess.updateDeleteNamedQuery(Integer.class, QueryNamesEnum.MISSION_WEAPON_RETURN_ALL_MISSION_WEAPON.getCode(), qParams, session);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
		}
		return null;
	}

	/**
	 * Return All Mission Car
	 * 
	 * @param employeeId
	 * @param missionNumber
	 * @param orderNumber
	 * @param orderDate
	 * @param missionStartDate
	 * @param missionEndDate
	 * @param missionType
	 * @param status
	 * @param returnDate
	 * @param returnTime
	 * @param session
	 * @return
	 * @throws BusinessException
	 */
	private static List<Integer> returnMissionCar(long employeeId, String missionNumber, String orderNumber, Date orderDate, Date missionStartDate, Date missionEndDate, long missionType, int status, String returnDate, String returnTime, CustomSession session) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_MISSION_NUMBER", missionNumber == null || missionNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : missionNumber);
			qParams.put("P_ORDER_NUMBER", orderNumber == null || orderNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : orderNumber);
			qParams.put("P_ORDER_DATE_NULL", orderDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_ORDER_DATE", orderDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(orderDate));
			qParams.put("P_MISSION_START_DATE_NULL", missionStartDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_MISSION_START_DATE", missionStartDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(missionStartDate));
			qParams.put("P_MISSION_END_DATE_NULL", missionEndDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_MISSION_END_DATE", missionEndDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(missionEndDate));
			qParams.put("P_MISSION_TYPE", missionType);
			qParams.put("P_STATUS", status);
			qParams.put("P_RETURN_DATE", returnDate);
			qParams.put("P_RETURN_TIME", returnTime);

			return DataAccess.updateDeleteNamedQuery(Integer.class, QueryNamesEnum.MISSION_CAR_RETURN_ALL_MISSION_CAR.getCode(), qParams, session);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
		}
		return null;
	}

	/**
	 * Return All Mission Equiment
	 * 
	 * @param employeeId
	 * @param missionNumber
	 * @param orderNumber
	 * @param orderDate
	 * @param missionStartDate
	 * @param missionEndDate
	 * @param missionType
	 * @param status
	 * @param returnDate
	 * @param returnTime
	 * @param session
	 * @return
	 * @throws BusinessException
	 */
	private static List<Integer> returnMissionEquipment(long employeeId, String missionNumber, String orderNumber, Date orderDate, Date missionStartDate, Date missionEndDate, long missionType, int status, String returnDate, String returnTime, CustomSession session) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_MISSION_NUMBER", missionNumber == null || missionNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : missionNumber);
			qParams.put("P_ORDER_NUMBER", orderNumber == null || orderNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : orderNumber);
			qParams.put("P_ORDER_DATE_NULL", orderDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_ORDER_DATE", orderDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(orderDate));
			qParams.put("P_MISSION_START_DATE_NULL", missionStartDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_MISSION_START_DATE", missionStartDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(missionStartDate));
			qParams.put("P_MISSION_END_DATE_NULL", missionEndDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_MISSION_END_DATE", missionEndDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(missionEndDate));
			qParams.put("P_MISSION_TYPE", missionType);
			qParams.put("P_STATUS", status);
			qParams.put("P_RETURN_DATE", returnDate);
			qParams.put("P_RETURN_TIME", returnTime);

			return DataAccess.updateDeleteNamedQuery(Integer.class, QueryNamesEnum.MISSION_EQUIPMENT_RETURN_ALL_MISSION_EQUIPMENT.getCode(), qParams, session);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SecurityGuardMissionService.class, e, "SecurityGuardMissionService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
		}
		return null;
	}
}