package com.code.ui.backings.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.setup.DepartmentData;
import com.code.dal.orm.setup.RankData;
import com.code.enums.CategoryEnum;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.LabCheckEmployeeCheckStatusEnum;
import com.code.enums.WFPositionsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.labcheck.LabCheckService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.SetupService;
import com.code.services.workflow.WFPositionService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "promotionsEligibleReport")
@ViewScoped
public class PromotionsEligibleReport extends BaseBacking implements Serializable {
	private Long regionId;
	private String employeeRegionName;
	private List<DepartmentData> regionDepartmentsList = new ArrayList<DepartmentData>();
	private Long categoryId;
	private Long rank;
	private List<RankData> ranksList = new ArrayList<RankData>();
	private Date promotionEligibleLastDate;
	private Date promotionExecutionDate;
	private Integer labCheckStatus;
	private Integer labCheckResult;
	private boolean checkReusltFlag = true;

	/**
	 * Constructor
	 */
	public PromotionsEligibleReport() {
		super();
		init();
	}

	/**
	 * Reset Search Fields
	 */
	public void init() {
		try {
			categoryId = getSolider();
			rank = (long) FlagsEnum.ALL.getCode();
			promotionEligibleLastDate = null;
			promotionExecutionDate = null;
			labCheckStatus = FlagsEnum.ALL.getCode();
			labCheckResult = FlagsEnum.ALL.getCode();
			regionDepartmentsList = new ArrayList<DepartmentData>();
			Long empRegionId = DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());
			if (empRegionId == null) {
				regionDepartmentsList.add(DepartmentService.getDepartment(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId()));
				regionDepartmentsList.addAll(DepartmentService.getDepartmentsBydepartmentType(DepartmentTypeEnum.REGION.getCode()));
				regionId = (long) FlagsEnum.ALL.getCode();
			} else {
				employeeRegionName = DepartmentService.getDepartment(empRegionId).getArabicName();
				regionId = empRegionId;
			}
			ranksList = SetupService.getRanksByCategoryId(categoryId);
			checkReusltFlag = false;
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(PromotionsEligibleReport.class, e, "PromotionsEligibleReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void onCategoryChange() {
		try {
			ranksList = SetupService.getRanksByCategoryId(categoryId);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	public void onLabCheckStatusChange() {
		labCheckResult = FlagsEnum.ALL.getCode();
		if (labCheckStatus.equals(getLabCheckExist())) {
			checkReusltFlag = true;
		} else {
			checkReusltFlag = false;
		}
	}

	/**
	 * Print Report Using Given Fields
	 */
	public void printReport() {
		if (promotionEligibleLastDate == null || promotionExecutionDate == null) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_mandatory"));
			return;
		}
		try {
			String regionName = getParameterizedMessage("label_all");
			if (!regionId.equals((long) FlagsEnum.ALL.getCode())) {
				regionName = DepartmentService.getDepartment(regionId).getArabicName();
			}
			String categoryName = getParameterizedMessage("label_all");
			if (!categoryId.equals((long) FlagsEnum.ALL.getCode())) {
				if (categoryId.equals(getOfficer()))
					categoryName = getParameterizedMessage("label_officer");
				else if (categoryId.equals(getSolider()))
					categoryName = getParameterizedMessage("label_solider");
			}
			String rankDesc = getParameterizedMessage("label_all");
			if (!rank.equals((long) FlagsEnum.ALL.getCode())) {
				rankDesc = SetupService.getRankByRankId(rank).getDescription();
			}
			String labCheckStatusName = getParameterizedMessage("label_all");
			if (!labCheckStatus.equals(FlagsEnum.ALL.getCode())) {
				if (labCheckStatus.equals(getLabCheckExist()))
					labCheckStatusName = getParameterizedMessage("label_labCheckExist");
				else if (labCheckStatus.equals(getLabCheckNotExist()))
					labCheckStatusName = getParameterizedMessage("label_labCheckNotExist");
			}
			String labCheckResultName = getParameterizedMessage("label_all");
			if (!labCheckResult.equals(FlagsEnum.ALL.getCode())) {
				if (labCheckResult.equals(getNoSampleCheckResult()))
					labCheckResultName = getParameterizedMessage("label_noSample");
				else if (labCheckResult.equals(getNegativeCheckResult()))
					labCheckResultName = getParameterizedMessage("label_negative");
				else if (labCheckResult.equals(getPositiveCheckResult()))
					labCheckResultName = getParameterizedMessage("label_positive");
				else if (labCheckResult.equals(getPositiveUnderApproveCheckResult()))
					labCheckResultName = getParameterizedMessage("label_positiveUnderApprove");
				else if (labCheckResult.equals(getSampleTakenCheckResult()))
					labCheckResultName = getParameterizedMessage("label_sampleTakenAndWaitResult");
			}
			byte[] bytes;
			if(labCheckStatus.equals(getAll())){
				bytes = LabCheckService.getAllPromotionsEligibleReportBytes(regionId, categoryId, rank, promotionEligibleLastDate, promotionExecutionDate, regionName, categoryName, rankDesc, labCheckStatusName, loginEmpData.getFullName());
			}else if(labCheckStatus.equals(getLabCheckExist())){
				bytes = LabCheckService.getWithLabCheckPromotionsEligibleReportBytes(regionId, categoryId, rank, promotionEligibleLastDate, promotionExecutionDate, labCheckResult, regionName, categoryName, rankDesc, labCheckStatusName, labCheckResultName, loginEmpData.getFullName());
			}else{
				bytes = LabCheckService.getWithoutLabCheckPromotionsEligibleReportBytes(regionId, categoryId, rank, promotionEligibleLastDate, promotionExecutionDate, regionName, categoryName, rankDesc, labCheckStatusName, loginEmpData.getFullName());
			}
			super.print(bytes, "Promotions Eligible Report");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(PromotionsEligibleReport.class, e, "PromotionsEligibleReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public String getEmployeeRegionName() {
		return employeeRegionName;
	}

	public void setEmployeeRegionName(String employeeRegionName) {
		this.employeeRegionName = employeeRegionName;
	}

	public List<DepartmentData> getRegionDepartmentsList() {
		return regionDepartmentsList;
	}

	public void setRegionDepartmentsList(List<DepartmentData> regionDepartmentsList) {
		this.regionDepartmentsList = regionDepartmentsList;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getRank() {
		return rank;
	}

	public void setRank(Long rank) {
		this.rank = rank;
	}

	public List<RankData> getRanksList() {
		return ranksList;
	}

	public void setRanksList(List<RankData> ranksList) {
		this.ranksList = ranksList;
	}

	public Date getPromotionEligibleLastDate() {
		return promotionEligibleLastDate;
	}

	public void setPromotionEligibleLastDate(Date promotionEligibleLastDate) {
		this.promotionEligibleLastDate = promotionEligibleLastDate;
	}

	public Date getPromotionExecutionDate() {
		return promotionExecutionDate;
	}

	public void setPromotionExecutionDate(Date promotionExecutionDate) {
		this.promotionExecutionDate = promotionExecutionDate;
	}

	public Integer getLabCheckStatus() {
		return labCheckStatus;
	}

	public void setLabCheckStatus(Integer labCheckStatus) {
		this.labCheckStatus = labCheckStatus;
	}

	public Integer getLabCheckResult() {
		return labCheckResult;
	}

	public void setLabCheckResult(Integer labCheckResult) {
		this.labCheckResult = labCheckResult;
	}

	public boolean isCheckReusltFlag() {
		return checkReusltFlag;
	}

	public Long getOfficer() {
		return CategoryEnum.OFFICER.getCode();
	}

	public Long getSolider() {
		return CategoryEnum.SOLIDER.getCode();
	}

	public Integer getAll() {
		return FlagsEnum.ALL.getCode();
	}

	public Integer getLabCheckNotExist() {
		return FlagsEnum.OFF.getCode();
	}

	public Integer getLabCheckExist() {
		return FlagsEnum.ON.getCode();
	}

	public Integer getNoSampleCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.NO_SAMPLE.getCode();
	}

	public Integer getNegativeCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.NEGATIVE.getCode();
	}

	public Integer getPositiveCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.POSITIVE.getCode();
	}

	public Integer getPositiveUnderApproveCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.POSITIVE_UNDER_APPROVE.getCode();
	}
	
	public Integer getSampleTakenCheckResult() {
		 return LabCheckEmployeeCheckStatusEnum.SAMPLE_TAKED.getCode();
	}

}
