package com.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@NamedQueries({
		@NamedQuery(
				name = "question_fetchAllQuestions",
				query = "from Question q")
})

@Entity
@Table(name = "question")
public class Question {
	private int id;
	private String text;
	private int score;
	private int examId;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public int getId() {
		return id;
	}

	@Column(name = "text")
	public String getText() {
		return text;
	}

	@Column(name = "score")
	public int getScore() {
		return score;
	}

	@Column(name = "examId")
	public int getExamId() {
		return examId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setExamId(int examId) {
		this.examId = examId;
	}

}
