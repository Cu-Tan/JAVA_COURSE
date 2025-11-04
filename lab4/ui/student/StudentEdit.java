package com.example.student_registry.ui.student;

import com.example.student_registry.logic.DB;
import com.example.student_registry.logic.Group;
import com.example.student_registry.logic.Student;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class StudentEdit extends StudentInfo {

  public StudentEdit(Student _student){

    nameField.setText(_student.name);
    surnameField.setText(_student.surname);
    groupField.setValue(DB.getInstance().getGroupByID(_student.groupID));

    Button editButton = new Button("Edit student");
    editButton.setOnAction( e -> {

      Group group = groupField.getValue();

      if(nameField.getText().isBlank()){ return; }
      if(surnameField.getText().isBlank()){ return; }

      Student student = new Student(
          nameField.getText(),
          surnameField.getText(),
          (group == null) ? null : group.ID
      );

      DB.getInstance().editStudent(_student, student);

    });
    this.add(editButton, 0, ROW_COUNT);
    GridPane.setHalignment(editButton, HPos.CENTER);
    GridPane.setValignment(editButton, VPos.CENTER);
    GridPane.setColumnSpan(editButton, 2);

  }

}
