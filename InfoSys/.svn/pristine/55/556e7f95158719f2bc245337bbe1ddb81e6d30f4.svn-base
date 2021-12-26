package com.code.ui.backings.setup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.info.InfoSubject;
import com.code.dal.orm.info.InfoType;
import com.code.enums.FlagsEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.NoDataException;
import com.code.services.infosys.info.InfoTypeService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "infoTypes")
@ViewScoped
public class InfoTypes extends BaseBacking implements Serializable {
	private InfoType infoTypeSearch;
	private InfoType infoTypeInsert;
	private Integer infoTypeSelectedIndex;
	private List<InfoType> infoTypesList;

	private InfoSubject infoSubjectInsert;
	private Integer infoSubjectSelectedIndex;
	private List<InfoSubject> infoSubjectsList;
	private InfoType selectedInfoType;
	private String selectedInfoTypeDesc;

	private boolean infoTypeExpandFlag;
	private boolean infoSubjectExpandFlag;

	/**
	 * Constructor
	 */
	public InfoTypes() {
		super();
		init();
	}

	/**
	 * initialize bean variables
	 */
	public void init() {
		infoTypeSearch = new InfoType();
		infoTypeInsert = new InfoType();
		infoTypeSelectedIndex = null;
		infoTypesList = new ArrayList<InfoType>();

		infoSubjectInsert = new InfoSubject();
		infoSubjectSelectedIndex = null;
		infoSubjectsList = new ArrayList<InfoSubject>();

		selectedInfoType = null;
		infoSubjectExpandFlag = false;
		infoTypeExpandFlag = false;
	}

	/**
	 * Initialize/ Reset Variables
	 */
	public void resetSearchParams() {
		infoTypeSearch = new InfoType();
	}

	/**
	 * Reset info subject Insert Form
	 */
	public void resetInfoSubjectInsertForm() {
		infoSubjectInsert = new InfoSubject();
		infoSubjectSelectedIndex = null;
		infoSubjectExpandFlag = false;
	}

	/**
	 * Reset Info Subject List and reset the previously selected info type object
	 */
	public void resetInfoSubjectData() {
		infoSubjectsList = new ArrayList<InfoSubject>();
		selectedInfoType = null;
	}

	/**
	 * Reset info type Insert Form
	 */
	public void resetInfoTypeInsertForm() {
		infoTypeInsert = new InfoType();
		infoTypeSelectedIndex = null;
		infoTypeExpandFlag = false;
	}

