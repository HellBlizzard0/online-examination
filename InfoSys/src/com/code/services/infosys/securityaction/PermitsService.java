package com.code.services.infosys.securityaction;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.securitymission.CarPermit;
import com.code.dal.orm.securitymission.EmployeeNonEmployeeCars;
import com.code.dal.orm.securitymission.NonEmployeePermit;
import com.code.dal.orm.securitymission.PermitsPatch;
import com.code.dal.orm.securitymission.PermitsPatchDetails;
import com.code.dal.orm.setup.CountryData;
import com.code.dal.orm.setup.DepartmentData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.setup.NonEmployeeData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.PermitsPatchTypesEnum;
import com.code.enums.PermitsStatusEnum;
import com.code.enums.QueryNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.CountryService;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.EmployeeService;
import com.code.services.setup.NonEmployeeService;
import com.code.services.setup.SetupService;
import com.code.services.util.HijriDateService;

public class PermitsService extends BaseService {
	//// sheet password : BG_INFOSYS
	private static ResourceBundle messages = ResourceBundle.getBundle("com.code.resources.messages", new Locale("ar"));
	private static final int PERSONS_SOCIAL_ID = 0;
	private static final int PERSONS_FULL_NAME = 1;
	private static final int PERSONS_NATIONALITY = 2;
	private static final int PERSONS_DEPARTMENT = 3;
	private static final int PERSONS_PHONE_NO = 4;
	private static final int PERSONS_PERMIT_END_DATE = 5;
	private static final int PERSONS_COL_COUNT = 6;

	private static final int CARS_PERMIT_NO = 0;
	private static final int CARS_SOCIAL_ID = 1;
	private static final int CARS_FULL_NAME = 2;
	private static final int CARS_DEPARTMENT = 3;
	private static final int CARS_CAR_MODEL = 6;
	private static final int CARS_CAR_PLATE = 7;
	private static final int CARS_REMARKS = 8;
	private static final int CARS_COL_COUNT = 9;

	private PermitsService() {
	}

