package com.code.dal.orm.setup;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
	  @NamedQuery(name = "elmIntegConfigData_getElmConfigurations", 
	              query= " select c from ElmIntegConfigData c where" +
	              		 " (c.code = :P_CODE or :P_CODE = '-1')" +
	              		 " and (c.value = :P_VALUE or :P_VALUE = '-1')" + 
	              		 " and (c.comment like :P_COMMENT or :P_COMMENT = '-1')"
  )
})

@SuppressWarnings("serial")
@Entity
@Table(name = "ELM_VW_CONFIGS")
public class ElmIntegConfigData {
	private Long id;
	private String code;
	private String value;
	private String comment;

	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long Id) {
		this.id = Id;
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
	@Column(name = "VALUE")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Basic
	@Column(name = "COMMENTS")
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
