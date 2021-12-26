package com.code.services.fishing;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.DataAccess;
import com.code.dal.ReportService;
import com.code.dal.orm.fishing.FishingBoatOwnerData;
import com.code.dal.orm.fishing.FishingNavigationData;
import com.code.dal.orm.fishing.FishingNavigationDelegationsData;
import com.code.dal.orm.fishing.FishingNavigationFollowerData;
import com.code.dal.orm.fishing.FishingNavigationSailorData;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryNamesEnum;
import com.code.enums.ReportNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.log4j.Log4j;

public class FishingService extends BaseService {

	/**
	 * Get Navigations filtered by navigation number
	 * 
	 * @param number
	 * @return
	 * @throws BusinessException
	 */
	public static FishingNavigationData getNavigationByNumber(String number) throws BusinessException {
		try {
			return searchNavigations(number).get(0);
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * Search Navigations filtered by navigation number
	 * 
	 * @param number
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<FishingNavigationData> searchNavigations(String number) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_NUMBER", number == null || number.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : number);
			return DataAccess.executeNamedQuery(FishingNavigationData.class, QueryNamesEnum.FISHING_NAVIGATION_DATA_SEARCH_FISHING_NAVIGATIONS.getCode(), qParams);
		} catch (DatabaseException e) {
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get Fishing Boat Owner filtered by boat id
	 * 
	 * @param boatId
	 * @return
	 * @throws BusinessException
	 */
	public static List<FishingBoatOwnerData> getFishingBoatOwnersByBoatId(Long boatId) throws BusinessException {
		try {
			return searchFishingBoatOwners(boatId == null ? FlagsEnum.ALL.getCode() : boatId);
		} catch (NoDataException e) {
			return new ArrayList<FishingBoatOwnerData>();
		}
	}

	/**
	 * Search Fishing Boat Owner filtered by boat id
	 * 
	 * @param boatId
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<FishingBoatOwnerData> searchFishingBoatOwners(long boatId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_BOAT_ID", boatId);
			return DataAccess.executeNamedQuery(FishingBoatOwnerData.class, QueryNamesEnum.FISHING_BOAT_OWNER_DATA_SEARCH_FISHING_BOAT_OWNERS.getCode(), qParams);
		} catch (DatabaseException e) {
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get Navigation Sailor filtered by navigation id
	 * 
	 * @param navigationId
	 * @return
	 * @throws BusinessException
	 */
	public static List<FishingNavigationSailorData> getNavigationSailorsByNavigationId(Long navigationId) throws BusinessException {
		try {
			return searchNavigationSailors(navigationId == null ? FlagsEnum.ALL.getCode() : navigationId);
		} catch (NoDataException e) {
			return new ArrayList<FishingNavigationSailorData>();
		}
	}

	/**
	 * Search Navigation Sailor filtered by navigation id
	 * 
	 * @param navigationId
	 * @return
	 * @throws BusinessException
	 */
	private static List<FishingNavigationSailorData> searchNavigationSailors(long navigationId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_NAVIGATION_ID", navigationId);
			return DataAccess.executeNamedQuery(FishingNavigationSailorData.class, QueryNamesEnum.FISHING_NAVIGATION_SAILOR_DATE_SEARCH_FISHING_NAVIGATION_SAILORS.getCode(), qParams);
		} catch (DatabaseException e) {
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get Navigation Followers filtered by navigation id
	 * 
	 * @param navigationId
	 * @return
	 * @throws BusinessException
	 */
	public static List<FishingNavigationFollowerData> getNavigationFollowersByNavigationId(Long navigationId) throws BusinessException {
		try {
			return searchNavigationFollowers(navigationId == null ? FlagsEnum.ALL.getCode() : navigationId);
		} catch (NoDataException e) {
			return new ArrayList<FishingNavigationFollowerData>();
		}
	}

	/**
	 * Search Navigation Followers filtered by navigation id
	 * 
	 * @param navigationId
	 * @return
	 * @throws BusinessException
	 */
	private static List<FishingNavigationFollowerData> searchNavigationFollowers(long navigationId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_NAVIGATION_ID", navigationId);
			return DataAccess.executeNamedQuery(FishingNavigationFollowerData.class, QueryNamesEnum.FISHING_NAVIGATION_FOLLOWER_DATA_SEARCH_FISHING_NAVIGATION_FOLLOWERS.getCode(), qParams);
		} catch (DatabaseException e) {
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * 
	 * @param navigationNumber
	 * @param dateNow
	 * @param fullName
	 * @param hijriDateString
	 * @param timeNow
	 * @param exceededTimestampStr
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getFishingNavigationDetails(String navigationNumber, Date dateNow, String fullName, String hijriDateString, String timeNow, String exceededTimestampStr) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_NAVIGATION_NUMBER", navigationNumber);
			parameters.put("P_CURRENT_DATE", dateNow);
			parameters.put("P_FULL_NAME", fullName);
			parameters.put("P_HIJRI_DATE_STRING", hijriDateString);
			parameters.put("P_TIME_NOW", timeNow);
			parameters.put("P_EXCEEDED_STR", exceededTimestampStr);
			reportName = ReportNamesEnum.FISHING_DETAILS_REPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(FishingService.class, e, "FishingService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * 
	 * @param boatId
	 * @param type
	 * @return
	 * @throws BusinessException
	 */
	public static List<FishingNavigationDelegationsData> getNavigationDelegatesByBoatIdAndDelegateType(Long boatId, String type) throws BusinessException {
		try {
			return searchNavigationDelegatesByBoatIdAndDelegateType(boatId == null ? FlagsEnum.ALL.getCode() : boatId, type);
		} catch (NoDataException e) {
			return new ArrayList<FishingNavigationDelegationsData>();
		}
	}

	/**
	 * 
	 * @param boatId
	 * @param type
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<FishingNavigationDelegationsData> searchNavigationDelegatesByBoatIdAndDelegateType(Long boatId, String type) throws BusinessException, NoDataException {

		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_BOAT_ID", boatId);
			qParams.put("P_DELEGATE_TYPE", type);
			return DataAccess.executeNamedQuery(FishingNavigationDelegationsData.class, QueryNamesEnum.FISHING_NAVIGATION_DELEGATIONS_DATE_SEARCH_FISHING_NAVIGATION_DELEGATES.getCode(), qParams);
		} catch (DatabaseException e) {
			throw new BusinessException("error_DBError");
		}
	}
}
