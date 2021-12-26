package com.code.ui.backings.info;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.assignment.AssignmentDetailData;
import com.code.dal.orm.info.InfoAnalysis;
import com.code.dal.orm.info.InfoAnalysisDetailData;
import com.code.dal.orm.info.InfoAnalysisReferenceData;
import com.code.dal.orm.info.InfoCoordinate;
import com.code.dal.orm.info.InfoData;
import com.code.dal.orm.info.InfoPhone;
import com.code.dal.orm.info.InfoRecommendationData;
import com.code.dal.orm.info.OpenSource;
import com.code.dal.orm.setup.CountryData;
import com.code.dal.orm.setup.DepartmentData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.setup.NonEmployeeData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.enums.InfoAnalysisRedirectionTypeEnum;
import com.code.enums.InfoConfidentialityEnum;
import com.code.enums.InfoImportanceEnum;
import com.code.enums.InfoRelatedEntityTypeEnum;
import com.code.enums.InfoSourceTypeEnum;
import com.code.enums.InfoStatusEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.assignment.AssignmentService;
import com.code.services.infosys.info.InfoService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.CountryService;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.EmployeeService;
import com.code.services.setup.NonEmployeeService;
import com.code.services.setup.OpenSourceService;
import com.code.services.setup.SetupService;
import com.code.services.workflow.info.InfoWorkFlow;
import com.code.ui.backings.base.WFBaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "infoView")
@ViewScoped
public class InfoView extends WFBaseBacking implements Serializable {
	private InfoData infoRequest;
	private Integer createInfoType;
	private List<SetupDomain> infoImportanceList;
	private List<SetupDomain> infoConfidentialityList;
	private List<InfoCoordinate> infoCoordinateList;
	private List<InfoPhone> infoPhoneList;
	private List<OpenSource> openSourceList;
	private List<AssignmentDetailData> assignmentList;
	private List<DepartmentData> departmentDataList;
	private List<SetupDomain> setupDomainList;
	private List<CountryData> countryDataList;
	private List<EmployeeData> employeeDataList;
	private List<NonEmployeeData> nonEmployeesDataList;
	private List<InfoRecommendationData> infoRecommendationList;
	private Integer selectedOption;
	private List<InfoAnalysisDetailData> infoAnalysisDetailDataList;
	private List<InfoAnalysis> infoAnalysisList;
	private List<InfoAnalysisReferenceData> analysisRefenceList;
	private List<DepartmentData> visibleDepartments;
	private String infoClosedDate;

