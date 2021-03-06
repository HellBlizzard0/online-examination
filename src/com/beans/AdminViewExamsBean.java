package com.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.entities.Departments;
import com.entities.Exam;
import com.entities.Question;
import com.models.ExamModel;
import com.models.ResultModel;

@SessionScoped
@ManagedBean(name = "viewExamsBean")
public class AdminViewExamsBean {

	private List<Exam> exams;
	private ArrayList<Exam> selectedExam = new ArrayList<Exam>();
	private Question question;
	
	private boolean normal = true;
	private boolean edit = false;
	private boolean addNew = false;
	private List<Exam> backupExams;
	private String[] departments= {"ICS","SWE","COE","MATH","ISE"};
//	private List<Departments> department;
	
	private enum Flags {
		NORMAL,
		EDIT,
		NEW
	}

	private void toggleFlag(Flags flagName) {
		switch (flagName) {
		case NORMAL:
			this.setNormal(true);
			this.setAddNew(false);
			this.setEdit(false);
			break;
		case EDIT:
			this.setNormal(false);
			this.setAddNew(false);
			this.setEdit(true);
			break;
		case NEW:
			this.setNormal(false);
			this.setAddNew(true);
			this.setEdit(false);
			break;

		default:
			break;
		}
	}
	
	public String newEnable() {
		exams.add(new Exam(0,0,"",""));
		toggleFlag(Flags.NEW);
		return "faces-redirect=true";
	}
	
	public String addNewConfirm(boolean save) {
		if (save) {
			try {
				if (!ExamModel.create(exams.get(exams.size() - 1)))
					throw new Exception("addNewConfirm has an Exception!");
			} catch (Exception e) {
				System.out.println(e);
			} finally {

			}
		} else {
			exams.remove(exams.size() - 1);
		}
		toggleFlag(Flags.NORMAL);
		return "faces-redirect=true";

	}
	
	public String editEnable() {
		this.backupExams = this.exams;
		toggleFlag(Flags.EDIT);
		return "faces-redirect=true";
	}
	
	public String editConfirm(boolean update) {
		if (update) {
			try {
				for (Exam exam : exams) {
					if (!ExamModel.update(exam))
						throw new Exception("editConfirm() has an Error!");
				}
			} catch (Exception e) {
				System.out.println(e);
			} finally {
				this.backupExams = null;

			}

		} else {
			this.exams = this.backupExams;
			this.backupExams = null;
		}
		toggleFlag(Flags.NORMAL);
		return "faces-redirect=true";

	}

	public boolean render(Exam exam) {
		if (exam.getTitle().equals("") && exam.getDepartment().equals("")) {
			return this.addNew;
		} else {
			return this.edit;
		}
	}
	
	public String deleteExam(Exam exam) {
		exams.remove(exam);
		ExamModel.delete(exam);
		return "faces-redirect=true";
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public List<Exam> getSelectedExam() {
		return selectedExam;
	}

	public void setSelectedExam(ArrayList<Exam> selectedExam) {
		this.selectedExam = selectedExam;
	}

	public List<Exam> getExams() {
		return exams;
	}

	public void setexams(List<Exam> exams) {
		this.exams = exams;
	}

	public String fetchExams() {
		this.exams = ExamModel.getExamsList(null, -1);
	//	getDepartmentss();
		return "AdminViewExams";
	}

	public boolean isNormal() {
		return normal;
	}

	public void setNormal(boolean normal) {
		this.normal = normal;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public boolean isAddNew() {
		return addNew;
	}

	public void setAddNew(boolean addNew) {
		this.addNew = addNew;
	}

	public void setExams(List<Exam> exams) {
		this.exams = exams;
	}

	public String[] getDepartments() {
		return departments;
	}

	public void setDepartments(String[] departments) {
		this.departments = departments;
	}

/*	public List<Departments> getDepartment() {
		return department;
	}

	public void setDepartment(List<Departments> department) {
		this.department = department;
	}

	public void  getDepartmentss() {
		//getStudent().setId(4);
		 department= ResultModel.getDepartments();
	}
*/
}
