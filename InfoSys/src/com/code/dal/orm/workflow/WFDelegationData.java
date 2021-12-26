package com.code.dal.orm.workflow;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.code.dal.orm.BaseEntity;

@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name = "wf_delegationData_getWFDelegationData",
				query = " select d from WFDelegationData d" +
						" where (:P_ID = -1 or d.id = :P_ID) " +
						" and (:P_EMP_ID = -1 or d.empId = :P_EMP_ID) " +
						" and (:P_DELEGATE_ID = -1 or d.delegateId = :P_DELEGATE_ID) " +
						" and ((:P_PROCESS_ID_FLAG = -1 and d.processId is null) or (:P_PROCESS_ID_FLAG = 1 and d.processId is not null)) " +
						" order by d.empId, d.delegateId, d.processId "
	)
})
@Entity
@Table(name = "FIS_VW_DELEGATIONS")
public class WFDelegationData extends BaseEntity implements Serializable {
	private Long id;
	private Long empId;
	private Long delegateId;
	private Long processId;
	private Long unitId;
	private String empName;
	private String delegateName;
	private String processName;
	private String unitFullName;
	private WFDelegation wfDelegation;

	public WFDelegationData(){
		wfDelegation = new WFDelegation();
	}
	
	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
		wfDelegation.setId(id);
	}

	@Basic
	@Column(name = "EMP_ID")
	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
		wfDelegation.setEmpId(empId);
	}

	@Basic
	@Column(name = "DELEGATE_ID")
	public Long getDelegateId() {
		return delegateId;
	}

	public void setDelegateId(Long delegateId) {
		this.delegateId = delegateId;
		wfDelegation.setDelegateId(delegateId);
	}

	@Basic
	@Column(name = "PROCESS_ID")
	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
		wfDelegation.setProcessId(processId);
	}

	@Basic
	@Column(name = "UNIT_ID")
	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
		wfDelegation.setUnitId(unitId);
	}

	@Basic
	@Column(name = "EMP_NAME")
	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	@Basic
	@Column(name = "DELEGATE_NAME")
	public String getDelegateName() {
		return delegateName;
	}

	public void setDelegateName(String delegateName) {
		this.delegateName = delegateName;
	}

	@Basic
	@Column(name = "PROCESS_NAME")
	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	@Basic
	@Column(name = "UNIT_FULL_NAME")
	public String getUnitFullName() {
		return unitFullName;
	}

	public void setUnitFullName(String unitFullName) {
		this.unitFullName = unitFullName;
	}

	@Transient
	public WFDelegation getWfDelegation() {
		return wfDelegation;
	}

	public void setWfDelegation(WFDelegation wfDelegation) {
		this.wfDelegation = wfDelegation;
	}
}