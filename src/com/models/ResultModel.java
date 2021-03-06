package com.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.entities.Departments;
import com.entities.Exam;
import com.entities.ExamResult;
import com.entities.Result;
import com.entities.ResultDetailed;
import com.entities.Student;
import com.util.SessionManager;
import com.util.Exceptions.SessionException;

@SuppressWarnings({"unchecked"})
public class ResultModel {

	public static List<Result> getResultList(int id) {
		Session session = null;
		try {
			session = SessionManager.getSession();

			TypedQuery<Result> query = session.getNamedQuery("student_fetchAllStudentResult");
			query.setParameter("P_UID", id);
			return query.getResultList();

		} catch (SessionException e) {
			System.out.println(e);
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public static boolean create(Result newResult) {
		boolean result = true;
		Session session = null;
		Transaction transaction = null;

		try {
			session = SessionManager.getSession();
			transaction = session.beginTransaction();
			session.save(newResult);
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

	public static Result getExamResult(Exam exam, Student student) {
		Session session = null;
		try {
			session = SessionManager.getSession();

			TypedQuery<Result> query = session.getNamedQuery("result_getResultByStudentIdAndExamId");
			query.setParameter("P_UID", student.getId());
			query.setParameter("P_EID", exam.getId());
			return (Result) query.getSingleResult();

		} catch (SessionException e) {
			System.out.println(e);
		} catch (Exception e) {
			// System.out.println(e);
		}
		return null;
	}

	/*
	 * public static ExamResult getExamTotalScore(int id) { Session session = null; try { session = SessionManager.getSession();
	 * 
	 * TypedQuery<ExamResult> query = session.getNamedQuery("examScore_fetchExameScoreAndTitle"); query.setParameter("P_EID", id); return query.getSingleResult();
	 * 
	 * } catch (Exception e) { System.out.println(e); } return null; }
	 */

	public static ExamResult getExamTitleAndTotalScore(int id) {
		Session session = null;
		try {
			session = SessionManager.getSession();

			TypedQuery<ExamResult> query = session.getNamedQuery("examScore_fetchAll");
			query.setParameter("P_EID", id);
			return query.getSingleResult();

		} catch (SessionException e) {
			System.out.println(e);
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public static List<ResultDetailed> getAllDetailed() {
		Session session = null;
		List<Result> results;
		Exam exam;
		Student student;
		ArrayList<ResultDetailed> resultsDetailed = new ArrayList<ResultDetailed>();
		;
		try {
			session = SessionManager.getSession();

			results = session
					.getNamedQuery("result_fetchAllResult")
					.getResultList();
			for (Result result : results) {
				try {
					exam = (Exam) session
							.getNamedQuery("exam_fetchExamById")
							.setParameter("P_ID", result.getEid())
							.getSingleResult();
					student = (Student) session
							.getNamedQuery("student_fetchStudentById")
							.setParameter("P_ID", result.getUid())
							.getSingleResult();

					resultsDetailed.add(new ResultDetailed(result, exam, student));
				} catch (Exception e) {
					System.out.println(e);
				}
			}
			return resultsDetailed;

		} catch (SessionException e) {
			System.out.println(e);
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public static ExamResult getExamTotalScore(int id) {
		Session session = null;
		try {
			session = SessionManager.getSession();

			
			TypedQuery<ExamResult> query = session.getNamedQuery("examScore_fetchExameScore");
			query.setParameter("P_EID", id);
			return query.getSingleResult();

		} catch (SessionException e) {
			System.out.println(e);
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	public static List<Departments> getDepartments() {
		Session session = null;
		try {
			session = SessionManager.getSession();

			TypedQuery<Departments> query = session.getNamedQuery("departments_fetchAll");
			return query.getResultList();

		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

}
