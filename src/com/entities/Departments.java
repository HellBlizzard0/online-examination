package com.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@SuppressWarnings("serial")
@NamedQueries({
		@NamedQuery(
				name = "departments_fetchAll",
				query = "from Departments "),
		
})

@Entity
@Table(name = "departments")
public class Departments implements Serializable {

	public Departments() {
		
	}
	
	public Departments(int did, String department) {
		super();
		this.did = did;
		this.department=department;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DID")
	private int did;

	@Column(name = "Department")
	private String department;

	public int getDid() {
		return did;
	}

	public void setDid(int did) {
		this.did = did;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
	
	

}
