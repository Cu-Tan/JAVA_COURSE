package com.example.student_registry.ui.group;

import com.example.student_registry.logic.DB;
import com.example.student_registry.logic.Group;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class GroupCreate extends GroupInfo {

  public GroupCreate(){

    Button createGroup = new Button("Create group");

    createGroup.setOnAction( e -> {

      if(groupNameField.getText().isBlank()){
        return;
      }

      DB.getInstance().addGroup( new Group(
          groupNameField.getText()
      ));

      groupNameField.setText("");

    });

    this.add(createGroup, 0, ROW_COUNT);
    GridPane.setHalignment(createGroup, HPos.CENTER);
    GridPane.setValignment(createGroup, VPos.CENTER);
    GridPane.setColumnSpan(createGroup, 2);

  }

}
