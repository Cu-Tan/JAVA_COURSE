package com.example.student_registry.ui.group;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class GroupInfo extends GridPane {

  public GroupInfo(){

    this.setVgap(8);
    this.setHgap(16);

    Label groupNameLabel = new Label("Name:");

    addRow(groupNameLabel, groupNameField);

  }

  protected int ROW_COUNT = 0;
  protected final TextField groupNameField = new TextField();

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
