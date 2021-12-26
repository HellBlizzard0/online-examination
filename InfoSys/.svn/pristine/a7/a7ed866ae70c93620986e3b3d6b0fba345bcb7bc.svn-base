package com.code.integration.employeecasesdata;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.code.dal.dto.empcase.EmployeeCaseData;
import com.code.enums.CasesIntegrationStatusEnum;
import com.code.exceptions.BusinessException;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.log4j.Log4j;
import com.google.gson.Gson;

public class EmployeeCasesWSRespones {
	private static final String EMPLOYEE_RELATED_CASE_DATA_WEB_SERVICE_URL = InfoSysConfigurationService.getCasesRestWebServiceURL();
	private static final String EMPLOYEE_RELATED_CASE_DATA_REQUEST_METHOD = "POST";
	private static final String EMPLOYEE_RELATED_CASE_DATA_CONTENT_TYPE = "application/json";

	/**
	 * 
	 * @param socialId
	 * @throws BusinessException
	 */
	public static List<EmployeeCaseData> getEmployeeRelatedCaseDataBySoicalId(String socialId) throws BusinessException {
		try {
			URL url = new URL(EMPLOYEE_RELATED_CASE_DATA_WEB_SERVICE_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod(EMPLOYEE_RELATED_CASE_DATA_REQUEST_METHOD);
			conn.setRequestProperty("Content-Type", EMPLOYEE_RELATED_CASE_DATA_CONTENT_TYPE);
			OutputStream os = conn.getOutputStream();
			os.write(socialId.getBytes());
			os.flush();
			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED && conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
			bufferedReader.close();
			conn.disconnect();
			JSONObject result = new JSONObject(sb.toString().replaceAll(":null", ":\"\""));
			int status = Integer.parseInt(result.get("Status").toString());
			if (status == CasesIntegrationStatusEnum.CASES_INTERNAL_ERROR.getCode()) {
				throw new BusinessException("error_integrationWithCasesInternalCasesError");
			} else if (status == CasesIntegrationStatusEnum.SOCIAL_ID_NOT_FOUND.getCode()) {
				throw new BusinessException("error_integrationWithCasesSocialIdNotFound");
			}
			JSONArray cases = result.getJSONArray("CASES");
			return setEmployeeCaseDataList(cases);
		} catch (Exception e) {
			Log4j.traceErrorException(EmployeeCasesWSRespones.class, e, "EmployeeCasesWSRespones");
			throw new BusinessException("error_integrationWithCasesSystemError");
		}
	}

	/**
	 * 
	 * @param response
	 * @return
	 */
	private static List<EmployeeCaseData> setEmployeeCaseDataList(JSONArray cases) throws BusinessException {
		Gson gson = new Gson();
		List<EmployeeCaseData> employeeCaseDataList = new ArrayList<EmployeeCaseData>();
		for (int i = 0; i < cases.length(); i++) {
			EmployeeCaseData employeeCaseData = new EmployeeCaseData();
			employeeCaseData = gson.fromJson(cases.getJSONObject(i).toString(), EmployeeCaseData.class);
			employeeCaseDataList.add(employeeCaseData);
		}
		return employeeCaseDataList;
	}
}
