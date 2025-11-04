package com.example.student_registry.ui.tables;

import com.example.student_registry.logic.Student;
import com.example.student_registry.logic.UIUpdateType;
import com.example.student_registry.ui.student.StudentEdit;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class StudentManagementTable extends StudentTable {

  public StudentManagementTable(){
    drawEditButtonsPane();
  }

  @Override
  public void update(UIUpdateType _updateType) {
    if(_updateType == UIUpdateType.STUDENT || _updateType == UIUpdateType.GROUP || _updateType == UIUpdateType.ALL){
      updateStudentsPane();
      drawEditButtonsPane();
    }
  }

  private void drawEditButtonsPane(){

    // Start from 1 because 0 is for labels
    int ROW_COUNT = 1;

    for(Student student: students){

      Button editButton = new Button("Edit student");
      editButton.setOnAction( e -> {
        Stage editStudentWindow = new Stage();
        Scene editStudentScene = new Scene(new StudentEdit(student), 1280, 720);
        editStudentWindow.setScene(editStudentScene);
        editStudentWindow.show();
      });

      ROW_COUNT = addNodesToPane(studentPane, ROW_COUNT, 3 ,new Node[]{editButton});
    }
  }

}
