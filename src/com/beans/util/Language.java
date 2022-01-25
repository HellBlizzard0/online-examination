/**
 * 
 */
package com.beans.util;

import java.util.Locale;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 * @author aalsaqqa
 * Language Control Managed Bean
 */
@SessionScoped
@ManagedBean(name="languageBean")
public class Language {
	public enum Langs{
		ARABIC,
		ENGLISH
	}
	Langs lang;
	public String toggleLanguage() {
		switch (lang) {
			case ARABIC :
				FacesContext.getCurrentInstance().getViewRoot()
				.setLocale(new Locale("en"));
				lang = Langs.ENGLISH;
				break;
			case ENGLISH :
				FacesContext.getCurrentInstance().getViewRoot()
				.setLocale(new Locale("ar"));
				lang = Langs.ARABIC;
				break;

		}
		return "faces-redirect=true";
	}
	public Language() {
		if(Locale.getDefault().getDisplayLanguage().equals("English"))
			lang = Langs.ENGLISH;
		else
			lang = Langs.ARABIC;
	}
	
	public boolean isRTL() {
		if(lang == Langs.ARABIC)
			return true;
		else
			return false;
	}
	public boolean isLTR() {
		if(lang == Langs.ENGLISH)
			return true;
		else
			return false;
	}
}
