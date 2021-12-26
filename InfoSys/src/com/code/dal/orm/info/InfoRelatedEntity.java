package com.code.dal.orm.info;

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
	 @NamedQuery(name = "infoRelatedEntity_searchInfoRelatedEntities", 
         	 query= " select infoR " +
         	 		" from InfoRelatedEntity infoR " +
         			" where ( :P_INFO_ID = -1 or infoR.infoId = :P_INFO_ID ) " +
         			" and ( :P_NON_EMPLOYEE_ID = -1 or infoR.nonEmployeeId = :P_NON_EMPLOYEE_ID ) " +
         			" and ( :P_EMPLOYEE_ID = -1 or infoR.employeeId = :P_EMPLOYEE_ID ) " +
         			" and ( :P_COUNTRY_ID = -1 or infoR.countryId = :P_COUNTRY_ID ) " +
         			" and ( :P_DEPARTMENT_ID = -1 or infoR.departmentId = :P_DEPARTMENT_ID ) " +
         			" and ( :P_DOMAIN_ID = -1 or infoR.domainId = :P_DOMAIN_ID ) " +
         			" order by infoR.id "
			 ),
	 @NamedQuery(name = "infoRelatedEntity_deleteInfoRelatedEntities", 
 	 		query= 	" delete " +
 	 				" from InfoRelatedEntity infoRelatedEntity " +
 	 				" where (infoRelatedEntity.infoId = :P_INFO_ID) " 
 			)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_INFO_RELATED_ENTITIES")
public class InfoRelatedEntity extends AuditEntity implements Serializable, DeleteableAuditEntity, InsertableAuditEntity, UpdateableAuditEntity {
	private Long id;
	private Long nonEmployeeId;
	private Long employeeId;
	private Long countryId;
	private Long departmentId;
	private Long domainId;
	private Long infoId;
	private Integer entityType;

	@Id
	@SequenceGenerator(name = "FIS_INFO_RELATED_ENTITIES_SEQ", sequenceName = "FIS_INFO_RELATED_ENTITIES_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_INFO_RELATED_ENTITIES_SEQ")
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "NON_EMPLOYEES_ID")
	public Long getNonEmployeeId() {
		return nonEmployeeId;
	}

	public void setNonEmployeeId(Long nonEmployeeId) {
		this.nonEmployeeId = nonEmployeeId;
	}

	@Basic
	@Column(name = "STP_VW_EMPLOYEES_ID")
	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	@Basic
	@Column(name = "STP_VW_COUNTIRES_ID")
	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	@Basic
	@Column(name = "STP_VW_DEPARTMENTS_ID")
	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	@Basic
	@Column(name = "DOMAINS_ID")
	public Long getDomainId() {
		return domainId;
	}

	public void setDomainId(Long domainId) {
		this.domainId = domainId;
	}

	@Basic
	@Column(name = "INFORMATION_ID")
	public Long getInfoId() {
		return infoId;
	}

	public void setInfoId(Long infoId) {
		this.infoId = infoId;
	}

	@Basic
	@Column(name = "ENTITY_TYPE")
	public Integer getEntityType() {
		return entityType;
	}

	public void setEntityType(Integer entityType) {
		this.entityType = entityType;
	}

	@Override
	public Long calculateContentId() {
		return this.id;
	}
}
