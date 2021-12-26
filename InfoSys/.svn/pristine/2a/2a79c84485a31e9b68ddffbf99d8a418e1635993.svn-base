package com.code.ui.backings.minisearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.setup.DepartmentData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.enums.WFPositionsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.EmployeeService;
import com.code.services.workflow.WFPositionService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "empsMiniSearch")
@ViewScoped
public class EmpsMiniSearch extends BaseBacking implements Serializable {
	private int rowsCount = 10;
	private int page = 1;
	private String searchEmpName = null;
	private String searchEmpSocialId = null;
	private String searchEmpMilitaryNumber = null;
	private int mode = 1;
	private Long[] actualDepartments = null;
	private boolean multipleSelection = false;
	private String selectedEmployeeIds = "";
	private String employeesIdString = "";

	private List<EmployeeData> searchEmployeeList = new ArrayList<EmployeeData>();
	private List<EmployeeData> selectedEmployeesList = new ArrayList<EmployeeData>();

	/**
	 * Constructor
	 * 
	 * @throws BusinessException
	 * @throws NumberFormatException
	 */
	public EmpsMiniSearch() throws NumberFormatException, BusinessException {
		if (!getRequest().getParameter("mode").equals("null") && !getRequest().getParameter("mode").isEmpty() && !getRequest().getParameter("mode").equals("undefined")) {
			mode = Integer.parseInt(getRequest().getParameter("mode"));
		}

		if (getRequest().getParameter("depsList") != null && !getRequest().getParameter("depsList").equals("null") && !getRequest().getParameter("depsList").isEmpty() && !getRequest().getParameter("depsList").equals("undefined")) {
			String deps = getRequest().getParameter("depsList");
			if (! deps.equals("-1")) {
			String[] depsList = deps.split(",");
			actualDepartments = new Long[depsList.length];
			for (int i = 0; i < depsList.length; i++) {
				actualDepartments[i] = Long.parseLong(depsList[i]);
			}
			}
		}

		if (getRequest().getParameter("multiple") != null && !getRequest().getParameter("multiple").equals("null") && !getRequest().getParameter("multiple").isEmpty() && !getRequest().getParameter("multiple").equals("undefined") && mode != 6) {
			multipleSelection = true;
			if (getRequest().getParameter("ids") != null && !getRequest().getParameter("ids").equals("null") && !getRequest().getParameter("ids").isEmpty() && !getRequest().getParameter("ids").equals("undefined")) {
				String selectedEmployees = getRequest().getParameter("ids");
				List<String> selectedEmployeesIdsList = Arrays.asList(selectedEmployees.split(","));
				for (String empId : selectedEmployeesIdsList) {
					EmployeeData empData = EmployeeService.getEmployee(Long.parseLong(empId));
					selectedEmployeesList.add(empData);
				}
			}
		}

		if (getRequest().getParameter("ids") != null && !getRequest().getParameter("ids").equals("null") && !getRequest().getParameter("ids").isEmpty() && !getRequest().getParameter("ids").equals("undefined") && (mode == 6 || mode == 5))
			employeesIdString = getRequest().getParameter("ids");
	}

	/**
	 * Reset search fields
	 */
	public void resetSearch() {
		searchEmpName = null;
		searchEmpSocialId = null;
		searchEmpMilitaryNumber = null;
	}

