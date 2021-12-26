package com.code.ui.backings.labcheck;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.labcheck.LabCheckReport;
import com.code.dal.orm.labcheck.LabCheckReportData;
import com.code.dal.orm.setup.DepartmentData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.NavigationEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.labcheck.MissionReportService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.SetupService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "missionReportsSearch")
@ViewScoped
public class MissionReportsSearch extends BaseBacking implements Serializable{
	private LabCheckReport labCheckReportSearchData;
	private List<LabCheckReportData> labCheckReportDataResultList;
	private List<SetupDomain> labCheckMissionTypes;
	private List<DepartmentData> regionList;
	
	public MissionReportsSearch(){
		try {
			labCheckReportSearchData = new LabCheckReport();
			labCheckMissionTypes=SetupService.getDomains(ClassesEnum.LAB_CHECK_MISSIONS.getCode());
			setRegionList(DepartmentService.getDepartmentsBydepartmentType(DepartmentTypeEnum.REGION.getCode()));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(MissionReportsSearch.class, e, "MissionReportsSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}
	
	public void search() {
		try {
			if(labCheckReportSearchData.getMissionTypeId()==null)
				labCheckReportSearchData.setMissionTypeId((long)FlagsEnum.ALL.getCode());
			if(labCheckReportSearchData.getDestinationDepartmentId()==null)
				labCheckReportSearchData.setDestinationDepartmentId((long)FlagsEnum.ALL.getCode());
			labCheckReportDataResultList = MissionReportService.getLabCheck(labCheckReportSearchData.getStartDate(), labCheckReportSearchData.getEndDate(), labCheckReportSearchData.getDestinationDepartmentId(), labCheckReportSearchData.getMissionTypeId());
			if (labCheckReportDataResultList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(MissionReportsSearch.class, e, "MissionReportsSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}
	
	public String view(long labCheckReportId) {
		getRequest().setAttribute("mode", String.valueOf(labCheckReportId));
		return NavigationEnum.VIEW_LAB_CHECK_REPORT.toString();
	}
	
	public void deleteMissionReport(LabCheckReportData labCheckReportData){
		try {
			MissionReportService.deleteMissionReport(labCheckReportData.getLabCheckReport(), loginEmpData);
			labCheckReportDataResultList.remove(labCheckReportData);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(MissionReportsSearch.class, e, "MissionReportsSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void reset() {
		labCheckReportSearchData = new LabCheckReport();
	}
	
	public LabCheckReport getLabCheckReportSearchData() {
		return labCheckReportSearchData;
	}

	public void setLabCheckReportSearchData(LabCheckReport labCheckReportSearchData) {
		this.labCheckReportSearchData = labCheckReportSearchData;
	}

	public List<SetupDomain> getLabCheckMissionTypes() {
		return labCheckMissionTypes;
	}

	public void setLabCheckMissionTypes(List<SetupDomain> labCheckMissionTypes) {
		this.labCheckMissionTypes = labCheckMissionTypes;
	}
	public List<DepartmentData> getRegionList() {
		return regionList;
	}
	public void setRegionList(List<DepartmentData> regionList) {
		this.regionList = regionList;
	}

	public List<LabCheckReportData> getLabCheckReportDataResultList() {
		return labCheckReportDataResultList;
	}

	public void setLabCheckReportDataResultList(List<LabCheckReportData> labCheckReportDataResultList) {
		this.labCheckReportDataResultList = labCheckReportDataResultList;
	}
}
