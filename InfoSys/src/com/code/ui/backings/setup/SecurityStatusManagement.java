package com.code.ui.backings.setup;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.setup.SecurityStatusUser;
import com.code.enums.SecurityStatusPrivilageEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.securityaction.SecurityStatusService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.EmployeeService;
import com.code.ui.backings.base.BaseBacking;
import com.code.ui.util.EncryptionUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "securityStatusManagement")
@ViewScoped
public class SecurityStatusManagement extends BaseBacking implements Serializable {
	private SecurityStatusUser securityStatusUserInsert;
	private Integer securityStatusUserSelectedIndex;
	private List<SecurityStatusUser> SecurityStatusUsersList;
	private boolean securityStatusExpandFlag;

	/**
	 * Constructor
	 */
	public SecurityStatusManagement() {
		super();
		init();
	}

	/**
	 * Initialize bean variables
	 */
	public void init() {
		securityStatusUserInsert = new SecurityStatusUser();
		securityStatusUserSelectedIndex = null;
		securityStatusExpandFlag = false;
		try {
			SecurityStatusUsersList = SecurityStatusService.getSecurityStatusUserByType(SecurityStatusPrivilageEnum.USER.getValue());
			for (SecurityStatusUser user : SecurityStatusUsersList) {
				String socialId = EncryptionUtil.decryptSymmetrically(user.getKey());
				EmployeeData empData = EmployeeService.getEmployee(socialId);
				user.setFullName(empData.getFullName());
				user.setActualDepartmentName(empData.getActualDepartmentName());
				user.setSocialId(empData.getSocialID());
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SecurityStatusManagement.class, e, "SecurityStatusManagement");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Reset cooperator Type Insert Form
	 */
	public void resetCooperatorInsertForm() {
		securityStatusUserInsert = new SecurityStatusUser();
		securityStatusUserSelectedIndex = null;
		securityStatusExpandFlag = false;
	}

	/**
	 * Delete SecurityStatusUser
	 * 
	 * @param securityStatusUser
	 */
	public void deleteSecurityStatusUser(SecurityStatusUser securityStatusUser) {
		try {
			SecurityStatusService.deleteSecurityStatusUser(securityStatusUser, loginEmpData);
			SecurityStatusUsersList.remove(securityStatusUser);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Save SecurityStatusUser Type
	 * 
	 */
	public void saveSecurityStatusUser() {
		try {
			if (securityStatusUserInsert.getSocialId() == null || securityStatusUserInsert.getSocialId().trim().isEmpty()) {
				throw new BusinessException("error_fullNameMandatory");
			}
			EmployeeData admin = EmployeeService.getEmployee(securityStatusUserInsert.getSocialId());
			for (SecurityStatusUser user : SecurityStatusUsersList) {
				if (user.getSocialId().equals(admin.getSocialID())) {
					throw new BusinessException("error_alreadyChoosen");
				}
			}
			securityStatusUserInsert.setKey(EncryptionUtil.encryptSymmetrically(admin.getSocialID()));
			securityStatusUserInsert.setSystemFlag(true);
			securityStatusUserInsert.setActualDepartmentName(admin.getActualDepartmentName());
			securityStatusUserInsert.setType(SecurityStatusPrivilageEnum.USER.getValue());
			SecurityStatusService.saveSecurityStatusUser(securityStatusUserInsert, loginEmpData);
			SecurityStatusUsersList.add(securityStatusUserInsert);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			   Log4j.traceErrorException(SecurityStatusManagement.class, e, "SecurityStatusManagement");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
		securityStatusUserInsert = new SecurityStatusUser();
		securityStatusExpandFlag = true;
	}

	// Setters and getters
	public SecurityStatusUser getSecurityStatusUserInsert() {
		return securityStatusUserInsert;
	}

	public void setSecurityStatusUserInsert(SecurityStatusUser securityStatusUserInsert) {
		this.securityStatusUserInsert = securityStatusUserInsert;
	}

	public Integer getSecurityStatusUserSelectedIndex() {
		return securityStatusUserSelectedIndex;
	}

	public void setSecurityStatusUserSelectedIndex(Integer securityStatusUserSelectedIndex) {
		this.securityStatusUserSelectedIndex = securityStatusUserSelectedIndex;
	}

	public List<SecurityStatusUser> getSecurityStatusUsersList() {
		return SecurityStatusUsersList;
	}

	public void setSecurityStatusUsersList(List<SecurityStatusUser> securityStatusUsersList) {
		SecurityStatusUsersList = securityStatusUsersList;
	}

	public boolean isSecurityStatusExpandFlag() {
		return securityStatusExpandFlag;
	}

	public void setSecurityStatusExpandFlag(boolean securityStatusExpandFlag) {
		this.securityStatusExpandFlag = securityStatusExpandFlag;
	}
}