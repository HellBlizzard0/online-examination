package com.code.ui.backings.worklist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.workflow.WFPositionData;
import com.code.enums.WFPositionDiscriminatorEnum;
import com.code.exceptions.BusinessException;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.EmployeeService;
import com.code.services.workflow.WFPositionService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "wfPositions")
@ViewScoped
public class WFPositions extends BaseBacking implements Serializable {
	private List<WFPositionData> wfPositionList;
	private String posDesc;

	public WFPositions() {
		super();
		init();
		wfPositionList = new ArrayList<WFPositionData>();
	}

	/**
	 * search
	 */
	public void search() {
		try {
			wfPositionList = WFPositionService.getWFPositionData(posDesc);
			if (wfPositionList.isEmpty()) {
				this.setServerSideSuccessMessages(getParameterizedMessage("error_noData"));
				return;
			} else {
				for (WFPositionData pos : wfPositionList) {
					if (pos.getUnitId() != null) {
						try {
							EmployeeData manager = EmployeeService.getEmployee(DepartmentService.getDepartmentManager(pos.getUnitId()));
							pos.setPosDepManagerName(manager.getFullName());
							pos.setPosDepManagerEmail(manager.getEmail());
						} catch (BusinessException e) {
							throw new BusinessException(e.getMessage(), e.getParams());
						}
					}
					if (pos.getDiscriminator().equals(getEmpsGroupsDiscriminator())) {
						updateEmpGroup(pos);
					}
				}
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(WFPositions.class, e, "WFPositions");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * resetSearch
	 */
	public void resetSearch() {
		posDesc = null;
	}

	/**
	 * 
	 * @param wfPositionData
	 * @throws BusinessException
	 * @throws NumberFormatException
	 */
	public void updateEmpGroup(WFPositionData wfPositionData) throws NumberFormatException, BusinessException {
		wfPositionData.setEmpName("");
		for (Long empId : wfPositionData.getEmpsGroupList()) {
			EmployeeData employeeData = EmployeeService.getEmployee(empId);
			wfPositionData.setEmpName(wfPositionData.getEmpName().isEmpty() ? employeeData.getFullName() : wfPositionData.getEmpName() + " - " + employeeData.getFullName());
			if (wfPositionData.getEmpsGroupList().indexOf(empId) == 1) {
				wfPositionData.setEmpName(wfPositionData.getEmpName() + " ...");
				break;
			}
		}
	}

	/**
	 * savePosition
	 * 
	 * @param position
	 */
	public void savePosition(WFPositionData position) {
		try {
			if (position.getDiscriminator().equals(getEmpPosDiscriminator())) {
				EmployeeData emp = EmployeeService.getEmployee(position.getEmpId());
				position.setPosEmployeeDepId(emp.getActualDepartmentId());
				position.setPosEmployeeTitleId(emp.getActualTitleId());
				position.setPosEmployeeEmail(emp.getEmail());
			} else if (position.getDiscriminator().equals(getUnitPosDiscriminator())) {
				try {
					EmployeeData manager = EmployeeService.getEmployee(DepartmentService.getDepartmentManager(position.getUnitId()));
					position.setPosDepManagerName(manager.getFullName());
					position.setPosDepManagerEmail(manager.getEmail());
				} catch (BusinessException e) {
					throw new BusinessException(e.getMessage());
				}
			}
			WFPositionService.updateWFPosition(position, loginEmpData);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(WFPositions.class, e, "WFPositions");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public List<WFPositionData> getWfPositionList() {
		return wfPositionList;
	}

	public void setWfPositionList(List<WFPositionData> wfPositionList) {
		this.wfPositionList = wfPositionList;
	}

	public String getPosDesc() {
		return posDesc;
	}

	public void setPosDesc(String posDesc) {
		this.posDesc = posDesc;
	}

	public int getEmpPosDiscriminator() {
		return WFPositionDiscriminatorEnum.EMPLOYEE.getCode();
	}

	public int getUnitPosDiscriminator() {
		return WFPositionDiscriminatorEnum.DEPARTMENT.getCode();
	}

	public int getEmpsGroupsDiscriminator() {
		return WFPositionDiscriminatorEnum.EMPS_GROUP.getCode();
	}
}
