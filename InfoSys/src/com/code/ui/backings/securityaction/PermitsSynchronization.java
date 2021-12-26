package com.code.ui.backings.securityaction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.UploadedFile;

import com.code.dal.orm.securitymission.CarPermit;
import com.code.dal.orm.securitymission.EmployeeNonEmployeeCars;
import com.code.dal.orm.securitymission.NonEmployeePermit;
import com.code.dal.orm.securitymission.PermitsPatch;
import com.code.dal.orm.securitymission.PermitsPatchDetails;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.enums.PermitsPatchTypesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.securityaction.PermitsService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.SetupService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "permitsSynchronization")
@ViewScoped
public class PermitsSynchronization extends BaseBacking implements Serializable {
	private int permitsPatchType = 0;
	private UploadedFile file;
	private List<PermitsPatchTypesEnum> permitsPatchTypesEnumList;
	private PermitsPatch patch;
	private List<PermitsPatchDetails> permitsPatchDetailsList;
	private List<ColumnModel> columns;
	private NonEmployeePermit nonEmpPermit;
	private String nonEmpName;
	private String depName;
	private EmployeeNonEmployeeCars employeeNonEmployeeCars;
	private CarPermit carPermit;
	private int permitCarType = 0;
	private String empName;
	private List<SetupDomain> carModels;
	private Long loginEmpRegionId;
	private String loginEmpRegionName;

	public PermitsSynchronization() throws BusinessException {
		permitsPatchTypesEnumList = Arrays.asList(PermitsPatchTypesEnum.values());
		permitsPatchDetailsList = new ArrayList<PermitsPatchDetails>();
		nonEmpPermit = new NonEmployeePermit();
		employeeNonEmployeeCars = new EmployeeNonEmployeeCars();
		carPermit = new CarPermit();
		carModels = SetupService.getDomains(ClassesEnum.CAR_MODELS.getCode());
		try {
			Long regionId = DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());
			if (regionId != null) {
				loginEmpRegionId = regionId;
				loginEmpRegionName = DepartmentService.getDepartment(loginEmpRegionId).getArabicName();
			} else {
				loginEmpRegionId = getHeadQuarter();
				loginEmpRegionName = DepartmentService.getDepartment(loginEmpRegionId).getArabicName();
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * synchrnoziePermits
	 */
	public void synchrnoziePermits() {
		if (file != null) {
			try {
				String fileName = new String(file.getFileName().getBytes(), "UTF-8");
				saveUploadedFile(fileName, file.getInputstream());
			} catch (Exception e) {
				Log4j.traceErrorException(PermitsSynchronization.class, e, "PermitsSynchronization");
				this.setServerSideErrorMessages(getParameterizedMessage("error_loadingFile"));
			}
		}
	}

	/**
	 * createDynamicColumns
	 */
	private void createDynamicColumns() {
		columns = new ArrayList<ColumnModel>();
		if (patch != null) {
			if (patch.getPatchType().equals(PermitsPatchTypesEnum.PERSONS.getCode())) {
				columns.add(new ColumnModel(getParameterizedMessage("label_socialId"), "field1"));
				columns.add(new ColumnModel(getParameterizedMessage("label_name"), "field2"));
				columns.add(new ColumnModel(getParameterizedMessage("label_nationality"), "field3"));
				columns.add(new ColumnModel(getParameterizedMessage("label_departmentName"), "field4"));
				columns.add(new ColumnModel(getParameterizedMessage("label_phoneNumber"), "field5"));
				columns.add(new ColumnModel(getParameterizedMessage("label_permitEndDate"), "field6"));
				columns.add(new ColumnModel(getParameterizedMessage("label_errorDesc"), "errorDesc"));
				columns.add(new ColumnModel(getParameterizedMessage("label_rowNum"), "rowNumber"));
			} else {
				columns.add(new ColumnModel(getParameterizedMessage("label_permitNo"), "field1"));
				columns.add(new ColumnModel(getParameterizedMessage("label_socialId"), "field2"));
				columns.add(new ColumnModel(getParameterizedMessage("label_name"), "field3"));
				columns.add(new ColumnModel(getParameterizedMessage("label_departmentName"), "field4"));
				columns.add(new ColumnModel(getParameterizedMessage("label_rank"), "field5"));
				columns.add(new ColumnModel(getParameterizedMessage("label_telExt"), "field6"));
				columns.add(new ColumnModel(getParameterizedMessage("label_carModel"), "field7"));
				columns.add(new ColumnModel(getParameterizedMessage("label_plateNumber"), "field8"));
				columns.add(new ColumnModel(getParameterizedMessage("label_remarks"), "field9"));
				columns.add(new ColumnModel(getParameterizedMessage("label_errorDesc"), "errorDesc"));
				columns.add(new ColumnModel(getParameterizedMessage("label_rowNum"), "rowNumber"));
			}
		}
	}

	/**
	 * Define file path then upload file
	 * 
	 * @param fileName
	 * @param is
	 */
	public void saveUploadedFile(String fileName, InputStream is) {
		try {
			File uploadFilePath = new File(InfoSysConfigurationService.getReportsRoot() + "/tempUpload");
			if (!uploadFilePath.exists())
				uploadFilePath.mkdir();
			String timedFilename = Calendar.getInstance().getTimeInMillis() + fileName.substring(fileName.lastIndexOf("."));
			FileOutputStream fos = new FileOutputStream(uploadFilePath.getCanonicalPath() + "/" + timedFilename);
			int BUFFER_SIZE = 8192;
			byte[] buffer = new byte[BUFFER_SIZE];
			int a;
			while (true) {
				a = is.read(buffer);
				if (a < 0)
					break;
				fos.write(buffer, 0, a);
				fos.flush();
			}
			fos.close();
			is.close();
			patch = PermitsService.readPermitsData(permitsPatchType == 0 ? PermitsPatchTypesEnum.PERSONS.getCode() : PermitsPatchTypesEnum.CARS.getCode(), uploadFilePath.getCanonicalPath() + "/" + timedFilename, loginEmpData);
			permitsPatchDetailsList = PermitsService.getPermitsPatchDetails(patch.getId());
			createDynamicColumns();
		} catch (Exception e) {
			Log4j.traceErrorException(PermitsSynchronization.class, e, "PermitsSynchronization");
			this.setServerSideErrorMessages(getParameterizedMessage("error_loadingFile"));
		}
	}

	/**
	 * saveNonEmpPermit
	 */
	public void saveNonEmpPermit() {
		try {
			nonEmpPermit.setRegionId(loginEmpRegionId);
			PermitsService.saveNonEmployeePermit(nonEmpPermit, loginEmpData);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
			resetEmpPermit();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage()));
		}
	}

