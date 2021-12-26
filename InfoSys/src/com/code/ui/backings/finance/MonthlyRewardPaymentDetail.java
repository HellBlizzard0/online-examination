package com.code.ui.backings.finance;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.assignment.AssignmentDetailData;
import com.code.enums.PaymentMethodEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.assignment.AssignmentNonEmployeeService;
import com.code.services.log4j.Log4j;
import com.code.ui.backings.base.WFBaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "mnthRewardDetail")
@ViewScoped
public class MonthlyRewardPaymentDetail extends WFBaseBacking implements Serializable {
	private List<AssignmentDetailData> assignmentDetailDataList;
	private int mode;

	public MonthlyRewardPaymentDetail() {
		super();
		init();
		if (getRequest().getParameter("mode") != null && !getRequest().getParameter("mode").isEmpty() && !getRequest().getParameter("mode").equals("undefined")) {
			mode = Integer.parseInt(getRequest().getParameter("mode"));
		}
		Long regionId = null;
		if (getRequest().getParameter("regionId") != null && !getRequest().getParameter("regionId").isEmpty() && !getRequest().getParameter("regionId").equals("undefined")) {
			regionId = Long.parseLong(getRequest().getParameter("regionId"));
		}
		Integer month = null;
		if (getRequest().getParameter("month") != null && !getRequest().getParameter("month").isEmpty() && !getRequest().getParameter("month").equals("undefined")) {
			month = Integer.parseInt(getRequest().getParameter("month"));
		}
		Integer hijriYear = null;
		if (getRequest().getParameter("hijriYear") != null && !getRequest().getParameter("hijriYear").isEmpty() && !getRequest().getParameter("hijriYear").equals("undefined")) {
			hijriYear = Integer.parseInt(getRequest().getParameter("hijriYear"));
		}
		try {
			if (regionId != null && month !=null && hijriYear !=null) {
				assignmentDetailDataList = AssignmentNonEmployeeService.getRegionAssignmentDetails(regionId, month, hijriYear);
			}
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(MonthlyRewardPaymentDetail.class, e, "MonthlyRewardPaymentDetail");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public List<AssignmentDetailData> getAssignmentDetailDataList() {
		return assignmentDetailDataList;
	}

	public void setAssignmentDetailDataList(List<AssignmentDetailData> assignmentDetailDataList) {
		this.assignmentDetailDataList = assignmentDetailDataList;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public Integer getBankAccountEnum() {
		return PaymentMethodEnum.BANK_ACCOUNT.getCode();
	}

	public Integer getChequeAcountEnum() {
		return PaymentMethodEnum.CHEQUE.getCode();
	}

	public Integer getCashAccountEnum() {
		return PaymentMethodEnum.CASH.getCode();
	}
}
