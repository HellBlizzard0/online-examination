package com.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.entities.Answer;
import com.entities.Exam;
import com.entities.Question;
import com.models.AnswerModel;
import com.models.QuestionModel;

@SessionScoped
@ManagedBean(name = "viewExamDetailsBean")
public class AdminViewExamDetailsBean {
	private Exam exam;
	private List<Question> questions;
	private List<Answer> answers;
	private boolean displayQuestionDetails;
	private boolean edit = false;

	public void deleteQuestion(Question question) {
		questions.remove(question);
		QuestionModel.delete(question);	
	}
	
	public void toggleEdit(Question question) {
		if (!this.edit) {
			this.edit = true;
		} else {
			QuestionModel.update(question);
			this.edit = false;
		}

	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public boolean isDisplayQuestionDetails() {
		return displayQuestionDetails;
	}

	public void setDisplayQuestionDetails(boolean displayQuestionDetails) {
		this.displayQuestionDetails = displayQuestionDetails;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public String viewInformation(Exam exam) {
		this.exam = exam;
		this.questions = QuestionModel.getQuestionList(exam);
		this.answers = AnswerModel.getAnswerByQuestionIdList(questions);

		return "AdminViewExamDetails";
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

}
