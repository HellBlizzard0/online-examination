package com.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.entities.Student;
import com.models.StudentModel;

@SessionScoped
@ManagedBean(name = "adminViewStudentsBean")
public class AdminViewStudents {
	List<Student> students;
	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
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

	public List<Student> getBackupExams() {
		return backUpStudents;
	}

	public void setBackupExams(List<Student> backUpStudents) {
		this.backUpStudents = backUpStudents;
	}

	private boolean normal = true;
	private boolean edit = false;
	private boolean addNew = false;
	private List<Student> backUpStudents;
	
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
		students.add(new Student(0,"","","","","",0));
		toggleFlag(Flags.NEW);
	}
	
	public void addNewConfirm(boolean save) {
		if (save) {
			try {
				if (!StudentModel.create(students.get(students.size() - 1)))
					throw new Exception("addNewConfirm has an Exception!");
			} catch (Exception e) {
				System.out.println(e);
			} finally {

			}
		} else {
			students.remove(students.size() - 1);
		}
		toggleFlag(Flags.NORMAL);
	}
	
	public void editEnable() {
		this.backUpStudents = this.students;
		toggleFlag(Flags.EDIT);
	}
	
	public void editConfirm(boolean update) {
		if (update) {
			try {
				for (Student student : students) {
					if (!StudentModel.update(student))
						throw new Exception("editConfirm() has an Error!");
				}
			} catch (Exception e) {
				System.out.println(e);
			} finally {
				this.backUpStudents = null;

			}

		} else {
			this.students = this.backUpStudents;
			this.backUpStudents = null;
		}
		toggleFlag(Flags.NORMAL);
	}

	public boolean render(Student student) {
		if (student.getUsername().equals("") && student.getDepartment().equals("")) {
			return this.addNew;
		} else {
			return this.edit;
		}
	}
	
	public String deleteStudent(Student student) {
		students.remove(student);
		StudentModel.delete(student);
		return "faces-redirect=true";
	}
	
	public String goTo() {
		this.students = StudentModel.getStudentList();
		
		return "AdminViewStudents";
	}
}
