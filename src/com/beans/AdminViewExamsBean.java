package com.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.entities.Exam;
import com.entities.Question;
import com.models.ExamModel;

@SessionScoped
@ManagedBean(name = "viewExamsBean")
public class AdminViewExamsBean {

	private List<Exam> examsList;
	private ArrayList<Exam> selectedExam = new ArrayList<Exam>();
	private Question question;

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public List<Exam> getSelectedExam() {
		return selectedExam;
	}

	public void setSelectedExam(ArrayList<Exam> selectedExam) {
		this.selectedExam = selectedExam;
	}

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
