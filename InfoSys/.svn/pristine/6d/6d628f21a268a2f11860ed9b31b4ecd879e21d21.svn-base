package com.code.ui.backings.carrental;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.carrental.CarRentalData;
import com.code.dal.orm.setup.SetupClass;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.ServiceCodesEnum;
import com.code.enums.SessionAttributesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.attach.AttachmentServiceCallBack;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.carrental.CarRentalService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.SetupService;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;
import com.code.ui.util.EncryptionUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "carRentalsSearch")
@ViewScoped
public class CarRentalsSearch extends BaseBacking implements Serializable {
	private String contractNum;
	private long regionOrSector;
	private String selectedDepartmentName;
	private long renterId;
	private String renterName;
	private int contractStatus;
	private SetupClass subRentalCmpnyDomainClass;
	private List<SetupDomain> subRentalCompanies;
	private long selectedSubRentalCompanyId;
	private Date rentalStartDate;
	private Date rentalEndDate;
	private String searchChequeNumber;
	private String searchChequeNumberEdit;
	private String searchPlateNumber;
	private boolean changeContractStatusCollapseFlag;
	private boolean selectCarRentalDataFlag;
	private String chequeNumber;
	private Date hijriChequeDate;
	private Float chequeAmount;
	private float totalAmount;
	private List<CarRentalData> carRentalDataList;
	private List<CarRentalData> selectedCarRentalDataList;
	private List<CarRentalData> chequeEditedCarRentalDataList;
	private String departmentTypesList;
	private int dialogueMode;
	private boolean validState;
	private boolean savedFlag;
	private String fileArchivingParam;

