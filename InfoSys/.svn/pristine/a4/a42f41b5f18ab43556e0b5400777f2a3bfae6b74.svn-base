package com.code.dal.orm;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
	private boolean selected;

	@Transient
	public boolean getSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public abstract void setId(Long id);
	
}