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

import org.hibernate.annotations.Type;

import com.code.dal.audit.DeleteableAuditEntity;
import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;

@NamedQueries({
	 @NamedQuery(name = "setupClass_searchClass", 
             	 query= " select s " +
             	 		" from SetupClass s " +
             			" where (:P_CODE = '-1' or upper( s.code ) like UPPER(:P_CODE) )" +
                    	" and (:P_TYPE = -1 or s.type like :P_TYPE )" +
                    	" and (:P_DESCRIPTION = '-1' or s.description like :P_DESCRIPTION )" +
                    	" and (:P_SECURITY_ANALYSIS_FLAG = -1 or s.securityAnalysisFlag= :P_SECURITY_ANALYSIS_FLAG)" +
                    	" order by s.id "
    )
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_CLASSES")
public class SetupClass extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity{
	private Long id;
	private String code;
	private Integer type;
	private String description;
	private Boolean securityAnalysisFlag;

	
	public void setId(Long id) {
		this.id = id;
	}

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_CLASSES_SEQ", sequenceName = "FIS_CLASSES_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_CLASSES_SEQ")
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

	public void setDescription(String description) {
		this.description = description;
	}

	@Basic
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Basic
	@Column(name = "TYPE")
	public Integer getType() {
		return type;
	}

	@Override
	public Long calculateContentId() {
		return this.id;
	}
	
	@Basic
	@Column(name = "SECURITY_ANALYSIS_FLAG")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getSecurityAnalysisFlag() {
		return securityAnalysisFlag;
	}

	public void setSecurityAnalysisFlag(Boolean securityAnalysisFlag) {
		this.securityAnalysisFlag = securityAnalysisFlag;
	}
}