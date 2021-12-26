package com.code.dal.orm.labcheck;

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

import org.hibernate.annotations.Type;

import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;
import com.code.services.util.HijriDateService;



@NamedQueries({
		@NamedQuery(name = "labCheckEmployeeData_countLabCheckEmployees", 
				   query = " select  count(labEmp.id) "  + 
						   " from LabCheckEmployee labEmp" +
						   " where (:P_LAB_CHECK_ID = -1 or labEmp.labCheckId = :P_LAB_CHECK_ID )" +
						   " and   (:P_EMPLOYEE_ID = -1 or labEmp.employeeId = :P_EMPLOYEE_ID )"
	   ),
		@NamedQuery(name = "labCheckEmployeeData_checkEmployeeActionStatus", 
				   query = " select count(labEmp.id) "  + 
						   " from LabCheckEmployee labEmp" +
						   " where (:P_EMPLOYEE_ID = -1 or labEmp.employeeId = :P_EMPLOYEE_ID )" +
						   " and (labEmp.labCheckId != :P_LAB_CHECK_ID )" +
						   " and (labEmp.checkStatus is null or labEmp.checkStatus in (:P_STATUS)) "
	   ),
		@NamedQuery(name = "labCheckEmployeeData_validateEmployeeLabCheck", 
				   query = " select count(labEmp.id) "  + 
						   " from LabCheckEmployee labEmp" +
						   " where (:P_EMPLOYEE_ID = -1 or labEmp.employeeId = :P_EMPLOYEE_ID )" +
						   " and ( labEmp.retestNumber = (select max(emp.retestNumber) from LabCheckEmployee emp where (:P_EMPLOYEE_ID = -1 or emp.employeeId = :P_EMPLOYEE_ID )))" +
						   " and (labEmp.checkStatus is null or labEmp.checkStatus not  in (:P_STATUS)) "+
						   " and (labEmp.labCheckId != :P_LAB_CHECK_ID )"
	   ),
		@NamedQuery(name = "labCheckEmployeeData_validateSampleNumber", 
					query = " select count(labEmp.id) "  + 
							" from LabCheckEmployee labEmp " +
							" where ( labEmp.sampleNumber = :P_SAMPLE_NUMBER ) " +
							" and ( PKG_DATE_TO_CHAR(labEmp.sampleDate,'YYYY') = :P_SAMPLE_YEAR ) "
		),
		@NamedQuery(name = "labCheckEmployeeData_countLabCheckEmployeesWithinPeriod", 
					query = " select count(labEmp.id) "  + 
							" from LabCheckEmployee labEmp" +
							" where (labEmp.employeeId = :P_EMPLOYEE_ID )" +
							" and (labEmp.checkStatus in (:P_STATUS)) "+
							" and ( PKG_CHAR_TO_DATE (:P_ORDER_DATE, 'MI/MM/YYYY') <= labEmp.periodicRetestDate)" +
							" and (labEmp.labCheckId != :P_LAB_CHECK_ID )"
		),
				
	})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_LAB_CHECK_EMPLOYEES")
