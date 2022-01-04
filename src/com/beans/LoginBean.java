package com.beans;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.entities.Student;
import com.util.HibernateUtil;

@SessionScoped
@ManagedBean(name = "login")
public class LoginBean {
	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	public void login() {
		
		// Prep work
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		@SuppressWarnings({ "rawtypes", "deprecation" })
		Query query = session.getNamedQuery("student_fetchAllStudents");
		List<Student> studentessList = query.list();
		for (Student student : studentessList) {
			System.out.println("List of Address::" + student.getId() + "::"
					+ student.getUsername() + "::" + student.getPassword());
		}
		
	}
}
