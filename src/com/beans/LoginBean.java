package com.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import com.models.AdminModel;

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
	
	public String logout() {
		this.setPassword("");
		this.setPassword("");
		this.setStudentLogin(false);
		return "login";
	}
}
