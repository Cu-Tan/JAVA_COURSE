package com.example.lab7.ui;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.lab7.R;
import com.example.lab7.logic.Loan;

public class Table {

  public Table (
    Activity _activity
  ) {
    activity = _activity;
    tableLayout = activity.findViewById(R.id.table);
  }

  public void update(
    Loan.MonthData[] _mothData,
    int startMonth
  ) {

    clear();

    double totalLoanPayment = 0;
    double totalLoanInterest = 0;

    for(int i = 0; i < _mothData.length; ++i){

      addRow(
        Integer.toString(i + startMonth),
        String.format("%.2f", _mothData[i].loanRemainder),
        String.format("%.2f", _mothData[i].loanPayment),
        String.format("%.2f", _mothData[i].loanInterest),
        String.format("%.2f", _mothData[i].loanCredit)
      );

      totalLoanPayment += _mothData[i].loanPayment;
      totalLoanInterest += _mothData[i].loanInterest;

    }

    addRow(
      "Total:",
      "",
      String.format("%.2f", totalLoanPayment),
      String.format("%.2f", totalLoanInterest),
      ""
    );

    Log.d("my_tag", "table updated " + _mothData.length);

  }

  private final Activity activity;
  private final TableLayout tableLayout;

  private void addRow(
    String month,
    String _loanRemainder,
    String _loanPayment,
    String _loanInterest,
    String _loanCredit
  ) {

    TableRow row = new TableRow(activity);
    row.setGravity(Gravity.CENTER);

    TextView col1 = new TextView(activity);
    col1.setText(month);
    col1.setPadding(4, 4, 4, 4);
    col1.setGravity(Gravity.CENTER);

    TextView col2 = new TextView(activity);
    col2.setText(_loanRemainder);
    col2.setPadding(4, 4, 4, 4);
    col2.setGravity(Gravity.CENTER);

    TextView col3 = new TextView(activity);
    col3.setText(_loanPayment);
    col3.setPadding(4, 4, 4, 4);
    col3.setGravity(Gravity.CENTER);

    TextView col4 = new TextView(activity);
    col4.setText(_loanInterest);
    col4.setPadding(4, 4, 4, 4);
    col4.setGravity(Gravity.CENTER);

    TextView col5 = new TextView(activity);
    col5.setText(_loanCredit);
    col5.setPadding(4, 4, 4, 4);
    col5.setGravity(Gravity.CENTER);

    row.addView(col1);
    row.addView(col2);
    row.addView(col3);
    row.addView(col4);
    row.addView(col5);

    tableLayout.addView(row);

  }

  private void clear(){

    // Start = 1 to avoid removing headers
    tableLayout.removeViews(1, tableLayout.getChildCount() - 1);

  }

}
