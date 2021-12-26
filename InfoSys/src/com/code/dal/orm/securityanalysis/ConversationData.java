package com.code.dal.orm.securityanalysis;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
import com.code.services.infosys.securityanalysis.ConversationService;
import com.code.services.util.HijriDateService;

@NamedQueries({
	@NamedQuery(name = "conversationData_searchConversations",
				query = " select c " +
						" from ConversationData c" +
						" WHERE (:P_FOLLOW_UP_ID = -1 OR c.followUpId = :P_FOLLOW_UP_ID )" +
						" And (:P_CONVERSATION_ID = -1 OR c.id like :P_CONVERSATION_ID)" +
						" And (:P_CONVERSATION_RESULT = -1 OR c.conversationResult = :P_CONVERSATION_RESULT)" +
						" And (:P_CONVERSATION_DATE_NULL = 1 or c.conversationDate = PKG_CHAR_TO_DATE (:P_CONVERSATION_DATE, 'MI/MM/YYYY') ) " +
						" and (:P_TIME = '-1' or c.conversationTime = :P_TIME)" +
						" and (:P_INSTANCE_ID = -1 or c.instanceId = :P_INSTANCE_ID)" +
						" order by c.id "),
	@NamedQuery(name = "conversationData_searchConversationDataList",
				query = " select c " +
						" from ConversationData c, ConversationPartyData cp" +
						" WHERE c.id = cp.conversationId" +
						" AND (:P_FOLLOW_UP_ID = -1 OR c.followUpId = :P_FOLLOW_UP_ID )" +
						" And (:P_CONVERSATION_TYPE = -1 OR c.conversationType like :P_CONVERSATION_TYPE)" +
						" And (:P_CONVERSATION_RESULT = -1 OR c.conversationResult = :P_CONVERSATION_RESULT)" +
						" And (:P_CONVERSATION_LATITUDE = -1 OR c.coordinateLatitude = :P_CONVERSATION_LATITUDE)" +
						" And (:P_CONVERSATION_LONGITUDE = -1 OR c.coordinateLongitude = :P_CONVERSATION_LONGITUDE)" +
						" And (:P_CONTACT_NUMBER = -1 OR cp.contactNumber = :P_CONTACT_NUMBER)" +
						" and (:P_FROM_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_FROM_DATE, 'MI/MM/YYYY') <= c.conversationDate) " +
						" and (:P_TO_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_TO_DATE, 'MI/MM/YYYY') >= c.conversationDate) " + 
						" order by c.id "),

})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_SC_VW_CONVERSATIONS")
public class ConversationData extends BaseEntity implements Serializable {
	private Long id;
	private Double coordinateLatitude;
	private Double coordinateLongitude;
	private Integer conversationType;
	private Date conversationDate;
	private String conversationDateString;
	private String conversationTime;
	private String conversationLocation;
	private Integer conversationResult;
	private String conversationDetails;
	private String conversationSummary;
	private Long domainIdChatAction;
	private String domainChatDescription;
	private String actionTaken;
	private Long followUpId;
	private String followUpCode;
	private String partiesAndNames;
	private Long regionId;
	private String regionName;
	private Integer regionType;
	private Long instanceId;

	private Conversation conversation;

	public ConversationData() {
		this.conversation = new Conversation();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.conversation.setId(id);
		this.id = id;
	}

	@Basic
	@Column(name = "COORDINATE_LAT")
	public Double getCoordinateLatitude() {
		return coordinateLatitude;
	}

	public void setCoordinateLatitude(Double coordinateLatitude) {
		this.conversation.setCoordinateLatitude(coordinateLatitude);
		this.coordinateLatitude = coordinateLatitude;
	}

	@Basic
	@Column(name = "COORDINATE_LONG")
	public Double getCoordinateLongitude() {
		return coordinateLongitude;
	}

	public void setCoordinateLongitude(Double coordinateLongitude) {
		this.conversation.setCoordinateLongitude(coordinateLongitude);
		this.coordinateLongitude = coordinateLongitude;
	}

	@Basic
	@Column(name = "CONVERSATION_TYPE")
	public Integer getConversationType() {
		return conversationType;
	}

