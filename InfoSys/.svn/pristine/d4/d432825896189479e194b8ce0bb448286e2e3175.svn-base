package com.code.ui.backings.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;

import com.code.dal.DataAccess;
import com.code.dal.orm.security.MenuData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.enums.MenuFlagEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.SessionAttributesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.security.MITokenService;
import com.code.services.security.SecurityService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "login")
@ViewScoped
public class Login extends BaseBacking implements Serializable {
	private String username;
	private String password;
	private String token;
	private String serverSideErrorMessages;
	private int mode;
	String copyRightMessage = this.getParameterizedMessage("label_copyright", new Object[] { Calendar.getInstance().get(Calendar.YEAR) + "" });

	@PostConstruct
	public void init() {
		super.init();
		username = null;
		password = null;
		HttpSession session = getSession();
		if (session.getAttribute(SessionAttributesEnum.EMP_DATA.getCode()) == null) {
			mode = 1;
		} else {
			mode = 2;
		}
	}

	public String login() {
		try {
			HttpSession session = getSession();
			EmployeeData empData = null;
			boolean ldapFlag = Boolean.valueOf(DataAccess.configuration.getString("ldapFlag"));
			if (mode == 1) {
				if (ldapFlag) {
					empData = SecurityService.authenticateUser(username, password);
				} else {
					empData = SecurityService.authenticateUserTemp(username, password);
				}
				session.setAttribute(SessionAttributesEnum.EMP_DATA.getCode(), empData);
			} else {
				empData = (EmployeeData) session.getAttribute(SessionAttributesEnum.EMP_DATA.getCode());
			}

			boolean tokenFlag = SecurityService.checkUserMitoken(empData.getEmpId());

			if (tokenFlag && mode == 2) {
				if (ldapFlag) {
					MITokenService.verify(username, password, token);
				}
				return prepareSessionAttributes(session, empData);
			} else if (!tokenFlag) {
				return prepareSessionAttributes(session, empData);
			}
			mode = 2;
		} catch (BusinessException e) {
			serverSideErrorMessages = getParameterizedMessage(e.getMessage(), e.getParams());
		}
		return NavigationEnum.FAILURE.toString();
	}

	/**
	 * Prepare Session Attributes
	 * 
	 * @param session
	 * @param empData
	 * @return
	 * @throws BusinessException
	 */
	private String prepareSessionAttributes(HttpSession session, EmployeeData empData) throws BusinessException {
		List<MenuData> userMenus = SecurityService.getEmployeeMenus(empData.getEmpId(), null, null);
		List<MenuData> normalMenuList = new ArrayList<MenuData>();
		List<MenuData> workflowUserMenus = new ArrayList<MenuData>();
		List<MenuData> reportMenuList = new ArrayList<MenuData>();
		for (MenuData menu : userMenus) {
			if (menu.getMenuFlag().equals(MenuFlagEnum.NORMAL_MENUS.getCode())) {
				normalMenuList.add(menu);
			} else if (menu.getMenuFlag().equals(MenuFlagEnum.WORKFLOW_MENUS.getCode())) {
				workflowUserMenus.add(menu);
			} else if (menu.getMenuFlag().equals(MenuFlagEnum.REPORT_MENUS.getCode())) {
				reportMenuList.add(menu);
			}
		}
		userMenus.addAll(SecurityService.getEmployeeMenusByMenuFlag(MenuFlagEnum.INQUIRES.getCode()));
		userMenus.addAll(SecurityService.getEmployeeMenusByMenuFlag(MenuFlagEnum.WORKFLOW_MENUS.getCode()));
		session.setAttribute(SessionAttributesEnum.USER_MENU.getCode(), userMenus);
		session.setAttribute(SessionAttributesEnum.USER_MENU_TREE.getCode(), normalMenuList);
		session.setAttribute(SessionAttributesEnum.USER_MENU_WORKFLOW_TREE.getCode(), workflowUserMenus);
		session.setAttribute(SessionAttributesEnum.USER_MENU_REPORT_TREE.getCode(), reportMenuList);
		return NavigationEnum.SUCCESS.toString();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public String getServerSideErrorMessages() {
		return serverSideErrorMessages;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public String getCopyRightMessage() {
		return copyRightMessage;
	}
}