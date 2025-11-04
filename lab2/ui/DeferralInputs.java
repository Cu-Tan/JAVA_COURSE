package com.example.paskolos_skaiciuokle.ui;

import com.example.paskolos_skaiciuokle.logic.DeferralData;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.function.Consumer;

public class DeferralInputs extends GridPane {

  public DeferralInputs(){

    this.setVgap(2);
    this.setHgap(8);

    Label deferralStartLabel = new Label("Deferral start month:");
    deferralStartField = new TextField("");
    deferralStartErrorLabel = new Label("");
    deferralStartErrorLabel.setTextFill(Color.RED);

    Label deferralDurationLabel = new Label("Deferral duration:");
    deferralDurationField = new TextField("");
    deferralDurationErrorLabel = new Label("");
    deferralDurationErrorLabel.setTextFill(Color.RED);

    Label deferralInterestLabel = new Label("Deferral interest (%):");
    deferralInterestField = new TextField("");
    deferralInterestErrorLabel = new Label("");
    deferralInterestErrorLabel.setTextFill(Color.RED);

    Button deferButton = new Button("Defer");
    deferButton.setOnAction( e -> {
      if(inputValidation()){
        onDefer.accept(new DeferralData(
            Integer.parseInt(deferralStartField.getText()),
            Integer.parseInt(deferralDurationField.getText()),
            Float.parseFloat(deferralInterestField.getText())
        ));
      }
    });

    Button clearButton = new Button("Clear deferral");
    clearButton.setOnAction( e -> {
      onClear.run();
    });

    addRow(deferralStartLabel, deferralStartField, deferralStartErrorLabel);
    addRow(deferralDurationLabel, deferralDurationField, deferralDurationErrorLabel);
    addRow(deferralInterestLabel, deferralInterestField, deferralInterestErrorLabel);

    this.add(deferButton, 0, ROW_COUNT);
    this.add(clearButton, 1, ROW_COUNT);
  }

  public void setOnDefer(Consumer<DeferralData> _onDefer){
    onDefer = _onDefer;
  }
  public void setOnClear(Runnable _onClear) { onClear = _onClear; }

  public void setDeferralStartError(String _errorText){
    deferralStartErrorLabel.setText(_errorText);
    deferralStartErrorLabel.setVisible(true);
  }


  private Consumer<DeferralData> onDefer;
  private Runnable onClear;

  private TextField deferralStartField;
  private TextField deferralDurationField;
  private TextField deferralInterestField;

  private Label deferralStartErrorLabel;
  private Label deferralDurationErrorLabel;
  private Label deferralInterestErrorLabel;

  private int ROW_COUNT = 0;

  private void addRow(Node label, Node content, Node error){
    this.add(label, 0, ROW_COUNT);
    this.add(content, 1, ROW_COUNT);
    this.add(error, 2, ROW_COUNT);
    ++ROW_COUNT;
  }

  private boolean inputValidation(){

    boolean isValid = true;

    if(!inputValidationDeferralStart()){
      isValid = false;
    }
    if(!inputValidationDeferralDuration()){
      isValid = false;
    }
    if(!inputValidationDeferralInterest()){
      isValid = false;
    }

    return isValid;
  }

  private boolean inputValidationDeferralStart(){

    if(deferralStartField.getText().isBlank()){
      deferralStartErrorLabel.setText("Field can't be empty");
      deferralStartErrorLabel.setVisible(true);
      return false;
    }

    try {
      int deferralStart = Integer.parseInt(deferralStartField.getText());

      if(deferralStart <= 0){
        deferralStartErrorLabel.setText("Input must be above 0");
        deferralStartErrorLabel.setVisible(true);
        return false;
      }

    }
    catch (Exception e){
      deferralStartErrorLabel.setText("Input must be an integer");
      deferralStartErrorLabel.setVisible(true);
      return false;
    }

    deferralStartErrorLabel.setVisible(false);
    return true;
  }
  private boolean inputValidationDeferralDuration(){

    if(deferralDurationField.getText().isBlank()){
      deferralDurationErrorLabel.setText("Field can't be empty");
      deferralDurationErrorLabel.setVisible(true);
      return false;
    }

    try {
      int deferralDuration = Integer.parseInt(deferralDurationField.getText());

      if(deferralDuration <= 0){
        deferralDurationErrorLabel.setText("Input must be above 0");
        deferralDurationErrorLabel.setVisible(true);
        return false;
      }

    }
    catch (Exception e){
      deferralDurationErrorLabel.setText("Input must be an integer");
      deferralDurationErrorLabel.setVisible(true);
      return false;
    }

    deferralDurationErrorLabel.setVisible(false);
    return true;
  }
  private boolean inputValidationDeferralInterest(){

    if(deferralInterestField.getText().isBlank()){
      deferralInterestErrorLabel.setText("Field can't be empty");
      deferralInterestErrorLabel.setVisible(true);
      return false;
    }

    try {
      float deferralInterest = Float.parseFloat(deferralInterestField.getText());

      if(deferralInterest < 0){
        deferralInterestErrorLabel.setText("Input must be above or equal 0");
        deferralInterestErrorLabel.setVisible(true);
        return false;
      }

    }
    catch (Exception e){
      deferralInterestErrorLabel.setText("Input must be a number");
      deferralInterestErrorLabel.setVisible(true);
      return false;
    }

    deferralInterestErrorLabel.setVisible(false);
    return true;
  }
}
