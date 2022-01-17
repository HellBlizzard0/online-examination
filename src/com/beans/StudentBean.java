package com.beans;



import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TypedQuery;
import java.sql.*;

import org.hibernate.Session;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.security.MessageDigest;

import com.entities.Result;
import com.entities.Student;
import com.models.ResultModel;
import com.models.StudentModel;
import com.util.SessionManager;


@SessionScoped
@ManagedBean(name = "studentManagedBean")
public class StudentBean {

	private Student student;
	private List<Student> studentsList;
	private List<Result> resultList;
	
	
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public List<Student> getStudentsList() {
		return studentsList;
	}
	
	public void setStudentsList(List<Student> studentsList) {
		this.studentsList = studentsList;
	}

	public List<Result> getResultList() {
		return resultList;
	}

	public void setResultList(List<Result> resultList) {
		this.resultList = resultList;
	}
	
	public StudentBean() {
		this.student = new Student();
	}


	public String save() {
		StudentModel studentModel = new StudentModel();
		student.setPassword(LoginBean.encrypt(student.getPassword()));
		studentModel.create(this.student);
		this.student=new Student();
		return "success?faces-redirect=true";
	}
	
	
	/*public String ss() {
		studentsList=StudentModel.getStudentList();
		return "result";
	}*/
	
	public String getStudentResult() {
		//getStudent().setId(4);
		resultList=ResultModel.getResultList(4);
		return "result";
	}

	
}
