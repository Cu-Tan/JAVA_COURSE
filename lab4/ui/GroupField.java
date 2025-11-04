package com.example.student_registry.ui;

import com.example.student_registry.logic.DB;
import com.example.student_registry.logic.Group;
import com.example.student_registry.logic.UIUpdatable;
import com.example.student_registry.logic.UIUpdateType;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

import java.util.ArrayList;

public class GroupField extends ComboBox<Group> implements UIUpdatable {

  public GroupField(ArrayList<Group> _groups){

    this.setValue(null);

    setGroups(_groups);

    this.setConverter(new StringConverter<>() {
      @Override
      public String toString(Group _group) {
        return (_group == null) ? "" : _group.name;
      }

      @Override
      public Group fromString(String string) {
        return null;
      }
    });
    this.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
      @Override
      protected void updateItem(Group _group, boolean _empty) {
        super.updateItem(_group, _empty);
        setText(_empty || _group == null ? null : _group.name);
      }
    });

  }

  @Override
  public void update(UIUpdateType _updateType) {

    if(_updateType != UIUpdateType.GROUP){
      return;
    }

    setGroups(DB.getInstance().groups);

  }

  private void setGroups(ArrayList<Group> _groups){
    if(!_groups.isEmpty()){
      this.getItems().setAll(_groups.toArray(new Group[0]));
      this.setPromptText("");
      this.setDisable(false);
    }
    else {
      this.setPromptText("No groups available");
      this.setDisable(true);
    }
  }
}
