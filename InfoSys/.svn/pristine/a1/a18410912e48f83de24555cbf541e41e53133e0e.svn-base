package com.code.ui.backings.minisearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.info.OpenSource;
import com.code.exceptions.BusinessException;
import com.code.services.setup.OpenSourceService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "openSourcesMiniSearch")
@ViewScoped
public class OpenSourcesMiniSearch extends BaseBacking implements Serializable {
	private int rowsCount = 10;
	private int mode = 1;

	private OpenSource openSourceSearch;
	private OpenSource openSourceInsert;
	private boolean openSourceExpandFlag;
	private Integer openSourceSelectedIndex;

	private List<OpenSource> openSourcesList;

	/**
	 * Constructor
	 */
	public OpenSourcesMiniSearch() {
		if (!getRequest().getParameter("mode").equals("null") && !getRequest().getParameter("mode").isEmpty() && !getRequest().getParameter("mode").equals("undefined")) {
			mode = Integer.parseInt(getRequest().getParameter("mode"));
		}
		init();
	}

	/**
	 * Initialize/Reset Variables
	 */
	public void init() {
		openSourceSearch = new OpenSource();
		openSourceInsert = new OpenSource();
		openSourceSelectedIndex = null;
		openSourcesList = new ArrayList<OpenSource>();
	}

	/**
	 * Reset Search Parameters
	 */
	public void resetSearch() {
		openSourceSearch = new OpenSource();
		resetCooepratorInsertForm();
	}

	/**
	 * Reset open Source Insert Form
	 */
	public void resetCooepratorInsertForm() {
		openSourceInsert = new OpenSource();
		openSourceSelectedIndex = null;
		openSourceExpandFlag = false;
	}

	/**
	 * Search open sources
	 */
	public void searchOpenSource() {
		try {
			openSourcesList = OpenSourceService.getOpenSources(openSourceSearch.getId(), openSourceSearch.getName());
			if (openSourcesList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Edit open source
	 * 
	 * @param openSource
	 * @param index
	 */
	public void editOpenSource(OpenSource openSource, Integer index) {
		try {
			openSourceInsert = (OpenSource) openSource.clone();
			openSourceSelectedIndex = index;
			openSourceExpandFlag = true;
		} catch (CloneNotSupportedException e) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_general", null));
		}
	}

	/**
	 * Insert new open source
	 */
	public void saveOpenSource() {
		insertUpdateOpenSource(openSourceInsert);
		openSourceInsert = new OpenSource();
		openSourceExpandFlag = true;
	}

	/**
	 * Add new open source
	 */
	public void insertUpdateOpenSource(OpenSource openSource) {
		try {
			if (openSource.getId() == null) {
				OpenSourceService.saveOpenSource(openSource, loginEmpData);
			} else {
				OpenSourceService.updateOpenSource(openSource, loginEmpData);
				openSourcesList.set(openSourceSelectedIndex, openSource);
			}
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Delete openSource
	 * 
	 * @param openSource
	 */
	public void deleteOpenSource(OpenSource openSource) {
		try {
			OpenSourceService.deleteOpenSource(openSource, loginEmpData);
			openSourcesList.remove(openSource);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	// setters and getters
	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}

	public int getRowsCount() {
		return rowsCount;
	}

	public OpenSource getOpenSourceSearch() {
		return openSourceSearch;
	}

	public void setOpenSourceSearch(OpenSource openSourceSearch) {
		this.openSourceSearch = openSourceSearch;
	}

	public OpenSource getOpenSourceInsert() {
		return openSourceInsert;
	}

	public void setOpenSourceInsert(OpenSource openSourceInsert) {
		this.openSourceInsert = openSourceInsert;
	}

	public List<OpenSource> getOpenSourcesList() {
		return openSourcesList;
	}

	public void setOpenSourcesList(List<OpenSource> openSourcesList) {
		this.openSourcesList = openSourcesList;
	}

	public boolean isOpenSourceExpandFlag() {
		return openSourceExpandFlag;
	}

	public void setOpenSourceExpandFlag(boolean openSourceCollapsFlag) {
		this.openSourceExpandFlag = openSourceCollapsFlag;
	}
}
