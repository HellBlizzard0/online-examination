package com.code.services.infosys.securityanalysis;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.ReportService;
import com.code.dal.orm.securityanalysis.ConversationData;
import com.code.dal.orm.securityanalysis.ConversationPartyData;
import com.code.dal.orm.securityanalysis.GuidanceRequestData;
import com.code.dal.orm.securityanalysis.ReferralData;
import com.code.dal.orm.securityanalysis.ReminderData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.enums.ConversationResultsEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryNamesEnum;
import com.code.enums.ReferralTypeValuesEnum;
import com.code.enums.ReportNamesEnum;
import com.code.enums.WFPositionsEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseConstraintException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.util.CommonService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.WFPositionService;
import com.code.services.workflow.securityanalysis.SecurityAnalysisWorkFlow;

public class ConversationService extends BaseService {

	private ConversationService() {
	}

	/***
	 * Validate mandatory data of conversationPartyData
	 * 
	 * @param conversationPartyData
	 * @throws BusinessException
	 */
	private static void validateConversationParty(ConversationPartyData conversationPartyData) throws BusinessException {
		if (conversationPartyData.getContactNumber() == null)
			throw new BusinessException("error_contactNumberMandatory");
		if (conversationPartyData.getName() == null || conversationPartyData.getName().trim().isEmpty())
			throw new BusinessException("error_fullNameMandatory");
	}

	/***
	 * Validate the conversation party then Insert a new conversation party
	 * 
	 * @param conversationPartyData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveConversationParty(ConversationPartyData conversationPartyData, CustomSession... useSession) throws BusinessException {
		validateConversationParty(conversationPartyData);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			DataAccess.addEntity(conversationPartyData.getConversationParty(), session);
			conversationPartyData.setId(conversationPartyData.getConversationParty().getId());

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}
			
			conversationPartyData.setId(null);

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_descriptionViolation");
			} else {
				Log4j.traceErrorException(ConversationService.class, e, "ConversationService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Validate the conversation party then update it
	 * 
	 * @param conversationPartyData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateConversationParty(ConversationPartyData conversationPartyData, CustomSession... useSession) throws BusinessException {
		validateConversationParty(conversationPartyData);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			DataAccess.updateEntity(conversationPartyData.getConversationParty(), session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				conversationPartyData.setId(null);
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_descriptionViolation");
			} else {
				Log4j.traceErrorException(ConversationService.class, e, "ConversationService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/***
	 * Delete the conversation party
	 * 
	 * @param conversationPartyData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteConversationParty(ConversationPartyData conversationPartyData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			DataAccess.deleteEntity(conversationPartyData.getConversationParty(), session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_descriptionViolation");
			} else {
				Log4j.traceErrorException(ConversationService.class, e, "ConversationService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/***
	 * Validate mandatory data of referral
	 * 
	 * @param referralData
	 * @throws BusinessException
	 */
	public static void validateReferral(ReferralData referralData) throws BusinessException {
		if (referralData.getDetails() == null)
			throw new BusinessException("error_referralDetailsMandatory");
	}

