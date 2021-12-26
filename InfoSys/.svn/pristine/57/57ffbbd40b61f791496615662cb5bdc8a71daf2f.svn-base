package com.code.dal.orm.securitymission;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.code.dal.orm.BaseEntity;
import com.code.services.util.HijriDateService;

@NamedQueries({
		@NamedQuery(
				name = "visitorEntranceData_searchVisitorEntranceData",
				query = " select ve " +
						" from VisitorEntranceData ve " +
						" where (:P_CURRENT_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_CURRENT_DATE, 'MI/MM/YYYY') = ve.entryDate" +
						" 								   or PKG_CHAR_TO_DATE (:P_CURRENT_DATE, 'MI/MM/YYYY') = ve.exitDate)" +
						" and   (:P_VISITOR_CARD_NUMBER = '-1' or ve.visitorCardNumber = :P_VISITOR_CARD_NUMBER )" +
						" and   (:P_VISITOR_IDENTITY = -1 or ve.visitorIdentity = :P_VISITOR_IDENTITY )" +
						" and   (:P_VISITOR_NAME = '-1' or ve.visitorName = :P_VISITOR_NAME )" +
						" and   (:P_DEPARTMENT_ID = -1 or ve.departmentId = :P_DEPARTMENT_ID )" +
						" and   (:P_EMPLOYEE_ID = -1 or ve.employeeId = :P_EMPLOYEE_ID )" +
						" and   (:P_REGION_ID = -1 or ve.regionId = :P_REGION_ID )" +
						" and	((:P_ENTRY_EXIT_FLAG <> 0 and ve.entryDate is not null" +
						"								and (:P_FROM_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_FROM_DATE, 'MI/MM/YYYY') <= ve.entryDate)" +
						" 								and (:P_TO_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_TO_DATE, 'MI/MM/YYYY') >= ve.entryDate)" +
						" 								and (:P_FROM_TIME = '-1' or :P_FROM_TIME <= ve.entryTime)" +
						" 								and (:P_TO_TIME = '-1' or :P_TO_TIME >= ve.entryTime))" +
						" 		or	(:P_ENTRY_EXIT_FLAG <> 1 and ve.exitDate is not null" +
						"								and (:P_FROM_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_FROM_DATE, 'MI/MM/YYYY') <= ve.exitDate)" +
						" 								and (:P_TO_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_TO_DATE, 'MI/MM/YYYY') >= ve.exitDate)" +
						" 								and (:P_FROM_TIME = '-1' or :P_FROM_TIME <= ve.exitTime)" +
						" 								and (:P_TO_TIME = '-1' or :P_TO_TIME >= ve.exitTime)))" +
						" and (:P_EXIT_FLAG = -1 or ve.exitDate is null)" +
						" order by ve.entryDate desc, ve.entryTime desc, ve.exitDate desc, ve.exitTime desc")
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_VISITORS_ENTRANCES")
public class VisitorEntranceData extends BaseEntity {
	private Long id;
	private Long visitorId;
	private String visitorCardNumber;
	private Long departmentId;
	private Long employeeId;
	private Date entryDate;
	private String entryDateString;
	private Date exitDate;
	private String exitDateString;
	private String entryTime;
	private String exitTime;

	private String visitorName;
	private String visitorCountryName;
	private Long visitorIdentity;
	private String departmentName;
	private VisitorEntrance visitorEntrance;
	private Long regionId;

	public VisitorEntranceData() {
		visitorEntrance = new VisitorEntrance();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return visitorEntrance.getId();
	}

	public void setId(Long id) {
		this.id = id;
		visitorEntrance.setId(id);
	}

	@Basic
	@Column(name = "VISITOR_NON_EMPLOYEES_ID")
	public Long getVisitorId() {
		return visitorId;
	}

	public void setVisitorId(Long visitorId) {
		this.visitorId = visitorId;
		visitorEntrance.setVisitorId(visitorId);
	}

	@Basic
	@Column(name = "VISITOR_CARD_NUMBER")
	public String getVisitorCardNumber() {
		return visitorCardNumber;
	}

	public void setVisitorCardNumber(String visitorCardNumber) {
		this.visitorCardNumber = visitorCardNumber;
		visitorEntrance.setVisitorCardNumber(visitorCardNumber);
	}

	@Basic
	@Column(name = "VISITED_STP_VW_DEPARTMENTS_ID")
	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
		visitorEntrance.setDepartmentId(departmentId);
	}

	@Basic
	@Column(name = "VISITED_STP_VW_EMPLOYEES_ID")
	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
		visitorEntrance.setEmployeeId(employeeId);
	}

	@Basic
	@Column(name = "ENTRY_DATE")
	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
		this.entryDateString = HijriDateService.getHijriDateString(entryDate);
		this.visitorEntrance.setEntryDate(entryDate);
	}

	@Transient
	public String getEntryDateString() {
		return entryDateString;
	}

	@Basic
	@Column(name = "EXIT_DATE")
	public Date getExitDate() {
		return exitDate;
	}

	public void setExitDate(Date exitDate) {
		this.exitDate = exitDate;
		this.exitDateString = HijriDateService.getHijriDateString(exitDate);
		this.visitorEntrance.setExitDate(exitDate);
	}

	@Transient
	public String getExitDateString() {
		return exitDateString;
	}

	@Basic
	@Column(name = "ENTRY_TIME")
	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
		this.visitorEntrance.setEntryTime(entryTime);
	}

	@Basic
	@Column(name = "EXIT_TIME")
	public String getExitTime() {
		return exitTime;
	}

	public void setExitTime(String exitTime) {
		this.exitTime = exitTime;
		this.visitorEntrance.setExitTime(exitTime);
	}

	@Basic
	@Column(name = "VISITOR_NAME")
	public String getVisitorName() {
		return visitorName;
	}

	public void setVisitorName(String visitorName) {
		this.visitorName = visitorName;
	}

	@Basic
	@Column(name = "VISITOR_COUNTRY_NAME")
	public String getVisitorCountryName() {
		return visitorCountryName;
	}

	public void setVisitorCountryName(String visitorCountryName) {
		this.visitorCountryName = visitorCountryName;
	}

	@Basic
	@Column(name = "VISITOR_IDENTITY")
	public Long getVisitorIdentity() {
		return visitorIdentity;
	}

	public void setVisitorIdentity(Long visitorIdentity) {
		this.visitorIdentity = visitorIdentity;
	}

	@Basic
	@Column(name = "VISITED_DEPARTMENT_NAME")
	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	@Transient
	public VisitorEntrance getVisitorEntrance() {
		return visitorEntrance;
	}

	public void setVisitorEntrance(VisitorEntrance visitorEntrance) {
		this.visitorEntrance = visitorEntrance;
	}

	@Basic
	@Column(name = "REGION_ID")
	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.visitorEntrance.setRegionId(regionId);
		this.regionId = regionId;
	}

}