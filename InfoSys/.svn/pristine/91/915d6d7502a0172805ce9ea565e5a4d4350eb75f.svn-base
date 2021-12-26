package com.code.ui.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.code.dal.orm.security.MenuData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.enums.FlagsEnum;
import com.code.enums.MenuFlagEnum;
import com.code.enums.SessionAttributesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.log4j.Log4j;
import com.code.services.security.SecurityService;
import com.code.services.setup.EmployeeService;
import com.code.ui.util.EncryptionUtil;

public class AppPhaseListener implements PhaseListener {
	private static final long serialVersionUID = 1L;

	public void afterPhase(PhaseEvent phaseEvent) {
	}

	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

	@SuppressWarnings("unchecked")
	public void beforePhase(PhaseEvent phaseEvent) {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		// .. Unified WorList... etc
		/*---------Single Sign on-----------*/
		byPassLogin(req);
		/*----------------------------------*/
		boolean ajax = phaseEvent.getFacesContext().isPostback() && phaseEvent.getFacesContext().getPartialViewContext().isAjaxRequest();

		String curLang = (String) req.getSession().getAttribute(SessionAttributesEnum.CURRENT_LANG.getCode());
		if (curLang != null) {
			FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(curLang));
		}

		String requestURI = req.getRequestURI();
		requestURI = requestURI.substring(requestURI.indexOf("/", 1), requestURI.length());
		int index = requestURI.indexOf("?");
		if (index != -1) {
			requestURI = requestURI.substring(0, index);
		}
		//Start MS SQL
		index = requestURI.indexOf(";");
		if (index != -1)
			requestURI = requestURI.substring(0, index);
		//END MS SQL
		
		// Check Security Status Privilages
		if ((requestURI.endsWith("/SecurityAction/SecurityStatusSearch.jsf") && req.getSession().getAttribute(SessionAttributesEnum.SECURITY_STATUS_SEARCH_FLAG.getCode()).equals(FlagsEnum.ON.getCode())) || (requestURI.endsWith("/Setup/SecurityStatusManagement.jsf") && req.getSession().getAttribute(SessionAttributesEnum.SECURITY_STATUS_MANAGEMENT_FLAG.getCode()).equals(FlagsEnum.ON.getCode()))
				|| (requestURI.endsWith("/Setup/AdminManagement.jsf") && req.getSession().getAttribute(SessionAttributesEnum.SECURITY_STATUS_ADMIN_MANAGEMENT_FLAG.getCode()).equals(FlagsEnum.ON.getCode()))) {
			return;
		}

		if (!requestURI.endsWith("/Main/Login.jsf") && !requestURI.endsWith("Welcome.jsf") && !requestURI.endsWith("/Main/error.jsf")) {
			// If no session Emp_ID then redirect to login
			if (req.getSession().getAttribute(SessionAttributesEnum.EMP_DATA.getCode()) == null) {
				redirect("/Main/Login.jsf");
			}
			// If request doesn't contain parameter "TaskId" then check
			// priviledge
			else if (req.getParameter("taskId") == null && !ajax) {
				// check menue
				boolean authorized = false;
				List<MenuData> allMenus = new ArrayList<MenuData>();
				allMenus.addAll((List<MenuData>) req.getSession().getAttribute(SessionAttributesEnum.USER_MENU.getCode()));

				for (MenuData menuEntry : allMenus) {
					if (menuEntry.getUrl() != null && menuEntry.getUrl().contains(requestURI)) {
						authorized = true;
						break;
					}
				}
				// If user still not authorized
				if (!authorized) {
					req.setAttribute("privilegeError", "error");
					redirect("/Main/Welcome.jsf");
				}
			}
		} else if (!requestURI.endsWith("Welcome.jsf") && !requestURI.endsWith("/Main/error.jsf")) {
			boolean skip = false;
			if (req.getAttribute("skipLogin") != null)
				skip = (Boolean) req.getAttribute("skipLogin");
			if (req.getParameter("logout") != null)
				skip = false;
			HttpSession session = req.getSession();
			if (session.getAttribute(SessionAttributesEnum.EMP_DATA.getCode()) == null)
				skip = false;
			if (skip)
				redirect("/Main/Welcome.jsf");
		}
	}

	/**
	 * By Pass Login
	 * 
	 * @param req
	 */
	private void byPassLogin(HttpServletRequest req) {
		Map<String, String[]> map = req.getParameterMap();
		if (map.containsKey("user") && map.get("user").length != 0) {// coming from unified-worklist
			try {
				String userNameDecrypted = map.get("user")[0];
				String employeeEmail = EncryptionUtil.decryptSymmetrically(userNameDecrypted);

				EmployeeData empData = EmployeeService.getEmployeeByEmail(employeeEmail);

				HttpSession session = req.getSession();
				session.setAttribute(SessionAttributesEnum.EMP_DATA.getCode(), empData);
				List<MenuData> userMenus = SecurityService.getEmployeeMenus(empData.getEmpId(), null, null);
				userMenus.addAll(SecurityService.getEmployeeMenusByMenuFlag(MenuFlagEnum.INQUIRES.getCode()));
				userMenus.addAll(SecurityService.getEmployeeMenusByMenuFlag(MenuFlagEnum.WORKFLOW_MENUS.getCode()));
				session.setAttribute(SessionAttributesEnum.USER_MENU.getCode(), userMenus);
				session.setAttribute(SessionAttributesEnum.USER_MENU_TREE.getCode(), SecurityService.getEmployeeMenus(empData.getEmpId(), null, new Integer[] { 1 }));
				session.setAttribute(SessionAttributesEnum.USER_MENU_WORKFLOW_TREE.getCode(), SecurityService.getEmployeeMenus(empData.getEmpId(), null, new Integer[] { 2 }));
				session.setAttribute(SessionAttributesEnum.USER_MENU_REPORT_TREE.getCode(), SecurityService.getEmployeeMenus(empData.getEmpId(), null, new Integer[] { 3 }));
			} catch (BusinessException e) {
				Log4j.traceErrorException(AppPhaseListener.class, e, "AppPhaseListener");
			}catch (Exception e) {
				   Log4j.traceErrorException(AppPhaseListener.class, e, "AppPhaseListener");
			  }
		}
	}

	private void redirect(String viewURI) {
		Application app = FacesContext.getCurrentInstance().getApplication();
		ViewHandler viewHandler = app.getViewHandler();
		UIViewRoot viewRoot = viewHandler.createView(FacesContext.getCurrentInstance(), viewURI);
		FacesContext.getCurrentInstance().setViewRoot(viewRoot);
	}
}