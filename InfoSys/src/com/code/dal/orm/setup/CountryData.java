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
	@NamedQuery(name  = "countryData_searchCountry", 
			    query = " select c " +
					    " from CountryData c " +
			    		" where (:P_ID = -1 or c.id = :P_ID) " +
			    		" and (:P_ARABIC_NAME = '-1' or c.arabicName like :P_ARABIC_NAME )" +
					    " order by c.arabicName"
			    ),
    @NamedQuery(name  = "countryData_searchInfoRelatedCountries", 
			    query = " select c " +
					    " from CountryData c, InfoRelatedEntity infoRel" +
					    " where ( c.id = infoRel.countryId)" +
			 			 " and (:P_INFO_ID = '-1' or infoRel.infoId = :P_INFO_ID )" +
			        	 " order by c.id "
			    )
			    
})

@SuppressWarnings("serial")
@Entity
@Table(name = "STP_VW_COUNTRIES")
public class CountryData extends BaseEntity implements Serializable{
	private Long id;
	private String arabicName;
	private Short yaqeenCode;

	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	@Column(name = "YAQEEN_CODE")
	public Short getYaqeenCode() {
		return yaqeenCode;
	}

	public void setYaqeenCode(Short yaqeenCode) {
		this.yaqeenCode = yaqeenCode;
	}
}
