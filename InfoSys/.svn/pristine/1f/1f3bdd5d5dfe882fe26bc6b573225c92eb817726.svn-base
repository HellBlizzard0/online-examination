package com.code.dal.orm.surveillance;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	@NamedQuery(name = "SurveillanceEmpNonEmpData_searchSurveillanceEmpNonEmpData", 
				query = " select sed " + 
						" from SurveillanceEmpNonEmpData sed" + 
						" where (:P_WF_INSTANCE_STATUS = -1 or sed.wFInstanceStatus = :P_WF_INSTANCE_STATUS)" +
						" and (:P_SUR_EMPLOYEE_ID = -1 or sed.id = :P_SUR_EMPLOYEE_ID )" +
						" and (:P_ORDER_ID = -1 or sed.surveillanceOrderId = :P_ORDER_ID )" + 
						" and (:P_ORDER_NUMBER = '-1' or sed.orderNumber = :P_ORDER_NUMBER )" + 
						" and (:P_ORDER_DATE_NULL = 1 or sed.orderDate = PKG_CHAR_TO_DATE (:P_ORDER_DATE, 'MI/MM/YYYY'))" +
						" and (:P_FINAL_APPROVAL_ENTITY = '-1' or sed.finalApprovalEntity = :P_FINAL_APPROVAL_ENTITY )" + 
						" and (:P_EMPLOYEE_ID = -1 or sed.employeeId = :P_EMPLOYEE_ID )" + 
						" and (:P_NON_EMP_ID = -1 or sed.nonEmployeeId = :P_NON_EMP_ID )" + 
						" and (:P_ORDER_REGION_ID = -1 or sed.orderRegionId = :P_ORDER_REGION_ID )" +
						" and (:P_REGION_ID = -1 or sed.regionId = :P_REGION_ID)" + 
						" and (:P_SOCIAL_ID = '-1' or sed.socialId = :P_SOCIAL_ID )" + 
						" and (:P_SURVEILLANCE_STATUS = -1 " + 
						" 		or (:P_SURVEILLANCE_STATUS = 1 and sed.endDate >= PKG_CHAR_TO_DATE (:P_CURRENT_DATE, 'MI/MM/YYYY') )" + 
						" 		or (:P_SURVEILLANCE_STATUS = 0 and sed.endDate < PKG_CHAR_TO_DATE (:P_CURRENT_DATE, 'MI/MM/YYYY') ))" + 
						" and (:P_LATE_FLAG = -1 or sed.id in ( select sr.surveillanceEmpId " + 
						" 										from SurveillanceReport sr " + 
						"										where sr.endDate < PKG_CHAR_TO_DATE (:P_CURRENT_DATE, 'MI/MM/YYYY')" + 
						" 										and sr.approved = 0 )" + 
						"	  )" +
						" order by sed.orderDate desc"
				),
	@NamedQuery(name = "SurveillanceEmpNonEmpData_countSurveillanceEmpNonEmpData", 
				query = " select count(sed.id) " + 
						" from SurveillanceEmpNonEmpData sed" + 
						" where (:P_WF_INSTANCE_STATUS = -1 or sed.wFInstanceStatus = :P_WF_INSTANCE_STATUS)" +
						" and (:P_EMPLOYEE_ID = -1 or sed.employeeId = :P_EMPLOYEE_ID )"
				),
	@NamedQuery(name = "SurveillanceEmpNonEmpData_getActiveSurveillanceEmpNonEmpData", 
            	query = " select sed " + 
            			" from SurveillanceEmpNonEmpData sed" + 
            			" where (:P_EMPLOYEE_ID = -1 or sed.employeeId = :P_EMPLOYEE_ID )" +
            			" and   (:P_NON_EMP_ID = -1 or sed.nonEmployeeId = :P_NON_EMP_ID )" +
            			" and   (( sed.wFInstanceStatus <> :P_WF_INSTANCE_STATUS )" +
            			" and   (  sed.id NOT IN ( select sr.surveillanceEmpId" + 
            			"					       from SurveillanceReport sr" +
            			"				           where sr.surveillanceEmpId = sed.id" +
            			" 				           and sr.recommendationDecisionType = :P_RECOMMEND_DECISION_TYPE" +
            			"				           and sr.approved = 1) ) )"
            	)
})
@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_SURVEILLANC_EMP_NON_EMP")
public class SurveillanceEmpNonEmpData extends BaseEntity implements Serializable {
	private Long id;
	private Long surveillanceOrderId;
	private Date startDate;
	private String startDateString;
	private Date endDate;
	private String endDateString;
	private Integer periodMonths;
	private Long employeeId;
	private Long nonEmployeeId;
	private String employeeFullName;
	private String militaryNumber;
	private String socialId;
	private String rank;
	private Date orderDate;
	private String orderDateString;
	private String orderNumber;
	private String finalApprovalEntity;
	private Long orderRegionId;
	private String orderRegionName;
	private Integer periodicReporting;
	private String surveillanceReasons;
	private Long regionId;
	private String regionName;
	private Long wFInstanceId;
	private Integer wFInstanceStatus;
	private SurveillanceEmpNonEmp surveillanceEmployee;
	private List<String> surveillanceOrderReasonsList;
	private String actualDepartmentName;
	private Date birthDate;
	private String birthDateString;
	private Date birthDateGreg;
	private String birthDateGregString;
	private Integer yaqeenStatus;
	private boolean workflowFlag;

