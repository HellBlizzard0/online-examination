package com.code.ui.backings.minisearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.setup.SetupDomain;
import com.code.exceptions.BusinessException;
import com.code.services.setup.SetupService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "domainsMiniSearch")
@ViewScoped
public class DomainsMiniSearch extends BaseBacking implements Serializable {
	private int rowsCount = 10;
	private int mode = 1;
	private String domainDescription;
	private String classCodes;
	private String classDescription;
	private String classDetailsTitle;
	private String DomainDetailsTitle;
	private boolean searchWithClassFlag;
	private String screenTitle;

	private List<SetupDomain> domainsList;

	public DomainsMiniSearch() {
		if (!getRequest().getParameter("mode").equals("null") && !getRequest().getParameter("mode").isEmpty() && !getRequest().getParameter("mode").equals("undefined")) {
			mode = Integer.parseInt(getRequest().getParameter("mode"));
			classDetailsTitle = getParameterizedMessage("label_classDescription");
			DomainDetailsTitle = getParameterizedMessage("label_domainDescription");
			screenTitle = getParameterizedMessage("title_domainsInquiry");
			searchWithClassFlag = true;
		}
		if (mode == 2 && !getRequest().getParameter("classesCodeList").equals("null") && !getRequest().getParameter("classesCodeList").isEmpty() && !getRequest().getParameter("classesCodeList").equals("undefined")) {
			classCodes = getRequest().getParameter("classesCodeList");
			if (classCodes.equals("ORGNAIZATIONS_AND_ISSUES")) {
				DomainDetailsTitle = getParameterizedMessage("title_organizations");
				screenTitle = getParameterizedMessage("label_search") + " " + getParameterizedMessage("title_organizations");
				searchWithClassFlag = false;
			}
		}

		init();
		domainsList = new ArrayList<SetupDomain>();
	}

	/**
	 * Initialize/Reset Variables
	 */
	public void init() {
		domainDescription = null;
		classDescription = null;
	}

	/**
	 * Search Domains
	 */
	public void searchDomains() {
		try {
			domainsList.clear();
			String[] classesCodeList;
			if (mode == 1) {
				classesCodeList = new String[0];
				domainsList = SetupService.getDomains(domainDescription, classesCodeList, classDescription);
			} else if (mode == 2) {
				classesCodeList = classCodes.split(",");
				// searchDepartmentList = DepartmentService.getDepartmentsBydepartmentTypes(searchDepName, searchDepCode, longTypesList);
				domainsList = SetupService.getDomains(domainDescription, classesCodeList, classDescription);
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	// setters and getters
	public int getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public String getDomainDescription() {
		return domainDescription;
	}

	public void setDomainDescription(String domainDescription) {
		this.domainDescription = domainDescription;
	}

	public String getClassCode() {
		return classCodes;
	}

	public void setClassCodes(String classCodes) {
		this.classCodes = classCodes;
	}

	public String getClassDescription() {
		return classDescription;
	}

	public void setClassDescription(String classDescription) {
		this.classDescription = classDescription;
	}

	public List<SetupDomain> getDomainsList() {
		return domainsList;
	}

	public void setDomainsList(List<SetupDomain> domainsList) {
		this.domainsList = domainsList;
	}

	public String getClassDetailsTitle() {
		return classDetailsTitle;
	}

	public void setClassDetailsTitle(String classDetailsTitle) {
		this.classDetailsTitle = classDetailsTitle;
	}

	public String getDomainDetailsTitle() {
		return DomainDetailsTitle;
	}

	public void setDomainDetailsTitle(String domainDetailsTitle) {
		DomainDetailsTitle = domainDetailsTitle;
	}

	public boolean isSearchWithClassFlag() {
		return searchWithClassFlag;
	}

	public void setSearchWithClassFlag(boolean searchWithClassOnlyFlag) {
		this.searchWithClassFlag = searchWithClassOnlyFlag;
	}

	public String getScreenTitle() {
		return screenTitle;
	}

	public void setScreenTitle(String screenTitle) {
		this.screenTitle = screenTitle;
	}
}