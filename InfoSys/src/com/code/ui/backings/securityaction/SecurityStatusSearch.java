package com.code.ui.backings.securityaction;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;

import com.code.dal.orm.setup.CountryData;
import com.code.enums.CarRegisterationTypEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.GccCountriesEnum;
import com.code.enums.PlateNumberEnum;
import com.code.enums.SecurityStatusEnum;
import com.code.enums.SocialTypeEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.securityaction.SecurityStatusService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.CountryService;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;
import com.elm.yakeen.yakeen4borderguard.Gender;
import com.elm.yakeen.yakeen4borderguard.LifeStatus;
import com.elm.yakeen.yakeen4borderguard.SecurityRecordList;

import yaqeen.ejada.com.CarInfoResult;
import yaqeen.ejada.com.IdType;
import yaqeen.ejada.com.PersonInfoResultWithDetailedSecurityStatus;

@SuppressWarnings("serial")
@ManagedBean(name = "securityStatusSearch")
@ViewScoped
public class SecurityStatusSearch extends BaseBacking implements Serializable {
	private Integer searchSocialType;
	private String searchSocialId;
	private Date searchBirthDate;
	private Short searchCountryCode;
	private String searchCountryName;
	private List<CountryData> countriesList;
	private List<GccCountriesEnum> gccCountriesList = Arrays.asList(GccCountriesEnum.values());
	private List<Integer> SecurityStatus;
	private List<PlateNumberEnum> plateNumberList = Arrays.asList(PlateNumberEnum.values());
	private String plateText1;
	private String plateText2;
	private String plateText3;
	private Short plateNumber;
	private Short plateType;
	private PersonInfoResultWithDetailedSecurityStatus personInfoResult;
	private String personInfoResultStatusString;
	private CarInfoResult carInfoByPlateResult;
	private Boolean personPanel = true;
	private Long personalPhotoId = 0L;

	/**
	 * Default Constructor
	 */
	public SecurityStatusSearch() {
		try {
			searchSocialType = getNationalIdentity();
			countriesList = CountryService.getAllCountries();
		} catch (BusinessException e) {
		}
	}

	/**
	 * 
	 * @param event
	 */
	public void onTabChange(TabChangeEvent event) {
		personPanel = event.getTab().getId().equals("personTabId") ? true : false;
	}

	/**
	 * 
	 * @param event
	 */
	public void onTabClose(TabCloseEvent event) {
		personPanel = null;
	}

