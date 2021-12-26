package com.code.services.workflow;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.workflow.WFInstance;
import com.code.dal.orm.workflow.WFInstanceData;
import com.code.dal.orm.workflow.WFProcess;
import com.code.dal.orm.workflow.WFProcessGroup;
import com.code.dal.orm.workflow.WFTask;
import com.code.dal.orm.workflow.WFTaskData;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryNamesEnum;
import com.code.enums.WFInstanceStatusEnum;
import com.code.enums.WFTaskActionsEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.log4j.Log4j;
import com.code.services.util.HijriDateService;

public abstract class BaseWorkFlow extends BaseService {
	protected BaseWorkFlow() {
	}

	/****************************************** Instance Methods ****************************************************/
	protected static WFInstance addWFInstance(long processId, long requesterId, Date requestDate, Date hijriRequestDate, int status, String attachments, String arMsg, String enMsg, CustomSession session) throws DatabaseException {
		WFInstance instance = new WFInstance();
		instance.setProcessId(processId);
		instance.setRequesterId(requesterId);
		instance.setRequestDate(requestDate);
		instance.setHijriRequestDate(hijriRequestDate);
		instance.setStatus(status);
		instance.setAttachments(attachments);
		instance.setArabicDetailsSummary(arMsg);
		instance.setEnglishDetailsSummary(enMsg);

		DataAccess.addEntity(instance, session);

		return instance;
	}

	protected static WFInstance changeWFInstanceStatus(WFInstance instance, int newStatus, CustomSession session) throws DatabaseException {
		instance.setStatus(newStatus);

		DataAccess.updateEntity(instance, session);

		return instance;
	}

