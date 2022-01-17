package com.models;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;

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
	
}
