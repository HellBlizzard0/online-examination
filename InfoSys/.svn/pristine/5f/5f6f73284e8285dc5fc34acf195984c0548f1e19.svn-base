package com.code.dal.orm.setup;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.code.dal.audit.DeleteableAuditEntity;
import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;


@NamedQueries({
	  @NamedQuery(name = "infoSys_getConfigurations", 
	              query= " select c from InfoSysConfig c where" +
	              		 " (c.code = :P_CODE or :P_CODE = '-1')" +
	              		 " and (c.value = :P_VALUE or :P_VALUE = '-1')" + 
	              		 " and (c.comment like :P_COMMENT or :P_COMMENT = '-1')"
    )
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_CONFIG")
public class InfoSysConfig extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private String code;
	private String value;
	private String comment;

	public void setId(Long Id) {
		this.id = Id;
	}

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_CONFIG_SEQ", sequenceName = "FIS_CONFIG_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_CONFIG_SEQ")
	public Long getId() {
		return id;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Basic
	@Column(name = "CODE")
	public String getCode() {
		return code;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Basic
	@Column(name = "VALUE")
	public String getValue() {
		return value;
	}

	@Basic
	@Column(name = "COMMENTS")
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public Long calculateContentId() {
		return this.id;
	}
}