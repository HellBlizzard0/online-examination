package com.code.dal.orm.info;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.code.dal.audit.DeleteableAuditEntity;
import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;

@NamedQueries({
	 @NamedQuery(name = "infoCoordinate_searchInfoCoordinates", 
        	 	 query= " select infoCo " +
        	 			" from InfoCoordinate infoCo " +
        	 			" where (:P_INFO_ID = -1 or infoCo.infoId = :P_INFO_ID) " +
        	 			" order by infoCo.id "
        	 	),
     @NamedQuery(name = "infoCoordinate_deleteInfoCoordinates", 
 	 			query= 	" delete " +
 	 					" from InfoCoordinate infoCoordinate " +
 	 					" where (infoCoordinate.infoId = :P_INFO_ID) " 
 				)
})
@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_INFO_COORDINATES")
public class InfoCoordinate extends AuditEntity implements Serializable, DeleteableAuditEntity, InsertableAuditEntity, UpdateableAuditEntity {
	private Long id;
	private Long infoId;
	private Integer northDegree;
	private Integer northMinutes;
	private String northSecondes;
	private Integer eastDegree;
	private Integer eastMinutes;
	private String eastSecondes;

	@Id
	@SequenceGenerator(name = "FIS_INFO_COORDINATES_SEQ", sequenceName = "FIS_INFO_COORDINATES_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_INFO_COORDINATES_SEQ")
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	@Column(name = "NORTH_DEGREE")
	public Integer getNorthDegree() {
		return northDegree;
	}

	public void setNorthDegree(Integer northDegree) {
		this.northDegree = northDegree;
	}

	@Basic
	@Column(name = "NORTH_MINUTES")
	public Integer getNorthMinutes() {
		return northMinutes;
	}

	public void setNorthMinutes(Integer northMinutes) {
		this.northMinutes = northMinutes;
	}

	@Basic
	@Column(name = "NORTH_SECONDES")
	public String getNorthSecondes() {
		return northSecondes;
	}

	public void setNorthSecondes(String northSecondes) {
		this.northSecondes = northSecondes;
	}

	@Basic
	@Column(name = "EAST_DEGREE")
	public Integer getEastDegree() {
		return eastDegree;
	}

	public void setEastDegree(Integer eastDegree) {
		this.eastDegree = eastDegree;
	}

	@Basic
	@Column(name = "EAST_MINUTES")
	public Integer getEastMinutes() {
		return eastMinutes;
	}

	public void setEastMinutes(Integer eastMinutes) {
		this.eastMinutes = eastMinutes;
	}

	@Basic
	@Column(name = "EAST_SECONDES")
	public String getEastSecondes() {
		return eastSecondes;
	}

	public void setEastSecondes(String eastSecondes) {
		this.eastSecondes = eastSecondes;
	}

	@Override
	public Long calculateContentId() {
		return this.id;
	}
}
