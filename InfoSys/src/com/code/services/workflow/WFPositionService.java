package com.code.services.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.workflow.WFPosition;
import com.code.dal.orm.workflow.WFPositionData;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryNamesEnum;
import com.code.enums.WFPositionDiscriminatorEnum;
import com.code.enums.WFPositionsEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.log4j.Log4j;

public class WFPositionService extends BaseService {

	/**
	 * updateWFPosition
	 * 
	 * @param wfPositionData
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateWFPosition(WFPositionData wfPositionData, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			wfPositionData.getWfPosition().setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.updateEntity(wfPositionData.getWfPosition(), session);

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
				Log4j.traceErrorException(WFPositionService.class, e, "WFPositionService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * get region anti_drug department
	 * 
	 * @param regionId
	 * @return
	 * @throws BusinessException
	 */
	public static Long getRegionAntiDrugDepartmentId(long regionId) throws BusinessException {
		Long unitId = null;
		try {
			WFPosition pos = getWFPositionByUnitId(regionId);
			if (pos.getPositionId().equals((long) WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.DIRECTORATE_ANTI_DRUG_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ALMADINA_REGION_DEPARTMENT.getCode())) {
				unitId = WFPositionService.getWFPosition(WFPositionsEnum.ALMADINA_ALMNWARA_REGION_ANTI_DRUG_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ASEER_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.ASEER_REGION_ANTI_DRUG_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.EASTERN_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.EASTERN_REGION_ANTI_DRUG_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.GAZAN_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.GAZAN_REGION_ANTI_DRUG_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.GOUF_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.GOUF_REGION_ANTI_DRUG_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.MAKA_ELMOKRAMA_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.MAKA_ELMOKRAMA_REGION_ANTI_DRUG_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.NAGRAN_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.NAGRAN_REGION_ANTI_DRUG_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.NORTH_BORDER_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.NORTH_BORDER_REGION_ANTI_DRUG_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.TABOK_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.TABOK_REGION_ANTI_DRUG_DIRECTORATE.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ACADEMY_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.ACADEMY_REGION_ANTI_DRUG_DIRECTORATE.getCode()).getUnitId();
			}

		} catch (Exception e) {
			Log4j.traceErrorException(WFPositionService.class, e, "WFPositionService");
			throw new BusinessException("error_workflow");
		}
		return unitId;
	}

	/**
	 * getRegionIntelleginceDepartmentManager
	 * 
	 * @param regionId
	 * @return
	 * @throws BusinessException
	 */
	public static Long getRegionIntelligenceDepartmentId(long regionId) throws BusinessException {
		Long unitId = null;
		try {
			WFPosition pos = getWFPositionByUnitId(regionId);
			if (pos.getPositionId().equals((long) WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.DIRECTORATE_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ALMADINA_REGION_DEPARTMENT.getCode())) {
				unitId = WFPositionService.getWFPosition(WFPositionsEnum.ALMADINA_ALMNWARA_REGION_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ASEER_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.ASEER_REGION_DIRECTOR_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.EASTERN_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.EASTERN_REGION_DIRECTOR_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.GAZAN_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.GAZAN_REGION_DIRECTOR_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.GOUF_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.GOUF_REGION_DIRECTOR_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.MAKA_ELMOKRAMA_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.MAKA_ELMOKRAMA_REGION_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.NAGRAN_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.NAGRAN_REGION_DIRECTOR_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.NORTH_BORDER_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.NORTH_BORDER_REGION_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.TABOK_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.TABOK_REGION_DIRECTOR_INTELLIGENCE_DIRECTORATE.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ACADEMY_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.ACADEMY_REGION_DIRECTOR_INTELLIGENCE_DIRECTORATE.getCode()).getUnitId();
			}

		} catch (Exception e) {
			Log4j.traceErrorException(WFPositionService.class, e, "WFPositionService");
			throw new BusinessException("error_workflow");
		}
		return unitId;
	}

