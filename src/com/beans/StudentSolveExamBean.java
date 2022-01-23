package com.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;

import com.entities.Answer;
import com.entities.Exam;
import com.entities.Question;
import com.entities.QuestionAnswers;
import com.entities.Result;
import com.entities.Student;
import com.models.AnswerModel;
import com.models.QuestionModel;
import com.models.ResultModel;

@SessionScoped
@ManagedBean(name = "studentSolveExam")
public class StudentSolveExamBean {
	private Exam exam;
	private ArrayList<QuestionAnswers> questions = new ArrayList<QuestionAnswers>();
	private Answer selectedAnswers;
	private Student student;
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	private int score = 0;

	public int getTotalScore() {
		int res = 0;
		for (QuestionAnswers questionAnswers : questions) {
			res += questionAnswers.getScore();
		}
		return res;
	}

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

	public String submit() {
		for (QuestionAnswers questionAnswer : questions) {
			if(questionAnswer.getSelectedAnswer()==null)
				continue;
			for (Answer answer : questionAnswer.getAnswers()) {	
				if (answer.getId() == questionAnswer.getSelectedAnswer().getId()
						&& answer.isCorrect()) {
					this.score += questionAnswer.getScore();
				}
			}

		}
		ResultModel.create(new Result(
				0,
				exam.getId(),
				student.getId(),
				this.getScore()));
		return "StudentExamSubmitResult";
	}

	public int getScore() {
		return score;
	}

	public String goTo(Exam exam, Student student) {
		this.exam = exam;
		List<Question> list = QuestionModel.getQuestionList(exam);
		this.student = student;

		for (Question question : list) {
			QuestionAnswers q = new QuestionAnswers(
					question,
					AnswerModel
							.getAnswerByQuestionId(question));
			// q.setSelectedAnswer(new Answer(0, "", 0, false));
			this.questions
					.add(q);
		}

		return "StudentSolveExam";
	}
}
