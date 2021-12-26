package com.code.dal.orm.securityanalysis;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.code.dal.orm.BaseEntity;
import com.code.services.util.HijriDateService;


@NamedQueries({ 
    @NamedQuery(name  = "letterData_searchLetterData",
				query = " SELECT l " +
			            " FROM LetterData l " + 
			            " WHERE (:P_FOLLOW_UP_ID = -1 OR l.followUpId = :P_FOLLOW_UP_ID )"+
			            " And (:P_LETTER_NUMBER = '-1' OR l.letterNumber = :P_LETTER_NUMBER)"+
			            " ORDER BY l.letterNumber"
				)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_SC_VW_LETTERS")
public class LetterData extends BaseEntity implements Serializable {
	private String letterNumber;
	private Integer type;
	private Date letterDate;
	private String letterDateString;
	private String notes;
	private String organization;
	private String contactNumber;
	private String fileNumber;
	private Long followUpId;
	private Letter letter;

	public LetterData() {
		this.letter = new Letter();
	}

	@Id
	@Column(name = "LETTER_ID")
	public Long getId() {
		return letter.getId();
	}

	public void setId(Long id) {
		this.letter.setId(id);
	}

	@Basic
	@Column(name = "LETTER_NUMBER")
	public String getLetterNumber() {
		return letterNumber;
	}

	public void setLetterNumber(String letterNumber) {
		this.letter.setLetterNumber(letterNumber);
		this.letterNumber = letterNumber;
	}

	@Basic
	@Column(name = "TYPE")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.letter.setType(type);
		this.type = type;
	}

	@Basic
	@Column(name = "NOTES")
	public String getNotes() {
		return notes;
	}

	public void setNotes(String Notes) {
		this.letter.setNotes(Notes);
		this.notes = Notes;
	}

	@Basic
	@Column(name = "ORGANIZATION")
	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.letter.setOrganization(organization);
		this.organization = organization;
	}

	@Basic
	@Column(name = "FOLLOW_UP_ID")
	public Long getFollowUpId() {
		return followUpId;
	}

	public void setFollowUpId(Long followUpId) {
		this.letter.setFollowUpId(followUpId);
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
		this.letterDateString = HijriDateService.getHijriDateString(letterDate);
		this.letter.setLetterDate(letterDate);
	}

	@Transient
	public String getLetterDateString() {
		return letterDateString;
	}

	public void setLetterDateString(String letterDateString) {
		this.letterDateString = letterDateString;
		this.setLetterDate(HijriDateService.getHijriDate(letterDateString));
	}

	@Basic
	@Column(name = "CONTACT_NUMBER")
	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	@Basic
	@Column(name = "FILE_NUMBER")
	public String getFileNumber() {
		return fileNumber;
	}

	public void setFileNumber(String fileNumber) {
		this.fileNumber = fileNumber;
		this.letter.setFileNumber(fileNumber);
	}

	@Transient
	public Letter getLetter() {
		return letter;
	}

	public void setLetter(Letter letter) {
		this.letter = letter;
	}
}
