package com.models;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.beans.LoginBean;
import com.beans.StudentBean;
import com.entities.Admin;
import com.entities.Student;
import com.util.SessionManager;
import com.util.Exceptions.SessionException;

@SuppressWarnings("unused")
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
		} catch(SessionException e) {
			result = false;
			if (transaction != null) {
				transaction.rollback();
			}
		}
		catch (Exception e) {
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
		}  catch(SessionException e) {
			result = false;
			if (transaction != null) {
				transaction.rollback();
			}
		}
		catch (Exception e) {
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
		String encrypted=null;
		try {
			session = SessionManager.getSession();
			encrypted=LoginBean.encrypt(password);
			TypedQuery query = session.getNamedQuery("admin_fetchAdminByLoginCredintials");
			query.setParameter("P_USERNAME", username);
			query.setParameter("P_PASSWORD", encrypted);
			List<Admin> admin = query.getResultList();
			
			if (admin.size() != 0)
				return true;
			else
				return false;
			
		}  catch(SessionException e) {
			System.out.println(e);
		}
		catch(Exception e) {
			System.out.println(e);
		}
		
		return false;
	}
	
	
	
	
	
}
