package com.code.dal.orm.securityanalysis;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.code.dal.orm.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_SC_LETTERS")
public class Letter extends BaseEntity implements Serializable {
	private Long id;
	private String letterNumber;
	private Integer type;
	private Date letterDate;
	private String notes;
	private String organization;
	private String fileNumber;
	private Long followUpId;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_SC_LETTER_SEQ", sequenceName = "FIS_SC_LETTER_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_SC_LETTER_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "LETTER_NUMBER")
	public String getLetterNumber() {
		return letterNumber;
	}

	public void setLetterNumber(String letterNumber) {
		this.letterNumber = letterNumber;
	}

	@Basic
	@Column(name = "TYPE")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Basic
	@Column(name = "NOTES")
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Basic
	@Column(name = "ORGANIZATION")
	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	@Basic
	@Column(name = "FILE_NUMBER")
	public String getFileNumber() {
		return fileNumber;
	}

	public void setFileNumber(String fileNumber) {
		this.fileNumber = fileNumber;
	}

	@Basic
	@Column(name = "FOLLOW_UP_ID")
	public Long getFollowUpId() {
		return followUpId;
	}

	public void setFollowUpId(Long followUpId) {
		this.followUpId = followUpId;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LETTER_DATE")
	public Date getLetterDate() {
		return letterDate;
	}

	public void setLetterDate(Date letterDate) {
		this.letterDate = letterDate;
	}
}
