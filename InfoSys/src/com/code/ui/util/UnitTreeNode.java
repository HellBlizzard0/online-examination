package com.code.ui.util;

import java.io.Serializable;

public class UnitTreeNode extends TreeNodeImpl implements Serializable{
    private String unitName;
    private String unitType;
    
    public UnitTreeNode() {
	super();
    }

    public String getUnitName() {
	return unitName;
    }

    public void setUnitName(String unitName) {
	this.unitName = unitName;
    }

    public String getUnitType() {
	return unitType;
    }

    public void setUnitType(String unitType) {
	this.unitType = unitType;
    }
}