	public static Long getRegionMilitaryPoliceDepartmentId(long regionId) throws BusinessException {
		Long unitId = null;
		try {
			WFPosition pos = getWFPositionByUnitId(regionId);
			if (pos.getPositionId().equals((long) WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.DIRECTORATE_MILITARY_POLICE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ALMADINA_REGION_DEPARTMENT.getCode())) {
				unitId = WFPositionService.getWFPosition(WFPositionsEnum.ALMADINA_ALMNWARA_REGION_MILITARY_POLICE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ASEER_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.ASEER_REGION_MILITARY_POLICE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.EASTERN_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.EASTERN_REGION_MILITARY_POLICE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.GAZAN_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.GAZAN_REGION_MILITARY_POLICE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.GOUF_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.GOUF_REGION_MILITARY_POLICE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.MAKA_ELMOKRAMA_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.MAKA_ELMOKRAMA_REGION_MILITARY_POLICE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.NAGRAN_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.NAGRAN_REGION_MILITARY_POLICE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.NORTH_BORDER_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.NORTH_BORDER_REGION_MILITARY_POLICE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.TABOK_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.TABOK_REGION_MILITARY_POLICE_DIPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ACADEMY_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.ACADEMY_REGION_MILITARY_POLICE_DIPARTMENT.getCode()).getUnitId();
			}

		} catch (Exception e) {
			Log4j.traceErrorException(WFPositionService.class, e, "WFPositionService");
			throw new BusinessException("error_workflow");
		}
		return unitId;
	}

	/**
	 * getRegionSecurityIntelligenceDepartmentId
	 * 
	 * @param regionId
	 * @return
	 * @throws BusinessException
	 */
	public static Long getRegionSecurityIntelligenceDepartmentId(long regionId) throws BusinessException {
		Long unitId = null;
		try {
			WFPosition pos = getWFPositionByUnitId(regionId);
			if (pos.getPositionId().equals((long) WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.GENERAL_INTELLIGENCE_DIRECTORATE.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ALMADINA_REGION_DEPARTMENT.getCode())) {
				unitId = WFPositionService.getWFPosition(WFPositionsEnum.ALMADINA_ALMNWARA_REGION_SECURITY_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ASEER_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.ASEER_REGION_SECURITY_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.EASTERN_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.EASTERN_REGION_SECURITY_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.GAZAN_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.GAZAN_REGION_SECURITY_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.GOUF_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.GOUF_REGION_SECURITY_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.MAKA_ELMOKRAMA_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.MAKA_ELMOKRAMA_REGION_SECURITY_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.NAGRAN_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.NAGRAN_REGION_SECURITY_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.NORTH_BORDER_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.NORTH_BORDER_REGION_SECURITY_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.TABOK_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.TABOK_REGION_SECURITY_INTELLIGENCE_DIRECTORATE.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ACADEMY_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.ACADEMY_REGION_SECURITY_INTELLIGENCE_DIRECTORATE.getCode()).getUnitId();
			}

		} catch (Exception e) {
			Log4j.traceErrorException(WFPositionService.class, e, "WFPositionService");
			throw new BusinessException("error_workflow");
		}
		return unitId;
	}
	
	/**
	 * getRegionProtectionDepartmentId
	 * 
	 * @param regionId
	 * @return
	 * @throws BusinessException
	 */
	public static Long getRegionProtectionDepartmentId(long regionId) throws BusinessException {
		Long unitId = null;
		try {
			WFPosition pos = getWFPositionByUnitId(regionId);
			if (pos.getPositionId().equals((long) WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.GENERAL_PROTECTION_DIRECTORATE.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ALMADINA_REGION_DEPARTMENT.getCode())) {
				unitId = WFPositionService.getWFPosition(WFPositionsEnum.ALMADINA_ALMNWARA_REGION_PROTECTION_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ASEER_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.ASEER_REGION_PROTECTION_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.EASTERN_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.EASTERN_REGION_PROTECTION_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.GAZAN_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.GAZAN_REGION_PROTECTION_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.GOUF_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.GOUF_REGION_PROTECTION_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.MAKA_ELMOKRAMA_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.MAKA_ELMOKRAMA_REGION_PROTECTION_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.NAGRAN_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.NAGRAN_REGION_PROTECTION_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.NORTH_BORDER_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.NORTH_BORDER_REGION_PROTECTION_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.TABOK_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.TABOK_REGION_PROTECTION_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ACADEMY_REGION_DEPARTMENT.getCode())) {
				unitId = getWFPosition(WFPositionsEnum.ACADEMY_REGION_PROTECTION_DEPARTMENT.getCode()).getUnitId();
			}

		} catch (Exception e) {
			Log4j.traceErrorException(WFPositionService.class, e, "WFPositionService");
			throw new BusinessException("error_workflow");
		}
		return unitId;
	}

