package com.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * This Class Creates and manages the session.
 * @author aalsaqqa
 * 
 * */
public class SessionManager {
	private static boolean sessionInitiated = false;
	private static Session session;
	public static Session getSession() {
		if(sessionInitiated)
			return session;
		else {
			StandardServiceRegistry testssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
			Metadata m = new MetadataSources(testssr).getMetadataBuilder().build();
			SessionFactory sessfact = m.getSessionFactoryBuilder().build();
			session = sessfact.openSession();
			sessionInitiated = true;
			return session;
		}
			
	}
}
