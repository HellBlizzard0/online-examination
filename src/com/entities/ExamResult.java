package com.entities;




import javax.annotation.concurrent.Immutable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@NamedQueries({
	
	
	@NamedQuery(
			name = "examScore_fetchAll",
			query = "from ExamResult q "
					+ "where q.eid=:P_EID"),
		@NamedQuery(
				name = "examScore_fetchExameScore",
				query = "SELECT score "
						+"from ExamResult q "
						+ "where q.eid=:P_EID"),
		
		@NamedQuery(
				name = "examScore_fetchExameTitle",
				query = "SELECT title "
						+"from ExamResult q "
						+ "where q.eid=:P_EID"),
		
})

@Entity
@Immutable
@Table(name = "exam_score")
public class ExamResult {
	
	public ExamResult() {
		
	}
	public ExamResult(int eid, String title, int score) {
		this.eid = eid;
		this.title = title;
		this.score = score;
	}
	
	private int eid;
	private String title;
	private int score;
	
	@Id
	@Column(name = "eid")
	public int getEid() {
		return eid;
	}
	@Basic
	@Column(name = "title")
	public String getTitle() {
		return title;
	}
	
	@Basic
	@Column(name = "TotalScore")
	public int getScore() {
		return score;
	}
	
	
	public void setEid(int eid) {
		this.eid = eid;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	public void setScore(int score) {
		this.score = score;
	}

	

}
