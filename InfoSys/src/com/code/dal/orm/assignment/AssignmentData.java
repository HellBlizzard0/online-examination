package com.code.dal.orm.assignment;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.code.dal.orm.AuditEntity;
import com.code.services.util.HijriDateService;

@NamedQueries({
	@NamedQuery(name = "assignmentData_searchAssignments", 
	            query= " select a " +
	               	   " from AssignmentData a " +
	               	   " where (:P_ID = -1 or a.id = :P_ID) " +
	               	   " and (:P_INSTANCE_ID = -1 or a.wFInstanceId = :P_INSTANCE_ID) " +
	               	   " and (:P_OFFICER_ID = -1 or a.officerId = :P_OFFICER_ID ) " +
	               	   " and (:P_HIJRI_START_DATE_NULL = 1 or a.startDate = PKG_CHAR_TO_DATE (:P_HIJRI_START_DATE, 'MI/MM/YYYY') ) " +
	               	   " order by a.id "
	)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_ASSIGNMENTS")
public class AssignmentData extends AuditEntity implements Serializable {
	private Long id;
	private Long regionId;
	private String regionName;
	private Long sectorId;
	private String sectorName;
	private Long unitId;
	private String unitName;
	private Date startDate;
	private String startDateString;
	private Integer period;
	private String requestNumber;
	private Date requestDate;
	private String requestDateString;
	private Long officerId;
	private String officerName;
	private Date endDate;
	private String endDateString;
	private Long wFInstanceId;
	private Assignment assignment;

	public AssignmentData() {
		assignment = new Assignment();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return assignment.getId();
	}

	public void setId(Long id) {
		assignment.setId(id);
		this.id = id;
	}

	@Basic
	@Column(name = "STP_REGION_ID")
	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		assignment.setRegionId(regionId);
		this.regionId = regionId;
	}

	@Basic
	@Column(name = "STP_REGION_NAME")
	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		assignment.setRegionName(regionName);
		this.regionName = regionName;
	}

	@Basic
	@Column(name = "STP_SECTOR_ID")
	public Long getSectorId() {
		return sectorId;
	}

	public void setSectorId(Long sectorId) {
		assignment.setSectorId(sectorId);
		this.sectorId = sectorId;
	}

	@Basic
	@Column(name = "STP_SECTOR_NAME")
	public String getSectorName() {
		return sectorName;
	}

	public void setSectorName(String sectorName) {
		assignment.setSectorName(sectorName);
		this.sectorName = sectorName;
	}
	

	@Basic
	@Column(name = "STP_UNIT_ID")
	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		assignment.setUnitId(unitId);
		this.unitId = unitId;
	}

	@Basic
	@Column(name = "STP_UNIT_NAME")
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		assignment.setStartDate(startDate);
		this.startDateString = HijriDateService.getHijriDateString(startDate);
		this.startDate = startDate;
	}

	@Transient
	public String getStartDateString() {
		return startDateString;
	}

	@Basic
	@Column(name = "PERIOD")
	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		assignment.setPeriod(period);
		this.period = period;
	}

	@Basic
	@Column(name = "REQUEST_NUMBER")
	public String getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(String requestNumber) {
		assignment.setRequestNumber(requestNumber);
		this.requestNumber = requestNumber;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REQUEST_DATE")
	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		assignment.setRequestDate(requestDate);
		this.requestDateString = HijriDateService.getHijriDateString(requestDate);
		this.requestDate = requestDate;
	}

	@Transient
	public String getRequestDateString() {
		return requestDateString;
	}

	@Basic
	@Column(name = "STP_VW_EMPLOYEES_ID_OFFICER")
	public Long getOfficerId() {
		return officerId;
	}

	public void setOfficerId(Long officerId) {
		assignment.setOfficerId(officerId);
		this.officerId = officerId;
	}

	@Basic
	@Column(name = "STP_VW_EMPLOYEES_NAME_OFFICER")
	public String getOfficerName() {
		return officerName;
	}

	public void setOfficerName(String officerName) {
		this.officerName = officerName;
	}

	@Transient
	// TODO @Basic
	// @Column(name = "END_DATE")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDateString = HijriDateService.getHijriDateString(endDate);
		this.endDate = endDate;
	}

	@Transient
	public String getEndDateString() {
		return endDateString;
	}

	@Basic
	@Column(name = "WF_INSTANCES_ID")
	public Long getwFInstanceId() {
		return wFInstanceId;
	}

	public void setwFInstanceId(Long wFInstanceId) {
		assignment.setwFInstanceId(wFInstanceId);
		this.wFInstanceId = wFInstanceId;
	}

	@Override
	public Long calculateContentId() {
		return this.id;
	}

	@Transient
	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}
}
