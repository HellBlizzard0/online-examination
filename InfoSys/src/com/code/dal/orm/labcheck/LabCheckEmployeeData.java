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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.code.dal.orm.BaseEntity;
import com.code.exceptions.BusinessException;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.util.HijriDateService;

@NamedQueries({
				@NamedQuery(name = "labCheckEmployeeData_searchLabCheckEmployees", 
							query = " select  labEmp " +
									" from LabCheckEmployeeData labEmp" + 
									" where ( :P_LAB_CHECK_STATUS =-1 or labEmp.labCheckStatus = :P_LAB_CHECK_STATUS )" +
									" and (:P_NOT_LAB_CHECK_STATUS =-1 or labEmp.labCheckStatus != :P_NOT_LAB_CHECK_STATUS ) " + 
									" and (:P_ID = -1 or labEmp.id = :P_ID )" + 
									" and (:P_SOCIAL_ID = '-1' or labEmp.employeeSocialId = :P_SOCIAL_ID )"+ 
									" and (:P_EMPLOYEE_ID = -1 or labEmp.employeeId = :P_EMPLOYEE_ID )" + 
									" and (:P_REGION_ID = -1 or labEmp.employeeRegionId = :P_REGION_ID )" + 
									" and (:P_LAB_CHECK_ID = -1 or labEmp.labCheckId = :P_LAB_CHECK_ID )" + 
									" and (:P_ORDER_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_ORDER_DATE, 'MI/MM/YYYY') = labEmp.orderDate)" + 
									" and (:P_PREVIOUS_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_PREVIOUS_DATE, 'MI/MM/YYYY') > labEmp.orderDate)" + 
									" and (:P_ORDER_NUMBER = '-1' or labEmp.orderNumber LIKE  :P_ORDER_NUMBER )"+
									" and (:P_EXACT_ORDER_NUMBER = '-1' or labEmp.orderNumber = :P_EXACT_ORDER_NUMBER )"+
									" and (:P_SAMPLE_NUMBER = '-1' or labEmp.sampleNumber = :P_SAMPLE_NUMBER )" + 
									" and (:P_CHECK_REASON = -1 or labEmp.checkReason = :P_CHECK_REASON )" + 
									" and (:P_CEHCK_STATUS = -1 or labEmp.checkStatus = :P_CEHCK_STATUS )" + 
									" and (:P_RETEST_NUMBER = -1 or labEmp.retestNumber = :P_RETEST_NUMBER )" + 
									" and (:P_EMPLOYEE_DEPARTMENT_ID = -1 or labEmp.employeeDepartmentId = :P_EMPLOYEE_DEPARTMENT_ID )" + 
									" and (:P_EMPLOYEE_ID = -1 or labEmp.employeeId = :P_EMPLOYEE_ID )" +
							        " order by labEmp.orderDate desc "
				),
				 @NamedQuery(name = "labCheckEmployeeData_searchLabCheckEmployeesToRetest", 
							query = " select  labEmp "  + 
									" from LabCheckEmployeeData labEmp" +
									" where  (:P_CURRENT_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_CURRENT_DATE, 'MI/MM/YYYY') <= labEmp.periodicRetestDate)" +
									" and (:P_END_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_END_DATE, 'MI/MM/YYYY') >= labEmp.periodicRetestDate)" +
									" and (:P_CEHCK_STATUS = -1 or labEmp.checkStatus = :P_CEHCK_STATUS )" +
									" order by labEmp.id "
			 	),
				@NamedQuery(name = "labCheckEmployeeData_searchEmployeeLabCheckBySampleData", 
            				query = " select labEmp.id "  + 
                    				" from LabCheckEmployeeData labEmp " +
                    				" where ( labEmp.sampleNumber = :P_SAMPLE_NUMBER ) " +
                    				" and ( PKG_DATE_TO_CHAR(labEmp.sampleDate,'YYYY') = :P_SAMPLE_YEAR ) "
            	),
				@NamedQuery(name = "labCheckEmployeeData_searchLastLabCheckEmployeesByCheckReason", 
							query = " select labEmp "  + 
        							" from LabCheckEmployeeData labEmp" +
        							" where labEmp.id =( select max(lce.id) from LabCheckEmployeeData lce" +
        							" where ( lce.employeeId = :P_EMPLOYEE_ID ) " +
        							" and ( labEmp.domainNationalForceMaterialTypeId is not null)" +
        							" and ( :P_LAB_CHECK_STATUS = -1 or labEmp.labCheckStatus = :P_LAB_CHECK_STATUS )" +
        							" and ( :P_CHECK_REASON = -1 or lce.checkReason = :P_CHECK_REASON ))" 
				),
				@NamedQuery(name = "labCheckEmployeeData_searchDelayedLabCheckEmployees", 
							query = " select  labEmp " +
        							" from LabCheckEmployeeData labEmp" + 
        							" where ( PKG_CHAR_TO_DATE (:P_ORDER_DATE , 'MI/MM/YYYY') > labEmp.orderDate)" +
        							" and ( :P_LAB_CHECK_STATUS = labEmp.labCheckStatus)" +
        							" and (labEmp.checkStatus is null)" 
				),
				@NamedQuery(name = "labCheckEmployeeData_searchLabChecksAtUnitForEmployees", 
							query = " select  labEmp " +
									" from LabCheckEmployeeData labEmp" + 
									" where ( :P_LAB_CHECK_STATUS =-1 or labEmp.labCheckStatus = :P_LAB_CHECK_STATUS )" +
									" and (:P_EMPLOYEE_ID = -1 or labEmp.employeeId = :P_EMPLOYEE_ID )" + 
									" and (:P_ORDER_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_ORDER_DATE, 'MI/MM/YYYY') = labEmp.orderDate)" + 
									" and (:P_ORDER_NUMBER = '-1' or labEmp.orderNumber LIKE  :P_ORDER_NUMBER )"+
									" and (:P_CHECK_REASON = -1 or labEmp.checkReason = :P_CHECK_REASON )" + 
									" and (:P_CEHCK_STATUS = -1 or labEmp.checkStatus = :P_CEHCK_STATUS )" + 
									" and (labEmp.employeeDepartmentId in (:DEP_CHILDS))" +
							        " order by labEmp.orderDate desc "
	),
						
			})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_LAB_CHECK_EMPLOYEES")
