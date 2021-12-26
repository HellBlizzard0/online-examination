/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.code.services.util;

/**
 *
 * @author Ali.Abdullah
 */
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.code.services.BaseService;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.log4j.Log4j;

public class MailService extends BaseService {
	private static Session session;

	/**
	 * 
	 * @param messageSubject
	 * @param messageText
	 * @param toMails
	 *            : to mails comma separated
	 */
	public static boolean send(String messageSubject, String messageText, String toMails) {
		boolean success = false;
		try {
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.host", InfoSysConfigurationService.getLDAPIP());
			props.put("mail.smtp.port", InfoSysConfigurationService.getLDAPPort());
			session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(InfoSysConfigurationService.getLDAPAdminUsername() + "@" + InfoSysConfigurationService.getLDAPDomain(), InfoSysConfigurationService.getLDAPAdminPassword());
				}
			});
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("infosys.dont.reply@fg.gov.sa"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMails));
			message.setSubject(messageSubject);
			message.setContent(messageText, "text/html; charset=utf-8");
			Transport.send(message);
			success = true;
		} catch (Exception e) {
			Log4j.traceErrorException(MailService.class, e, "MailService");
		}
		return success;
	}
}
