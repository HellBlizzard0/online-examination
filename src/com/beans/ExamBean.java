package com.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.entities.Exam;
import com.models.ExamModel;

@SessionScoped
@ManagedBean(name = "examManagedBean")
public class ExamBean {
	public ExamBean() {
		exam = new Exam();
	}
	public Exam exam;

	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}
	public String save() {
		ExamModel examModel = new ExamModel();
		examModel.create(this.exam);
		return "success?faces-redirect=true";
	}
}