public class LabCheckEmployeeData extends BaseEntity implements Cloneable, Serializable {
	private Long id;
	private Long employeeId;
	private Long labCheckId;
	private Integer checkStatus;
	private String sampleNumber;
	private Date sampleDate;
	private String sampleDateString;
	private Long domainMaterialTypeId;
	private String domainMaterialTypeDescripttion;
	private String nationalForceSampleNumber;
	private Date nationalForceSampleSentDate;
	private String nationalForceSampleSentDateString;
	private Boolean curement;
	private Long domainNationalForceMaterialTypeId;
	private String domainNationalForceMaterialTypeDesc;
	private Long domainCureHospitalId;
	private String domainCureHospitalDesc;
	private String employeeFullName;
	private String employeeSocialId;
	private String employeeMilitaryNumber;
	private String employeeRank;
	private String employeeDepartmentName;
	private Long employeeDepartmentId;
	private Long employeeRegionId;
	private String employeeRegionName;
	private String employeeTitleDesc;
	private String orderNumber;
	private Date orderDate;
	private String orderDateString;
	private Integer checkReason;
	private Integer labCheckStatus;
	private Date periodicRetestDate;
	private String periodicRetestDateString;
	private Integer retestNumber;
	private Boolean retested;
	private Long instanceId;
	private Boolean hcmFlag = false;
	private Long editorEmployeeId;
	private String editorEmployeeName;
	private String editorEmployeeRank;
	private Long editorEmployeeDepId;
	private String editorEmployeeDepName;
	private Date editingDate;
	private String editingDateString;
	private Date resultEnteredDate;
	private String resultEnteredDateString;
	private Long resultEnteredEmpId;
	private String resultEnteredEmpName;
	private Date nationalForceResultEnteredDate;
	private String nationalForceResultEnteredDateString;
	private Long nationalForceResultEnteredEmpId;
	private String nationalForceResultEnteredEmpName;
	private String orderReferenceNumber;
	private String sampleNumberBeforeEdit;
	private Date sampleDateBeforeEdit;
	private String sampleDateStringBeforeEdit;
	private Integer checkStatusBeforeEdit;
	private Long domainMaterialTypeIdBeforeEdit;
	private String domainMaterialTypeDescripttionBeforeEdit;
	private String nationalForceSampleNumberBeforeEdit;
	private Date nationalForceSampleSentDateBeforeEdit;
	private String nationalForceSampleSentDateStringBeforeEdit;
	private Boolean curementBeforeEdit;
	private Long domainNationalForceMaterialTypeIdBeforeEdit;
	private String domainNationalForceMaterialTypeDescBeforeEdit;
	private Long domainCureHospitalIdBeforeEdit;
	private String domainCureHospitalDescBeforeEdit;
	private Date periodicRetestDateBeforeEdit;
	private String periodicRetestDateStringBeforeEdit;
	private String employeeRemarks;
	private String employeeRemarksBeforeEdit;
	private boolean editableFlag;
	private Integer oldCheckStatus;
	private LabCheckEmployee labCheckEmployee;

