package com.code.dal.orm.security;

import java.io.Serializable;

import com.code.dal.orm.BaseEntity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@NamedQueries({
	   @NamedQuery(name  = "userMenuActionData_checkEmployeeMenuAction", 
			   	   query = " select uma " +
			   			   " from UserMenuActionData uma " +
			   			   " where (:P_EMP_ID = -1 or uma.empId = :P_EMP_ID) " +
			   			   " and (:P_ACTION_CODE = '-1' or uma.actionCode = :P_ACTION_CODE)" +
			   			   " and (:P_MENU_ID = -1 or uma.menuId = :P_MENU_ID)" +
			   			   " order by uma.id "
	   ),
})

@SuppressWarnings("serial")
@Entity
@Table(name = "SEC_VW_USERS_ACTIONS_DATA")
@IdClass(UserMenuActionDataId.class)
public class UserMenuActionData extends BaseEntity implements Serializable {
	private Long empId;
	private Long menuId;
	private Long actionId;
	private String actionCode;

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Id
	@Column(name = "EMP_ID")
	public Long getEmpId() {
		return empId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	@Id
	@Column(name = "MENU_ID")
	public Long getMenuId() {
		return menuId;
	}

	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}

	@Id
	@Column(name = "ACTION_ID")
	public Long getActionId() {
		return actionId;
	}

	@Basic
	@Column(name = "ACTION_CODE")
	public String getActionCode() {
		return actionCode;
	}

	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	@Override
	public void setId(Long id) {
	}
}