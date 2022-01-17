package com.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;
import com.entities.Answer;
import com.entities.Question;
import com.util.SessionManager;

public class AnswerModel {
	public static boolean create(Answer answer) {
		boolean result = true;
		Session session = null;
		Transaction transaction = null;

		try {
			session = SessionManager.getSession();	
			transaction = session.beginTransaction();
			session.save(answer);
			transaction.commit();
		} catch (Exception e) {
			result = false;
			System.out.println(e);
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			session.close();
		}
		return result;
	}

	public static List<Answer> getAnswerByQuestionId(Question question) {
		Session session = null;
		try {
			session = SessionManager.getSession();

			if (question == null) {

				TypedQuery query = session.getNamedQuery("answer_fetchAllAnswers");
				return query.getResultList();
			} else {
				TypedQuery query = session.getNamedQuery("answer_fetchAnswerByQuestionId");
				query.setParameter("P_QUESTIONID", question.getId());
				return query.getResultList();
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		return null;
	}

	public static List<Answer> getAnswerByQuestionIdList(List<Question> questionList) {		Session session = null;
		try {
			session = SessionManager.getSession();

			List<Integer> idList = new ArrayList<Integer>();
			for(Question q: questionList)
				idList.add(q.getId());

			TypedQuery query = session.getNamedQuery("answer_fetchAnswerByQuestionIdList");
			query.setParameter("P_QUESTIONIDLIST", idList);
			return query.getResultList();

		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public static boolean delete(Answer answer) {
		// TODO Auto-generated method stub
		boolean result = true;
		Session session = null;
		Transaction transaction = null;

		try {
			session = SessionManager.getSession();
			transaction = session.beginTransaction();
			session.remove(answer);
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

	public static boolean update(Answer answer) {
		// TODO Auto-generated method stub
		boolean result = true;
		Session session = null;
		Transaction transaction = null;

		try {
			session = SessionManager.getSession();
			transaction = session.beginTransaction();
			session.update(answer);
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
