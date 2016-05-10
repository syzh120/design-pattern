package com.mamba.mvc;

public class MVCPatternDemo {
	public static void main(String[] args) {
		Student model = retriveStudentFromDatabase();
		StudentView view = new StudentView();
		StudentController controller = new StudentController(model, view);
		controller.updateView();

		controller.setStudentName("Lily");
		controller.updateView();
	}

	private static Student retriveStudentFromDatabase() {
		Student student = new Student();
		student.setName("Tony");
		student.setRollNo("10");
		return student;
	}

}
