package com.models;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.beans.LoginBean;
import com.entities.Student;
import com.util.SessionManager;
import com.util.Exceptions.SessionException;

public class StudentModel {

	public static boolean create(Student student) {
		boolean result = true;
		Session session = null;
		Transaction transaction = null;

		try {
			session = SessionManager.getSession();
			transaction = session.beginTransaction();
			session.save(student);
			transaction.commit();
		} catch (SessionException e) {
			result = false;
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

	public static List<Student> getStudentList() {
		Session session = null;
		try {
			session = SessionManager.getSession();

			TypedQuery query = session.getNamedQuery("student_fetchAllStudents");
			return query.getResultList();

		} catch (Exception e) {
			e.getStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Student loginStudent(String username, String password) {
		Session session = null;
		String encrypted = null;
		try {
			session = SessionManager.getSession();
			encrypted = LoginBean.encrypt(password);
			TypedQuery query = session.getNamedQuery("student_fetchStudentByLoginCredintials");
			query.setParameter("P_USERNAME", username);
			query.setParameter("P_PASSWORD", encrypted);
			List<Student> student = query.getResultList();

			if (student.size() != 0)
				return student.get(0);
			else
				return null;

		} catch (SessionException e) {
			System.out.println(e);
		} catch (Exception e) {
			e.getStackTrace();
		}

		return null;
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
		} catch (SessionException e) {
			result = false;
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

	public static boolean delete(Student student) {
		boolean result = true;
		Session session = null;
		Transaction transaction = null;

		try {
			session = SessionManager.getSession();
			transaction = session.beginTransaction();
			session.remove(student);
			transaction.commit();
		} catch (SessionException e) {
			result = false;
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

}