	/**
	 * readPermitsData
	 * 
	 * @param permitsPatchType
	 * @param filePath
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static PermitsPatch readPermitsData(String permitsPatchType, String filePath, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		try {
			PermitsPatch patch = new PermitsPatch();
			patch.setFileName(filePath.substring(filePath.lastIndexOf("/") + 1));
			patch.setPatchDate(Calendar.getInstance().getTime());
			patch.setPatchType(permitsPatchType);
			patch.setStatus(PermitsStatusEnum.RUNNING.getCode());

			DataAccess.addEntity(patch);

			FileInputStream inputStream = new FileInputStream(new File(filePath));
			Workbook workbook = getWorkbook(inputStream, filePath);
			Sheet firstSheet = workbook.getSheetAt(0);

			if (permitsPatchType.equals(PermitsPatchTypesEnum.PERSONS.getCode())) {
				readPersonsPermitsData(patch, firstSheet, loginEmpData);
			} else {
				readCarsPermitsData(patch, firstSheet, loginEmpData);
			}

			patch.setStatus(PermitsStatusEnum.COMPLETED.getCode());
			DataAccess.updateEntity(patch);

			workbook.close();
			inputStream.close();
			new File(filePath).delete();

			return patch;
		} catch (Exception e) {
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(PermitsService.class, e, "PermitsService");
				throw new BusinessException("error_general");
			}
		}
	}

	/**
	 * readPersonsPermitsData
	 * 
	 * @param patch
	 * @param firstSheet
	 * @param loginEmpData
	 * @throws BusinessException
	 */
	private static void readPersonsPermitsData(PermitsPatch patch, Sheet firstSheet, EmployeeData loginEmpData) throws BusinessException {
		try {
			Iterator<Row> iterator = firstSheet.iterator();
			iterator.next();
			long success = 0, failure = 0;
			while (iterator.hasNext()) {
				Row nextRow = iterator.next();

				StringBuilder errorDesc = new StringBuilder("");
				String socialId = new String("");
				Date permitEndDate = null;
				if (nextRow.getLastCellNum() != PERSONS_COL_COUNT) {
					errorDesc.append(" - " + messages.getString("error_invalidTemplate"));
				} else {
					// National Id
					socialId = getCellValue(nextRow.getCell(PERSONS_SOCIAL_ID));
					if (socialId == null || socialId.isEmpty() || socialId.toString().length() != 10 || (!socialId.toString().startsWith("1") && !socialId.toString().startsWith("2"))) {
						errorDesc.append(" - " + messages.getString("error_invalidIndintty"));
					} else {
						try {
							Long.parseLong(socialId);
						} catch (Exception e) {
							errorDesc.append(" - " + messages.getString("error_invalidIndintty"));
						}
					}
					// Permit End Date
					permitEndDate = HijriDateService.getHijriDate(getCellValue(nextRow.getCell(PERSONS_PERMIT_END_DATE)));
					if (permitEndDate == null) {
						errorDesc.append(" - " + messages.getString("error_invalidDatePermit"));
					}
				}

				if (errorDesc.length() == 0) {
					NonEmployeePermit permit = new NonEmployeePermit();
					List<NonEmployeeData> nonEmpList = NonEmployeeService.getNonEmployee(Long.parseLong(socialId), null);
					if (nonEmpList.size() == 0) {
						NonEmployeeData nonEmpDate = new NonEmployeeData();
						nonEmpDate.setFullName(getCellValue(nextRow.getCell(PERSONS_FULL_NAME)));
						nonEmpDate.setIdentity(Long.parseLong(socialId));
						String nationality = getCellValue(nextRow.getCell(PERSONS_NATIONALITY));

						if (nationality != null && nationality.length() != 0) {
							List<CountryData> countryList = CountryService.getCountry(nationality);
							if (!countryList.isEmpty()) {
								nonEmpDate.setCountryId(countryList.get(0).getId());
							}
						}
						nonEmpDate.setPhoneNo(getCellValue(nextRow.getCell(PERSONS_PHONE_NO)));
						NonEmployeeService.insertNonEmployee(loginEmpData, nonEmpDate);
						permit.setNonEmployeeId(nonEmpDate.getId());
					} else {
						List<NonEmployeePermit> existPermit = EmployeeNonEmployeeCarService.getNonEmployeePermit(Long.parseLong(socialId));
						if (!existPermit.isEmpty()) {
							permit = existPermit.get(0);
							if (permitEndDate.after(existPermit.get(0).getEndDate())) {
								permit.setEndDate(permitEndDate);
							}
						} else {
							permit.setNonEmployeeId(nonEmpList.get(0).getId());
						}
					}

					String department = getCellValue(nextRow.getCell(PERSONS_DEPARTMENT));
					if (department != null && department.length() != 0) {
						List<DepartmentData> depList = DepartmentService.getDepartment(department, null);
						if (!depList.isEmpty()) {
							permit.setDepartmentId(depList.get(0).getId());
						}
					}

					if (permit.getId() == null) {
						permit.setEndDate(permitEndDate);
						DataAccess.addEntity(permit);
					} else {
						DataAccess.updateEntity(permit);
					}
					success++;
				} else {
					insertErrorRecord(nextRow, patch.getId(), errorDesc.toString());
					failure++;
				}
			}

			patch.setSuccessCount(success);
			patch.setFailureCount(failure);

		} catch (Exception e) {
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(PermitsService.class, e, "PermitsService");
				throw new BusinessException("error_general");
			}
		}
	}

	/**
	 * readCarsPermitsData
	 * 
	 * @param patch
	 * @param firstSheet
	 * @param loginEmpData
	 * @throws BusinessException
	 */
	private static void readCarsPermitsData(PermitsPatch patch, Sheet firstSheet, EmployeeData loginEmpData) throws BusinessException {
		try {
			Iterator<Row> iterator = firstSheet.iterator();
			iterator.next();
			long success = 0, failure = 0;
			while (iterator.hasNext()) {
				Row nextRow = iterator.next();

				StringBuilder errorDesc = new StringBuilder("");
				String socialId = new String("");
				String permitNo = new String("");
				Long carModelId = null;
				String carModelDesc = "";
				String plateChar1 = "";
				String plateChar2 = "";
				String plateChar3 = "";
				String plateNumber = "";
				if (nextRow.getLastCellNum() != CARS_COL_COUNT && nextRow.getLastCellNum() != CARS_COL_COUNT - 1) {
					errorDesc.append(" - " + messages.getString("error_invalidTemplate"));
				} else {
					// Permit No
					permitNo = getCellValue(nextRow.getCell(CARS_PERMIT_NO));
					if (permitNo == null || permitNo.length() == 0 || permitNo.isEmpty()) {
						errorDesc.append(" - " + messages.getString("error_invalidPermitNo"));
					}
					// National Id
					socialId = getCellValue(nextRow.getCell(CARS_SOCIAL_ID));
					if (socialId == null || socialId.isEmpty() || socialId.toString().length() != 10 || (!socialId.toString().startsWith("1") && !socialId.toString().startsWith("2"))) {
						errorDesc.append(" - " + messages.getString("error_invalidIndintty"));
					} else {
						try {
							Long.parseLong(socialId);
						} catch (Exception e) {
							errorDesc.append(" - " + messages.getString("error_invalidIndintty"));
						}
					}
					// Car Model
					String carModel = getCellValue(nextRow.getCell(CARS_CAR_MODEL));
					if (carModel == null || carModel.length() == 0 || carModel.isEmpty()) {
						errorDesc.append(" - " + messages.getString("error_invalidCarModel"));
					} else {
						List<SetupDomain> domList = SetupService.getDomains(carModel, null, null);
						if (domList.isEmpty()) {
							errorDesc.append(" - " + messages.getString("error_invalidCarModel"));
						} else {
							carModelId = domList.get(0).getId();
							carModelDesc = domList.get(0).getDescription();
						}
					}
					// Car Plate
					String carPlateNumber = getCellValue(nextRow.getCell(CARS_CAR_PLATE));
					if (carModel == null || carModel.length() == 0 || carModel.isEmpty()) {
						errorDesc.append(" - " + messages.getString("error_invalidCarPlate"));
					} else {
						String[] plate = carPlateNumber.split(" ");
						if (plate.length != 4) {
							errorDesc.append(" - " + messages.getString("error_invalidCarPlate"));
						} else {
							if (plate[0].length() == 1 && plate[1].length() == 1 && plate[2].length() == 1 && (plate[3].length() >= 1 && plate[3].length() <= 4)) {
								plateChar1 = plate[0];
								plateChar2 = plate[1];
								plateChar3 = plate[2];
								plateNumber = plate[3];
							} else {
								errorDesc.append(" - " + messages.getString("error_invalidCarPlate"));
							}
						}
					}
				}

				if (errorDesc.length() == 0) {
					List<CarPermit> carPermitList = EmployeeNonEmployeeCarService.getCarPermit(permitNo, null, null, null, null);
					if (carPermitList.isEmpty()) {
						CarPermit carPermit = new CarPermit();
						carPermit.setPermitNo(permitNo);
						carPermit.setRemarks(getCellValue(nextRow.getCell(CARS_REMARKS)));

						List<EmployeeNonEmployeeCars> carsList = EmployeeNonEmployeeCarService.getEmployeeNonEmployeeCarsByPlateNumber(plateNumber, plateChar1, plateChar2, plateChar3);
						if (carsList.isEmpty()) {
							EmployeeNonEmployeeCars car = new EmployeeNonEmployeeCars();
							car.setDomainCarModelId(carModelId);
							car.setDomainCarModelDescription(carModelDesc);
							car.setPlateChar1(plateChar1);
							car.setPlateChar2(plateChar2);
							car.setPlateChar3(plateChar3);
							car.setPlateNumber(plateNumber);

							// social id check
							EmployeeData emp = EmployeeService.getEmployee(socialId);
							if (emp == null) {
								List<NonEmployeeData> nonEmpList = NonEmployeeService.getNonEmployee(Long.parseLong(socialId), null);
								if (nonEmpList.size() == 0) {
									NonEmployeeData nonEmpDate = new NonEmployeeData();
									nonEmpDate.setFullName(getCellValue(nextRow.getCell(CARS_FULL_NAME)));
									nonEmpDate.setIdentity(Long.parseLong(socialId));
									NonEmployeeService.insertNonEmployee(loginEmpData, nonEmpDate);
									car.setNonEmployeeId(nonEmpDate.getId());
								} else {
									car.setNonEmployeeId(nonEmpList.get(0).getId());
								}
							} else {
								car.setEmployeeId(emp.getEmpId());
							}

							EmployeeNonEmployeeCarService.saveEmployeeNonEmployeeCar(car, loginEmpData);
							carPermit.setEmployeeNonEmployeeCarsId(car.getId());
						} else {
							carPermit.setEmployeeNonEmployeeCarsId(carsList.get(0).getId());
						}

						// department
						String department = getCellValue(nextRow.getCell(CARS_DEPARTMENT));
						if (department != null && department.length() != 0) {
							List<DepartmentData> depList = DepartmentService.getDepartment(department, null);
							if (!depList.isEmpty()) {
								carPermit.setDepartmentId(depList.get(0).getId());
							}
						}

						DataAccess.addEntity(carPermit);
						success++;
					} else {
						failure++;
					}

				} else {
					insertErrorRecord(nextRow, patch.getId(), errorDesc.toString());
					failure++;
				}
			}

			patch.setSuccessCount(success);
			patch.setFailureCount(failure);
		} catch (Exception e) {
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(PermitsService.class, e, "PermitsService");
				throw new BusinessException("error_general");
			}
		}
	}

	/**
	 * insertErrorRecord
	 * 
	 * @param nextRow
	 * @param patchId
	 * @param errorDesc
	 * @throws DatabaseException
	 */
	private static void insertErrorRecord(Row nextRow, long patchId, String errorDesc) throws DatabaseException {
		Iterator<Cell> cellIterator = nextRow.cellIterator();
		PermitsPatchDetails permitsPatchDetail = new PermitsPatchDetails();
		permitsPatchDetail.setPatchId(patchId);
		permitsPatchDetail.setErrorDesc(errorDesc);
		permitsPatchDetail.setRowNumber(nextRow.getRowNum());

		while (cellIterator.hasNext()) {
			Cell nextCell = cellIterator.next();
			int columnIndex = nextCell.getColumnIndex();

			switch (columnIndex) {
			case 0:
				permitsPatchDetail.setField1(getCellValue(nextCell));
				break;
			case 1:
				permitsPatchDetail.setField2(getCellValue(nextCell));
				break;
			case 2:
				permitsPatchDetail.setField3(getCellValue(nextCell));
				break;
			case 3:
				permitsPatchDetail.setField4(getCellValue(nextCell));
				break;
			case 4:
				permitsPatchDetail.setField5(getCellValue(nextCell));
				break;
			case 5:
				permitsPatchDetail.setField6(getCellValue(nextCell));
				break;
			case 6:
				permitsPatchDetail.setField7(getCellValue(nextCell));
				break;
			case 7:
				permitsPatchDetail.setField8(getCellValue(nextCell));
				break;
			case 8:
				permitsPatchDetail.setField9(getCellValue(nextCell));
				break;
			}
		}
		DataAccess.addEntity(permitsPatchDetail);
	}

	/**
	 * getCellValue
	 * 
	 * @param cell
	 * @return
	 */
	private static String getCellValue(Cell cell) {
		if (cell == null)
			return null;
		cell.setCellType(Cell.CELL_TYPE_STRING);
		return cell.getStringCellValue().toString();
	}

	/**
	 * getWorkbook
	 * 
	 * @param inputStream
	 * @param excelFilePath
	 * @return
	 * @throws BusinessException
	 */
	private static Workbook getWorkbook(FileInputStream inputStream, String excelFilePath) throws BusinessException {
		Workbook workbook = null;
		try {
			if (excelFilePath.endsWith("xlsx")) {
				workbook = new XSSFWorkbook(inputStream);
			} else if (excelFilePath.endsWith("xls")) {
				workbook = new HSSFWorkbook(inputStream);
			} else {
				throw new BusinessException("error_errorFileType");
			}
		} catch (Exception e) {
			throw new BusinessException("error_errorFileType");
		}
		return workbook;
	}

	/**
	 * saveNonEmployeePermit
	 * 
	 * @param nonEmployeePermit
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveNonEmployeePermit(NonEmployeePermit nonEmployeePermit, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}
			List<NonEmployeePermit> existPermit = EmployeeNonEmployeeCarService.getNonEmployeePermitByNonEmployeeId(nonEmployeePermit.getNonEmployeeId());
			if (!existPermit.isEmpty()) {
				if (nonEmployeePermit.getEndDate().after(existPermit.get(0).getEndDate())) {
					existPermit.get(0).setEndDate(nonEmployeePermit.getEndDate());
					DataAccess.updateEntity(existPermit.get(0), session);
				}
			} else {
				DataAccess.addEntity(nonEmployeePermit, session);
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
				Log4j.traceErrorException(PermitsService.class, e, "PermitsService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * saveCarEmployeePermit
	 * 
	 * @param employeeNonEmployeeCars
	 * @param carPermit
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveCarEmployeePermit(EmployeeNonEmployeeCars employeeNonEmployeeCars, CarPermit carPermit, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		if (carPermit.getPermitNo() == null || carPermit.getPermitNo().trim().equals("") || carPermit.getPermitNo().isEmpty()) {
			throw new BusinessException("error_mandatory");
		}
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}
			List<CarPermit> carPermitList = EmployeeNonEmployeeCarService.getCarPermit(carPermit.getPermitNo(), null, null, null, null);
			if (!carPermitList.isEmpty()) {
				throw new BusinessException("error_permitExist");
			} else {
				List<EmployeeNonEmployeeCars> carsList = EmployeeNonEmployeeCarService.getEmployeeNonEmployeeCarsByPlateNumber(employeeNonEmployeeCars.getPlateNumber(), employeeNonEmployeeCars.getPlateChar1(), employeeNonEmployeeCars.getPlateChar2(), employeeNonEmployeeCars.getPlateChar3());
				if (!carsList.isEmpty()) {
					carPermit.setEmployeeNonEmployeeCarsId(carsList.get(0).getId());
				} else {
					EmployeeNonEmployeeCarService.saveEmployeeNonEmployeeCar(employeeNonEmployeeCars, loginEmpData, session);
					carPermit.setEmployeeNonEmployeeCarsId(employeeNonEmployeeCars.getId());
				}
			}
			DataAccess.addEntity(carPermit, session);
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
				Log4j.traceErrorException(PermitsService.class, e, "PermitsService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**********************************************/
	/******************* Queries *****************/

	/**
	 * getPermitsPatchDetails
	 * 
	 * @param patchId
	 * @return
	 * @throws BusinessException
	 */
	public static List<PermitsPatchDetails> getPermitsPatchDetails(long patchId) throws BusinessException {
		try {
			return searchPermitsPatchDetails(patchId);
		} catch (NoDataException e) {
			return new ArrayList<PermitsPatchDetails>();
		}
	}

	/**
	 * searchPermitsPatchDetails
	 * 
	 * @param patchId
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<PermitsPatchDetails> searchPermitsPatchDetails(long patchId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_PATCH_ID", patchId);
			return DataAccess.executeNamedQuery(PermitsPatchDetails.class, QueryNamesEnum.PERMITS_PATCH_DETAILS_SEARCH_PERMITS_PATCH_DETAILS.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(PermitsService.class, e, "PermitsService");
			throw new BusinessException("error_DBError");
		}
	}
}