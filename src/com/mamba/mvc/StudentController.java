package com.mamba.mvc;

public class StudentController {
	private Student stuModel;
	private StudentView stuView;

	public StudentController(Student stuModel, StudentView stuView) {
		this.stuModel = stuModel;
		this.stuView = stuView;
	}

	public void setStudentName(String name) {
		stuModel.setName(name);
	}

	public String getStudentName() {
		return stuModel.getName();
	}

	public void setStudentRollNo(String rollNo) {
		stuModel.setRollNo(rollNo);
	}

	public String getStudentRollNo() {
		return stuModel.getRollNo();
	}

	public void updateView() {
		stuView.printStudentDetails(stuModel.getName(), stuModel.getRollNo());
	}

}
