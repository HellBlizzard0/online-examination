package com.code.dal.orm.security;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.code.dal.orm.BaseEntity;

@NamedQueries({
	  @NamedQuery(name = "menuData_getEmployeeMenus", 
	              query= " select m from MenuData m, EmployeeMenuData em " +
            		     " where m.menuId = em.menuId " +
                         " and em.empId = :P_EMP_ID " +
                         " and m.activeFlag = 1 " +
                         " and (:P_MENU_FLAG_ALL = -1 or m.menuFlag in (:P_MENU_FLAG) ) " + 
                         " and (:P_NAME_KEY = '-1' or m.nameKey = :P_NAME_KEY) " + 
                         " order by m.orderBy "
      ),
      @NamedQuery(name = "menuData_getEmployeeMenusByMenuFlag", 
			      query= " select m from MenuData m" +
					     " where ( :P_MENU_FLAG = -1 or m.menuFlag = :P_MENU_FLAG )" +
			             " and m.activeFlag = 1 " + 
			             " order by m.orderBy "
	  )
})

@SuppressWarnings("serial")
@Entity
@Table(name = "SEC_VW_MENUS_DATA")
public class MenuData extends BaseEntity implements Serializable {
	private Long menuId;
	private String nameKey;
	private Long parentId;
	private Integer orderBy;
	private String url;
	private Integer activeFlag;
	private Integer menuFlag;
	private List<MenuData> subMenus;
	private List<UserMenuActionData> menuAction;
	private Integer showInMenu;
	private Boolean leaf;

	@Id
	@Column(name = "ID")
	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	@Basic
	@Column(name = "NAME_KEY")
	public String getNameKey() {
		return nameKey;
	}

	public void setNameKey(String nameKey) {
		this.nameKey = nameKey;
	}

	@Basic
	@Column(name = "PARENT_ID")
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Basic
	@Column(name = "ORDER_BY")
	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}

	@Basic
	@Column(name = "URL")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Basic
	@Column(name = "ACTIVE_FLAG")
	public Integer getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}

	@Basic
	@Column(name = "MENU_FLAG")
	public Integer getMenuFlag() {
		return menuFlag;
	}

	public void setMenuFlag(Integer menuFlag) {
		this.menuFlag = menuFlag;
	}
	
	@Basic
	@Column(name = "SHOW_IN_MENU")
	public Integer getShowInMenu() {
		return showInMenu;
	}

	public void setShowInMenu(Integer showInMenu) {
		this.showInMenu = showInMenu;
	}
	
	@Basic
	@Column(name = "LEAF_FLAG")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getLeaf() {
		return leaf;
	}

	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}

	@Transient
	public List<MenuData> getSubMenus() {
		return subMenus;
	}

	public void setSubMenus(List<MenuData> subMenus) {
		this.subMenus = subMenus;
	}

	@Transient
	public List<UserMenuActionData> getMenuAction() {
		return menuAction;
	}

	public void setMenuAction(List<UserMenuActionData> menuAction) {
		this.menuAction = menuAction;
	}

	@Override
	public void setId(Long id) {
	}
}