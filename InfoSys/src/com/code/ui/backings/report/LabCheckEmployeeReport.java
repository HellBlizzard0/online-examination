package com.code.ui.backings.report;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.labcheck.LabCheckEmployeeData;
import com.code.enums.LabCheckReasonsEnum;
import com.code.enums.LabCheckStatusEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.labcheck.LabCheckService;
import com.code.services.log4j.Log4j;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "labCheckEmployeeReport")
@ViewScoped
public class LabCheckEmployeeReport extends BaseBacking implements Serializable {
	private Long employeeId;
	private String employeeName;

	/**
	 * Constructor
	 */
	public LabCheckEmployeeReport() {
		super();
	}

	/**
	 * Print Report Using Given Fields
	 */
	public void print() {
		String endTreatmentDate = "";
		if (employeeId == null) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_mandatory"));
			return;
		}
		try {
			LabCheckEmployeeData labCheckEmployeeData = LabCheckService.getLastLabCheckEmpByEmpIdAndCheckReason(employeeId, LabCheckReasonsEnum.TREATMENT_PURPOSE.getCode(), LabCheckStatusEnum.APPROVED.getCode());
			if (labCheckEmployeeData != null && labCheckEmployeeData.getOrderDate() != null) {
				endTreatmentDate = HijriDateService.addSubStringHijriMonthsDays(HijriDateService.getHijriDateString(labCheckEmployeeData.getOrderDate()), 6, 0);
			}
			byte[] bytes = LabCheckService.getLabCheckEmployeeBytes(employeeId, endTreatmentDate, LabCheckStatusEnum.APPROVED.getCode(), loginEmpData.getFullName());
			String reportName = "Lab Check Employee Report";
			super.print(bytes, reportName);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckEmployeeReport.class, e, "LabCheckEmployeeReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Reset Search Fields
	 */
	public void reset() {
		employeeId = null;
		employeeName = null;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

}
