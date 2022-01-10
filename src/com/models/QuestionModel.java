package com.models;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.entities.Question;
import com.util.SessionManager;

public class QuestionModel {

	public boolean create(Question question) {
		// TODO Auto-generated method stub
		boolean result = true;
		Session session = null;
		Transaction transaction = null;
		
		try {
			session = SessionManager.getSession();
			transaction = session.beginTransaction();
			session.save(question);
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
