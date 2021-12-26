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
	 @NamedQuery(name = "infoVisibleDepartment_searchDepartmentData", 
	      	 	 query= " select d " +
	      	 			" from InfoVisibleDepartment infoVisDep, DepartmentData d" +
	      	 			" where (:P_INFO_ID = '-1' or infoVisDep.infoId = :P_INFO_ID ) " +
	      	 			" and ( d.id = infoVisDep.departmentId )" +
	      	 			" order by d.id "
			 	),
	@NamedQuery(name = "infoVisibleDepartment_deleteInfoVisibleDepartment", 
		 	 	query= " delete " +
		 	 		   " from InfoVisibleDepartment infoVisibleDepartment " +
		 	 		   " where (infoVisibleDepartment.infoId = :P_INFO_ID) " 
		 		)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_INFO_VISIBLE_DEPARTMENTS")
public class InfoVisibleDepartment extends AuditEntity implements Serializable, DeleteableAuditEntity, UpdateableAuditEntity, InsertableAuditEntity  {
	private Long id;
	private Long infoId;
	private Long departmentId;

	@Id
	@SequenceGenerator(name = "FIS_INFO_VISIBLE_DEPS_SEQ", sequenceName = "FIS_INFO_VISIBLE_DEPS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_INFO_VISIBLE_DEPS_SEQ")
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	@Column(name = "STP_VW_DEPARTMENTS_ID")
	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	@Override
	public Long calculateContentId() {
		return this.id;
	}
}
