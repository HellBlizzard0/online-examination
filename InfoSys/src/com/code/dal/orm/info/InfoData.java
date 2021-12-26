package com.code.dal.orm.info;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.code.dal.orm.BaseEntity;
import com.code.services.util.HijriDateService;

@NamedQueries({
	 @NamedQuery(name  = "infoData_searchInfoWithRelatedEntitiesAndSources", 
			 	 query = " select distinct infoD " + 
	           	 		 " from InfoData infoD , InfoRelatedEntity infoR, InfoSource infoS , AssignmentDetailData assignmentDetail" +
	           			 " where ( (infoD.id in (select iv.infoId from InfoVisibleDepartment iv where :P_VISIBLE_DPT_LIST_SIZE = 0 or iv.departmentId in (:P_VISIBLE_DPT_LIST)) ) " +
	           			 " or  (infoD.id in (select iAl.infoId from InfoAnalysis iAl where iAl.departmentId in (:P_VISIBLE_DPT_LIST))) )" +
	           			 " and infoD.id = infoR.infoId " +
	           			 " and infoD.id = infoS.infoId " +
	           			 " and assignmentDetail.id = infoS.assignmentDetailId " +
						 " and (:P_ASSIGNMENT_DETAIL_ID = -1 or infoS.assignmentDetailId = :P_ASSIGNMENT_DETAIL_ID ) " +
						 " and (:P_OPEN_SOURCE_ID = -1 or infoS.openSourceId = :P_OPEN_SOURCE_ID ) " +
	           			 " and (:P_COUNTRY_ID = -1 or infoR.countryId = :P_COUNTRY_ID)" +
		                 " and (:P_DEPARTMENT_ID = -1 or infoR.departmentId = :P_DEPARTMENT_ID)" +
		                 " and (:P_DOMAIN_ID = -1 or infoR.domainId = :P_DOMAIN_ID)" +
		                 " and (:P_EMPLOYEE_ID = -1 or infoR.employeeId = :P_EMPLOYEE_ID)" +
		                 " and (:P_OFFICER_ID = -1 or assignmentDetail.officerId = :P_OFFICER_ID)" +
		                 " and (:P_IDENTITY = -1 or assignmentDetail.identity = :P_IDENTITY)" +
		                 " and (:P_NON_EMPLOYEE_ID = -1 or infoR.nonEmployeeId = :P_NON_EMPLOYEE_ID)" +
	           			 " and (:P_INFO_NUMBER = '-1' or infoD.infoNumber = :P_INFO_NUMBER )" +
	           			 " and (:P_INFO_DETAILS = '-1' or infoD.infoDetails like :P_INFO_DETAILS )" +
	           			 " and (:P_INFO_SUBJECT_ID = -1 or infoD.infoSubjectId = :P_INFO_SUBJECT_ID )" +
	           			 " and (:P_INFO_TYPE_ID = -1 or infoD.infoTypeId = :P_INFO_TYPE_ID )" +
	           			 " and (:P_DOMAIN_INCOMING_SIDE_ID = -1 or infoD.domainIncomingSideId = :P_DOMAIN_INCOMING_SIDE_ID )" +
	           			 " and (:P_FROM_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_FROM_DATE, 'MI/MM/YYYY') <= infoD.receiveDate)" + 
	           			 " and (:P_TO_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_TO_DATE, 'MI/MM/YYYY') >= infoD.receiveDate) " +
	           			 " and (:P_INSTANCE_ID = -1 or infoD.wFInstanceId = :P_INSTANCE_ID )" +
	           			 " and (:P_ID = -1 or infoD.id = :P_ID )" +
	           			 " and (:P_REGION_ID = -1 or infoD.regionId = :P_REGION_ID )" +
	           			 " and (:P_SECTOR_ID = -1 or infoD.sectorId = :P_SECTOR_ID )" +
	           			 " and (:P_UNIT_ID = -1 or infoD.unitId = :P_UNIT_ID )" +
	           			 " and (:P_PHONE_NUMBER = '-1' or infoD.id in " +
	           			 "(select phone.infoId from InfoPhone phone where phone.phoneNumber = :P_PHONE_NUMBER)) " +
	           			 " and (:P_INCOMING_FILE_NUMBER = '-1' or infoD.incomingFileNumber like :P_INCOMING_FILE_NUMBER )" +
	           			 " and (:P_INCOMING_FILE_YEAR = -1 or infoD.incomingFileYear = :P_INCOMING_FILE_YEAR )" +
	           			 " and (:P_INCOMING_NUMBER = '-1' or infoD.incomingNumber like :P_INCOMING_NUMBER )" +
	           			 " and (:P_INCOMING_FROM_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_INCOMING_FROM_DATE, 'MI/MM/YYYY') <= infoD.incomingDate)" + 
	           			 " and (:P_INCOMING_TO_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_INCOMING_TO_DATE, 'MI/MM/YYYY') >= infoD.incomingDate) " +
	           			 " order by infoD.receiveDate desc , infoD.id "
			 	),
		@NamedQuery(name  = "infoData_searchInfoWithRelatedEntities", 
			 	 query = " select distinct infoD " +
	           	 		 " from InfoData infoD, InfoRelatedEntity infoR" +
	           	 		 " where ( (infoD.id in (select iv.infoId from InfoVisibleDepartment iv where :P_VISIBLE_DPT_LIST_SIZE = 0 or iv.departmentId in (:P_VISIBLE_DPT_LIST)) ) " +
	           			 " or  (infoD.id in (select iAl.infoId from InfoAnalysis iAl where iAl.departmentId in (:P_VISIBLE_DPT_LIST))) )" +
	           			 " and infoD.id = infoR.infoId " +
	           			 " and (:P_COUNTRY_ID = -1 or infoR.countryId = :P_COUNTRY_ID)" +
		                 " and (:P_DEPARTMENT_ID = -1 or infoR.departmentId = :P_DEPARTMENT_ID)" +
		                 " and (:P_DOMAIN_ID = -1 or infoR.domainId = :P_DOMAIN_ID)" +
		                 " and (:P_EMPLOYEE_ID = -1 or infoR.employeeId = :P_EMPLOYEE_ID)" +
		                 " and (:P_NON_EMPLOYEE_ID = -1 or infoR.nonEmployeeId = :P_NON_EMPLOYEE_ID)" +
	           			 " and (:P_INFO_NUMBER = '-1' or infoD.infoNumber = :P_INFO_NUMBER )" +
	           			 " and (:P_INFO_DETAILS = '-1' or infoD.infoDetails like :P_INFO_DETAILS )" +
	           			 " and (:P_INFO_SUBJECT_ID = -1 or infoD.infoSubjectId = :P_INFO_SUBJECT_ID )" +
	           			 " and (:P_INFO_TYPE_ID = -1 or infoD.infoTypeId = :P_INFO_TYPE_ID )" +
	           			 " and (:P_DOMAIN_INCOMING_SIDE_ID = -1 or infoD.domainIncomingSideId = :P_DOMAIN_INCOMING_SIDE_ID )" +
	           			 " and (:P_FROM_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_FROM_DATE, 'MI/MM/YYYY') <= infoD.receiveDate)" + 
	           			 " and (:P_TO_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_TO_DATE, 'MI/MM/YYYY') >= infoD.receiveDate) " +
	           			 " and (:P_INSTANCE_ID = -1 or infoD.wFInstanceId = :P_INSTANCE_ID )" +
	           			 " and (:P_ID = -1 or infoD.id = :P_ID )" +
	           			 " and (:P_REGION_ID = -1 or infoD.regionId = :P_REGION_ID )" +
	           			 " and (:P_SECTOR_ID = -1 or infoD.sectorId = :P_SECTOR_ID )" +
	           			 " and (:P_UNIT_ID = -1 or infoD.unitId = :P_UNIT_ID )" +
	           			 " and (:P_PHONE_NUMBER = '-1' or infoD.id in " +
	           			 "(select phone.infoId from InfoPhone phone where phone.phoneNumber = :P_PHONE_NUMBER)) " +
	           			 " and (:P_INCOMING_FILE_NUMBER = '-1' or infoD.incomingFileNumber like :P_INCOMING_FILE_NUMBER )" +
	           			 " and (:P_INCOMING_FILE_YEAR = -1 or infoD.incomingFileYear = :P_INCOMING_FILE_YEAR )" +
	           			 " and (:P_INCOMING_NUMBER = '-1' or infoD.incomingNumber like :P_INCOMING_NUMBER )" +
	           			 " and (:P_INCOMING_FROM_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_INCOMING_FROM_DATE, 'MI/MM/YYYY') <= infoD.incomingDate)" + 
	           			 " and (:P_INCOMING_TO_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_INCOMING_TO_DATE, 'MI/MM/YYYY') >= infoD.incomingDate) " +
	           			 " order by infoD.receiveDate desc , infoD.id "
			 	),
		@NamedQuery(name  = "infoData_searchInfoWithSources", 
			 	 query = " select distinct infoD " + 
	           	 		 " from InfoData infoD, InfoSource infoS , AssignmentDetailData assignmentDetail" +
	           	 		 " where ( (infoD.id in (select iv.infoId from InfoVisibleDepartment iv where :P_VISIBLE_DPT_LIST_SIZE = 0 or iv.departmentId in (:P_VISIBLE_DPT_LIST)) ) " +
	           			 " or  (infoD.id in (select iAl.infoId from InfoAnalysis iAl where iAl.departmentId in (:P_VISIBLE_DPT_LIST))) )" +
	           			 " and infoD.id = infoS.infoId " +
	           			 " and assignmentDetail.id = infoS.assignmentDetailId "+
						 " and (:P_ASSIGNMENT_DETAIL_ID = -1 or infoS.assignmentDetailId = :P_ASSIGNMENT_DETAIL_ID ) " +
						 " and (:P_IDENTITY = '-1' or assignmentDetail.identity = :P_IDENTITY )" +
						 " and (:P_OFFICER_ID = '-1' or assignmentDetail.officerId = :P_OFFICER_ID )" +
						 " and (:P_OPEN_SOURCE_ID = -1 or infoS.openSourceId = :P_OPEN_SOURCE_ID ) " +
	           			 " and (:P_INFO_NUMBER = '-1' or infoD.infoNumber = :P_INFO_NUMBER )" +
	           			 " and (:P_INFO_DETAILS = '-1' or infoD.infoDetails like :P_INFO_DETAILS )" +
	           			 " and (:P_INFO_SUBJECT_ID = -1 or infoD.infoSubjectId = :P_INFO_SUBJECT_ID )" +
	           			 " and (:P_INFO_TYPE_ID = -1 or infoD.infoTypeId = :P_INFO_TYPE_ID )" +
	           			 " and (:P_DOMAIN_INCOMING_SIDE_ID = -1 or infoD.domainIncomingSideId = :P_DOMAIN_INCOMING_SIDE_ID )" +
	           			 " and (:P_FROM_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_FROM_DATE, 'MI/MM/YYYY') <= infoD.receiveDate)" + 
	           			 " and (:P_TO_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_TO_DATE, 'MI/MM/YYYY') >= infoD.receiveDate) " +
	           			 " and (:P_INSTANCE_ID = -1 or infoD.wFInstanceId = :P_INSTANCE_ID )" +
	           			 " and (:P_ID = -1 or infoD.id = :P_ID )" +
	           			 " and (:P_REGION_ID = -1 or infoD.regionId = :P_REGION_ID )" +
	           			 " and (:P_SECTOR_ID = -1 or infoD.sectorId = :P_SECTOR_ID )" +
	           			 " and (:P_UNIT_ID = -1 or infoD.unitId = :P_UNIT_ID )" +
	           			 " and (:P_PHONE_NUMBER = '-1' or infoD.id in " +
	           			 "(select phone.infoId from InfoPhone phone where phone.phoneNumber = :P_PHONE_NUMBER)) " +
	           			 " and (:P_INCOMING_FILE_NUMBER = '-1' or infoD.incomingFileNumber like :P_INCOMING_FILE_NUMBER )" +
	           			 " and (:P_INCOMING_FILE_YEAR = -1 or infoD.incomingFileYear = :P_INCOMING_FILE_YEAR )" +
	           			 " and (:P_INCOMING_NUMBER = '-1' or infoD.incomingNumber like :P_INCOMING_NUMBER )" +
	           			 " and (:P_INCOMING_FROM_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_INCOMING_FROM_DATE, 'MI/MM/YYYY') <= infoD.incomingDate)" + 
	           			 " and (:P_INCOMING_TO_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_INCOMING_TO_DATE, 'MI/MM/YYYY') >= infoD.incomingDate) " +
	           			 " order by infoD.receiveDate desc , infoD.id "
			 	),
		@NamedQuery(name  = "infoData_searchInfo", 
			 	 query = " select distinct infoD " + 
	           	 		 " from InfoData infoD" +
	           	 		 " where ( (infoD.id in (select iv.infoId from InfoVisibleDepartment iv where :P_VISIBLE_DPT_LIST_SIZE = 0 or iv.departmentId in (:P_VISIBLE_DPT_LIST)) ) " +
	           			 " or  (infoD.id in (select iAl.infoId from InfoAnalysis iAl where iAl.departmentId in (:P_VISIBLE_DPT_LIST))) )" +
						 " and (:P_INFO_NUMBER = '-1' or infoD.infoNumber = :P_INFO_NUMBER )" +
						 " and (:P_INFO_DETAILS = '-1' or infoD.infoDetails like :P_INFO_DETAILS )" +
	           			 " and (:P_INFO_SUBJECT_ID = -1 or infoD.infoSubjectId = :P_INFO_SUBJECT_ID )" +
	           			 " and (:P_INFO_TYPE_ID = -1 or infoD.infoTypeId = :P_INFO_TYPE_ID )" +
	           			 " and (:P_DOMAIN_INCOMING_SIDE_ID = -1 or infoD.domainIncomingSideId = :P_DOMAIN_INCOMING_SIDE_ID )" +
	           			 " and (:P_FROM_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_FROM_DATE, 'MI/MM/YYYY') <= infoD.receiveDate)" + 
	           			 " and (:P_TO_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_TO_DATE, 'MI/MM/YYYY') >= infoD.receiveDate) " +
	           			 " and (:P_INSTANCE_ID = -1 or infoD.wFInstanceId = :P_INSTANCE_ID )" +
	           			 " and (:P_ID = -1 or infoD.id = :P_ID )" +
	           			 " and (:P_REGION_ID = -1 or infoD.regionId = :P_REGION_ID )" +
	           			 " and (:P_SECTOR_ID = -1 or infoD.sectorId = :P_SECTOR_ID )" +
	           			 " and (:P_UNIT_ID = -1 or infoD.unitId = :P_UNIT_ID )" +
	           			 " and (:P_PHONE_NUMBER = '-1' or infoD.id in " +
	           			 "(select phone.infoId from InfoPhone phone where phone.phoneNumber = :P_PHONE_NUMBER)) " +
	           			 " and (:P_INCOMING_FILE_NUMBER = '-1' or infoD.incomingFileNumber like :P_INCOMING_FILE_NUMBER )" +
	           			 " and (:P_INCOMING_FILE_YEAR = -1 or infoD.incomingFileYear = :P_INCOMING_FILE_YEAR )" +
	           			 " and (:P_INCOMING_NUMBER = '-1' or infoD.incomingNumber like :P_INCOMING_NUMBER )" +
	           			 " and (:P_INCOMING_FROM_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_INCOMING_FROM_DATE, 'MI/MM/YYYY') <= infoD.incomingDate)" + 
	           			 " and (:P_INCOMING_TO_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_INCOMING_TO_DATE, 'MI/MM/YYYY') >= infoD.incomingDate) " +
	           			 " order by infoD.receiveDate desc , infoD.id "
			 	),
		@NamedQuery(name  = "infoData_searchInfoVisibleDepartments", 
			 	 query = " select infoD.id " + 
	           	 		 " from InfoData infoD, InfoVisibleDepartment iv" +
	           			 " where infoD.id = iv.infoId " +
	           	 		 " and (:P_DEPARTMENT_ID = -1 or iv.departmentId = :P_DEPARTMENT_ID) " +
	           			 " order by infoD.id "
	           	 ),
	 @NamedQuery(name = "infoData_searchInfoAssignment", 
			  	 query= " select infoD " +
						" from InfoData infoD , InfoAssignmentData assignment , AssignmentDetailData assignmentDetail "+ 
						" where infoD.id =  assignment.infoId  and assignmentDetail.id = assignment.assignmentDetailsId" + 
						" and ( (infoD.id in (select iv.infoId from InfoVisibleDepartment iv where :P_VISIBLE_DPT_LIST_SIZE = 0 or iv.departmentId in (:P_VISIBLE_DPT_LIST)) ) " +
	           			" or  (infoD.id in (select iAl.infoId from InfoAnalysis iAl where iAl.departmentId in (:P_VISIBLE_DPT_LIST))) )" +
						" and (:P_INFO_NUMBER = '-1' or infoD.infoNumber = :P_INFO_NUMBER )" +
						" and (:P_AGENT_CODE = '-1' or assignment.agentCode = :P_AGENT_CODE )" +
						" and (:P_IDENTITY = '-1' or assignmentDetail.identity = :P_IDENTITY )" +
						" and (:P_OFFICER_ID = '-1' or assignmentDetail.officerId = :P_OFFICER_ID )" +
						" and (:P_ASSIGNMENT_DETAIL_ID = -1 or assignment.assignmentDetailsId = :P_ASSIGNMENT_DETAIL_ID )" +
						" and (:P_SAVE_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_SAVE_DATE, 'MI/MM/YYYY') >= infoD.receiveDate)" +
						" order by infoD.receiveDate desc "
			 	),
	 @NamedQuery(name = "infoData_searchInfoOpenSource", 
	 			 query= " select infoD " +
						" from InfoData infoD , InfoOpenSourceData openSrc" + 
						" where infoD.id =  openSrc.infoId " + 
						" and ( (infoD.id in (select iv.infoId from InfoVisibleDepartment iv where :P_VISIBLE_DPT_LIST_SIZE = 0 or iv.departmentId in (:P_VISIBLE_DPT_LIST)) ) " +
	           			" or  (infoD.id in (select iAl.infoId from InfoAnalysis iAl where iAl.departmentId in (:P_VISIBLE_DPT_LIST))) )" +
						" and (:P_OPEN_SOURCE_ID = -1 or openSrc.openSourceId = :P_OPEN_SOURCE_ID )" +
						" and (:P_SAVE_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_SAVE_DATE, 'MI/MM/YYYY') >= infoD.receiveDate)" +
						" order by infoD.receiveDate desc "
			 	),
	@NamedQuery(name = "infoData_searchInfoPhone", 
			  	 query= " select infoD " +
						" from InfoData infoD , InfoPhone phone" + 
						" where infoD.id = phone.infoId " + 
						" and ( (infoD.id in (select iv.infoId from InfoVisibleDepartment iv where :P_VISIBLE_DPT_LIST_SIZE = 0 or iv.departmentId in (:P_VISIBLE_DPT_LIST)) ) " +
	           			" or  (infoD.id in (select iAl.infoId from InfoAnalysis iAl where iAl.departmentId in (:P_VISIBLE_DPT_LIST))) )" +
						" and (:P_PHONE_NUMBER = '-1' or phone.phoneNumber = :P_PHONE_NUMBER )" +
						" order by infoD.receiveDate desc "
			 	),
	 @NamedQuery(name = "infoData_searchInfoRelatedEntity", 
		         query= " select infoD " +
		                " from InfoData infoD, InfoRelatedEntity infoR" +
		                " where infoR.infoId = infoD.id" + 
		                " and ( (infoD.id in (select iv.infoId from InfoVisibleDepartment iv where :P_VISIBLE_DPT_LIST_SIZE = 0 or iv.departmentId in (:P_VISIBLE_DPT_LIST)) ) " +
	           			" or  (infoD.id in (select iAl.infoId from InfoAnalysis iAl where iAl.departmentId in (:P_VISIBLE_DPT_LIST))) )" +
		                " and (:P_COUNTRY_ID = -1 or infoR.countryId = :P_COUNTRY_ID)" +
		                " and (:P_DEPARTMENT_ID = -1 or infoR.departmentId = :P_DEPARTMENT_ID)" +
		                " and (:P_DOMAIN_ID = -1 or infoR.domainId = :P_DOMAIN_ID)" +
		                " and (:P_EMPLOYEE_ID = -1 or infoR.employeeId = :P_EMPLOYEE_ID)" +
		                " and (:P_NON_EMPLOYEE_ID = -1 or infoR.nonEmployeeId = :P_NON_EMPLOYEE_ID)" +
		                " and (:P_SAVE_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_SAVE_DATE, 'MI/MM/YYYY') >= infoD.receiveDate)" +
		                " order by infoD.id "
		       ),
	@NamedQuery(name = "infoData_countInfoRelatedEntity", 
		         query= " select count(infoD.id) " +
		                " from InfoData infoD, InfoRelatedEntity infoR" +
		                " where infoR.infoId = infoD.id" + 
		                " and (:P_EMPLOYEE_ID = -1 or infoR.employeeId = :P_EMPLOYEE_ID)" 
		       )
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_INFORMATION")
public class InfoData extends BaseEntity implements Serializable, Cloneable{
	private Long id;
	private Date registerationDate;
	private String registerationDateString;
	private String infoNumber;
	private Date receiveDate;
	private String receiveDateString;
	private String receiveTime;
	private Long importance;
	private Long relatedInfoId;
	private String infoDetails;
	private String relatedInfoNumber;
	private Long infoSubjectId;
	private String subjectDescription;
	private Long infoTypeId;
	private String typeDescription;
	private String incomingNumber;
	private Date incomingDate;
	private String incomingDateString;
	private Long domainIncomingSideId;
	private Long domainDealingTypeId;
	private Long domainDealingLevelId;
	private String domainIncomingSideDescription;
	private String domainDealingTypeDescription;
	private String domainDealingLevelDescription;
	private String incomingFileNumber;
	private Integer incomingFileYear;
	private Long sectorId;
	private String sectorName;
	private Long regionId;
	private String regionName;
	private Long unitId;
	private String unitName;
	private Integer status;
	private Long wFInstanceId;
	private String letterCopies;
	private String letterNotes;
	private Boolean payReward;
	private Boolean agentEvaluation;
	private Boolean agentApprove;
	private Boolean hasPermission;
	private Long confidentiality;
	private Long infoEvaluationId;
	private String infoEvaluation;

	private Info info;

	public InfoData() {
		this.info = new Info();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return info.getId();
	}

	public void setId(Long id) {
		this.info.setId(id);
		this.id = id;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REGISTERATION_DATE")
	public Date getRegisterationDate() {
		return registerationDate;
	}

	public void setRegisterationDate(Date registerationDate) {
		this.info.setRegisterationDate(registerationDate);
		this.registerationDateString = HijriDateService.getHijriDateString(registerationDate);
		this.registerationDate = registerationDate;
	}

	@Transient
	public String getRegisterationDateString() {
		return registerationDateString;
	}

	@Basic
	@Column(name = "INFO_NUMBER")
	public String getInfoNumber() {
		return infoNumber;
	}

	public void setInfoNumber(String infoNumber) {
		this.info.setInfoNumber(infoNumber);
		this.infoNumber = infoNumber;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RECEIVE_DATE")
	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDateString = HijriDateService.getHijriDateString(receiveDate);
		this.info.setReceiveDate(receiveDate);
		this.receiveDateString = HijriDateService.getHijriDateString(receiveDate);
		this.receiveDate = receiveDate;
	}

	@Transient
	public String getReceiveDateString() {
		return receiveDateString;
	}

	@Basic
	@Column(name = "RECEIVE_TIME")
	public String getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(String receiveTime) {
		this.info.setReceiveTime(receiveTime);
		this.receiveTime = receiveTime;
	}

	@Basic
	@Column(name = "IMPORTANCE")
	public Long getImportance() {
		return importance;
	}

	public void setImportance(Long importance) {
		this.info.setImportance(importance);
		this.importance = importance;
	}

	@Basic
	@Column(name = "RELATED_INFORMATION_ID")
	public Long getRelatedInfoId() {
		return relatedInfoId;
	}

	public void setRelatedInfoId(Long relatedInfoId) {
		this.info.setRelatedInfoId(relatedInfoId);
		this.relatedInfoId = relatedInfoId;
	}

	@Basic
	@Column(name = "INFO_DETAILS")
	public String getInfoDetails() {
		return infoDetails;
	}

	public void setInfoDetails(String infoDetails) {
		this.info.setInfoDetails(infoDetails);
		this.infoDetails = infoDetails;
	}

	@Basic
	@Column(name = "RELATED_INFO_NUMBER")
	public String getRelatedInfoNumber() {
		return relatedInfoNumber;
	}

	public void setRelatedInfoNumber(String relatedInfoNumber) {
		this.relatedInfoNumber = relatedInfoNumber;
	}

	@Basic
	@Column(name = "INFO_SUBJECTS_ID")
	public Long getInfoSubjectId() {
		return infoSubjectId;
	}

	public void setInfoSubjectId(Long infoSubjectId) {
		this.info.setInfoSubjectId(infoSubjectId);
		this.infoSubjectId = infoSubjectId;
	}

	@Basic
	@Column(name = "SUBJECT_DESCRIPTION")
	public String getSubjectDescription() {
		return subjectDescription;
	}

	public void setSubjectDescription(String subjectDescription) {
		this.subjectDescription = subjectDescription;
	}

	@Basic
	@Column(name = "TYPE_DESCRIPTION")
	public String getTypeDescription() {
		return typeDescription;
	}

	public void setTypeDescription(String typeDescription) {
		this.typeDescription = typeDescription;
	}

	@Basic
	@Column(name = "INCOMING_NUMBER")
	public String getIncomingNumber() {
		return incomingNumber;
	}

	public void setIncomingNumber(String incomingNumber) {
		this.info.setIncomingNumber(incomingNumber);
		this.incomingNumber = incomingNumber;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INCOMING_DATE")
	public Date getIncomingDate() {
		return incomingDate;
	}

	public void setIncomingDate(Date incomingDate) {
		this.info.setIncomingDate(incomingDate);
		this.incomingDateString = HijriDateService.getHijriDateString(incomingDate);
		this.incomingDate = incomingDate;
	}

	@Transient
	public String getIncomingDateString() {
		return incomingDateString;
	}

	@Basic
	@Column(name = "DOMAINS_ID_INCOMING_SIDES")
	public Long getDomainIncomingSideId() {
		return domainIncomingSideId;
	}

	public void setDomainIncomingSideId(Long domainIncomingSideId) {
		this.info.setDomainIncomingSideId(domainIncomingSideId);
		this.domainIncomingSideId = domainIncomingSideId;
	}
	
	@Basic
	@Column(name = "DOMAINS_ID_DEALING_TYPES")
	public Long getDomainDealingTypeId() {
		return domainDealingTypeId;
	}

	public void setDomainDealingTypeId(Long domainDealingTypeId) {
		this.info.setDomainDealingTypeId(domainDealingTypeId);
		this.domainDealingTypeId = domainDealingTypeId;
	}

	@Basic
	@Column(name = "DOMAINS_ID_DEALING_LEVELS")
	public Long getDomainDealingLevelId() {
		return domainDealingLevelId;
	}

	public void setDomainDealingLevelId(Long domainDealingLevelId) {
		this.info.setDomainDealingLevelId(domainDealingLevelId);
		this.domainDealingLevelId = domainDealingLevelId;
	}

	@Basic
	@Column(name = "DOMAIN_INCOMING_SIDES_DESC")
	public String getDomainIncomingSideDescription() {
		return domainIncomingSideDescription;
	}

	public void setDomainIncomingSideDescription(String domainIncomingSideDescription) {
		this.domainIncomingSideDescription = domainIncomingSideDescription;
	}

	@Basic
	@Column(name = "DOMAIN_DEALING_TYPES_DESC")
	public String getDomainDealingTypeDescription() {
		return domainDealingTypeDescription;
	}

	public void setDomainDealingTypeDescription(String domainDealingTypeDescription) {
		this.domainDealingTypeDescription = domainDealingTypeDescription;
	}

	@Basic
	@Column(name = "DOMAIN_DEALING_LEVELS_DESC")
	public String getDomainDealingLevelDescription() {
		return domainDealingLevelDescription;
	}

	public void setDomainDealingLevelDescription(String domainDealingLevelDescription) {
		this.domainDealingLevelDescription = domainDealingLevelDescription;
	}

	@Basic
	@Column(name = "STP_VW_DEPARTMENTS_ID_SECTOR")
	public Long getSectorId() {
		return sectorId;
	}

	public void setSectorId(Long sectorId) {
		this.info.setSectorId(sectorId);
		this.sectorId = sectorId;
	}

	@Basic
	@Column(name = "SECTOR_NAME")
	public String getSectorName() {
		return sectorName;
	}

	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}

	@Basic
	@Column(name = "STATUS")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.info.setStatus(status);
		this.status = status;
	}

	@Basic
	@Column(name = "WF_INSTANCES_ID")
	public Long getwFInstanceId() {
		return wFInstanceId;
	}

	public void setwFInstanceId(Long wFInstanceId) {
		this.info.setwFInstanceId(wFInstanceId);
		this.wFInstanceId = wFInstanceId;
	}

	@Basic
	@Column(name = "LETTER_COPIES")
	public String getLetterCopies() {
		return letterCopies;
	}

	public void setLetterCopies(String letterCopies) {
		this.info.setLetterCopies(letterCopies);
		this.letterCopies = letterCopies;
	}

	@Basic
	@Column(name = "LETTERS_NOTES")
	public String getLetterNotes() {
		return letterNotes;
	}

	public void setLetterNotes(String letterNotes) {
		this.info.setLetterNotes(letterNotes);
		this.letterNotes = letterNotes;
	}

	@Basic
	@Column(name = "PAY_REWARD")
	public Boolean getPayReward() {
		return payReward;
	}

	public void setPayReward(Boolean payReward) {
		this.info.setPayReward(payReward);
		this.payReward = payReward;
	}

	@Basic
	@Column(name = "AGENT_EVALUATION")
	public Boolean getAgentEvaluation() {
		return agentEvaluation;
	}

	public void setAgentEvaluation(Boolean agentEvaluation) {
		this.info.setAgentEvaluation(agentEvaluation);
		this.agentEvaluation = agentEvaluation;
	}

	@Basic
	@Column(name = "AGENT_APPROVE")
	public Boolean getAgentApprove() {
		return agentApprove;
	}

	public void setAgentApprove(Boolean agentApprove) {
		this.info.setAgentApprove(agentApprove);
		this.agentApprove = agentApprove;
	}
	
	@Basic
	@Column(name = "DEPARTMENT_REGION_ID")
	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.info.setRegionId(regionId);
		this.regionId = regionId;
	}

	@Basic
	@Column(name = "DEPARTMENT_REGION_NAME")
	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	@Basic
	@Column(name = "DEPARTMENT_UNIT_ID")
	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.info.setUnitId(unitId);;
		this.unitId = unitId;
	}

	@Basic
	@Column(name = "DEPARTMENT_UNIT_NAME")
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	@Transient
	public Boolean getHasPermission() {
		return hasPermission;
	}

	public void setHasPermission(Boolean hasPermission) {
		this.hasPermission = hasPermission;
	}
	
	@Transient
	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Basic
	@Column(name = "INFO_TYPE_ID")
	public Long getInfoTypeId() {
		return infoTypeId;
	}

	public void setInfoTypeId(Long infoTypeId) {
		this.infoTypeId = infoTypeId;
	}
	
	@Basic
	@Column(name = "INCOMING_FILE_NUMBER")
	public String getIncomingFileNumber() {
		return incomingFileNumber;
	}

	public void setIncomingFileNumber(String incomingFileNumber) {
		this.info.setIncomingFileNumber(incomingFileNumber);
		this.incomingFileNumber = incomingFileNumber;
	}

	@Basic
	@Column(name = "INCOMING_FILE_YEAR")
	public Integer getIncomingFileYear() {
		return incomingFileYear;
	}

	public void setIncomingFileYear(Integer incomingFileYear) {
		this.info.setIncomingFileYear(incomingFileYear);
		this.incomingFileYear = incomingFileYear;
	}

	@Basic
	@Column(name = "CONFIDENTIALITY")
	public Long getConfidentiality() {
		return confidentiality;
	}

	public void setConfidentiality(Long confidentiality) {
		this.info.setConfidentiality(confidentiality);
		this.confidentiality = confidentiality;
	}

	@Basic
	@Column(name = "INFO_EVALUATION")
	public String getInfoEvaluation() {
		return infoEvaluation;
	}

	public void setInfoEvaluation(String infoEvaluation) {
		this.infoEvaluation = infoEvaluation;
	}

	@Basic
	@Column(name = "INFO_EVALUATION_ID")
	public Long getInfoEvaluationId() {
		return infoEvaluationId;
	}

	public void setInfoEvaluationId(Long infoEvaluationId) {
		this.info.setInfoEvaluationId(infoEvaluationId);
		this.infoEvaluationId = infoEvaluationId;
	}
}