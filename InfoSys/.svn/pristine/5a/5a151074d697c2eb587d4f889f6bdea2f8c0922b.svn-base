package com.code.services.setup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.DataAccess;
import com.code.dal.orm.setup.DepartmentData;
import com.code.dal.orm.setup.DepartmentHierData;
import com.code.dal.orm.setup.DepartmentSectorData;
import com.code.dal.orm.setup.DepartmentTypeData;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryNamesEnum;
import com.code.enums.WFPositionsEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.log4j.Log4j;
import com.code.services.workflow.WFPositionService;

public class DepartmentService extends BaseService {

	private static List<Long> childDepartmentsList = new ArrayList<Long>();

	// ----------------- Queries ------------//
	/**
	 * Get all departments by Ids
	 * 
	 * @param ids
	 * @return
	 * @throws BusinessException
	 */
	public static List<DepartmentData> getDepartmentsByMultipleIds(Long[] ids) throws BusinessException {
		try {
			return searchDepartmentsByMultipleIds(ids);
		} catch (NoDataException e) {
			return new ArrayList<DepartmentData>();
		}
	}

	/**
	 * Get all departments by Ids
	 * 
	 * @param ids
	 * @return
	 * @throws BusinessException
	 */
	public static List<DepartmentData> getDepartmentsByMultipleIdsAndNameAndCode(Long[] ids, String arabicName, String code) throws BusinessException {
		try {
			return searchDepartmentsByMultipleIdsAndNameAndCode(ids, arabicName, code);
		} catch (NoDataException e) {
			return new ArrayList<DepartmentData>();
		}
	}

