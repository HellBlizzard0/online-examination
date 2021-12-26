package com.code.services.setup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.DataAccess;
import com.code.dal.orm.setup.CountryData;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.log4j.Log4j;

public class CountryService extends BaseService {
	// ----------------- Queries ------------//
	/**
	 * Get all countries
	 * @return list of countries
	 * @throws BusinessException
	 */
	public static List<CountryData> getAllCountries() throws BusinessException {
		try {
			return searchCountry(FlagsEnum.ALL.getCode(), null);
		} catch (NoDataException e) {
			return new ArrayList<CountryData>();
		}
	}
	
	/**
	 * Get country by id
	 * 
	 * @param id
	 * @return CountryData
	 * @throws BusinessException
	 */
	public static CountryData getCountryById(Long id) throws BusinessException {
		try {
			return searchCountry(id, null).get(0);
		} catch (NoDataException e) {
			throw new BusinessException("error_general");
		}
	}

	/**
	 * List All countries by arabic name
	 * @param arabicName
	 * @return list of countries
	 * @throws BusinessException
	 */
	public static List<CountryData> getCountry(String arabicName) throws BusinessException {
		try {
			return searchCountry(FlagsEnum.ALL.getCode(), arabicName);
		} catch (NoDataException e) {
			return new ArrayList<CountryData>();
		}
	}

	/**
	 * Search countries filtered by arabic name
	 * @return list of countries
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<CountryData> searchCountry(long id, String arabicName) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_ARABIC_NAME", arabicName == null || arabicName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + arabicName + "%");
			return DataAccess.executeNamedQuery(CountryData.class, QueryNamesEnum.COUNTRY_DATA_SEARCH_COUNTRY.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(CountryService.class, e, "CountryService");
			throw new BusinessException("error_DBError");
		}
	}
	
	/**
	 * Get info related countries
	 * @param infoId
	 * @return
	 * @throws BusinessException
	 */
	public static List<CountryData> getInfoRelatedCountries(long infoId) throws BusinessException {
		try {
			return searchInfoRelatedCountries(infoId);
		} catch (NoDataException e) {
			return new ArrayList<CountryData>();
		}
	}

	/**
	 * Search info related countries filtered by infoId
	 * @param infoId
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<CountryData> searchInfoRelatedCountries(long infoId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INFO_ID", infoId);
			return DataAccess.executeNamedQuery(CountryData.class, QueryNamesEnum.COUNTRY_DATA_SEARCH_INFO_RELATED_COUNTRIES.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(CountryService.class, e, "CountryService");
			throw new BusinessException("error_DBError");
		}
	}
}