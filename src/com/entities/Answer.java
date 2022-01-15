package com.entities;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@NamedQueries({
		@NamedQuery(
				name = "answer_fetchAllAnswers",
				query = "from Answer a"),
		@NamedQuery(
				name = "answer_fetchAnswerByQuestionId",
				query = "from Answer a "
						+ "where a.questionId = :P_QUESTIONID"),
		@NamedQuery(
				name = "answer_fetchAnswerByQuestionIdList",
				query = "from Answer a "
						+ "where a.questionId in :P_QUESTIONIDLIST")
})

@Entity
@Table(name = "answer")
public class Answer {
	
	private int id;
	private String text;
	private int questionId;
	private boolean correct;
	
	

	public Answer() {
		super();
		this.text="";
	}

	public Answer(int id, String text, int questionId, boolean correct) {
		super();
		this.id = id;
		this.text = text;
		this.questionId = questionId;
		this.correct = correct;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "text")
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Column(name = "questionID")
	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	@Column(name = "correct")
	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

}
