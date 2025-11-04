package com.example.student_registry.ui.tables;

import com.example.student_registry.logic.DB;
import com.example.student_registry.logic.Group;
import com.example.student_registry.logic.Student;
import com.example.student_registry.logic.UIUpdatable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class StudentTable extends Pane implements UIUpdatable {

  public StudentTable(){

    studentPane.setHgap(16);
    studentPane.setVgap(16);

    updateStudentsPane();

    scrollPane.setContent(studentPane);

    scrollPane.setMaxWidth(400);

    vLayout.getChildren().add(scrollPane);

    this.getChildren().add(vLayout);
  }

  public void setGroupFilter(Group _group){
    group = _group;
  }

  protected Group group;
  protected final GridPane studentPane = new GridPane();
  protected final ArrayList<Student> students = new ArrayList<>();
  protected final VBox vLayout = new VBox();
  protected ScrollPane scrollPane = new ScrollPane();


  protected void updateStudentsPane(){
    filterStudents();
    drawStudentPane();
  }
  /**
   * Adds a row of nodes to grid pane from start col
   * @param _gp - grid pane to add nodes
   * @param _row - current grid pane row
   * @param _startCol - the column to start from
   * @param _nodes - nodes to add to grid pane
   * @return next row number
   */
  protected int addNodesToPane(GridPane _gp, int _row, int _startCol, Node[] _nodes){
    for(int i = 0; i < _nodes.length; ++i){
      _gp.add(_nodes[i], _startCol + i, _row);
      GridPane.setHalignment(_nodes[i], HPos.CENTER);
      GridPane.setValignment(_nodes[i], VPos.CENTER);
    }
    return _row + 1;
  }

  private void drawStudentPane(){

    studentPane.getChildren().clear();

    if(students.isEmpty()){
      addNodesToPane(studentPane, 0, 0, new Node[]{ new Text("NO DATA") });
      return;
    }

    int ROW_COUNT = 0;

    Label nameLabel = new Label("Name");
    Label surnameLabel = new Label("Surname");
    Label groupLabel = new Label("Group");

    ROW_COUNT = addNodesToPane(studentPane, ROW_COUNT, 0, new Node[]{nameLabel, surnameLabel, groupLabel});

    DB db = DB.getInstance();

    for(int i = 0; i < students.size(); ++i){

      Text nameText = new Text(students.get(i).name);
      Text surnameText = new Text(students.get(i).surname);

      Group group = db.getGroupByID(students.get(i).groupID);
      String groupName = (group == null) ? "NO GROUP" : group.name;
      Text groupText = new Text(groupName);

      ROW_COUNT = addNodesToPane(studentPane, ROW_COUNT, 0, new Node[]{nameText, surnameText, groupText});
    }

  }

  private void filterStudents(){

    DB db = DB.getInstance();

    students.clear();

    if(group == null){
      students.addAll(db.students);
    }
    else {
      students.addAll(Arrays.asList(db.getStudentsByGroupID(group.ID)));
    }

  }

}
