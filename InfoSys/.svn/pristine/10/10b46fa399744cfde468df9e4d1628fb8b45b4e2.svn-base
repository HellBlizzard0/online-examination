package com.code.dal.orm.setup;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.code.dal.orm.BaseEntity;

@NamedQueries({
	 @NamedQuery(name = "qualificationData_searchQualification", 
	           	 query= " select q " +
	           	 		" from QualificationData q" +
	           			" order by q.id "
	)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "STP_VW_QUALIFICATIONS")
public class QualificationData extends BaseEntity implements Serializable {
	private Long id;
	private String code;
	private String description;
	private String latinDesc;

	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Basic
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Basic
	@Column(name = "LATIN_DESC")
	public String getLatinDesc() {
		return latinDesc;
	}

	public void setLatinDesc(String latinDesc) {
		this.latinDesc = latinDesc;
	}

}
