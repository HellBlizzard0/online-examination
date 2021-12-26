package com.code.dal.orm.security;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.code.dal.orm.BaseEntity;

@NamedQueries({
	   @NamedQuery(name  = "userMiTokenData_checkUserMiTokenData", 
			   	   query = " select u " +
			   			   " from UserMiTokenData u " +
			   			   " where u.empId = :P_EMP_ID "
	   ),
})

@SuppressWarnings("serial")
@Entity
@Table(name = "SEC_VW_USERS_MI_TOKEN")
public class UserMiTokenData extends BaseEntity implements Serializable {
	private Long empId;
	private String empName;

	@Override
	public void setId(Long id) {
	}

	@Id
	@Column(name = "EMP_ID")
	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Id
	@Column(name = "EMP_NAME")
	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}
}