	public SurveillanceEmpNonEmpData() {
		this.surveillanceEmployee = new SurveillanceEmpNonEmp();
		this.setSurveillanceOrderReasonsList(new ArrayList<String>());
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return surveillanceEmployee.getId();
	}

	public void setId(Long id) {
		this.surveillanceEmployee.setId(id);
		this.id = id;
	}

	@Basic
	@Column(name = "FIS_SURVEILLANCE_ORDERS_ID")
	public Long getSurveillanceOrderId() {
		return surveillanceOrderId;
	}

	public void setSurveillanceOrderId(Long surveillanceOrderId) {
		this.surveillanceEmployee.setSurveillanceOrderId(surveillanceOrderId);
		this.surveillanceOrderId = surveillanceOrderId;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDateString = HijriDateService.getHijriDateString(startDate);
		this.startDate = startDate;
	}

	@Transient
	public String getStartDateString() {
		return startDateString;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_DATE")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDateString = HijriDateService.getHijriDateString(endDate);
		this.surveillanceEmployee.setEndDate(endDate);
		this.endDate = endDate;
	}

	@Transient
	public String getEndDateString() {
		return endDateString;
	}

	@Basic
	@Column(name = "PERIOD_MONTHS")
	public Integer getPeriodMonths() {
		return periodMonths;
	}

	public void setPeriodMonths(Integer periodMonths) {
		this.surveillanceEmployee.setPeriodMonths(periodMonths);
		this.periodMonths = periodMonths;
	}