public class LabCheckEmployee extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity {
	private Long id;
	private Long employeeId;
	private Long labCheckId;
	private Integer checkStatus;
	private String sampleNumber;
	private Date sampleDate;
	private String sampleDateString;
	private Long domainMaterialTypeId;
	private String nationalForceSampleNumber;
	private Date nationalForceSampleSentDate;
	private String nationalForceSampleSentDateString;
	private Boolean curement;
	private Long domainNationalForceMaterialTypeId;
	private Long domainCureHospitalId;
	private Date periodicRetestDate;
	private String periodicRetestDateString;
	private Integer retestNumber;
	private Boolean retested;
	private Boolean hcmFlag = false;
	private Long editorEmployeeId;
	private Date editingDate;
	private Date resultEnteredDate;
	private String resultEnteredDateString;
	private Long resultEnteredEmpId;
	private Date nationalForceResultEnteredDate;
	private String nationalForceResultEnteredDateString;
	private Long nationalForceResultEnteredEmpId;
	private String sampleNumberBeforeEdit;
	private Date sampleDateBeforeEdit;
	private String sampleDateStringBeforeEdit;
	private Integer checkStatusBeforeEdit;
	private Long domainMaterialTypeIdBeforeEdit;
	private String nationalForceSampleNumberBeforeEdit;
	private Date nationalForceSampleSentDateBeforeEdit;
	private String nationalForceSampleSentDateStringBeforeEdit;
	private Boolean curementBeforeEdit;
	private Long domainNationalForceMaterialTypeIdBeforeEdit;
	private Long domainCureHospitalIdBeforeEdit;
	private Date periodicRetestDateBeforeEdit;
	private String periodicRetestDateStringBeforeEdit;
	private String employeeRemarks;
	private String employeeRemarksBeforeEdit;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_LAB_CHECK_EMPLOYEES_SEQ", sequenceName = "FIS_LAB_CHECK_EMPLOYEES_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_LAB_CHECK_EMPLOYEES_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "STP_VW_EMPLOYEES_ID")
	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	@Basic
	@Column(name = "LAB_CHECKS_ID")
	public Long getLabCheckId() {
		return labCheckId;
	}

	public void setLabCheckId(Long labCheckId) {
		this.labCheckId = labCheckId;
	}

	@Basic
	@Column(name = "CHECK_STATUS")
	public Integer getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}

	@Basic
	@Column(name = "SAMPLE_NUMBER")
	public String getSampleNumber() {
		return sampleNumber;
	}

	public void setSampleNumber(String sampleNumber) {
		this.sampleNumber = sampleNumber;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SAMPLE_DATE")
	public Date getSampleDate() {
		return sampleDate;
	}

	public void setSampleDate(Date sampleDate) {
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
		this.domainMaterialTypeId = domainMaterialTypeId;
	}

	@Basic
	@Column(name = "NATIONAL_FORCE_SAMPLE_NUMBER")
	public String getNationalForceSampleNumber() {
		return nationalForceSampleNumber;
	}

	public void setNationalForceSampleNumber(String nationalForceSampleNumber) {
		this.nationalForceSampleNumber = nationalForceSampleNumber;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "NATNAL_FORCE_SMPLE_SENT_DATE")
	public Date getNationalForceSampleSentDate() {
		return nationalForceSampleSentDate;
	}

	public void setNationalForceSampleSentDate(Date nationalForceSampleSentDate) {
		this.nationalForceSampleSentDateString = HijriDateService.getHijriDateString(nationalForceSampleSentDate);
		this.nationalForceSampleSentDate = nationalForceSampleSentDate;
	}

	@Transient
	public String getNationalForceSampleSentDateString() {
		return nationalForceSampleSentDateString;
	}

	@Basic
	@Column(name = "CUREMENT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getCurement() {
		return curement;
	}

	public void setCurement(Boolean curement) {
		this.curement = curement;
	}

	@Basic
	@Column(name = "DOMNS_ID_NATNL_FORC_MTRL_TYP")
	public Long getDomainNationalForceMaterialTypeId() {
		return domainNationalForceMaterialTypeId;
	}

	public void setDomainNationalForceMaterialTypeId(Long domainNationalForceMaterialTypeId) {
		this.domainNationalForceMaterialTypeId = domainNationalForceMaterialTypeId;
	}

	@Basic
	@Column(name = "DOMAINS_ID_CURE_HOSPITAL")
	public Long getDomainCureHospitalId() {
		return domainCureHospitalId;
	}

	public void setDomainCureHospitalId(Long domainCureHospitalId) {
		this.domainCureHospitalId = domainCureHospitalId;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PERIODIC_RETEST_DATE")
	public Date getPeriodicRetestDate() {
		return periodicRetestDate;
	}

	public void setPeriodicRetestDate(Date periodicRetestDate) {
		this.periodicRetestDateString = HijriDateService.getHijriDateString(periodicRetestDate);
		this.periodicRetestDate = periodicRetestDate;
	}

	@Basic
	@Column(name = "RETEST_NUMBER")
	public Integer getRetestNumber() {
		return retestNumber;
	}

	public void setRetestNumber(Integer retestNumber) {
		this.retestNumber = retestNumber;
	}

	@Basic
	@Column(name = "RETESTED")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getRetested() {
		return retested;
	}

	public void setRetested(Boolean retested) {
		this.retested = retested;
	}

	@Transient
	public String getPeriodicRetestDateString() {
		return periodicRetestDateString;
	}

	public void setPeriodicRetestDateString(String periodicRetestDateString) {
		this.periodicRetestDateString = periodicRetestDateString;
	}

	@Basic
	@Column(name = "EDITOR_EMPLOYEE_ID")
	public Long getEditorEmployeeId() {
		return editorEmployeeId;
	}

	public void setEditorEmployeeId(Long editorEmployeeId) {
		this.editorEmployeeId = editorEmployeeId;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EDITING_DATE")
	public Date getEditingDate() {
		return editingDate;
	}

	public void setEditingDate(Date editingDate) {
		this.editingDate = editingDate;
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
	}

	@Basic
	@Column(name = "SAMPLE_NUMBER_BEFORE_EDIT")
	public String getSampleNumberBeforeEdit() {
		return sampleNumberBeforeEdit;
	}

	public void setSampleNumberBeforeEdit(String sampleNumberBeforeEdit) {
		this.sampleNumberBeforeEdit = sampleNumberBeforeEdit;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SAMPLE_DATE_BEFORE_EDIT")
	public Date getSampleDateBeforeEdit() {
		return sampleDateBeforeEdit;
	}

	public void setSampleDateBeforeEdit(Date sampleDateBeforeEdit) {
		this.sampleDateStringBeforeEdit = HijriDateService.getHijriDateString(sampleDateBeforeEdit);
		this.sampleDateBeforeEdit = sampleDateBeforeEdit;
	}

	@Basic
	@Column(name = "CHECK_STATUS_BEFORE_EDIT")
	public Integer getCheckStatusBeforeEdit() {
		return checkStatusBeforeEdit;
	}

	public void setCheckStatusBeforeEdit(Integer checkStatusBeforeEdit) {
		this.checkStatusBeforeEdit = checkStatusBeforeEdit;
	}

	@Basic
	@Column(name = "DMNS_ID_MAT_TYPE_BEFORE_EDIT")
	public Long getDomainMaterialTypeIdBeforeEdit() {
		return domainMaterialTypeIdBeforeEdit;
	}

	public void setDomainMaterialTypeIdBeforeEdit(Long domainMaterialTypeIdBeforeEdit) {
		this.domainMaterialTypeIdBeforeEdit = domainMaterialTypeIdBeforeEdit;
	}

	@Basic
	@Column(name = "NTNAL_FRC_SMPL_NUM_BEFORE_EDIT")
	public String getNationalForceSampleNumberBeforeEdit() {
		return nationalForceSampleNumberBeforeEdit;
	}

	public void setNationalForceSampleNumberBeforeEdit(String nationalForceSampleNumberBeforeEdit) {
		this.nationalForceSampleNumberBeforeEdit = nationalForceSampleNumberBeforeEdit;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "NTNL_FRC_SMPL_SNT_DT_BFR_EDIT")
	public Date getNationalForceSampleSentDateBeforeEdit() {
		return nationalForceSampleSentDateBeforeEdit;
	}

	public void setNationalForceSampleSentDateBeforeEdit(Date nationalForceSampleSentDateBeforeEdit) {
		this.nationalForceSampleSentDateStringBeforeEdit = HijriDateService.getHijriDateString(nationalForceSampleSentDateBeforeEdit);
		this.nationalForceSampleSentDateBeforeEdit = nationalForceSampleSentDateBeforeEdit;
	}

	@Basic
	@Column(name = "CUREMENT_BEFORE_EDIT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getCurementBeforeEdit() {
		return curementBeforeEdit;
	}

	public void setCurementBeforeEdit(Boolean curementBeforeEdit) {
		this.curementBeforeEdit = curementBeforeEdit;
	}

	@Basic
	@Column(name = "DMN_ID_NTNL_FRC_MTRL_BFR_EDT")
	public Long getDomainNationalForceMaterialTypeIdBeforeEdit() {
		return domainNationalForceMaterialTypeIdBeforeEdit;
	}

	public void setDomainNationalForceMaterialTypeIdBeforeEdit(Long domainNationalForceMaterialTypeIdBeforeEdit) {
		this.domainNationalForceMaterialTypeIdBeforeEdit = domainNationalForceMaterialTypeIdBeforeEdit;
	}

	@Basic
	@Column(name = "DMNS_ID_CURE_HOSPITAL_BFR_EDIT")
	public Long getDomainCureHospitalIdBeforeEdit() {
		return domainCureHospitalIdBeforeEdit;
	}

	public void setDomainCureHospitalIdBeforeEdit(Long domainCureHospitalIdBeforeEdit) {
		this.domainCureHospitalIdBeforeEdit = domainCureHospitalIdBeforeEdit;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PRIODIC_RETST_DATE_BEFORE_EDIT")
	public Date getPeriodicRetestDateBeforeEdit() {
		return periodicRetestDateBeforeEdit;
	}

	public void setPeriodicRetestDateBeforeEdit(Date periodicRetestDateBeforeEdit) {
		this.periodicRetestDateStringBeforeEdit = HijriDateService.getHijriDateString(periodicRetestDateBeforeEdit);
		this.periodicRetestDateBeforeEdit = periodicRetestDateBeforeEdit;
	}
	
	@Basic
	@Column(name = "EMPLOYEE_REMARKS")
	public String getEmployeeRemarks() {
		return employeeRemarks;
	}

	public void setEmployeeRemarks(String employeeRemarks) {
		this.employeeRemarks = employeeRemarks;
	}
	
	@Basic
	@Column(name = "EMPLOYEE_REMARKS_BEFORE_EDIT")
	public String getEmployeeRemarksBeforeEdit() {
		return employeeRemarksBeforeEdit;
	}

	public void setEmployeeRemarksBeforeEdit(String employeeRemarksBeforeEdit) {
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
	public String getSampleDateStringBeforeEdit() {
		return sampleDateStringBeforeEdit;
	}

	@Transient
	public String getNationalForceSampleSentDateStringBeforeEdit() {
		return nationalForceSampleSentDateStringBeforeEdit;
	}

	@Override
	public Long calculateContentId() {
		return this.id;
	}

	@Basic
	@Column(name = "HCM_FLAG")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getHcmFlag() {
		return hcmFlag;
	}

	public void setHcmFlag(Boolean hcmFlag) {
		this.hcmFlag = hcmFlag;
	}
}