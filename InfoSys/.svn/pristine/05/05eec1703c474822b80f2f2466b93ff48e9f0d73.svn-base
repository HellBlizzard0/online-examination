package com.code.ui.backings.setup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.setup.InfoSysConfig;
import com.code.exceptions.BusinessException;
import com.code.services.config.InfoSysConfigurationService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "configurations")
@ViewScoped
public class Configuration extends BaseBacking implements Serializable {
	private List<InfoSysConfig> infoSysConfigs = new ArrayList<InfoSysConfig>();
	private Integer infoSysConfigListSize;
	private String code;
	private String value;
	private String comment;

	/**
	 * Constructor
	 */
	public Configuration() {
		super();
	}

	/**
	 * Search configuration
	 */
	public void search() {
		try {
			infoSysConfigs = InfoSysConfigurationService.getConfiguration(code, value, comment);
			if (infoSysConfigs.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noConfigs"));
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Save edited configuration
	 * @param eresConfig
	 */
	public void saveConfig(InfoSysConfig eresConfig) {
		try {
			if (eresConfig.getId() == null) {
				InfoSysConfigurationService.insertConfiguration(eresConfig);
			} else {
				InfoSysConfigurationService.updateConfiguration(eresConfig);
			}
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<InfoSysConfig> getInfoSysConfigs() {
		return infoSysConfigs;
	}

	public void setInfoSysConfigs(List<InfoSysConfig> infoSysConfigs) {
		this.infoSysConfigs = infoSysConfigs;
	}

	public Integer getInfoSysConfigListSize() {
		return infoSysConfigListSize;
	}

	public void setInfoSysConfigListSize(Integer infoSysConfigListSize) {
		this.infoSysConfigListSize = infoSysConfigListSize;
	}
}
