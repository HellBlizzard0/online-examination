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
		@NamedQuery(name = "exam_fetchAllExams",
				query = "from Exam e"),
		@NamedQuery(name = "exam_fetchExamById",
				query = "from Exam e "
						+ "where e.id = :P_ID"),
		@NamedQuery(name = "exam_fetchExamByDepartment",
				query = "from Exam e "
						+ "where e.department = :P_DEPARTMENT"),
		@NamedQuery(name = "exam_fetchExamByLevel",
				query = "from Exam e "
						+ "where e.level = :P_LEVEL"),
		@NamedQuery(name = "exam_fetchExamByDepartmentAndLevel",
				query = "from Exam e "
						+ "where e.department = :P_DEPARTMENT "
						+ "and e.level = :P_LEVEL"),

})

@Entity
@Table(name = "exams")
public class Exam {
	public Exam() {
		super();
	}

	public Exam(int id, int level, String title, String department) {
		super();
		this.id = id;
		this.level = level;
		this.title = title;
		this.department = department;
	}

	private int id;
	private int level;
	private String title;
	private String department;
	private int timer;
	
	@Column(name="Time")
	public int getTimer() {
		return timer;
	}

	public Exam(int id, int level, String title, String department, int timer) {
		super();
		this.id = id;
		this.level = level;
		this.title = title;
		this.department = department;
		this.timer = timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EID")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "Level")
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Column(name = "Title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "Department")
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

}
