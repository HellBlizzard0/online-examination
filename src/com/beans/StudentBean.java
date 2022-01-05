package com.beans;



import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.entities.Student;
import com.models.StudentModel;


@SessionScoped
@ManagedBean(name = "studentManagedBean")
public class StudentBean {

	private Student student;

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public StudentBean() {
		this.student = new Student();
	}

	public String save() {
		StudentModel studentModel = new StudentModel();
		studentModel.create(this.student);
		return "success?faces-redirect=true";
	}
	

}
