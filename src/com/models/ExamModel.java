package com.models;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.entities.Exam;
import com.util.SessionManager;

public class ExamModel {

	public boolean create(Exam exam) {
		
		boolean result = true;
		Session session = null;
		Transaction transaction = null;
		
		try {
			session = SessionManager.getSession();
			transaction = session.beginTransaction();
			session.save(exam);
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

}
