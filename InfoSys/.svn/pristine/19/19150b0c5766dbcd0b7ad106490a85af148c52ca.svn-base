package com.code.ui.backings.surveillance;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.setup.EmployeeNonEmployeeDetail;
import com.code.dal.orm.setup.EmpNonEmpRelative;
import com.code.dal.orm.setup.Image;
import com.code.dal.orm.setup.NonEmployeeData;
import com.code.dal.orm.setup.QualificationData;
import com.code.enums.FlagsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.log4j.Log4j;
import com.code.services.setup.EmployeeService;
import com.code.services.setup.ImageService;
import com.code.services.setup.NonEmployeeService;
import com.code.services.util.CommonService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "surveillanceEmployeeDetails")
@ViewScoped
public class SurveillanceEmployeeDetails extends BaseBacking implements Serializable {
	private EmployeeData employee;
	private NonEmployeeData nonEmployee;
	private EmployeeNonEmployeeDetail employeeDetail;
	private List<EmpNonEmpRelative> employeeRelativesList;
	private List<QualificationData> qualificationsDataList;
	private Image personalPhoto;
	private boolean relativeExpand;

	/**
	 * Constructor
	 */
	public SurveillanceEmployeeDetails() {
		super();
		try {
			Long empId = null, nonEmpId = null;
			if(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("empId") != null && !FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("empId").equals("")) {
				empId = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("empId"));
				nonEmpId = null;
				employee = EmployeeService.getEmployee(empId);
			} else if(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("nonEmpId") != null && !FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("nonEmpId").equals("")) {
				empId = null;
				nonEmpId = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("nonEmpId"));
				nonEmployee = NonEmployeeService.getNonEmployeeById(nonEmpId);
				employee = new EmployeeData();
				employee.setFullName(nonEmployee.getFullName());
				employee.setSocialID(nonEmployee.getIdentity().toString());
				employee.setMobileNumber(nonEmployee.getPhoneNo());
			}
			
			List<EmployeeNonEmployeeDetail> employeeDetailsTemp = EmployeeService.getEmpNonEmpDetails(empId, nonEmpId);
			employeeDetail = employeeDetailsTemp.isEmpty() ? new EmployeeNonEmployeeDetail() : employeeDetailsTemp.get(0);
			employeeDetail.setEmpId(empId);
			employeeDetail.setNonEmpId(nonEmpId);
			employeeRelativesList = EmployeeService.getEmployeeRelatives(empId, nonEmpId);
			qualificationsDataList = CommonService.getQualifications();
			personalPhoto = ImageService.getImageById(employeeDetail.getImageId() == null ? FlagsEnum.ALL.getCode() : employeeDetail.getImageId());
			if (personalPhoto == null) {
				personalPhoto = new Image();
				personalPhoto.setId(0L);
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SurveillanceEmployeeDetails.class, e, "SurveillanceEmployeeDetails");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Upload image virtually
	 * 
	 * @param event
	 */
	public void uploadListener(FileUploadEvent event) {
		try {
			UploadedFile file = event.getFile();
			personalPhoto.setContent(file.getContents());
			personalPhoto.setType(file.getContentType());
			if (personalPhoto.getContent() == null || personalPhoto.getContent().length == 0) { // No File Upload.
				return;
			} else {
				if (employeeDetail.getImageId() == null) {
					ImageService.insertImage(personalPhoto);
				} else {
					ImageService.updateImage(personalPhoto);
				}
				employeeDetail.setImageId(personalPhoto.getId());
				EmployeeService.saveUpdateEmployeeDetails(employeeDetail, loginEmpData);
			}
		} catch (Exception e) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	/**
	 * Insert New Relative
	 */
	public void insertRelative() {
		EmpNonEmpRelative newRelative = new EmpNonEmpRelative();
		newRelative.setEmpId(employeeDetail.getEmpId());
		newRelative.setNonEmpId(employeeDetail.getNonEmpId());
		employeeRelativesList.add(newRelative);
	}

	/**
	 * Delete Relative
	 * 
	 * @param employeeRelative
	 */
	public void deleteRelative(EmpNonEmpRelative employeeRelative) {
		try {
			if (employeeRelative.getId() != null) {
				EmployeeService.deleteEmployeeRelative(employeeRelative, loginEmpData);
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return;
		}
		employeeRelativesList.remove(employeeRelative);
	}

	/**
	 * Save Details
	 */
	public void saveDetails() {
		try {
			for(QualificationData qualificationData: qualificationsDataList) {
				if(qualificationData.getId().equals(employeeDetail.getQualificationId())) {
					employeeDetail.setQualificationDescription(qualificationData.getDescription());
				}
			}
			EmployeeService.saveEmployeeDetails(employeeDetail, employeeRelativesList, loginEmpData);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	// setters and getters
	public EmployeeData getEmployee() {
		return employee;
	}

	public void setEmployee(EmployeeData employee) {
		this.employee = employee;
	}

	public EmployeeNonEmployeeDetail getEmployeeDetail() {
		return employeeDetail;
	}

	public void setEmployeeDetail(EmployeeNonEmployeeDetail employeeDetail) {
		this.employeeDetail = employeeDetail;
	}

	public List<EmpNonEmpRelative> getEmployeeRelativesList() {
		return employeeRelativesList;
	}

	public void setEmployeeRelativesList(List<EmpNonEmpRelative> employeeRelativesList) {
		this.employeeRelativesList = employeeRelativesList;
	}

	public List<QualificationData> getQualificationsDataList() {
		return qualificationsDataList;
	}

	public void setQualificationsDataList(List<QualificationData> qualificationsDataList) {
		this.qualificationsDataList = qualificationsDataList;
	}

	public boolean isRelativeExpand() {
		return relativeExpand;
	}

	public void setRelativeExpand(boolean relativeExpand) {
		this.relativeExpand = relativeExpand;
	}

	public Image getPersonalPhoto() {
		return personalPhoto;
	}

	public void setPersonalPhoto(Image personalPhoto) {
		this.personalPhoto = personalPhoto;
	}
}