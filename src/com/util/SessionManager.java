package com.util;

import javax.faces.context.FacesContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import com.beans.LoginBean;
import com.util.Exceptions.SessionException;

/**
 * This Class Creates and manages the session.
 * 
 * @author aalsaqqa
 * 
 */
public class SessionManager {
	private static boolean sessionInitiated = false;
	private static Session session;
	private static boolean transactionInitiated = false;
	private static Transaction transaction;
	private static SessionFactory sessfact;

	public static Session getSession() throws SessionException {
		LoginBean login = FacesContext
				.getCurrentInstance()
				.getApplication()
				.evaluateExpressionGet(
						FacesContext.getCurrentInstance(),
						"#{login}",
						LoginBean.class);
		if(login.getUsername() == null 
				|| login.getUsername().equals("")
				|| login.getPassword() == null
				|| login.getPassword().equals(""))
			throw new SessionException("");
		
		if (sessionInitiated) {
			session = sessfact.openSession();
			return session;
		} else {
			StandardServiceRegistry testssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
			Metadata m = new MetadataSources(testssr).getMetadataBuilder().build();
			sessfact = m.getSessionFactoryBuilder().build();
			session = sessfact.openSession();
			sessionInitiated = true;

			return session;
		}
	}

	public static Transaction getTransaction() throws SessionException {
		if (transactionInitiated) {
			return transaction;
		} else {
			transactionInitiated = true;
			return SessionManager.getSession().beginTransaction();
		}

	}
}
