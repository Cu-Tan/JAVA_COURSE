package com.example.lab7;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab7.logic.Loan;
import com.example.lab7.logic.LoanAnnuity;
import com.example.lab7.logic.LoanLinear;
import com.example.lab7.ui.DeferralInputFields;
import com.example.lab7.ui.FilterInputFields;
import com.example.lab7.ui.Graph;
import com.example.lab7.ui.LoanInputFields;
import com.example.lab7.ui.Table;

/**
 * @author Karolis Ribačonka Informatika 2 kursas 2 grupė 2 pogrupis
 */

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_main);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    table = new Table(this);
    graph = new Graph(this);

    LoanInputFields.init(
      this,
      loanInputData -> {

        switch (loanInputData.loanType){

          case LINEAR:
            loan = new LoanLinear(
              loanInputData.loanAmount,
              loanInputData.loanDuration,
              loanInputData.loanInterest
            );

          break;

          case ANNUITY:
            loan = new LoanAnnuity(
              loanInputData.loanAmount,
              loanInputData.loanDuration,
              loanInputData.loanInterest
            );
            loan.calculate();
            table.update(loan.getMonthData(), loan.getStartMonth());
          break;

        }

        loan.calculate();
        table.update(loan.getMonthData(), loan.getStartMonth());
        graph.update(loan.getMonthData(), loan.getStartMonth());

      }
    );

    DeferralInputFields.init(
      this,
      deferralData -> {

        if(loan == null){
          return;
        }

        loan.setDeferral(deferralData);
        table.update(loan.getMonthData(), loan.getStartMonth());
        graph.update(loan.getMonthData(), loan.getStartMonth());

      },
      () -> {

        if(loan == null){
          return;
        }

        loan.setDeferral(null);
        table.update(loan.getMonthData(), loan.getStartMonth());
        graph.update(loan.getMonthData(), loan.getStartMonth());

      }
    );

    FilterInputFields.init(
      this,
      filterData -> {

        if(loan == null){
          return;
        }

        loan.setFilter(filterData);
        table.update(loan.getMonthData(), loan.getStartMonth());
        graph.update(loan.getMonthData(), loan.getStartMonth());

      },
      () -> {

        if(loan == null){
          return;
        }

        loan.setFilter(null);
        table.update(loan.getMonthData(), loan.getStartMonth());
        graph.update(loan.getMonthData(), loan.getStartMonth());

      }

    );

  }

  private Table table = null;
  private Graph graph = null;
  private Loan loan = null;
}