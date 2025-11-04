package com.example.student_registry.ui;

import com.example.student_registry.logic.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.function.Consumer;

public class GroupSelector extends GridPane {

  public GroupSelector(Consumer<Group> onSelect) {

    Label groupLabel = new Label("Group:");

    GroupField groupField = new GroupField(DB.getInstance().groups);
    UIUpdater.getInstance().subscribe(groupField);

    Button clearGroup = new Button("Clear");

    groupField.setOnAction( e -> {
      onSelect.accept(groupField.getValue());
    });

    clearGroup.setOnAction( e -> {
      if(!groupField.isDisabled()){
        groupField.setValue(null);
        onSelect.accept(null);
      }
    });

    this.add(groupLabel, 0, 0);
    this.add(groupField, 1, 0);
    this.add(clearGroup, 2, 0);

  }

}
