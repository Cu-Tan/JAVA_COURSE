package com.example.paskolos_skaiciuokle.ui;

import com.example.paskolos_skaiciuokle.logic.FilterData;
import com.example.paskolos_skaiciuokle.logic.Loan;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.ArrayList;

/**
 * Display loan data in a grid
 */
public class LoanGrid extends ScrollPane {

  GridPane grid;

  public LoanGrid(){

    grid = new GridPane();

    grid.setHgap(32);
    grid.setHgap(16);

    this.setContent(grid);

    grid.add(new Text("NO DATA"), 0 , 0);
  }

  public void setLoanMonthData(Loan.MonthData[] _monthData, int _startMonth){

    if(_monthData == null){
      return;
    }

    drawGrid(_startMonth, _monthData);

  }

  private void drawGrid(int _startMonth, Loan.MonthData[] _loanMonthData){

    grid.getChildren().clear();

    Text monthLabel = new Text("Month");
    Text loanRemainderLabel = new Text("Loan remainder");
    Text loanPaymentLabel = new Text("Monthly payment");
    Text loanInterestLabel = new Text("Interest");
    Text loanCreditLabel = new Text("Credit");

    grid.add(monthLabel, 0, 0);
    grid.add(loanRemainderLabel, 1, 0);
    grid.add(loanPaymentLabel, 2, 0);
    grid.add(loanInterestLabel, 3, 0);
    grid.add(loanCreditLabel, 4, 0);

    centerNode(monthLabel);
    centerNode(loanRemainderLabel);
    centerNode(loanPaymentLabel);
    centerNode(loanInterestLabel);
    centerNode(loanCreditLabel);

    double totalLoanPayment = 0;
    double totalLoanInterest = 0;

    int month = _startMonth;

    for(int i = 0; i < _loanMonthData.length; ++i){

      totalLoanPayment += _loanMonthData[i].loanPayment;
      totalLoanInterest += _loanMonthData[i].loanInterest;

      Text monthText = new Text(Integer.toString(month));
      Text loanRemainderText = new Text(String.format("%.2f", _loanMonthData[i].loanRemainder));
      Text loanPaymentText = new Text(String.format("%.2f", _loanMonthData[i].loanPayment));
      Text loanInterestText = new Text(String.format("%.2f", _loanMonthData[i].loanInterest));
      Text loanCreditText = new Text(String.format("%.2f", _loanMonthData[i].loanCredit));

      grid.add(monthText, 0, i + 1);
      grid.add(loanRemainderText, 1, i + 1);
      grid.add(loanPaymentText, 2, i + 1);
      grid.add(loanInterestText, 3, i + 1);
      grid.add(loanCreditText, 4, i + 1);

      centerNode(monthText);
      centerNode(loanRemainderText);
      centerNode(loanPaymentText);
      centerNode(loanInterestText);
      centerNode(loanCreditText);

      ++month;
    }

    Text totalText = new Text("Total:");
    Text totalLoanPaymentText = new Text(String.format("%.2f", totalLoanPayment));
    Text totalLoanInterestText = new Text(String.format("%.2f", totalLoanInterest));

    Text spacer = new Text("Spacer");
    spacer.setVisible(false);
    grid.add(spacer, 0, _loanMonthData.length + 1);

    grid.add(totalText, 0, _loanMonthData.length + 2);
    grid.add(totalLoanPaymentText, 2, _loanMonthData.length + 2);
    grid.add(totalLoanInterestText, 3, _loanMonthData.length + 2);

    centerNode(totalText);
    centerNode(totalLoanPaymentText);
    centerNode(totalLoanInterestText);
  }

  /**
   * Used to make a box inside LoanGrid centered
   */
  private void centerNode(Node node){
    GridPane.setHalignment(node, HPos.CENTER);
    GridPane.setValignment(node, VPos.CENTER);
  }


}
