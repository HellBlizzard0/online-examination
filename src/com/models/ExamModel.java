package com.models;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.entities.Exam;
import com.entities.Student;
import com.util.SessionManager;
import com.util.Exceptions.SessionException;

public class ExamModel {

	public static boolean create(Exam exam) {

		boolean result = true;
		Session session = null;
		Transaction transaction = null;

		try {
			session = SessionManager.getSession();
			transaction = session.beginTransaction();
			session.save(exam);
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Exam> getExamsList(String dept, int level) {
		Session session = null;
		try {
			session = SessionManager.getSession();

			if (dept != null && level == -1) {

				TypedQuery query = session.getNamedQuery("exam_fetchExamByDepartmentAndLevel");
				query.setParameter("P_DEPARTMENT", dept);
				query.setParameter("P_LEVEL", level);
				return query.getResultList();
			} else if (dept != null) {
				TypedQuery query = session.getNamedQuery("exam_fetchExamByLevel");
				query.setParameter("P_LEVEL", level);
				return query.getResultList();

			} else if (level != -1) {
				TypedQuery query = session.getNamedQuery("exam_fetchExamByDepartment");
				query.setParameter("P_DEPARTMENT", dept);
				return query.getResultList();
			} else {
				TypedQuery query = session.getNamedQuery("exam_fetchAllExams");
				return query.getResultList();
			}
		} catch (SessionException e) {
			System.out.println(e);
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public static boolean update(Exam exam) {
		// TODO Auto-generated method stub
		boolean result = true;
		Session session = null;
		Transaction transaction = null;

		try {
			session = SessionManager.getSession();
			transaction = session.beginTransaction();
			session.update(exam);
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

	public static boolean delete(Exam exam) {

		boolean result = true;
		Session session = null;
		Transaction transaction = null;

		try {
			session = SessionManager.getSession();
			transaction = session.beginTransaction();
			session.remove(exam);
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Exam> getExamByStudent(Student student) {
		Session session = null;
		try {
			session = SessionManager.getSession();

			TypedQuery query = session.getNamedQuery("exam_fetchExamByDepartmentAndLevel");
			query.setParameter("P_DEPARTMENT", student.getDepartment());
			query.setParameter("P_LEVEL", student.getLevel());
			return query.getResultList();

		} catch (SessionException e) {
			System.out.println(e);
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;

	}

}
