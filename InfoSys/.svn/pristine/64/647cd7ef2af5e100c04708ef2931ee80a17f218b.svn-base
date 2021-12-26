package com.code.dal.orm.workflow;

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
import com.code.dal.orm.AuditEntity;

@SuppressWarnings("serial")
@NamedQueries({
	// get total delegation from me or partial delegation for specific process from me either for all units or for a specific unit.
	@NamedQuery(name = "wf_delegation_getWFDelegation",
				query = " select d from WFDelegation d " +
						" where d.empId = :P_EMP_ID " +
						" and ((:P_PROCESS_ID = -1 and d.processId is null) or (:P_PROCESS_ID <> -1 and d.processId = :P_PROCESS_ID)) " +
						" and ((:P_UNIT_IDS_FLAG = -1 and d.unitId is null) or (:P_UNIT_IDS_FLAG <> -1 and d.unitId in (:P_UNITS_IDS))) "
	),
	// get counts of total delegations and partial delegations from and to me.
	@NamedQuery(name = "wf_delegation_getWFDelegationsCount",
				query = " select count(case when d.processId is null and d.empId = :P_EMP_ID then 1 end), " +
						" count(case when d.processId is null and d.delegateId = :P_EMP_ID then 1 end), " +
						" count(case when d.processId is not null and d.empId = :P_EMP_ID then 1 end), " +
						" count(case when d.processId is not null and d.delegateId = :P_EMP_ID then 1 end) " +
						" from WFDelegation d "
	)
})
@Entity
@Table(name = "FIS_WF_DELEGATIONS")
public class WFDelegation extends AuditEntity implements InsertableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private Long empId;
	private Long delegateId;
	private Long processId;
	private Long unitId;

	public void setId(Long id) {
		this.id = id;
	}

	@SequenceGenerator(name = "FIS_WF_DELEGATIONS_SEQ", sequenceName = "FIS_WF_DELEGATIONS_SEQ",allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "FIS_WF_DELEGATIONS_SEQ")
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Basic
	@Column(name = "EMP_ID")
	public Long getEmpId() {
		return empId;
	}

	public void setDelegateId(Long delegateId) {
		this.delegateId = delegateId;
	}

	@Basic
	@Column(name = "DELEGATE_ID")
	public Long getDelegateId() {
		return delegateId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	@Basic
	@Column(name = "PROCESS_ID")
	public Long getProcessId() {
		return processId;
	}

	@Basic
	@Column(name = "UNIT_ID")
	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	@Override
	public Long calculateContentId() {
		return id;
	}
}