package com.code.ui.backings.minisearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.info.InfoCoordinate;
import com.code.dal.orm.info.InfoData;
import com.code.enums.FlagsEnum;
import com.code.enums.InfoImportanceEnum;
import com.code.enums.InfoRelatedEntityTypeEnum;
import com.code.enums.InfoSourceTypeEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.info.InfoService;
import com.code.services.log4j.Log4j;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.info.InfoWorkFlow;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "infoMiniSearch")
@ViewScoped
public class InfoMiniSearch extends BaseBacking implements Serializable {
	private int rowsCount = 10;
	private int page = 1;
	private int mode;

	private String infoNumberSearch;
	private String agentCodeSearch;
	private List<InfoData> resultInfoData;
	private InfoData selectedInfo;

	private boolean isAuthDep;
	InfoCoordinate infoCoordinate;

	/**
	 * Constructor
	 */
	public InfoMiniSearch() {
		super();
		init();
		if (!getRequest().getParameter("mode").equals("null") && !getRequest().getParameter("mode").isEmpty() && !getRequest().getParameter("mode").equals("undefined")) {
			mode = Integer.parseInt(getRequest().getParameter("mode"));
		}
		Date saveDate = null;
		if (getRequest().getParameter("saveDate") != null && !getRequest().getParameter("saveDate").equals("null") && !getRequest().getParameter("saveDate").isEmpty() && !getRequest().getParameter("saveDate").equals("undefined")) {
			saveDate = HijriDateService.getHijriDate(getRequest().getParameter("saveDate"));
		}
		resultInfoData = new ArrayList<InfoData>();

		try {
			isAuthDep = InfoWorkFlow.isIntelleigenceOrAnalysisDepartment(loginEmpData.getActualDepartmentId());

			if (mode == 2) {// search Info By ASSIGNMENT , COOPERATOR and OPEN_SOURCE InfoSource
				long sourceId = Long.parseLong(getRequest().getParameter("param1"));
				long sourceType = Integer.parseInt(getRequest().getParameter("param2"));

				if (sourceType == InfoSourceTypeEnum.OPEN_SOURCE.getCode()) {
					if (isAuthDep) {
						resultInfoData = InfoService.getInfoDataByOpenSourceId(sourceId, saveDate, loginEmpData);
					} else {
						resultInfoData = InfoService.getInfoDataByOpenSourceIdByAnalysis(sourceId, saveDate, loginEmpData);
					}
				} else {
					if (isAuthDep) {
						resultInfoData = InfoService.getInfoDataByAssignmentId(sourceId, saveDate, loginEmpData);
					} else {
						resultInfoData = InfoService.getInfoDataByAssignmentIdByAnalysis(sourceId, saveDate, loginEmpData);
					}
				}
			} else if (mode == 3) {
				long subjectId = getRequest().getParameter("param1").equals("null") ? FlagsEnum.ALL.getCode() : Long.valueOf(getRequest().getParameter("param1"));
				long typeId = getRequest().getParameter("param2").equals("null") ? FlagsEnum.ALL.getCode() : Long.valueOf(getRequest().getParameter("param2"));
				if (isAuthDep) {
					resultInfoData = InfoService.getInfoDataBySubjectIdAndTypeId(subjectId, typeId, saveDate, loginEmpData);
				} else {
					resultInfoData = InfoService.getInfoDataBySubjectIdAndTypeIdByAnalysis(subjectId, typeId, saveDate, loginEmpData);
				}
			} else if (mode == 4) {
				long entityId = Long.parseLong(getRequest().getParameter("param1"));
				long entityType = Integer.parseInt(getRequest().getParameter("param2"));

				if (entityType == InfoRelatedEntityTypeEnum.COUNTRY.getCode()) {
					if (isAuthDep) {
						resultInfoData = InfoService.getInfoDataRelatedEntity(entityId, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), saveDate, loginEmpData);
					} else {
						resultInfoData = InfoService.getInfoDataRelatedEntityByAnalysis(entityId, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), saveDate, loginEmpData);
					}
				} else if (entityType == InfoRelatedEntityTypeEnum.DEPARTMENT.getCode()) {
					if (isAuthDep) {
						resultInfoData = InfoService.getInfoDataRelatedEntity(FlagsEnum.ALL.getCode(), entityId, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), saveDate, loginEmpData);
					} else {
						resultInfoData = InfoService.getInfoDataRelatedEntityByAnalysis(FlagsEnum.ALL.getCode(), entityId, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), saveDate, loginEmpData);
					}
				} else if (entityType == InfoRelatedEntityTypeEnum.DOMAIN.getCode()) {
					if (isAuthDep) {
						resultInfoData = InfoService.getInfoDataRelatedEntity(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), entityId, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), saveDate, loginEmpData);
					} else {
						resultInfoData = InfoService.getInfoDataRelatedEntityByAnalysis(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), entityId, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), saveDate, loginEmpData);
					}
				} else if (entityType == InfoRelatedEntityTypeEnum.EMPLOYEE.getCode()) {
					if (isAuthDep) {
						resultInfoData = InfoService.getInfoDataRelatedEntity(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), entityId, FlagsEnum.ALL.getCode(), saveDate, loginEmpData);
					} else {
						resultInfoData = InfoService.getInfoDataRelatedEntityByAnalysis(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), entityId, FlagsEnum.ALL.getCode(), saveDate, loginEmpData);
					}
				} else if (entityType == InfoRelatedEntityTypeEnum.NON_EMPLOYEE.getCode()) {
					if (isAuthDep) {
						resultInfoData = InfoService.getInfoDataRelatedEntity(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), entityId, saveDate, loginEmpData);
					} else {
						resultInfoData = InfoService.getInfoDataRelatedEntityByAnalysis(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), entityId, saveDate, loginEmpData);
					}
				}
			} else if (mode == 5) {// serach Info By ASSIGNMENT and COOPERATOR InfoSource
				long sourceId = Long.parseLong(getRequest().getParameter("param1"));
				resultInfoData = InfoService.getInfoDataByAssignmentId(sourceId, saveDate, loginEmpData);

			} else if (mode == 6) {
				String phoneNumber = getRequest().getParameter("param1");
				if (isAuthDep) {
					resultInfoData = InfoService.getInfoPhone(phoneNumber, loginEmpData);
				} else {
					resultInfoData = InfoService.getInfoPhoneByAnalysis(phoneNumber, loginEmpData);
				}
			}

		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoMiniSearch.class, e, "InfoMiniSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Search for information
	 */
	public void searchInfo() {
		try {
			resultInfoData.clear();
			if (agentCodeSearch == null || agentCodeSearch.trim().equals("") || agentCodeSearch.isEmpty()) {
				if (isAuthDep) {
					resultInfoData = InfoService.getInfoData(infoNumberSearch, null, null, null, loginEmpData, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
				} else {
					resultInfoData = InfoService.getInfoDataByAnalysis(infoNumberSearch, null, null, null, loginEmpData, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
				}
			} else {
				if (isAuthDep) {
					resultInfoData = InfoService.getInfoDataByAgentCodeAndInfoNumber(agentCodeSearch, infoNumberSearch, loginEmpData);
				} else {
					resultInfoData = InfoService.getInfoDataByAgentCodeAndInfoNumberByAnalysis(agentCodeSearch, infoNumberSearch, loginEmpData);
				}
			}

			if (resultInfoData.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
				return;
			}
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Show Info Details
	 */
	public void showInfoDetails(InfoData infoRow) {
		selectedInfo = infoRow;
		infoCoordinate = new InfoCoordinate();
		try {
			List<InfoCoordinate> coordinatesList = InfoService.getInfoCoordinates(infoRow.getId());
			if (!coordinatesList.isEmpty()) {
				infoCoordinate = InfoService.getInfoCoordinates(infoRow.getId()).get(0);
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Reset search parameters
	 */
	public void resetSearch() {
		infoNumberSearch = null;
		agentCodeSearch = null;
	}

	// setters and getters
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

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public List<InfoData> getResultInfoData() {
		return resultInfoData;
	}

	public void setResultInfoData(List<InfoData> resultInfoData) {
		this.resultInfoData = resultInfoData;
	}

	public String getInfoNumberSearch() {
		return infoNumberSearch;
	}

	public void setInfoNumberSearch(String infoNumberSearch) {
		this.infoNumberSearch = infoNumberSearch;
	}

	public String getAgentCodeSearch() {
		return agentCodeSearch;
	}

	public void setAgentCodeSearch(String agentCodeSearch) {
		this.agentCodeSearch = agentCodeSearch;
	}

	public InfoData getSelectedInfo() {
		return selectedInfo;
	}

	public void setSelectedInfo(InfoData selectedInfo) {
		this.selectedInfo = selectedInfo;
	}

	public InfoCoordinate getInfoCoordinate() {
		return infoCoordinate;
	}

	public void setInfoCoordinate(InfoCoordinate infoCoordinate) {
		this.infoCoordinate = infoCoordinate;
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
}