	/**
	 * Search Security Status
	 */
	public void search() {
		try {
			personInfoResult = null;
			carInfoByPlateResult = null;
			if (personPanel == null) {
				throw new BusinessException("error_oneTabAtLeast");
			} else if (personPanel) {
				if (searchBirthDate == null || searchSocialId == null || searchSocialId.isEmpty()) {
					throw new BusinessException("error_detailMandatory");
				}
				if (searchSocialType.equals(getNationalIdentity())) {
					personInfoResult = SecurityStatusService.searchPersonInfoNationalIdentity(searchBirthDate, IdType.C, searchSocialId, loginEmpData);
					adjustSecurityStatus();
				} else if (searchSocialType.equals(getResident())) {
					personInfoResult = SecurityStatusService.searchPersonInfo(searchBirthDate, IdType.R, searchSocialId, loginEmpData);
					adjustSecurityStatus();
				} else if (searchSocialType.equals(getVisitVisa())) {
					personInfoResult = SecurityStatusService.searchPersonInfo(searchBirthDate, IdType.V, searchSocialId, loginEmpData);
					adjustSecurityStatus();
				}

				// TODO uncomment after getting the details
				// else if (socialType.equals(4)) {
				// gccPassportResult = SecurityStatusService.searchGccInfoByPassportRequest(socialId, countryId);
				// fillGccInfoByPassportResult(gccPassportResult);
				// } else if (socialType.equals(5)) {
				// GccInfoByNINResult gccInfoByNINResult = SecurityStatusService.searchGccInfoByNINRequest(socialId, countryId);
				// fillGccInfoByNINResult(gccInfoByNINResult);
				// }
			} else {
				if (plateNumber == null) {
					throw new BusinessException("error_detailMandatory");
				}
				carInfoByPlateResult = SecurityStatusService.searchCarInfoByPlateRequest(plateText1, plateText2, plateText3, plateNumber, plateType, loginEmpData);

			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return;
		}catch (Exception e) {
			   Log4j.traceErrorException(SecurityStatusSearch.class, e, "SecurityStatusSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			   return;
		  }
	}

	/**
	 * Adjust Security Status Values
	 */
	public void adjustSecurityStatus() {
		personInfoResultStatusString = "";
		if (personInfoResult.getSecurityRecordListList().size() == 1) {
			personInfoResultStatusString = getParameterizedMessage("label_wanted") + " - ";
		}

		for (SecurityRecordList recordList : personInfoResult.getSecurityRecordListList()) {
			if (recordList.getActionCode() == getArrested()) {
				personInfoResultStatusString += getParameterizedMessage("label_getArrested") + " - ";
			} else if (recordList.getActionCode() == getServiceSuspension()) {
				personInfoResultStatusString += getParameterizedMessage("label_serviceSuspension") + " - ";
			} else if (recordList.getActionCode() == getReviwer()) {
				personInfoResultStatusString += getParameterizedMessage("label_reviewer") + " - ";
			} else if (recordList.getActionCode() == getTravelBan()) {
				personInfoResultStatusString += getParameterizedMessage("label_tarvelBan") + " - ";
			}
		}
		personInfoResultStatusString = personInfoResultStatusString.trim().length() > 0 ? personInfoResultStatusString.substring(0, personInfoResultStatusString.length() - 2) : "";

		if (personInfoResultStatusString.isEmpty()) {
			if (personInfoResult.getSecurityStatus().getValue().equals(FlagsEnum.ON.getCode() + "")) {
				personInfoResultStatusString = getParameterizedMessage("label_wanted");
			} else {
				personInfoResultStatusString = getParameterizedMessage("label_notWanted");
			}
		}
	}

	/**
	 * Reset Search Form
	 */
	public void resetSearchParams() {
		personInfoResult = null;
		carInfoByPlateResult = null;
		searchSocialId = null;
		searchBirthDate = null;
		searchCountryCode = null;
		plateText1 = null;
		plateText2 = null;
		plateText3 = null;
		plateNumber = null;
		plateType = CarRegisterationTypEnum.PRIVATE_CAR.getCode();
	}

	/**
	 * Reset Search Form
	 */
	public void resetSearch() {
		personInfoResult = null;
		carInfoByPlateResult = null;
		//TODO personPanel = false;
		searchSocialType = getNationalIdentity();
		searchSocialId = null;
		searchBirthDate = null;
		searchCountryCode = null;
		plateText1 = null;
		plateText2 = null;
		plateText3 = null;
		plateNumber = null;
		plateType = CarRegisterationTypEnum.PRIVATE_CAR.getCode();
	}

	/**
	 * Print Security Status Report
	 */
	public void printReport() {
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String todayDateStr = df.format(new Date());
		String timeNow = todayDateStr.substring(11);
		String DATE_FORMAT_LTR = "mm/MM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_LTR);
		String hijriDateString = sdf.format(HijriDateService.gregToHijriDate(new Date()));
		String fullName = loginEmpData.getFullName();

		if (personPanel) {
			String arabicName = personInfoResult.getArabicName();
			String foreignName = personInfoResult.getEnglishName();
			String socialId = searchSocialId;
			String birthDate = personInfoResult.getDateOfBirthG();
			String expiryDate = personInfoResult.getIdExpiryDateH();
			String releaseDate = personInfoResult.getIdIssueDateH();
			String lifeStatus = personInfoResult.getLifeStatus().toString();
			String gender = personInfoResult.getGender().toString();
			String job = personInfoResult.getOccupationDesc();
			String nationality = personInfoResult.getNationalityDesc();
			String releaseRegion = personInfoResult.getIdIssuePlace().getValue();
			String securityStatusString = "";

			if (personInfoResult.getSecurityRecordListList().size() == 1)
				securityStatusString += getParameterizedMessage("label_wanted") + " - ";

			for (SecurityRecordList recordList : personInfoResult.getSecurityRecordListList()) {
				if (recordList.getActionCode() == SecurityStatusEnum.ARRESTED.getCode())
					securityStatusString += getParameterizedMessage("label_getArrested") + " - ";

				else if (recordList.getActionCode() == SecurityStatusEnum.SERVICE_SUSPENSION.getCode())
					securityStatusString += getParameterizedMessage("label_serviceSuspension") + " - ";

				else if (recordList.getActionCode() == SecurityStatusEnum.REVIEWER.getCode())
					securityStatusString += getParameterizedMessage("label_reviewer") + " - ";

				else if (recordList.getActionCode() == SecurityStatusEnum.TRAVEL_BAN.getCode())
					securityStatusString += getParameterizedMessage("label_tarvelBan") + " - ";

			}

			securityStatusString = securityStatusString.trim().length() > 0 ? securityStatusString.substring(0, securityStatusString.length() - 2).trim() : "";

			// NationalIdentity SecurityStatus
			if (searchSocialType == getNationalIdentity()) {
				try {
					String versionNumber = personInfoResult.getIdVersionNumber().toString();
					byte[] bytes = SecurityStatusService.printNationalIdentitySecurityStatus(arabicName, foreignName, socialId, expiryDate, versionNumber, releaseDate, releaseRegion, lifeStatus, birthDate, gender, job, timeNow, fullName, hijriDateString, securityStatusString);
					String reportName = "NationalIdentitySecurityStatus";
					super.print(bytes, reportName);
				} catch (BusinessException e) {
					this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
				}
			}

			// Resident SecurityStatus
			else if (searchSocialType == getResident()) {
				try {
					String passportExpiryDate = personInfoResult.getPassportExpiryDate().getValue();
					String ownerNumber = personInfoResult.getSponsorMoiNumber();
					String ownerName = personInfoResult.getSponsorName();
					byte[] bytes = SecurityStatusService.printResidentSecurityStatus(arabicName, foreignName, socialId, expiryDate, releaseDate, releaseRegion, lifeStatus, birthDate, gender, job, passportExpiryDate, nationality, ownerNumber, ownerName, timeNow, fullName, hijriDateString, securityStatusString);
					String reportName = "ResidentSecurityStatus";
					super.print(bytes, reportName);
				} catch (BusinessException e) {
					this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
				}
			}

			// Visit Visa SecurityStatus
			else if (searchSocialType == getVisitVisa()) {
				try {
					String visitVisaNumber = personInfoResult.getVisaNumber().getValue();
					String passportNumber = personInfoResult.getPassportNumber().getValue();
					String ownerNumber = personInfoResult.getSponsorMoiNumber();
					String ownerName = personInfoResult.getSponsorName();
					byte[] bytes = SecurityStatusService.printVisitVisaSecurityStatus(arabicName, foreignName, socialId, visitVisaNumber, expiryDate, releaseDate, passportNumber, nationality, lifeStatus, birthDate, gender, job, ownerNumber, ownerName, timeNow, fullName, hijriDateString, securityStatusString);
					String reportName = "VisitVisaSecurityStatus";
					super.print(bytes, reportName);
				} catch (BusinessException e) {
					this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
				}
			}

			// KhaleejiIdentity SecurityStatus
			else if (searchSocialType == getKhaleejiIdentity()) {
				try {
					String passportNumber = personInfoResult.getPassportNumber().getValue();
					byte[] bytes = SecurityStatusService.printKhaleejiIdentitySecurityStatus(arabicName, foreignName, socialId, releaseDate, birthDate, expiryDate, passportNumber, lifeStatus, nationality, gender, timeNow, fullName, hijriDateString, securityStatusString);
					String reportName = "KhaleejiIdentitySecurityStatus";
					super.print(bytes, reportName);
				} catch (BusinessException e) {
					this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
				}
			}

			// KhaleejiPassport SecurityStatus
			else if (searchSocialType == getKhaleejiIdentity()) {
				try {
					byte[] bytes = SecurityStatusService.printKhaleejiPassportSecurityStatus(arabicName, foreignName, socialId, releaseDate, birthDate, expiryDate, nationality, lifeStatus, gender, timeNow, fullName, hijriDateString, securityStatusString);
					String reportName = "KhaleejiPassportSecurityStatus";
					super.print(bytes, reportName);
				} catch (BusinessException e) {
					this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
				}
			}
		} // CarSecurityStatus
		else {
			try {
				Long ownerId = carInfoByPlateResult.getOwnerId();
				Short registerationType = plateType;
				String plateNo = plateText1 + " " + plateText2 + " " + plateText3 + " " + plateNumber;
				String vehicleMaker = carInfoByPlateResult.getVehicleMaker();
				String vehicleModel = carInfoByPlateResult.getVehicleModel();
				String vehicleColor = carInfoByPlateResult.getMajorColor();
				Short modelYear = carInfoByPlateResult.getModelYear();
				Integer sequenceNumber = carInfoByPlateResult.getSequenceNumber();
				String releaseRegion = carInfoByPlateResult.getRegPlace();
				String releaseDate = carInfoByPlateResult.getRegIssueDate();
				String expiryDate = carInfoByPlateResult.getRegExpiryHDate();
				Boolean securityStatus = carInfoByPlateResult.isLegalStatus();

				byte[] bytes = SecurityStatusService.printCarSecurityStatus(ownerId, registerationType, plateNo, vehicleMaker, vehicleModel, vehicleColor, modelYear, sequenceNumber, releaseRegion, releaseDate, expiryDate, timeNow, fullName, hijriDateString, securityStatus);
				String reportName = "CarSecurityStatus";
				super.print(bytes, reportName);
			} catch (BusinessException e) {
				this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			}
		}
	}

	public Integer getSearchSocialType() {
		return searchSocialType;
	}

	public void setSearchSocialType(Integer searchSocialType) {
		this.searchSocialType = searchSocialType;
	}

	public String getSearchSocialId() {
		return searchSocialId;
	}

	public void setSearchSocialId(String searchSocialId) {
		this.searchSocialId = searchSocialId;
	}

	public Date getSearchBirthDate() {
		return searchBirthDate;
	}

	public void setSearchBirthDate(Date searchBirthDate) {
		this.searchBirthDate = searchBirthDate;
	}

	public Short getSearchCountryCode() {
		return searchCountryCode;
	}

	public void setSearchCountryCode(Short searchCountryCode) {
		this.searchCountryCode = searchCountryCode;
	}

	public String getSearchCountryName() {
		return searchCountryName;
	}

	public void setSearchCountryName(String searchCountryName) {
		this.searchCountryName = searchCountryName;
	}

	public List<Integer> getSecurityStatus() {
		return SecurityStatus;
	}

	public void setSecurityStatus(List<Integer> securityStatus) {
		SecurityStatus = securityStatus;
	}

	public List<CountryData> getCountriesList() {
		return countriesList;
	}

	public void setCountriesList(List<CountryData> countriesList) {
		this.countriesList = countriesList;
	}

	public PersonInfoResultWithDetailedSecurityStatus getPersonInfoResult() {
		return personInfoResult;
	}

	public void setPersonInfoResult(PersonInfoResultWithDetailedSecurityStatus perosnInfoResult) {
		this.personInfoResult = perosnInfoResult;
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

	public Short getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(Short plateNumber) {
		this.plateNumber = plateNumber;
	}

	public Short getPlateType() {
		return plateType;
	}

	public void setPlateType(Short plateType) {
		this.plateType = plateType;
	}

	public CarInfoResult getCarInfoByPlateResult() {
		return carInfoByPlateResult;
	}

	public void setCarInfoByPlateResult(CarInfoResult carInfoByPlateResult) {
		this.carInfoByPlateResult = carInfoByPlateResult;
	}

	public List<PlateNumberEnum> getPlateNumberList() {
		return plateNumberList;
	}

	public void setPlateNumberList(List<PlateNumberEnum> plateNumberList) {
		this.plateNumberList = plateNumberList;
	}

	public Integer getNationalIdentity() {
		return SocialTypeEnum.NATIONAL_IDENTITY.getCode();
	}

	public Integer getResident() {
		return SocialTypeEnum.RESIDENT.getCode();
	}

	public Integer getVisitVisa() {
		return SocialTypeEnum.VISIT_VISA.getCode();
	}

	public Integer getKhaleejiPassport() {
		return SocialTypeEnum.KHALEEJI_PASSPORT.getCode();
	}

	public Integer getKhaleejiIdentity() {
		return SocialTypeEnum.KHALEEJI_IDENTITY.getCode();
	}

	public short getWanted() {
		return SecurityStatusEnum.WANTED.getCode();
	}

	public short getArrested() {
		return SecurityStatusEnum.ARRESTED.getCode();
	}

	public short getServiceSuspension() {
		return SecurityStatusEnum.SERVICE_SUSPENSION.getCode();
	}

	public short getReviwer() {
		return SecurityStatusEnum.REVIEWER.getCode();
	}

	public short getTravelBan() {
		return SecurityStatusEnum.TRAVEL_BAN.getCode();
	}

	public LifeStatus getLifeStatusDead() {
		return LifeStatus.D;
	}

	public LifeStatus getLifeStatusLive() {
		return LifeStatus.L;
	}

	public Gender getGenderMale() {
		return Gender.M;
	}

	public Gender getGenderFemale() {
		return Gender.F;
	}

	public short getPrivateCar() {
		return CarRegisterationTypEnum.PRIVATE_CAR.getCode();
	}

	public short getPublicTransport() {
		return CarRegisterationTypEnum.PUBLIC_TRANSPORT.getCode();
	}

	public short getPrivateTransport() {
		return CarRegisterationTypEnum.PRIVATE_TRANSPORT.getCode();
	}

	public short getPublicBus() {
		return CarRegisterationTypEnum.PUBLIC_BUS.getCode();
	}

	public short getPrivateBus() {
		return CarRegisterationTypEnum.PRIVATE_BUS.getCode();
	}

	public short getTaxi() {
		return CarRegisterationTypEnum.TAXI.getCode();
	}

	public short getHeavyEquipment() {
		return CarRegisterationTypEnum.HEAVY_EQUIPMENT.getCode();
	}

	public short getExport() {
		return CarRegisterationTypEnum.EXPORT.getCode();
	}

	public short getDiplomatic() {
		return CarRegisterationTypEnum.DIPLOMATIC.getCode();
	}

	public short getMotorCycle() {
		return CarRegisterationTypEnum.MOTORCYCLE.getCode();
	}

	public short getTemporary() {
		return CarRegisterationTypEnum.TEMPORARY.getCode();
	}

	public List<GccCountriesEnum> getGccCountriesList() {
		return gccCountriesList;
	}

	public void setGccCountriesList(List<GccCountriesEnum> gccCountriesList) {
		this.gccCountriesList = gccCountriesList;
	}

	public Long getPersonalPhotoId() {
		return personalPhotoId;
	}

	public void setPersonalPhotoId(Long personalPhotoId) {
		this.personalPhotoId = personalPhotoId;
	}

	public String getPersonInfoResultStatusString() {
		return personInfoResultStatusString;
	}

	public void setPersonInfoResultStatusString(String personInfoResultStatusString) {
		this.personInfoResultStatusString = personInfoResultStatusString;
	}
}