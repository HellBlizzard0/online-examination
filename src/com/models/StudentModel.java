package com.models;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.entities.Admin;
import com.entities.Student;
import com.util.SessionManager;

public class StudentModel {

	public boolean create(Student student) {
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
	
	
}