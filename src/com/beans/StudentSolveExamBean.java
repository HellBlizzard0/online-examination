package com.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.entities.Answer;
import com.entities.Exam;
import com.entities.Question;
import com.entities.QuestionAnswers;
import com.models.AnswerModel;
import com.models.QuestionModel;

@SessionScoped
@ManagedBean(name = "studentSolveExam")
public class StudentSolveExamBean {
	private Exam exam;
	ArrayList<QuestionAnswers> questions = new ArrayList<QuestionAnswers>();
	Answer selectedAnswers;
	public Answer getSelectedAnswers() {
		return selectedAnswers;
	}

	public void setSelectedAnswers(Answer selectedAnswers) {
		this.selectedAnswers = selectedAnswers;
	}

	public ArrayList<QuestionAnswers> getQuestions() {
		return questions;
	}

	public void setQuestions(ArrayList<QuestionAnswers> questions) {
		this.questions = questions;
	}

	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public void debug() {
		ArrayList<QuestionAnswers> d = this.questions;
		System.out.println("Debug method Accessed");

	}

	public String goTo(Exam exam) {
		this.exam = exam;
		List<Question> list = QuestionModel.getQuestionList(exam);

		for (Question question : list) {
			QuestionAnswers q = new QuestionAnswers(
					question,
					AnswerModel
							.getAnswerByQuestionId(question));
			//q.setSelectedAnswer(new Answer(0, "", 0, false));
			this.questions
					.add(q);
		}
		return "StudentSolveExam";
	}
}
