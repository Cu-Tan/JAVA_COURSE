package com.example.student_registry.ui.tables;

import com.example.student_registry.logic.DB;
import com.example.student_registry.logic.Group;
import com.example.student_registry.logic.UIUpdatable;
import com.example.student_registry.logic.UIUpdateType;
import com.example.student_registry.ui.group.GroupEdit;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GroupTable extends ScrollPane implements UIUpdatable {

  public GroupTable(){

    groupPane.setHgap(16);
    groupPane.setVgap(16);

    this.setContent(groupPane);

    draw();
  }

  @Override
  public void update(UIUpdateType _updateType){

    if(_updateType == UIUpdateType.GROUP || _updateType == UIUpdateType.ALL){
      draw();
    }

  }

  private final GridPane groupPane = new GridPane();

  /**
   * Adds a row of nodes to grid pane from start col
   * @param _gp - grid pane to add nodes
   * @param _row - current grid pane row
   * @param _startCol - the column to start from
   * @param _nodes - nodes to add to grid pane
   * @return next row number
   */
  private int addNodesToPane(GridPane _gp, int _row, int _startCol, Node[] _nodes){
    for(int i = 0; i < _nodes.length; ++i){
      _gp.add(_nodes[i], _startCol + i, _row);
      GridPane.setHalignment(_nodes[i], HPos.CENTER);
      GridPane.setValignment(_nodes[i], VPos.CENTER);
    }
    return _row + 1;
  }
  private void draw(){

    groupPane.getChildren().clear();

    ArrayList<Group> groups = DB.getInstance().groups;

    if(groups.isEmpty()){
      addNodesToPane(groupPane, 0, 0, new Node[]{new Text("NO DATA")});
      return;
    }

    Label groupNameLabel = new Label("Group name");

    int row = 0;
    row = addNodesToPane(groupPane, row, 0, new Node[]{groupNameLabel});



    for(Group group: groups){

      Text groupNameText = new Text(group.name);
      Button groupEditButton = new Button("Edit");

      groupEditButton.setOnAction( e -> {
        Stage editGroupWindow = new Stage();
        Scene editGroupScene = new Scene(new GroupEdit(group), 1280, 720);
        editGroupWindow.setScene(editGroupScene);
        editGroupWindow.show();
      });

      row = addNodesToPane(groupPane, row, 0, new Node[]{groupNameText, groupEditButton});
    }

  }
}
