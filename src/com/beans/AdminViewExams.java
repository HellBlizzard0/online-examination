package com.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.entities.Exam;
import com.models.ExamModel;

@SessionScoped
@ManagedBean(name = "viewExamsBean")
public class AdminViewExams {
	
	private List<Exam> examsList;

	public List<Exam> getExamsList() {
		return examsList;
	}

	public void setExamsList(List<Exam> examsList) {
		this.examsList = examsList;
	}
	
	public String fetchExams() {
		this.examsList = ExamModel.getExamsList(null, -1);
		return "AdminViewExams";
	}
}