	/***
	 * Validate the referral then Insert a new referral
	 * 
	 * @param referralData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveReferral(ReferralData referralData, CustomSession... useSession) throws BusinessException {
		validateReferral(referralData);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			DataAccess.addEntity(referralData.getReferral(), session);
			referralData.setId(referralData.getReferral().getId());

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}
			
			referralData.setId(null);

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_descriptionViolation");
			} else {
				Log4j.traceErrorException(ConversationService.class, e, "ConversationService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/***
	 * Validate the referral then update it
	 * 
	 * @param referralData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateReferral(ReferralData referralData, CustomSession... useSession) throws BusinessException {
		validateReferral(referralData);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			DataAccess.updateEntity(referralData.getReferral(), session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_descriptionViolation");
			} else {
				Log4j.traceErrorException(ConversationService.class, e, "ConversationService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/***
	 * Delete the referral
	 * 
	 * @param referralData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteReferral(ReferralData referralData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			DataAccess.deleteEntity(referralData.getReferral(), session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_descriptionViolation");
			} else {
				Log4j.traceErrorException(ConversationService.class, e, "ConversationService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/***
	 * Validate mandatory data of conversationData
	 * 
	 * @param conversationData
	 * @throws BusinessException
	 */
	public static void validateConversation(ConversationData conversationData) throws BusinessException {
		if (conversationData.getConversationDate() == null)
			throw new BusinessException("error_conversationDateMandatory");
		if (conversationData.getConversationTime() == null || conversationData.getConversationTime().trim().isEmpty())
			throw new BusinessException("error_conversationTimeMandatory");
		if (conversationData.getConversationLocation() == null || conversationData.getConversationLocation().trim().isEmpty())
			throw new BusinessException("error_conversationLocationMandatory");
		// check Saudi Arabia boundaries
		if (conversationData.getCoordinateLatitude() != null && conversationData.getCoordinateLongitude() != null) {
			if (CommonService.isLocationOutOfBounds(conversationData.getCoordinateLatitude(), conversationData.getCoordinateLongitude())) {
				throw new BusinessException("error_coordinatesOutOfBounds");
			}
		}
		if (conversationData.getDomainIdChatAction() != null) {
			if (conversationData.getActionTaken() == null || conversationData.getActionTaken().trim().isEmpty())
				throw new BusinessException("error_actionTakenMandatory");
		}
		List<ConversationData> convesartionDataExists = getConversationsByConversationDateAndTime(conversationData.getFollowUpId(), conversationData.getConversationDate(), conversationData.getConversationTime());
		if (conversationData.getId() == null) {
			if (!convesartionDataExists.isEmpty() && convesartionDataExists.size() >= 1) {
				throw new BusinessException("error_conversationDateAndTimeBefore");
			}
		} else {
			// in update case
			if (!convesartionDataExists.isEmpty() && convesartionDataExists.size() >= 1 && !(conversationData.getId().equals(convesartionDataExists.get(0).getId()))) {
				throw new BusinessException("error_conversationDateAndTimeBefore");
			}
		}
	}

	/***
	 * Validate mandatory data of reminderData
	 * 
	 * @param reminderData
	 * @throws BusinessException
	 */
	public static void validateReminder(ReminderData reminderData) throws BusinessException {
		if (reminderData.getReminderDate() == null)
			throw new BusinessException("error_reminderDateMandatory");
		if (reminderData.getReminderTime() == null)
			throw new BusinessException("error_reminderTimeMandatory");
		if (reminderData.getReminderDetails() == null)
			throw new BusinessException("error_reminderDetailMandatory");
	}

