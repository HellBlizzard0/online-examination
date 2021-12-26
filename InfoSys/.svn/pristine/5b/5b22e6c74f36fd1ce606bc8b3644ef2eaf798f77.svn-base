package com.code.dal.orm.labcheck;

import java.io.Serializable;
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
	 @NamedQuery(name  = "labCheckReport_searchlabCheckReport", 
			 	query = " select l " +
             	 		" from LabCheckReportData l" +
             	 		" where (:P_ID = -1 or l.id = :P_ID )" +
             			" and (:P_DESTINATION_DEPARTMENT_ID = -1 or l.destinationDepartmentId = :P_DESTINATION_DEPARTMENT_ID )" +
             			" and (:P_MISSION_TYPE = -1 or l.missionTypeId = :P_MISSION_TYPE )" +
             			" and (:P_MISSION_START_DATE_NULL = 1 or PKG_CHAR_TO_DATE(:P_MISSION_START_DATE, 'MI/MM/YYYY') <= l.startDate)" + 
             			" and (:P_MISSION_END_DATE_NULL = 1 or PKG_CHAR_TO_DATE(:P_MISSION_END_DATE, 'MI/MM/YYYY') >= l.endDate)" + 
             			" order by l.startDate desc"
			 	),
	@NamedQuery(name  = "labCheckReportData_countLabCheckReportsPerOrderNumber", 
				query = " select count (l.id) " +
	             	 	" from LabCheckReportData l" +
	             	 	" where (:P_LAB_CHECK_ORDER_NUMBER = '-1' or l.orderNumber = :P_LAB_CHECK_ORDER_NUMBER )" 
				 	)
})
@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_LAB_CHECK_REPORTS")
public class LabCheckReportData extends BaseEntity implements Serializable{
	private Long id;
	private String orderNumber;
	private Long labChecksId;
	private Long destinationDepartmentId;
	private Date startDate;
	private String startDateString;
	private Date endDate;
	private String endDateString;
	private Integer period;
	private String remarks;
	private Long missionTypeId;
	private String missionTypeDescription;
	private Integer checkedNumber;
	private Date checkStartDate;
	private Date checkEndDate;
	private String checkStartDateString;
	private String checkEndDateString;
	private String pros;
	private String cons;
	
	private LabCheckReport labCheckReport;
	
	public LabCheckReportData(){
		this.labCheckReport = new LabCheckReport();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return labCheckReport.getId();
	}

	public void setId(Long id) {
		this.id = id;
		this.labCheckReport.setId(id);
	}

	@Basic
	@Column(name = "ORDER_NUMBER")
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
		this.labCheckReport.setOrderNumber(orderNumber);
	}

	@Basic
	@Column(name = "LAB_CHECKS_ID")
	public Long getLabChecksId() {
		return labChecksId;
	}

	public void setLabChecksId(Long labChecksId) {
		this.labChecksId = labChecksId;
		this.labCheckReport.setLabChecksId(labChecksId);
	}

	@Basic
	@Column(name = "STP_VW_DEPARTMENTS_ID_DST")
	public Long getDestinationDepartmentId() {
		return destinationDepartmentId;
	}

	public void setDestinationDepartmentId(Long destinationDepartmentId) {
		this.destinationDepartmentId = destinationDepartmentId;
		this.labCheckReport.setDestinationDepartmentId(destinationDepartmentId);
	}

	@Basic
	@Column(name = "START_DATE")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
		this.labCheckReport.setStartDate(startDate);
		this.startDateString = HijriDateService.getHijriDateString(startDate);
	}

	@Transient
	public String getStartDateString() {
		return startDateString;
	}

	@Basic
	@Column(name = "END_DATE")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
		this.labCheckReport.setEndDate(endDate);
		this.endDateString = HijriDateService.getHijriDateString(endDate);
	}

	@Transient
	public String getEndDateString() {
		return endDateString;
	}

	@Basic
	@Column(name = "PERIOD")
	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
		this.labCheckReport.setPeriod(period);
	}

	@Basic
	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
		this.labCheckReport.setRemarks(remarks);
	}

	@Basic
	@Column(name = "MISSION_TYPE")
	public Long getMissionTypeId() {
		return missionTypeId;
	}

	public void setMissionTypeId(Long missionTypeId) {
		this.missionTypeId = missionTypeId;
		this.labCheckReport.setMissionTypeId(missionTypeId);
	}

	@Basic
	@Column(name = "MISSION_TYPE_DESCRIPTION")
	public String getMissionTypeDescription() {
		return missionTypeDescription;
	}

	public void setMissionTypeDescription(String missionTypeDescription) {
		this.missionTypeDescription = missionTypeDescription;
	}

	@Basic
	@Column(name = "CHECKED_NUMBER")
	public Integer getCheckedNumber() {
		return checkedNumber;
	}

	public void setCheckedNumber(Integer checkedNumber) {
		this.checkedNumber = checkedNumber;
		this.labCheckReport.setCheckedNumber(checkedNumber);
	}

	@Basic
	@Column(name = "CHECK_START_DATE")
	public Date getCheckStartDate() {
		return checkStartDate;
	}

	public void setCheckStartDate(Date checkStartDate) {
		this.checkStartDate = checkStartDate;
		this.labCheckReport.setCheckStartDate(checkStartDate);
		this.startDateString = HijriDateService.getHijriDateString(checkStartDate);
	}

	@Transient
	public String getCheckStartDateString() {
		return checkStartDateString;
	}

	@Basic
	@Column(name = "CHECK_END_DATE")
	public Date getCheckEndDate() {
		return checkEndDate;
	}

	public void setCheckEndDate(Date checkEndDate) {
		this.checkEndDate = checkEndDate;
		this.labCheckReport.setCheckEndDate(checkEndDate);
		this.checkEndDateString = HijriDateService.getHijriDateString(checkEndDate);
	}

	@Transient
	public String getCheckEndDateString() {
		return checkEndDateString;
	}

	@Basic
	@Column(name = "PROS")
	public String getPros() {
		return pros;
	}

	public void setPros(String pros) {
		this.pros = pros;
		this.labCheckReport.setPros(pros);
	}

	@Basic
	@Column(name = "CONS")
	public String getCons() {
		return cons;
	}

	public void setCons(String cons) {
		this.cons = cons;
		this.labCheckReport.setCons(cons);
	}

	@Transient
	public LabCheckReport getLabCheckReport() {
		return labCheckReport;
	}

	public void setLabCheckReport(LabCheckReport labCheckReport) {
		this.labCheckReport = labCheckReport;
	}
}
