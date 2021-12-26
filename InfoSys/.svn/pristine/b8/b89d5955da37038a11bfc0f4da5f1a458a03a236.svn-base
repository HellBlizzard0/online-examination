package com.code.dal.orm.securitymission;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;
import com.code.services.util.HijriDateService;

@NamedQueries({
		@NamedQuery(
				name = "nonEmployeePermit_searchNonEmployeePermit",
				query = " select p " +
						" from NonEmployeeData ne , NonEmployeePermit p " +
						" where ne.id = p.nonEmployeeId " +
						" and (:P_SOCIAL_ID = -1 or ne.identity = :P_SOCIAL_ID )" +
						" and (:P_NON_EMPLOYEE_ID = -1 or p.nonEmployeeId = :P_NON_EMPLOYEE_ID )" +
						" order by p.endDate desc"),
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_NON_EMPS_PERMITS")
public class NonEmployeePermit extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity {
	private Long id;
	private Long nonEmployeeId;
	private Date endDate;
	private String endDateString;
	private Long departmentId;
	private Long regionId;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_NON_EMPS_PERMITS_SEQ", sequenceName = "FIS_NON_EMPS_PERMITS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_NON_EMPS_PERMITS_SEQ")
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
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_DATE")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
		this.endDateString = HijriDateService.getHijriDateString(endDate);
	}

	@Transient
	public String getEndDateString() {
		return endDateString;
	}

	@Override
	public Long calculateContentId() {
		return id;
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
	@Column(name = "REGION_ID")
	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

}
