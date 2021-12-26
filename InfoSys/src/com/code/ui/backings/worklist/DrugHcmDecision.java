package com.code.ui.backings.worklist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.labcheck.LabCheck;
import com.code.dal.orm.labcheck.LabCheckEmployeeData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.enums.LabCheckEmployeeCheckStatusEnum;
import com.code.enums.NavigationEnum;
import com.code.exceptions.BusinessException;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.labcheck.LabCheckService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.EmployeeService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.labcheck.LabCheckWorkFlow;
import com.code.ui.backings.base.WFBaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "drugHcmDecision")
@ViewScoped
public class DrugHcmDecision extends WFBaseBacking implements Serializable {
	private LabCheckEmployeeData labCheckEmployeeData;
	private LabCheck labCheck;
	private int retestMonthNumber;
	private EmployeeData admin;

	/**
	 * Constructor
	 * 
	 * @throws BusinessException
	 */
	public DrugHcmDecision() throws Exception {
		super();
		init();
		admin = EmployeeService.getEmployee(InfoSysConfigurationService.getSystemAdmin());
		labCheck = LabCheckService.getLabCheckByWfInstanceId(currentTask.getInstanceId());
		List<LabCheckEmployeeData> tempLabCheckEmpData = LabCheckService.getLabCheckEmployesByLabCheckId(labCheck.getId(), null);
		if (!tempLabCheckEmpData.isEmpty()) {
			labCheckEmployeeData = tempLabCheckEmpData.get(0);
			retestMonthNumber = HijriDateService.hijriDateDiff(labCheckEmployeeData.getSampleDate(), labCheckEmployeeData.getPeriodicRetestDate()) / 29;
		}
	}

	/**
	 * Take old result
	 * 
	 * @return
	 */
	public String takeOldResult() {
		try {
			LabCheckWorkFlow.takeOldResultAndCloseInstance(currentTask, labCheck, loginEmpData);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Create new approved order
	 * 
	 * @return
	 * @throws CloneNotSupportedException
	 */
	public String createNewApprovedOrder() throws CloneNotSupportedException {
		try {
			List<LabCheckEmployeeData> employeeDataList = new ArrayList<LabCheckEmployeeData>();
			LabCheckEmployeeData tempLabCheckEmployeeData = (LabCheckEmployeeData) labCheckEmployeeData.clone();
			tempLabCheckEmployeeData.setCheckStatus(LabCheckEmployeeCheckStatusEnum.NO_SAMPLE.getCode());
			tempLabCheckEmployeeData.setSampleDate(null);
			tempLabCheckEmployeeData.setSampleNumber(null);
			tempLabCheckEmployeeData.setNationalForceSampleSentDate(null);
			tempLabCheckEmployeeData.setNationalForceSampleNumber(null);
			tempLabCheckEmployeeData.setDomainCureHospitalDesc(null);
			tempLabCheckEmployeeData.setDomainCureHospitalId(null);
			tempLabCheckEmployeeData.setDomainMaterialTypeDescripttion(null);
			tempLabCheckEmployeeData.setDomainMaterialTypeId(null);
			tempLabCheckEmployeeData.setRetestNumber(1);
			tempLabCheckEmployeeData.setRetested(false);
			employeeDataList.add(tempLabCheckEmployeeData);
			labCheck.setIntegration(true);
			LabCheckWorkFlow.initApprovedLabCheckAndApproveTask(currentTask, labCheck, employeeDataList, admin);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		} catch (Exception e) {
			   Log4j.traceErrorException(DrugHcmDecision.class, e, "DrugHcmDecision");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		return NavigationEnum.INBOX.toString();
	}

	public Integer getNegativeCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.NEGATIVE.getCode();
	}

	public Integer getPositiveCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.POSITIVE.getCode();
	}

	public LabCheckEmployeeData getLabCheckEmployeeData() {
		return labCheckEmployeeData;
	}

	public void setLabCheckEmployeeData(LabCheckEmployeeData labCheckEmployeeData) {
		this.labCheckEmployeeData = labCheckEmployeeData;
	}

	public LabCheck getLabCheck() {
		return labCheck;
	}

	public void setLabCheck(LabCheck labCheck) {
		this.labCheck = labCheck;
	}

	public int getRetestMonthNumber() {
		return retestMonthNumber;
	}

	public void setRetestMonthNumber(int retestMonthNumber) {
		this.retestMonthNumber = retestMonthNumber;
	}
}
