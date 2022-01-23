package com.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.entities.Exam;
import com.entities.Student;
import com.models.ExamModel;
import com.models.QuestionModel;

@SessionScoped
@ManagedBean(name = "studentViewExams")
public class StudentViewExamsBean {
	List<Exam> exams;
	List<Integer> totalMarks;

	public Long getTotalMarks(Exam exam) {
		return QuestionModel.getTotalMarks(exam);
	}

	public List<Exam> getExams() {
		return exams;
	}

	public void setExams(List<Exam> exams) {
		this.exams = exams;
	}

	public List<Integer> getTotalMarks() {
		return totalMarks;
	}

	public void setTotalMarks(List<Integer> totalMarks) {
		this.totalMarks = totalMarks;
	}

	/**
	 *
	 * @author aalsaqqa
	 * @return outcome String [StudentViewExams]
	 */
	public String goTo(Student student) {
		if (student == null) {
			student = new Student(0, "stdName", "stdUsername", "stdPassword", "std@mail.com", "ICS", 3);
			exams = ExamModel.getExamByStudent(student);
		} else {
			exams = ExamModel.getExamByStudent(student);
		}
		return "StudentViewExams";
	}
}
