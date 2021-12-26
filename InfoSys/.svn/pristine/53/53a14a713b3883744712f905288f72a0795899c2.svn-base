package com.code.ui.backings.security;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import com.code.dal.orm.security.MenuData;
import com.code.enums.FlagsEnum;
import com.code.enums.SessionAttributesEnum;
import com.code.services.log4j.Log4j;
import com.code.ui.backings.base.BaseBacking;

@ManagedBean(name = "menuTree")
@SessionScoped
public class MenuTree extends BaseBacking {
	private List<MenuData> currentTree;
	private List<MenuData> transactions, workFlow, reports;
	private Map<Long, DefaultSubMenu> menuMap;
	private MenuModel menuModel;
	private MenuModel workFlowMenuModel;
	private MenuModel reportMenuModel;
	private Integer rootOpened, subParentOpened;
	private int activeIndex;

	public MenuTree() {
		super();
	}

	@SuppressWarnings("unchecked")
	public void reInitMenuModel() {
		if (menuModel == null || workFlowMenuModel == null) {
			transactions = (List<MenuData>) getSession().getAttribute(SessionAttributesEnum.USER_MENU_TREE.getCode());
			reports = (List<MenuData>) getSession().getAttribute(SessionAttributesEnum.USER_MENU_REPORT_TREE.getCode());
			workFlow = (List<MenuData>) getSession().getAttribute(SessionAttributesEnum.USER_MENU_WORKFLOW_TREE.getCode());

			menuModel = new DefaultMenuModel();
			menuMap = new LinkedHashMap<Long, DefaultSubMenu>();
			if (transactions != null)
				adjustMenuModel(transactions, menuModel);

			workFlowMenuModel = new DefaultMenuModel();
			menuMap = new LinkedHashMap<Long, DefaultSubMenu>();
			if (workFlow != null)
				adjustMenuModel(workFlow, workFlowMenuModel);
			
			reportMenuModel = new DefaultMenuModel();
			menuMap = new LinkedHashMap<Long, DefaultSubMenu>();
			if (reports != null)
				adjustMenuModel(reports, reportMenuModel);

			activeIndex = 0;
		} else {
			return;
		}

	}

	private void adjustMenuModel(List<MenuData> menus, MenuModel menuModel) {
		String contextPath = getRequest().getContextPath();
		String name;
		try {
			for (MenuData menu : menus) {
				if (menu.getShowInMenu() != null && menu.getShowInMenu().equals(FlagsEnum.OFF.getCode()))
					continue;

				try {
					name = getParameterizedMessage(menu.getNameKey());
				} catch (Exception e) {
					name = menu.getNameKey();
				}

				if (menu.getParentId().longValue() == 0) { // Parent Menus
					DefaultSubMenu subMenu = new DefaultSubMenu(name);
					if (menuMap.containsKey(menu.getMenuId())) {
						subMenu = menuMap.get(menu.getMenuId());
						subMenu.setLabel(name);
					} else {
						menuMap.put(menu.getMenuId(), subMenu);
					}
					menuModel.addElement(subMenu);
				} else if (menu.getParentId().longValue() != 0 && !menu.getLeaf()) { //TODO // Sub Menus 
					DefaultSubMenu subMenu = new DefaultSubMenu(name);
					subMenu.setStyleClass("subMenuClass");
					DefaultSubMenu parentMenu;
					if (menuMap.containsKey(menu.getMenuId())) {
						subMenu = menuMap.get(menu.getMenuId());
						subMenu.setLabel(name);
					} else {
						menuMap.put(menu.getMenuId(), subMenu);
					}
					if (menuMap.containsKey(menu.getParentId())) {
						parentMenu = menuMap.get(menu.getParentId());
					} else {
						parentMenu = new DefaultSubMenu();
						menuMap.put(menu.getParentId(), parentMenu);
					}
					parentMenu.addElement(subMenu);
					parentMenu.setStyleClass("subMenuClass2");
				} else { // Menu Items
					DefaultSubMenu subMenu;
					if (menuMap.containsKey(menu.getParentId())) {
						subMenu = menuMap.get(menu.getParentId());
					} else {
						subMenu = new DefaultSubMenu();
						menuMap.put(menu.getParentId(), subMenu);
					}
					DefaultMenuItem item = new DefaultMenuItem(name);
					item.setUrl(menu.getUrl().startsWith(contextPath) ? menu.getUrl().replace(contextPath, "") : menu.getUrl());
					subMenu.addElement(item);
				}
			}

			if (menuModel.getElements().size() == 1) {
				menuMap.values().iterator().next().setExpanded(true);
			}
		} catch (Exception e) {
			Log4j.traceErrorException(MenuTree.class, e, "MenuTree");
		}
	}

