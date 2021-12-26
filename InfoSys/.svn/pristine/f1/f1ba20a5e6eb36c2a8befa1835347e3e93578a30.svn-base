package com.code.ui.backings.minisearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.setup.CountryData;
import com.code.dal.orm.setup.NonEmployeeData;
import com.code.enums.FlagsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.setup.CountryService;
import com.code.services.setup.NonEmployeeService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "nonEmpsMiniSearch")
@ViewScoped
public class NonEmpsMiniSearch extends BaseBacking implements Serializable {
	private int rowsCount = 10;
	private int mode;

	private NonEmployeeData searchNonEmployee;
	private NonEmployeeData nonEmployee;

	private List<NonEmployeeData> nonEmployeeList = new ArrayList<NonEmployeeData>();

	private List<CountryData> countriesList;

	private boolean nonEmployeeExpandFlag;
	private Integer nonEmployeeSelectedIndex;

	/**
	 * Constructor
	 */
	public NonEmpsMiniSearch() {
		if (getRequest().getParameter("mode") != null && !getRequest().getParameter("mode").equals("null") && !getRequest().getParameter("mode").isEmpty() && !getRequest().getParameter("mode").equals("undefined")) {
			mode = Integer.parseInt(getRequest().getParameter("mode"));
		}
		try {
			countriesList = CountryService.getAllCountries();
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
		init();
	}

	/**
	 * Initialize/Reset Variables
	 */
	public void init() {
		searchNonEmployee = new NonEmployeeData();
		nonEmployee = new NonEmployeeData();
		nonEmployeeExpandFlag = false;
	}

	/**
	 * Reset Search Parameters
	 */
	public void resetSearch() {
		searchNonEmployee = new NonEmployeeData();
	}

	/**
	 * Search nonemployees given identity and/or full name
	 */
	public void searchNonEmployee() {
		try {
			nonEmployeeList = NonEmployeeService.getNonEmployee(searchNonEmployee.getIdentity() == null ? FlagsEnum.ALL.getCode() : searchNonEmployee.getIdentity(), searchNonEmployee.getFullName());
			if (nonEmployeeList.isEmpty()) {
				setServerSideErrorMessages(getParameterizedMessage("error_noData"));
			}
			nonEmployeeExpandFlag = false;
			nonEmployee = new NonEmployeeData();
		} catch (BusinessException e) {
			setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Add new nonemployee or Edit existing nonemployee
	 */
	public void saveNonEmployee() {
		try {
			if (nonEmployee.getId() == null) {
				NonEmployeeService.insertNonEmployee(loginEmpData, nonEmployee);
				if (nonEmployee.getCountryId() != null) {
					nonEmployee.setCountryArabicName(CountryService.getCountryById(nonEmployee.getCountryId()).getArabicName());
				}
				nonEmployeeList.add(0, nonEmployee);
			} else {
				NonEmployeeService.updateNonEmployee(loginEmpData, nonEmployee);
				if (nonEmployee.getCountryId() != null) {
					nonEmployee.setCountryArabicName(CountryService.getCountryById(nonEmployee.getCountryId()).getArabicName());
				}
				nonEmployeeList.set(nonEmployeeSelectedIndex, nonEmployee);
			}
			setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
			nonEmployeeExpandFlag = true;
			nonEmployee = new NonEmployeeData();
		} catch (BusinessException e) {
			setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Edit nonemployee
	 * 
	 * @param index
	 */
	public void editNonEmployee(Integer index) {
		nonEmployeeExpandFlag = true;
		try {
			nonEmployee = (NonEmployeeData) nonEmployeeList.get(index).clone();
			nonEmployeeSelectedIndex = index;
		} catch (CloneNotSupportedException e) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	// setters and getters
	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}

	public int getRowsCount() {
		return rowsCount;
	}

	public List<NonEmployeeData> getNonEmployeeList() {
		return nonEmployeeList;
	}

	public void setNonEmployeeList(List<NonEmployeeData> nonEmployeeList) {
		this.nonEmployeeList = nonEmployeeList;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public NonEmployeeData getNonEmployee() {
		return nonEmployee;
	}

	public void setNonEmployee(NonEmployeeData nonEmployee) {
		this.nonEmployee = nonEmployee;
	}

	public List<CountryData> getCountriesList() {
		return countriesList;
	}

	public void setCountriesList(List<CountryData> countriesList) {
		this.countriesList = countriesList;
	}

	public NonEmployeeData getSearchNonEmployee() {
		return searchNonEmployee;
	}

	public void setSearchNonEmployee(NonEmployeeData searchNonEmployee) {
		this.searchNonEmployee = searchNonEmployee;
	}

	public boolean isNonEmployeeExpandFlag() {
		return nonEmployeeExpandFlag;
	}

	public void setNonEmployeeExpandFlag(boolean nonEmployeeExpandFlag) {
		this.nonEmployeeExpandFlag = nonEmployeeExpandFlag;
	}

	public Integer getNonEmployeeSelectedIndex() {
		return nonEmployeeSelectedIndex;
	}

	public void setNonEmployeeSelectedIndex(Integer nonEmployeeSelectedIndex) {
		this.nonEmployeeSelectedIndex = nonEmployeeSelectedIndex;
	}

	public Long getKSACountryId() {
		return InfoSysConfigurationService.getKSACountryId();
	}
}
