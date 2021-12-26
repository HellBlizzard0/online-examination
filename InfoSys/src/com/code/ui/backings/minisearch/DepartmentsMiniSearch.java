package com.code.ui.backings.minisearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.setup.DepartmentData;
import com.code.dal.orm.setup.DepartmentTypeData;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.WFPositionsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.workflow.WFPositionService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "departmentsMiniSearch")
@ViewScoped
public class DepartmentsMiniSearch extends BaseBacking implements Serializable {
	private int rowsCount = 10;
	private int page = 1;

	private String searchDepName = null;
	private String searchDepCode = null;
	private int mode = 1;
	private Long regionId;
	private Long departmentTypeId;
	private String departmentTypesIdList = "";
	List<Long> departmentIds = new ArrayList<Long>();

	private List<DepartmentData> searchDepartmentList = new ArrayList<DepartmentData>();

	private List<DepartmentTypeData> departmentTypeList;

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	/**
	 * Constructor
	 * 
	 * @throws BusinessException
	 * @throws NumberFormatException
	 */
	public DepartmentsMiniSearch() {
		if (!getRequest().getParameter("mode").equals("null") && !getRequest().getParameter("mode").isEmpty() && !getRequest().getParameter("mode").equals("undefined")) {
			mode = Integer.parseInt(getRequest().getParameter("mode"));
		}

		if (mode == 2 && !getRequest().getParameter("departmentTypesList").equals("null") && !getRequest().getParameter("departmentTypesList").isEmpty() && !getRequest().getParameter("departmentTypesList").equals("undefined")) {
			departmentTypesIdList = getRequest().getParameter("departmentTypesList");
		}

		if ((mode == 3) && !getRequest().getParameter("regionId").equals("null") && !getRequest().getParameter("regionId").isEmpty() && !getRequest().getParameter("regionId").equals("undefined")) {
			regionId = Long.parseLong(getRequest().getParameter("regionId"));
		}

		if ((mode == 4) && !getRequest().getParameter("regionId").equals("null") && !getRequest().getParameter("regionId").isEmpty() && !getRequest().getParameter("regionId").equals("undefined")) {
			regionId = Long.parseLong(getRequest().getParameter("regionId"));
		}

		if ((mode == 5) && !getRequest().getParameter("regionId").equals("null") && !getRequest().getParameter("regionId").isEmpty() && !getRequest().getParameter("regionId").equals("undefined")) {
			regionId = Long.parseLong(getRequest().getParameter("regionId"));
		}

		if ((mode == 6) && !getRequest().getParameter("regionId").equals("null") && !getRequest().getParameter("regionId").isEmpty() && !getRequest().getParameter("regionId").equals("undefined")) {
			String[] depIdsList = getRequest().getParameter("regionId").split(",");
			Long[] longDepIdssList = new Long[depIdsList.length];
			int counter = 0;
			for (String depId : depIdsList) {
				longDepIdssList[counter++] = Long.valueOf(depId);
			}

			for (Long depId : longDepIdssList) {
				DepartmentService.initChildDepartmentsList();
				try {
					DepartmentService.getChildrenDepartments(depId);
				} catch (BusinessException e) {
					super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
				}
				departmentIds.addAll(DepartmentService.getChildDepartmentsList());
			}
		}

		if ((mode == 7) && !getRequest().getParameter("regionId").equals("null") && !getRequest().getParameter("regionId").isEmpty() && !getRequest().getParameter("regionId").equals("undefined")) {
			regionId = Long.valueOf(getRequest().getParameter("regionId"));
		}

		try {
			departmentTypeList = DepartmentService.getAllDepartmentTypes();
			departmentTypeId = (long) FlagsEnum.ALL.getCode();
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Reset search fields
	 */
	public void resetSearch() {
		searchDepName = null;
		searchDepCode = null;
		departmentTypeId = (long) FlagsEnum.ALL.getCode();
	}

	/**
	 * Search all departments by name or code
	 */
	public void searchDepartment() {
		try {
			searchDepartmentList.clear();
			if (mode == 1) {
				if (departmentTypeId == null || departmentTypeId == FlagsEnum.ALL.getCode()) {
					searchDepartmentList = DepartmentService.getDepartment(searchDepName, searchDepCode);
				} else {
					searchDepartmentList = DepartmentService.getDepartmentsBydepartmentTypes(searchDepName, searchDepCode, new Long[] { departmentTypeId });
				}
			} else if (mode == 2) {
				String[] typesList = departmentTypesIdList.split(",");
				Long[] longTypesList = new Long[typesList.length];
				int counter = 0;
				for (String type : typesList) {
					longTypesList[counter++] = Long.valueOf(type);
				}

				searchDepartmentList = DepartmentService.getDepartmentsBydepartmentTypes(searchDepName, searchDepCode, longTypesList);
			} else if (mode == 3) {
				searchDepartmentList = DepartmentService.getDepartmentsByRegionId(regionId, searchDepName, searchDepCode);
			} else if (mode == 4) {
				if (regionId == null) {
					DepartmentData loginDep = DepartmentService.getDepartment(loginEmpData.getActualDepartmentId());
					if (loginDep.getRegionId().equals(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId())) {
						regionId = null;
					} else {
						regionId = loginDep.getRegionId();
					}
				}
				searchDepartmentList = DepartmentService.getDepartmentsByRegionIdAndType(regionId, searchDepName, searchDepCode, DepartmentTypeEnum.SECTOR.getCode());
			} else if (mode == 5) {
				searchDepartmentList = DepartmentService.getAssignmentDepartments(regionId == null ? FlagsEnum.ALL.getCode() : regionId);
			} else if (mode == 6) {
				Long[] depIds = departmentIds.toArray(new Long[departmentIds.size()]);
				searchDepartmentList = DepartmentService.getDepartmentsByMultipleIdsAndNameAndCode(depIds, searchDepName, searchDepCode);
			} else if (mode == 7) {
				searchDepartmentList = DepartmentService.getDepartmentsByRegionAndDepartmentTypes(regionId, searchDepName, searchDepCode,
						new Long[] { DepartmentTypeEnum.GENERAL_UNIT.getCode(), DepartmentTypeEnum.INSTITUTE.getCode(), DepartmentTypeEnum.MARINE_UNITS.getCode(), DepartmentTypeEnum.UNIT.getCode(), DepartmentTypeEnum.TRAINING_CENTER.getCode(), DepartmentTypeEnum.HEAD_TRAINING_CENTER.getCode(), DepartmentTypeEnum.CENTERS_FORCE.getCode(), DepartmentTypeEnum.FORCE.getCode(), DepartmentTypeEnum.UNITY.getCode(), DepartmentTypeEnum.BATTALION.getCode(), DepartmentTypeEnum.CENTER.getCode(),
								DepartmentTypeEnum.PATROLS_AND_SARAYA.getCode(), DepartmentTypeEnum.GROUP_OF_BOATS.getCode(), DepartmentTypeEnum.COMPANY.getCode(), DepartmentTypeEnum.DIVISION.getCode(), DepartmentTypeEnum.SECTION.getCode(), DepartmentTypeEnum.OFFICE.getCode(), DepartmentTypeEnum.POINT.getCode(), DepartmentTypeEnum.TEMPORARY_UNIT.getCode(), DepartmentTypeEnum.WORKSHOP.getCode() });
			}

			if (searchDepartmentList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
			}
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(DepartmentsMiniSearch.class, e, "DepartmentsMiniSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public int getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getSearchDepName() {
		return searchDepName;
	}

	public void setSearchDepName(String searchDepName) {
		this.searchDepName = searchDepName;
	}

	public String getSearchDepCode() {
		return searchDepCode;
	}

	public void setSearchDepCode(String searchDepCode) {
		this.searchDepCode = searchDepCode;
	}

	public List<DepartmentData> getSearchDepartmentList() {
		return searchDepartmentList;
	}

	public void setSearchDepartmentList(List<DepartmentData> searchDepartmentList) {
		this.searchDepartmentList = searchDepartmentList;
	}

	public List<DepartmentTypeData> getDepartmentTypeList() {
		return departmentTypeList;
	}

	public void setDepartmentTypeList(List<DepartmentTypeData> departmentTypeList) {
		this.departmentTypeList = departmentTypeList;
	}

	public Long getDepartmentTypeId() {
		return departmentTypeId;
	}

	public void setDepartmentTypeId(Long departmentTypeId) {
		this.departmentTypeId = departmentTypeId;
	}
}
