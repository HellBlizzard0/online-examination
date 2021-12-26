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

import com.code.dal.audit.DeleteableAuditEntity;
import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;

@SuppressWarnings("serial")
@NamedQueries({
	  @NamedQuery(name = "wf_position_getWFPosition", 
	              query= " select p from WFPosition p " +
                         " where (:P_POSITION_ID =-1 or p.positionId = :P_POSITION_ID) "  +
                         " and (:P_UNIT_ID =-1 or p.unitId = :P_UNIT_ID) "  
          ),
      @NamedQuery(name = "wf_position_getWFPositionByDescription", 
		          query= " select p from WFPosition p " +
		        		 " where :P_DESCRIPTION = '-1'  or p.positionDesc like :P_DESCRIPTION "                            
          )
})

@Entity
@Table(name = "FIS_WF_POSITIONS")
public class WFPosition extends AuditEntity implements Serializable, InsertableAuditEntity, DeleteableAuditEntity, UpdateableAuditEntity {
	private Long positionId;
	private String positionDesc;
	private Long unitId;
	private Long empId;
	private String empsGroup;
	private Integer discriminator;
	private Long employeeTitleId;
	private Long employeeDepId;

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    @Id
    @Column(name="ID")
    public Long getPositionId() {
        return positionId;
    }

    public void setPositionDesc(String positionDesc) {
        this.positionDesc = positionDesc;
    }

    @Basic
    @Column(name="POSITION_DESC")
    public String getPositionDesc() {
        return positionDesc;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    @Basic
    @Column(name="UNIT_ID")
    public Long getUnitId() {
        return unitId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    @Basic
    @Column(name="EMP_ID")
    public Long getEmpId() {
        return empId;
    }

    public void setEmpsGroup(String empsGroup) {
        this.empsGroup = empsGroup;
    }

    @Basic
    @Column(name="EMPS_GROUP")
    public String getEmpsGroup() {
        return empsGroup;
    }
    
    @Basic
    @Column(name="DISCRIMINATOR")
	public Integer getDiscriminator() {
		return discriminator;
	}

	public void setDiscriminator(Integer discriminator) {
		this.discriminator = discriminator;
	}
	
	@Basic
	@Column(name = "EMPLOYEE_TITLE_ID")
	public Long getEmployeeTitleId() {
		return employeeTitleId;
	}

	public void setEmployeeTitleId(Long employeeTitleId) {
		this.employeeTitleId = employeeTitleId;
	}

	@Basic
	@Column(name = "EMPLOYEE_DEPT_ID")
	public Long getEmployeeDepId() {
		return employeeDepId;
	}

	public void setEmployeeDepId(Long employeeDepId) {
		this.employeeDepId = employeeDepId;
	}
	
	@Override
	public Long calculateContentId() {
		return this.positionId;
	}

	@Override
	public void setId(Long id) {
		this.positionId = id;
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