	// @SuppressWarnings("unchecked")
	// public void init() {
	// super.init();
	//
	// HttpServletRequest req = getRequest();
	// if (req.getParameter("menuType") != null) {
	// int menuType = Integer.parseInt(req.getParameter("menuType"));
	// if (menuType == 1) {
	// currentTree = transactions;
	// } else if (menuType == 2) {
	// if (workFlow == null) {
	// workFlow = (List<MenuData>) req.getSession().getAttribute(SessionAttributesEnum.USER_MENU_WORKFLOW_TREE.getCode());
	// workFlow = adjustMenuTree(workFlow);
	// }
	// currentTree = workFlow;
	// } else if (menuType == 3) {
	// if (reports == null) {
	// reports = (List<MenuData>) req.getSession().getAttribute(SessionAttributesEnum.USER_MENU_REPORT_TREE.getCode());
	// reports = adjustMenuTree(reports);
	// }
	// currentTree = reports;
	// }
	// }
	//
	// if (req.getParameter("rootOpened") != null) {
	// rootOpened = Integer.parseInt(req.getParameter("rootOpened"));
	// } else {
	// rootOpened = 0;
	// }
	//
	// if (req.getParameter("subParentOpened") != null) {
	// subParentOpened = Integer.parseInt(req.getParameter("subParentOpened"));
	// } else {
	// subParentOpened = 0;
	// }
	// }

	// private List<MenuData> adjustMenuTree(List<MenuData> menus) {
	// List<MenuData> menuTree = new ArrayList<MenuData>();
	//
	// Map<Long, MenuData> parents = new HashMap<Long, MenuData>();
	//
	// for (MenuData m : menus) {
	// if (m.getMenuFlag().equals(0))
	// continue;
	//
	// if (!parents.containsKey(m.getParentId())) { // Root Node
	// m.setSubMenus(new ArrayList<MenuData>());
	// menuTree.add(m);
	// parents.put(m.getMenuId(), m);
	// } else if (parents.containsKey(m.getParentId()) && m.getUrl() == null) { // Parent
	// // Node
	// // under
	// // the
	// // Root
	// // Node
	// m.setSubMenus(new ArrayList<MenuData>());
	// MenuData p = parents.get(m.getParentId());
	// p.getSubMenus().add(m);
	// parents.put(m.getMenuId(), m);
	// } else if (parents.containsKey(m.getParentId()) && m.getUrl() != null) { // Link
	// // Node
	// MenuData p = parents.get(m.getParentId());
	// String url = m.getUrl() + (m.getUrl().contains("?") ? "&" : "?");
	//
	// if (!parents.containsKey(p.getParentId())) { // Link under Root
	// // Node
	// url += "rootOpened=" + p.getMenuId();
	// } else { // Link under sub parent
	// MenuData r = parents.get(p.getParentId());
	// url += "rootOpened=" + r.getMenuId() + "&subParentOpened=" + p.getMenuId();
	// }
	//
	// m.setUrl(url);
	// p.getSubMenus().add(m);
	// }
	// }
	//
	// return menuTree;
	// }

	public boolean checkCurrentURL(String url) {
		HttpServletRequest req = getRequest();
		String testUrl = req.getContextPath() + url;
		if (testUrl.contains(req.getRequestURI())) {
			if (testUrl.contains("?")) {
				String[] testParams = testUrl.substring(testUrl.indexOf("?") + 1).split("&");
				for (int i = 0; i < testParams.length; i++) {
					String[] testParamNameValue = testParams[i].split("=");
					if (!(req.getParameter(testParamNameValue[0]) != null && req.getParameter(testParamNameValue[0]).equals(testParamNameValue[1])))
						return false;
				}
			}
			return true;
		}

		return false;
	}
	
	public void onTabChange(TabChangeEvent event) {
		if (event.getTab().getId().equals("mainMenuId")) {
			activeIndex = 0;
		} else if (event.getTab().getId().equals("workFlowMenuId")) {
			activeIndex = 1;
		}else if(event.getTab().getId().equals("reportMenuId")){
			activeIndex = 2;
		}
	}

	public void setCurrentTree(List<MenuData> currentTree) {
		this.currentTree = currentTree;
	}

	public List<MenuData> getCurrentTree() {
		return currentTree;
	}

	public Integer getRootOpened() {
		return rootOpened;
	}

	public void setRootOpened(Integer rootOpened) {
		this.rootOpened = rootOpened;
	}

	public Integer getSubParentOpened() {
		return subParentOpened;
	}

	public void setSubParentOpened(Integer subParentOpened) {
		this.subParentOpened = subParentOpened;
	}

	public MenuModel getMenuModel() {
		return menuModel;
	}

	public void setMenuModel(MenuModel menuModel) {
		this.menuModel = menuModel;
	}

	public MenuModel getWorkFlowMenuModel() {
		return workFlowMenuModel;
	}

	public void setWorkFlowMenuModel(MenuModel workFlowMenuModel) {
		this.workFlowMenuModel = workFlowMenuModel;
	}

	public MenuModel getReportMenuModel() {
		return reportMenuModel;
	}

	public void setReportMenuModel(MenuModel reportMenuModel) {
		this.reportMenuModel = reportMenuModel;
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}
	
	
}