	/**
	 * Get all departments by Ids
	 * 
	 * @param ids
	 * @return
	 * @throws BusinessException
	 */
	private static List<DepartmentData> searchDepartmentsByMultipleIdsAndNameAndCode(Long[] idsArray, String arabicName, String code) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID_LIST_SIZE", idsArray == null ? 0 : idsArray.length);
			qParams.put("P_ID_LIST", idsArray == null || idsArray.length == 0 ? new Long[] { -1L } : idsArray);
			qParams.put("P_ARABIC_NAME", arabicName == null || arabicName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + arabicName + "%");
			qParams.put("P_CODE", code == null || code.isEmpty() ? FlagsEnum.ALL.getCode() + "" : code);
			return DataAccess.executeNamedQuery(DepartmentData.class, QueryNamesEnum.DEPARTMENT_DATA_SEARCH_DEPARTMENT_BY_MULTIPLE_IDS_AND_NAME_AND_CODE.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(DepartmentService.class, e, "DepartmentService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get all departments by Ids
	 * 
	 * @param ids
	 * @return
	 * @throws BusinessException
	 */
	private static List<DepartmentData> searchDepartmentsByMultipleIds(Long[] idsArray) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID_LIST_SIZE", idsArray == null ? 0 : idsArray.length);
			qParams.put("P_ID_LIST", idsArray == null || idsArray.length == 0 ? new Long[] { -1L } : idsArray);
			return DataAccess.executeNamedQuery(DepartmentData.class, QueryNamesEnum.DEPARTMENT_DATA_SEARCH_DEPARTMENT_BY_MULTIPLE_IDS.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(DepartmentService.class, e, "DepartmentService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get departments by Id
	 * 
	 * @param id:
	 *            department Id
	 * @return Department of given id
	 * @throws BusinessException
	 */
	public static DepartmentData getDepartment(long id) throws BusinessException {
		try {
			return searchDepartment(id, null, FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode(), null, false).get(0);
		} catch (NoDataException e) {
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get department manager by department Id
	 * 
	 * @param id:
	 *            department Id
	 * @return manager Id
	 * @throws BusinessException
	 */
	public static Long getDepartmentManager(long id) throws BusinessException {
		try {
			DepartmentData dep = searchDepartment(id, null, FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode(), null, false).get(0);
			try {
				return searchDepartmentManager(id);
			} catch (NoDataException e) {
				throw new BusinessException("error_managerDoesnotExist", new Object[] { dep.getArabicName() });
			}
		} catch (NoDataException e) {
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get department manager by department Id
	 * 
	 * @param id:
	 *            department Id
	 * @return manager Id
	 * @throws BusinessException
	 */
	public static Long getDepartmentManagerId(long id) throws BusinessException {
		try {
			DepartmentData dep = searchDepartment(id, null, FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode(), null, false).get(0);
			try {
				return searchDepartmentManager(id);
			} catch (NoDataException e) {
				return null;
			}
		} catch (NoDataException e) {
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Search Department Manager
	 * 
	 * @param id
	 * @return DepartmentManagerId
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static Long searchDepartmentManager(long id) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_DEPT_ID", id);
			return DataAccess.executeNamedQuery(Long.class, QueryNamesEnum.DEPARTMENT_TITLE_DATA_SEARCH_DEPARTMENT_MANAGER.getCode(), qParams).get(0);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(DepartmentService.class, e, "DepartmentService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get all departments in the system
	 * 
	 * @return list of departments
	 * @throws BusinessException
	 */
	public static List<DepartmentData> getAllDepartments() throws BusinessException {
		try {
			return searchDepartment(FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode(), null, false);
		} catch (NoDataException e) {
			return new ArrayList<DepartmentData>();
		}
	}

	/**
	 * Get All Sectors under selected regionId
	 * 
	 * @param regionId
	 * @return
	 * @throws BusinessException
	 */
	public static List<DepartmentData> getSectorsByRegion(long regionId) throws BusinessException {
		try {
			return searchDepartment(FlagsEnum.ALL.getCode(), null, regionId, null, DepartmentTypeEnum.SECTOR.getCode(), null, false);
		} catch (NoDataException e) {
			return new ArrayList<DepartmentData>();
		}
	}

	/**
	 * List All departments by arabicName, code
	 * 
	 * @param arabicName
	 * @param code
	 * @return DepartmentData
	 * @throws BusinessException
	 */
	public static List<DepartmentData> getDepartment(String arabicName, String code) throws BusinessException {
		try {
			return searchDepartment(FlagsEnum.ALL.getCode(), arabicName, FlagsEnum.ALL.getCode(), code, FlagsEnum.ALL.getCode(), null, true);
		} catch (NoDataException e) {
			return new ArrayList<DepartmentData>();
		}
	}

	/**
	 * 
	 * @param regionId
	 * @param searchDepName
	 * @param searchDepCode
	 * @param longTypesList
	 * @return
	 * @throws BusinessException
	 */
	public static List<DepartmentData> getDepartmentsByRegionAndDepartmentTypes(Long regionId, String searchDepName, String searchDepCode, Long[] longTypesList) throws BusinessException {
		try {
			return searchDepartment(FlagsEnum.ALL.getCode(), searchDepName, regionId, searchDepCode, FlagsEnum.ALL.getCode(), longTypesList, false);
		} catch (NoDataException e) {
			return new ArrayList<DepartmentData>();
		}
	}

	/**
	 * List All departments by departmentType
	 * 
	 * @param departmentType
	 * @return DepartmentData
	 * @throws BusinessException
	 */
	public static List<DepartmentData> getDepartmentsBydepartmentType(Long departmentType) throws BusinessException {
		try {
			return searchDepartment(FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode(), null, departmentType == null ? FlagsEnum.ALL.getCode() : departmentType, null, false);
		} catch (NoDataException e) {
			return new ArrayList<DepartmentData>();
		}
	}

	/**
	 * get classifications by id and description
	 * 
	 * @param searchDepName
	 * @param searchDepCode
	 * @param longTypesList
	 * @return
	 * @throws BusinessException
	 */
	public static List<DepartmentData> getDepartmentsBydepartmentTypes(String searchDepName, String searchDepCode, Long[] longTypesList) throws BusinessException {
		try {
			return searchDepartment(FlagsEnum.ALL.getCode(), searchDepName, FlagsEnum.ALL.getCode(), searchDepCode, FlagsEnum.ALL.getCode(), longTypesList, false);
		} catch (NoDataException e) {
			return new ArrayList<DepartmentData>();
		}
	}

	/**
	 * get all departments by region ID and arabic name and code
	 * 
	 * @param regionId
	 * @return
	 * @throws BusinessException
	 */
	public static List<DepartmentData> getDepartmentsByRegionIdAndType(Long regionId, String arabicName, String code, long depType) throws BusinessException {
		try {
			return searchDepartment(FlagsEnum.ALL.getCode(), arabicName, regionId == null ? FlagsEnum.ALL.getCode() : regionId, code, depType, null, false);
		} catch (NoDataException e) {
			return new ArrayList<DepartmentData>();
		}
	}

	/**
	 * get all departments by region ID and arabic name and code
	 * 
	 * @param regionId
	 * @return
	 * @throws BusinessException
	 */
	public static List<DepartmentData> getDepartmentsByRegionId(Long regionId, String arabicName, String code) throws BusinessException {
		try {
			return searchDepartment(FlagsEnum.ALL.getCode(), arabicName, regionId == null ? FlagsEnum.ALL.getCode() : regionId, code, FlagsEnum.ALL.getCode(), null, false);
		} catch (NoDataException e) {
			return new ArrayList<DepartmentData>();
		}
	}

	/**
	 * Search departments filtered by id, arabicName, code
	 * 
	 * @param id
	 * @param arabicName
	 * @param regionId
	 *            : all departments exist under this region
	 * @param code
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<DepartmentData> searchDepartment(long id, String arabicName, long regionId, String code, long departmentType, Long[] departmentTypesList, boolean optimize) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_CODE", code == null || code.isEmpty() ? FlagsEnum.ALL.getCode() + "" : code);
			qParams.put("P_DEPARTMENT_TYPE", departmentType);
			qParams.put("P_REGION_ID", regionId);
			qParams.put("P_ARABIC_NAME", arabicName == null || arabicName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + arabicName + "%");
			qParams.put("P_DEPT_TYPES_LIST_SIZE", departmentTypesList == null ? 0 : departmentTypesList.length);
			qParams.put("P_DEPT_TYPES_LIST", departmentTypesList == null || departmentTypesList.length == 0 ? new Long[] { -1L } : departmentTypesList);
			qParams.put("P_OPTIMIZE", optimize ? FlagsEnum.ON.getCode() : FlagsEnum.ALL.getCode());
			return DataAccess.executeNamedQuery(DepartmentData.class, QueryNamesEnum.DEPARTMENT_DATA_SEARCH_DEPARTMENT.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(DepartmentService.class, e, "DepartmentService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * List department Hier
	 * 
	 * @param id
	 * @param parentId
	 * @return DepartmentHierData
	 * @throws BusinessException
	 */
	public static DepartmentHierData getDepartmentHier(Long id, Long parentId) throws BusinessException {
		try {
			return searchDepartmentHier(id == null ? FlagsEnum.ALL.getCode() : id, parentId == null ? FlagsEnum.ALL.getCode() : parentId).get(0);
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * Search department Hier
	 * 
	 * @param id
	 * @param parentId
	 * @return DepartmentHierData
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<DepartmentHierData> searchDepartmentHier(long id, long parentId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_PARENT_ID", parentId);
			return DataAccess.executeNamedQuery(DepartmentHierData.class, QueryNamesEnum.DEPARTMENT_HIER_DATA_SEARCH_DEPARTMENT_HIER.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(DepartmentService.class, e, "DepartmentService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get info related departments
	 * 
	 * @param infoId
	 * @return
	 * @throws BusinessException
	 */
	public static List<DepartmentData> getInfoRelatedDepartments(long infoId) throws BusinessException {
		try {
			return searchInfoRelatedDepartments(infoId);
		} catch (NoDataException e) {
			return new ArrayList<DepartmentData>();
		}
	}

	/**
	 * Search info related departments filtered by infoId
	 * 
	 * @param infoId
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<DepartmentData> searchInfoRelatedDepartments(long infoId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INFO_ID", infoId);
			return DataAccess.executeNamedQuery(DepartmentData.class, QueryNamesEnum.DEPARTMENT_DATA_SEARCH_INFO_RELATED_DEPARTMENTS.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(DepartmentService.class, e, "DepartmentService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get Assignment Departments
	 * 
	 * @param regionId
	 * @return list of DepartmentData
	 * @throws BusinessException
	 */
	public static List<DepartmentData> getAssignmentDepartments(long regionId) throws BusinessException {
		try {
			return searchAssignmentDepartments(regionId);
		} catch (NoDataException e) {
			return new ArrayList<DepartmentData>();
		}
	}

	/**
	 * Search Assignment Departments
	 * 
	 * @param regionId
	 * @return list of DepartmentData
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<DepartmentData> searchAssignmentDepartments(long regionId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_REGION_ID", regionId);
			return DataAccess.executeNamedQuery(DepartmentData.class, QueryNamesEnum.DEPARTMENT_DATA_SEARCH_ASSIGNMENT_DEPARTMENTS.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(DepartmentService.class, e, "DepartmentService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get all department types
	 * 
	 * @return list of department types
	 * @throws BusinessException
	 */
	public static List<DepartmentTypeData> getAllDepartmentTypes() throws BusinessException {
		try {
			return searchDepartmentTypes();
		} catch (NoDataException e) {
			return new ArrayList<DepartmentTypeData>();
		}
	}

	/**
	 * Search department type
	 * 
	 * @return list of department types
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<DepartmentTypeData> searchDepartmentTypes() throws BusinessException, NoDataException {
		try {
			return DataAccess.executeNamedQuery(DepartmentTypeData.class, QueryNamesEnum.DEPARTMENT_TYPE_DATA_SEARCH_DEPARTMENT_TYPE.getCode(), null);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(DepartmentService.class, e, "DepartmentService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get Department Sector
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public static Long getDepartmentSectorData(Long departmentId) throws BusinessException {
		try {
			return searchDepartmentSectorData(departmentId).getSectorId();
		} catch (NoDataException e) {
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Search Department Sector Data
	 * 
	 * @param departmentId
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static DepartmentSectorData searchDepartmentSectorData(Long departmentId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", departmentId);
			return DataAccess.executeNamedQuery(DepartmentSectorData.class, QueryNamesEnum.DEPARTMENT_SECTOR_DATA_SEARCH_DEPARTMENT_SECTOR_DATA.getCode(), qParams).get(0);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(DepartmentService.class, e, "DepartmentService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Decide if department is defined under Region in organization hierarchy
	 * 
	 * @param departmentId
	 * @return boolean
	 * @throws BusinessException
	 */
	public static Long isRegionDepartment(long departmentId) throws BusinessException { // return
		DepartmentData department = DepartmentService.getDepartment(departmentId);
		if (department.getDepartmentTypeId().equals(DepartmentTypeEnum.REGION.getCode()))
			return departmentId;
		if (department.getRegionId().equals(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId()))
			return null;
		return department.getRegionId();
	}

	/**
	 * Get the Region id or Head Quarter id of any department
	 * 
	 * @param departmentId
	 * @return Long
	 * @throws BusinessException
	 */
	public static Long getRegionByDepId(long departmentId) throws BusinessException { // return
		DepartmentData department = DepartmentService.getDepartment(departmentId);
		if (department.getDepartmentTypeId().equals(DepartmentTypeEnum.REGION.getCode()) || department.getDepartmentTypeId().equals(DepartmentTypeEnum.DIRECTORATE.getCode()))
			return departmentId;
		return department.getRegionId();
	}

	/**
	 * Get all children departments by department parent id
	 * 
	 * @param departmetnId
	 * @throws BusinessException
	 */
	public static List<Long> getChildrenDepartments(Long departmetnId) throws BusinessException {
		// TODO to be Modified later
		List<DepartmentHierData> depHierList;
		try {
			depHierList = searchDepartmentHier(FlagsEnum.ALL.getCode(), departmetnId);
		} catch (NoDataException e) {
			return new ArrayList<Long>();
		}
		for (DepartmentHierData depHier : depHierList) {
			childDepartmentsList.add(depHier.getId());
			getChildrenDepartments(depHier.getId());
		}
		return childDepartmentsList;
	}

	public static List<Long> getChildDepartmentsList() {
		return childDepartmentsList;
	}

	// Initialize childDepartmentsList
	public static void initChildDepartmentsList() {
		childDepartmentsList = new ArrayList<Long>();
	}

}