package com.code.ui.backings.setup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.setup.SetupClass;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.FlagsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.setup.SetupService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "setups")
@ViewScoped
public class Setups extends BaseBacking implements Serializable {
	private List<SetupClass> classesList = new ArrayList<SetupClass>();

	private SetupClass setupClass;
	private List<SetupDomain> domainsList = new ArrayList<SetupDomain>();
	private String domainDescription;
	private SetupClass selectedClass;
	private SetupDomain setupDomain;
	private boolean domainPaneExpandFlag;
	private int setupDomainSelectedIndex;

	/**
	 * Constructor
	 */
	public Setups() {
		super();
		selectedClass = null;
		init();
	}

	/**
	 * Initialize/Reset search variables
	 */
	public void init() {
		setupClass = new SetupClass();
		resetDomain();
	}

	/**
	 * Search Class
	 */
	public void searchClass() {
		domainsList.clear();
		selectedClass = null;
		try {
			classesList = SetupService.getClasses(setupClass.getCode(), setupClass.getDescription(), setupClass.getType() == null ? FlagsEnum.ALL.getCode() : setupClass.getType());
			if (classesList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * View Domain
	 * 
	 * @param setupClass
	 */
	public void viewDomains(SetupClass setupClass) {
		try {
			selectedClass = setupClass;
			if (setupClass.getId() == null) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
				domainsList.clear();
				resetDomain();
				return;
			}
			domainsList = SetupService.getDomains(setupClass.getId(), null, FlagsEnum.ALL.getCode());
			if (domainsList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
			}
			resetDomain();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Reset domain
	 */
	public void resetDomain() {
		setupDomain = new SetupDomain();
		setupDomainSelectedIndex = FlagsEnum.ALL.getCode();
		domainPaneExpandFlag = false;
	}

	/**
	 * handle saving domain
	 */
	public void saveDomain() {
		saveUpdateDomain();
		setupDomain = new SetupDomain();
		domainPaneExpandFlag = true;
	}

	/**
	 * Save or update Domain
	 */
	public void saveUpdateDomain() {
		try {
			if (setupDomain.getId() == null) {
				setupDomain.setClassId(selectedClass.getId());
				setupDomain.setSecurityAnalysisFlag(false);
				SetupService.saveDomain(setupDomain, this.loginEmpData);
				domainsList.add(setupDomain);
			} else {
				SetupService.updateDomain(setupDomain, this.loginEmpData);
				domainsList.set(setupDomainSelectedIndex, setupDomain);
			}
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Edit domain
	 * 
	 * @param choosenSetupDomain
	 * @param index
	 */
	public void editSetupDomain(SetupDomain choosenSetupDomain, int index) {
		try {
			setupDomain = (SetupDomain) choosenSetupDomain.clone();
			setupDomainSelectedIndex = index;
			domainPaneExpandFlag = true;
		} catch (CloneNotSupportedException e) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_general", null));
		}
	}

	// Setters and getters
	public List<SetupClass> getClassesList() {
		return classesList;
	}

	public void setClassesList(List<SetupClass> classes) {
		this.classesList = classes;
	}

	public List<SetupDomain> getDomainsList() {
		return domainsList;
	}

	public void setDomainsList(List<SetupDomain> domains) {
		this.domainsList = domains;
	}

	public String getDomainDescription() {
		return domainDescription;
	}

	public void setDomainDescription(String domainDescription) {
		this.domainDescription = domainDescription;
	}

	public SetupClass getSelectedClass() {
		return selectedClass;
	}

	public void setSelectedClass(SetupClass selectedClass) {
		this.selectedClass = selectedClass;
	}

	public SetupClass getSetupClass() {
		return setupClass;
	}

	public void setSetupClass(SetupClass setupClass) {
		this.setupClass = setupClass;
	}

	public SetupDomain getSetupDomain() {
		return setupDomain;
	}

	public void setSetupDomain(SetupDomain setupDomain) {
		this.setupDomain = setupDomain;
	}

	public boolean isDomainPaneExpandFlag() {
		return domainPaneExpandFlag;
	}

	public void setDomainPaneExpandFlag(boolean domainPaneExpandFlag) {
		this.domainPaneExpandFlag = domainPaneExpandFlag;
	}

	public int getSetupDomainSelectedIndex() {
		return setupDomainSelectedIndex;
	}

	public void setSetupDomainSelectedIndex(int setupDomainSelectedIndex) {
		this.setupDomainSelectedIndex = setupDomainSelectedIndex;
	}
}