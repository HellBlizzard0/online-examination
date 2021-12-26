package com.code.ui.backings.report;

import java.io.Serializable;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.exceptions.BusinessException;
import com.code.services.infosys.labcheck.LabCheckService;
import com.code.services.log4j.Log4j;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "labCheckByOrderNoReport")
@ViewScoped
public class LabCheckBasedOnOrderNoReport extends BaseBacking implements Serializable {
	private String orderNumber;
	private Date orderDate;

	/**
	 * Constructor
	 */
	public LabCheckBasedOnOrderNoReport() {
		super();
	}

	/**
	 * Print Report Using Given Fields
	 */
	public void printReport() {
		if (orderDate == null || orderNumber == null || orderNumber.isEmpty()) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_mandatory"));
			return;
		}
		try {
			if (LabCheckService.getLastLabCheckEmployeeDataByOrderNoAndOrderDate(orderDate, orderNumber) == null) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_labCheckNotFound"));
				return;
			}
			byte[] bytes = LabCheckService.getLabCheckReportBytesByOrderNoAndOrderDate(orderNumber, orderDate, loginEmpData.getFullName());
			super.print(bytes, "Lab Check Based On Order Number Report");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckBasedOnOrderNoReport.class, e, "LabCheckBasedOnOrderNoReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Reset Search Fields
	 */
	public void reset() {
		orderNumber = "";
		orderDate = null;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
}
