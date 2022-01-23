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
			name = "student_fetchAllStudentResult",
			query =  "from Result s "
					+ "where s.uid = :P_UID"),
	@NamedQuery(
			name = "result_getResultByStudentIdAndExamId",
			query = "from Result s "
					+ "where s.uid = :P_UID "
					+ "and s.eid = :P_EID"),
})

@Entity
@Table(name = "result")


public class Result implements Serializable {
	public Result(int rid, int eid, int uid, int score) {
		super();
		this.rid = rid;
		this.eid = eid;
		this.uid = uid;
		this.score = score;
	}



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RID")
	private int rid;
	
	@Column(name = "EID")
	private int eid;
	
	@Column(name = "UID")
	private int uid;
	
	@Column(name = "Score")
	private int score;
	
	
	
	public Result() {
		
	}



	public int getRid() {
		return rid;
	}



	public void setRid(int rid) {
		this.rid = rid;
	}



	public int getEid() {
		return eid;
	}



	public void setEid(int eid) {
		this.eid = eid;
	}



	public int getUid() {
		return uid;
	}



	public void setUid(int uid) {
		this.uid = uid;
	}



	public int getScore() {
		return score;
	}



	public void setScore(int score) {
		this.score = score;
	}
	
	
}
