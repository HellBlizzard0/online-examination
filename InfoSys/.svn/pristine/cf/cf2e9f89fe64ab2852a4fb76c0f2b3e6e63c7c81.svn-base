package com.code.ui.backings.carrental;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import com.code.dal.orm.attach.Attachment;
import com.code.dal.orm.carrental.CarRentalData;
import com.code.dal.orm.security.UserMenuActionData;
import com.code.dal.orm.setup.DepartmentData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.enums.EntityNameEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.PlateNumberEnum;
import com.code.enums.ReportNamesEnum;
import com.code.enums.ServiceCodesEnum;
import com.code.enums.SessionAttributesEnum;
import com.code.enums.UserMenuActionsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.attach.AttachmentService;
import com.code.services.attach.AttachmentServiceCallBack;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.carrental.CarRentalService;
import com.code.services.log4j.Log4j;
import com.code.services.security.SecurityService;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.EmployeeService;
import com.code.services.setup.SetupService;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;
import com.code.ui.util.EncryptionUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "carRentalRegisteration")
@ViewScoped
public class CarRentalRegisteration extends BaseBacking implements Serializable {
	private DepartmentData registerDepartment;
	private String renterEmployeeName;
	private String missionDesc;
	private String missionConclusion;
	private CarRentalData carRentalData = new CarRentalData();
	private List<SetupDomain> rentalCompanies;
	private List<SetupDomain> subRentalCompanies;
	private List<SetupDomain> carModels;
	private Boolean isMissionRltdToInfo;
	private String fileArchivingParam;
	private String downloadFileParamId;
	private List<Attachment> attachmentList;
	private boolean openDownloadPopupFlag;
	private boolean openDownloadDialogueFlag;
	private String boolServerUploadPath;
	private String boolServerDownloadPath;
	private List<PlateNumberEnum> plateNumberList = Arrays.asList(PlateNumberEnum.values());
	private String plateText1;
	private String plateText2;
	private String plateText3;
	private String plateNumber;
	private boolean deletePrivilage = false;

