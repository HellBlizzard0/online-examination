package com.code.dal.orm.securitymission;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;


@NamedQueries({
	@NamedQuery(name  = "carPermit_searchCarPermit", 
        	 	query = " select p " +
            	 		 " from CarPermit p , EmployeeNonEmployeeCars emp " +
            			 " where p.employeeNonEmployeeCarsId = emp.id  "+
            	 		 " and (:P_PERMIT_NO = '-1' or p.permitNo = :P_PERMIT_NO )" +
            	 		 " and (:P_PLATE_NUMBER_FLAG = 1 or (emp.plateNumber = :P_PLATE_NUMBER and emp.plateChar1 = :P_PLATE_CHAR_1 and emp.plateChar2 = :P_PLATE_CHAR_2 and emp.plateChar3 = :P_PLATE_CHAR_3 )) " +
            	 		 " order by p.id desc"
			 	)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_CARS_PERMITS")
public class CarPermit extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity {
	private Long id;
	private String permitNo;
	private Long employeeNonEmployeeCarsId;
	private Long departmentId;
	private String remarks;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_CARS_PERMITS_SEQ", sequenceName = "FIS_CARS_PERMITS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_CARS_PERMITS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "PERMIT_NO")
	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}

	@Override
	public Long calculateContentId() {
		return id;
	}

	@Basic
	@Column(name = "EMPLOYEE_NON_EMPLOYEE_CARS_ID")
	public Long getEmployeeNonEmployeeCarsId() {
		return employeeNonEmployeeCarsId;
	}

	public void setEmployeeNonEmployeeCarsId(Long employeeNonEmployeeCarsId) {
		this.employeeNonEmployeeCarsId = employeeNonEmployeeCarsId;
	}

	@Basic
	@Column(name = "STP_VW_DEPARTMENTS_ID")
	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	@Basic
	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
