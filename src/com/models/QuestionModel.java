package com.models;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.entities.Exam;
import com.entities.Question;
import com.util.SessionManager;
import com.util.Exceptions.SessionException;

public class QuestionModel {

	public static boolean create(Question question) {
		// TODO Auto-generated method stub
		boolean result = true;
		Session session = null;
		Transaction transaction = null;

		try {
			session = SessionManager.getSession();
			transaction = session.beginTransaction();
			session.save(question);
			transaction.commit();
		} catch(SessionException e) {
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

	public static boolean update(Question question) {
		// TODO Auto-generated method stub
		boolean result = true;
		Session session = null;
		Transaction transaction = null;

		try {
			session = SessionManager.getSession();
			transaction = session.beginTransaction();
			session.update(question);
			transaction.commit();
		} catch(SessionException e) {
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

	public static boolean delete(Question question) {
		// TODO Auto-generated method stub
		boolean result = true;
		Session session = null;
		Transaction transaction = null;

		try {
			session = SessionManager.getSession();
			transaction = session.beginTransaction();
			session.delete(question);
			transaction.commit();
		}catch(SessionException e) {
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Question> getQuestionList(Exam exam) {
		Session session = null;
		try {
			session = SessionManager.getSession();

			if (exam == null) {

				TypedQuery query = session.getNamedQuery("question_fetchAllQuestions");
				return query.getResultList();
			} else {
				TypedQuery query = session.getNamedQuery("question_fetchQuestionsByExamId");
				query.setParameter("P_ID", exam.getId());
				return query.getResultList();
			}
		}catch(SessionException e) {
System.out.println(e);		} catch (Exception e) {
			e.getStackTrace();
		}
		return null;
	}

	public static long getTotalMarks(Exam exam) {
		// TODO Auto-generated method stub
		Session session = null;
		try {
			session = SessionManager.getSession();

			if (exam == null) {

				TypedQuery query = session.getNamedQuery("question_fetchQAllScores");
				return (Long) query.getSingleResult();
			} else {
				TypedQuery query = session.getNamedQuery("question_fetchScoreByExamId");
				query.setParameter("P_EXAMID", exam.getId());
				return (Long) query.getSingleResult();
			}
		}catch(SessionException e) {
System.out.println(e);		} catch (Exception e) {
			e.getStackTrace();
		}
		return -1;
	
	}
}
