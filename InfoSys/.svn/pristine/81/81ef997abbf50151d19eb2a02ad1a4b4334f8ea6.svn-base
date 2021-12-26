package com.code.ui.backings.security;

import java.util.Calendar;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.code.ui.backings.base.BaseBacking;

@ManagedBean(name = "footer")
@SessionScoped
public class Footer extends BaseBacking {
	String copyRightMessage = this.getParameterizedMessage("label_copyright", new Object[] { Calendar.getInstance().get(Calendar.YEAR) + "" });

	public Footer() {
		super();
	}

	public String getCopyRightMessage() {
		return copyRightMessage;
	}
}
