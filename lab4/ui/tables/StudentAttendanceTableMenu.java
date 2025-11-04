package com.example.student_registry.ui.tables;

import com.example.student_registry.logic.Group;
import com.example.student_registry.logic.UIUpdater;
import com.example.student_registry.ui.GroupSelector;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class StudentAttendanceTableMenu extends GridPane {

  public StudentAttendanceTableMenu(
      BiConsumer<LocalDate, LocalDate> _onSetDate,
      Consumer<Boolean> onEditMode,
      Consumer<Group> onGroupFilter,
      Runnable onExport
  ){

    Label startDateLabel = new Label("Start date:");
    Label endDateLabel = new Label("End date:");

    Button setDateButton = new Button("Set date");

    int row = 0;
    row = addNodesToPane(this, row, 0, new Node[]{startDateLabel, startDateField});
    row = addNodesToPane(this, row,0, new Node[]{endDateLabel, endDateField});
    row = addNodesToPane(this, row,0, new Node[]{setDateButton});
    GridPane.setColumnSpan(setDateButton, 2);

    setDateButton.setOnAction( e -> {
      _onSetDate.accept(startDateField.getValue(), endDateField.getValue());
    });

    Label editModeLabel = new Label("Edit:");
    CheckBox editModeCheckBox = new CheckBox();

    editModeCheckBox.setOnAction( e -> {
      onEditMode.accept(editModeCheckBox.isSelected());
    });

    addNodesToPane(this, 0, 2, new Node[]{editModeLabel, editModeCheckBox});

    // Add group filter
    GroupSelector groupSelector = new GroupSelector(onGroupFilter);
    addNodesToPane(this, 1, 2, new Node[]{groupSelector});

    Button exportButton = new Button("Export to PDF");
    exportButton.setOnAction( e -> onExport.run() );
    addNodesToPane(this, 2, 2, new Node[]{exportButton});

  }

  private final DatePicker startDateField = new DatePicker();
  private final DatePicker endDateField = new DatePicker();

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

}