	@Basic
	@Column(name = "STP_VW_EMPLOYEES_ID")
	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.surveillanceEmployee.setEmployeeId(employeeId);
		this.employeeId = employeeId;
	}

	@Basic
	@Column(name = "FULL_NAME")
	public String getEmployeeFullName() {
		return employeeFullName;
	}

	public void setEmployeeFullName(String employeeFullName) {
		this.employeeFullName = employeeFullName;
	}

	@Basic
	@Column(name = "MILITARY_NO")
	public String getMilitaryNumber() {
		return militaryNumber;
	}

	public void setMilitaryNumber(String militaryNumber) {
		this.militaryNumber = militaryNumber;
	}

	@Basic
	@Column(name = "SOCIAL_ID")
	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

	@Basic
	@Column(name = "RANK")
	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ORDER_DATE")
	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDateString = HijriDateService.getHijriDateString(orderDate);
		this.orderDate = orderDate;
	}

	@Transient
	public String getOrderDateString() {
		return orderDateString;
	}

	@Basic
	@Column(name = "ORDER_NUMBER")
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	@Basic
	@Column(name = "FINAL_APPROVAL_ENTITY")
	public String getFinalApprovalEntity() {
		return finalApprovalEntity;
	}

	public void setFinalApprovalEntity(String finalApprovalEntity) {
		this.finalApprovalEntity = finalApprovalEntity;
	}

	@Basic
	@Column(name = "PERIODIC_REPORTING")
	public Integer getPeriodicReporting() {
		return periodicReporting;
	}

	public void setPeriodicReporting(Integer periodicReporting) {
		this.periodicReporting = periodicReporting;
	}

	@Transient
	public SurveillanceEmpNonEmp getSurveillanceEmployee() {
		return surveillanceEmployee;
	}

	public void setSurveillanceEmployee(SurveillanceEmpNonEmp surveillanceEmployee) {
		this.surveillanceEmployee = surveillanceEmployee;
	}

	@Basic
	@Column(name = "SURVEILLANCE_REASONS")
	public String getSurveillanceReasons() {
		return surveillanceReasons;
	}

	public void setSurveillanceReasons(String surveillanceReasons) {
		this.surveillanceReasons = surveillanceReasons;
		this.surveillanceEmployee.setSurveillanceReasons(surveillanceReasons);
	}

	@Basic
	@Column(name = "REGION_ID")
	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	@Basic
	@Column(name = "REGION_NAME")
	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	@Transient
	public List<String> getSurveillanceOrderReasonsList() {
		return surveillanceOrderReasonsList;
	}

	public void setSurveillanceOrderReasonsList(List<String> surveillanceOrderReasonsList) {
		this.surveillanceReasons = "";

		for (int i = 0; i < surveillanceOrderReasonsList.size(); i++) {
			if (i != 0) {
				this.surveillanceReasons = surveillanceReasons.concat(",");
			}
			this.surveillanceReasons += surveillanceOrderReasonsList.get(i);
			setSurveillanceReasons(this.surveillanceReasons);
		}
		this.surveillanceOrderReasonsList = surveillanceOrderReasonsList;
	}

	@Basic
	@Column(name = "WF_INSTANCES_ID")
	public Long getwFInstanceId() {
		return wFInstanceId;
	}

	public void setwFInstanceId(Long wFInstanceId) {
		this.wFInstanceId = wFInstanceId;
	}

	@Basic
	@Column(name = "WF_INSTANCES_STATUS")
	public Integer getwFInstanceStatus() {
		return wFInstanceStatus;
	}

	public void setwFInstanceStatus(Integer wFInstanceStatus) {
		this.wFInstanceStatus = wFInstanceStatus;
	}

	@Basic
	@Column(name = "ACTUAL_DEPARTMENT_NAME")
	public String getActualDepartmentName() {
		return actualDepartmentName;
	}

	public void setActualDepartmentName(String actualDepartmentName) {
		this.actualDepartmentName = actualDepartmentName;
	}

	@Basic
	@Column(name = "BIRTH_DATE")
	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDateString = HijriDateService.getHijriDateString(birthDate);
		this.birthDate = birthDate;
	}

	@Transient
	public String getBirthDateString() {
		return birthDateString;
	}

	@Basic
	@Column(name = "ORDER_REGION_ID")
	public Long getOrderRegionId() {
		return orderRegionId;
	}

	public void setOrderRegionId(Long orderRegionId) {
		this.orderRegionId = orderRegionId;
	}

	@Basic
	@Column(name = "ORDER_REGION_NAME")
	public String getOrderRegionName() {
		return orderRegionName;
	}

	public void setOrderRegionName(String orderRegionName) {
		this.orderRegionName = orderRegionName;
	}

	@Basic
	@Column(name = "NON_EMPLOYEES_ID")
	public Long getNonEmployeeId() {
		return nonEmployeeId;
	}

	public void setNonEmployeeId(Long nonEmployeeId) {
		surveillanceEmployee.setNonEmployeeId(nonEmployeeId);
		this.nonEmployeeId = nonEmployeeId;
	}
	
	@Basic
	@Column(name = "BIRTH_DATE_GREG")
	public Date getBirthDateGreg() {
		return birthDateGreg;
	}

	public void setBirthDateGreg(Date birthDateGreg) {
		this.birthDateGreg = birthDateGreg;
		this.birthDateGregString = ((birthDateGreg == null) ? null : new SimpleDateFormat("dd/MM/yyyy").format(birthDateGreg));
	}
	
	@Transient
	public String getBirthDateGregString() {
		return birthDateGregString;
	}
	
	public void setBirthDateGregString(String birthDateGregString) {
		this.birthDateGregString = birthDateGregString;
	}

	@Basic
	@Column(name = "YAQEEN_STATUS")
	public Integer getYaqeenStatus() {
		return yaqeenStatus;
	}

	public void setYaqeenStatus(Integer yaqeenStatus) {
		this.surveillanceEmployee.setYaqeenStatus(yaqeenStatus);
		this.yaqeenStatus = yaqeenStatus;
	}

	@Transient
	public boolean isWorkflowFlag() {
		return workflowFlag;
	}

	public void setWorkflowFlag(boolean workflowFlag) {
		this.workflowFlag = workflowFlag;
	}
}