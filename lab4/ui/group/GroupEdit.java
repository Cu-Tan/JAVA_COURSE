package com.example.student_registry.ui.group;

import com.example.student_registry.logic.DB;
import com.example.student_registry.logic.Group;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class GroupEdit extends GroupInfo {

  public GroupEdit(Group _group){

    groupNameField.setText(_group.name);

    Button editGroup = new Button("Edit group");

    editGroup.setOnAction( e -> {

      if(groupNameField.getText().isBlank()){
        return;
      }

      DB.getInstance().editGroup(
        _group,
        new Group(
          groupNameField.getText()
        )
      );

      groupNameField.setText("");

    });

    this.add(editGroup, 0, ROW_COUNT);
    GridPane.setHalignment(editGroup, HPos.CENTER);
    GridPane.setValignment(editGroup, VPos.CENTER);
    GridPane.setColumnSpan(editGroup, 2);

  }
}
