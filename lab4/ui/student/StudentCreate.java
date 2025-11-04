package com.example.student_registry.ui.student;

import com.example.student_registry.logic.DB;
import com.example.student_registry.logic.Group;
import com.example.student_registry.logic.Student;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class StudentCreate extends StudentInfo {

  public StudentCreate(){

    Button addButton = new Button("Add student");
    addButton.setOnAction( e -> {

      Group group = groupField.getValue();

      if(nameField.getText().isBlank()){ return; }
      if(surnameField.getText().isBlank()){ return; }

      Student student = new Student(
          nameField.getText(),
          surnameField.getText(),
          (group == null) ? null : group.ID
      );

      DB.getInstance().addStudent(student);

      nameField.setText("");
      surnameField.setText("");
      groupField.setValue(null);
    });
    this.add(addButton, 0, ROW_COUNT);
    GridPane.setHalignment(addButton, HPos.CENTER);
    GridPane.setValignment(addButton, VPos.CENTER);
    GridPane.setColumnSpan(addButton, 2);
  }



}
