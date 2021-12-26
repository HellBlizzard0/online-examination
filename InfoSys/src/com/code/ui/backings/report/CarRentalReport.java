package com.code.ui.backings.report;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.setup.DepartmentData;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.WFPositionsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.carrental.CarRentalService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.workflow.WFPositionService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "carRentalReport")
@ViewScoped
public class CarRentalReport extends BaseBacking implements Serializable {
	private Long regionId;
	private Long renterId;
	private String renterName;
	private Integer contractStatus;
	private Date rentalStartDate;
	private Date rentalEndDate;
	private List<DepartmentData> regionList;

	/**
	 * Default Constructor
	 */
	public CarRentalReport() {
		this.contractStatus = FlagsEnum.ALL.getCode();
		try {
			regionList = DepartmentService.getDepartmentsBydepartmentType(DepartmentTypeEnum.REGION.getCode());
			regionList.add(DepartmentService.getDepartment(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId()));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(CarRentalReport.class, e, "CarRentalReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
		this.regionId = (long) FlagsEnum.ALL.getCode();
	}

	/**
	 * Reset Form Parameters
	 */
	public void resetFormParameters() throws BusinessException {
		this.regionId = (long) FlagsEnum.ALL.getCode();
		this.renterId = null;
		this.renterName = "";
		this.contractStatus = FlagsEnum.ALL.getCode();
		this.rentalEndDate = null;
		this.rentalStartDate = null;
	}

	/**
	 * print Car Rentals
	 */
	public void print() {
		if (rentalStartDate == null || rentalEndDate == null) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_mandatory"));
			return;
		}

		try {
			String regionName = "";
			if (!regionId.equals((long) FlagsEnum.ALL.getCode())) {
				regionName = getRegionName();
			}
			byte[] bytes = CarRentalService.getCarRentalReportBytes(renterId, contractStatus, rentalStartDate, rentalEndDate, regionId, regionName, loginEmpData.getFullName());
			super.print(bytes, "Car Rental Report");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(CarRentalReport.class, e, "CarRentalReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * printStatistics
	 */
	public void printStatistics() {
		if (rentalStartDate == null || rentalEndDate == null) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_mandatory"));
			return;
		}
		try {
			String regionName = "";
			if (!regionId.equals((long) FlagsEnum.ALL.getCode())) {
				regionName = getRegionName();
			}
			byte[] bytes = CarRentalService.getCarRentalStatisiticsReportBytes(renterId, contractStatus, rentalStartDate, rentalEndDate, regionId, regionName, loginEmpData.getFullName());
			super.print(bytes, "Car Rental Statistics Report");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(CarRentalReport.class, e, "CarRentalReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Get region name
	 * 
	 * @return regionName
	 */
	public String getRegionName() {
		for (DepartmentData dep : regionList) {
			if (dep.getId().equals(regionId)) {
				return dep.getArabicName();
			}
		}
		return null;
	}

	public Long getRenterId() {
		return renterId;
	}

	public void setRenterId(Long renterId) {
		this.renterId = renterId;
	}

	public String getRenterName() {
		return renterName;
	}

	public void setRenterName(String renterName) {
		this.renterName = renterName;
	}

	public Integer getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(Integer contractStatus) {
		this.contractStatus = contractStatus;
	}

	public Date getRentalStartDate() {
		return rentalStartDate;
	}

	public void setRentalStartDate(Date rentalStartDate) {
		this.rentalStartDate = rentalStartDate;
	}

	public Date getRentalEndDate() {
		return rentalEndDate;
	}

	public void setRentalEndDate(Date rentalEndDate) {
		this.rentalEndDate = rentalEndDate;
	}

	public List<DepartmentData> getRegionList() {
		return regionList;
	}

	public void setRegionList(List<DepartmentData> regionList) {
		this.regionList = regionList;
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}
}