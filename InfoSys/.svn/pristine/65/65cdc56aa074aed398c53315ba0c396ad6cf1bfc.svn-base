package com.code.services.workflow.surveillance.jobs;

import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.surveillance.SurveillanceEmpNonEmpData;
import com.code.dal.orm.workflow.WFInstance;
import com.code.enums.EmployeeSocialIdTypeEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.SecurityStatusEnum;
import com.code.enums.WFInstanceStatusEnum;
import com.code.enums.WFProcessesEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.enums.WFTaskUrlEnum;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.securityaction.SecurityStatusService;
import com.code.services.infosys.surveillance.SurveillanceOrdersService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.EmployeeService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.BaseWorkFlow;
import com.code.services.workflow.surveillance.SurveillanceWorkFlow;

import yaqeen.ejada.com.IdType;
import yaqeen.ejada.com.PersonInfoResultWithDetailedSecurityStatus;

public class SecurityStatusQuartzJob extends BaseWorkFlow implements Job {

	public void execute(JobExecutionContext context) throws JobExecutionException {

		CustomSession session = DataAccess.getSession();
		try {
			session.beginTransaction();
			EmployeeData admin = EmployeeService.getEmployee(InfoSysConfigurationService.getSystemAdmin());
			List<SurveillanceEmpNonEmpData> surveillanceEmployeeList = SurveillanceOrdersService.getSurveillanceEmployeeData();
			// Send Notifications if employee or non-employee security status is changed
			for (SurveillanceEmpNonEmpData surveillanceEmpNonEmpData : surveillanceEmployeeList) {

				PersonInfoResultWithDetailedSecurityStatus personInfoResult = null;

				String socialId = surveillanceEmpNonEmpData.getSocialId();
				Date birthDate = surveillanceEmpNonEmpData.getBirthDate();
				Date birthDateGreg = surveillanceEmpNonEmpData.getBirthDateGreg();

				Date hijriCurDate = HijriDateService.getHijriSysDate();

				// if Person with national identity
				if (socialId != null && socialId.startsWith(EmployeeSocialIdTypeEnum.NATIONAL_IDENTITY.getCode())) {
					personInfoResult = SecurityStatusService.searchPersonInfoNationalIdentity(birthDate, IdType.C, socialId, admin);

				} // if Person with accommodation identity
				else if (socialId != null && socialId.startsWith(EmployeeSocialIdTypeEnum.ACCOMIDATION.getCode())) {
					personInfoResult = SecurityStatusService.searchPersonInfo(birthDateGreg, IdType.R, socialId, admin);
				}

				Integer personInfoSecurityStatus = (int) (personInfoResult.getSecurityStatus().getValue().equals(FlagsEnum.ON.getCode()) ? SecurityStatusEnum.WANTED.getCode() : (short) FlagsEnum.OFF.getCode());
				// TODO check status later On with new Yaqeen Service
				if (surveillanceEmpNonEmpData.getYaqeenStatus() != null && surveillanceEmpNonEmpData.getYaqeenStatus() != personInfoSecurityStatus) {
					String arabicDetailsSummaryForSurveillance = getParameterizedMessage("quartzMsg_surveillanceSecurityStatusCheckNotify", "ar", new Object[] { surveillanceEmpNonEmpData.getEmployeeFullName(), surveillanceEmpNonEmpData.getSocialId(), surveillanceEmpNonEmpData.getBirthDateString(), hijriCurDate });
					String englishDetailsSummaryForSurveillance = getParameterizedMessage("quartzMsg_surveillanceSecurityStatusCheckNotify", "en", new Object[] { surveillanceEmpNonEmpData.getEmployeeFullName(), surveillanceEmpNonEmpData.getSocialId(), surveillanceEmpNonEmpData.getBirthDateString(), hijriCurDate });

					WFInstance instance = addWFInstance(WFProcessesEnum.NOTIFICATIONS.getCode(), admin.getEmpId(), new Date(), hijriCurDate, WFInstanceStatusEnum.COMPLETED.getCode(), null, null, null, session);
					surveillanceEmpNonEmpData.setYaqeenStatus(personInfoSecurityStatus);

					SurveillanceOrdersService.updateSurveillanceEmployee(admin, surveillanceEmpNonEmpData, session);
					List<Long> notifiersIds = SurveillanceWorkFlow.getSurveillanceNotifers(surveillanceEmpNonEmpData.getOrderRegionId());
					for (Long originalIdForSurveillance : notifiersIds) {
						Long assigneeIdForSurveillance = getDelegate(originalIdForSurveillance, WFProcessesEnum.SURVEILLANCE_ORDER.getCode());
						addWFTask(instance.getInstanceId(), assigneeIdForSurveillance, originalIdForSurveillance, new Date(), hijriCurDate, WFTaskUrlEnum.SURVEILlANCE_ORDER.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", arabicDetailsSummaryForSurveillance, englishDetailsSummaryForSurveillance, session);
					}
				}

			}
			session.commitTransaction();
		} catch (Exception e) {
			session.rollbackTransaction();
			Log4j.traceErrorException(SurveillanceWorkFlow.class, e, "SurveillanceWorkFlow");
		} finally {
			session.close();
		}

	}

}