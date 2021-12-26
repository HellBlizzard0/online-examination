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
	 @NamedQuery(name = "infoPhone_searchInfoPhones", 
       	 	 query= " select infoPhone " +
       	 			" from InfoPhone infoPhone " +
       	 			" where (:P_INFO_ID = -1 or infoPhone.infoId = :P_INFO_ID) " +
       	 			" order by infoPhone.id "
       	 			),
	 @NamedQuery(name = "infoPhone_deleteInfoPhones", 
 	 		query= 	" delete " +
 	 				" from InfoPhone infoPhone " +
 	 				" where (infoPhone.infoId = :P_INFO_ID) " 
 					)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_INFO_PHONES")
public class InfoPhone extends AuditEntity implements Serializable, DeleteableAuditEntity, InsertableAuditEntity, UpdateableAuditEntity {
	private Long id;
	private String phoneNumber;
	private String phoneOwner;
	private Long infoId;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_INFO_PHONES_SEQ", sequenceName = "FIS_INFO_PHONES_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_INFO_PHONES_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "PHONE_NUMBER")
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Basic
	@Column(name = "PHONE_OWNER")
	public String getPhoneOwner() {
		return phoneOwner;
	}

	public void setPhoneOwner(String phoneOwner) {
		this.phoneOwner = phoneOwner;
	}

	@Basic
	@Column(name = "INFORMATION_ID")
	public Long getInfoId() {
		return infoId;
	}

	public void setInfoId(Long infoId) {
		this.infoId = infoId;
	}

	@Override
	public Long calculateContentId() {
		return this.id;
	}
}
