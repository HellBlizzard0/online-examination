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
	 @NamedQuery(name = "infoSource_searchInfoSource", 
          	 query= " select infoS " +
          	 		" from InfoSource infoS " +
          	 		" where (:P_INFO_ID = -1 or infoS.infoId = :P_INFO_ID ) " +
          	 		" and (:P_ASSIGNMENT_DETAIL_ID = -1 or infoS.assignmentDetailId = :P_ASSIGNMENT_DETAIL_ID) " +
          			" and (:P_OPEN_SOURCE_ID = -1 or infoS.openSourceId = :P_OPEN_SOURCE_ID) " +
          			" order by infoS.id "
			 ),
	@NamedQuery(name = "infoSource_deleteInfoSource", 
	 	 	 query= " delete " +
	 	 			" from InfoSource infoSource " +
	 	 			" where (infoSource.infoId = :P_INFO_ID) " 
	 		)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_INFO_SOURCES")
public class InfoSource extends AuditEntity implements Serializable, InsertableAuditEntity, DeleteableAuditEntity, UpdateableAuditEntity{
	private Long id;
	private Long assignmentDetailId;
	private Long openSourceId;
	private Long infoId;
	private Integer sourceType;
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_INFO_SOURCE_SEQ", sequenceName = "FIS_INFO_SOURCE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_INFO_SOURCE_SEQ")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Basic
	@Column(name = "ASSIGNMENT_DETAILS_ID")
	public Long getAssignmentDetailId() {
		return assignmentDetailId;
	}
	
	public void setAssignmentDetailId(Long assignmentDetailId) {
		this.assignmentDetailId = assignmentDetailId;
	}

	@Basic
	@Column(name = "OPEN_SOURCES_ID")
	public Long getOpenSourceId() {
		return openSourceId;
	}
	
	public void setOpenSourceId(Long openSourceId) {
		this.openSourceId = openSourceId;
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
	@Column(name = "SOURCE_TYPE")
	public Integer getSourceType() {
		return sourceType;
	}
	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}
	
	@Override
	public Long calculateContentId() {
		return this.id;
	}
}
