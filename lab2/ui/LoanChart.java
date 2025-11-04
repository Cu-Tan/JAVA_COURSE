package com.example.paskolos_skaiciuokle.ui;

import com.example.paskolos_skaiciuokle.logic.Loan;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;

/**
 * Displays monthly loan interest in a chart
 */
public class LoanChart extends Pane {

  public LoanChart(){
    NumberAxis xLabel = new NumberAxis();
    xLabel.setLabel("Months");
    xLabel.setLowerBound(1);
    xLabel.setTickUnit(1);

    NumberAxis yLabel = new NumberAxis();
    yLabel.setLabel("Interest");

    chart = new LineChart<>(xLabel, yLabel);
    chart.setLegendVisible(false);

    this.getChildren().add(chart);
  }

  public void setLoanMonthData(Loan.MonthData[] _loanMonthData, int _startMonth){

    if(_loanMonthData == null){
      return;
    }

    populateChart(_startMonth, _loanMonthData);
  }

  private final LineChart<Number, Number> chart;

  private void populateChart(int _startMonth, Loan.MonthData[] _loanMonthData){

    if(_loanMonthData == null){
      return;
    }

    chart.getData().clear();

    XYChart.Series<Number, Number> chartData = new XYChart.Series<>();

    int month = _startMonth;

    for(Loan.MonthData monthData: _loanMonthData){
      chartData.getData().add(new XYChart.Data<>(month, monthData.loanInterest));
      ++month;
    }

    chart.getData().add(chartData);
  }

}
