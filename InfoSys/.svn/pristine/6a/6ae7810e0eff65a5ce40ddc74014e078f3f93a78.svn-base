package com.code.dal.orm.security;

import java.io.Serializable;

import com.code.dal.orm.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "SEC_VW_EMPS_MENUS_DATA")
@IdClass(EmployeeMenuId.class)
public class EmployeeMenuData extends BaseEntity implements Serializable {
	private Long empId;
	private Long menuId;

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

	@Override
	public void setId(Long id) {
	}
}