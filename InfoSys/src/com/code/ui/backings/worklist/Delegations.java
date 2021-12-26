package com.code.ui.backings.worklist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.workflow.WFDelegationData;
import com.code.dal.orm.workflow.WFProcess;
import com.code.enums.FlagsEnum;
import com.code.enums.WFProcessesGroupsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.EmployeeService;
import com.code.services.workflow.BaseWorkFlow;
import com.code.services.workflow.WFDelegationService;
import com.code.ui.backings.base.BaseBacking;
import com.code.ui.util.EncryptionUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "delegations")
@ViewScoped
public class Delegations extends BaseBacking implements Serializable {
	private Long delegatorId;
	private EmployeeData delegatorEmployee;
	private Long delegateeId;
	private EmployeeData delegateeEmployee;
	private List<WFProcess> processList;
	private long selectedProcessId;
	private List<WFDelegationData> totalDelegationList;
	private List<WFDelegationData> partialDelegationList;
	private String delegatorEmpManagerName = "";
	private String delegateeEmpManagerName = "";
	private String depsList;
	private Integer pageMode = 1; // 1- My Delegations. 2- To Me Delegations. 3- Delegation Management
	private int rowsCount = 10;

	/**
	 * Default Constructor
	 */
	public Delegations() {
		init();
		try {
			if (getRequest().getParameter("mode") != null && !getRequest().getParameter("mode").equals("null") && !getRequest().getParameter("mode").isEmpty() && !getRequest().getParameter("mode").equals("undefined")) {
				pageMode = Integer.parseInt(EncryptionUtil.decryptSymmetrically(getRequest().getParameter("mode")));
			}
			if (pageMode == 1) {
				super.setScreenTitle(getParameterizedMessage("title_myDelegations"));
			} else if (pageMode == 2) {
				super.setScreenTitle(getParameterizedMessage("title_toMeDelegations"));
			} else if (pageMode == 3) {
				super.setScreenTitle(getParameterizedMessage("title_delegationManagement"));
			}

			if (pageMode == 1 || pageMode == 3) {
				processList = BaseWorkFlow.getGroupProcesses(WFProcessesGroupsEnum.FIS_GROUP.getCode());
			}
			
			if (pageMode == 1) {
				addDelegatorDetails(loginEmpData.getEmpId());
			} else if (pageMode == 2) {
				delegatorEmployee = loginEmpData;
				Long managerId = DepartmentService.getDepartmentManagerId(loginEmpData.getActualDepartmentId());
				if (managerId != null) {
					delegatorEmpManagerName = EmployeeService.getEmployee(managerId).getFullName();
				}
				totalDelegationList = WFDelegationService.getWFDelegationList(null, loginEmpData.getEmpId(), null);
				partialDelegationList = WFDelegationService.getWFDelegationList(null, loginEmpData.getEmpId(), FlagsEnum.ON.getCode());
			}
		} catch (Exception e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage()));
		}
	}

	/**
	 * Bean Initializer
	 */
	public void init() {
		super.init();
		totalDelegationList = new ArrayList<WFDelegationData>();
		partialDelegationList = new ArrayList<WFDelegationData>();
		delegatorEmployee = new EmployeeData();
		delegateeEmployee = new EmployeeData();
	}

	/**
	 * Add Delegator Detail
	 * 
	 * @param delegatorId
	 */
	public void addDelegatorDetails(Long delegatorId) {
		try {
			delegatorEmpManagerName = "";
			delegateeEmpManagerName = "";
			delegateeEmployee = new EmployeeData();
			delegateeId = null;
			delegatorEmployee = EmployeeService.getEmployee(delegatorId);
			Long managerId = DepartmentService.getDepartmentManagerId(delegatorEmployee.getActualDepartmentId());
			if (managerId != null) {
				delegatorEmpManagerName = EmployeeService.getEmployee(managerId).getFullName();
			}
			totalDelegationList = WFDelegationService.getWFDelegationList(delegatorEmployee.getEmpId(), null, null);
			partialDelegationList = WFDelegationService.getWFDelegationList(delegatorEmployee.getEmpId(), null, FlagsEnum.ON.getCode());
			depsList = delegatorEmployee.getActualDepartmentId() + "";
			DepartmentService.initChildDepartmentsList();
			DepartmentService.getChildrenDepartments(delegatorEmployee.getActualDepartmentId());
			List<Long> childrenDeps = DepartmentService.getChildDepartmentsList();
			for (Long childrenDep : childrenDeps) {
				depsList += "," + childrenDep + "";
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Add Delegate Detail
	 */
	public void addDelegateeDetails() {
		try {
			if (loginEmpData.getEmpId().intValue() == delegateeId.intValue()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_sameDelegator"));
				return;
			}
			delegateeEmpManagerName = "";
			delegateeEmployee = EmployeeService.getEmployee(delegateeId);
			Long managerId = DepartmentService.getDepartmentManagerId(delegateeEmployee.getActualDepartmentId());
			if (managerId != null) {
				delegateeEmpManagerName = EmployeeService.getEmployee(managerId).getFullName();
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getMessage()));
		}
	}

	/**
	 * Save Total Delegation
	 */
	public void saveTotalDelegation() {
		try {
			if (!totalDelegationList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_onlyOneTotalDelegation"));
				return;
			}
			WFDelegationData saveDelegation = new WFDelegationData();
			saveDelegation.setEmpId(loginEmpData.getEmpId());
			saveDelegation.setDelegateId(delegateeId);
			saveDelegation.setProcessId(null);
			WFDelegationService.saveDelegation(saveDelegation);
			totalDelegationList.add(WFDelegationService.getWFDelegationById(saveDelegation.getId(), null));
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Save Partial Delegation
	 */
	public void savePartialDelegation() {
		try {
			for (WFDelegationData wfDelegationData : partialDelegationList) {
				if (wfDelegationData.getProcessId().intValue() == selectedProcessId) {
					this.setServerSideErrorMessages(getParameterizedMessage("error_onepartialProcessPerPerson"));
					return;
				}
			}
			WFDelegationData saveDelegation = new WFDelegationData();
			saveDelegation.setEmpId(loginEmpData.getEmpId());
			saveDelegation.setDelegateId(delegateeId);
			saveDelegation.setProcessId(selectedProcessId);
			WFDelegationService.saveDelegation(saveDelegation);
			partialDelegationList.add(WFDelegationService.getWFDelegationById(saveDelegation.getId(), FlagsEnum.ON.getCode()));
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Delete Total Delegation
	 */
	public void deleteTotalDelegation(WFDelegationData wfDelegationData) {
		try {
			WFDelegationService.deleteDelegate(wfDelegationData);
			totalDelegationList.remove(wfDelegationData);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Delete partial Delegation
	 */
	public void deletePartialDelegation(WFDelegationData wfDelegationData) {
		try {
			WFDelegationService.deleteDelegate(wfDelegationData);
			partialDelegationList.remove(wfDelegationData);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	public void setSelectedProcessId(long selectedProcessId) {
		this.selectedProcessId = selectedProcessId;
	}

	public long getSelectedProcessId() {
		return selectedProcessId;
	}

	public void setProcessList(List<WFProcess> processList) {
		this.processList = processList;
	}

	public List<WFProcess> getProcessList() {
		return processList;
	}

	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}

	public int getRowsCount() {
		return rowsCount;
	}

	public List<WFDelegationData> getTotalDelegationList() {
		return totalDelegationList;
	}

	public void setTotalDelegationList(List<WFDelegationData> totalDelegationList) {
		this.totalDelegationList = totalDelegationList;
	}

	public List<WFDelegationData> getPartialDelegationList() {
		return partialDelegationList;
	}

	public void setPartialDelegationList(List<WFDelegationData> partialDelegationList) {
		this.partialDelegationList = partialDelegationList;
	}

	public Integer getPageMode() {
		return pageMode;
	}

	public void setPageMode(Integer pageMode) {
		this.pageMode = pageMode;
	}

	public Long getDelegateeId() {
		return delegateeId;
	}

	public void setDelegateeId(Long delegateeId) {
		this.delegateeId = delegateeId;
	}

	public EmployeeData getDelegateeEmployee() {
		return delegateeEmployee;
	}

	public void setDelegateeEmployee(EmployeeData delegateeEmployee) {
		this.delegateeEmployee = delegateeEmployee;
	}

	public Long getDelegatorId() {
		return delegatorId;
	}

	public void setDelegatorId(Long delegatorId) {
		this.delegatorId = delegatorId;
	}

	public EmployeeData getDelegatorEmployee() {
		return delegatorEmployee;
	}

	public void setDelegatorEmployee(EmployeeData delegatorEmployee) {
		this.delegatorEmployee = delegatorEmployee;
	}

	public String getDelegatorEmpManagerName() {
		return delegatorEmpManagerName;
	}

	public void setDelegatorEmpManagerName(String delegatorEmpManagerName) {
		this.delegatorEmpManagerName = delegatorEmpManagerName;
	}

	public String getDelegateeEmpManagerName() {
		return delegateeEmpManagerName;
	}

	public void setDelegateeEmpManagerName(String delegateeEmpManagerName) {
		this.delegateeEmpManagerName = delegateeEmpManagerName;
	}

	public String getDepsList() {
		return depsList;
	}

	public void setDepsList(String depsList) {
		this.depsList = depsList;
	}
	
	
}
