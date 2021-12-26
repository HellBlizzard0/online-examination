package com.code.services.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.code.services.config.InfoSysConfigurationService;
import com.code.services.log4j.Log4j;

public class SMSService {

	// public static boolean sendWebServiceSmsMessage(String mobileNumber, String message) {
	// try {
	// String url = InfoSysConfigurationService.getSmsUrl();//"http://172.16.4.105:81/smsprovider.asmx?WSDL";
	// String userNameValue = InfoSysConfigurationService.getSmsUsername();//"aviation";
	// String passwordValue = InfoSysConfigurationService.getSmsPassword();//"136479";
	// String senderNameValue = InfoSysConfigurationService.getSmsSenderName();//"BG Aviation";
	//
	// if (url != "" && userNameValue != "" && passwordValue != "" && senderNameValue != "") {
	// ServiceClient dynamicClient = new ServiceClient(null, new URL(url), new QName("http://fg.gov.sa/", "SMSProvider"), "SMSProviderSoap");
	//
	// SOAPFactory fac2 = OMAbstractFactory.getSOAP11Factory();
	// OMNamespace omNs2 = fac2.createOMNamespace("http://fg.gov.sa/", "fg");
	// SOAPEnvelope envelope = fac2.getDefaultEnvelope();
	// envelope.declareNamespace(omNs2);
	//
	// // creating the payload
	// OMFactory fac = OMAbstractFactory.getOMFactory();
	// OMElement method = fac.createOMElement("SendSMS", omNs2);
	//
	// // Username
	// OMElement username = fac.createOMElement("username", omNs2);
	// username.setText(userNameValue); // old erezeq new tamrir
	// method.addChild(username);
	//
	// // password
	// OMElement password = fac.createOMElement("password", omNs2);
	// password.setText(passwordValue); // old 123456 new 136479
	// method.addChild(password);
	//
	// // Mobile
	// OMElement mobile = fac.createOMElement("mobile", omNs2);
	// mobile.setText(mobileNumber);
	// method.addChild(mobile);
	//
	// // Unicode
	// OMElement unicode = fac.createOMElement("unicode", omNs2);
	// unicode.setText("u");
	// method.addChild(unicode);
	//
	// // message
	// OMElement messages = fac.createOMElement("message", omNs2);
	//
	// OMTextImpl omText = (OMTextImpl) fac.createOMText(message);
	// omText.setType(12);
	//
	// messages.addChild(omText);
	// method.addChild(messages);
	//
	// // message
	// OMElement sendername = fac.createOMElement("sendername", omNs2);
	// sendername.setText(senderNameValue);
	// method.addChild(sendername);
	//
	// envelope.getBody().addChild(method);
	//
	// OperationClient operationClient = dynamicClient.createClient(new QName("http://fg.gov.sa/","SendSMS"));
	// MessageContext outMsgCtx = new MessageContext();
	// Options opts = outMsgCtx.getOptions();
	//
	// // setting properties into option
	// opts.setTo(new EndpointReference(url));
	// opts.setAction("");
	// opts.setTimeOutInMilliSeconds(10000);
	// System.out.println(envelope);
	// outMsgCtx.setEnvelope(envelope);
	//
	// operationClient.addMessageContext(outMsgCtx);
	// operationClient.execute(true);
	//
	// MessageContext inMsgtCtx = operationClient.getMessageContext("In");
	//
	// SOAPEnvelope response = inMsgtCtx.getEnvelope();
	// String res = response.toString();
	// if (res.contains("<SendSMSResult>0</SendSMSResult>"))
	// return true;
	// System.out.println(response);
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// return false;
	// }
	// return false;
	//
	// }

	/**
	 * This method is used to send Arabic or English message to specific mobileNumber
	 * 
	 * @param message
	 *            : String containing message to be sent
	 * @param mobileNumber
	 *            : String containing mobile number
	 * @param language
	 *            : language of the message. 1 for English and 2 for Arabic. default is 2 Arabic
	 * @return Boolean represent success of the operation
	 */
	public static boolean sendHttpSmsMessage(String message, String mobileNumber, Integer... language) {
		if (message == null || mobileNumber == null)
			return false;
		String smsMessageUrl = InfoSysConfigurationService.getSmsCarrierLink();
		smsMessageUrl += "&message=" + stringToHex(message);

		// smsMessageUrl += "&MobileNo=" + "507734774,582076429";
		smsMessageUrl += "&MobileNo=" + mobileNumber;
		smsMessageUrl += "&txtlang=" + (language == null ? 2 : 1);

		try {
			URL url = new URL(smsMessageUrl);

			URLConnection connection = url.openConnection();
			connection.connect();

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			// String inputLine;
			while (in.readLine() != null)
				;

			in.close();

			return true;
		} catch (MalformedURLException e) {
			Log4j.traceErrorException(SMSService.class, e, "SMSService");
		} catch (IOException e) {
			Log4j.traceErrorException(SMSService.class, e, "SMSService");
		}
		return false;
	}

	private static String stringToHex(String Str) {
		StringBuffer buffer = new StringBuffer();
		for (int x = 0; x < Str.length(); x++) {
			String hex = new String(Integer.toHexString(Str.charAt(x)));
			buffer.append(addPadding(hex, 4));
		}
		return buffer.toString();
	}

	private static String addPadding(String str, int size) {
		String paddedStr = "";
		for (int j = 0; j < size - str.length(); j++) {
			paddedStr += "0";
		}
		paddedStr += str;
		return paddedStr;
	}
}
