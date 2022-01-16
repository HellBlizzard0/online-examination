package com.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.entities.Answer;
import com.entities.Question;
import com.models.AnswerModel;

@SessionScoped
@ManagedBean(name = "answersByQuestion")
public class AdminViewAnswersOfQuestion {

	private Question question;
	private List<Answer> answers;
	private List<Answer> backupAnswers;

	public List<Answer> getBackupAnswers() {
		return backupAnswers;
	}

	public void setBackupAnswers(List<Answer> backupAnswers) {
		this.backupAnswers = backupAnswers;
	}

	private boolean normal = true;
	private boolean edit = false;
	private boolean addNew = false;

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

	public void editEnable() {
		this.backupAnswers = this.answers;
		toggleFlag(Flags.EDIT);
	}

	/**
	 * Should switch the isCorrect flag of an answer. 
	 * @param answer
	 */
	public void toggleCorrect(Answer answer) {
		answers
				.get(answers.indexOf(answer))
				.setCorrect(
						!answers
								.get(answers.indexOf(answer))
								.isCorrect());
	}

	public void newEnable() {
		answers.add(new Answer(0, "", this.question.getId(), false));
		toggleFlag(Flags.NEW);
	}

	public void editConfirm(boolean update) {
		if (update) {
			try {
				for (Answer answer : answers) {
					if (!AnswerModel.update(answer))
						throw new Exception("editConfirm() has an Error!");
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e);
			} finally {
				this.backupAnswers = null;

			}

		} else {
			this.answers = this.backupAnswers;
			this.backupAnswers = null;
		}
		toggleFlag(Flags.NORMAL);
	}

	public void addNewConfirm(boolean save) {
		if (save) {
			try {
				if (!AnswerModel.create(answers.get(answers.size() - 1)))
					throw new Exception("addNewConfirm has an Exception!");
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e);
			} finally {

			}
		} else {
			answers.remove(answers.size() - 1);
		}
		toggleFlag(Flags.NORMAL);
	}

	public boolean render(Answer answer) {
		if (answer.getText().equals("")) {
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

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public String displayAnswers(Question question) {

		this.answers = AnswerModel.getAnswerByQuestionId(question);
		this.question = question;
		return "AdminViewAnswersOfQuestion";
	}

	public void deleteAnswer(Answer answer) {
		answers.remove(answer);
		AnswerModel.delete(answer);
	}
}
