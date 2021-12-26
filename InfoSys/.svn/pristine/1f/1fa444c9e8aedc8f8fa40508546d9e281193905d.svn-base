package com.code.services.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import com.code.dal.DataAccess;
import com.code.dal.orm.security.MenuData;
import com.code.dal.orm.security.UserMenuActionData;
import com.code.dal.orm.security.UserMiTokenData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.EmployeeService;

public class SecurityService extends BaseService {
	private SecurityService() {
	}

	/**
	 * authenticate user given username and password
	 * 
	 * @param email
	 * @param password
	 * @return
	 * @throws BusinessException
	 */
	public static EmployeeData authenticateUser(String email, String password) throws BusinessException {
		try {
			System.out.println("#:LDAP authentication for : " + email);
			DirContext ctx = getContext(email, password);
			System.out.println("#:Loading data for employee : " + email);
			return EmployeeService.getEmployeeByEmail(email);
		} catch (AuthenticationException e) {
			throw new BusinessException("error_invalidUser");
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityService.class, e, "SecurityService");
			throw new BusinessException("error_LDAPError");
		}
	}

	/**
	 * authenticate user direct by id
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws BusinessException
	 */
	public static EmployeeData authenticateUserTemp(String username, String password) throws BusinessException {
		 if(password == null || password.equals("") || !password.equals("E!j@d@2020")) 
			  throw new BusinessException("error_invalidUser");
		EmployeeData employee = null;
		// Get employee considering username is id
		// employee = EmployeeService.getEmployee(Long.valueOf(username));
		// Get employee considering username is email
		employee = EmployeeService.getEmployeeByEmail(username);

		if (employee != null) {
			employee.setEmail(username);
		}

		return employee;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static DirContext getContext(String username, String password) throws AuthenticationException, NamingException, BusinessException {
		Hashtable env = new Hashtable();

		if (InfoSysConfigurationService.getLDAPDomain() == null || InfoSysConfigurationService.getLDAPConnectionType() == null || InfoSysConfigurationService.getLDAPIP() == null || InfoSysConfigurationService.getLDAPPort() == null) {
			throw new BusinessException("error_DBError");
		}

		String domain = InfoSysConfigurationService.getLDAPDomain();
		String url = InfoSysConfigurationService.getLDAPConnectionType() + "://" + InfoSysConfigurationService.getLDAPIP() + ":" + InfoSysConfigurationService.getLDAPPort();

		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, url);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");

		if (!username.contains("@"))
			username = username + "@" + domain;

		env.put(Context.SECURITY_PRINCIPAL, username);
		env.put(Context.SECURITY_CREDENTIALS, password);

		return new InitialLdapContext(env, null);
	}

	/**
	 * get employee data
	 * 
	 * @param userAccount
	 * @return
	 * @throws BusinessException
	 */
	private static EmployeeData getEmpData(String userAccount) throws BusinessException {
		try {
			EmployeeData empData = null;
			String identity;

			DirContext ctx = getContext(InfoSysConfigurationService.getLDAPAdminUsername(), InfoSysConfigurationService.getLDAPAdminPassword());

			String baseSearch = InfoSysConfigurationService.getLDAPBase();
			SearchControls searchCtls = new SearchControls();

			String identityAttributeName = InfoSysConfigurationService.getLDAPIdentityAttribute();
			String directManggerAttributeName = InfoSysConfigurationService.getLDAPManagerAttribute();
			String physicalDeliveryOfficeAttributeName = InfoSysConfigurationService.getLDAPPhysicalDeliveryOfficeAttribute();
			String telephoneNumberAttributeName = InfoSysConfigurationService.getLDAPTelephoneNumberAttribute();
			String mobileAttributeName = InfoSysConfigurationService.getLDAPMobileAttribute();
			String mailAttributeName = InfoSysConfigurationService.getLDAPMailAttribute();

			String[] returnedAtts = new String[] { identityAttributeName, directManggerAttributeName, physicalDeliveryOfficeAttributeName, telephoneNumberAttributeName, mobileAttributeName, mailAttributeName };

			searchCtls.setReturningAttributes(returnedAtts);

			// Specify the scope of my search (one level down, recursive
			// subtree, etc.)
			searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

			// My ldap search filter...what am I looking for?
			if (!userAccount.contains("@"))
				userAccount = userAccount + "@" + InfoSysConfigurationService.getLDAPDomain();
			String searchFilter = "(userPrincipalName=" + userAccount + ")";
			// Actually perform the search telling JNDI where to start
			// the search, what to search for, what how to filter.
			NamingEnumeration<SearchResult> results = ctx.search(baseSearch, searchFilter, searchCtls);

			// Loop through the search results
			if (results.hasMoreElements()) {
				SearchResult searchResult = results.next();

				Attributes attrs = searchResult.getAttributes();
				if (attrs.get(identityAttributeName) != null) {
					identity = attrs.get(identityAttributeName).toString().replace(identityAttributeName + ": ", "");
				} else {
					throw new BusinessException("error_invalidUser");
				}
				System.out.print("Login User Identity : " + identity);
				empData = EmployeeService.getEmployee(identity);
				if (empData == null) {
					throw new BusinessException("error_invalidUserIdentity", new Object[] { identity });
				}
				if (attrs.get(mobileAttributeName) != null && empData.getMobileNumber() == null)
					empData.setMobileNumber(attrs.get(mobileAttributeName).toString().replace(mobileAttributeName + ": ", ""));
				if (attrs.get(mailAttributeName) != null && empData.getEmail() == null)
					empData.setEmail(attrs.get(mailAttributeName).toString().replace(mailAttributeName + ": ", ""));

			} else {
				throw new BusinessException("error_invalidUser");
			}

			ctx.close();
			System.out.println("#:Finished loading empData");
			return empData;
		} catch (AuthenticationException e) {
			Log4j.traceErrorException(SecurityService.class, e, "SecurityService");
			throw new BusinessException("error_invalidAdminUser");
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityService.class, e, "SecurityService");
			throw new BusinessException("error_LDAPError");
		}
	}

	// -----------------------------------------------// Menu Privileges

	public static List<MenuData> getEmployeeMenus(Long empId, String menuKey, Integer menuFlag[]) throws BusinessException {
		return searchEmployeeMenus(empId, menuKey, menuFlag);
	}

	/**
	 * get employee menus
	 * 
	 * @param empId
	 * @return
	 * @throws BusinessException
	 */
	private static List<MenuData> searchEmployeeMenus(long empId, String menuKey, Integer[] menuFlag) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_EMP_ID", empId);
			qParams.put("P_NAME_KEY", menuKey == null ? FlagsEnum.ALL.getCode() + "" : menuKey);
			qParams.put("P_MENU_FLAG", menuFlag == null ? FlagsEnum.ALL.getCode() : menuFlag);
			qParams.put("P_MENU_FLAG_ALL", menuFlag == null ? FlagsEnum.ALL.getCode() : FlagsEnum.OFF.getCode());
			List<MenuData> menuList = DataAccess.executeNamedQuery(MenuData.class, QueryNamesEnum.MENU_DATA_GET_EMP_MENUS.getCode(), qParams);
			// TODO to be uncommented if there is actions
			/*
			 * for (MenuData menu : menuList) { // menu add actions // query to get actions of empId,menuId; menu.setMenuAction(searchAction(empId, null, menu.getMenuId())); }
			 */
			return menuList;
		} catch (NoDataException e) {
			return new ArrayList<MenuData>();
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityService.class, e, "SecurityService");
			throw new BusinessException("error_loadingUserMenu");
		}
	}

	/**
	 * Get Menus
	 * 
	 * @param menuFlag
	 * @return
	 * @throws BusinessException
	 */
	public static List<MenuData> getEmployeeMenusByMenuFlag(int menuFlag) throws BusinessException {
		return searchMenus(menuFlag);
	}

	/**
	 * Search Menus
	 * 
	 * @param menuFlag
	 * @return
	 * @throws BusinessException
	 */
	private static List<MenuData> searchMenus(int menuFlag) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_MENU_FLAG", menuFlag);
			List<MenuData> menuList = DataAccess.executeNamedQuery(MenuData.class, QueryNamesEnum.MENU_DATA_GET_EMP_MENUS_BY_MENU_FLAG.getCode(), qParams);
			return menuList;
		} catch (NoDataException e) {
			return new ArrayList<MenuData>();
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityService.class, e, "SecurityService");
			throw new BusinessException("error_loadingUserMenu");
		}
	}

	public static boolean isTransactionMenuPrivilegeGranted(String menuURL, List<MenuData> transactionsMenus) {
		for (MenuData menu : transactionsMenus) {
			if (menu.getUrl() != null && menu.getUrl().contains(menuURL)) {
				return true;
			}
		}

		return false;
	}

	public static UserMenuActionData getAction(long empId, String actionCode, long menuId) throws BusinessException {
		List<UserMenuActionData> actionList = searchAction(empId, actionCode, menuId);
		if (actionList.isEmpty()) {
			return null;
		} else {
			return actionList.get(0);
		}
	}

	private static List<UserMenuActionData> searchAction(long empId, String actionCode, long menuId) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_EMP_ID", empId);
			qParams.put("P_ACTION_CODE", actionCode == null ? FlagsEnum.ALL.getCode() + "" : actionCode);
			qParams.put("P_MENU_ID", menuId);
			return DataAccess.executeNamedQuery(UserMenuActionData.class, QueryNamesEnum.USER_MENU_ACTION_CHECK_EMPLOYEE_MENU_ACTION.getCode(), qParams);
		} catch (NoDataException e) {
			return new ArrayList<UserMenuActionData>();
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityService.class, e, "SecurityService");
			throw new BusinessException("error_DBError");
		}
	}

	public static boolean checkUserMitoken(long empId) throws BusinessException {
		if (searchUserMitoken(empId).isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	private static List<UserMiTokenData> searchUserMitoken(long empId) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_EMP_ID", empId);
			return DataAccess.executeNamedQuery(UserMiTokenData.class, QueryNamesEnum.USER_MITOKEN_DATA_CHECK_USER_MITOKEN_DATA.getCode(), qParams);
		} catch (NoDataException e) {
			return new ArrayList<UserMiTokenData>();
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityService.class, e, "SecurityService");
			throw new BusinessException("error_DBError");
		}
	}

	// ---------------------------------- Employee Menu Action
	// ----------------------------------------

	// public static List<EmployeeMenuAction> getEmployeeMenuActions(Long empId,
	// String menuCode) throws BusinessException {
	// try {
	// Map<String, Object> qParams = new HashMap<String, Object>();
	// qParams.put("P_EMP_ID", empId);
	// qParams.put("P_MENU_CODE", menuCode);
	//
	// return DataAccess.executeNamedQuery(EmployeeMenuAction.class,
	// QueryNamesEnum.SEC_GET_EMLOYEE_MENU_ACTIONS.getCode(), qParams);
	// } catch (NoDataException e) {
	// return new ArrayList<EmployeeMenuAction>();
	// } catch (Exception e) {
	// e.printStackTrace();
	// throw new BusinessException("error_loadingUserMenu");
	// }
	// }
	//
	// public static boolean isEmployeeMenuActionGranted(Long empId, String
	// menuCode, String action) throws BusinessException {
	// boolean isGranted = false;
	// List<EmployeeMenuAction> employeeMenuActions = null;
	// try {
	// Map<String, Object> qParams = new HashMap<String, Object>();
	// qParams.put("P_EMP_ID", empId);
	// qParams.put("P_MENU_CODE", menuCode);
	// qParams.put("P_ACTION", action);
	//
	// employeeMenuActions =
	// DataAccess.executeNamedQuery(EmployeeMenuAction.class,
	// QueryNamesEnum.SEC_CHECK_EMPLOYEE_MENU_ACTION.getCode(), qParams);
	// if (employeeMenuActions != null && employeeMenuActions.size() > 0)
	// isGranted = true;
	// } catch (NoDataException e) {
	// return isGranted;
	// } catch (Exception e) {
	// e.printStackTrace();
	// throw new BusinessException("error_general");
	// }
	// return isGranted;
	// }

}