	public void setConversationType(Integer conversationType) {
		this.conversation.setConversationType(conversationType);
		this.conversationType = conversationType;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CONVERSATION_DATE")
	public Date getConversationDate() {
		return conversationDate;
	}

	public void setConversationDate(Date conversationDate) {
		this.conversationDate = conversationDate;
		this.conversationDateString = HijriDateService.getHijriDateString(conversationDate);
		this.conversation.setConversationDate(conversationDate);
	}

	@Transient
	public String getConversationDateString() {
		return conversationDateString;
	}

	public void setConversationDateString(String conversationDateString) {
		this.conversationDateString = conversationDateString;
		this.setConversationDate(HijriDateService.getHijriDate(conversationDateString));
	}

	@Basic
	@Column(name = "CONVERSATION_TIME")
	public String getConversationTime() {
		return conversationTime;
	}

	public void setConversationTime(String conversationTime) {
		this.conversation.setConversationTime(conversationTime);
		this.conversationTime = conversationTime;
	}

	@Basic
	@Column(name = "CONVERSATION_LOCATION")
	public String getConversationLocation() {
		return conversationLocation;
	}

	public void setConversationLocation(String conversationLocation) {
		this.conversation.setConversationLocation(conversationLocation);
		this.conversationLocation = conversationLocation;
	}

	@Basic
	@Column(name = "CONVERSATION_RESULT")
	public Integer getConversationResult() {
		return conversationResult;
	}

	public void setConversationResult(Integer conversationResult) {
		this.conversation.setConversationResult(conversationResult);
		this.conversationResult = conversationResult;
	}

	@Basic
	@Column(name = "CONVERSATION_DETAILS")
	public String getConversationDetails() {
		return conversationDetails;
	}

	public void setConversationDetails(String conversationDetails) {
		this.conversation.setConversationDetails(conversationDetails);
		this.conversationDetails = conversationDetails;
	}

	@Basic
	@Column(name = "CONVERSATION_SUMMARY")
	public String getConversationSummary() {
		return conversationSummary;
	}

	public void setConversationSummary(String conversationSummary) {
		this.conversation.setConversationSummary(conversationSummary);
		this.conversationSummary = conversationSummary;
	}

	@Basic
	@Column(name = "DOMAIN_ID_CHAT_ACTIONS")
	public Long getDomainIdChatAction() {
		return domainIdChatAction;
	}

	public void setDomainIdChatAction(Long domainIdChatAction) {
		this.conversation.setDomainIdChatAction(domainIdChatAction);
		this.domainIdChatAction = domainIdChatAction;
	}

	@Basic
	@Column(name = "DOMAIN_CHAT_DESCRIPTION")
	public String getDomainChatDescription() {
		return domainChatDescription;
	}

	public void setDomainChatDescription(String domainChatDescription) {
		this.domainChatDescription = domainChatDescription;
	}

	@Basic
	@Column(name = "ACTION_TAKEN")
	public String getActionTaken() {
		return actionTaken;
	}

	public void setActionTaken(String actionTaken) {
		this.actionTaken = actionTaken;
		this.conversation.setActionTaken(actionTaken);
	}

	@Basic
	@Column(name = "FOLLOW_UP_ID")
	public Long getFollowUpId() {
		return followUpId;
	}

	public void setFollowUpId(Long followUpId) {
		this.conversation.setFollowUpId(followUpId);
		this.followUpId = followUpId;
	}

	@Basic
	@Column(name = "FOLLOW_UP_CODE")
	public String getFollowUpCode() {
		return followUpCode;
	}

	public void setFollowUpCode(String followUpCode) {
		this.followUpCode = followUpCode;
	}

	@Transient
	public Conversation getConversation() {
		return conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	@Basic
	@Column(name = "WF_INSTANCE_ID")
	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
		this.conversation.setInstanceId(instanceId);
	}

	@Transient
	public String getPartiesAndNames() {
		List<ConversationPartyData> partiesList = ConversationService.getConversationsPartiesByConversationId(id);
		partiesAndNames = "";
		for (int i = 0; i < partiesList.size(); i++) {
			partiesAndNames += partiesList.get(i).getName() + " " + partiesList.get(i).getContactNumber() + "\n";
		}
		return partiesAndNames;
	}

	public void setPartiesAndNames(String partiesAndNames) {
		this.partiesAndNames = partiesAndNames;
	}

	@Basic
	@Column(name = "REGION_ID")
	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.conversation.setRegionId(regionId);
		this.regionId = regionId;
	}

	@Basic
	@Column(name = "REGION_NAME")
	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	@Basic
	@Column(name = "REGION_TYPE")
	public Integer getRegionType() {
		return regionType;
	}

	public void setRegionType(Integer regionType) {
		this.regionType = regionType;
	}

}
