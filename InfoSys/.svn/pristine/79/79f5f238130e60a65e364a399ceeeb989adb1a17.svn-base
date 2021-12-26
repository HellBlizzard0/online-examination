package com.code.dal.orm.setup;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.code.dal.orm.BaseEntity;

@NamedQueries({
	  		   @NamedQuery(name ="image_getImage", 
	  					   query= "select i from Image i where id = :P_IMAGE_ID")
			  }
)


@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_IMAGES")
public class Image extends BaseEntity implements Serializable {
	private Long id;
	private String type;
	private byte[] content;
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Id
	@Column(name="ID")
	@SequenceGenerator(name="STP_ITEM_IMAGE_SEQ", sequenceName="STP_ITEM_IMAGE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="STP_ITEM_IMAGE_SEQ")
	public Long getId() {
		return id;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	@Basic
	@Column(name="TYPE")
	public String getType() {
		return type;
	}
	
	public void setContent(byte[] content) {
		this.content = content;
	}
	
	@Basic
	@Column(name="CONTENT")
	@Lob
	public byte[] getContent() {
		return content;
	}
}
