package com.code.ui.backings.finance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.finance.FinInfoReward;
import com.code.dal.orm.finance.FinYearApproval;
import com.code.dal.orm.info.InfoData;
import com.code.dal.orm.setup.DepartmentData;
import com.code.enums.InfoSourceTypeEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.WFPositionsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.assignment.AssignmentService;
import com.code.services.infosys.finance.FinanceAccountsService;
import com.code.services.infosys.finance.InfoRewardPaymentService;
import com.code.services.infosys.info.InfoService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.workflow.WFPositionService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "infoReward")
@ViewScoped
public class InfoRewardPayment extends BaseBacking implements Serializable {
	private FinYearApproval finYearApproval;
	private FinInfoReward finInfoReward;
	private List<InfoData> infoDataLsit;
	private String sectorName;
	private Long sectorId;
	private String sourceName;
	private Integer sourceType = 1;
	private boolean paymnetMethod;
	private Long selectedInfoId;
	private DepartmentData regionDepartment;
	private boolean isHeadQuarter;
	private double regionAmount;
	private String errorMessage;

	public InfoRewardPayment() {
		super();
		init();
		finInfoReward = new FinInfoReward();
		infoDataLsit = new ArrayList<InfoData>();
		try {
			finYearApproval = FinanceAccountsService.getFinYearApproval(Calendar.getInstance().get(Calendar.YEAR));
			if (finYearApproval == null) {
				errorMessage = getParameterizedMessage("error_finYearApprovalNotRegistered");
				return;
			}
			finInfoReward.setFinYearApprovalId(finYearApproval.getId());
			DepartmentData loginDep = DepartmentService.getDepartment(loginEmpData.getActualDepartmentId());
			regionDepartment = DepartmentService.getDepartment(loginDep.getRegionId());
			if (regionDepartment.getId().equals(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId())) {
				regionAmount = FinanceAccountsService.calculateDepartmentSupportNetBalance(regionDepartment.getId(), finInfoReward.getFinYearApprovalId());
				isHeadQuarter = true;
				paymnetMethod = true;
			} else {
				regionAmount = 0.0;
			}

		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRewardPayment.class, e, "InfoRewardPayment");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void resetSource() {
		sourceName = null;
		sectorId = null;
		sectorName = null;
		selectedInfoId = null;
		finInfoReward.setAssignmentDetailId(null);
		infoDataLsit = new ArrayList<InfoData>();
		paymnetMethod = true;
		calculateRegionAmount();
	}

	public void calculateRegionAmount() {
		try {
			// TODO SHERIF : Check business
			if (paymnetMethod || AssignmentService.getAssignmentDetailsDataById(finInfoReward.getAssignmentDetailId()).equals(InfoSourceTypeEnum.COOPERATOR.getCode())) {
				finInfoReward.setDepartmentId(regionDepartment.getId());
			} else {
				finInfoReward.setDepartmentId(sectorId);
			}
			if (finInfoReward.getDepartmentId() != null) {
				regionAmount = FinanceAccountsService.calculateDepartmentSupportNetBalance(finInfoReward.getDepartmentId(), finInfoReward.getFinYearApprovalId());
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRewardPayment.class, e, "InfoRewardPayment");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void emptyInfoList() {
		infoDataLsit = new ArrayList<InfoData>();
		calculateRegionAmount();
	}

	public void addInfo() {
		for (InfoData info : infoDataLsit) {
			if (info.getId().equals(selectedInfoId)) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_alreadyChoosen"));
				return;
			}
		}

		try {
			InfoData info = InfoService.getInfoDataById(selectedInfoId);
			infoDataLsit.add(info);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRewardPayment.class, e, "InfoRewardPayment");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public String save() {
		try {
			// TODO SHERIF : Check business
			if (paymnetMethod || AssignmentService.getAssignmentDetailsDataById(finInfoReward.getAssignmentDetailId()).equals(InfoSourceTypeEnum.COOPERATOR.getCode())) {
				finInfoReward.setDepartmentId(regionDepartment.getId());
			} else {
				finInfoReward.setDepartmentId(sectorId);
			}
			InfoRewardPaymentService.saveFinInfoReward(finInfoReward, infoDataLsit, loginEmpData);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
			return NavigationEnum.INBOX.toString();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(InfoRewardPayment.class, e, "InfoRewardPayment");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
		return null;
	}

	/********************************************************** Setters And Getters **********************************************/
	public FinYearApproval getFinYearApproval() {
		return finYearApproval;
	}

	public void setFinYearApproval(FinYearApproval finYearApproval) {
		this.finYearApproval = finYearApproval;
	}

	public FinInfoReward getFinInfoReward() {
		return finInfoReward;
	}

	public void setFinInfoReward(FinInfoReward finInfoReward) {
		this.finInfoReward = finInfoReward;
	}

	public List<InfoData> getInfoDataLsit() {
		return infoDataLsit;
	}

	public void setInfoDataLsit(List<InfoData> infoDataLsit) {
		this.infoDataLsit = infoDataLsit;
	}

	public String getSectorName() {
		return sectorName;
	}

	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	public boolean isPaymnetMethod() {
		return paymnetMethod;
	}

	public void setPaymnetMethod(boolean paymnetMethod) {
		this.paymnetMethod = paymnetMethod;
	}

	public Long getSelectedInfoId() {
		return selectedInfoId;
	}

	public void setSelectedInfoId(Long selectedInfoId) {
		this.selectedInfoId = selectedInfoId;
	}

	public Long getSectorId() {
		return sectorId;
	}

	public void setSectorId(Long sectorId) {
		this.sectorId = sectorId;
	}

	public boolean isHeadQuarter() {
		return isHeadQuarter;
	}

	public void setHeadQuarter(boolean isHeadQuarter) {
		this.isHeadQuarter = isHeadQuarter;
	}

	public DepartmentData getRegionDepartment() {
		return regionDepartment;
	}

	public void setRegionDepartment(DepartmentData regionDepartment) {
		this.regionDepartment = regionDepartment;
	}

	public Integer getAssignmentType() {
		return InfoSourceTypeEnum.ASSIGNMENT.getCode();
	}

	public Integer getCooperatorType() {
		return InfoSourceTypeEnum.COOPERATOR.getCode();
	}

	public double getRegionAmount() {
		return regionAmount;
	}

	public void setRegionAmount(double regionAmount) {
		this.regionAmount = regionAmount;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
