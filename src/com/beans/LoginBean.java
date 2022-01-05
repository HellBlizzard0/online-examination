package com.beans;

import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.TypedQuery;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import com.entities.Student;
import com.util.HibernateUtil;
import com.util.SessionManager;

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

	public String login() {

		Session session = SessionManager.getSession();
		TypedQuery testquery = session.getNamedQuery("student_fetchStudentByLoginCredintials");
		testquery.setParameter("P_USERNAME", username);
		testquery.setParameter("P_PASSWORD", password);
		List<Student> student = testquery.getResultList();

		if (student.size() != 0)
			return "success";
		else
			return "failure";

	}
}