	/**
	 * Get Region Command And Control Department Id
	 * 
	 * @param regionId
	 * @return
	 * @throws BusinessException
	 */
	public static Long getRegionCommandAndControlDepartmentId(long regionId) throws BusinessException {
		try {
			WFPosition pos = WFPositionService.getWFPositionByUnitId(regionId);
			if (pos.getPositionId().equals((long) WFPositionsEnum.EASTERN_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.EASTERN_REGION_COMMAND_AND_CONTROL.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ALMADINA_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.ALMADINA_ALMNWARA_REGION_COMMAND_AND_CONTROL.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ASEER_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.ASEER_REGION_COMMAND_AND_CONTROL.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.GAZAN_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.GAZAN_REGION_COMMAND_AND_CONTROL.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.GOUF_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.GOUF_REGION_COMMAND_AND_CONTROL.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.MAKA_ELMOKRAMA_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.MAKA_ELMOKRAMA_REGION_COMMAND_AND_CONTROL.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.NAGRAN_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.NAGRAN_REGION_COMMAND_AND_CONTROL.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.NORTH_BORDER_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.NORTH_BORDER_REGION_COMMAND_AND_CONTROL.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.TABOK_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.TABOK_REGION_COMMAND_AND_CONTROL.getCode()).getUnitId();
			} else {
				return WFPositionService.getWFPosition(WFPositionsEnum.ACADEMY_REGION_COMMAND_AND_CONTROL.getCode()).getUnitId();
			}
		} catch (Exception e) {
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(WFPositionService.class, e, "WFPositionService");
				throw new BusinessException("error_workflow");
			}
		}
	}

	/**
	 * get region anti_drug department
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public static Long getHCMDecisionDepartmentId() throws BusinessException {
		Long unitId = null;
		try {
			unitId = getWFPosition(WFPositionsEnum.GENERAL_INTELLIGENCE_DIRECTORATE.getCode()).getUnitId();
		} catch (Exception e) {
			Log4j.traceErrorException(WFPositionService.class, e, "WFPositionService");
			throw new BusinessException("error_general");
		}
		return unitId;
	}

	/**********************************************/
	/******************* Queries *****************/

	/**
	 * getAllWFPosition
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public static List<WFPositionData> getWFPositionData(String positionDesc) throws BusinessException {
		try {
			return searchWFPositionData(FlagsEnum.ALL.getCode(), positionDesc);
		} catch (NoDataException e) {
			return new ArrayList<WFPositionData>();
		}
	}

	/**
	 * searchWFPositionData
	 * 
	 * @param id
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<WFPositionData> searchWFPositionData(long id, String positionDesc) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_POSITION_ID", id);
			qParams.put("P_POSITION_DESC", (positionDesc == null || positionDesc.isEmpty()) ? FlagsEnum.ALL.getCode() + "" : "%" + positionDesc.trim() + "%");
			return DataAccess.executeNamedQuery(WFPositionData.class, QueryNamesEnum.WF_POSITION_DATA_GET_WF_POSITION.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(WFPositionService.class, e, "WFPositionService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get Wf Position Data For Transfered Employees
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public static List<WFPositionData> getWFPositionDataForTransferedEmployees() throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_DISC", WFPositionDiscriminatorEnum.EMPLOYEE.getCode());
			return DataAccess.executeNamedQuery(WFPositionData.class, QueryNamesEnum.WF_POSITION_DATA_GET_WF_POSITION_FOR_TRANSFERED_EMPLOYEES.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(WFPositionService.class, e, "WFPositionService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return new ArrayList<WFPositionData>();
		}
	}

	/********************************************** Position Methods ***********************************************/

	/**
	 * get WF Position by position id
	 * 
	 * @param positionId
	 * @return
	 * @throws BusinessException
	 */
	public static WFPosition getWFPosition(int positionId) throws BusinessException {
		try {
			WFPosition wFPosition = searchWFPosition(positionId, FlagsEnum.ALL.getCode());
			if (wFPosition.getDiscriminator().equals(WFPositionDiscriminatorEnum.EMPLOYEE.getCode())) {
				if (wFPosition.getEmpId() != null) {
					return wFPosition;
				} else {
					throw new BusinessException("error_employeeAttachedWithWfPositionNotSettedYet", new Object[] { wFPosition.getPositionDesc() });
				}
			} else if (wFPosition.getDiscriminator().equals(WFPositionDiscriminatorEnum.DEPARTMENT.getCode())) {
				if (wFPosition.getUnitId() != null) {
					return wFPosition;
				} else {
					throw new BusinessException("error_DepartmentAttachedWithWfPositionNotSettedYet", new Object[] { wFPosition.getPositionDesc() });
				}
			} else if (wFPosition.getDiscriminator().equals(WFPositionDiscriminatorEnum.EMPS_GROUP.getCode())) {
				if (wFPosition.getEmpsGroup() != null) {
					return wFPosition;
				} else {
					throw new BusinessException("error_employeeAttachedWithWfPositionNotSettedYet", new Object[] { wFPosition.getPositionDesc() });
				}
			}
		} catch (NoDataException e) {
			throw new BusinessException("error_general");
		}
		return null;
	}

	/**
	 * getWFPositionByUnitId
	 * 
	 * @param unitId
	 * @return
	 * @throws BusinessException
	 */
	public static WFPosition getWFPositionByUnitId(long unitId) throws BusinessException {
		try {
			return searchWFPosition(FlagsEnum.ALL.getCode(), unitId);
		} catch (NoDataException e) {
			throw new BusinessException("error_general");
		}
	}

	/**
	 * get WF Position by position description
	 * 
	 * @param description
	 * @return
	 * @throws BusinessException
	 */
	public static WFPosition getWFPosition(String description) throws BusinessException {
		try {
			WFPosition wFPosition = searchWFPositionByDescription(description);
			if (wFPosition.getDiscriminator().equals(WFPositionDiscriminatorEnum.EMPLOYEE.getCode())) {
				if (wFPosition.getEmpId() != null) {
					return wFPosition;
				} else {
					throw new BusinessException("error_employeeAttachedWithWfPositionNotSettedYet", new Object[] { wFPosition.getPositionDesc() });
				}
			} else if (wFPosition.getDiscriminator().equals(WFPositionDiscriminatorEnum.DEPARTMENT.getCode())) {
				if (wFPosition.getUnitId() != null) {
					return wFPosition;
				} else {
					throw new BusinessException("error_DepartmentAttachedWithWfPositionNotSettedYet", new Object[] { wFPosition.getPositionDesc() });
				}
			} else if (wFPosition.getDiscriminator().equals(WFPositionDiscriminatorEnum.EMPS_GROUP.getCode())) {
				if (wFPosition.getEmpsGroup() != null) {
					return wFPosition;
				} else {
					throw new BusinessException("error_employeeAttachedWithWfPositionNotSettedYet", new Object[] { wFPosition.getPositionDesc() });
				}
			}
		} catch (NoDataException e) {
			throw new BusinessException("error_general");
		}
		return null;
	}

	private static WFPosition searchWFPosition(int positionId, long unitId) throws NoDataException, BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_POSITION_ID", positionId);
			qParams.put("P_UNIT_ID", unitId);
			return DataAccess.executeNamedQuery(WFPosition.class, QueryNamesEnum.WF_GET_POSITION.getCode(), qParams).get(0);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(WFPositionService.class, e, "WFPositionService");
			throw new BusinessException("error_DBError");
		}
	}

	private static WFPosition searchWFPositionByDescription(String description) throws NoDataException, BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_DESCRIPTION", (description == null || description.equals(FlagsEnum.ALL.getCode() + "")) ? FlagsEnum.ALL.getCode() + "" : "%" + description + "%");
			return DataAccess.executeNamedQuery(WFPosition.class, QueryNamesEnum.WF_GET_POSITION_BY_DESCRIPTION.getCode(), qParams).get(0);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(WFPositionService.class, e, "WFPositionService");
			throw new BusinessException("error_DBError");
		}
	}
}