	/**
	 * Constructor
	 */
	public InfoView() {
		super();
		init();
		try {

			if (getRequest().getAttribute("mode") != null) {
				infoRequest = InfoService.getInfoDataById((Long) getRequest().getAttribute("mode"));
			} else if (currentTask != null) {
				infoRequest = InfoService.getInfoDataByInstanceId(currentTask.getInstanceId());
			}

			if (infoRequest.getStatus().equals(InfoStatusEnum.DONE.getCode())) {
				infoClosedDate = InfoWorkFlow.getInfoClosedData(infoRequest);
			}

			if (infoRequest != null) {
				if (infoRequest.getRelatedInfoId() == null) {
					createInfoType = 1;
				} else {
					createInfoType = 2;
				}
				infoImportanceList = SetupService.getDomains(ClassesEnum.INFO_PRIORITY.getCode());
				infoConfidentialityList = SetupService.getDomains(ClassesEnum.INFO_CONFIDENTIALITY.getCode());
				infoCoordinateList = InfoService.getInfoCoordinates(infoRequest.getId());
				infoPhoneList = InfoService.getInfoPhones(infoRequest.getId());
				assignmentList = AssignmentService.getInfoAssignments(infoRequest.getId());
				openSourceList = OpenSourceService.getInfoOpenSources(infoRequest.getId());
				departmentDataList = DepartmentService.getInfoRelatedDepartments(infoRequest.getId());
				countryDataList = CountryService.getInfoRelatedCountries(infoRequest.getId());
				setupDomainList = SetupService.getInfoRelatedEntities(infoRequest.getId());
				employeeDataList = EmployeeService.getInfoRelatedEmployees(infoRequest.getId());
				nonEmployeesDataList = NonEmployeeService.getInfoRelatedNonEmployees(infoRequest.getId());
				infoAnalysisList = InfoService.getInfoAnalysis(infoRequest.getId(), InfoAnalysisRedirectionTypeEnum.PROCESSING.getCode());
				infoRecommendationList = InfoService.getInfoRecommendations(infoRequest.getId());
				analysisRefenceList = new ArrayList<InfoAnalysisReferenceData>();
				infoAnalysisDetailDataList = new ArrayList<InfoAnalysisDetailData>();
				if (infoRequest.getLetterCopies() != null)
					selectedOption = 0;
				else if (infoRequest.getStatus().equals(InfoStatusEnum.DONE.getCode()))
					selectedOption = 1;
				visibleDepartments = InfoService.getInfoVisibleDepartments(infoRequest.getId());
			}

		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoView.class, e, "InfoView");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * show Info Analysis Details
	 * 
	 * @param infoAnalysisId
	 */
	public void showInfoAnalysisDetails(Long infoAnalysisId) {
		try {
			infoAnalysisDetailDataList = InfoService.getInfoAnalysisDetails(infoAnalysisId);
			analysisRefenceList = InfoService.getInfoAnalysisReferences(infoAnalysisId);
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoView.class, e, "InfoView");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * print information with details
	 */
	public void print() {
		try {
			byte[] bytes = InfoService.getInformationDetailsBytes(infoRequest.getId(), loginEmpData.getFullName(), infoAnalysisList != null && !infoAnalysisList.isEmpty());
			super.print(bytes, "InformationReport");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoView.class, e, "InfoView");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/********************************************************** Setters And Getters **********************************************/
	public InfoData getInfoRequest() {
		return infoRequest;
	}

	public void setInfoRequest(InfoData infoRequest) {
		this.infoRequest = infoRequest;
	}

	public Integer getCreateInfoType() {
		return createInfoType;
	}

	public void setCreateInfoType(Integer createInfoType) {
		this.createInfoType = createInfoType;
	}

	public List<SetupDomain> getInfoImportanceList() {
		return infoImportanceList;
	}

	public void setInfoImportanceList(List<SetupDomain> infoImportanceList) {
		this.infoImportanceList = infoImportanceList;
	}

	public List<SetupDomain> getInfoConfidentialityList() {
		return infoConfidentialityList;
	}

	public void setInfoConfidentialityList(List<SetupDomain> infoConfidentialityList) {
		this.infoConfidentialityList = infoConfidentialityList;
	}

	public List<InfoCoordinate> getInfoCoordinateList() {
		return infoCoordinateList;
	}

	public void setInfoCoordinateList(List<InfoCoordinate> infoCoordinateList) {
		this.infoCoordinateList = infoCoordinateList;
	}

	public List<InfoPhone> getInfoPhoneList() {
		return infoPhoneList;
	}

	public void setInfoPhoneList(List<InfoPhone> infoPhoneList) {
		this.infoPhoneList = infoPhoneList;
	}

	public Integer getOpenSourceType() {
		return InfoSourceTypeEnum.OPEN_SOURCE.getCode();
	}

	public Integer getCooperatorSourceType() {
		return InfoSourceTypeEnum.COOPERATOR.getCode();
	}

	public Integer getAssignmentSourceType() {
		return InfoSourceTypeEnum.ASSIGNMENT.getCode();
	}

	public List<OpenSource> getOpenSourceList() {
		return openSourceList;
	}

	public void setOpenSourceList(List<OpenSource> openSourceList) {
		this.openSourceList = openSourceList;
	}

	public List<DepartmentData> getDepartmentDataList() {
		return departmentDataList;
	}

	public void setDepartmentDataList(List<DepartmentData> departmentDataList) {
		this.departmentDataList = departmentDataList;
	}

	public List<SetupDomain> getSetupDomainList() {
		return setupDomainList;
	}

	public void setSetupDomainList(List<SetupDomain> setupDomainList) {
		this.setupDomainList = setupDomainList;
	}

	public List<CountryData> getCountryDataList() {
		return countryDataList;
	}

	public void setCountryDataList(List<CountryData> countryDataList) {
		this.countryDataList = countryDataList;
	}

	public List<EmployeeData> getEmployeeDataList() {
		return employeeDataList;
	}

	public void setEmployeeDataList(List<EmployeeData> employeeDataList) {
		this.employeeDataList = employeeDataList;
	}

	public List<NonEmployeeData> getNonEmployeesDataList() {
		return nonEmployeesDataList;
	}

	public void setNonEmployeesDataList(List<NonEmployeeData> nonEmployeesDataList) {
		this.nonEmployeesDataList = nonEmployeesDataList;
	}

	public Integer getRelatedDepartmentType() {
		return InfoRelatedEntityTypeEnum.DEPARTMENT.getCode();
	}

	public Integer getRelatedCountryType() {
		return InfoRelatedEntityTypeEnum.COUNTRY.getCode();
	}

	public Integer getRelatedEmployeeType() {
		return InfoRelatedEntityTypeEnum.EMPLOYEE.getCode();
	}

	public Integer getRelatedSetupDomainType() {
		return InfoRelatedEntityTypeEnum.DOMAIN.getCode();
	}

	public Integer getRelatedNonEmployeeType() {
		return InfoRelatedEntityTypeEnum.NON_EMPLOYEE.getCode();
	}

	public List<AssignmentDetailData> getAssignmentList() {
		return assignmentList;
	}

	public void setAssignmentList(List<AssignmentDetailData> assignmentList) {
		this.assignmentList = assignmentList;
	}

	public List<InfoRecommendationData> getInfoRecommendationList() {
		return infoRecommendationList;
	}

	public void setInfoRecommendationList(List<InfoRecommendationData> infoRecommendationList) {
		this.infoRecommendationList = infoRecommendationList;
	}

	public Integer getSelectedOption() {
		return selectedOption;
	}

	public void setSelectedOption(Integer selectedOption) {
		this.selectedOption = selectedOption;
	}

	public List<InfoAnalysisDetailData> getInfoAnalysisDetailDataList() {
		return infoAnalysisDetailDataList;
	}

	public void setInfoAnalysisDetailDataList(List<InfoAnalysisDetailData> infoAnalysisDetailDataList) {
		this.infoAnalysisDetailDataList = infoAnalysisDetailDataList;
	}

	public List<InfoAnalysis> getInfoAnalysisList() {
		return infoAnalysisList;
	}

	public void setInfoAnalysisList(List<InfoAnalysis> infoAnalysisList) {
		this.infoAnalysisList = infoAnalysisList;
	}

	public List<InfoAnalysisReferenceData> getAnalysisRefenceList() {
		return analysisRefenceList;
	}

	public void setAnalysisRefenceList(List<InfoAnalysisReferenceData> analysisRefenceList) {
		this.analysisRefenceList = analysisRefenceList;
	}

	public List<DepartmentData> getVisibleDepartments() {
		return visibleDepartments;
	}

	public void setVisibleDepartments(List<DepartmentData> visibleDepartments) {
		this.visibleDepartments = visibleDepartments;
	}

	public String getInfoClosedDate() {
		return infoClosedDate;
	}

	public void setInfoClosedDate(String infoClosedDate) {
		this.infoClosedDate = infoClosedDate;
	}

	public Integer getInfoImportanceEnumImmediate() {
		return InfoImportanceEnum.IMMEDIATE.getCode();
	}

	public Integer getInfoImportanceEnumUrgent() {
		return InfoImportanceEnum.URGENT.getCode();
	}

	public Integer getInfoImportanceEnumMedium() {
		return InfoImportanceEnum.MEDIUM.getCode();
	}

	public Integer getInfoImportanceEnumNormal() {
		return InfoImportanceEnum.NORMAL.getCode();
	}

	public Integer getInfoImportanceEnumWithout() {
		return InfoImportanceEnum.WITHOUT.getCode();
	}

	public Integer getInfoConfidentialityEnumForbidden() {
		return InfoConfidentialityEnum.FORBIDDEN.getCode();
	}

	public Integer getInfoConfidentialityEnumVerySecret() {
		return InfoConfidentialityEnum.VERY_SECRET.getCode();
	}

	public Integer getInfoConfidentialityEnumSecret() {
		return InfoConfidentialityEnum.SECRET.getCode();
	}

	public Integer getInfoConfidentialityEnumWithout() {
		return InfoConfidentialityEnum.WITHOUT.getCode();
	}
}
