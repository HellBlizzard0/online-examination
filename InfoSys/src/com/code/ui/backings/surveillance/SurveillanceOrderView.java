package com.code.ui.backings.surveillance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.setup.SetupClass;
import com.code.dal.orm.setup.SetupDomain;
import com.code.dal.orm.surveillance.SurveillanceEmpNonEmpData;
import com.code.dal.orm.surveillance.SurveillanceOrderData;
import com.code.enums.ClassesEnum;
import com.code.enums.FlagsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.surveillance.SurveillanceOrdersService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.SetupService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "surveillanceOrderView")
@ViewScoped
public class SurveillanceOrderView extends BaseBacking implements Serializable {
	private SurveillanceOrderData surveillanceOrderData;
	private List<String> surveillanceOrderReasonsList;
	private List<String> surveillanceOrderSelectedReasonsList;
	private List<SurveillanceEmpNonEmpData> surveillanceEmployeesList;

	/**
	 * Constructor
	 */
	public SurveillanceOrderView() {
		this.init();
		try {
			Long surveillanceOrderId = null;
			// Set Reasons List
			List<SetupClass> setupClass = SetupService.getClasses(ClassesEnum.SURVEILLANCE_REASONS.getCode(), null, FlagsEnum.ALL.getCode());
			if (!setupClass.isEmpty()) {
				List<SetupDomain> reasonsDomainList = SetupService.getDomains(setupClass.get(0).getId(), null, FlagsEnum.ALL.getCode());
				for (SetupDomain domain : reasonsDomainList) {
					surveillanceOrderReasonsList.add(domain.getDescription());
				}
			}

			if (getRequest().getParameter("id") != null) {
				surveillanceOrderId = Long.parseLong(getRequest().getParameter("id"));
			} else {
				this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
				return;
			}

			surveillanceOrderData = SurveillanceOrdersService.getSurveillanceOrderData(surveillanceOrderId, null, FlagsEnum.ALL.getCode()).get(0);
			surveillanceEmployeesList = SurveillanceOrdersService.getSurveillanceEmployeeDataByOrderId(surveillanceOrderId);
			// Set employees reasons list
			for (SurveillanceEmpNonEmpData emp : surveillanceEmployeesList) {
				if (emp.getSurveillanceReasons() != null && !emp.getSurveillanceReasons().isEmpty()) {
					String[] reasons = emp.getSurveillanceReasons().split(",");
					emp.setSurveillanceOrderReasonsList(Arrays.asList(reasons));
				}
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SurveillanceOrderView.class, e, "SurveillanceOrderView");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Initialize bean variables
	 */
	public void init() {
		surveillanceOrderData = new SurveillanceOrderData();
		surveillanceOrderReasonsList = new ArrayList<String>();
		surveillanceOrderSelectedReasonsList = new ArrayList<String>();
		surveillanceEmployeesList = new ArrayList<SurveillanceEmpNonEmpData>();
	}

	public SurveillanceOrderData getSurveillanceOrderData() {
		return surveillanceOrderData;
	}

	public void setSurveillanceOrderData(SurveillanceOrderData surveillanceOrderData) {
		this.surveillanceOrderData = surveillanceOrderData;
	}

	public List<String> getSurveillanceOrderReasonsList() {
		return surveillanceOrderReasonsList;
	}

	public void setSurveillanceOrderReasonsList(List<String> surveillanceOrderReasonsList) {
		this.surveillanceOrderReasonsList = surveillanceOrderReasonsList;
	}

	public List<SurveillanceEmpNonEmpData> getSurveillanceEmployeesList() {
		return surveillanceEmployeesList;
	}

	public void setSurveillanceEmployeesList(List<SurveillanceEmpNonEmpData> surveillanceEmployeesList) {
		this.surveillanceEmployeesList = surveillanceEmployeesList;
	}

	public List<String> getSurveillanceOrderSelectedReasonsList() {
		return surveillanceOrderSelectedReasonsList;
	}

	public void setSurveillanceOrderSelectedReasonsList(List<String> surveillanceOrderSelectedReasonsList) {
		this.surveillanceOrderSelectedReasonsList = surveillanceOrderSelectedReasonsList;
	}
}
