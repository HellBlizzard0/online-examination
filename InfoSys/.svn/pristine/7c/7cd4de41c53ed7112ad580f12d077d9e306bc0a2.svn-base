package com.code.dal.orm.info;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.code.dal.orm.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_INFO_OPEN_SOURCES")
public class InfoOpenSourceData extends BaseEntity implements Serializable {
	private Long id;
	private Long openSourceId;
	private Long infoId;
	private Integer sourceType;
	private String openSourceName;

	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "OPEN_SOURCES_ID")
	public Long getOpenSourceId() {
		return openSourceId;
	}

	public void setOpenSourceId(Long openSourceId) {
		this.openSourceId = openSourceId;
	}

	@Basic
	@Column(name = "INFORMATION_ID")
	public Long getInfoId() {
		return infoId;
	}

	public void setInfoId(Long infoId) {
		this.infoId = infoId;
	}

	@Basic
	@Column(name = "SOURCE_TYPE")
	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	@Basic
	@Column(name = "OPEN_SOURCE_NAME")
	public String getOpenSourceName() {
		return openSourceName;
	}

	public void setOpenSourceName(String openSourceName) {
		this.openSourceName = openSourceName;
	}
}
