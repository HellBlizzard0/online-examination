package com.code.dal.orm.securitymission;

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

import com.code.dal.orm.BaseEntity;
import com.code.services.util.HijriDateService;

@NamedQueries({ 
	@NamedQuery(name = "penaltyArrestData_searchPenaltyArrestData", 
				query = " select  pen "  + 
						" from PenaltyArrestData pen , WFInstance instance" +
						" where pen.wFInstanceId = instance.instanceId" +
						" and (:P_ID = -1 or pen.id = :P_ID )" +
						" and (:P_ARRESTED_EMPLOYEE_ID = -1 or pen.arrestedEmployeeId = :P_ARRESTED_EMPLOYEE_ID )" +
						" and (:P_ENTRY_FROM_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_ENTRY_FROM_DATE, 'MI/MM/YYYY') <= pen.entryDate)" +
						" and (:P_ENTRY_TO_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_ENTRY_TO_DATE, 'MI/MM/YYYY') >= pen.entryDate)" +
						" and (:P_EXIT_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_EXIT_DATE, 'MI/MM/YYYY') = pen.exitDate)" +
						" and (:P_ARREST_PERIOD = -1 or pen.arrestPeriod = :P_ARREST_PERIOD )" +
						" and (:P_WFINSTANCE_ID = -1 or pen.wFInstanceId = :P_WFINSTANCE_ID )" +
						" and (:P_WFINSTANCE_STATUS = -1 or instance.status = :P_WFINSTANCE_STATUS )" +
						" order by pen.id "),
	@NamedQuery(name = "penaltyArrestData_validatePenaltyArrestData", 
				query = " select  count(pen.id) "  + 
						" from PenaltyArrestData pen" +
						" where pen.arrestedEmployeeId = :P_ARRESTED_EMPLOYEE_ID" +
						" and pen.exitDate is null")
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_PENALTY_ARREST")
public class PenaltyArrestData extends BaseEntity implements Serializable {
	private Long id;
	private Long requesterEmployeeId;
	private String requesterEmployeeName;
	private String requesterEmployeeRank;
	private String requesterEmployeeDepartmentName;
	private Long arrestedEmployeeId;
	private String arrestedEmployeeName;
	private String arrestedEmployeeRank;
	private String arrestedEmployeeDepartmentName;
	private String arrestedEmployeeNumber;
	private String requestNumber;
	private Date requestDate;
	private String requestDateString;
	private Integer arrestPeriod;
	private String arrestLocation;
	private String remarks;
	private Date entryDate;
	private String entryDateString;
	private String entryTime;
	private Date exitDate;
	private String exitDateString;
	private String exitTime;
	private Long wFInstanceId;
	
	private PenaltyArrest penaltyArrest;
	
	public PenaltyArrestData() {
		this.penaltyArrest = new PenaltyArrest();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return penaltyArrest.getId();
	}

	public void setId(Long id) {
		this.penaltyArrest.setId(id);
		this.id = id;
	}

	@Basic
	@Column(name = "STP_EMPLOYEES_ID_REQUESTER")
	public Long getRequesterEmployeeId() {
		return requesterEmployeeId;
	}

	public void setRequesterEmployeeId(Long requesterEmployeeId) {
		this.penaltyArrest.setRequesterEmployeeId(requesterEmployeeId);
		this.requesterEmployeeId = requesterEmployeeId;
	}

	@Basic
	@Column(name = "STP_EMPLOYEES_ID_ARRESTED")
	public Long getArrestedEmployeeId() {
		return arrestedEmployeeId;
	}

	public void setArrestedEmployeeId(Long arrestedEmployeeId) {
		this.penaltyArrest.setArrestedEmployeeId(arrestedEmployeeId);
		this.arrestedEmployeeId = arrestedEmployeeId;
	}

	@Basic
	@Column(name = "REQUEST_NUMBER")
	public String getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(String requestNumber) {
		this.penaltyArrest.setRequestNumber(requestNumber);
		this.requestNumber = requestNumber;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REQUEST_DATE")
	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.penaltyArrest.setRequestDate(requestDate);
		this.requestDate = requestDate;
		this.requestDateString = HijriDateService.getHijriDateString(requestDate);
	}

	@Transient
	public String getRequestDateString() {
		return requestDateString;
	}

	@Basic
	@Column(name = "ARREST_PERIOD")
	public Integer getArrestPeriod() {
		return arrestPeriod;
	}

	public void setArrestPeriod(Integer arrestPeriod) {
		this.penaltyArrest.setArrestPeriod(arrestPeriod);
		this.arrestPeriod = arrestPeriod;
	}

	@Basic
	@Column(name = "ARREST_LOCATION")
	public String getArrestLocation() {
		return arrestLocation;
	}

	public void setArrestLocation(String arrestLocation) {
		this.penaltyArrest.setArrestLocation(arrestLocation);
		this.arrestLocation = arrestLocation;
	}

	@Basic
	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.penaltyArrest.setRemarks(remarks);
		this.remarks = remarks;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENTRY_DATE")
	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.penaltyArrest.setEntryDate(entryDate);
		this.entryDate = entryDate;
		this.entryDateString = HijriDateService.getHijriDateString(entryDate);
	}

	@Transient
	public String getEntryDateString() {
		return entryDateString;
	}

	@Basic
	@Column(name = "ENTRY_TIME")
	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.penaltyArrest.setEntryTime(entryTime);
		this.entryTime = entryTime;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXIT_DATE")
	public Date getExitDate() {
		return exitDate;
	}

	public void setExitDate(Date exitDate) {
		this.penaltyArrest.setExitDate(exitDate);
		this.exitDate = exitDate;
		this.exitDateString = HijriDateService.getHijriDateString(exitDate);
	}

	@Transient
	public String getExitDateString() {
		return exitDateString;
	}

	@Basic
	@Column(name = "EXIT_TIME")
	public String getExitTime() {
		return exitTime;
	}

	public void setExitTime(String exitTime) {
		this.penaltyArrest.setExitTime(exitTime);
		this.exitTime = exitTime;
	}

	@Basic
	@Column(name = "WF_INSTANCES_ID")
	public Long getwFInstanceId() {
		return wFInstanceId;
	}

	public void setwFInstanceId(Long wFInstanceId) {
		this.penaltyArrest.setwFInstanceId(wFInstanceId);
		this.wFInstanceId = wFInstanceId;
	}

	@Basic
	@Column(name = "REQUESTER_EMPLOYEE_NAME")
	public String getRequesterEmployeeName() {
		return requesterEmployeeName;
	}

	public void setRequesterEmployeeName(String requesterEmployeeName) {
		this.requesterEmployeeName = requesterEmployeeName;
	}

	@Basic
	@Column(name = "REQUESTER_EMPLOYEE_RANK")
	public String getRequesterEmployeeRank() {
		return requesterEmployeeRank;
	}

	public void setRequesterEmployeeRank(String requesterEmployeeRank) {
		this.requesterEmployeeRank = requesterEmployeeRank;
	}

	@Basic
	@Column(name = "REQUESTER_EMPLOYEE_DEP_NAME")
	public String getRequesterEmployeeDepartmentName() {
		return requesterEmployeeDepartmentName;
	}

	public void setRequesterEmployeeDepartmentName(String requesterEmployeeDepartmentName) {
		this.requesterEmployeeDepartmentName = requesterEmployeeDepartmentName;
	}

	@Basic
	@Column(name = "ARRESTED_EMPLOYEE_NAME")
	public String getArrestedEmployeeName() {
		return arrestedEmployeeName;
	}

	public void setArrestedEmployeeName(String arrestedEmployeeName) {
		this.arrestedEmployeeName = arrestedEmployeeName;
	}

	@Basic
	@Column(name = "ARRESTED_EMPLOYEE_RANK")
	public String getArrestedEmployeeRank() {
		return arrestedEmployeeRank;
	}

	public void setArrestedEmployeeRank(String arrestedEmployeeRank) {
		this.arrestedEmployeeRank = arrestedEmployeeRank;
	}

	@Basic
	@Column(name = "ARRESTED_EMPLOYEE_DEP_NAME")
	public String getArrestedEmployeeDepartmentName() {
		return arrestedEmployeeDepartmentName;
	}

	public void setArrestedEmployeeDepartmentName(String arrestedEmployeeDepartmentName) {
		this.arrestedEmployeeDepartmentName = arrestedEmployeeDepartmentName;
	}

	@Basic
	@Column(name = "ARRESTED_EMPLOYEE_NUMBER")
	public String getArrestedEmployeeNumber() {
		return arrestedEmployeeNumber;
	}

	public void setArrestedEmployeeNumber(String arrestedEmployeeNumber) {
		this.arrestedEmployeeNumber = arrestedEmployeeNumber;
	}

	@Transient
	public PenaltyArrest getPenaltyArrest() {
		return penaltyArrest;
	}

	public void setPenaltyArrest(PenaltyArrest penaltyArrest) {
		this.penaltyArrest = penaltyArrest;
	}

}