	/**
	 * Search all employees given name, social id and military number
	 */
	public void searchEmployee() {
		try {
			searchEmployeeList.clear();
			if ((searchEmpName == null || searchEmpName.trim().isEmpty()) && (searchEmpSocialId == null || searchEmpSocialId.trim().isEmpty()) && (searchEmpMilitaryNumber == null || searchEmpMilitaryNumber.trim().isEmpty())) {
				throw new BusinessException("error_oneFieldMandatory");
			}
			if (searchEmpName != null && !searchEmpName.trim().isEmpty()) {
				if (searchEmpName.length() < 3) {
					throw new BusinessException("error_nameNotLessThanThreeCharacters");
				}
			}
			if (mode == 1) {
				searchEmployeeList = EmployeeService.getEmployee(searchEmpName, searchEmpSocialId, searchEmpMilitaryNumber, null);
			} else if (mode == 2) { // from the same region
				DepartmentData loginDep = DepartmentService.getDepartment(loginEmpData.getActualDepartmentId());
				if (loginDep.getRegionId().equals(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId())) {
					searchEmployeeList = EmployeeService.getEmployee(searchEmpName, searchEmpSocialId, searchEmpMilitaryNumber, null);
				} else {
					searchEmployeeList = EmployeeService.getRegionEmployee(searchEmpName, searchEmpSocialId, searchEmpMilitaryNumber, loginDep.getRegionId());
				}
			} else if (mode == 3) { // from certain departments
				searchEmployeeList = EmployeeService.getEmployee(searchEmpName, searchEmpSocialId, searchEmpMilitaryNumber, actualDepartments);
			} else if (mode == 4) { // get employees that only have assignments
				DepartmentData loginDep = DepartmentService.getDepartment(loginEmpData.getActualDepartmentId());
				if (loginDep.getRegionId().equals(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId())) {
					searchEmployeeList = EmployeeService.getEmployeesHavingAssignment(searchEmpName, searchEmpSocialId, searchEmpMilitaryNumber, null);
				} else {
					searchEmployeeList = EmployeeService.getRegionsEmployeeHavingAssignment(searchEmpName, searchEmpSocialId, searchEmpMilitaryNumber, loginDep.getRegionId());
				}
			} else if (mode == 5) { // 1-Employee Direct Manager 2-Employees sharing same direct manager 3-Employees that he is their direct manager
				if (!employeesIdString.equals("")) {
					Long delegatorDepId = actualDepartments[0];
					Long loginEmpDepartmentManagerId = DepartmentService.getDepartmentManagerId(delegatorDepId);
					boolean isDepManager = loginEmpDepartmentManagerId != null && loginEmpDepartmentManagerId.longValue() == Long.parseLong(employeesIdString) ? true : false;
					if (isDepManager) { // upper department direct manager - his children - his siblings
						Long upperDepartmentId = DepartmentService.getDepartmentHier(delegatorDepId, null).getParentId();
						actualDepartments = Arrays.copyOf(actualDepartments, actualDepartments.length + 1);
						actualDepartments[actualDepartments.length - 1] = upperDepartmentId;
						searchEmployeeList = EmployeeService.getEmployee(searchEmpName, searchEmpSocialId, searchEmpMilitaryNumber, actualDepartments);
					} else { // direct manager + siblings
						searchEmployeeList = EmployeeService.getEmployee(searchEmpName, searchEmpSocialId, searchEmpMilitaryNumber, actualDepartments);
					}
				}
			} else if (mode == 6) { // Get employee from employees were sent in request
				if (!employeesIdString.equals("")) {
					List<String> employeesIdsStringList = Arrays.asList(employeesIdString.split(","));
					List<Long> employeesIdsList = new ArrayList<Long>();
					for (String empId : employeesIdsStringList) {
						employeesIdsList.add(Long.valueOf(empId));
					}
					searchEmployeeList = EmployeeService.getEmployeeByIdList(searchEmpName, searchEmpSocialId, searchEmpMilitaryNumber, employeesIdsList.toArray(new Long[employeesIdsList.size()]));
				}
			}
			if (searchEmployeeList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
			}
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(EmpsMiniSearch.class, e, "EmpsMiniSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void addSelectedEmployee(EmployeeData selectedEmployee) {
		if (selectedEmployeesList.contains(selectedEmployee)) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_employeeAlreadyExists"));
		} else {
			selectedEmployeesList.add(selectedEmployee);
			searchEmployeeList.remove(selectedEmployee);
		}
	}

	public void removeSelectedEmployee(EmployeeData selectedEmployee) {
		selectedEmployeesList.remove(selectedEmployee);
	}

	public void selectMultiEmployee() {
		for (EmployeeData employee : selectedEmployeesList) {
			selectedEmployeeIds += employee.getEmpId() + ",";
		}
	}

	/********************* Getters and Setters ********************************/

	public int getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}

	public String getSearchEmpName() {
		return searchEmpName;
	}

	public void setSearchEmpName(String searchEmpName) {
		this.searchEmpName = searchEmpName;
	}

	public String getSearchEmpSocialId() {
		return searchEmpSocialId;
	}

	public void setSearchEmpSocialId(String searchEmpSocialId) {
		this.searchEmpSocialId = searchEmpSocialId;
	}

	public List<EmployeeData> getSearchEmployeeList() {
		return searchEmployeeList;
	}

	public void setSearchEmployeeList(List<EmployeeData> searchEmployeeList) {
		this.searchEmployeeList = searchEmployeeList;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getSearchEmpMilitaryNumber() {
		return searchEmpMilitaryNumber;
	}

	public void setSearchEmpMilitaryNumber(String searchEmpMilitaryNumber) {
		this.searchEmpMilitaryNumber = searchEmpMilitaryNumber;
	}

	public boolean isMultipleSelection() {
		return multipleSelection;
	}

	public void setMultipleSelection(boolean multipleSelection) {
		this.multipleSelection = multipleSelection;
	}

	public List<EmployeeData> getSelectedEmployeesList() {
		return selectedEmployeesList;
	}

	public void setSelectedEmployeesList(List<EmployeeData> selectedEmployeesList) {
		this.selectedEmployeesList = selectedEmployeesList;
	}

	public String getSelectedEmployeeIds() {
		return selectedEmployeeIds;
	}

	public void setSelectedEmployeeIds(String selectedEmployeeIds) {
		this.selectedEmployeeIds = selectedEmployeeIds;
	}
}