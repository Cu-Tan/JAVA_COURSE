package com.example.paskolos_skaiciuokle.ui;

import com.example.paskolos_skaiciuokle.logic.FilterData;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.function.Consumer;

/**
 * UI for user filter inputs
 */
public class FilterInputs extends GridPane {

  public FilterInputs(){

    this.setVgap(2);
    this.setHgap(8);

    Label startLabel = new Label("Start month:");
    startField = new TextField("");
    startErrorLabel = new Label("");
    startErrorLabel.setTextFill(Color.RED);
    startErrorLabel.setVisible(false);

    Label endLabel = new Label("End month:");
    endField = new TextField("");
    endErrorLabel = new Label("");
    endErrorLabel.setTextFill(Color.RED);
    endErrorLabel.setVisible(false);

    Button onFilterButton = new Button("Filter");
    onFilterButton.setOnAction( e -> {

      if(!inputValidation()){
        return;
      }

      onFilter.accept(new FilterData(
          Integer.parseInt(startField.getText()),
          Integer.parseInt(endField.getText())
      ));

    });

    Button clearButton = new Button("Clear Filter");
    clearButton.setOnAction( e -> {
      onClear.run();
    });

    addRow(startLabel, startField, startErrorLabel);
    addRow(endLabel, endField, endErrorLabel);

    this.add(onFilterButton, 0, ROW_COUNT);
    this.add(clearButton, 1, ROW_COUNT);
  }

  public void setOnFilter(Consumer<FilterData> _onFilter){
    onFilter = _onFilter;
  }
  public void setOnClear(Runnable _onClear) { onClear = _onClear; }
  public void setStartError(String _errorText){
    startErrorLabel.setText(_errorText);
    startErrorLabel.setVisible(true);
  }
  public void setEndError(String _errorText){
    endErrorLabel.setText(_errorText);
    endErrorLabel.setVisible(true);
  }

  private Consumer<FilterData> onFilter; // Callback when user confirms filter input
  private Runnable onClear;

  private TextField startField;
  private TextField endField;

  private Label startErrorLabel;
  private Label endErrorLabel;

  private int ROW_COUNT = 0;

  private void addRow(Node label, Node content, Node error){
    this.add(label, 0, ROW_COUNT);
    this.add(content, 1, ROW_COUNT);
    this.add(error, 2, ROW_COUNT);
    ++ROW_COUNT;
  }

  private boolean inputValidation(){
    boolean isValid = true;

    if(!inputValidationStart()){
      isValid = false;
    }
    if(!inputValidationEnd()){
      isValid = false;
    }

    return isValid;
  }
  /**
   * validates start field
   */
  private boolean inputValidationStart(){

    if(startField.getText().isBlank()){
      startErrorLabel.setText("Field can't be empty");
      startErrorLabel.setVisible(true);
      return false;
    }

    try {

      int start = Integer.parseInt(startField.getText());

      if(start <= 0){
        startErrorLabel.setText("Must be above 0");
        startErrorLabel.setVisible(true);
        return false;
      }

    }
    catch (Exception e){
      startErrorLabel.setText("Must be an integer");
      startErrorLabel.setVisible(true);
      return false;
    }

    startErrorLabel.setVisible(false);
    return true;
  }
  /**
   * validates end field
   */
  private boolean inputValidationEnd(){
    if(endField.getText().isBlank()){
      endErrorLabel.setText("Field can't be empty");
      endErrorLabel.setVisible(true);
      return false;
    }

    try {

      int start = Integer.parseInt(endField.getText());

      if(start <= 0){
        endErrorLabel.setText("Must be above 0");
        endErrorLabel.setVisible(true);
        return false;
      }

    }
    catch (Exception e){
      endErrorLabel.setText("Must be an integer");
      endErrorLabel.setVisible(true);
      return false;
    }

    endErrorLabel.setVisible(false);
    return true;
  }

}
