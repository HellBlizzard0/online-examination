package com.code.dal.orm.security;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserMenuActionDataId implements Serializable {
	private Long empId;
	private Long menuId;
	private Long actionId;

	public UserMenuActionDataId() {
	}
	
	public UserMenuActionDataId(Long empId, Long menuId, Long actionId) {
		this.empId = empId;
		this.menuId = menuId;
		this.actionId = actionId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public Long getEmpId() {
		return empId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public Long getMenuId() {
		return menuId;
	}

	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}

	public Long getActionId() {
		return actionId;
	}

	public boolean equals(Object o) {
		return ((o instanceof UserMenuActionDataId) && (empId.equals(((UserMenuActionDataId) o).getEmpId())) && (menuId.equals(((UserMenuActionDataId) o).getMenuId())) && (actionId.equals(((UserMenuActionDataId) o).getActionId())));
	}

	public int hashCode() {
		return empId.hashCode() + menuId.hashCode() + actionId.hashCode();
	}
}