	/**
	 * resetEmpPermit
	 */
	public void resetEmpPermit() {
		nonEmpPermit = new NonEmployeePermit();
		depName = null;
		nonEmpName = null;
	}

	/**
	 * saveCarPermit
	 */
	public void saveCarPermit() {
		try {
			for (SetupDomain carModel : carModels) {
				if (employeeNonEmployeeCars.getDomainCarModelId().equals(carModel.getId())) {
					employeeNonEmployeeCars.setDomainCarModelDescription(carModel.getDescription());
					break;
				}
			}
			employeeNonEmployeeCars.setRegionId(loginEmpRegionId);
			PermitsService.saveCarEmployeePermit(employeeNonEmployeeCars, carPermit, loginEmpData);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
			resetCarPermit();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage()));
		}
	}

	/**
	 * resetCarPermit
	 */
	public void resetCarPermit() {
		carPermit = new CarPermit();
		employeeNonEmployeeCars = new EmployeeNonEmployeeCars();
		depName = null;
		nonEmpName = null;
		empName = null;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		if (file == null) {
			setServerSideErrorMessages(getParameterizedMessage("error_mandatoryFileType"));
		} else {
			if (file.getFileName().endsWith("xls") || file.getFileName().endsWith("xlsx")) {
				this.file = file;
			} else {
				setServerSideErrorMessages(getParameterizedMessage("error_errorFileType"));
			}
		}
	}

	public String getTemplatePath() {
		if (permitsPatchType == 1) {
			return "/TemplateDocuments/CarsTemplate.xlsx";
		} else {
			return "/TemplateDocuments/PersonsTemplate.xlsx";
		}
	}

	public String getComleteMsg() {
		if (patch != null) {
			return getParameterizedMessage("label_completePermitMsg", new Object[] { patch.getSuccessCount(), patch.getFailureCount() });
		}
		return "";
	}

	public int getPermitsPatchType() {
		return permitsPatchType;
	}

	public void setPermitsPatchType(int permitsPatchType) {
		this.permitsPatchType = permitsPatchType;
	}

	public List<PermitsPatchTypesEnum> getPermitsPatchTypesEnumList() {
		return permitsPatchTypesEnumList;
	}

	public void setPermitsPatchTypesEnumList(List<PermitsPatchTypesEnum> permitsPatchTypesEnumList) {
		this.permitsPatchTypesEnumList = permitsPatchTypesEnumList;
	}

	public List<PermitsPatchDetails> getPermitsPatchDetailsList() {
		return permitsPatchDetailsList;
	}

	public void setPermitsPatchDetailsList(List<PermitsPatchDetails> permitsPatchDetailsList) {
		this.permitsPatchDetailsList = permitsPatchDetailsList;
	}

	public PermitsPatch getPatch() {
		return patch;
	}

	public void setPatch(PermitsPatch patch) {
		this.patch = patch;
	}

	public List<ColumnModel> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnModel> columns) {
		this.columns = columns;
	}

	public NonEmployeePermit getNonEmpPermit() {
		return nonEmpPermit;
	}

	public void setNonEmpPermit(NonEmployeePermit nonEmpPermit) {
		this.nonEmpPermit = nonEmpPermit;
	}

	public String getNonEmpName() {
		return nonEmpName;
	}

	public void setNonEmpName(String nonEmpName) {
		this.nonEmpName = nonEmpName;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public EmployeeNonEmployeeCars getEmployeeNonEmployeeCars() {
		return employeeNonEmployeeCars;
	}

	public void setEmployeeNonEmployeeCars(EmployeeNonEmployeeCars employeeNonEmployeeCars) {
		this.employeeNonEmployeeCars = employeeNonEmployeeCars;
	}

	public CarPermit getCarPermit() {
		return carPermit;
	}

	public void setCarPermit(CarPermit carPermit) {
		this.carPermit = carPermit;
	}

	public int getPermitCarType() {
		return permitCarType;
	}

	public void setPermitCarType(int permitCarType) {
		this.permitCarType = permitCarType;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public List<SetupDomain> getCarModels() {
		return carModels;
	}

	public void setCarModels(List<SetupDomain> carModels) {
		this.carModels = carModels;
	}

	public Long getHeadQuarter() {
		return InfoSysConfigurationService.getHeadQuarter();
	}

	public String getLoginEmpRegionName() {
		return loginEmpRegionName;
	}

	public void setLoginEmpRegionName(String loginEmpRegionName) {
		this.loginEmpRegionName = loginEmpRegionName;
	}

	/**********************************************
	 * ***********************************************
	 */
	static public class ColumnModel implements Serializable {

		private String header;
		private String property;

		public ColumnModel(String header, String property) {
			this.header = header;
			this.property = property;
		}

		public String getHeader() {
			return header;
		}

		public String getProperty() {
			return property;
		}
	}
}
