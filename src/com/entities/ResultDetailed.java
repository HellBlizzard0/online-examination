package com.entities;

public class ResultDetailed {
	private Result result;
	private Exam exam;
	private Student student;

	public Result getResult() {
		return result;
	}

	public ResultDetailed(Result result, Exam exam, Student student) {
		super();
		this.result = result;
		this.exam = exam;
		this.student = student;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
}
