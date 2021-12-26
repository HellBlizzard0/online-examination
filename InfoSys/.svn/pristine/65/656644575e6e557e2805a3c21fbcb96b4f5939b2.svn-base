package com.code.ui.backings.base;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.code.dal.orm.setup.EmployeeData;
import com.code.enums.SessionAttributesEnum;
import com.code.services.log4j.Log4j;

public abstract class BaseBacking {
	private ResourceBundle messages;
	private String screenTitle;
	protected EmployeeData loginEmpData;

	public BaseBacking() {
		HttpSession session = getSession();
		if (session.getAttribute(SessionAttributesEnum.EMP_DATA.getCode()) != null) {
			loginEmpData = (EmployeeData) session.getAttribute(SessionAttributesEnum.EMP_DATA.getCode());
		}
		this.messages = ResourceBundle.getBundle("com.code.resources.messages", new Locale("ar"));
	}

	protected void init() {
	}

	protected void print(byte[] bytes, String filename) {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletResponse resp = (HttpServletResponse) context.getExternalContext().getResponse();
			ServletOutputStream outputStream = resp.getOutputStream();

			resp.setContentType("application/PDF");
			resp.setHeader("Content-Disposition", "attachment; filename=\"" + filename + ".pdf\"");
			resp.setContentLength(bytes.length);

			outputStream.write(bytes);
			outputStream.flush();
			outputStream.close();
			context.responseComplete();
		} catch (Exception e) {
			Log4j.traceErrorException(BaseBacking.class, e, "BaseBacking");
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	protected HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}

	protected HttpSession getSession() {
		return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession();
	}

	protected Application getApplication() {
		return FacesContext.getCurrentInstance().getApplication();
	}

	protected boolean isArabic() {
		return getParameterizedMessage("dir").equals("rtl");
	}

	public void refreshSubmit() {
	}

	public void refreshSubmit(AjaxBehaviorEvent event) {
	}

	@SuppressWarnings("rawtypes")
	protected String getCompleteURL(HttpServletRequest req) {
		String url = req.getRequestURI();
		Enumeration paramNames = req.getParameterNames();
		if (paramNames != null && paramNames.hasMoreElements()) {
			url += "?";
			String paramSeparator = "";
			do {
				String paramName = (String) paramNames.nextElement();
				url += paramSeparator + paramName + "=" + req.getParameter(paramName);
				paramSeparator = "&";
			} while (paramNames.hasMoreElements());
		}
		return url;
	}

	public void setScreenTitle(String screenTitle) {
		this.screenTitle = screenTitle;
	}

	public String getScreenTitle() {
		return screenTitle;
	}
	
	public void setServerSideWarnMessages(String serverSideWarnMessages) {
		setNotifyMessage(FacesMessage.SEVERITY_WARN, "", serverSideWarnMessages);
	}

	public void setServerSideSuccessMessages(String serverSideSuccessMessages) {
		setNotifyMessage(FacesMessage.SEVERITY_INFO, "", serverSideSuccessMessages);
	}

	public void setServerSideErrorMessages(String serverSideErrorMessages) {
		setNotifyMessage(FacesMessage.SEVERITY_ERROR, "", serverSideErrorMessages);
	}

	public void setNotifyMessage(Severity severity, String messageHeader, String messageDetail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, messageHeader, messageDetail));
	}

	public String getParameterizedMessage(String key, Object... params) {
		String value = messages.getString(key);
		return params == null ? value : MessageFormat.format(value, params);
	}

	public void changeLocale(String lang) {
		Locale locale = new Locale(lang);
		this.messages = ResourceBundle.getBundle("com.code.resources.messages", locale);

		getSession().setAttribute(SessionAttributesEnum.CURRENT_LANG.getCode(), lang);
	}

	public EmployeeData getLoginEmpData() {
		return loginEmpData;
	}

	public void setLoginEmpData(EmployeeData loginEmpData) {
		this.loginEmpData = loginEmpData;
	}

}