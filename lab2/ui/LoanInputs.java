package com.example.paskolos_skaiciuokle.ui;

import com.example.paskolos_skaiciuokle.logic.LoanInputData;
import com.example.paskolos_skaiciuokle.logic.LoanType;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.function.Consumer;


/**
 * UI for user to input base loan data
 */
public class LoanInputs extends GridPane {

  public LoanInputs(Consumer<LoanInputData> _onCalculate){

    this.setVgap(2);
    this.setHgap(8);

    onCalculate = _onCalculate;

    Label loanAmountLabel = new Label("Loan amount:");
    loanAmountField = new TextField("");
    loanAmountErrorLabel = new Label("");
    loanAmountErrorLabel.setVisible(false);
    loanAmountErrorLabel.setTextFill(Color.RED);

    Label loanDeadlineLabel = new Label("Loan duration: ");
    loanDurationField = new TextField("");
    loanDurationErrorLabel = new Label("");
    loanDurationErrorLabel.setVisible(false);
    loanDurationErrorLabel.setTextFill(Color.RED);

    Label loanTypeLabel = new Label("Loan type: ");
    loanTypeField = new ComboBox<>();
    loanTypeField.setValue(LoanType.LINEAR);
    loanTypeField.getItems().addAll(LoanType.values());

    Label loanInterestLabel = new Label("Interest (%) :");
    loanInterestField = new TextField("");
    loanInterestErrorLabel = new Label("");
    loanInterestErrorLabel.setVisible(false);
    loanInterestErrorLabel.setTextFill(Color.RED);

    addRow(loanAmountLabel, loanAmountField, loanAmountErrorLabel);
    addRow(loanDeadlineLabel, loanDurationField, loanDurationErrorLabel);
    addRow(loanTypeLabel, loanTypeField, null);
    addRow(loanInterestLabel, loanInterestField, loanInterestErrorLabel);

    Button confirmButton = new Button("Calculate");

    confirmButton.setOnAction( e -> {
      if(inputValidation()){
        onCalculate.accept(new LoanInputData(
            Double.parseDouble(loanAmountField.getText()),
            Integer.parseInt(loanDurationField.getText()),
            loanTypeField.getValue(),
            Double.parseDouble(loanInterestField.getText())
        ));
      }
    });

    this.add(confirmButton, 0, ROW_COUNT);
  }

  public class Data{

    public Data(
      String _loanAmount,
      String _loanDeadline,
      LoanType _loanType,
      String _loanInterest
    ){
      this.loanAmount = _loanAmount;
      this.loanDeadline = _loanDeadline;
      this.loanType = _loanType;
      this.loanInterest = _loanInterest;
    }

    public String loanAmount;
    public String loanDeadline;
    public LoanType loanType;
    public String loanInterest;
  }

  public Data getData(){

    return new Data(
        loanAmountField.getText(),
        loanDurationField.getText(),
        loanTypeField.getValue(),
        loanInterestField.getText()
    );
  }

  private Consumer<LoanInputData> onCalculate; // callback when user confirms inputs

  private TextField loanAmountField;
  private TextField loanDurationField;
  private ComboBox<LoanType> loanTypeField;
  private TextField loanInterestField;

  private Label loanAmountErrorLabel;
  private Label loanDurationErrorLabel;
  private Label loanInterestErrorLabel;

  private int ROW_COUNT = 0;

  private void addRow(Node _label, Node _field, Node _error){
    this.add(_label, 0, ROW_COUNT);
    this.add(_field, 1, ROW_COUNT);
    if(_error != null){
      this.add(_error, 2, ROW_COUNT);
    }
    ++ROW_COUNT;
  }

  private boolean inputValidation(){

    boolean isValid = true;

    if(!inputValidationLoanAmount()){
      isValid = false;
    }
    if(!inputValidationLoanDuration()){
      isValid = false;
    }
    if(!inputValidationLoanInterest()){
      isValid = false;
    }

    return isValid;
  }
  private boolean inputValidationLoanAmount(){

    if(loanAmountField.getText().isBlank()){
      loanAmountErrorLabel.setText("Field can't be empty");
      loanAmountErrorLabel.setVisible(true);
      return false;
    }

    if(!loanAmountField.getText().matches("^(\\d+)?([.,]\\d{0,2})?$")){
      loanAmountErrorLabel.setText("Invalid format (egz. 123.45)");
      loanAmountErrorLabel.setVisible(true);
      return false;
    }

    // If no errors
    loanAmountErrorLabel.setVisible(false);
    return true;
  }
  private boolean inputValidationLoanDuration(){

    if(loanDurationField.getText().isBlank()){
      loanDurationErrorLabel.setText("Field can't be empty");
      loanDurationErrorLabel.setVisible(true);
      return false;
    }

    try {

      int loanDuration = Integer.parseInt(loanDurationField.getText());

      if(loanDuration <= 0){
        loanDurationErrorLabel.setText("Must be a positive integer");
        loanDurationErrorLabel.setVisible(true);
        return false;
      }

    }
    catch (Exception e){
      loanDurationErrorLabel.setText("Must be an integer");
      loanDurationErrorLabel.setVisible(true);
      return false;
    }

    // If no errors
    loanDurationErrorLabel.setVisible(false);
    return true;
  }
  private boolean inputValidationLoanInterest(){

    if(loanInterestField.getText().isBlank()){
      loanInterestErrorLabel.setText("Field can't be empty");
      loanInterestErrorLabel.setVisible(true);
      return false;
    }

    try {

      float loanInterest = Float.parseFloat(loanInterestField.getText());

      if(loanInterest < 0){
        loanInterestErrorLabel.setText("Input must be above or equal 0");
        loanInterestErrorLabel.setVisible(true);
        return false;
      }

    }
    catch (Exception e){
      loanInterestErrorLabel.setText("Input must be a number");
      loanInterestErrorLabel.setVisible(true);
      return false;
    }

    // If no errors
    loanInterestErrorLabel.setVisible(false);
    return true;
  }

}
