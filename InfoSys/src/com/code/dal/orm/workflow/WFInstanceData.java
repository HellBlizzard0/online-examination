package com.code.dal.orm.workflow;

import com.code.dal.orm.BaseEntity;
import com.code.services.util.HijriDateService;

import java.text.SimpleDateFormat;
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

@SuppressWarnings("serial")
@NamedQueries({
          @NamedQuery(name="wf_instanceData_searchWFInstancesData", 
                      query= " select i from WFInstanceData i " +
                             " where i.requesterId = :P_REQUESTER_ID " +
                             "   and (:P_PROCESS_GROUP_ID = 0 OR i.processGroupId = :P_PROCESS_GROUP_ID) " +
                             "   and (:P_PROCESS_ID = 0 OR i.processId = :P_PROCESS_ID) " +
                             "   and ((i.arabicDetailsSummary like :P_INSTANCE_DETAILS or i.englishDetailsSummary like :P_INSTANCE_DETAILS) or (:P_INSTANCE_DETAILS_FLAG = 0 and i.arabicDetailsSummary  is null))" +
                             "   and i.status in :P_STATUS_VALUES " +
                             " 	 and (:P_FROM_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_FROM_DATE, 'MI/MM/YYYY') <= i.hijriRequestDate)" + 
    	           			 "   and (:P_TO_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_TO_DATE, 'MI/MM/YYYY') >= i.hijriRequestDate) " +
                             " order by i.requestDate desc "                             
          ),
          @NamedQuery(name="wf_instanceData_getWFInstanceDataById", 
                      query= " select i from WFInstanceData i " +
                             " where i.instanceId = :P_INSTANCE_ID "                             
          ),
          @NamedQuery(name="wf_instanceData_getWFInstancesUnderProcessingCount", 
          	      query= " select count(i.instanceId) from WFInstanceData i " +
                             " where i.requesterId = :P_REQUESTER_ID " +
                             " and i.status = 1 "
          )
})

@Entity
@Table(name="FIS_VW_INSTANCES")
public class WFInstanceData extends BaseEntity{  
    private Long instanceId;
    private Long processId;
    private String processName;
    private Long processGroupId;
    private Long requesterId;
    private String requesterName;
    private String requesterRankDesc;
    private Date requestDate;
    private Date hijriRequestDate;
    private String hijriRequestDateString;
    private Integer status;
    private String attachments;
    private String arabicDetailsSummary;
    private String englishDetailsSummary;
    
    private WFInstance wFInstance;
    
    public WFInstanceData() {
    	this.wFInstance = new WFInstance();
	}

    public void setInstanceId(Long instanceId) {
    	this.wFInstance.setInstanceId(instanceId);
        this.instanceId = instanceId;
    }

    @Id   
    @Column(name="ID")
    public Long getInstanceId() {
        return wFInstance.getInstanceId();
    }

    public void setProcessId(Long processId) {
    	this.wFInstance.setProcessId(processId);
        this.processId = processId;
    }

    @Basic
    @Column(name="PROCESS_ID")
    public Long getProcessId() {
        return processId;
    }  
    
    public void setProcessName(String processName) {
        this.processName = processName;
    }

    @Basic
    @Column(name="PROCESS_NAME")
    public String getProcessName() {
        return processName;
    }

    public void setProcessGroupId(Long processGroupId) {
        this.processGroupId = processGroupId;
    }

    @Basic
    @Column(name="PROCESS_GROUP_ID")
    public Long getProcessGroupId() {
        return processGroupId;
    }
    
    public void setRequesterId(Long requesterId) {
    	this.wFInstance.setRequesterId(requesterId);
        this.requesterId = requesterId;
    }
    
    @Basic
    @Column(name="REQUESTER_ID")
    public Long getRequesterId() {
        return requesterId;
    }
    
    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    @Basic
    @Column(name="REQUESTER_NAME")
    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterRankDesc(String requesterRankDesc) {
        this.requesterRankDesc = requesterRankDesc;
    }

    @Basic
    @Column(name="REQUESTER_RANK_DESC")
    public String getRequesterRankDesc() {
        return requesterRankDesc;
    }
    
    public void setRequestDate(Date requestDate) {
    	this.wFInstance.setRequestDate(requestDate);
        this.requestDate = requestDate;
        
        if(this.requestDate != null && this.hijriRequestDate != null){
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            this.hijriRequestDateString = HijriDateService.getHijriDateString(hijriRequestDate) + " " + sdf.format(requestDate);
        }
    }

    @Basic
    @Column(name="REQUEST_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getRequestDate() {
        return requestDate;
    }

    public void setHijriRequestDate(Date hijriRequestDate) {
    	this.wFInstance.setHijriRequestDate(hijriRequestDate);
        this.hijriRequestDate = hijriRequestDate;
        
        if(this.requestDate != null && this.hijriRequestDate != null){
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            this.hijriRequestDateString = HijriDateService.getHijriDateString(hijriRequestDate) + " " + sdf.format(requestDate);
        }
    }

    @Basic
    @Column(name="HIJRI_REQUEST_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getHijriRequestDate() {
        return hijriRequestDate;
    }

    public void setStatus(Integer status) {
    	this.wFInstance.setStatus(status);
        this.status = status;
    }

    @Basic
    @Column(name="STATUS")
    public Integer getStatus() {
        return status;
    }

    public void setAttachments(String attachments) {
    	this.wFInstance.setAttachments(attachments);
        this.attachments = attachments;
    }

    @Basic
    @Column(name="ATTACHMENTS")
    public String getAttachments() {
        return attachments;
    }
    
    @Basic
    @Column(name="ARABIC_DETAILS_SUMMARY")
	public String getArabicDetailsSummary() {
		return arabicDetailsSummary;
	}

	public void setArabicDetailsSummary(String arabicDetailsSummary) {
		this.arabicDetailsSummary = arabicDetailsSummary;
		this.wFInstance.setArabicDetailsSummary(arabicDetailsSummary);
	}

	@Basic
    @Column(name="ENGLISH_DETAILS_SUMMARY")
	public String getEnglishDetailsSummary() {
		return englishDetailsSummary;
	}

	public void setEnglishDetailsSummary(String englishDetailsSummary) {
		this.englishDetailsSummary = englishDetailsSummary;
		this.wFInstance.setEnglishDetailsSummary(englishDetailsSummary);
	}

    @Transient
    public String getHijriRequestDateString() {
        return hijriRequestDateString;
    }

    @Transient
	public WFInstance getwFInstance() {
		return wFInstance;
	}

	public void setwFInstance(WFInstance wFInstance) {
		this.wFInstance = wFInstance;
	}

	@Override
	public void setId(Long id) {
		this.wFInstance.setInstanceId(id);
        this.instanceId = id;
	}
}