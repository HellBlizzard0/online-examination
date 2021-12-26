package com.code.ui.backings.minisearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.setup.SetupClass;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.FlagsEnum;
import com.code.enums.SecurityAnalysisClassesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.setup.SetupService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "externalSidesMiniSearch")
@ViewScoped
public class ExternalSidesMiniSearch extends BaseBacking implements Serializable {

	private int rowsCount = 10;
	private SetupDomain searchDomain;
	private SetupDomain insertSetupDomain;
	private List<SetupDomain> setupDomainList;
	private SetupClass setupClass;

	public ExternalSidesMiniSearch() {
		super();
		try {
			searchDomain = new SetupDomain();
			setupDomainList = new ArrayList<SetupDomain>();
			insertSetupDomain = new SetupDomain();
			setupClass = SetupService.getSecurityAnalysisClasses(SecurityAnalysisClassesEnum.EXTERNAL_SIDES.getCode(), null, FlagsEnum.ALL.getCode()).get(0);
		} catch (BusinessException e) {
			setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	public void searchExternalDomains() {
		try {
			setupDomainList = SetupService.getSecurityAnalysisDomainsByDomainDescAndClassCode(searchDomain.getDescription(), SecurityAnalysisClassesEnum.EXTERNAL_SIDES.getCode());
			if (setupDomainList.isEmpty()) {
				setServerSideErrorMessages(getParameterizedMessage("error_noData"));
			}
			insertSetupDomain = new SetupDomain();
		} catch (BusinessException e) {
			setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * handle saving domain
	 */
	public void saveDomain() {
		try {
			insertSetupDomain.setClassId(setupClass.getId());
			insertSetupDomain.setSecurityAnalysisFlag(true);
			SetupService.saveDomain(insertSetupDomain, this.loginEmpData);
			setupDomainList.add(0, insertSetupDomain);
			insertSetupDomain = new SetupDomain();
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * reset search Domain
	 */
	public void resetSearch() {
		searchDomain = new SetupDomain();
	}

	public int getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}

	public SetupDomain getSearchDomain() {
		return searchDomain;
	}

	public void setSearchDomain(SetupDomain searchDomain) {
		this.searchDomain = searchDomain;
	}

	public List<SetupDomain> getSetupDomainList() {
		return setupDomainList;
	}

	public void setSetupDomainList(List<SetupDomain> setupDomainList) {
		this.setupDomainList = setupDomainList;
	}

	public SetupDomain getInsertSetupDomain() {
		return insertSetupDomain;
	}

	public void setInsertSetupDomain(SetupDomain insertSetupDomain) {
		this.insertSetupDomain = insertSetupDomain;
	}

	public SetupClass getSetupClass() {
		return setupClass;
	}

	public void setSetupClass(SetupClass setupClass) {
		this.setupClass = setupClass;
	}
}