	/***
	 * Validate the reminder then Insert it
	 * 
	 * @param reminderData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveReminder(ReminderData reminderData, CustomSession... useSession) throws BusinessException {
		validateReminder(reminderData);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			DataAccess.addEntity(reminderData.getReminder(), session);
			reminderData.setId(reminderData.getReminder().getId());

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}
			
			reminderData.setId(null);

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_descriptionViolation");
			} else {
				Log4j.traceErrorException(ConversationService.class, e, "ConversationService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Validate the reminder then Update it
	 * 
	 * @param reminderData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateReminder(ReminderData reminderData, CustomSession... useSession) throws BusinessException {
		validateReminder(reminderData);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			DataAccess.updateEntity(reminderData.getReminder(), session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_descriptionViolation");
			} else {
				Log4j.traceErrorException(ConversationService.class, e, "ConversationService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Save Conversation Data
	 * 
	 * @param conversationData
	 * @param loginEmp
	 * @param conversationPartyDataList
	 * @param referralDataList
	 * @param reminderDataList
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveConversation(ConversationData conversationData, EmployeeData loginEmp, List<ConversationPartyData> conversationPartyDataList, List<ReferralData> referralDataList, List<ReminderData> reminderDataList, CustomSession... useSession) throws BusinessException {
		validateConversation(conversationData);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			DataAccess.addEntity(conversationData.getConversation(), session);
			conversationData.setId(conversationData.getConversation().getId());

			if (conversationData.getConversationResult() != null && conversationData.getConversationResult().equals(ConversationResultsEnum.POSITIVE.getCode())) {
				long notifierId = DepartmentService.getDepartmentManager(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_SECURITY_ANALYSIS_DEPARTMENT.getCode()).getUnitId());
				String arabicDetailsSummary = getParameterizedMessage("wfMsg_positiveConversationaddedNotifier", "ar", new Object[] { conversationData.getFollowUpCode() });
				String englishDetailsSummary = getParameterizedMessage("wfMsg_positiveConversationaddedNotifier", "en", new Object[] { conversationData.getFollowUpCode() });
				SecurityAnalysisWorkFlow.notify(loginEmp.getEmpId(), arabicDetailsSummary, arabicDetailsSummary, englishDetailsSummary, notifierId, session);
			}

			for (ConversationPartyData conversationPartyData : conversationPartyDataList) {
				conversationPartyData.setConversationId(conversationData.getId());
				conversationPartyData.setRegistraionDate(HijriDateService.getHijriSysDate());
				saveConversationParty(conversationPartyData, session);
			}

			for (ReferralData ref : referralDataList) {
				ref.setConversationId(conversationData.getId());
				saveReferral(ref, session);
				if (ref.getDomainIdReferralTypeDesc() != null && ref.getDomainIdReferralTypeDesc().equals(ReferralTypeValuesEnum.REDIRECT.getCode())) {
					SecurityAnalysisWorkFlow.initGuidanceRequest(conversationData, ref, loginEmp, session);
				} else {
					String arabicDetailsSummary = getParameterizedMessage("wfMsg_guidenceRequestNotifier", "ar", new Object[] { conversationData.getFollowUpCode(), ref.getDetails() });
					String englishDetailsSummary = getParameterizedMessage("wfMsg_guidenceRequestNotifier", "en", new Object[] { conversationData.getFollowUpCode(), ref.getDetails() });
					SecurityAnalysisWorkFlow.notify(loginEmp.getEmpId(), arabicDetailsSummary, arabicDetailsSummary, englishDetailsSummary, ref.getEmployeeNumber(), session);
				}
			}

			for (ReminderData reminder : reminderDataList) {
				reminder.setConversationId(conversationData.getId());
				reminder.setEmpId(loginEmp.getEmpId());
				reminder.setDoneFlag(false);
				saveReminder(reminder, session);

				String arabicDetailsSummary = getParameterizedMessage("wfMsg_reminderConversationaddedNotifier", "ar", new Object[] { conversationData.getFollowUpCode(), reminder.getReminderDateString(), reminder.getReminderTime(), reminder.getReminderDetails() });
				String englishDetailsSummary = getParameterizedMessage("wfMsg_reminderConversationaddedNotifier", "en", new Object[] { conversationData.getFollowUpCode(), reminder.getReminderDateString(), reminder.getReminderTime(), reminder.getReminderDetails() });
				SecurityAnalysisWorkFlow.notify(loginEmp.getEmpId(), arabicDetailsSummary, arabicDetailsSummary, englishDetailsSummary, loginEmp.getEmpId(), session);
			}

			if (!isOpenedSession) {
				session.commitTransaction();
			}

		} catch (Exception e) {
			conversationData.setId(null);
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}
			
			conversationData.setId(null);

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(ConversationService.class, e, "ConversationService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * 
	 * @param conversationData
	 * @param loginEmp
	 * @param conversationPartyDataList
	 * @param referralDataList
	 * @param reminderDataList
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateConversation(ConversationData conversationData, EmployeeData loginEmp, List<ConversationPartyData> conversationPartyDataList, List<ConversationPartyData> deletedConversationPartyDataList, List<ReferralData> referralDataList, List<ReminderData> reminderDataList, CustomSession... useSession) throws BusinessException {
		validateConversation(conversationData);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}
			DataAccess.updateEntity(conversationData.getConversation(), session);

			if (conversationData.getConversationResult() != null && conversationData.getConversationResult().equals(ConversationResultsEnum.POSITIVE.getCode())) {
				long notifierlId = DepartmentService.getDepartmentManager(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_SECURITY_ANALYSIS_DEPARTMENT.getCode()).getUnitId());
				String arabicDetailsSummary = getParameterizedMessage("wfMsg_positiveConversationaddedNotifier", "ar", new Object[] { conversationData.getFollowUpCode() });
				String englishDetailsSummary = getParameterizedMessage("wfMsg_positiveConversationaddedNotifier", "en", new Object[] { conversationData.getFollowUpCode() });
				SecurityAnalysisWorkFlow.notify(loginEmp.getEmpId(), arabicDetailsSummary, arabicDetailsSummary, englishDetailsSummary, notifierlId, session);
			}

			if (deletedConversationPartyDataList != null) {
				for (ConversationPartyData conversationPartyData : deletedConversationPartyDataList) {
					deleteConversationParty(conversationPartyData, session);
				}
			}
			for (ConversationPartyData conversationPartyData : conversationPartyDataList) {
				if (conversationPartyData.getId() == null) {
					conversationPartyData.setConversationId(conversationData.getId());
					conversationPartyData.setRegistraionDate(HijriDateService.getHijriSysDate());
					saveConversationParty(conversationPartyData, session);
				} else {
					updateConversationParty(conversationPartyData, session);
				}

			}

			for (ReferralData ref : referralDataList) {
				if (ref.getId() == null) {
					ref.setConversationId(conversationData.getId());
					saveReferral(ref, session);
					if (ref.getDomainIdReferralTypeDesc() != null && ref.getDomainIdReferralTypeDesc().equals(ReferralTypeValuesEnum.REDIRECT.getCode())) {
						SecurityAnalysisWorkFlow.initGuidanceRequest(conversationData, ref, loginEmp, session);
					} else {
						String arabicDetailsSummary = getParameterizedMessage("wfMsg_guidenceRequestNotifier", "ar", new Object[] { conversationData.getFollowUpCode(), ref.getDetails() });
						String englishDetailsSummary = getParameterizedMessage("wfMsg_guidenceRequestNotifier", "en", new Object[] { conversationData.getFollowUpCode(), ref.getDetails() });
						SecurityAnalysisWorkFlow.notify(loginEmp.getEmpId(), arabicDetailsSummary, arabicDetailsSummary, englishDetailsSummary, ref.getEmployeeNumber(), session);
					}
				} else {
					updateReferral(ref, session);
				}

			}

			for (ReminderData reminder : reminderDataList) {
				if (reminder.getId() == null) {
					reminder.setEmpId(loginEmp.getEmpId());
					reminder.setDoneFlag(false);
					reminder.setConversationId(conversationData.getId());
					saveReminder(reminder, session);
					// Send Notification
					String arabicDetailsSummary = getParameterizedMessage("wfMsg_reminderConversationaddedNotifier", "ar", new Object[] { conversationData.getFollowUpCode(), reminder.getReminderDateString(), reminder.getReminderTime(), reminder.getReminderDetails() });
					String englishDetailsSummary = getParameterizedMessage("wfMsg_reminderConversationaddedNotifier", "en", new Object[] { conversationData.getFollowUpCode(), reminder.getReminderDateString(), reminder.getReminderTime(), reminder.getReminderDetails() });
					SecurityAnalysisWorkFlow.notify(loginEmp.getEmpId(), arabicDetailsSummary, arabicDetailsSummary, englishDetailsSummary, loginEmp.getEmpId(), session);

				} else {
					updateReminder(reminder, session);
				}
			}

			if (!isOpenedSession) {
				session.commitTransaction();
			}

		} catch (Exception e) {
			conversationData.setId(null);
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(ConversationService.class, e, "ConversationService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/***
	 * Validate mandatory data of GuidanceRequest
	 * 
	 * @param guidanceRequest
	 * @throws BusinessException
	 */
	public static void validateGuidanceRequest(GuidanceRequestData guidanceRequest) throws BusinessException {
		if (guidanceRequest.getDomainIdGuidanceTypes() == null)
			throw new BusinessException("error_mandatoryFields", new Object[] { getParameterizedMessage("label_guidanceType", "ar") });
		if (guidanceRequest.getGuidanceDetails() == null || guidanceRequest.getGuidanceDetails().trim().isEmpty())
			throw new BusinessException("error_mandatoryFields", new Object[] { getParameterizedMessage("label_guidanceDetails", "ar") });
	}

