package com.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.entities.Exam;
import com.entities.Question;
import com.models.QuestionModel;

@SessionScoped
@ManagedBean
public class CreateQuestionBean {
	private Question question;
	private int level;
	private String department;
	private List<Exam> examList;
	
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public List<Exam> getExamList() {
		return examList;
	}
	public void setExamList(List<Exam> examList) {
		this.examList = examList;
	}
	
	public String save() {
		QuestionModel questionModel = new QuestionModel();
		questionModel.create(this.question);
		return "success?faces-redirect=true";
	}
	
	
}
