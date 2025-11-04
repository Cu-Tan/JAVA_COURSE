package com.example.student_registry.ui;

import com.example.student_registry.logic.*;
import com.example.student_registry.ui.group.GroupCreate;
import com.example.student_registry.ui.student.StudentCreate;
import com.example.student_registry.ui.tables.GroupTable;
import com.example.student_registry.ui.tables.StudentAttendanceTable;
import com.example.student_registry.ui.tables.StudentManagementTable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminPane extends Pane {

  public AdminPane(){

    VBox layout = new VBox();
    layout.setSpacing(16);

    Button createStudentButton = new Button("Create student");
    createStudentButton.setOnAction( e -> {
      Stage newStudentWindow = new Stage();
      Scene newStudentScene = new Scene(new StudentCreate(), 1280, 720);
      newStudentWindow.setScene(newStudentScene);
      newStudentWindow.show();
    });

    StudentManagementTable studentManagementTable = new StudentManagementTable();
    UIUpdater.getInstance().subscribe(studentManagementTable);

    Button createGroupButton = new Button("Create group");
    createGroupButton.setOnAction( e -> {
      Stage newGroupWindow = new Stage();
      Scene newGroupScene = new Scene(new GroupCreate(), 1280, 720);
      newGroupWindow.setScene(newGroupScene);
      newGroupWindow.show();
    });

    GroupTable groupTable = new GroupTable();
    UIUpdater.getInstance().subscribe(groupTable);

    StudentAttendanceTable studentAttendanceTable = new StudentAttendanceTable();
    UIUpdater.getInstance().subscribe(studentAttendanceTable);

    Button saveData = new Button("Export data");
    saveData.setOnAction( e -> {
      DataSaver.save("data.csv");
    });

    Button loadData = new Button("Import data");
    loadData.setOnAction( e -> {
      DataLoader.load("data.csv");
    });

    HBox dataSaverLayout = new HBox(saveData, loadData);

    layout.getChildren().addAll(dataSaverLayout, createStudentButton, studentManagementTable, createGroupButton, groupTable, studentAttendanceTable);

    ScrollPane scrollPane = new ScrollPane(layout);
    this.getChildren().add(scrollPane);

  }

}