	/***
	 * first validate the guidanceRequest then Insert a new guidanceRequest
	 * 
	 * @param guidanceRequest
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveGuidanceRequest(GuidanceRequestData guidanceRequest, CustomSession... useSession) throws BusinessException {
		validateGuidanceRequest(guidanceRequest);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			DataAccess.addEntity(guidanceRequest.getGuidanceRequest(), session);
			guidanceRequest.setId(guidanceRequest.getGuidanceRequest().getId());

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}
			
			guidanceRequest.setId(null);
			
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_descriptionViolation");
			} else {
				Log4j.traceErrorException(ConversationService.class, e, "ConversationService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Get Conversation During Period Report
	 * 
	 * @param regionId
	 * @param followUpId
	 * @param networkId
	 * @param ghostName
	 * @param fromDateString
	 * @param toDateString
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getConversationDuringPeriodReportBytes(Long followUpId, String followUpCode, Integer conversationResult, String fromDateString, String toDateString, Long chatAction, String loginEmployeeName) throws BusinessException {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_FOLLOW_UP_ID", followUpId == null ? FlagsEnum.ALL.getCode() : followUpId);
			parameters.put("P_FOLLOW_UP_CODE", followUpCode == null || followUpCode.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + followUpCode + "%");
			parameters.put("P_CONVERSATION_RESULT", conversationResult == null ? FlagsEnum.ALL.getCode() : conversationResult);
			parameters.put("P_FROM_DATE", fromDateString);
			parameters.put("P_TO_DATE", toDateString);
			parameters.put("P_CHAT_ACTION", chatAction == null ? FlagsEnum.ALL.getCode() : chatAction);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			String reportName = ReportNamesEnum.CONVERSATIONS_DURING_PERIOD_REPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(ConversationService.class, e, "ConversationService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Referral Conversation Report
	 * 
	 * @param followUpId
	 * @param regionList
	 * @param employeeId
	 * @param referralType
	 * @param fromDateString
	 * @param toDateString
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getReferralConversationReportBytes(Long followUpId, List<Long> regionList, Long employeeId, Long referralType, String fromDateString, String toDateString, String loginEmployeeName) throws BusinessException {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_FOLLOW_UP_ID", followUpId == null ? FlagsEnum.ALL.getCode() : followUpId);
			parameters.put("P_REGION_LIST", regionList);
			parameters.put("P_EMPLOYEE_ID", employeeId == null ? FlagsEnum.ALL.getCode() : employeeId);
			parameters.put("P_REFERRAL_TYPE", referralType == null ? FlagsEnum.ALL.getCode() : referralType);
			parameters.put("P_FROM_DATE", fromDateString);
			parameters.put("P_TO_DATE", toDateString);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			String reportName = ReportNamesEnum.CONVERSATION_REFERRALS_REPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(ConversationService.class, e, "ConversationService");
			throw new BusinessException("error_general");
		}
	}

	
	/**
	 * 
	 * @param followUpId
	 * @param regionList
	 * @param employeeId
	 * @param referralType
	 * @param fromDateString
	 * @param toDateString
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getConversationBasedOnCodeWithinPeriodReportBytes(String employeeName, String followUpCode, String fromDateString, String toDateString, String contactNumber, String aliasName, String name, String socialId) throws BusinessException {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_LOGIN_EMPLOYEE_NAME", employeeName);
			parameters.put("P_FOLLOW_UP_CODE",  followUpCode);
			parameters.put("P_FROM_DATE", fromDateString);
			parameters.put("P_TO_DATE", toDateString);
			parameters.put("P_CONTACT_NUMBER", contactNumber == null ? "" : contactNumber);
			parameters.put("P_ALISA_NAME", aliasName == null ? "" : aliasName);
			parameters.put("P_NAME", name == null ? "" : name);
			parameters.put("P_SOCIAL_ID", socialId == null ? "" : socialId);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			String reportName = ReportNamesEnum.CONVERSATIONS_WITHIN_PERIOD_BASED_ON_CODE_REPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(ConversationService.class, e, "ConversationService");
			throw new BusinessException("error_general");
		}
	}
	
	/** -------------------------------------------------------------------- Queries -------------------------------------------------------------------- **/
	/**
	 * Get conversations by conversation id
	 * 
	 * @param conversationId
	 * @return
	 * @throws BusinessException
	 */
	public static ConversationData getConversationsByConversationId(Long conversationId) throws BusinessException {
		return searchConversations(FlagsEnum.ALL.getCode(), conversationId == null ? FlagsEnum.ALL.getCode() : conversationId, FlagsEnum.ALL.getCode(), null, null, FlagsEnum.ALL.getCode()).get(0);
	}

