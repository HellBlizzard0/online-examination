package com.code.services.infosys.carrental;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.ReportService;
import com.code.dal.orm.carrental.CarRentalData;
import com.code.dal.orm.setup.DepartmentData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.EntitySequenceGeneratorEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryNamesEnum;
import com.code.enums.ReportNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.infosys.info.InfoService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.util.CommonService;
import com.code.services.util.HijriDateService;

/**
 * 
 * @author aismail
 * 
 */
public class CarRentalService extends BaseService {

	private CarRentalService() {
	}

	/**
	 * Save Car Rental order
	 * 
	 * @param infoData
	 * @return
	 * @throws BusinessException
	 */
	public static void saveCarRental(CarRentalData carRentalData, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateCarRental(carRentalData);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			carRentalData.setOrderDate(HijriDateService.getHijriSysDate());
			carRentalData.setOrderNumber(generateOrderNumber(session));

			if (carRentalData.getInfoNumber() != null) {
				carRentalData.setInfoNumber(carRentalData.getInfoNumber().trim());

				if (carRentalData.getInfoNumber().isEmpty() || !InfoService.isExistingInfo(carRentalData.getInfoNumber()))
					throw new BusinessException("error_infoNotExist");
			}

			carRentalData.setRentPeriod(HijriDateService.hijriDateDiff(carRentalData.getRentStartDate(), carRentalData.getRentEndDate()));

			carRentalData.setTotalAmount((carRentalData.getDailyAmount() * carRentalData.getRentPeriod()) + carRentalData.getExtraAmount());
			carRentalData.setPaid(FlagsEnum.OFF.getCode());

			carRentalData.getCarRental().setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.addEntity(carRentalData.getCarRental(), session);
			carRentalData.setId(carRentalData.getCarRental().getId());

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(CarRentalService.class, e, "CarRentalService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	private static String generateOrderNumber(CustomSession session) throws BusinessException {
		try {
			return CommonService.generateSequenceNumber(EntitySequenceGeneratorEnum.CAR_RENTAL.getEntityId(), 100000, session).toString();
		} catch (Exception e) {
			Log4j.traceErrorException(CarRentalService.class, e, "CarRentalService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Update Car Rental order
	 * 
	 * @param infoData
	 * @return
	 * @throws BusinessException
	 */
	public static void updateCarRental(CarRentalData carRentalData, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateCarRental(carRentalData);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			if (carRentalData.getInfoNumber() != null) {
				carRentalData.setInfoNumber(carRentalData.getInfoNumber().trim());

				if (carRentalData.getInfoNumber().isEmpty() || !InfoService.isExistingInfo(carRentalData.getInfoNumber()))
					throw new BusinessException("error_infoNotExist");
			}

			carRentalData.setRentPeriod(HijriDateService.hijriDateDiff(carRentalData.getRentStartDate(), carRentalData.getRentEndDate()));
			carRentalData.setTotalAmount((carRentalData.getDailyAmount() * carRentalData.getRentPeriod()) + carRentalData.getExtraAmount());

			carRentalData.getCarRental().setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.updateEntity(carRentalData.getCarRental(), session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(CarRentalService.class, e, "CarRentalService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Delete Car Rental order
	 * 
	 * @param infoData
	 * @return
	 * @throws BusinessException
	 */
	public static void deleteCarRental(CarRentalData carRentalData, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		if (carRentalData.getPaid() == FlagsEnum.ON.getCode()) {
			throw new BusinessException("error_orderCantBeDeleted");
		}

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			carRentalData.getCarRental().setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.deleteEntity(carRentalData.getCarRental(), session);

			if (!isOpenedSession) {
				session.commitTransaction();
			}
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(CarRentalService.class, e, "CarRentalService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * check mandatory fields and business rules
	 * 
	 * @param carRentalData
	 * @throws BusinessException
	 */
	private static void validateCarRental(CarRentalData carRentalData) throws BusinessException {
		if (carRentalData.getRentStartDate() == null || carRentalData.getRentEndDate() == null)
			throw new BusinessException("error_rentDatesMandatory");
		else if (carRentalData.getRentStartDate().after(HijriDateService.getHijriSysDate())) {
			throw new BusinessException("error_rentalDateCantExceedCurrentDate");
		}

		if (carRentalData.getRentStartDate().equals(carRentalData.getRentEndDate()) || carRentalData.getRentStartDate().after(carRentalData.getRentEndDate())) {
			throw new BusinessException("error_datesNotValid");
		}

		if (carRentalData.getEmployeeId() == null)
			throw new BusinessException("error_rentEmployeeMandatory");
		if (carRentalData.getDomainCarModelId() == null)
			throw new BusinessException("error_carModelMandatory");
		if (carRentalData.getMissionConclusion() == null)
			throw new BusinessException("error_missionConclusionMandatory");
		if (carRentalData.getMissionDescription() == null)
			throw new BusinessException("error_missionDescriptionMandatory");
		if (carRentalData.getContractNumber() == null)
			throw new BusinessException("error_contractNumberMandatory");
		else {
			for (char c : carRentalData.getContractNumber().toCharArray()) {
				if (!Character.isDigit(c))
					throw new BusinessException("error_contractNumberMustBeOnlyNumbers");
			}
		}
		if (carRentalData.getDailyAmount() == null || carRentalData.getDailyAmount() <= 0.0)
			throw new BusinessException("error_dailyAmountMandatory");
		if (carRentalData.getCarPlateNumber() == null)
			throw new BusinessException("error_plateNumberMandatory");
		else if (!carRentalData.getCarPlateNumber().matches("^([\\u0600-\\u06FF]\\s?){3}\\s?\\d+$")) {
			throw new BusinessException("error_plateNumberWrongFormat");
		}
	}

	/**
	 * Update Contract status for all selected car rentals : This method changes the status of sent carRentals list to be paid and assigns them to a specific cheque number and it's related data
	 * 
	 * @param chequeNumber
	 * @param hijriChequeDate
	 * @param chequeAmount
	 * @throws BusinessException
	 */
	public static void updateContractStatus(List<CarRentalData> carRentalDataList, String chequeNumber, Date hijriChequeDate, Float chequeAmount, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			for (CarRentalData carRentalData : carRentalDataList) {
				carRentalData.setChequeAmount(chequeAmount);
				carRentalData.setChequeDate(hijriChequeDate);
				carRentalData.setChequeNumber(chequeNumber);
				carRentalData.setPaid(FlagsEnum.ON.getCode());

				carRentalData.getCarRental().setSystemUser(loginEmpData.getEmpId().toString());
				DataAccess.updateEntity(carRentalData.getCarRental(), session);
			}

			if (!isOpenedSession) {
				session.commitTransaction();
			}
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(CarRentalService.class, e, "CarRentalService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}

	}

	/**
	 * Get car rental id
	 * 
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	public static CarRentalData getCarRental(long id) throws BusinessException {
		try {
			return searchCarRentals(id, null, new Long[] {}, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, null, null, null).get(0);
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * Get car rentals
	 * 
	 * @param orderNumber
	 * @param regionOrSectorId
	 *            : Choosen id from the user , in case of regionId the system get all carRentals which registered department is a sector under this region , in case of specific ssector , the system get the carRentals matching this sector only , otherwise the system shows error indication it's nethier sector nor region is choosen
	 * @param employeeId
	 * @param contractStatus
	 * @param domainSubRentalCompanyId
	 * @param rentStartDate
	 * @param rentEndDate
	 * @return
	 * @throws BusinessException
	 */
	public static List<CarRentalData> getCarRental(String contractNumber, long regionOrSectorId, long employeeId, int contractStatus, long domainSubRentalCompanyId, Date rentStartDate, Date rentEndDate, String chequeNumber, String plateNumber) throws BusinessException {
		try {
			if(plateNumber != null && !plateNumber.trim().isEmpty() && !plateNumber.matches("^([\\u0600-\\u06FF]\\s?){3}\\s?\\d+$")){
				throw new BusinessException("error_plateNumberWrongFormat");
			}
			if (regionOrSectorId != (long) FlagsEnum.ALL.getCode()) {
				DepartmentData department = DepartmentService.getDepartment(regionOrSectorId);
				if (department.getDepartmentTypeId() != DepartmentTypeEnum.REGION.getCode() && department.getDepartmentTypeId() != DepartmentTypeEnum.SECTOR.getCode()) {
					throw new BusinessException("error_departmentNeitherRegionNorSector");
				} else if (department.getDepartmentTypeId() == DepartmentTypeEnum.REGION.getCode()) {
					List<DepartmentData> sectorsList = DepartmentService.getSectorsByRegion(regionOrSectorId);
					Long[] departmentLongArray = new Long[sectorsList.size() + 1];
					departmentLongArray[0] = regionOrSectorId;
					int i = 1;
					for (DepartmentData sectors : sectorsList) {
						departmentLongArray[i++] = sectors.getId();
					}
					return searchCarRentals(FlagsEnum.ALL.getCode(), contractNumber, departmentLongArray, employeeId, contractStatus, domainSubRentalCompanyId, rentStartDate, rentEndDate, chequeNumber, plateNumber);
				} else if (department.getDepartmentTypeId() == DepartmentTypeEnum.SECTOR.getCode()) {
					return searchCarRentals((long) FlagsEnum.ALL.getCode(), contractNumber, new Long[] { regionOrSectorId }, employeeId, contractStatus, domainSubRentalCompanyId, rentStartDate, rentEndDate, chequeNumber, plateNumber);
				}
			} else {
				return searchCarRentals((long) FlagsEnum.ALL.getCode(), contractNumber, new Long[] {}, employeeId, contractStatus, domainSubRentalCompanyId, rentStartDate, rentEndDate, chequeNumber, plateNumber);
			}
		} catch (NoDataException e) {
			return new ArrayList<CarRentalData>();
		}
		return new ArrayList<CarRentalData>();
	}

	/**
	 * Search inside car rentals
	 * 
	 * @param id
	 * @param contractNumber
	 * @param departmentIdList
	 *            : list of sectors under specific region choosed by the user , in case of specific sector choosen , the system returns only the carRental instance matching this sectorId
	 * @param employeeId
	 * @param contractStatus
	 * @param domainSubRentalCompanyId
	 * @param rentStartDate
	 * @param rentEndDate
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<CarRentalData> searchCarRentals(long id, String contractNumber, Long[] departmentIdList, long employeeId, int contractStatus, long domainSubRentalCompanyId, Date rentStartDate, Date rentEndDate, String chequeNumber, String plateNumber) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_RENT_START_DATE_NULL", rentStartDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_RENT_END_DATE_NULL", rentEndDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_RENT_START_DATE", rentStartDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(rentStartDate));
			qParams.put("P_RENT_END_DATE", rentEndDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(rentEndDate));
			qParams.put("P_CONTRACT_NUMBER", contractNumber == null || contractNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : contractNumber);
			qParams.put("P_CHEQUE_NUMBER", chequeNumber == null || chequeNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : chequeNumber);
			qParams.put("P_PLATE_NUMBER", plateNumber == null || plateNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : plateNumber);
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_CONTRACT_STATUS", contractStatus);
			qParams.put("P_DOMAIN_SUB_RENTAL_COMPANY", domainSubRentalCompanyId);
			qParams.put("P_DEPT_ID_LIST_SIZE", departmentIdList == null ? 0 : departmentIdList.length);
			qParams.put("P_DEPT_ID_LIST", departmentIdList == null || departmentIdList.length == 0 ? new Long[] { -1L } : departmentIdList);

			return DataAccess.executeNamedQuery(CarRentalData.class, QueryNamesEnum.CAR_RENTAL_SEARCH_CAR_RENTALS.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(CarRentalService.class, e, "CarRentalService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * getCarRentalReportBytes
	 * 
	 * @param regionId
	 * @param rentalStartDate
	 * @param rentalEndDate
	 * @param renterId
	 * @param contractStatus
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getCarRentalReportBytes(Long renterId, Integer contractStatus, Date rentalStartDate, Date rentalEndDate, Long regionId, String regionName, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_RENTER_ID", renterId == null ? FlagsEnum.ALL.getCode() : renterId);
			parameters.put("P_CONTRACT_STATUS", contractStatus);

			parameters.put("P_START_DATE_NULL", rentalStartDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			parameters.put("P_END_DATE_NULL", rentalEndDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());

			parameters.put("P_START_DATE", rentalStartDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(rentalStartDate));
			parameters.put("P_END_DATE", rentalEndDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(rentalEndDate));

			parameters.put("P_REGION_ID", regionId == null ? FlagsEnum.ALL.getCode() : regionId);
			parameters.put("P_REGION_NAME", regionName);

			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);

			reportName = ReportNamesEnum.CAR_RENTAL.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(CarRentalService.class, e, "CarRentalService");
			throw new BusinessException("error_general");
		}
	}
	
	/**
	 * getCarRentalReportBytes
	 * 
	 * @param regionId
	 * @param rentalStartDate
	 * @param rentalEndDate
	 * @param renterId
	 * @param contractStatus
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getCarRentalRegistrationReportBytes(Long carRentalId) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_CAR_RENTAL_ID", carRentalId == null ? FlagsEnum.ALL.getCode() : carRentalId);

			reportName = ReportNamesEnum.CAR_RENTAL_REGISTRATION.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(CarRentalService.class, e, "CarRentalService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * getCarRentalStatisiticsReportBytes
	 * 
	 * @param regionId
	 * @param rentalStartDate
	 * @param rentalEndDate
	 * @param renterId
	 * @param contractStatus
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getCarRentalStatisiticsReportBytes(Long renterId, Integer contractStatus, Date rentalStartDate, Date rentalEndDate, Long regionId, String regionName, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_RENTER_ID", renterId == null ? FlagsEnum.ALL.getCode() : renterId);
			parameters.put("P_CONTRACT_STATUS", contractStatus);

			parameters.put("P_START_DATE_NULL", rentalStartDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			parameters.put("P_END_DATE_NULL", rentalEndDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());

			parameters.put("P_START_DATE", rentalStartDate == null ? null : HijriDateService.getHijriDateString(rentalStartDate));
			parameters.put("P_END_DATE", rentalEndDate == null ? null : HijriDateService.getHijriDateString(rentalEndDate));

			parameters.put("P_REGION_ID", regionId == null ? FlagsEnum.ALL.getCode() : regionId);
			parameters.put("P_REGION_NAME", regionName);

			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);

			reportName = ReportNamesEnum.CAR_RENTAL_STATISTICS.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(CarRentalService.class, e, "CarRentalService");
			throw new BusinessException("error_general");
		}
	}

}