package com.code.dal.orm.setup;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name  = "locationProfile_searchLocation", 
				    query = " select l from LocationProfileData l order by l.id"
				    )
})


@SuppressWarnings("serial")
@Entity
@Table(name = "STP_VW_LOCATIONS_PROFILE")
public class LocationProfileData implements Serializable {
	private Long id;
	private String name;

	public void setId(Long Id) {
		this.id = Id;
	}

	@Id
	@Column(name = "DOCK_ID")
	public Long getId() {
		return id;
	}
	
	@Basic
	@Column(name = "DOCK_NM")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	/// -- TODO: Description to be added
}