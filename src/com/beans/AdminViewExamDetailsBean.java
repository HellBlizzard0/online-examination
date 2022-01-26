package com.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.entities.Exam;
import com.entities.Question;
import com.models.QuestionModel;

@SessionScoped
@ManagedBean(name = "viewExamDetailsBean")
public class AdminViewExamDetailsBean {
	private Exam exam;
	private List<Question> questions;

	private boolean displayQuestionDetails = false;
	
	private boolean normal = true;
	private boolean edit = false;
	private boolean addNew = false;
	private List<Question> backupQuestions;
	
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
		questions.add(new Question(0,"",0,exam.getId()));
		toggleFlag(Flags.NEW);
		return "faces-redirect=true";
}
	
	public String addNewConfirm(boolean save) {
		if (save) {
			try {
				if (!QuestionModel.create(questions.get(questions.size() - 1)))
					throw new Exception("addNewConfirm has an Exception!");
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e);
			} finally {

			}
		} else {
			questions.remove(questions.size() - 1);
		}
		toggleFlag(Flags.NORMAL);
		return "faces-redirect=true";
}
	
	public String editEnable() {
		this.backupQuestions = this.questions;
		toggleFlag(Flags.EDIT);
		return "faces-redirect=true";
}
	
	public String editConfirm(boolean update) {
		if (update) {
			try {
				for (Question question : questions) {
					if (!QuestionModel.update(question))
						throw new Exception("editConfirm() has an Error!");
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e);
			} finally {
				this.backupQuestions = null;

			}

		} else {
			this.questions = this.backupQuestions;
			this.backupQuestions = null;
		}
		toggleFlag(Flags.NORMAL);
		return "faces-redirect=true";
}

	public boolean render(Question question) {
		if (question.getText().equals("")) {
			return this.addNew;
		} else {
			return this.edit;
		}
	}
	
	public boolean isNormal() {
		return normal;
	}

	public void setNormal(boolean normal) {
		this.normal = normal;
	}

	public boolean isAddNew() {
		return addNew;
	}

	public void setAddNew(boolean addNew) {
		this.addNew = addNew;
	}

	public String deleteQuestion(Question question) {
		questions.remove(question);
		QuestionModel.delete(question);
		return "faces-redirect=true";
	}

	public void toggleEditQuestion(Question question) {
		if (!this.edit) {
			this.edit = true;
		} else {
			QuestionModel.update(question);
			this.edit = false;
		}

	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public boolean isDisplayQuestionDetails() {
		return displayQuestionDetails;
	}

	public void setDisplayQuestionDetails(boolean displayQuestionDetails) {
		this.displayQuestionDetails = displayQuestionDetails;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public String goTo(Exam exam) {
		this.exam = exam;
		this.questions = QuestionModel.getQuestionList(exam);

		return "AdminViewExamDetails";
	}

}
