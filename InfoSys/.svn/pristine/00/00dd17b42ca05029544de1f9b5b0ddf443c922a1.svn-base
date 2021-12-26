package com.code.ui.backings.security;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;

import com.code.dal.orm.setup.SecurityStatusUser;
import com.code.enums.FlagsEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.SecurityStatusPrivilageEnum;
import com.code.enums.SessionAttributesEnum;
import com.code.services.infosys.securityaction.SecurityStatusService;
import com.code.ui.backings.base.BaseBacking;
import com.code.ui.util.EncryptionUtil;

@ManagedBean(name = "welcome")
@ViewScoped
public class Welcome extends BaseBacking implements Serializable {
	String welcomeMessage = getParameterizedMessage("title_welcomePage");
	boolean red;
	boolean superAdmin;
	boolean admin;
	boolean user;

	/**
	 * 
	 */
	public Welcome() {
		if (getRequest().getAttribute("blackList") != null) {
			welcomeMessage = getParameterizedMessage("label_travelBlackList");
			red = true;
		}
		super.setScreenTitle(welcomeMessage);
		applySecurityStatusPrivilages();

	}

	private void applySecurityStatusPrivilages() {
		try {
			HttpSession session = getSession();
			SecurityStatusUser superAdminUser = SecurityStatusService.getSecurityStatusUserByKeyAndType(EncryptionUtil.encryptSymmetrically(loginEmpData.getSocialID()), SecurityStatusPrivilageEnum.SUPER_ADMIN.getValue());
			if (superAdminUser != null) {
				this.superAdmin = true;
				session.setAttribute(SessionAttributesEnum.SECURITY_STATUS_SEARCH_FLAG.getCode(), FlagsEnum.ON.getCode());
				session.setAttribute(SessionAttributesEnum.SECURITY_STATUS_MANAGEMENT_FLAG.getCode(), FlagsEnum.ON.getCode());
				session.setAttribute(SessionAttributesEnum.SECURITY_STATUS_ADMIN_MANAGEMENT_FLAG.getCode(), FlagsEnum.ON.getCode());
			} else {
				SecurityStatusUser adminUser = SecurityStatusService.getSecurityStatusUserByKeyAndType(EncryptionUtil.encryptSymmetrically(loginEmpData.getSocialID()), SecurityStatusPrivilageEnum.ADMIN.getValue());
				if (adminUser != null) {
					this.admin = true;
					session.setAttribute(SessionAttributesEnum.SECURITY_STATUS_MANAGEMENT_FLAG.getCode(), FlagsEnum.ON.getCode());
				}
				SecurityStatusUser normalUser = SecurityStatusService.getSecurityStatusUserByKeyAndType(EncryptionUtil.encryptSymmetrically(loginEmpData.getSocialID()), SecurityStatusPrivilageEnum.USER.getValue());
				if (normalUser != null) {
					this.user = true;
					session.setAttribute(SessionAttributesEnum.SECURITY_STATUS_SEARCH_FLAG.getCode(), FlagsEnum.ON.getCode());
				}
			}
		} catch (Exception e) {
			this.setServerSideErrorMessages("error_general");
		}
	}

	/**
	 * Redirect to security status management screen
	 * 
	 * @return
	 */
	public String openSecurityManagementScreen() {
		return NavigationEnum.SECURITY_STATUS_MANAGEMENT.toString();
	}

	/**
	 * Redirect to security status search screen
	 * 
	 * @return
	 */
	public String openSecurityStatusScreen() {
		return NavigationEnum.SECURITY_STATUS_SEARCH.toString();
	}

	/**
	 * Redirect to security status admin managment screen
	 * 
	 * @return
	 */
	public String openAdminManagementScreen() {
		return NavigationEnum.SECURITY_ADMIN_MANAGEMENT.toString();
	}

	public String getWelcomeMessage() {
		return welcomeMessage;
	}

	public void setWelcomeMessage(String welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}

	public boolean isRed() {
		return red;
	}

	public void setRed(boolean red) {
		this.red = red;
	}

	public boolean isSuperAdmin() {
		return superAdmin;
	}

	public void setSuperAdmin(boolean superAdmin) {
		this.superAdmin = superAdmin;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isUser() {
		return user;
	}

	public void setUser(boolean user) {
		this.user = user;
	}
}
