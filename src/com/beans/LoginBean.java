package com.beans;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import com.models.StudentModel;
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
	
	public String loginStudint() {
		if (!studentLogin) {
			if (StudentModel.loginStudent(username, password))
				return "Student-main";
			else
				return "failure";
		} else {
			return "failure";
		}
	}
	
	public static String encrypt(String password) {
        String encryptedpassword = null;  

		try   
	        {  
	            MessageDigest m = MessageDigest.getInstance("MD5");  
	              
	            m.update(password.getBytes());  
	              
	            byte[] bytes = m.digest();  
	              
	            StringBuilder s = new StringBuilder();  
	            for(int i=0; i< bytes.length ;i++)  
	            {  
	                s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));  
	            }  
	              
	            encryptedpassword = s.toString();  
	        }   
	        catch (NoSuchAlgorithmException e)   
	        {  
	            e.printStackTrace();  
	        }
		return encryptedpassword;  	
		}


}