	/**
	 * Default Constructor
	 */
	public CarRentalsSearch() {
		fileArchivingParam = "";
		try {
			if (getRequest().getAttribute(SessionAttributesEnum.FILE_ARCHIVING_PARAM.getCode()) != null) {
				String requestURL = getRequest().getRequestURL() + "";
				String url = requestURL.replace(getRequest().getServletPath(), "");
				String serviceURL = url + AttachmentServiceCallBack.SERVICE_URL;
				fileArchivingParam = (String) getRequest().getAttribute(SessionAttributesEnum.FILE_ARCHIVING_PARAM.getCode());
				fileArchivingParam = EncryptionUtil.encryptSymmetrically("code=" + ServiceCodesEnum.INFO_SYS.getCode() + "&token=" + fileArchivingParam + "&serviceurl=" + serviceURL);
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(CarRentalsSearch.class, e, "CarRentalsSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }

		resetFormParameters();
		departmentTypesList = DepartmentTypeEnum.REGION.getCode() + "," + DepartmentTypeEnum.SECTOR.getCode();
	}

	/**
	 * Reset Form Parameters
	 */
	public void resetFormParameters() {
		try {
			this.contractNum = null;
			this.regionOrSector = (long) FlagsEnum.ALL.getCode();
			this.selectedDepartmentName = "";
			this.renterId = 0l;
			this.renterName = "";
			this.selectedSubRentalCompanyId = FlagsEnum.ALL.getCode();
			this.contractStatus = FlagsEnum.ALL.getCode();
			this.searchChequeNumber = null;
			this.searchPlateNumber = null;
			this.totalAmount = 0;
			this.changeContractStatusCollapseFlag = false;
			this.hijriChequeDate = HijriDateService.getHijriSysDate();
			this.subRentalCmpnyDomainClass = SetupService.getClasses(ClassesEnum.CAR_COMPANIES.getCode(), null, FlagsEnum.ALL.getCode()).get(0);
			this.subRentalCompanies = SetupService.getDomains(subRentalCmpnyDomainClass.getId(), null, FlagsEnum.ALL.getCode());
			this.rentalStartDate = null;
			this.rentalEndDate = null;
			carRentalDataList = new ArrayList<CarRentalData>();
			selectedCarRentalDataList = new ArrayList<CarRentalData>();
			chequeEditedCarRentalDataList = new ArrayList<CarRentalData>();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(CarRentalsSearch.class, e, "CarRentalsSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }

	}

	/**
	 * Search for car rental
	 */
	public void search() {
		try {
			renterId = renterId == 0l ? (long) FlagsEnum.ALL.getCode() : renterId;
			carRentalDataList = CarRentalService.getCarRental(contractNum, regionOrSector, renterId, contractStatus, selectedSubRentalCompanyId, rentalStartDate, rentalEndDate, searchChequeNumber, searchPlateNumber);
			if (carRentalDataList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
			}
			for (CarRentalData carRentalData : carRentalDataList) {
				carRentalData.setTotalAmount((carRentalData.getDailyAmount() * carRentalData.getRentPeriod()) + carRentalData.getExtraAmount());
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(CarRentalsSearch.class, e, "CarRentalsSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Search for car rental
	 */
	public void selectAllUnPaid() {
		for (CarRentalData carRentalData : carRentalDataList) {
			if (carRentalData.getPaid() == FlagsEnum.OFF.getCode()) {
				if (carRentalData.getSelected()) {
					carRentalData.setSelected(false);
				} else {
					carRentalData.setSelected(true);
				}
				selectCarRentalData(carRentalData);
			}
		}
	}

	/**
	 * Send the id of the selected record to the CarRentalRegisteration page to be (edited) if editable (ie : unpaid) otherwise to be viewed
	 * 
	 * @param carRentalDataId
	 * @return
	 */
	public String edit(long carRentalDataId) {
		getRequest().setAttribute("mode", String.valueOf(carRentalDataId));
		return NavigationEnum.EDIT_CAR_RENTAL.toString();
	}

	/**
	 * Delete car rental
	 * 
	 * @param carRentalData
	 */
	public void deleteCarRental(CarRentalData carRentalData) {
		try {
			CarRentalService.deleteCarRental(carRentalData, loginEmpData);
			carRentalDataList.remove(carRentalData);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(CarRentalsSearch.class, e, "CarRentalsSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Select/unselect specific carRental record to add it to the list containing records need to be changed in status from unpaid to paid
	 * 
	 * @param carRentalData
	 */
	public void selectCarRentalData(CarRentalData carRentalData) {
		if (carRentalData.getSelected()) {
			selectedCarRentalDataList.add(carRentalData);
			totalAmount += carRentalData.getTotalAmount();
		} else {
			selectedCarRentalDataList.remove(carRentalData);
			totalAmount -= carRentalData.getTotalAmount();
		}
	}

	/**
	 * Change Contract Status
	 */
	public void changeContractStatus() {
		try {
			savedFlag = false;
			if(chequeNumber == null || chequeAmount == null || hijriChequeDate == null) {
				throw new BusinessException("error_mandatory");
			}
			if(chequeAmount == 0) {
				throw new BusinessException("error_chequeAmountMoreThanZero");
			}
			if (!chequeEditedCarRentalDataList.isEmpty()) {
				CarRentalService.updateContractStatus(chequeEditedCarRentalDataList, chequeNumber, hijriChequeDate, chequeAmount, loginEmpData);
				resetChequeData();
				this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
				savedFlag = true;
			} else {
				if (chequeEditedCarRentalDataList.isEmpty() && !selectedCarRentalDataList.isEmpty()) {
					CarRentalService.updateContractStatus(selectedCarRentalDataList, chequeNumber, hijriChequeDate, chequeAmount, loginEmpData);
					resetChequeData();
					validState = false;
					selectedCarRentalDataList = new ArrayList<CarRentalData>();
					this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
					savedFlag = true;
				} else {
					this.setServerSideErrorMessages(getParameterizedMessage("error_noSelectedCarRentals"));
				}
			}
			validState = false;
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			validState = false;
		}catch (Exception e) {
			   Log4j.traceErrorException(CarRentalsSearch.class, e, "CarRentalsSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   validState = false;
		  }
	}

	/**
	 * open popup to insert cheque data
	 * 
	 * @param dialogueMode
	 */
	public void insertChequeData(int dialogueMode) {
		if (dialogueMode == 2 && selectedCarRentalDataList.isEmpty()) {
			validState = false;
			this.setServerSideErrorMessages(getParameterizedMessage("error_noSelectedCarRentals"));
		} else {
			resetChequeData();
			validState = true;
			this.dialogueMode = dialogueMode;
		}
	}

	/**
	 * Search cheque data
	 */
	public void searchCheque() {
		try {
			if (searchChequeNumberEdit == null || searchChequeNumberEdit.trim().equals("")) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_chequeNumberIsMandatory"));
				return;
			}
			chequeEditedCarRentalDataList = CarRentalService.getCarRental(null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, null, searchChequeNumberEdit, null);
			if (chequeEditedCarRentalDataList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
			} else {
				chequeNumber = chequeEditedCarRentalDataList.get(0).getChequeNumber();
				chequeAmount = chequeEditedCarRentalDataList.get(0).getChequeAmount();
				hijriChequeDate = chequeEditedCarRentalDataList.get(0).getChequeDate();
				totalAmount = 0;
				for (CarRentalData carRentalData : chequeEditedCarRentalDataList) {
					totalAmount += (carRentalData.getDailyAmount() * carRentalData.getRentPeriod()) + carRentalData.getExtraAmount();
				}
				dialogueMode = 2;
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(CarRentalsSearch.class, e, "CarRentalsSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Reset cheque data
	 */
	public void resetChequeData() {
		try {
			searchChequeNumberEdit = null;
			chequeNumber = null;
			chequeAmount = 0F;
			hijriChequeDate = HijriDateService.getHijriSysDate();
			totalAmount = 0;
			chequeEditedCarRentalDataList = new ArrayList<CarRentalData>();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}

	public long getRegionOrSector() {
		return regionOrSector;
	}

	public void setRegionOrSector(long regionOrSector) {
		this.regionOrSector = regionOrSector;
	}

	public String getSelectedDepartmentName() {
		return selectedDepartmentName;
	}

	public void setSelectedDepartmentName(String selectedDepartmentName) {
		this.selectedDepartmentName = selectedDepartmentName;
	}

	public long getRenterId() {
		return renterId;
	}

	public void setRenterId(long renterId) {
		this.renterId = renterId;
	}

	public String getRenterName() {
		return renterName;
	}

	public void setRenterName(String renterName) {
		this.renterName = renterName;
	}

	public int getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(int contractStatus) {
		this.contractStatus = contractStatus;
	}

	public List<SetupDomain> getSubRentalCompanies() {
		return subRentalCompanies;
	}

	public void setSubRentalCompanies(List<SetupDomain> subRentalCompanies) {
		this.subRentalCompanies = subRentalCompanies;
	}

	public long getSelectedSubRentalCompanyId() {
		return selectedSubRentalCompanyId;
	}

	public void setSelectedSubRentalCompanyId(long selectedSubRentalCompanyId) {
		this.selectedSubRentalCompanyId = selectedSubRentalCompanyId;
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

	public String getSearchChequeNumber() {
		return searchChequeNumber;
	}

	public void setSearchChequeNumber(String searchChequeNumber) {
		this.searchChequeNumber = searchChequeNumber;
	}

	public String getSearchPlateNumber() {
		return searchPlateNumber;
	}

	public String getSearchChequeNumberEdit() {
		return searchChequeNumberEdit;
	}

	public void setSearchChequeNumberEdit(String searchChequeNumberEdit) {
		this.searchChequeNumberEdit = searchChequeNumberEdit;
	}

	public void setSearchPlateNumber(String searchPlateNumber) {
		this.searchPlateNumber = searchPlateNumber;
	}

	public List<CarRentalData> getCarRentalDataList() {
		return carRentalDataList;
	}

	public void setCarRentalDataList(List<CarRentalData> carRentalDataList) {
		this.carRentalDataList = carRentalDataList;
	}

	public boolean isChangeContractStatusCollapseFlag() {
		return changeContractStatusCollapseFlag;
	}

	public void setChangeContractStatusCollapseFlag(boolean changeContractStatusCollapseFlag) {
		this.changeContractStatusCollapseFlag = changeContractStatusCollapseFlag;
	}

	public List<CarRentalData> getSelectedCarRentalDataList() {
		return selectedCarRentalDataList;
	}

	public void setSelectedCarRentalDataList(List<CarRentalData> selectedCarRentalDataList) {
		this.selectedCarRentalDataList = selectedCarRentalDataList;
	}

	public String getChequeNumber() {
		return chequeNumber;
	}

	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}

	public Date getHijriChequeDate() {
		return hijriChequeDate;
	}

	public void setHijriChequeDate(Date hijriChequeDate) {
		this.hijriChequeDate = hijriChequeDate;
	}

	public Float getChequeAmount() {
		return chequeAmount;
	}

	public void setChequeAmount(Float chequeAmount) {
		this.chequeAmount = chequeAmount;
	}

	public float getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(float totalAmount) {
		this.totalAmount = totalAmount;
	}

	public boolean isSelectCarRentalDataFlag() {
		return selectCarRentalDataFlag;
	}

	public void setSelectCarRentalDataFlag(boolean selectCarRentalDataFlag) {
		this.selectCarRentalDataFlag = selectCarRentalDataFlag;
	}

	public String getDepartmentTypesList() {
		return departmentTypesList;
	}

	public void setDepartmentTypesList(String departmentTypesList) {
		this.departmentTypesList = departmentTypesList;
	}

	public int getDialogueMode() {
		return dialogueMode;
	}

	public void setDialogueMode(int dialogueMode) {
		this.dialogueMode = dialogueMode;
	}

	public boolean isValidState() {
		return validState;
	}

	public void setValidState(boolean validState) {
		this.validState = validState;
	}

	public String getFileArchivingParam() {
		return fileArchivingParam;
	}

	public void setFileArchivingParam(String fileArchivingParam) {
		this.fileArchivingParam = fileArchivingParam;
	}

	public String getBoolServerUploadPath() {
		return InfoSysConfigurationService.getBoolServerUploadPath();
	}

	public boolean isSavedFlag() {
		return savedFlag;
	}

	public void setSavedFlag(boolean savedFlag) {
		this.savedFlag = savedFlag;
	}
}