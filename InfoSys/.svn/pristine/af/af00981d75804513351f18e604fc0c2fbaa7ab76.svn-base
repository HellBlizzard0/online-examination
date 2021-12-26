package com.code.dal.orm.setup;

import java.io.Serializable;
import java.util.Date;

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
		 @NamedQuery(name  = "employeeData_searchEmployee", 
	              	 query = " select e" +
	              			 " from EmployeeData e" +
	              			 " where (:P_ID = -1 or e.empId = :P_ID )" +
	              			 " and (:P_EMAIL = '-1' or lower(e.email) = lower(:P_EMAIL) )" +
                         	 " and (:P_FULL_NAME = '-1' or e.fullName like :P_FULL_NAME )" +
                         	 " and (:P_SOCIAL_ID = '-1' or e.socialID = :P_SOCIAL_ID )" +
                         	 " and (:P_MILITARY_NUMBER = '-1' or e.militaryNo = :P_MILITARY_NUMBER )" +
                         	 /*" and (:P_OPTIMIZE = -1 or rownum <= 500)" +*/ 
                         	 " and (:P_ACTUAL_DEP_LIST_SIZE = 0 or e.actualDepartmentId in (:P_ACTUAL_DEP_LIST))" +
                         	 " order by e.empId "
         ),
         @NamedQuery(name  = "employeeData_searchInfoRelatedEmployees", 
			      	 query = " select e " +
			      			 " from EmployeeData e, InfoRelatedEntity infoRel" +
			      			 " where ( e.empId = infoRel.employeeId)" +
			      			 " and (:P_INFO_ID = '-1' or infoRel.infoId = :P_INFO_ID )" +
			             	 " order by e.empId "
         ),
         @NamedQuery(name  = "employeeData_searchEmployeeByRegionId", 
			      	 query = " select e" +
			      			 " from EmployeeData e, DepartmentData dep" +
			      			 " where (:P_ID = -1 or e.empId = :P_ID )" +
			      			 " and (:P_EMAIL = '-1' or e.email = :P_EMAIL )" +
			             	 " and (:P_FULL_NAME = '-1' or e.fullName like :P_FULL_NAME )" +
			             	 " and (:P_SOCIAL_ID = '-1' or e.socialID = :P_SOCIAL_ID )" +
			             	 " and (:P_MILITARY_NUMBER = '-1' or e.militaryNo = :P_MILITARY_NUMBER )" +
			             	 " and (:P_REGION_ID = -1 or dep.regionId = :P_REGION_ID ) " +
			             	 /*" and (:P_OPTIMIZE = -1 or rownum <= 100)" +*/
			             	 " and e.actualDepartmentId = dep.id " +
			             	 " order by e.empId "
	 	),
	 	@NamedQuery(name  = "employeeData_searchEmployeesHavingAssignment", 
			     	query = " select distinct e" +
				     		" from EmployeeData e, AssignmentDetail aD" +
				     		" where (:P_ID = -1 or e.empId = :P_ID)" +
				     		" and (:P_EMAIL = '-1' or e.email = :P_EMAIL )" +
				           	" and (:P_FULL_NAME = '-1' or e.fullName like :P_FULL_NAME )" +
				           	" and (:P_SOCIAL_ID = '-1' or e.socialID = :P_SOCIAL_ID )" +
				           	" and (:P_MILITARY_NUMBER = '-1' or e.militaryNo = :P_MILITARY_NUMBER )" +
				           	/*" and (:P_OPTIMIZE = -1 or rownum <= 500)" +*/ 
				           	" and (:P_ACTUAL_DEP_LIST_SIZE = 0 or e.actualDepartmentId in (:P_ACTUAL_DEP_LIST))" +
				           	" and e.id = aD.employeeId" +
				           	" order by e.empId "
		),
	 	@NamedQuery(name  = "employeeData_searchEmployeesByRegionHavingAssignment", 
	     	 		query = " select distinct e" +
			     			" from EmployeeData e, DepartmentData dep, AssignmentDetail aD" +
			     			" where (:P_ID = -1 or e.empId = :P_ID )" +
			     			" and (:P_EMAIL = '-1' or e.email = :P_EMAIL )" +
			            	" and (:P_FULL_NAME = '-1' or e.fullName like :P_FULL_NAME )" +
			            	" and (:P_SOCIAL_ID = '-1' or e.socialID = :P_SOCIAL_ID )" +
			            	" and (:P_MILITARY_NUMBER = '-1' or e.militaryNo = :P_MILITARY_NUMBER )" +
			            	" and (:P_REGION_ID = -1 or dep.regionId = :P_REGION_ID ) " +
			            	/*" and (:P_OPTIMIZE = -1 or rownum <= 100)" +*/
			            	" and e.actualDepartmentId = dep.id " +
			            	" and e.id = aD.employeeId" +
			            	" order by e.empId "
		),
	 	@NamedQuery(name  = "employeeData_searchEmployeesByEmployeeIdList", 
     	 query = " select e" +
     			 " from EmployeeData e" +
     			 " where (:P_ID = -1 or e.empId = :P_ID )" +
     			 " and (:P_EMAIL = '-1' or e.email = :P_EMAIL )" +
            	 " and (:P_FULL_NAME = '-1' or e.fullName like :P_FULL_NAME )" +
            	 " and (:P_SOCIAL_ID = '-1' or e.socialID = :P_SOCIAL_ID )" +
            	 " and (:P_MILITARY_NUMBER = '-1' or e.militaryNo = :P_MILITARY_NUMBER )" +
            	 /*" and (:P_OPTIMIZE = -1 or rownum <= 500)" + */
            	 " and (:P_EMPLOYEE_ID_LIST_SIZE = 0 or e.empId in (:P_EMPLOYEE_ID_LIST))" +
            	 " order by e.empId "
	    ),
})

