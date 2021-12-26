package com.code.ui.backings.worklist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.code.dal.orm.setup.LocationProfileData;
import com.code.dal.orm.setup.SetupClass;
import com.code.dal.orm.setup.SetupDomain;
import com.code.dal.orm.workflow.WFTaskData;
import com.code.enums.ClassesEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.WFProcessesEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.setup.SetupService;
import com.code.services.util.CommonService;
import com.code.services.workflow.BaseWorkFlow;
import com.code.ui.backings.base.BaseBacking;

@ManagedBean(name = "fishingNotificationInquiry")
@SessionScoped
public class FishingNotificationInquiry extends BaseBacking {
	private List<WFTaskData> tasks;
	private String searchByTaskOwnerName;
	private long searchByAssigneeId;
	private String searchByTaskDetails;
	private String searchBySocialId;
	private String searchByName;
	private String searchByDomainDescription;
	private String searchByNavigationDock;
	private Date fromDate;
	private Date toDate;

	private List<SetupDomain> domainsList = new ArrayList<SetupDomain>();
	private List<LocationProfileData> navDocksList = new ArrayList<LocationProfileData>();

	private List<WFTaskData> withResendTasks;
	private List<WFTaskData> withoutResendTasks;

	private Date hijriPassingDate;
	private Long passingEmpOriginalId;

	private int pageSize = 10;

	public FishingNotificationInquiry() {
		super();
		this.setScreenTitle(getParameterizedMessage("title_fishingNotification"));
		init();
	}

	public void init() {
		super.init();
		try {
			SetupClass fishingClass = SetupService.getClasses(ClassesEnum.FISHING.getCode(), null, FlagsEnum.ALL.getCode()).get(0);
			this.domainsList = SetupService.getDomains(fishingClass.getId(), null, FlagsEnum.ON.getCode());
			this.navDocksList = CommonService.getLocations();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return;
		}
		tasks = new ArrayList<WFTaskData>();
		searchByTaskOwnerName = "";
		searchByTaskDetails = "";
		searchByAssigneeId = this.loginEmpData.getEmpId();
		searchBySocialId = "";
		searchByName = "";
		searchByDomainDescription = "";
		searchByNavigationDock = "";
		fromDate = null;
		toDate = null;
		withResendTasks = new ArrayList<WFTaskData>();
		withoutResendTasks = new ArrayList<WFTaskData>();
		passingEmpOriginalId = null;
		hijriPassingDate = null;
	}

	/**
	 * 
	 * Searching for Fishing Notifications
	 * 
	 */
	public void search() {
		try {
			tasks = BaseWorkFlow.getWFFishingTasksData(searchByAssigneeId, searchByTaskOwnerName, searchByTaskDetails, searchBySocialId, searchByName, searchByDomainDescription, searchByNavigationDock,WFProcessesEnum.FIHSING_AND_EXCURSIONS.getCode(), fromDate, toDate);
		} catch (BusinessException e) {
			tasks = new ArrayList<WFTaskData>();
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage()));
			return;
		}
	}


	/**
	 * Getting fishing notifications for details dialog
	 * 
	 * @param taskIntanceId
	 */
	public void fishingNotificationDetails(long taskIntanceId) {
		try {
			withoutResendTasks = BaseWorkFlow.getWFTasksDataByInstanceIdAndAssigneeWfRole(taskIntanceId, WFTaskRolesEnum.NOTIFICATION_WITHOUT_RESEND.getCode());
			withResendTasks = BaseWorkFlow.getWFTasksDataByInstanceIdAndAssigneeWfRole(taskIntanceId, WFTaskRolesEnum.NOTIFICATION.getCode());
			if (!withoutResendTasks.isEmpty()) {
				hijriPassingDate = withoutResendTasks.get(0).getHijriAssignDate();
				passingEmpOriginalId = withoutResendTasks.get(0).getOriginalId();
			}
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	// --------------------------------------------------#Setters & Getters---------------------------------------------------------------

	public void setTasks(List<WFTaskData> tasks) {
		this.tasks = tasks;
	}

	public List<WFTaskData> getTasks() {
		return tasks;
	}

	public void setSearchByTaskOwnerName(String searchByTaskOwnerName) {
		this.searchByTaskOwnerName = searchByTaskOwnerName;
	}

	public String getSearchByTaskOwnerName() {
		return searchByTaskOwnerName;
	}

	public String getSearchBySocialId() {
		return searchBySocialId;
	}

	public void setSearchBySocialId(String searchBySocialId) {
		this.searchBySocialId = searchBySocialId;
	}

	public String getSearchByName() {
		return searchByName;
	}

	public void setSearchByName(String searchByName) {
		this.searchByName = searchByName;
	}

	public String getSearchByDomainDescription() {
		return searchByDomainDescription;
	}

	public void setSearchByDomainDescription(String searchByDomainDescription) {
		this.searchByDomainDescription = searchByDomainDescription;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	public String getBoolServerUploadPath() {
		return InfoSysConfigurationService.getBoolServerUploadPath();
	}

	public String getSearchByTaskDetails() {
		return searchByTaskDetails;
	}

	public void setSearchByTaskDetails(String searchByTaskDetails) {
		this.searchByTaskDetails = searchByTaskDetails;
	}

	public String getSearchByNavigationDock() {
		return searchByNavigationDock;
	}

	public void setSearchByNavigationDock(String searchByNavigationDock) {
		this.searchByNavigationDock = searchByNavigationDock;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public List<SetupDomain> getDomainsList() {
		return domainsList;
	}

	public void setDomainsList(List<SetupDomain> domainsList) {
		this.domainsList = domainsList;
	}

	public List<LocationProfileData> getNavDocksList() {
		return navDocksList;
	}

	public void setNavDocksList(List<LocationProfileData> navDocksList) {
		this.navDocksList = navDocksList;
	}

	public List<WFTaskData> getWithResendTasks() {
		return withResendTasks;
	}

	public void setWithResendTasks(List<WFTaskData> withResendTasks) {
		this.withResendTasks = withResendTasks;
	}

	public List<WFTaskData> getWithoutResendTasks() {
		return withoutResendTasks;
	}

	public void setWithoutResendTasks(List<WFTaskData> withoutResendTasks) {
		this.withoutResendTasks = withoutResendTasks;
	}

	public Date getHijriPassingDate() {
		return hijriPassingDate;
	}

	public void setHijriPassingDate(Date hijriPassingDate) {
		this.hijriPassingDate = hijriPassingDate;
	}

	public Long getPassingEmpOriginalId() {
		return passingEmpOriginalId;
	}

	public void setPassingEmpOriginalId(Long passingEmpOriginalId) {
		this.passingEmpOriginalId = passingEmpOriginalId;
	}
}
