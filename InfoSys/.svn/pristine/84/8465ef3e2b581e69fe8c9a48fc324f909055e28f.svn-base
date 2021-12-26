package com.code.dal.orm.securityanalysis;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.code.dal.orm.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_SC_CONVERSATIONS")
public class Conversation extends BaseEntity implements Serializable {
	private Long id;
	private Double coordinateLatitude;
	private Double coordinateLongitude;
	private Integer conversationType;
	private Date conversationDate;
	private String conversationTime;
	private String conversationLocation;
	private Integer conversationResult;
	private String conversationDetails;
	private String conversationSummary;
	private Long domainIdChatAction;
	private String actionTaken;
	private Long followUpId;
	private Long instanceId;
	private Long regionId;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_SC_CONVERSATIONS_SEQ", sequenceName = "FIS_SC_CONVERSATIONS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_SC_CONVERSATIONS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "COORDINATE_LAT")
	public Double getCoordinateLatitude() {
		return coordinateLatitude;
	}

	public void setCoordinateLatitude(Double coordinateLatitude) {
		this.coordinateLatitude = coordinateLatitude;
	}

	@Basic
	@Column(name = "COORDINATE_LONG")
	public Double getCoordinateLongitude() {
		return coordinateLongitude;
	}

	public void setCoordinateLongitude(Double coordinateLongitude) {
		this.coordinateLongitude = coordinateLongitude;
	}

	@Basic
	@Column(name = "CONVERSATION_TYPE")
	public Integer getConversationType() {
		return conversationType;
	}

	public void setConversationType(Integer conversationType) {
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
	}

	@Basic
	@Column(name = "CONVERSATION_TIME")
	public String getConversationTime() {
		return conversationTime;
	}

	public void setConversationTime(String conversationTime) {
		this.conversationTime = conversationTime;
	}

	@Basic
	@Column(name = "CONVERSATION_LOCATION")
	public String getConversationLocation() {
		return conversationLocation;
	}

	public void setConversationLocation(String conversationLocation) {
		this.conversationLocation = conversationLocation;
	}

	@Basic
	@Column(name = "CONVERSATION_RESULT")
	public Integer getConversationResult() {
		return conversationResult;
	}

	public void setConversationResult(Integer conversationResult) {
		this.conversationResult = conversationResult;
	}

	@Basic
	@Column(name = "CONVERSATION_DETAILS")
	public String getConversationDetails() {
		return conversationDetails;
	}

	public void setConversationDetails(String conversationDetails) {
		this.conversationDetails = conversationDetails;
	}

	@Basic
	@Column(name = "CONVERSATION_SUMMARY")
	public String getConversationSummary() {
		return conversationSummary;
	}

	public void setConversationSummary(String conversationSummary) {
		this.conversationSummary = conversationSummary;
	}

	@Basic
	@Column(name = "DOMAIN_ID_CHAT_ACTIONS")
	public Long getDomainIdChatAction() {
		return domainIdChatAction;
	}

	public void setDomainIdChatAction(Long domainIdChatAction) {
		this.domainIdChatAction = domainIdChatAction;
	}

	@Basic
	@Column(name = "ACTION_TAKEN")
	public String getActionTaken() {
		return actionTaken;
	}

	public void setActionTaken(String actionTaken) {
		this.actionTaken = actionTaken;
	}

	@Basic
	@Column(name = "FOLLOW_UP_ID")
	public Long getFollowUpId() {
		return followUpId;
	}

	public void setFollowUpId(Long followUpId) {
		this.followUpId = followUpId;
	}

	@Basic
	@Column(name = "WF_INSTANCE_ID")
	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}
	
	@Basic
	@Column(name = "REGION_ID")
	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}
}
