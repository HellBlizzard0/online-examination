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
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.code.dal.audit.DeleteableAuditEntity;
import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;

@NamedQueries({
	   @NamedQuery(name ="SecurityStatusUser_searchSecurityStatusUsers", 
				   query= "select ssu from SecurityStatusUser ssu where ( :P_KEY = '-1' or key = :P_KEY ) "
				   		+ "and (:P_TYPE = -1 or type = :P_TYPE) "
						+ "order by ssu.id")
	  }
)

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_SECURITY_STATUS_USERS")
public class SecurityStatusUser extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity,Cloneable {
	private Long id;
	private String key;
	private Boolean systemFlag;
	private String fullName;
	private String actualDepartmentName;
	private String socialId;
	private Integer type;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_SECURITY_STATUS_USERS_SEQ", sequenceName = "FIS_SECURITY_STATUS_USERS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_SECURITY_STATUS_USERS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Basic
	@Column(name = "USER_KEY")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Basic
	@Column(name = "SYSTEM_FLAG")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getSystemFlag() {
		return systemFlag;
	}

	public void setSystemFlag(Boolean systemFlag) {
		this.systemFlag = systemFlag;
	}

	@Transient
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Transient
	public String getActualDepartmentName() {
		return actualDepartmentName;
	}

	public void setActualDepartmentName(String actualDepartmentName) {
		this.actualDepartmentName = actualDepartmentName;
	}

	@Transient
	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

	@Basic
	@Column(name = "TYPE")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public Long calculateContentId() {
		return this.id;
	}
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
