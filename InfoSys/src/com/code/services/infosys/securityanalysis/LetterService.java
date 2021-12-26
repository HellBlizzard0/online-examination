package com.code.services.infosys.securityanalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.securityanalysis.LetterData;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.log4j.Log4j;

public class LetterService extends BaseService {

	private LetterService() {

	}

	/**
	 * Validate mandatory data of letter
	 * 
	 * @param letterData
	 * @throws BusinessException
	 */
	private static void validateLetter(LetterData letterData) throws BusinessException {
		if (letterData.getLetterNumber() == null || letterData.getLetterNumber().trim().isEmpty())
			throw new BusinessException("error_mandatoryFields", new Object[] { getParameterizedMessage("label_letterNumber", "ar") });

		if (letterData.getLetterNumber() != null && !letterData.getLetterNumber().trim().isEmpty() && letterData.getFollowUpId() != null) {
			List<LetterData> oldLetter = searchLetters(letterData.getFollowUpId(), letterData.getLetterNumber());
			if (oldLetter.size() > 1)
				throw new BusinessException("error_changeLetterNumber");
		}

		if (letterData.getOrganization() == null || letterData.getOrganization().trim().isEmpty())
			throw new BusinessException("error_mandatoryFields", new Object[] { getParameterizedMessage("label_unitSource", "ar") });

		if (letterData.getLetterDate() == null)
			throw new BusinessException("error_mandatoryFields", new Object[] { getParameterizedMessage("label_itsDate", "ar") });
		
		if (letterData.getType() == null)
			throw new BusinessException("error_mandatoryFields", new Object[] { getParameterizedMessage("label_letterType", "ar") });

	}

	/**
	 * Validate the letter then Insert it
	 * 
	 * @param letterData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveLetter(LetterData letterData, CustomSession... useSession) throws BusinessException {
		validateLetter(letterData);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			DataAccess.addEntity(letterData.getLetter(), session);
			letterData.setId(letterData.getLetter().getId());

			if (!isOpenedSession) {
				session.commitTransaction();
			}
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			letterData.setId(null);

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(LetterService.class, e, "LetterService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/** -------------------------------------------------------------------- Queries -------------------------------------------------------------------- **/
	/**
	 * Get letters by followUp id except the current one
	 * 
	 * @param followUpId
	 * @return
	 * @throws BusinessException
	 */
	public static List<LetterData> getLettersByFollowUpId(Long followUpId, Long currentLetterId) throws BusinessException {
		List<LetterData> result = searchLetters(followUpId == null ? FlagsEnum.ALL.getCode() : followUpId, null);
		if (currentLetterId != null) {
			result.removeIf(item -> item.getId().equals(currentLetterId));
		}
		return result;
	}

	/**
	 * Get letters by letter number and follow up id
	 * 
	 * @param followUpId
	 * @param letterNumber
	 * @return
	 * @throws BusinessException
	 */
	public static List<LetterData> getLetters(Long followUpId, String letterNumber) throws BusinessException {
		return searchLetters(followUpId == null ? FlagsEnum.ALL.getCode() : followUpId, letterNumber);
	}

	/**
	 * Search letters
	 * 
	 * @param followUpId
	 * @param letterNumber
	 * @return
	 * @throws BusinessException
	 */
	private static List<LetterData> searchLetters(long followUpId, String letterNumber) throws BusinessException {
		try {
			Map<String, Object> qParam = new HashMap<String, Object>();
			qParam.put("P_FOLLOW_UP_ID", followUpId);
			qParam.put("P_LETTER_NUMBER", letterNumber == null || letterNumber.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : letterNumber);
			return DataAccess.executeNamedQuery(LetterData.class, QueryNamesEnum.LETTER_DATA_SEARCH_LETTER_DATA.getCode(), qParam);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(LetterService.class, e, "LetterService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return new ArrayList<LetterData>();
		}
	}
}
