package com.code.ui.backings.setup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.info.Classification;
import com.code.dal.orm.info.ClassificationType;
import com.code.exceptions.BusinessException;
import com.code.services.setup.ClassificationService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "classifications")
@ViewScoped
public class Classifications extends BaseBacking implements Serializable {
	private ClassificationType classificationTypeSearch;
	private ClassificationType classificationTypeInsert;
	private Integer classificationTypeSelectedIndex;
	private List<ClassificationType> classificationTypesList;

	private Classification classificationInsert;
	private Integer classificationSelectedIndex;
	private List<Classification> classificationsList;

	private ClassificationType selectedClassificationType;

	private boolean classificationTypeExpandFlag;
	private boolean classificationExpandFlag;

	/**
	 * Constructor
	 */
	public Classifications() {
		super();
		init();
	}

	/**
	 * initialize bean variables
	 */
	public void init() {
		classificationTypeSearch = new ClassificationType();
		classificationTypeInsert = new ClassificationType();
		classificationTypesList = new ArrayList<ClassificationType>();
		classificationInsert = new Classification();
		classificationsList = new ArrayList<Classification>();
		selectedClassificationType = null;
		classificationExpandFlag = false;
		classificationTypeExpandFlag = false;
		classificationTypeSelectedIndex = null;
		classificationSelectedIndex = null;
	}

	/**
	 * Initialize/ Reset Variables
	 */
	public void resetSearchParams() {
		classificationTypeSearch = new ClassificationType();
		classificationTypeSelectedIndex = null;
		classificationSelectedIndex = null;
		resetClassificationInsertForm();
		resetClassificationTypeInsertForm();
	}

	/**
	 * Reset Classification Type Insert Form
	 */
	public void resetClassificationInsertForm() {
		classificationInsert = new Classification();
		classificationSelectedIndex = null;
		classificationExpandFlag = false;
	}
	
	/**
	 * Reset Classification List
	 */
	public void resetClassificationData(){
		classificationsList = new ArrayList<Classification>();
		selectedClassificationType = null;
	}

	/**
	 * Reset Classification Insert Form
	 */
	public void resetClassificationTypeInsertForm() {
		classificationTypeInsert = new ClassificationType();
		classificationTypeSelectedIndex = null;
		classificationTypeExpandFlag = false;
	}

