package com.code.ui.backings.labcheck;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.labcheck.LabCheck;
import com.code.dal.orm.labcheck.LabCheckEmployeeData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.enums.LabCheckEmployeeCheckStatusEnum;
import com.code.enums.LabCheckReasonsEnum;
import com.code.enums.LabCheckStatusEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.labcheck.LabCheckService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.SetupService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.labcheck.LabCheckWorkFlow;
import com.code.ui.backings.base.WFBaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "labCheckEd")
@ViewScoped
public class LabCheckEdit extends WFBaseBacking implements Serializable {
	private LabCheck labCheck;
	private LabCheckEmployeeData labCheckEmployeeData;
	private LabCheck labCheckTemp;
	private LabCheckEmployeeData labCheckEmployeeDataTemp;
	private Long selectedEmpId;
	private List<SetupDomain> orderSourcesList;
	private List<SetupDomain> domainMetrialTypeList;
	private List<SetupDomain> domainCurmentHospitalsList;
	private Integer retestMonthNumber;
	private Integer retestMonthNumberBeforeEd;
	private boolean orderNumberEditedFLag;
	private int pageMode;

	public LabCheckEdit() {
		super();
		init();
		labCheck = new LabCheck();

		try {
			domainMetrialTypeList = SetupService.getDomains(ClassesEnum.MATERIAL_TYPES.getCode());
			orderSourcesList = SetupService.getDomains(ClassesEnum.ORDER_SOURCES.getCode());
			domainCurmentHospitalsList = SetupService.getDomains(ClassesEnum.HEALTH_CARE_CANTERS.getCode());

			if (getRequest().getAttribute("labCheckEmployeeDataId") != null) {
				labCheckEmployeeData = LabCheckService.getLabCheckEmployeeDataById((Long) getRequest().getAttribute("labCheckEmployeeDataId"));
				labCheck = LabCheckService.getLabCheck(labCheckEmployeeData.getLabCheckId());
			}

			if (currentTask != null) {
				labCheck = LabCheckService.getLabCheckByWfInstanceId(currentTask.getInstanceId());
				labCheckEmployeeData = LabCheckService.getLabCheckEmployeeDataById(Long.parseLong(currentTask.getFlexField1()));
			}
			if (labCheckEmployeeData.getPeriodicRetestDate() != null) {
				retestMonthNumber = HijriDateService.hijriDateDiff(labCheckEmployeeData.getSampleDateBeforeEdit() != null ? labCheckEmployeeData.getSampleDateBeforeEdit() : labCheckEmployeeData.getSampleDate(), labCheckEmployeeData.getPeriodicRetestDate()) / 29;
			}
			if (labCheckEmployeeData.getPeriodicRetestDateBeforeEdit() != null) {
				retestMonthNumberBeforeEd = HijriDateService.hijriDateDiff(labCheckEmployeeData.getSampleDateBeforeEdit() != null ? labCheckEmployeeData.getSampleDateBeforeEdit() : labCheckEmployeeData.getSampleDate(), labCheckEmployeeData.getPeriodicRetestDateBeforeEdit()) / 29;
			}
			labCheckEmployeeDataTemp = (LabCheckEmployeeData) labCheckEmployeeData.clone();
			labCheckTemp = (LabCheck) labCheck.clone();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (CloneNotSupportedException e) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckEdit.class, e, "LabCheckEdit");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}
	
