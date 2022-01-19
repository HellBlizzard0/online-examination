package com.entities;

import java.util.List;

public class QuestionAnswers extends Question {
	public QuestionAnswers(Question question, List<Answer> answers) {
		super(
				question.getId(),
				question.getText(),
				question.getScore(),
				question.getExamId());
		this.answers = answers;
	}

	List<Answer> answers;
	Answer selectedAnswer;

	public Answer getSelectedAnswer() {
		return selectedAnswer;
	}

	public void setSelectedAnswer(Answer selectedAnswer) {
		this.selectedAnswer = selectedAnswer;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}


}
