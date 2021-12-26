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
		@NamedQuery(name = "rankData_searchRankByCategoryId",
				query = " select r " +
						" from RankData r " +
						" where r.categoryId in (:P_CATEGORY_ID)" +
						" and :P_RANK_ID = -1 or r.id = :P_RANK_ID" +
						" order by r.id ")
})

@SuppressWarnings("serial")
@Entity
@Table(name = "STP_VW_RANKS")
public class RankData extends BaseEntity implements Serializable {
	private Long id;
	private String code;
	private String description;
	private String latinDescription;
	private Long categoryId;

	@Id
	@Column(name = "id")
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
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Basic
	@Column(name = "LATIN_DESC")
	public String getLatinDescription() {
		return latinDescription;
	}

	public void setLatinDescription(String latinDescription) {
		this.latinDescription = latinDescription;
	}

	@Basic
	@Column(name = "STP_CATEGORIES_ID")
	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

}