	/**
	 * Search Classification Types
	 */
	public void searchClassificationTypes() {
		classificationsList.clear();
		classificationTypesList.clear();
		selectedClassificationType = null;
		try {
			classificationTypesList = ClassificationService.getClassificationTypes(null, classificationTypeSearch.getDescription().trim());
			if (classificationTypesList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
			}
			resetClassificationInsertForm();
			resetClassificationTypeInsertForm();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * View Classification Types Details
	 * 
	 * @param classificationType
	 */
	public void viewClassificationTypeDetails(ClassificationType classificationType) {
		try {
			selectedClassificationType = classificationType;
			classificationsList = ClassificationService.getClassifications(selectedClassificationType.getId(), null);
			if (classificationsList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
			}
			resetClassificationInsertForm();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Edit classification Types
	 * 
	 * @param classificationType
	 */
	public void editClassificationType(ClassificationType classificationType, Integer index) {
		classificationTypeExpandFlag = true;
		classificationTypeInsert = new ClassificationType(classificationType);
		classificationTypeSelectedIndex = index;
		resetClassificationInsertForm();
		resetClassificationData();
	}

	/**
	 * Insert new classification Type
	 */
	public void insertClassificationType() {
		saveUpdateClassificationType(classificationTypeInsert);
		classificationTypeInsert = new ClassificationType();
		classificationTypeExpandFlag = true;
		resetClassificationInsertForm();
		resetClassificationData();
	}

	/**
	 * Save / update classification Type
	 * 
	 * @param classificationType
	 */
	public void saveUpdateClassificationType(ClassificationType classificationType) {
		try {
			if (classificationType.getId() == null) {
				ClassificationService.saveClassificationType(classificationType, this.loginEmpData);
				classificationTypesList.add(classificationType);
			} else {
				ClassificationService.updateClassificationType(classificationType, this.loginEmpData);
				classificationTypesList.set(classificationTypeSelectedIndex, classificationType);
			}
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			classificationType.setId(null);
		}
	}
	
	/**
	 * Delete classificationType
	 * @param classificationType
	 */
	public void deleteClassificationType(ClassificationType classificationType) {
		try {
			ClassificationService.deleteClassificationType(classificationType, loginEmpData);
			classificationTypesList.remove(classificationType);
			resetClassificationTypeInsertForm();
			resetClassificationInsertForm();
			resetClassificationData();
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Edit classification
	 * 
	 * @param classification
	 */
	public void editClassification(Classification classification, Integer index) {
		classificationExpandFlag = true;
		classificationInsert = new Classification(classification);
		classificationSelectedIndex = index;
	}

	/**
	 * Insert new classification
	 */
	public void insertClassification() {
		saveUpdateClassification(classificationInsert);
		classificationInsert = new Classification();
		classificationExpandFlag = true;
	}

	/**
	 * Save / update Classification
	 * 
	 * @param classification
	 */
	public void saveUpdateClassification(Classification classification) {
		try {
			if (classification.getId() == null) {
				classification.setClassificationTypeId(selectedClassificationType.getId());
				ClassificationService.saveClassification(classification, this.loginEmpData);
				classificationsList.add(classification);
			} else {
				ClassificationService.updateClassification(classification, this.loginEmpData);
				classificationsList.set(classificationSelectedIndex, classification);
			}
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			classification.setId(null);
		}
	}
	
	/**
	 * Delete classification
	 * @param classification
	 */
	public void deleteClassification(Classification classification) {
		try {
			ClassificationService.deleteClassification(classification, loginEmpData);
			classificationsList.remove(classification);
			resetClassificationInsertForm();
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	// Setters and getters
	public ClassificationType getClassificationTypeSearch() {
		return classificationTypeSearch;
	}

	public void setClassificationTypeSearch(ClassificationType classificationTypeSearch) {
		this.classificationTypeSearch = classificationTypeSearch;
	}

	public ClassificationType getClassificationTypeInsert() {
		return classificationTypeInsert;
	}

	public void setClassificationTypeInsert(ClassificationType classificationTypeInsert) {
		this.classificationTypeInsert = classificationTypeInsert;
	}

	public List<ClassificationType> getClassificationTypesList() {
		return classificationTypesList;
	}

	public void setClassificationTypesList(List<ClassificationType> classificationTypesList) {
		this.classificationTypesList = classificationTypesList;
	}

	public Classification getClassificationInsert() {
		return classificationInsert;
	}

	public void setClassificationInsert(Classification classificationInsert) {
		this.classificationInsert = classificationInsert;
	}

	public List<Classification> getClassificationsList() {
		return classificationsList;
	}

	public void setClassificationsList(List<Classification> classificationsList) {
		this.classificationsList = classificationsList;
	}

	public ClassificationType getSelectedClassificationType() {
		return selectedClassificationType;
	}

	public void setSelectedClassificationType(ClassificationType selectedClassificationType) {
		this.selectedClassificationType = selectedClassificationType;
	}

	public boolean isClassificationExpandFlag() {
		return classificationExpandFlag;
	}

	public void setClassificationExpandFlag(boolean classificationCollapsFlag) {
		this.classificationExpandFlag = classificationCollapsFlag;
	}

	public boolean isClassificationTypeExpandFlag() {
		return classificationTypeExpandFlag;
	}

	public void setClassificationTypeExpandFlag(boolean classificationTypeCollapsFlag) {
		this.classificationTypeExpandFlag = classificationTypeCollapsFlag;
	}
}
