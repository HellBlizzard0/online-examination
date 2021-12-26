package com.code.services.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.workflow.WFDelegation;
import com.code.dal.orm.workflow.WFDelegationData;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseConstraintException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.log4j.Log4j;

public class WFDelegationService extends BaseService {
	/**
	 * Singleton
	 */
	private WFDelegationService() {
	}

	/**
	 * Search delegation by most specific rules first
	 * 
	 * @param empId
	 * @param processId
	 * @return
	 * @throws BusinessException
	 */
	protected static long getDelegate(long empId, long processId) throws BusinessException {
		List<Long> prevIds = new ArrayList<Long>();
		prevIds.add(empId);
		WFDelegation delegate = new WFDelegation();
		delegate.setDelegateId(empId);
		// i = 0 : Search for process delegation.
		// i = 1 : Search for complete delegation.
		for (int i = 0; i <= 1; i++) {
			while (true) {
				try {
					if (i == 0)
						delegate = getWFDelegation(delegate.getDelegateId(), processId);
					else if (i == 1) {
						delegate = getWFDelegation(delegate.getDelegateId(), FlagsEnum.ALL.getCode());
						i = 0;
					}

					if (prevIds.contains(delegate.getDelegateId()))
						throw new BusinessException("error_delegationLoop");

					prevIds.add(delegate.getDelegateId());
				} catch (NoDataException e) {
					break;
				}
			}
		}
		return delegate.getDelegateId();
	}

	/**
	 * Save Delegation
	 * 
	 * @param wfDelegationData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveDelegation(WFDelegationData wfDelegationData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			DataAccess.addEntity(wfDelegationData.getWfDelegation());
			wfDelegationData.setId(wfDelegationData.getWfDelegation().getId());

			if (!isOpenedSession) {
				session.commitTransaction();
			}
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}
			if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_duplicatedDelegation");
			}
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(WFDelegationService.class, e, "WFDelegationService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Delete Delegation
	 * 
	 * @param wfDelegationData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteDelegate(WFDelegationData wfDelegationData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			DataAccess.deleteEntity(wfDelegationData.getWfDelegation());

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
				Log4j.traceErrorException(WFDelegationService.class, e, "WFDelegationService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/********************************* QUEIRES *********************************/
	/**
	 * Get Delegate By EmpId, ProcessId
	 * 
	 * @param empId
	 * @param processId
	 * @return
	 * @throws NoDataException
	 * @throws BusinessException
	 */
	private static WFDelegation getWFDelegation(long empId, long processId) throws NoDataException, BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_EMP_ID", empId);
			qParams.put("P_PROCESS_ID", processId);
			qParams.put("P_UNIT_IDS_FLAG", FlagsEnum.ALL.getCode());
			qParams.put("P_UNITS_IDS", new Long[] { -1L });
			return DataAccess.executeNamedQuery(WFDelegation.class, QueryNamesEnum.WF_GET_DELEGATE.getCode(), qParams).get(0);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(WFDelegationService.class, e, "WFDelegationService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Wf Delegation By Id
	 * 
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	public static WFDelegationData getWFDelegationById(long id, Integer processIdFlag) throws BusinessException {
		try {
			return searchWFDelegationList(id, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), processIdFlag == null ? FlagsEnum.ALL.getCode() : processIdFlag).get(0);
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * Get Wf Delegation List
	 * 
	 * @param empId
	 * @param delegateId
	 * @param processIdFlag
	 * @return
	 * @throws BusinessException
	 */
	public static List<WFDelegationData> getWFDelegationList(Long empId, Long delegateId, Integer processIdFlag) throws BusinessException {
		try {
			return searchWFDelegationList(FlagsEnum.ALL.getCode(), empId == null ? FlagsEnum.ALL.getCode() : empId, delegateId == null ? FlagsEnum.ALL.getCode() : delegateId, processIdFlag == null ? FlagsEnum.ALL.getCode() : processIdFlag);
		} catch (NoDataException e) {
			return new ArrayList<WFDelegationData>();
		}
	}

	/**
	 * Search Wf Delegation List
	 * 
	 * @param empId
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<WFDelegationData> searchWFDelegationList(long id, long empId, long delegateId, int processIdFlag) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_EMP_ID", empId);
			qParams.put("P_DELEGATE_ID", delegateId);
			qParams.put("P_PROCESS_ID_FLAG", processIdFlag);
			return DataAccess.executeNamedQuery(WFDelegationData.class, QueryNamesEnum.WF_GET_DELEGATE_DATA.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(WFDelegationService.class, e, "WFDelegationService");
			throw new BusinessException("error_DBError");
		}
	}
}
