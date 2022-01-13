package com.models;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.entities.Admin;
import com.entities.Student;
import com.util.SessionManager;

public class AdminModel {
	public static boolean create(Student student) {
		boolean result = true;
		Session session = null;
		Transaction transaction = null;
		
		try {
			session = SessionManager.getSession();
			transaction = session.beginTransaction();
			session.save(student);
			transaction.commit();
		} catch (Exception e) {
			result = false;
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			session.close();
		}
		return result;
	}
	
	public static boolean update(Student student) {
		boolean result = true;
		Session session = null;
		Transaction transaction = null;
		
		try {
			session = SessionManager.getSession();
			transaction = session.beginTransaction();
			session.update(student);
			transaction.commit();
		} catch (Exception e) {
			result = false;
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			session.close();
		}
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean login(String username, String password) {
		Session session = null;
		try {
			session = SessionManager.getSession();
			TypedQuery query = session.getNamedQuery("admin_fetchAdminByLoginCredintials");
			query.setParameter("P_USERNAME", username);
			query.setParameter("P_PASSWORD", password);
			List<Admin> admin = query.getResultList();
			
			if (admin.size() != 0)
				return true;
			else
				return false;
			
		} catch(Exception e) {
			e.getStackTrace();
		}
		
		return false;
	}
	
	
	
	
	
}
