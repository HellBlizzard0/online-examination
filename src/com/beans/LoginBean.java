package com.beans;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.entities.Student;
import com.models.AdminModel;
import com.models.StudentModel;

@SessionScoped
@ManagedBean(name = "login")
public class LoginBean {
	private String username;
	private String password;
	private boolean studentLogin;
	private Student student;

	public String debug() {
		if (Locale.getDefault().getDisplayLanguage().equals("ar"))
			FacesContext.getCurrentInstance().getViewRoot()
					.setLocale(new Locale("en"));
		else
			FacesContext.getCurrentInstance().getViewRoot()
					.setLocale(new Locale("ar"));

		return "index";
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

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

	public String loginAdmin() {

		if (AdminModel.login(username, password))
			return "admin-main";
		else
			return "failure";
	}

	public String loginStudents() {
		student = StudentModel.loginStudent(username, password);
		if (student != null)
			return "student-main";
		else
			return "failure";
	}

	public String logout() {
		this.setPassword("");
		this.setPassword("");
		this.setStudentLogin(false);
		this.student = null;
		((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
				.getSession(false)).invalidate();
		return "index";
	}

	public static String encrypt(String password) {
		String encryptedpassword = null;

		try {
			MessageDigest m = MessageDigest.getInstance("MD5");

			m.update(password.getBytes());

			byte[] bytes = m.digest();

			StringBuilder s = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
						.substring(1));
			}

			encryptedpassword = s.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return encryptedpassword;
	}

}
