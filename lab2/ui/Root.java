package com.example.paskolos_skaiciuokle.ui;

import com.example.paskolos_skaiciuokle.logic.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.function.Consumer;

public class Root extends Application {

  private Loan loan = null;

  @Override
  public void start(Stage stage) throws IOException {

    LoanGrid loanGrid = new LoanGrid();
    LoanChart loanChart = new LoanChart();
    DeferralInputs deferralInputPane = new DeferralInputs();
    FilterInputs filterInputsPane = new FilterInputs();

    // region button callbacks
    Consumer<LoanInputData> onCalculate = loanData -> {
      switch (loanData.loanType){
        case LINEAR -> {
          loan = new LoanLinear(
              loanData.loanAmount,
              loanData.loanDuration,
              loanData.loanInterest
          );
        }
        case ANNUITY -> {
          loan = new LoanAnnuity(
              loanData.loanAmount,
              loanData.loanDuration,
              loanData.loanInterest
          );
        }
      }
      loan.calculate();
      loanGrid.setLoanMonthData(loan.getMonthData(), loan.getStartMonth());
      loanChart.setLoanMonthData(loan.getMonthData(), loan.getStartMonth());
    };
    Consumer<DeferralData> onDeferral = deferralData -> {

      if(loan == null){
        return;
      }

      // Check if deferral start is out of bounds. If so do nothing and set error
      if(deferralData.deferralStart > loan.getBaseDuration()){
        deferralInputPane.setDeferralStartError("Out of bounds (must be between 1 and " + loan.getBaseDuration() + ")");
        return;
      }

      loan.setDeferral(deferralData);
      loanGrid.setLoanMonthData(loan.getMonthData(), loan.getStartMonth());
      loanChart.setLoanMonthData(loan.getMonthData(), loan.getStartMonth());
    };
    Runnable onDeferralClear = () -> {

      if(loan == null){
        return;
      }

      loan.setDeferral(null);
      loanGrid.setLoanMonthData(loan.getMonthData(), loan.getStartMonth());
      loanChart.setLoanMonthData(loan.getMonthData(), loan.getStartMonth());

    };
    Consumer<FilterData> onFilter = _filterData -> {

      if(loan == null){
        return;
      }

      if(_filterData.start > _filterData.end){
        filterInputsPane.setStartError("Can't be above \"End Month\"");
        filterInputsPane.setEndError("Can't be below \"Start Month\"");
        return;
      }

      boolean outOfBounds = false;
      if(_filterData.start > loan.getDuration()){
        filterInputsPane.setStartError("Out of bounds (Must be between 1 and " + loan.getDuration() + ")");
        outOfBounds = true;
      }
      if(_filterData.end > loan.getDuration()){
        filterInputsPane.setEndError("Out of bounds (Must be between 1 and " + loan.getDuration() + ")");
        outOfBounds = true;
      }
      if(outOfBounds){
        return;
      }

      loan.setFilter(_filterData);
      loanGrid.setLoanMonthData(loan.getMonthData(), loan.getStartMonth());
      loanChart.setLoanMonthData(loan.getMonthData(), loan.getStartMonth());
    };
    Runnable onFilterClear = () -> {

      if(loan == null){
        return;
      }

      loan.setFilter(null);
      loanGrid.setLoanMonthData(loan.getMonthData(), loan.getStartMonth());
      loanChart.setLoanMonthData(loan.getMonthData(), loan.getStartMonth());

    };
    // endregion
    // region set button callbacks
    deferralInputPane.setOnDefer(onDeferral);
    deferralInputPane.setOnClear(onDeferralClear);

    filterInputsPane.setOnFilter(onFilter);
    filterInputsPane.setOnClear(onFilterClear);
    // endregion

    LoanInputs loanInputs = new LoanInputs(onCalculate);

    Button saveToFileButton = new Button("Save to file");
    saveToFileButton.setOnAction( e -> {

      if(loan == null){
        return;
      }

      LoanSaver.save(loan, "loan.txt");

    });

    // region layout
    HBox dataBox = new HBox(loanGrid, loanChart);
    VBox root = new VBox(loanInputs, deferralInputPane, filterInputsPane, saveToFileButton ,dataBox);
    ScrollPane rootScroll = new ScrollPane();
    rootScroll.setContent(root);
    loanGrid.setMaxHeight(400);
    // endregion

    Scene scene = new Scene(rootScroll, 1280, 720);

    loanGrid.prefWidthProperty().bind(scene.widthProperty().divide(2));
    loanChart.prefWidthProperty().bind(scene.widthProperty().divide(2));

    stage.setTitle("Loan calculator");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}
