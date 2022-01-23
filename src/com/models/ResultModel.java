package com.models;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.entities.Question;
import com.entities.Result;
import com.util.SessionManager;

public class ResultModel {

	public static List<Result> getResultList(int id) {
		Session session = null;
		try {
		session = SessionManager.getSession();
		
		TypedQuery<Result> query = session.getNamedQuery("student_fetchAllStudentResult");
		query.setParameter("P_UID", id);
		return query.getResultList();
		
		} catch (Exception e) {
		System.out.println(e);
		}
		return null;
		}
	
	public static boolean create(Result newResult) {
		// TODO Auto-generated method stub
		boolean result = true;
		Session session = null;
		Transaction transaction = null;

		try {
			session = SessionManager.getSession();
			transaction = session.beginTransaction();
			session.save(newResult);
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
