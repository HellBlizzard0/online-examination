package com.code.ui.backings.minisearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.labcheck.LabCheck;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.enums.LabCheckReasonsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.labcheck.LabCheckService;
import com.code.services.setup.SetupService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "labCheckMiniSearch")
@ViewScoped
public class LabCheckMiniSearch extends BaseBacking implements Serializable {
	private String orderNo;
	private Date orderFromDate;
	private Date orderToDate;
	private Long orderSourceDomainId;
	private List<SetupDomain> orderSourcesList;
	private Integer checkReason;
	private List<LabCheck> labCheckList;
	private int rowsCount = 10;

	/**
	 * Default Constructor Initializer
	 */
	public LabCheckMiniSearch() {
		try {
			init();
			orderSourcesList = SetupService.getDomains(ClassesEnum.ORDER_SOURCES.getCode());
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Initialize/Reset Variables
	 */
	public void init() {
		orderNo = null;
		orderFromDate = null;
		orderToDate = null;
		orderSourceDomainId = null;
		checkReason = null;
		labCheckList = new ArrayList<LabCheck>();
	}

	/**
	 * Search Lab Checks
	 */
	public void searchLabChecks() {
		try {
			labCheckList = LabCheckService.getApprovedLabCheck(orderNo, orderFromDate, orderToDate, orderSourceDomainId, checkReason);
			if (labCheckList.isEmpty()) {
				this.setServerSideSuccessMessages(getParameterizedMessage("error_noData"));
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	// setters and getters
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Date getOrderFromDate() {
		return orderFromDate;
	}

	public void setOrderFromDate(Date orderFromDate) {
		this.orderFromDate = orderFromDate;
	}

	public Date getOrderToDate() {
		return orderToDate;
	}

	public void setOrderToDate(Date orderToDate) {
		this.orderToDate = orderToDate;
	}

	public Long getOrderSourceDomainId() {
		return orderSourceDomainId;
	}

	public void setOrderSourceDomainId(Long orderSourceDomainId) {
		this.orderSourceDomainId = orderSourceDomainId;
	}

	public List<SetupDomain> getOrderSourcesList() {
		return orderSourcesList;
	}

	public void setOrderSourcesList(List<SetupDomain> orderSourcesList) {
		this.orderSourcesList = orderSourcesList;
	}

	public Integer getCheckReason() {
		return checkReason;
	}

	public void setCheckReason(Integer checkReason) {
		this.checkReason = checkReason;
	}

	public List<LabCheck> getLabCheckList() {
		return labCheckList;
	}

	public void setLabCheckList(List<LabCheck> labCheckList) {
		this.labCheckList = labCheckList;
	}

	public int getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}

	public List<LabCheckReasonsEnum> getLabCheckReasonsEnumList() {
		return LabCheckReasonsEnum.getAllCheckReasonsEnumValues();
	}

}