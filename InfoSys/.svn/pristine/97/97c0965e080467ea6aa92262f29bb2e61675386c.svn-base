package com.code.dal.orm.workflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.code.dal.orm.BaseEntity;

@NamedQueries({
	  @NamedQuery(name = "wfPositionData_getWFPosition", 
	              query= " select p from WFPositionData p "    +
	              		 " where (:P_POSITION_ID  =-1 or p.positionId = :P_POSITION_ID)"  +
	              		 " and (:P_POSITION_DESC = '-1' or p.positionDesc like :P_POSITION_DESC)" +
	              		 " order by p.positionId"
      ),
      @NamedQuery(name = "wfPositionData_getWFPositionForTransferedEmployees", 
			      query= " select p from WFPositionData p "    +
			      		 " where p.posEmployeeTitleId is not null " +
			      		 " and p.actualEmployeeTitleId is not null " +
			      		 " and p.posEmployeeDepId is not null " +
			      		 " and p.actualEmployeeDepId is not null" +
			      		 " and (p.posEmployeeTitleId != p.actualEmployeeTitleId or p.posEmployeeDepId != p.actualEmployeeDepId)"  +
			    		 " and p.discriminator = :P_DISC" +
			      		 " order by p.positionId"
	  )
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_WF_POSITIONS")
public class WFPositionData extends BaseEntity implements Serializable {
	private Long positionId;
	private String positionDesc;
	private Long unitId;
	private String unitName;
	private Long empId;
	private String empName;
	private String empsGroup;
	private Integer discriminator;
	private Long posEmployeeTitleId;
	private Long posEmployeeDepId;
	private Long actualEmployeeTitleId;
	private Long actualEmployeeDepId;
	private String posEmployeeEmail;
	private String posDepManagerName;
	private String posDepManagerEmail;

	private WFPosition wfPosition;
	
	public WFPositionData() {
		wfPosition = new WFPosition();
	}

	public void setPositionId(Long positionId) {
		this.wfPosition.setPositionId(positionId);
		this.positionId = positionId;
	}

	@Id
	@Column(name = "ID")
	public Long getPositionId() {
		return wfPosition.getPositionId();
	}

	public void setPositionDesc(String positionDesc) {
		this.wfPosition.setPositionDesc(positionDesc);
		this.positionDesc = positionDesc;
	}

	@Basic
	@Column(name = "POSITION_DESC")
	public String getPositionDesc() {
		return positionDesc;
	}

	public void setUnitId(Long unitId) {
		this.wfPosition.setUnitId(unitId);
		this.unitId = unitId;
	}

	@Basic
	@Column(name = "UNIT_ID")
	public Long getUnitId() {
		return unitId;
	}

	public void setEmpId(Long empId) {
		this.wfPosition.setEmpId(empId);
		this.empId = empId;
	}

	@Basic
	@Column(name = "EMP_ID")
	public Long getEmpId() {
		return empId;
	}

	public void setEmpsGroup(String empsGroup) {
		this.wfPosition.setEmpsGroup(empsGroup);
		this.empsGroup = empsGroup;
	}

	@Basic
	@Column(name = "EMPS_GROUP")
	public String getEmpsGroup() {
		return empsGroup;
	}

	@Basic
	@Column(name = "DISCRIMINATOR")
	public Integer getDiscriminator() {
		return discriminator;
	}

	public void setDiscriminator(Integer discriminator) {
		this.wfPosition.setDiscriminator(discriminator);
		this.discriminator = discriminator;
	}

	@Basic
	@Column(name = "UNIT_NAME")
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	@Basic
	@Column(name = "EMP_NAME")
	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}
	
	@Basic
	@Column(name = "POS_EMPLOYEE_TITLE_ID")
	public Long getPosEmployeeTitleId() {
		return posEmployeeTitleId;
	}

	public void setPosEmployeeTitleId(Long posEmployeeTitleId) {
		this.posEmployeeTitleId = posEmployeeTitleId;
		this.wfPosition.setEmployeeTitleId(posEmployeeTitleId);
	}

	@Basic
	@Column(name = "POS_EMPLOYEE_DEP_ID")
	public Long getPosEmployeeDepId() {
		return posEmployeeDepId;
	}

	public void setPosEmployeeDepId(Long posEmployeeDepId) {
		this.posEmployeeDepId = posEmployeeDepId;
		this.wfPosition.setEmployeeDepId(posEmployeeDepId);
	}

	@Basic
	@Column(name = "ACTUAL_EMPLOYEE_TITLE_ID")
	public Long getActualEmployeeTitleId() {
		return actualEmployeeTitleId;
	}

	public void setActualEmployeeTitleId(Long actualEmployeeTitleId) {
		this.actualEmployeeTitleId = actualEmployeeTitleId;
	}

	@Basic
	@Column(name = "ACTUAL_EMPLOYEE_DEP_ID")
	public Long getActualEmployeeDepId() {
		return actualEmployeeDepId;
	}

	public void setActualEmployeeDepId(Long actualEmployeeDepId) {
		this.actualEmployeeDepId = actualEmployeeDepId;
	}

	@Transient
	public WFPosition getWfPosition() {
		return wfPosition;
	}

	public void setWfPosition(WFPosition wfPosition) {
		this.wfPosition = wfPosition;
	}

	@Override
	public void setId(Long id) {
		this.wfPosition.setPositionId(id);
		this.positionId = id;
	}

	@Basic
	@Column(name = "EMP_EMAIL")
	public String getPosEmployeeEmail() {
		return posEmployeeEmail;
	}

	public void setPosEmployeeEmail(String posEmployeeEmail) {
		this.posEmployeeEmail = posEmployeeEmail;
	}
	
	@Transient
	public String getPosDepManagerName() {
		return posDepManagerName;
	}

	public void setPosDepManagerName(String posDepManagerName) {
		this.posDepManagerName = posDepManagerName;
	}

	@Transient
	public String getPosDepManagerEmail() {
		return posDepManagerEmail;
	}

	public void setPosDepManagerEmail(String posDepManagerEmail) {
		this.posDepManagerEmail = posDepManagerEmail;
	}
	
	@Transient
	public List<Long> getEmpsGroupList() {
		List<Long> idsList = new ArrayList<Long>();
		List<String> empIds = this.empsGroup != null && !this.empsGroup.isEmpty() ? Arrays.asList(this.empsGroup.split(",")) : new ArrayList<String>();
		for (String empId : empIds) {
			idsList.add(Long.parseLong(empId));
		}
		return idsList;
	}
}
