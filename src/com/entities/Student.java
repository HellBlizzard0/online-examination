package com.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.NamedQueries;

@SuppressWarnings("serial")
@NamedQueries({
		@NamedQuery(
				name = "student_fetchAllStudents",
				query = "from Student s"),
		@NamedQuery(
				name = "student_fetchStudentByLoginCredintials",
				query = "from Student s "
						+ "where s.username = :P_USERNAME "
						+ "and s.password = :P_PASSWORD"),
		@NamedQuery(
				name = "student_fetchStudentById",
				query = "from Student s "
						+ "where s.id = :P_ID "),

})

@Entity
@Table(name = "student")
public class Student implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "email")
	private String email;

	@Column(name = "department")
	private String department;

	@Column(name = "level")
	private int level;

	public Student() {

	}

	public Student(int id, String name, String username, String password, String email, String department, int level) {
		super();
		this.id = id;
		this.name = name;
		this.username = username;
		this.password = password;
		this.email = email;
		this.department = department;
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
