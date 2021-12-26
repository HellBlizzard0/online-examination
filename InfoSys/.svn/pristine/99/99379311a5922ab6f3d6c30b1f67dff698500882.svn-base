package com.code.ui.backings.minisearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.securitymission.EmpNonEmpCarPermitsData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.securityaction.EmployeeNonEmployeeCarService;
import com.code.services.setup.SetupService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "carPermitsMiniSearch")
@ViewScoped
public class CarPermitsMiniSearch extends BaseBacking implements Serializable {
	private int rowsCount = 10;
	private int mode;
	private Long employeeId;
	private Long nonEmployeeId;
	private String searchPlateNumber = "";
	private String searchPlateChar1 = "";
	private String searchPlateChar2 = "";
	private String searchPlateChar3 = "";
	private List<SetupDomain> carTypesList;
	private Long domainCarModelId;
	private String plateNumber = "";
	private String plateChar1 = "";
	private String plateChar2 = "";
	private String plateChar3 = "";
	private String permitNo = "";
	private List<EmpNonEmpCarPermitsData> empNonEmpCarPermitsDataList;

	/**
	 * Constructor
	 */
	public CarPermitsMiniSearch() {
		if (getRequest().getParameter("mode") != null && !getRequest().getParameter("mode").equals("null") && !getRequest().getParameter("mode").isEmpty() && !getRequest().getParameter("mode").equals("undefined")) {
			mode = Integer.parseInt(getRequest().getParameter("mode"));
		}
		if (getRequest().getParameter("empNonEmpFlag") != null && !getRequest().getParameter("empNonEmpFlag").equals("null") && !getRequest().getParameter("empNonEmpFlag").isEmpty() && !getRequest().getParameter("empNonEmpFlag").equals("undefined")) {
			String type = getRequest().getParameter("empNonEmpFlag");
			if (getRequest().getParameter("empNonEmpId") != null && !getRequest().getParameter("empNonEmpId").equals("null") && !getRequest().getParameter("empNonEmpId").isEmpty() && !getRequest().getParameter("empNonEmpId").equals("undefined")) {
				if (type.equals("true")) {
					employeeId = Long.parseLong(getRequest().getParameter("empNonEmpId"));
				} else if (type.equals("false")) {
					nonEmployeeId = Long.parseLong(getRequest().getParameter("empNonEmpId"));
				}
			}
		}

		try {
			carTypesList = SetupService.getDomains(ClassesEnum.CAR_MODELS.getCode());
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
		init();
	}

	/**
	 * Initialize/Reset Variables
	 */
	public void init() {
		searchLastCars();
	}

	/**
	 * Reset Search Parameters
	 */
	public void resetSearch() {
		empNonEmpCarPermitsDataList = new ArrayList<EmpNonEmpCarPermitsData>();
		searchPlateNumber = "";
		searchPlateChar1 = "";
		searchPlateChar2 = "";
		searchPlateChar3 = "";
	}

	/**
	 * Search Employee Last Cars
	 */
	public void searchLastCars() {
		try {
			empNonEmpCarPermitsDataList = EmployeeNonEmployeeCarService.getEmpNonEmpCarPermitsData(employeeId, nonEmployeeId, null, searchPlateChar1 + " " + searchPlateChar2 + " " + searchPlateChar3 + " " + searchPlateNumber);
			if (empNonEmpCarPermitsDataList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
			}
		} catch (BusinessException e) {
			setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * 
	 */
	public void resetSavedParameters() {
		plateNumber = null;
		plateChar1 = null;
		plateChar2 = null;
		plateChar3 = null;
	}

	/**
	 * Add Employee Car
	 */
	public void saveEmployeeCar() {
		try {
			EmpNonEmpCarPermitsData empNonEmpCarPermitData = new EmpNonEmpCarPermitsData();
			for (SetupDomain domain : carTypesList) {
				if (domain.getId().equals(domainCarModelId)) {
					empNonEmpCarPermitData.setCarModel(domain.getDescription());
					break;
				}
			}
			empNonEmpCarPermitData.setCarModelId(domainCarModelId);
			empNonEmpCarPermitData.setEmpId(employeeId);
			empNonEmpCarPermitData.setNonEmpId(nonEmployeeId);
			empNonEmpCarPermitData.setPermitNo(permitNo);
			empNonEmpCarPermitData.setPlateNumber(plateChar1 + " " + plateChar2 + " " + plateChar3 + " " + plateNumber);
			EmployeeNonEmployeeCarService.saveEmployeeNonEmployeeCarPermit(empNonEmpCarPermitData, loginEmpData);
			empNonEmpCarPermitsDataList.add(empNonEmpCarPermitData);
			resetSavedParameters();
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	// setters and getters
	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}

	public int getRowsCount() {
		return rowsCount;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public String getSearchPlateNumber() {
		return searchPlateNumber;
	}

	public void setSearchPlateNumber(String searchPlateNumber) {
		this.searchPlateNumber = searchPlateNumber;
	}

	public String getSearchPlateChar1() {
		return searchPlateChar1;
	}

	public void setSearchPlateChar1(String searchPlateChar1) {
		this.searchPlateChar1 = searchPlateChar1;
	}

	public String getSearchPlateChar2() {
		return searchPlateChar2;
	}

	public void setSearchPlateChar2(String searchPlateChar2) {
		this.searchPlateChar2 = searchPlateChar2;
	}

	public String getSearchPlateChar3() {
		return searchPlateChar3;
	}

	public void setSearchPlateChar3(String searchPlateChar3) {
		this.searchPlateChar3 = searchPlateChar3;
	}

	public List<SetupDomain> getCarTypesList() {
		return carTypesList;
	}

	public void setCarTypesList(List<SetupDomain> carTypesList) {
		this.carTypesList = carTypesList;
	}

	public Long getDomainCarModelId() {
		return domainCarModelId;
	}

	public void setDomainCarModelId(Long domainCarModelId) {
		this.domainCarModelId = domainCarModelId;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getPlateChar1() {
		return plateChar1;
	}

	public void setPlateChar1(String plateChar1) {
		this.plateChar1 = plateChar1;
	}

	public String getPlateChar2() {
		return plateChar2;
	}

	public void setPlateChar2(String plateChar2) {
		this.plateChar2 = plateChar2;
	}

	public String getPlateChar3() {
		return plateChar3;
	}

	public void setPlateChar3(String plateChar3) {
		this.plateChar3 = plateChar3;
	}

	public List<EmpNonEmpCarPermitsData> getEmpNonEmpCarPermitsDataList() {
		return empNonEmpCarPermitsDataList;
	}

	public void setEmpNonEmpCarPermitsDataList(List<EmpNonEmpCarPermitsData> empNonEmpCarPermitsDataList) {
		this.empNonEmpCarPermitsDataList = empNonEmpCarPermitsDataList;
	}

	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}
}
