package com.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.entities.Exam;
import com.entities.Question;
import com.models.ExamModel;

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
	
	public void newEnable() {
		exams.add(new Exam(0,0,"",""));
		toggleFlag(Flags.NEW);
	}
	
	public void addNewConfirm(boolean save) {
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
	}
	
	public void editEnable() {
		this.backupExams = this.exams;
		toggleFlag(Flags.EDIT);
	}
	
	public void editConfirm(boolean update) {
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
	}

	public boolean render(Exam exam) {
		if (exam.getTitle().equals("") && exam.getDepartment().equals("")) {
			return this.addNew;
		} else {
			return this.edit;
		}
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

	

}
