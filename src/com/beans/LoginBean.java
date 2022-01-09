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
import com.models.AdminModel;
import com.util.HibernateUtil;
import com.util.SessionManager;

@SessionScoped
@ManagedBean(name = "login")
public class LoginBean {
	private String username;
	private String password;
	private boolean studentLogin;

	public boolean isStudentLogin() {
		return studentLogin;
	}

	public void setStudentLogin(boolean studentLogin) {
		this.studentLogin = studentLogin;
	}

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
		if (!studentLogin) {
			if (AdminModel.login(username, password))
				return "admin-main";
			else
				return "failure";
		} else {
			return "failure";
		}
	}
}
