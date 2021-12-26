
package com.code.dal.orm.setup;


import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.code.dal.orm.BaseEntity;

@NamedQueries({
	 @NamedQuery(name  = "departmentData_searchDepartment", 
             	 query = " select d " +
             	 		 " from DepartmentData d" +
             			 " where (:P_ID = -1 or d.id = :P_ID )" +
             			 " and (:P_DEPARTMENT_TYPE = -1 or d.departmentTypeId = :P_DEPARTMENT_TYPE )" +
             			"  and (:P_REGION_ID = -1 or d.regionId = :P_REGION_ID )" +
             			 " and (:P_CODE = '-1' or d.code = :P_CODE )" +
                    	 " and (:P_ARABIC_NAME = '-1' or d.arabicName like :P_ARABIC_NAME )" +
                    	 " and (:P_DEPT_TYPES_LIST_SIZE = 0 or d.departmentTypeId IN (:P_DEPT_TYPES_LIST)) "+
                    	 /*" and (:P_OPTIMIZE = -1 or rownum <= 100)" +*/ 
                    	 " order by d.arabicName "
    ),
    @NamedQuery(name  = "departmentData_searchInfoRelatedDepartments", 
			    query = " select d " +
					    " from DepartmentData d, InfoRelatedEntity infoRel" +
					    " where (d.id = infoRel.departmentId)" +
			 			 " and (:P_INFO_ID = -1 or infoRel.infoId = :P_INFO_ID )" +
			        	 " order by d.id "
    ),
    @NamedQuery(name  = "departmentData_searchDepartmentByMultipleIds", 
			    query = " select d " +
				 		 " from DepartmentData d" +
						 " where (:P_ID_LIST_SIZE = 0 or d.id IN (:P_ID_LIST)) "+
						 " order by d.arabicName "
    ),
    @NamedQuery(name  = "departmentData_searchDepartmentByMultipleIdsAndNameAndCode", 
			    query = " select d " +
				 		 " from DepartmentData d" +
						 " where (:P_ID_LIST_SIZE = 0 or d.id IN (:P_ID_LIST)) "+
						 " and (:P_CODE = '-1' or d.code = :P_CODE )" +
			        	 " and (:P_ARABIC_NAME = '-1' or d.arabicName like :P_ARABIC_NAME )" +
						 " order by d.arabicName "
    ),
    @NamedQuery(name  = "departmentData_searchAssignmentDepartments", 
			    query = " select d " +
					    " from DepartmentData d, AssignmentAgentCode a " +
					    " where d.id = a.departmentId " +
					    " and (:P_REGION_ID = -1 or d.regionId = :P_REGION_ID or d.id = :P_REGION_ID) " +
			        	" order by d.id "
			
    )    
})

@SuppressWarnings("serial")
@Entity
@Table(name = "STP_VW_DEPARTMENTS")
public class DepartmentData extends BaseEntity implements Serializable {
	private Long id;
	private String code;
	private String arabicName;
	private String latinName;
	private Integer deptLevel;
	private Long departmentTypeId;
	private Long regionId;

	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Basic
	@Column(name = "ARABIC_NAME")
	public String getArabicName() {
		return arabicName;
	}

	public void setArabicName(String arabicName) {
		this.arabicName = arabicName;
	}

	@Basic
	@Column(name = "LATIN_NAME")
	public String getLatinName() {
		return latinName;
	}

	public void setLatinName(String latinName) {
		this.latinName = latinName;
	}

	@Basic
	@Column(name = "DEPT_LEVEL")
	public Integer getDeptLevel() {
		return deptLevel;
	}

	public void setDeptLevel(Integer deptLevel) {
		this.deptLevel = deptLevel;
	}

	@Basic
	@Column(name = "DEPARTMENT_TYPEID")
	public Long getDepartmentTypeId() {
		return departmentTypeId;
	}

	public void setDepartmentTypeId(Long departmentTypeId) {
		this.departmentTypeId = departmentTypeId;
	}

	@Basic
	@Column(name = "REGIONID")
	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}
}
