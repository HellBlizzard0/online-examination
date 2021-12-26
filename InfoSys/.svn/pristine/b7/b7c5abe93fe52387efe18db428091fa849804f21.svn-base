package com.code.ui.backings.surveillance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.activity.IntegrationEmployeeActivityData;
import com.code.dal.orm.setup.SetupClass;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.enums.FlagsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.log4j.Log4j;
import com.code.services.setup.SetupService;
import com.code.services.util.CommonService;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "employeeActivites")
@ViewScoped
public class EmployeeActivites extends BaseBacking implements Serializable {
	private String searchSocialId;
	private String searchName;
	private List<SetupClass> classesList = new ArrayList<SetupClass>();
	private String searchClassDescription;
	private List<SetupDomain> domainsList = new ArrayList<SetupDomain>();
	private String searchDomainDescription;
	private String searchNotes;
	private SetupClass selectedClass;
	private Date searchFromDate;
	private Date searchToDate;
	private final int defaultPeriodInMonth = -3;
	private List<IntegrationEmployeeActivityData> employeeActivityDataList;

	/**
	 * Constructor
	 */
	public EmployeeActivites() {
		super();
		init();
	}

	/**
	 * Initialize/Reset search variables
	 */
	public void init() {
		try {
			this.classesList = new ArrayList<SetupClass>();
			SetupClass fishingClass = SetupService.getClasses(ClassesEnum.FISHING.getCode(), null, FlagsEnum.ALL.getCode()).get(0);
			this.classesList.add(fishingClass);
			this.domainsList = SetupService.getDomains(fishingClass.getId(), null, FlagsEnum.ON.getCode());
			this.employeeActivityDataList = new ArrayList<IntegrationEmployeeActivityData>();
			searchSocialId = null;
			selectedClass = null;
			searchName = null;
			searchClassDescription = null;
			searchDomainDescription = null;
			searchToDate = HijriDateService.getHijriSysDate();
			searchFromDate = HijriDateService.addSubHijriMonthsDays(searchToDate, defaultPeriodInMonth, 0);
			searchNotes = null;
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Search Class
	 */
	public void search() {
		try {
			this.employeeActivityDataList = CommonService.getEmployeeActivity(searchSocialId, searchName, searchClassDescription, searchDomainDescription, HijriDateService.getLTRHijriDateString(searchFromDate), HijriDateService.getLTRHijriDateString(searchToDate), searchNotes);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * 
	 */
	public void updateDomainList() {
		try {
			SetupClass setupClass = SetupService.getClasses(null, searchClassDescription, FlagsEnum.ALL.getCode()).get(0);
			this.domainsList = SetupService.getDomains(setupClass.getId(), null, FlagsEnum.ON.getCode());
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Print Report
	 */
	public void printResults() {
		try {
			byte[] bytes = CommonService.getEmployeeActivityBytes(searchSocialId == null || searchSocialId.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : searchSocialId, searchName == null || searchName.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : searchName, searchClassDescription == null ? FlagsEnum.ALL.getCode() + "" : searchClassDescription, searchDomainDescription == null ? FlagsEnum.ALL.getCode() + "" : searchDomainDescription,
					HijriDateService.getLTRHijriDateString(searchFromDate) == null ? FlagsEnum.ALL.getCode() + "" : HijriDateService.getLTRHijriDateString(searchFromDate), HijriDateService.getLTRHijriDateString(searchToDate) == null ? FlagsEnum.ALL.getCode() + "" : HijriDateService.getLTRHijriDateString(searchToDate), searchNotes == null || searchNotes.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : searchNotes, loginEmpData.getFullName());
			super.print(bytes, "employeeEvalEndDetail");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(EmployeeActivites.class, e, "EmployeeActivites");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	// Setters and getters
	public List<SetupClass> getClassesList() {
		return classesList;
	}

	public String getSearchSocialId() {
		return searchSocialId;
	}

	public void setSearchSocialId(String searchSocialId) {
		this.searchSocialId = searchSocialId;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
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

	public SetupClass getSelectedClass() {
		return selectedClass;
	}

	public void setSelectedClass(SetupClass selectedClass) {
		this.selectedClass = selectedClass;
	}

	public String getSearchClassDescription() {
		return searchClassDescription;
	}

	public void setSearchClassDescription(String searchClassDescription) {
		this.searchClassDescription = searchClassDescription;
	}

	public String getSearchDomainDescription() {
		return searchDomainDescription;
	}

	public void setSearchDomainDescription(String searchDomainDescription) {
		this.searchDomainDescription = searchDomainDescription;
	}

	public Date getSearchFromDate() {
		return searchFromDate;
	}

	public void setSearchFromDate(Date searchFromDate) {
		this.searchFromDate = searchFromDate;
	}

	public Date getSearchToDate() {
		return searchToDate;
	}

	public void setSearchToDate(Date searchToDate) {
		this.searchToDate = searchToDate;
	}

	public List<IntegrationEmployeeActivityData> getEmployeeActivityDataList() {
		return employeeActivityDataList;
	}

	public void setEmployeeActivityDataList(List<IntegrationEmployeeActivityData> employeeActivityDataList) {
		this.employeeActivityDataList = employeeActivityDataList;
	}

	public String getSearchNotes() {
		return searchNotes;
	}

	public void setSearchNotes(String searchNotes) {
		this.searchNotes = searchNotes;
	}
}