	/**
	 * CarRentalRegisteration: Construct page , check if mode parameter is set , then the page is coming from the search page so it would work in update mode otherwise it's in the registeration mode , registration mode has two internal states either it's a new not saved car rental in this case the carRentalData.getId() == null is true , or it's new but it's just saved now so the id is not null and any change to be done should update the current carRental
	 */
	public CarRentalRegisteration() {
		super();
		try {
			if (getRequest().getAttribute("mode") != null && !((String) getRequest().getAttribute("mode")).isEmpty() && !getRequest().getAttribute("mode").equals("undefined")) {
				carRentalData = CarRentalService.getCarRental(Long.parseLong((String) getRequest().getAttribute("mode")));
				if (carRentalData == null) {
					carRentalData = new CarRentalData();
				}
			}

			if (carRentalData.getId() == null) {
				intializeCarRentalData();
			} else {
				renterEmployeeName = carRentalData.getEmployeeName();
				plateText1 = carRentalData.getPlateText1();
				plateText2 = carRentalData.getPlateText2();
				plateText3 = carRentalData.getPlateText3();
				plateNumber = carRentalData.getPlateNumber();
				if (carRentalData.getInfoNumber() != null)
					this.isMissionRltdToInfo = true;
			}
			UserMenuActionData deleteAttachmentAction = SecurityService.getAction(loginEmpData.getEmpId(), UserMenuActionsEnum.DELETE_ATTACHMENT_PERMISSION.getCode(), FlagsEnum.ALL.getCode());
			if (deleteAttachmentAction != null) {
				setDeletePrivilage(true);
			}
			rentalCompanies = SetupService.getDomains(ClassesEnum.CAR_COMPANIES.getCode());

			subRentalCompanies = new ArrayList<SetupDomain>();
			subRentalCompanies.addAll(rentalCompanies);

			carModels = SetupService.getDomains(ClassesEnum.CAR_MODELS.getCode());

			calculateRentPeriod();
			calculateTotalPaymentAmount();
			boolServerUploadPath = InfoSysConfigurationService.getBoolServerUploadPath();
			boolServerDownloadPath = InfoSysConfigurationService.getBoolServerDownloadPath();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			   Log4j.traceErrorException(CarRentalRegisteration.class, e, "CarRentalRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * IntializeCarRentalData: Initializing page objects
	 */
	public void intializeCarRentalData() {
		try {
			carRentalData.setRentStartDate(HijriDateService.getHijriSysDate());
			carRentalData.setRentEndDate(HijriDateService.addSubHijriDays(HijriDateService.getHijriSysDate(), 1));
			carRentalData.setExtraAmount(0F);
			carRentalData.setDailyAmount(0F);
			carRentalData.setPaid(0);
			registerDepartment = DepartmentService.getDepartment(loginEmpData.getActualDepartmentId());
			carRentalData.setDepartmentId(registerDepartment.getId());
			carRentalData.setRegisterDepartmentName(registerDepartment.getArabicName());
			renterEmployeeName = null;
			plateText1 = null;
			plateText2 = null;
			plateText3 = null;
			plateNumber = null;
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * print Car Rentals
	 */
	public void print() {
		try {
			byte[] bytes = CarRentalService.getCarRentalRegistrationReportBytes(carRentalData.getId());
			String reportName = ReportNamesEnum.CAR_RENTAL_REGISTRATION.toString().replace("_", " ");
			super.print(bytes, reportName);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(CarRentalRegisteration.class, e, "CarRentalRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * GetEmployeeDetails: get renter employee details
	 */
	public void getEmployeeDetails() {
		try {
			EmployeeData employee = EmployeeService.getEmployee(carRentalData.getEmployeeId());
			renterEmployeeName = employee.getFullName();
			carRentalData.setEmployeeRank(employee.getRank());
			carRentalData.setEmployeeUnitName(employee.getActualDepartmentName());
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * SaveCarRental: save or update car rentals
	 */
	public String saveCarRental() {
		try {
			if (plateNumber == null) {
				throw new BusinessException("error_plateNumberMandatory");
			}
			carRentalData.setCarPlateNumber(plateText1 + " " + plateText2 + " " + plateText3 + " " + plateNumber);
			if (carRentalData.getId() == null) {
				saveNewCarRental();
			} else {
				updateExistingCarRental();
			}
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
			getRequest().setAttribute(SessionAttributesEnum.FILE_ARCHIVING_PARAM.getCode(), EntityNameEnum.CAR_RENTAL.getCode() + "_" + carRentalData.getId());
			getRequest().setAttribute("mode", String.valueOf(carRentalData.getId()));
			return NavigationEnum.EDIT_CAR_RENTAL.toString();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(CarRentalRegisteration.class, e, "CarRentalRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
		return null;
	}

	/**
	 * Save a new car rental
	 * 
	 * @return
	 */
	private void saveNewCarRental() throws BusinessException {
		if (!isMissionRltdToInfo && carRentalData.getInfoNumber() != null) {
			carRentalData.setInfoNumber(null);
		}
		CarRentalService.saveCarRental(carRentalData, loginEmpData);
	}

	/**
	 * Update existing car rental
	 */
	private void updateExistingCarRental() throws BusinessException {
		String tempInfoNumber = new String();
		try {
			if (!isMissionRltdToInfo && carRentalData.getInfoNumber() != null) {
				tempInfoNumber = carRentalData.getInfoNumber();
				carRentalData.setInfoNumber(null);
			}
			CarRentalService.updateCarRental(carRentalData, loginEmpData);
		} catch (BusinessException e) {
			if (!isMissionRltdToInfo && carRentalData.getInfoNumber() != null) {
				carRentalData.setInfoNumber(tempInfoNumber);
				this.isMissionRltdToInfo = true;
			}
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			 if (!isMissionRltdToInfo && carRentalData.getInfoNumber() != null) {
					carRentalData.setInfoNumber(tempInfoNumber);
					this.isMissionRltdToInfo = true;
				}
			   Log4j.traceErrorException(CarRentalRegisteration.class, e, "CarRentalRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Constitute upload parameter which contains bool server registered serviceCode and the token (EntityName_EntityId)
	 * 
	 * @param carRentalDataId
	 */
	public void getUploadParam(long carRentalDataId) {
		try {
			String requestURL = getRequest().getRequestURL() + "";
			String url = requestURL.replace(getRequest().getServletPath(), "");
			String serviceURL = url + AttachmentServiceCallBack.SERVICE_URL;
			fileArchivingParam = EncryptionUtil.encryptSymmetrically("code=" + ServiceCodesEnum.INFO_SYS.getCode() + "&token=" + EntityNameEnum.CAR_RENTAL.getCode() + "_" + carRentalDataId + "&serviceurl=" + serviceURL);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			   Log4j.traceErrorException(CarRentalRegisteration.class, e, "CarRentalRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Constitute Download parameter which contains contentId (attachmentId)
	 * 
	 * @param attachmentId
	 */
	public void getDownloadParam(String attachmentId) {
		try {
			downloadFileParamId = EncryptionUtil.encryptSymmetrically("contentId=" + attachmentId);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(CarRentalRegisteration.class, e, "CarRentalRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Delete Attachment
	 * 
	 * @param attachment
	 */
	public void deleteAttachment(Attachment attachment) {
		try {
			AttachmentService.deleteFileNetAttachment(attachment);
			attachmentList.remove(attachment);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(CarRentalRegisteration.class, e, "CarRentalRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Get attachment list related to the entity id passed to the method then check if there's no records then show message indicating no daya else if it's only one record , open the download popup directly , otherwise open a dialogue shows file names and make the user select which one to download then open the download popup with the choosen contentId (attachmentId)
	 * 
	 * @param carRentalDataId
	 * @throws BusinessException
	 */
	public void getDownloadedAttachmentsIdList(long carRentalDataId) throws BusinessException {
		try {
			attachmentList = AttachmentService.getAttachments(EntityNameEnum.CAR_RENTAL.getCode(), carRentalDataId);
			openDownloadPopupFlag = false;
			openDownloadDialogueFlag = false;
			if (attachmentList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noAttachments"));
			} else if (attachmentList.size() == 1) {
				openDownloadPopupFlag = true;
				downloadFileParamId = EncryptionUtil.encryptSymmetrically("contentId=" + attachmentList.get(0).getAttachmentId());
			} else {
				openDownloadDialogueFlag = true;
			}
		} catch (Exception e) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	/**
	 * Reset Car Rental Form
	 */
	public void resetCarRentalForm() {
		carRentalData = new CarRentalData();
		intializeCarRentalData();
	}

	/**
	 * Calculate Rent Period
	 * 
	 * @param event
	 */
	public void dateChangedListener(AjaxBehaviorEvent event) {
		calculateRentPeriod();
	}

	/**
	 * calculateRentPeriod: calculate rent period from the inserted dates
	 */
	public void calculateRentPeriod() {
		carRentalData.setRentPeriod(Math.abs(HijriDateService.hijriDateDiff(carRentalData.getRentEndDate(), carRentalData.getRentStartDate())));
		calculateTotalPaymentAmount();
	}

	/**
	 * calculateTotalPaymentAmount: calculate total payment amount from the inserted dates and daily amount
	 */
	public void calculateTotalPaymentAmount() {
		carRentalData.setTotalAmount((carRentalData.getDailyAmount() * carRentalData.getRentPeriod()) + carRentalData.getExtraAmount());
	}

	public DepartmentData getRegisterDepartment() {
		return registerDepartment;
	}

	public void setRegisterDepartment(DepartmentData registerDepartment) {
		this.registerDepartment = registerDepartment;
	}

	public String getRenterEmployeeName() {
		return renterEmployeeName;
	}

	public void setRenterEmployeeName(String renterEmployeeName) {
		this.renterEmployeeName = renterEmployeeName;
	}

	public String getMissionDesc() {
		return missionDesc;
	}

	public void setMissionDesc(String missionDesc) {
		this.missionDesc = missionDesc;
	}

	public String getMissionConclusion() {
		return missionConclusion;
	}

	public void setMissionConclusion(String missionConclusion) {
		this.missionConclusion = missionConclusion;
	}

	public CarRentalData getCarRentalData() {
		return carRentalData;
	}

	public void setCarRentalData(CarRentalData crd) {
		this.carRentalData = crd;
	}

	public List<SetupDomain> getRentalCompanies() {
		return rentalCompanies;
	}

	public void setRentalCompanies(List<SetupDomain> rentalCompanies) {
		this.rentalCompanies = rentalCompanies;
	}

	public List<SetupDomain> getCarModels() {
		return carModels;
	}

	public void setCarModels(List<SetupDomain> carModels) {
		this.carModels = carModels;
	}

	public Boolean getIsMissionRltdToInfo() {
		return isMissionRltdToInfo;
	}

	public void setIsMissionRltdToInfo(Boolean isMissionRltdToInfo) {
		this.isMissionRltdToInfo = isMissionRltdToInfo;
	}

	public List<SetupDomain> getSubRentalCompanies() {
		return subRentalCompanies;
	}

	public void setSubRentalCompanies(List<SetupDomain> subRentalCompanies) {
		this.subRentalCompanies = subRentalCompanies;
	}

	public String getFileArchivingParam() {
		return fileArchivingParam;
	}

	public void setFileArchivingParam(String fileArchivingParam) {
		this.fileArchivingParam = fileArchivingParam;
	}

	public List<Attachment> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<Attachment> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public boolean isOpenDownloadPopupFlag() {
		return openDownloadPopupFlag;
	}

	public void setOpenDownloadPopupFlag(boolean openDownloadPopupFlag) {
		this.openDownloadPopupFlag = openDownloadPopupFlag;
	}

	public String getDownloadFileParamId() {
		return downloadFileParamId;
	}

	public void setDownloadFileParamId(String downloadFileParamId) {
		this.downloadFileParamId = downloadFileParamId;
	}

	public boolean isOpenDownloadDialogueFlag() {
		return openDownloadDialogueFlag;
	}

	public void setOpenDownloadDialogueFlag(boolean openDownloadDialogueFlag) {
		this.openDownloadDialogueFlag = openDownloadDialogueFlag;
	}

	public String getBoolServerUploadPath() {
		return boolServerUploadPath;
	}

	public void setBoolServerUploadPath(String boolServerUploadPath) {
		this.boolServerUploadPath = boolServerUploadPath;
	}

	public String getBoolServerDownloadPath() {
		return boolServerDownloadPath;
	}

	public void setBoolServerDownloadPath(String boolServerDownloadPath) {
		this.boolServerDownloadPath = boolServerDownloadPath;
	}

	public List<PlateNumberEnum> getPlateNumberList() {
		return plateNumberList;
	}

	public void setPlateNumberList(List<PlateNumberEnum> plateNumberList) {
		this.plateNumberList = plateNumberList;
	}

	public String getPlateText1() {
		return plateText1;
	}

	public void setPlateText1(String plateText1) {
		this.plateText1 = plateText1;
	}

	public String getPlateText2() {
		return plateText2;
	}

	public void setPlateText2(String plateText2) {
		this.plateText2 = plateText2;
	}

	public String getPlateText3() {
		return plateText3;
	}

	public void setPlateText3(String plateText3) {
		this.plateText3 = plateText3;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public boolean isDeletePrivilage() {
		return deletePrivilage;
	}

	public void setDeletePrivilage(boolean deletePrivilage) {
		this.deletePrivilage = deletePrivilage;
	}
}