@SuppressWarnings("serial")
@Entity
@Table(name = "STP_VW_EMPLOYEES")
public class EmployeeData extends BaseEntity implements Serializable{
	private Long empId;
	private String fullName;
	private String firstName;
	private String lastName;
	private String mobileNumber;
	private String email;
	private String socialID;
	private String gender;
	private String militaryNo;
	private String rank;
	private Long actualDepartmentId; 
	private String actualDepartmentName;
	private Long actualTitleId;
	private Date birthDate;
	private String birthDateString;
	private String actualTitleDescription;
	private Long nationalityId;

	@Id
	@Column(name = "ID")
	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Basic
	@Column(name = "FULL_NAME")
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Basic
	@Column(name = "FIRST_NAME")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Basic
	@Column(name = "LAST_NAME")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Basic
	@Column(name = "MOBILE_NO")
	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	@Basic
	@Column(name = "E_MAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Basic
	@Column(name = "SOCIAL_ID")
	public String getSocialID() {
		return socialID;
	}

	public void setSocialID(String socialID) {
		this.socialID = socialID;
	}

	@Basic
	@Column(name = "GENDER")
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Basic
	@Column(name = "MILITARY_NO")
	public String getMilitaryNo() {
		return militaryNo;
	}

	public void setMilitaryNo(String militaryNo) {
		this.militaryNo = militaryNo;
	}
	
	@Basic
	@Column(name = "RANK")
	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	@Basic
	@Column(name = "ACTUAL_DEPT_ID")
	public Long getActualDepartmentId() {
		return actualDepartmentId;
	}

	public void setActualDepartmentId(Long actualDepartmentId) {
		this.actualDepartmentId = actualDepartmentId;
	}

	@Basic
	@Column(name = "ACTUAL_DEPARTMENT_NAME")
	public String getActualDepartmentName() {
		return actualDepartmentName;
	}

	public void setActualDepartmentName(String actualDepartmentName) {
		this.actualDepartmentName = actualDepartmentName;
	}

	@Basic
	@Column(name = "ACTUAL_TITLE_ID")
	public Long getActualTitleId() {
		return actualTitleId;
	}

	public void setActualTitleId(Long actualTitleId) {
		this.actualTitleId = actualTitleId;
	}

	@Basic
	@Column(name = "BIRTH_DATE")
	public Date getBirthDate() {
		return birthDate;
	}
	
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	@Basic
	@Column(name = "ACTUAL_TITLE_DESCRIPTION")
	public String getActualTitleDescription() {
		return actualTitleDescription;
	}

	public void setActualTitleDescription(String actualTitleDescription) {
		this.actualTitleDescription = actualTitleDescription;
	}
	
	@Basic
	@Column(name = "NATIONALITY_ID")
	public Long getNationalityId() {
		return nationalityId;
	}

	public void setNationalityId(Long nationalityId) {
		this.nationalityId = nationalityId;
	}

	@Transient
	public String getBirthDateString() {
		return birthDateString;
	}

	@Override
	public void setId(Long id) {
	}

	@Override
	public boolean equals(Object employeeData) {
		if (employeeData == null || !(employeeData instanceof EmployeeData))
			return false;
		if (employeeData == this || ((EmployeeData) employeeData).getEmpId().longValue() == this.getEmpId().longValue())
			return true;
		return false;
	}

	@Override
	public int hashCode() {
		return this.empId.hashCode();
	}
	
}