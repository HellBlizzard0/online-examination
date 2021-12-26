package com.code.filters;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.code.dal.orm.security.MenuData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.enums.MenuFlagEnum;
import com.code.enums.SessionAttributesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.log4j.Log4j;
import com.code.services.security.SecurityService;
import com.code.services.setup.EmployeeService;

import jcifs.http.NtlmHttpFilter;
import jcifs.smb.NtlmPasswordAuthentication;

public class LoginFilter extends NtlmHttpFilter {
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		jcifs.Config.setProperty("jcifs.http.domainController", InfoSysConfigurationService.getLDAPIP());
		jcifs.Config.setProperty("jcifs.smb.client.domain", InfoSysConfigurationService.getLDAPDomain());
		super.init(arg0);

	}

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		jcifs.Config.setProperty("jcifs.smb.client.username", InfoSysConfigurationService.getLDAPAdminUsername());
		jcifs.Config.setProperty("jcifs.smb.client.password", InfoSysConfigurationService.getLDAPAdminPassword());
		super.doFilter(request, response, chain);
		System.out.println(((HttpServletRequest) request).getRequestURI());
		System.out.println(((HttpServletRequest) request).getHeader("Authorization"));
	}

	@Override
	protected NtlmPasswordAuthentication negotiate(HttpServletRequest arg0, HttpServletResponse arg1, boolean arg2) throws IOException, ServletException {
		NtlmPasswordAuthentication authentication = super.negotiate(arg0, arg1, arg2);
		if (authentication == null)
			return null;

		String username = authentication.getUsername();
		if (username != null)
			try {
				authenticateUser(username, arg0, arg1);
			} catch (BusinessException e) {
				Log4j.traceErrorException(LoginFilter.class, e, "LoginFilter");
			}
		return authentication;
	}

	public void authenticateUser(String username, HttpServletRequest req, HttpServletResponse arg1) throws BusinessException {
		try {
			HttpSession session = req.getSession();
			if (session != null && session.getAttribute(SessionAttributesEnum.EMP_DATA.getCode()) != null) {
				req.setAttribute("skipLogin", true);
				return;
			}
			if (req.getParameter("logout") != null) {
				req.setAttribute("skipLogin", false);
				return;
			}
			EmployeeData empData = EmployeeService.getEmployeeByEmail(username);
			session.setAttribute(SessionAttributesEnum.EMP_DATA.getCode(), empData);
			boolean token = SecurityService.checkUserMitoken(empData.getEmpId());
			if (!token) {
				List<MenuData> userMenus = SecurityService.getEmployeeMenus(empData.getEmpId(), null, null);
				userMenus.addAll(SecurityService.getEmployeeMenusByMenuFlag(MenuFlagEnum.INQUIRES.getCode()));
				session.setAttribute(SessionAttributesEnum.USER_MENU.getCode(), userMenus);
				session.setAttribute(SessionAttributesEnum.USER_MENU_TREE.getCode(), SecurityService.getEmployeeMenus(empData.getEmpId(), null, new Integer[] { 1 }));
				session.setAttribute(SessionAttributesEnum.USER_MENU_WORKFLOW_TREE.getCode(), SecurityService.getEmployeeMenus(empData.getEmpId(), null, new Integer[] { 2 }));
				session.setAttribute(SessionAttributesEnum.USER_MENU_REPORT_TREE.getCode(), SecurityService.getEmployeeMenus(empData.getEmpId(), null, new Integer[] { 3 }));
				req.setAttribute("skipLogin", true);
			} else {
				req.setAttribute("skipLogin", false);
			}

		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			Log4j.traceErrorException(LoginFilter.class, e, "LoginFilter");
			throw new BusinessException("error_LDAPError");
		} finally {

		}
	}
}