	protected static void closeWFInstance(WFInstance instance, WFTask task, String action, Date actionDate, Date hijriActionDate, CustomSession... useSession) throws DatabaseException {
		boolean isOpenedSession = isSessionOpened(useSession);

		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();

			setWFTaskAction(task, action, actionDate, hijriActionDate, session);

			if (canChangeWFInstanceStatusToComplete(instance.getInstanceId()))
				changeWFInstanceStatus(instance, WFInstanceStatusEnum.REJECTED.getCode(), session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession)
				session.rollbackTransaction();
			throw new DatabaseException(e.getMessage());
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	public static void closeWFInstanceByNotification(WFInstance instance, WFTask task) throws BusinessException {
		try {
			closeWFInstance(instance, task, WFTaskActionsEnum.NOTIFIED.getCode(), new Date(), HijriDateService.getHijriSysDate());
		} catch (DatabaseException e) {
			throw new BusinessException("error_general");
		}
	}

	protected static boolean canChangeWFInstanceStatusToDone(long instanceId) throws DatabaseException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INSTANCE_ID", instanceId);
			Long count = DataAccess.executeNamedQuery(Long.class, QueryNamesEnum.WF_CAN_CHANGE_WFINSTANCE_DONE.getCode(), qParams).get(0);

			if (count == 1)
				return true;

			return false;
		} catch (NoDataException e) {
			return false;
		}
	}

	protected static boolean canChangeWFInstanceStatusToComplete(long instanceId) throws DatabaseException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INSTANCE_ID", instanceId);
			Long count = DataAccess.executeNamedQuery(Long.class, QueryNamesEnum.WF_CAN_CHANGE_WFINSTANCE_COMPLETE.getCode(), qParams).get(0);

			if (count == 1)
				return true;

			return false;
		} catch (NoDataException e) {
			return false;
		}
	}

	public static WFInstance getWFInstanceById(long instanceId) throws NoDataException, BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INSTANCE_ID", instanceId);
			return DataAccess.executeNamedQuery(WFInstance.class, QueryNamesEnum.WF_GET_WFINSTANCE_BY_ID.getCode(), qParams).get(0);
		} catch (DatabaseException e) {
			throw new BusinessException("error_general");
		}
	}

	public static WFInstanceData getWFInstanceDataById(long instanceId) throws NoDataException, BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INSTANCE_ID", instanceId);
			return DataAccess.executeNamedQuery(WFInstanceData.class, QueryNamesEnum.WF_GET_WFINSTANCE_DATA_BY_ID.getCode(), qParams).get(0);
		} catch (DatabaseException e) {
			throw new BusinessException("error_general");
		}
	}

	public static List<WFInstanceData> searchWFInstancesData(long requesterId, long processGroupId, long processId, boolean isRunning, boolean isASC, String instanceDetails, Date fromDate, Date toDate) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_REQUESTER_ID", requesterId);
			qParams.put("P_PROCESS_GROUP_ID", processGroupId);
			qParams.put("P_PROCESS_ID", processId);
			qParams.put("P_STATUS_VALUES", isRunning ? new Integer[] { WFInstanceStatusEnum.RUNING.getCode() } : new Integer[] { WFInstanceStatusEnum.DONE.getCode(), WFInstanceStatusEnum.COMPLETED.getCode() });
			qParams.put("P_INSTANCE_DETAILS", "%" + instanceDetails + "%");
			qParams.put("P_INSTANCE_DETAILS_FLAG", instanceDetails == null || instanceDetails.trim().isEmpty() || instanceDetails.trim().equals("") ? FlagsEnum.OFF.getCode() + "" : FlagsEnum.ON.getCode() + "");
			qParams.put("P_FROM_DATE_NULL", fromDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_TO_DATE_NULL", toDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_FROM_DATE", fromDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(fromDate));
			qParams.put("P_TO_DATE", toDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(toDate));

			List<WFInstanceData> instances = DataAccess.executeNamedQuery(WFInstanceData.class, QueryNamesEnum.WF_SEARCH_WFINSTANCE_DATA.getCode(), qParams);

			if (isASC)
				Collections.reverse(instances);

			return instances;
		} catch (NoDataException e) {
			return new ArrayList<WFInstanceData>();
		} catch (DatabaseException e) {
			throw new BusinessException("error_general");
		}
	}

	public static Long getWFInstancesUnderProcessingCount(long requesterId) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_REQUESTER_ID", requesterId);

			return DataAccess.executeNamedQuery(Long.class, QueryNamesEnum.WF_GET_WFINSTANCE_UNDER_PROCESSING_COUNT.getCode(), qParams).get(0);
		} catch (NoDataException e) {
			return 0L;
		} catch (DatabaseException e) {
			throw new BusinessException("error_general");
		}
	}

	public static long countInstancesByProcessesIds(Long[] processesIds, Integer[] statusesIds) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			if (processesIds != null && processesIds.length > 0) {
				qParams.put("P_PROCESSES_IDS_FLAG", FlagsEnum.ON.getCode());
				qParams.put("P_PROCESSES_IDS", processesIds);
			} else {
				qParams.put("P_PROCESSES_IDS_FLAG", FlagsEnum.ALL.getCode());
				qParams.put("P_PROCESSES_IDS", new Long[] { (long) FlagsEnum.ALL.getCode() });
			}

			if (statusesIds != null && statusesIds.length > 0) {
				qParams.put("P_STATUSES_IDS_FLAG", FlagsEnum.ON.getCode());
				qParams.put("P_STATUSES_IDS", statusesIds);
			} else {
				qParams.put("P_STATUSES_IDS_FLAG", FlagsEnum.ALL.getCode());
				qParams.put("P_STATUSES_IDS", new Integer[] { FlagsEnum.ALL.getCode() });
			}

			return DataAccess.executeNamedQuery(Long.class, QueryNamesEnum.WF_COUNT_INSTANCES_BY_PROCESSES_IDS.getCode(), qParams).get(0);
		} catch (NoDataException e) {
			return 0L;
		} catch (DatabaseException e) {
			Log4j.traceErrorException(BaseWorkFlow.class, e, "BaseWorkFlow");
			throw new BusinessException("error_general");
		}
	}

	/****************************************************************************************************************/

	/********************************************** Task Methods ****************************************************/
	protected static WFTask addWFTask(long instanceId, long assigneeId, long originalId, Date assignDate, Date hijriAssignDate, String taskUrl, String assigneeWfRole, String level, String arabicDetailsSummary, String englishDetailsSummary, CustomSession session) throws DatabaseException {
		WFTask task = new WFTask();
		task.setInstanceId(instanceId);
		task.setAssigneeId(assigneeId);
		task.setOriginalId(originalId);
		task.setAssignDate(assignDate);
		task.setHijriAssignDate(hijriAssignDate);
		task.setTaskUrl(taskUrl);
		task.setAssigneeWfRole(assigneeWfRole);
		task.setLevel(level);
		task.setArabicDetailsSummary(arabicDetailsSummary);
		task.setEnglishDetailsSummary(englishDetailsSummary);

		DataAccess.addEntity(task, session);

		return task;
	}

	protected static WFTask addWFTask(long instanceId, long assigneeId, long originalId, Date assignDate, Date hijriAssignDate, String taskUrl, String assigneeWfRole, String level, String notes, String arabicDetailsSummary, String englishDetailsSummary, CustomSession session) throws DatabaseException {
		WFTask task = new WFTask();
		task.setInstanceId(instanceId);
		task.setAssigneeId(assigneeId);
		task.setOriginalId(originalId);
		task.setAssignDate(assignDate);
		task.setHijriAssignDate(hijriAssignDate);
		task.setTaskUrl(taskUrl);
		task.setAssigneeWfRole(assigneeWfRole);
		task.setLevel(level);
		task.setNotes(notes);
		task.setArabicDetailsSummary(arabicDetailsSummary);
		task.setEnglishDetailsSummary(englishDetailsSummary);

		DataAccess.addEntity(task, session);

		return task;
	}

	protected static WFTask addWFTask(long instanceId, long assigneeId, long originalId, Date assignDate, Date hijriAssignDate, String taskUrl, String assigneeWfRole, String level, String notes, String arabicDetailsSummary, String englishDetailsSummary, String flexField1, CustomSession session) throws DatabaseException {
		WFTask task = new WFTask();
		task.setInstanceId(instanceId);
		task.setAssigneeId(assigneeId);
		task.setOriginalId(originalId);
		task.setAssignDate(assignDate);
		task.setHijriAssignDate(hijriAssignDate);
		task.setTaskUrl(taskUrl);
		task.setAssigneeWfRole(assigneeWfRole);
		task.setLevel(level);
		task.setNotes(notes);
		task.setArabicDetailsSummary(arabicDetailsSummary);
		task.setEnglishDetailsSummary(englishDetailsSummary);
		task.setFlexField1(flexField1);
		DataAccess.addEntity(task, session);

		return task;
	}

	protected static void addNotificationTasks(String assigneeIdsString, long instanceId, String taskUrl, Date curDate, Date curHijriDate, String levelPrefix, int levelStartIndex, String arabicDetailsSummary, String englishDetailsSummary, CustomSession session) throws BusinessException {
		try {
			if (assigneeIdsString != null && assigneeIdsString.length() > 0) {
				String[] assigneeIdsStrings = assigneeIdsString.split(",");
				if (assigneeIdsStrings != null && assigneeIdsStrings.length > 0) {
					for (int i = 0; i < assigneeIdsStrings.length; i++) {
						long assigneelId = Long.parseLong(assigneeIdsStrings[i]);
						addWFTask(instanceId, assigneelId, assigneelId, curDate, curHijriDate, taskUrl, WFTaskRolesEnum.NOTIFICATION.getCode(), levelPrefix + "." + (i + levelStartIndex), arabicDetailsSummary, englishDetailsSummary, session);
					}
				}
			}
		} catch (DatabaseException e) {
			Log4j.traceErrorException(BaseWorkFlow.class, e, "BaseWorkFlow");
			throw new BusinessException("error_general");
		}
	}

	protected static WFTask setWFTaskAction(WFTask task, String action, Date actionDate, Date hijriActionDate, CustomSession... useSession) throws DatabaseException {
		boolean isOpenedSession = isSessionOpened(useSession);

		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();

			task.setAction(action);
			task.setActionDate(actionDate);
			task.setHijriActionDate(hijriActionDate);

			DataAccess.updateEntity(task, session);

			if (!isOpenedSession)
				session.commitTransaction();

			return task;
		} catch (Exception e) {
			if (!isOpenedSession)
				session.rollbackTransaction();
			throw new DatabaseException(e.getMessage());
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	protected static WFTask completeWFTask(WFTask curTask, String action, Date actionDate, Date hijriActionDate, long instanceId, long assigneeId, long originalId, String taskUrl, String assigneeWfRole, String level, String arabicDetailsSummary, String englishDetailsSummary, CustomSession... useSession) throws DatabaseException {
		boolean isOpenedSession = isSessionOpened(useSession);

		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();

			setWFTaskAction(curTask, action, actionDate, hijriActionDate, session);
			WFTask newTask = addWFTask(instanceId, assigneeId, originalId, actionDate, hijriActionDate, taskUrl, assigneeWfRole, level, arabicDetailsSummary, englishDetailsSummary, session);

			if (!isOpenedSession)
				session.commitTransaction();

			return newTask;
		} catch (Exception e) {
			if (!isOpenedSession)
				session.rollbackTransaction();
			throw new DatabaseException(e.getMessage());
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	protected static WFTask completeWFTask(WFTask curTask, String action, Date actionDate, Date hijriActionDate, long instanceId, long assigneeId, long originalId, String taskUrl, String assigneeWfRole, String level, String notes, String arabicDetailsSummary, String englishDetailsSummary, CustomSession... useSession) throws DatabaseException {
		boolean isOpenedSession = isSessionOpened(useSession);

		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();

			setWFTaskAction(curTask, action, actionDate, hijriActionDate, session);
			WFTask newTask = addWFTask(instanceId, assigneeId, originalId, actionDate, hijriActionDate, taskUrl, assigneeWfRole, level, notes, arabicDetailsSummary, englishDetailsSummary, session);

			if (!isOpenedSession)
				session.commitTransaction();

			return newTask;
		} catch (Exception e) {
			if (!isOpenedSession)
				session.rollbackTransaction();
			throw new DatabaseException(e.getMessage());
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	protected static WFTask completeWFTask(WFTask curTask, String action, Date actionDate, Date hijriActionDate, long instanceId, long assigneeId, long originalId, String taskUrl, String assigneeWfRole, String level, String notes, String arabicDetailsSummary, String englishDetailsSummary, String flexField1, CustomSession... useSession) throws DatabaseException {
		boolean isOpenedSession = isSessionOpened(useSession);

		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();

			setWFTaskAction(curTask, action, actionDate, hijriActionDate, session);
			WFTask newTask = addWFTask(instanceId, assigneeId, originalId, actionDate, hijriActionDate, taskUrl, assigneeWfRole, level, notes, arabicDetailsSummary, englishDetailsSummary, flexField1, session);

			if (!isOpenedSession)
				session.commitTransaction();

			return newTask;
		} catch (Exception e) {
			if (!isOpenedSession)
				session.rollbackTransaction();
			throw new DatabaseException(e.getMessage());
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	public static void doNotified(WFTask curTask) throws BusinessException {
		CustomSession session = DataAccess.getSession();

		try {
			session.beginTransaction();

			setWFTaskAction(curTask, WFTaskActionsEnum.NOTIFIED.getCode(), new Date(), HijriDateService.getHijriSysDate(), session);

			session.commitTransaction();
		} catch (Exception e) {
			session.rollbackTransaction();
			throw new BusinessException("error_general");
		} finally {
			session.close();
		}
	}

	public static WFTask getWFTaskById(long taskId) throws NoDataException, BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_TASK_ID", taskId);
			return DataAccess.executeNamedQuery(WFTask.class, QueryNamesEnum.WF_GET_WFTASK_BY_ID.getCode(), qParams).get(0);
		} catch (DatabaseException e) {
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get WF tasks list by ids of tasks list
	 * 
	 * @param taskIdsList
	 * @return
	 * @throws BusinessException
	 */
	public static List<WFTask> getWFTaskByTaskIdsList(Long[] taskIdsList) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_TASK_IDS_LIST", taskIdsList);
			return DataAccess.executeNamedQuery(WFTask.class, QueryNamesEnum.WF_TASK_GET_WF_TASK_BY_TASK_IDS_LIST.getCode(), qParams);
		} catch (DatabaseException e) {
			throw new BusinessException("error_general");
		} catch (NoDataException e) {
			return new ArrayList<WFTask>();
		}
	}

	public static List<WFTask> getWFInstanceTasksByInstanceId(long instanceId) throws BusinessException {
		try {
			return getWFInstanceTasks(instanceId);
		} catch (NoDataException e) {
			return new ArrayList<WFTask>();
		}
	}

	public static List<WFTask> getWFInstanceTasks(long instanceId) throws NoDataException, BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INSTANCE_ID", instanceId);
			return DataAccess.executeNamedQuery(WFTask.class, QueryNamesEnum.WF_GET_WFINSTANCE_TASKS.getCode(), qParams);
		} catch (DatabaseException e) {
			throw new BusinessException("error_general");
		}
	}

	public static List<WFTask> getWFInstanceTasksByRole(long instanceId, String role) throws BusinessException {
		try {
			return searchWFInstanceTasksByRole(instanceId, role);
		} catch (NoDataException e) {
			return new ArrayList<WFTask>();
		}
	}

	private static List<WFTask> searchWFInstanceTasksByRole(long instanceId, String role) throws NoDataException, BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INSTANCE_ID", instanceId);
			qParams.put("P_ROLE", role);
			return DataAccess.executeNamedQuery(WFTask.class, QueryNamesEnum.WF_GET_WFINSTANCE_TASKS_BY_ROLE.getCode(), qParams);
		} catch (DatabaseException e) {
			throw new BusinessException("error_general");
		}
	}

	public static List<WFTask> getWFInstanceTasksByRoleAndLevel(long instanceId, String role, String level) throws NoDataException, BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INSTANCE_ID", instanceId);
			qParams.put("P_ROLE", role);
			qParams.put("P_LEVEL", level);
			return DataAccess.executeNamedQuery(WFTask.class, QueryNamesEnum.WF_GET_WFINSTANCE_TASKS_BY_ROLE_LEVEL.getCode(), qParams);
		} catch (DatabaseException e) {
			throw new BusinessException("error_general");
		}
	}

	public static WFTaskData getWFTaskDataById(long taskId) throws NoDataException, BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_TASK_ID", taskId);
			return DataAccess.executeNamedQuery(WFTaskData.class, QueryNamesEnum.WF_GET_WFTASK_DATA_BY_ID.getCode(), qParams).get(0);
		} catch (DatabaseException e) {
			throw new BusinessException("error_general");
		}
	}

	public static List<WFTaskData> searchWFTasksData(long assigneeId, String taskOwnerName, String taskDetails, long processGroupId, long processId, boolean isRunning, int taskRole, boolean isDESC, Date fromDate, Date toDate) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ASSIGNEE_ID", assigneeId);
			qParams.put("P_TASK_OWNER_NAME", "%" + taskOwnerName + "%");
			qParams.put("P_TASK_DETAILS", "%" + taskDetails + "%");
			qParams.put("P_TASK_DETAILS_FLAG", taskDetails == null || taskDetails.trim().isEmpty() || taskDetails.trim().equals("") ? FlagsEnum.OFF.getCode() + "" : FlagsEnum.ON.getCode() + "");
			qParams.put("P_PROCESS_GROUP_ID", processGroupId);
			qParams.put("P_PROCESS_ID", processId);
			qParams.put("P_RUNNING", isRunning ? 1 : 0);
			qParams.put("P_TASK_ROLE", taskRole);
			qParams.put("P_NOTIFICATION_ROLE", new String[] { WFTaskRolesEnum.NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION_WITHOUT_RESEND.getCode() });
			qParams.put("P_FROM_DATE_NULL", fromDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_TO_DATE_NULL", toDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_FROM_DATE", fromDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(fromDate));
			qParams.put("P_TO_DATE", toDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(toDate));

			List<WFTaskData> tasks = DataAccess.executeNamedQuery(WFTaskData.class, QueryNamesEnum.WF_SEARCH_WFTASK_DATA.getCode(), qParams);

			if (isDESC)
				Collections.reverse(tasks);

			return tasks;
		} catch (NoDataException e) {
			return new ArrayList<WFTaskData>();
		} catch (DatabaseException e) {
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get next wf notification given specific user that has no action and task URL
	 * 
	 * @param assigneeId
	 * @param unReaded
	 * @param taskUrl
	 * @return
	 * @throws BusinessException
	 */
	public static WFTask getWfNextNotification(long assigneeId, boolean unReaded, String taskUrl) throws BusinessException {
		return searchWfNextNotification(assigneeId, unReaded, taskUrl);
	}

	/**
	 * Search next wf notification given specific user that has no action and task URL
	 * 
	 * @param assigneeId
	 * @param unReaded
	 * @return
	 * @throws BusinessException
	 */
	private static WFTask searchWfNextNotification(long assigneeId, boolean unReaded, String taskUrl) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ASSIGNEE_ID", assigneeId);
			qParams.put("P_TASK_URL", taskUrl);
			qParams.put("P_UN_READED", unReaded ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			return DataAccess.executeNamedQuery(WFTask.class, QueryNamesEnum.WF_GET_NEXT_NOTIFICATION.getCode(), qParams).get(0);
		} catch (NoDataException e) {
			return null;
		} catch (DatabaseException e) {
			throw new BusinessException("error_general");
		}
	}

	/**
	 * get next wf notification (having the notification page url) for specific user
	 * 
	 * @param assigneeId
	 * @param unReaded
	 * @return
	 * @throws BusinessException
	 */
	public static WFTask getWfNextNotification(long assigneeId, Long taskId, boolean unReaded, String taskUrl) throws BusinessException {
		return searchWfNextNotification(assigneeId, taskId, unReaded, taskUrl);
	}

	/**
	 * 
	 * @param assigneeId
	 * @param unReaded
	 * @return
	 * @throws BusinessException
	 */
	private static WFTask searchWfNextNotification(long assigneeId, Long taskId, boolean unReaded, String taskUrl) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ASSIGNEE_ID", assigneeId);
			qParams.put("P_ID", taskId);
			return DataAccess.executeNamedQuery(WFTask.class, QueryNamesEnum.WF_GET_NEXT_NOTIFICATION_RELATTIVE_TO.getCode(), qParams).get(0);
		} catch (NoDataException e) {
			return null;
		} catch (DatabaseException e) {
			throw new BusinessException("error_general");
		}
	}

	public static Long countWFTasks(long assigneeId, int notificationFlag) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ASSIGNEE_ID", assigneeId);
			qParams.put("P_NOTIFICATION_ROLE", new String[] { WFTaskRolesEnum.NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION_WITHOUT_RESEND.getCode() });
			qParams.put("P_NOTIFICATION_FLAG", notificationFlag);
			return DataAccess.executeNamedQuery(Long.class, QueryNamesEnum.WF_COUNT_TASKS.getCode(), qParams).get(0);
		} catch (DatabaseException e) {
			throw new BusinessException("error_general");
		} catch (NoDataException e) {
			return 0L;
		}
	}

	/**
	 * Get WF running tasks by process id, assignee id and assignee role
	 * 
	 * @param processId
	 * @param assigneeId
	 * @param assigneeRole
	 * @return
	 * @throws BusinessException
	 */
	public static List<Long> getWFRunningTasks(long processId, long assigneeId, String assigneeRole) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_PROCESS_ID", processId);
			qParams.put("P_ASSIGNEE_ID", assigneeId);
			qParams.put("P_ASSIGNEE_ROLE", assigneeRole);
			return DataAccess.executeNamedQuery(Long.class, QueryNamesEnum.WF_TASK_DATA_GET_WF_TASK_IDS.getCode(), qParams);
		} catch (DatabaseException e) {
			throw new BusinessException("error_general");
		} catch (NoDataException e) {
			return new ArrayList<Long>();
		}
	}

	public static List<WFTaskData> getWFInstanceTasksData(long instanceId) throws NoDataException, BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INSTANCE_ID", instanceId);
			return DataAccess.executeNamedQuery(WFTaskData.class, QueryNamesEnum.WF_GET_WFINSTANCE_TASKS_DATA.getCode(), qParams);
		} catch (DatabaseException e) {
			throw new BusinessException("error_general");
		}
	}

	public static List<WFTaskData> getWFInstanceTasksDataSortedByTaskId(long instanceId) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INSTANCE_ID", instanceId);
			return DataAccess.executeNamedQuery(WFTaskData.class, QueryNamesEnum.WF_GET_WFINSTANCE_TASKS_DATA_SORTED_BY_TASK_ID.getCode(), qParams);
		} catch (DatabaseException e) {
			throw new BusinessException("error_general");
		} catch (NoDataException e) {
			throw new BusinessException("error_noData");
		}
	}

	public static List<WFTaskData> getWFInstanceCompletedTasksData(long instanceId, long taskId, String taskLevel) throws BusinessException {
		try {
			String levels = taskLevel, tempLevel = taskLevel;
			while (tempLevel.indexOf(".") != -1) {
				tempLevel = tempLevel.substring(0, tempLevel.lastIndexOf('.'));
				levels += "," + tempLevel;
			}

			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INSTANCE_ID", instanceId);
			qParams.put("P_TASK_ID", taskId);
			qParams.put("P_LEVELS", levels.split(","));
			return DataAccess.executeNamedQuery(WFTaskData.class, QueryNamesEnum.WF_GET_WFINSTANCE_COMPLETED_TASKS_DATA.getCode(), qParams);
		} catch (NoDataException e) {
			return new ArrayList<WFTaskData>();
		} catch (DatabaseException e) {
			throw new BusinessException("error_general");
		}
	}

	public static List<String> getWFTaskSecurityUrls(long assigneeId, long taskId, String taskUrl) throws BusinessException {
		return searchWFTaskSecurityUrls(assigneeId, taskId, taskUrl);
	}

	private static List<String> searchWFTaskSecurityUrls(long assigneeId, long taskId, String taskUrl) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ASSIGNEE_ID", assigneeId);
			qParams.put("P_TASK_ID", taskId);
			qParams.put("P_TASK_URL", (taskUrl == null || taskUrl.equals(FlagsEnum.ALL.getCode() + "")) ? FlagsEnum.ALL.getCode() + "" : "%" + taskUrl + "%");
			return DataAccess.executeNamedQuery(String.class, QueryNamesEnum.WF_TASK_SECURITY.getCode(), qParams);
		} catch (NoDataException e) {
			return new ArrayList<String>();
		} catch (DatabaseException e) {
			Log4j.traceErrorException(BaseWorkFlow.class, e, "BaseWorkFlow");
			throw new BusinessException("error_general");
		}
	}

	public static List<WFTaskData> getWFInstanceTasksDataSorted(long instanceId) throws NoDataException, BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INSTANCE_ID", instanceId);
			return DataAccess.executeNamedQuery(WFTaskData.class, QueryNamesEnum.WF_GET_WFINSTANCE_TASKS_DATA_SORTED.getCode(), qParams);
		} catch (DatabaseException e) {
			throw new BusinessException("error_general");
		}
	}

	/****************************************************************************************************************/

	/**********************************************
	 * Groups - Processes Methods
	 **************************************/
	public static List<WFProcessGroup> getProcessesGroups() throws BusinessException {
		try {
			return DataAccess.executeNamedQuery(WFProcessGroup.class, QueryNamesEnum.WF_GET_PROCESSES_GROUPS.getCode(), null);
		} catch (NoDataException e) {
			return new ArrayList<WFProcessGroup>();
		} catch (DatabaseException e) {
			throw new BusinessException("error_general");
		}
	}

	public static List<WFProcess> getGroupProcesses(long processGroupId) throws BusinessException {
		return searchProcesses(processGroupId, null, null);
	}

	public static List<WFProcess> getGroupProcesses(long processGroupId, String processName, Long[] processesIds) throws BusinessException {
		return searchProcesses(processGroupId, processName, processesIds);
	}

	private static List<WFProcess> searchProcesses(long processGroupId, String processName, Long[] processesIds) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_GROUP_ID", processGroupId);
			qParams.put("P_PROCESS_NAME", (processName == null || processName.equals("")) ? FlagsEnum.ALL.getCode() + "" : "%" + processName + "%");
			if (processesIds != null && processesIds.length > 0) {
				qParams.put("P_PROCESSES_IDS_FLAG", FlagsEnum.ON.getCode());
				qParams.put("P_PROCESSES_IDS", processesIds);
			} else {
				qParams.put("P_PROCESSES_IDS_FLAG", FlagsEnum.ALL.getCode());
				qParams.put("P_PROCESSES_IDS", new Long[] { (long) FlagsEnum.ALL.getCode() });
			}
			return DataAccess.executeNamedQuery(WFProcess.class, QueryNamesEnum.WF_GET_GROUP_PROCESSES.getCode(), qParams);
		} catch (NoDataException e) {
			return new ArrayList<WFProcess>();
		} catch (DatabaseException e) {
			throw new BusinessException("error_general");
		}
	}

	public static List<Object> getProcessesGroupsApprovalCounts(long assigneeId, String assigneeWfRole, Long[] processGroupIds) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ASSIGNEE_ID", assigneeId);
			qParams.put("P_ASSIGNEE_WF_ROLE", assigneeWfRole);
			qParams.put("P_PROCESS_GROUPS_IDS_LIST", processGroupIds);
			return DataAccess.executeNamedQuery(Object.class, QueryNamesEnum.WF_GET_PROCESSES_GROUPS_APRRROVAL_COUNTS.getCode(), qParams);
		} catch (NoDataException e) {
			return new ArrayList<Object>();
		} catch (Exception e) {
			throw new BusinessException("error_general");
		}
	}

	protected static WFProcess getWFProcess(long processId) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_PROCESS_ID", processId);
			return DataAccess.executeNamedQuery(WFProcess.class, QueryNamesEnum.WF_GET_PROCESS.getCode(), qParams).get(0);
		} catch (Exception e) {
			throw new BusinessException("error_general");
		}
	}

	/****************************************************************************************************************/

	/**********************************************
	 * Delegation Methods
	 **********************************************/
	/**
	 * Search delegation by most specific rules first
	 * 
	 * @param empId
	 * @param processId
	 * @return
	 * @throws BusinessException
	 */
	protected static long getDelegate(long empId, long processId) throws BusinessException {
		return WFDelegationService.getDelegate(empId, processId);
	}

	/**
	 * Delegate task to selected employee
	 * 
	 * @param taskId
	 * @param EmpId
	 * @throws BusinessException
	 */
	public static void delegateTask(long taskId, long EmpId) throws BusinessException {
		try {
			WFTask task = getWFTaskById(taskId);
			task.setAssigneeId(EmpId);
			DataAccess.updateEntity(task);
		} catch (Exception e) {
			Log4j.traceErrorException(BaseWorkFlow.class, e, "BaseWorkFlow");
			throw new BusinessException("error_general");
		}

	}

	/****************************************************************************************************************/
	protected static String getParameterizedMessage(String key, String curLang, Object... params) {
		ResourceBundle messages;
		messages = ResourceBundle.getBundle("com.code.resources.messages", new Locale(curLang));

		String value = messages.getString(key);
		return MessageFormat.format(value, params);
	}

	/****************************************************************************************************************/

	/**
	 * get WF Fishsing TasksData
	 * 
	 * @param assigneeId
	 * @param taskOwnerName
	 * @param taskDetails
	 * @param socialId
	 * @param fullName
	 * @param activityDomain
	 * @param navigationDock
	 * @param processId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws BusinessException
	 */
	public static List<WFTaskData> getWFFishingTasksData(Long assigneeId, String taskOwnerName, String taskDetails, String socialId, String fullName, String activityDomain, String navigationDock, Long processId, Date fromDate, Date toDate) throws BusinessException {
		try {
			return searchWFFishingTasksData(assigneeId, taskOwnerName, taskDetails, socialId, fullName, activityDomain, navigationDock, processId, fromDate, toDate);
		} catch (NoDataException e) {
			return new ArrayList<WFTaskData>();
		}
	}

	/**
	 * 
	 * @param assigneeId
	 * @param taskOwnerName
	 * @param taskDetails
	 * @param socialId
	 * @param fullName
	 * @param activityDomain
	 * @param navigationDock
	 * @param processId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<WFTaskData> searchWFFishingTasksData(Long assigneeId, String taskOwnerName, String taskDetails, String socialId, String fullName, String activityDomain, String navigationDock, Long processId, Date fromDate, Date toDate) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ASSIGNEE_ID", assigneeId == null ? FlagsEnum.ALL.getCode() : assigneeId);
			qParams.put("P_TASK_OWNER_NAME", taskOwnerName == null || taskOwnerName.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + taskOwnerName + "%");
			qParams.put("P_TASK_DETAILS", taskDetails == null || taskDetails.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + taskDetails + "%");
			qParams.put("P_SOCIAL_ID", socialId == null || socialId.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + socialId + "%");
			qParams.put("P_FULL_NAME", fullName == null || fullName.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + fullName + "%");
			qParams.put("P_ACTIVITY_DOMAIN", activityDomain.equals("-1") || activityDomain.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + activityDomain + "%");
			qParams.put("P_NAVIGATION_DOCK", navigationDock.equals("-1") || navigationDock.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + navigationDock + "%");
			qParams.put("P_FROM_DATE_NULL", fromDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_TO_DATE_NULL", toDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_PROCESS_ID", processId == null ? FlagsEnum.ALL.getCode() : processId);
			qParams.put("P_FROM_DATE", fromDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(fromDate));
			qParams.put("P_TO_DATE", toDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(toDate));

			List<WFTaskData> tasks = DataAccess.executeNamedQuery(WFTaskData.class, QueryNamesEnum.WF_SEARCH_FISHING_NOTIFICATION_WFTASK_DATA.getCode(), qParams);

			return tasks;
		} catch (DatabaseException e) {
			throw new BusinessException("error_general");
		}
	}

	/**
	 * 
	 * Get WFTasks data by InstanceId and AssigneeRole
	 * 
	 * @param instanceId
	 * @param assigneeWfRole
	 * @return
	 * @throws BusinessException
	 */
	public static List<WFTaskData> getWFTasksDataByInstanceIdAndAssigneeWfRole(long instanceId, String assigneeWfRole) throws BusinessException {
		try {
			return searchWFTasksDataByInstanceIdAndAssigneeWfRole(instanceId, assigneeWfRole);
		} catch (NoDataException e) {
			return new ArrayList<WFTaskData>();
		}
	}

	/**
	 * 
	 * @param instanceId
	 * @param assigneeWfRole
	 * @return
	 * @throws NoDataException
	 * @throws BusinessException
	 */

	private static List<WFTaskData> searchWFTasksDataByInstanceIdAndAssigneeWfRole(long instanceId, String assigneeWfRole) throws NoDataException, BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INSTANCE_ID", instanceId);
			qParams.put("P_ASSIGNEE_WF_ROLE", assigneeWfRole == null || assigneeWfRole.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : assigneeWfRole);
			List<WFTaskData> result = DataAccess.executeNamedQuery(WFTaskData.class, QueryNamesEnum.WF_GET_WFTASKS_DATA_BY_INSTANCE_ID_AND_ASSIGNEE_ROLE.getCode(), qParams);
			return result;
		} catch (DatabaseException e) {
			throw new BusinessException("error_dbError");
		}
	}
}