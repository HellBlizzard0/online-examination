package com.code.dal.orm.securitymission;

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
	 @NamedQuery(name  = "permitsPatchDetails_searchPermitsPatchDetails", 
			 	 query = " select  d " + 
	           	 		 " from PermitsPatchDetails d" +
	           			 " where (:P_PATCH_ID = -1 or d.patchId = :P_PATCH_ID )" +
	           			 " order by d.rowNumber"
			 	),
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_PERMITS_PATCH_DETAILS")
public class PermitsPatchDetails extends AuditEntity implements Serializable, InsertableAuditEntity, DeleteableAuditEntity, UpdateableAuditEntity {
	private Long id;
	private Long patchId;
	private String field1;
	private String field2;
	private String field3;
	private String field4;
	private String field5;
	private String field6;
	private String field7;
	private String field8;
	private String field9;
	private String errorDesc;
	private Integer rowNumber;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_PERMITS_PATCH_DETAILS_SEQ", sequenceName = "FIS_PERMITS_PATCH_DETAILS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_PERMITS_PATCH_DETAILS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "PATCH_ID")
	public Long getPatchId() {
		return patchId;
	}

	public void setPatchId(Long patchId) {
		this.patchId = patchId;
	}

	@Basic
	@Column(name = "FIELD_1")
	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	@Basic
	@Column(name = "FIELD_2")
	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	@Basic
	@Column(name = "FIELD_3")
	public String getField3() {
		return field3;
	}

	public void setField3(String field3) {
		this.field3 = field3;
	}

	@Basic
	@Column(name = "FIELD_4")
	public String getField4() {
		return field4;
	}

	public void setField4(String field4) {
		this.field4 = field4;
	}

	@Basic
	@Column(name = "FIELD_5")
	public String getField5() {
		return field5;
	}

	public void setField5(String field5) {
		this.field5 = field5;
	}

	@Basic
	@Column(name = "FIELD_6")
	public String getField6() {
		return field6;
	}

	public void setField6(String field6) {
		this.field6 = field6;
	}

	@Basic
	@Column(name = "FIELD_7")
	public String getField7() {
		return field7;
	}

	public void setField7(String field7) {
		this.field7 = field7;
	}

	@Basic
	@Column(name = "FIELD_8")
	public String getField8() {
		return field8;
	}

	public void setField8(String field8) {
		this.field8 = field8;
	}

	@Basic
	@Column(name = "FIELD_9")
	public String getField9() {
		return field9;
	}

	public void setField9(String field9) {
		this.field9 = field9;
	}

	@Basic
	@Column(name = "ERROR_DESC")
	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	@Override
	public Long calculateContentId() {
		return this.id;
	}

	@Basic
	@Column(name = "ROW_NUMBER")
	public Integer getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
	}
}
