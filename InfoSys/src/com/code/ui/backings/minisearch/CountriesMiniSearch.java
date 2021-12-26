package com.code.ui.backings.minisearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.setup.CountryData;
import com.code.exceptions.BusinessException;
import com.code.services.setup.CountryService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "countriesMiniSearch")
@ViewScoped
public class CountriesMiniSearch extends BaseBacking implements Serializable {
	private int rowsCount = 10;
	private int page = 1;

	private String searchCountryName = null;
	private int mode = 1;

	private List<CountryData> searchCountryList = new ArrayList<CountryData>();

	/**
	 * Constructor
	 */
	public CountriesMiniSearch() {
		if (!getRequest().getParameter("mode").equals("null") && !getRequest().getParameter("mode").isEmpty() && !getRequest().getParameter("mode").equals("undefined")) {
			mode = Integer.parseInt(getRequest().getParameter("mode"));
		}
	}

	/**
	 * Reset search fields
	 */
	public void resetSearch() {
		searchCountryName = null;
	}

	/**
	 * Search all employees given name, social id and military number
	 */
	public void searchCountry() {
		try {
			searchCountryList.clear();
			searchCountryList = CountryService.getCountry(searchCountryName);
			if (searchCountryList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
			}
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
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

	public String getSearchCountryName() {
		return searchCountryName;
	}

	public void setSearchCountryName(String searchCountryName) {
		this.searchCountryName = searchCountryName;
	}

	public List<CountryData> getSearchCountryList() {
		return searchCountryList;
	}

	public void setSearchCountryList(List<CountryData> searchCountryList) {
		this.searchCountryList = searchCountryList;
	}
}