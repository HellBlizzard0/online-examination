package com.models;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.entities.Admin;
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
	
	public static List<Exam> getExamsList(String dept, int level){
		Session session = null;
		try {
			session = SessionManager.getSession();
			
			if(dept != null && level == -1) {
				TypedQuery query = session.getNamedQuery("exam_fetchExamByDepartmentAndLevel");
				query.setParameter("P_DEPARTMENT", dept);
				query.setParameter("P_LEVEL", level);
				return query.getResultList();
			}
			else if(dept != null) {
				TypedQuery query = session.getNamedQuery("exam_fetchExamByLevel");
				query.setParameter("P_LEVEL", level);
				return query.getResultList();
				
			}
			else if(level != -1 ) {
				TypedQuery query = session.getNamedQuery("exam_fetchExamByDepartment");
				query.setParameter("P_DEPARTMENT", dept);
				return query.getResultList();
			}
			else {
				TypedQuery query = session.getNamedQuery("exam_fetchAllExams");
				return query.getResultList();
			}
		} catch(Exception e) {
			e.getStackTrace();
		}
		return null;
	}

}
