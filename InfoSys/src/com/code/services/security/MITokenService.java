package com.code.services.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONObject;

import com.code.exceptions.BusinessException;
import com.code.services.BaseService;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.log4j.Log4j;

public class MITokenService extends BaseService {
	private MITokenService() {
	}

	public static void verify(String username, String password, String validate) throws BusinessException {
		try {
			String urlParameters = "username=" + username + "&password=" + password + "&validate=" + validate + "&usertype=domain";

			String resp = executeHttpGet(InfoSysConfigurationService.getMITokenPath() + urlParameters);

			JSONObject jsonResp = new JSONObject(resp);
			String result = jsonResp.getString("result");
			if (!result.equals("success")) {
				throw new BusinessException("error_invalidToken");
			}
		} catch (Exception e) {
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(MITokenService.class, e, "MITokenService");
				throw new BusinessException("error_mitokenWebserviceFail");
			}
		}
	}

	private static String executeHttpGet(String targetURL) throws IOException {
		URL url = new URL(targetURL);
		HttpsURLConnection conn = null;

		try {
			conn = (HttpsURLConnection) url.openConnection();

			conn.setHostnameVerifier(new TrustAllHostnameVerifier());
			conn.setSSLSocketFactory(getNoCheckSSLSocketFactory());

			InputStream is = conn.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			StringBuilder response = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append("\r\n");
			}
			rd.close();

			return response.toString();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	private static SSLSocketFactory getNoCheckSSLSocketFactory() {
		SSLSocketFactory factory = null;

		try {
			SSLContext ctx = SSLContext.getInstance("TLSv1.2");
			ctx.init(new KeyManager[0], new TrustManager[] { new NoCheckTrustManager() }, new SecureRandom());
			factory = ctx.getSocketFactory();
		} catch (Exception e) {
			factory = null;
			Log4j.traceErrorException(MITokenService.class, e, "MITokenService");
		}

		return factory;
	}

	private static class NoCheckTrustManager implements X509TrustManager {
		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}

	private static class TrustAllHostnameVerifier implements HostnameVerifier {
		@Override
		public boolean verify(String arg0, SSLSession arg1) {
			return true;
		}
	}
}