	public LabCheckEmployeeData() {
		labCheckEmployee = new LabCheckEmployee();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return labCheckEmployee.getId();
	}

	public void setId(Long id) {
		this.labCheckEmployee.setId(id);
		this.id = id;
	}

	@Basic
	@Column(name = "STP_VW_EMPLOYEES_ID")
	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.labCheckEmployee.setEmployeeId(employeeId);
		this.employeeId = employeeId;
	}

	@Basic
	@Column(name = "LAB_CHECKS_ID")
	public Long getLabCheckId() {
		return labCheckId;
	}

	public void setLabCheckId(Long labCheckId) {
		this.labCheckEmployee.setLabCheckId(labCheckId);
		this.labCheckId = labCheckId;
	}

	@Basic
	@Column(name = "CHECK_STATUS")
	public Integer getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Integer checkStatus) {
		if(getOldCheckStatus() == null){
			setOldCheckStatus(this.labCheckEmployee.getCheckStatus());
		}
		this.labCheckEmployee.setCheckStatus(checkStatus);
		this.checkStatus = checkStatus;
	}
	
	@Transient
	public Integer getOldCheckStatus(){
		return oldCheckStatus;
	}
	
	public void setOldCheckStatus(Integer oldCheckStatus){
		this.oldCheckStatus = oldCheckStatus;
	}

	@Basic
	@Column(name = "SAMPLE_NUMBER")
	public String getSampleNumber() {
		return sampleNumber;
	}

	public void setSampleNumber(String sampleNumber) {
		this.labCheckEmployee.setSampleNumber(sampleNumber);
		this.sampleNumber = sampleNumber;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SAMPLE_DATE")
	public Date getSampleDate() {
		return sampleDate;
	}

	public void setSampleDate(Date sampleDate) {
		this.labCheckEmployee.setSampleDate(sampleDate);
		this.sampleDateString = HijriDateService.getHijriDateString(sampleDate);
		this.sampleDate = sampleDate;
	}

	@Transient
	public String getSampleDateString() {
		return sampleDateString;
	}

	@Basic
	@Column(name = "DOMAINS_ID_MATERIAL_TYPE")
	public Long getDomainMaterialTypeId() {
		return domainMaterialTypeId;
	}

	public void setDomainMaterialTypeId(Long domainMaterialTypeId) {
		this.labCheckEmployee.setDomainMaterialTypeId(domainMaterialTypeId);
		this.domainMaterialTypeId = domainMaterialTypeId;
	}

	@Basic
	@Column(name = "NATIONAL_FORCE_SAMPLE_NUMBER")
	public String getNationalForceSampleNumber() {
		return nationalForceSampleNumber;
	}

	public void setNationalForceSampleNumber(String nationalForceSampleNumber) {
		this.labCheckEmployee.setNationalForceSampleNumber(nationalForceSampleNumber);
		this.nationalForceSampleNumber = nationalForceSampleNumber;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "NATNAL_FORCE_SMPLE_SENT_DATE")
	public Date getNationalForceSampleSentDate() {
		return nationalForceSampleSentDate;
	}

	public void setNationalForceSampleSentDate(Date nationalForceSampleSentDate) {
		this.labCheckEmployee.setNationalForceSampleSentDate(nationalForceSampleSentDate);
		this.nationalForceSampleSentDateString = HijriDateService.getHijriDateString(nationalForceSampleSentDate);
		this.nationalForceSampleSentDate = nationalForceSampleSentDate;
	}

	@Transient
	public String getNationalForceSampleSentDateString() {
		return nationalForceSampleSentDateString;
	}

	@Basic
	@Column(name = "CUREMENT")
	public Boolean getCurement() {
		return curement;
	}

	public void setCurement(Boolean curement) {
		this.labCheckEmployee.setCurement(curement);
		this.curement = curement;
	}

	@Basic
	@Column(name = "DOMNS_ID_NATNL_FORC_MTRL_TYP")
	public Long getDomainNationalForceMaterialTypeId() {
		return domainNationalForceMaterialTypeId;
	}

	public void setDomainNationalForceMaterialTypeId(Long domainNationalForceMaterialTypeId) {
		this.labCheckEmployee.setDomainNationalForceMaterialTypeId(domainNationalForceMaterialTypeId);
		this.domainNationalForceMaterialTypeId = domainNationalForceMaterialTypeId;
	}

	@Basic
	@Column(name = "DOMAINS_ID_CURE_HOSPITAL")
	public Long getDomainCureHospitalId() {
		return domainCureHospitalId;
	}

	public void setDomainCureHospitalId(Long domainCureHospitalId) {
		this.labCheckEmployee.setDomainCureHospitalId(domainCureHospitalId);
		this.domainCureHospitalId = domainCureHospitalId;
	}

	@Basic
	@Column(name = "HOSPITAL_NAME")
	public String getDomainCureHospitalDesc() {
		return domainCureHospitalDesc;
	}

	public void setDomainCureHospitalDesc(String domainCureHospitalDesc) {
		this.domainCureHospitalDesc = domainCureHospitalDesc;
	}

	@Basic
	@Column(name = "EMPLOYEE_FULL_NAME")
	public String getEmployeeFullName() {
		return employeeFullName;
	}

	public void setEmployeeFullName(String employeeFullName) {
		this.employeeFullName = employeeFullName;
	}

	@Basic
	@Column(name = "EMPLOYEE_SOCIAL_ID")
	public String getEmployeeSocialId() {
		return employeeSocialId;
	}

	public void setEmployeeSocialId(String employeeSocialId) {
		this.employeeSocialId = employeeSocialId;
	}

	@Basic
	@Column(name = "EMPLOYEE_MILITARY_NO")
	public String getEmployeeMilitaryNumber() {
		return employeeMilitaryNumber;
	}

	public void setEmployeeMilitaryNumber(String employeeMilitaryNumber) {
		this.employeeMilitaryNumber = employeeMilitaryNumber;
	}

	@Basic
	@Column(name = "EMPLOYEE_RANK")
	public String getEmployeeRank() {
		return employeeRank;
	}

	public void setEmployeeRank(String employeeRank) {
		this.employeeRank = employeeRank;
	}

	@Basic
	@Column(name = "EMPLOYEE_DEP_NAME")
	public String getEmployeeDepartmentName() {
		return employeeDepartmentName;
	}

	public void setEmployeeDepartmentName(String employeeDepartmentName) {
		this.employeeDepartmentName = employeeDepartmentName;
	}

	@Basic
	@Column(name = "EMPLOYEE_DEP_ID")
	public Long getEmployeeDepartmentId() {
		return employeeDepartmentId;
	}

	public void setEmployeeDepartmentId(Long employeeDepartmentId) {
		this.employeeDepartmentId = employeeDepartmentId;
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

	public void setOrderDateString(String orderDateString) {
		this.orderDateString = orderDateString;
	}

	@Basic
	@Column(name = "CHECK_REASON")
	public Integer getCheckReason() {
		return checkReason;
	}

	public void setCheckReason(Integer checkReason) {
		this.checkReason = checkReason;
	}

	@Transient
	public LabCheckEmployee getLabCheckEmployee() {
		return labCheckEmployee;
	}

	public void setLabCheckEmployee(LabCheckEmployee labCheckEmployee) {
		this.labCheckEmployee = labCheckEmployee;
	}

	@Basic
	@Column(name = "LAB_CHECK_STATUS")
	public Integer getLabCheckStatus() {
		return labCheckStatus;
	}

	public void setLabCheckStatus(Integer labCheckStatus) {
		this.labCheckStatus = labCheckStatus;
	}

	@Basic
	@Column(name = "DOMNS_MATERIAL_TYPE_DESC")
	public String getDomainMaterialTypeDescripttion() {
		return domainMaterialTypeDescripttion;
	}

	public void setDomainMaterialTypeDescripttion(String domainMaterialTypeDescripttion) {
		this.domainMaterialTypeDescripttion = domainMaterialTypeDescripttion;
	}

	@Basic
	@Column(name = "DOMNS_NTIONAL_MAT_TYPE_DESC")
	public String getDomainNationalForceMaterialTypeDesc() {
		return domainNationalForceMaterialTypeDesc;
	}

	public void setDomainNationalForceMaterialTypeDesc(String domainNationalForceMaterialTypeDesc) {
		this.domainNationalForceMaterialTypeDesc = domainNationalForceMaterialTypeDesc;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PERIODIC_RETEST_DATE")
	public Date getPeriodicRetestDate() {
		return periodicRetestDate;
	}

	public void setPeriodicRetestDate(Date periodicRetestDate) {
		this.labCheckEmployee.setPeriodicRetestDate(periodicRetestDate);
		this.periodicRetestDateString = HijriDateService.getHijriDateString(periodicRetestDate);
		this.periodicRetestDate = periodicRetestDate;
	}

	@Transient
	public String getPeriodicRetestDateString() {
		return periodicRetestDateString;
	}

	public void setPeriodicRetestDateString(String periodicRetestDateString) {
		this.periodicRetestDateString = periodicRetestDateString;
	}

	@Basic
	@Column(name = "EMPLOYEE_REGION_ID")
	public Long getEmployeeRegionId() {
		return employeeRegionId;
	}

	public void setEmployeeRegionId(Long employeeRegionId) {
		this.employeeRegionId = employeeRegionId;
	}

	@Basic
	@Column(name = "EMPLOYEE_REGION_NAME")
	public String getEmployeeRegionName() {
		return employeeRegionName;
	}

	public void setEmployeeRegionName(String employeeRegionName) {
		this.employeeRegionName = employeeRegionName;
	}

	@Basic
	@Column(name = "EMPLOYEE_TITLE_DESCRIPTION")
	public String getEmployeeTitleDesc() {
		return employeeTitleDesc;
	}

	public void setEmployeeTitleDesc(String employeeTitleDesc) {
		this.employeeTitleDesc = employeeTitleDesc;
	}

	@Basic
	@Column(name = "RETEST_NUMBER")
	public Integer getRetestNumber() {
		return retestNumber;
	}

	public void setRetestNumber(Integer retestNumber) {
		this.labCheckEmployee.setRetestNumber(retestNumber);
		this.retestNumber = retestNumber;
	}

	@Basic
	@Column(name = "RETESTED")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getRetested() {
		return retested;
	}

	public void setRetested(Boolean retested) {
		this.labCheckEmployee.setRetested(retested);
		this.retested = retested;
	}

	@Basic
	@Column(name = "WF_INSTANCES_ID")
	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	@Basic
	@Column(name = "EDITOR_EMPLOYEE_ID")
	public Long getEditorEmployeeId() {
		return editorEmployeeId;
	}

	public void setEditorEmployeeId(Long editorEmployeeId) {
		this.labCheckEmployee.setEditorEmployeeId(editorEmployeeId);
		this.editorEmployeeId = editorEmployeeId;
	}

	@Basic
	@Column(name = "EDITOR_EMPLOYEE_NAME")
	public String getEditorEmployeeName() {
		return editorEmployeeName;
	}

	public void setEditorEmployeeName(String editorEmployeeName) {
		this.editorEmployeeName = editorEmployeeName;
	}

	@Basic
	@Column(name = "EDITOR_EMPLOYEE_RANK")
	public String getEditorEmployeeRank() {
		return editorEmployeeRank;
	}

	public void setEditorEmployeeRank(String editorEmployeeRank) {
		this.editorEmployeeRank = editorEmployeeRank;
	}

	@Basic
	@Column(name = "EDITOR_EMPLOYEE_DEP_ID")
	public Long getEditorEmployeeDepId() {
		return editorEmployeeDepId;
	}

	public void setEditorEmployeeDepId(Long editorEmployeeDepId) {
		this.editorEmployeeDepId = editorEmployeeDepId;
	}

	@Basic
	@Column(name = "EDITOR_EMPLOYEE_DEP_NAME")
	public String getEditorEmployeeDepName() {
		return editorEmployeeDepName;
	}

	public void setEditorEmployeeDepName(String editorEmployeeDepName) {
		this.editorEmployeeDepName = editorEmployeeDepName;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EDITING_DATE")
	public Date getEditingDate() {
		return editingDate;
	}

	public void setEditingDate(Date editingDate) {
		this.labCheckEmployee.setEditingDate(editingDate);
		this.editingDateString = HijriDateService.getHijriDateString(editingDate);
		this.editingDate = editingDate;
	}

	@Transient
	public String getEditingDateString() {
		return editingDateString;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RESULT_ENTERED_DATE")
	public Date getResultEnteredDate() {
		return resultEnteredDate;
	}

	public void setResultEnteredDate(Date resultEnteredDate) {
		this.resultEnteredDate = resultEnteredDate;
		this.resultEnteredDateString = HijriDateService.getHijriDateString(resultEnteredDate);
		this.labCheckEmployee.setResultEnteredDate(resultEnteredDate);
	}

	@Transient
	public String getResultEnteredDateString() {
		return resultEnteredDateString;
	}

	public void setResultEnteredDateString(String resultEnteredDateString) {
		this.resultEnteredDateString = resultEnteredDateString;
	}

	@Basic
	@Column(name = "RESULT_ENTERED_EMP_ID")
	public Long getResultEnteredEmpId() {
		return resultEnteredEmpId;
	}

	public void setResultEnteredEmpId(Long resultEnteredEmpId) {
		this.resultEnteredEmpId = resultEnteredEmpId;
		this.labCheckEmployee.setResultEnteredEmpId(resultEnteredEmpId);
	}

	@Basic
	@Column(name = "RESULT_ENTERED_EMP_NAME")
	public String getResultEnteredEmpName() {
		return resultEnteredEmpName;
	}

	public void setResultEnteredEmpName(String resultEnteredEmpName) {
		this.resultEnteredEmpName = resultEnteredEmpName;
	}
	
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "NATNAL_FRC_RESULT_ENTERED_DATE")
	public Date getNationalForceResultEnteredDate() {
		return nationalForceResultEnteredDate;
	}

	public void setNationalForceResultEnteredDate(Date nationalForceResultEnteredDate) {
		this.nationalForceResultEnteredDate = nationalForceResultEnteredDate;
		this.nationalForceResultEnteredDateString = HijriDateService.getHijriDateString(nationalForceResultEnteredDate);
		this.labCheckEmployee.setNationalForceResultEnteredDate(nationalForceResultEnteredDate);
	}

	@Transient
	public String getNationalForceResultEnteredDateString() {
		return nationalForceResultEnteredDateString;
	}

	public void setNationalForceResultEnteredDateString(String nationalForceResultEnteredDateString) {
		this.nationalForceResultEnteredDateString = nationalForceResultEnteredDateString;
	}

	@Basic
	@Column(name = "NATNAL_FRC_RESULT_ENTRD_EMP_ID")
	public Long getNationalForceResultEnteredEmpId() {
		return nationalForceResultEnteredEmpId;
	}

	public void setNationalForceResultEnteredEmpId(Long nationalForceResultEnteredEmpId) {
		this.nationalForceResultEnteredEmpId = nationalForceResultEnteredEmpId;
		this.labCheckEmployee.setNationalForceResultEnteredEmpId(nationalForceResultEnteredEmpId);
	}
	
	
	@Basic
	@Column(name = "NTNL_FRC_RESULT_ENTRD_EMP_NAME")
	public String getNationalForceResultEnteredEmpName() {
		return nationalForceResultEnteredEmpName;
	}

	public void setNationalForceResultEnteredEmpName(String nationalForceResultEnteredEmpName) {
		this.nationalForceResultEnteredEmpName = nationalForceResultEnteredEmpName;
	}

	@Basic
	@Column(name = "ORDER_REFERENCE_NUMBER")
	public String getOrderReferenceNumber() {
		return orderReferenceNumber;
	}

	public void setOrderReferenceNumber(String orderReferenceNumber) {
		this.orderReferenceNumber = orderReferenceNumber;
	}

	@Basic
	@Column(name = "SAMPLE_NUMBER_BEFORE_EDIT")
	public String getSampleNumberBeforeEdit() {
		return sampleNumberBeforeEdit;
	}

	public void setSampleNumberBeforeEdit(String sampleNumberBeforeEdit) {
		this.labCheckEmployee.setSampleNumberBeforeEdit(sampleNumberBeforeEdit);
		this.sampleNumberBeforeEdit = sampleNumberBeforeEdit;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SAMPLE_DATE_BEFORE_EDIT")
	public Date getSampleDateBeforeEdit() {
		return sampleDateBeforeEdit;

	}

	public void setSampleDateBeforeEdit(Date sampleDateBeforeEdit) {
		this.labCheckEmployee.setSampleDateBeforeEdit(sampleDateBeforeEdit);
		this.sampleDateStringBeforeEdit = HijriDateService.getHijriDateString(sampleDateBeforeEdit);
		this.sampleDateBeforeEdit = sampleDateBeforeEdit;
	}

	@Transient
	public String getSampleDateStringBeforeEdit() {
		return sampleDateStringBeforeEdit;
	}

	@Basic
	@Column(name = "CHECK_STATUS_BEFORE_EDIT")
	public Integer getCheckStatusBeforeEdit() {
		return checkStatusBeforeEdit;
	}

	public void setCheckStatusBeforeEdit(Integer checkStatusBeforeEdit) {
		this.labCheckEmployee.setCheckStatusBeforeEdit(checkStatusBeforeEdit);
		this.checkStatusBeforeEdit = checkStatusBeforeEdit;
	}

	@Basic
	@Column(name = "DMNS_ID_MAT_TYPE_BEFORE_EDIT")
	public Long getDomainMaterialTypeIdBeforeEdit() {
		return domainMaterialTypeIdBeforeEdit;
	}

	public void setDomainMaterialTypeIdBeforeEdit(Long domainMaterialTypeIdBeforeEdit) {
		this.labCheckEmployee.setDomainMaterialTypeIdBeforeEdit(domainMaterialTypeIdBeforeEdit);
		this.domainMaterialTypeIdBeforeEdit = domainMaterialTypeIdBeforeEdit;
	}

	@Basic
	@Column(name = "DMNS_MAT_TYPE_DESC_BFOR_EDIT")
	public String getDomainMaterialTypeDescripttionBeforeEdit() {
		return domainMaterialTypeDescripttionBeforeEdit;
	}

	public void setDomainMaterialTypeDescripttionBeforeEdit(String domainMaterialTypeDescripttionBeforeEdit) {
		this.domainMaterialTypeDescripttionBeforeEdit = domainMaterialTypeDescripttionBeforeEdit;
	}

	@Basic
	@Column(name = "NTNAL_FRC_SMPL_NUM_BEFORE_EDIT")
	public String getNationalForceSampleNumberBeforeEdit() {
		return nationalForceSampleNumberBeforeEdit;
	}

	public void setNationalForceSampleNumberBeforeEdit(String nationalForceSampleNumberBeforeEdit) {
		this.labCheckEmployee.setNationalForceSampleNumberBeforeEdit(nationalForceSampleNumberBeforeEdit);
		this.nationalForceSampleNumberBeforeEdit = nationalForceSampleNumberBeforeEdit;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "NTNL_FRC_SMPL_SNT_DT_BFR_EDIT")
	public Date getNationalForceSampleSentDateBeforeEdit() {
		return nationalForceSampleSentDateBeforeEdit;
	}

	public void setNationalForceSampleSentDateBeforeEdit(Date nationalForceSampleSentDateBeforeEdit) {
		this.labCheckEmployee.setNationalForceSampleSentDateBeforeEdit(nationalForceSampleSentDateBeforeEdit);
		this.nationalForceSampleSentDateStringBeforeEdit = HijriDateService.getHijriDateString(nationalForceSampleSentDateBeforeEdit);
		this.nationalForceSampleSentDateBeforeEdit = nationalForceSampleSentDateBeforeEdit;
	}

	@Transient
	public String getNationalForceSampleSentDateStringBeforeEdit() {
		return nationalForceSampleSentDateStringBeforeEdit;
	}

	@Basic
	@Column(name = "CUREMENT_BEFORE_EDIT")
	public Boolean getCurementBeforeEdit() {
		return curementBeforeEdit;
	}

	public void setCurementBeforeEdit(Boolean curementBeforeEdit) {
		this.labCheckEmployee.setCurementBeforeEdit(curementBeforeEdit);
		this.curementBeforeEdit = curementBeforeEdit;
	}

	@Basic
	@Column(name = "DMN_ID_NTNL_FRC_MTRL_BFR_EDT")
	public Long getDomainNationalForceMaterialTypeIdBeforeEdit() {
		return domainNationalForceMaterialTypeIdBeforeEdit;
	}

	public void setDomainNationalForceMaterialTypeIdBeforeEdit(Long domainNationalForceMaterialTypeIdBeforeEdit) {
		this.labCheckEmployee.setDomainNationalForceMaterialTypeIdBeforeEdit(domainNationalForceMaterialTypeIdBeforeEdit);
		this.domainNationalForceMaterialTypeIdBeforeEdit = domainNationalForceMaterialTypeIdBeforeEdit;
	}

	@Basic
	@Column(name = "DMNS_NTNL_MAT_TYP_DSC_BFR_EDT")
	public String getDomainNationalForceMaterialTypeDescBeforeEdit() {
		return domainNationalForceMaterialTypeDescBeforeEdit;
	}

	public void setDomainNationalForceMaterialTypeDescBeforeEdit(String domainNationalForceMaterialTypeDescBeforeEdit) {
		this.domainNationalForceMaterialTypeDescBeforeEdit = domainNationalForceMaterialTypeDescBeforeEdit;
	}

	@Basic
	@Column(name = "DMNS_ID_CURE_HOSPITAL_BFR_EDIT")
	public Long getDomainCureHospitalIdBeforeEdit() {
		return domainCureHospitalIdBeforeEdit;
	}

	public void setDomainCureHospitalIdBeforeEdit(Long domainCureHospitalIdBeforeEdit) {
		this.labCheckEmployee.setDomainCureHospitalIdBeforeEdit(domainCureHospitalIdBeforeEdit);
		this.domainCureHospitalIdBeforeEdit = domainCureHospitalIdBeforeEdit;
	}

	@Basic
	@Column(name = "HOSPITAL_NAME_BEFORE_EDIT")
	public String getDomainCureHospitalDescBeforeEdit() {
		return domainCureHospitalDescBeforeEdit;
	}

	public void setDomainCureHospitalDescBeforeEdit(String domainCureHospitalDescBeforeEdit) {
		this.domainCureHospitalDescBeforeEdit = domainCureHospitalDescBeforeEdit;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PRIODIC_RETST_DATE_BEFORE_EDIT")
	public Date getPeriodicRetestDateBeforeEdit() {
		return periodicRetestDateBeforeEdit;
	}

	public void setPeriodicRetestDateBeforeEdit(Date periodicRetestDateBeforeEdit) {
		this.labCheckEmployee.setPeriodicRetestDateBeforeEdit(periodicRetestDateBeforeEdit);
		this.periodicRetestDateStringBeforeEdit = HijriDateService.getHijriDateString(periodicRetestDateBeforeEdit);
		this.periodicRetestDateBeforeEdit = periodicRetestDateBeforeEdit;
	}
	
	@Basic
	@Column(name = "EMPLOYEE_REMARKS")
	public String getEmployeeRemarks() {
		return employeeRemarks;
	}

	public void setEmployeeRemarks(String employeeRemarks) {
		this.labCheckEmployee.setEmployeeRemarks(employeeRemarks);
		this.employeeRemarks = employeeRemarks;
	}
	
	@Basic
	@Column(name = "EMPLOYEE_REMARKS_BEFORE_EDIT")
	public String getEmployeeRemarksBeforeEdit() {
		return employeeRemarksBeforeEdit;
	}

	public void setEmployeeRemarksBeforeEdit(String employeeRemarksBeforeEdit) {
		this.labCheckEmployee.setEmployeeRemarksBeforeEdit(employeeRemarksBeforeEdit);
		this.employeeRemarksBeforeEdit = employeeRemarksBeforeEdit;
	}

	@Transient
	public String getPeriodicRetestDateStringBeforeEdit() {
		return periodicRetestDateStringBeforeEdit;
	}

	public void setPeriodicRetestDateStringBeforeEdit(String periodicRetestDateStringBeforeEdit) {
		this.periodicRetestDateStringBeforeEdit = periodicRetestDateStringBeforeEdit;
	}

	@Transient
	public boolean isEditableFlag() throws BusinessException {
		if (HijriDateService.addSubHijriMonthsDays(orderDate, InfoSysConfigurationService.getLabCheckEditPeriodId(), 0).compareTo(HijriDateService.getHijriSysDate()) >= 0)
			editableFlag = true;
		else
			editableFlag = false;
		return editableFlag;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	
	@Basic
	@Column(name = "HCM_FLAG")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getHcmFlag() {
		return hcmFlag;
	}

	public void setHcmFlag(Boolean hcmFlag) {
		this.labCheckEmployee.setHcmFlag(hcmFlag);
		this.hcmFlag = hcmFlag;
	}

}
