package com.example.student_registry.ui.student;

import com.example.student_registry.logic.DB;
import com.example.student_registry.logic.Group;
import com.example.student_registry.ui.GroupField;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class StudentInfo extends GridPane {

  public StudentInfo(){

    this.setVgap(8);
    this.setHgap(16);

    Label nameLabel = new Label("Name:");
    Label surnameLabel = new Label("Surname:");
    Label groupLabel = new Label("Group:");

    groupField = new GroupField(DB.getInstance().groups);
    groupField.widthProperty().addListener((obs, old, bounds) -> {
          groupField.setPrefWidth(nameField.getWidth());
        }
    );

    addRow(nameLabel, nameField);
    addRow(surnameLabel, surnameField);
    addRow(groupLabel, groupField);
  }

  protected final TextField nameField = new TextField("");
  protected final TextField surnameField = new TextField("");
  protected final GroupField groupField;

  protected int ROW_COUNT = 0;
  private void addRow(Node a, Node b){
    this.add(a, 0, ROW_COUNT);
    this.add(b, 1, ROW_COUNT);

    GridPane.setHalignment(a, HPos.LEFT);
    GridPane.setValignment(a, VPos.CENTER);
    GridPane.setHalignment(b, HPos.LEFT);
    GridPane.setValignment(b, VPos.CENTER);

    ++ROW_COUNT;
  }

}