	/**
	 * get Conversations By ConversationDate And Time
	 * 
	 * @param conversationDate
	 * @param conversationTime
	 * @return
	 * @throws BusinessException
	 */
	public static List<ConversationData> getConversationsByConversationDateAndTime(Long followUpId, Date conversationDate, String conversationTime) throws BusinessException {
		return searchConversations(followUpId == null ? FlagsEnum.ALL.getCode() : followUpId, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), conversationDate, conversationTime, FlagsEnum.ALL.getCode());
	}

	/**
	 * Get conversations by followUp id
	 * 
	 * @param followUpId
	 * @return
	 * @throws BusinessException
	 */
	public static List<ConversationData> getConversationsByFollowUpId(Long followUpId) throws BusinessException {
		return searchConversations(followUpId == null ? FlagsEnum.ALL.getCode() : followUpId, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, null, FlagsEnum.ALL.getCode());
	}

	/**
	 * Get conversations by conversation result
	 * 
	 * @param conversationResult
	 * @return
	 * @throws BusinessException
	 */
	public static List<ConversationData> getConversationsByConversationResult(Long followUpId, Integer conversationResult) throws BusinessException {
		return searchConversations(followUpId == null ? FlagsEnum.ALL.getCode() : followUpId, FlagsEnum.ALL.getCode(), conversationResult == null ? FlagsEnum.ALL.getCode() : conversationResult, null, null, FlagsEnum.ALL.getCode());
	}

	/**
	 * Get conversation by instance id
	 * 
	 * @param instanceId
	 * @return
	 * @throws BusinessException
	 */
	public static ConversationData getConversationsByInstanceId(Long instanceId) throws BusinessException {
		return searchConversations(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, null, instanceId == null ? FlagsEnum.ALL.getCode() : instanceId).get(0);
	}

	/**
	 * Get list of ConversationData
	 * 
	 * @param followUpId
	 * @param conversationId
	 * @param conversationResult
	 * @param conversationDate
	 * @param time
	 * @param instanceId
	 * @return
	 * @throws BusinessException
	 */
	private static List<ConversationData> searchConversations(long followUpId, long conversationId, int conversationResult, Date conversationDate, String time, long instanceId) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_FOLLOW_UP_ID", followUpId);
			qParams.put("P_CONVERSATION_ID", conversationId);
			qParams.put("P_CONVERSATION_RESULT", conversationResult);
			qParams.put("P_CONVERSATION_DATE_NULL", conversationDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_CONVERSATION_DATE", conversationDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(conversationDate));
			qParams.put("P_TIME", time == null || time.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : time);
			qParams.put("P_INSTANCE_ID", instanceId);
			return DataAccess.executeNamedQuery(ConversationData.class, QueryNamesEnum.CONVERSATION_DATA_SEARCH_CONVERSATIONS.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(ConversationService.class, e, "ConversationService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return new ArrayList<ConversationData>();
		}
	}

	/**
	 * Get list of Conversation Data List
	 * 
	 * @param followUpId
	 * @param conversationType
	 * @param conversationResult
	 * @param coordinateLatitude
	 * @param coordinateLongitude
	 * @param fromDate
	 * @param toDate
	 * @param conversationPartyContactNum
	 * @return
	 * @throws BusinessException
	 */
	public static List<ConversationData> getConversationDataList(Long followUpId, Integer conversationType, Integer conversationResult, Double coordinateLatitude, Double coordinateLongitude, Date fromDate, Date toDate, Long conversationPartyContactNum) throws BusinessException {
		try {
			return searchConversationDataList(followUpId == null ? FlagsEnum.ALL.getCode() : followUpId, conversationType == null ? FlagsEnum.ALL.getCode() : conversationType, conversationResult == null ? FlagsEnum.ALL.getCode() : conversationResult, coordinateLatitude == null ? FlagsEnum.ALL.getCode() : coordinateLatitude, coordinateLongitude == null ? FlagsEnum.ALL.getCode() : coordinateLongitude, fromDate, toDate,
					conversationPartyContactNum == null ? FlagsEnum.ALL.getCode() : conversationPartyContactNum);
		} catch (NoDataException e) {
			return new ArrayList<ConversationData>();
		}
	}

	/**
	 * Search Conversation Data List
	 * 
	 * @param followUpId
	 * @param conversationType
	 * @param conversationResult
	 * @param coordinateLatitude
	 * @param coordinateLongitude
	 * @param fromDate
	 * @param toDate
	 * @param conversationPartyContactNum
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<ConversationData> searchConversationDataList(long followUpId, int conversationType, int conversationResult, double coordinateLatitude, double coordinateLongitude, Date fromDate, Date toDate, long conversationPartyContactNum) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_FOLLOW_UP_ID", followUpId);
			qParams.put("P_CONVERSATION_TYPE", conversationType);
			qParams.put("P_CONVERSATION_RESULT", conversationResult);
			qParams.put("P_CONVERSATION_LATITUDE", coordinateLatitude);
			qParams.put("P_CONVERSATION_LONGITUDE", coordinateLongitude);
			qParams.put("P_CONTACT_NUMBER", conversationPartyContactNum);
			qParams.put("P_FROM_DATE_NULL", fromDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_FROM_DATE", fromDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(fromDate));
			qParams.put("P_TO_DATE_NULL", fromDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_TO_DATE", toDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(toDate));
			return DataAccess.executeNamedQuery(ConversationData.class, QueryNamesEnum.CONVERSATION_DATA_SEARCH_CONVERSATION_DATA_LIST.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(ConversationService.class, e, "ConversationService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * 
	 * @param contactNumber
	 * @param followUpCode
	 * @return
	 */
	public static List<ConversationPartyData> getConversationParties(String contactNumber, String followUpCode) {
		try {
			return searchConversationParties(contactNumber, followUpCode, FlagsEnum.ALL.getCode());
		} catch (BusinessException e) {
			return new ArrayList<ConversationPartyData>();
		}
	}

	/**
	 * Get Conversations Party by conversation Id
	 * 
	 * @param contactNumber
	 * @param followUpCode
	 * @return
	 */
	public static List<ConversationPartyData> getConversationsPartiesByConversationId(Long conversationId) {
		try {
			return searchConversationParties(null, null, conversationId == null ? FlagsEnum.ALL.getCode() : conversationId);
		} catch (BusinessException e) {
			return new ArrayList<ConversationPartyData>();
		}
	}

	/**
	 * Get Conversations Party by follow up code
	 * 
	 * @param followUpCode
	 * @return
	 */
	public static List<ConversationPartyData> getConversationsPartiesByFollowUpCode(String followUpCode) {
		try {
			return searchConversationParties(null, followUpCode, FlagsEnum.ALL.getCode());
		} catch (BusinessException e) {
			return new ArrayList<ConversationPartyData>();
		}
	}

	/**
	 * Get Conversation Parties
	 * 
	 * @param contactNumber
	 * @param followUpCode
	 * @param conversationId
	 * @return
	 * @throws BusinessException
	 */
	private static List<ConversationPartyData> searchConversationParties(String contactNumber, String followUpCode, long conversationId) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_CONTACT_NUMBER", contactNumber  == null || contactNumber.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : contactNumber);
			qParams.put("P_FOLLOW_CODE", followUpCode == null || followUpCode.isEmpty() ? FlagsEnum.ALL.getCode() + "" : followUpCode);
			qParams.put("P_CONVERSATION_ID", conversationId);
			return DataAccess.executeNamedQuery(ConversationPartyData.class, QueryNamesEnum.CONVERSATION_PARTY_DATA_SEARCH_CONVERSATION_PARTY_DATA.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(ConversationService.class, e, "ConversationService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return new ArrayList<ConversationPartyData>();
		}
	}

	/**
	 * Get Reminders by conversationId
	 * 
	 * @param conversationId
	 * @return
	 * @throws BusinessException
	 */
	public static List<ReminderData> getRemindersByConversationId(Long conversationId) throws BusinessException {
		try {
			return searchReminders(conversationId == null ? FlagsEnum.ALL.getCode() : conversationId, null);
		} catch (NoDataException e) {
			return new ArrayList<ReminderData>();
		}
	}

	/**
	 * Get Reminders by conversationId
	 * 
	 * @param conversationId
	 * @return
	 * @throws BusinessException
	 */
	public static List<ReminderData> getAllRemindersNotDone() throws BusinessException {
		try {
			return searchReminders(FlagsEnum.ALL.getCode(), false);
		} catch (NoDataException e) {
			return new ArrayList<ReminderData>();
		}
	}

	/**
	 * Get List of Reminders
	 * 
	 * @param conversationId
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<ReminderData> searchReminders(long conversationId, Boolean doneFlag) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_CONVERSATION_ID", conversationId);
			qParams.put("P_DONE_FLAG", doneFlag == null ? FlagsEnum.ALL.getCode() : doneFlag ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			return DataAccess.executeNamedQuery(ReminderData.class, QueryNamesEnum.REMINDER_DATA_SEARCH_REMINDERS.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(ConversationService.class, e, "ConversationService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get Referrals by conversationId
	 * 
	 * @param conversationId
	 * @return
	 * @throws BusinessException
	 */
	public static List<ReferralData> getReferralsByConversationId(Long conversationId) throws BusinessException {
		try {
			return searchReferrals(conversationId == null ? FlagsEnum.ALL.getCode() : conversationId);
		} catch (NoDataException e) {
			return new ArrayList<ReferralData>();
		}
	}

	/**
	 * Get List of Referral
	 * 
	 * @param conversationId
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<ReferralData> searchReferrals(long conversationId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_CONVERSATION_ID", conversationId);
			return DataAccess.executeNamedQuery(ReferralData.class, QueryNamesEnum.REFERRAL_DATA_SEARCH_REFERRALS.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(ConversationService.class, e, "ConversationService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get Guidance Requests by conversationId
	 * 
	 * @param conversationId
	 * @return
	 * @throws BusinessException
	 */
	public static GuidanceRequestData getGuidanceRequestByConversationId(Long conversationId) throws BusinessException {
		try {
			return searchGuidanceRequest(conversationId == null ? FlagsEnum.ALL.getCode() : conversationId);
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * Search for GuidanceRequest
	 * 
	 * @param conversationId
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static GuidanceRequestData searchGuidanceRequest(long conversationId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_CONVERSATION_ID", conversationId);
			return DataAccess.executeNamedQuery(GuidanceRequestData.class, QueryNamesEnum.GUIDANCE_REQUEST_DATA_SEARCH_GUIDANCE_REQUEST.getCode(), qParams).get(0);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(ConversationService.class, e, "ConversationService");
			throw new BusinessException("error_DBError");
		}
	}
}