	/**
	 * Search info Types
	 */
	public void searchInfoTypes() {
		infoSubjectsList.clear();
		infoTypesList.clear();
		selectedInfoType = null;
		try {
			infoTypesList = InfoTypeService.getInfoTypes(infoTypeSearch.getDescription().trim());
			if (infoTypesList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
			}
			resetInfoSubjectInsertForm();
			resetInfoTypeInsertForm();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * View info Types Details
	 * 
	 * @param infoType
	 * @throws NoDataException
	 */
	public void viewInfoTypeDetails(InfoType infoType) throws NoDataException {
		try {
			selectedInfoType = infoType;
			selectedInfoTypeDesc = infoType.getDescription();
			infoSubjectsList = InfoTypeService.getInfoSubjects(FlagsEnum.ALL.getCode(), infoType.getId(), null);
			if (infoSubjectsList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
			}
			resetInfoSubjectInsertForm();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Edit info Types
	 * 
	 * @param index
	 */
	public void editInfoType(Integer index) {
		infoTypeExpandFlag = true;
		try {
			infoTypeInsert = (InfoType) infoTypesList.get(index).clone();
			infoTypeSelectedIndex = index;
			resetInfoSubjectInsertForm();
			resetInfoSubjectData();
		} catch (CloneNotSupportedException e) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_general", null));
		}
	}

	/**
	 * Save / update Info Type
	 */
	public void saveInfoType() {
		try {
			if (infoTypeInsert.getId() == null) {
				InfoTypeService.saveInfoType(infoTypeInsert, this.loginEmpData);
				infoTypesList.add(infoTypeInsert);
			} else {
				InfoTypeService.updateInfoType(infoTypeInsert, this.loginEmpData);
				infoTypesList.set(infoTypeSelectedIndex, infoTypeInsert);
			}
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}

		infoTypeInsert = new InfoType();
		infoTypeExpandFlag = true;
		resetInfoSubjectInsertForm();
		resetInfoSubjectData();
	}

	/**
	 * Deletes the info type if there are no info Subjects related to it
	 * 
	 * @param infoType
	 * @throws BusinessException
	 */
	public void deleteInfoType(InfoType infoType) throws BusinessException {
		try {
			InfoTypeService.deleteInfoType(infoType, this.loginEmpData);
			infoTypesList.remove(infoType);
			resetInfoTypeInsertForm();
			resetInfoSubjectInsertForm();
			resetInfoSubjectData();
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Edit info subject
	 * 
	 * @param index
	 */
	public void editInfoSubject(Integer index) {
		infoSubjectExpandFlag = true;
		try {
			infoSubjectInsert = (InfoSubject) infoSubjectsList.get(index).clone();
			infoSubjectSelectedIndex = index;
		} catch (CloneNotSupportedException e) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_general", null));
		}
	}

	/**
	 * Save / update Info Subject
	 */
	public void saveInfoSubject() {
		try {
			if (infoSubjectInsert.getId() == null) {
				infoSubjectInsert.setInfoTypeId(selectedInfoType.getId());
				InfoTypeService.saveInfoSubject(infoSubjectInsert, this.loginEmpData);
				infoSubjectsList.add(infoSubjectInsert);
			} else {
				InfoTypeService.updateInfoSubject(infoSubjectInsert, this.loginEmpData);
				infoSubjectsList.set(infoSubjectSelectedIndex, infoSubjectInsert);
			}
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
		infoSubjectInsert = new InfoSubject();
		infoSubjectExpandFlag = true;
	}

	/**
	 * Delete info Subject
	 * 
	 * @param infoSubject
	 * @throws BusinessException
	 */
	public void deleteInfoSubject(InfoSubject infoSubject) throws BusinessException {
		try {
			InfoTypeService.deleteInfoSubject(infoSubject, this.loginEmpData);
			infoSubjectsList.remove(infoSubject);
			resetInfoSubjectInsertForm();
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	public InfoType getInfoTypeSearch() {
		return infoTypeSearch;
	}

	public void setInfoTypeSearch(InfoType infoTypeSearch) {
		this.infoTypeSearch = infoTypeSearch;
	}

	public InfoType getInfoTypeInsert() {
		return infoTypeInsert;
	}

	public void setInfoTypeInsert(InfoType infoTypeInsert) {
		this.infoTypeInsert = infoTypeInsert;
	}

	public Integer getInfoTypeSelectedIndex() {
		return infoTypeSelectedIndex;
	}

	public void setInfoTypeSelectedIndex(Integer infoTypeSelectedIndex) {
		this.infoTypeSelectedIndex = infoTypeSelectedIndex;
	}

	public List<InfoType> getInfoTypesList() {
		return infoTypesList;
	}

	public void setInfoTypesList(List<InfoType> infoTypesList) {
		this.infoTypesList = infoTypesList;
	}

	public InfoSubject getInfoSubjectInsert() {
		return infoSubjectInsert;
	}

	public void setInfoSubjectInsert(InfoSubject infoSubjectInsert) {
		this.infoSubjectInsert = infoSubjectInsert;
	}

	public Integer getInfoSubjectSelectedIndex() {
		return infoSubjectSelectedIndex;
	}

	public void setInfoSubjectSelectedIndex(Integer infoSubjectSelectedIndex) {
		this.infoSubjectSelectedIndex = infoSubjectSelectedIndex;
	}

	public List<InfoSubject> getInfoSubjectsList() {
		return infoSubjectsList;
	}

	public void setInfoSubjectsList(List<InfoSubject> infoSubjectsList) {
		this.infoSubjectsList = infoSubjectsList;
	}

	public InfoType getSelectedInfoType() {
		return selectedInfoType;
	}

	public void setSelectedInfoType(InfoType selectedInfoType) {
		this.selectedInfoType = selectedInfoType;
	}

	public boolean getInfoTypeExpandFlag() {
		return infoTypeExpandFlag;
	}

	public void setInfoTypeExpandFlag(boolean infoTypeExpandFlag) {
		this.infoTypeExpandFlag = infoTypeExpandFlag;
	}

	public boolean isInfoSubjectExpandFlag() {
		return infoSubjectExpandFlag;
	}

	public void setInfoSubjectExpandFlag(boolean infoSubjectExpandFlag) {
		this.infoSubjectExpandFlag = infoSubjectExpandFlag;
	}

	public String getSelectedInfoTypeDesc() {
		return selectedInfoTypeDesc;
	}

	public void setSelectedInfoTypeDesc(String selectedInfoTypeDesc) {
		this.selectedInfoTypeDesc = selectedInfoTypeDesc;
	}
}