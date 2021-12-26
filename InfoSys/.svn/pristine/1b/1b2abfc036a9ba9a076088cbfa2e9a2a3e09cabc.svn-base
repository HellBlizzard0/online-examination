package com.code.services.workflow.securityaction;

import java.util.Date;
import java.util.List;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.securitymission.MissionGuardData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.workflow.WFInstance;
import com.code.dal.orm.workflow.WFInstanceData;
import com.code.dal.orm.workflow.WFPosition;
import com.code.dal.orm.workflow.WFTask;
import com.code.enums.WFInstanceStatusEnum;
import com.code.enums.WFPositionsEnum;
import com.code.enums.WFProcessesEnum;
import com.code.enums.WFTaskActionsEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.enums.WFTaskUrlEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.securityaction.SecurityGuardMissionService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.EmployeeService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.BaseWorkFlow;
import com.code.services.workflow.WFPositionService;
import com.code.services.workflow.info.InfoWorkFlow;

public class SecurityGuardMissionWorkFlow extends BaseWorkFlow {

	private SecurityGuardMissionWorkFlow() {
	}

	/**
	 * Save wf instacne and task at : If task in HQ then "Moder eledara elshorta el3askrya" in HQ If task in Region then "Moder eledara elshorta el3askrya" in region to approve or Reject
	 * 
	 * @param missionGuard
	 * @param attachments
	 * @throws BusinessException
	 */
	public static void initMissionGuard(MissionGuardData missionGuard, String attachments, EmployeeData loginEmployee, CustomSession... useSession) throws BusinessException {

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();

			Long regionId = DepartmentService.isRegionDepartment(loginEmployee.getActualDepartmentId());

			String arabicDetailsSummary;
			String englishDetailsSummary;
			String arInstanceMsg = "";
			String enInstanceMsg = "";
			if (missionGuard.getOrderNumber() != null) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_securityGuardMissionTask", "ar", new Object[] { missionGuard.getDepartmentMissionLocationName(), missionGuard.getOrderNumber(), missionGuard.getOrderDateString() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_securityGuardMissionTask", "en", new Object[] { missionGuard.getDepartmentMissionLocationName(), missionGuard.getOrderNumber(), missionGuard.getOrderDateString() });
				arInstanceMsg = getParameterizedMessage("wfMsg_securityGuardMissionInstance", "ar", new Object[] { missionGuard.getOrderNumber(), missionGuard.getOrderSourceDomainName(), missionGuard.getMissionPeriod(), missionGuard.getMissionTypeDescription() });
				enInstanceMsg = getParameterizedMessage("wfMsg_securityGuardMissionInstance", "en", new Object[] { missionGuard.getOrderNumber(), missionGuard.getOrderSourceDomainName(), missionGuard.getMissionPeriod(), missionGuard.getMissionTypeDescription() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_securityGuardMissionTaskOnVerbalOrder", "ar", new Object[] { missionGuard.getDepartmentMissionLocationName(), missionGuard.getOrderDateString() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_securityGuardMissionTaskOnVerbalOrder", "en", new Object[] { missionGuard.getDepartmentMissionLocationName(), missionGuard.getOrderDateString() });
				arInstanceMsg = getParameterizedMessage("wfMsg_securityGuardMissionInstanceVerbal", "ar", new Object[] { missionGuard.getOrderSourceDomainName(), missionGuard.getMissionPeriod(), missionGuard.getMissionTypeDescription() });
				enInstanceMsg = getParameterizedMessage("wfMsg_securityGuardMissionInstanceVerbal", "en", new Object[] { missionGuard.getOrderSourceDomainName(), missionGuard.getMissionPeriod(), missionGuard.getMissionTypeDescription() });
			}
			WFInstance instance = addWFInstance(WFProcessesEnum.SECURITY_GUARD_MISSION.getCode(), loginEmployee.getEmpId(), new Date(), HijriDateService.getHijriSysDate(), WFInstanceStatusEnum.RUNING.getCode(), attachments, arInstanceMsg, enInstanceMsg, session);
			missionGuard.setwFInstanceId(instance.getInstanceId());
			DataAccess.updateEntity(missionGuard.getMissionGuard(), session);
			Long originalId;
			Long assigneeId;
			if (regionId == null) {
				originalId = DepartmentService.getDepartmentManager(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_MILITARY_POLICE_DEPARTMENT.getCode()).getUnitId());
				assigneeId = getDelegate(originalId, WFProcessesEnum.SECURITY_GUARD_MISSION.getCode());
				addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), HijriDateService.getHijriSysDate(), WFTaskUrlEnum.SECURITY_GUARD_MISSION.getCode(), WFTaskRolesEnum.GENERAL_MILITARY_POLICE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			} else {
				originalId = getRegionWorkflowEmployeesRegionDepartment(regionId);
				assigneeId = getDelegate(originalId, WFProcessesEnum.SECURITY_GUARD_MISSION.getCode());
				addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), HijriDateService.getHijriSysDate(), WFTaskUrlEnum.SECURITY_GUARD_MISSION.getCode(), WFTaskRolesEnum.REGION_MILITARY_POLICE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			}

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession)
				session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(SecurityGuardMissionWorkFlow.class, e, "SecurityGuardMissionWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Only one action is taked : then open notifications at either in Region or HQ mas2ol el syarat if has details cars monaweb el shorta requester
	 * 
	 * @param dmTask
	 * @param missionGuard
	 * @param attachments
	 * @throws BusinessException
	 */
	public static void doApproveMissionGuardDM(WFTask dmTask, MissionGuardData missionGuard, EmployeeData loginEmployee, String attachments, CustomSession... useSession) throws BusinessException {

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();

			SecurityGuardMissionService.updateSecurityGuardMission(missionGuard, loginEmployee);

			WFInstanceData instance = getWFInstanceDataById(dmTask.getInstanceId());
			closeWFInstance(instance.getwFInstance(), dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), HijriDateService.getHijriSysDate(), session);

			String arabicDetailsSummary;
			String englishDetailsSummary;
			if (missionGuard.getOrderNumber() != null) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_securityGuardMissionTaskApproveNotification", "ar", new Object[] { missionGuard.getDepartmentMissionLocationName(), missionGuard.getOrderNumber(), missionGuard.getOrderDateString() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_securityGuardMissionTaskApproveNotification", "en", new Object[] { missionGuard.getDepartmentMissionLocationName(), missionGuard.getOrderNumber(), missionGuard.getOrderDateString() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_securityGuardMissionOnVerbalApprovalTaskApproveNotification", "ar", new Object[] { missionGuard.getDepartmentMissionLocationName(), missionGuard.getOrderDateString() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_securityGuardMissionOnVerbalApprovalTaskApproveNotification", "en", new Object[] { missionGuard.getDepartmentMissionLocationName(), missionGuard.getOrderDateString() });
			}

			Long requesterId = instance.getRequesterId();
			Long requesterAssigneeId = getDelegate(requesterId, WFProcessesEnum.SECURITY_GUARD_MISSION.getCode());
			addWFTask(instance.getInstanceId(), requesterAssigneeId, requesterId, new Date(), HijriDateService.getHijriSysDate(), WFTaskUrlEnum.SECURITY_GUARD_MISSION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", null, arabicDetailsSummary, englishDetailsSummary, session);

			Long regionId = DepartmentService.isRegionDepartment(EmployeeService.getEmployee(requesterAssigneeId).getActualDepartmentId());

			if (regionId == null) {
				// Notify Command and Control Center
				Long cAndControlManagerHQId = DepartmentService.getDepartmentManager(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_COMMAND_AND_CONTROL.getCode()).getUnitId());
				Long cAndControlHQAssigneeId = getDelegate(cAndControlManagerHQId, WFProcessesEnum.SECURITY_GUARD_MISSION.getCode());
				addWFTask(instance.getInstanceId(), cAndControlManagerHQId, cAndControlHQAssigneeId, new Date(), HijriDateService.getHijriSysDate(), WFTaskUrlEnum.SECURITY_GUARD_MISSION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", null, arabicDetailsSummary, englishDetailsSummary, session);

				// Notify Military Police Employees
				if (missionGuard.getOrderNumber() != null) {
					arabicDetailsSummary = getParameterizedMessage("wfMsg_securityGuardMissionTaskMilitaryPoliceNotification", "ar", new Object[] { missionGuard.getDepartmentMissionLocationName(), missionGuard.getOrderNumber(), missionGuard.getOrderDateString() });
					englishDetailsSummary = getParameterizedMessage("wfMsg_securityGuardMissionTaskMilitaryPoliceNotification", "en", new Object[] { missionGuard.getDepartmentMissionLocationName(), missionGuard.getOrderNumber(), missionGuard.getOrderDateString() });
				} else {
					arabicDetailsSummary = getParameterizedMessage("wfMsg_securityGuardMissionTaskOnVerbalApprovalMilitaryPoliceNotification", "ar", new Object[] { missionGuard.getDepartmentMissionLocationName(), missionGuard.getOrderDateString() });
					englishDetailsSummary = getParameterizedMessage("wfMsg_securityGuardMissionTaskOnVerbalApprovalMilitaryPoliceNotification", "en", new Object[] { missionGuard.getDepartmentMissionLocationName(), missionGuard.getOrderDateString() });
				}
				List<Long> militaryPoliceEmployees = WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_MILITARY_POLICE_DUTY_EMPLOYEE.getCode()).getEmpsGroupList();
				for (Long empId : militaryPoliceEmployees) {
					Long militaryPoliceEmployeeId = empId;
					Long militaryPoliceAssigneeId = getDelegate(militaryPoliceEmployeeId, WFProcessesEnum.SECURITY_GUARD_MISSION.getCode());
					addWFTask(instance.getInstanceId(), militaryPoliceAssigneeId, militaryPoliceEmployeeId, new Date(), HijriDateService.getHijriSysDate(), WFTaskUrlEnum.SECURITY_GUARD_MISSION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", null, arabicDetailsSummary, englishDetailsSummary, session);
				}

				// Notify Car management Employees
				if (SecurityGuardMissionService.countAllMissionCars(missionGuard.getId()) > 0) {
					if (missionGuard.getOrderNumber() != null) {
						arabicDetailsSummary = getParameterizedMessage("wfMsg_securityGuardMissionTaskCarManagementEmployeeNotification", "ar", new Object[] { missionGuard.getDepartmentMissionLocationName(), missionGuard.getOrderNumber(), missionGuard.getOrderDateString() });
						englishDetailsSummary = getParameterizedMessage("wfMsg_securityGuardMissionTaskCarManagementEmployeeNotification", "en", new Object[] { missionGuard.getDepartmentMissionLocationName(), missionGuard.getOrderNumber(), missionGuard.getOrderDateString() });
					} else {
						arabicDetailsSummary = getParameterizedMessage("wfMsg_securityGuardMissionTaskOnVerbalOrderCarManagementEmployeeNotification", "ar", new Object[] { missionGuard.getDepartmentMissionLocationName(), missionGuard.getOrderDateString() });
						englishDetailsSummary = getParameterizedMessage("wfMsg_securityGuardMissionTaskOnVerbalOrderCarManagementEmployeeNotification", "en", new Object[] { missionGuard.getDepartmentMissionLocationName(), missionGuard.getOrderDateString() });
					}
					List<Long> carManagmentEmployees = WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_CAR_MANAGEMENT_EMPLOYEE.getCode()).getEmpsGroupList();
					for (Long empId : carManagmentEmployees) {
						Long carManagementEmployeeId = empId;
						Long carManagementAssigneeId = getDelegate(carManagementEmployeeId, WFProcessesEnum.SECURITY_GUARD_MISSION.getCode());
						addWFTask(instance.getInstanceId(), carManagementAssigneeId, carManagementEmployeeId, new Date(), HijriDateService.getHijriSysDate(), WFTaskUrlEnum.SECURITY_GUARD_MISSION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", null, arabicDetailsSummary, englishDetailsSummary, session);
					}
				}

			} else {
				// Notify Command and Control Center
				Long cAndControlManagerId = DepartmentService.getDepartmentManager(WFPositionService.getRegionCommandAndControlDepartmentId(regionId));
				Long cAndControlAssigneeId = getDelegate(cAndControlManagerId, WFProcessesEnum.SECURITY_GUARD_MISSION.getCode());
				addWFTask(instance.getInstanceId(), cAndControlManagerId, cAndControlAssigneeId, new Date(), HijriDateService.getHijriSysDate(), WFTaskUrlEnum.SECURITY_GUARD_MISSION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", null, arabicDetailsSummary, englishDetailsSummary, session);

				// Notify Military Police Employees
				if (missionGuard.getOrderNumber() != null) {
					arabicDetailsSummary = getParameterizedMessage("wfMsg_securityGuardMissionTaskMilitaryPoliceNotification", "ar", new Object[] { missionGuard.getDepartmentMissionLocationName(), missionGuard.getOrderNumber(), missionGuard.getOrderDateString() });
					englishDetailsSummary = getParameterizedMessage("wfMsg_securityGuardMissionTaskMilitaryPoliceNotification", "en", new Object[] { missionGuard.getDepartmentMissionLocationName(), missionGuard.getOrderNumber(), missionGuard.getOrderDateString() });
				} else {
					arabicDetailsSummary = getParameterizedMessage("wfMsg_securityGuardMissionTaskOnVerbalApprovalMilitaryPoliceNotification", "ar", new Object[] { missionGuard.getDepartmentMissionLocationName(), missionGuard.getOrderDateString() });
					englishDetailsSummary = getParameterizedMessage("wfMsg_securityGuardMissionTaskOnVerbalApprovalMilitaryPoliceNotification", "en", new Object[] { missionGuard.getDepartmentMissionLocationName(), missionGuard.getOrderDateString() });
				}
				List<Long> militaryPoliceEmployees = getRegionWorkflowEmployeesMiliatryPolice(regionId);
				for (Long empId : militaryPoliceEmployees) {
					Long militaryPoliceEmployeeId = empId;
					Long militaryPoliceAssigneeId = getDelegate(militaryPoliceEmployeeId, WFProcessesEnum.SECURITY_GUARD_MISSION.getCode());
					addWFTask(instance.getInstanceId(), militaryPoliceAssigneeId, militaryPoliceEmployeeId, new Date(), HijriDateService.getHijriSysDate(), WFTaskUrlEnum.SECURITY_GUARD_MISSION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", null, arabicDetailsSummary, englishDetailsSummary, session);
				}

				// Notify Car management Employees
				if (missionGuard.getOrderNumber() != null) {
					arabicDetailsSummary = getParameterizedMessage("wfMsg_securityGuardMissionTaskCarManagementEmployeeNotification", "ar", new Object[] { missionGuard.getDepartmentMissionLocationName(), missionGuard.getOrderNumber(), missionGuard.getOrderDateString() });
					englishDetailsSummary = getParameterizedMessage("wfMsg_securityGuardMissionTaskCarManagementEmployeeNotification", "en", new Object[] { missionGuard.getDepartmentMissionLocationName(), missionGuard.getOrderNumber(), missionGuard.getOrderDateString() });
				} else {
					arabicDetailsSummary = getParameterizedMessage("wfMsg_securityGuardMissionTaskOnVerbalOrderCarManagementEmployeeNotification", "ar", new Object[] { missionGuard.getDepartmentMissionLocationName(), missionGuard.getOrderDateString() });
					englishDetailsSummary = getParameterizedMessage("wfMsg_securityGuardMissionTaskOnVerbalOrderCarManagementEmployeeNotification", "en", new Object[] { missionGuard.getDepartmentMissionLocationName(), missionGuard.getOrderDateString() });
				}

				List<Long> carManagmentEmployees = getRegionWorkflowEmployeesCarManagement(regionId);
				if (SecurityGuardMissionService.countAllMissionCars(missionGuard.getId()) > 0) {
					for (Long empId : carManagmentEmployees) {
						Long carManagementEmployeeId = empId;
						Long carManagementAssigneeId = getDelegate(carManagementEmployeeId, WFProcessesEnum.SECURITY_GUARD_MISSION.getCode());
						addWFTask(instance.getInstanceId(), carManagementAssigneeId, carManagementEmployeeId, new Date(), HijriDateService.getHijriSysDate(), WFTaskUrlEnum.SECURITY_GUARD_MISSION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", null, arabicDetailsSummary, englishDetailsSummary, session);
					}
				}
			}

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession)
				session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(SecurityGuardMissionWorkFlow.class, e, "SecurityGuardMissionWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}

	}

	/**
	 * Only one action is taked : then open notifications at either in Region or HQ requester
	 * 
	 * @param dmTask
	 * @param missionGuard
	 * @param attachments
	 * @throws BusinessException
	 */
	public static void doRejectMissionGuardDM(WFTask dmTask, MissionGuardData missionGuard, EmployeeData loginEmployee, String attachments, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();

			SecurityGuardMissionService.updateSecurityGuardMission(missionGuard, loginEmployee);
			String arabicDetailsSummary;
			String englishDetailsSummary;
			if (missionGuard.getOrderNumber() != null) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_securityGuardMissionTaskRejectNotification", "ar", new Object[] { missionGuard.getDepartmentMissionLocationName(), missionGuard.getOrderNumber(), missionGuard.getOrderDateString() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_securityGuardMissionTaskRejectNotification", "en", new Object[] { missionGuard.getDepartmentMissionLocationName(), missionGuard.getOrderNumber(), missionGuard.getOrderDateString() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_securityGuardMissionTaskOnVerbalOrderRejectNotification", "ar", new Object[] { missionGuard.getDepartmentMissionLocationName(), missionGuard.getOrderDateString() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_securityGuardMissionTaskOnVerbalOrderRejectNotification", "en", new Object[] { missionGuard.getDepartmentMissionLocationName(), missionGuard.getOrderDateString() });
			}

			WFInstanceData instance = getWFInstanceDataById(dmTask.getInstanceId());

			closeWFInstance(instance.getwFInstance(), dmTask, WFTaskActionsEnum.REJECT.getCode(), new Date(), HijriDateService.getHijriSysDate(), session);

			long originalId = instance.getRequesterId();
			long assigneeId = getDelegate(originalId, WFProcessesEnum.SECURITY_GUARD_MISSION.getCode());
			addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), HijriDateService.getHijriSysDate(), WFTaskUrlEnum.SECURITY_GUARD_MISSION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", null, arabicDetailsSummary, englishDetailsSummary, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession)
				session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(SecurityGuardMissionWorkFlow.class, e, "SecurityGuardMissionWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Notification done
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

	private static Long getRegionWorkflowEmployeesRegionDepartment(long regionId) throws BusinessException {
		try {
			WFPosition pos = WFPositionService.getWFPositionByUnitId(regionId);
			if (pos.getPositionId().equals((long) WFPositionsEnum.EASTERN_REGION_DEPARTMENT.getCode())) {
				return DepartmentService.getDepartmentManager(WFPositionService.getWFPosition(WFPositionsEnum.EASTERN_REGION_MILITARY_POLICE_DEPARTMENT.getCode()).getUnitId());
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ALMADINA_REGION_DEPARTMENT.getCode())) {
				return DepartmentService.getDepartmentManager(WFPositionService.getWFPosition(WFPositionsEnum.ALMADINA_ALMNWARA_REGION_MILITARY_POLICE_DEPARTMENT.getCode()).getUnitId());
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ASEER_REGION_DEPARTMENT.getCode())) {
				return DepartmentService.getDepartmentManager(WFPositionService.getWFPosition(WFPositionsEnum.ASEER_REGION_MILITARY_POLICE_DEPARTMENT.getCode()).getUnitId());
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.GAZAN_REGION_DEPARTMENT.getCode())) {
				return DepartmentService.getDepartmentManager(WFPositionService.getWFPosition(WFPositionsEnum.GAZAN_REGION_MILITARY_POLICE_DEPARTMENT.getCode()).getUnitId());
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.GOUF_REGION_DEPARTMENT.getCode())) {
				return DepartmentService.getDepartmentManager(WFPositionService.getWFPosition(WFPositionsEnum.GOUF_REGION_MILITARY_POLICE_DEPARTMENT.getCode()).getUnitId());
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.MAKA_ELMOKRAMA_REGION_DEPARTMENT.getCode())) {
				return DepartmentService.getDepartmentManager(WFPositionService.getWFPosition(WFPositionsEnum.MAKA_ELMOKRAMA_REGION_MILITARY_POLICE_DEPARTMENT.getCode()).getUnitId());
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.NAGRAN_REGION_DEPARTMENT.getCode())) {
				return DepartmentService.getDepartmentManager(WFPositionService.getWFPosition(WFPositionsEnum.NAGRAN_REGION_MILITARY_POLICE_DEPARTMENT.getCode()).getUnitId());
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.NORTH_BORDER_REGION_DEPARTMENT.getCode())) {
				return DepartmentService.getDepartmentManager(WFPositionService.getWFPosition(WFPositionsEnum.NORTH_BORDER_REGION_MILITARY_POLICE_DEPARTMENT.getCode()).getUnitId());
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.TABOK_REGION_DEPARTMENT.getCode())) {
				return DepartmentService.getDepartmentManager(WFPositionService.getWFPosition(WFPositionsEnum.TABOK_REGION_MILITARY_POLICE_DIPARTMENT.getCode()).getUnitId());
			} else {
				return DepartmentService.getDepartmentManager(WFPositionService.getWFPosition(WFPositionsEnum.ACADEMY_REGION_MILITARY_POLICE_DIPARTMENT.getCode()).getUnitId());
			}
		} catch (Exception e) {
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(SecurityGuardMissionWorkFlow.class, e, "SecurityGuardMissionWorkFlow");
				throw new BusinessException("error_general");
			}
		}
	}

	/**
	 * Get region work flow related employee returned in a list ordered as : index = 0 --> EASTERN_REGION_MILITARY_POLICE_DEPARTMENT index = 1 --> EASTERN_REGION_MILITARY_POLICE_DUTY_EMPLOYEE index = 2 --> EASTERN_REGION_CAR_MANAGEMENT_EMPLOYEE index = 3 --> EASTERN_REGION_SECOND_MILITARY_POLICE_DUTY_EMPLOYEE index = 4 --> EASTERN_REGION_SECOND_CAR_MANAGEMENT_EMPLOYEE
	 * 
	 * @param regionId
	 * @return
	 * @return
	 * @throws BusinessException
	 */
	private static List<Long> getRegionWorkflowEmployeesMiliatryPolice(long regionId) throws BusinessException {
		try {
			WFPosition pos = WFPositionService.getWFPositionByUnitId(regionId);
			if (pos.getPositionId().equals((long) WFPositionsEnum.EASTERN_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.EASTERN_REGION_MILITARY_POLICE_DUTY_EMPLOYEE.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ALMADINA_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.ALMADINA_ALMNWARA_REGION_MILITARY_POLICE_DUTY_EMPLOYEE.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ASEER_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.ASEER_REGION_MILITARY_POLICE_DUTY_EMPLOYEE.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.GAZAN_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.GAZAN_REGION_MILITARY_POLICE_DUTY_EMPLOYEE.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.GOUF_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.GOUF_REGION_MILITARY_POLICE_DUTY_EMPLOYEE.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.MAKA_ELMOKRAMA_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.MAKA_ELMOKRAMA_REGION_MILITARY_POLICE_DUTY_EMPLOYEE.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.NAGRAN_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.NAGRAN_REGION_MILITARY_POLICE_DUTY_EMPLOYEE.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.NORTH_BORDER_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.NORTH_BORDER_REGION_MILITARY_POLICE_DUTY_EMPLOYEE.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.TABOK_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.TABOK_REGION_MILITARY_POLICE_DUTY_EMPLOYEE.getCode()).getEmpsGroupList();
			} else {
				return WFPositionService.getWFPosition(WFPositionsEnum.ACADEMY_REGION_MILITARY_POLICE_DUTY_EMPLOYEE.getCode()).getEmpsGroupList();
			}
		} catch (Exception e) {
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(SecurityGuardMissionWorkFlow.class, e, "SecurityGuardMissionWorkFlow");
				throw new BusinessException("error_general");
			}
		}
	}

	/**
	 * Get region work flow related employee returned in a list ordered as : index = 0 --> EASTERN_REGION_MILITARY_POLICE_DEPARTMENT index = 1 --> EASTERN_REGION_MILITARY_POLICE_DUTY_EMPLOYEE index = 2 --> EASTERN_REGION_CAR_MANAGEMENT_EMPLOYEE index = 3 --> EASTERN_REGION_SECOND_MILITARY_POLICE_DUTY_EMPLOYEE index = 4 --> EASTERN_REGION_SECOND_CAR_MANAGEMENT_EMPLOYEE
	 * 
	 * @param regionId
	 * @return
	 * @return
	 * @throws BusinessException
	 */
	private static List<Long> getRegionWorkflowEmployeesCarManagement(long regionId) throws BusinessException {
		try {
			WFPosition pos = WFPositionService.getWFPositionByUnitId(regionId);
			if (pos.getPositionId().equals((long) WFPositionsEnum.EASTERN_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.EASTERN_REGION_CAR_MANAGEMENT_EMPLOYEE.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ALMADINA_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.ALMADINA_ALMNWARA_REGION_CAR_MANAGEMENT_EMPLOYEE.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ASEER_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.ASEER_REGION_CAR_MANAGEMENT_EMPLOYEE.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.GAZAN_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.GAZAN_REGION_CAR_MANAGEMENT_EMPLOYEE.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.GOUF_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.GOUF_REGION_CAR_MANAGEMENT_EMPLOYEE.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.MAKA_ELMOKRAMA_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.MAKA_ELMOKRAMA_REGION_CAR_MANAGEMENT_EMPLOYEE.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.NAGRAN_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.NAGRAN_REGION_CAR_MANAGEMENT_EMPLOYEE.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.NORTH_BORDER_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.NORTH_BORDER_REGION_CAR_MANAGEMENT_EMPLOYEE.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.TABOK_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.TABOK_REGION_CAR_MANAGEMENT_EMPLOYEE.getCode()).getEmpsGroupList();
			} else {
				return WFPositionService.getWFPosition(WFPositionsEnum.ACADEMY_REGION_CAR_MANAGEMENT_EMPLOYEE.getCode()).getEmpsGroupList();
			}
		} catch (Exception e) {
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(SecurityGuardMissionWorkFlow.class, e, "SecurityGuardMissionWorkFlow");
				throw new BusinessException("error_general");
			}
		}
	}
}