	public void save() {
		try {
			for (SetupDomain domain : orderSourcesList) {
				if (domain.getId().equals(labCheck.getOrderSourceDomainId())) {
					labCheck.setOrderSourceDomainDescription(domain.getDescription());
					break;
				}
			}
			LabCheckWorkFlow.editLabCheck(labCheck, labCheckEmployeeData, labCheckEmployeeDataTemp, loginEmpData);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckEdit.class, e, "LabCheckEdit");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }

	}

	/**
	 * save And init workflow
	 */
	public String saveAndSend() {
		try {
			for (SetupDomain domain : orderSourcesList) {
				if (domain.getId().equals(labCheck.getOrderSourceDomainId())) {
					labCheck.setOrderSourceDomainDescription(domain.getDescription());
					break;
				}
			}
			if(labCheckEmployeeData.getCheckStatus()== getNegativeCheckResult() && labCheckEmployeeData.getNationalForceSampleNumber() == null){
				labCheckEmployeeData.setDomainMaterialTypeId(null);
				labCheckEmployeeData.setDomainMaterialTypeDescripttion(null);
			}
			labCheckEmployeeData.setPeriodicRetestDate(retestMonthNumber == null ? null : HijriDateService.addSubHijriMonthsDays(labCheckEmployeeData.getSampleDate(), retestMonthNumber, 0));
			LabCheckWorkFlow.editLabCheckAndInitWorkflow(labCheck, labCheckTemp, labCheckEmployeeData, labCheckEmployeeDataTemp, loginEmpData, currentTask);
		} catch (BusinessException e) {
			labCheckEmployeeData.setSampleNumber(labCheckEmployeeDataTemp.getSampleNumber());
			labCheckEmployeeData.setNationalForceSampleNumber(labCheckEmployeeDataTemp.getNationalForceSampleNumber());
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   labCheckEmployeeData.setSampleNumber(labCheckEmployeeDataTemp.getSampleNumber());
			   labCheckEmployeeData.setNationalForceSampleNumber(labCheckEmployeeDataTemp.getNationalForceSampleNumber());
			   Log4j.traceErrorException(LabCheckEdit.class, e, "LabCheckEdit");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Anti Drug Department Manager Approve
	 * 
	 * @return
	 */
	public String doApproveDM() {
		try {
			LabCheckWorkFlow.doApproveDMLabCheckEdit(currentTask, labCheck, labCheckEmployeeData, loginEmpData, null);
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckEdit.class, e, "LabCheckEdit");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * doReject
	 * 
	 * @return
	 */
	public String doReject() {
		try {
			LabCheckWorkFlow.doRejectLabCheckEdit(currentTask, labCheck, labCheckEmployeeData, loginEmpData, null);
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckEdit.class, e, "LabCheckEdit");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Closed Notification
	 * 
	 * @return
	 */
	public String doNotify() {
		try {
			LabCheckWorkFlow.doNotify(currentTask);
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckEdit.class, e, "LabCheckEdit");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return null;
		  }
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Print report
	 */
	public void print() {
		try {
			byte[] bytes = LabCheckService.getLabCheckDetailsReportBytes(labCheck.getId(), loginEmpData.getFullName());
			super.print(bytes, "Lab_Check_Details");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckEdit.class, e, "LabCheckEdit");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void checkOrderNumberChange() {
		orderNumberEditedFLag = false;
		if (!(labCheck.getOrderNumber().equals(labCheckTemp.getOrderNumber()))) {
			try {
				if (LabCheckService.getCountLabCheckEmployees(labCheck.getId()) > 1L) {
					orderNumberEditedFLag = true;
				}
			} catch (BusinessException e) {
				this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			}catch (Exception e) {
				   Log4j.traceErrorException(LabCheckEdit.class, e, "LabCheckEdit");
				   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			  }
		}
	}
	
	public void labCheckStatusChange() {
		if(!labCheckEmployeeData.getCheckStatus().equals(getPositiveUnderApproveCheckResult()))
			labCheckEmployeeData.setDomainMaterialTypeId(null);
	}

	/********************************************************** Setters And Getters **********************************************/
	public LabCheck getLabCheck() {
		return labCheck;
	}

	public void setLabCheck(LabCheck labCheck) {
		this.labCheck = labCheck;
	}

	public LabCheckEmployeeData getLabCheckEmployeeData() {
		return labCheckEmployeeData;
	}

	public void setLabCheckEmployeeData(LabCheckEmployeeData labCheckEmployeeData) {
		this.labCheckEmployeeData = labCheckEmployeeData;
	}

	public Long getSelectedEmpId() {
		return selectedEmpId;
	}

	public void setSelectedEmpId(Long selectedEmpId) {
		this.selectedEmpId = selectedEmpId;
	}

	public String getRegionDirector() {
		return WFTaskRolesEnum.REGION_DIRECTOR.getCode();
	}

	public String getGeneralDirector() {
		return WFTaskRolesEnum.GENERAL_DIRECTOR.getCode();
	}

	public String getRegionAntiDrugManager() {
		return WFTaskRolesEnum.REGION_ANTI_DRUG_MANAGER.getCode();
	}

	public String getRegionSecurityManager() {
		return WFTaskRolesEnum.REGION_SECURITY_INTELLIGENCE_MANAGER.getCode();
	}

	public String getGeneralAntiDrugManager() {
		return WFTaskRolesEnum.GENERAL_ANTI_DRUG_MANAGER.getCode();
	}

	public String getNotification() {
		return WFTaskRolesEnum.NOTIFICATION.getCode();
	}

	public String getRequesterRole() {
		return WFTaskRolesEnum.REQUESTER.getCode();
	}

	public Integer getNoSampleCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.NO_SAMPLE.getCode();
	}
	
	public Integer getSampleTakedCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.SAMPLE_TAKED.getCode();
	}

	public Integer getRetestCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.RETEST.getCode();
	}

	public Integer getCheckPrivationCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.CHECK_PRIVATION.getCode();
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

	public Integer getSendingToForceCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.SENDING_TO_FORCE.getCode();
	}
	
	public Integer getCheatingCheckResult() {
		return LabCheckEmployeeCheckStatusEnum.CHEATING.getCode();
	}
	
	public Integer getRegisteredLabCheckStatus() {
		return LabCheckStatusEnum.REGISTERED.getCode();
	}

	public List<SetupDomain> getOrderSourcesList() {
		return orderSourcesList;
	}

	public void setOrderSourcesList(List<SetupDomain> orderSourcesList) {
		this.orderSourcesList = orderSourcesList;
	}

	public List<SetupDomain> getDomainMetrialTypeList() {
		return domainMetrialTypeList;
	}

	public void setDomainMetrialTypeList(List<SetupDomain> domainMetrialTypeList) {
		this.domainMetrialTypeList = domainMetrialTypeList;
	}

	public List<SetupDomain> getDomainCurmentHospitalsList() {
		return domainCurmentHospitalsList;
	}

	public void setDomainCurmentHospitalsList(List<SetupDomain> domainCurmentHospitalsList) {
		this.domainCurmentHospitalsList = domainCurmentHospitalsList;
	}

	public Integer getRetestMonthNumber() {
		return retestMonthNumber;
	}

	public void setRetestMonthNumber(Integer retestMonthNumber) {
		this.retestMonthNumber = retestMonthNumber;
	}

	public Integer getRetestMonthNumberBeforeEd() {
		return retestMonthNumberBeforeEd;
	}

	public void setRetestMonthNumberBeforeEd(Integer retestMonthNumberBeforeEd) {
		this.retestMonthNumberBeforeEd = retestMonthNumberBeforeEd;
	}

	public boolean isOrderNumberEditedFLag() {
		return orderNumberEditedFLag;
	}

	public void setOrderNumberEditedFLag(boolean orderNumberEditedFLag) {
		this.orderNumberEditedFLag = orderNumberEditedFLag;
	}

	public int getPageMode() {
		return pageMode;
	}

	public void setPageMode(int pageMode) {
		this.pageMode = pageMode;
	}

	public Integer getCasesReason() {
		return LabCheckReasonsEnum.CASES.getCode();
	}
	
	public Integer getMissionFromDirectorateReason() {
		return LabCheckReasonsEnum.RANDOM_MISSION_FROM_DIRECTORATE.getCode();
	}

	public List<LabCheckReasonsEnum> getLabCheckReasonsEnumList() {
		return LabCheckReasonsEnum.getAllCheckReasonsEnumValues();
	}

}